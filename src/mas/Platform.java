package mas;

import jade.core.Profile;
import jade.core.ProfileImpl;
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

import network.PortChecker;

import application.Application;
import application.Language;

public class Platform extends java.lang.Object {

	/**
	 * This class manages the jade-platform 
	 * for this local application 
	 */
	public static Runtime MASrt;
	public static AgentContainer MASmc;
	public static Vector<AgentContainer> MAScontainer = new Vector<AgentContainer>();
		
	private jadeClasses Agents; 
	public static Vector<Class<?>> AgentsVector;
	

	public Platform() {
		
		// ----------------------------------------------
		// --- Nach allen Agent-Klassen suchen ----------
		jadeFindAgentClasse(); // Extra Thread !! -------
		// ----------------------------------------------
		/*String Res = PlatformRMI.isJADERunning("rudel1", "1100");
		System.out.println( Res );
		jade.domain.FIPAAgentManagement.APDescription AP  = new jade.domain.FIPAAgentManagement.APDescription();
		AP.setName("Hallo");
		System.out.println( AP.toString() );
		*/
		//PlatformSysInfo.getLoadAverage();
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
				
				// --- Services zusammenstellen ---------------------
				String ServiceList = new String();
				ServiceList = ServiceList.concat("jade.core.event.NotificationService;");
				ServiceList = ServiceList.concat("jade.core.mobility.AgentMobilityService;");
				ServiceList = ServiceList.concat("jade.core.messaging.TopicManagementService;");
				ServiceList = ServiceList.concat("jade.core.migration.InterPlatformMobilityService;");
				
				// --- Freien Port für die Plattform finden ---------
				PortChecker portCheck = new PortChecker(Application.RunInfo.getJadeLocalPort());
				Integer usePort = portCheck.getFreePort();
				
				// --- MainContainer starten ------------------------				
				Profile pMain = new ProfileImpl();
				pMain.setParameter(Profile.SERVICES, ServiceList );
				pMain.setParameter(Profile.LOCAL_PORT, usePort.toString());
				MASmc = MASrt.createMainContainer( pMain );				
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println( "Jade läuft bereits! => " + MASrt );			
		}
		Application.MainWindow.setStatusJadeRunning(true);
		// --- Jade-GUI zeigen --------------------------------------
		jadeSystemAgentOpen( "rma", null );
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
				System.out.println("Jade wurde beendet!");
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
			MsgHead = Language.translate("Jade wurde noch nicht gestartet!");
			MsgText = Language.translate("Möchten Sie Jade nun starten und fortfahren?");
			MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return; // --- NO,just exit 
			// --- Start the Jade-Platform -------------------
			jadeStart();
			if ( RootAgentName == "rma" ) {
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
		if ( jadeAgentIsRunning( AgentNameForStart ) == true ) {
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
				if ( RootAgentName == "DF" ) {
					// --- Show the DF-GUI -----------------------
					AgeCon = MASmc.createNewAgent("DFOpener", AgentNameClass, new Object[0]);
					AgeCon.start();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					AgeCon.kill();					
				}
				else {
					// --- Show a standard jade ToolAgent --------
					AgeCon = MASmc.createNewAgent(AgentNameForStart, AgentNameClass, new Object[0]);
					AgeCon.start();					
				}
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
		
		Profile pSub = new ProfileImpl();
		pSub.setParameter( Profile.CONTAINER_NAME, ContainerName );
		pSub.setParameter( Profile.SERVICES ,"jade.core.event.NotificationService;jade.core.messaging.TopicManagementService" );
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
		
		// --- Falls Jade noch nicht läuft ... ----------------
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
			MsgHead = Language.translate("Jade wurde noch nicht gestartet!");
			MsgText = Language.translate("Möchten Sie Jade nun starten und fortfahren?");
			MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return; // --- NO,just exit 
			// --- Start the Jade-Platform -------------------
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
	
	
	public Vector<Class<?>> jadeGetAgentClasses( String FilterFor ) {
		
		boolean PrintMsg = true;
		Class<?> CurrClass = null;
		String CurrClassName;
		Vector<Class<?>> FilteredVector = new Vector<Class<?>>();
		
		// -------------------------------------------------------------
		// --- Falls noch keine Klassen gefunden wurden, warten ...  ---
		// -------------------------------------------------------------
		while ( Agents.getClassesCount() == 0 ) {
			try {
				if ( PrintMsg == true ) {
					Application.setStatusBar( Language.translate("Warte auf Agentenliste...") );
					Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );															
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
	 * Starts the search for Agents in the  
	 * current environment in an own Thread
	 */
	public void jadeFindAgentClasse() {
		Agents = new jadeClasses("jade.core.Agent");
		Thread Th = new Thread( Agents );		
		Th.start();		
	}	
	/**
	 * Class for searching special classe, given
	 * by an example (e. g. 'jade.core.Agent') 
	 */
	public class jadeClasses implements Runnable {

		private Vector<Class<?>> ClassVector = null;
		private String SuperClass = null;
		
		public jadeClasses(String SuperClazz) {
			SuperClass = SuperClazz;			
		}		
		@Override
		public void run() {
			ClassVector = null;
			FindClasse(SuperClass);
		}
		@SuppressWarnings("unchecked")
		public void FindClasse(String SuperClass)  {
			ClassFinder cf = new ClassFinder();
			ClassVector = cf.findSubclasses(SuperClass);
			//System.out.println( Language.translate( "Suche nach Agenten beendet .. " ) );
		}	
		public Vector<Class<?>> getClassesFound() {
			return ClassVector;
		}
		public int getClassesCount() {
			if ( ClassVector== null || ClassVector.isEmpty() ) {
				return 0;
			}
			else {
				return ClassVector.size();	
			}			
		}		
	} // -- End Sub-Class ---
	
	
}// -- End Class ---
