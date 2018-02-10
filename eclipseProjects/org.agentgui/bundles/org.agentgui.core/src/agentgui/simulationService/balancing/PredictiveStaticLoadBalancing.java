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
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.agentgui.gui.swing.project.Distribution;

import agentgui.core.project.AgentClassMetricDescription;
import agentgui.core.project.setup.AgentClassElement4SimStart;
import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.ClientAvailableMachinesReply;
import agentgui.simulationService.ontology.MachineDescription;

/**
 * This class is for static distribution of Agents based on given metrics,
 * predictive or real (empirical)
 * 
 * @see StaticLoadBalancingBase
 * @see BaseLoadBalancing
 * @see Distribution
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public class PredictiveStaticLoadBalancing extends StaticLoadBalancingBase{
	
	private static final long serialVersionUID = -6884445863598676300L;
	
	/** local constants */
	private static final double LOAD_CPU_LOW_PERCENT 	= 5 ;	//default 5
	private static final double LOAD_CPU_IDEAL_PERCENT 	= 70 ; //default 70
	private static final double LOAD_CPU_CRIT_PERCENT  	= 80 ; //default 80
	private static final double LOAD_CPU_CRIT_DANGER  	= (100 * agentgui.simulationService.load.threading.ThreadCalculateMetrics.FACTOR_MAX_MACHINE_LOAD); //default 95%
	private static final double LOAD_CPU_LOW = (LOAD_CPU_LOW_PERCENT/100) ;
	private static final double LOAD_CPU_IDEAL = (LOAD_CPU_IDEAL_PERCENT/100) ;
	private static final double LOAD_CPU_CRIT  = (LOAD_CPU_CRIT_PERCENT/100) ;
	
	private static final String SORT_DESCENDING = "SORT_DESCENDING";
	private static final String SORT_DESCENDING_CHARGE = "SORT_DESCENDING_CHARGE";
	private static final String SORT_RANDOM = "SORT_RANDOM";
	
	/** The console output. */
	private String consoleOutput;
	/** The load CPU low. */
	private double loadCpuLow = LOAD_CPU_LOW;
	/** The load CPU ideal. */
	private double loadCpuIdeal = LOAD_CPU_IDEAL;
	/** The load CPU critical. */
	private double loadCpuCrit  = LOAD_CPU_CRIT;
	
	/** The boolen for even distribution of agents on all available machines. */
	private boolean isEvenDistribution = false;
	/** The boolean verbose. */
	private boolean verbose = true;
	
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
		
		this.verbose = false; //menu option ?? global setting ??
		
		isEvenDistribution = currDisSetup.isEvenDistribution();
		
		if(currDisSetup.isUseUserThresholds()){
			loadCpuLow = (double)currDisSetup.getUserThresholds().getThCpuL()/100;
			loadCpuIdeal =  (double)currDisSetup.getUserThresholds().getThCpuH()/100;
			loadCpuCrit  =  (double)currDisSetup.getUserThresholds().getThCpuH()/100;
			
			if(loadCpuIdeal < loadCpuLow){
				loadCpuIdeal = loadCpuLow;
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.balancing.BaseLoadBalancingInterface#doBalancing()
	 */
	@Override
	public void doBalancing() {//one-shot behavior
		
		// --------------------------------------------------------------------------------------------------
		// --- Distribution is based on predictive metrics, thus: 										-----
		// --- => 1. Start the remote containers and get their locations								-----
		// --- => 2. Get the metrics (MFLOP-benchmark) of remote container                    			-----
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
				
				System.out.println(consoleOutput);
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
		Hashtable<String, Location> containerLocations;
		
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
			containerLocations = startNumberOfRemoteContainer(numberOfRemoteSlaveMachines , true, null);
			return containerLocations;
			
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
					
					Double mflopsBenchmarkValueTotal = (double) containerDesc.getBenchmarkValue().getBenchmarkValue() * containerDesc.getPlPerformace().getCpu_numberOfLogicalCores();
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
	 * Gets the agent container list for predictive distribution.
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
		Location localContainer = containerLocations.get(localContainerName);
		
		/** A list of agents*/
		ArrayList<AgentClassElement4SimStart> agentList = new ArrayList<AgentClassElement4SimStart>();
		
		/** All container names */
		Vector<String> containerNames = new Vector<String>(containerLocations.keySet());
		
		/** temporary variable for sum of CPU metrics*/
		Hashtable<Location, Double> cpuContainerSum = new Hashtable<Location, Double>();
		
		/** The actual remote container name. */
		String remoteContainerName;		
		
		/** The actual remote container*/
		Location remoteContainer = null;
		
		/** A flag if machines/container are to be overloaded with agents
		 * if there are not enough machines available
		 * */
		boolean modeOverload = false;
		
		if(containerNames != null){
			Iterator<String> containerIterator = containerNames.iterator();
			
			//fill containerSum with default value 0.0d (prevent null pointer exceptions)
			while(containerIterator.hasNext()){
				cpuContainerSum.put(containerLocations.get(containerIterator.next()), 0.0d);
			}
			
			if (currAgentList!=null) {
				/* sort local container to last position --> remote container to be assigned first */
				int indexLocalContainer = containerNames.indexOf(localContainer.getName());
				containerNames.remove(indexLocalContainer);	
				
				//add local container if necessary
				if(!isRemoteOnly || containerNames.size() == 0){
					containerNames.add(localContainer.getName());
				}
				ArrayList<AgentClassElement4SimStart> agents2bStartedList;
				
				//sort Agents by class metrics to best fit distribution method
				if(isEvenDistribution){
					agents2bStartedList = this.getSortedAgentList(currSimSetup.getAgentList(), SORT_RANDOM);
				}else{
					agents2bStartedList = this.getSortedAgentList(currSimSetup.getAgentList(), SORT_DESCENDING);
				}
				
				containerIterator = containerNames.iterator();	
				
				//iterate over available container as long as there are agents
				while(containerIterator.hasNext() && agents2bStartedList != null) {
					
					remoteContainerName = containerIterator.next();
					if(verbose){
						System.out.println("Container: " + remoteContainerName);					
					}
					
					/** Iterate over agent start list and determine if agent fits on container**/
					Iterator<AgentClassElement4SimStart> agents2bStartedIterator = agents2bStartedList.iterator();
					while(agents2bStartedIterator.hasNext()){
						int classNameIndex = -1;
						String  classname = "";
						double metric;
						AgentClassElement4SimStart agent = agents2bStartedIterator.next();
						
						if(agent.getAgentClassReference() != null){
							classname = agent.getAgentClassReference();
							classNameIndex = currProject.getAgentClassLoadMetrics().getIndexOfAgentClassMetricDescription(classname);
							
							//Overload container or even distribution ?? => distribute evenly 
							if(modeOverload || isEvenDistribution){
								if(!isRemoteOnly){
									//distribute Agents on all remote containers AND local container
									if(containerNames.indexOf(localContainer.getName()) == -1){
										containerNames.add(localContainer.getName());
									}
								}
								
								//find machine with lowest strain
								double cpuStrain = 666;
								Iterator<String> containerIteratorOverload = null;
								containerIteratorOverload = containerNames.iterator();
								
								while(containerIteratorOverload.hasNext()){
									String containerNameTemp = containerIteratorOverload.next();
									
									//less strain ?
									if(100/cpuBenchmark.get(containerLocations.get(containerNameTemp)) * cpuContainerSum.get(containerLocations.get(containerNameTemp)) < cpuStrain){
										//update lowest
										cpuStrain = 100/cpuBenchmark.get(containerLocations.get(containerNameTemp)) * cpuContainerSum.get(containerLocations.get(containerNameTemp));
										//update remoteContainerName for next iteration
										remoteContainerName = containerNameTemp;
									}
								}
								
								if(modeOverload && verbose){
									System.out.println("#### WARNING ! Limit of " + loadCpuCrit*100 + " exceeded with" + Math.round((100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer))) + "% CPU");
									System.out.println("#### NOT ENOUGH REMOTE CONTAINERS #### (STILL) USING " + remoteContainerName);
									
									if( 100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer) >= LOAD_CPU_CRIT_DANGER){
										System.err.println("###### WARNING ! " + remoteContainer.getName() + ": "+ Math.round((100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer))) + "% => DANGEROUSLY OVERBOOKED !");		
									}
								}
							}// ---- end overload ----
						}
						
						remoteContainer = containerLocations.get(remoteContainerName);
						
						if(classNameIndex == -1 || classname.contains("SimulationManager")){// Simulation Agent and "unknown" agents always kept locally
							//TODO: don't make an exception for SiMa any longer ....one day
							remoteContainer = localContainer; //overwrite actual container
						}
						if(classNameIndex == -1){
							if(verbose){
								System.out.println("#### No Agent-Class Metric Description available");
							}
							metric = 1;
						}else{
							//get agent class metric from project settings
							if(currProject.getAgentClassLoadMetrics().isUseRealLoadMetric() == true){
								metric = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector().get(classNameIndex).getRealMetric();
							}else{
								metric = currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector().get(classNameIndex).getUserPredictedMetric();	
							}
						}
						
						double metricSumTemp = metric + cpuContainerSum.get(remoteContainer);
						
						//still enough "space" on container ?
						if(metricSumTemp >= cpuBenchmark.get(remoteContainer)*loadCpuIdeal){ /* limits reached*/
							if(!modeOverload){
								continue; //try next agent
							}
						}									
						
						/** add agent to container*/
						
						if( agentContainerList.containsKey(remoteContainer)){
							agentList = (ArrayList<AgentClassElement4SimStart>) agentContainerList.get(remoteContainer).clone() ;	
						}
						//add actual agent, save mapping and clear agent list for next iteration
						agentList.add(agent);
						agentContainerList.put(remoteContainer, (ArrayList<AgentClassElement4SimStart>)agentList.clone());
						agentList.clear();	
						
						//update sum CPU metrics
						cpuContainerSum.put(remoteContainer, cpuContainerSum.get(remoteContainer) + metric);
						
						//update agent list
						agents2bStartedList.remove(agent);
						agents2bStartedIterator = agents2bStartedList.iterator();
						
					}//end agent iteration
					
					if(agents2bStartedList.isEmpty()){
						break; //FINISHED -- leave container loop
					}else if(!containerIterator.hasNext()){
						modeOverload = true;				
						//reset iterator and loop again (until agents2bStartedList is empty)
						containerIterator = containerNames.iterator();
					}
				}//end container loop
				
				
				/**
				* Finished calculation of agent distribution
				* summarize in a short report
				*/
				consoleOutput = "#### Summary ------- ####\r\n";
				boolean isOverload = false;
				containerIterator = containerNames.iterator();
				
				while(containerIterator.hasNext()){
					remoteContainerName = containerIterator.next();
					remoteContainer = containerLocations.get(remoteContainerName);
					
					if( agentContainerList.containsKey(remoteContainer)){
						
						double value = 100/cpuBenchmark.get(remoteContainer) * cpuContainerSum.get(remoteContainer);
						int noOfAgents = agentContainerList.get(remoteContainer).size();
						consoleOutput += remoteContainerName +  " => " + Math.round(value) + "% CPU, " + noOfAgents + " agents \r\n";
						
						if( value >= LOAD_CPU_CRIT_DANGER){
							isOverload = true;
						}
					}
				}
				consoleOutput += "#### --------------- ####\r\n";
				
				if(isOverload){//color text red
//					consoleOutput = (char)27 + "[31;37m" + consoleOutput;//add red text color
					consoleOutput += "###### WARNING ! NOT ENOUGH MACHINES IN CLUSTER ! ######\r\n";
//					consoleOutput += (char)27 + "[0m";//remove formatting
				}
				return agentContainerList;
			}
		}	
		return null;
	}

	/**
	 * Gets the sorted agent list.
	 *
	 * @param agentList the agent list
	 * @return the sorted agent list
	 */
	private ArrayList<AgentClassElement4SimStart> getSortedAgentList(ArrayList<AgentClassElement4SimStart> agentList, String order) {
		ArrayList<AgentClassElement4SimStart> sortedAgentList = new ArrayList<AgentClassElement4SimStart>();
		Vector<AgentClassMetricDescription> acmdv = sortAgentClassMetricDescriptionVectorDescending(currProject.getAgentClassLoadMetrics().getAgentClassMetricDescriptionVector());

		//copy
		for(int i = 0; i < agentList.size(); i++){
			sortedAgentList.add(agentList.get(i));
		}
		
		if(order.equals(SORT_DESCENDING)){
			//sort agents by class descending, e.g. fff eeee dddd cccc bbbb aaaa
			for(int indexA = acmdv.size()-1; indexA >= 0; indexA--){
				for(int indexB = 0; indexB < sortedAgentList.size() ; indexB++){
					if(sortedAgentList.get(indexB).getAgentClassReference().equals(acmdv.get(indexA).getClassName())){
						sortedAgentList.add(0, sortedAgentList.get(indexB));
						sortedAgentList.remove(indexB+1);					
					}
				}
			}
		}else if(order.equals(SORT_DESCENDING_CHARGE)){
			//sort agents by class descending, alternating (charge), e.g. fedcba fedcba fedcba fedcba
			int offset = 0;
			while(sortedAgentList.size() != offset){
				for(int indexA = acmdv.size()-1; indexA >= 0; indexA--){
					for(int indexB = offset; indexB < sortedAgentList.size() ; indexB++){
						if(sortedAgentList.get(indexB).getAgentClassReference().equals(acmdv.get(indexA).getClassName())){
							sortedAgentList.add(0,sortedAgentList.get(indexB));
							sortedAgentList.remove(indexB+1);
							offset++;
							break;
						}
					}
				}
			}
		}else if(order.equals(SORT_RANDOM)){
			//sort agents randomly, e.g. feacdefaacbdfe
			 Collections.shuffle(sortedAgentList);
		}
		return sortedAgentList;
	}
	
	/**
	 * Sort agent class metric description vector descending.
	 *
	 * @param acmdv the agent class metric description vector
	 * @return the sorted (descending) agent class metric description vector
	 */
	private Vector<AgentClassMetricDescription> sortAgentClassMetricDescriptionVectorDescending(Vector<AgentClassMetricDescription> acmdv){
		Vector<AgentClassMetricDescription> sortedAcmdv = new Vector<AgentClassMetricDescription>();
		
		//copy
		for(int i = 0; i < acmdv.size(); i++){
			sortedAcmdv.add(acmdv.get(i));;
		}
		
		for(int indexA = 0; indexA < sortedAcmdv.size()-1; indexA++){
			for(int indexB = 1; indexB < sortedAcmdv.size()-indexA; indexB++){
				double metricA = 0;
				double metricB = 0;
				
				if(currProject.getAgentClassLoadMetrics().isUseRealLoadMetric() == true){
					metricA = sortedAcmdv.get(indexB-1).getRealMetric();
					metricB = sortedAcmdv.get(indexB).getRealMetric();
				}else{
					metricA = sortedAcmdv.get(indexB-1).getUserPredictedMetric();
					metricB = sortedAcmdv.get(indexB).getUserPredictedMetric();	
				}
				//compare and reorder
				if(metricA < metricB){
					AgentClassMetricDescription acmdvTemp = sortedAcmdv.get(indexB-1);
					sortedAcmdv.remove(indexB-1);
					sortedAcmdv.add(indexB, acmdvTemp);	
				}
			}
		}
		return sortedAcmdv;	
	}
}
