package de.enflexit.awb.remoteControl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener;
import agentgui.core.jade.Platform.SystemAgent;
import agentgui.core.jade.PlatformStateInformation.PlatformState;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.simulationService.agents.LoadExecutionAgent;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;

/**
 * This class implements the basic control functions, that should be provided by a remote control implementation.
 * To implement a remote control, create a subclass that receives and interprets the incoming command messages
 * from your chosen communication protocol, and calls the corresponding method from this class to execute them.    
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AwbRemoteControl implements ApplicationListener, PropertyChangeListener, Observer {
	
	/**
	 * Loads the project with the specified name.
	 * @param projectName the project name
	 * @return true, if successful
	 */
	public boolean loadProject(String projectName) {
		
		int projectIndex = Application.getProjectsLoaded().getIndexByFolderName(projectName);
		
		if (projectIndex==-1) {
			// --- Not loaded yet -> load ---------------------------
			Project project = Application.getProjectsLoaded().add(projectName);
			if (project!=null) {
				project.addObserver(this);
			}
			return project!=null;
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
		return false;
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
	
	/**
	 * Triggers the next step of a discrete simulation.
	 */
	public void discreteSimulationNextStep() {
		
	}

	/* (non-Javadoc)
	 * @see agentgui.core.application.ApplicationListener#onApplicationEvent(agentgui.core.application.ApplicationListener.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent ae) {
		if (ae.getApplicationEvent()==ApplicationEvent.PROJECT_LOADED) {
			Project project = (Project) ae.getEventObject();
			if (project!=null) {
				project.addObserver(this);
				this.projectLoaded(project.getProjectName());
			}
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
				this.setupReady(Application.getProjectFocused().getSimulationSetupCurrent());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
//		System.out.println("[" + this.getClass().getSimpleName() + "] Received property change event " + pce.getPropertyName());
		if (pce.getPropertyName().equals("PlatformState")) {
			PlatformState newState = (PlatformState) pce.getNewValue();
			if (newState==PlatformState.RunningMAS) {
				
			}
		}
	}
	
	/**
	 * This method is called when the simulation is ready. Override it if you want to react on this event.
	 */
	public abstract void simulationReady();

	/**
	 * This method is called when a project was loaded. Override it if you want to react on this event.
	 * @param projectName the project name
	 */
	public abstract void projectLoaded(String projectName);
	
	/**
	 * This method is called when a selected setup is ready. Override it if you want to react on this event.
	 * @param setupName the setup name
	 */
	public abstract void setupReady(String setupName);

}
