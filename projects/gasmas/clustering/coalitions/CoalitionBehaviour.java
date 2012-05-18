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
package gasmas.clustering.coalitions;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.clustering.analyse.ComponentFunctions;
import gasmas.clustering.behaviours.ClusteringBehaviour;
import jade.core.ServiceException;
import jade.core.behaviours.ParallelBehaviour;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.Collections;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class CoalitionBehaviour.
 */
public class CoalitionBehaviour extends ParallelBehaviour {
	
	/** The Constant WAITING_TIME_FOR_COALITION_AUTHORIRY in miliseconds. */
	private static final long WAITING_TIME_FOR_COALITION_AUTHORIRY = 10000;

	/** The Constant WAITING_TIME_FOR_COALITION_AUTHORIRY_INCEMENT. */
	private static final long WAITING_TIME_FOR_COALITION_AUTHORIRY_INCEMENT = 3000;
	
	/** The environment model. */
	private EnvironmentModel environmentModel;


	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/** The suggested cluster network component. */
	private ClusterNetworkComponent suggestedClusterNetworkComponent;

	/** The active NetworkCompoenents within a suggestedClusterNetworkComponent. */
	private ArrayList<NetworkComponent> activeNCs;

	/** The coalition authority behaviour. */
	private ActiveNAAuthorityBehaviour coalitionAuthorityBehaviour;

	/** Starts own coalitionAuthorityBehaviour if estimated calitionAuthority has not asked. */
	private AuthorityWakerBehaviour coalitionANCWakerBehaviour = null;

	/**
	 * Instantiates a new coalition behaviour.
	 *
	 * @param agent the agent
	 * @param environmentModel the environment model
	 * @param networkModel the network model
	 * @param clusteringBehaviour the clustering behaviour
	 */
	public CoalitionBehaviour(SimulationAgent agent, EnvironmentModel environmentModel, NetworkModel networkModel, ClusteringBehaviour clusteringBehaviour) {
		this.environmentModel = environmentModel;
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());

