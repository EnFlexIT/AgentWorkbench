package de.enflexit.awb.remoteControl;

import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.jade.Platform.SystemAgent;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.time.TimeModelDateBased;
import agentgui.simulationService.time.TimeModelDiscrete;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;

/**
 * This class implements the basic control functions, that should be provided by a remote control implementation.
 * To implement a remote control, create a subclass that receives and interprets the incoming command messages
 * from your chosen communication protocol, and calls the corresponding method from this class to execute them.    
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AwbRemoteControl implements ApplicationListener, Observer {
	
	private AwbState awbState;
	
	private boolean loadSuccess;

	/**
	 * Instantiates a new awb remote control.
	 */
	public AwbRemoteControl() {
		Application.addApplicationListener(this);
		this.setAwbState(AwbState.AWB_READY);
	}

	/**
	 * Loads the project with the specified name.
	 * @param projectName the project name
	 * @return true, if successful
	 */
	public boolean loadProject(String projectName) {
		
		int projectIndex = Application.getProjectsLoaded().getIndexByFolderName(projectName);
		
		if (projectIndex==-1) {
			
			Object syncObject = new Object();
			
			// --- Not loaded yet -> load ---------------------------
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					Project project = Application.getProjectsLoaded().add(projectName);
					if (project!=null) {
						project.addObserver(AwbRemoteControl.this);
						loadSuccess = true;
					} else {
						loadSuccess = false;
					}
					synchronized (syncObject) {
						syncObject.notify();
					}
					
				}
			});
			
			try {
				synchronized (syncObject) {
					syncObject.wait();
				}
				return this.loadSuccess;
			} catch (InterruptedException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Interrupted while waiting for project to be loaded!");
				e.printStackTrace();
				return false;
			}
			
			
		} else {
			// --- Already loaded -> set the focus to the project ---
			Project project = Application.getProjectsLoaded().get(projectIndex);
			if (Application.getProjectFocused()!=project) {
				project.setFocus(false);
			}
			return true;
		}
	}
	
	/**
	 * Selects the simulation setup with the specified name.
	 * @param setupName the setup name
	 * @return true, if successful
	 */
	public boolean selectSetup(String setupName) {
		if (Application.getProjectFocused().getSimulationSetupCurrent().equals(setupName)) {
			// --- Already selected -> nothing to do ----------------
			return true;
		} else {
			// --- Not selected yet -> try to select ----------------
			return Application.getProjectFocused().getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, setupName, false);
		}
	}
	
	/**
	 * Applies the provided {@link AwbSimulationSettings} to the currently selected setup.
	 * @param simulationSettings the simulation settings
	 * @return true, if successful
	 */
	public boolean configureSimulation(AwbSimulationSettings simulationSettings) {
		
		TimeModelController timeModelController = Application.getProjectFocused().getTimeModelController();
		
		if (timeModelController.getTimeModel() instanceof TimeModelDateBased) {
			TimeModelDateBased tmdb = (TimeModelDateBased) timeModelController.getTimeModel();
			tmdb.setTimeStart(simulationSettings.getSimulationStartTime());
			tmdb.setTimeStop(simulationSettings.getSimulationEndTime());
			
			if (tmdb instanceof TimeModelDiscrete) {
				((TimeModelDiscrete)tmdb).setStep(simulationSettings.getSimulationStepSeconds()*1000);
			}
			
			timeModelController.saveTimeModelToSimulationSetup();
			
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Starts the multi agent system.
	 * @return true, if successful
	 */
	public boolean startMultiAgentSystem() {
		Object[] startWith = new Object[1];
		startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
		Application.getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith, true);
		Application.getMainWindow().setEnableSimStart(false);
		return true;
	}
	
	/**
	 * Stops the multi agent system.
	 * @return true, if successful
	 */
	public boolean stopMultiAgentSystem() {
		Application.getJadePlatform().stop(true);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.application.ApplicationListener#onApplicationEvent(agentgui.core.application.ApplicationListener.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent ae) {
		
		if (ae.getApplicationEvent().equals(ApplicationEvent.PROJECT_LOADED)) {
			Project project = (Project) ae.getEventObject();
			if (project!=null) {
				project.addObserver(this);
				this.setAwbState(AwbState.PROJECT_LOADED);
			}
		} else if (ae.getApplicationEvent().equals(ApplicationEvent.JADE_START)) {
			this.setAwbState(AwbState.MAS_STARTED);
		} else if (ae.getApplicationEvent().equals(ApplicationEvent.JADE_STOP)) {
			this.setAwbState(AwbState.MAS_STOPPED);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.Observer#update(de.enflexit.common.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SimulationSetupNotification) {
			SimulationSetupNotification setupNotification = (SimulationSetupNotification) arg;
			if (setupNotification.getUpdateReason()==SimNoteReason.SIMULATION_SETUP_DETAILS_LOADED) {
				this.setAwbState(AwbState.SETUP_READY);
			}
		}
	}
	
	/**
	 * Gets the current awb state.
	 * @return the awb state
	 */
	public AwbState getAwbState() {
		return awbState;
	}

	/**
	 * Sets the current awb state.
	 * @param awbState the new awb state
	 */
	public void setAwbState(AwbState awbState) {
		this.awbState = awbState;
	}	
}
