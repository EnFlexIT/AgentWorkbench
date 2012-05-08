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
import gasmas.clustering.ActiveNetworkComponents;
import gasmas.clustering.ClusterCompare;
import gasmas.clustering.ClusteringBehaviour;
import jade.core.ServiceException;
import jade.core.behaviours.ParallelBehaviour;

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
	
	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/** The suggested cluster network component. */
	private ClusterNetworkComponent suggestedClusterNetworkComponent;

	/** The coalition authority behaviour. */
	private CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour;

	/** The coalition pnc authority behaviour. */
	private CoalitionPNCAuthorityBehaviour coalitionPNCAuthorityBehaviour;

	/**
	 * Instantiates a new coalition behaviour.
	 *
	 * @param agent the agent
	 * @param environmentModel the environment model
	 * @param clusteringBehaviour the clustering behaviour
	 */
	public CoalitionBehaviour(SimulationAgent agent, EnvironmentModel environmentModel, ClusteringBehaviour clusteringBehaviour) {
		this.environmentModel = environmentModel;
		networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());

		clusteringBehaviour.setCoalitionBehaviours(this);
		addSubBehaviour(clusteringBehaviour);
		addSubBehaviour(new CoalitionANCResponseBehaviour(this, myAgent));
	}

	/**
	 * Check suggested cluster.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @return true, if successful
	 */
	public boolean checkSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent) {
		if (suggestedClusterNetworkComponent == null) {
			changeSuggestedCluster(clusterNetworkComponent);
			changeDisplay();
		}
		if (coalitionPNCAuthorityBehaviour != null) {
			return false;
		}
		ClusterCompare clusterCompare = new ClusterCompare(clusterNetworkComponent, suggestedClusterNetworkComponent);
		switch (clusterCompare.compare()) {
		case ClusterCompare.BETTER:
		case ClusterCompare.PART_OF_NEW:
			changeSuggestedCluster(clusterNetworkComponent);
			return true;
		case ClusterCompare.EQUAL:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Change suggested cluster.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	private void changeSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent) {
		if (coalitionAuthorityBehaviour != null) {
			this.removeSubBehaviour(coalitionAuthorityBehaviour);
		}
		suggestedClusterNetworkComponent = clusterNetworkComponent;
		ArrayList<NetworkComponent> activeNCs = ActiveNetworkComponents.getActiveNetworkComponents(suggestedClusterNetworkComponent.getClusterNetworkModel());
		if (activeNCs.size() < 1) {
			System.out.println("Bad");
			for (NetworkComponent net : suggestedClusterNetworkComponent.getClusterNetworkModel().getClusterComponents())
				System.out.println(net.getId());
		} else {
			ArrayList<String> activeNCIDs = new ArrayList<String>();
			for( NetworkComponent networkComponent : activeNCs){
				activeNCIDs.add(networkComponent.getId());
			}
			Collections.sort(activeNCIDs);
			if (activeNCIDs.get(0).equals(thisNetworkComponent.getId())) {
				coalitionAuthorityBehaviour = new CoalitionANCAuthorityBehaviour(this, activeNCs);
				addSubBehaviour(coalitionAuthorityBehaviour);
			}
		}
	}

	/**
	 * Change display.
	 */
	private void changeDisplay() {
		SimulationServiceHelper simulationServiceHelper = null;
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		networkModel.getAlternativeNetworkModel().put(thisNetworkComponent.getId() + " " + suggestedClusterNetworkComponent.getId(),
				suggestedClusterNetworkComponent.getClusterNetworkModel());
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
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
		System.out.println("Do");
		try {		
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			loadHelper.startAgent("" + ClusterNetworkAgent.CLUSTER_AGENT_Prefix + ClusterNetworkAgent.getFreeID(), "gasmas.agents.components.ClusterNetworkAgent",
					new Object[] { suggestedClusterNetworkComponent }, "gasmas");
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
