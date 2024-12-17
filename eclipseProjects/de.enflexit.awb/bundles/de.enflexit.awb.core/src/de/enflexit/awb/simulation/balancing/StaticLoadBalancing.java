package de.enflexit.awb.simulation.balancing;

import jade.core.Location;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import de.enflexit.awb.core.project.setup.AgentClassElement4SimStart;
import de.enflexit.awb.simulation.agents.LoadExecutionAgent;

/**
 * This class is the default class for the start of an agency of <b>Agent.Workbench</b>. 
 * In case, that you want to define a tailored method to start an agent 
 * system, write a new class that extends {@link StaticLoadBalancingBase}.<br>
 * 
 * @see StaticLoadBalancingBase
 * @see BaseLoadBalancing
 * @see Distribution
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StaticLoadBalancing extends StaticLoadBalancingBase {

	private static final long serialVersionUID = -6884445863598676300L;
	
	public StaticLoadBalancing(LoadExecutionAgent agent) {
		super(agent);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.balancing.BaseLoadBalancingInterface#doBalancing()
	 */
	@Override
	public void doBalancing() {
		
		if (currDisSetup.isDoStaticLoadBalancing()==false) {
			// -----------------------------------------------------------
			// --- This is the very default case -------------------------
			// --- => Just start all defined agents ----------------------
			// -----------------------------------------------------------
			this.startAgentsFromCurrAgentList();
			
		} else {
			// -----------------------------------------------------------
			// --- The case if no specific distribution is required: -----
			// --- => Start the required number of remote container  -----
			// --- and distribute the agents, defined in 			 -----
			// --- 'this.currAgentList' to these container			 -----
			// --- => The Agents defined in the visualization-setup  -----
			// --- will be distributed as well 					 	 -----
			// -----------------------------------------------------------

			// --- Just in case, that we don't have enough information ---
			if (currNumberOfContainer==0) {
				if (currNumberOfAgents!=0) {
					int noAgentsMax = currThresholdLevels.getThNoThreadsH();
					currNumberOfContainer = (int) Math.ceil(((float)currNumberOfAgents / (float)noAgentsMax)) + 1;
				}
			}	

			if (currNumberOfContainer<=1) {
				// --- Just start all defined agents ---------------------
				this.startAgentsFromCurrAgentList();
				return;
			}
			
			// --- If we know how many container are needed, start them --
			Vector<String> locationNames = null;
			int cont4DisMax = 0;
			int cont4DisI = 0;
			Hashtable<String, Location> newContainerLocations = this.startNumberOfRemoteContainer(currNumberOfContainer - 1, true, null);
			if (newContainerLocations!=null) {
				locationNames = new Vector<String>(newContainerLocations.keySet());
				cont4DisMax = newContainerLocations.size();
			}		
			
			/* remove local container from location names ?*/
			if (locationNames!=null){
				if(isRemoteOnly && locationNames.size() != 0){
					int indexLocalContainer = locationNames.indexOf(localContainerName);
					if(indexLocalContainer != -1){
						locationNames.remove(indexLocalContainer);
						cont4DisMax--;
					}
				}
			}
			// --- merge all agents, which have to be started ------------
			Vector<AgentClassElement4SimStart> currAgentListMerged = new Vector<AgentClassElement4SimStart>();
			if (currAgentList!=null) {
				currAgentListMerged.addAll(currAgentList);	
			}			
			
			// --- start the distribution of the agents to the locations -
			for (Iterator<AgentClassElement4SimStart> iterator = currAgentListMerged.iterator(); iterator.hasNext();) {
				// --- Get the agent, which has to be started ------------
				AgentClassElement4SimStart agent2Start = iterator.next();
				// --- Check for start arguments -------------------------
				Object[] startArgs = this.getStartArguments(agent2Start);
				
				if (locationNames==null) {
					// --- Just start the agent locally ------------------
					this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs , null);

				} else {
					// --- Set the location for the agent ----------------
					String containerName = locationNames.get(cont4DisI);
					Location location = newContainerLocations.get(containerName);
					cont4DisI++;
					if (cont4DisI>=cont4DisMax) {
						cont4DisI=0;
					}
					// --- finally start the agent -----------------------		
					//TODO: don't make an exception for SiMa any longer ....one day
					if(agent2Start.getAgentClassReference().contains("SimulationManager")){
						this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs, newContainerLocations.get(localContainerName));
					}else{
						this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs, location);	
					}				
				}
			} // --- end for
		} // --- end if
	}
}
