package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;
import java.util.Iterator;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.lang.acl.ACLMessage;

public class EntryAgentBehaviour extends ResourceAllocationBehaviour {

	private static final long serialVersionUID = 4471250444116997490L;

	/** Initial production */
	private int prod = 5;
	/** Not used production */
	private int open_prod = prod;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

	
	
	public EntryAgentBehaviour(SimulationServiceBehaviour simServiceBehaviour, NetworkModel networkModel, NetworkComponent networkComponent) {
		super(simServiceBehaviour, networkModel, networkComponent);
		myAgent = (GenericNetworkAgent) simServiceBehaviour.getAgent();
	}
	
	/**
	 * Sends its initial flow to its neighbours
	 * 
	 * @see jade.core.behaviours.Behaviour#onStart()
	 */
	public void onStart() {

		Iterator<NetworkComponent> it1 = networkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (it1.hasNext()) {
			msgSend(it1.next().getId(), new AllocData(prod, myAgent.getLocalName(), ACLMessage.INFORM, "MAX_INITIAL"));
		}

		/* Only test data */
		// open_prod=0;
		// if (myAgent.getLocalName().equals("n9")) {
		// open_prod = 10;
		// }
	}

	/**
	 * Generate the appropriate reaction to a need request
	 * 
	 * @param sender
	 * @param content
	 */
	private void need(String sender, AllocData content) {

		/* Object that has all information about the use that is aligned */
		AllocData data_send = new AllocData();

		/*
		 * Check, if the open production is still enough to supply the asked
		 * resources
		 */
		if (open_prod >= content.getCap()) {
			/* Enough resources */
			open_prod -= content.getCap();
			data_send.setCap(content.getCap());
			use.put(sender, content);
			data_send.setPerformative(ACLMessage.CONFIRM);
		} else if (open_prod > 0) {
			/* Not enough resources, but some */
			data_send.setCap(open_prod);
			open_prod = 0;
			use.put(sender, content);
			data_send.setPerformative(ACLMessage.CONFIRM);
		} else {
			/* No open resources at all */
			data_send.setCap(0);
			data_send.setPerformative(ACLMessage.DISCONFIRM);
		}


		/* We add ourself to the route */
		for (Iterator<String> it = content.getWay().iterator(); it.hasNext();) {
			String route = it.next();
			String[] temp = route.split("::");
			route = temp[0];
			for (int i = 1; i < temp.length; i++) {
				route = route + "::" + temp[i];
			}
			data_send.addRoute(route + "::" + myAgent.getLocalName());

		}

		
		data_send.setReason("USE");
		/* The message goes back to the questioner */
		msgSend(sender, data_send);
	}

	/**
	 * Generate the status of this agent. What information do this agent have?
	 * 
	 * @return Status as string
	 */
	public String Status() {

		return "Entry//::\\" + "Prod " + prod + "//::Open_Prod " + open_prod;
	}

	@Override
	public void interpretMsg(EnvironmentNotification msg) {
		// if (msg.getProtocol() != null && msg.getProtocol().equals("RA")) {

		AllocData content = (AllocData) msg.getNotification();
		if (content.getReason().equals("NEED_INITIAL")) {
			need(msg.getSender().getLocalName(), content);
		}
		// }
	}
	
}
