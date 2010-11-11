package agentgui.simulationService.balancing;

import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.LoadAgent;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadMerger;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.PlatformLoad;


public class DynamicLoadBalancingBase extends OneShotBehaviour{

	private static final long serialVersionUID = -7614035278070031234L;

	// -------------
	protected LoadAgent myLoadAgent = null;
	// -------------
	protected Integer loadThresholdExceededOverAll = 0;
	protected LoadThresholdLevels loadThresholdLevels = null; 
	
	protected Hashtable<String, LoadMerger> loadMachines4Balancing = null;
	protected Hashtable<String, LoadMerger> loadJVM4Balancing = null;
	
	protected LoadAgentMap loadContainerAgentMap = null;
	protected Hashtable<String, PlatformLoad> loadContainer = null;
	protected Hashtable<String, Location> loadContainerLoactions = null;
	protected Hashtable<String, Float> loadContainerBenchmarkResults = null;

	
	protected int noMachines = 0;
	protected String[] machineArray = null; 
	protected int noAgents = 0;
	protected float machinesBenchmarkSummation = 0;
	
	
	/**
	 * Default constructor of this class  
	 * @param loadAgent
	 */
	public DynamicLoadBalancingBase(LoadAgent loadAgent) {
		super(loadAgent);
		myLoadAgent = loadAgent;
	}
	
	@Override
	public void action() {
		this.setMeasurements();
		this.doBalancing();	
		myLoadAgent.loadBalancingActivated = false;
	}
	/**
	 * This method transfers the measurement data from the loadAgent
	 * to this class. Do NOT override!!
	 */
	public void setMeasurements() {
		
		loadThresholdExceededOverAll = new Integer(myLoadAgent.loadThresholdExceededOverAll);
		loadThresholdLevels = myLoadAgent.loadThresholdLevels;
		
		loadMachines4Balancing = new Hashtable<String, LoadMerger>(myLoadAgent.loadMachines4Balancing);
		loadJVM4Balancing = new Hashtable<String, LoadMerger>(myLoadAgent.loadJVM4Balancing) ;
		
		loadContainer = new Hashtable<String, PlatformLoad>(myLoadAgent.loadContainer) ;
		loadContainerLoactions = new Hashtable<String, Location>(myLoadAgent.loadContainerLoactions) ;
		loadContainerBenchmarkResults = new Hashtable<String, Float>(myLoadAgent.loadContainerBenchmarkResults);
		
		loadContainerAgentMap = myLoadAgent.loadContainerAgentMap;		
		
		this.refreshCountingsAndLists();
	}

	/**
	 * Here some important counting will be done standarized
	 */
	private void refreshCountingsAndLists() {
		
		noMachines = loadMachines4Balancing.size();
		noAgents = loadContainerAgentMap.noAgentsAtPlatform;
		
		machineArray = new String[loadMachines4Balancing.size()];
		new Vector<String>(loadMachines4Balancing.keySet()).toArray(machineArray);
		
		machinesBenchmarkSummation = 0;
		for (int i = 0; i<noMachines; i++) {
			machinesBenchmarkSummation += loadMachines4Balancing.get(machineArray[i]).getBenchmarkValue();
		}
	}
	
	/**
	 * This method will do the balancing. Use sub classes to do your
	 * own balancing while implementing the 'LoadBalancingInterface'
	 */
	public void doBalancing() {
		
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
			SimulationServiceHelper simHelper = (SimulationServiceHelper) myLoadAgent.getHelper(SimulationService.NAME);
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
				loadContainerLoactions = simHelper.getContainerLocations();
				
				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc = simHelper.getContainerDescription(newContainerName);
				Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
				String jvmPID = containerDesc.getJvmPID(); 
				String machineURL = containerDesc.getPlAddress().getUrl();
				
				// --- Get all needed load informations -----------------------------
				PlatformLoad containerLoad = simHelper.getContainerLoads().get(newContainerName);
				Integer containerNoAgents = 0;
				loadContainerBenchmarkResults.put(newContainerName, benchmarkValue);	
				
				// ------------------------------------------------------------------
				// --- Store informations also by the JVM (merge) -------------------
				// ------------------------------------------------------------------
				if (containerLoad!=null && jvmPID!=null) {
					// --- Observe the over all Threshold -----------------
					loadThresholdExceededOverAll += ((Math.abs(containerLoad.getLoadExceeded())));
					
					// --- Merge the load per physical machine  -----------
					LoadMerger loadMachine = loadMachines4Balancing.get(machineURL);
					if (loadMachine==null) {
						loadMachine = new LoadMerger(machineURL);
					}
					loadMachine.merge(newContainerName, jvmPID, benchmarkValue, containerLoad, containerNoAgents);
					loadMachines4Balancing.put(machineURL, loadMachine);
					
					// --- Merge the load per JVM -------------------------
					LoadMerger loadJvmMachine = loadJVM4Balancing.get(jvmPID);
					if (loadJvmMachine==null) {
						loadJvmMachine = new LoadMerger(jvmPID);
					}
					loadJvmMachine.merge(newContainerName, jvmPID, benchmarkValue, containerLoad, containerNoAgents);
					loadJVM4Balancing.put(jvmPID, loadJvmMachine);
				}
				// ------------------------------------------------------------------	
				this.refreshCountingsAndLists();
				// ------------------------------------------------------------------
				return newContainerName;
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * This Method transfers the new LoadAgentMap-Instance to the SimulationService
	 * and informs the agent about the location they have to migrate
	 * @param newAgentMap
	 */
	protected void setAgentMigration(Vector<AID_Container> transferAgents) {
		
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) myLoadAgent.getHelper(SimulationService.NAME);
			simHelper.setAgentMigration(transferAgents);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		
	}
}
