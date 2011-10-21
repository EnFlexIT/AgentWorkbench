package agentgui.simulationService.balancing;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.jade.Platform;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.envModel.p2Dsvg.controller.Physical2DEnvironmentController;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadThresholdLevels;

public class StaticLoadBalancingBase extends OneShotBehaviour {

	private static final long serialVersionUID = 8876791160586073658L;

	protected LoadServiceHelper loadHelper = null;
	
	protected Project currProject = null;
	protected SimulationSetup currSimSetup = null;
	protected DistributionSetup currDisSetup = null;
	protected ArrayList<AgentClassElement4SimStart> currAgentList = null;
	
	protected int currNumberOfAgents = 0;
	protected int currNumberOfContainer = 0;

	protected LoadThresholdLevels currThresholdLevels = null; 
	protected Hashtable<String, Float> loadContainerBenchmarkResults = new Hashtable<String, Float>();
	
	public StaticLoadBalancingBase() {
		super();
		// --- Which project is currently used?  --------------------
		currProject = Application.ProjectCurr;		
		// --- Get the current simulation setup ---------------------
		currSimSetup = currProject.simulationSetups.getCurrSimSetup();
		// --- Get the current distribution setup -------------------
		currDisSetup = currSimSetup.getDistributionSetup();
		// --- Which agents are to start ----------------------------
		currAgentList = currSimSetup.getAgentList();	

		// --- Get number of expected agents and the number --------- 
		// --- of container, which are wanted for this setup --------
		currNumberOfAgents = currDisSetup.getNumberOfAgents();
		currNumberOfContainer = currDisSetup.getNumberOfContainer();
	}
	
	/**
	 * This Method will be call right before the begin of the  
	 * action() method and will start the svg - visualization
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.setSimHelperAndThresholds();
		this.startVisualizationAgent();
	}
	
	@Override
	public void action() {
		
	}
	/**
	 * This Method will be call right after the end of the  
	 * action() method and will remove the simstart agent
	 */
	@Override
	public int onEnd() {
		myAgent.doDelete();
		return super.onEnd();
	}

	/**
	 * This method initializes the simHelper - Instance and sets the user
	 * threshold to the currently running system 
	 */
	private void setSimHelperAndThresholds() {
		
		// --- Set the simHelper-Instance ---------------------------
		try {
			loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}	
		
		// --- If the user wants to use his own Threshold, ----------
		// --- load them to the SimulationsService		   ----------
		if (currDisSetup.isUseUserThresholds()) {
			currThresholdLevels = currDisSetup.getUserThresholds();
			try {
				loadHelper.setThresholdLevels(currThresholdLevels);
			} catch (ServiceException e) {
				e.printStackTrace();
			}	
		} else {
			currThresholdLevels = LoadMeasureThread.getThresholdLevels();
		}
	}
	
