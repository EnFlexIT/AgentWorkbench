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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.controller.BasicGraphGUI;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentControllerGUI;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * This agent can be used in order to display the current network model
 * during a running simulation. It is not necessary that this agent is
 * used within the application window - it is also possible to just start 
 * this agent anywhere else.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DisplayAgent extends SimulationAgent {

	private static final long serialVersionUID = -766291673903767678L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private JFrame useFrame = null;
	private JPanel usePanel = null;
	
	private GraphEnvironmentControllerGUI myGraphDisplay = null;
	private NetworkModel netModel = null;
	
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
			
			EnvironmentModel envModel = this.grabEnvironmentModelFromSimulationService(); 
			this.netModel = (NetworkModel) envModel.getDisplayEnvironment();

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
		super.takeDown();
		this.destroyVisualizationGUI();
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
		this.netModel = (NetworkModel) envModel.getAbstractEnvironment();
		this.buildVisualizationGUI();
		super.afterMove();
	}
	
	/**
	 * Builds the visualization GUI.
	 */
	private void buildVisualizationGUI() {
		
		// --- Build the new Controller GUI ---------------
		GraphEnvironmentController myGraphEnvController = new GraphEnvironmentController();
		myGraphEnvController.setEnvironmentModel(this.netModel);
		this.myGraphDisplay = (GraphEnvironmentControllerGUI) myGraphEnvController.getEnvironmentPanel();
		
		BasicGraphGUI basicGraphGUI = new BasicGraphGUI(myGraphEnvController);
		basicGraphGUI.addObserver(this.myGraphDisplay);
		if (this.netModel!=null) {
			basicGraphGUI.setGraph(this.netModel.getGraph());	
		}
		this.myGraphDisplay.setGraphGUI(basicGraphGUI);
				
		if (this.useFrame!=null) {
			this.useFrame.setContentPane(this.myGraphDisplay);
			this.useFrame.pack();
			this.useFrame.setVisible(true);
			
		} else {
			this.usePanel.add(this.myGraphDisplay, BorderLayout.CENTER);
			this.usePanel.validate();
			this.usePanel.repaint();
			
		}
	}
	
	/**
	 * Destroys the visualization GUI.
	 */
	private void destroyVisualizationGUI() {
		
		this.netModel = null;
		if (this.myGraphDisplay!=null) {
			this.myGraphDisplay.setVisible(false);
			this.myGraphDisplay=null;
		}
		if (this.useFrame != null) {
			this.useFrame.dispose();
			this.useFrame = null;
		}
		if (this.usePanel != null) {
			this.usePanel.removeAll();
			this.usePanel.repaint();
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
		
		
	}
	
}
