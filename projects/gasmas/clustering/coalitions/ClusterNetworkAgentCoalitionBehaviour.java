package gasmas.clustering.coalitions;

import gasmas.clustering.ClusteringBehaviour;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

public class ClusterNetworkAgentCoalitionBehaviour extends ParallelBehaviour {

	/** The environment model. */
	private EnvironmentModel environmentModel;

	private HashMap<String, Boolean> activeNCMap = new HashMap<String, Boolean>();

	private ClusterNetworkComponent clusterNetworkComponent;

	public ClusterNetworkAgentCoalitionBehaviour(EnvironmentModel environmentModel, ClusterNetworkComponent clusterNetworkComponent) {
		this.environmentModel = environmentModel;
		this.clusterNetworkComponent = clusterNetworkComponent;
		sendMessagesToActiveNCs(clusterNetworkComponent.getNetworkComponents());
	}

	private void sendMessagesToActiveNCs(ArrayList<NetworkComponent> activeNCs) {
		for (NetworkComponent networkComponent : activeNCs) {
			activeNCMap.put(networkComponent.getId(), false);
			createRequest(networkComponent.getId());
			addSubBehaviour(new ClusterNetworkAgentProposeBehaviour(this, myAgent, createRequest(networkComponent.getId()), networkComponent.getId()));
		}
	}

	private ACLMessage createRequest(String receiver) {
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
		request.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		try {
			request.setContentObject(clusterNetworkComponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

	public void addAgree(String networkComponentID) {
		activeNCMap.put(networkComponentID, true);
		for (Boolean value : activeNCMap.values()) {
			if( !value ){
				return;
			}
		}
		reacreateCluster();
	}

	private void reacreateCluster() {
		NetworkModel clusteredNM = getClusteredModel();
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (String id : clusterNetworkComponent.getNetworkComponentIDs()) {
			networkComponents.add(clusteredNM.getNetworkComponent(id));
		}
		clusteredNM.replaceComponentsByCluster(networkComponents);
		changeDisplay(clusteredNM);
	}

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
}
