package org.awb.env.networkModel.visualisation;

import java.util.Vector;

import javax.swing.SwingUtilities;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.visualisation.notifications.DisplayAgentNotificationGraph;
import org.awb.env.networkModel.visualisation.notifications.EnableNetworkModelUpdateNotification;
import org.awb.env.networkModel.visualisation.notifications.EnvironmentModelUpdateNotification;

import de.enflexit.awb.baseUI.agents.AbstractSwingDisplayAgent;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

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
public class DisplayAgent extends AbstractSwingDisplayAgent {

	private static final long serialVersionUID = -766291673903767678L;
	
	private GraphEnvironmentController myGraphEnvironmentController;
	
	private EnvironmentModelFetcher envModelFetcher;
	private Vector<EnvironmentModel> environmentModelStimuli;
	
	private DisplayAgentNotificationHandler myDisplayAgentNotificationHandler;
	
	private boolean enableNetworkModelUpdate = true;
	private boolean enableNetworkModelCopy = false;
	
	private Vector<ACLMessageForwardingListener> aclMessageForwardingListeners;
	
	
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
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.myGraphEnvironmentController=null;
		this.environmentModelStimuli=null;
		this.disposeDisplayAgentNotificationHandler();
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
		super.beforeMove();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
	}

	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#setMigration(jade.core.Location)
	 */
	@Override
	public void setMigration(Location newLocation) {
		// --- Nothing to do here -------------------------
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		super.setPauseSimulation(isPauseSimulation);
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#createEnvironmentController()
	 */
	@Override
	protected EnvironmentController createNewEnvironmentController() {
		return new GraphEnvironmentController();
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
	 * Returns the current network model.
	 * @return the network model
	 */
	protected NetworkModel getNetworkModel() {
		return this.getGraphEnvironmentController().getNetworkModel();
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
			if (envModel!=null && this.myEnvironmentModel!=envModel) {
				// --- Set a new environment model to display ---------------------------
				this.myEnvironmentModel = envModel;	

				// --- Update NetworkModel, if allowed ---------------------------------- 
				if (this.isEnableNetworkModelUpdate()==true) {
					
					// --- Get original or copy -----------------------------------------
					NetworkModel networkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();
					if (networkModel!=null && this.isEnableNetworkModelCopy()==true) {
						networkModel = networkModel.getCopy();
					}
					// --- Set to visualization -----------------------------------------
					if (networkModel!=null) {
						final NetworkModel networkModelToDisplay = networkModel;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								DisplayAgent.this.getGraphEnvironmentController().setDisplayEnvironmentModel(networkModelToDisplay);
							}
						});
					}
				}
			}
			
			// --- Finally, update TimModel Display -------------------------------------
			this.setTimeModelDisplay(this.myEnvironmentModel.getTimeModel());
						
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
		for (int i = 0; i < this.getACLMessageForwardingListeners().size(); i++) {
			ACLMessageForwardingListener listener = this.getACLMessageForwardingListeners().get(i);
			try {
				listener.forwardACLMessage(message);
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while forwarding ACL message to Listener / NetworkComponentAdapter!");
				ex.printStackTrace();
			}
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
