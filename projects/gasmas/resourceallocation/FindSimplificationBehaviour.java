package gasmas.resourceallocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import gasmas.agents.components.GenericNetworkAgent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

public class FindSimplificationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2365487643740457952L;

	public List<String> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<String> incoming) {
		this.incoming = incoming;
	}

	public List<String> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<String> outgoing) {
		this.outgoing = outgoing;
	}

	public List<String> getDead() {
		return dead;
	}

	public void setDead(List<String> dead) {
		this.dead = dead;
	}

	/** NetworkComponentNames, which has positive flow */
	private List<String> incoming = new ArrayList<String>();

	/** NetworkComponentNames, which has negative flow */
	private List<String> outgoing = new ArrayList<String>();

	/** NetworkComponentNames, which has no flow */
	private List<String> dead = new ArrayList<String>();

	/** NetworkComponentNames, which has positive flow */
	private List<String> toAsk = new ArrayList<String>();

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
	 * @see jade.core.behaviours.Behaviour#onStart()
	 */
	public void onStart() {
		// Start from the network component, where we do not have all
		// information
		toAsk.addAll(incoming);
		toAsk.addAll(outgoing);
		toAsk.addAll(dead);
		if (thisNetworkComponent.getAgentClassName().equals("gasmas.agents.components.BranchAgent") && networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() != toAsk.size()
				&& networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() > 0) {
			end = true;
			msgSend(toAsk.get(alreadyReportedStations), new SimplificationData(thisNetworkComponent.getId()));
			alreadyReportedStations += 1;
			System.out.println("Start " + thisNetworkComponent.getId());
		}
		if (incoming.isEmpty() || outgoing.isEmpty()) {
			noInformation = true;
		}
	}

	public void msgSend(String receiver, SimplificationData simplificationData) {
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), simplificationData) == false) {

		}

	}

	public synchronized void interpretMsg(EnvironmentNotification msg) {
		SimplificationData content = (SimplificationData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		if (content.isAnswer()) {

			if (alreadyReportedStations < toAsk.size()) {
				content.setAnswer(false);
				msgSend(toAsk.get(alreadyReportedStations), content);
				alreadyReportedStations += 1;
			} else {
				if (end) {
					System.out.println("Auslöser: " + sender + " bei " + thisNetworkComponent.getId());
					content.addStation(thisNetworkComponent.getId());
					setCluster(content.getWay());
				} else {
					msgSend(initiator, content);
				}
			}
		} else {
			content.addStation(thisNetworkComponent.getId());
			if (noInformation) {
				content.setAnswer(true);
				msgSend(content.getInitiator(), content);
			} else {
				toAsk.remove(sender);
				if (networkModel.getNeighbourNetworkComponents(thisNetworkComponent).size() == 2) {
					msgSend(toAsk.get(0), content);
				} else {
					if (end) {
						System.out.println("we have to think about something");
					} else {
						initiator = content.getInitiator();
						if (alreadyReportedStations < toAsk.size()) {
							content.setAnswer(false);
							msgSend(toAsk.get(alreadyReportedStations), new SimplificationData(thisNetworkComponent.getId()));
							alreadyReportedStations += 1;
						}
					}
				}
			}
		}

	}

	private void setCluster(List<String> list) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(networkModel.getNetworkComponents().values())) {
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				if (it.next().equals(networkComponent.getId())) {
					networkComponents.add(networkComponent);
				}

			}
		}
		// Wofür gibt es überhaupt die Notification, wenn sie
		// nicht genutzt wird?
		// ClusterNotification cn = new ClusterNotification();
		// cn.setNotificationObject(networkModel.replaceComponentsByCluster(networkComponents));
		System.out.println("Von " + thisNetworkComponent.getId() + " Cluster: " + list);
		myAgent.sendManagerNotification(networkModel.replaceComponentsByCluster(networkComponents));

	}

	@Override
	protected void onTick() {
		// Not used jet
	}

}
