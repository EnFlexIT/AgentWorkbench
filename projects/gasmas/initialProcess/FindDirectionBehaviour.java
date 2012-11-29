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
package gasmas.initialProcess;

import gasmas.agents.components.BranchAgent;
import gasmas.agents.components.CompressorAgent;
import gasmas.agents.components.ControlValveAgent;
import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.agents.components.PipeAgent;
import gasmas.agents.components.PipeShortAgent;
import gasmas.agents.components.SimpleValveAgent;
import gasmas.ontology.DirectionSettingNotification;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.GraphEdgeDirection;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentDirectionSettings;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class FindDirectionBehaviour.
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class FindDirectionBehaviour {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4471250444116997490L;

	/** Integer, which holds the maximal age of a ?-message. */
	private static final int maxMsgAge = 8;

	/** NetworkComponentNames, which has positive flow. */
	private HashSet<String> incoming = new HashSet<String>();

	/** NetworkComponentNames, which has negative flow. */
	private HashSet<String> outgoing = new HashSet<String>();

	/** NetworkComponentNames, which has no flow. */
	private HashSet<String> dead = new HashSet<String>();

	/** The agent, how started the behaviour. */
	private GenericNetworkAgent myAgent;
	
	/** The partent behaviour. */
	private InitialProcessBehaviour parentBehaviour;

	/** The network model. */
	private NetworkModel myNetworkModel;

	/** My own NetworkComponent. */
	private NetworkComponent myNetworkComponent;

	/** Shows, if this NetworkComponent already set the directions. */
	private boolean done = false;

	/** Shows, if this NetworkComponent has fixed directions. */
	private boolean fixed = false;

	/** Shows if this component had started. */
	private boolean startdone = false;

	/** Shows if this component is allowed to guessing the next step. */
	private boolean tryToGuess = false;

	/** Saves the neighbours of this component. */
	private Vector<NetworkComponent> myNeighbours;

	/**
	 * Shows if this class is already in step 2
	 *
	 * @param dead the dead
	 */
	public boolean isStep2() {
		return tryToGuess;
	}

	/**
	 * Adds the dead.
	 *
	 * @param dead the dead
	 */
	public void addDead(String dead) {
		this.dead.add(dead);
	}

	/**
	 * Gets the dead.
	 *
	 * @return the dead
	 */
	public HashSet<String> getDead() {
		return dead;
	}

	/**
	 * Adds the incoming.
	 *
	 * @param incoming the incoming
	 */
	public void addIncoming(String incoming) {
		this.incoming.add(incoming);
	}

	/**
	 * Gets the incoming.
	 *
	 * @return the incoming
	 */
	public HashSet<String> getIncoming() {
		return incoming;
	}

	/**
	 * Adds the outgoing.
	 *
	 * @param outgoing the outgoing
	 */
	public void addOutgoing(String outgoing) {
		this.outgoing.add(outgoing);
	}

	/**
	 * Gets the outgoing.
	 *
	 * @return the outgoing
	 */
	public HashSet<String> getOutgoing() {
		return outgoing;
	}

	/**
	 * Instantiates a new find direction behaviour.
	 *
	 * @param agent the agent
	 * @param myNetworkModel the my network model
	 * @param parentBehaviour the partent behaviour
	 */
	public FindDirectionBehaviour(GenericNetworkAgent agent, NetworkModel myNetworkModel, InitialProcessBehaviour parentBehaviour) {
		this.myAgent = agent;
		this.myNetworkModel = myNetworkModel;
		this.myNetworkComponent = myNetworkModel.getNetworkComponent(myAgent.getLocalName());
		this.myNeighbours = myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent);
		this.parentBehaviour = parentBehaviour;
	}

	/**
	 * Start of the behaviour, which start the find direction algorithm.
	 */
	public void start() {
		String flow = "";
		
		if (myNetworkComponent.getAgentClassName().equals(EntryAgent.class.getName())) {
			// --- Entry sends to all neighbours its flow ---
			flow = "in";
			sendToAllNeighbours(flow);
		
		} else if (myNetworkComponent.getAgentClassName().equals(ExitAgent.class.getName())) {
			// --- Exit sends to all neighbours its flow ---
			flow = "out";
			sendToAllNeighbours(flow);
		
		} else if (myNetworkComponent.getAgentClassName().equals(PipeAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(PipeShortAgent.class.getName())
				|| myNetworkComponent.getAgentClassName().equals(SimpleValveAgent.class.getName())) {
			
			if (myNeighbours.size() < 2) {
				// --- Pipe, how has only 1 neighbour knows, that it is dead, so inform neighbours ---
				flow = "dead";
				System.out.println(myAgent.getLocalName() + " is dead");
				sendToAllNeighbours(flow);
			}
		
		} else if (myNetworkComponent.getAgentClassName().equals(ControlValveAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(CompressorAgent.class.getName())) {
			// --- ControlValves and Compressors are directed, so get the information out of the network model ---
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

			// --- Send these information to the appropriate neighbour ---
			if (netCompToHash != null) {
				NetworkComponent netCompTo = netCompToHash.iterator().next();
				outgoing.add(netCompTo.getId());
				msgSend(netCompTo.getId(), new FindDirectionData("in"));
			}
			if (netCompFromHash != null) {
				NetworkComponent netCompFrom = netCompFromHash.iterator().next();
				incoming.add(netCompFrom.getId());
				msgSend(netCompFrom.getId(), new FindDirectionData("out"));
			}
			// --- Component is done ---
			fixed = true;
			done = true;
		}
		// --- The start of the component is done ---
		startdone = true;
	}

	/**
	 * Start of the second step, where the component try to guess (to find cycles).
	 */
	public void startStep2() {
		// --- Boolean to show the component, that guessing is allowed ---
		tryToGuess = true;

		// --- Check, if we have less or equal than two edges with no direction
		// (so it could be start point of a cycle) ---
		int i = 0;
		int p = 0;
		String toInform = "";
		Iterator<NetworkComponent> it1 = myNeighbours.iterator();
		while (it1.hasNext()) {
			String neighbour = it1.next().getId();
			if (incoming.contains(neighbour) || getDead().contains(neighbour) || outgoing.contains(neighbour)) {
				i += 1;
			} else {
				toInform += "::" + neighbour;
			}
			p += 1;
		}
		if ((p - i) <= 2 && i > 0) {
			// System.out.println("Start second step " + myAgent.getLocalName());
			// --- Possible start point found, start guessing ---
			for (int k = 1; k < toInform.split("::").length; k++) {
				msgSend(toInform.split("::")[k], new FindDirectionData(myAgent.getLocalName(), "?"));
			}
		}
	}

	/**
	 * Send its flow to the neighbours.
	 *
	 * @param flow the flow
	 */
	private void sendToAllNeighbours(String flow) {
		// --- Component is done ---
		done = true;
		// --- Send the flow to all neigbours ---
		Iterator<NetworkComponent> it1 = myNeighbours.iterator();
		while (it1.hasNext()) {
			String receiver = it1.next().getId();
			msgSend(receiver, new FindDirectionData(flow));
		}
	}

	/**
	 * Get the messages and calls the appropriate method to deal with this type of message.
	 *
	 * @param msg the msg
	 */
	public synchronized void interpretMsg(EnvironmentNotification msg) {
		// --- Wait until the start is done ---
		while (!startdone) {
			System.err.println("Start problem (FD) at " + myAgent.getLocalName());
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		FindDirectionData content = (FindDirectionData) ((InitialBehaviourMessageContainer) msg.getNotification()).getData();
		String sender = msg.getSender().getLocalName();
		// --- Check the flow and call the appropriate method ---
		if (content.getFlow().equals("in")) {
			forwarding(sender, content, getOutgoing(), getIncoming(), "in");
		} else if (content.getFlow().equals("out")) {
			forwarding(sender, content, getIncoming(), getOutgoing(), "out");
		} else if (content.getFlow().equals("dead")) {
			deadPipe(sender);
		} else if (content.getFlow().equals("?")) {
			forwardingMaybe(sender, content);
		}
		// --- Send the information about a deleted message to the manager agent ---
		try {
			wait(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg-"));
	}

	/**
	 * Method to interpret flow messages.
	 *
	 * @param sender the sender
	 * @param content the content
	 * @param hashSet2 the hash set2
	 * @param hashSet the hash set
	 * @param msg the msg
	 */
	private void forwarding(String sender, FindDirectionData content, HashSet<String> hashSet2, HashSet<String> hashSet, String msg) {
		if (hashSet2.contains(sender)) {
			// --- Information, which did not fit the information of this component, contradiction ---
//			System.out.println("Error, message with a wrong direction at " + myAgent.getLocalName() + " to " + sender);
		} else {
			done = false;
			if (content.getWay().equals("")) {
				// --- Information about the flow get add to the appropriate HashSet ---
				hashSet.add(sender);
			} else {
				hashSet.add(sender);
				done = true;
				if (!myNetworkComponent.getAgentClassName().equals(BranchAgent.class.getName()) && !content.getWay().split("::")[0].equals("end")) {
					hashSet2.add(content.getWay().split("::")[0]);
					msgSend(content.getWay().split("::")[0], new FindDirectionData(deleteFirstStation(content.getWay()), msg));
				}
			}
			// --- Try to interpret the information to find new information ---
			int i = 0;
			int p = 0;
			String toInform = "";
			Iterator<NetworkComponent> it1 = myNeighbours.iterator();
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
				// --- Found the situation, that only one neighbour
				// has no information and all other neighbours are incoming or outgoing ---
				if (hashSet2.contains(toInform) == false) {
					// --- Inform neighbour about this new information ---
					hashSet2.add(toInform);
					msgSend(toInform, new FindDirectionData(msg));
				}
				setDirections();
			} else if ((p - i) > 1) {
				// --- Try to guess the next step, only if the component is in the second step ---
				if (tryToGuess) {
					Iterator<NetworkComponent> it11 = myNeighbours.iterator();
					while (it11.hasNext()) {
						String neighbour = it11.next().getId();
						if (neighbour.equals(sender) == false) {
							msgSend(neighbour, new FindDirectionData(myAgent.getLocalName(), "?"));
						}
					}
				}
			}
		}
	}

	/**
	 * Method to interpret message, which inform about a dead component.
	 *
	 * @param sender the sender
	 */
	private void deadPipe(String sender) {
		// --- Information about the flow get add to the appropriate HashSet ---
		addDead(sender);
//		System.out.println(myAgent.getLocalName() + " is partly dead, because of " + sender);
		// --- Try to interpret the information to find new information ---
		// --- First check, if all neighbours except one are dead ---
		int i = 0;
		int p = 0;
		String toInform = "";
		Iterator<NetworkComponent> it1 = myNeighbours.iterator();
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
			// --- If all neighbours except one are dead, the other one is also dead ---
			addDead(toInform);
			msgSend(toInform, new FindDirectionData("dead"));
			System.out.println(myAgent.getLocalName() + " All dead: " + getDead());
			return;
		}
		// --- Second check, if all neighbours except one are dead or incoming ---
		i = 0;
		p = 0;
		toInform = "";
		it1 = myNeighbours.iterator();
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
			// --- If all neighbours except one are dead or incoming, then the one must be outgoing ---
			addOutgoing(toInform);
			msgSend(toInform, new FindDirectionData("in"));
			setDirections();
			return;

		}
		// --- Second check, if all neighbours except one are dead or outgoing ---
		i = 0;
		p = 0;
		toInform = "";
		it1 = myNeighbours.iterator();
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
			// --- If all neighbours except one are dead or outgoing, then the one must be incoming ---
			addIncoming(toInform);
			msgSend(toInform, new FindDirectionData("out"));
			setDirections();
			return;

		}
	}

	/**
	 * Method to interpret guessed flow messages.
	 *
	 * @param sender the sender
	 * @param content the content
	 */
	private void forwardingMaybe(String sender, FindDirectionData content) {
		// --- Check if the information is to "old", too many hops ---
		if (content.getWay().split("::").length < maxMsgAge) {
			if (content.getWay().split("::")[0].equals(myAgent.getLocalName())) {
				if (incoming.size() + outgoing.size() + dead.size() != myNeighbours.size()) {
					// --- Got my own ? message, so this should be a cycle ---
//					System.out.println("My own name in the way: " + myAgent.getLocalName() + " Way: " + content.getWay().toString());
					Iterator<NetworkComponent> it1 = myNeighbours.iterator();
					String inform = "";
					String direction = "";
					boolean found = false;
					String newFlow = "";
					// --- Check, which neighbours have to inform about the cycle ---
					// --- Check, which flow have to be assigned to the neighbours from outside ---
					while (it1.hasNext()) {
						String neighbour = it1.next().getId();
						for (int i = 1; i < content.getWay().split("::").length; i++) {
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
					if (incoming.contains(direction)) {
						newFlow = "in";
					}
					if (outgoing.contains(direction)) {
						newFlow = "out";
					}
					// // --- Check, which flow have to be assigned to the neighbours from inside ---
					// if (newFlow.equals("")) {
					// for (int i = 1; i < content.getWay().split("::").length; i++) {
					// if (incoming.contains(content.getWay().split("::")[i])) {
					// newFlow = "out";
					// }
					// if (outgoing.contains(content.getWay().split("::")[i])) {
					// newFlow = "in";
					// }
					// if (!newFlow.equals("")) {
					// break;
					// }
					// }
					// }
					if (!newFlow.equals("")) {
						// --- Only use the information about a cycle, if we have 2 unknown neighbours ---
						if (inform.split("::").length == 3) {
							for (int i = 1; i < inform.split("::").length; i++) {
								if (newFlow.equals("in")) {
									outgoing.add(inform.split("::")[i]);
								} else if (newFlow.equals("out")) {
									incoming.add(inform.split("::")[i]);
								}
//								System.out.println("Send direction from " + myAgent.getLocalName() + " to " + inform.split("::")[i] + " with " + newFlow);
								if (!inform.split("::")[i].equals(content.getWay().split("::")[1])) {
									content.setWay(changeOrder(content.getWay()));
								}
								msgSend(inform.split("::")[i], new FindDirectionData(deleteFirstStation(deleteFirstStation(content.getWay())), newFlow));
							}
							done = true;
							setDirections();
						}
					} else {
						// --- Maybe found an cycle, but also did not know the direction of the third neighbour ---
//						System.out.println("No direction for my own way!" + myAgent.getLocalName() + "   " + direction);
					}
				}
			} else {
				// System.out.println(myAgent.getLocalName() + "    " + content.getWay());
				// --- Check, if this station is two times in the way, but is not the start point ---
				for (int i = 1; i < content.getWay().split("::").length; i++) {
					if (content.getWay().split("::")[i].equals(myAgent.getLocalName())) {
						// --- Kill this ?-message ---
						return;
					}
				}
				/*
				 * The estimated information get forward to all neighbours, except the sender
				 */
				Iterator<NetworkComponent> it11 = myNeighbours.iterator();
				while (it11.hasNext()) {
					String neighbour = it11.next().getId();
					if (neighbour.equals(sender) == false) {
						msgSend(neighbour, new FindDirectionData(content.getWay() + "::" + myAgent.getLocalName(), "?"));

					}
				}
			}
		} else {
			/* Message is too old and get ignored */
			// System.out.println("Message too old, " + myAgent.getLocalName());
		}
	}

	/**
	 * Change the order of the stations.
	 *
	 * @param way the way
	 * @return the string
	 */
	private String changeOrder(String way) {
		String wayWithNewOrder = "";
		for (int i = way.split("::").length - 1; i >= 0; i--) {
			wayWithNewOrder += "::" + way.split("::")[i];
		}
		if (wayWithNewOrder.startsWith("::" + myAgent.getLocalName())) {
			return wayWithNewOrder.substring(2);
		} else {
			return myAgent.getLocalName() + wayWithNewOrder;
		}

	}

	/**
	 * Delete the first station from the way (so the first string in front of ::).
	 *
	 * @param way the way
	 * @return way without the first station
	 */
	private String deleteFirstStation(String way) {
		String wayWithoutFirst = "";
		for (int i = 1; i < way.split("::").length; i++) {
			wayWithoutFirst += "::" + way.split("::")[i];
		}
		if (wayWithoutFirst.equals("")) {
			return "end";
		} else {
			return wayWithoutFirst.substring(2);
		}
	}

	/**
	 * Set the directions to the network model / network manager.
	 */
	public void setDirections() {
		if (!done && !fixed) {
			// --- Delete the list of maybe flows ---
			// --- Check, if we have information, which we can set in the network model ---
			if (((!incoming.isEmpty() || !outgoing.isEmpty()) && this.myNeighbours.size() > 2) || (!incoming.isEmpty() && !outgoing.isEmpty() && this.myNeighbours.size() == 2)) {

//				System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead());
				// --- Set information in the network model ---
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
				// --- Send the information to the network manager, that the manager can distribute them ---
				DirectionSettingNotification dsn = new DirectionSettingNotification();
				dsn.setNotificationObject(myNetworkComponent);
				myAgent.sendManagerNotification(dsn);
			}

		}
		// --- Component is done ---
		done = true;

	}

	/**
	 * Send a message about the simulation service.
	 *
	 * @param receiver the receiver
	 * @param content the content
	 */
	public void msgSend(String receiver, GenericMesssageData content) {
		parentBehaviour.msgSend(receiver, content);
	}
}