		clusteringBehaviour.setCoalitionBehaviours(this);
		addSubBehaviour(clusteringBehaviour);
		if (myAgent.getLocalName().contains(ClusterNetworkAgent.CLUSTER_AGENT_Prefix)) {
			System.out.println("XX");
			clusteringBehaviour.restart();
		}
		addSubBehaviour(new ActiveNAResponderBehaviour(this, myAgent));
	}

	/**
	 * Check suggested cluster based on Rules
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @param internal the internal
	 * @return true, if successful
	 */
	public boolean checkSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent, boolean internal) {
		if (suggestedClusterNetworkComponent == null) {
			changeSuggestedCluster(clusterNetworkComponent, internal);
		}
		switch (ClusterCompare.compareClusters(clusterNetworkComponent, suggestedClusterNetworkComponent)) {
		case ClusterCompare.PART_OF_SUGGESTED:
		case ClusterCompare.BETTER:
			changeSuggestedCluster(clusterNetworkComponent, internal);
		case ClusterCompare.EQUAL:
			return true;
			
		default:
			// all three other cases
			return false;
		}
	}

	/**
	 * Start waker behaviour.
	 *
	 * @param activeNCIDs the active nci ds
	 */
	private void startWakerBehaviour(ArrayList<String> activeNCIDs) {
		int position = 0;
		for (int i = 0; i < activeNCIDs.size(); i++) {
			if (activeNCIDs.get(i).equals(thisNetworkComponent.getId()))
				position = i;
		}
		coalitionANCWakerBehaviour = new AuthorityWakerBehaviour(myAgent, WAITING_TIME_FOR_COALITION_AUTHORIRY + position * WAITING_TIME_FOR_COALITION_AUTHORIRY_INCEMENT, this, activeNCIDs.get(0));
		this.addSubBehaviour(coalitionANCWakerBehaviour);
	}

	/**
	 * Removes the coalition anc waker behaviour.
	 *
	 * @param componentID the component id
	 */
	public void killCoalitionANCWakerBehaviour(String componentID) {
		if (suggestedClusterNetworkComponent != null && suggestedClusterNetworkComponent.getNetworkComponentIDs().contains(componentID)) {
			killCoalitionANCWakerBehaviour();
		}
	}

	/**
	 * Removes the coalition ANCWakerehaviour this is called locally
	 */
	private void killCoalitionANCWakerBehaviour() {
		if (coalitionANCWakerBehaviour != null) {
			this.removeSubBehaviour(coalitionANCWakerBehaviour);
			coalitionANCWakerBehaviour = null;
		}
	}

	/**
	 * Kill previous behaviours based on suggested cnc.
	 */
	private void killPreviousBehavioursBasedOnSuggestedCNC() {
		if (coalitionAuthorityBehaviour != null) {
			this.removeSubBehaviour(coalitionAuthorityBehaviour);
			coalitionAuthorityBehaviour = null;
		}
		killCoalitionANCWakerBehaviour();
	}

	/**
	 * Change suggested cluster.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @param internal the internal
	 */
	private void changeSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent, boolean internal) {
		killPreviousBehavioursBasedOnSuggestedCNC();
		suggestedClusterNetworkComponent = clusterNetworkComponent;
		activeNCs = ComponentFunctions.getActiveAgentComponents(suggestedClusterNetworkComponent.getClusterNetworkModel());

		startNewBehaviours(internal);
	}

	/**
	 * Start new behaviours.
	 *
	 * @param internal the internal
	 */
	private void startNewBehaviours(boolean internal) {
		// Actually shouldn't happen, only if Agents and getActiveAgentComponents are out of Sync
		if (activeNCs.size() < 1) {
			System.out.println("Bad " + thisNetworkComponent.getId() + " " + suggestedClusterNetworkComponent.getNetworkComponentIDs().contains(thisNetworkComponent.getId()));
		} else {
			ArrayList<String> activeNCIDs = getSortedActiveNCIDs(activeNCs);
			if (activeNCIDs.get(0).equals(thisNetworkComponent.getId())) {
				startCoalitionAuthorityBehaviour();
			} else if (internal) {
				startWakerBehaviour(activeNCIDs);
			}
		}
	}

	/**
	 * Gets the sorted active nci ds.
	 *
	 * @param activeNCs the active n cs
	 * @return the sorted active nci ds
	 */
	private ArrayList<String> getSortedActiveNCIDs(ArrayList<NetworkComponent> activeNCs) {
		ArrayList<String> activeNCIDs = new ArrayList<String>();
		for (NetworkComponent networkComponent : activeNCs) {
			activeNCIDs.add(networkComponent.getId());
		}
		Collections.sort(activeNCIDs);
		return activeNCIDs;
	}

	/**
	 * Start coalition authority behaviour.
	 */
	private void startCoalitionAuthorityBehaviour() {
		if (activeNCs.size() == 1) {
			startClusterAgent();
		}
		coalitionAuthorityBehaviour = new ActiveNAAuthorityBehaviour(this, activeNCs);
		addSubBehaviour(coalitionAuthorityBehaviour);
	}

	/**
	 * Inform coalition authority, if it is not runnning yet. This is called by the Wake Behaviour
	 *
	 * @param componentID the component id
	 */
	public void informCoalitionAuthority(String componentID) {
		addSubBehaviour(new ActiveNAProposeBehaviour(null, myAgent, ActiveNAAuthorityBehaviour.createRequest(componentID, suggestedClusterNetworkComponent)));
	}

	/**
	 * Gets the this network component.
	 *
	 * @return the this network component
	 */
	public NetworkComponent getThisNetworkComponent(){
		return thisNetworkComponent;
	}

	/**
	 * Gets the suggested cluster network component.
	 *
	 * @return the suggested cluster network component
	 */
	public ClusterNetworkComponent getSuggestedClusterNetworkComponent() {
		return suggestedClusterNetworkComponent;
	}

	/**
	 * Start cluster agent.
	 */
	public void startClusterAgent() {
		try {		
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			String agentName = "" + ClusterNetworkAgent.CLUSTER_AGENT_Prefix + ClusterNetworkAgent.getFreeID();
			Object[] params = new Object[] { suggestedClusterNetworkComponent };
			try {
				loadHelper.startAgent(agentName, "gasmas.agents.components.ClusterNetworkAgent", params, myAgent.getContainerController().getContainerName());
			} catch (ControllerException e) {
				e.printStackTrace();
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change display for debugging pruposes
	 *
	 * @param id the id
	 * @param newNetworkModel the new network model
	 */
	private void changeDisplay(String id, NetworkModel newNetworkModel) {
		SimulationServiceHelper simulationServiceHelper = null;
		NetworkModel networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		networkModel.getAlternativeNetworkModel().put(id, newNetworkModel);
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
