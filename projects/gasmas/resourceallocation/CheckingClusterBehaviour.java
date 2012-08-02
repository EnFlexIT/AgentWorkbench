package gasmas.resourceallocation;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.agents.components.GenericNetworkAgent;
import jade.core.AID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class CheckingClusterBehaviour {

	private static final long serialVersionUID = -2365487643740457952L;

	private GenericNetworkAgent myAgent;
	private NetworkModel networkModel;
	private NetworkComponent myNetworkComponent;

	/** NetworkComponentNames, which shows the stations, which can be asked */
	private HashSet<String> toAsk = new HashSet<String>();

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

	/** NetworkComponentNames, which are informed */
	private HashSet<String> alreadyInformedStations = new HashSet<String>();

	/** Shows if this station is a cluster, how tries to optimise itself */
	private boolean askingCluster = false;

	/**
	 * @param agent
	 * @param environmentModel
	 */
	public CheckingClusterBehaviour(GenericNetworkAgent genericNetworkAgent, NetworkModel networkModel, NetworkComponent networkComponent) {
		this.myAgent = genericNetworkAgent;
		this.networkModel = networkModel;
		this.myNetworkComponent = networkComponent;
	}

	/**
	 * Sends its initial flow to its neighbours
	 * 
	 */
	public void start() {
		// Start from the network component, where we do not have all
		// information
		System.out.println(myNetworkComponent.getId() + "   " + myNetworkComponent.getAgentClassName());
		Iterator<NetworkComponent> neighbours =networkModel.getNeighbourNetworkComponents(myNetworkComponent).iterator();
		while (neighbours.hasNext()) {
			toAsk.add(neighbours.next().getId());
		}

		if (myNetworkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName()) && toAsk.size() > 1) {
			duringACluster = true;
			askingCluster = true;
			msgSend((String) toAsk.toArray()[alreadyReportedStations], new ClusterCheckData(myNetworkComponent.getId()));
			alreadyReportedStations += 1;
			System.out.println(myNetworkComponent.getId() + " Start with " + toAsk.size() + "(" + toAsk + ")" + " and neighbours "
					+ networkModel.getNeighbourNetworkComponents(myNetworkComponent).size());
		}
		if (toAsk.size() <= 1) {
			noInformation = true;
		}
		startdone = true;
	}

	public void msgSend(String receiver, ClusterCheckData clusterCheckData) {
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), clusterCheckData) == false) {

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
		ClusterCheckData content = (ClusterCheckData) msg.getNotification();
		String sender = msg.getSender().getLocalName();
		buildCluster(content, sender);

	}

	private void buildCluster(ClusterCheckData content, String sender) {
		if (content.isAnswer()) {
			if (!alreadyInformedStations.contains(sender)) {

				if (alreadyReportedStations < toAsk.size()) {
					alreadyInformedStations.add(sender);
					content.setAnswer(false);
					content.setInitiator(myNetworkComponent.getId());
					System.out.println("A branch " + myNetworkComponent.getId() + " is asking the next station " + toAsk.toArray()[alreadyReportedStations] + ". Answer from " + sender
							+ " Initiator: " + content.getInitiator());

					msgSend((String) toAsk.toArray()[alreadyReportedStations], content);
					alreadyReportedStations += 1;

				} else {
					content.addStation(myNetworkComponent.getId());
					if (askingCluster) {
						System.out.println("Done: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());

						setCluster(content.getWay());
					} else {
						System.out.println("Done, branch: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
						msgSend(initiator, content);
					}
				}
			}
		} else {
			if (noInformation) {
				content.setAnswer(true);
				content.addStation(myNetworkComponent.getId());
				msgSend(content.getInitiator(), content);
			} else {
				if (content.getWay().contains(myNetworkComponent.getId())) {
					content.setAnswer(true);
					System.out.println("Got an request, where I found myself in the line: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
					msgSend(content.getInitiator(), content);
				} else {
					toAsk.remove(sender);

					if (networkModel.getNeighbourNetworkComponents(myNetworkComponent).size() == 2) {
						System.out.println("Sending ahead: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator() + " To: " + toAsk.toArray()[0]);
						content.addStation(myNetworkComponent.getId());
						msgSend((String) toAsk.toArray()[0], content);
						toAsk.add(sender);
					} else {
						if (!duringACluster) {
							System.out.println("Start clustering at: " + myNetworkComponent.getId() + " from " + sender + " Initiator: " + content.getInitiator());
							// if
							// (myNetworkComponent.getId().equals("n5")){
							// System.out.println("STOP clustering at: " +
							// myNetworkComponent.getId() );
							// }else{
							content.addStation(myNetworkComponent.getId());

							duringACluster = true;
							initiator = content.getInitiator();
							if (alreadyReportedStations < toAsk.size()) {
								msgSend((String) toAsk.toArray()[alreadyReportedStations], new ClusterCheckData(myNetworkComponent.getId(), content.getWay()));
								alreadyReportedStations += 1;
								// }
							}
						} else {

							msgSend(sender, new ClusterCheckData(myNetworkComponent.getId(), true));
							msgSend(initiator, new ClusterCheckData(myNetworkComponent.getId(), true));

						}
					}
				}
			}
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
		System.out.println("________________________________________________Von " + myNetworkComponent.getId() + " Cluster: " + list);
		// ClusterNotification cn = new ClusterNotification();
		// cn.setNotificationObject(networkComponents);
		// simServiceBehaviour.sendManagerNotification(cn);

	}

}
