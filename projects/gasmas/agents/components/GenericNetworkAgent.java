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
import gasmas.resourceallocation.InitialBehaviourMessageContainer;
import gasmas.resourceallocation.InitialProcessBehaviour;
import gasmas.resourceallocation.ResourceAllocationBehaviour;
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

	public NetworkModel myNetworkModel = null;

	/** My own NetworkComponent. */
	public NetworkComponent myNetworkComponent;
	
	/** Contains a network component id, if the agent is part of a cluster */
	protected String partOfCluster = "";

	/** Shows, if this component need a normal initial process */
	protected boolean normalStart = true;
	
	protected InitialProcessBehaviour initialProcessBehaviour;
	protected ResourceAllocationBehaviour resourceAllocationBehaviour;
	
	public String getPartOfCluster() {
		return partOfCluster;
	}
	
	public void setPartOfCluster(String temp) {
		partOfCluster = temp;
	}
	
	public EnvironmentModel getMyEnvironmentModel() {
		return myEnvironmentModel;
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
					this.myNetworkModel = ((NetworkModel) this.myEnvironmentModel.getDisplayEnvironment());

				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
		initialProcessBehaviour = new InitialProcessBehaviour(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	protected void onEnvironmentStimulus() {
		// TODO: Consider that myEnvironmentModel will change from time to time
		// ... !!
		if (this.myNetworkModel == null) {
			this.myNetworkModel = ((NetworkModel) this.myEnvironmentModel.getDisplayEnvironment());
			this.myNetworkComponent = myNetworkModel.getNetworkComponent(this.getLocalName());
			// Start of the initial start process
			if (normalStart) {
				initialProcessBehaviour.action();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification
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
		} else if (notification.getNotification() instanceof InitialBehaviourMessageContainer) {
			notification = initialProcessBehaviour.interpretMsg(notification);
		}
		return notification;

	}

}
