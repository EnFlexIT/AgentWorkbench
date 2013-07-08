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
import gasmas.initialProcess.InitialBehaviourMessageContainer;
import gasmas.initialProcess.StatusData;
import gasmas.ontology.ClusterNotification;
import gasmas.ontology.DirectionSettingNotification;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class NetworManagerAgent.
 */
public class NetworkManagerAgent extends SimulationManagerAgent {

	private static final long serialVersionUID = 1823164338744218569L;

	/** The my network model. */
	private NetworkModel myNetworkModel = null;
	/** Shows the actual step of the initial process. */
	private int actualStep = 0;
	/** Shows the actual number of message in the system in a specific step. */
	private int actualMessageFlow = 0;
	/** Shows if in the actual step something changed. */
	private boolean changeDuringStep = false;
	/** The prefix for cluster components */
	public static String clusterComponentPrefix = "C";
	/** List, how holds used cluster names globally */
	private HashSet<String> usedClusterNames = new HashSet<String>();
	/** StartTime of a local phase */
	private long startTime;
	/** StartTime of the initial process */
	private long startTimeGlobal;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();

		// --- Remind the current network model ---------------------
		this.myNetworkModel = (NetworkModel) this.getDisplayEnvironment();

		// --- Make sure that all agents were started ---------------
		this.addBehaviour(new WaitForTheEndOfSimulationStart(this, 300));
	}

	/**
	 * Setup simulation.
	 */
	private void setupSimulation() {

		//this.debug();
		
		// --- Put the environment model into the SimulationService -
		// --- in order to make it accessible for the whole agency --
		this.notifyAboutEnvironmentChanges();

		ComponentFunctions.printAmountOfDiffernetTypesOfAgents("Global", myNetworkModel);
		ComponentFunctions.printAmountOfNodesEdgesAgents("Global", myNetworkModel);
		startTime = new Date().getTime();
		startTimeGlobal = new Date().getTime();
	}

	// ++++++++++++++ Some temporary test cases here +++++++++++++++++++++
	@SuppressWarnings("unused")
	private void debug() {
		
		Vector<NetworkComponent> searchVector = new Vector<NetworkComponent>();
		searchVector.add(this.myNetworkModel.getNetworkComponent("n47"));
		searchVector.add(this.myNetworkModel.getNetworkComponent("n49"));
		
		Vector<NetworkComponent> neighbours = this.myNetworkModel.getNeighbourNetworkComponents(searchVector);
		
		System.out.println("=> Number of neighbours for " + searchVector.toString() + ": " + neighbours.size());
		for (NetworkComponent netCompNeigghbour : neighbours) {
			System.out.println("=> Neighbour: " + netCompNeigghbour.getId());
		}
		System.out.println("=> ");
		
		
	}
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		// TODO Auto-generated method stub
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
			simHelper.setEnvironmentModel(this.myEnvironmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @seeagentgui.simulationService.agents.SimulationManagerAgent#onManagerNotification (agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onManagerNotification(EnvironmentNotification notification) {

		// Do not see the reason, why there is a type safety warning by handling and
		// casting the HashSet
		
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
			String partOfCluster = "ClusterdNM";
			
			// --- Get a list of names of the NetworkComponents, which are in the cluster ---
			HashSet<String> clusterNetworkComponentIDs = (HashSet<String>) cn.getNotificationObject();
			NetworkModel clusterNetworkModel = null;
			// --- At least one new cluster found ---
			changeDuringStep = true;
			// --- Distinguish between different reasons of a cluster notification ---
			if (cn.getReason().startsWith("newCluster")){
				// --- New cluster found, so no special operations needed ---
				clusterNetworkModel = this.getClusteredModel();
				
			} else if (cn.getReason().startsWith("rearrangeCluster")){
				// --- Rearrange of an existing cluster ---
				clusterNetworkModel = getClusteredModel();
				// --- Remove the existing cluster ---
				clusterNetworkComponentIDs.remove(notification.getSender().getLocalName());
				ClusterNetworkComponent oldClusterNetworkComponent = (ClusterNetworkComponent) clusterNetworkModel.getNetworkComponent(notification.getSender().getLocalName());
				if (oldClusterNetworkComponent == null) {
					this.setActualMessageFlow(new StatusData(-1, "msg-"));
					return;
				}
				// --- Add the network components, which are already in the cluster, to the new cluster ---
				clusterNetworkComponentIDs.addAll(oldClusterNetworkComponent.getNetworkComponentIDs());
				// --- Replace cluster by components in the network model ---
				clusterNetworkModel.replaceClusterByComponents(oldClusterNetworkComponent);
				// --- Delete alternative network model ---
				clusterNetworkModel.getAlternativeNetworkModel().remove(oldClusterNetworkComponent.getId());

			} else if (cn.getReason().startsWith("furtherClustering")){
				// --- The reason also got information about in which cluster, the algorithm found another cluster  ---
				partOfCluster = cn.getReason().split("//")[1];
				clusterNetworkModel = myNetworkModel.getAlternativeNetworkModel().get(partOfCluster.split("::")[0]);
				for (int i = 1; i < partOfCluster.split("::").length; i++) {
					clusterNetworkModel = clusterNetworkModel.getAlternativeNetworkModel().get(partOfCluster.split("::")[i]);
				}
			}
			
			if (clusterNetworkModel != null) {
				// --- Get all NetworkComponents from the list of names ---
				HashSet<NetworkComponent> clusterNetworkComponents = getClusterNetworkComponents(clusterNetworkComponentIDs, clusterNetworkModel);
				if (clusterNetworkComponents.isEmpty()) {
					System.err.println("Error while replacing cluster, list of cluster network components is empty. " + clusterNetworkComponentIDs);
				} else {
					String clusterNetCompIdNew = "";

					// --- Find the new name for the ClusterNetworkComponent ---
					clusterNetCompIdNew = clusterComponentPrefix + notification.getSender().getLocalName();
					while (usedClusterNames.contains(clusterNetCompIdNew)) {
						clusterNetCompIdNew = clusterComponentPrefix + clusterNetCompIdNew;
					}	
					usedClusterNames.add(clusterNetCompIdNew);
					
					// --- Replace the components by the cluster ---
					ClusterNetworkComponent clusterNetworkComponent = clusterNetworkModel.replaceComponentsByCluster(clusterNetworkComponents, true);

					// --- Rename the ClusterNetworkComponent ---
					String clusterNetCompIdOld = clusterNetworkComponent.getId();
					clusterNetworkModel.renameNetworkComponent(clusterNetCompIdOld, clusterNetCompIdNew);

					// --- Shift alternative models to the appropriate parent network model ---
					for (NetworkComponent networkComponent : clusterNetworkComponents) {
						if (networkComponent instanceof ClusterNetworkComponent) {
							clusterNetworkModel.getAlternativeNetworkModel().remove(networkComponent.getId());
							clusterNetworkComponent.getClusterNetworkModel().getAlternativeNetworkModel().put(networkComponent.getId(), ((ClusterNetworkComponent) networkComponent).getClusterNetworkModel());
						}
					}

					if (clusterNetworkComponent != null) {
						clusterNetworkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
						// Starts the agent to the cluster network component
						this.startClusterAgent(clusterNetCompIdNew, clusterNetworkComponent, partOfCluster);
					}

					// -- Update the environment for all agents -----
					this.notifyAboutEnvironmentChanges();
					
//					ComponentFunctions.printAmountOfDiffernetTypesOfAgents(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
//					ComponentFunctions.printAmountOfNodesEdgesAgents(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
//					ComponentFunctions.printAmountOfConnectionsWithEnviroment(clusterNetworkComponent);
//					ComponentFunctions.printAmountOfFixedEdgeDirections(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
					System.out.println(clusterNetworkComponent.getId());
					this.setActualMessageFlow(new StatusData(-1, "msg-"));
				}
			}
			
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
			StatusData information = ((StatusData) notification.getNotification());
			if (actualStep == -2) return;
			// --- A agent is still working in a specific step ---
			if (actualStep == information.getPhase()){
				// --- Record the message flow ---
				setActualMessageFlow(information);
			}else
				System.out.println("Step is" + actualStep + ", but got " + information.getPhase() +" "+ information.getReason()+ " from " + notification.getSender().getLocalName());
		}

	}

	/**
	 * Sets the actual message flow, which is used to monitor
	 * the count of messages used by a specific step
	 * (If it is null -> start new step 
	 * 
	 * @param information the new actual message flow
	 */
	private synchronized void setActualMessageFlow(StatusData information) {
		if (actualStep == -2) return;
		if (information.getReason().equals("msg+")) {
			// --- One more message is used by this step ---
			actualMessageFlow++;
		} else if (information.getReason().equals("msg-")) {
			// --- One less message is used by this step ---
			actualMessageFlow--;
			
			if (actualMessageFlow == 0) {
				// --- No message are in flow, so step is at its end ---
				// --- Short delay, that all agents can proceed new information ---
				try {
					wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// --- Next step can be started ---
				if (actualStep == 4) {
					StartNextStepClustering startWaker = new StartNextStepClustering(this, 3000);
					this.addBehaviour(startWaker);
				} else {
					startNextStep();
				}
			}
		}
	}
	
	/**
	 * Start next step in the initial process behaviour.
	 */
	private void startNextStep() {
		// Step done -> inform network components about this
		HashSet<String> networkComponentIDs = new HashSet<String>();
		// Find the network components, how have to be informed about the end of the step
		if (actualStep == -2) return;
		if (actualStep == 0) {
			networkComponentIDs.addAll(myNetworkModel.getNetworkComponents().keySet());
			actualStep = 1;
		} else if (actualStep == 1) {
			System.out.println("Duration after step 2: " + (new Date().getTime() - startTime));
			startTime = new Date().getTime();
			networkComponentIDs.addAll(myNetworkModel.getNetworkComponents().keySet());
			actualStep = 2;
		} else if (actualStep == 2) {
			ComponentFunctions.printAmountOfFixedEdgeDirections("Global", myNetworkModel);
			networkComponentIDs.addAll(getClusteredModel().getNetworkComponents().keySet());
			actualStep = 31;
		} else if (actualStep == 3) {
			if (changeDuringStep) {
				networkComponentIDs.addAll(getClusteredModel().getNetworkComponents().keySet());
				actualStep = 32;
			} else {
				System.out.println("Duration after step 4: " + (new Date().getTime() - startTime));
				startTime = new Date().getTime();
				getAllNetworkComponents(networkComponentIDs, myNetworkModel);
				actualStep = 4;
			}
			changeDuringStep = false;
		} else if (actualStep == 31) {
			networkComponentIDs.addAll(getClusteredModel().getNetworkComponents().keySet());
			actualStep = 3;
		} else if (actualStep == 32) {
			getAllNetworkComponents(networkComponentIDs, myNetworkModel);
			actualStep = 3;
		} else if (actualStep == 4) {
			// Clustering Round until the clustering algorithm did not find anything new
			if (changeDuringStep) {
				getAllNetworkComponents(networkComponentIDs, myNetworkModel);
			}
			changeDuringStep = false;
			actualStep = 4;
		}
		// Now inform the specific network components about the next step
		if (!networkComponentIDs.isEmpty()) {
			System.out.println("----------------------------------------------------------------Start of the next step. Phase: " + (actualStep));
			Iterator<String> it = networkComponentIDs.iterator();
			while (it.hasNext()) {
				String networkComponentID = it.next();
				int tries = 0;
				while (!sendAgentNotification(new AID(networkComponentID, AID.ISLOCALNAME), new InitialBehaviourMessageContainer(new StatusData(actualStep)))) {
					tries++;
					if (tries > 10) {
						System.err.println("PROBLEM (NS) to send a message to " + networkComponentID + " from " + this.getLocalName() + " Phase: " + (actualStep));
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
			
		} else {
			// The last step of the initial process is done
			System.out.println("Duration after step 5: " + (new Date().getTime() - startTime));
			System.out.println("Duration after initial process: " + (new Date().getTime() - startTimeGlobal));
			System.out.println("----------------------------------------------------------------Finished the last step. Phase: " + actualStep);
			actualStep = -2;
			ComponentFunctions.printNodesEdgesAgentsForAllNetworkModels("Global", myNetworkModel);
			JOptionPane.showMessageDialog(null, "Initial process finished!", "Done", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Gets all network components over all network models.
	 *
	 * @param networkComponentIDs the network component IDs
	 * @param networkModel the network model
	 * @return the all network components
	 */
	private void getAllNetworkComponents(HashSet<String> networkComponentIDs, NetworkModel networkModel) {
		Iterator<Entry<String, NetworkModel>> it = networkModel.getAlternativeNetworkModel().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, NetworkModel> actualNetworkModel = it.next();
			// --- Get all network components in a network model ---
			networkComponentIDs.addAll(actualNetworkModel.getValue().getNetworkComponents().keySet());
			// --- Check, if there are alternative models in this network model ---
			getAllNetworkComponents(networkComponentIDs, actualNetworkModel.getValue());
		}
	}


	/**
	 * Gets the cluster network components.
	 *
	 * @param clusterNetworkComponentIDs the cluster network component IDs
	 * @param clusterNetworkModel the cluster network model
	 * @return the cluster network components
	 */
	private HashSet<NetworkComponent> getClusterNetworkComponents(HashSet<String> clusterNetworkComponentIDs, NetworkModel clusterNetworkModel) {
		HashSet<NetworkComponent> clusterNetworkComponents = new HashSet<NetworkComponent>();

		// --- Match the list of Strings to find the NetworkComponent ---
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(clusterNetworkModel.getNetworkComponents().values())) {

			for (Iterator<String> it = clusterNetworkComponentIDs.iterator(); it.hasNext();) {
				String temp = it.next(); 
				if (temp.equals(networkComponent.getId())) {
					// --- Add the NetworkComponent to the list of ClusterNetworkComponents ---
					clusterNetworkComponents.add(networkComponent);

					// --- If it is a cluster, you have to add also all Exits and Entrys of this cluster ---
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
	 * @param networkModel the network model
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
		 * @param agent the agent
		 * @param period  the period
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
				noAgents = loadHelper.getAgentMap().getAgentsAtPlatform().size();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return noAgents;
		}

	}

	/**
	 * The Class StartNextStepClustering as a WakerBehaviour
	 * Only used for step 5 of the initial process, because
	 * there are no fixed end points
	 */
	private class StartNextStepClustering extends WakerBehaviour {

		private static final long serialVersionUID = -5097674480963770914L;

		public StartNextStepClustering(Agent a, long timeout) {
			super(a, timeout);
			
		}

		public void onWake(){
			actualMessageFlow = 0;
			startNextStep();
			this.stop();
		}

	}
	
	/**
	 * Starts cluster agent.
	 * 
	 * @param agentName
	 * @param clusterNetworkComponent
	 * @param partOfCluster
	 */
	private void startClusterAgent(String agentName, ClusterNetworkComponent clusterNetworkComponent, String partOfCluster) {
		try {
			LoadServiceHelper loadHelper = (LoadServiceHelper) this.getHelper(LoadService.NAME);
			Object[] params = new Object[] {"clusterInformation", false, clusterNetworkComponent, partOfCluster };
			try {
				loadHelper.startAgent(agentName, gasmas.agents.components.ClusterNetworkComponentAgent.class.getName(), params, this.getContainerController().getContainerName());
			} catch (ControllerException e) {
				e.printStackTrace();
			}

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
}