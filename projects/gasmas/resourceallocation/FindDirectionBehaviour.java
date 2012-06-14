package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;
import gasmas.ontology.DirectionSettingNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdgeDirection;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentDirectionSettings;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

public class FindDirectionBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 4471250444116997490L;

	private static final int maxMsgAge = 10;

	/** NetworkComponentNames, which has positive flow */
	private List<String> incoming = new ArrayList<String>();

	/** NetworkComponentNames, which has negative flow */
	private List<String> outgoing = new ArrayList<String>();

	/** NetworkComponentNames, which could have a positive or negative flow */
	private List<String> optional = new ArrayList<String>();

	/** NetworkComponentNames, which has positive flow */
	private List<String> dead = new ArrayList<String>();

	public void addDead(String dead) {
		this.dead.add(dead);
	}

	public List<String> getDead() {
		return dead;
	}

	public void addIncoming(String incoming) {
		this.incoming.add(incoming);
	}

	public List<String> getIncoming() {
		return incoming;
	}

	public void addOutgoing(String outgoing) {
		this.outgoing.add(outgoing);
	}

	public List<String> getOutgoing() {
		return outgoing;
	}

	public void addOptional(String optional) {
		this.optional.add(optional);
	}

	public List<String> getOptional() {
		return optional;
	}

	/** NetworkComponentNames, which has maybe positive flow */
	private HashMap<String, String> incomingMaybe = new HashMap<String, String>();

	/** NetworkComponentNames, which has maybe negative flow */
	private HashMap<String, String> outgoingMaybe = new HashMap<String, String>();

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** Shows if this component thinks the finddirection action is done */
	private boolean done = false;

	/** The network model. */
	private NetworkModel networkModel;

	/** My own NetworkComponent. */
	private NetworkComponent thisNetworkComponent;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public FindDirectionBehaviour(GenericNetworkAgent agent, long period, EnvironmentModel environmentModel) {
		super(agent, period);
		this.environmentModel = environmentModel;
		networkModel = (NetworkModel) this.environmentModel.getDisplayEnvironment();
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());
	}

	/**
	 * Sends its initial flow to its neighbours
	 * 
	 * @see jade.core.behaviours.Behaviour#onStart()
	 */
	public void onStart() {
		String msg = "";
		if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.EntryAgent")) {
			msg = "in";
			sendall(msg);
		} else if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.ExitAgent")) {
			msg = "out";
			sendall(msg);
		} else if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.PipeAgent") || thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.PipeShortAgent")
				|| thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.SimpleValveAgent")) {
			if (networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() < 2) {
				msg = "dead";
				System.out.println(myAgent.getLocalName() + " is dead");
				sendall(msg);
			}
		} else if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.ControlValveAgent")
				|| thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.CompressorAgent")) {

			NetworkComponentDirectionSettings netCompDirect = new NetworkComponentDirectionSettings(networkModel, thisNetworkComponent);
			thisNetworkComponent.setEdgeDirections(netCompDirect.getEdgeDirections());
			networkModel.setDirectionsOfNetworkComponent(thisNetworkComponent);

			DirectionSettingNotification dsn = new DirectionSettingNotification();
			dsn.setNotificationObject(thisNetworkComponent);
			myAgent.sendManagerNotification(dsn);

			GraphEdgeDirection ged = netCompDirect.getEdgeDirections().values().iterator().next();
			String toComGraphNodeID = ged.getGraphNodeIDTo();
			GraphNode toComGraphNode = (GraphNode) networkModel.getGraphElement(toComGraphNodeID);
			HashSet<NetworkComponent> netCompToHash = netCompDirect.getNeighbourNetworkComponent(toComGraphNode);

			String fromComGraphNodeID = ged.getGraphNodeIDFrom();
			GraphNode fromComGraphNode = (GraphNode) networkModel.getGraphElement(fromComGraphNodeID);
			HashSet<NetworkComponent> netCompFromHash = netCompDirect.getNeighbourNetworkComponent(fromComGraphNode);
			if (netCompToHash != null) {
				NetworkComponent netCompTo = netCompToHash.iterator().next();
				msgSend(netCompTo.getId(), new FindDirData("in"));
			}
			if (netCompFromHash != null) {
				NetworkComponent netCompFrom = netCompFromHash.iterator().next();
				msgSend(netCompFrom.getId(), new FindDirData("out"));
			}

			// myAgent.startRA();
			// myAgent.removeBehaviour(this);
		}
	}

	private void sendall(String msg) {
		/*
		 * Add neighbour to receivers, which should be only one for Exits and
		 * Entries and for dead pipes
		 */
		Iterator<NetworkComponent> it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
		while (it1.hasNext()) {
			String receiver = it1.next().getId();
			msgSend(receiver, new FindDirData(msg));
			// System.out.println("Empänger: "+receiver + " Semder:" +
			// myAgent.getLocalName());
		}
		done = true;
		// myAgent.startRA();
		myAgent.removeBehaviour(this);
	}

	/**
	 * Get the messages from the queue and calls the appropriate method to deal
	 * with this type of message
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	public synchronized void interpretMsg(EnvironmentNotification msg) {
		FindDirData content = (FindDirData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		// System.out.println("Agent: " + myAgent.getLocalName() +
		// " bekommen von " + msg.getSender().getLocalName() + "    " +
		// content.reason);
		if (content.getReason().equals("in")) {

			forwarding(sender, content, getIncoming(), getOutgoing(), "in", incomingMaybe, outgoingMaybe);

		} else if (content.getReason().equals("out")) {

			forwarding(sender, content, getOutgoing(), getIncoming(), "out", outgoingMaybe, incomingMaybe);

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
		Iterator<NetworkComponent> it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
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
			done = true;
			System.out.println(myAgent.getLocalName() + " All dead: " + getDead());

			myAgent.removeBehaviour(this);
			return;
		}
		i = 0;
		p = 0;
		toInform = "";
		it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
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
			addDead(toInform);
			msgSend(toInform, new FindDirData("in"));
			done = true;
			System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead() + " Opt: " + getOptional() + " InMaybe: " + incomingMaybe
					+ " OutMaybe: " + outgoingMaybe);
			setDirections();
			return;

		}
		i = 0;
		p = 0;
		toInform = "";
		it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
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
			addDead(toInform);
			msgSend(toInform, new FindDirData("out"));
			done = true;
			System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead() + " Opt: " + getOptional() + " InMaybe: " + incomingMaybe
					+ " OutMaybe: " + outgoingMaybe);
			setDirections();
			return;

		}

	}

	private void forwardingMaybe(String sender, FindDirData content, HashMap<String, String> temp1, HashMap<String, String> temp2, String msg1, String msg2, List<String> temp11, List<String> temp22) {
		/* Check if the information is to "old", too many hops */
		if (content.getWay().split("::").length < maxMsgAge) {
			if (temp22.contains(sender)) {
				/* Information is wrong */
				System.out.println(myAgent.getLocalName() + " got a wrong ? message from " + sender + ". Message get ignored");
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
					Iterator<NetworkComponent> it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
					while (it1.hasNext()) {
						String neighbour = it1.next().getId();
						for (int i = 0; i < content.getWay().split("::").length; i++) {
							if (content.getWay().split("::")[i].equals(neighbour)) {
								temp22.add(neighbour);
								msgSend(neighbour, new FindDirData(msg1));
							}
						}
					}
					temp1.clear();
					temp2.clear();
					done = true;
					System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead() + " Opt: " + getOptional() + " InMaybe: " + incomingMaybe
							+ " OutMaybe: " + outgoingMaybe);
					setDirections();
					// myAgent.startRA();
					// myAgent.removeBehaviour(this);
				} else {
					temp1.put(sender, content.getWay());

					/* The estimated information could be still wrong... */
					Iterator<NetworkComponent> it11 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
					while (it11.hasNext()) {
						String neighbour = it11.next().getId();
						if (neighbour.equals(sender) == false) {
							temp2.put(neighbour, content.getWay());
							msgSend(neighbour, new FindDirData(content.getWay() + "::" + myAgent.getLocalName(), msg1 + "?"));

						}
					}
					// }
				}
			}
		} else
			System.out.println("Message too old, " + myAgent.getLocalName() + " Way:" + content.getWay());
	}

	private void setDirections() {
		if (incoming.isEmpty() == false || outgoing.isEmpty() == false) {
			NetworkComponentDirectionSettings netCompDirect = new NetworkComponentDirectionSettings(networkModel, thisNetworkComponent);
			HashSet<GraphNode> outerNodes = netCompDirect.getOuterNodes();
			HashSet<NetworkComponent> inComps = new HashSet<NetworkComponent>();
			HashSet<NetworkComponent> outComps = new HashSet<NetworkComponent>();
			HashSet<NetworkComponent> outerComps = netCompDirect.translateGraphNodeHashSet(outerNodes);
			for (Iterator<NetworkComponent> iterator = outerComps.iterator(); iterator.hasNext();) {
				NetworkComponent outerComp = iterator.next();
				for (Iterator<String> iterator2 = getIncoming().iterator(); iterator2.hasNext();) {
					String incoming = iterator2.next();
					if (incoming.equals(outerComp.getId()))
						inComps.add(networkModel.getNetworkComponent(incoming));
				}
				for (Iterator<String> iterator2 = getOutgoing().iterator(); iterator2.hasNext();) {
					String outgoing = iterator2.next();
					if (outgoing.equals(outerComp.getId()))
						outComps.add(networkModel.getNetworkComponent(outgoing));
				}
			}
			netCompDirect.setGraphEdgeDirection(inComps, outComps);

			thisNetworkComponent.setEdgeDirections(netCompDirect.getEdgeDirections());
			DirectionSettingNotification dsn = new DirectionSettingNotification();
			dsn.setNotificationObject(thisNetworkComponent);
			myAgent.sendManagerNotification(dsn);
		}
	}

	private void forwarding(String sender, FindDirData content, List<String> temp1, List<String> temp2, String msg, HashMap<String, String> temp11, HashMap<String, String> temp22) {
		if (temp1.contains(sender) == false) {
			if (temp2.contains(sender)) {
				// Should never happen
				temp2.remove(sender);
				getOptional().add(sender);
				System.out.println("Never happen...Optional edge found " + myAgent.getLocalName() + " to " + sender);

			} else {
				temp22.clear();
				temp11.clear();
				temp1.add(sender);
				int i = 0;
				int p = 0;
				String toInform = "";
				Iterator<NetworkComponent> it1 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
				while (it1.hasNext()) {
					String neighbour = it1.next().getId();
					if (temp1.contains(neighbour) || getDead().contains(neighbour)) {
						i += 1;
					} else {
						toInform = neighbour;
					}
					p += 1;
				}
				if ((p - i) == 1) {

					if (temp2.contains(toInform) == false) {
						temp2.add(toInform);

						msgSend(toInform, new FindDirData(msg));
					}
					done = true;
					System.out.println(myAgent.getLocalName() + " In: " + getIncoming() + " Out: " + getOutgoing() + " Dead: " + getDead() + " Opt: " + getOptional() + " InMaybe: " + incomingMaybe
							+ " OutMaybe: " + outgoingMaybe);
					setDirections();
					// myAgent.startRA();
					// myAgent.removeBehaviour(this);
				} else if ((p - i) > 1) {
					// We try to guess the next step

					Iterator<NetworkComponent> it11 = networkModel.getNeighbourNetworkComponents(thisNetworkComponent).iterator();
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

	@Override
	protected void onTick() {
		// Not used jet
	}

}
