package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.lang.acl.ACLMessage;

public class MessengerBehaviour extends ResourceAllocationBehaviour {

	private static final long serialVersionUID = 2050608942933290538L;

	/** NetworkComponentNames, which has positive flow */
	private List<String> incoming;

	/** NetworkComponentNames, which has negative flow */
	private List<String> outgoing;

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** Are we already checking a need request? */
	private boolean checkneed = false;

	/** My own NetworkComponent. */
	@SuppressWarnings("unused")
	private NetworkComponent thisNetworkComponent;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

	/**
	 * Msgpool, which contains messages, which are not needed/allowed at the
	 * moment
	 */
	private List<EnvironmentNotification> msgpool = new ArrayList<EnvironmentNotification>();

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public MessengerBehaviour(GenericNetworkAgent agent, EnvironmentModel environmentModel) {
		this.environmentModel = environmentModel;
		networkModel = (NetworkModel) this.environmentModel.getDisplayEnvironment();
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());
		incoming = getIncoming();
		outgoing = getOutgoing();
//		incoming = new ArrayList<String>();
//		outgoing = new ArrayList<String>();
//		if (myAgent.getLocalName().equals("n4")) {
//			incoming.add("n8");
//			outgoing.add("n11");
//		} else if (myAgent.getLocalName().equals("n5")) {
//			incoming.add("n9");
//			outgoing.add("n10");
//		} else if (myAgent.getLocalName().equals("n6")) {
//			incoming.add("n11");
//			outgoing.add("n7");
//		} else if (myAgent.getLocalName().equals("n10")) {
//			incoming.add("n5");
//			outgoing.add("n15");
//			outgoing.add("n11");
//		} else if (myAgent.getLocalName().equals("n11")) {
//			incoming.add("n10");
//			incoming.add("n4");
//
//			outgoing.add("n7");
//		} else if (myAgent.getLocalName().equals("n15")) {
//			incoming.add("n10");
//			outgoing.add("n14");
//		} else if (myAgent.getLocalName().equals("n14")) {
//			incoming.add("n15");
//			outgoing.add("n13");
//		} else if (myAgent.getLocalName().equals("n13")) {
//			incoming.add("n14");
//			outgoing.add("n12");
//		}

	}
	
	/**
	 * Get the messages from the queue and calls the appropriate method to deal
	 * with this type of message
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {

	}

	public void interpretMsg(EnvironmentNotification msg) {
//		if (msg.getProtocol() != null && msg.getProtocol().equals("RA")) {
		AllocData content = (AllocData) msg.getNotification();
		if (content.getReason().equals("MAX_INITIAL")) {
			max_initial.put(msg.getSender().getLocalName(), content);
			max();
		} else if (content.getReason().equals("NEED_INITIAL")) {
			if (checkneed) {
				msgpool.add(msg);
			} else {
				// checkneed = true;
				need.put(msg.getSender().getLocalName() + "::" + content.getWay().get(0).split("::")[0], new AllocDataNeed(content));
				if (need(msg.getSender().getLocalName() + "::" + content.getWay().get(0).split("::")[0]) == false) {
					System.out.println("Nach einem Need Initial keine Ansprechpartner " + myAgent.getLocalName());
					checkneed = false;
					if (msgpool.size() != 0) {
						interpretMsg(msgpool.get(0));
						msgpool.remove(0);
					}
					// We have to inform somebody about this, otherwise
					// deadlock, but this never happen right?
				}
			}
		
		} else if (content.getReason().equals("USE")) {
			use(content);
		}
//		}
	}

	/**
	 * Forward and save the information about allocated resources
	 * 
	 * @param Content
	 *            of the original message
	 * @param Integer
	 *            with the information, if requested are confirmed
	 */
	private void use(AllocData content) {
		AllocData data_send = new AllocData();
		data_send.setCap(content.getCap());

		String last = "";
		/* We got ressources / now we have to show how much */

		/*
		 * Identify the actual last station, which is to inform
		 */
		for (Iterator<String> it = content.getWay().iterator(); it.hasNext();) {
			String route = it.next();
			String[] temp = route.split("::");
			route = temp[0];
			for (int i = 1; i < temp.length; i++) {
				route = route + "::" + temp[i];
				if (temp[i].equals(myAgent.getLocalName())) {
					last = temp[i - 1];
				}
			}
			last = last + "::" + temp[0];
			data_send.addRoute(route);
		}
		if (content.getPerformative() == ACLMessage.CONFIRM) {

			/* Do we have already information about the last station? */
			if (use.containsKey(last)) {
				/*
				 * If yes, we have do modify the use HashMap to the new
				 * situation
				 */
				AllocData temp = use.get(last);
				for (Iterator<String> it = data_send.getWay().iterator(); it.hasNext();) {
					temp.addRoute(it.next());
				}

				temp.setCap(data_send.getCap() + temp.getCap());
				data_send.setCap(temp.getCap());
				data_send.setWay(temp.getWay());
				use.put(last, temp);
			} else {
				/* If no, we can put the information into the use HashMap */
				use.put(last, data_send);
			}

			if (need.get(last).getCap() == content.getCap()) {
				/* Got resources confirmed, as asked */

				/* We can delete the need request */
				need.remove(last);

				/* Send a confirm to the last station with the initial request */

				data_send.setPerformative(ACLMessage.CONFIRM);
				data_send.setReason("USE");
				msgSend(last.split("::")[0], data_send);
				checkneed = false;
				if (msgpool.size() != 0) {
					interpretMsg(msgpool.get(0));
					msgpool.remove(0);
				}
			} else {
				/* Got less resources confirmed, as asked */

				/* Change the need request. We got some resources */
				need.get(last).setCap(need.get(last).getCap() - content.getCap());
				need.get(last).setReducedby(content.getCap());
				/* Check, if we can find another producer */
				if (need(last) == false) {
					/*
					 * We can delete the need request, because we can not found
					 * another producer
					 */
					need.remove(last);
					System.out.println("Kann nicht die kompletten angefragten Ressourcen befriedigen " + myAgent.getLocalName());

					/*
					 * Send a confirm to the last station with the changed
					 * request, which shows how many resources could be
					 * allocated
					 */
					data_send.setPerformative(ACLMessage.CONFIRM);
					data_send.setReason("USE");
					msgSend(last.split("::")[0], data_send);
					checkneed = false;
					if (msgpool.size() != 0) {
						interpretMsg(msgpool.get(0));
						msgpool.remove(0);
					}
				}
			}

		} else if (content.getPerformative() == ACLMessage.DISCONFIRM) {
			/* No resources left, we have to find a new producer */

			if (need(last) == false) {
				/*
				 * No new producer found, inform the last station, that we can
				 * not offer ressources
				 */
				System.out.println("Obwohl Ressourcen angesagt waren, keine mehr vorhanden, bei " + myAgent.getLocalName());

				/* Send a decline to the last station with the initial request */
				if (need.get(last).getReducedby() > 0) {
					data_send.setPerformative(ACLMessage.CONFIRM);
					data_send.setCap(need.get(last).getReducedby());
					data_send.setWay(need.get(last).getWay());
				} else {
					data_send.setPerformative(ACLMessage.DISCONFIRM);
				}
			
				data_send.setReason("USE");
				msgSend(last.split("::")[0], data_send);

				/* Delete request for resources out of the HashMap */
				need.remove(last);
				checkneed = false;
				if (msgpool.size() != 0) {
					interpretMsg(msgpool.get(0));
					msgpool.remove(0);
				}
			}

		}
	}

	/**
	 * Forward and save the information about the maximal input flow
	 * 
	 */
	private void max() {
		/*
		 * Only sends is "own" production to its neighbours if every incoming
		 * node has informed it about their production
		 */
		boolean all = true;
		Iterator<String> it1 = incoming.iterator();
		while (it1.hasNext()) {
			if (max_initial.containsKey(it1.next()) == false)
				all = false;
		}
		if (all) {

			/* Prepare data to forward initial production. */
			AllocData data = new AllocData();

			/*
			 * Iterator that go trough each entry of the production HashMap to
			 * summarise its "own" production Summarising = Add capacity up and
			 * put itself to the route
			 */
			Iterator<Entry<String, AllocData>> it = max_initial.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, AllocData> max_initial_entry = it.next();
				data.setCap(data.getCap() + max_initial_entry.getValue().cap);

				String[] way = (String[]) max_initial_entry.getValue().way.toArray(new String[0]);
				for (int i = 0; i < way.length; i++) {
					data.addRoute(way[i] + "::" + myAgent.getLocalName());
				}

			}

			/*
			 * Put the data object to the answer. The data object includes the
			 * reason, the production and the "way"
			 */
			data.setReason("MAX_INITIAL");
			data.setPerformative(ACLMessage.INFORM);
			/* Add neighbours, which are consumers, to receivers */
			Iterator<String> it11 = outgoing.iterator();
			while (it11.hasNext()) {
				String receiver=it11.next();
				msgSend(receiver, data);
			}
		}
	}

	/**
	 * As the neighbours about free resources
	 * 
	 * @param Sender
	 *            as string
	 * @return Returns true, if you could ask somebody about resources, returns
	 *         false, if no one would asked
	 */
	private boolean need(String name) {
		{
			AllocDataNeed data_total = need.get(name);
			/*
			 * Iterating the list of producers to find producer, which open
			 * resources
			 */
			Iterator<Entry<String, AllocData>> it = max_initial.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, AllocData> max_initial_entry = it.next();

				/* Check, if we already asked this producers for this request */
				if (data_total.getAsked().contains(max_initial_entry.getKey()) == false) {

					/*
					 * Create message data to inform the next station about the
					 * need
					 */
					AllocDataNeed data = new AllocDataNeed();
					data.setCap(data_total.getCap());
					/* We add ourself to the route */
					String[] way = (String[]) data_total.way.toArray(new String[0]);
					for (int i = 0; i < way.length; i++) {
						data.addRoute(way[i] + "::" + myAgent.getLocalName());
					}

					/*
					 * Put the data object to the answer. The data object
					 * includes the reason, the production and the "way"
					 */
					data.setReason("NEED_INITIAL");
					data.setPerformative(ACLMessage.REQUEST);

					/* Add receiver, which is the producer that we identified */
					msgSend(max_initial_entry.getKey(), data);
					
					/* Add receiver to the list of the already asked producers */
					data_total.addAsked(max_initial_entry.getKey());

					/*
					 * We found a producer, that deliver all/some asked
					 * resources Now we have update the need in the HashMap
					 */
					need.put(name, data_total);
					return true;

				}
			}
			/* All known producers asked, but do not find any open resources */
			return false;
		}
	}

	/**
	 * Generate the status of this agent. What information do this agent have?
	 * 
	 * @return Status as string
	 */
	public String Status() {

		Iterator<Entry<String, AllocData>> it = max_initial.entrySet().iterator();
		String test = "Alles";
		while (it.hasNext()) {
			Entry<String, AllocData> hallo = it.next();
			test = test + "//::\\" + hallo.getKey() + "::" + hallo.getValue().getCap() + "::" + hallo.getValue().way;
		}
		Iterator<Entry<String, AllocDataNeed>> it1 = need.entrySet().iterator();
		test = test + "|||||NEED";
		while (it1.hasNext()) {
			Entry<String, AllocDataNeed> hallo = it1.next();
			test = test + "//::\\" + hallo.getKey() + "::" + hallo.getValue().getCap() + "::" + hallo.getValue().way + "::" + hallo.getValue().asked;
		}
		it = use.entrySet().iterator();
		test = test + "|||||USE";
		while (it.hasNext()) {
			Entry<String, AllocData> hallo = it.next();
			test = test + "//::\\" + hallo.getKey() + "::" + hallo.getValue().getCap() + "::" + hallo.getValue().way;
		}
		return test;
	}
}
