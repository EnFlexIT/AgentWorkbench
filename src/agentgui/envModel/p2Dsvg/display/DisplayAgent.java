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
package agentgui.envModel.p2Dsvg.display;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.p2Dsvg.behaviours.MoveToPointBehaviour;
import agentgui.envModel.p2Dsvg.controller.Physical2DEnvironmentController;
import agentgui.envModel.p2Dsvg.controller.Physical2DEnvironmentControllerGUI;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;

/**
 * This type of agent controls a visualization of a Physical2DEnvironment
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 * 
 */
@SuppressWarnings("deprecation")
public class DisplayAgent extends Agent {

	private static final long serialVersionUID = 8613715346940866246L;

	private static final int PERIOD = 50;
	/**
	 * The DisplayAgent's GUI
	 */
	private DisplayAgentGUI myGUI = null;

	private JFrame useFrame = null;

	private JPanel usePanel = null;
	private Document svgDoc = null;
	private Physical2DEnvironment environment = null;
	private EnvironmentProviderHelper envHelper = null;
	private int lastMaximumValue = -2;
	private int lastCurrentValue = -1;
	private int sameTransactionSizeCounter = 0;
	private int sameVisualisationCounter = 0;
	private int counter = 0;
	private int addValue = 1;
	private long initialTimeStep = 0;
	private boolean play = true;

	public void setup() {

		int use4Visualization = 0;

		Object[] startArgs = getArguments();
		if (startArgs == null || startArgs.length == 0) {
			// --- started in a normal way ----------------
			use4Visualization = 1;
			useFrame = getIndependentFrame();
			try {
				EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				svgDoc = helper.getSVGDoc();
				environment = helper.getEnvironment();
			} catch (ServiceException e) {
				System.err.println(getLocalName() +  " - Error: Could not retrieve EnvironmentProviderHelper, shutting down!");
				doDelete();
			}

		} else {
			// --- started from Agent.GUI -----------------
			use4Visualization = 2;
			usePanel = (JPanel) startArgs[0];

			Physical2DEnvironmentControllerGUI envPanel = (Physical2DEnvironmentControllerGUI) startArgs[1];
			Physical2DEnvironmentController p2DCont = (Physical2DEnvironmentController) envPanel.getEnvironmentController();
			environment = p2DCont.getEnvironmentModelCopy();
			svgDoc = p2DCont.getSvgDocCopy();
			
			// --- Set the environment globally -----------
			try {
				EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				helper.setEnvironment(environment);
				helper.setSVGDoc(svgDoc);
				
			} catch (ServiceException e) {
				System.err.println(getLocalName() +  " - Error: Could not set EnvironmentProviderHelper, shutting down!");
			}
		}

		// --- initiate the GUI - element -----------------
		this.myGUI = new DisplayAgentGUI(svgDoc, environment);

		// --- place it on right place --------------------
		switch (use4Visualization) {
		case 1:
			useFrame.setContentPane(myGUI);
			useFrame.setSize(800, 600);
			useFrame.setVisible(true);
			useFrame.pack();
			break;

		case 2:
			usePanel.add(myGUI, BorderLayout.CENTER);
			usePanel.repaint();
			break;
		}

		try {
			myGUI.jPanelSimuInfos.setVisible(true);
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
			EnvironmentModel envModel = simHelper.getEnvironmentModel();
			while (envModel == null && MoveToPointBehaviour.IS_USED == false) {
				Thread.sleep(100);
				envModel = simHelper.getEnvironmentModel();
			}
			if (!MoveToPointBehaviour.IS_USED) {
				TimeModelDiscrete model = (TimeModelDiscrete) envModel
						.getTimeModel();
				initialTimeStep = model.getStep();
				String unit = Language.translate("Sekunden");
				myGUI.jLabelSpeedFactor.setText((initialTimeStep / 1000.0)
						+ " " + unit);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		myGUI.jButtonPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				play = !play;
				envHelper.setRunning(play);
				String pathImage = Application.RunInfo.PathImageIntern();
				Icon icon = null;
				if (play) {
					icon = new ImageIcon(getClass().getResource(
							pathImage + "MBLoadPlay.png"));
				} else {
					icon = new ImageIcon(getClass().getResource(
							pathImage + "MBLoadPause.png"));
				}
				myGUI.jButtonPlay.setIcon(icon);
				sameVisualisationCounter = 0;

			}
		});

