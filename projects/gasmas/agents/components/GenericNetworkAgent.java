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
package gasmas.agents.components;

import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import gasmas.resourceallocation.AllocData;
import gasmas.resourceallocation.CheckingClusterBehaviour;
import gasmas.resourceallocation.ClusterCheckData;
import gasmas.resourceallocation.FindDirData;
import gasmas.resourceallocation.FindDirectionBehaviour;
import gasmas.resourceallocation.FindSimplificationBehaviour;
import gasmas.resourceallocation.ResourceAllocationBehaviour;
import gasmas.resourceallocation.SimplificationData;
import gasmas.resourceallocation.StatusData;
import jade.core.ServiceException;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public abstract class GenericNetworkAgent extends SimulationAgent {

	private static final long serialVersionUID = 1743261783247570185L;

	protected NetworkModel myNetworkModel = null;

	/** My own NetworkComponent. */
	protected NetworkComponent myNetworkComponent;

	/** Different Behaviours */
	protected ResourceAllocationBehaviour resourceAllocationBehaviour;
	protected FindDirectionBehaviour findDirectionBehaviour;
	protected FindSimplificationBehaviour findSimplificationBehaviour;
	protected CheckingClusterBehaviour checkingClusterBehaviour;
	protected CoalitionBehaviour coalitionBehaviour;
	protected PassiveNAResponderBehaviour passiveClusteringBehaviour;

	/** Contains a network component id, if the agent is part of a cluster */
	protected String partOfCluster = "";

	/** Shows, if this component need a normal initial process */
	protected boolean normalStart = true;

	/** Shows the internal step of the agent */
	protected int step = -1;

	public int getStep() {
		return step;
	}

	public String getPartOfCluster() {
		return partOfCluster;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();

		// Check for start arguments of the agent
		if (this.getArguments() != null) {
			// Used to give a cluster network component all information that are needed
			normalStart = (Boolean) this.getArguments()[0];
			this.myNetworkComponent = (NetworkComponent) this.getArguments()[1];
			partOfCluster = (String) this.getArguments()[2];
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				EnvironmentModel envModel = simHelper.getEnvironmentModel();
				if (envModel != null) {
					this.myEnvironmentModel = envModel;
					this.myNetworkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();

				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	protected void onEnvironmentStimulus() {
		// TODO: Consider that myEnvironmentModel will change from time to time
		// ... !!
		if (this.myNetworkModel == null) {
			this.myNetworkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();
			this.myNetworkComponent = myNetworkModel.getNetworkComponent(this.getLocalName());
			// Start of the initial start process 
			if (normalStart) {
				reactOnStatusInformation(0, "");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification
	 * (agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		if (notification.getNotification() instanceof AllocData) {
			if (resourceAllocationBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'AllocData'! Receiver: " + this.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				resourceAllocationBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof FindDirData) {
			if (findDirectionBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'FindDirData' !Receiver: " + this.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				sendManagerNotification(new StatusData(step));
				findDirectionBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof SimplificationData) {

			if (findSimplificationBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'SimplificationData' ! Receiver: " + this.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				sendManagerNotification(new StatusData(step));
				findSimplificationBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof ClusterCheckData) {

			if (checkingClusterBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'ClusterCheckData' ! Receiver: " + this.getLocalName() + " Sender: " + notification.getSender().getLocalName());
			} else {
				sendManagerNotification(new StatusData(step));
				checkingClusterBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof StatusData) {
			// Got status information
			reactOnStatusInformation(((StatusData) notification.getNotification()).getPhase(), ((StatusData) notification.getNotification()).getClusterName());
		}
		return notification;
	}

	/**
	 * Method to interpret status information
	 * 
	 * @param phase
	 * @param clusterName
	 */
	private void reactOnStatusInformation(int phase, String clusterName) {
		if (myNetworkModel == null) {
			// Got status information, but no network model is there, so ignoring of the message
			System.err.println("Error: " + this.getLocalName() + " did not get the network model, so no status information are interpreted.");
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
				sendManagerNotification(new StatusData(step));
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
					this.removeBehaviour(coalitionBehaviour);
				if (passiveClusteringBehaviour != null)
					this.removeBehaviour(passiveClusteringBehaviour);
				startClusteringBehaviour();
			} else if (!clusterName.isEmpty()) {
				// Saves the name of the cluster in which this agent is
				partOfCluster = clusterName;
			}
		}
	}

	/**
	 * Starts the find direction behaviour
	 */
	private void startFindDirectionBehaviour() {
		findDirectionBehaviour = new FindDirectionBehaviour(this, myNetworkModel);
		findDirectionBehaviour.start();
	}

	/**
	 * Starts the checking cluster behaviour
	 */
	private void startCheckingClusterBehaviour() {

		checkingClusterBehaviour = new CheckingClusterBehaviour(this, myNetworkModel);
		checkingClusterBehaviour.start();
	}

	/**
	 * Starts the clustering behaviour
	 */
	protected void startClusteringBehaviour() {
		// If this agent is not part of a cluster, it is part of the normal network model
		if (partOfCluster.isEmpty()) {
			partOfCluster = "ClusterdNM";
		}
		// Get the cluster network model, where the clustering algorithm should work
		NetworkModel clusterNetworkModel = myNetworkModel.getAlternativeNetworkModel().get(partOfCluster);
		
		// The algorithm distinguishes between active and passive components, so different behaviours have to be started
		if (myNetworkComponent.getAgentClassName().equals(CompressorAgent.class.getName()) || myNetworkComponent.getAgentClassName().equals(ControlValveAgent.class.getName())
				 || myNetworkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName())) {
			//|| myNetworkComponent.getAgentClassName().equals(SimpleValveAgent.class.getName())
			// Active component
			coalitionBehaviour = new CoalitionBehaviour(this, myEnvironmentModel, clusterNetworkModel, new CycleClusteringBehaviour(this, clusterNetworkModel));
			this.addBehaviour(coalitionBehaviour);
		} else {
			// Passive component
			passiveClusteringBehaviour = new PassiveNAResponderBehaviour(this);
			this.addBehaviour(passiveClusteringBehaviour);

		}
	}
	
	/**
	 * Starts the find simplification behaviour
	 */
	private void startFindSimplificationBehaviour() {
		
		findSimplificationBehaviour = new FindSimplificationBehaviour(this, myNetworkModel);
		// Because the internal structures can not save dead pipes, so I have to
		// transfer the knowledge from one behaviour to another
		findSimplificationBehaviour.setDead(findDirectionBehaviour.getDead());
		findSimplificationBehaviour.setIncoming(findDirectionBehaviour.getIncoming());
		findSimplificationBehaviour.setOutgoing(findDirectionBehaviour.getOutgoing());
		// Start find simplification behaviour
		findSimplificationBehaviour.start();
	}

}
