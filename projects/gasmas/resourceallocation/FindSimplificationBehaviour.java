package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;
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

	/** NetworkComponentNames, which has positive flow */
	private HashSet<String> temp = new HashSet<String>();

	/** Stores the next station, how asked because of a cluster */
	private HashMap<String, HashSet<String>> nextQuestionier = new HashMap<String, HashSet<String>>();

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** My own NetworkComponent. */
	private NetworkComponent thisNetworkComponent;

	/** Overrides class Agent with the special GenericNetworkAgent */
	private GenericNetworkAgent myAgent;

	/** Shows if this component is a endpoint for simplification */
	private boolean end = false;

	/** Represents the station, which already contribute to the simplification */
	private int alreadyReportedStations = 0;

	/** Represents the initiator */
	private String initiator = "";

	/** Shows if this component has information */
	private boolean noInformation = false;

	/** Shows if this component had started */
	private boolean startdone = false;

	/** Shows if this component had started */
	private boolean duringACluster = false;

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public FindSimplificationBehaviour(GenericNetworkAgent agent, long period, EnvironmentModel environmentModel) {
		super(agent, period);
		this.environmentModel = environmentModel;
		networkModel = ((NetworkModel) this.environmentModel.getDisplayEnvironment()).getCopy();
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
		if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.BranchAgent") && networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() != toAsk.size()
				&& toAsk.size() > 0) {
			end = true;
			msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(thisNetworkComponent.getId()));
			alreadyReportedStations += 1;
			System.out.println("Start " + thisNetworkComponent.getId());
		}
		if (incoming.isEmpty() || outgoing.isEmpty()) {
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
		if (content.isAnswer()) {

			if (alreadyReportedStations < toAsk.size()) {
				content.setAnswer(false);
				msgSend((String) toAsk.toArray()[alreadyReportedStations], content);
				alreadyReportedStations += 1;
			} else {
				content.addStation(thisNetworkComponent.getId());
				if (end) {
					setCluster(content.getWay());
				} else {
//					content.addAnotherWay(temp);
					msgSend(initiator, content);
					next();
				}
			}
		} else {
			if (noInformation) {
				content.setAnswer(true);
				content.addStation(thisNetworkComponent.getId());
				msgSend(content.getInitiator(), content);
			} else {
				if (content.getWay().contains(thisNetworkComponent.getId())) {
					content.setAnswer(true);
					msgSend(content.getInitiator(), content);
					System.out.println("____________"+thisNetworkComponent.getId() + "   "+sender);
				} else {
					toAsk.remove(sender);
					content.addStation(thisNetworkComponent.getId());
					if (networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() == 2) {
						
						msgSend((String) toAsk.toArray()[0], content);
						
						toAsk.add(sender);
					} else {
						if (end && thisNetworkComponent.getId().compareTo(content.getInitiator()) < 0 || !end) {
							if (!duringACluster) {
								duringACluster = true;
								initiator = content.getInitiator();
								if (alreadyReportedStations < toAsk.size()) {
									temp.addAll(content.getWay());
									msgSend((String) toAsk.toArray()[alreadyReportedStations], new SimplificationData(thisNetworkComponent.getId(),content.getWay(),false));
									alreadyReportedStations += 1;
								}
							} else {
								System.out.println(thisNetworkComponent.getId());
								HashSet<String> newWay = new HashSet<String>();
								newWay.addAll(content.getWay());
								nextQuestionier.put(content.getInitiator(), newWay);
							}
						} else {
							toAsk.add(sender);
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
		duringACluster = false;
		if (!nextQuestionier.isEmpty()) {
			Entry<String, HashSet<String>> nextOne = nextQuestionier.entrySet().iterator().next();
			nextQuestionier.remove(nextOne.getKey());
			buildCluster(new SimplificationData(nextOne.getKey(), nextOne.getValue(), false), "");
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
		myAgent.sendManagerNotification(networkModel.replaceComponentsByCluster(networkComponents, true));

	}

	@Override
	protected void onTick() {
		// Not used jet
	}

}
