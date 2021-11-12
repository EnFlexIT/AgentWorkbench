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
package agentgui.core.update;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import agentgui.core.application.Application;
import agentgui.core.config.BundleProperties;
import agentgui.core.gui.options.DirectoryOptions;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.ProjectExportController;
import agentgui.core.project.transfer.ProjectExportControllerProvider;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.RepositoryEntry;

/**
 * The Class ProjectRepositoryExport manages the export 
 * to the globally specified project repository directory.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryExport extends Thread {

	private Project currProject;
	private ProjectExportController projectExportController;
	private String repositoryLocationDirectoryPath;
	private ProjectExportSettings projectExportSettings;
	private RepositoryEntry repositoryEntry;
	private boolean showUserDialogs;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryExport(Project projectToUpdate, ProjectExportController projectExportController) {
		this.currProject = projectToUpdate;
		this.projectExportController = projectExportController;
		this.setName(this.getClass().getSimpleName() + " " + this.currProject.getProjectName());
	}
	
	/**
	 * Returns the repository location directory path.
	 * @return the repository location directory path
	 */
	public String getRepositoryLocationDirectoryPath() {
		if (repositoryLocationDirectoryPath==null || repositoryLocationDirectoryPath.isEmpty()==true) {
			// --- Try to get the globally configured location ------
			repositoryLocationDirectoryPath = Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_LOCAL_PROJECT_REPOSITORY, null);
			if (repositoryLocationDirectoryPath==null || repositoryLocationDirectoryPath.isEmpty()) {
				repositoryLocationDirectoryPath=null;
			}
		}
		return repositoryLocationDirectoryPath;
	}
	/**
	 * Sets the repository location directory path.
	 * @param repositoryLocationPath the new repository location directory path
	 */
	public void setRepositoryLocationDirectoryPath(String repositoryLocationPath) {
		if (repositoryLocationPath==null || repositoryLocationPath.isEmpty()==true) return;
		this.repositoryLocationDirectoryPath = repositoryLocationPath;
	}
	
	/**
	 * Returns the project export settings.
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings() {
		return projectExportSettings;
	}
	/**
	 * Sets the project export settings.
	 * @param projectExportSettings the new project export settings
	 */
	public void setProjectExportSettings(ProjectExportSettings projectExportSettings) {
		this.projectExportSettings = projectExportSettings;
	}
	
	/**
	 * Returns the {@link RepositoryEntry}.
	 * @return the repository entry
	 */
	public RepositoryEntry getRepositoryEntry() {
		return repositoryEntry;
	}
	/**
	 * Sets the {@link RepositoryEntry}.
	 * @param repositoryEntry the new repository entry
	 */
	public void setRepositoryEntry(RepositoryEntry repositoryEntry) {
		this.repositoryEntry = repositoryEntry;
	}
	
	/**
	 * Sets the show user dialogs.
	 * @param showUserDialogs the new show user dialogs
	 */
	public void setShowUserDialogs(boolean showUserDialogs) {
		this.showUserDialogs = showUserDialogs;
	}
	/**
	 * Checks if is show user dialogs.
	 * @return true, if is show user dialogs
	 */
	public boolean isShowUserDialogs() {
		return showUserDialogs;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		// --- Check if the setting are complete --------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			String errMsg = configError + " - Cancel repository export.";
			if (this.isShowUserDialogs()==true) {
				JOptionPane.showMessageDialog(Application.getMainWindow(), errMsg, "Repository Export Error", JOptionPane.ERROR_MESSAGE);
				if (this.getRepositoryLocationDirectoryPath()==null) {
					// --- Special case: missing repository path ----
					Application.showOptionDialog(DirectoryOptions.TAB_TITLE);
					return;
				}
			}
			errMsg = "[" + this.getClass().getSimpleName() + "] " + errMsg; 
			throw new IllegalArgumentException(errMsg);
		}
		
		// --- Check if the destination directory exists ------------ 
		File destinDir = new File(this.getRepositoryLocationDirectoryPath());
		if (destinDir.exists()==false) {
			destinDir.mkdirs();
			if (destinDir.exists()==false) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Could not create directory '" + this.getRepositoryLocationDirectoryPath() + "'.");
				return;
			}
		}

		// --- Export the project -----------------------------------
		this.getProjectExportController().exportProject(this.currProject, this.getProjectExportSettings(), this.isShowUserDialogs(), false);
		
		// --- Load the current repository --------------------------
		ProjectRepository repo = ProjectRepository.loadProjectRepository(destinDir);
		if (repo==null) {
			repo = new ProjectRepository();
		}
		// --- Add the new entry to the repository ------------------
		repo.addRepositoryEntry(this.getRepositoryEntry());
		repo.save(destinDir);
		
	}
	
	
	/**
	 * Gets the project export controller.
	 * @return the project export controller
	 */
	private ProjectExportController getProjectExportController() {
		if (projectExportController==null) {
			projectExportController = ProjectExportControllerProvider.getProjectExportController();
		}
		return projectExportController;
	}

	/**
	 * Return the configuration error as string, if there is an error.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		
		if (this.getRepositoryLocationDirectoryPath()==null) {
			return "The local directory of the repository is not specified!";
		}
		if (this.getProjectExportSettings()==null) {
			return "The project export settings were not specified!";
		}
		if (this.getRepositoryEntry()==null) {
			return "No repository entry was specified for the export!";
		}
		return null;
	}
	

	/**
	 * Gets the repository file name.
	 *
	 * @param project the project
	 * @return the repository file name
	 */
	public static String getRepositoryFileName(Project project) {
		String fileName = project.getProjectFolder() + "_" + project.getVersionTag() + "_" + project.getVersion().toString() + "." + Application.getGlobalInfo().getFileEndProjectZip();
		// --- Replace one or more whitespaces by an underscore -----
		fileName.replaceAll("\\s+", "_");
		return fileName;
	}
	/**
	 * Return a version qualifier for a given time stamp.
	 * @param timeStamp the time stamp
	 * @return the version qualifier for time stamp
	 */
	public static String getVersionQualifierForTimeStamp(long timeStamp) {
		return getVersionQualifierForDate(new Date(timeStamp));
	}
	/**
	 * Return the version qualifier for the given time.
	 *
	 * @param currTime the curr time
	 * @return the version qualifier for time stamp
	 */
	public static String getVersionQualifierForDate(Date currTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
		return sdf.format(currTime);
	}
	
}
