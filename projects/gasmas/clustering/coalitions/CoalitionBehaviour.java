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

	/** The network model. */
	private NetworkModel networkModel;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/** The suggested cluster network component. */
	private ClusterNetworkComponent suggestedClusterNetworkComponent;

	/** The active NetworkCompoenents within a suggestedClusterNetworkComponent. */
	private ArrayList<NetworkComponent> activeNCs;

	/** The list of bigger cnc. */
	private ArrayList<ClusterNetworkComponent> listOfBiggerCNC = new ArrayList<ClusterNetworkComponent>();

	/** The coalition authority behaviour. */
	private CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour;

	/** Starts own coalitionAuthorityBehaviour if estimated calitionAuthority has not asked. */
	private CoalitionANCWakerBehaviour coalitionANCWakerBehaviour = null;
	
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
	 * @param internal the internal
	 * @return true, if successful
	 */
	public boolean checkSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent, boolean internal) {
		if (suggestedClusterNetworkComponent == null) {
			changeSuggestedCluster(clusterNetworkComponent, internal);
			changeDisplay();
		}
		switch (ClusterCompare.compareClusters(clusterNetworkComponent, suggestedClusterNetworkComponent)) {
		case ClusterCompare.PART_OF_SUGGESTED:
			sendSuggestedToAuthorityForSubClustering();
		case ClusterCompare.BETTER:
			changeSuggestedCluster(clusterNetworkComponent, internal);
			return true;
		case ClusterCompare.PART_OF_NEW:
			addToListOfBiggerCNC(clusterNetworkComponent);
			return false;
		case ClusterCompare.EQUAL:
			return true;

		default:
			return false;
		}
	}
	
	/**
	 * Adds the to list of bigger cnc.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	private void addToListOfBiggerCNC(ClusterNetworkComponent clusterNetworkComponent) {
		for (ClusterNetworkComponent cNC : listOfBiggerCNC) {
			if (ClusterCompare.compareClusters(cNC, clusterNetworkComponent) == ClusterCompare.EQUAL) {
				return;
			}
		}
		// check if components still part of model
		NetworkModel networkModel = getClusteredModel();
		for( String componentID:clusterNetworkComponent.getNetworkComponentIDs()){
			if( networkModel.getNetworkComponent(componentID) == null){
				return;
			}
		}
		listOfBiggerCNC.add(clusterNetworkComponent);
	}

	/**
	 * Gets the clustered model.
	 *
	 * @return the clustered model
	 */
	private NetworkModel getClusteredModel() {
		NetworkModel networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		NetworkModel clusteredNM = networkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		if (clusteredNM == null) {
			clusteredNM = networkModel.getCopy();
			clusteredNM.setAlternativeNetworkModel(null);
			changeDisplay(clusteredNM);
		}
		return clusteredNM;
	}

	/**
	 * Change display.
	 *
	 * @param clusteredNM the clustered nm
	 * @param cluster the cluster
	 */
	private void changeDisplay(NetworkModel clusteredNM) {
		SimulationServiceHelper simulationServiceHelper = null;
		NetworkModel networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		networkModel.getAlternativeNetworkModel().put(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME, clusteredNM);
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send suggested to authority for sub clustering.
	 */
	private void sendSuggestedToAuthorityForSubClustering() {
		ArrayList<NetworkComponent> activeNCs = ComponentFunctions.getActiveAgentComponents(suggestedClusterNetworkComponent.getClusterNetworkModel());
		ArrayList<String> activeNCIDs = getSortedActiveNCIDs(activeNCs);
		if (!thisNetworkComponent.getId().equals(activeNCIDs.get(0))) {
			informCoalitionAuthority(activeNCIDs.get(0));
		}
	}

	/**
	 * Removes the coalition anc waker behaviour.
	 *
	 * @param componentID the component id
	 */
	public void removeCoalitionANCWakerBehaviour(String componentID) {
		if (suggestedClusterNetworkComponent != null && suggestedClusterNetworkComponent.getNetworkComponentIDs().contains(componentID)) {
			removeCoalitionANCWakerBehaviour();
		}
	}
	
	/**
	 * Removes the coalition anc waker behaviour.
	 */
	private void removeCoalitionANCWakerBehaviour() {
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
		removeCoalitionANCWakerBehaviour();
	}

	/**
	 * Start new behaviours.
	 *
	 * @param internal the internal
	 */
	private void startNewBehaviours(boolean internal) {
		// Actually shouldn't happen, only if Agents and getActiveAgentComponents are out of Sync
		if (activeNCs.size() < 1) {
			System.out.println("Bad");
			for (NetworkComponent net : suggestedClusterNetworkComponent.getClusterNetworkModel().getClusterComponents())
				System.out.println(net.getId());
		} else {
			ArrayList<String> activeNCIDs = getSortedActiveNCIDs(activeNCs);
			if (activeNCIDs.get(0).equals(thisNetworkComponent.getId())) {
				System.out.println("Authority " + thisNetworkComponent.getId() + " Size " + suggestedClusterNetworkComponent.getSize());
				startCoalitionAuthorityBehaviour();
			} else if (internal) {
				int position = 0;
				for( int i = 0; i < activeNCIDs.size(); i++){
					if (activeNCIDs.get(i).equals(thisNetworkComponent.getId()))
						position = i;
				}
				coalitionANCWakerBehaviour = new CoalitionANCWakerBehaviour(myAgent, WAITING_TIME_FOR_COALITION_AUTHORIRY + position * WAITING_TIME_FOR_COALITION_AUTHORIRY_INCEMENT, this,
						activeNCIDs.get(0));
				this.addSubBehaviour(coalitionANCWakerBehaviour);
			}
		}
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
		changeListOfBiggerCNC();

		startNewBehaviours(internal);
	}

	/**
	 * Change list of bigger cnc.
	 */
	private void changeListOfBiggerCNC(){
		for (ClusterNetworkComponent clusterNetworkComponent : new ArrayList<ClusterNetworkComponent>(listOfBiggerCNC)) {
			if (ClusterCompare.compareClusters(clusterNetworkComponent, suggestedClusterNetworkComponent) != ClusterCompare.PART_OF_NEW) {
				listOfBiggerCNC.remove(clusterNetworkComponent);
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
		coalitionAuthorityBehaviour = new CoalitionANCAuthorityBehaviour(this, activeNCs);
		addSubBehaviour(coalitionAuthorityBehaviour);
	}

	/**
	 * Inform coalition authority, if it is not runnning yet. This is called by the Wake Behaviour
	 *
	 * @param componentID the component id
	 */
	public void informCoalitionAuthority(String componentID) {
		addSubBehaviour(new CoalitionANCProposeBehaviour(null, myAgent, CoalitionANCAuthorityBehaviour.createRequest(componentID, suggestedClusterNetworkComponent)));
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
		System.out.println("startAgent " + thisNetworkComponent.getId());
		try {		
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			String agentName = "" + ClusterNetworkAgent.CLUSTER_AGENT_Prefix + ClusterNetworkAgent.getFreeID();
			Object[] params = new Object[] { suggestedClusterNetworkComponent, listOfBiggerCNC };
			try {
				loadHelper.startAgent(agentName, "gasmas.agents.components.ClusterNetworkAgent", params, myAgent.getContainerController().getContainerName());
			} catch (ControllerException e) {
				e.printStackTrace();
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
