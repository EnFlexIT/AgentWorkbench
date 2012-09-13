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

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.agents.components.CompressorAgent;
import gasmas.agents.components.ControlValveAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class InitialProcessBehaviour is used for organizatoric issues of the initial process.
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class InitialProcessBehaviour extends OneShotBehaviour {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5824582916279166931L;

	/** The my agent. */
	protected GenericNetworkAgent myAgent;

	/* Different Behaviours. */
	/** The find direction behaviour. */
	protected FindDirectionBehaviour findDirectionBehaviour;

	/** The find simplification behaviour. */
	protected FindSimplificationBehaviour findSimplificationBehaviour;

	/** The checking cluster behaviour. */
	protected CheckingClusterBehaviour checkingClusterBehaviour;

	/** The coalition behaviour. */
	protected CoalitionBehaviour coalitionBehaviour;

	/** The passive clustering behaviour. */
	protected PassiveNAResponderBehaviour passiveClusteringBehaviour;

	/** Shows the internal step of the agent. */
	private int step = -1;
	
	/**
	 * Gets the step.
	 * 
	 * @return the step
	 */
	public int getStep() {
		return step;
	}

	/**
	 * Instantiates a new initial process behaviour.
	 * 
	 * @param myAgent
	 *            the my agent
	 */
	public InitialProcessBehaviour(GenericNetworkAgent myAgent) {
		super();
		this.myAgent = myAgent;
	}

	/**
	 * Starts the find direction behaviour.
	 */
	private void startFindDirectionBehaviour() {
		findDirectionBehaviour = new FindDirectionBehaviour(myAgent, myAgent.getMyNetworkModel(), this);
		findDirectionBehaviour.start();
	}

	/**
	 * Starts the checking cluster behaviour.
	 */
	private void startCheckingClusterBehaviour(boolean iterativeStep) {
		if (myAgent.getPartOfCluster().isEmpty()) {
			myAgent.setPartOfCluster("ClusterdNM");
		}
		NetworkModel clusterNetworkModel = myAgent.getMyNetworkModel().getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[0]);
		for (int i = 1; i < myAgent.getPartOfCluster().split("::").length; i++) {
			clusterNetworkModel = clusterNetworkModel.getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[i]);
		}
		
		checkingClusterBehaviour = new CheckingClusterBehaviour(myAgent, clusterNetworkModel, this, iterativeStep);
		checkingClusterBehaviour.start();
	}

	/**
	 * Starts the clustering behaviour.
	 */
	protected void startClusteringBehaviour() {
		// Get the cluster network model, where the clustering algorithm should work
		if (myAgent.getPartOfCluster().isEmpty()) {
			myAgent.setPartOfCluster("ClusterdNM");
		}
		NetworkModel clusterNetworkModel = myAgent.getMyNetworkModel().getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[0]);
		for (int i = 1; i < myAgent.getPartOfCluster().split("::").length; i++) {
			clusterNetworkModel = clusterNetworkModel.getAlternativeNetworkModel().get(myAgent.getPartOfCluster().split("::")[i]);
		}

		if (clusterNetworkModel == null) {
			System.err.println("No appropriate network model found for clustering at " + myAgent.getLocalName() + ". Should be part of cluster: " + myAgent.getPartOfCluster());
		} else {
			// The algorithm distinguishes between active and passive components, so different behaviours have to be
			// started
			if (myAgent.getMyNetworkComponent().getAgentClassName().equals(CompressorAgent.class.getName())
					|| myAgent.getMyNetworkComponent().getAgentClassName().equals(ControlValveAgent.class.getName())
					|| myAgent.getMyNetworkComponent().getAgentClassName().equals(ClusterNetworkAgent.class.getName())) {
				// || myNetworkComponent.getAgentClassName().equals(SimpleValveAgent.class.getName())
				// Active component
				coalitionBehaviour = new CoalitionBehaviour(myAgent, myAgent.getMyEnvironmentModel(), clusterNetworkModel, new CycleClusteringBehaviour(myAgent, clusterNetworkModel, this));
				myAgent.addBehaviour(coalitionBehaviour);
			} else {
				// Passive component
				passiveClusteringBehaviour = new PassiveNAResponderBehaviour(myAgent);
				myAgent.addBehaviour(passiveClusteringBehaviour);
			}
		}
	}

	/**
	 * Starts the find simplification behaviour.
	 */
	private void startFindSimplificationBehaviour() {

		findSimplificationBehaviour = new FindSimplificationBehaviour(myAgent, myAgent.getMyNetworkModel(), this);
		// Because the internal structures can not save dead pipes, so I have to
		// transfer the knowledge from one behaviour to another
		findSimplificationBehaviour.setDead(findDirectionBehaviour.getDead());
		findSimplificationBehaviour.setIncoming(findDirectionBehaviour.getIncoming());
		findSimplificationBehaviour.setOutgoing(findDirectionBehaviour.getOutgoing());
		// Start find simplification behaviour
		findSimplificationBehaviour.start();
	}

	/**
	 * Method to interpret status information.
	 * 
	 * @param phase
	 *            the phase
	 * @param clusterName
	 *            the cluster name
	 */
	private void reactOnStatusInformation(int phase, String clusterName) {
		if (myAgent.getMyNetworkModel() == null) {
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
			} else if (phase == 31) {
				// Starts a verification of the cluster found in step 2
				step = 31;
				startCheckingClusterBehaviour(false);
			} else if (phase == 32) {
				// Starts a verification of the cluster found in step 2
				step = 32;
				startCheckingClusterBehaviour(true);
			} else if (phase == 3) {
				// Publish the results of the verification
				step = 3;
				if (checkingClusterBehaviour != null) {
					checkingClusterBehaviour.rearrangeCluster();
					checkingClusterBehaviour = null;
				}
			} else if (phase == 4) {
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
				if (myAgent.getMyNetworkComponent() instanceof ClusterNetworkComponent) {
					for (String networkComponentID : ((ClusterNetworkComponent) myAgent.getMyNetworkComponent()).getNetworkComponentIDs()) {
						int tries = 0;
						while (myAgent.sendAgentNotification(new AID(networkComponentID, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(new StatusData(myAgent.getPartOfCluster() + "::"
								+ myAgent.getMyNetworkComponent().getId()))) == false) {
							tries++;
							if (tries > 10) {
								System.err.println("PROBLEM (GNA) to send a message to " + networkComponentID + " from " + myAgent.getLocalName());
								break;
							}
							synchronized (this) {
								try {
									wait(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
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
	 * Get the messages and calls the appropriate method to deal with this type of message.
	 * 
	 * @param notification
	 *            the notification
	 * @return the environment notification
	 */
	public synchronized EnvironmentNotification interpretMsg(EnvironmentNotification notification) {
		Object content = ((InitialBehaviourMessageContainer) notification.getNotification()).getData();
		if (content instanceof FindDirectionData) {
			if (findDirectionBehaviour == null || ((FindDirectionData) content).getFlow().equals("?") && !findDirectionBehaviour.isStep2()) {
				notification.moveLastOrBlock(100);
//				System.out.println("=> Notification parked for 'FindDirData' !Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				findDirectionBehaviour.interpretMsg(notification);
			}
		} else if (content instanceof SimplificationData) {
			if (((SimplificationData) content).getStep() == 1) {
				if (findSimplificationBehaviour == null) {
					notification.moveLastOrBlock(100);
//					System.out.println("=> Notification parked for 'SimplificationData' ! Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
				} else
					findSimplificationBehaviour.interpretMsg(notification);
			} else if (((SimplificationData) content).getStep() == 2) {
				if (checkingClusterBehaviour == null) {
					notification.moveLastOrBlock(100);
//					System.out.println("=> Notification parked for 'SimplificationData' ! Receiver: " + myAgent.getLocalName() + " Sender: " + notification.getSender().getLocalName());
				} else
					checkingClusterBehaviour.interpretMsg(notification);
			}
		} else if (content instanceof StatusData) {
			// Got status information
			reactOnStatusInformation(((StatusData) content).getPhase(), ((StatusData) content).getClusterName());
		}
		return notification;
	}

	/**
	 * Send a message about the simulation service.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param content
	 *            the content
	 */
	public void msgSend(String receiver, GenericMesssageData content) {
		// --- Send the information about a new message to the manager agent ---
		myAgent.sendManagerNotification(new StatusData(this.getStep(), "msg+"));
		// --- Try to send the message until the method gives back true (or after 10 retries) ---
		int tries = 0;
		while (myAgent.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(content)) == false) {
			tries++;
			if (tries > 10) {
				System.err.println("PROBLEM (IPB) to send a message to " + receiver + " from " + myAgent.getLocalName());
				break;
			}
			synchronized (this) {
				try {
					wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
		// Start with the first step
		reactOnStatusInformation(0, "");
	}

}
