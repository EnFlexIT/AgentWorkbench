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

import gasmas.clustering.behaviours.ClusteringBehaviour;
import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.ClusterNACoalitionBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import jade.core.Location;
import jade.core.ServiceException;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class ClusterNetworkAgent extends SimulationAgent {

	private static final long serialVersionUID = -4266211623904956974L;
	
	public static final String CLUSTER_AGENT_Prefix = "C_";
	private static int clusterIDCounter = 0;
	
	private ClusterNetworkComponent clusterNetworkComponent;

	private ClusterNACoalitionBehaviour clusterNACoalitionBehaviour;

	@Override
	protected void setup() {
		super.setup();

		while (this.myEnvironmentModel == null) {

			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				EnvironmentModel envModel = simHelper.getEnvironmentModel();
				if (envModel != null) {
					this.myEnvironmentModel = envModel;
					break;
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}

		}
		clusterNetworkComponent = (ClusterNetworkComponent) this.getArguments()[0];
		clusterNACoalitionBehaviour = new ClusterNACoalitionBehaviour(this, clusterNetworkComponent);
		this.addBehaviour(clusterNACoalitionBehaviour);
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
	
	public static int getFreeID() {
		return clusterIDCounter++;
	}

	public void setClusterNetworkComponent(ClusterNetworkComponent clusterNetworkComponent) {
		this.clusterNetworkComponent=clusterNetworkComponent;
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		if (notification.getNotification() instanceof ClusterNetworkComponent) {
			clusterNetworkComponent = (ClusterNetworkComponent) notification.getNotification();
			startCoalitionBehaviour();
		}
		return notification;
	}

	private void startCoalitionBehaviour() {
		NetworkModel clusteredNM = getClusteredModel().getCopy();
		ClusteringBehaviour clusteringBehaviour = new CycleClusteringBehaviour(this, clusteredNM, null);
		this.addBehaviour(new CoalitionBehaviour(this, myEnvironmentModel, clusteredNM, clusteringBehaviour));
	}

	private NetworkModel getClusteredModel() {
		NetworkModel networkModel = (NetworkModel) myEnvironmentModel.getDisplayEnvironment();
		NetworkModel clusteredNM = networkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		return clusteredNM;
	}

	
}