	/**
	 * This method will start all agents defined in the agent list
	 * of 'Agent-Start' from the 'Simulation-Setup'
	 */
	protected void startAgentsFromCurrAgentList() {
		
		for (Iterator<AgentClassElement4SimStart> iterator = currAgentList.iterator(); iterator.hasNext();) {
			AgentClassElement4SimStart agent2Start = iterator.next();
			// --- Check for start arguments -------------------------
			Object[] startArgs = this.getStartArguments(agent2Start);
			// --- Start the agent -----------------------------------
			this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs);
		} 
	}

	/**
	 * This method will start the agents, which will show the 
	 * visualisation of the current environment model
	 */
	protected void startVisualizationAgent() {
		
		EnvironmentPanel envPanel = currProject.getEnvironmentPanel();
		if (envPanel!=null) {
			
			EnvironmentType envType = currProject.getEnvironmentModelType();
			String envTypeInternalKey = envType.getInternalKey();
			
			// ----------------------------------------------------------------
			// --- If an visualised environment is used -----------------------
			// ----------------------------------------------------------------
			if (envTypeInternalKey.equalsIgnoreCase("none")) {
				// --- Do nothing here ------------------------------
			} else if (envTypeInternalKey.equalsIgnoreCase("condhfghftinous2Denvironment")) {
				// --------------------------------------------------
				// --- Start the SVG visualization ------------------
				// --------------------------------------------------
				Physical2DEnvironmentController envController = (Physical2DEnvironmentController) envPanel.getEnvironmentController();
				this.startSVGVisualizationAgents(envController);
			
			} else {
				// --------------------------------------------------
				// --- Start the visualization of the model ---------
				// --------------------------------------------------
				EnvironmentController envController = envPanel.getEnvironmentController();
				Object envModel = envController.getEnvironmentModelCopy();

				// --- Get the Agent which has to be started for ---
				Class<? extends Agent> displayAgentClass = envType.getDisplayAgentClass();
				
				Object[] startArg = new Object[3];
				startArg[0] = currProject.projectVisualizationPanel;
				startArg[1] = envModel;
				this.startAgent("DisplayAgent", displayAgentClass, startArg);
				
				// --- Set the focus on Visualisation-Tab ---------------------
				currProject.projectWindow.setFocus2Tab(Language.translate("Simulations-Visualisierung"));
			}
			// ----------------------------------------------------------------
		}
	}
	
	/**
	 * Depending on the current project configuration, the 
	 * svg - visualization will be started here
	 */
	protected void startSVGVisualizationAgents(Physical2DEnvironmentController physical2DEnvironmentController) {
		
		Physical2DEnvironment environment = physical2DEnvironmentController.getEnvironmentModelCopy();
		Document svgDocument = physical2DEnvironmentController.getSvgDocCopy();
		
		if (environment!=null && svgDocument!=null) {
			
			// --- Start the agent visualisation --------------------
			Object[] args = {environment, svgDocument};
			this.startAgent("EvPrAg_"+ currProject.getProjectFolder(), agentgui.envModel.p2Dsvg.provider.EnvironmentProviderAgent.class, args);
			
			// --- Start the DisplayAgent inside of Agent.GUI -------
			Object[] startArg = new Object[3];
			startArg[0] = currProject.projectVisualizationPanel;
			startArg[1] = svgDocument;
			startArg[2] = environment;
			this.startAgent("EvVis", agentgui.envModel.p2Dsvg.display.DisplayAgent.class, startArg);
			
			// --- get agents, defined in the physical-/svg-setup ---			
//			Vector<ActiveObject> activeObjects = physical2DEnvironmentController.getEnvWrap().getAgents();
//			for (Iterator<ActiveObject> iterator = activeObjects.iterator(); iterator.hasNext();) {
//				
//				ActiveObject activeObject = iterator.next();
//				String agentClassName = activeObject.getAgentClassName();
//				String agentName = activeObject.getId();
//				
//				if (currAgentListVisual==null) {
//					currAgentListVisual= new ArrayList<AgentClassElement4SimStart>();
//				}
//				AgentClassElement4SimStart ace4s = new AgentClassElement4SimStart();
//				ace4s.setAgentClassReference(agentClassName);
//				ace4s.setStartAsName(agentName);
//				currAgentListVisual.add(ace4s);
//			}
			// --- set focus on Visualisation-Tab -------------------
			currProject.projectWindow.setFocus2Tab(Language.translate("Simulations-Visualisierung"));
		}
	}
	
	/**
	 * This method will return the Object Array for the start argument of an agent 
	 * @param ace4SimStart
	 */
	protected Object[] getStartArguments(AgentClassElement4SimStart ace4SimStart) {
		
		if (ace4SimStart.getStartArguments()==null) {
			return null;
		} else {
			String selectedAgentReference = ace4SimStart.getElementClass().getName();
			OntologyInstanceViewer oiv = new OntologyInstanceViewer(currProject, selectedAgentReference);
			oiv.setConfigurationXML(ace4SimStart.getStartArguments());
			
			Object[] startArgs = oiv.getConfigurationInstances();
			return startArgs;
		}
	}
	
	/**
	 * Method to start a new agent
	 * @param nickName
	 * @param agentClassName
	 * @param args
	 */
	protected void startAgent(String nickName, String agentClassName, Object[] args) {
		this.startAgent(nickName, agentClassName, args, null);
	}
	/**
	 * Method to start a new agent
	 * @param nickName
	 * @param agentClass
	 * @param args
	 */
	protected void startAgent(String nickName, Class<? extends Agent> agentClass, Object[] args) {
		this.startAgent(nickName, agentClass, args, null);
	}
	/**
	 * Method to start a new agent
	 * @param nickName
	 * @param agentClassName
	 * @param args
	 * @param toLocation
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
	 * @param nickName
	 * @param agentClass
	 * @param args
	 * @param toLocation
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
	 * This method will start the Load-Monitor-Agent 
	 */
	protected void openLoadMonitor() {
		 Application.JadePlatform.jadeUtilityAgentStart( Platform.UTIL_CMD_OpenLoadMonitor);
	}
	
	/**
	 * Checks if an agent can be found locally
	 * @param cc
	 * @param nickName
	 * @return
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
	 * Checks if the load service is running or not 
	 * @return
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
	 * This method will start the number of agents 
	 * @param numberOfContainer
	 */
	protected Hashtable<String, Location> startRemoteContainer(int numberOfContainer, boolean preventUsageOfAlreadyUsedComputers, boolean filterMainContainer) {
		
		Hashtable<String, Location> newContainerLocations = null;
		
		// --- Is the simulation service running ? -----------------------
		if (loadServiceIsRunning()==false) {
			System.out.println("Can not start remote container - LoadService is not running!");
			return null;
		}
		
		// --- Start the required number of container -------------------- 
		int startMistakes = 0;
		int startMistakesMax = 1;
		Vector<String> containerList = new Vector<String>();
		while (containerList.size()< numberOfContainer) {
		
			String newContainer = this.startRemoteContainer(preventUsageOfAlreadyUsedComputers);
			if (newContainer!=null) {
				containerList.add(newContainer);	
			} else {
				startMistakes++;
			}
			if (startMistakes>=startMistakesMax) {
				break;
			}
		}
		
		// --- Start a new remote container ------------------------------
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
	 * This Method can be invoked, if a new remote container is required. The Method 
	 * returns, if informations about the new container are available.
	 */
	protected String startRemoteContainer(boolean preventUsageOfAlreadyUsedComputers) {
		
		boolean newContainerStarted = false;
		String newContainerName = null;
		try {
			// --- Start a new remote container -----------------
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			newContainerName = loadHelper.startNewRemoteContainer(preventUsageOfAlreadyUsedComputers);
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

				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc = loadHelper.getContainerDescription(newContainerName);
				Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
				loadContainerBenchmarkResults.put(newContainerName, benchmarkValue);				
				return newContainerName;
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
}
