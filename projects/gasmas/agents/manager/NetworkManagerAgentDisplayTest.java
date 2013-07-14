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

import gasmas.ontology.Exit;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.Color;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphElementLayout;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.notifications.DataModelNotification;
import agentgui.envModel.graph.visualisation.notifications.DataModelOpenViewNotification;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraphMultiple;
import agentgui.envModel.graph.visualisation.notifications.GraphLayoutNotification;
import agentgui.ontology.TimeSeriesChart;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.time.TimeModelContinuous;

/**
 * The Class NetworManagerAgent.
 */
public class NetworkManagerAgentDisplayTest extends SimulationManagerAgent {

	private static final long serialVersionUID = 1823164338744218569L;

	private NetworkModel myNetworkModel = null;
	

	/*
	 * (non-Javadoc)
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

		// --- Put the environment model into the SimulationService -
		// --- in order to make it accessible for the whole agency --
		((TimeModelContinuous) this.getTimeModel()).setExecuted(true);
		this.notifyAboutEnvironmentChanges();
		
		NetworkComponent netComp = this.myNetworkModel.getNetworkComponent("n38");
		this.sendDisplayAgentNotification(new DataModelOpenViewNotification(netComp));
		
		// ----------------------------------------------------------
		// --- Create a set of display notifications ----------------
		// ----------------------------------------------------------
		DisplayAgentNotificationGraphMultiple notifications = new DisplayAgentNotificationGraphMultiple();
		// ----------------------------------------------------------
		
		// ----------------------------------------------------------
		// --- First example: Color all NetworkComponents -----------
		// ----------------------------------------------------------
		// --- Create list of layout changes ------------------------
		GraphLayoutNotification layoutNotification = new GraphLayoutNotification();

		// --- Get all NetworkComponents ----------------------------
		for (NetworkComponent netCompSingle : this.myNetworkModel.getNetworkComponents().values()) {
			HashSet<GraphElement> graphElements = this.myNetworkModel.getGraphElementsOfNetworkComponent(netCompSingle, new GraphEdge(null, null));
			if (graphElements!=null) {
				for (GraphElement graphElement : graphElements) {
					GraphEdge graphEdge = (GraphEdge) graphElement;
					GraphElementLayout layout = graphEdge.getGraphElementLayout(this.myNetworkModel);
					layout.setMarkerShow(true);
					layout.setMarkerStrokeWidth(15.0f);
					layout.setMarkerColor(Color.green);
					layoutNotification.addGraphElementLayout(layout);
				}
			}
		}
		notifications.addDisplayNotification(layoutNotification);
		// --- Send the notifications now ---------------------------
		this.sendDisplayAgentNotification(notifications);
		
		// ----------------------------------------------------------
		// --- Second example: Set data model of a NetworkModel -----
		// ----------------------------------------------------------
		this.runDataModelUpdate();
		
	}

	private void runDataModelUpdate() {
		
		for (int i=0; i < 15; i++) {
			
			NetworkComponent netCompCopy = this.myNetworkModel.getNetworkComponent("n38").getCopy(this.myNetworkModel);
			if (netCompCopy!=null) {
				
				// -- Get the data model of the 
				Object dataModel = netCompCopy.getDataModel();
				Object[] dataModelArr = (Object[]) dataModel;
				
				Exit exit = (Exit) dataModelArr[0];
				exit.setAlias("Test Nr. " + (i +1));
				
				TimeSeriesChart tsc = (TimeSeriesChart) dataModelArr[1];
				tsc.getVisualizationSettings().setChartTitle("This is a test notification");
			
				DataModelNotification dmNote = new DataModelNotification(netCompCopy);
				dmNote.setDataModelPartUpdateIndex(0);
				this.sendDisplayAgentNotification(dmNote);
				
			}
			// --- Just wait a little -----------
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		// TODO Auto-generated method stub
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
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#doSingleSimulationSequennce()
	 */
	@Override
	public void doSingleSimulationSequennce() {
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

}