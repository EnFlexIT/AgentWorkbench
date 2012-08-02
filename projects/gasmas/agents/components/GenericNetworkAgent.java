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

import gasmas.resourceallocation.AllocData;
import gasmas.resourceallocation.CheckingClusterBehaviour;
import gasmas.resourceallocation.FindDirData;
import gasmas.resourceallocation.FindDirectionBehaviour;
import gasmas.resourceallocation.FindSimplificationBehaviour;
import gasmas.resourceallocation.ResourceAllocationBehaviour;
import gasmas.resourceallocation.SimplificationData;
import gasmas.resourceallocation.StatusData;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.transaction.EnvironmentNotification;

public abstract class GenericNetworkAgent extends SimulationAgent {

	private static final long serialVersionUID = 1743261783247570185L;

	protected NetworkModel myNetworkModel = null;

	protected NetworkComponent myNetworkComponent = null;

	/** Different Behaviours, which get messages */
	protected ResourceAllocationBehaviour resourceAllocationBehaviour;
	protected FindDirectionBehaviour findDirectionBehaviour;
	protected FindSimplificationBehaviour findSimplificationBehaviour;
	protected CheckingClusterBehaviour checkingClusterBehaviour;

	/** Shows, if this component need a normal initial process */
	private boolean normalStart = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();

		if (this.getArguments() != null) {
			normalStart = (Boolean) this.getArguments()[0];
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
			if (normalStart) {
				startNewStep(1);
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
				System.out.println("=> Notification parked for 'AllocData' !Receiver: " + this.getLocalName());
			} else {
				resourceAllocationBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof FindDirData) {
			if (findDirectionBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'FindDirData' !Receiver: " + this.getLocalName());
			} else {
				sendManagerNotification(new StatusData(1));
				findDirectionBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof SimplificationData) {
			sendManagerNotification(new StatusData(2));
			if (findSimplificationBehaviour == null) {
				notification.moveLastOrBlock(100);
				System.out.println("=> Notification parked for 'SimplificationData' ! Receiver: " + this.getLocalName());
			} else {
				findSimplificationBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof StatusData) {
			startNewStep(((StatusData) notification.getNotification()).getPhase());
		}
		return notification;
	}

	private void startNewStep(int phase) {
		if (myNetworkModel == null) {
			System.err.println("Error: " + this.getLocalName() + " did not get the network model, so no Behaviour started");
		} else {
			if (phase == 1) {
				startFindDirectionBehaviour();
			} else if (phase == 2) {
				startFindSimplificationBehaviour();
			} else if (phase == 3) {
//				startCheckingClusterBehaviour();
			}
		}
	}

	private void startFindDirectionBehaviour() {
		findDirectionBehaviour = new FindDirectionBehaviour(this, myNetworkModel, myNetworkComponent);
		findDirectionBehaviour.start();
	}

	private void startCheckingClusterBehaviour() {
		checkingClusterBehaviour = new CheckingClusterBehaviour(this, myNetworkModel, myNetworkComponent);
		checkingClusterBehaviour.start();
	}

	private void startFindSimplificationBehaviour() {
		// The end of the the find direction behaviour, so I have to remove this
		// behaviour

		findDirectionBehaviour.setDirections();
		findSimplificationBehaviour = new FindSimplificationBehaviour(this, myNetworkModel, myNetworkComponent);
		// Because the internal structures can not save dead pipes, so I have to
		// transfer the knowledge from one behaviour to another
		findSimplificationBehaviour.setDead(findDirectionBehaviour.getDead());
		findSimplificationBehaviour.setIncoming(findDirectionBehaviour.getIncoming());
		findSimplificationBehaviour.setOutgoing(findDirectionBehaviour.getOutgoing());
		// Start next behaviour, in this case, find simplifications
		findSimplificationBehaviour.start();
	}

}
