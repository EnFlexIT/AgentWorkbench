package gasmas.clustering.coalitions;

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agentgui.envModel.graph.networkModel.NetworkComponent;

public class CoalitionANCAuthorityBehaviour extends ParallelBehaviour {

	private CoalitionBehaviour coalitionBehaviour;
	
	private HashMap<String, Boolean> activeNCMap = new HashMap<String, Boolean>();

	public CoalitionANCAuthorityBehaviour(CoalitionBehaviour coalitionBehaviour, ArrayList<NetworkComponent> activeNCs) {
		this.coalitionBehaviour = coalitionBehaviour;
		sendMessagesToActiveNCs(activeNCs);
	}
	
	private void sendMessagesToActiveNCs(ArrayList<NetworkComponent> activeNCs) {
		for (NetworkComponent networkComponent : activeNCs) {
			activeNCMap.put(networkComponent.getId(), false);
			createRequest(networkComponent.getId());
			addSubBehaviour(new CoalitionANCProposeBehaviour(this, myAgent, createRequest(networkComponent.getId()), networkComponent.getId()));
		}
	}

	private ACLMessage createRequest(String receiver) {
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
		request.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		try {
			request.setContentObject(coalitionBehaviour.getSuggestedClusterNetworkComponent());
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
		coalitionBehaviour.startClusterAgent();
	}
}
