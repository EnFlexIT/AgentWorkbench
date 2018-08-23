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
package agentgui.core.project.transfer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import agentgui.core.project.Project;

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
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings(Project project);
	
	/**
	 * Export project.
	 * @param project the project
	 * @param projectExportSettings the project export settings
	 */
	public void exportProject(Project project, ProjectExportSettings projectExportSettings);
	
	/**
	 * Exports the current project using the provided {@link ProjectExportSettings}.
	 * @param the project to be exported
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
