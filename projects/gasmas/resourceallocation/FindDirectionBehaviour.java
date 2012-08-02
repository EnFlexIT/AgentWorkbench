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
package gasmas.resourceallocation;

import gasmas.agents.components.CompressorAgent;
import gasmas.agents.components.ControlValveAgent;
import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.agents.components.PipeAgent;
import gasmas.agents.components.PipeShortAgent;
import gasmas.agents.components.SimpleValveAgent;
import gasmas.ontology.DirectionSettingNotification;
import jade.core.AID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import agentgui.envModel.graph.networkModel.GraphEdgeDirection;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentDirectionSettings;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class FindDirectionBehaviour {

	private static final long serialVersionUID = 4471250444116997490L;

	private static final int maxMsgAge = 10;

	/** NetworkComponentNames, which has positive flow */
	private HashSet<String> incoming = new HashSet<String>();

	/** NetworkComponentNames, which has negative flow */
	private HashSet<String> outgoing = new HashSet<String>();

	/** NetworkComponentNames, which could have a positive or negative flow */
	private HashSet<String> optional = new HashSet<String>();

	/** NetworkComponentNames, which has no flow */
	private HashSet<String> dead = new HashSet<String>();

	/** NetworkComponentNames, which has maybe positive flow */
	private HashMap<String, String> incomingMaybe = new HashMap<String, String>();

	/** NetworkComponentNames, which has maybe negative flow */
	private HashMap<String, String> outgoingMaybe = new HashMap<String, String>();

	/** The network model. */
	private NetworkModel myNetworkModel;

	/** My own NetworkComponent. */
	private NetworkComponent myNetworkComponent;

	/** Shows, if this NetworkComponent already set the directions. */
	private boolean done = false;

	/** Shows if this component had started */
	private boolean startdone = false;
	
	/** Shows if this component had started */
	private GenericNetworkAgent myAgent;

	/**
	 * @param agent
	 * @param myNetworkComponent
	 * @param myNetworkModel
	 * @param myNetworkModel
	 * @param environmentModel
	 */
	public FindDirectionBehaviour(GenericNetworkAgent agent, NetworkModel myNetworkModel, NetworkComponent myNetworkComponent) {
		this.myAgent=agent;
		this.myNetworkModel = myNetworkModel;
		this.myNetworkComponent = myNetworkComponent;
		
	}

	/**
	 * Sends its initial flow to its neighbours
	 * 
	 */
	public void start() {
		String msg = "";
		if (myNetworkComponent.getAgentClassName().equals(EntryAgent.class.getName())) {
			msg = "in";
			sendall(msg);
		} else if (myNetworkComponent.getAgentClassName().equals(ExitAgent.class.getName())) {
			msg = "out";
			sendall(msg);
		} else if (myNetworkComponent.getAgentClassName().equals(PipeAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(PipeShortAgent.class.getName())
				|| myNetworkComponent.getAgentClassName().equals(SimpleValveAgent.class.getName())) {
			if (myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).size() < 2) {
				msg = "dead";

				System.out.println(myAgent.getLocalName() + " is dead");
				sendall(msg);
			}
		} else if (myNetworkComponent.getAgentClassName().equals(ControlValveAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(CompressorAgent.class.getName())) {

			NetworkComponentDirectionSettings netCompDirect = new NetworkComponentDirectionSettings(myNetworkModel, myNetworkComponent);
			myNetworkComponent.setEdgeDirections(netCompDirect.getEdgeDirections());
			myNetworkModel.setDirectionsOfNetworkComponent(myNetworkComponent);

			DirectionSettingNotification dsn = new DirectionSettingNotification();
			dsn.setNotificationObject(myNetworkComponent);
			myAgent.sendManagerNotification(dsn);

			GraphEdgeDirection ged = netCompDirect.getEdgeDirections().values().iterator().next();
			String toComGraphNodeID = ged.getGraphNodeIDTo();
			GraphNode toComGraphNode = (GraphNode) myNetworkModel.getGraphElement(toComGraphNodeID);
			HashSet<NetworkComponent> netCompToHash = netCompDirect.getNeighbourNetworkComponent(toComGraphNode);

			String fromComGraphNodeID = ged.getGraphNodeIDFrom();
			GraphNode fromComGraphNode = (GraphNode) myNetworkModel.getGraphElement(fromComGraphNodeID);
			HashSet<NetworkComponent> netCompFromHash = netCompDirect.getNeighbourNetworkComponent(fromComGraphNode);
			if (netCompToHash != null) {
				NetworkComponent netCompTo = netCompToHash.iterator().next();
				outgoing.add(netCompTo.getId());
				msgSend(netCompTo.getId(), new FindDirData("in"));
			}
			if (netCompFromHash != null) {
				NetworkComponent netCompFrom = netCompFromHash.iterator().next();
				incoming.add(netCompFrom.getId());
				msgSend(netCompFrom.getId(), new FindDirData("out"));
			}
			done = true;
		}
		startdone = true;
	}

	private void sendall(String msg) {
		/*
		 * Add neighbour to receivers, which should be only one for Exits and
		 * Entries and for dead pipes
		 */
		done = true;
		Iterator<NetworkComponent> it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (it1.hasNext()) {
			String receiver = it1.next().getId();
			msgSend(receiver, new FindDirData(msg));
			// System.out.println("Empänger: "+receiver + " Semder:" +
			// myAgent.getLocalName());
		}
	}

	/**
	 * Get the messages from the queue and calls the appropriate method to deal
	 * with this type of message
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	public synchronized void interpretMsg(EnvironmentNotification msg) {
		while (!startdone) {
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		FindDirData content = (FindDirData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		// System.out.println("Agent: " + myAgent.getLocalName() +
		// " bekommen von " + msg.getSender().getLocalName() + "    " +
		// content.reason);
		if (content.getReason().equals("in")) {

			forwarding(sender, content, getOutgoing(), getIncoming(), "in", incomingMaybe, outgoingMaybe);

		} else if (content.getReason().equals("out")) {

			forwarding(sender, content, getIncoming(), getOutgoing(), "out", outgoingMaybe, incomingMaybe);

		} else if (content.getReason().equals("dead")) {
			deadPipe(sender);
		} else if (content.getReason().equals("in?")) {

			forwardingMaybe(sender, content, incomingMaybe, outgoingMaybe, "in", "out", getIncoming(), getOutgoing());

		} else if (content.getReason().equals("out?")) {

			forwardingMaybe(sender, content, outgoingMaybe, incomingMaybe, "out", "in", getOutgoing(), getIncoming());

		}

		// System.out.println("______" +myAgent.getLocalName() +
		// " IN PROGRESS In: " +
		// getIncoming() + " Out: " + getOutgoing() + " Dead: "
		// + getDead() + " InMaybe: " + incomingMaybe
		// + " OutMaybe: " + outgoingMaybe);

	}

	private void deadPipe(String sender) {
		addDead(sender);
		System.out.println(myAgent.getLocalName() + " is partly dead, because of " + sender);
		int i = 0;
		int p = 0;
		String toInform = "";
		Iterator<NetworkComponent> it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (it1.hasNext()) {
			String neighbour = it1.next().getId();
			if (getDead().contains(neighbour)) {
				i += 1;
			} else {
				toInform = neighbour;
			}
			p += 1;
		}

		if ((p - i) == 1) {
			addDead(toInform);
			msgSend(toInform, new FindDirData("dead"));
			System.out.println(myAgent.getLocalName() + " All dead: " + getDead());

			return;
		}
		i = 0;
		p = 0;
		toInform = "";
		it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (it1.hasNext()) {
			String neighbour = it1.next().getId();
			if (getDead().contains(neighbour) || getIncoming().contains(neighbour)) {
				i += 1;
			} else {
				toInform = neighbour;
			}
			p += 1;
		}

		if ((p - i) == 1) {
			addOutgoing(toInform);
			msgSend(toInform, new FindDirData("in"));
			setDirections();
			return;

		}
		i = 0;
		p = 0;
		toInform = "";
		it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (it1.hasNext()) {
			String neighbour = it1.next().getId();
			if (getDead().contains(neighbour) || getOutgoing().contains(neighbour)) {
				i += 1;
			} else {
				toInform = neighbour;
			}
			p += 1;
		}

		if ((p - i) == 1) {
			addIncoming(toInform);
			msgSend(toInform, new FindDirData("out"));
			setDirections();
			return;

		}

	}

	private void forwardingMaybe(String sender, FindDirData content, HashMap<String, String> temp1, HashMap<String, String> temp2, String msg1, String msg2, HashSet<String> hashSet,
			HashSet<String> hashSet2) {
		/* Check if the information is to "old", too many hops */
		if (content.getWay().split("::").length < maxMsgAge) {
			if (hashSet2.contains(sender)) {
				/* Information is wrong */
				// System.out.println(myAgent.getLocalName() +
				// " got a wrong ? message from " + sender +
				// ". Message get ignored");
			} else {
				boolean cycle = false;
				if (content.getWay().split("::")[0].equals(myAgent.getLocalName())) {
					cycle = true;
				}
				if (cycle) {
					/*
					 * Got my own ? message, so this should be a cycle
					 */
					System.out.println("My own name in the way: " + myAgent.getLocalName() + " Way: " + content.getWay().toString());
					Iterator<NetworkComponent> it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
					String inform = "";
					String direction = "";
					boolean found = false;
					msg1 = "";
					while (it1.hasNext()) {
						String neighbour = it1.next().getId();
						for (int i = 0; i < content.getWay().split("::").length; i++) {
							if (content.getWay().split("::")[i].equals(neighbour)) {
								inform += "::" + neighbour;
								found = true;

							}
						}
						if (!found) {
							direction = neighbour;
						}
						found = false;
					}
					Iterator<String> it2 = incoming.iterator();
					while (it2.hasNext()) {
						String temp = it2.next();
						if (temp.equals(direction)) {
							msg1 = "in";
							break;
						}
					}
					it2 = outgoing.iterator();
					while (it2.hasNext()) {
						String temp = it2.next();
						if (temp.equals(direction)) {
							msg1 = "out";
							break;
						}
					}

					if (!msg1.equals("")) {
						for (int i = 1; i < inform.split("::").length; i++) {
							if (msg1.equals("in")) {
								outgoing.add(inform.split("::")[i]);
							} else if (msg1.equals("out")) {
								incoming.add(inform.split("::")[i]);
							}
							msgSend(inform.split("::")[i], new FindDirData(msg1));
						}
						temp1.clear();
						temp2.clear();
						setDirections();
					}
				} else {
					temp1.put(sender, content.getWay());

					/* The estimated information could be still wrong... */
					Iterator<NetworkComponent> it11 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
					while (it11.hasNext()) {
						String neighbour = it11.next().getId();
						if (neighbour.equals(sender) == false) {
							temp2.put(neighbour, content.getWay());
							msgSend(neighbour, new FindDirData(content.getWay() + "::" + myAgent.getLocalName(), msg1 + "?"));

						}
					}
				}
			}
		} else {
			/* Message is too old and get ignored */
			// System.out.println("Message too old, " + myAgent.getLocalName());
		}
	}

	public void setDirections() {
		if (!done) {
			incomingMaybe.clear();
			outgoingMaybe.clear();
			if (!incoming.isEmpty() || !outgoing.isEmpty()) {
				System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead() + " Opt: " + getOptional() + " InMaybe: " + incomingMaybe
						+ " OutMaybe: " + outgoingMaybe);
				NetworkComponentDirectionSettings netCompDirect = new NetworkComponentDirectionSettings(myNetworkModel, myNetworkComponent);
				HashSet<GraphNode> outerNodes = netCompDirect.getOuterNodes();

				HashSet<NetworkComponent> inComps = new HashSet<NetworkComponent>();
				HashSet<NetworkComponent> outComps = new HashSet<NetworkComponent>();

				HashSet<NetworkComponent> outerComps = netCompDirect.translateGraphNodeHashSet(outerNodes);
				for (Iterator<NetworkComponent> iterator = outerComps.iterator(); iterator.hasNext();) {
					NetworkComponent outerComp = iterator.next();
					for (Iterator<String> iterator2 = getIncoming().iterator(); iterator2.hasNext();) {
						String incoming = iterator2.next();
						if (incoming.equals(outerComp.getId()))
							inComps.add(myNetworkModel.getNetworkComponent(incoming));
					}
					for (Iterator<String> iterator2 = getOutgoing().iterator(); iterator2.hasNext();) {
						String outgoing = iterator2.next();
						if (outgoing.equals(outerComp.getId()))
							outComps.add(myNetworkModel.getNetworkComponent(outgoing));
					}
				}
				netCompDirect.setGraphEdgeDirection(inComps, outComps);

				myNetworkComponent.setEdgeDirections(netCompDirect.getEdgeDirections());
				DirectionSettingNotification dsn = new DirectionSettingNotification();
				dsn.setNotificationObject(myNetworkComponent);
				myAgent.sendManagerNotification(dsn);
			}
		}
		done = true;

	}

	private void forwarding(String sender, FindDirData content, HashSet<String> hashSet2, HashSet<String> hashSet, String msg, HashMap<String, String> temp11, HashMap<String, String> temp22) {
		if (hashSet.contains(sender) == false) {
			if (hashSet2.contains(sender)) {
				// TODO - Think of appropriate behaviour
				// hashSet2.remove(sender);
				getOptional().add(sender);
				System.out.println("_______________________________Maybe error or optional edge found " + myAgent.getLocalName() + " to " + sender);

			} else {
				temp22.clear();
				temp11.clear();
				hashSet.add(sender);
				int i = 0;
				int p = 0;
				String toInform = "";
				Iterator<NetworkComponent> it1 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
				while (it1.hasNext()) {
					String neighbour = it1.next().getId();
					if (hashSet.contains(neighbour) || getDead().contains(neighbour)) {
						i += 1;
					} else {
						toInform = neighbour;
					}
					p += 1;
				}
				if ((p - i) == 1) {

					if (hashSet2.contains(toInform) == false) {
						hashSet2.add(toInform);

						msgSend(toInform, new FindDirData(msg));
					}
					setDirections();
				} else if ((p - i) > 1) {
					// We try to guess the next step

					Iterator<NetworkComponent> it11 = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
					while (it11.hasNext()) {
						String neighbour = it11.next().getId();
						if (neighbour.equals(sender) == false) {
							temp22.put(neighbour, content.getWay());
							msgSend(neighbour, new FindDirData(myAgent.getLocalName(), msg + "?"));
						}
					}
				}

			}
		}
	}

	public void msgSend(String receiver, GenericMesssageData content) {
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), content) == false) {

		}

	}

	public void addDead(String dead) {
		this.dead.add(dead);
	}

	public HashSet<String> getDead() {
		return dead;
	}

	public void addIncoming(String incoming) {
		this.incoming.add(incoming);
	}

	public HashSet<String> getIncoming() {
		return incoming;
	}

	public void addOutgoing(String outgoing) {
		this.outgoing.add(outgoing);
	}

	public HashSet<String> getOutgoing() {
		return outgoing;
	}

	public void addOptional(String optional) {
		this.optional.add(optional);
	}

	public HashSet<String> getOptional() {
		return optional;
	}

}
