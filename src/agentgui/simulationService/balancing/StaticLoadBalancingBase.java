package agentgui.simulationService.balancing;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
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
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;

public class StaticLoadBalancingBase extends OneShotBehaviour {

	private static final long serialVersionUID = 8876791160586073658L;

	protected SimulationServiceHelper simHelper = null;
	
	protected Project currProject = null;
	protected SimulationSetup currSimSetup = null;
	protected DistributionSetup currDisSetup = null;
	protected ArrayList<AgentClassElement4SimStart> currAgentList = null;
	protected ArrayList<AgentClassElement4SimStart> currAgentListVisual = null;

	protected LoadThresholdLevels currThresholdLevels = null; 
	
	protected Hashtable<String, Float> loadContainerBenchmarkResults = null;
	
	public StaticLoadBalancingBase() {
		super();
		// --- Which project is currently used?  --------------------
		currProject = Application.ProjectCurr;		
		// --- Get the current simulation setup ---------------------
		currSimSetup = currProject.simSetups.getCurrSimSetup();
		// --- Get the current distribution setup -------------------
		currDisSetup = currSimSetup.getDistributionSetup();
		// --- Which agents are to start ----------------------------
		currAgentList = currSimSetup.getAgentList();	
		
		// --- If the user wants to use his own Threshold, ----------
		// --- load them to the SimulationsService		   ----------
		if (currDisSetup.isUseUserThresholds()) {
			currThresholdLevels = currDisSetup.getUserThresholds();
			try {
				simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
				simHelper.setThresholdLevels(currThresholdLevels);
			} catch (ServiceException e) {
				e.printStackTrace();
			}	
		} else {
			currThresholdLevels = LoadMeasureThread.getThresholdLevels();
		}
	}
	
	/**
	 * This Method will be call right before the begin of the  
	 * action() method and will start the svg - visualization
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.startSVGVisualizationAgents();
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
	 * Depending on the current project configuration, the 
	 * svg - visualization will be started here
	 */
	protected void startSVGVisualizationAgents() {
		
		Physical2DEnvironment environment = currProject.getEnvironment();
		Document svgDocument = currProject.getSVGDoc();
		if (environment!=null && svgDocument!=null) {
			
			// --- Start the agent visualisation --------------------
			Object[] args = {environment, svgDocument};
			this.startAgent("EvPrAg_"+ currProject.getProjectFolder(), agentgui.physical2Denvironment.provider.EnvironmentProviderAgent.class, args);
			
			// --- Start the DisplayAgent inside of Agent.GUI -------
			Object[] startArg = new Object[3];
			startArg[0] = currProject.ProjectVisualizationPanel;
			startArg[1] = svgDocument;
			startArg[2] = environment;
			this.startAgent("EvVis", agentgui.physical2Denvironment.display.DisplayAgent.class, startArg);
			
			// --- get agents, defined in the physical-/svg-setup ---
			Vector<ActiveObject> activeObjects = currProject.getEnvironmentController().getEnvWrap().getAgents();
			for (Iterator<ActiveObject> iterator = activeObjects.iterator(); iterator.hasNext();) {
				
				ActiveObject activeObject = iterator.next();
				String agentClassName = activeObject.getAgentClassName();
				String agentName = activeObject.getId();
				
				if (currAgentListVisual==null) {
					currAgentListVisual= new ArrayList<AgentClassElement4SimStart>();
				}
				AgentClassElement4SimStart ace4s = new AgentClassElement4SimStart();
				ace4s.setAgentClassReference(agentClassName);
				ace4s.setStartAsName(agentName);
				currAgentListVisual.add(ace4s);
			}
			// --- set focus on Visualization-Tab -------------------
			currProject.ProjectGUI.setFocusOnProjectTab(Language.translate("Simulations-Visualisierung"));
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
		
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Agent> agentClass = (Class<? extends Agent>) Class.forName(agentClassName);
			this.startAgent(nickName, agentClass, args, toLocation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
		
		// --- Start the given agents -------------------------------
		ContainerController cc = myAgent.getContainerController();
		AgentController ac = null;
			
		try {
			Agent agent = (Agent) agentClass.newInstance();
			agent.setArguments(args);
			ac = cc.acceptNewAgent(nickName, agent);
			ac.start();
			
			// --- if other locations are there and -----------------
			// --- should be used, use them now		-----------------
			if (toLocation!=null) {
				ac.move(toLocation);
			}			
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * This method will start the number of agents 
	 * @param numberOfContainer
	 */
	protected Hashtable<String, Location> startRemoteContainer(int numberOfContainer) {
		
		// --- Start the required number of container -------------------- 
		int startMistakes = 0;
		int startMistakesMax = 3;
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
		
		// --- Start a new remote container ------------------------------
		SimulationServiceHelper simHelper;
		try {
			simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
			Hashtable<String, Location> newContainerLocations = simHelper.getContainerLocations();
			return newContainerLocations;
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * This Method can be invoked, if a new remote container is required. The Method 
	 * returns, if informations about the new container are available.
	 */
	protected String startRemoteContainer() {
		
		boolean newContainerStarted = false;
		String newContainerName = null;
		try {
			// --- Start a new remote container -----------------
			SimulationServiceHelper simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
			newContainerName = simHelper.startNewRemoteContainer();
			while (true) {
				Container2Wait4 waitCont = simHelper.startNewRemoteContainerStaus(newContainerName);	
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

				while (simHelper.getContainerDescription(newContainerName).getJvmPID()==null) {
					this.block(100);
				}
				while (simHelper.getContainerLoads().get(newContainerName)==null) {
					this.block(100);
				}

				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc = simHelper.getContainerDescription(newContainerName);
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
