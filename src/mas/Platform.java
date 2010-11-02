package mas;

import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;

import java.awt.Cursor;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import network.JadeUrlChecker;
import application.Application;
import application.Language;
import application.Project;
import database.DBConnection;

public class Platform extends Object {

	/**
	 * This class manages the jade-platform 
	 * for this local application 
	 */
	public static Runtime MASrt;
	public static AgentContainer MASmc;
	public static Vector<AgentContainer> MAS_ContainerLocal = new Vector<AgentContainer>();
	public static Vector<ContainerID> MAS_ContainerRemote = new Vector<ContainerID>();
	
	public PlatformJadeConfig MASplatformConfig = null;	
	public String MASrunningMode = "";
	
	public final static int UTIL_CMD_OpenDF = 1;
	public final static int UTIL_CMD_ShutdownPlatform = 2;
	public final static int UTIL_CMD_OpenLoadMonitor = 3;
	
	public static final String MASapplicationAgentName = "server.client";
	public JadeUrlChecker MASmasterAddress = null; 

	
	private jadeClasses<Agent> Agents; 
	private jadeClasses<Ontology> Ontologies;
	public Vector<Class<? extends Agent>> AgentsVector   = null;
	public Vector<Class<? extends Ontology>> OntologyVector = null;
	
	private String newLine = Application.RunInfo.AppNewLineString();
	
	public Platform() {
		// --- Search for all Agent-Classes -------------
//		this.jadeFindAgentClasse(); // new Thread !! 
		
		if (Application.isServer==true) {
			MASrunningMode = "Server";
		} else { 
			MASrunningMode = "Application";
		}
	}	
	
