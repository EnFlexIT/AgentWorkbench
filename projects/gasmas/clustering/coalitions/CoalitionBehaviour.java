package gasmas.clustering.coalitions;

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
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

public class CoalitionBehaviour extends ParallelBehaviour {
	
	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The simulation service helper. */
	private SimulationServiceHelper simulationServiceHelper;

	/** The network model. */
	private NetworkModel networkModel;

	private NetworkComponent thisNetworkComponent;

	private ClusterNetworkComponent suggestedClusterNetworkComponent;

	private CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour;

	private CoalitionPNCAuthorityBehaviour coalitionPNCAuthorityBehaviour;

	public CoalitionBehaviour(SimulationAgent agent, EnvironmentModel environmentModel, ClusteringBehaviour clusteringBehaviour) {
		this.environmentModel = environmentModel;
		networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getName().split("@")[0]);

		clusteringBehaviour.setCoalitionBehaviours(this);
		addSubBehaviour(clusteringBehaviour);
		addSubBehaviour(new CoalitionANCResponseBehaviour(this, myAgent));
	}

	public boolean checkSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent) {
		if (suggestedClusterNetworkComponent == null) {
			changeSuggestedCluster(clusterNetworkComponent);
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

		default:
			return false;
		}
	}

	private void changeSuggestedCluster(ClusterNetworkComponent clusterNetworkComponent) {
		if (coalitionAuthorityBehaviour != null) {
			this.removeSubBehaviour(coalitionAuthorityBehaviour);
		}
		suggestedClusterNetworkComponent = clusterNetworkComponent;
		ArrayList<NetworkComponent> activeNCs = ActiveNetworkComponents.getActiveNetworkComponents(suggestedClusterNetworkComponent.getClusterNetworkModel());
		if (activeNCs.size() == 1) {
			coalitionPNCAuthorityBehaviour = new CoalitionPNCAuthorityBehaviour(this, activeNCs);
			addSubBehaviour(coalitionPNCAuthorityBehaviour);
		} else {

			ArrayList<String> activeNCIDs = new ArrayList<String>();
			for( NetworkComponent networkComponent : activeNCs){
				activeNCIDs.add(networkComponent.getId());
			}
			Collections.sort(activeNCIDs);
			if (activeNCIDs.get(0).equals(thisNetworkComponent.getId())) {
				System.out.println(thisNetworkComponent.getId());
				coalitionAuthorityBehaviour = new CoalitionANCAuthorityBehaviour(this, activeNCs);
				addSubBehaviour(coalitionAuthorityBehaviour);
			}
		}
		changeDisplay();
	}

	private void changeDisplay() {
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		networkModel.getAlternativeNetworkModel().put(suggestedClusterNetworkComponent.getId(), suggestedClusterNetworkComponent.getClusterNetworkModel());
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public NetworkComponent getThisNetworkComponent(){
		return thisNetworkComponent;
	}

	public ClusterNetworkComponent getSuggestedClusterNetworkComponent() {
		return suggestedClusterNetworkComponent;
	}

	public void startClusterAgent() {
		System.out.println(suggestedClusterNetworkComponent.getId());
	}
}
