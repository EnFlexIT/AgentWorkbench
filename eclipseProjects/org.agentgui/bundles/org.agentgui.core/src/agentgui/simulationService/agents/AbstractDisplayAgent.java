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
package agentgui.simulationService.agents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelBaseExecutionElements;
import agentgui.simulationService.time.TimeModelContinuous;
import agentgui.simulationService.time.TimeModelContinuousExecutionElements;
import jade.core.ServiceException;

/**
 * The Class VisualisationAgent can be used in order to build agents
 * that are passively observing environment changes that have to be
 * displayed by an extended VisualisationAgent.<br>
 * If the SimulationService is running, the VisualisationAgent will 
 * register there as a displaying agent. 
 * 
 * @see SimulationAgent
 * @see EnvironmentModel
 * @see EnvironmentController
 * @see EnvironmentPanel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractDisplayAgent extends SimulationAgent {

	private static final long serialVersionUID = -6499021588257662334L;
	
	protected boolean isPausedSimulation = true;
	private boolean isAgentGuiEmbedded = false;
	private EnvironmentController myEnvironmentController;
	
	private JFrame jFrameStandalone;
	private JPanel usePanel;

	/** The display elements for the current TimeModel */
	private JToolBar jToolBar4TimeModel;
	private TimeModelBaseExecutionElements jToolBarElements4TimeModel;

	
	/**
	 * Instantiates a new visualisation agent for an EnvironmentModel.
	 */
	public AbstractDisplayAgent() {
		// --- Initialise this agent as a passive ---------
		// --- SimulationAgent (or observing agent) ------- 
		super(true);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		EnvironmentModel tmpEnvironmentModel = null;
			
		if (startArgs==null || startArgs.length==0) {
			// --- Started as independent display -------------------
			this.setAgentGuiEmbedded(false);
			// --- Get environment from SimulationService -----------
			tmpEnvironmentModel = this.getEnvironmentModelFromSimulationService();
			
		} else {
			// --- Started from Agent.GUI ---------------------------
			this.setAgentGuiEmbedded(true);
			// --- Get info from Agent.GUI configuration ------------
			this.usePanel = (JPanel) startArgs[0];
			// --- Get environment from given controller -------------
			EnvironmentController envController = (EnvironmentController) startArgs[1];
			tmpEnvironmentModel = envController.getEnvironmentModel();
			
		}
		// --- Set a copy of the EnvironmentModel to the local one --
		if (tmpEnvironmentModel!=null) {
			this.myEnvironmentModel = tmpEnvironmentModel.getCopy();
			this.getEnvironmentController().setEnvironmentModel(this.myEnvironmentModel);
		}
		
		// --- Build the visual components --------------------------
		this.buildVisualizationGUI();
		// --- Register as DisplayAgent at the SimulationService ----
		this.registerAsDisplayAgent();
		// --- Display the current TimeModel ------------------------
		this.displayTimeModel();
	}
	
	/**
	 * Returns a new environment controller that depends on the actual EnvironmentModel.<br> 
	 * As Example: In case of the 'Graph and Network Environment' a new 
	 * {@link GraphEnvironmentController} will be created and returned.
	 * @return the new EnvironmentController
	 */
	protected abstract EnvironmentController createNewEnvironmentController();
	
	/**
	 * Sets the current EnvironmentController of the agent. In case that the EnvironmentController 
	 * is not null, in turn this agent will be set as the controlling instance of the EnvironmentController.
	 * 
	 * @see EnvironmentController#setDisplayAgent(AbstractDisplayAgent)
	 * 
	 * @param newEnvironmentController the new environment controller
	 */
	protected void setEnvironmentController(EnvironmentController newEnvironmentController) {
		this.myEnvironmentController = newEnvironmentController;
		if (this.myEnvironmentController!=null) {
			this.myEnvironmentController.setDisplayAgent(this);	
		}
	}
	/**
	 * Returns the current EnvironmentController. In case that the EnvrionmentController is null, this
	 * method will invoke {@link #createNewEnvironmentController()} and will set this agent as the 
	 * controlling instance of the EnvironmentController.
	 * 
	 * @see #createNewEnvironmentController()
	 * @see EnvironmentController#setDisplayAgent(AbstractDisplayAgent)
	 * 
	 * @return the environment controller
	 */
	protected EnvironmentController getEnvironmentController() {
		if (this.myEnvironmentController==null) {
			this.myEnvironmentController = this.createNewEnvironmentController();
			if (this.myEnvironmentController!=null) {
				this.myEnvironmentController.setDisplayAgent(this);	
			}
		}
		return myEnvironmentController;
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.destroyVisualizationGUI();
		this.unregisterAsDisplayAgent();
		super.takeDown();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.destroyVisualizationGUI();
		this.unregisterAsDisplayAgent();
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		this.jFrameStandalone = this.getJFrameStandalone();
		this.myEnvironmentModel = this.getEnvironmentModelFromSimulationService();
		if (this.myEnvironmentModel!=null) {
			this.getEnvironmentController().setEnvironmentModel(this.myEnvironmentModel);	
		}
		this.buildVisualizationGUI();
		this.registerAsDisplayAgent();
		this.displayTimeModel();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterClone()
	 */
	@Override
	protected void afterClone() {
		this.destroyVisualizationGUI();
		super.afterClone();
		this.registerAsDisplayAgent();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.AbstractDisplayAgent#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		this.isPausedSimulation = isPauseSimulation;
		if (this.myEnvironmentModel.getTimeModel() instanceof TimeModelContinuous) {
			TimeModelContinuous tmc = (TimeModelContinuous)this.myEnvironmentModel.getTimeModel();
			tmc.setExecuted(!this.isPausedSimulation);
			this.setTimeModelDisplay(this.myEnvironmentModel.getTimeModel());	
		}
	}
	
	/**
	 * Register this agent as DisplayAgent at the SimulationServie.
	 */
	protected void registerAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentRegister(this);	
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
	/**
	 * Unregister this agent as DisplayAgent at the SimulationServie.
	 */
	protected void unregisterAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentUnregister(this);	
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Sets the Agent.GUI embedded.
	 * @param isAgentGuiEmbedded the new agent gui embeded
	 */
	public void setAgentGuiEmbedded(boolean isAgentGuiEmbedded) {
		this.isAgentGuiEmbedded = isAgentGuiEmbedded;
	}
	/**
	 * Checks if is the current display is embedded into the Agent.GUI main window.
	 * @return true, if is embedded into Agent.GUI 
	 */
	public boolean isAgentGuiEmbedded() {
		return isAgentGuiEmbedded;
	}

	/**
	 * Builds the visualization GUI.
	 */
	private void buildVisualizationGUI() {
		// --- Build the new Controller GUI ---------------
		if (this.isAgentGuiEmbedded()==true) {
			this.usePanel.add(this.getEnvironmentController().getOrCreateEnvironmentPanel(), BorderLayout.CENTER);
			this.usePanel.validate();
			this.usePanel.repaint();
		} else {
			this.getJFrameStandalone().getContentPane().add(this.getEnvironmentController().getOrCreateEnvironmentPanel(), BorderLayout.CENTER);
			this.getJFrameStandalone().validate();
			this.getJFrameStandalone().setVisible(true);
		}
	}

	/**
	 * Destroys the visualization GUI.
	 */
	private void destroyVisualizationGUI() {
		
		if (this.getEnvironmentController()!=null) {
			this.removeTimeModelDisplay();
			EnvironmentPanel envPanel = this.getEnvironmentController().getEnvironmentPanel();
			if (envPanel!=null) {
				envPanel.setVisible(false);
				envPanel.dispose();
				envPanel = null;
				this.getEnvironmentController().setEnvironmentPanel(null);
			}
			this.setEnvironmentController(null);
		}
		if (this.jFrameStandalone!=null) {
			this.jFrameStandalone.setVisible(false);
		}
		
		this.jToolBar4TimeModel = null;
		this.usePanel = null;
		this.jFrameStandalone = null;
		
		this.setAgentGuiEmbedded(false);
		
	}

	/**
	 * Gets an independent frame in order to display the current environment model.
	 * @return the independent frame
	 */
	protected JFrame getJFrameStandalone() {
		if (this.jFrameStandalone==null) {
			
			// --- Define the content pane ----------
			JPanel jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			
			// --- Define the Frame itself ----------
			this.jFrameStandalone= new JFrame();
			this.jFrameStandalone.setContentPane(jContentPane);
			this.jFrameStandalone.setSize(1150, 640);
			this.jFrameStandalone.setTitle("DisplayAgent: " + getLocalName());
			this.jFrameStandalone.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
			this.jFrameStandalone.setLocationRelativeTo(null);
			this.jFrameStandalone.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					doDelete();
				}
			});
		}
		return this.jFrameStandalone;
	}
	
	/**
	 * Displays the current TimeModel, if available.
	 */
	private void displayTimeModel() {
		if (myEnvironmentModel!=null) {
			if (myEnvironmentModel.getTimeModel()!=null) {
				if (myEnvironmentModel.getTimeModel() instanceof TimeModelContinuous) {
					// --- Set this agent to the TimeModel in order to use ----
					// --- the synchronized platform time ---------------------
					((TimeModelContinuous)myEnvironmentModel.getTimeModel()).setTimeAskingAgent(this);
				}
				this.setTimeModelDisplay(myEnvironmentModel.getTimeModel());
			}
		}
	}
	
	/**
	 * Sets the time model display.
	 */
	protected void setTimeModelDisplay(TimeModel timeModel) {
		
		if (this.jToolBar4TimeModel==null) {
			if (this.isAgentGuiEmbedded()==false) {
				this.jToolBar4TimeModel = getJToolBarNew();
				this.getJFrameStandalone().getContentPane().add(this.jToolBar4TimeModel, BorderLayout.NORTH);
			} else {
				this.jToolBar4TimeModel = Application.getMainWindow().getJToolBarApplication();
			}
		}
	
		// --- Just for a continuous time model --------------------- 
		if (timeModel instanceof TimeModelContinuous) {
			TimeModelContinuous tmc = (TimeModelContinuous) timeModel;
			tmc.setTimeAskingAgent(this);
			if (this.isPausedSimulation==tmc.isExecuted()) {
				this.isPausedSimulation=!tmc.isExecuted();
			} 
		}
		
		// --- Set the TimeModel to the display ---------------------
		this.setTimeModelDisplay(timeModel, this.jToolBar4TimeModel);	
		
	}

	/**
	 * Returns a new empty JToolBar for the display of a TimeModel 
	 * @return the new JToolBar
	 */
	private JToolBar getJToolBarNew() {
		JToolBar toolBar = new JToolBar("TimeModel-Display"); 
		toolBar.setPreferredSize(new Dimension(100, 28));
		toolBar.setFloatable(false);
		return toolBar;
	}
	
	/**
	 * This method can be used in order to display the current TimeModel.
	 *
	 * @param timeModel the current time model
	 * @param jToolBar2Display the j tool bar2 display
	 * @see TimeModel#getDisplayElements4Execution()
	 */
	private void setTimeModelDisplay(final TimeModel timeModel, JToolBar jToolBar2DisplayTimeModel) {
	
		if (timeModel==null) return;
			
		// ------------------------------------------------------
		// --- Set the JToolBar to display the TimeModel --------
		if (jToolBar2DisplayTimeModel==null && this.jToolBar4TimeModel==null) {
			// --- No place for displaying the TimeModel --------
			return;
		} else {
			// --- Set the JToolBar for the display elements ---- 
			if (this.jToolBar4TimeModel==null) {
				this.jToolBar4TimeModel = jToolBar2DisplayTimeModel;
				
			} else {
				if (jToolBar2DisplayTimeModel!=null) {
					if (this.jToolBar4TimeModel!=jToolBar2DisplayTimeModel) {
						this.removeTimeModelDisplay();
						this.jToolBar4TimeModel = jToolBar2DisplayTimeModel;
					}	
				}
				
			}
		}

		// ------------------------------------------------------
		// --- Remind the components to display the TimeModel --- 
		if (this.jToolBarElements4TimeModel==null) {
			this.jToolBarElements4TimeModel = timeModel.getDisplayElements4Execution();	
			if (this.jToolBarElements4TimeModel!=null) {
				// --- Display the elements in the toolbar ------
				this.jToolBarElements4TimeModel.addToolbarElements(this.jToolBar4TimeModel);
			}
		}

		// ------------------------------------------------------
		// --- Display the current Time Model -------------------
		if (this.jToolBarElements4TimeModel!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (jToolBarElements4TimeModel!=null) {
						jToolBarElements4TimeModel.setTimeModel(timeModel);
					}
				}
			});

		}
			
	}
	
	/**
	 * Removes the time model display.
	 */
	protected void removeTimeModelDisplay() {
		
		if (this.jToolBar4TimeModel!=null && this.jToolBarElements4TimeModel!=null) {
			if (this.jToolBarElements4TimeModel instanceof TimeModelContinuousExecutionElements) {
				// --- Stop the time displaying thread ------------------------
				((TimeModelContinuousExecutionElements)this.jToolBarElements4TimeModel).getTimeSettingThread().setClockRunning(false);
			}
			this.jToolBarElements4TimeModel.removeToolbarElements();
			this.jToolBar4TimeModel.validate();
			this.jToolBar4TimeModel.repaint();
		}
		
		if (this.getJFrameStandalone()!=null && this.jToolBar4TimeModel!=null) {
			this.getJFrameStandalone().remove(this.jToolBar4TimeModel);
		}
		
		this.jToolBarElements4TimeModel=null;
		this.jToolBar4TimeModel=null;
		
	}
	
	
}