	/**
	 * This Method will start - depending on the Configuration - the
	 * programs-background-agents.
	 * It starts directly after starting the JADE-Platform
	 * @return
	 */
	private boolean jadeStartBackgroundAgents() {
		
		// --- Define the Address of the Main-Platform --------------
		MASmasterAddress = new JadeUrlChecker(Application.RunInfo.getServerMasterURL());
		MASmasterAddress.setPort(Application.RunInfo.getServerMasterPort());
		MASmasterAddress.setPort4MTP(Application.RunInfo.getServerMasterPort4MTP());
		
		// ----------------------------------------------------------
		// --- Differentiation of the Application-Case --------------
		// ----------------------------------------------------------
		if (Application.isServer==true) {
			// ------------------------------------------------------
			// --- Running as Agent.GUI - Server --------------------
			// ------------------------------------------------------
			// --- Not yet known: Master- or Slave-Server? ----------
			JadeUrlChecker thisAddress = new JadeUrlChecker(MASmc.getPlatformName());	
			if ( thisAddress.getHostIP().equalsIgnoreCase(MASmasterAddress.getHostIP())  && 
				 thisAddress.getPort().equals(MASmasterAddress.getPort()) ) {
				// -------------------------------------------------
				// --- This is a Master-Server-Platform ------------
				// -------------------------------------------------
				MASrunningMode = "Server [Master]";
				// --- Connecting to Database ----------------------
				Application.DBconnection = new DBConnection();
				if ( Application.DBconnection.hasErrors==true ) {
					
					this.jadeStop();
					
					String msgHead = "";
					String msgText = "";
					
					msgHead += Language.translate("Konfiguration des") + " " + Application.RunInfo.AppTitel() + "-" +  MASrunningMode;
					msgText += "Die Systemkonfiguration enthält keine gültigen Angaben über den" + newLine +
							   "Datenbankserver. Der Start von JADE wird deshalb unterbrochen." + newLine +
							   "Bitte konfigurieren Sie einen MySQL-Datenbank-Server und" + newLine +
							   "starten Sie den Server-Master anschließend erneut." + newLine + newLine +
							   "Möchten Sie die Konfiguration nun vornehmen?";
					msgText = Language.translate(msgText);
					
					int answer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						Application.showOptionDialog();
					} 
					return false;
					
				}
				// --- Starting 'Server.Master'-Agent --------------				
				jadeAgentStart("server.master", mas.service.agents.MasterServerAgent.class.getName());
				
			} else {
				// -------------------------------------------------
				// --- This is a Slave-Server-Platform -------------
				// -------------------------------------------------
				MASrunningMode = "Server [Slave]";
				if (Application.RunInfo.getServerMasterURL()==null ||
					Application.RunInfo.getServerMasterURL().equalsIgnoreCase("")==true ||
					Application.RunInfo.getServerMasterPort().equals(0)==true ||
					Application.RunInfo.getServerMasterPort4MTP().equals(0)==true ) {
					
					this.jadeStop();
					
					String msgHead = "";
					String msgText = "";
					
					msgHead += Language.translate("Konfiguration des") + " " + Application.RunInfo.AppTitel() + "-" +  MASrunningMode;
					msgText += "Die Systemkonfiguration enthält keine gültigen Angaben über den" + newLine +
							   "Hauptserver. Der Start von JADE wird deshalb unterbrochen." + newLine +
							   "Bitte konfigurieren Sie eine gültige Server-URL oder IP (inkl. Port)" + newLine +
							   "und starten Sie den Dienst anschließend erneut." + newLine + newLine +
							   "Möchten Sie die Konfiguration nun vornehmen?";
					msgText = Language.translate(msgText);
					
					int answer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						Application.showOptionDialog();
					} 
					return false;
					
				} 
				// --- Starting 'Server.Slave'-Agent ---------------
				jadeAgentStart("server.slave", mas.service.agents.SlaveServerAgent.class.getName());
					
			}
		} else {
			// ------------------------------------------------------
			// --- Running as Agent.GUI - Application ---------------
			// ------------------------------------------------------
			MASrunningMode = "Application";
			// --- Starting 'Server.Client'-Agent -------------------				
			if (jadeAgentIsRunning(MASapplicationAgentName)==false) {
				jadeAgentStart(MASapplicationAgentName, mas.service.agents.ClientServerAgent.class.getName());	
			}			
			// --- Start RMA ('Remote Monitoring Agent') ------------ 
			jadeSystemAgentOpen( "rma", null );	
						
		}
		return true;
	}
	
	/**
	 * Starting the jade-platform  
	 */		
	public boolean jadeStart() {
		
		boolean startSucceed = false;		
		
		if ( jadeMainContainerIsRunning() == false ) {
			try {
				// --- Start Platform -------------------------------
				MASrt = Runtime.instance();	
				MASrt.invokeOnTermination( new Runnable() {
					public void run() {
						MASmc = null;
						MASrt = null;
						Application.setStatusJadeRunning(false);
					}
				});
				// --- Start MainContainer --------------------------				
				MASmc = MASrt.createMainContainer( this.jadeGetContainerProfile() );
				startSucceed = true;
			}
			catch ( Exception e ) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			System.out.println( "JADE läuft bereits! => " + MASrt );			
		}

		// --- Start the Application Background-Agents ---------------
		if (this.jadeStartBackgroundAgents()==false) return false;
		
		Application.setStatusJadeRunning(true);
		return startSucceed;
		
	}
	
	/**
	 * This method returns the JADE-Profile, which has to be used
	 * for the container-profiles.
	 * If a project is focused the specific project-configuration will
	 * be used. Otherwise the default-configuration of AgentGUI will be
	 * used.
	 * @return Profile (for Jade-Containers)
	 */
	private Profile jadeGetContainerProfile() {

		Profile MAScontainerProfile = null;
		Project currProject = Application.ProjectCurr;
		
		// --- Configure the JADE-Profile to use --------------------
		if (currProject==null) {
			// --- Take the AgentGUI-Default-Profile ----------------
			this.MASplatformConfig = Application.RunInfo.getJadeDefaultPlatformConfig();
			MAScontainerProfile = Application.RunInfo.getJadeDefaultProfile();
			System.out.println("JADE-Profile: Use AgentGUI-defaults");
		} else {
			// --- Take the Profile of the current Project ----------
			if (currProject.JadeConfiguration.isUseDefaults()==true) {
				this.MASplatformConfig = Application.RunInfo.getJadeDefaultPlatformConfig();
				MAScontainerProfile = Application.RunInfo.getJadeDefaultProfile();
				System.out.println("JADE-Profile: Use Agent.GUI-defaults");
			} else {
				this.MASplatformConfig = currProject.JadeConfiguration;
				MAScontainerProfile = currProject.JadeConfiguration.getNewInstanceOfProfilImpl();				
				System.out.println("JADE-Profile: Use " + currProject.getProjectName() + "-configuration" );
			}
		}		
		return MAScontainerProfile;
	}
	
	/**
	 * Shutting down the jade-platform 
	 */
	public void jadeStop() {

		// ------------------------------------------------
		// --- Starts the UtilityAgent which sends --------
		// --- a 'ShutdownPlatform()' to the AMS   --------	
		// ------------------------------------------------
		if (jadeMainContainerIsRunning()) {
			this.jadeUtilityAgentStart(UTIL_CMD_ShutdownPlatform);
			// --- Wait for the end of Jade ---------------
			Long timeStop = System.currentTimeMillis() + (10 * 1000);
			while(jadeMainContainerIsRunning()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
				if (System.currentTimeMillis() >= timeStop) {
					break;
				}
			}
			System.out.println(Language.translate("Jade wurde beendet!"));
		}
		// ------------------------------------------------

		// --- Reset runtime-variables -------------------- 
		MAS_ContainerRemote.removeAllElements();
		MAS_ContainerLocal.removeAllElements();
		MASmc = null;
		MASrt = null;
		Application.setStatusJadeRunning(false);
		
	}

	/** 
	 * Checks, whether the main-container (Jade himself) is running or not 
	 */
	public boolean jadeMainContainerIsRunning(Boolean ForceJadeStart) {
		if ( ForceJadeStart == true ) {
			jadeSystemAgentOpen("rma", null);
		}		
		return jadeMainContainerIsRunning();
	}
	public boolean jadeMainContainerIsRunning () {
		boolean JiR;		
		try {
			MASmc.getState();
			JiR = true;
		}
		catch (Exception eMC) {
			JiR = false; //	eMC.printStackTrace();	
			MASmc = null;
			try {
				MASrt.shutDown();				
			} catch (Exception eRT ) { 
				//eRT.printStackTrace();				
			}			
			MASrt = null;	
		}
		return JiR;
	}
	
	
	/**
	 * Starts the Utility-Agent with a job defined in agentAction 
	 */
	public void jadeUtilityAgentStart(int utilityCMD) {
		Object[] agentArgs = new Object[5];
		agentArgs[0] = utilityCMD;
		jadeAgentStart("utility", mas.agents.UtilityAgent.class.getName(), agentArgs);
	}
	
	/**
	 * Starts an Agent, if the main-container exists 
	 */
	public void jadeSystemAgentOpen( String RootAgentName, Integer OptionalPostfixNo ) {
		// --- Table of the known Jade System-Agents -----
		Hashtable<String, String> JadeSystemTools = new Hashtable<String, String>();
		JadeSystemTools.put( "rma", "jade.tools.rma.rma" );
		JadeSystemTools.put( "sniffer", "jade.tools.sniffer.Sniffer" );
		JadeSystemTools.put( "dummy", "jade.tools.DummyAgent.DummyAgent" );
		JadeSystemTools.put( "df", "mas.agents.DFOpener" );
		JadeSystemTools.put( "loadmonitor", mas.service.agents.LoadAgent.class.getName());
		JadeSystemTools.put( "introspector", "jade.tools.introspector.Introspector" );
		JadeSystemTools.put( "log", "jade.tools.logging.LogManagerAgent" );
		
		AgentController AgeCon = null;
		String AgentNameSearch  = RootAgentName.toLowerCase();
		String AgentNameClass = null;
		String AgentNameForStart = RootAgentName;
		
		String MsgHead = null;
		String MsgText = null;
		Integer MsgAnswer = null;
		
		
		// --- Setting the real name of the agent to start --- 
		if ( OptionalPostfixNo != null ) 
			AgentNameForStart = RootAgentName + OptionalPostfixNo.toString(); 
		
		// --- Was the system already started? ---------------
		if ( jadeMainContainerIsRunning() == false ) {
			MsgHead = Language.translate("JADE wurde noch nicht gestartet!");
			MsgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
			MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return; // --- NO,just exit 
			// --- Start the JADE-Platform -------------------
			jadeStart();
			if ( AgentNameForStart.equalsIgnoreCase("rma") ) {
				try {
					AgeCon = MASmc.getAgent("rma");
				} catch (ControllerException e) {
					e.printStackTrace();
				}				
				return;
			}
		}
	
		// ---------------------------------------------------
		// --- Can a path to the agent be found? -------------   
		AgentNameClass = JadeSystemTools.get( AgentNameSearch );
		if ( AgentNameClass == null ) {
			System.out.println( "jadeSystemAgentOpen: Unbekannter System-Agent => " + RootAgentName);
			return;
		}
		
		// --- Does an agent (see name) already exists? ------
		if ( jadeAgentIsRunning( AgentNameForStart ) == true && AgentNameForStart.equalsIgnoreCase("df")==false ) {
			// --- Agent already EXISTS !! -------------------
			MsgHead = Language.translate("Der Agent '") + RootAgentName +  Language.translate("' ist bereits geöffnet!");
			MsgText = Language.translate("Möchten Sie einen weiteren Agenten dieser Art starten?");
			if (Application.isServer) {
				MsgAnswer =  JOptionPane.showConfirmDialog( null, MsgText, MsgHead, JOptionPane.YES_NO_OPTION);				
			} else {
				MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);	
			}
			if ( MsgAnswer == 0 ) {
				// --- YES - Start another agent of this kind ---------
				jadeSystemAgentOpen( RootAgentName, NewPostfixNo(RootAgentName) );
			}
			else {
				// --- NO - Set Focus to already running component ----
				/**
				 * ToDo: Set Focus to already running component 
				 */
			}
		}
		else {
			// --- Agent doe's NOT EXISTS !! ---------------------
			try {
				if ( AgentNameForStart.equalsIgnoreCase("df") ) {
					// --- Show the DF-GUI -----------------------
					this.jadeUtilityAgentStart(UTIL_CMD_OpenDF);
					return;					
				} else if (AgentNameForStart.equalsIgnoreCase("loadMonitor") ) {
					this.jadeUtilityAgentStart(UTIL_CMD_OpenLoadMonitor);
					return;					
				} else {
					// --- Show a standard jade ToolAgent --------
					AgeCon = MASmc.createNewAgent(AgentNameForStart, AgentNameClass, new Object[0]);
				}
				AgeCon.start();
			} 
			catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}	
	/**
	 * Find's a new postfix number for the name of an agent
	 */
	private Integer NewPostfixNo( String AgentName ) {

		String NewName = AgentName;
		Integer i = 0;
		
		while ( jadeAgentIsRunning( NewName ) == true ) {
			i++;
			NewName = AgentName + i.toString();			
		}			
		return i;
	}
	
	/**
	 * Kills an Agent, if he is running
	 */
	public void jadeSystemAgentKill( String AgentName ) {

		AgentController AgeCon = null;
		if ( jadeAgentIsRunning( AgentName ) ) {
			// --- get Agent(Controller) -----
			try {
				AgeCon = MASmc.getAgent(AgentName);
			} 
			catch (ControllerException e) {
				//  e.printStackTrace();
			}
			// --- Kill the Agent ------------			
			try {
				AgeCon.kill();
			} 
			catch (StaleProxyException e) {
				// e.printStackTrace();
			}
		}
	}	
	
	
	
	/** 
	 * Checks, whether one Agent is running (or not) in the main-container. 
	 * Returns true or false  
	 */
	public boolean jadeAgentIsRunning ( String AgentName ) {
		return jadeAgentIsRunning( AgentName, MASmc.getName() );
	}
	public boolean jadeAgentIsRunning ( String AgentName, String ContainerName ) {
		
		boolean AgentiR;
		AgentContainer AgenCont = null;
		AgentController AgeCon = null;
		
		if (jadeMainContainerIsRunning() == false ) {
			return false;
		}

		// --- 0. Set to the right Container ------------------------
		AgenCont = jadeContainer(ContainerName);
		if ( AgenCont == null ) {
			return false;
		}
		// --- 1. try to find the agent in main-container -----------
		try {
			AgeCon = AgenCont.getAgent(AgentName);
		} 
		catch (ControllerException CE) {
			return false; 	// CE.printStackTrace();			
		}
		
		// --- 2. try to get agent's state --------------------------				
		try {
			@SuppressWarnings("unused")
			State Stat = AgeCon.getState();
			//System.out.println( "Staus of Agent '" + AgeCon.getName() + "': " +  Stat.getCode() + " - " + Stat.getName()); 
			AgentiR = true;
		}
		catch (Exception eMC) {
			AgentiR = false; //	eMC.printStackTrace();			
		}
		return AgentiR;
	}	
	
	/**
	 * Adding an AgentContainer to the local platform
	 */
	public AgentContainer jadeContainerCreate( String ContainerName ) {
		Profile pSub = this.jadeGetContainerProfile();
		pSub.setParameter( Profile.CONTAINER_NAME, ContainerName );
		AgentContainer MAS_AgentContainer = MASrt.createAgentContainer( pSub );
		MAS_ContainerLocal.add( MAS_AgentContainer );
		return MAS_AgentContainer;		
	}	
	/**
	 * Returns the Container given by it's name 
	 */
	public AgentContainer jadeContainer ( String ContainerNameSearch ) {
		
		AgentContainer AgeCont = null;
		String AgeContName = null;
		
		// --- Falls JADE noch nicht läuft ... ----------------
		if ( jadeMainContainerIsRunning() == false ) {
			return null;
		}
		// --- Wird nach dem 'Main-Container' gesucht? --------
		if ( ContainerNameSearch == MASmc.getName() ) {
			return MASmc;
		}	
		
		// --- Den richtigen Container abgreifen -------------- 
		for ( int i=0; i < MAS_ContainerLocal.size(); i++ ) {
			AgeCont = MAS_ContainerLocal.get(i);
			try {
				AgeContName = AgeCont.getContainerName();
			} 
			catch (ControllerException ex) {
				ex.printStackTrace();
			}			
			if ( AgeContName.equalsIgnoreCase( ContainerNameSearch ) == true ) {
				break;
			}
		}		
		return AgeCont;
	}
	/**
	 * Kills an AgentContainer to the local platform
	 */
	public void jadeContainerKill( String ContainerName ) {
		AgentContainer AgeCont = null;
		AgeCont = jadeContainer(ContainerName);
		jadeContainerKill( AgeCont );
	}
	public void jadeContainerKill( AgentContainer agentContainer ) {
		
		MAS_ContainerLocal.remove( agentContainer );
		try {
			agentContainer.kill();
		} 
		catch (StaleProxyException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Adding an Agent to a Container 
	 */
	public void jadeAgentStart(String AgentName, String Clazz) {
		String MainContainerName = MASmc.getName();
		jadeAgentStart(AgentName, Clazz, null, MainContainerName) ;
	}
	public void jadeAgentStart(String AgentName, String Clazz, String ContainerName ) {
		jadeAgentStart(AgentName, Clazz, null, ContainerName) ;
	}
	public void jadeAgentStart(String AgentName, String Clazz, Object[] AgentArgs ) {
		String MainContainerName = MASmc.getName();
		jadeAgentStart(AgentName, Clazz, AgentArgs, MainContainerName);
	}
	public void jadeAgentStart(String AgentName, String clazzName, Object[] AgentArgs, String ContainerName ) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Agent> clazz = (Class<? extends Agent>) Class.forName(clazzName);
			jadeAgentStart(AgentName, clazz, AgentArgs, ContainerName );

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void jadeAgentStart(String AgentName, Class<? extends Agent> Clazz, Object[] AgentArgs, String ContainerName ) {
		
		int MsgAnswer;
		String MsgHead, MsgText;
		AgentController AgentCont;
		AgentContainer AgeCont;

		// --- Was the system already started? ---------------
		if ( jadeMainContainerIsRunning() == false ) {
			MsgHead = Language.translate("JADE wurde noch nicht gestartet!");
			MsgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
			MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return; // --- NO,just exit 
			// --- Start the JADE-Platform -------------------
			jadeStart();			
		}
		
		AgeCont = this.jadeContainer( ContainerName );
		if ( AgeCont == null ) {
			AgeCont = jadeContainerCreate( ContainerName );
		}		
		try {
			Agent agent = (Agent) Clazz.newInstance();
			agent.setArguments(AgentArgs);
//			AgentCont = AgeCont.createNewAgent( AgentName, Clazz, AgentArgs );
			AgentCont = AgeCont.acceptNewAgent(AgentName, agent);
			AgentCont.start();

		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Starts the search for Agents in the  
	 * current environment in an own Thread
	 */
	public void jadeFindAgentClasse(String jarFilePath) {
		Agents = new jadeClasses<Agent>("jade.core.Agent",jarFilePath);
		Thread th = new Thread( Agents  );
		th.setName("Search4Agents");
//		th.setContextClassLoader(classLoader);
		th.start();
	}
	
	public Vector<Class<? extends Agent>> jadeGetAgentClasses( String FilterFor) {
		return 	jadeGetAgentClasses(FilterFor, null );
	}
	
	/**
	 * 
	 * @param FilterFor
	 * @return
	 */
	public Vector<Class<? extends Agent>> jadeGetAgentClasses( String FilterFor, String jarFilePath ) {
		
		boolean PrintMsg = true;
		Class<? extends Agent> CurrClass = null;
		String CurrClassName;
		Vector<Class<? extends Agent>> FilteredVector = new Vector<Class<? extends Agent>>();
		
		// -------------------------------------------------------------
		// --- Falls noch keine Klassen gefunden wurden, warten ...  ---
		// -------------------------------------------------------------
		if (Agents==null) {
			this.jadeFindAgentClasse(jarFilePath);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while ( Agents.getResultCount()==0 ) {
			try {
				if ( PrintMsg == true ) {
					Application.setStatusBar( Language.translate("Warte auf Agentenliste...") );
					Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );															
					Application.MainWindow.repaint();
					PrintMsg = false;
				}				
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		Application.setStatusBar( Language.translate("Fertig") );
		Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );
		Application.MainWindow.repaint();
		
		// -------------------------------------------------------------
		// --- Agentenliste auslesen und ggf. filtern ------------------
		// -------------------------------------------------------------
		AgentsVector = Agents.getClassesFound();
		int i_max = AgentsVector.size();
		for ( int i = 0; i<i_max; i++ ) {			
			CurrClass = AgentsVector.get(i);
			CurrClassName = CurrClass.getName();
			if ( FilterFor == null || CurrClassName.startsWith( FilterFor ) == true ) {
				FilteredVector.addElement(  CurrClass );								
			}			
		}		
		return FilteredVector;
	}
	/**
	 * Starts the search for Ontologies in the  
	 * current environment in an own Thread
	 */
	public void jadeFindOntologyClasse() {
		Ontologies = new jadeClasses<Ontology>("jade.content.onto.Ontology",null);
		Thread th = new Thread( Ontologies );
		th.setName("Search4Ontologies");
		th.start();
	}
	/**
	 * This method returns all known Ontologies  
	 * from the current environment
	 * @return
	 */
	public Vector<Class<? extends Ontology>> jadeGetOntologyClasse() {
		
		boolean PrintMsg = true;
		// -------------------------------------------------------------
		// --- Falls noch keine Klassen gefunden wurden, warten ...  ---
		// -------------------------------------------------------------
		if (Ontologies==null) {
			this.jadeFindOntologyClasse();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while ( Ontologies.getResultCount()==0 ) {
			try {
				if ( PrintMsg == true ) {
					Application.setStatusBar( Language.translate("Warte auf Ontologie-Liste...") );
					Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );	
					Application.MainWindow.repaint();
					PrintMsg = false;
				}				
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		Application.setStatusBar( Language.translate("Fertig") );
		Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );
		Application.MainWindow.repaint();
		
		OntologyVector = Ontologies.getClassesFound();
		return OntologyVector;
	}	
	
	/**
	 * Class for searching special classe, given
	 * by an example (e. g. 'jade.core.Agent') 
	 */
	public class jadeClasses<classType> implements Runnable {

		private CustomClassFinder cf = null;
		private Vector<Class<? extends classType>> classVector = new Vector<Class<? extends classType>>();
		private String superClass = null;
		private String jarFilePath = null;
		
		public jadeClasses( String superClazz , String jarFilePath) {
			superClass = superClazz;
			this.jarFilePath=jarFilePath;
		}		
		@Override
		public void run() {
			this.FindClasse(superClass);
		}
		@SuppressWarnings("unchecked")
		public void FindClasse(String SuperClass)  {
		
			cf = new CustomClassFinder(jarFilePath);
			
			classVector = cf.findSubclasses(SuperClass);
			//System.out.println( Language.translate( "Suche nach " + SuperClass + " beendet .. " ) );
		}	
		public Vector<Class<? extends classType>> getClassesFound() {
			return classVector;
		}
		public boolean isWorking() {
			return cf.isWorking();
		}
		public int getResultCount() {
			return classVector.size();
		}

	} // -- End Sub-Class ---
	
	
}// -- End Class ---
