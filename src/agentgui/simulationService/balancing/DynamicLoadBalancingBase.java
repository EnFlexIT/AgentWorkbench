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

import jade.core.Location;
import jade.core.ServiceException;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.LoadMeasureAgent;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.LoadMerger;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.ontology.PlatformLoad;

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
	/** The indicator that says if the thresholds were exceeded over all. */
	protected Integer loadThresholdExceededOverAll = 0;
	/** The current threshold levels. */
	protected LoadThresholdLevels loadThresholdLevels = null; 
	
	/** The load information distributed over all machines. */
	protected Hashtable<String, LoadMerger> loadMachines4Balancing = null;
	/** The load information distributed over all JVM's. */
	protected Hashtable<String, LoadMerger> loadJVM4Balancing = null;
	
	/** The LoadAgentMap. */
	protected LoadAgentMap loadContainerAgentMap = null;
	/** The load distributed over container. */
	protected Hashtable<String, PlatformLoad> loadContainer = null;
	/** The location in the distributed system. */
	protected Hashtable<String, Location> loadContainerLoactions = null;
	/** The benchmark results of all containers. */
	protected Hashtable<String, Float> loadContainerBenchmarkResults = null;

	
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
		this.doBalancing();	
		myLoadAgent.loadBalancingIsStillActivated = false;
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
	 * Here some important counting will be done by default.
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
	 * This Method can be invoked, if a new remote container is required. The Method
	 * returns, if informations about the new container are available.
	 *
	 * @return the new container name 
	 */
	protected String startRemoteContainer() {
		
		boolean newContainerStarted = false;
		String newContainerName = null;
		try {
			// --- Start a new remote container -----------------
			LoadServiceHelper loadHelper = (LoadServiceHelper) myLoadAgent.getHelper(LoadService.NAME);
			newContainerName = loadHelper.startNewRemoteContainer();
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
				loadContainerLoactions = loadHelper.getContainerLocations();
				
				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc = loadHelper.getContainerDescription(newContainerName);
				Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
				String jvmPID = containerDesc.getJvmPID(); 
				String machineURL = containerDesc.getPlAddress().getUrl();
				
				// --- Get all needed load informations -----------------------------
				PlatformLoad containerLoad = loadHelper.getContainerLoads().get(newContainerName);
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
	 * and informs the agent about the location they have to migrate.
	 *
	 * @param transferAgents the new agent migration
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
