package de.enflexit.awb.baseUI.agents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentPanel;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.simulation.agents.AbstractDisplayAgent;
import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelBaseExecutionElements;
import de.enflexit.awb.simulation.environment.time.TimeModelContinuous;
import de.enflexit.awb.simulation.environment.time.TimeModelContinuousExecutionElements;

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
public abstract class AbstractSwingDisplayAgent extends AbstractDisplayAgent<JPanel> {

	private static final long serialVersionUID = -6499021588257662334L;
	
	private JFrame jFrameStandalone;

	/** The display elements for the current TimeModel */
	private JToolBar jToolBar4TimeModel;
	private TimeModelBaseExecutionElements jToolBarElements4TimeModel;

	
	/**
	 * Instantiates a new visualization agent for an EnvironmentModel.
	 */
	public AbstractSwingDisplayAgent() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.AbstractDisplayAgent#buildEnvironmentModelVisualization()
	 */
	@Override
	public void buildEnvironmentModelVisualization() {
		// --- Build the new Controller GUI ---------------
		if (this.isAgentWorkbenchEmbedded()==true) {
			this.visualizationContainer.add(this.getEnvironmentController().getOrCreateEnvironmentPanel(), BorderLayout.CENTER);
			this.visualizationContainer.validate();
			this.visualizationContainer.repaint();
		} else {
			this.getJFrameStandalone().getContentPane().add(this.getEnvironmentController().getOrCreateEnvironmentPanel(), BorderLayout.CENTER);
			this.getJFrameStandalone().validate();
			this.getJFrameStandalone().setVisible(true);
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.agents.AbstractDisplayAgent#destroyEnvironmentModelVisualization()
	 */
	@Override
	public void destroyEnvironmentModelVisualization() {
		
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
		this.visualizationContainer = null;
		this.jFrameStandalone = null;
		
		this.setAgentWorkbenchEmbedded(false);
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
	 * Sets the time model display.
	 * @param timeModel the new time model display
	 */
	public void setTimeModelDisplay(TimeModel timeModel) {
		
		if (this.jToolBar4TimeModel==null) {
			if (this.isAgentWorkbenchEmbedded()==false) {
				this.jToolBar4TimeModel = getJToolBarNew();
				this.getJFrameStandalone().getContentPane().add(this.jToolBar4TimeModel, BorderLayout.NORTH);
			} else {
				@SuppressWarnings("unchecked")
				AwbMainWindow<JMenu, JMenuItem, JToolBar, JComponent> mainWindow = (AwbMainWindow<JMenu, JMenuItem, JToolBar, JComponent>) Application.getMainWindow();
				this.jToolBar4TimeModel = mainWindow.getApplicationToolbar();
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
