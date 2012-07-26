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
package agentgui.simulationService.balancing;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.jade.Platform;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.core.project.DistributionSetup;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.ontology.RemoteContainerConfig;

/**
 * This class is the abstract super class for the dynamic and static load balancing.
 * It provide functionalities, which should be available in both kind
 * of balancing approaches.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class BaseLoadBalancing extends OneShotBehaviour implements BaseLoadBalancingInterface {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4987462350754111029L;
	
	/** The simulation helper for the connection to the SimulationsService. */
	protected SimulationServiceHelper simHelper = null;
	/** The load helper for the connection to the LoadService. */
	protected LoadServiceHelper loadHelper = null;
	
	
	/** The current project instance. */
	protected Project currProject = null;
	/** The current simulation setup. */
	protected SimulationSetup currSimSetup = null;
	/** The current distribution setup. */ 
	protected DistributionSetup currDisSetup = null;
	
	/** The current threshold levels. */
	protected LoadThresholdLevels currThresholdLevels = null;
	/** The indicator that says if the thresholds were exceeded over all. */
	protected Integer currThresholdExceededOverAll = 0;
	
	/** The BenchmarkResults of all involved container. */
	protected Hashtable<String, Float> currContainerBenchmarkResults = new Hashtable<String, Float>();
	/** The locations in the distributed system. */
	protected Hashtable<String, Location> currContainerLoactions = null;
	
	
	
	/**
	 * Instantiates a new base load balancing.
	 */
	public BaseLoadBalancing(Agent agent) {
		super(agent);
		currProject = Application.getProjectFocused();		
		if (currProject!=null) {
			currSimSetup = currProject.simulationSetups.getCurrSimSetup();
			currDisSetup = currProject.getDistributionSetup();
		}
		this.setLoadHelper();
		this.setSimulationLoadHelper();
		this.setThresholdLevels();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		this.doBalancing();
	}
	
	/**
	 * This method receives the LoadServiceHelper to the corresponding local variable. 
	 */
	private void setLoadHelper() {

		try {
			loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * This method receives the SimulationsServiceHelper to the corresponding local variable. 
	 */
	private void setSimulationLoadHelper() {
		try {
			simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * This method puts the threshold levels to the corresponding local variable. 
	 */
	private void setThresholdLevels() {
		
		if (currDisSetup!=null) {
			// --- If the user wants to use his own Threshold, ----------
			// --- load them to the SimulationsService		   ----------
			if (currDisSetup.isUseUserThresholds()) {
				currThresholdLevels = currDisSetup.getUserThresholds();
				try {
					loadHelper.setThresholdLevels(currThresholdLevels);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				return;
			} else {
				currThresholdLevels = LoadMeasureThread.getThresholdLevels();
			}
			
		} else {
			currThresholdLevels = LoadMeasureThread.getThresholdLevels();
		}

	}
	
	
	/**
	 * This method will return the Object Array for the start argument of an agent.
	 *
	 * @param ace4SimStart the AgentClassElement4SimStart
	 * @return the start arguments as object array
	 */
	protected Object[] getStartArguments(AgentClassElement4SimStart ace4SimStart) {
		
		if (ace4SimStart.getStartArguments()==null) {
			return null;
		} else {
			String selectedAgentReference = ace4SimStart.getElementClass().getName();
			OntologyInstanceViewer oiv = new OntologyInstanceViewer(currProject.getOntologyVisualisationHelper(), currProject.getAgentStartConfiguration(), selectedAgentReference);
			oiv.setConfigurationXML(ace4SimStart.getStartArguments());
			
			Object[] startArgs = oiv.getConfigurationInstances();
			return startArgs;
		}
	}
	
	/**
	 * Method to start a new agent.
	 *
	 * @param nickName the nick name
	 * @param agentClassName the agent class name
	 * @param args the start arguments as Object array 
	 */
	protected void startAgent(String nickName, String agentClassName, Object[] args) {
		this.startAgent(nickName, agentClassName, args, null);
	}
	
	/**
	 * Method to start a new agent.
	 *
	 * @param nickName the nick name
	 * @param agentClass the agent class
	 * @param args the start arguments as Object array 
	 */
	protected void startAgent(String nickName, Class<? extends Agent> agentClass, Object[] args) {
		this.startAgent(nickName, agentClass, args, null);
	}
	
	/**
	 * Method to start a new agent.
	 *
	 * @param nickName the nick name
	 * @param agentClassName the agent class name
	 * @param args the start arguments as Object array 
	 * @param toLocation the location, where the agent should start
	 */
	protected void startAgent(String nickName, String agentClassName, Object[] args, Location toLocation ) {
		
		if (agentClassName==null | agentClassName.equalsIgnoreCase("") | agentClassName.equalsIgnoreCase(Language.translate("Keine")) ) {
			System.err.println(Language.translate("Agent '" + nickName + "': " + Language.translate("Keine Klasse definiert") + "!"));
		} else {
			// --- Initialize the agent-class -------------
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Agent> agentClass = (Class<? extends Agent>) Class.forName(agentClassName);
				this.startAgent(nickName, agentClass, args, toLocation);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Main-Method to start a new agent. All other methods will use this one at least.
	 *
	 * @param nickName the nick name
	 * @param agentClass the agent class
	 * @param args the start arguments as Object array 
	 * @param toLocation the location, where the agent should start
	 */
	protected void startAgent(String nickName, Class<? extends Agent> agentClass, Object[] args, Location toLocation ) {
		
		boolean startLocally = false;
		ContainerController cc = myAgent.getContainerController();
		AgentController ac = null;
		
		try {
			// ----------------------------------------------------------
			// --- Start here or on a remote container? -----------------
			// ----------------------------------------------------------
			if (toLocation==null) {
				startLocally = true;
			} else {
				if (cc.getContainerName().equalsIgnoreCase(toLocation.getName())) {
					startLocally = true;
				} else {
					startLocally = false;
				}
			}
			// ----------------------------------------------------------
			
			
			// ----------------------------------------------------------
			// --- Start the agent now ! --------------------------------
			// ----------------------------------------------------------
			if (startLocally==true) {
				// --------------------------------------------------
				// --- Start on this local container ----------------				
				Agent agent = (Agent) agentClass.newInstance();
				agent.setArguments(args);
				ac = cc.acceptNewAgent(nickName, agent);
				ac.start();
				// --------------------------------------------------
				
			} else {
				// --------------------------------------------------
				// --- Is the SimulationServioce running? -----------
				if (loadServiceIsRunning()==true) {
					// ----------------------------------------------
					// --- START: Start direct on remote-container --
					// ----------------------------------------------
					String containerName = toLocation.getName();
					String agentClassName = agentClass.getName();
					try {
						LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
						loadHelper.startAgent(nickName, agentClassName, args, containerName);
					} catch (ServiceException e) {
						e.printStackTrace();
					}
					// ----------------------------------------------
					// --- END: Start direct on remote-container ----					
					// ----------------------------------------------
				} else {
					// ----------------------------------------------
					// --- START: 'Start and migrate' - procedure ---
					// ----------------------------------------------
					Agent agent = (Agent) agentClass.newInstance();
					agent.setArguments(args);
					ac = cc.acceptNewAgent(nickName, agent);
					ac.start();
					// --------------------------------
					int retryCounter = 0;
					while(agentFound(cc,nickName)==false){
						block(100);
						if (retryCounter>=5) {
							break;
						}
					}					
					// --------------------------------
					retryCounter = 0;
					while(agentFound(cc,nickName)==true){
						// --- Move the agent ---------
						if (retryCounter==0) {
							agent.doMove(toLocation);	
						}
						block(100);
						retryCounter++;
						if (retryCounter>=5) {
							retryCounter = 0;
						}
					} // --- end while
					// ----------------------------------------------
					// --- END: 'Start and migrate' - procedure -----	
					// ----------------------------------------------					
				}
				// --------------------------------------------------
			}
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * This method will start the Load-Monitor-Agent.
	 */
	protected void openLoadMonitor() {
		 Application.getJadePlatform().jadeUtilityAgentStart( Platform.UTIL_CMD_OpenLoadMonitor);
	}
	
	/**
	 * Checks if an agent can be found locally.
	 *
	 * @param cc the local ContainerController
	 * @param nickName the local agent name to search for
	 * @return true, if successful
	 */
	private boolean agentFound(ContainerController cc, String nickName) {
		try {
			cc.getAgent(nickName);
			return true;
		} catch (ControllerException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if the load service is running or not.
	 *
	 * @return true, if the Service is running
	 */
	private boolean loadServiceIsRunning() {
		
		try {
			@SuppressWarnings("unused")
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			return true;
		} catch (ServiceException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This Method can be invoked, if a new remote container is required.
	 * If the container was started the method returns the new containers name and
	 * will update the local information of {@link #currContainerLoactions} and
	 * {@link #currContainerBenchmarkResults}.
	 *
	 * @return the name of the new container
	 * 
	 * @see #currContainerLoactions
	 * @see #currContainerBenchmarkResults
	 */
	protected String startRemoteContainer() {
		return this.startRemoteContainer(null);
	}
	/**
	 * This Method can be invoked, if a new remote container is required.
	 * If the container was started the method returns the new containers name and
	 * will update the local information of {@link #currContainerLoactions} and
	 * {@link #currContainerBenchmarkResults}.
	 *
	 * @param remoteContainerConfig the remote container configuration out of the Project 
	 * @return the name of the new container
	 * 
	 * @see #currContainerLoactions
	 * @see #currContainerBenchmarkResults
	 */
	protected String startRemoteContainer(RemoteContainerConfig remoteContainerConfig) {
		
		boolean newContainerStarted = false;
		String newContainerName = null;
		try {
			// --- Start a new remote container -----------------
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			newContainerName = loadHelper.startNewRemoteContainer(remoteContainerConfig);
			
			while (true) {
				Container2Wait4 waitCont = loadHelper.startNewRemoteContainerStaus(newContainerName);	
				if (waitCont.isStarted()) {
					System.out.println("Remote Container '" + newContainerName + "' was started!");
					newContainerStarted = true;
					break;
				}
				if (waitCont.isCancelled()) {
					System.out.println("Remote Container '" + newContainerName + "' was NOT started!");
					newContainerStarted = false;
					break;
				}
				if (waitCont.isTimedOut()) {
					System.out.println("Remote Container '" + newContainerName + "' timed out!");
					newContainerStarted = false;
					break;	
				}
				this.block(100);
			} // end while
			
			if (newContainerStarted==true) {

				while (loadHelper.getContainerDescription(newContainerName).getJvmPID()==null) {
					this.block(100);
				}
				while (loadHelper.getContainerLoads().get(newContainerName)==null) {
					this.block(100);
				}
				// --- Update the locations of all involved container --------------- 
				currContainerLoactions = loadHelper.getContainerLocations();
				
				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc = loadHelper.getContainerDescription(newContainerName);
				Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
				currContainerBenchmarkResults.put(newContainerName, benchmarkValue);
				
				return newContainerName;
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * This method will start a number of remote container.
	 *
	 * @param numberOfContainer the number of container
	 * @param remoteContainerConfig the remote container configuration
	 * @param filterMainContainer true, if the Main-Container should be filter out of the result
	 * 
	 * @return the of all newly started locations
	 */
	protected Hashtable<String, Location> startNumberOfRemoteContainer(int numberOfContainer, boolean filterMainContainer, RemoteContainerConfig remoteContainerConfig) {
		
		Hashtable<String, Location> newContainerLocations = null;
		
		// --- Is the simulation service running ? -----------------------
		if (loadServiceIsRunning()==false) {
			System.out.println("Can not start remote container - LoadService is not running!");
			return null;
		}
		
		// --- Start the required number of container -------------------- 
		int startMistakes = 0;
		int startMistakesMax = 2;
		Vector<String> containerList = new Vector<String>();
		while (containerList.size()< numberOfContainer) {
		
			String newContainer = this.startRemoteContainer();
			if (newContainer!=null) {
				containerList.add(newContainer);	
			} else {
				startMistakes++;
			}
			if (startMistakes>=startMistakesMax) {
				break;
			}
		}
		
		// --- Get the locations of the started container ----------------
		LoadServiceHelper loadHelper;
		try {
			loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			newContainerLocations = loadHelper.getContainerLocations();
			
		} catch (ServiceException e) {
			e.printStackTrace();
			return null;
		}

		// --- If wanted, filter the Main-Container out ------------------
		if (filterMainContainer == true) {
			newContainerLocations.remove("Main-Container");
			if (newContainerLocations.size()==0) {
				newContainerLocations = null;
			}
		}
		return newContainerLocations;
	}


	/**
	 * This Method transfers the new LoadAgentMap-Instance to the SimulationService
	 * and informs the agent about the location they have to migrate.
	 *
	 * @param transferAgents the new agent migration
	 */
	protected void setAgentMigration(Vector<AID_Container> transferAgents) {
		
		try {
			simHelper.setAgentMigration(transferAgents);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
}
