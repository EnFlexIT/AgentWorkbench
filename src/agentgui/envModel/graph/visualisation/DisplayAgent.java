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

import java.util.Vector;

import agentgui.core.environment.EnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This agent can be used in order to display the current network model
 * during a running simulation. It is not necessary that this agent is
 * used within the application window - it is also possible to just start 
 * this agent anywhere else.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DisplayAgent extends AbstractDisplayAgent {

	private static final long serialVersionUID = -766291673903767678L;

	private GraphEnvironmentController myGraphEnvironmentController = null;
	private NetworkModel networkModel = null;
	
	private Vector<EnvironmentModel> stimuliOfNetworkModel = null;
	private Boolean stimuliAction = false;
	
	
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
		this.myGraphEnvironmentController = (GraphEnvironmentController) getEnvironmentController();
		this.networkModel = this.myGraphEnvironmentController.getNetworkModel().getCopy();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		this.myGraphEnvironmentController = (GraphEnvironmentController) this.getEnvironmentController();
		this.networkModel = this.myGraphEnvironmentController.getNetworkModel().getCopy();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.myGraphEnvironmentController=null;
		this.networkModel=null;
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.myGraphEnvironmentController=null;
		this.networkModel=null;
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	protected void onEnvironmentStimulus() {
		
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

					TimeModel timeModel = envModel.getTimeModel();
					NetworkModel netModel = (NetworkModel) envModel.getDisplayEnvironment();
					this.networkModel = netModel.getCopy();

					this.getStimuliOfNetworkModel().remove(0);
					
					this.setTimeModelDisplay(timeModel);
					this.myGraphEnvironmentController.setDisplayEnvironmentModel(this.networkModel);
					
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
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		
		if (notification.getNotification() instanceof NetworkComponentDirectionNotification) {
			
			NetworkComponentDirectionNotification ncdm = (NetworkComponentDirectionNotification) notification.getNotification();
			NetworkComponent netComp = ncdm.getNetworkComponent();
			this.networkModel.setDirectionsOfNetworkComponent(netComp);
			
		}
		myGraphEnvironmentController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint));

		return notification;
	}
	
}
