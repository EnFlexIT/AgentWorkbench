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
package agentgui.simulationService.load.threading.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jfree.data.xy.XYSeries;

import agentgui.simulationService.load.threading.ThreadCalculateMetrics;
import agentgui.simulationService.load.threading.ThreadDetail;
import agentgui.simulationService.load.threading.ThreadProtocol;

/**
 * Storage class for relevant load information about agents/threads.
 * 
 * Analysis the received ThredProtocol and adds data to the corresponding
 * XY-Series, calculates moving averages and delta-values.
 * Updates table model of ThreadMonitorProtocolTableTab and tree model
 * of ThreadMonitorDetailTreeTab
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorage extends Vector<ThreadProtocol> {
	
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
	public final String AVG_LOAD_CPU 			  	= "AVG_LOAD_CPU";
	
	public final String DELIMITER   				= "<>";
	public final String AT   						= "@";
	public final String CLUSTER   					= "Cluster";

	private static final long serialVersionUID = 5821118253370178316L;
	
	/** The tree model. */
	private DefaultTreeModel model;
	
	/** The table model. */
	private DefaultTableModel tableModel;
	
	/** The root node. */
	private DefaultMutableTreeNode rootNode;
	
	/** The Hash-Map that Stores ThreadInfo of cluster. */
	private HashMap<String, ThreadInfoStorageCluster> mapCluster;

	
	/** The Hash-Map that Stores ThreadInfo of each machine. */
	private HashMap<String, ThreadInfoStorageMachine> mapMachine;
	
	/** The Hash-Map that Stores ThreadInfo of each JavaVirtualMachine. */
	private HashMap<String, ThreadInfoStorageJVM> mapJVM;
	
	/** The Hash-Map that Stores ThreadInfo of each Container. */
	private HashMap<String, ThreadInfoStorageContainer> mapContainer;
	
	/** The Hash-Map that Stores ThreadInfo of each agent. */
	private HashMap<String, ThreadInfoStorageAgent> mapAgent;
	
	/** The Hash-Map that Stores ThreadInfo of each agent-class. */
	private HashMap<String, ThreadInfoStorageAgentClass> mapAgentClass;
	
	/** The no of agents per class. */
	private HashMap<String, Integer> noOfAgentsPerClass;
	
	/** The calculation of metrics. */
	private ThreadCalculateMetrics threadCalculateMetrics;

	/** The JVM set. */
	private Set<String> jvmSet;
	
	/** The machine set. */
	private Set<String> machineSet;
	
	/** The container set. */
	private Set<String> containerSet;
	
	/** The agent set. */
	private Set<String> agentSet;
	
	/**
	 * Instantiates a new thread info storage.
	 */
	public ThreadInfoStorage() {
		initialize();
	}

	private void initialize(){
		jvmSet = new HashSet<String>();
		machineSet = new HashSet<String>();
		containerSet = new HashSet<String>();
		agentSet = new HashSet<String>();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(ThreadProtocol threadProtocol) {
		
		// --- Add local vector ---
		boolean done = super.add(threadProtocol);
		
		double value = 0;
		double deltaUserTime = 0;
		double deltaSystemTime = 0;
		int index = 0;
		XYSeries actualSeries = null;
		
		double sumTotalCPUSystemTime = 0;
		double sumTotalCPUUsertime = 0;
		
		/*
		 * Initialize dynamically on new protocol arrival
		 * 
		 * 
		 */
		
		String machineName = threadProtocol.getMachineName();
		String jvmName = threadProtocol.getJVMName()+AT+machineName;
		String containerName = threadProtocol.getContainerName()+AT+jvmName;
		String clusterName = CLUSTER;
		
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
			
			machineStorage.setMflops(threadProtocol.getMflops());
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
			clusterStorage.getXYSeriesMap().put(AVG_LOAD_CPU, new XYSeries(AVG_LOAD_CPU+DELIMITER+clusterName));
			mapCluster.put(clusterName, clusterStorage);
		}
		
		// --- Get thread times from protocol, calculate values and put them to time series
		for (int i = 0; i < threadProtocol.getThreadDetails().size(); i++) {
			
			/******* AGENT
			 * simply add data to series
			 */
			int noOfAgents = 1;

			ThreadDetail tDetail = threadProtocol.getThreadDetails().get(i);
			String agentName = tDetail.toString() + AT + containerName;
			String classKey = tDetail.getClassName();
			boolean isAgent = tDetail.isAgent();
			
			if (this.getNoOfAgentsPerClass().get(classKey)==null) {
				noOfAgentsPerClass.put(classKey, 0);
			}
			
			ThreadInfoStorageAgent agentStorage = getMapAgent().get(agentName);
			if (agentStorage == null) {
				// --- add agent to map
				agentStorage = new ThreadInfoStorageAgent(agentName, classKey, isAgent);
				agentStorage.getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+agentName));
				agentStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+agentName));
				mapAgent.put(agentName, agentStorage);
				
				// --- add to agent-counter for class, neccessary for average ---
				noOfAgentsPerClass.put(classKey,(noOfAgentsPerClass.get(classKey) + 1));
			}
			
			noOfAgents = getNoOfAgentsPerClass().get(classKey);
			
			// --- get system-and user data series ---
			XYSeries agentTotalCPUUserTimeXYSeries = agentStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
			XYSeries totalCPUSystemTimeXYSeries = agentStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
			
			// --- get total system-and user time ---
			double userTime = threadProtocol.getThreadDetails().get(i).getUserTime();
			double systemTime = threadProtocol.getThreadDetails().get(i).getSystemTime();
			
			//--- update with time of actual thread ---
			sumTotalCPUUsertime = sumTotalCPUUsertime + userTime;
			sumTotalCPUSystemTime = sumTotalCPUSystemTime + systemTime;
			
			// --- add total times to total data series ---
			agentTotalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), userTime);
			totalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), systemTime);			
			// --- calculate delta and add to delta data series  ---
			deltaUserTime = agentStorage.getLastDeltaForXYSeries(agentTotalCPUUserTimeXYSeries);
			deltaSystemTime = agentStorage.getLastDeltaForXYSeries(totalCPUSystemTimeXYSeries);
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
				agentClassStorage.getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME,new XYSeries(DELTA_CPU_SYSTEM_TIME + DELIMITER + classKey));	
				agentClassStorage.getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME,new XYSeries(TOTAL_CPU_SYSTEM_TIME + DELIMITER + classKey));
				
				mapAgentClass.put(classKey, agentClassStorage);
				
			}			

			// --- Metrics only exist for Agent-Threads ---
			if (isAgent==true) {
				
				double predictiveMetric = threadProtocol.getThreadDetails().get(i).getPredictiveMetric();
				double realMetric = threadProtocol.getThreadDetails().get(i).getRealMetric();	
				
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
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + systemTime));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), systemTime);
			}
			actualSeries = agentClassStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double) actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(MAX_TOTAL_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
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
				actualSeries.update(threadProtocol.getTimestamp(), (value + (userTime/noOfAgents)));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), (userTime/noOfAgents));
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
				actualSeries.update(threadProtocol.getTimestamp(), (value + (systemTime/noOfAgents)));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), (systemTime/noOfAgents));
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
				actualSeries.update(threadProtocol.getTimestamp(), (value + (deltaSystemTime/noOfAgents)));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), (deltaSystemTime/noOfAgents));
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
				actualSeries.update(threadProtocol.getTimestamp(), (value + (deltaUserTime/noOfAgents)));
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), (deltaUserTime/noOfAgents));
			}	
		}
		
		/******* CONTAINER
		 *  
		 * 
		 */
		// --- CPU load, not summed up ---
		containerStorage.getXYSeriesMap().get(LOAD_CPU).addOrUpdate(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
				
		actualSeries = containerStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUUsertime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUUsertime);
		}
		deltaUserTime = containerStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = containerStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaUserTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
		}
		
		actualSeries = containerStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUSystemTime);
		}
		deltaSystemTime = containerStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = containerStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
		}
		
		/******* JVM
		 *  
		 * 
		 */

		
		// --- CPU load, not summed up ---
		jvmStorage.getXYSeriesMap().get(LOAD_CPU).addOrUpdate(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
				
		actualSeries = jvmStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUUsertime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUUsertime);
		}
		deltaUserTime = jvmStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = jvmStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaUserTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
		}
		
		actualSeries = jvmStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUSystemTime);
		}
		deltaSystemTime = jvmStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = jvmStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
		}
		
		/******* MACHINE
		 *  
		 * 
		 */		
		
		// --- CPU load, not summed up ---
		machineStorage.getXYSeriesMap().get(LOAD_CPU).addOrUpdate(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
						
		actualSeries = machineStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUUsertime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUUsertime);
		}
		deltaUserTime = machineStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = machineStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaUserTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
		}
		
		actualSeries = machineStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUSystemTime);
		}
		deltaSystemTime = machineStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = machineStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
		}
		
		//update actual average Load
		machineStorage.setActualAverageLoadCPU(getThreadMeasureMetrics().getAverageCPUPercentage(machineStorage));

		/*******
		 * CLUSTER
		 * 
		 * 
		 */
		//TODO: CPU chart for each machine in one window ?

		actualSeries = clusterStorage.getXYSeriesMap().get(AVG_LOAD_CPU);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if (index >= 0) {
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(),(value + (threadProtocol.getLoadCPU() / getMapMachine().size())));
		} else {// first entry
			actualSeries.add(threadProtocol.getTimestamp(),
					threadProtocol.getLoadCPU() / getMapMachine().size());
		}
								
		actualSeries = clusterStorage.getXYSeriesMap().get(TOTAL_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUUsertime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUUsertime);
		}
		deltaUserTime = clusterStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = clusterStorage.getXYSeriesMap().get(DELTA_CPU_USER_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaUserTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
		}
		
		actualSeries = clusterStorage.getXYSeriesMap().get(TOTAL_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + sumTotalCPUSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), sumTotalCPUSystemTime);
		}
		deltaSystemTime = clusterStorage.getLastDeltaForXYSeries(actualSeries);
		
		actualSeries = clusterStorage.getXYSeriesMap().get(DELTA_CPU_SYSTEM_TIME);
		index = actualSeries.indexOf(threadProtocol.getTimestamp());
		if(index >= 0){
			value = (Double) actualSeries.getY(index);
			actualSeries.update(threadProtocol.getTimestamp(), (value + deltaSystemTime));
		}else{//first entry
			actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
		}
		
		//TODO: Observer  pattern notifyObserver
		// --- Add (new) nodes to the tree model ----------
		this.addNodesToTreeModel(mapMachine, mapJVM, mapContainer, mapAgent);
		
		// --- add data to table model ---
		this.clearTableModel();
		this.addTableModelRow(mapAgent);
		
		//update metrics
		this.getThreadMeasureMetrics().getMetrics();
		
		return done;
	}
	
	
	/**
	 * Gets the table model for this {@link ThreadInfoStorage}.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("Thread Name");
			header.add("Class");
			header.add("Instances Of Class");
			header.add("Real Metric");
			
			tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;

				/* (non-Javadoc)
				 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
				 */
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int column){
					if (column >= 0 && column <= getColumnCount()) {
						return getValueAt(0, column).getClass();
					} else {
						return Object.class;
					}
				}
			};
			// --- Necessary for preventing sorter from throwing error about empty row
			this.addTableModelRow(null);
		}
		return tableModel;
	}
	
	/**
	 * Adds a row to the table model.
	 *
	 * @param mapAgent the map agent
	 */
	private void addTableModelRow(HashMap<String, ThreadInfoStorageAgent> mapAgent) {
		
		if (mapAgent==null) {
			ThreadInfoStorageAgent actualAgent = new ThreadInfoStorageAgent("", "", false);
			
			Vector<Object> row = new Vector<Object>();
			row.add(actualAgent);
			row.add("");
			row.add(0);
			row.add(0);
			getTableModel().addRow(row);
			
		} else {
			
			ArrayList<String> agentNames = new ArrayList<>( mapAgent.keySet());  
			for (int i = 0; i < agentNames.size(); i++) {
				
				String agentName = agentNames.get(i);
				ThreadInfoStorageAgent threadInfoAgent = mapAgent.get(agentName);
				
				// --- Set the class name entry -----------------------------
				String className = threadInfoAgent.getClassName();
				if (className.equals(ThreadDetail.UNKNOWN_THREAD_CLASSNAME) || className.equals(ThreadDetail.UNKNOWN_AGENT_CLASSNAME)) {
					String[] classNameSplitArray = className.split("\\.");
					className = classNameSplitArray[classNameSplitArray.length-1];
				}
				
				Vector<Object> row = new Vector<Object>();
				row.add(threadInfoAgent);
				row.add(className);
				row.add(getNoOfAgentsPerClass().get(threadInfoAgent.getClassName()));
				
				// --- remove decimal .000 ----
				row.add(Math.round(threadInfoAgent.getRealMetric()));
				
				// --- Add row to table model ---
				getTableModel().addRow(row);
			}
		}
	}
	
	/**
	 * Clears the table model.
	 */
	private void clearTableModel() {
		while (getTableModel().getRowCount()>0) {
			getTableModel().removeRow(0);
		}
	}
	
	
	/**
	 * Adds the nodes to tree model.
	 *
	 * @param mapMachine the map machine
	 * @param mapJVM the map JVM
	 * @param mapContainer the map container
	 * @param mapAgent the map agent
	 * @param mapCluster the map cluster
	 */
	private void addNodesToTreeModel(
			HashMap<String,	ThreadInfoStorageMachine> mapMachine, 
			HashMap<String, ThreadInfoStorageJVM> mapJVM,
			HashMap<String, ThreadInfoStorageContainer> mapContainer, 
			HashMap<String, ThreadInfoStorageAgent> mapAgent) {
		
		// --- get root-element ---
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
		
		/** leave if nothing has changed to avoid expensive iteration over sets
		 * hint: root never changes, so this is left out
		 */
		if(mapMachine.keySet().equals(machineSet) 
				&& mapJVM.keySet().equals(jvmSet) 
				&& mapContainer.keySet().equals(containerSet) 
				&& mapAgent.keySet().equals(agentSet)){

			//---sort threads in overwritten method ---
			getModel().nodeChanged(root);
			return;
		}
		
		root.removeAllChildren();
		
		// ---update keysets ---
		machineSet.addAll(mapMachine.keySet());
		jvmSet.addAll(mapJVM.keySet());
		containerSet.addAll(mapContainer.keySet());
		agentSet.addAll(mapAgent.keySet());
		
		// -- set iterators ---
		Iterator<String> iteratorMachine = machineSet.iterator();
		Iterator<String> iteratorJvm = jvmSet.iterator();
		Iterator<String> iteratorContainer = containerSet.iterator();
		Iterator<String> iteratorAgent = agentSet.iterator();
		
		//--- set fix user object for root---
		root.setUserObject(mapCluster.get(CLUSTER));
		// --- build tree ---
		while (iteratorMachine.hasNext()){
			
			String keyMachine = (String) iteratorMachine.next();
			DefaultMutableTreeNode machine = new DefaultMutableTreeNode(mapMachine.get(keyMachine));
	      
	      	if (root.getIndex((TreeNode)machine) == -1){
	      		root.add(machine);
	    	  
	      		while (iteratorJvm.hasNext()){
	      			String keyJVM = (String) iteratorJvm.next();
	      			DefaultMutableTreeNode jvm = new DefaultMutableTreeNode(mapJVM.get(keyJVM));
	      			
	      			if (machine.getIndex((TreeNode)jvm) == -1 && ((ThreadInfoStorageXYSeries) jvm.getUserObject()).getName().contains(((ThreadInfoStorageXYSeries) machine.getUserObject()).getName())){
	      				machine.add(jvm);
		  	    	
	      				while (iteratorContainer.hasNext()) {
	      					String keyContainer = (String) iteratorContainer.next();
	      					DefaultMutableTreeNode container = new DefaultMutableTreeNode(mapContainer.get(keyContainer));
				  	      
	      					if (jvm.getIndex((TreeNode)container) == -1 && ((ThreadInfoStorageXYSeries) container.getUserObject()).getName().contains(((ThreadInfoStorageXYSeries) jvm.getUserObject()).getName())){
	      						jvm.add(container);
				  	    	
	      						while (iteratorAgent.hasNext()) {
	      							String keyAgent = (String) iteratorAgent.next();
	      							DefaultMutableTreeNode agent = new DefaultMutableTreeNode(mapAgent.get(keyAgent));
						  	      
	      							if (container.getIndex((TreeNode)agent) == -1 && ((ThreadInfoStorageXYSeries) agent.getUserObject()).getName().contains(((ThreadInfoStorageXYSeries) container.getUserObject()).getName())){
	      								container.add(agent);
						  	    	  
	      							}
	      						}  
	      						iteratorAgent = agentSet.iterator();
	      					}
	      				}
	      				iteratorContainer = containerSet.iterator();
	      			}
	      		} 
	      		iteratorJvm = jvmSet.iterator();
	      	}
		}
		rootNode = root;
		getModel().setRoot(root);
		getModel().reload(root);
		getModel().nodeStructureChanged(root);
		return;
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
	 * Gets the map JVM.
	 * @return the map JVM
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
	 * Gets the number of agents per class.
	 * @return the number of agents per class
	 */
	public HashMap<String, Integer> getNoOfAgentsPerClass() {
		if (noOfAgentsPerClass == null) {
			noOfAgentsPerClass = new HashMap<String, Integer>();
		}
		return noOfAgentsPerClass;
	}
	
	/**
	 * Gets the thread measure metrics.
	 *
	 * @return the thread measure metrics
	 */
	public ThreadCalculateMetrics getThreadMeasureMetrics(){
		if(threadCalculateMetrics == null){
			threadCalculateMetrics = new ThreadCalculateMetrics(this, "CALC_TYPE_INTEGRAL_DELTA", "METRIC_BASE_CLASS");
		}
		return threadCalculateMetrics;
	}
	
	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public DefaultTreeModel getModel() {
		if (model == null) {
			model = new DefaultTreeModel( new DefaultMutableTreeNode(CLUSTER) );
		}
		return model;
	}
	
	/**
	 * Gets the root node.
	 *
	 * @return the root node
	 */
	public DefaultMutableTreeNode getRootNode() {
		if (rootNode == null) {
			rootNode = new DefaultMutableTreeNode(CLUSTER);
		}
		return rootNode;
	}
}