		// the user change the status of the simulation
		myGUI.jSliderTime.addChangeListener(new javax.swing.event.ChangeListener() {

					public void stateChanged(javax.swing.event.ChangeEvent e) {
						counter = myGUI.jSliderTime.getValue();
						sameVisualisationCounter = 0;
						if (counter == -1) {
							counter = 0;
						}

						try {
							if (envHelper != null) {

								if (initialTimeStep == 0) {
									SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
									TimeModelDiscrete model = (TimeModelDiscrete) simHelper.getEnvironmentModel().getTimeModel();
									initialTimeStep = model.getStep();
								}

								String unit = Language.translate("Sekunden");
								myGUI.jLabelTimeDisplay.setText((initialTimeStep * myGUI.jSliderTime.getValue())/ 1000.0 + " " + unit);

							} else {
								envHelper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
		
		// User adjusted the simulation time steps
		myGUI.jSliderVisualation.addChangeListener(new javax.swing.event.ChangeListener() {
					SimulationServiceHelper simHelper = null;

					public void stateChanged(javax.swing.event.ChangeEvent e) {
						// Call simu service and tell him that something is
						// changed!
						try {
							if (simHelper == null) {
								simHelper = (SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
								TimeModelDiscrete model = (TimeModelDiscrete) simHelper
										.getEnvironmentModel().getTimeModel();
								initialTimeStep = model.getStep();
							}

							String unit = Language.translate("Sekunden");
							myGUI.jLabelSpeedFactor.setText((((myGUI.jSliderVisualation
									.getValue() * initialTimeStep)) / 1000.0)
									+ " " + unit);
							// TimeModelDiscrete model=(TimeModelDiscrete)
							// simHelper.getEnvironmentModel().getTimeModel();
							// model.setStep(initialTimeStep*myGUI.jSliderVisualation.getValue());
							addValue = myGUI.jSliderVisualation.getValue();

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

		addBehaviour(new UpdateSVGBehaviour());

	}

	
	public void takeDown() {
		if (useFrame != null) {
			useFrame.dispose();
		}
		if (usePanel != null) {
			myGUI = null;
			usePanel.removeAll();
			usePanel.repaint();
		}
	}

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

	/**
	 * 
	 * @author 
	 */
	private class UpdateSVGBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -4205541330627054644L;
		private EnvironmentProviderHelper helper;

		public UpdateSVGBehaviour() {
			super(DisplayAgent.this, PERIOD);

			try {
				helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			} catch (ServiceException e) {
				System.err
						.println(getLocalName()
								+ " - EnvironmentProviderHelper not found, shutting down");
				doDelete();
			}
		}

		public HashSet<Physical2DObject> fordwardToVisualation(
				HashMap<AID, PositionUpdate> pos) {

			HashSet<Physical2DObject> movingObjects = envHelper
					.getCurrentlyMovingObjects();

			if (movingObjects.size() > 0) {

			}
			// Clear map
			movingObjects.clear();
			Set<AID> keys = pos.keySet();
			Iterator<AID> it = keys.iterator();
			while (it.hasNext()) {
				AID aid = it.next();
				Physical2DObject obj = envHelper.getObject(aid.getLocalName());
				obj.setPosition(pos.get(aid).getNewPosition());
				// System.out.println("DisplayAgent:"+aid.getLocalName()
				// +","+pos.get(aid).getNewPosition().getXPos()+","+pos.get(aid).getNewPosition().getYPos());
				movingObjects.add(obj);
			}
			// System.out.println("----");
			return movingObjects;
		}

		@Override
		protected void onTick() {

			try {

				if (MoveToPointBehaviour.IS_USED) {

					myGUI.updatePositions(helper.getCurrentlyMovingObjects());
					return;

				}
				int size = -1;
				if (sameTransactionSizeCounter == 20) {
					size = lastMaximumValue;

				} else {

					size = helper.getTransactionSize();
					if (lastMaximumValue != size) {

						sameTransactionSizeCounter = 0;
						myGUI.setMaximum(size);
					} else {
						sameTransactionSizeCounter++;
					}

				}

				if (size > 1) {

					if (lastCurrentValue == counter) {
						sameVisualisationCounter++;
					} else {
						sameVisualisationCounter = 0;
					}
					if (sameVisualisationCounter < 20) {
						HashSet<Physical2DObject> movingObjects = this.fordwardToVisualation(helper.getModel(counter));
						synchronized (movingObjects) {
							myGUI.updatePositions(movingObjects);
						}
						myGUI.setCurrentTimePos(counter);
						if (play) {
							if (counter + addValue < size) {
								counter = counter + addValue;
							}
						}
						lastMaximumValue = size;
						lastCurrentValue = counter;
					}
					
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
