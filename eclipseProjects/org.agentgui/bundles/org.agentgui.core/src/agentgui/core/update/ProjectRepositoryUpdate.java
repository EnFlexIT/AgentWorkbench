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
import java.net.MalformedURLException;
import java.net.URL;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.ProjectImportController;
import agentgui.core.project.transfer.ProjectImportSettings;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.RepositoryEntry;
import de.enflexit.common.transfer.Download;
import de.enflexit.common.transfer.FileCopier;

/**
 * The Class ProjectUpdater does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdate extends Thread {

	private Project currProject; 
	
	private boolean isRepositoryFromWeb;
	private ProjectRepository projectRepository;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryUpdate(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		this.setName(this.getClass().getSimpleName()  + " " + this.currProject.getProjectName());
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
			String errMsg = "[" + this.getClass().getSimpleName() + "] " + configError + " - Cancel update check.";
			throw new IllegalArgumentException(errMsg);
		}

		// -- Check for the configure update site -------------------
		if (this.currProject.getUpdateSite()==null) {
			System.err.println("No update-site was specified for the project '" + this.currProject.getProjectName() + "'!");
			return;
		}
		
		// --- Check if the repository can be loaded ----------------
		if (this.getProjectRepository()==null) return;
		
		// --- Check if an update is available ----------------------
		RepositoryEntry update = this.getProjectRepository().getProjectUpdate(this.currProject);
		if (update!=null) {
			// --- An update is available ---------------------------
			System.out.println("An Update is available => Do the update");

			// --- Rename the original project directory ------------
			System.out.println("Download has to be done!");
			String updateFilename = this.getLinkOrPathWithDirectorySuffix(Application.getGlobalInfo().getPathProjects(true), update.getFileName());
			if (this.downloadUpdateOrCopyFromRepository(update, updateFilename)==true) {
				System.out.println("Download is done!");
				// --- Do the installation of the new update --------
				if (this.updateProject(updateFilename)==true) {
					System.out.println("Update was successful");
				} else {
					System.err.println("Update failed ");
				}
			}
		}
		
	}

	
	/**
	 * Updates the current project with the specified project archive file.
	 *
	 * @param updateFileName the update file name
	 * @return true, if successful
	 */
	public boolean updateProject(String updateFileName) {
		
		// --- Collect required information -------------------------
		String projectDirectory = this.currProject.getProjectFolderFullPath();
		String projectDirectoryDuringUpdate = Application.getGlobalInfo().getPathProjects() + this.currProject.getProjectFolder() + "_"  + this.currProject.getVersion().toString() + File.separator;
		
		// --- Save and close the current project -------------------
		boolean saved = this.currProject.save();
		boolean closed = false;
		if (saved==true) {
			closed = this.currProject.close();
		}
		if (closed==false) return false;
		
		// --- Rename current project folder ------------------------
		if (this.renameDirectory(projectDirectory, projectDirectoryDuringUpdate)==false) return false;
		
		// --- Import the new version of the project ----------------
		ProjectImportSettings pims = new ProjectImportSettings(new File(updateFileName));
		pims.setExtractInThread(false);
		ProjectImportController pic = new ProjectImportController(pims);
		boolean successfuil = pic.doProjectImport();
		if (successfuil==true) {
			// --- Clean-up things remaining ------------------------
			// TODO
			
		}
		return successfuil;
	}
	
	
	/**
	 * Renames the specified directory to the new .
	 *
	 * @param sourceDirName the source directory name
	 * @param destinDirName the destination directory name
	 * @return true, if successful
	 */
	public boolean renameDirectory(String sourceDirName, String destinDirName) {
		
		boolean successful = false;
		
		File sourceDir = new File(sourceDirName);
		if (sourceDir.isDirectory()==true) {
		    File destinDir = new File(destinDirName);
		    successful = sourceDir.renameTo(destinDir);
		}
		return successful;
	}
	
	/**
	 * Download update or copy from repository.
	 *
	 * @param updateRepositoryEntry the update repository entry
	 * @param destinationFileName the destination file name
	 * @return true, if successful
	 */
	private boolean downloadUpdateOrCopyFromRepository(RepositoryEntry updateRepositoryEntry, String destinationFileName) {
		
		boolean successful = false;
		if (this.isRepositoryFromWeb==true) {
			// -- Start the web download ----------------------------
			String sourceFileURL = this.getFileNameURLDownload(updateRepositoryEntry);
			
			Download download = new Download(sourceFileURL, destinationFileName);
			download.startDownload();
			successful = download.wasSuccessful();
			
		} else {
			// --- Copy file to destination 
			String sourceFileName = this.getFileNameDownload(updateRepositoryEntry);
			FileCopier copier = new FileCopier();
			successful = copier.copyFile(sourceFileName, destinationFileName);
		}
		return successful;
	}
	
	/**
	 * Return the download file name URL base on the specified {@link RepositoryEntry}.
	 * @param updateRepositoryEntry the update repository entry
	 * @return the download file name URL
	 */
	private String getFileNameURLDownload(RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(this.currProject.getUpdateSite(), "/") + updateRepositoryEntry.getFileName();
	}
	/**
	 * Return the repository file name.
	 * @param updateRepositoryEntry the update repository entry
	 * @return the repository file name
	 */
	private String getFileNameDownload(RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(this.currProject.getUpdateSite(), File.separator) + updateRepositoryEntry.getFileName();
	}
	/**
	 * Returns the link or path with the deisred directory suffix.
	 * @param linkOfPath the link of path
	 * @param desiredSuffix the desired suffix
	 * @return the link or path with directory suffix
	 */
	private String getLinkOrPathWithDirectorySuffix(String linkOfPath, String desiredSuffix) {
		String pathChecked = linkOfPath;
		if (pathChecked.endsWith(desiredSuffix)==false) {
			pathChecked += desiredSuffix;
		}
		return pathChecked;
	}
	
	/**
	 * Returns the project repository from the projects update site.
	 * @return the project repository
	 */
	public ProjectRepository getProjectRepository() {
		if (projectRepository==null && this.currProject.getUpdateSite()!=null) {
			// --- Check if the update site is a web site URL -------
			try {
				URL updateURL = new URL(this.currProject.getUpdateSite());
				projectRepository = ProjectRepository.loadProjectRepository(updateURL);
				this.isRepositoryFromWeb = true;
				
			} catch (MalformedURLException urlEx) {
				//urlEx.printStackTrace();
				this.printSystemError("URL access action says " + urlEx.getLocalizedMessage());
			}
			
			// --- Backup, if repository comes not from an URL ------
			if (projectRepository==null) {
				// --- Check if update site is a local directory ----
				File localRepo = new File(this.currProject.getUpdateSite());
				if (localRepo.exists()==true) {
					projectRepository = ProjectRepository.loadProjectRepository(localRepo);
					this.isRepositoryFromWeb = false;
				}
			}
			
		}
		return projectRepository;
	}
	
	/**
	 * Return the configuration error as string, if there is an error.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		if (this.currProject==null) {
			return "No project was specified for an update!";
		}
		return null;
	}
	
	/**
	 * Prints the specified system error.
	 * @param message the message
	 */
	private void printSystemError(String message) {
		System.err.println("[" + this.getClass().getSimpleName() + "] " + message);
	}
	
}
