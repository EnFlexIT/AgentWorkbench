package gasmas.resourceallocation;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.agents.components.CompressorAgent;
import gasmas.agents.components.ControlValveAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.agents.manager.NetworkManagerAgent;
import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

public class InitialProcessBehaviour extends Behaviour {

	private static final long serialVersionUID = -5824582916279166931L;

	protected GenericNetworkAgent myAgent;
	/** Different Behaviours */
	protected FindDirectionBehaviour findDirectionBehaviour;
	protected FindSimplificationBehaviour findSimplificationBehaviour;
	protected CheckingClusterBehaviour checkingClusterBehaviour;
	protected CoalitionBehaviour coalitionBehaviour;
	protected PassiveNAResponderBehaviour passiveClusteringBehaviour;

	/** Shows the internal step of the agent */
	private int step = -1;

	public int getStep() {
		return step;
	}

	public InitialProcessBehaviour(GenericNetworkAgent myAgent) {
		super();
		this.myAgent = myAgent;
	}

	/**
	 * Starts the find direction behaviour
	 */
	private void startFindDirectionBehaviour() {
		findDirectionBehaviour = new FindDirectionBehaviour(myAgent, myAgent.myNetworkModel, this);
		findDirectionBehaviour.start();
	}

	/**
	 * Starts the checking cluster behaviour
	 */
	private void startCheckingClusterBehaviour() {

		checkingClusterBehaviour = new CheckingClusterBehaviour(myAgent, myAgent.myNetworkModel, this);
		checkingClusterBehaviour.start();
	}

