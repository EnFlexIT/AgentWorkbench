package mas;

import jade.core.Profile;
import jade.core.Runtime;
import jade.util.ClassFinder;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;

import java.awt.Cursor;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import application.Application;
import application.Language;
import application.Project;

public class Platform extends Object {

	/**
	 * This class manages the jade-platform 
	 * for this local application 
	 */
	public static Runtime MASrt;
	public static AgentContainer MASmc;
	public static Vector<AgentContainer> MAScontainer = new Vector<AgentContainer>();
	public PlatformJadeConfig MASplatformConfig = null;
	
	private jadeClasses Agents; 
	private jadeClasses Ontologies;
	public Vector<Class<?>> AgentsVector   = null;
	public Vector<Class<?>> OntologyVector = null;
	
	public Platform() {
		// --- Search for all Agent-Classes -------------
		this.jadeFindAgentClasse(); // new Thread !! 
	}	
	
	/**
	 * Starting the jade-platform  
	 */		
	public void jadeStart() {
		if ( jadeMainContainerIsRunning() == false ) {
			try {
				// --- Plattfom starten -----------------------------
				MASrt = Runtime.instance();	
				MASrt.invokeOnTermination( new Runnable() {
					public void run() {
						MASmc = null;
						MASrt = null;
						Application.MainWindow.setStatusJadeRunning(false);
					}
				});
				// --- MainContainer starten ------------------------				
				MASmc = MASrt.createMainContainer( this.jadeGetContainerProfile() );				
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println( "JADE läuft bereits! => " + MASrt );			
		}
		Application.MainWindow.setStatusJadeRunning(true);
		// --- JADE-GUI zeigen --------------------------------------
		jadeSystemAgentOpen( "rma", null );
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
			MAScontainerProfile = Application.RunInfo.getJadeDefaultProfile();
			System.out.println("JADE-Profile: Use AgentGUI-defaults");
		} else {
			// --- Take the Profile of the current Project ----------
			if (currProject.JadeConfiguration.isUseDefaults()==true) {
				MAScontainerProfile = Application.RunInfo.getJadeDefaultProfile();
				System.out.println("JADE-Profile: Use Agent.GUI-defaults");
			} else {
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

		AgentContainer AgeCont;
		
		// --- Stop all known platform-container ---------
		for ( int i=0; i < MAScontainer.size(); i++ ) {
			AgeCont = MAScontainer.get(i);
			jadeContainerKill(AgeCont);	
		}	
		// --- Stop the Main-Platform --------------------
		try {
			if ( jadeMainContainerIsRunning() ){
				MASmc.kill();
			}
			if ( MASrt != null ) {
				MASrt.shutDown();
				System.out.println("JADE wurde beendet!");
			}			
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		MASmc = null;
		MASrt = null;
		Application.MainWindow.setStatusJadeRunning(false);
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
			@SuppressWarnings("unused")
			State Status = MASmc.getState();
			//System.out.println( "Status of main-container: " +  Status.getCode() + " - " + Status.getName()); 
			JiR = true;
			Application.MainWindow.setStatusJadeRunning(true);
		}
		catch (Exception eMC) {
			JiR = false; //	eMC.printStackTrace();	
			Application.MainWindow.setStatusJadeRunning(false);
			MASmc  = null;
			try {
				MASrt.shutDown();				
			}
			catch (Exception eRT ){				
			}
			MASrt = null;			
		}
		return JiR;
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
			MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
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
					AgeCon = MASmc.createNewAgent("DFOpener", AgentNameClass, new Object[0]);
				}
				else {
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
			State Stat = AgeCon.getState();
			System.out.println( "Staus of Agent '" + AgeCon.getName() + "': " +  Stat.getCode() + " - " + Stat.getName()); 
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
		MAScontainer.add( MAS_AgentContainer );
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
		for ( int i=0; i < MAScontainer.size(); i++ ) {
			AgeCont = MAScontainer.get(i);
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
	public void jadeContainerKill( AgentContainer AgeCont ) {
		
		MAScontainer.remove( AgeCont );
		try {
			AgeCont.kill();
		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
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
	public void jadeAgentStart(String AgentName, String Clazz, Object[] AgentArgs, String ContainerName ) {
		
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
		
		AgeCont = jadeContainer( ContainerName );
		if ( AgeCont == null ) {
			AgeCont = jadeContainerCreate( ContainerName );
		}		
		try {
			AgentCont = AgeCont.createNewAgent( AgentName, Clazz, AgentArgs );
			AgentCont.start();
		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
		}		

	}
	
	/**
	 * Starts the search for Agents in the  
	 * current environment in an own Thread
	 */
	public void jadeFindAgentClasse() {
		Agents = new jadeClasses("jade.core.Agent");
		Thread th = new Thread( Agents  );
		th.setName("Search4Agents");
		th.start();
	}	
	/**
	 * 
	 * @param FilterFor
	 * @return
	 */
	public Vector<Class<?>> jadeGetAgentClasses( String FilterFor ) {
		
		boolean PrintMsg = true;
		Class<?> CurrClass = null;
		String CurrClassName;
		Vector<Class<?>> FilteredVector = new Vector<Class<?>>();
		
		// -------------------------------------------------------------
		// --- Falls noch keine Klassen gefunden wurden, warten ...  ---
		// -------------------------------------------------------------
		if (Agents==null) {
			this.jadeFindAgentClasse();
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
			CurrClass = (Class<?>) AgentsVector.get(i);
			CurrClassName = CurrClass.getName();
			if ( FilterFor == null || CurrClassName.startsWith( FilterFor ) == true ) {
				FilteredVector.addElement( CurrClass );								
			}			
		}		
		return FilteredVector;
	}
	/**
	 * Starts the search for Ontologies in the  
	 * current environment in an own Thread
	 */
	public void jadeFindOntologyClasse() {
		Ontologies = new jadeClasses("jade.content.onto.Ontology");
		Thread th = new Thread( Ontologies );
		th.setName("Search4Ontologies");
		th.start();
	}
	/**
	 * This method returns all known Ontologies  
	 * from the current environment
	 * @return
	 */
	public Vector<Class<?>> jadeGetOntologyClasse() {
		
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
	public class jadeClasses implements Runnable {

		private ClassFinder cf = null;
		private Vector<Class<?>> classVector = new Vector<Class<?>>();
		private String superClass = null;
		
		public jadeClasses( String superClazz ) {
			superClass = superClazz;			
		}		
		@Override
		public void run() {
			this.FindClasse(superClass);
		}
		@SuppressWarnings("unchecked")
		public void FindClasse(String SuperClass)  {
			cf = new ClassFinder();
			classVector = cf.findSubclasses(SuperClass);
			//System.out.println( Language.translate( "Suche nach " + SuperClass + " beendet .. " ) );
		}	
		public Vector<Class<?>> getClassesFound() {
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
