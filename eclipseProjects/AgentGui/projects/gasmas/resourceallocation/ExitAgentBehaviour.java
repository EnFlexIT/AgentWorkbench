package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;

import java.util.Iterator;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.lang.acl.ACLMessage;

public class ExitAgentBehaviour extends ResourceAllocationBehaviour {

	private static final long serialVersionUID = 4471250444116997490L;

	/** Initial need of this exit */
	private int need = 3;
	/** Not allocated need */
	private int open_need = need;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

	
	public ExitAgentBehaviour(SimulationServiceBehaviour simServiceBehaviour, NetworkModel networkModel, NetworkComponent networkComponent) {
		super(simServiceBehaviour, networkModel, networkComponent);
		myAgent = (GenericNetworkAgent) simServiceBehaviour.getAgent();
	}

	public void interpretMsg(EnvironmentNotification msg) {
		// if (msg.getProtocol() != null && msg.getProtocol().equals("RA")) {
		AllocData content =(AllocData) msg.getNotification();
		if (content.getReason().equals("MAX_INITIAL")) {
			max(msg.getSender().getLocalName().split("@")[0], content);
		} else if (content.getReason().equals("USE")) {
			use(msg.getSender().getLocalName(), content);

		}
		// }
	}

	/**
	 * Interpret if the need request was positive or negative
	 * 
	 * @param sender
	 * @param content
	 */
	private void use(String sender, AllocData content) {
		if (content.getPerformative() == ACLMessage.CONFIRM) {
			use.put(sender, content);
			open_need -= content.cap;
			if (need == content.getCap()) {
				System.out.println("All ressources allocated for " + myAgent.getLocalName());
			} else {
				System.out.println("Some ressources allocated for " + myAgent.getLocalName() + "(" + content.getCap() + ")");
				/* Try to find another way for resources */
				// need_prio(sender, content);
			}
		} else if (content.getPerformative() == ACLMessage.DISCONFIRM) {
			System.out.println("No ressources found at all for " + myAgent.getLocalName());
			// need_prio(sender, content);
		}
	}

	
	/**
	 * Get information about the production and answered with his needs
	 * 
	 * @param sender
	 * @param content
	 */
	private void max(String sender, AllocData content) {
		/* Put the initial production into the production HashMap */
		max_initial.put(sender, content);

		send_need(sender);

	}

	private void send_need(String sender) {
		msgSend(sender, new AllocData(open_need, myAgent.getLocalName(),ACLMessage.REQUEST,"NEED_INITIAL"));
	}

	/**
	 * Generate the status of this agent. What information do this agent have?
	 * 
	 * @return Status as string
	 */
	public String Status() {

		Iterator<Entry<String, AllocData>> it11 = max_initial.entrySet().iterator();
		String test = "Exit::Need " + need + "/" + open_need;
		while (it11.hasNext()) {
			Entry<String, AllocData> hallo = it11.next();
			test = test + "//::\\" + hallo.getKey() + "::" + hallo.getValue().cap + "::" + hallo.getValue().way;
		}
		it11 = use.entrySet().iterator();
		test = test + "|||||USE";
		while (it11.hasNext()) {
			Entry<String, AllocData> hallo = it11.next();
			test = test + "//::\\" + hallo.getKey() + "::" + hallo.getValue().getCap() + "::" + hallo.getValue().way;
		}
		return test;
	}

}
