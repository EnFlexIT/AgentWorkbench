package mas.service.distribution.agents;

import jade.core.behaviours.OneShotBehaviour;

import java.util.Hashtable;

import mas.service.distribution.ontology.PlatformLoad;
import mas.service.load.LoadThresholdLevels;
import mas.service.load.LoadInformation.AgentMap;

public class LoadBalancingBase extends OneShotBehaviour{

	private static final long serialVersionUID = -7614035278070031234L;

	// -------------
	protected LoadAgent myLoadAgent = null;
	// -------------
	protected Integer loadThresholdExceededOverAll = 0;
	protected Hashtable<String, LoadMachine> loadMachines4Balancing = null;
	
	protected Hashtable<String, PlatformLoad> loadOnContainer = null;
	protected Hashtable<String, Float> loadBenchmarkResults = new Hashtable<String, Float>();
	protected AgentMap loadAgentMap = null;	
	protected LoadThresholdLevels loadThresholdLevels = null; 

	/**
	 * Default constructor of this class  
	 * @param loadAgent
	 */
	public LoadBalancingBase(LoadAgent loadAgent) {
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
		
		loadThresholdExceededOverAll = myLoadAgent.loadThresholdExceededOverAll;
		loadMachines4Balancing = myLoadAgent.loadJvmMachines4Balancing;
		loadOnContainer = myLoadAgent.loadOnContainer;
		loadBenchmarkResults = myLoadAgent.loadBenchmarkResults;
		loadAgentMap = myLoadAgent.loadAgentMap;
		loadThresholdLevels = myLoadAgent.loadThresholdLevels;
	}
		
	/**
	 * This method will do th balancing. Use sub classes to do your
	 * own balancing whil implementing the 'LoadBalancingInterface'
	 */
	public void doBalancing() {
		
	}
	
	/**
	 * This Method can be invoked, if a new remote container is required. The Method 
	 * returns, if informations about the new container are available.
	 */
	public String startRemoteContainer() {
		return myLoadAgent.monitorBehaviour.startRemoteContainer();
	}
	
}
