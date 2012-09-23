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

import jade.core.ServiceException;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.environment.EnvironmentModel;
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

	private final String pathImage = GraphGlobals.getPathImages();
	
	private JFrame useFrame = null;
	private JPanel usePanel = null;
	
	private GraphEnvironmentController myGraphEnvironmentController = null;
	private NetworkModel netModel = null;
	
	private Vector<NetworkModel> stimuliOfNetworkModel = null;
	private Boolean stimuliAction = false;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		if (startArgs == null || startArgs.length == 0) {
			// --- started in a normal way ----------------
			this.useFrame = this.getIndependentFrame();
			
			this.myEnvironmentModel = this.grabEnvironmentModelFromSimulationService(); 
			if (this.myEnvironmentModel!=null) {
				NetworkModel tmpNetModel = (NetworkModel) myEnvironmentModel.getDisplayEnvironment();
				this.netModel = tmpNetModel.getCopy();	
			}

		} else {
			// --- started from Agent.GUI -----------------
			this.usePanel = (JPanel) startArgs[0];
			
			GraphEnvironmentController startController = (GraphEnvironmentController) startArgs[1];
			this.netModel = (NetworkModel) startController.getEnvironmentModelCopy();
			
		}
		// --- Build the visual components ----------------
		this.buildVisualizationGUI();
		
	}
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.destroyVisualizationGUI();
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.destroyVisualizationGUI();
		super.beforeMove();
		
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		this.useFrame = this.getIndependentFrame();
		EnvironmentModel envModel = this.grabEnvironmentModelFromSimulationService(); 
		NetworkModel tmpNetModel = (NetworkModel) envModel.getDisplayEnvironment();
		this.netModel = tmpNetModel.getCopy();
		this.buildVisualizationGUI();
		super.afterMove();
	}
	
	/**
	 * Builds the visualization GUI.
	 */
	private void buildVisualizationGUI() {
		
		// --- Build the new Controller GUI ---------------
		this.myGraphEnvironmentController = new GraphEnvironmentController();
		this.myGraphEnvironmentController.setEnvironmentModel(this.netModel);
		
		if (this.useFrame!=null) {
			this.useFrame.setContentPane(myGraphEnvironmentController.getEnvironmentPanel());
			this.useFrame.pack();
			this.useFrame.setVisible(true);
		} else {
			this.usePanel.add(myGraphEnvironmentController.getEnvironmentPanel(), BorderLayout.CENTER);
			this.usePanel.validate();
			this.usePanel.repaint();
		}
		
	}
	
	/**
	 * Destroys the visualization GUI.
	 */
	private void destroyVisualizationGUI() {
		
		this.netModel = null;
		if (this.myGraphEnvironmentController!=null) {
			this.myGraphEnvironmentController.getEnvironmentPanel().setVisible(false);
			this.myGraphEnvironmentController.getEnvironmentPanel().dispose();
			this.myGraphEnvironmentController = null;
		}
		if (this.useFrame != null) {
			this.useFrame.dispose();
			this.useFrame = null;
		}
		if (this.usePanel != null) {
			this.usePanel = null;
		}
		
	}
	
	/**
	 * Gets the independent frame.
	 * @return the independent frame
	 */
	private JFrame getIndependentFrame() {

		ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource(pathImage + "AgentGUI.png"));
		Image imageAgentGUI = iconAgentGUI.getImage();

		JFrame frame = new JFrame();
		frame.setSize(1150, 640);
		frame.setTitle("DisplayAgent: " + getLocalName());
		frame.setIconImage(imageAgentGUI);		
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DisplayAgent.this.doDelete();
			}
		});
		return frame;
	}
		
	/**
	 * Grab the environment model from the simulation service.
	 * @return the environment model
	 */
	private EnvironmentModel grabEnvironmentModelFromSimulationService(){
		EnvironmentModel envModel = null;
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			envModel = simHelper.getEnvironmentModel();
		} catch (ServiceException e) {
			System.err.println(getLocalName() +  " - Error: Could not retrieve SimulationServiceHelper, shutting down!");
			this.doDelete();
		}
		return envModel;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	protected void onEnvironmentStimulus() {
		
		boolean runStimuliRemover = false;
		
		// --- Add the new NetorkModel to the Vector of not yet displayed NetworkModel's ----------
		this.getStimuliOfNetworkModel().add(((NetworkModel) myEnvironmentModel.getDisplayEnvironment()).getCopy());

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
					this.netModel = this.getStimuliOfNetworkModel().get(0);
					this.getStimuliOfNetworkModel().remove(0);
					this.myGraphEnvironmentController.setEnvironmentModel(netModel);
					
//					SwingUtilities.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							
//						}
//					});
					
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
	 * Returns the Vector of NetworkModels that arrived this agent by an EnvironmentStimulus.
	 * @return the stimuli of network model
	 */
	private synchronized Vector<NetworkModel> getStimuliOfNetworkModel() {
		if (this.stimuliOfNetworkModel==null) {
			this.stimuliOfNetworkModel = new Vector<NetworkModel>();
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
			this.netModel.setDirectionsOfNetworkComponent(netComp);
			
		}
		myGraphEnvironmentController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint));

		return notification;
	}
	
}
