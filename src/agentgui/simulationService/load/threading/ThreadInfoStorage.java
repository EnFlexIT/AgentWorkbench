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
package agentgui.simulationService.load.threading;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jfree.data.xy.XYSeries;

/**
 * Storage class for all relevant load information about agents/threads.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorage extends Vector<ThreadProtocol>{
	/**
	* The available series keys as constants
	*/
	public final String TOTAL_CPU_USER_TIME   		= "TOTAL_CPU_USER_TIME";
	public final String DELTA_CPU_USER_TIME   		= "DELTA_CPU_USER_TIME";
	public final String TOTAL_CPU_SYSTEM_TIME 		= "TOTAL_CPU_SYSTEM_TIME";
	public final String DELTA_CPU_SYSTEM_TIME 		= "DELTA_CPU_SYSTEM_TIME";
	public final String AVG_TOTAL_CPU_USER_TIME   	= "AVG_TOTAL_CPU_USER_TIME";
	public final String AVG_TOTAL_CPU_SYSTEM_TIME 	= "AVG_TOTAL_CPU_SYSTEM_TIME";
	public final String AVG_DELTA_CPU_USER_TIME   	= "AVG_DELTA_CPU_USER_TIME";
	public final String AVG_DELTA_CPU_SYSTEM_TIME 	= "AVG_DELTA_CPU_SYSTEM_TIME";
	public final String MAX_TOTAL_CPU_USER_TIME   	= "MAX_TOTAL_CPU_USER_TIME";
	public final String MAX_TOTAL_CPU_SYSTEM_TIME 	= "MAX_TOTAL_CPU_SYSTEM_TIME";
	public final String MAX_DELTA_CPU_USER_TIME   	= "MAX_DELTA_CPU_USER_TIME";
	public final String MAX_DELTA_CPU_SYSTEM_TIME 	= "MAX_DELTA_CPU_SYSTEM_TIME";
	public final String LOAD_CPU 			  		= "LOAD_CPU";
	public final String DELIMITER   				= "<>";
	public final String AT   						= "@";

	private static final long serialVersionUID = 5821118253370178316L;
	
	/** The tree model. */
	private DefaultTreeModel treeModel;
	
	/** The Hash-Map that Stores ThreadInfo of cluster. */
	private HashMap<String, ThreadInfoStorageCluster> mapCluster;

	
	/** The Hash-Map that Stores ThreadInfo of each machine. */
	private HashMap<String, ThreadInfoStorageMachine> mapMachine;
	private Set<String> machineSet;
	
	/** The Hash-Map that Stores ThreadInfo of each JavaVirtualMachine. */
	private HashMap<String, ThreadInfoStorageJVM> mapJVM;
	private Set<String> jvmSet;
	
	/** The Hash-Map that Stores ThreadInfo of each Container. */
	private HashMap<String, ThreadInfoStorageContainer> mapContainer;
	private Set<String> containerSet;
	
	/** The Hash-Map that Stores ThreadInfo of each agent. */
	private HashMap<String, ThreadInfoStorageAgent> mapAgent;
	private Set<String> agentSet;
	
	/** The Hash-Map that Stores ThreadInfo of each agent-class. */
	private HashMap<String, ThreadInfoStorageAgentClass> mapAgentClass;
	
	/**
	 * Instantiates a new thread info storage.
	 */
	public ThreadInfoStorage() {
	}

	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(ThreadProtocol threadProtocol) {
		
		// --- Add local vector ---
		boolean done = super.add(threadProtocol);
		
		double sumMachineTotalCPUSystemTime = 0;
		double sumMachineTotalCPUUsertime = 0;
		
		/*
		 * Initialize dynamically on new protocol arrival
		 * 
		 * 
		 */
		
		String machineName = threadProtocol.getMachineName();
		String jvmName = threadProtocol.getJVMName()+AT+machineName;
		String containerName = threadProtocol.getContainerName()+AT+jvmName;
		String clusterName = "Cluster";
		
		//// CONTAINER ////
		ThreadInfoStorageContainer containerStorage = getMapContainer().get(containerName);
		if(containerStorage == null){
			// --- add agent to map
			containerStorage = new ThreadInfoStorageContainer(containerName);
			containerStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+containerName));
			containerStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+containerName));
			containerStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+containerName));
			containerStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+containerName));
			containerStorage.getXYSeriesMap().put(LOAD_CPU, new XYSeries(LOAD_CPU+DELIMITER+machineName));
			mapContainer.put(containerName, containerStorage);
		}
		
		//// JVM ////
		ThreadInfoStorageJVM jvmStorage = getMapJVM().get(jvmName);
		if(jvmStorage == null){
			// --- add agent to map
			jvmStorage = new ThreadInfoStorageJVM(jvmName);
			jvmStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+jvmName));
			jvmStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+jvmName));
			jvmStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+jvmName));
			jvmStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+jvmName));
			jvmStorage.getXYSeriesMap().put(LOAD_CPU, new XYSeries(LOAD_CPU+DELIMITER+machineName));
			mapJVM.put(jvmName, jvmStorage);
		}
		
		///// Machine //////
		ThreadInfoStorageMachine machineStorage = getMapMachine().get(machineName);
		if(machineStorage == null){
			// --- add agent to map
			machineStorage = new ThreadInfoStorageMachine(machineName);
			machineStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+machineName));
			machineStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+machineName));
			machineStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+machineName));
			machineStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+machineName));
			machineStorage.getXYSeriesMap().put(LOAD_CPU, new XYSeries(LOAD_CPU+DELIMITER+machineName));
			mapMachine.put(machineName, machineStorage);
		}
			
		///// Cluster //////
		ThreadInfoStorageCluster clusterStorage = getMapCluster().get(clusterName);
		if(clusterStorage == null){
			// --- add agent to map
			clusterStorage = new ThreadInfoStorageCluster(clusterName);
			clusterStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+clusterName));
			clusterStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+clusterName));
			clusterStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+clusterName));
			clusterStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+clusterName));
			clusterStorage.getXYSeriesMap().put(LOAD_CPU, new XYSeries(LOAD_CPU+DELIMITER+clusterName));
			mapCluster.put(clusterName, clusterStorage);
		}
		
		/*
		 * get thread times from protocol,
		 * calculate values and put them to time series 
		 */
		
		for (int i = 0; i < threadProtocol.getThreadTimes().size(); i++) {
			
			/******* AGENT
			 * simply add data to series
			 * 
			 */
			String agentName = threadProtocol.getThreadTimes().get(i).toString()+AT+containerName;
			String classKey;
			boolean isAgent = threadProtocol.getThreadTimes().get(i).isAgent();
			
			if(isAgent == true){
				classKey = threadProtocol.getThreadTimes().get(i).getClassName();
			}else{
				classKey = "all-non-agent-classes";
			}
			
			ThreadInfoStorageAgent agentStorage = getMapAgent().get(agentName);
			if(agentStorage == null){
				// --- add agent to map
				agentStorage = new ThreadInfoStorageAgent(agentName, classKey, isAgent);
				agentStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+agentName));
				mapAgent.put(agentName, agentStorage);
			}
			
			// --- get system-and user data series ---
			XYSeries agentTotalCPUUserTimeXYSeries = agentStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
			XYSeries totalCPUSystemTimeXYSeries = agentStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
			
			// --- get total system-and user time ---
			double userTime = threadProtocol.getThreadTimes().get(i).getUserTime();
			double systemTime = threadProtocol.getThreadTimes().get(i).getSystemTime();
			
			// --- add total times to total data series ---
			agentTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), userTime);
			totalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), systemTime);			
			// --- calculate delta and add to delta data series  ---
			double deltaUserTime = agentStorage.getLastDeltaForXYSeries(agentTotalCPUUserTimeXYSeries);
			double deltaSystemTime = agentStorage.getLastDeltaForXYSeries(totalCPUSystemTimeXYSeries);
			agentStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(), deltaUserTime);
			agentStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(), deltaSystemTime);
			
			/******* AGENT-CLASS
			 *  (re)calculate data based on actual agent
			 * 
			 */
			ThreadInfoStorageAgentClass agentClassStorage = getMapAgentClass().get(classKey);
			if(agentClassStorage == null){
				// --- add agent classes to map
				agentClassStorage = new ThreadInfoStorageAgentClass(classKey);
				agentClassStorage.getXYSeriesMap().put(AVG_TOTAL_CPU_SYSTEM_TIME,new XYSeries(AVG_TOTAL_CPU_SYSTEM_TIME + DELIMITER + classKey));
				agentClassStorage.getXYSeriesMap().put(AVG_TOTAL_CPU_USER_TIME,new XYSeries(AVG_TOTAL_CPU_USER_TIME + DELIMITER + classKey));
				agentClassStorage.getXYSeriesMap().put(MAX_TOTAL_CPU_SYSTEM_TIME,new XYSeries(MAX_TOTAL_CPU_SYSTEM_TIME + DELIMITER + classKey));
				agentClassStorage.getXYSeriesMap().put(MAX_TOTAL_CPU_USER_TIME,new XYSeries(MAX_TOTAL_CPU_USER_TIME + DELIMITER + classKey));
				agentClassStorage.getXYSeriesMap().put(AVG_DELTA_CPU_SYSTEM_TIME,new XYSeries(AVG_DELTA_CPU_SYSTEM_TIME + DELIMITER + classKey));
				agentClassStorage.getXYSeriesMap().put(AVG_DELTA_CPU_USER_TIME,new XYSeries(AVG_DELTA_CPU_USER_TIME + DELIMITER + classKey));	
				agentClassStorage.getXYSeriesMap().put(MAX_DELTA_CPU_SYSTEM_TIME,new XYSeries(MAX_DELTA_CPU_SYSTEM_TIME + DELIMITER + classKey));	
				agentClassStorage.getXYSeriesMap().put(MAX_DELTA_CPU_USER_TIME,new XYSeries(MAX_DELTA_CPU_USER_TIME + DELIMITER + classKey));
				
				mapAgentClass.put(classKey, agentClassStorage);
			}
			
			//--- update with time of actual thread ---
			sumMachineTotalCPUUsertime = sumMachineTotalCPUUsertime + userTime;
			sumMachineTotalCPUSystemTime = sumMachineTotalCPUSystemTime + systemTime;
			
			// --- Metrics only exist for Agent-Threads ---
			if(isAgent == true){
				double predictiveMetric = threadProtocol.getThreadTimes().get(i).getPredictiveMetric();
				double realMetric = threadProtocol.getThreadTimes().get(i).getRealMetric();	
				
				// --- update MIN and MAX predictive-metric values ---
				if(predictiveMetric < agentClassStorage.getMinPredictiveMetric()){
					agentClassStorage.setMinPredictiveMetric(predictiveMetric);
				}else if(predictiveMetric > agentClassStorage.getMaxPredictiveMetric()){
					agentClassStorage.setMaxPredictiveMetric(predictiveMetric);
				}
				// --- update MIN and MAX real-metric values ---
				if(realMetric < agentClassStorage.getMinRealMetric()){
					agentClassStorage.setMinRealMetric(realMetric);
				}else if(realMetric > agentClassStorage.getMaxRealMetric()){
					agentClassStorage.setMaxRealMetric(realMetric);
				}
				// --- calculate and update AVG metric values ---
				agentClassStorage.setAvgPredictiveMetric((agentClassStorage.getAvgPredictiveMetric()+predictiveMetric)/2);
				agentClassStorage.setAvgRealMetric((agentClassStorage.getAvgRealMetric()+realMetric)/2);
			}
			
			double value = 0;
			XYSeries actualSeries = null;
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(MAX_TOTAL_CPU_SYSTEM_TIME);
			int index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				if(systemTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), systemTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), systemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(AVG_TOTAL_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + userTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), userTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(MAX_TOTAL_CPU_USER_TIME);
			
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				if(userTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), userTime);
				}				
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), userTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(AVG_TOTAL_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + systemTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), systemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(MAX_DELTA_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				if(deltaSystemTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), deltaSystemTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(AVG_DELTA_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(MAX_DELTA_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				if(deltaUserTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), deltaUserTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
			}
			
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(AVG_DELTA_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + deltaUserTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
			}	
		}
		
		/******* CONTAINER
		 *  
		 * 
		 */
		
		// --- get data series ---
		XYSeries containerLoadCPUXYSeries = containerStorage.getXYSeriesMap().get(LOAD_CPU);
		XYSeries containerTotalCPUUserTimeXYSeries = containerStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		XYSeries containerTotalCPUSystemTimeXYSeries = containerStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);

		// --- add X/Y values to data series ---
		containerLoadCPUXYSeries.add(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
		containerTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUUsertime);
		containerTotalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUSystemTime);
		
		
		// --- calculate delta and add to delta data series  ---			
		double containerDeltaUserTime = containerStorage.getLastDeltaForXYSeries(containerTotalCPUUserTimeXYSeries);
		containerStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),containerDeltaUserTime);
		double containerDeltaSystemTime = containerStorage.getLastDeltaForXYSeries(containerTotalCPUSystemTimeXYSeries);
		containerStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),containerDeltaSystemTime);
		
		/******* JVM
		 *  
		 * 
		 */
		// --- get data series ---
		XYSeries jvmLoadCPUXYSeries = jvmStorage.getXYSeriesMap().get(LOAD_CPU);
		XYSeries jvmTotalCPUUserTimeXYSeries = jvmStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		XYSeries jvmTotalCPUSystemTimeXYSeries = jvmStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);

		// --- add X/Y values to data series ---
		jvmLoadCPUXYSeries.add(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
		jvmTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUUsertime);
		jvmTotalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUSystemTime);
		
		
		// --- calculate delta and add to delta data series  ---			
		double jvmDeltaUserTime = jvmStorage.getLastDeltaForXYSeries(jvmTotalCPUUserTimeXYSeries);
		jvmStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),jvmDeltaUserTime);
		double jvmDeltaSystemTime = jvmStorage.getLastDeltaForXYSeries(jvmTotalCPUSystemTimeXYSeries);
		jvmStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),jvmDeltaSystemTime);
		
		/******* MACHINE
		 *  
		 * 
		 */
		// --- get data series ---
		XYSeries machineLoadCPUXYSeries = machineStorage.getXYSeriesMap().get(LOAD_CPU);
		XYSeries machineTotalCPUUserTimeXYSeries = machineStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		XYSeries machineTotalCPUSystemTimeXYSeries = machineStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);

		// --- add X/Y values to data series ---
		machineLoadCPUXYSeries.add(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
		machineTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUUsertime);
		machineTotalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUSystemTime);
		
		
		// --- calculate delta and add to delta data series  ---			
		double deltaUserTime = machineStorage.getLastDeltaForXYSeries(machineTotalCPUUserTimeXYSeries);
		machineStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),deltaUserTime);
		double deltaSystemTime = machineStorage.getLastDeltaForXYSeries(machineTotalCPUSystemTimeXYSeries);
		machineStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),deltaSystemTime);
		
		/******* CLUSTER
		 *  
		 * 
		 */
		// --- get data series ---
		XYSeries clusterLoadCPUXYSeries = clusterStorage.getXYSeriesMap().get(LOAD_CPU);
		XYSeries clusterTotalCPUUserTimeXYSeries = clusterStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		XYSeries clusterTotalCPUSystemTimeXYSeries = clusterStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);

		// --- add X/Y values to data series ---
		clusterLoadCPUXYSeries.add(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
		clusterTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUUsertime);
		clusterTotalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), sumMachineTotalCPUSystemTime);
		
		
		// --- calculate delta and add to delta data series  ---			
		double clusterDeltaUserTime = clusterStorage.getLastDeltaForXYSeries(clusterTotalCPUUserTimeXYSeries);
		clusterStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),clusterDeltaUserTime);
		double clusterDeltaSystemTime = clusterStorage.getLastDeltaForXYSeries(clusterTotalCPUSystemTimeXYSeries);
		clusterStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),clusterDeltaSystemTime);
		
		// --- Add nodes to the tree model ----------
		this.addNodesToTreeModel(mapMachine, mapJVM, mapContainer, mapAgent);
		
		return done;
	}

	/* (non-Javadoc)
	 * @see java.util.Vector#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		
		mapAgent.clear();
		mapAgentClass.clear();
		mapMachine.clear();
		mapContainer.clear();
		mapJVM.clear();
		mapCluster.clear();
	}
	
	/**
	 * Gets the map machine.
	 * @return the map machine
	 */
	public HashMap<String, ThreadInfoStorageCluster> getMapCluster() {
		if(mapCluster == null){
			mapCluster = new HashMap<String, ThreadInfoStorageCluster>();
		}
		return mapCluster;
	}
	/**
	 * Gets the map machine.
	 * @return the map machine
	 */
	public HashMap<String, ThreadInfoStorageMachine> getMapMachine() {
		if(mapMachine == null){
			mapMachine = new HashMap<String, ThreadInfoStorageMachine>();
		}
		return mapMachine;
	}
	
	/**
	 * Gets the map container.
	 * @return the map container
	 */
	public HashMap<String, ThreadInfoStorageContainer> getMapContainer() {
		if(mapContainer == null){
			mapContainer = new HashMap<String, ThreadInfoStorageContainer>();
		}
		return mapContainer;
	}

	/**
	 * Gets the map jvm.
	 * @return the map jvm
	 */
	public HashMap<String, ThreadInfoStorageJVM> getMapJVM() {
		if(mapJVM == null){
			mapJVM = new HashMap<String, ThreadInfoStorageJVM>();
		}
		return mapJVM;
	}

	/**
	 * Gets the map agent.
	 * @return the map agent
	 */
	public HashMap<String, ThreadInfoStorageAgent> getMapAgent() {
		if(mapAgent == null){
			mapAgent = new HashMap<String, ThreadInfoStorageAgent>();
		}
		return mapAgent;
	}

	/**
	 * Gets the map agent class.
	 * @return the map agent class
	 */
	public HashMap<String, ThreadInfoStorageAgentClass> getMapAgentClass() {
		if(mapAgentClass == null){
			mapAgentClass = new HashMap<String, ThreadInfoStorageAgentClass>();
		}
		return mapAgentClass;
	}
	
	/**
	 * Gets the tree model.
	 * @return the tree model
	 */
	public DefaultTreeModel getTreeModel(){
		
		if (treeModel==null) {
		    treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Cluster"));
		    
		}
		return treeModel;
		
	}

	/**
	 * Adds the nodes to tree model.
	 * @param threadProtocol the thread protocol
	 */
	private void addNodesToTreeModel(
			HashMap<String,	ThreadInfoStorageMachine> mapMachine, 
			HashMap<String, ThreadInfoStorageJVM> mapJVM,
			HashMap<String, ThreadInfoStorageContainer> mapContainer, 
			HashMap<String, ThreadInfoStorageAgent> mapAgent) {
		
		/** leave if nothing has changed to avoid expensive iteration over sets
		 * hint: root never changes, so this is left out
		 */
		if(mapMachine.keySet().equals(this.machineSet) 
				&& mapJVM.keySet().equals(this.jvmSet) 
				&& mapContainer.keySet().equals(this.containerSet) 
				&& mapAgent.keySet().equals(this.agentSet)){
			return;
		}
		// --- get root-element ---
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTreeModel().getRoot();
		root.removeAllChildren();
		
		// ---update keysets ---
		machineSet 		= mapMachine.keySet();
		jvmSet 			= mapJVM.keySet();
		containerSet 	= mapContainer.keySet();
		agentSet 		= mapAgent.keySet();
		
		// -- set iterators ---
		Iterator<String> iteratorMachine = machineSet.iterator();
		Iterator<String> iteratorJvm = jvmSet.iterator();
		Iterator<String> iteratorContainer = containerSet.iterator();
		Iterator<String> iteratorAgent = agentSet.iterator();
		
		//--- set fix user object for root---
		root.setUserObject(mapCluster.get("Cluster"));
		// --- build tree ---
		while (iteratorMachine.hasNext()){
			
			String keyMachine = (String) iteratorMachine.next();
			DefaultMutableTreeNode machine = new DefaultMutableTreeNode(mapMachine.get(keyMachine));
	      
	      	if (root.getIndex((TreeNode)machine) == -1){
	      		root.add(machine);
	    	  
	      		while (iteratorJvm.hasNext()){
	      			String keyJVM = (String) iteratorJvm.next();
	      			DefaultMutableTreeNode jvm = new DefaultMutableTreeNode(mapJVM.get(keyJVM));
		  	      
	      			if (machine.getIndex((TreeNode)jvm) == -1){
	      				machine.add(jvm);
		  	    	
	      				while (iteratorContainer.hasNext()) {
	      					String keyContainer = (String) iteratorContainer.next();
	      					DefaultMutableTreeNode container = new DefaultMutableTreeNode(mapContainer.get(keyContainer));
				  	      
	      					if (jvm.getIndex((TreeNode)container) == -1){
	      						jvm.add(container);
				  	    	
	      						while (iteratorAgent.hasNext()) {
	      							String keyAgent = (String) iteratorAgent.next();
	      							DefaultMutableTreeNode agent = new DefaultMutableTreeNode(mapAgent.get(keyAgent));
						  	      
	      							if (container.getIndex((TreeNode)agent) == -1){
	      								container.add(agent);
						  	    	  
	      							}
	      						}  
	      					}
	      				}
	      			}
	      		} 
	      	}
		}
	
		getTreeModel().setRoot(root);
		getTreeModel().nodeChanged(root);
		return;
	}
	
	

}
