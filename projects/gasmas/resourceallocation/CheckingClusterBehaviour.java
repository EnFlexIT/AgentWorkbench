package gasmas.resourceallocation;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.agents.manager.NetworkManagerAgent;
import gasmas.clustering.behaviours.ClusteringBehaviour;
import gasmas.ontology.ClusterNotification;
import jade.core.AID;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class CheckingClusterBehaviour {

	private static final long serialVersionUID = -2365487643740457952L;

	/** The agent, how started the behaviour */
	private GenericNetworkAgent myAgent;

	/** The cluster network model. */
	private NetworkModel clusterNetworkModel;

	/** The network model. */
	private NetworkModel myNetworkModel;

	/** My own NetworkComponent. */
	private NetworkComponent myNetworkComponent;

	/** NetworkComponentNames, which shows the stations, which can be asked */
	private HashSet<String> toAsk = new HashSet<String>();

	/** NetworkComponentNames, which have to add to the cluster */
	private HashSet<String> found = new HashSet<String>();

	/** Represents the station, which already contribute to the simplification */
	private int alreadyReportedStations = 0;

	/** Represents the initiator */
	private String initiator = "";

	/** Shows if this component has information */
	private boolean noInformation = false;

	/** Shows if this component had started */
	private boolean startdone = false;

	/** Shows if is during a cluster checking process */
	private boolean duringACluster = false;

	/** Shows if this station is a cluster, how tries to optimise itself */
	private boolean askingCluster = false;

	/** Saves the neighbours of this component */
	private Vector<NetworkComponent> myNeighbours;

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public CheckingClusterBehaviour(GenericNetworkAgent genericNetworkAgent, NetworkModel networkModel) {
		this.myAgent = genericNetworkAgent;
		this.myNetworkModel = networkModel;
		this.clusterNetworkModel = this.myNetworkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		this.myNetworkComponent = clusterNetworkModel.getNetworkComponent(myAgent.getLocalName());
		this.myNeighbours = this.clusterNetworkModel.getNeighbourNetworkComponents(myNetworkComponent);

	}

	/**
	 * Start of the behaviour, which start the checking cluster algorithm
	 */
	public void start() {
		// Start from the clusters
		Iterator<NetworkComponent> neighbours = myNeighbours.iterator();
		while (neighbours.hasNext()) {
			toAsk.add(neighbours.next().getId());
		}

		if (myNetworkComponent.getAgentClassName().equals(ExitAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(EntryAgent.class.getName())) {
			// System.out.println("No Information: " +
			// myNetworkComponent.getId() + " with " + toAsk);
			noInformation = true;
		} else if (myNetworkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName()) && toAsk.size() > 1) {
			int i = 0;
			for (NetworkComponent neighbour : myNeighbours) {
				if (neighbour.getAgentClassName().equals(ExitAgent.class.getName()) || neighbour.getAgentClassName().equals(EntryAgent.class.getName())) {
					i++;
				}
			}
			if (myNeighbours.size() - i <= 1) {
				noInformation = true;
			} else {
				duringACluster = true;
				askingCluster = true;
				found.add(myNetworkComponent.getId());
				msgSend((String) toAsk.toArray()[alreadyReportedStations], new ClusterCheckData(myNetworkComponent.getId()));
				alreadyReportedStations += 1;
				System.out.println(myNetworkComponent.getId() + " Start with " + toAsk.size() + "(" + toAsk + ")" + " and neighbours " + myNeighbours.size());
			}
		}
		startdone = true;
	}

	/**
	 * Send a message about the simulation service
	 * 
	 * @param receiver
	 * @param content
	 */
	public void msgSend(String receiver, ClusterCheckData clusterCheckData) {
		// --- Try to send the message until the method gives back true ---
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), clusterCheckData) == false) {
			System.out.println("PROBLEM (CC) to send a message to " + receiver + " from " + myAgent.getLocalName());
		}

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
			System.out.println("Start problem (CC) at " + myAgent.getLocalName());
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ClusterCheckData content = (ClusterCheckData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		buildCluster(content, sender);

	}

	/**
	 * Interpret the message, how the cluster has to extend
	 * 
	 * @param content
	 * @param sender
	 */
	private void buildCluster(ClusterCheckData content, String sender) {
		if (content.isAnswer()) {
			// --- Message is a answer to an clustering question
			if (content.getWay().isEmpty() && !myNetworkComponent.getId().startsWith(NetworkManagerAgent.clusterComponentPrefix)) {
				System.out.println("Done Kill, branch: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " RealInitiator: " + initiator + " UrInitiator: "
						+ content.getUrInitiator());
				content.setInitiator(initiator);
				msgSend(initiator, content);
			} else {
				// --- Component is a cluster ---
				nextRound(content, sender);
			}
		} else {
			if (noInformation) {
				// --- Component has no other neighbour, is a one way station ---
				if (content.getInitiator().startsWith(NetworkManagerAgent.clusterComponentPrefix) && !myAgent.getLocalName().startsWith(NetworkManagerAgent.clusterComponentPrefix)) {
					System.out.println("Kill: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					// --- Is a distribution point, which is already "in" the cluster, no need to change something ---
					content.setAnswer(true);
					// --- Shows the receiver, that there is no need to change ---
					content.getWay().clear();
					msgSend(content.getInitiator(), content);
				} else {
					System.out.println("No Info: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					// --- Is a cluster, how can be migrated to the other cluster ---
					content.setAnswer(true);
					content.addStation(myNetworkComponent.getId());
					msgSend(content.getInitiator(), content);
				}
			} else {
				if (content.getWay().contains(myNetworkComponent.getId())) {
					System.out.println("Got an request, where I found myself in the line: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					// --- Cycle found -> send the initiator the answer back ---
					content.setAnswer(true);
					msgSend(content.getInitiator(), content);
				} else {
					if (myNetworkComponent.getId().startsWith(NetworkManagerAgent.clusterComponentPrefix)) {
						// --- This component is a cluster ---
						if (content.getUrInitiator().equals(myNetworkComponent.getId())) {
							// --- Component is the initiator of the request
							if (content.getInitiator().equals(myNetworkComponent.getId())) {
								System.out.println("REAL Done: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
								// --- Component is the last initiator, which means that we found some components, that need to add to the cluster ---
								found.addAll(content.getWay());
								// --- Start the next round ---
								nextRound(content, sender);
							} else {
								System.out.println("Cluster stop: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
								// --- Found a cycle, so send the initiator the answer back ---
								content.setAnswer(true);
								content.addStation(myNetworkComponent.getId());
								msgSend(content.getInitiator(), content);
							}
						} else {
							System.out.println("Kill Cluster: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " UrInitiator: "
									+ content.getUrInitiator());
							// --- Cluster get an request of another cluster, so stop clustering this branch, no possible improvements ---
							content.setAnswer(true);
							content.getWay().clear();
							msgSend(content.getInitiator(), content);
						}
					} else {
						if (myNeighbours.size() == 2) {
							System.out.println("Sending ahead/back: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " To: " + toAsk.toArray()[0]);
							// --- Component is e.g. a pipe, so only add itself to the route and send ahead
							toAsk.remove(sender);
							if (toAsk.isEmpty()) {
								// Pipe only have one neighbour, e.g. dead pipe
								toAsk.add(sender);
							}
							content.addStation(myNetworkComponent.getId());
							msgSend((String) toAsk.toArray()[0], content);
							// --- Reset pipe, so that the next request can proceed ---
							toAsk.add(sender);
						} else {
							if (!duringACluster) {
								System.out.println("Start clustering at: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
								// --- Remove the sender from the list of neighbours, how have to be asked ---
								toAsk.remove(sender);
								// --- Set the information about the actual clustering round ---
								duringACluster = true;
								initiator = content.getInitiator();
								// Component ask a neighbour about clustering possibilities
								content.addStation(myNetworkComponent.getId());
								if (alreadyReportedStations < toAsk.size()) {
									msgSend((String) toAsk.toArray()[alreadyReportedStations], new ClusterCheckData(myNetworkComponent.getId(), content.getWay(), content.getUrInitiator()));
									// --- Remind which neigbours are already asked ---
									alreadyReportedStations += 1;
								} else {
									System.out.println("Done, branch: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " UrInitiator: "
											+ content.getUrInitiator());
									// --- No possible neighbours to ask, send back to the initiator ---
									content.setAnswer(true);
									msgSend(initiator, content);
								}
							} else {
								System.out.println("During clustering at, kill: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
								// --- Get another request of clustering, which means is in between of two clusters -> no possible improvements ---
								msgSend(content.getInitiator(), new ClusterCheckData(myNetworkComponent.getId(), true, content.getUrInitiator()));

							}
						}
					}
				}
			}
		}
	}

	/**
	 * Starts the next asking round or finalised the cluster check in this component
	 * @param content
	 * @param sender
	 */
	private void nextRound(ClusterCheckData content, String sender) {
		if (alreadyReportedStations < toAsk.size()) {
			// --- Component has still neighbours to ask about cluster possibilities ---
			System.out.println("A branch " + myNetworkComponent.getId() + " is asking the next station " + toAsk.toArray()[alreadyReportedStations] + ". Answer from " + sender + " Initiator: "
					+ content.getInitiator());
			if (askingCluster) {
				// --- Component is a cluster, how tries to improve, so the found station get stored in a temporal HashSet ---
				found.addAll(content.getWay());
			}
			// --- Send a message to the next station to ask for improvements ---
			content.setAnswer(false);
			content.setInitiator(myNetworkComponent.getId());
			msgSend((String) toAsk.toArray()[alreadyReportedStations], content);
			// --- Remind which neigbours are already asked ---
			alreadyReportedStations += 1;

		} else {
			content.addStation(myNetworkComponent.getId());
			if (askingCluster) {
				System.out.println("Done: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
				found.addAll(content.getWay());
				rearrangeCluster();
			} else {
				System.out.println("Done, branch: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " \n " + content.getWay());
				msgSend(initiator, content);
			}
		}
	}

	/**
	 * A cluster component finished asking the neighbours about improvements
	 * -> Send the information to the manager agent
	 */
	private void rearrangeCluster() {
		// --- Check, if the component find new components ---
		if (found.size() > 1) {
			System.out.println("________________________________________________Von " + myNetworkComponent.getId() + " Cluster: " + found);
			// --- Send the information to the manager agent ---
			ClusterNotification cn = new ClusterNotification();
			cn.setReason("rearrangeCluster");
			cn.setNotificationObject(found);
			myAgent.sendManagerNotification(cn);
		}
	}

}
