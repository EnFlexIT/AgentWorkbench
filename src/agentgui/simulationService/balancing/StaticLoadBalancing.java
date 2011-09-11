package agentgui.simulationService.balancing;

import jade.core.Location;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.agents.AgentClassElement4SimStart;

public class StaticLoadBalancing extends StaticLoadBalancingBase {

	private static final long serialVersionUID = -6884445863598676300L;

	public StaticLoadBalancing() {
		super();
	}
	
	/**
	 * This is the default method for the start of Agent.GUI simulation
	 * project. In case, that you want to define this method by your
	 * own, write a new class, which extends this one and overwrite this 
	 * action() method.
	 */
	@Override
	public void action() {
		
		if (currDisSetup.isDoStaticLoadBalalncing()==false) {
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
			// --- will be deistributed as well 					 -----
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
			Hashtable<String, Location> newContainerLocations = this.startRemoteContainer(currNumberOfContainer - 1, true, true);
			if (newContainerLocations!=null) {
				locationNames = new Vector<String>(newContainerLocations.keySet());
				cont4DisMax = newContainerLocations.size();
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
					this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs, location);
				
				}
			} // --- end for
		} // --- end if
		
	} // --- end action()
	

	
}
