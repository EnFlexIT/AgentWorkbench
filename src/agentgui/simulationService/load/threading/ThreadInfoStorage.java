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

	private static final long serialVersionUID = 5821118253370178316L;
	
	/** The tree model. */
	private DefaultTreeModel treeModel;
	
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
		
		// --- add to hashmaps, XYSeries

		double machineTotalCPUSystemTime = 0;
		double machineTotalCPUUsertime = 0;
		
		int numberOfThreads = threadProtocol.getThreadTimes().size();
		for (int i = 0; i < numberOfThreads; i++) {
			
			////// AGENT //////
			String agentName = threadProtocol.getThreadTimes().get(i).toString();
			
			boolean isAgent = threadProtocol.getThreadTimes().get(i).isAgent();
			String classKey = "";
			if(isAgent == true){
				classKey = threadProtocol.getThreadTimes().get(i).getClassName();
			}else{
				classKey = "all-non-agent-classes";
			}
			
			ThreadInfoStorageAgent agentStorage = getMapAgent().get(agentName);
			if(agentStorage == null){
				// --- add agent to map
				mapAgent.put(agentName, new ThreadInfoStorageAgent(agentName, classKey, isAgent));
				// --- update ---
				agentStorage = mapAgent.get(agentName);
			}
			
			// --- get system-and user data series ---
			XYSeries totalCPUUserTimeXYSeries = agentStorage.getXYSeriesMap().get(agentStorage.TOTAL_CPU_USER_TIME);
			XYSeries totalCPUSystemTimeXYSeries = agentStorage.getXYSeriesMap().get(agentStorage.TOTAL_CPU_SYSTEM_TIME);
			
			// --- add X/Y value to total system-and user data series ---
			double userTime = threadProtocol.getThreadTimes().get(i).getUserTime();
			double systemTime = threadProtocol.getThreadTimes().get(i).getSystemTime();
			totalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), userTime);
			totalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), systemTime);
			
			machineTotalCPUUsertime = machineTotalCPUUsertime + userTime;
			machineTotalCPUSystemTime = machineTotalCPUSystemTime + systemTime;
			
			
			// --- calculate delta and add to delta data series  ---
			double deltaUserTime = agentStorage.getLastDeltaForXYSeries(totalCPUUserTimeXYSeries);
			double deltaSystemTime = agentStorage.getLastDeltaForXYSeries(totalCPUSystemTimeXYSeries);
			agentStorage.getXYSeriesMap().get(agentStorage.DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),deltaUserTime);
			agentStorage.getXYSeriesMap().get(agentStorage.DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),deltaSystemTime);
		
			
			////// AGENTCLASS //////
			ThreadInfoStorageAgentClass agentClassStorage = getMapAgentClass().get(classKey);
			if(agentClassStorage == null){
				// --- add agent to map
				mapAgentClass.put(classKey, new ThreadInfoStorageAgentClass(classKey));
				// --- update ---
				agentClassStorage = mapAgentClass.get(classKey);
			}
			
			// --- Metrics only exist for Agent-Threads ---
			if(isAgent == true){
				double predictiveMetric = threadProtocol.getThreadTimes().get(i).getPredictiveMetric();
				double realMetric = threadProtocol.getThreadTimes().get(i).getRealMetric();	
				
				// --- calculate and update min metric values ---
				if(predictiveMetric < agentClassStorage.getMinPredictiveMetric()){
					agentClassStorage.setMinPredictiveMetric(predictiveMetric);
				}else if(predictiveMetric > agentClassStorage.getMaxPredictiveMetric()){
					agentClassStorage.setMaxPredictiveMetric(predictiveMetric);
				}
				// --- calculate and update max metric values ---
				if(realMetric < agentClassStorage.getMinRealMetric()){
					agentClassStorage.setMinRealMetric(realMetric);
				}else if(realMetric > agentClassStorage.getMaxRealMetric()){
					agentClassStorage.setMaxRealMetric(realMetric);
				}
				// --- calculate and update avg metric values ---
				agentClassStorage.setAvgPredictiveMetric((agentClassStorage.getAvgPredictiveMetric()+predictiveMetric)/2);
				agentClassStorage.setAvgRealMetric((agentClassStorage.getAvgRealMetric()+realMetric)/2);
			}
			
			double value;
			
			XYSeries actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.MAX_TOTAL_CPU_SYSTEM_TIME);
			int index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				if(systemTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), systemTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), systemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.AVG_TOTAL_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value+userTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), userTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.AVG_TOTAL_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value+systemTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), systemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.MAX_TOTAL_CPU_USER_TIME);
			
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				if(userTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), userTime);
				}				
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), userTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.MAX_DELTA_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				if(deltaSystemTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), deltaSystemTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.MAX_DELTA_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				if(deltaUserTime > value){
					actualSeries.update(threadProtocol.getTimestamp(), deltaUserTime);
				}
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
			}
			
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.AVG_DELTA_CPU_SYSTEM_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value+deltaSystemTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaSystemTime);
			}
			
			actualSeries = agentClassStorage.getXYSeriesMap().get(agentClassStorage.AVG_DELTA_CPU_USER_TIME);
			index = actualSeries.indexOf(threadProtocol.getTimestamp());
			if(index >= 0){
				value = (Double)actualSeries.getY(index);
				actualSeries.update(threadProtocol.getTimestamp(), (value+deltaUserTime)/2);
			}else{//first entry
				actualSeries.add(threadProtocol.getTimestamp(), deltaUserTime);
			}	
		}
		///// Machine //////
		String machineName = threadProtocol.getMachineName();
		ThreadInfoStorageMachine machineStorage = getMapMachine().get(machineName);
		if(machineStorage == null){
			// --- add agent to map
			mapMachine.put(machineName, new ThreadInfoStorageMachine(machineName));
			
			// --- update ---
			machineStorage = mapMachine.get(machineName);
		}
		//// JVM ////
		String jvmName = threadProtocol.getJVMName();
		ThreadInfoStorageJVM jvmStorage = getMapJVM().get(jvmName);
		if(jvmStorage == null){
			// --- add agent to map
			mapJVM.put(jvmName, new ThreadInfoStorageJVM(jvmName));
			// --- update ---
			jvmStorage = mapJVM.get(jvmName);
		}
		//// CONTAINER ////
		String containerName = threadProtocol.getContainerName();
		ThreadInfoStorageContainer containerStorage = getMapContainer().get(containerName);
		if(containerStorage == null){
			// --- add agent to map
			mapContainer.put(containerName, new ThreadInfoStorageContainer(containerName));
			// --- update ---
			containerStorage = mapContainer.get(containerName);
		}
		
		// --- get data series ---
		XYSeries loadCPUXYSeries = machineStorage.getXYSeriesMap().get(machineStorage.LOAD_CPU);
		XYSeries totalCPUUserTimeXYSeries = machineStorage.getXYSeriesMap().get(machineStorage.TOTAL_CPU_USER_TIME);
		XYSeries totalCPUSystemTimeXYSeries = machineStorage.getXYSeriesMap().get(machineStorage.TOTAL_CPU_SYSTEM_TIME);

		// --- add X/Y values to data series ---
		loadCPUXYSeries.add(threadProtocol.getTimestamp(), threadProtocol.getLoadCPU());
		totalCPUUserTimeXYSeries.add(threadProtocol.getTimestamp(), machineTotalCPUUsertime);
		totalCPUSystemTimeXYSeries.add(threadProtocol.getTimestamp(), machineTotalCPUSystemTime);
		
		
		// --- calculate delta and add to delta data series  ---			
		double deltaUserTime = machineStorage.getLastDeltaForXYSeries(totalCPUUserTimeXYSeries);
		machineStorage.getXYSeriesMap().get(machineStorage.DELTA_CPU_USER_TIME).add(threadProtocol.getTimestamp(),deltaUserTime);
		double deltaSystemTime = machineStorage.getLastDeltaForXYSeries(totalCPUSystemTimeXYSeries);
		machineStorage.getXYSeriesMap().get(machineStorage.DELTA_CPU_SYSTEM_TIME).add(threadProtocol.getTimestamp(),deltaSystemTime);
		
		// --- Add nodes to the tree model ----------
		this.addNodesToTreeModel(mapMachine, mapJVM, mapContainer, mapAgent);
		
		return done;
	}

	@Override
	public void clear() {
		super.clear();
		
		mapAgent.clear();
		mapAgentClass.clear();
		mapMachine.clear();
	}
	
	/**
	 * Gets the map machine.
	 * @return the map machine
	 */
	public HashMap<String, ThreadInfoStorageMachine> getMapMachine() {
		if(mapMachine == null){
			this.mapMachine = new HashMap<String, ThreadInfoStorageMachine>();
		}
		return mapMachine;
	}
	
	/**
	 * Gets the map container.
	 * @return the map container
	 */
	public HashMap<String, ThreadInfoStorageContainer> getMapContainer() {
		if(mapContainer == null){
			this.mapContainer = new HashMap<String, ThreadInfoStorageContainer>();
		}
		return mapContainer;
	}

	/**
	 * Gets the map jvm.
	 * @return the map jvm
	 */
	public HashMap<String, ThreadInfoStorageJVM> getMapJVM() {
		if(mapJVM == null){
			this.mapJVM = new HashMap<String, ThreadInfoStorageJVM>();
		}
		return mapJVM;
	}

	/**
	 * Gets the map agent.
	 * @return the map agent
	 */
	public HashMap<String, ThreadInfoStorageAgent> getMapAgent() {
		if(mapAgent == null){
			this.mapAgent = new HashMap<String, ThreadInfoStorageAgent>();
		}
		return mapAgent;
	}

	/**
	 * Gets the map agent class.
	 * @return the map agent class
	 */
	public HashMap<String, ThreadInfoStorageAgentClass> getMapAgentClass() {
		if(mapAgentClass == null){
			this.mapAgentClass = new HashMap<String, ThreadInfoStorageAgentClass>();
		}
		return mapAgentClass;
	}
	
	/**
	 * Gets the tree model.
	 * @return the tree model
	 */
	public DefaultTreeModel getTreeModel(){
		
		if (treeModel==null) {
		    this.treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Machines"));
		    
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
		
		//--- leave if nothing has changed to avoid expensive iteration over sets
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
		machineSet 	= mapMachine.keySet();
		jvmSet 		= mapJVM.keySet();
		containerSet 	= mapContainer.keySet();
		agentSet 		= mapAgent.keySet();
		
		// -- set iterators ---
		Iterator<String> iteratorMachine = machineSet.iterator();
		Iterator<String> iteratorJvm = jvmSet.iterator();
		Iterator<String> iteratorContainer = containerSet.iterator();
		Iterator<String> iteratorAgent = agentSet.iterator();
		
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
