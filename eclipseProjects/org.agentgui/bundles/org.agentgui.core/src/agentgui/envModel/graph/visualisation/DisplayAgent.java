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
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Vector;

import javax.swing.SwingUtilities;

import agentgui.core.environment.EnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraph;
import agentgui.envModel.graph.visualisation.notifications.EnableNetworkModelUpdateNotification;
import agentgui.envModel.graph.visualisation.notifications.EnvironmentModelUpdateNotification;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This agent type can be used in order to display the current network model during a 
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
	
	private GraphEnvironmentController myGraphEnvironmentController;
	
	private EnvironmentModelFetcher envModelFetcher;
	private Vector<EnvironmentModel> environmentModelStimuli;
	
	private DisplayAgentNotificationHandler myDisplayAgentNotificationHandler;
	
	private boolean enableNetworkModelUpdate = true;
	private boolean enableNetworkModelCopy = false;
	
	private Vector<ACLMessageForwardingListener> aclMessageForwardingListeners;
	
	
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
		this.addBehaviour(this.getEnvironmentModelFetcher());
		this.addBehaviour(new MessageReceiveBehaviour());
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.myGraphEnvironmentController=null;
		this.environmentModelStimuli=null;
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
		this.environmentModelStimuli=null;
		this.disposeDisplayAgentNotificationHandler();
		this.myDisplayAgentNotificationHandler = null;
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#setMigration(jade.core.Location)
	 */
	@Override
	public void setMigration(Location newLocation) {
		System.out.println("No action specified for the 'setMigratioin()' method of the DisplayAgent: " + this.getLocalName());
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		super.setPauseSimulation(isPauseSimulation);
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
		if (myGraphEnvironmentController==null) {
			myGraphEnvironmentController = (GraphEnvironmentController) this.getEnvironmentController();
		}
		return myGraphEnvironmentController;
	}
	
	
	
	/**
	 * Returns the Vector of EnvironmentModel's that arrived this agent by an EnvironmentStimulus.
	 * @return the stimuli of network model
	 */
	private synchronized Vector<EnvironmentModel> getEnvironmentModelStimuli() {
		if (this.environmentModelStimuli==null) {
			this.environmentModelStimuli = new Vector<EnvironmentModel>();
		}
		return this.environmentModelStimuli;
	}

	// ----------------------------------------------------------------------------------
	// --- From here methods and sub classes to work on environment model changes -------
	// ----------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#setEnvironmentModel(agentgui.simulationService.environment.EnvironmentModel, boolean)
	 */
	@Override
	public void setEnvironmentModel(EnvironmentModel envModel, boolean aSynchron) {
		this.getEnvironmentModelStimuli().add(envModel);
		if (this.getEnvironmentModelFetcher().isRunnable()==false) {
			this.getEnvironmentModelFetcher().restart();
		}
	}

	/**
	 * Get the environment model fetcher.
	 * @return the environment model fetcher
	 */
	private EnvironmentModelFetcher getEnvironmentModelFetcher() {
		if (envModelFetcher==null) {
			envModelFetcher = new EnvironmentModelFetcher();
		}
		return envModelFetcher;
	}
	/**
	 * The Class EnvironmentModelFetcher.
	 */
	private class EnvironmentModelFetcher extends CyclicBehaviour {
		private static final long serialVersionUID = -3330511176232919534L;
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			while (getEnvironmentModelStimuli().size()>0) {
				onEnvironmentStimulus();
				if (getEnvironmentModelStimuli().size()>10) {
					// --- In case of too many models remove until there not more than 10 ---------
					while (getEnvironmentModelStimuli().size()>10) {
						getEnvironmentModelStimuli().remove(0);
					}
				}
			}
			this.block();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	public void onEnvironmentStimulus() {
		
		try {
			// --- Empty the Vector of NetworkModel's -----------------------------------
			EnvironmentModel envModel = this.getEnvironmentModelStimuli().remove(0);
			if (this.myEnvironmentModel!=envModel) {
				this.myEnvironmentModel = envModel;	
			}
			// --- Update TimModel Display ----------------------------------------------
			this.setTimeModelDisplay(this.myEnvironmentModel.getTimeModel());
			
			// --- Update NetworkModel, if allowed -------------------------------------- 
			if (this.isEnableNetworkModelUpdate()==true) {
				if (this.isEnableNetworkModelCopy()==true) {
					final NetworkModel netModelCopy = this.getNetworkModel().getCopy();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							getGraphEnvironmentController().setDisplayEnvironmentModel(netModelCopy);
						}
					});
					
				} else {
					// --- Set original NetworkModel ------------------------------------ 
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							getGraphEnvironmentController().setDisplayEnvironmentModel(myEnvironmentModel.getDisplayEnvironment());
						}
					});					
					
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		if (notification.getNotification() instanceof DisplayAgentNotificationGraph) {
			if (notification.getNotification() instanceof EnableNetworkModelUpdateNotification) {
				// --- Enable / Disable the update of the network model ------- 
				EnableNetworkModelUpdateNotification netmodelUpdate = (EnableNetworkModelUpdateNotification) notification.getNotification(); 
				this.setEnableNetworkModelUpdate(netmodelUpdate.isEnableNetworkModelUpdate());
				
			} else if (notification.getNotification() instanceof EnvironmentModelUpdateNotification) {
				// --- An explicit EnvironmentModel update was send -----------
				EnvironmentModelUpdateNotification envModelUpdate = (EnvironmentModelUpdateNotification) notification.getNotification();
				boolean enabledNetworkModelUpdate = this.isEnableNetworkModelUpdate();
				this.setEnableNetworkModelUpdate(true);
				this.getEnvironmentModelStimuli().add(envModelUpdate.getEnvironmentModel());
				this.onEnvironmentStimulus();
				this.setEnableNetworkModelUpdate(enabledNetworkModelUpdate);
				
			} else {
				// --- Do other things ----------------------------------------
				notification = this.getDisplayAgentNotificationHandler().setDisplayNotification(this.getGraphEnvironmentController(), this.getGraphEnvironmentController().getNetworkModel(), notification);	
			}
		}
		return notification;
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
			this.myDisplayAgentNotificationHandler = null;
		}
	}

	/**
	 * Checks if is enable network model update.
	 * @return true, if is enable network model update
	 */
	public boolean isEnableNetworkModelUpdate() {
		return enableNetworkModelUpdate;
	}
	/**
	 * Sets the enable network model update.
	 * @param enableNetworkModelUpdate the new enable network model update
	 */
	public void setEnableNetworkModelUpdate(boolean enableNetworkModelUpdate) {
		this.enableNetworkModelUpdate = enableNetworkModelUpdate;
	}
	
	/**
	 * Checks if is enable network model copy.
	 * @return true, if is enable network model copy
	 */
	public boolean isEnableNetworkModelCopy() {
		return enableNetworkModelCopy;
	}
	/**
	 * Sets the enable network model copy.
	 * @param enableNetworkModelCopy the new enable network model copy
	 */
	public void setEnableNetworkModelCopy(boolean enableNetworkModelCopy) {
		this.enableNetworkModelCopy = enableNetworkModelCopy;
	}

	
	/**
	 * Gets the list of registered ACL message forwarding listeners.
	 * @return the ACL message forwarding
	 */
	private Vector<ACLMessageForwardingListener> getACLMessageForwardingListeners() {
		if (this.aclMessageForwardingListeners==null){
			this.aclMessageForwardingListeners = new Vector<ACLMessageForwardingListener>();
		}
		return aclMessageForwardingListeners;
	}
	/**
	 * Adds a new ACL message forwarding listener.
	 * @param listener the listener
	 */
	public void addACLMessageForwardingListener(ACLMessageForwardingListener listener){
		this.getACLMessageForwardingListeners().addElement(listener);
	}
	/**
	 * Removes an ACL message forwarding listener.
	 * @param listener the listener
	 */
	public void removeACLMessageForwardingListener(ACLMessageForwardingListener listener){
		this.getACLMessageForwardingListeners().remove(listener);
	}
	/**
	 * Forward an ACL message to the registered listeners.
	 * @param message the notification
	 */
	private void forwardACLMessage(ACLMessage message){
		for (ACLMessageForwardingListener listener : this.getACLMessageForwardingListeners()) {
			listener.forwardACLMessage(message);
		}
	}
	
	/**
	 * Behaviour for receiving ACL messages. The messages will be forwarded to registered NetworkComponentAdapters
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 */
	private class MessageReceiveBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 6462851620274965730L;
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				DisplayAgent.this.forwardACLMessage(msg);
			} else {
				this.block();
			}
		}
	}
	
}
