package gasmas.resourceallocation;

import gasmas.agents.components.BranchAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.ontology.ClusterNotification;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class FindSimplificationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2365487643740457952L;

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

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** My own NetworkComponent. */
	private NetworkComponent thisNetworkComponent;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

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

	/** Shows if which cluster process is the newest */
	private int sessionID = 0;
	private int sessionIDrec = 0;

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public FindSimplificationBehaviour(GenericNetworkAgent agent, long period, EnvironmentModel environmentModel) {
		super(agent, period);
		this.environmentModel = environmentModel;
		networkModel = (NetworkModel) this.environmentModel.getDisplayEnvironment();
		myAgent = agent;
		thisNetworkComponent = networkModel.getNetworkComponent(myAgent.getLocalName());
	}

	/**
	 * Sends its initial flow to its neighbours
	 * 
	 */
	public void start() {
		// Start from the network component, where we do not have all
		// information
		toAsk.addAll(incoming);
		toAsk.addAll(outgoing);
		toAsk.addAll(dead);

		if (thisNetworkComponent.getAgentClassName().equals(BranchAgent.class.getName()) && networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() != toAsk.size()
				&& toAsk.size() > 0) {
			duringACluster = true;
			duringAClusterUrInitiator = thisNetworkComponent.getId();
			msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(thisNetworkComponent.getId(), true));
			alreadyReportedStations += 1;
			System.out.println(thisNetworkComponent.getId() + " Start with " + toAsk.size() + "(" + toAsk + ")" + " and neighbours "
					+ networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() + " In: " + incoming + " Out: " + outgoing + " Dead: " + dead);
		}
		if (toAsk.size() <= 1) {
			noInformation = true;
		}
		startdone = true;
	}

	public void msgSend(String receiver, SimplificationData simplificationData) {
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), simplificationData) == false) {

		}

	}

	public synchronized void interpretMsg(EnvironmentNotification msg) {
		while (!startdone) {
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

	private void buildCluster(SimplificationData content, String sender) {
		if (thisNetworkComponent.getId().equals("n68")) {
			System.out.println("eo");
		}
		if (content.isAnswer()) {
			if (content.getSessionID() == sessionID) {
				if (alreadyReportedStations < toAsk.size()) {

					content.setAnswer(false);
					content.setInitiator(thisNetworkComponent.getId());
					System.out.println("A branch " + thisNetworkComponent.getId() + " is asking the next station " + toAsk.toArray()[alreadyReportedStations] + ". Answer from " + sender
							+ " Initiator: " + content.getInitiator());

					msgSend((String) toAsk.toArray()[alreadyReportedStations], content);
					alreadyReportedStations += 1;

				} else {
					content.addStation(thisNetworkComponent.getId());
					if (content.getUrInitiator().equals(thisNetworkComponent.getId())) {
						System.out.println("Done: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());

						setCluster(content.getWay());
					} else {
						System.out.println("Done, branch: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
						content.setSessionID(sessionIDrec);
						msgSend(initiator, content);
						next();
					}
				}
			} else {
				System.out.println("Ignoring msg. Stop clustering: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
			}
		} else {
			if (noInformation) {
				content.setAnswer(true);
				content.addStation(thisNetworkComponent.getId());
				msgSend(content.getInitiator(), content);
			} else {
				if (content.getWay().contains(thisNetworkComponent.getId())) {
					content.setAnswer(true);
					System.out.println("Got an request, where I found myself in the line: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					msgSend(content.getInitiator(), content);
				} else {
					toAsk.remove(sender);

					if (networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() == 2) {
						System.out.println("Sending ahead: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " To: " + toAsk.toArray()[0]);
						content.addStation(thisNetworkComponent.getId());
						msgSend((String) toAsk.toArray()[0], content);
						toAsk.add(sender);
					} else {
						if (!duringACluster) {
							System.out.println("Start clustering at: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
							// if
							// (thisNetworkComponent.getId().equals("n5")){
							// System.out.println("STOP clustering at: " +
							// thisNetworkComponent.getId() );
							// }else{
							content.addStation(thisNetworkComponent.getId());
							sessionIDrec = content.getSessionID();
							duringACluster = true;
							duringAClusterUrInitiator = content.getUrInitiator();
							initiator = content.getInitiator();
							if (alreadyReportedStations < toAsk.size()) {
								msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(thisNetworkComponent.getId(), content.getWay(), false, content.getUrInitiator(),
										sessionID));
								alreadyReportedStations += 1;
								// }
							}
						} else {
							toAsk.add(sender);
							if (content.getUrInitiator().compareTo(duringAClusterUrInitiator) < 0) {
								System.out.println("Got an second question for clustering with an higher prio: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: "
										+ content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								nextQuestionier.put(sender, content);
								next();
							} else if (content.getUrInitiator().compareTo(duringAClusterUrInitiator) == 0) {
								System.out.println("Got an second question for clustering with an equal prio, answer directly (circle): " + thisNetworkComponent.getId() + " from " + sender
										+ " Initiator: " + content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
								msgSend(sender, content);
							} else {
								System.out.println("Got an second question for clustering with a lower prio: " + thisNetworkComponent.getId() + " from " + sender + " Initiator: "
										+ content.getInitiator() + " UrInitiator: " + content.getUrInitiator() + " DuringInt: " + duringAClusterUrInitiator);
							}
						}
					}
				}
			}
		}
	}

	private void next() {
		toAsk.clear();
		toAsk.addAll(incoming);
		toAsk.addAll(outgoing);
		toAsk.addAll(dead);
		alreadyReportedStations = 0;
		sessionID += 1;
		duringACluster = false;
		duringAClusterUrInitiator = "";
		if (!nextQuestionier.isEmpty()) {
			Entry<String, SimplificationData> nextOne = nextQuestionier.entrySet().iterator().next();
			nextQuestionier.remove(nextOne.getKey());
			buildCluster(nextOne.getValue(), nextOne.getKey());
		}
	}

	private void setCluster(HashSet<String> list) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(networkModel.getNetworkComponents().values())) {
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				if (it.next().equals(networkComponent.getId())) {
					networkComponents.add(networkComponent);
				}

			}
		}
		System.out.println("Von " + thisNetworkComponent.getId() + " Cluster: " + list);
		ClusterNotification cn = new ClusterNotification();
		cn.setNotificationObject(networkComponents);
		myAgent.sendManagerNotification(cn);

	}

	@Override
	protected void onTick() {
		// Not used jet
	}

}
