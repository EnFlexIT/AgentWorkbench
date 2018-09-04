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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.config.DeviceAgentDescription;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.plugin.PlugInsLoaded;
import agentgui.core.project.Project;
import agentgui.core.utillity.UtilityAgent;
import agentgui.core.utillity.UtilityAgent.UtilityAgentJob;
import agentgui.logging.DebugService;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.load.LoadMeasureThread;
import de.enflexit.common.transfer.RecursiveFolderDeleter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * This class manages the interaction between Agent.GUI and JADE.
 * It provides methods to start / stop JADE, as well as methods
 * for starting new container or agents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Platform {

	public static final String BackgroundSystemAgentApplication = "server.client";
	public static final String BackgroundSystemAgentServerMaster = "server.master";
	public static final String BackgroundSystemAgentServerSlave = "server.slave";
	public static final String BackgroundSystemAgentFileManger = "file.manager";
	
	private AgentContainer jadeMainContainer;
	private final String mainContainerName = jade.core.AgentContainer.MAIN_CONTAINER_NAME;
	private ArrayList<AgentContainer> agentContainerList;
	
	private Project fileMangerProject;
	
	/**
	 * Constructor of this class.
	 */
	public Platform() {

	}	
	
	/**
	 * Starts JADE without displaying the RMA
	 * @return true, if successful
	 */		
	public boolean start() {
		return start(false, null);
	}	
	/**
	 * Starts JADE.
	 * @param showRMA set true, if you want also to start the RMA agent and its visualization 
	 * @return true, if successful
	 */
	public boolean start(boolean showRMA) {
		return start(showRMA, null);
	}
	/**
	 * Starts JADE.
	 * @param showRMA set true, if you want also to start the RMA agent and its visualization 
	 * @param containerProfile the actual container Profile
	 * @return true, if successful
	 */
	public boolean start(boolean showRMA, Profile containerProfile) {
		
		boolean startSucceed = false;		
		if (this.isMainContainerRunning()==true) {
			startSucceed = true;
			
		} else {
			
			try {
				// --------------------------------------------------
				// --- In case of execution as Service, check -------
				// --- Master-URL and maybe delay the JADE start ----
				// --------------------------------------------------
				this.delayHeadlessServerStartByCheckingMasterURL();
				// --- Notify plugins for agent Start --------------- 
				this.notifyPluginsForStartMAS();
				// --- Check for valid plugin preconditions --------- 
				if (this.hasValidPreconditionsInPlugins()==false)  return false;
				// --- Start Platform -------------------------------
				Runtime jadeRuntime = Runtime.instance();	
				jadeRuntime.invokeOnTermination(new Runnable() {
					public void run() {
						// --- terminate platform -------------------
						LoadMeasureThread.removeMonitoringTasksForAgents();
						Platform.this.jadeMainContainer = null;
						Platform.this.getAgentContainerList().clear();
						Application.setStatusJadeRunning(false);
						if (Application.getMainWindow()!=null){
							Application.getMainWindow().setSimulationReady2Start();
						}
						// --- Notify plugins for termination -------
						Platform.this.notifyPluginsForTerminatedMAS();
					}
				});
				
				// --- Set and check the container profile ----------
				Profile profile = containerProfile;
				if (profile==null) {
					profile = this.getContainerProfile();
				}
				
				// --- Start MainContainer --------------------------
				this.jadeMainContainer = jadeRuntime.createMainContainer(profile);
				if (this.jadeMainContainer!=null) {
					startSucceed = true;
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		// --- Start the Application Background-Agents ---------------
		if (startSucceed==true) {
			if (this.startBackgroundAgents(showRMA)==false) return false;
			Application.setStatusJadeRunning(true);
		}
		return startSucceed;
	}
	
	/**
	 * This Method will start - depending on the Configuration - the
	 * programs-background-agents.
	 * It starts directly after starting the JADE-Platform
	 *
	 * @param showRMA specifies if the rma should appear or not
	 * @return true, if successful
	 */
	private boolean startBackgroundAgents(boolean showRMA) {
		
		// ----------------------------------------------------------
		// --- Differentiation of the Application-Case --------------
		// ----------------------------------------------------------
		String newLine = Application.getGlobalInfo().getNewLineSeparator();
		String applicationTitle = Application.getGlobalInfo().getApplicationTitle();
		String executionModeDescription = Application.getGlobalInfo().getExecutionModeDescription();
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			// --- Start "server.client" agent ----------------------
			if (this.isAgentRunningInMainContainer(BackgroundSystemAgentApplication)==false) {
				this.startAgent(BackgroundSystemAgentApplication, agentgui.simulationService.agents.ServerClientAgent.class.getName());	
			}
			
			// --- Start "file.manager" agent -----------------------
			this.startFileMangerAgent();
			
			// --- Start RMA ('Remote Monitoring Agent') -----------
			if (showRMA==true) {
				this.startSystemAgent("rma", null);	
			}
			break;
		
		case SERVER_MASTER:
			// ------------------------------------------------------
			// --- This is a Master-Server-Platform -----------------
			// ------------------------------------------------------
			// --- Connecting to Database ---------------------------
			if (Application.getDatabaseConnection(true).hasErrors()==true ) {
				
				this.stop();
				
				String msgHead = "";
				String msgText = "";
				
				msgHead += Language.translate("Konfiguration des") + " " + applicationTitle + "-" +  executionModeDescription;
				msgText += "Die Systemkonfiguration enthält keine gültigen Angaben über den" + newLine +
						   "Datenbankserver. Der Start von JADE wird deshalb unterbrochen." + newLine +
						   "Bitte konfigurieren Sie einen MySQL-Datenbank-Server und" + newLine +
						   "starten Sie den Server-Master anschließend erneut.";
				msgText = Language.translate(msgText);

				if (Application.isOperatingHeadless()==true) {
					System.err.println("=> " + msgHead + " <=");
					System.err.println(msgText);
				} else {
					String msgQuestion = Language.translate("Möchten Sie die Konfiguration nun vornehmen?");
					msgText += newLine + newLine + msgQuestion; 
					int answer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						Application.showOptionDialog();
					}	
				}
				return false;
				
			}
			// --- Start 'server.master' agent ----------------------		
			if (isAgentRunningInMainContainer(BackgroundSystemAgentServerMaster)==false) {
				this.startAgent(BackgroundSystemAgentServerMaster, agentgui.simulationService.agents.ServerMasterAgent.class.getName());	
			}
			break;
			
		case SERVER_SLAVE:
			// ------------------------------------------------------
			// --- This is a Slave-Server-Platform ------------------
			// ------------------------------------------------------
			if (Application.getGlobalInfo().getServerMasterURL()==null ||
				Application.getGlobalInfo().getServerMasterURL().equalsIgnoreCase("")==true ||
				Application.getGlobalInfo().getServerMasterPort().equals(0)==true ||
				Application.getGlobalInfo().getServerMasterPort4MTP().equals(0)==true ||
				Application.getGlobalInfo().getJadeUrlConfigurationForMaster().hasErrors()==true) {
				
				this.stop();
				
				String msgHead = "";
				String msgText = "";
				
				msgHead += Language.translate("Konfiguration des") + " " + applicationTitle + "-" +  executionModeDescription;
				msgText += "Die Systemkonfiguration enthält keine gültigen Angaben über den" + newLine +
						   "Hauptserver. Der Start von JADE wird deshalb unterbrochen." + newLine +
						   "Bitte konfigurieren Sie eine gültige Server-URL oder IP (inkl. Port)" + newLine +
						   "und starten Sie den Dienst anschließend erneut.";
				msgText = Language.translate(msgText);
				
				if (Application.isOperatingHeadless()==true) {
					System.err.println("=> " + msgHead + " <=");
					System.err.println(msgText);
				} else {
					String msgQuestion = Language.translate("Möchten Sie die Konfiguration nun vornehmen?");
					msgText += newLine + newLine + msgQuestion;
					int answer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						Application.showOptionDialog();
					}	
				}
				return false;
				
			} 
			// --- Start 'server.slave' agent -----------------------
			if (isAgentRunningInMainContainer(BackgroundSystemAgentServerSlave)==false) {			
				this.startAgent(BackgroundSystemAgentServerSlave, agentgui.simulationService.agents.ServerSlaveAgent.class.getName());
			}
			break;
		
		case DEVICE_SYSTEM:
			// ------------------------------------------------------
			// --- Run as Service / Embedded System Agent -----------
			// ------------------------------------------------------
			
			// --- Make sure that the project was loaded ------------
			while (Application.getProjectFocused()==null) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case SETUP:
				if (isAgentRunningInMainContainer(BackgroundSystemAgentApplication)==false) {
					startAgent(BackgroundSystemAgentApplication, agentgui.simulationService.agents.ServerClientAgent.class.getName());	
				}
				// --- Start "file.manager" agent -----------------------
				if (this.fileMangerProject!=null) {
					this.startFileMangerAgent();
				}
				break;

			case AGENT:
				// --- nothing to do here yet ---
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
	 * Checks, if the preconditions in the {@link PlugInsLoaded} of the {@link Project} are fulfilled.
	 * @return true, if the preconditions are fulfilled 
	 */
	private boolean hasValidPreconditionsInPlugins() {
		boolean hasValidPreconditions = true;
		Project currProject = Application.getProjectFocused();
		if (currProject!=null) {
			hasValidPreconditions = currProject.getPlugInsLoaded().haveValidPreconditions();	
		}
		return hasValidPreconditions;
	}
	/**
	 * Notifies all loaded plugins for the upcoming agent start.
	 */
	private void notifyPluginsForStartMAS() {
		Project currProject = Application.getProjectFocused();
		if (currProject!=null) {
			currProject.getPlugInsLoaded().notifyPluginsForStartMAS();	
		}
	}
	/**
	 * Notifies all project plugins for agent termination.
	 */
	private void notifyPluginsForTerminatedMAS() {
		Project currProject = Application.getProjectFocused();
		if (currProject!=null) {
			currProject.getPlugInsLoaded().notifyPluginsForTerminatedMAS();	
		}
	}
	
	/**
	 * [Trial] Delays the server start by checking the master URL. Using Agent.GUI as
	 * a Service on Linux systems caused an {@link UnknownHostException} that
	 * disappeared when Agemt.GUI service was restarted. So maybe this delay can help
	 * to solve the problem of the name resolution. 
	 * Checking time is set to 30 seconds. After that the system will continue with 
	 * regular JADE start. This method operates only in case of a headless Server execution.
	 * 
	 * @see Application#isRunningAsServer()
	 * @see Application#isOperatingHeadless()
	 * @see GlobalInfo#getServerMasterURL()
	 */
	private void delayHeadlessServerStartByCheckingMasterURL() {
		
		if (Application.isOperatingHeadless()==true && Application.isRunningAsServer()) {
			
			InetAddress inetAddr = null;
			String masterURL = Application.getGlobalInfo().getServerMasterURL();
			
			long timeDelayStop = System.currentTimeMillis() + 30 * 1000; 
			while (System.currentTimeMillis()<timeDelayStop) {

				// --- Do the URL check -----------------------
				try {
					inetAddr = InetAddress.getByName(masterURL);
					
				} catch (UnknownHostException uhe) {
					System.out.println("UnknownHostException for '" + masterURL + "' => Delaying JADE start ...");
//					uhe.printStackTrace();
				}
				
				// --- Exit if URL could be resolved ----------
				if (inetAddr!=null) return;
				
				// --- Retry in the next second ---------------
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			} // --- end while ---
		}
	}
	
	/**
	 * Start JADE for a globally specified embedded system agent.
	 * @return true, if the start was successful for every agent
	 */
	public boolean start4EmbeddedSystemAgent() {
		
		Vector<DeviceAgentDescription> agents = Application.getGlobalInfo().getDeviceServiceAgents();
		if (agents.size()==0) return false;

		// --- Question is, was everything successful? -------
		boolean success = true; 
		for (int i = 0; i < agents.size(); i++) {
			DeviceAgentDescription dad = agents.get(i);
			boolean partSuccess = this.start4EmbeddedSystemAgent(dad.getAgentName(), dad.getAgentClass());
			if (success==true) {
				success = partSuccess;
			}
		}
		return success;
	}
	/**
	 * Start JADE for a specified embedded system agent.
	 *
	 * @param agentName the agents name
	 * @param agentClassName the agent class name
	 * @return true, if successful
	 */
	public boolean start4EmbeddedSystemAgent(String agentName, String agentClassName) {
		
		boolean jadeStarted = false;
		
		// --- Remove the Agent.GUI services ------------------------
		Profile jadeProfile = this.getContainerProfile();

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
		if (this.start(false, jadeProfile)==true) {
			try {
				// --- Start the selected Agent ---------------------
				Class<?> agentClass = ClassLoadServiceUtility.forName(agentClassName);
				String startAs = null;
				if (agentName==null || agentName.equals("")==true) {
					startAs = agentClass.getSimpleName();
				} else {
					startAs = agentName;
				}
				this.startAgent(startAs, agentClassName);
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
	public ProfileImpl getContainerProfile() {

		ProfileImpl jadeContainerProfile = null;
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
			
			// --- If the current project has external resources ---- 
			boolean hasBundelJars = currProject.getProjectBundleLoader().getBundleJarsListModel().size()>0;
			boolean hasRegularJars = currProject.getProjectBundleLoader().getRegularJarsListModel().size()>0;
			boolean ideExecuted = Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE;
			if (hasBundelJars==true || hasRegularJars==true || ideExecuted==true) {
				// --- Set marker to start the FileManagerAgent ---------
				this.fileMangerProject = currProject;
			}
			
		}		
		return jadeContainerProfile;
	}
	
	/**
	 * Shutting down the jade-platform.
	 */
	public void stop() {

		// ------------------------------------------------
		// --- Starts the UtilityAgent which sends --------
		// --- a 'ShutdownPlatform()' to the AMS   --------	
		// ------------------------------------------------
		if (isMainContainerRunning()) {
			this.startUtilityAgent(UtilityAgentJob.ShutdownPlatform);
			// --- Wait for the end of Jade ---------------
			Long timeStop = System.currentTimeMillis() + (10 * 1000);
			while(isMainContainerRunning()) {
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
			
			
			// --- Clean up the memory ------------
			System.gc();
		}
		// ------------------------------------------------

		// --- Stop Download-Server -----------------------
		this.removeFileTransferDirectories();
		
		// --- Reset runtime-variables -------------------- 
		this.getAgentContainerList().clear();
		this.jadeMainContainer = null;
		
		Application.setStatusJadeRunning(false);
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().setSimulationReady2Start();
		}
		
	}
	
	/**
	 * Asks the user to shutdown Jade.
	 * @return true, if the user answered 'yes'
	 */
	public boolean stopAskUserBefore() {
		
		if (this.isMainContainerRunning()==true && Application.getMainWindow()!=null) {
			String title = Language.translate("JADE wird zur Zeit ausgeführt!");
			String message = Language.translate("Möchten Sie JADE nun beenden?");
			Integer answer =  JOptionPane.showConfirmDialog(Application.getMainWindow().getContentPane(), message, title, JOptionPane.YES_NO_OPTION);
			if (answer==1) return false; // --- NO,just exit 
			// --- Stop the JADE-Platform -------------------
			this.stop();
		}
		return true;
	}
	
	/**
	 * Checks, whether the main-container (Jade himself) is running or not.
	 *
	 * @param forceJadeStart will force the jade start, if JADE is not running
	 * @return true, if the MainContainer is running
	 */
	public boolean isMainContainerRunning(boolean forceJadeStart) {
		if (forceJadeStart==true) {
			this.startSystemAgent("rma", null);
		}		
		return this.isMainContainerRunning();
	}
	
	/**
	 * Jade main container is running.
	 *
	 * @return true, if the Main-Container is running
	 */
	public boolean isMainContainerRunning () {
		boolean isRunning;		
		try {
			jadeMainContainer.getState();
			isRunning = true;
			
		} catch (Exception eMC) {
			isRunning = false; //	eMC.printStackTrace();	
			jadeMainContainer = null;
			try {
				Runtime.instance().shutDown();				
			} catch (Exception ex) { 
				//ex.printStackTrace();				
			}			
		}
		return isRunning;
	}
	
	
	/**
	 * Starts the Utility-Agent with a job defined in its start argument.
	 *
	 * @param utilityAgentJob the job for the utility UtilityAgent to do
	 * @see UtilityAgentJob
	 * @see UtilityAgent
	 */
	public void startUtilityAgent(UtilityAgentJob utilityAgentJob) {
		Object[] agentArgs = new Object[1];
		agentArgs[0] = utilityAgentJob;
		this.startUtilityAgent(agentArgs);
	}
	/**
	 * Starts the Utility-Agent with the specified start arguments.
	 * @param utilityAgentStartArguments the utility agent start arguments
	 */
	public void startUtilityAgent(Object[] utilityAgentStartArguments) {
		startAgent("utility", agentgui.core.utillity.UtilityAgent.class.getName(), utilityAgentStartArguments);
	}
	
	
	/**
	 * Starts an Agent, if the main-container exists.
	 *
	 * @param rootAgentName the root agent name
	 * @param optionalSuffixNo the optional postfix no
	 */
	public void startSystemAgent(String rootAgentName, Integer optionalSuffixNo) {
		this.startSystemAgent(rootAgentName, optionalSuffixNo, null);
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
	public void startSystemAgent(String rootAgentName, Integer optionalSuffixNo, Object[] openArgs) {
		
		// --- Table of the known Jade System-Agents ----------------
		Hashtable<String, String> systemAgents = new Hashtable<String, String>();
		systemAgents.put("rma", jade.tools.rma.rma.class.getName());
		systemAgents.put("sniffer", jade.tools.sniffer.Sniffer.class.getName());
		systemAgents.put("dummy", jade.tools.DummyAgent.DummyAgent.class.getName());
		systemAgents.put("df", "mas.agents.DFOpener");
		systemAgents.put("introspector", jade.tools.introspector.Introspector.class.getName());
		systemAgents.put("log", jade.tools.logging.LogManagerAgent.class.getName());

		// --- AgentGUI - Agents ------------------------------------
		systemAgents.put("loadmonitor", agentgui.simulationService.agents.LoadMeasureAgent.class.getName());
		systemAgents.put("threadmonitor", agentgui.simulationService.agents.LoadMeasureAgent.class.getName());
		systemAgents.put("simstarter", agentgui.simulationService.agents.LoadExecutionAgent.class.getName());
		
		boolean showRMA = true;
		
		AgentController agentController = null;
		String agentNameSearch  = rootAgentName.toLowerCase();
		String agentClassName = null;
		String agentNameForStart = rootAgentName;
		
		// --- For 'simstarter': is there a project? ----------------
		if (agentNameForStart.equalsIgnoreCase("simstarter")) {
			showRMA = false;
			if (Application.getProjectFocused()==null) {
				String msgHead = Language.translate("Abbruch: Kein Projekt geöffnet!");
				String msgText = Language.translate("Zur Zeit ist kein Agenten-Projekt geöffnet.");
				JOptionPane.showMessageDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.OK_OPTION);
				return;	
			} else {
				Application.getProjectFocused().save();
			}
		}
		
		// --- Setting the real name of the agent to start ----------
		if (optionalSuffixNo!=null) {
			agentNameForStart = rootAgentName + optionalSuffixNo.toString(); 
		}
		
		// --- Was the system already started? ----------------------
		if (this.isMainContainerRunning()==false) {
			
			boolean isSkipUserRequest = false;
			Project currProject = Application.getProjectFocused(); 
			if (currProject!=null) {
				isSkipUserRequest = Application.getProjectFocused().getJadeConfiguration().isSkipUserRequestForJadeStart();
			}
			
			if (isSkipUserRequest==false) {
				// --- Ask user to start JADE or not ----------------
				String msgHead = Language.translate("JADE wurde noch nicht gestartet!");
				String msgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
				String msgCheckBox = Language.translate("Beim nächsten mal direkt starten und nicht erneut nachfragen.");
				JCheckBox jCheckBoxDoNotAskAgain = new JCheckBox(msgCheckBox);
				
				// --- Configure dialog content ---------------------
				Object[] dialogContent = null;
				if (currProject==null) {
					dialogContent = new Object[1];
					dialogContent[0] = msgText;
				} else {
					dialogContent = new Object[2];
					dialogContent[0] = msgText + "\n\n";
					dialogContent[1] = jCheckBoxDoNotAskAgain;
				}
				 
				int msgAnswer = JOptionPane.showConfirmDialog(Application.getMainWindow(), dialogContent, msgHead, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (msgAnswer==JOptionPane.NO_OPTION) return; // --- NO, just exit
				// --- Remind CheckBox value if selected ------------
				if (currProject!=null && jCheckBoxDoNotAskAgain.isSelected()==true) {
					currProject.getJadeConfiguration().setSkipUserRequestForJadeStart(true);
				}
			}
			
			// --- Start the JADE-Platform --------------------------
			if(this.start(showRMA) == false){
				// --- Abort if the jade platform was not started -----------
				return;
			}
			
			if (agentNameForStart.equalsIgnoreCase("rma")) {
				try {
					agentController = jadeMainContainer.getAgent("rma");
				} catch (ControllerException e) {
					e.printStackTrace();
				}				
				return;
			}
		}
	
		// ----------------------------------------------------------
		// --- Can a path to the agent be found? --------------------   
		agentClassName = systemAgents.get(agentNameSearch);
		if (agentClassName==null) {
			System.err.println( "jadeSystemAgentOpen: Unknown System-Agent => " + rootAgentName);
			return;
		}
		
		// --- Does an agent (see name) already exists? -------------
		if (isAgentRunningInMainContainer(agentNameForStart)==true && agentNameForStart.equalsIgnoreCase("df")==false) {
			// --- Agent already EXISTS !! --------------------------
			int msgAnswer;
			String msgHead = Language.translate("Der Agent '") + rootAgentName +  Language.translate("' ist bereits geöffnet!");
			String msgText = Language.translate("Möchten Sie einen weiteren Agenten dieser Art starten?");
			if (Application.getMainWindow()==null) {
				msgAnswer = JOptionPane.showConfirmDialog(null, msgText, msgHead, JOptionPane.YES_NO_OPTION);				
			} else {
				msgAnswer = JOptionPane.showConfirmDialog(Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_OPTION);	
			}
			if (msgAnswer==0) {
				// --- YES - Start another agent of this kind --------
				startSystemAgent(rootAgentName, getSuffixNoForAgentName(rootAgentName), openArgs);
			}
			
		} else {
			// --- Agent doe's NOT EXISTS !! ------------------------
			try {
				if (agentNameForStart.equalsIgnoreCase("df")) {
					// --- Show the DF-GUI --------------------------
					this.startUtilityAgent(UtilityAgentJob.OpernDF);
					return;					
				} else if (agentNameForStart.equalsIgnoreCase("loadMonitor") ) {
					this.startUtilityAgent(UtilityAgentJob.OpenLoadMonitor);
					return;
				} else if (agentNameForStart.equalsIgnoreCase("threadMonitor") ) {
					this.startUtilityAgent(UtilityAgentJob.OpenThreadMonitor);
					return;
				} else if (agentNameForStart.equalsIgnoreCase("simstarter")) {
					String containerName = Application.getProjectFocused().getProjectFolder();
					this.startAgent(agentNameForStart, agentClassName, openArgs, containerName);
				} else {
					// --- Show a standard jade ToolAgent -----------
					agentController = jadeMainContainer.createNewAgent(agentNameForStart, agentClassName, openArgs);
					agentController.start();
				}
				
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Starts the file manger agent that provides the required project resources for a 
	 * distributed execution in different container (requires a running background system).
	 */
	private void startFileMangerAgent() {
		
		if (this.fileMangerProject==null) return;
		if (this.isAgentRunningInMainContainer(BackgroundSystemAgentFileManger)==true) return;
		
		// --- Move required resources to server directory -- 
		Object[] fileMangerArguments = new Object[1];
		String fileMangerPath = Application.getGlobalInfo().getFileManagerServerPath(true);
		String messageSuccess = "[" + this.fileMangerProject.getProjectName() + "] Project resources for remote container execution successfully prepared!";;
		String messageFailure = "[" + this.fileMangerProject.getProjectName() + "] Provisioning of project resources for remote container execution failed!";
		fileMangerArguments[0] = this.fileMangerProject.exportProjectRessurcesToDestinationDirectory(fileMangerPath, messageSuccess, messageFailure);
		// --- Start the file manager agent -----------------
		if (fileMangerArguments[0]!=null) {
			this.startAgent(BackgroundSystemAgentFileManger, jade.misc.FileManagerAgent.class.getName(), fileMangerArguments);	
		}
	}
	
	/**
	 * Returns a new suffix number for the specified name of an agent.
	 *
	 * @param agentName the agent name
	 * @return the integer
	 */
	public int getSuffixNoForAgentName(String agentName) {

		String newAgentName = agentName;
		Integer i = 0;
		
		while (this.isAgentRunningInMainContainer(newAgentName)==true) {
			i++;
			newAgentName = agentName + i.toString();			
		}			
		return i;
	}
	
	/**
	 * Kills an agent in the MainContainer, if it is running.
	 * @param localAgentName the agent name
	 */
	public void killAgentInMainContainer(String localAgentName) {

		AgentController agentController = null;
		if (isAgentRunningInMainContainer(localAgentName)) {
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
	public boolean isAgentRunningInMainContainer(String localAgentName) {
		return isAgentRunning(localAgentName, this.jadeMainContainer);
	}
	
	/**
	 * Checks, if the specified Agent is running (or not).
	 *
	 * @param localAgentName the agent name
	 * @return true, if the agent is running
	 */
	public boolean isAgentRunning(String localAgentName) {
		// --- Check if agent is running in the main container ------
		if (this.isAgentRunningInMainContainer(localAgentName)) return true;
		// --- Check if agent is running in other container ---------
		for (AgentContainer agentContainer : this.getAgentContainerList()) {
			if (this.isAgentRunning(localAgentName, agentContainer)) return true;
		}
		return false;
	}
	
	/**
	 * Checks, whether one Agent is running (or not) in the specified container
	 *
	 * @param localAgentName the agent name
	 * @param localContainerName the local container name
	 * @return true, if the agent is running
	 */
	public boolean isAgentRunning(String localAgentName, String localContainerName) {
		
		if (this.isMainContainerRunning()==false) return false;
		if (localAgentName==null) return false;
		if (localContainerName==null) return false;
		
		return this.isAgentRunning(localAgentName, this.getContainer(localContainerName));
	}
	
	/**
	 * Checks, whether one Agent is running (or not) in the specified container.
	 *
	 * @param localAgentName the agent name
	 * @param agentContainer the agent container
	 * @return true, if the agent is running
	 */
	public boolean isAgentRunning(String localAgentName, AgentContainer agentContainer) {
		
		if (this.isMainContainerRunning()==false) return false;
		if (localAgentName==null) return false;
		if (agentContainer==null) return false;
		
		// --- 1. Try to find the agent in the container ------------
		AgentController agentController = null;
		try {
			agentController = agentContainer.getAgent(localAgentName);
		
		} catch (ControllerException ce) {
			// ce.printStackTrace();
			return false; 				
		}
		
		// --- 2. Try to get the agent's state ----------------------
		boolean isRunning;
		try {
			agentController.getState();
			isRunning = true;
		
		} catch (StaleProxyException spex) {
			// spex.printStackTrace();
			isRunning = false; 			
		}		
		return isRunning;
	}	
	
	/**
	 * Returns the Jade main container.
	 * @return the agent container
	 */
	public AgentContainer getMainContainer() {
		if (this.isMainContainerRunning()==false) {
			return null;
		}
		return this.jadeMainContainer;
	}
	/**
	 * Adding an AgentContainer to the local platform.
	 *
	 * @param newContainerName the container name
	 * @return the agent container
	 */
	public AgentContainer createAgentContainer(String newContainerName) {
		ProfileImpl pSub = this.getContainerProfile();
		pSub.setParameter(Profile.CONTAINER_NAME, newContainerName);
		pSub.setParameter(Profile.MAIN, (new Boolean(false)).toString());
		pSub.setParameter(Profile.MTPS, null);
		AgentContainer agentContainer = Runtime.instance().createAgentContainer(pSub);
		this.getAgentContainerList().add(agentContainer);
		return agentContainer;		
	}
	
	/**
	 * Returns the List of local agent container.
	 * @return the agent container local
	 */
	public ArrayList<AgentContainer> getAgentContainerList() {
		if (agentContainerList==null) {
			agentContainerList = new ArrayList<>();
		}
		return agentContainerList;
	}
	/**
	 * Returns the {@link AgentContainer} given by it's name.
	 *
	 * @param containerNameSearch the container name search
	 * @return the agent container
	 */
	public AgentContainer getContainer(String containerNameSearch ) {
		
		// --- If JADE is not already running -------------------
		if (this.isMainContainerRunning()==false) {
			return null;
		}
		// --- Searching for the 'Main-Container'? -------------
		if (containerNameSearch.equals(this.mainContainerName)==true) {
			return this.jadeMainContainer;
		}	
		
		// --- Get the right container ------------------------- 
		for (int i=0; i < this.getAgentContainerList().size(); i++) {
			
			AgentContainer agentContainer = this.getAgentContainerList().get(i);
			try {
				String acName = agentContainer.getContainerName();
				if (acName!=null && acName.equalsIgnoreCase(containerNameSearch)==true) {
					return agentContainer;
				}
			
			} catch (ControllerException ex) {
				ex.printStackTrace();
			}
		}		
		return null;
	}
	
	/**
	 * Kills an AgentContainer to the local platform.
	 * @param containerName the container name
	 */
	public void killContainer(String containerName) {
		AgentContainer agentContainer = this.getContainer(containerName);
		if (agentContainer!=null) {
			try {
				this.getAgentContainerList().remove(agentContainer);
				agentContainer.kill();
			} catch (StaleProxyException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adding an Agent to a Container.
	 *
	 * @param agentName the agent name
	 * @param agentClassName the agent class name
	 */
	public void startAgent(String agentName, String agentClassName) {
		this.startAgent(agentName, agentClassName, null, this.mainContainerName) ;
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param agentName the agent name
	 * @param agentClassName the agent class name
	 * @param inContainer the container name
	 */
	public void startAgent(String agentName, String agentClassName, String inContainer ) {
		this.startAgent(agentName, agentClassName, null, inContainer) ;
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param agentName the agent name
	 * @param agentClassName the agent class name
	 * @param startArguments the start arguments for the agent
	 */
	public void startAgent(String agentName, String agentClassName, Object[] startArguments ) {
		this.startAgent(agentName, agentClassName, startArguments, this.mainContainerName);
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param agentName the agent name
	 * @param agentClassName the agent class name
	 * @param startArguments the start arguments for the agent
	 * @param inContainer the container name
	 */
	public void startAgent(String agentName, String agentClassName, Object[] startArguments, String inContainer ) {
		try {
			Class<? extends Agent> clazz = ClassLoadServiceUtility.getAgentClass(agentClassName);
			this.startAgent(agentName, clazz, startArguments, inContainer);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts an agent as specified
	 *
	 * @param agentName the agent name
	 * @param agentClass the class of the agent
	 * @param startArguments the start arguments for the agent
	 * @param inContainer the container name
	 */
	public void startAgent(String agentName, Class<? extends Agent> agentClass, Object[] startArguments, String inContainer) {
		
		// --- Was the system already started? ----------------------
		if (this.isMainContainerRunning()==false) {
			String msgHead = Language.translate("JADE wurde noch nicht gestartet!");
			String msgText = Language.translate("Möchten Sie JADE nun starten und fortfahren?");
			int msgAnswer = JOptionPane.showConfirmDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer==JOptionPane.NO_OPTION) return; // --- NO,just exit 
			
			// --- Start the JADE-Platform -------------------------------
			if (this.start() == false) {
				// --- Abort if JADE was not successfully started --------
				return;
			}
		}
		
		// --- Get the AgentContainer -------------------------------
		AgentContainer agentContainer = this.getContainer(inContainer);
		if (agentContainer==null) {
			// --- Do we have a remote container of that name? ------
			if (this.startRemoteAgent(agentName, agentClass, startArguments, inContainer)==true) return;
			// --- Start a new local container ----------------------
			agentContainer = this.createAgentContainer(inContainer);
		}
		
		// --- Check if the agent name is already used --------------
		Integer newAgentNoTmp = 0;
		String newAgentNameTmp = agentName;
		try {
			agentContainer.getAgent(newAgentNameTmp, AID.ISLOCALNAME);
			while (true) {
				newAgentNoTmp++;
				newAgentNameTmp = agentName + "-" + newAgentNoTmp;
				agentContainer.getAgent(newAgentNameTmp, AID.ISLOCALNAME);
			}
			
		} catch (ControllerException ce) {
			//ce.printStackTrace();
			if (newAgentNoTmp>0) {
				agentName = agentName + "-" + newAgentNoTmp;
			}
		}
		
		// --- Start the actual agent -------------------------------
		try {
			Agent agent = (Agent) ClassLoadServiceUtility.newInstance(agentClass.getName());
			agent.setArguments(startArguments);
			AgentController agentController = agentContainer.acceptNewAgent(agentName, agent);
			agentController.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	/**
	 * Tries to start an agent on a remote container by using the LoadService.
	 *
	 * @param agentName the agent name
	 * @param agentClass the agent class
	 * @param startArguments the start arguments
	 * @param inContainer the in container
	 * @return true, if successful
	 */
	private boolean startRemoteAgent(String agentName, Class<? extends Agent> agentClass, Object[] startArguments, String inContainer ) {
		
		boolean successful=false;
		
		RemoteStartAgentWaiter waiterObject = new RemoteStartAgentWaiter();
		
		Object[] UtilityAgentArgs = new Object[6];
		UtilityAgentArgs[0] = UtilityAgentJob.StartAgent;
		UtilityAgentArgs[1] = waiterObject;
		UtilityAgentArgs[2] = agentName;
		UtilityAgentArgs[3] = agentClass.getName();
		UtilityAgentArgs[4] = startArguments;
		UtilityAgentArgs[5] = inContainer;
		
		this.startUtilityAgent(UtilityAgentArgs);

		// --- Wait for the finalization of the UtilityAgent -------- 
		synchronized (waiterObject) {
			try {
				waiterObject.wait(2000);
				successful = waiterObject.isAgentStarted();
				
			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
			}	
		}
		return successful;
	}
	/**
	 * The Class RemoteStartAgentWaiter.
	 */
	public class RemoteStartAgentWaiter {
		
		private boolean agentStarted;

		public boolean isAgentStarted() {
			return agentStarted;
		}
		public void setAgentStarted(boolean agentStarted) {
			this.agentStarted = agentStarted;
		}
	}
	
	/**
	 * Removes the file transfer directories that are only used if JADE is running.
	 */
	private void removeFileTransferDirectories() {
		
		// --- Delete 'server' directory ------------------
		try {
			String serverPath = Application.getGlobalInfo().getFileManagerServerPath(false);
			if (new File(serverPath).exists()==true) {
				RecursiveFolderDeleter killer = new RecursiveFolderDeleter();
				killer.deleteFolder(serverPath);
			}
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		// --- Delete 'download' directory ----------------
		try {
			String downloadPath = Application.getGlobalInfo().getFileManagerDownloadPath(false);
			if (new File(downloadPath).exists()==true) {
				RecursiveFolderDeleter killer = new RecursiveFolderDeleter();
				killer.deleteFolder(downloadPath);
			}
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
}
