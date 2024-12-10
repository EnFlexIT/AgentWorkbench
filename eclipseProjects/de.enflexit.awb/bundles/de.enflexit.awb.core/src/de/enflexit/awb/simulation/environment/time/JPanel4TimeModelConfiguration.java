package de.enflexit.awb.simulation.environment.time;

import javax.swing.JPanel;

import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetup.SetupChangeEvent;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification;
import de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget;
import de.enflexit.common.Observable;

/**
 * The Class JPanel4TimeModelConfiguration has to be extended in order to
 * provide a specific JPanle for the configuration of a TimeModel.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class JPanel4TimeModelConfiguration extends JPanel implements AwbTimeModelConfigurationWidget {

	private static final long serialVersionUID = 4966720402773236025L;

	protected Project currProject;
	private TimeModelController timeModelController;
	
	private boolean isDisabledObserver;
	

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget#setProject(de.enflexit.awb.core.project.Project)
	 */
	@Override
	public void setProject(Project project) {
		this.currProject = project;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget#addAsProjectObserver()
	 */
	@Override
	public void addAsProjectObserver() {
		if (this.currProject!=null) {
			this.currProject.addObserver(this);
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget#deleteAsProjectObserver()
	 */
	@Override
	public void deleteAsProjectObserver() {
		if (this.currProject!=null) {
			this.currProject.deleteObserver(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget#setTimeModelController(de.enflexit.awb.core.environment.time.TimeModelController)
	 */
	@Override
	public void setTimeModelController(TimeModelController timeModelController) {
		this.timeModelController = timeModelController;
		if (this.timeModelController!=null) {
			// --- Set the time model to display ----------
			this.setTimeModel(this.timeModelController.getTimeModel());
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget#getTimeModelController()
	 */
	@Override
	public TimeModelController getTimeModelController() {
		if (timeModelController==null && this.currProject!=null) {
			timeModelController = this.currProject.getTimeModelController();
		}
		return timeModelController;
	}
	
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public abstract void setTimeModel(TimeModel timeModel);
	
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public abstract TimeModel getTimeModel();
	
	/**
	 * Internally sets the time model.
	 * @param timeModel the new time model internal
	 */
	private void setTimeModelInternal(TimeModel timeModel) {
		this.isDisabledObserver = true;
		this.setTimeModel(timeModel);
		this.isDisabledObserver = false;
	}
	/**
	 * Save the current TimeModel to the simulation setup.
	 */
	public void saveTimeModelToSimulationSetup() {
		this.isDisabledObserver = true;
		this.getTimeModelController().setTimeModel(this.getTimeModel());
		this.isDisabledObserver = false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (this.isDisabledObserver==true) return;
		
		if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_SAVED:
				break;
			case SIMULATION_SETUP_PREPARE_SAVING:
				this.saveTimeModelToSimulationSetup();
				break;
				
			default:
				this.setTimeModelInternal(this.getTimeModelController().getTimeModel());
				break;
			}
			
		} else if (updateObject instanceof SetupChangeEvent) {
			// --- Change inside the simulation setup ---------------
			SetupChangeEvent setupChangeEvent = (SetupChangeEvent) updateObject;
			switch (setupChangeEvent) {
			case TimeModelSettings:
				this.setTimeModelInternal(this.getTimeModelController().getTimeModel());
				break;

			default:
				break;
			} 
		}
		
		
	}
	
}
