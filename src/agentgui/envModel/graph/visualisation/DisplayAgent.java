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
package agentgui.envModel.graph.visualisation;

import jade.core.Location;

import java.util.Vector;

import javax.swing.SwingUtilities;

import agentgui.core.environment.EnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraph;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This agent can be used in order to display the current network model during a 
 * running simulation. It is not necessary that this agent is used within the 
 * application window - it is also possible to just start this agent by using 
 * the JADE RMA.
 * For displaying changes of single agents that are representing {@link NetworkComponent}'s 
 * see the classes that are inherit from {@link DisplayAgentNotificationGraph}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DisplayAgent extends AbstractDisplayAgent {

	private static final long serialVersionUID = -766291673903767678L;

	private GraphEnvironmentController myGraphEnvironmentController = null;
	
	private Vector<EnvironmentModel> stimuliOfNetworkModel = null;
	private Boolean stimuliAction = false;
	
	private DisplayAgentNotificationHandler myDisplayAgentNotificationHandler = null;
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#createEnvironmentController()
	 */
	@Override
	protected EnvironmentController createNewEnvironmentController() {
		return new GraphEnvironmentController();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.myGraphEnvironmentController = (GraphEnvironmentController) this.getEnvironmentController();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		this.myGraphEnvironmentController = (GraphEnvironmentController) this.getEnvironmentController();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.myGraphEnvironmentController=null;
		this.stimuliOfNetworkModel=null;
		this.stimuliAction = null;
		this.disposeDisplayAgentNotificationHandler();
		this.myDisplayAgentNotificationHandler = null;
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.myGraphEnvironmentController=null;
		this.stimuliOfNetworkModel=null;
		this.stimuliAction = null;
		this.disposeDisplayAgentNotificationHandler();
		this.myDisplayAgentNotificationHandler = null;
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#setMigration(jade.core.Location)
	 */
	@Override
	public void setMigration(Location newLocation) {
		System.out.println("No action specified for the migratioin of this DisplayAgent: " + this.getLocalName());
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		super.setPauseSimulation(isPauseSimulation);
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	public void onEnvironmentStimulus() {
		
		boolean runStimuliRemover = false;
		
		// --- Add the new NetorkModel to the Vector of not yet displayed NetworkModel's ----------
		this.getStimuliOfNetworkModel().add(myEnvironmentModel);

		// --- Check if the Vector of NetworkModel's needs to be emptied -------------------------- 
		synchronized (stimuliAction) {
			if (this.stimuliAction==false) {
				this.stimuliAction=true;
				runStimuliRemover = true;
			}
		}
		
		if (runStimuliRemover==true) {
			// --- Empty the Vector of NetworkModel's ---------------------------------------------
			while (this.getStimuliOfNetworkModel().size()!=0) {
				try {
					EnvironmentModel envModel = this.getStimuliOfNetworkModel().get(0);
					if (this.myEnvironmentModel==envModel) {
						this.myEnvironmentModel = envModel.getCopy();	
					} else {
						this.myEnvironmentModel = envModel;
					}
					this.getStimuliOfNetworkModel().remove(0);
					
					this.setTimeModelDisplay(this.myEnvironmentModel.getTimeModel());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							myGraphEnvironmentController.setDisplayEnvironmentModel(myEnvironmentModel.getDisplayEnvironment());
						}
					});
					
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} // end while
		}

		// --- Set that the Vector of new NetworkModel's is empty now -----------------------------
		synchronized (stimuliAction) {
			this.stimuliAction=false;
		}
		
	}
	
	/**
	 * Returns the Vector of EnvironmentModel's that arrived this agent by an EnvironmentStimulus.
	 * @return the stimuli of network model
	 */
	private synchronized Vector<EnvironmentModel> getStimuliOfNetworkModel() {
		if (this.stimuliOfNetworkModel==null) {
			this.stimuliOfNetworkModel = new Vector<EnvironmentModel>();
		}
		return this.stimuliOfNetworkModel;
	}
	
	/**
	 * Gets the notification handler for the agent.
	 * @return the display agent notification handler
	 */
	private DisplayAgentNotificationHandler getDisplayAgentNotificationHandler() {
		if (myDisplayAgentNotificationHandler==null) {
			myDisplayAgentNotificationHandler = new DisplayAgentNotificationHandler();
		}
		return myDisplayAgentNotificationHandler;
	}
	
	/**
	 * Disposes the current DisplayAgentNotificationHandler.
	 */
	private void disposeDisplayAgentNotificationHandler() {
		if (this.myDisplayAgentNotificationHandler!=null) {
			this.myDisplayAgentNotificationHandler.getDisplayNotificationStack().removeAllElements();
			this.myDisplayAgentNotificationHandler.dispose();
		}
	}
	
	/**
	 * Returns the current network model.
	 * @return the network model
	 */
	protected NetworkModel getNetworkModel() {
		return this.getGraphEnvironmentController().getNetworkModel();
	}
	/**
	 * Returns the current GraphEnvironmentController.
	 * @return the graph environment controller
	 */
	protected GraphEnvironmentController getGraphEnvironmentController() {
		return this.myGraphEnvironmentController;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		if (notification.getNotification() instanceof DisplayAgentNotificationGraph) {
			notification = this.getDisplayAgentNotificationHandler().setDisplayNotification(this.getGraphEnvironmentController(), this.getGraphEnvironmentController().getNetworkModel(), notification);
		}
		return notification;
	}

}
