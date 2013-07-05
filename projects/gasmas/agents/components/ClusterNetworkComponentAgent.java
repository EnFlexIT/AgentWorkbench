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

import gasmas.initialProcess.InitialBehaviourMessageContainer;
import gasmas.initialProcess.StatusData;
import jade.core.AID;
import jade.core.Location;
import jade.core.ServiceException;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class ClusterNetworkComponentAgent.
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class ClusterNetworkComponentAgent extends GenericNetworkAgent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5755894155609484866L;

	@Override
	protected void setup() {
		super.setup();
		
		// Check for start arguments of the agent
		if (this.getArguments() != null) {
			if (this.getArguments()[0] != null && this.getArguments()[0] instanceof String) {
				if (((String) this.getArguments()[0]).equals("clusterInformation")) {
					// Used to give a cluster network component all information that are needed
					this.normalStart = (Boolean) this.getArguments()[1];
					this.myNetworkComponent = (NetworkComponent) this.getArguments()[2];
					this.partOfCluster = (String) this.getArguments()[3];
				}

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
		}
		
		// Inform every network component, that the component is part of the cluster
		for (String networkComponentID : ((ClusterNetworkComponent) myNetworkComponent).getNetworkComponentIDs()) {
			int tries = 0;
			while (this.sendAgentNotification(new AID(networkComponentID, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(new StatusData(this.getPartOfCluster() + "::"
					+ this.myNetworkComponent.getId()))) == false) {
				tries++;
				if (tries > 10) {
					System.out.println("PROBLEM (CCCA) to send a message to " + networkComponentID + " from " + this.getLocalName());
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

	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		// TODO Auto-generated method stub
	}
	@Override
	public void setMigration(Location newLocation) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onEnvironmentStimulus() {
		// TODO Auto-generated method stub
	}
	
}
