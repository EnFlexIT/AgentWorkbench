package agentgui.simulationService.balancing;

import jade.core.Location;

import java.util.Hashtable;
import java.util.Iterator;

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
		
		// ---------------------------------------------------------------
		// --- If we use the static load balancing, get the parameter ----
		// --- and start the indicated number of container			  ----
		// ---------------------------------------------------------------
		int numberOfAgents = currDisSetup.getNumberOfAgents();
		int numberOfContainer = currDisSetup.getNumberOfContainer();
		
		// --- Just in case, that we don't have enough information -------
		if (numberOfContainer==0) {
			if (numberOfAgents!=0) {
				int noAgentsMax = currThresholdLevels.getThNoThreadsH();
				numberOfContainer = (int) Math.ceil(((float)numberOfAgents / (float)noAgentsMax)) + 1;
			}
		}
		// --- If we know how many container are needed, start them ------
		if (numberOfContainer!=0) {
			// --- Start getting the desired number of container ---------
			Hashtable<String, Location> newContainerLocations = this.startRemoteContainer(numberOfContainer - 1);
		}
		
		// --- Start the agents coming from the agent-configuration ------  
		for (Iterator<AgentClassElement4SimStart> iterator = currAgentList.iterator(); iterator.hasNext();) {
			AgentClassElement4SimStart agent2Start = iterator.next();
			this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), null);
		} 
		
		// --- Start the agents coming from the visual-agent-configuration ------  
		if (currAgentListVisual!=null) {
			for (Iterator<AgentClassElement4SimStart> iterator = currAgentListVisual.iterator(); iterator.hasNext();) {
				AgentClassElement4SimStart agent2Start = iterator.next();
				this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), null);
			} 
		}
		
	}

	
}
