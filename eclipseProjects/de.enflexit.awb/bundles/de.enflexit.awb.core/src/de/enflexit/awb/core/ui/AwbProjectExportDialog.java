package de.enflexit.awb.core.ui;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.project.transfer.ProjectExportSettings;

/**
 * The Interface AwbProjectExportDialog.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbProjectExportDialog {

	/**
	 * Sets the project.
	 * @param project the new project
	 */
	public void setProject(Project project);
	
	/**
	 * Sets the project export controller.
	 * @param projectExportController the new project export controller
	 */
	public void setProjectExportController(ProjectExportController projectExportController);
	
	/**
	 * Sets it the installation package configuration is allowed or not.
	 * @param allowConfiguration the new allow installation package configuration
	 */
	public void setAllowInstallationPackageConfiguration(boolean allowConfiguration);

	/**
	 * Has to return the export settings to be used for the actual Export.
	 * @return the export settings
	 */
	public ProjectExportSettings getExportSettings();

	/**
	 * Sets the visible.
	 * @param setVisible the new visible
	 */
	public void setVisible(boolean setVisible);
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled();

	/**
	 * Dispose.
	 */
	public void dispose();

	
	
}