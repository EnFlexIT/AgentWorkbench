package de.enflexit.awb.core.ui;

import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.common.Observer;

/**
 * The Interface AwbTimeModelConfigurationWidget.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbTimeModelConfigurationWidget extends Observer {

	
	/**
	 * Sets the current project to the time model configuration.
	 * @param project the new project
	 */
	public void setProject(Project project);
	
	/**
	 * Adds the observer to the current project.
	 */
	public void addAsProjectObserver();
	/**
	 * Deletes the observer from the current project.
	 */
	public void deleteAsProjectObserver();
	
	
	/**
	 * Sets the current TimeModelController.
	 * @param timeModelController the new time model controller
	 */
	public void setTimeModelController(TimeModelController timeModelController);
	/**
	 * Returns the currents project TimeModelController that holds the current {@link TimeModel}.
	 * @return the time model controller
	 */
	public TimeModelController getTimeModelController();
	
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public void setTimeModel(TimeModel timeModel);
	
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public TimeModel getTimeModel();

	/**
	 * Save the current TimeModel to the simulation setup.
	 */
	public void saveTimeModelToSimulationSetup();

	
}
