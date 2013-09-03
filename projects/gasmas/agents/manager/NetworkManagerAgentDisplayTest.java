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
import agentgui.envModel.graph.visualisation.notifications.UpdateTimeSeries;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;
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
		
		// ----------------------------------------------------------
		// --- Get the network component for the test ---------------
		// ----------------------------------------------------------
		NetworkComponent netComp = this.myNetworkModel.getNetworkComponent("n38");
		// --- Open the property window for the component -----------
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
		// --- Second example: Update a TimeSeriesChart -------------
		// ----------------------------------------------------------
//		this.runDataModelNotificationUpdates();

		this.updateTimeSeries_AddTimeSeries();
		
	}

	/**
	 * Update time series: add time series.
	 */
	private void updateTimeSeries_AddTimeSeries() {
		
		NetworkComponent netComp = this.myNetworkModel.getNetworkComponent("n38"); // Exit in that case
		Long startTime = System.currentTimeMillis();

		for (int i=0; i<20; i++) {
		
			// --- Produce a TimeSeries ---------
			TimeSeries tmpSeries = new TimeSeries();
			tmpSeries.setLabel("TimeSeries " + (i+1));
			tmpSeries.setUnit("Random Integer");
			
			// --- Create some value pairs ------
			for (int j = 0; j<10; j++) {
				
				Simple_Long sLong = new Simple_Long();
				if (i<=1) {
					sLong.setLongValue(startTime + (j*1000));
				} else {
					sLong.setLongValue(startTime + (j*i*1000));	
				}

				Simple_Float sFloat = new Simple_Float();
				sFloat.setFloatValue(this.getRandomInteger(-10, 10));

				TimeSeriesValuePair tsvp = new TimeSeriesValuePair();
				tsvp.setTimestamp(sLong);
				tsvp.setValue(sFloat);			
				
				tmpSeries.addTimeSeriesValuePairs(tsvp);
				
			}
			
			// --- Create an UpdateTimeSeries instance ----
			UpdateTimeSeries uts = new UpdateTimeSeries(netComp, 1);
			if (i<=1) {
				uts.addOrExchangeTimeSeries(tmpSeries, i);
			} else {
				uts.exchangeTimeSeries(tmpSeries, 0);
			}

			// --- Send update to the display ---
			this.sendDisplayAgentNotification(uts);
			
//			if (i>2) {
//				// --- Remove last TimeSeries ---
//				UpdateTimeSeries utsRemove = new UpdateTimeSeries(netComp, 1);
//				utsRemove.removeTimeSeries(2);
//				this.sendDisplayAgentNotification(utsRemove);
//			}
			
			// --- Just wait a little -----------
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("=> 'updateTimeSeries' test is done!");
		
	}
	
	/**
	 * Returns a random integer.
	 *
	 * @param fromBound the from bound
	 * @param toBound the to bound
	 * @return the random integer
	 */
	public int getRandomInteger(int fromBound, int toBound) {   
		toBound++;   
		return (int) (Math.random() * (toBound - fromBound) + fromBound);   
	} 
	
	/**
	 * Run data model notification updates.
	 */
	@SuppressWarnings("unused")
	private void runDataModelNotificationUpdates() {
		
		NetworkComponent netComp = this.myNetworkModel.getNetworkComponent("n38"); // Exit in that case
		Object dataModel = netComp.getDataModel();
		Object[] dataModelArr = (Object[]) dataModel;
		
		// --- Get the TimeSeries -------------------------
		Exit exit = (Exit) dataModelArr[0];
		TimeSeriesChart tsc = (TimeSeriesChart) dataModelArr[1];
		tsc.getVisualizationSettings().setChartTitle("This is a test notification");
		
		TimeSeries ts = null;
		if (tsc.getTimeSeriesChartData().size()==0) {
			ts = new TimeSeries();
			ts.setLabel("Energy Consumption");
			ts.setUnit("Consumption [W]");
			tsc.addTimeSeriesChartData(ts);
			
		} else {
			ts = (TimeSeries) tsc.getTimeSeriesChartData().get(0);
		}
		
		// --- Run through updates of the TimeSeriesChart -----
		for (int i=0; i < 100; i++) {
			
			exit.setAlias("Test Nr. " + (i +1));
			
			TimeSeriesValuePair tsvp = new TimeSeriesValuePair();
			
			Simple_Long sLong = new Simple_Long();
			sLong.setLongValue(System.currentTimeMillis());		
			Simple_Float sFloat = new Simple_Float();
			sFloat.setFloatValue((i*i)/1.5f);

			tsvp.setTimestamp(sLong);
			tsvp.setValue(sFloat);			
			
			ts.addTimeSeriesValuePairs(tsvp);
			
			DataModelNotification dmNote = new DataModelNotification(netComp, this.myNetworkModel);
			this.sendDisplayAgentNotification(dmNote);
			
			// --- Just wait a little -----------
			try {
				Thread.sleep(250);
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