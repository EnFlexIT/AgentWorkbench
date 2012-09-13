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
import gasmas.agents.components.ClusterNetworkComponentAgent;
import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;
import gasmas.agents.components.GenericNetworkAgent;
import gasmas.agents.manager.NetworkManagerAgent;
import gasmas.ontology.ClusterNotification;

import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class CheckingClusterBehaviour.
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class CheckingClusterBehaviour {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2365487643740457952L;

	/** The agent, how started the behaviour */
	private GenericNetworkAgent myAgent;
	private InitialProcessBehaviour parentBehaviour;

	/** The network model. */
	private NetworkModel myNetworkModel;

	/** My own NetworkComponent. */
	private NetworkComponent myNetworkComponent;

	/** NetworkComponentNames, which have to add to the cluster */
	private HashSet<String> found = new HashSet<String>();

	/** Shows if this component has information */
	private boolean noInformation = false;

	/** Shows if this component had started */
	private boolean startdone = false;

	/** Saves the neighbours of this component */
	private HashSet<NetworkComponent> myNeighbours = new HashSet<NetworkComponent>();

	/** Shows the limit of age without cluster */
	private int noClusterAge;

	/** Shows the limit of age with cluster */
	private int clusterAge;

	/**
	 * Instantiates a new checking cluster behaviour.
	 * 
	 * @param genericNetworkAgent the generic network agent
	 * @param networkModel the network model
	 * @param parentBehaviour the parent behaviour
	 * @param noClusterAge the no cluster age
	 * @param clusterAge the cluster age
	 */
	public CheckingClusterBehaviour(GenericNetworkAgent genericNetworkAgent, NetworkModel networkModel, InitialProcessBehaviour parentBehaviour, boolean iterativeStep) {
		this.myAgent = genericNetworkAgent;
		this.myNetworkModel = networkModel;
		this.myNetworkComponent = this.myNetworkModel.getNetworkComponent(myAgent.getLocalName());
		this.myNeighbours.addAll(this.myNetworkModel.getNeighbourNetworkComponents(myNetworkComponent));
		this.parentBehaviour = parentBehaviour;
		if (iterativeStep) {
			this.noClusterAge = 50;
			this.clusterAge = 20;
		} else {
			this.noClusterAge = 25;
			this.clusterAge = 0;
		}
	}

	/**
	 * Start of the behaviour, which start the checking cluster algorithm
	 */
	public synchronized void start() {
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg+"));
		if (myNeighbours.size() == 1) {
			noInformation = true;
		} else if (myNetworkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName()) && myNeighbours.size() > 1) {
			// Start from the clusters, which have more then one real neighbour
			// (Exits / Entries do not help in this step, because they are already part of the cluster)
			int i = 0;
			for (NetworkComponent neighbour : myNeighbours) {
				if (neighbour.getAgentClassName().equals(ExitAgent.class.getName()) || neighbour.getAgentClassName().equals(EntryAgent.class.getName())) {
					i++;
				}
			}
			if (myNeighbours.size() - i <= 1) {
				noInformation = true;
			} else {
//				System.out.println("Start at " + myNetworkComponent.getId());
				found.add(myNetworkComponent.getId());
				for (NetworkComponent neighbour : myNeighbours) {
					if (!neighbour.getAgentClassName().equals(ExitAgent.class.getName()) || !neighbour.getAgentClassName().equals(EntryAgent.class.getName())) {
						msgSend(neighbour.getId(), new SimplificationData(myNetworkComponent.getId(), 2));
					}
				}
			}
		}
		// --- Send the information about a deleted message to the manager agent ---
		try {
			wait(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg-"));
		startdone = true;
	}

	/**
	 * Send a message about the simulation service
	 * 
	 * @param receiver
	 * @param content
	 */
	public void msgSend(String receiver, GenericMesssageData content) {
		parentBehaviour.msgSend(receiver, content);
	}

	/**
	 * Get the messages and calls the appropriate method to deal with this type of message
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
		SimplificationData content = (SimplificationData) ((InitialBehaviourMessageContainer) msg.getNotification()).getData();
		String sender = msg.getSender().getLocalName();
		buildCluster(content, sender);
		// --- Send the information about a deleted message to the manager agent ---
		try {
			wait(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg-"));
	}

	/**
	 * Interpret the message, how the cluster has to extend.
	 *
	 * @param content the content
	 * @param sender the sender
	 */
	private void buildCluster(SimplificationData content, String sender) {
		if (content.getWay().size() < noClusterAge && containsCluster(content.getWay(), 2) || content.getWay().size() < clusterAge && containsCluster(content.getWay(), 4)) {
			if (myAgent instanceof ClusterNetworkComponentAgent && !noInformation) {
				// Check if the Cluster is how asked for information
				if (content.getUrInitiator().equals(myNetworkComponent.getId())) {
					// Find an cycle or a part of the network with no other connection
					found.addAll(content.getWay());
				} else if (content.getUrInitiator().compareTo(myNetworkComponent.getId()) < 0) {
					content.addStation(myNetworkComponent.getId());
					if (myNeighbours.size() > 2)
						// All Clusters have to add there own initiator, to verify which one sends the message head 
						content.setInitiator(myNetworkComponent.getId());
					// Sending ahead
					for (NetworkComponent neighbour : myNeighbours) {
						if (!neighbour.getId().equals(sender)) {
							msgSend(neighbour.getId(), content.getCopy());
						}
					}
				}
			} else {
				if (noInformation) {
					// Dead end so it can be part of the cluster
					if (content.getUrInitiator().equals(content.getInitiator())) {
						// Only part, if the question comes directly from the cluster
						if (!content.getWay().isEmpty()) {
							// The way have to contain information, otherwise it is only an Entry or Exit
							content.addStation(myNetworkComponent.getId());
							msgSend(content.getUrInitiator(), content);
						}
					}
				} else {
					if (!content.getWay().contains(myNetworkComponent.getId())) {
						// Check, if this station was not already asked
						content.addStation(myNetworkComponent.getId());
						if (myNeighbours.size() > 2) 
							// All branches have to add there own initiator, to verify which one sends the message head 
							content.setInitiator(myNetworkComponent.getId());
						// Sending ahead
						for (NetworkComponent neighbour : myNeighbours) {
							if (!neighbour.getId().equals(sender)) {
								msgSend(neighbour.getId(), content.getCopy());
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * Checks, how many clusters are in the HashSet way and checks it against the number in j
	 *
	 * @param way the way
	 * @param j the j
	 * @return true, if the number of clusters in the HashSet is smaller than j
	 */
	private boolean containsCluster(HashSet<String> way, int j) {
		int i = 0;
		for (String station : way) {
			if (station.startsWith(NetworkManagerAgent.clusterComponentPrefix)) {
				i++;
				if (i == j)
					return false;
			}
		}
		return true;
	}

	/**
	 * A cluster component finished asking the neighbours about improvements -> Send the information to the manager
	 * agent
	 */
	public synchronized void rearrangeCluster() {
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg+"));
		if (myNetworkComponent instanceof ClusterNetworkComponent) {
			// --- Check, if the component find new components ---
			HashSet<String> tempFound = new HashSet<String>(found);
			for (String temp : tempFound) {
				if (((ClusterNetworkComponent) myNetworkComponent).getNetworkComponentIDs().contains(temp))
					found.remove(temp);
			}
			if (found.size() > 1) {
				System.out.println(myAgent.getLocalName() + " found " + found);
				myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg+"));
				try {
					wait(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// --- Send the information to the manager agent ---
				ClusterNotification cn = new ClusterNotification();
				cn.setReason("rearrangeCluster");
				cn.setNotificationObject(found);
				myAgent.sendManagerNotification(cn);
			}
		}
		try {
			wait(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myAgent.sendManagerNotification(new StatusData(parentBehaviour.getStep(), "msg-"));
	}

}
