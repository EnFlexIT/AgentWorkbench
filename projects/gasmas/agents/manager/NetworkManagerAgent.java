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
package gasmas.agents.manager;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;
import gasmas.clustering.analyse.ComponentFunctions;
import gasmas.clustering.behaviours.ClusteringBehaviour;
import gasmas.ontology.ClusterNotification;
import gasmas.ontology.DirectionSettingNotification;
import gasmas.resourceallocation.StatusData;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class NetworManagerAgent.
 */
public class NetworkManagerAgent extends SimulationManagerAgent {

	private static final long serialVersionUID = 1823164338744218569L;

	/** The current project. */
	private Project currProject = null;
	/** The my network model. */
	private NetworkModel myNetworkModel = null;
	/** Time since the last action in the initial process. */
	private long timeOfAction = -1;
	/** Shows the actual step of the initial process. */
	private int actualStep = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();

		// --- Get the connection to the current project ------------
		currProject = Application.getProjectFocused();
		if (currProject == null) {
			takeDown();
			return;
		}

		// --- Get the initial environment model --------------------
		this.envModel = this.getInitialEnvironmentModel();
		// --- Remind the current network model ---------------------
		this.myNetworkModel = (NetworkModel) this.getDisplayEnvironment();
		this.getClusteredModel();

