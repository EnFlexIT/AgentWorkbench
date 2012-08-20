package gasmas.resourceallocation;

import gasmas.agents.components.BranchAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.ontology.ClusterNotification;
import jade.core.AID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class FindSimplificationBehaviour {

	private static final long serialVersionUID = -2365487643740457952L;

	/** NetworkComponentNames, which has positive flow */
	private HashSet<String> incoming = new HashSet<String>();

	/** NetworkComponentNames, which has negative flow */
	private HashSet<String> outgoing = new HashSet<String>();

	/** NetworkComponentNames, which has no flow */
	private HashSet<String> dead = new HashSet<String>();

	/** NetworkComponentNames, which has positive flow */
	private HashSet<String> toAsk = new HashSet<String>();

	/** Stores the next station, how asked because of a cluster */
	private HashMap<String, SimplificationData> nextQuestionier = new HashMap<String, SimplificationData>();

	/** The agent, how started the behaviour */
	private GenericNetworkAgent myAgent;

	/** The network model. */
	private NetworkModel myNetworkModel;

	/** My own NetworkComponent. */
	private NetworkComponent myNetworkComponent;

	/** Represents the station, which already contribute to the simplification */
	private int alreadyReportedStations = 0;

	/** Represents the initiator */
	private String initiator = "";

	/** Shows if this component has information */
	private boolean noInformation = false;

	/** Shows if this component had started */
	private boolean startdone = false;

	/** Shows if this component had started to build a cluster */
	private boolean duringACluster = false;
	private String duringAClusterUrInitiator = "";

	/** Saves the neighbours of this component */
	private Vector<NetworkComponent> myNeighbours;

	/** Saves minimal cluster size */
	private final int minClusterSize = 4;

	public HashSet<String> getIncoming() {
		return incoming;
	}

	public void setIncoming(HashSet<String> incoming) {
		this.incoming = incoming;
	}

	public HashSet<String> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(HashSet<String> outgoing) {
		this.outgoing = outgoing;
	}

	public HashSet<String> getDead() {
		return dead;
	}

	public void setDead(HashSet<String> dead) {
		this.dead = dead;
	}

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public FindSimplificationBehaviour(GenericNetworkAgent genericNetworkAgent, NetworkModel networkModel) {
		this.myAgent = genericNetworkAgent;
		this.myNetworkModel = networkModel;
		this.myNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());
		this.myNeighbours = this.myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent);
	}

	/**
	 * Start of the behaviour, which start the find simplification algorithm
	 */
	public void start() {
		// --- All flow information combined in one HashSet ---
		toAsk.addAll(incoming);
		toAsk.addAll(outgoing);
		toAsk.addAll(dead);

		// --- Start point of this algorithm are all Branch components, which do
		// not have all information about their neighbours ---
		if (myNetworkComponent.getAgentClassName().equals(BranchAgent.class.getName()) && myNeighbours.size() != toAsk.size() && toAsk.size() > 0) {
			duringACluster = true;
			duringAClusterUrInitiator = myNetworkComponent.getId();
			// --- Ask the first station about possible clusters ---
			msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(myNetworkComponent.getId(), true));
			alreadyReportedStations += 1;
			System.out.println(myNetworkComponent.getId() + " Start with " + toAsk.size() + "(" + toAsk + ")" + " and neighbours " + myNeighbours.size() + " In: " + incoming + " Out: " + outgoing
					+ " Dead: " + dead);
		} else if (toAsk.size() <= 1) {
			// --- This component do not have information, that can help for
			// this algorithm ---
			noInformation = true;
		}
		// --- The start of the component is done ---
		startdone = true;
	}

	/**
	 * Get the messages and calls the appropriate method to deal with this type
	 * of message
	 * 
	 * @param msg
	 */
	public synchronized void interpretMsg(EnvironmentNotification msg) {
		// --- Wait until the start is done ---
		while (!startdone) {
			System.out.println("Start problem (FS) at " + myAgent.getLocalName());
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		SimplificationData content = (SimplificationData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		buildCluster(content, sender);
	}

	/**
	 * Interpret the message, how to cluster
	 * 
	 * @param content
	 * @param sender
	 */
	private void buildCluster(SimplificationData content, String sender) {
		if (content.isAnswer()) {
			// --- Message is a answer to an clustering question
			if (content.getUrInitiator().equals(duringAClusterUrInitiator)) {
				// --- Answer is actual ---
				if (alreadyReportedStations < toAsk.size()) {
					// --- Component has still neighbours to ask about cluster possibilities ---
					System.out.println("A branch " + myNetworkComponent.getId() + " is asking the next station " + toAsk.toArray()[alreadyReportedStations] + ". Answer from " + sender
							+ " Initiator: " + content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
					// --- Send the question to the next neighbour ---
					content.setAnswer(false);
					content.setInitiator(myNetworkComponent.getId());
					msgSend((String) toAsk.toArray()[alreadyReportedStations], content);
					// --- Remind which neigbours are already asked ---
					alreadyReportedStations += 1;

				} else {
					// --- Component is done, all neigbours are asked
					if (content.getUrInitiator().equals(myNetworkComponent.getId())) {
						System.out.println("Done: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
						// --- Component initiated the request for clustering ->
						// is the authority how set the cluster ---
						setCluster(content.getWay());
					} else {
						System.out.println("Done, branch: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " UrInitiator: " + content.getUrInitiator()
								+ " DuringInt: " + duringAClusterUrInitiator);
						// --- Component is only a intermediate station, so send
						// the answer back to the next initiator ---
						content.addStation(myNetworkComponent.getId());
						msgSend(initiator, content);
					}
				}
			} else {
				// --- Answer is old, some higher priority question is actual ->
				// ignoring ---
				System.out.println("Ignoring msg. Stop clustering: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " UrInitiator: "
						+ content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
			}
		} else {
			if (noInformation) {
				// --- Component has no further information ---
				if (!myNetworkComponent.getAgentClassName().equals(BranchAgent.class.getName())) {
					// --- Add only station, how are fully part of the cluster ---
					content.addStation(myNetworkComponent.getId());
				}
				// --- Send the initiator the answer back ---
				content.setAnswer(true);
				msgSend(content.getInitiator(), content);
			} else {
				if (content.getWay().contains(myNetworkComponent.getId())) {
					System.out.println("Got an request, where I found myself in the line: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					// --- Cycle found -> send the initiator the answer back ---
					content.setAnswer(true);
					msgSend(content.getInitiator(), content);
				} else {
					// --- Remove the sender from the list of neighbours, how have to be asked ---
					toAsk.remove(sender);
					if (myNeighbours.size() == 2) {
						// --- Component is e.g. a pipe, so only add itself to the route and send ahead
						System.out.println("Sending ahead: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " To: " + toAsk.toArray()[0]);
						content.addStation(myNetworkComponent.getId());
						msgSend((String) toAsk.toArray()[0], content);
						// --- Reset pipe, so that the next request can proceed ---
						toAsk.add(sender);
					} else {
						if (!duringACluster) {
							// --- First clustering question -> start clustering ---
							System.out.println("Start clustering at: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
							// --- Set the information about the actual clustering round ---
							duringACluster = true;
							duringAClusterUrInitiator = content.getUrInitiator();
							initiator = content.getInitiator();
							// Component ask a neighbour about clustering possibilities
							content.addStation(myNetworkComponent.getId());
							if (alreadyReportedStations < toAsk.size()) {
								msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(myNetworkComponent.getId(), content.getWay(), false, content.getUrInitiator()));
								// --- Remind which neigbours are already asked ---
								alreadyReportedStations += 1;
							} else {
								System.out.println("Done, branch:: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " UrInitiator: "
										+ content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								// --- No possible neighbours to ask, send back to the initiator ---
								content.setAnswer(true);
								msgSend(initiator, content);
							}
						} else {
							// --- Component get already a clustering question, check how to react --- 
							toAsk.add(sender);
							if (content.getUrInitiator().compareTo(duringAClusterUrInitiator) < 0) {
								System.out.println("Got an second question for clustering with an higher prio: " + myNetworkComponent.getId() + " from " + sender + " Initiator: "
										+ content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								// --- Question has an higher priority ---
								// --- Put the request in a pool ---
								nextQuestionier.put(sender, content);
								// --- Reset component and get the request from the pool ---
								workOnHigherRequest();
							} else if (content.getUrInitiator().compareTo(duringAClusterUrInitiator) == 0) {
								System.out.println("Got an second question for clustering with an equal prio, answer directly (circle): " + myNetworkComponent.getId() + " from " + sender
										+ " Initiator: " + content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								// --- Question comes from this network component -> cycle ---
								// --- Send the request back to the local initiator ---
								msgSend(sender, content);
							} else {
								System.out.println("Got an second question for clustering with a lower prio: " + myNetworkComponent.getId() + " from " + sender + " Initiator: "
										+ content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								// --- Question with a lower priority -> ignoring ---
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Stops the clustering and starts the clustering again for another component with an higher priority
	 */
	private void workOnHigherRequest() {
		// --- Reset the internal variables ---
		toAsk.clear();
		toAsk.addAll(incoming);
		toAsk.addAll(outgoing);
		toAsk.addAll(dead);
		alreadyReportedStations = 0;
		duringACluster = false;
		duringAClusterUrInitiator = "";
		// --- Start the next request with an higher priority
		if (!nextQuestionier.isEmpty()) {
			Entry<String, SimplificationData> nextOne = nextQuestionier.entrySet().iterator().next();
			nextQuestionier.remove(nextOne.getKey());
			buildCluster(nextOne.getValue(), nextOne.getKey());
		}
	}

	/**
	 * Send the cluster to the manager agent
	 * @param list
	 */
	private void setCluster(HashSet<String> list) {
		// --- Add only station, how are fully part of the cluster ---
		if ((incoming.size() + outgoing.size() + dead.size()) >= 2) {
			list.add(myNetworkComponent.getId());
		}
		// --- Cluster must be larger than the minimal cluster size ---
		if (list.size() > minClusterSize) {
			System.out.println("Von " + myNetworkComponent.getId() + " Cluster: " + list);
			// --- Send the notification about the cluster to the manager agent ---
			ClusterNotification cn = new ClusterNotification();
			cn.setReason("newCluster");
			cn.setNotificationObject(list);
			myAgent.sendManagerNotification(cn);
		}
	}

	/**
	 * Send a message about the simulation service
	 * 
	 * @param receiver
	 * @param content
	 */
	public void msgSend(String receiver, SimplificationData simplificationData) {
		// --- Try to send the message until the method gives back true ---
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), simplificationData) == false) {
			System.out.println("PROBLEM (FS) to send a message to " + receiver + " from " + myAgent.getLocalName());
		}

	}
}
