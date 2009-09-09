package mas;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import application.Application;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.*;

import application.Language;

public class Plattform extends java.lang.Object {

	/**
	 * This class manages the jade-platform 
	 * for this local application 
	 */
	public static Runtime MASrt;
	public static AgentContainer MASmc;
	
	public void jadeStart() {
		/**
		 * Starting the jade-platform  
		 */		
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
				Profile pMain = new ProfileImpl();
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
	
	public void jadeStop() {
		/**
		 * Shutting down the jade-platform 
		 */
		try {
			if ( jadeMainContainerIsRunning() ) MASmc.kill();
			if ( MASrt != null ) {
				MASrt.shutDown();
				System.out.println("Jade wurde beendet!");
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		MASmc = null;
		MASrt = null;
		Application.MainWindow.setStatusJadeRunning(false);
	}

	
	public static boolean jadeMainContainerIsRunning () {
		/** 
		 * Checks, whether the main-container is running or not 
		 */
		boolean JiR;		
		try {
			State Stat = MASmc.getState();
			System.out.println( "Staus of main-container: " +  Stat.getCode() + " - " + Stat.getName()); 
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
	
	public void jadeSystemAgentOpen( String RootAgentName, Integer OptionalPostfixNo ) {
		/**
		 * Starts an Agent, if the main-container exists 
		 */
		// --- Table of the known Jade System-Agents -----
		Hashtable<String, String> JadeSystemTools = new Hashtable<String, String>();
		JadeSystemTools.put( "rma", "jade.tools.rma.rma" );
		JadeSystemTools.put( "sniffer", "jade.tools.sniffer.Sniffer" );
		JadeSystemTools.put( "dummy", "jade.tools.DummyAgent.DummyAgent" );
		JadeSystemTools.put( "introspector", "jade.tools.introspector.Introspector" );
		JadeSystemTools.put( "log", "jade.tools.logging.LogManagerAgent" );
		
		AgentController AgeCon;
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
			this.jadeStart();
			if ( RootAgentName == "rma" ) return;
		}
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
			// --- Agent doe's NOT EXISTS !! -----------------
			try {
				AgeCon = MASmc.createNewAgent(AgentNameForStart, AgentNameClass, new Object[0]);
				AgeCon.start();
			} 
			catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}	
	private Integer NewPostfixNo( String AgentName ) {
		/**
		 * Find's a new postfix number for the name of an agent
		 */
		String NewName = AgentName;
		Integer i = 0;
		
		while ( jadeAgentIsRunning( NewName ) == true ) {
			i++;
			NewName = AgentName + i.toString();			
		}			
		return i;
	}

	
	public void jadeSystemAgentKill( String AgentName ) {
		/**
		 * Kills an Agent, if he is running
		 */
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
	public static boolean jadeAgentIsRunning ( String AgentName ) {
		/** 
		 * Checks, whether one Agent is running (or not) in the main-container. 
		 * Returns true or false  
		 */
		boolean AgentiR;
		AgentController AgeCon = null;

		// --- 1. try to find the agent in main-container -----------
		try {
			AgeCon = MASmc.getAgent(AgentName);			
		} 
		catch (ControllerException CE) {
			return false; 	// CE.printStackTrace();			
		}
		
		// --- 2. try to get agent's state -----------				
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
	
	public AgentContainer jadeCreateAgentContainer() {
		/**
		 * Adding an AgentContainer to the local platform
		 */
		Profile pSub = new ProfileImpl();
		AgentContainer MAS_AgentContainer = MASrt.createAgentContainer( pSub );		
		return MAS_AgentContainer;		
	}
	
	public void jadeCreateNewAgent() throws StaleProxyException {
		/**
		 * Adding an Agent to a Container 
		 */
		MASmc.createNewAgent("Chris_1", "mas.HelloWorldAgent", null);
		
	}

}
