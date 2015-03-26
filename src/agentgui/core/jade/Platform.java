/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.jade;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Profile;
import jade.core.Runtime;
import jade.debugging.DebugService;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import agentgui.core.agents.UtilityAgent;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.project.Project;
import agentgui.core.webserver.DownloadServer;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.SimulationService;

/**
 * This class manages the interaction between AgentGUI and JADE.<br>
 * It contains the methods to start / stop JADE, as well as methods<br>
 * for starting new container or agents <br>
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Platform extends Object {

	public enum UTILITY_AGENT_JOB {
		OpernDF,
		ShutdownPlatform,
		OpenLoadMonitor,
		ExitDeviceExecutionModus
	}
	
	private static final String BackgroundSystemAgentApplication = "server.client";
	private static final String BackgroundSystemAgentServerMaster = "server.master";
	private static final String BackgroundSystemAgentServerSlave = "server.slave";

	private Runtime jadeRuntime;
	private AgentContainer jadeMainContainer;
	private Vector<AgentContainer> jadeContainerLocal = new Vector<AgentContainer>();
	private Vector<ContainerID> jadeContainerRemote = new Vector<ContainerID>();
	
	private String newLine = Application.getGlobalInfo().getNewLineSeparator();
	
	
	/**
	 * Constructor of this class.
	 */
	public Platform() {

	}	
	
	/**
	 * This Method will start - depending on the Configuration - the
	 * programs-background-agents.
	 * It starts directly after starting the JADE-Platform
	 *
	 * @param showRMA specifies if the rma should appear or not
	 * @return true, if successful
	 */
	private boolean jadeStartBackgroundAgents(boolean showRMA) {
		
		// ----------------------------------------------------------
		// --- Differentiation of the Application-Case --------------
		// ----------------------------------------------------------
		String applicationTitle = Application.getGlobalInfo().getApplicationTitle();
		String executionModeDescription = Application.getGlobalInfo().getExecutionModeDescription();
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			if (jadeAgentIsRunning(BackgroundSystemAgentApplication)==false) {
				jadeAgentStart(BackgroundSystemAgentApplication, agentgui.simulationService.agents.ServerClientAgent.class.getName());	
			}
			// --- Start RMA ('Remote Monitoring Agent') -----------
			if (showRMA==true) {
				jadeSystemAgentOpen("rma", null);	
			}
			break;
		
		case SERVER_MASTER:
			// -------------------------------------------------
			// --- This is a Master-Server-Platform ------------
			// -------------------------------------------------
			// --- Connecting to Database ----------------------
			if (Application.getDatabaseConnection(true).hasErrors==true ) {
				
				this.jadeStop();
				
				String msgHead = "";
				String msgText = "";
				
				msgHead += Language.translate("Konfiguration des") + " " + applicationTitle + "-" +  executionModeDescription;
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
			if (jadeAgentIsRunning(BackgroundSystemAgentServerMaster)==false) {
				this.jadeAgentStart(BackgroundSystemAgentServerMaster, agentgui.simulationService.agents.ServerMasterAgent.class.getName());	
			}
			break;
			
		case SERVER_SLAVE:
			// -------------------------------------------------
			// --- This is a Slave-Server-Platform -------------
			// -------------------------------------------------
			if (Application.getGlobalInfo().getServerMasterURL()==null ||
				Application.getGlobalInfo().getServerMasterURL().equalsIgnoreCase("")==true ||
				Application.getGlobalInfo().getServerMasterPort().equals(0)==true ||
				Application.getGlobalInfo().getServerMasterPort4MTP().equals(0)==true ) {
				
				this.jadeStop();
				
				String msgHead = "";
				String msgText = "";
				
				msgHead += Language.translate("Konfiguration des") + " " + applicationTitle + "-" +  executionModeDescription;
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
			if (jadeAgentIsRunning(BackgroundSystemAgentServerSlave)==false) {			
				this.jadeAgentStart(BackgroundSystemAgentServerSlave, agentgui.simulationService.agents.ServerSlaveAgent.class.getName());
			}
			break;
		
		case DEVICE_SYSTEM:
			// -------------------------------------------------
			// --- Run as Service / Embedded System Agent ------
			// -------------------------------------------------
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case SETUP:
				if (jadeAgentIsRunning(BackgroundSystemAgentApplication)==false) {
					jadeAgentStart(BackgroundSystemAgentApplication, agentgui.simulationService.agents.ServerClientAgent.class.getName());	
				}
				break;

			case AGENT:
				EmbeddedSystemAgentVisualisation visualisation = Application.getGlobalInfo().getDeviceServiceAgentVisualisation();
				if (visualisation==EmbeddedSystemAgentVisualisation.NONE) {
					jadeUtilityAgentStart(UTILITY_AGENT_JOB.ExitDeviceExecutionModus);	
				}
				break;
			}
			
			break;
			
		default:
			// --- Nothing to do here -----------
			break;
		}
		return true;
	}

	/**
	 * Starts JADE and displays the RMA
	 * @return true, if successful
	 */		
	public boolean jadeStart() {
		return jadeStart(false, null);
	}	
	/**
	 * Starts JADE.
	 * @param showRMA set true, if you want to see the RMA
	 * @return true, if successful
	 */
	public boolean jadeStart(boolean showRMA) {
		return jadeStart(showRMA, null);
	}
	/**
	 * Starts JADE.
	 * @param showRMA set true, if you want to see the RMA 
	 * @param containerProfile the actual container Profile
	 * @return true, if successful
	 */
	public boolean jadeStart(boolean showRMA, Profile containerProfile) {
		
		boolean startSucceed = false;		
		
		if (jadeIsMainContainerRunning()==false) {
			try {
				// --- Start Platform -------------------------------
				jadeRuntime = Runtime.instance();	
				jadeRuntime.invokeOnTermination(new Runnable() {
					public void run() {
						jadeMainContainer = null;
						jadeRuntime = null;
						Application.setStatusJadeRunning(false);
						if (Application.getMainWindow()!=null){
							Application.getMainWindow().setSimulationReady2Start();
						}
					}
				});
				// --- Start MainContainer --------------------------
				if (containerProfile!=null) {
					jadeMainContainer = jadeRuntime.createMainContainer(containerProfile);
				} else {
					jadeMainContainer = jadeRuntime.createMainContainer(this.jadeGetContainerProfile());
				}
				startSucceed = true;
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		} else {
			System.out.println( "JADE läuft bereits! => " + jadeRuntime );			
		}

		// --- Start the Application Background-Agents ---------------
		if (this.jadeStartBackgroundAgents(showRMA)==false) return false;
		
		Application.setStatusJadeRunning(true);
		return startSucceed;
	}
	
	/**
	 * Start JADE for a globally specified embedded system agent.
	 * @return true, if successful
	 */
	public boolean jadeStart4EmbeddedSystemAgent() {
		String agentClassName = Application.getGlobalInfo().getDeviceServiceAgentSelected();
		if (agentClassName!=null) {
			return this.jadeStart4EmbeddedSystemAgent(agentClassName);
		}
		return false;
	}
	/**
	 * Start JADE for a specified embedded system agent.
	 * @param agentClassName the agent class name
	 * @return true, if successful
	 */
	public boolean jadeStart4EmbeddedSystemAgent(String agentClassName) {
		
		boolean jadeStarted = false;
		
		// --- Remove the Agent.GUI services ------------------------
		Profile jadeProfile = this.jadeGetContainerProfile();
		// --- Stop the DownloadServer again ------------------------
		Application.stopDownloadServer();

		// --- Remove Agent.GUI services ----------------------------
		String servicesNew = "";
		String services = jadeProfile.getParameter("services", null);
		String[] serviceArray = services.split(";");
		for (int i = 0; i < serviceArray.length; i++) {
			if (serviceArray[i].equals(SimulationService.class.getName())==false &&
				serviceArray[i].equals(LoadService.class.getName())==false &&
				serviceArray[i].equals(DebugService.class.getName())==false) {
				servicesNew += serviceArray[i] + ";";
			}
		}
		jadeProfile.setParameter("services", servicesNew);
		
		// --- start JADE with this profile -------------------------
		if (this.jadeStart(false, jadeProfile)) {
			try {
				// --- Start the selected Agent ---------------------
				Class<?> agentClass = Class.forName(agentClassName);
				String startAs = agentClass.getSimpleName();
				this.jadeAgentStart(startAs, agentClassName);
				System.out.println(Language.translate("Agent gestartet") + ": '" + agentClassName + "'");
				return true;
				
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		return jadeStarted;
	}
	
	/**
	 * This method returns the JADE-Profile, which has to be used
	 * for the container-profiles.
	 * If a project is focused the specific project-configuration will
	 * be used. Otherwise the default-configuration of AgentGUI will be
	 * used.
	 * @return Profile (for Jade-Containers)
	 */
	public Profile jadeGetContainerProfile() {

		Profile jadeContainerProfile = null;
		Project currProject = Application.getProjectFocused();
		
		// --- Configure the JADE-Profile to use --------------------
		if (currProject==null) {
			// --- Take the AgentGUI-Default-Profile ----------------
			jadeContainerProfile = Application.getGlobalInfo().getJadeDefaultProfile();
			System.out.println("JADE-Profile: Use AgentGUI-defaults");
			
		} else {
			// --- Take the Profile of the current Project ----------
			jadeContainerProfile = currProject.getJadeConfiguration().getNewInstanceOfProfilImpl();	
			// --- Invoke the Profile configuration in the plug-ins -- 
			jadeContainerProfile = currProject.getPlugInsLoaded().getJadeProfile(jadeContainerProfile);
			System.out.println("JADE-Profile: Use " + currProject.getProjectName() + "-configuration" );
			
			// --- Start Download-Server for project-resources ------
			DownloadServer webServer = Application.startDownloadServer();			
			//System.out.println(Language.translate("Starte Webserver") + ": " + webServer.getHTTPAddress() );
			
			// --- If the current project has external resources ---- 
			boolean ideExecuted = Application.getGlobalInfo().AppExecutedOver().equalsIgnoreCase("IDE");
			if (currProject.getProjectResources().size()>0 || ideExecuted==true) {
				webServer.setProjectDownloadResources(currProject);
			}
			
		}		
		return jadeContainerProfile;
	}
	
	/**
	 * Shutting down the jade-platform.
	 */
	public void jadeStop() {

		// ------------------------------------------------
		// --- Starts the UtilityAgent which sends --------
		// --- a 'ShutdownPlatform()' to the AMS   --------	
		// ------------------------------------------------
		if (jadeIsMainContainerRunning()) {
			this.jadeUtilityAgentStart(UTILITY_AGENT_JOB.ShutdownPlatform);
			// --- Wait for the end of Jade ---------------
			Long timeStop = System.currentTimeMillis() + (10 * 1000);
			while(jadeIsMainContainerRunning()) {
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

		// --- Stop Download-Server -----------------------
		Application.stopDownloadServer();
		
		// --- Reset runtime-variables -------------------- 
		jadeContainerRemote.removeAllElements();
		jadeContainerLocal.removeAllElements();
		jadeMainContainer = null;
		jadeRuntime = null;
		
		Application.setStatusJadeRunning(false);
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().setSimulationReady2Start();
		}
		
	}
	
	/**
	 * Asks the user to shutdown Jade.
	 * @return true, if the user answered 'yes'
	 */
	public boolean jadeStopAskUserBefore() {
		
		if(this.jadeIsMainContainerRunning()==true && Application.getMainWindow()!=null) {
			String MsgHead = Language.translate("JADE wird zur Zeit ausgeführt!");
			String MsgText = Language.translate("Möchten Sie JADE nun beenden?");
			Integer MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.getMainWindow().getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return false; // --- NO,just exit 
			// --- Stop the JADE-Platform -------------------
			this.jadeStop();
		}
		return true;
	}
	
	/**
	 * Checks, whether the main-container (Jade himself) is running or not.
	 *
	 * @param forceJadeStart will force the jade start, if JADE is not running
	 * @return true, if the MainContainer is running
	 */
	public boolean jadeMainContainerIsRunning(boolean forceJadeStart) {
		if ( forceJadeStart == true ) {
			jadeSystemAgentOpen("rma", null);
		}		
		return jadeIsMainContainerRunning();
	}
	
	/**
	 * Jade main container is running.
	 *
	 * @return true, if the Main-Container is running
	 */
	public boolean jadeIsMainContainerRunning () {
		boolean isRunning;		
		try {
			jadeMainContainer.getState();
			isRunning = true;
		}
		catch (Exception eMC) {
			isRunning = false; //	eMC.printStackTrace();	
			jadeMainContainer = null;
			try {
				jadeRuntime.shutDown();				
			} catch (Exception eRT ) { 
				//eRT.printStackTrace();				
			}			
			jadeRuntime = null;	
		}
		return isRunning;
	}
	
	
	/**
	 * Starts the Utility-Agent with a job defined in its start argument.
	 *
	 * @param utilityAgentJob the job for the utility UtilityAgent to do
	 * @see UTILITY_AGENT_JOB
	 * @see UtilityAgent
	 */
	public void jadeUtilityAgentStart(UTILITY_AGENT_JOB utilityAgentJob) {
		Object[] agentArgs = new Object[1];
		agentArgs[0] = utilityAgentJob;
		jadeAgentStart("utility", agentgui.core.agents.UtilityAgent.class.getName(), agentArgs);
	}
	
	/**
	 * Starts an Agent, if the main-container exists.
	 *
	 * @param rootAgentName the root agent name
	 * @param optionalSuffixNo the optional postfix no
	 */
	public void jadeSystemAgentOpen(String rootAgentName, Integer optionalSuffixNo) {
		this.jadeSystemAgentOpen(rootAgentName, optionalSuffixNo, null);
	}
	
	/**
	 * Starts agents, which are available by JADE or AgentGUI like the rma, sniffer etc.<br>
	 * The herein known root agent names are:<br>
	 * 'rma', 'sniffer', 'dummy', 'df', 'introspector', 'log' for JADE and
	 * 'loadmonitor' and 'simstarter' for Agent.GUI
	 * 
	 *
	 * @param rootAgentName the root agent name
	 * @param optionalSuffixNo an optional postfix no
	 * @param openArgs the open args
	 */
	public void jadeSystemAgentOpen(String rootAgentName, Integer optionalSuffixNo, Object[] openArgs) {
		// --- Table of the known Jade System-Agents -----
		Hashtable<String, String> systemAgents = new Hashtable<String, String>();
		systemAgents.put("rma", jade.tools.rma.rma.class.getName());
		systemAgents.put("sniffer", jade.tools.sniffer.Sniffer.class.getName());
		systemAgents.put("dummy", jade.tools.DummyAgent.DummyAgent.class.getName());
		systemAgents.put("df", "mas.agents.DFOpener");
		systemAgents.put("introspector", jade.tools.introspector.Introspector.class.getName());
		systemAgents.put("log", jade.tools.logging.LogManagerAgent.class.getName());

		// --- AgentGUI - Agents --------------------------
		systemAgents.put("loadmonitor", agentgui.simulationService.agents.LoadMeasureAgent.class.getName());
		systemAgents.put("simstarter", agentgui.simulationService.agents.LoadExecutionAgent.class.getName());
		
		boolean showRMA = true;
		
		AgentController agentController = null;
		String agentNameSearch  = rootAgentName.toLowerCase();
		String agentNameClass = null;
		String agentNameForStart = rootAgentName;
		
		String msgHead = null;
		String msgText = null;
		Integer msgAnswer = null;
		
		// --- For 'simstarter': is there a project? --------- 
		if (agentNameForStart.equalsIgnoreCase("simstarter")) {
			showRMA = false;
			if (Application.getProjectFocused()==null) {
				msgHead = Language.translate("Abbruch: Kein Projekt geöffnet!");
				msgText = Language.translate("Zur Zeit ist kein Agenten-Projekt geöffnet.");
				JOptionPane.showMessageDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.OK_OPTION);
				return;	
			} else {
				Application.getProjectFocused().save();
			}
		}
		// --- Setting the real name of the agent to start --- 
		if (optionalSuffixNo!=null) 
			agentNameForStart = rootAgentName + optionalSuffixNo.toString(); 
		
		// --- Was the system already started? ---------------
		if (jadeIsMainContainerRunning()==false) {
			msgHead = Language.translate("JADE wurde noch nicht gestartet!");
			msgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
			msgAnswer = JOptionPane.showInternalConfirmDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer==JOptionPane.NO_OPTION) return; // --- NO, just exit 
			// --- Start the JADE-Platform -------------------
			this.jadeStart(showRMA);
			if (agentNameForStart.equalsIgnoreCase("rma")) {
				try {
					agentController = jadeMainContainer.getAgent("rma");
				} catch (ControllerException e) {
					e.printStackTrace();
				}				
				return;
			}
		}
	
		// ---------------------------------------------------
		// --- Can a path to the agent be found? -------------   
		agentNameClass = systemAgents.get(agentNameSearch);
		if (agentNameClass==null) {
			System.err.println( "jadeSystemAgentOpen: Unbekannter System-Agent => " + rootAgentName);
			return;
		}
		
		// --- Does an agent (see name) already exists? ------
		if (jadeAgentIsRunning(agentNameForStart)==true && agentNameForStart.equalsIgnoreCase("df")==false) {
			// --- Agent already EXISTS !! -------------------
			msgHead = Language.translate("Der Agent '") + rootAgentName +  Language.translate("' ist bereits geöffnet!");
			msgText = Language.translate("Möchten Sie einen weiteren Agenten dieser Art starten?");
			if (Application.getMainWindow()==null) {
				msgAnswer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);				
			} else {
				msgAnswer = JOptionPane.showInternalConfirmDialog(Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_OPTION);	
			}
			if (msgAnswer==0) {
				// --- YES - Start another agent of this kind ---------
				jadeSystemAgentOpen(rootAgentName, newSuffixNo(rootAgentName), openArgs);
			}
			
		} else {
			// --- Agent doe's NOT EXISTS !! ---------------------
			try {
				if (agentNameForStart.equalsIgnoreCase("df")) {
					// --- Show the DF-GUI -----------------------
					this.jadeUtilityAgentStart(UTILITY_AGENT_JOB.OpernDF);
					return;					
				} else if (agentNameForStart.equalsIgnoreCase("loadMonitor") ) {
					this.jadeUtilityAgentStart(UTILITY_AGENT_JOB.OpenLoadMonitor);
					return;
				} else if (agentNameForStart.equalsIgnoreCase("simstarter")) {
					String containerName = Application.getProjectFocused().getProjectFolder();
					this.jadeAgentStart(agentNameForStart, agentNameClass, openArgs, containerName);
				} else {
					// --- Show a standard jade ToolAgent --------
					agentController = jadeMainContainer.createNewAgent(agentNameForStart, agentNameClass, openArgs);
					agentController.start();
				}
			} 
			catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Will find a new suffix number for the name of an agent.
	 *
	 * @param agentName the agent name
	 * @return the integer
	 */
	private int newSuffixNo(String agentName) {

		String newAgentName = agentName;
		Integer i = 0;
		
		while (jadeAgentIsRunning(newAgentName)==true) {
			i++;
			newAgentName = agentName + i.toString();			
		}			
		return i;
	}
	
	/**
	 * Kills an agent in the MainContainer, if it is running.
	 *
	 * @param localAgentName the agent name
	 */
	public void jadeKillAgentInMainContainer(String localAgentName) {

		AgentController agentController = null;
		if ( jadeAgentIsRunning(localAgentName) ) {
			// --- get Agent(Controller) -----
			try {
				agentController = jadeMainContainer.getAgent(localAgentName);
			}  catch (ControllerException e) {
				//  e.printStackTrace();
			}
			// --- Kill the Agent ------------			
			try {
				agentController.kill();
				
			}  catch (StaleProxyException e) {
				// e.printStackTrace();
			}
		}
	}	
	
	
	
	/**
	 * Checks, whether one Agent is running (or not) in the main-container.
	 *
	 * @param localAgentName the agent name
	 * @return true, if the agent is running
	 */
	public boolean jadeAgentIsRunning (String localAgentName) {
		return jadeAgentIsRunning(localAgentName, jadeMainContainer.getName());
	}
	
	/**
	 * Checks, whether one Agent is running (or not) in the specified container
	 *
	 * @param localAgentName the agent name
	 * @param localContainerName the local container name
	 * @return true, if the agent is running
	 */
	public boolean jadeAgentIsRunning (String localAgentName, String localContainerName) {
		
		boolean AgentiR;
		AgentContainer AgenCont = null;
		AgentController AgeCon = null;
		
		if (jadeIsMainContainerRunning() == false ) {
			return false;
		}

		// --- 0. Set to the right Container ------------------------
		AgenCont = jadeGetContainer(localContainerName);
		if ( AgenCont == null ) {
			return false;
		}
		// --- 1. try to find the agent in main-container -----------
		try {
			AgeCon = AgenCont.getAgent(localAgentName);
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
	 * Adding an AgentContainer to the local platform.
	 *
	 * @param newContainerName the container name
	 * @return the agent container
	 */
	public AgentContainer jadeContainerCreate(String newContainerName) {
		Profile pSub = this.jadeGetContainerProfile();
		pSub.setParameter(Profile.CONTAINER_NAME, newContainerName);
		AgentContainer agentContainer = jadeRuntime.createAgentContainer(pSub);
		jadeContainerLocal.add(agentContainer);
		return agentContainer;		
	}	
	
	/**
	 * Returns the Jade main container.
	 * @return the agent container
	 */
	public AgentContainer jadeGetMainContainer() {
		if (jadeIsMainContainerRunning()==false) {
			return null;
		}
		return this.jadeMainContainer;
	}
	/**
	 * Returns the Container given by it's name.
	 *
	 * @param containerNameSearch the container name search
	 * @return the agent container
	 */
	public AgentContainer jadeGetContainer(String containerNameSearch ) {
		
		AgentContainer agentContainer = null;
		String agentContainerName = null;
		
		// --- IfJADE is not already running -------------------
		if (jadeIsMainContainerRunning()==false) {
			return null;
		}
		// --- Searching for the 'Main-Container'? -------------
		if (containerNameSearch==this.jadeMainContainer.getName()) {
			return this.jadeMainContainer;
		}	
		
		// --- Get the right container ------------------------- 
		for (int i=0; i < jadeContainerLocal.size(); i++) {
			agentContainer = jadeContainerLocal.get(i);
			try {
				agentContainerName = agentContainer.getContainerName();
			} 
			catch (ControllerException ex) {
				ex.printStackTrace();
			}			
			if (agentContainerName.equalsIgnoreCase( containerNameSearch )==true) {
				break;
			}
		}		
		return agentContainer;
	}
	
	/**
	 * Kills an AgentContainer to the local platform.
	 *
	 * @param containerName the container name
	 */
	public void jadeKillContainer(String containerName) {
		AgentContainer agentContainer = null;
		agentContainer = jadeGetContainer(containerName);
		jadeContainerKill( agentContainer );
	}
	
	/**
	 * Jade container kill.
	 *
	 * @param agentContainer the agent container
	 */
	public void jadeContainerKill( AgentContainer agentContainer ) {
		
		jadeContainerLocal.remove( agentContainer );
		try {
			agentContainer.kill();
		} catch (StaleProxyException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Adding an Agent to a Container.
	 *
	 * @param newAgentName the agent name
	 * @param agentClassName the agent class name
	 */
	public void jadeAgentStart(String newAgentName, String agentClassName) {
		String MainContainerName = jadeMainContainer.getName();
		jadeAgentStart(newAgentName, agentClassName, null, MainContainerName) ;
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param newAgentName the agent name
	 * @param agentClassName the agent class name
	 * @param inContainer the container name
	 */
	public void jadeAgentStart(String newAgentName, String agentClassName, String inContainer ) {
		jadeAgentStart(newAgentName, agentClassName, null, inContainer) ;
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param newAgentName the agent name
	 * @param agentClassName the agent class name
	 * @param startArguments the start arguments for the agent
	 */
	public void jadeAgentStart(String newAgentName, String agentClassName, Object[] startArguments ) {
		String MainContainerName = jadeMainContainer.getName();
		jadeAgentStart(newAgentName, agentClassName, startArguments, MainContainerName);
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param newAgentName the agent name
	 * @param agentClassName the agent class name
	 * @param startArguments the start arguments for the agent
	 * @param inContainer the container name
	 */
	public void jadeAgentStart(String newAgentName, String agentClassName, Object[] startArguments, String inContainer ) {
		
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Agent> clazz = (Class<? extends Agent>) Class.forName(agentClassName);
			jadeAgentStart(newAgentName, clazz, startArguments, inContainer );

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param newAgentName the agent name
	 * @param clazz the class of the agent
	 * @param startArguments the start arguments for the agent
	 * @param inContainer the container name
	 */
	public void jadeAgentStart(String newAgentName, Class<? extends Agent> clazz, Object[] startArguments, String inContainer ) {
		
		int MsgAnswer;
		String msgHead, msgText;
		AgentController agentController;
		AgentContainer agentContainer;

		// --- Was the system already started? ---------------
		if ( jadeIsMainContainerRunning() == false ) {
			msgHead = Language.translate("JADE wurde noch nicht gestartet!");
			msgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
			MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return; // --- NO,just exit 
			// --- Start the JADE-Platform -------------------
			jadeStart();			
		}
		
		agentContainer = this.jadeGetContainer( inContainer );
		if (agentContainer == null) {
			agentContainer = jadeContainerCreate( inContainer );
		}		
		try {
			Agent agent = (Agent) clazz.newInstance();
			agent.setArguments(startArguments);
//			AgentCont = AgeCont.createNewAgent( AgentName, clazz, AgentArgs );
			agentController = agentContainer.acceptNewAgent(newAgentName, agent);
			agentController.start();

		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
	}
	
	
}// -- End Class ---