		// --- Make sure that the setup was started, before
		this.addBehaviour(new WaitForTheEndOfSimulationStart(this, 200));
	}

	/**
	 * Setup simulation.
	 */
	private void setupSimulation() {

		// --- Put the environment model into the SimulationService -
		// --- in order to make it accessible for the whole agency --
		this.notifyAboutEnvironmentChanges();

		// --- Start of Time-Behaviour -------------------------------
		// --- To check if one step in the initial process is done --
		this.addBehaviour(new CheckForNextStep(this, 5000));

		ComponentFunctions.printAmountOfDiffernetTypesOfAgents("Global", myNetworkModel);
	}

	// ++++++++++++++ Some temporary test cases here +++++++++++++++++++++

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/*
	 * (non-Javadoc)
	 * 
	 * @seeagentgui.simulationService.agents.SimulationManagerInterface#
	 * getInitialEnvironmentModel()
	 */
	@Override
	public EnvironmentModel getInitialEnvironmentModel() {

		int currentlyDoing = 0;
		final int GET_EnvCont = 1;
		final int GET_NetworkModel = 2;

		EnvironmentModel environmentModel = new EnvironmentModel();
		TimeModelDiscrete myTimeModel = new TimeModelDiscrete(new Long(1000 * 60));

		NetworkModel networkModel = null;
		try {
			currentlyDoing = GET_EnvCont;
			GraphEnvironmentController graphEnvironmentController = (GraphEnvironmentController) currProject.getEnvironmentController();

			currentlyDoing = GET_NetworkModel;
			networkModel = (NetworkModel) graphEnvironmentController.getEnvironmentModelCopy();
			networkModel.getAlternativeNetworkModel().put(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME, networkModel.getCopy());

			environmentModel.setTimeModel(myTimeModel);
			environmentModel.setDisplayEnvironment(networkModel);

		} catch (Exception ex) {

			String msg = null;
			switch (currentlyDoing) {
			case GET_EnvCont:
				msg = ": Could not get GraphEnvironmentController!";
				break;
			case GET_NetworkModel:
				msg = ": Could not get NetworkModel!";
				break;
			}

			if (msg == null) {
				ex.printStackTrace();
			} else {
				System.err.println(this.getLocalName() + ": " + msg);
			}
			return null;
		}
		return environmentModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeagentgui.simulationService.agents.SimulationManagerInterface#
	 * doSingleSimulationSequennce()
	 */
	@Override
	public void doSingleSimulationSequennce() {

	}

	/**
	 * Notify all SimulationAgents about environment changes by using the
	 * SimulationService.
	 */
	private void notifyAboutEnvironmentChanges() {
		try {
			simHelper.setEnvironmentModel(this.envModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeagentgui.simulationService.agents.SimulationManagerAgent#
	 * onManagerNotification
	 * (agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@SuppressWarnings("unchecked")
	// Do not see the reason, why there is a type safety warning by handling and
	// casting the HashSet
	@Override
	protected void onManagerNotification(EnvironmentNotification notification) {

		if (notification.getNotification() instanceof ClusterNetworkComponent) {
			// TODO - This if branch only exists because of backwards
			// compatibility reasons, the old clustering behaviour is still
			// using it

			// --- Got a ClusterNetworkComponent
			// ----------------------------------------
			ClusterNetworkComponent clusterNetworkComponent = (ClusterNetworkComponent) notification.getNotification();
			// --- Substitute the components that are covered by the cluster
			// ------------
			clusterNetworkComponent = this.replaceNetworkModelPartWithCluster(clusterNetworkComponent, true);
			// --- Rename the ClusterNetworkComponent
			// -----------------------------------
			String clusterNetCompIdOld = clusterNetworkComponent.getId();
			String clusterNetCompIdNew = notification.getSender().getLocalName();
			this.getClusteredModel().renameNetworkComponent(clusterNetCompIdOld, clusterNetCompIdNew);

			if (clusterNetworkComponent != null) {
				this.myNetworkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
			}
			this.notifyAboutEnvironmentChanges();
			this.sendAgentNotification(notification.getSender(), clusterNetworkComponent);
			ComponentFunctions.printAmountOfDiffernetTypesOfAgents(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());

		} else if (notification.getNotification() instanceof ClusterNotification) {

			ClusterNotification cn = (ClusterNotification) notification.getNotification();
			// --- Get a list of names of the NetworkComponents, which are a
			// cluster ---
			HashSet<String> clusterNetworkComponentIDs = (HashSet<String>) cn.getNotificationObject();

			NetworkModel clusterNetworkModel = this.getClusteredModel();

			// --- Get all NetworkComponents from the list of names ---
			HashSet<NetworkComponent> clusterNetworkComponents = getClusterNetworkComponents(clusterNetworkComponentIDs, clusterNetworkModel);

			// --- Find the new name for the ClusterNetworkComponent ---
			String clusterNetCompIdNew = "C" + notification.getSender().getLocalName();
			while (clusterNetworkModel.getNetworkComponent(clusterNetCompIdNew) != null || this.myNetworkModel.getNetworkComponent(clusterNetCompIdNew) != null) {
				clusterNetCompIdNew = "C" + clusterNetCompIdNew;
			}

			// --- Replace the components by the cluster ---
			ClusterNetworkComponent clusterNetworkComponent = clusterNetworkModel.replaceComponentsByCluster(clusterNetworkComponents, true);

			// --- Rename the ClusterNetworkComponent ---
			String clusterNetCompIdOld = clusterNetworkComponent.getId();
			clusterNetworkModel.renameNetworkComponent(clusterNetCompIdOld, clusterNetCompIdNew);

			if (clusterNetworkComponent != null) {
				this.myNetworkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
			}
			this.notifyAboutEnvironmentChanges();
			this.sendAgentNotification(notification.getSender(), clusterNetworkComponent);
			ComponentFunctions.printAmountOfDiffernetTypesOfAgents(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());

		} else if (notification.getNotification() instanceof DirectionSettingNotification) {

			DirectionSettingNotification dsn = (DirectionSettingNotification) notification.getNotification();

			// --- Apply setting to the NetworkModel
			// -------------------------------
			NetworkComponent networkComponent = (NetworkComponent) dsn.getNotificationObject();
			this.myNetworkModel.setDirectionsOfNetworkComponent(networkComponent);

			// --- Update the NetworkModel over all container
			// ----------------------
			// TODO

			// --- Send changes to the DisplayAgents
			// -------------------------------
			NetworkComponentDirectionNotification ncdm = new NetworkComponentDirectionNotification(networkComponent);
			this.sendDisplayAgentNotification(ncdm);
		} else if (notification.getNotification() instanceof StatusData) {
			actualStep = ((StatusData) notification.getNotification()).getPhase();
			timeOfAction = System.currentTimeMillis();
		}

	}

	private HashSet<NetworkComponent> getClusterNetworkComponents(HashSet<String> clusterNetworkComponentIDs, NetworkModel clusterNetworkModel) {
		HashSet<NetworkComponent> clusterNetworkComponents = new HashSet<NetworkComponent>();

		// --- Match the list of Strings to find the NetworkComponent ---
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(clusterNetworkModel.getNetworkComponents().values())) {

			for (Iterator<String> it = clusterNetworkComponentIDs.iterator(); it.hasNext();) {
				if (it.next().equals(networkComponent.getId())) {
					// --- Add the NetworkComponent to the list of
					// ClusterNetworkComponents ---
					clusterNetworkComponents.add(networkComponent);

					// --- If it is a cluster, you have to add also all Exits
					// and Entrys of this cluster ---
					if (networkComponent.getAgentClassName().equals(ClusterNetworkAgent.class.getName())) {
						for (NetworkComponent networkComponent2 : clusterNetworkModel.getNeighbourNetworkComponents(networkComponent)) {
							if (networkComponent2.getAgentClassName().equals(EntryAgent.class.getName()) || networkComponent2.getAgentClassName().equals(ExitAgent.class.getName())) {
								clusterNetworkComponents.add(networkComponent2);
							}
						}
					}
					
				}
			}
		}
		return clusterNetworkComponents;
	}

	/**
	 * Replace network model part with cluster.
	 * 
	 * @param networkModel
	 *            the network model
	 * @return the cluster network component
	 */
	private ClusterNetworkComponent replaceNetworkModelPartWithCluster(ClusterNetworkComponent clusterNetworkComponent, boolean distributionNodesAreOuterNodes) {
		NetworkModel clusterNetworkModel = this.getClusteredModel();
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (String id : clusterNetworkComponent.getNetworkComponentIDs()) {
			networkComponents.add(clusterNetworkModel.getNetworkComponent(id));
		}
		return clusterNetworkModel.replaceComponentsByCluster(networkComponents, distributionNodesAreOuterNodes);
	}

	/**
	 * Returns the clustered model.
	 * 
	 * @return the clustered model
	 */
	private NetworkModel getClusteredModel() {
		NetworkModel clusteredNM = this.myNetworkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		if (clusteredNM == null) {
			clusteredNM = this.myNetworkModel.getCopy();
			clusteredNM.setAlternativeNetworkModel(null);
			this.myNetworkModel.getAlternativeNetworkModel().put(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME, clusteredNM);
		}
		return clusteredNM;
	}

	/**
	 * The Class WaitForTheEndOfSimulationStart.
	 */
	private class WaitForTheEndOfSimulationStart extends TickerBehaviour {

		private static final long serialVersionUID = 2352299009087259189L;
		private Integer noOfAgents = null;

		/**
		 * Instantiates a new wait for the end of simulation start.
		 * 
		 * @param agent
		 *            the agent
		 * @param period
		 *            the period
		 */
		public WaitForTheEndOfSimulationStart(Agent agent, long period) {
			super(agent, period);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {

			int runningAgents = this.getAgentsRunning();
			if (noOfAgents == null) {
				noOfAgents = runningAgents;
			} else {
				if (noOfAgents == runningAgents) {
					setupSimulation();
					this.stop();
				} else {
					noOfAgents = runningAgents;
				}
			}

		}

		/**
		 * Returns the countable agents that are connected to teh simulation
		 * service.
		 * 
		 * @return the agents running
		 */
		private int getAgentsRunning() {
			int noAgents = 0;
			try {
				LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
				loadHelper.getAgentMap().getAgentsAtPlatform().size();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return noAgents;
		}

	}

	/**
	 * The Behaviour class WaitForNextStep.
	 */
	protected class CheckForNextStep extends TickerBehaviour {

		private static final long serialVersionUID = -3436029671748149764L;

		/**
		 * Instantiates a new behaviour that waits for the initial
		 * EnvironmentModel.
		 * 
		 * @param agent
		 *            the agent
		 * @param period
		 *            the ticker period
		 */
		public CheckForNextStep(Agent agent, long period) {
			super(agent, period);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			if (System.currentTimeMillis() - timeOfAction >= 3000 && timeOfAction != -1) {
				// We now inform every network component that we finished the
				// first step
				timeOfAction = -1;
				System.out.println("Start of the next step. Phase: " + (actualStep + 1));

				for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(myNetworkModel.getNetworkComponents().values())) {
					while (!sendAgentNotification(new AID(networkComponent.getId(), AID.ISLOCALNAME), new StatusData(actualStep + 1))) {

					}
				}

			}
		}
	}

}