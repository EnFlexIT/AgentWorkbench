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
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.DefaultProjectExportController;
import agentgui.core.project.transfer.ProjectExportSettingsController;
import agentgui.core.project.transfer.ProjectImportController;
import agentgui.core.project.transfer.ProjectImportSettings;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.RepositoryEntry;
import de.enflexit.common.transfer.Download;
import de.enflexit.common.transfer.FileCopier;
import de.enflexit.common.transfer.RecursiveFolderDeleter;

/**
 * The Class ProjectUpdater does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdate extends Thread {

	private static final long UPDATE_CHECK_PERIOD = 1000 * 60 * 60 * 24; // - once a day -

	private boolean debugUpdateProcedure = false;
	
	private Project currProject; 
	private long currTimeStamp;
	private boolean skipTestOfLastDateChecked;
	
	private ProjectRepository projectRepository;
	
	private Boolean headlessUpdate;
	
	private boolean executedByUser;
	private Boolean userRequestForDownloadAndInstallation;
	private Boolean userRequestForInstallation;
	private Boolean showFinalUserMessage;
	private Boolean leaveUpdateProcedure;
	
	private boolean successfulUpdate;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryUpdate(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		if (this.currProject!=null) {
			this.setName(this.getClass().getSimpleName()  + " " + this.currProject.getProjectName());
		}
		this.currTimeStamp = System.currentTimeMillis();
	}
	
	/**
	 * Checks if is headless update.
	 * @return the boolean
	 */
	private Boolean isHeadlessUpdate() {
		if (headlessUpdate==null) {
			headlessUpdate = Application.isOperatingHeadless();
		}
		return headlessUpdate;
	}
	/**
	 * Sets if is a headless update.
	 * @param headlessUpdate the new headless update
	 */
	public void setHeadlessUpdate(boolean headlessUpdate) {
		this.headlessUpdate = headlessUpdate;
	}
	
	/**
	 * Checks if the ProjectRepositoryUpdate was executed by user.
	 * @return true, if is executed by user
	 */
	private boolean isExecutedByUser() {
		return executedByUser;
	}
	/**
	 * Sets the ProjectRepositoryUpdate executed by user.
	 * @param executedByUser the new executed by user
	 */
	public void setExecutedByUser(boolean executedByUser) {
		this.executedByUser = executedByUser;
	}

	/**
	 * Checks if is a user request for download and installation is required.
	 * @return true, if is user request for download
	 */
	private Boolean isUserRequestForDownloadAndInstallation() {
		return userRequestForDownloadAndInstallation;
	}
	/**
	 * Sets that the user request for download and installation is required.
	 * @param userRequestForInstallation the new user request for download
	 */
	public void setUserRequestForDownloadAndInstallation(boolean userRequestForDownload) {
		this.userRequestForDownloadAndInstallation = userRequestForDownload;
	}
	/**
	 * Checks if is confirmed user request for download and installation.
	 * @param update the RepositoryEntry of the update 
	 * @return true, if is confirmed user request for download and installation
	 */
	private boolean isConfirmedUserRequestForDownloadAndInstallation(RepositoryEntry update) {
		boolean confirmed = true;
		if (this.isUserRequestForDownloadAndInstallation()==true) {
			if (this.isHeadlessUpdate()==true) {
				confirmed = false;
			} else {
				String title   = "Download and Install the Update?";
				String message = this.getUpdateAsText(update) + " ";
				message += Language.translate("is available.", Language.EN) + "\n";
				message += Language.translate("Would you like to download and install this update?", Language.EN); 
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer==JOptionPane.NO_OPTION) {
					confirmed = false;
				}
			}
		}
		return confirmed;
	}
	
	/**
	 * Checks if is a user request for installation is required.
	 * @return true, if is user request for installation
	 */
	private Boolean isUserRequestForInstallation() {
		return userRequestForInstallation;
	}
	/**
	 * Sets that the user request for installation is required.
	 * @param userRequestForInstallation the new user request for installation
	 */
	public void setUserRequestForInstallation(boolean userRequestForInstallation) {
		this.userRequestForInstallation = userRequestForInstallation;
	}
	/**
	 * Checks if is confirmed user request for the installation.
	 * @param update the RepositoryEntry of the update 
	 * @return true, if is confirmed user request for download and installation
	 */
	private boolean isConfirmedUserRequestForInstallation(RepositoryEntry update) {
		boolean confirmed = true;
		if (this.isUserRequestForInstallation()==true) {
			if (this.isHeadlessUpdate()==true) {
				confirmed = false;
			} else {
				String title   = "Install the Update?";
				String message = this.getUpdateAsText(update) + " ";
				message += Language.translate("was downloaded.", Language.EN) + "\n";
				message += Language.translate("Would you like to install this update now?", Language.EN);
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer==JOptionPane.NO_OPTION) {
					confirmed = false;
				}
			}
		}
		return confirmed;
	}
	
	/**
	 * Returns if the final message has to be displayed to the user.
	 * @return the user final message
	 */
	private Boolean isShowFinalUserMessage() {
		return showFinalUserMessage;
	}
	/**
	 * Sets to show a final user message.
	 * @param showFinalUserMessage the new show final user message
	 */
	public void setShowFinalUserMessage(boolean showFinalUserMessage) {
		this.showFinalUserMessage = showFinalUserMessage;
	}
	
	/**
	 * Checks if is skip test of last date checked.
	 * @return true, if is skip test of last date checked
	 */
	public boolean isSkipTestOfLastDateChecked() {
		return skipTestOfLastDateChecked;
	}
	/**
	 * Sets the skip test of last date checked.
	 * @param skipTestOfLastDateChecked the new skip test of last date checked
	 */
	public void setSkipTestOfLastDateChecked(boolean skipTestOfLastDateChecked) {
		this.skipTestOfLastDateChecked = skipTestOfLastDateChecked;
	}

	/**
	 * Checks if a update check is required because of date.
	 * @return true, if is required update check because of date
	 */
	private boolean isRequiredUpdateCheckBecauseOfDate() {
		return this.currTimeStamp >= this.currProject.getUpdateDateLastChecked() + UPDATE_CHECK_PERIOD;
	}
	
	
	/**
	 * Checks if is leave update procedure.
	 * @return true, if is leave update procedure
	 */
	private Boolean isLeaveUpdateProcedure() {
		return leaveUpdateProcedure;
	}
	/**
	 * Sets the leave update procedure.
	 * @param leaveUpdateProcedure the new leave update procedure
	 */
	private void setLeaveUpdateProcedure(boolean leaveUpdateProcedure) {
		this.leaveUpdateProcedure = leaveUpdateProcedure;
	}
	
	/**
	 * Configures the internal update procedure.
	 */
	private void configureInernalUpdateProcedure() {
		
		boolean localLeaveUpdateProcedure = true;
		boolean localUserRequestForDownloadAndInstallation = true;
		boolean localUserRequestForInstallation = true;
		boolean localShowFinalUserMessage = true;
		
		if (this.isExecutedByUser()==true) {
			// --- Do not leave the update procedure ----------------
			localLeaveUpdateProcedure = false;
			// --- Request for download and installation ------------
			localUserRequestForDownloadAndInstallation = true; 
			// --- No further request for installation --------------
			localUserRequestForInstallation = false;
			// --- Show final user message --------------------------
			localShowFinalUserMessage = true;
			
		} else {
			// --- Do update as configured --------------------------
			int updateConfig = this.currProject.getUpdateAutoConfiguration();
			switch (updateConfig) {
			case 0:
				// --- Auto-Update ----------------------------------
				localLeaveUpdateProcedure = false;
				localUserRequestForDownloadAndInstallation = false; 
				localUserRequestForInstallation = false;
				localShowFinalUserMessage = false;
				break;
			case 1:
				// --- Ask for installation -------------------------
				localLeaveUpdateProcedure = false;
				localUserRequestForDownloadAndInstallation = false; 
				localUserRequestForInstallation = true;
				localShowFinalUserMessage = false;
				break;
			case 2:
				// --- No automated update --------------------------
				localLeaveUpdateProcedure = true;
				localUserRequestForDownloadAndInstallation = true; 
				localUserRequestForInstallation = false;
				localShowFinalUserMessage = false;
				break;
			}
			
			// --- Ensure that auto check happens once a day --------
			if (this.isSkipTestOfLastDateChecked()==false && this.isRequiredUpdateCheckBecauseOfDate()==false) {
				localLeaveUpdateProcedure = true;
			}
			
		}
		
		// --- Assign to local variables, if not already set --------
		if (this.isLeaveUpdateProcedure()==null) this.setLeaveUpdateProcedure(localLeaveUpdateProcedure);
		if (this.isUserRequestForDownloadAndInstallation()==null) this.setUserRequestForDownloadAndInstallation(localUserRequestForDownloadAndInstallation);
		if (this.isUserRequestForInstallation()==null) this.setUserRequestForInstallation(localUserRequestForInstallation);
		if (this.isShowFinalUserMessage()==null) this.setShowFinalUserMessage(localShowFinalUserMessage);
		
		// --- If AWB is executed from IDE, skip the update ---------  
		if (this.debugUpdateProcedure==false && Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {

			String message = Language.translate("No project updates are available in the IDE environment.", Language.EN);
			if (this.isHeadlessUpdate()==false && this.isExecutedByUser()==true) {
				JOptionPane.showMessageDialog(Application.getMainWindow(), message, "IDE - Environment", JOptionPane.INFORMATION_MESSAGE);
			} else {
				this.printSystemOutput(message, false);
			}
			this.setLeaveUpdateProcedure(true);
		}
		
	}
	
	/**
	 * Returns the update version as text.
	 *
	 * @param update the update
	 * @return the update as text
	 */
	private String getUpdateAsText(RepositoryEntry update) {
		return "Project '" + this.currProject.getProjectName() + "', version " + update.getVersion();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		this.startInSameThread();
	}
	/**
	 * Does the same as calling the threads {@link #start()} method, but 
	 * without starting an individual thread. Thus, it enables to wait
	 * until the project update is finalized.
	 */
	public void startInSameThread() {
		
		String updateTitle = null;
		String updateMessage = null;
		int updateMessageType = 0;

		// --- Configure update procedure --------------------------- 
		this.configureInernalUpdateProcedure();
		if (this.isLeaveUpdateProcedure()==true) return;
		
		// --- Check if the setting are complete --------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			String errMsg = "[" + this.getClass().getSimpleName() + "] " + configError + " - Cancel update check.";
			throw new IllegalArgumentException(errMsg);
		}

		// -- Check for the configure update site -------------------
		if (this.currProject.getUpdateSite()==null || this.currProject.getUpdateSite().isEmpty()==true) {
			this.printSystemOutput("No update-site was specified for the project '" + this.currProject.getProjectName() + "'!", true);
			return;
		}
		
		// --- Check if the repository can be loaded ----------------
		if (this.getProjectRepository()==null) return;
		
		// --- Check if an update is available ----------------------
		RepositoryEntry update = this.getProjectRepository().getProjectUpdate(this.currProject);
		if (update!=null) {
			// --- An update is available ---------------------------
			if (this.isConfirmedUserRequestForDownloadAndInstallation(update)==false) return;
			
			// --- Download the update ------------------------------
			String updateFilename = this.getLinkOrPathWithDirectorySuffix(Application.getGlobalInfo().getPathProjects(true), update.getFileName());
			if (this.downloadOrCopyProjectArchiveFromRepository(this.currProject.getUpdateSite(), update, updateFilename)==true) {
				// --- The download of update is done ---------------
				if (this.isConfirmedUserRequestForInstallation(update)==false) return;
				
				// --- Do the installation of the new update --------
				if (this.updateProject(updateFilename)==true) {
					updateTitle = Language.translate("Updated successful", Language.EN);
					updateMessage = Language.translate("The project was updated successfully!", Language.EN);
					updateMessageType = JOptionPane.INFORMATION_MESSAGE;
					this.setSuccessfulUpdate(true);
				} else {
					updateTitle = Language.translate("Update failed", Language.EN);
					updateMessage = Language.translate("The project update failed!", Language.EN);
					updateMessageType = JOptionPane.ERROR_MESSAGE;
				}
				// --- Give some feedback to the user ---------------
				if (this.isHeadlessUpdate()==false && this.isShowFinalUserMessage()==true) {
					JOptionPane.showMessageDialog(Application.getMainWindow(), updateMessage, updateTitle, updateMessageType);
				} else {
					this.printSystemOutput(updateMessage, (updateMessageType!=JOptionPane.INFORMATION_MESSAGE));
				}
			}
			
		} else {
			// --- Update last date checked -------------------------
			this.currProject.setUpdateDateLastChecked(this.currTimeStamp);
			if (this.isExecutedByUser()==false) {
				this.currProject.save();	
			}
			
			// --- No Update found ----------------------------------
			updateTitle = Language.translate("Updated check for", Language.EN) + " '" + this.currProject.getProjectName() + "'";
			updateMessage = Language.translate("No update could be found for the current project!", Language.EN);
			updateMessageType = JOptionPane.INFORMATION_MESSAGE;
			if (this.isHeadlessUpdate()==false && this.isShowFinalUserMessage()==true) {
				JOptionPane.showMessageDialog(Application.getMainWindow(), updateMessage, updateTitle, updateMessageType);
			} else {
				this.printSystemOutput(updateMessage, false);
			}
		}
		
	}
	
	/**
	 * Returns if the update was successful after the execution of the.
	 * @return true, if is successful update
	 */
	public boolean isSuccessfulUpdate() {
		return successfulUpdate;
	}
	/**
	 * Sets the successful update.
	 * @param successfulUpdate the new successful update
	 */
	private void setSuccessfulUpdate(boolean successfulUpdate) {
		this.successfulUpdate = successfulUpdate;
	}
	
	/**
	 * Updates the current project with the specified project archive file.
	 *
	 * @param updateFileName the update file name
	 * @return true, if successful
	 */
	public boolean updateProject(String updateFileName) {
		
		// --- Just close the current project without saving --------
		if (this.currProject.close(null, true)==false) return false;
		
		// --- Backup the old project into an archive ---------------
		if (this.packCurrentProjectToArchive()==false) return false;

		// --- Delete old structure (keep agent working directory) --
		try {
			String projectPath = this.currProject.getProjectFolderFullPath();
			String[] excludeDelete = {this.currProject.getProjectAgentWorkingFolderFullPath(false)};
			RecursiveFolderDeleter rfd = new RecursiveFolderDeleter();
			rfd.deleteFolder(projectPath, excludeDelete);
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		
		// --- Import the younger project archive -------------------
		return this.importProjectFromArchive(updateFileName);
	}
	
	/**
	 * Imports a project from the specified archive file.
	 *
	 * @param projectArchiveFileName the project archive file name
	 * @return true, if successful
	 */
	public boolean importProjectFromArchive(String projectArchiveFileName) {
		
		// --- Define settings for update import --------------------
		ProjectImportSettings pims = new ProjectImportSettings(new File(projectArchiveFileName));
		pims.setExtractInThread(false);
		pims.setOverwriteExistingVersion(true);
		pims.setAfterImportTask(this.getAfterUpdateImportTask());
		
		// --- Import the new version of the project ----------------
		ProjectImportController pic = new ProjectImportController(pims);
		return pic.doProjectImport();
	}
	
	/**
	 * Returns the task that is to be executed after the import of the project update.
	 * @return the after import task
	 */
	private Runnable getAfterUpdateImportTask() {
		
		if (this.currProject==null) return null;
		
		Runnable afterImportTask = null;
		final String projectFolderToOpen = this.currProject.getProjectFolder();
		
		ExecutionMode eMode = Application.getGlobalInfo().getExecutionMode();
		switch (eMode) {
		case APPLICATION:
			// --- Reopen the project -------------------------------
			afterImportTask = new Runnable() {
				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Application.getProjectsLoaded().add(projectFolderToOpen);
						}
					});
				}
			};
			break;

		case DEVICE_SYSTEM:
			// --- Internally restart Agent.Workbench ---------------
			afterImportTask = new Runnable() {
				@Override
				public void run() {
					// Nothing to do here ---------------------------
				}
			};
			break;
			
		default:
			// --- Nothing to do here -------------------------------
			break;
		}
		return afterImportTask;
	}
	
	/**
	 * Packs the current project into a project archive located 
	 * in the projects directory and removes the project.
	 * @return true, if successful
	 */
	private boolean packCurrentProjectToArchive() {
		
		boolean successful = false;
		
		String projectDir = Application.getGlobalInfo().getPathProjects();
		String fileName = ProjectRepositoryExport.getRepositoryFileName(this.currProject);
		File targetFile = new File(projectDir + fileName);
		if (targetFile.exists()==true) {
			// --- Delete the old file --------------------
			targetFile.delete();
		}

		
		// --- Define export settings ---------------------
		DefaultProjectExportController expController = new DefaultProjectExportController(this.currProject);
		ProjectExportSettingsController pesc = new ProjectExportSettingsController(this.currProject, expController); 
		pesc.setIncludeInstallationPackage(false);
		pesc.setIncludeAllSetups(true);
		pesc.addDefaultsToExcludeList();
		pesc.setTargetFile(targetFile);
		
		// --- Export the project -------------------------
		expController.exportProject(this.currProject, pesc.getProjectExportSettings(), false, false);
		successful = expController.isExportSuccessful();
		
		return successful;
	}
	
	
	/**
	 * Download or copy project archive from repository.
	 *
	 * @param sourceDirectoryOrWebReference the source directory or web reference (without any file)
	 * @param updateRepositoryEntry the update repository entry
	 * @param destinationFileName the destination file name
	 * @return true, if successful
	 */
	public boolean downloadOrCopyProjectArchiveFromRepository(String sourceDirectoryOrWebReference, RepositoryEntry updateRepositoryEntry, String destinationFileName) {
		
		boolean successful = false;
		if (this.getProjectRepository().isWebRepository()==true) {
			// -- Start the web download ----------------------------
			String sourceFileURL = this.getFileNameURLDownload(sourceDirectoryOrWebReference, updateRepositoryEntry);
			
			Download download = new Download(sourceFileURL, destinationFileName);
			download.startDownload();
			successful = download.wasSuccessful();
			
		} else {
			// --- Copy file to destination -------------------------
			String sourceFileName = this.getFileNameDownload(sourceDirectoryOrWebReference, updateRepositoryEntry);
			FileCopier copier = new FileCopier();
			successful = copier.copyFile(sourceFileName, destinationFileName);
		}
		return successful;
	}
	/**
	 * Return the download file name URL base on the specified {@link RepositoryEntry}.
	 *
	 * @param sourceDirectoryOrWebReference the source directory or web reference (without any file)
	 * @param updateRepositoryEntry the update repository entry
	 * @return the download file name URL
	 */
	private String getFileNameURLDownload(String sourceDirectoryOrWebReference, RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(sourceDirectoryOrWebReference, "/") + updateRepositoryEntry.getFileName();
	}
	/**
	 * Return the repository file name.
	 *
	 * @param sourceDirectoryOrWebReference the source directory or web reference (without any file)
	 * @param updateRepositoryEntry the update repository entry
	 * @return the repository file name
	 */
	private String getFileNameDownload(String sourceDirectoryOrWebReference, RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(sourceDirectoryOrWebReference, File.separator) + updateRepositoryEntry.getFileName();
	}
	/**
	 * Returns the link or path with the deisred directory suffix.
	 * @param linkOfPath the link of path
	 * @param desiredSuffix the desired suffix
	 * @return the link or path with directory suffix
	 */
	public String getLinkOrPathWithDirectorySuffix(String linkOfPath, String desiredSuffix) {
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
			projectRepository = ProjectRepository.loadProjectRepository(this.currProject.getUpdateSite());
			if (projectRepository==null) {
				this.printSystemOutput("Could not access any projct repository!", true);
			}
		}
		return projectRepository;
	}
	/**
	 * Sets the current project repository.
	 * @param projectRepository the new project repository
	 */
	public void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
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
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void printSystemOutput(String message, boolean isError) {
		String sysMessage = "[" + this.getClass().getSimpleName() + "] " + message;
		if (isError) {
			System.err.println(sysMessage);
		} else {
			System.out.println(sysMessage);
		}
	}
	
}
