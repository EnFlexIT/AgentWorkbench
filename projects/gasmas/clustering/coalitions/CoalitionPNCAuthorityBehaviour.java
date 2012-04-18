package gasmas.clustering.coalitions;

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agentgui.envModel.graph.networkModel.NetworkComponent;

public class CoalitionPNCAuthorityBehaviour extends ParallelBehaviour {

	private CoalitionBehaviour coalitionBehaviour;

	private HashMap<String, Boolean> passiveNCMap = new HashMap<String, Boolean>();

	public CoalitionPNCAuthorityBehaviour(CoalitionBehaviour coalitionBehaviour, ArrayList<NetworkComponent> activeNCs) {
		this.coalitionBehaviour = coalitionBehaviour;
		notifyPassiveNetworkComponents(activeNCs);
	}

	private void notifyPassiveNetworkComponents(ArrayList<NetworkComponent> activeNCs) {
		ArrayList<NetworkComponent> passiveNCs = new ArrayList<NetworkComponent>(coalitionBehaviour.getSuggestedClusterNetworkComponent().getClusterNetworkModel().getNetworkComponents().values());
		passiveNCs.removeAll(activeNCs);
		for (NetworkComponent networkComponent : passiveNCs) {
			addPassiveNetworkComponentBehaviour(networkComponent.getId());
		}
	}

	private void addPassiveNetworkComponentBehaviour(String receiver) {
		passiveNCMap.put(receiver, false);
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		try {
			request.setContentObject(coalitionBehaviour.getSuggestedClusterNetworkComponent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		addSubBehaviour(new CoalitionPNCRequestBehaviour(this, myAgent, request, receiver));
	}

	public void addAgree(String networkComponentID) {
		passiveNCMap.put(networkComponentID, true);
		for (Boolean value : passiveNCMap.values()) {
			if (!value) {
				return;
			}
		}
		coalitionBehaviour.startClusterAgent();
	}
}