	/**
	 * Starts the clustering behaviour
	 */
	protected void startClusteringBehaviour() {
		// Get the cluster network model, where the clustering algorithm should work
		if (myAgent.getPartOfCluster().isEmpty()) {
			myAgent.setPartOfCluster("ClusterdNM");
		}
		NetworkModel clusterNetworkModel = myAgent.myNetworkModel.getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[0]);
		for (int i = 1; i < myAgent.getPartOfCluster().split("::").length; i++) {
			clusterNetworkModel = clusterNetworkModel.getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[i]);
		}

		if (clusterNetworkModel == null) {
			System.err.println("No appropriate network model found at for clustering " + myAgent.getLocalName() + ". Should be part of cluster: " + myAgent.getPartOfCluster());
		} else {
			// The algorithm distinguishes between active and passive components, so different behaviours have to be
			// started
			if (myAgent.myNetworkComponent.getAgentClassName().equals(CompressorAgent.class.getName()) || myAgent.myNetworkComponent.getAgentClassName().equals(ControlValveAgent.class.getName())
					|| myAgent.myNetworkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName())) {
				// || myNetworkComponent.getAgentClassName().equals(SimpleValveAgent.class.getName())
				// Active component
				coalitionBehaviour = new CoalitionBehaviour(myAgent, myAgent.getMyEnvironmentModel(), clusterNetworkModel, new CycleClusteringBehaviour(myAgent, clusterNetworkModel));
				myAgent.addBehaviour(coalitionBehaviour);
			} else {
				// Passive component
				passiveClusteringBehaviour = new PassiveNAResponderBehaviour(myAgent);
				myAgent.addBehaviour(passiveClusteringBehaviour);

			}
		}
	}

	/**
	 * Starts the find simplification behaviour
	 */
	private void startFindSimplificationBehaviour() {

		findSimplificationBehaviour = new FindSimplificationBehaviour(myAgent, myAgent.myNetworkModel, this);
		// Because the internal structures can not save dead pipes, so I have to
		// transfer the knowledge from one behaviour to another
		findSimplificationBehaviour.setDead(findDirectionBehaviour.getDead());
		findSimplificationBehaviour.setIncoming(findDirectionBehaviour.getIncoming());
		findSimplificationBehaviour.setOutgoing(findDirectionBehaviour.getOutgoing());
		// Start find simplification behaviour
		findSimplificationBehaviour.start();
	}

	/**
	 * Method to interpret status information
	 * 
	 * @param phase
	 * @param clusterName
	 */
	private void reactOnStatusInformation(int phase, String clusterName) {
		if (myAgent.myNetworkModel == null) {
			// Got status information, but no network model is there, so ignoring of the message
			System.err.println("Error: " + myAgent.getLocalName() + " did not get the network model, so no status information are interpreted.");
		} else {
			if (phase == 0) {
				// Starts the initial find direction process
				step = 0;
				startFindDirectionBehaviour();
			} else if (phase == 1) {
				// Starts the second find direction process to find cycles
				step = 1;
				findDirectionBehaviour.setDirections();
				findDirectionBehaviour.startStep2();
			} else if (phase == 2) {
				// Starts a first clustering process, using information of the directions
				step = 2;
				findDirectionBehaviour.setDirections();
				startFindSimplificationBehaviour();
			} else if (phase == 3) {
				// Starts a verification of the cluster found in step 2
				step = 3;
				startCheckingClusterBehaviour();
			} else if (phase == 4) {
				// Starts a second clustering process, using a special algorithm
				step = 4;
				startClusteringBehaviour();
			} else if (phase == 5) {
				// Starts a new clustering round, using a special algorithm
				step = 4;
				// Delete the behaviour of the last clustering round
				if (coalitionBehaviour != null)
					myAgent.removeBehaviour(coalitionBehaviour);
				if (passiveClusteringBehaviour != null)
					myAgent.removeBehaviour(passiveClusteringBehaviour);
				startClusteringBehaviour();
			} else if (!clusterName.isEmpty()) {
				// Saves the name of the cluster in which this agent is
				myAgent.setPartOfCluster(clusterName);
				// If this component is a Cluster, it have to inform the network components about the cluster in cluster
				if (myAgent.myNetworkComponent.getId().startsWith(NetworkManagerAgent.clusterComponentPrefix)) {
					for (String networkComponentID : ((ClusterNetworkComponent) myAgent.myNetworkComponent).getNetworkComponentIDs()) {
						int tries = 0;
						while (myAgent.sendAgentNotification(new AID(networkComponentID, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(new StatusData(myAgent.getPartOfCluster() + "::"
								+ myAgent.myNetworkComponent.getId()))) == false) {
							tries++;
							if (tries > 10) {
								System.out.println("PROBLEM (GNA) to send a message to " + networkComponentID + " from " + myAgent.getLocalName());
								break;
							}
						}
					}
				}
				if (!myAgent.getPartOfCluster().startsWith("ClusterdNM")) {
					myAgent.setPartOfCluster("ClusterdNM::" + myAgent.getPartOfCluster());
				}
			}
		}
	}

	/**
	 * Get the messages and calls the appropriate method to deal with this type of message
	 * 
	 * @param msg
	 */
	public synchronized EnvironmentNotification interpretMsg(EnvironmentNotification notification) {
		Object content = ((InitialBehaviourMessageContainer) notification.getNotification()).getData();
		if (content instanceof FindDirData) {
			if (findDirectionBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'FindDirData' !Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				findDirectionBehaviour.interpretMsg(notification);
			}
		} else if (content instanceof SimplificationData) {
			if (findSimplificationBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'SimplificationData' ! Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				findSimplificationBehaviour.interpretMsg(notification);
			}
		} else if (content instanceof ClusterCheckData) {
			if (checkingClusterBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'ClusterCheckData' ! Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				checkingClusterBehaviour.interpretMsg(notification);
			}
		} else if (content instanceof StatusData) {
			// Got status information
			reactOnStatusInformation(((StatusData) content).getPhase(), ((StatusData) content).getClusterName());
		}
		return notification;
	}

	/**
	 * Send a message about the simulation service
	 * 
	 * @param receiver
	 * @param content
	 */
	public void msgSend(String receiver, GenericMesssageData content) {
		// --- Send the information about a new message to the manager agent ---
		myAgent.sendManagerNotification(new StatusData(this.getStep(), "msg+"));
		// --- Try to send the message until the method gives back true (or after 10 retries) ---
		int tries = 0;
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(content)) == false) {
			tries++;
			if (tries > 10) {
				System.out.println("PROBLEM (IPB) to send a message to " + receiver + " from " + myAgent.getLocalName());
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		reactOnStatusInformation(0, "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return false;
	}

}
