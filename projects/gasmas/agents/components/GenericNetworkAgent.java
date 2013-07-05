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

import jade.core.ServiceException;
import gasmas.initialProcess.InitialBehaviourMessageContainer;
import gasmas.initialProcess.InitialProcessBehaviour;
import gasmas.resourceallocation.AllocData;
import gasmas.resourceallocation.ResourceAllocationBehaviour;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This class can be used as a basis class for network components.
 * It is responsible for the notification handling and to keep up to
 * date in which network model the network component is and to start 
 * the initial process behaviour. 
 *
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public abstract class GenericNetworkAgent extends SimulationAgent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1743261783247570185L;

	/** The my network component. */
	protected NetworkComponent myNetworkComponent;
	
	/** The my network model. */
	protected NetworkModel myNetworkModel = null;

	/** Contains a network component id, if the agent is part of a cluster. */
	protected String partOfCluster = "";

	/** Shows, if this component need a normal initial process. */
	protected boolean normalStart = true;

	/** The initial process behaviour. */
	private InitialProcessBehaviour initialProcessBehaviour;
	
	/** The resource allocation behaviour. */
	private ResourceAllocationBehaviour resourceAllocationBehaviour;

	/**
	 * Gets the my network component.
	 *
	 * @return the my network component
	 */
	public NetworkComponent getMyNetworkComponent() {
		return myNetworkComponent;
	}

	/**
	 * Gets the my network model.
	 *
	 * @return the my network model
	 */
	public NetworkModel getMyNetworkModel() {
		return myNetworkModel;
	}

	/**
	 * Gets the part of cluster.
	 *
	 * @return the part of cluster
	 */
	public String getPartOfCluster() {
		return partOfCluster;
	}

	/**
	 * Sets the part of cluster.
	 *
	 * @param partOfCluster the new part of cluster
	 */
	public void setPartOfCluster(String partOfCluster) {
		this.partOfCluster = partOfCluster;
	}

	/**
	 * Gets the my environment model.
	 *
	 * @return the my environment model
	 */
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
		
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			EnvironmentModel envModel = simHelper.getEnvironmentModel();
			if (envModel != null) {
				this.myEnvironmentModel = envModel;
				this.myNetworkModel = ((NetworkModel) this.myEnvironmentModel.getDisplayEnvironment());
				this.myNetworkComponent = myNetworkModel.getNetworkComponent(this.getLocalName());
				// --- Creates the initialProcessBehaviour, but did not start it ---
				this.initialProcessBehaviour = new InitialProcessBehaviour(this);
				this.addBehaviour(initialProcessBehaviour);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	public void onEnvironmentStimulus() {
		// TODO: Consider that myEnvironmentModel will change from time to time
		// ... !!
		if (this.myNetworkModel == null) {
			this.myNetworkModel = ((NetworkModel) this.myEnvironmentModel.getDisplayEnvironment());
			this.myNetworkComponent = myNetworkModel.getNetworkComponent(this.getLocalName());
			// Start of the initial start process
			if (normalStart) {
				this.initialProcessBehaviour = new InitialProcessBehaviour(this);
				this.addBehaviour(initialProcessBehaviour);
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
