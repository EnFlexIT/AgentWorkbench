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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import agentgui.envModel.graph.controller.BasicGraphGUI;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentControllerGUI;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;

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

	private JFrame useFrame = null;
	private JPanel usePanel = null;
	
	private GraphEnvironmentControllerGUI myGraphDisplay = null;
	
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		NetworkModel netModel = null;
		
		Object[] startArgs = getArguments();
		if (startArgs == null || startArgs.length == 0) {
			// --- started in a normal way ----------------
			useFrame = getIndependentFrame();
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				Object envObject = simHelper.getEnvironmentModel();
				netModel = (NetworkModel) envObject;
				
			} catch (ServiceException e) {
				System.err.println(getLocalName() +  " - Error: Could not retrieve EnvironmentProviderHelper, shutting down!");
				this.doDelete();
			}

		} else {
			// --- started from Agent.GUI -----------------
			usePanel = (JPanel) startArgs[0];
			
			GraphEnvironmentControllerGUI startEnvPanel = (GraphEnvironmentControllerGUI) startArgs[1];
			GraphEnvironmentController startController = (GraphEnvironmentController) startEnvPanel.getEnvironmentController();
			netModel = (NetworkModel) startController.getEnvironmentModelCopy();
			
		}

		// --- Build the new Controller GUI ---------------
		GraphEnvironmentController myGraphEnvController = new GraphEnvironmentController();
		myGraphEnvController.setEnvironmentModel(netModel);
		myGraphDisplay = new GraphEnvironmentControllerGUI(myGraphEnvController);
		
		BasicGraphGUI basicGraphGUI = new BasicGraphGUI(myGraphEnvController);
		basicGraphGUI.addObserver(myGraphDisplay);
		if (netModel!=null) {
			basicGraphGUI.setGraph(netModel.getGraph());	
		}
		myGraphDisplay.setGraphGUI(basicGraphGUI);
				
		if (useFrame!=null) {
			useFrame.setContentPane(myGraphDisplay);
			useFrame.setSize(800, 600);
			useFrame.setVisible(true);
			useFrame.pack();
		} else {
			usePanel.add(myGraphDisplay, BorderLayout.CENTER);
			usePanel.validate();
			usePanel.repaint();
		}
		
	}
	
	@Override
	protected void onEnvironmentStimulus() {
		
		
		
		
	}
	
	
	/**
	 * Gets the independent frame.
	 * @return the independent frame
	 */
	private JFrame getIndependentFrame() {

		JFrame frame = new JFrame();
		frame.setTitle("DisplayAgent " + getLocalName() + " - GUI");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DisplayAgent.this.doDelete();
			}
		});
		return frame;
	}
		
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		if (useFrame != null) {
			useFrame.dispose();
		}
		if (usePanel != null) {
			usePanel.removeAll();
			usePanel.repaint();
			myGraphDisplay=null;
		}
	}
		
}
