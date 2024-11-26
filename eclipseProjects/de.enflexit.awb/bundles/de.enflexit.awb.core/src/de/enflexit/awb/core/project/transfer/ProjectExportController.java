package de.enflexit.awb.core.project.transfer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import de.enflexit.awb.core.project.Project;

/**
 * This interface can be implemented to provide specialized project export controllers.
 * It is highly recommended not to implement this interface yourself, but extend the
 * provided class {@link DefaultProjectExportController} instead, which provides basic
 * project export functionality and several callback functions to add own code. 
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ProjectExportController {
	
	/**
	 * Gets the project export settings.
	 *
	 * @param project the current project to work on
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings(Project project);
	
	/**
	 * Method to export the specified project.
	 * 
	 * @param project the project to be exported
	 * @param projectExportSettings the project export settings
	 */
	public void exportProject(Project project, ProjectExportSettings projectExportSettings);
	
	/**
	 * Exports the current project using the provided {@link ProjectExportSettings}.
	 * @param project the project to be exported
	 * @param exportSettings The {@link ProjectExportSettings}
	 * @param showUserDialogs specifies if user dialogs are shown
	 * @param useConcurrentThread specifies if the project should be exported in a concurrent thread
	 */
	public void exportProject(Project project, ProjectExportSettings exportSettings, boolean showUserDialogs, boolean useConcurrentThread);
	
	/**
	 * Checks if the export was successfully finished
	 * @return true if successfully finished
	 */
	public boolean isExportSuccessful();

	/**
	 * Sets the message for an export success.
	 * @param messageSuccess the new message success
	 */
	public void setMessageSuccess(String messageSuccess);

	/**
	 * Sets the message for export failures.
	 * @param messageFailure the new message failure
	 */
	public void setMessageFailure(String messageFailure);
	
	/**
	 * This method can be used to specify additional setup-related files, that should 
	 * be considered when determining which files to include when exporting projects.  
	 * @param setupName the setup name
	 * @return the additional setup files
	 */
	public ArrayList<File> getAdditionalSetupFiles(String setupName);
	
	/**
	 * This method can be used to provide a list of files and folders that should
	 * be excluded from the export by default. 
	 * @return the default exclude files
	 */
	public ArrayList<Path> getDefaultExcludeList();
}
