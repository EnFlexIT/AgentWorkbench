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
import jade.util.leap.List;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.gui.projectwindow.Distribution;
import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.ClientAvailableMachinesReply;
import agentgui.simulationService.ontology.MachineDescription;

/**
 * This class is for static distribution of Agents based on given metrics,
 * predictive or real (empirical)
 * @see StaticLoadBalancingBase
 * @see BaseLoadBalancing
 * @see Distribution
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class PredictiveStaticLoadBalancing extends StaticLoadBalancingBase{
	
	private static final long serialVersionUID = -6884445863598676300L;
	
	/** local constants */
	private static final double LOAD_CPU_IDEAL_PERCENT = 70 ;//default 70
	private static final double LOAD_CPU_CRIT_PERCENT  = 80 ;//default 80
	private static final double LOAD_CPU_CRIT_DANGER  = 90 ;//default 80
	private static final double LOAD_CPU_IDEAL = (LOAD_CPU_IDEAL_PERCENT/100) ;
	private static final double LOAD_CPU_CRIT  = (LOAD_CPU_CRIT_PERCENT/100) ;
	
	/** The load cpu ideal. */
	public double loadCpuIdeal,loadCpuCrit;
	
	/** The remote only boolean, do not use local machine for agents. */
	private boolean isRemoteOnly;
	
	/** The boolean verbose. */
	private boolean verbose;
	
	
	/**
	 * Instantiates a new predictive static load balancing.
	 *
	 * @param agent the agent
	 */
	public PredictiveStaticLoadBalancing(LoadExecutionAgent agent) {
		super(agent);
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		
		this.verbose = false; //TODO: menu option ?? global setting ??
		
		isRemoteOnly = currDisSetup.isRemoteOnly();
		
		if(currDisSetup.isUseUserThresholds()){
			loadCpuIdeal =  (float)currDisSetup.getUserThresholds().getThCpuH()/100;
			loadCpuCrit  =  (float)currDisSetup.getUserThresholds().getThCpuH()/100;
		}else{
			loadCpuIdeal = LOAD_CPU_IDEAL;
			loadCpuCrit  = LOAD_CPU_CRIT;
		}		
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.balancing.BaseLoadBalancingInterface#doBalancing()
	 */
	@Override
	public void doBalancing() {//one-shot behavior
		
		this.initialize();
		
		// --------------------------------------------------------------------------------------------------
		// --- Distribution is based on predictive metrics, thus: 										-----
		// --- => 1. Start the remote containers and get their locations								-----
		// --- => 2. Get the metrics (benchmark) of remote container                    				-----
		// --- => 3. Determine the predictive distribution of all agents from ('this.currAgentList')	----- 
		// --- => 4. distribute the agents according to calculation										-----
		// ---   (Agents defined in visualization-setup will be distributed as well)					-----
		// --------------------------------------------------------------------------------------------------	
		
		Hashtable<String, Location> containerLocations = this.getLocationsOfStartedRemoteContainers(this.verbose);
		if(containerLocations != null){
			
			Hashtable<Location, Double> cpuBenchmark = this.getBenchmarkOfContainer(containerLocations, this.verbose);
			if(cpuBenchmark != null){
				
				System.out.println("START (Predictive, static distribution)");
				
				Hashtable<Location, ArrayList<AgentClassElement4SimStart>> containerList = getAgentContainerList4PredictiveDistribution(containerLocations, cpuBenchmark, this.verbose);
				if(containerList != null){
					this.startAgentsOnContainers(containerList, this.verbose);
				}
				
				System.out.println("FINISHED (Predictive, static distribution)");
			}
		}
	}
	
	
	/**
	 * Start remote container on machines.
	 *
	 * @param verbose the verbose
	 * @return the locations of started remote containers
	 */
	private Hashtable<String, Location> getLocationsOfStartedRemoteContainers(boolean verbose) {

		/** All container/locations */
		Hashtable<String, Location> newContainerLocations;
		
		/** The number of remote containers. */
		int numberOfRemoteSlaveMachines = 0;
		
		try {
			ClientAvailableMachinesReply availableMachinesReply =  loadHelper.getAvailableMachines();
			
			if(availableMachinesReply != null){
				if(verbose){
					System.out.println("Server.Master registered the following:");
				}
				List availableMachines = availableMachinesReply.getAvailableMachines();
				Iterator<?> availableMachinesIterator = availableMachines.iterator();
				
				while(availableMachinesIterator.hasNext()){
					MachineDescription machineDescription = (MachineDescription) availableMachinesIterator.next();
					
					if(machineDescription.getContactAgent().contains("server.slave") 
							&& machineDescription.getIsAvailable() == true 
							&& machineDescription.getIsThresholdExceeded() == false){
						numberOfRemoteSlaveMachines++;
					}
					
					if(verbose){
						String role = "UNKNOWN";
						if(machineDescription.getContactAgent().contains("server.slave")){
							role = "SLAVE :";
						}else if(machineDescription.getContactAgent().contains("server.client")){
							role = "CLIENT:";
						}else if(machineDescription.getContactAgent().contains("server.master")){
							role = "MASTER:";
						}
						System.out.println(role + machineDescription.getPlatformAddress().getIp() + ", AVAILABLE: " + machineDescription.getIsAvailable() + ", TRESHOLD: " + machineDescription.getIsThresholdExceeded());
					}
				}
			}		
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		if(numberOfRemoteSlaveMachines != 0){
			/** start remote locations*/
			newContainerLocations = startNumberOfRemoteContainer(numberOfRemoteSlaveMachines , true, null);
			return newContainerLocations;
			
		}else{
			// --- Just start all defined agents locally ---
//			if(verbose){
				System.out.println("NO REMOTE MACHINES AVAILABLE, starting agents locally on container '" + currProject.getProjectFolder() + "'");
//			}
			startAgentsFromCurrAgentList(this.verbose);
		}
		return null;
	}
	
	
	
	/**
	 * Gets the total benchmark of a container.
	 *
	 * @param containerLocations the container locations
	 * @param verbose verbose 
	 * @return the benchmark of container
	 */
	private Hashtable<Location, Double> getBenchmarkOfContainer(Hashtable<String, Location> containerLocations, boolean verbose){
		
		if (containerLocations != null) {
			/** The CPU benchmark values (MFLOPS  * No of CPUs) of each container*/
			Hashtable<Location, Double> cpu = new Hashtable<Location, Double>();		
			/** All container names */
			Vector<String> containerNames = new Vector<String>(containerLocations.keySet());
			/** The actual remote container*/
			Location remoteContainer;
			
			// iterate over container
			Iterator<String> locationNamesIterator = containerNames.iterator();
			
			while (locationNamesIterator.hasNext()) {
				
				remoteContainer = containerLocations.get(locationNamesIterator.next());
				
				// --- Get the benchmark-result for this node/container -------------
				NodeDescription containerDesc;
				try {
					containerDesc = loadHelper.getContainerDescription(remoteContainer.getName());
					
					Double mflopsBenchmarkValueTotal = (double) containerDesc.getBenchmarkValue().getBenchmarkValue() * containerDesc.getPlPerformace().getCpu_numberOf();
					mflopsBenchmarkValueTotal = Math.round(mflopsBenchmarkValueTotal*100)/100.0;
					cpu.put(remoteContainer, mflopsBenchmarkValueTotal);
					
					if(verbose){
						System.out.println(remoteContainer.getName()+ ", Benchmark:"+ cpu.get(remoteContainer) + " MFLOPS, ");					
					}
				} catch (ServiceException e) {
					System.out.println("No container description available.");
					e.printStackTrace();
				}
			}
			return cpu;
		}
		return null;
	}
	 
	/**
	 * Gets the agent container list4 predictive distribution.
	 * Calculates the predictive distribution and assigns agents to container-list
	 *
	 * @param containerLocations the container locations
	 * @param cpuBenchmark the CPU benchmark
	 * @param verbose the verbose
	 * @return the agent container list for predictive distribution
	 */
	@SuppressWarnings("unchecked")
	private Hashtable<Location, ArrayList<AgentClassElement4SimStart>> getAgentContainerList4PredictiveDistribution(Hashtable<String, Location> containerLocations, Hashtable<Location, Double> cpuBenchmark, boolean verbose){
		/** a mapping of agents to containers*/
		Hashtable<Location, ArrayList<AgentClassElement4SimStart>> agentContainerList = new Hashtable<Location,ArrayList<AgentClassElement4SimStart>>();
		/** The local container*/
		Location localContainer = containerLocations.get(currProject.getProjectFolder());
		/** A list of agents*/
		ArrayList<AgentClassElement4SimStart> agentList = new ArrayList<AgentClassElement4SimStart>();
		
		/** All container names */
		Vector<String> containerNames = new Vector<String>(containerLocations.keySet());
		
		/** temp for sum of CPU metrics*/
		Hashtable<Location, Double> cpuContainerSum = new Hashtable<Location, Double>();
		
		/** The change container boolean. */
		boolean isChangeContainer = false;
		/** The actual remote container*/
		Location remoteContainer;
		/** The remote container name. */
		String remoteContainerName;		
		
		if (currAgentList!=null && containerNames != null) {
			// sort local container to last position --> remote container to be occupied first
			int indexLocalContainer = containerNames.indexOf(localContainer.getName());
			containerNames.remove(indexLocalContainer);
			if(isRemoteOnly == false){
				containerNames.add(localContainer.getName());
			}
			
			/** Set Iterator for containerNames **/
			Iterator<String> containerIteratorOverload = null;
			boolean modeOverload = false;
			Iterator<String> containerIterator = containerNames.iterator();
			
			remoteContainerName = containerIterator.next();
			
			if(verbose){
				System.out.println("Container: " + remoteContainerName);					
			}
			
			/** Iterate over agent start list and determine the container to start on**/
			ArrayList<AgentClassElement4SimStart> agents2bStartedList = currSimSetup.getAgentList();
			
			for (Iterator<AgentClassElement4SimStart> agents2bStartedIterator = agents2bStartedList.iterator(); agents2bStartedIterator.hasNext();) {
				
				AgentClassElement4SimStart agent = agents2bStartedIterator.next();
				remoteContainer = containerLocations.get(remoteContainerName);
				
				String  classname = agent.getAgentClassReference();
				int index = currProject.getAgentClassLoadMetrics().getIndexOfAgentClassMetricDescription(classname);
				double metric;
				
				if(index != -1 && !classname.contains("SimulationManager")){
					//Load-balancing Agents to be distributed
					
					//still enough "space" on container ?
					if((cpuContainerSum.get(remoteContainer) == null	/* no agents on this container yet*/
					   || (cpuBenchmark.get(remoteContainer)*loadCpuCrit) >= cpuContainerSum.get(remoteContainer)) /* limits not reached*/
					   || modeOverload){			/* overload mode*/
						//update sum CPU metrics
						
						if(currProject.getAgentClassLoadMetrics().isUseRealLoadMetric() == true){
							metric = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector().get(index).getRealMetricAverage();
						}else{
							metric = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector().get(index).getUserPredictedMetric();	
						}
						
						// add up
						if(cpuContainerSum.get(remoteContainer)!=null){
							cpuContainerSum.put(remoteContainer, cpuContainerSum.get(remoteContainer) + metric);
						}else{
							cpuContainerSum.put(remoteContainer, metric);
						}
						
						//ideal workload ?
						if((cpuBenchmark.get(remoteContainer)*loadCpuIdeal) <= cpuContainerSum.get(remoteContainer)){
							//change container
							isChangeContainer = true;	
						}
						
					}else{//change container
						isChangeContainer = true;
					}		
					
				}else{//Simulation Agent and "unknown" agents always kept locally
					
					remoteContainer = localContainer;
					
					if(verbose){
						if(index == -1){
							System.out.println("#### No Agent-Class Metric Description available");
						}
					}
				} 
				
				if(isChangeContainer == true){
					
					if(containerIterator.hasNext() == true){					
						
						if(verbose){
							System.out.println("# IDEAL workload distribution for " + remoteContainer.getName() + ": "+ Math.round((100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer))) + "% CPU");					
						}
						
						remoteContainerName = containerIterator.next();
						
						if(verbose){
							System.out.println("Container: " + remoteContainerName);					
						}
						
					}else{//not enough containers left						
						
						modeOverload = true;
						
						if(!isRemoteOnly){
							//distribute Agents on all remote containers AND local container
							if(containerNames.indexOf(localContainer.getName()) == -1){
								containerNames.add(localContainer.getName());
							}
						}
						
						//find machine with lowest strain
						double cpuStrain = 500;
						containerIteratorOverload = containerNames.iterator();
						
						while(containerIteratorOverload.hasNext()){
							remoteContainerName = containerIteratorOverload.next();
							
							//less strain ?
							if(100/cpuBenchmark.get(containerLocations.get(remoteContainerName)) * cpuContainerSum.get(containerLocations.get(remoteContainerName)) < cpuStrain){
								cpuStrain = 100/cpuBenchmark.get(containerLocations.get(remoteContainerName)) * cpuContainerSum.get(containerLocations.get(remoteContainerName));
								remoteContainer = containerLocations.get(remoteContainerName);
							}
						}
						//update remoteContainerName for next iteration
						remoteContainerName = remoteContainer.getName();
						
						if(verbose){
							System.out.println("#### WARNING ! Limit of " + loadCpuCrit*100 + " exceeded with" + Math.round((100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer))) + "% CPU");
							System.out.println("#### NOT ENOUGH REMOTE CONTAINERS #### (STILL) USING " + remoteContainerName);
							
							if( 100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer) >= LOAD_CPU_CRIT_DANGER){
								System.err.println("###### WARNING ! " + remoteContainer.getName() + ": "+ Math.round((100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer))) + "% => DANGEROUSLY OVERBOOKED !");		
							}
						}						
					}
					//reset for next loop
					isChangeContainer = false;
				}				
				
				/** add agent to determined container*/
				
				if( agentContainerList.containsKey(remoteContainer)){
					agentList = (ArrayList<AgentClassElement4SimStart>) agentContainerList.get(remoteContainer).clone() ;	
				}
				//add actual agent, save mapping and clear agent list for next iteration
				agentList.add(agent);
				agentContainerList.put(remoteContainer, (ArrayList<AgentClassElement4SimStart>)agentList.clone());
				agentList.clear();				
			}//end agent iteration
			
			String output = "#### Summary ------- ####\r\n";
			boolean isOverload = false;
			containerIterator = containerNames.iterator();
			
			while(containerIterator.hasNext()){
				remoteContainerName = containerIterator.next();
				remoteContainer = containerLocations.get(remoteContainerName);
				
				if( agentContainerList.containsKey(remoteContainer)){
					
					double value = 100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer);
					output += Math.round(value) + "% CPU => " + remoteContainerName + "\r\n";
					
					if( value >= LOAD_CPU_CRIT_DANGER){
						isOverload = true;
					}
				}
			}
			output += "#### --------------- ####";
			
			if(isOverload){
				System.err.println(output);
				System.err.println("###### WARNING ! NOT ENOUGH MACHINES IN CLUSTER !");
			}else{
				System.out.println(output);
			}
			
			return agentContainerList;
		}		
		return null;
	}
}
