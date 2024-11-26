package de.enflexit.awb.simulation.environment.time;

import javax.swing.JPanel;

import de.enflexit.common.Observable;
import de.enflexit.common.Observer;

/**
 * The Class JPanel4TimeModelConfiguration has to be extended in order to
 * provide a specific JPanle for the configuration of a TimeModel.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class JPanel4TimeModelConfiguration extends JPanel implements Observer {

	private static final long serialVersionUID = 4966720402773236025L;

	protected Project currProject;
	private TimeModelController timeModelController;
	
	private boolean isDisabledObserver;
	
	/**
	 * Instantiates a new display panel for the configuration of the current time model.
	 *
	 * @param project the current project
	 * @param timeModelController the TimeModelController that is part of the specified project
	 */
	public JPanel4TimeModelConfiguration(Project project, TimeModelController timeModelController) {
		this.currProject = project;
		this.timeModelController = timeModelController;
		this.addObserver();
		// --- Set the time model to display --------------
		TimeModelController tmController = this.getTimeModelController();
		if (tmController!=null) {
			this.setTimeModel(tmController.getTimeModel());
		}
	}

	/**
	 * Adds the observer to the current project.
	 */
	private void addObserver() {
		if (this.currProject!=null) {
			this.currProject.addObserver(this);
		}
	}
	/**
	 * Deletes the observer from the current project.
	 */
	public void deleteObserver() {
		if (this.currProject!=null) {
			this.currProject.deleteObserver(this);
		}
	}
	
	/**
	 * Returns the currents project TimeModelController that holds the current {@link TimeModel}.
	 * @return the time model controller
	 */
	protected TimeModelController getTimeModelController() {
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
	protected void saveTimeModelToSimulationSetup() {
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
