package de.enflexit.awb.simulation.balancing;

import jade.core.Location;
import jade.core.ServiceException;

import java.util.Hashtable;
import java.util.Vector;

import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import de.enflexit.awb.simulation.load.LoadAgentMap;
import de.enflexit.awb.simulation.load.LoadMerger;
import de.enflexit.awb.simulation.load.LoadInformation.NodeDescription;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;

/**
 * This is the base class for every tailored dynamic load balancing approach.
 * Its collects the load information from the running {@link LoadMeasureAgent} and
 * accumulates them in the local attribute. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class DynamicLoadBalancingBase extends BaseLoadBalancing  {

	private static final long serialVersionUID = -7614035278070031234L;

	// -------------
	/** The reference to the load agent. */
	protected LoadMeasureAgent myLoadAgent = null;
	// -------------

	/** The load information distributed over all machines. */
	protected Hashtable<String, LoadMerger> loadMachines4Balancing = null;
	/** The load information distributed over all JVM's. */
	protected Hashtable<String, LoadMerger> loadJVM4Balancing = null;
	/** The load distributed over container. */
	protected Hashtable<String, PlatformLoad> loadContainer = null;
	/** The LoadAgentMap. */
	protected LoadAgentMap loadContainerAgentMap = null;
	
	
	/** The number of machines. */
	protected int noMachines = 0;
	/** The Array for all machine names */
	protected String[] machineArray = null; 
	/** The number of agents in the system. */
	protected int noAgents = 0;
	/** the summation of all benchmark values */
	protected float machinesBenchmarkSummation = 0;
	
	
	/**
	 * Default constructor of this class.
	 *
	 * @param loadMeasureAgent the running load agent of the system
	 */
	public DynamicLoadBalancingBase(LoadMeasureAgent loadMeasureAgent) {
		super(loadMeasureAgent);
		myLoadAgent = loadMeasureAgent;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		this.setMeasurements();
		this.refreshCountingsAndLists();
		this.doBalancing();	
		myLoadAgent.setDynLoadBalancaingStillActivated(false);
	}
	/**
	 * This method transfers the measurement data from the loadAgent
	 * to this class. Do NOT override!!
	 */
	protected void setMeasurements() {
		
		currThresholdExceededOverAll =  Integer.valueOf(myLoadAgent.loadThresholdExceededOverAll);
		currContainerLoactions = new Hashtable<String, Location>(myLoadAgent.getLoadContainerLoactions()) ;
		currContainerBenchmarkResults = new Hashtable<String, Float>(myLoadAgent.loadContainerBenchmarkResults);
		
		loadMachines4Balancing = new Hashtable<String, LoadMerger>(myLoadAgent.loadMachines4Balancing);
		loadJVM4Balancing = new Hashtable<String, LoadMerger>(myLoadAgent.loadJVM4Balancing) ;
		loadContainer = new Hashtable<String, PlatformLoad>(myLoadAgent.getLoadContainer()) ;
		loadContainerAgentMap = myLoadAgent.getLoadContainerAgentMap();
		
	}

	/**
	 * Here some important counting will be done by default.
	 */
	protected void refreshCountingsAndLists() {
		
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
	 * Updates the local load information if a new container was started. These are in detail 
	 * the local variables {@link #currThresholdExceededOverAll}, {@link #loadMachines4Balancing} 
	 * and {@link #loadJVM4Balancing}.Additionally the method {@link #refreshCountingsAndLists()} 
	 * will be invoked at the end. 
	 * 
	 * @see #currThresholdExceededOverAll
	 * @see #loadMachines4Balancing
	 * @see #loadJVM4Balancing
	 * @see #refreshCountingsAndLists()
	 *
	 * @param newContainerName the new container name
	 */
	protected void updateLoadInfo4JVMandMachine(String newContainerName) {
		
		if (newContainerName==null) return;
		
		Float benchmarkValue = null;
		String jvmPID = null; 
		String machineURL = null;
		Integer containerNoAgents = 0;
		
		// --- Get the base information for this node/container -------------
		NodeDescription containerDesc = null;
		PlatformLoad containerLoad = null;
		try {
			containerDesc = loadHelper.getContainerDescription(newContainerName);
			containerLoad = loadHelper.getContainerLoadHash().get(newContainerName);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		if (containerDesc!=null) {
			benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
			jvmPID = containerDesc.getJvmPID(); 
			machineURL = containerDesc.getPlAddress().getUrl();
		}
		
		// ------------------------------------------------------------------
		// --- Store informations also by the JVM (merge) -------------------
		// ------------------------------------------------------------------
		if (containerLoad!=null && jvmPID!=null) {
			// --- Observe the over all Threshold -----------------
			currThresholdExceededOverAll += ((Math.abs(containerLoad.getLoadExceeded())));
			
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
		
	}	
	
	
	/**
	 * This Method can be invoked, if a new remote container is required.
	 * If the container was started the method returns the new containers name and
	 * will update the local information of {@link #currContainerLoactions} and
	 * {@link #currContainerBenchmarkResults}.<br>
	 * Additionally this method will update the load information for machines and JVM
	 * by invoking {@link #updateLoadInfo4JVMandMachine(String)}.
	 *
	 * @return the name of the new container
	 * 
	 * @see #currContainerLoactions
	 * @see #currContainerBenchmarkResults
	 */
	@Override
	protected String startRemoteContainer() {
		String newContainerName = super.startRemoteContainer();
		this.updateLoadInfo4JVMandMachine(newContainerName);
		return newContainerName;
	}
	/**
	 * This Method can be invoked, if a new remote container is required.
	 * If the container was started the method returns the new containers name and
	 * will update the local information of {@link #currContainerLoactions} and
	 * {@link #currContainerBenchmarkResults}.<br>
	 * Additionally this method will update the load information for machines and JVM
	 * by invoking {@link #updateLoadInfo4JVMandMachine(String)}.
	 *
	 * @param remoteContainerConfig the remote container configuration out of the Project 
	 * @return the name of the new container
	 * 
	 * @see #currContainerLoactions
	 * @see #currContainerBenchmarkResults
	 */
	@Override
	protected String startRemoteContainer(RemoteContainerConfig remoteContainerConfig) {
		String newContainerName = super.startRemoteContainer(remoteContainerConfig);
		this.updateLoadInfo4JVMandMachine(newContainerName);
		return newContainerName;
	}
	
}
