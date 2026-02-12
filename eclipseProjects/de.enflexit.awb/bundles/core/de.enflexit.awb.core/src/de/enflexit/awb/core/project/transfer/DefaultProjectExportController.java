package de.enflexit.awb.core.project.transfer;

import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import de.enflexit.language.Language;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetup;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.awb.core.ui.AwbProgressMonitor;
import de.enflexit.awb.core.ui.AwbProjectExportDialog;
import de.enflexit.common.GlobalRuntimeValues;
import de.enflexit.common.transfer.ArchiveFileHandler;
import de.enflexit.common.transfer.RecursiveFolderCopier;
import de.enflexit.common.transfer.RecursiveFolderDeleter;

/**
 * This class is responsible for exporting projects from AgentWorkbench.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class DefaultProjectExportController implements ProjectExportController{

	private static final String FILE_NAME_FOR_INSTALLATION_PACKAGE = "AgentWorkbench";
	
	private static final String PROJECT_PATH_WINDOWS_LINUX = "AgentWorkbench/projects";
	private static final String PROJECT_PATH_MAC = "agentgui.app/Contents/Eclipse/projects";
	
	protected static final String TEMP_FOLDER_SUFFIX = "_tmp";

	private Project project;
	private Path projectFolderPath;
	private ProjectExportSettings exportSettings;
	
	private boolean showUserDialogs = true;
	private String messageSuccess;
	private String messageFailure;
	
	private boolean exportSuccessful;
	
	protected Path tempFolderPath;

	private AwbProgressMonitor progressMonitor;
	
	private ArchiveFileHandler archiveFileHandler;
	
	private boolean confirmationDialogDisabled = false;
	
	
	
	/**
	 * Instantiates a new default project export controller.
	 */
	public DefaultProjectExportController() {
		this(null);
	}

	/**
	 * Instantiates a new default project export controller.
	 * @param project the project
	 */
	public DefaultProjectExportController(Project project) {
		this.project = project;
	}


	/* (non-Javadoc)
	 * @see agentgui.core.project.transfer.ProjectExportController#exportProject(agentgui.core.project.Project)
	 */
	public void exportProject(Project project) {
		this.exportProject(project, exportSettings, true, true);
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.core.project.transfer.ProjectExportController#getProjectExportSettings(agentgui.core.project.Project)
	 */
	@Override
	public ProjectExportSettings getProjectExportSettings(Project project) {
		
		this.project = project;
		
		// --- Show a dialog to configure the export ----------------
		AwbProjectExportDialog projectExportDialog = AgentWorkbenchUiManager.getInstance().getProjectExportDialog(project, this);
		projectExportDialog.setVisible(true);
		// - - Does the user action here - - - - - - - - - - - - - --
		if (projectExportDialog.isCanceled() == false) {
			
			// --- Get the export settings from the dialog ----------
			this.exportSettings = projectExportDialog.getExportSettings();
			
			// --- Select the export destination --------------------
			JFileChooser chooser = this.getJFileChooser(project);
			int userAnswer = chooser.showSaveDialog((Component) Application.getMainWindow());
			if (userAnswer == JFileChooser.APPROVE_OPTION) {
				
				File targetFile = chooser.getSelectedFile();
				Application.getGlobalInfo().setLastSelectedFolder(targetFile.getParentFile());
				
				// --- Check if the file already exists -------------
				if (targetFile.exists() == true) {
					// --- Overwrite existing file? -----------------
					String optionTitle = targetFile.getName() + ": " + Language.translate("Datei überschreiben?");
					String optionMsg = Language.translate("Die Datei existiert bereits. Wollen Sie diese Datei überschreiben?");
					int answer = AwbMessageDialog.showConfirmDialog(projectExportDialog, optionMsg, optionTitle, AwbMessageDialog.YES_NO_OPTION);
					if (answer == AwbMessageDialog.YES_OPTION) {
						targetFile.delete();
					} else {
						return null;
					}
				}
				
				// --- Set the target file --------------------------
				this.exportSettings.setTargetFile(targetFile);
				
			} else if (userAnswer==JFileChooser.CANCEL_OPTION) {
				return null;
			}
			
		}
		return this.exportSettings;
	}

	/**
	 * Exports the current project using the provided {@link ProjectExportSettings},
	 * and showing the user dialogs if necessary.
	 * @param project the project to be exported
	 * @param exportSettings The {@link ProjectExportSettings}
	 */
	public void exportProject(Project project, ProjectExportSettings exportSettings) {
		this.exportProject(project, exportSettings, true, true);
	}

	/**
	 * Exports the current project using the provided {@link ProjectExportSettings}.
	 * @param project the project to be exported
	 * @param exportSettings The {@link ProjectExportSettings}
	 * @param showUserDialogs specifies if user dialogs are shown
	 * @param useConcurrentThread specifies if the project should be exported in a concurrent thread
	 */
	public void exportProject(Project project, ProjectExportSettings exportSettings, boolean showUserDialogs, boolean useConcurrentThread) {

		this.project = project;
		this.exportSettings = exportSettings;
		this.showUserDialogs = showUserDialogs;
		if (useConcurrentThread == true) {
			new ProjectExportThread().start();
		} else {
			this.doExport();
		}
	}


	/**
	 * Creates and initialized a {@link JFileChooser} for selecting the export target.
	 *
	 * @param project the project to handle
	 * @return the {@link JFileChooser}
	 */
	public JFileChooser getJFileChooser(Project project) {

		// --- Create and initialize the JFileChooser -------------------------
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setMultiSelectionEnabled(false);
		jFileChooser.setAcceptAllFileFilterUsed(false);

		// --- Create the initial file name -----------------------------------
		File proposedFile = new File(this.getProposedFileName(project));
		jFileChooser.setSelectedFile(proposedFile);
		jFileChooser.setCurrentDirectory(proposedFile);
		
		// --- Remember the suffix --------------------------------------------
		String fileName = proposedFile.getName();
		String fileSuffix = fileName.substring(fileName.indexOf(".")+1, fileName.length());
		
		// --- Add file filters -----------------------------------------------
		List<FileNameExtensionFilter> filtersList = this.getFileNameExtensionFilters();
		for (FileNameExtensionFilter filter : filtersList) {
			jFileChooser.addChoosableFileFilter(filter);
			
			// --- Set the selected filter according to the suffix ------------
			if (filter.getExtensions()[0].equals(fileSuffix)) {
				jFileChooser.setFileFilter(filter);
			}
		}

		return jFileChooser;
	}

	/**
	 * Provides a default file name for the export target.
	 * @param project the project to be handled
	 * @return the default file name
	 */
	protected String getProposedFileName(Project project) {

		// --- Generate a file name proposition based on the export settings ---------------
		String proposedFileName = GlobalRuntimeValues.getLastSelectedDirectory().toString();
		proposedFileName = proposedFileName.endsWith(File.separator)==false ? proposedFileName + File.separator : proposedFileName;
		
		if (this.exportSettings.isIncludeInstallationPackage() == true) {
			// --- Installation package ---------
			proposedFileName += FILE_NAME_FOR_INSTALLATION_PACKAGE;
			if (this.exportSettings.getInstallationPackage().isForWindows() == true) {
				proposedFileName += ".zip";
			} else {
				proposedFileName += ".tar.gz";
			}

		} else {
			// --- Project only -----------------
			proposedFileName += project.getProjectFolder() + '.' + Application.getGlobalInfo().getFileEndProjectZip();

		}
		return proposedFileName;
	}

	/**
	 * Provides a list of {@link FileNameExtensionFilter}s for a {@link JFileChooser} dialog. Can be overridden by
	 * subclasses to customize the list of available filters.
	 * @return The list of {@link FileNameExtensionFilter}s
	 */
	protected List<FileNameExtensionFilter> getFileNameExtensionFilters() {
		List<FileNameExtensionFilter> filtersList = new ArrayList<FileNameExtensionFilter>();
		// --- Prepare file type filters -----------------------------
		String projectFileSuffix = Application.getGlobalInfo().getFileEndProjectZip();
		FileNameExtensionFilter projectFileFilter = new FileNameExtensionFilter(Language.translate("Agent.Workbench Projekt-Datei") + " (*." + projectFileSuffix + ")", projectFileSuffix);
		filtersList.add(projectFileFilter);
		FileNameExtensionFilter zipFileFilter = new FileNameExtensionFilter(Language.translate("Zip-Datei") + " (*.zip)", "zip");
		filtersList.add(zipFileFilter);
		FileNameExtensionFilter tarGzFileFilter = new FileNameExtensionFilter(Language.translate("Tar.gz-Datei") + " (*.tar.gz)", "tar.gz");
		filtersList.add(tarGzFileFilter);
		return filtersList;
	}

	protected ProjectExportSettings getExportSettings() {
		return exportSettings;
	}

	/**
	 * Copies all required project data to a temporary folder next to the selected
	 * @return Copying successful?
	 */
	private boolean copyProjectDataToTempFolder() {

		// --- Determine the source path --------------
		Path sourcePath = new File(project.getProjectFolderFullPath()).toPath();
		Path destinPath = this.getTempExportFolderPath();
		
		// --- Copy the required files to the temporary folder --------
		if (destinPath.equals(sourcePath)==false) {
			
			try {
				Files.createDirectories(destinPath);
				RecursiveFolderCopier rfc = new RecursiveFolderCopier();
				rfc.copyFolder(sourcePath, destinPath, this.getFolderCopySkipList(sourcePath));
			} catch (IOException e) {
				System.err.println("Error copying project data!");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets a list of folders and files to be skipped when copying the project directory
	 * @param sourcePath The source path (project directory)
	 * @return The skip list
	 */
	protected List<Path> getFolderCopySkipList(Path sourcePath) {
		return this.exportSettings.getProjectExportSettingsController(this.getProject()).getFileExcludeList();
	}

	/**
	 * Removes all setups that are not selected for export from the setup list
	 */
	private void removeUnexportedSetupsFromList(Project projectForExport) {

//		// --- Set authorization settings -------------------------------------
//		if (this.exportSettings.isIncludeAuthorizationSettings()==false) {
//			exportedProject.setUpdateAuthorization(null);
//		}
		
		// --- Remove all setups that are not exported --------------------
		List<String> setupNames = new ArrayList<>(projectForExport.getSimulationSetups().keySet());
		for (int i = 0; i < setupNames.size(); i++) {
			String setupName = setupNames.get(i);
			if (this.exportSettings.getSimSetups().contains(setupName) == false) {
				projectForExport.getSimulationSetups().remove(setupName);
			}
		}

		// --- If the currently selected setup is not exported, set the first exported
		// setup as selected instead -----
		if (this.exportSettings.getSimSetups().contains(projectForExport.getSimulationSetupCurrent()) == false) {
			if (this.exportSettings.getSimSetups().isEmpty()==false) {
				projectForExport.setSimulationSetupCurrent(this.exportSettings.getSimSetups().get(0));
			}
		}
	}

	/**
	 * Loads the simulation setup with the specified name
	 * @param setupName The setup to be loaded
	 * @return The setup
	 * @throws JAXBException Parsing the setup file failed
	 * @throws IOException Reading the setup file failed
	 */
	protected SimulationSetup loadSimSetup(String setupName) throws JAXBException, IOException {
		// --- Determine the setup file path ----------
		String setupFileName = this.project.getSimulationSetups().get(setupName);
		String setupFileFullPath = this.project.getSubFolder4Setups(true) + File.separator + setupFileName;
		File setupFile = new File(setupFileFullPath);

		// --- Load the setup -------------
		JAXBContext pc;
		SimulationSetup simSetup = null;
		pc = JAXBContext.newInstance(this.project.getSimulationSetups().getCurrSimSetup().getClass());
		Unmarshaller um = pc.createUnmarshaller();
		FileReader fr = new FileReader(setupFile);
		simSetup = (SimulationSetup) um.unmarshal(fr);
		fr.close();

		return simSetup;

	}

	/**
	 * Gets the temporary export folder path
	 * @return the temporary export folder path
	 */
	protected Path getTempExportFolderPath() {
		if (tempFolderPath == null) {

			// --- Determine the path for the temporary export folder, based on the selected target file ----
			File targetFile = this.exportSettings.getTargetFile();
			Path containingFolder = targetFile.getParentFile().toPath();
			String tempFolderName = project.getProjectFolder() + TEMP_FOLDER_SUFFIX;
			tempFolderPath = containingFolder.resolve(tempFolderName);
			
			// --- Replace the temp folder name with the original name when adding files to the archive -----
			this.getArchiveFileHandler().addPathReplacement(tempFolderName, project.getProjectFolder());
			
		}
		return this.tempFolderPath;
	}

	/**
	 * Integrate project into installation package.
	 * @return true, if successful
	 */
	private boolean integrateProjectIntoInstallationPackage() {

		File installationPackageFile = this.exportSettings.getInstallationPackage().getPacakgeFile();
		HashMap<File, String> foldersToAdd = this.buildFoldersToAddHasmap();
		this.getArchiveFileHandler().appendFoldersToArchive(installationPackageFile, this.exportSettings.getTargetFile(), foldersToAdd, true);
		return true;
	}

	/**
	 * Builds a HashMap containing the folders to be added to the installation package archive as keys and their paths inside the archive as values
	 * @return the HashMap
	 */
	protected HashMap<File, String> buildFoldersToAddHasmap() {
		HashMap<File, String> foldersToAdd = new HashMap<>();
		// --- Add the project directory ---------------------
		String pathInArchive;
		if (this.exportSettings.getInstallationPackage().isForMac() == true) {
			pathInArchive = PROJECT_PATH_MAC;
		} else {
			pathInArchive = PROJECT_PATH_WINDOWS_LINUX;
		}
		foldersToAdd.put(this.getTempFolderPath().toFile(), pathInArchive);
		return foldersToAdd;
	}

	/**
	 * This is called after exporting the project. In can be overridden by subclasses to provide specific success/failure messages.
	 * @param success the indicator, if a success message is to be shown
	 */
	protected void showResultMessage(boolean success) {
		// --- Show a feedback message to the user --------------------
		if (success == true) {
			if (this.getMessageSuccess().isEmpty()==false) System.out.println(this.getMessageSuccess());
			if (this.showUserDialogs == true && this.confirmationDialogDisabled == false) {
				String messageTitle = Language.translate("Export erfolgreich");
				String messageContent = Language.translate("Projekt") + " " + project.getProjectName() + " " + Language.translate("erfolgreich exportiert!");
				AwbMessageDialog.showMessageDialog(null, messageContent, messageTitle, AwbMessageDialog.INFORMATION_MESSAGE);
			}
		} else {
			if (this.getMessageFailure().isEmpty()==false) System.err.println(this.getMessageFailure());
			if (this.showUserDialogs == true && this.confirmationDialogDisabled == false) {
				String message = Language.translate("Export fehlgeschlagen");
				AwbMessageDialog.showMessageDialog(null, message, message, AwbMessageDialog.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Sets the text for the success message
	 * @param newMessageSuccess the new text for the success message
	 */
	public void setMessageSuccess(String newMessageSuccess) {
		this.messageSuccess = newMessageSuccess;
	}
	
	/**
	 * Gets the text for the success message
	 * @return the text for the success message
	 */
	public String getMessageSuccess() {
		if (messageSuccess==null) {
			messageSuccess = "Project '" + project.getProjectName() + "' export successful!";
		}
		return messageSuccess;
	}
	
	/**
	 * Sets the text for the failure message.
	 * @param newMessageFailure the new text for the failure message
	 */
	public void setMessageFailure(String newMessageFailure) {
		this.messageFailure = newMessageFailure;
	}
	
	/**
	 * Gets the text for the failure message.
	 * @return the text for the failure message
	 */
	public String getMessageFailure() {
		if (messageFailure==null) {
			messageFailure = "Project '" + project.getProjectName() + "' export failed!";
		}
		return messageFailure;
	}
	
	/**
	 * Does the actual project export.
	 */
	private void doExport() {
		
		this.updateProgressMonitor(0);

		// --- Copy the required data to a temporary folder -------------------
		boolean success = DefaultProjectExportController.this.copyProjectDataToTempFolder();
		this.updateProgressMonitor(10);

		if (success==true) {
			// --- Write new project file and remove setups -------------------
			success = this.performProjectModificationsForExport();
			this.updateProgressMonitor(30);

			if (success == true) {
				if (DefaultProjectExportController.this.exportSettings.isIncludeInstallationPackage()) {
					// --- Integrate project into the installation package ----
					success = DefaultProjectExportController.this.integrateProjectIntoInstallationPackage();
				} else {
					// --- Zip the temporary folder ---------------------------
					success = this.getArchiveFileHandler().compressFolder(this.getTempFolderPath().toFile(), DefaultProjectExportController.this.exportSettings.getTargetFile());
					try {
						new RecursiveFolderDeleter().deleteFolder(this.getTempFolderPath());
					} catch (IOException e) {
						System.err.println("[ProjectExportController]: Error deleting temporary export folder " + this.getTempFolderPath().toFile().getAbsolutePath());
						e.printStackTrace();
					}
				}
				this.updateProgressMonitor(80);
	
				this.updateProgressMonitor(100);
				this.disposeProgressMonitor();
			}
			this.showResultMessage(success);
		}
		this.exportSuccessful = success;
	}
	
	/**
	 * This method performs some modifications on the project instance that will be exported.
	 * @return true if successful
	 */
	private boolean performProjectModificationsForExport() {
		
		// --- Load the project file from the temp folder ---------
		File projectFolder = this.getTempFolderPath().toFile();
		String projectFolderPath = projectFolder.toString() + File.separator;

		Project projectForExport = Project.load(projectFolder, false);
		projectForExport.setProjectFolderFullPath(projectFolderPath);
		
		// --- Remove authorization settings ------------------------
		if (this.getExportSettings().isIncludeAuthorizationSettings()==false) {
			projectForExport.setUpdateAuthorization(null);
		}
		
		// --- Remove setups that are not included in the export ----
		this.removeUnexportedSetupsFromList(projectForExport);
		
		// --- Allow subclasses to perform their modifications ------
		projectForExport = this.modifyProjectForExport(projectForExport);
		
		// --- Save the modified project to the temp folder ---------
		if (projectForExport!=null) {
			projectForExport.save(projectFolder, false, true);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Override this callback method to perform changes on the project instance before it is exported.
	 * Don't call it directly, it will be invoked by the superclass during the export process.
	 * @param projectForExport the project instance that was loaded from the temporary export folder
	 * @return the modified project instance, or null in case of failure
	 */
	protected Project modifyProjectForExport(Project projectForExport) {
		// --- Callback method for subclasses. All modifications for this class are done in performProjectModificationsForExport()
		return projectForExport;
	}

	/**
	 * Gets the progress monitor.
	 * @return the progress monitor
	 */
	private AwbProgressMonitor getProgressMonitor() {
		if (this.progressMonitor==null && Application.isOperatingHeadless()==false) {
			String title = Language.translate("Projekt-Export");
			String header = Language.translate("Exportiere Projekt") + " " + project.getProjectName();
			String progress = Language.translate("Exportiere") + "...";
			this.progressMonitor = AgentWorkbenchUiManager.getInstance().getProgressMonitor(title, header, progress);
		}
		return this.progressMonitor;
	}

	/**
	 * Updates the progress monitor.
	 * @param currentProgress the current progress
	 */
	protected void updateProgressMonitor(final int currentProgress) {

		if (this.showUserDialogs==true) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					
					AwbProgressMonitor pm = getProgressMonitor();
					if (pm == null)
						return;
					// --- Show progress monitor if not visible ---------
					if (pm.isVisible() == false) {
						pm.setVisible(true);
					}
					// --- Set progress ---------------------------------
					pm.setProgress(currentProgress);
				}
			});
		}
	}

	/**
	 * Disposes the progress monitor.
	 */
	private void disposeProgressMonitor() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AwbProgressMonitor pm = getProgressMonitor();
				if (pm == null)
					return;
				pm.setVisible(false);
				pm.dispose();
			}
		});
	}

	/**
	 * Gets the archive file handler.
	 * @return the archive file handler
	 */
	protected ArchiveFileHandler getArchiveFileHandler() {
		if (archiveFileHandler==null) {
			archiveFileHandler = new ArchiveFileHandler();
		}
		return archiveFileHandler;
	}


	/**
	 * Gets the project.
	 * @return the project
	 */
	protected Project getProject() {
		if (project==null) {
			project = Application.getProjectFocused();
		}
		return project;
	}
	

	/**
	 * Sets the project.
	 * @param project the new project
	 */
	protected void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Checks if is show user dialogs.
	 * @return true, if is show user dialogs
	 */
	protected boolean isShowUserDialogs() {
		return showUserDialogs;
	}


	/**
	 * Gets the temp folder path.
	 * @return the temp folder path
	 */
	protected Path getTempFolderPath() {
		return tempFolderPath;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.project.transfer.ProjectExportController#isExportSuccessful()
	 */
	@Override
	public boolean isExportSuccessful() {
		return exportSuccessful;
	}
	
	/**
	 * Allows to disable the confirmation dialog independent of the progress monitor .
	 * @param confirmationDialogEnabled the indicator to enable / disable the confirmation dialog 
	 */
	public void setConfirmationDialogDisabled(boolean confirmationDialogEnabled) {
		this.confirmationDialogDisabled = confirmationDialogEnabled;
	}


	/**
	 * This Thread does the actual export.
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ProjectExportThread extends Thread {
		@Override
		public void run() {
			doExport();
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.project.transfer.ProjectExportController#getAdditionalSetupFiles(java.lang.String)
	 */
	@Override
	public ArrayList<File> getAdditionalSetupFiles(String setupName) {
		/* 
		 * Empty default implementation. Override this method in 
		 * subclasses to specify additional setup-related files.
		 */
		return new ArrayList<File>();
	}


	@Override
	public ArrayList<Path> getDefaultExcludeList() {
		ArrayList<Path> defaultExcludeList = new ArrayList<>();
		defaultExcludeList.add(this.getProjectFolderPath().resolve(Project.DEFAULT_TEMP_FOLDER));
		defaultExcludeList.add(this.getProjectFolderPath().resolve(Project.DEFAULT_SUB_FOLDER_SECURITY));
		return defaultExcludeList;
	}


	/**
	 * Gets the project folder path.
	 * @return the project folder path
	 */
	protected Path getProjectFolderPath() {
		if (projectFolderPath==null) {
			projectFolderPath = new File(this.getProject().getProjectFolderFullPath()).toPath();
		}
		return projectFolderPath;
	}

	
}
