package agentgui.core.project;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.agentgui.gui.AwbProjectExportDialog;
import org.agentgui.gui.AwbProjectNewOpenDialog;
import org.agentgui.gui.UiBridge;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener.ApplicationEvent;
import agentgui.core.config.BundleProperties;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentController.PersistenceStrategy;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.project.transfer.DefaultProjectExportController;
import agentgui.core.project.transfer.ProjectExportController;
import agentgui.core.project.transfer.ProjectExportControllerProvider;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.project.transfer.ProjectImportController;
import agentgui.core.project.transfer.ProjectImportSettings;
import agentgui.core.project.transfer.gui.ProjectExportDialog;
import agentgui.core.update.ProjectRepositoryExport;
import agentgui.core.update.repositoryModel.RepositoryEntry;
import de.enflexit.language.Language;

/**
 * This class holds the list of the projects, that are currently open
 * within Agent.GUI and offers methods to deal with them.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectsLoaded {

	/**
	 * The enumeration describing the ProjectAction.
	 */
	public enum ProjectAction {
		NewProject,
		OpenProject,
		ExportProject,
		DeleteProject
	}
	
	
	// --- Listing of the open projects -------------------
	private ArrayList<Project> projectsOpen;
	
	/**
	 * Returns the list of currently open projects.
	 * @return the projects open
	 */
	public ArrayList<Project> getProjectsOpen() {
		if (projectsOpen==null) {
			projectsOpen = new ArrayList<>();
		}
		return projectsOpen;
	}
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application.
	 * @param addNew the add new. If set false, the user dialog for selecting a project to open will be shown
	 * @return The Project-instance that was added or <code>null</code>.
	 */
	public Project add(boolean addNew) {
		return this.add(addNew, null, null, null, null);
	}
	/**
	 * Open a project corresponding to specified project folder.
	 *
	 * @param selectedProjectFolder the selected project folder from the projects directory 
	 * @return the Project-instance that was added here
	 */
	public Project add(String selectedProjectFolder) {
		return this.add(false, selectedProjectFolder, null, null, null);
	}
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application.
	 *
	 * @param addNew the add new
	 * @param selectedProjectFolder the selected project folder
	 * @param mApplication the eclipse MApplication
	 * @param ePartService the eclipse EPpartService
	 * @param eModelService the eclipse EMmodelService
	 * @return The Project-instance that was added here
	 */
	public Project add(boolean addNew, String selectedProjectFolder, MApplication mApplication, EPartService ePartService, EModelService eModelService) {

		ProjectAction action = null;
		String actionTitel = null;
		String projectNameTest = null;
		String projectFolderTest = null;
		String localTmpProjectName = null;
		String localTmpProjectFolder = null;
		
		// --- Start argument for "New" oder "Open" ---------------------------
		if (addNew == true){
			// --- New Project ------------------------------------------------
			action = ProjectAction.NewProject;
			actionTitel = Language.translate("Neues Projekt anlegen");
			
			// --- Define an initial project name -----------------------------
			String ProjectNamePrefix = Language.translate("Neues Projekt");
			projectNameTest = ProjectNamePrefix;
			int index = Application.getProjectsLoaded().getIndexByName(projectNameTest);
			int i = 2;
			while (index!=-1) {
				projectNameTest = ProjectNamePrefix + " " + i;
				index = Application.getProjectsLoaded().getIndexByName(projectNameTest);
				i++;
			}
			projectFolderTest = projectNameTest.toLowerCase().replace(" ", "_");
		
		} else {
			// --- Open existing project --------------------------------------
			action = ProjectAction.OpenProject;
			actionTitel = Language.translate("Projekt öffnen");			
		}
		Application.setStatusBarMessage(actionTitel + " ...");
		
		if (selectedProjectFolder==null) {
			// --- Open user dialog -------------------------------------------
			AwbProjectNewOpenDialog newProDia = UiBridge.getInstance().getProjectNewOpenDialog(Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitel, action);
			newProDia.setProjectName(projectNameTest);
			newProDia.setProjectDirectory(projectFolderTest);
			newProDia.setVisible(true);
			// === Wait for user here =========================================
			if (newProDia.isCanceled()==true || newProDia.getProjectDirectory()==null || newProDia.getProjectDirectory().isEmpty()==true) {
				Application.setStatusBarMessageReady();
				return null;
			} else {
				localTmpProjectName = newProDia.getProjectName();
				localTmpProjectFolder = newProDia.getProjectDirectory(); 
			}
			newProDia.close();
			newProDia = null;	
			
		} else {
			// --- take project folder from start argument --------------------
			localTmpProjectName = null;
			localTmpProjectFolder = selectedProjectFolder;
		}

		// --------------------------------------------------------------------
		// --- Define the project instance ------------------------------------
		// --------------------------------------------------------------------
		Project newProject = null;
		if (addNew==true) {			
			// --- Set project variables --------------------------------------
			newProject = new Project();
			newProject.setProjectName(localTmpProjectName);
			newProject.setProjectFolder(localTmpProjectFolder);
			// --- Create default project structure ---------------------------
			newProject.checkAndCreateProjectsDirectoryStructure();
			
		} else {
			// --- Get data model from file -----------------------------------
			newProject = Project.load(localTmpProjectFolder);
			if (newProject==null) {
				return null;
			}
		}
		
		// --------------------------------------------------------------------
		// --- Set required Eclipse instances ---------------------------------
		newProject.setEclipseMApplication(mApplication);
		newProject.setEclipseEPartService(ePartService);
		newProject.setEclipseEModelService(eModelService);
		
		// --- Possibly, set the AWB default JADE configuration ---------------
		if (addNew==true) {
			newProject.setJadeConfiguration(Application.getGlobalInfo().getJadeDefaultPlatformConfig());
		}
		
		// --- Inform ApplicationListener that the project files were loaded --
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_PROJECT_FILES_LOADED, newProject));
		
		// --- Is there already a simulation setup? ---------------------------
		if (newProject.getSimulationSetups().size()==0) {
			// --- Create default simulations setup ---------------------------
			newProject.getSimulationSetups().setupCreateDefault();			
		}
		// --- Load the setup configured -------------------------------------- 
		newProject.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, newProject.getSimulationSetupCurrent(), false);
		
		// --- Load configured PlugIns ----------------------------------------
		newProject.plugInVectorLoad();

		// --- Load the TimeModelController -----------------------------------
		newProject.getTimeModelController();

		// --- Load the environment controller --------------------------------
		EnvironmentController envController = newProject.getEnvironmentController();
		if (envController!=null) {
			envController.callLoadEnvironment(PersistenceStrategy.HandleWithProjectOpenOrSave);
		}
		
		// --- Add project to the project-listing -----------------------------
		this.getProjectsOpen().add(newProject);
		Application.setProjectFocused(newProject);

		// --- Configure the project in the main window -----------------------
		if (Application.getMainWindow()!=null || UiBridge.getInstance().isWorkbenchRunning()==true) {

			// --- Initiate project-window and the default tabs ---------------
			newProject.setMaximized();
			
			this.setProjectView();
			newProject.setChangedAndNotify(Project.VIEW_TabsLoaded);
			newProject.plugInVectorInformSetupLoaded();
			Application.setTitleAddition(newProject.getProjectName());
			Application.setStatusBarMessageReady();
			
		}

		// --- Inform ApplicationListener that the project was loaded ---------
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_LOADED, newProject));
		
		if (addNew==true) {
			// --- Save project for the first time ----------------------------
			newProject.save();   	
		} else {
			// --- Set Project to saved ---------------------------------------
			newProject.setUnsaved(false);
			// --- Check for a project update ---------------------------------
			newProject = newProject.doProjectUpdate(false);
		}
		return newProject;
	}

	/**
	 * This method will try to close all open projects.
	 * @return Returns true on success
	 */
	public boolean closeAll() {
		return this.closeAll(null, false);
	}
	/**
	 * This method will try to close all open projects.
	 * @param parentComponent the parent component (for messages by using the {@link JOptionPane})
	 * @return Returns true on success
	 */
	public boolean closeAll(Component parentComponent) {
		return closeAll(parentComponent, false);
	}
	/**
	 * This method will try to close all open projects.
	 * @param parentComponent the parent component (for messages by using the {@link JOptionPane})
	 * @param isSkipSaving set true, if you want to skip user interactions for saving a project
	 * @return Returns true on success
	 */
	public boolean closeAll(Component parentComponent, boolean isSkipSaving) {		
		while (Application.getProjectFocused()!=null) {
			if (Application.getProjectFocused().close(parentComponent, isSkipSaving)==false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the Project-instance given by its project name.
	 * 
	 * @param projectName the project name
	 * @return the Project instance
	 */
	public Project get(String projectName) {
		int index = this.getIndexByName(projectName);
		if (index == -1 ) {
			// --- if the folder name was used ---
			index = this.getIndexByFolderName(projectName);
		}
		return get(index);
	}
	/**
	 * Returns the Project-instance given by its index.
	 * @param indexOfProject the index of project
	 * @return the Project instance
	 */
	public Project get(int indexOfProject) {
		return this.getProjectsOpen().get(indexOfProject);
	}

	/**
	 * Removes a single Project.
	 * 
	 * @param project2Remove the project to remove
	 */
	public void remove(Project project2Remove) {

		// --- Remove from local array list -----------------------------------
		this.getProjectsOpen().remove(project2Remove);
		
		// --- Inform ApplicationListener that the project was closed ---------
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_CLOSED, project2Remove));

		// --- Adjust visualization / MainWindow ------------------------------
		if (this.getProjectsOpen().size()==0) {
			Application.setProjectFocused(null);
		}
		this.setProjectView();
	}

	/**
	 * Identifies a Project by its name and returns the Array-/Window-Index.
	 * 
	 * @param projectName the project name
	 * @return the index position of a project
	 */
	public int getIndexByName(String projectName) {
		int index = -1;
		for(int i=0; i<this.count(); i++) {
			if (this.getProjectsOpen().get(i).getProjectName().equalsIgnoreCase(projectName) ) {
				index = i;
				break;
			}	
		}
		return index;
	}
	
	/**
	 * Identifies a Project by its Root-Folder-Name and returns the Array-/Window-Index.
	 *
	 * @param projectFolderName the project folder name
	 * @return The index position of a project
	 */
	public int getIndexByFolderName(String projectFolderName) {
		int index = -1;
		for(int i=0; i<this.count(); i++) {
			if (this.getProjectsOpen().get(i).getProjectFolder().toLowerCase().equalsIgnoreCase( projectFolderName.toLowerCase() ) ) {
				index = i;
				break;
			}	
		}
		return index;
	}
	
	/**
	 * Counts the actual open projects.
	 * @return the number of projects that are currently loaded
	 */
	public int count() {
		return this.getProjectsOpen().size();		
	}

	/**
	 * Configures the appearance of the application, depending on the current project configuration
	 */
	public void setProjectView() {
		if (Application.getMainWindow()!=null) {
			Application.getMainWindow().setProjectView();
		}
	}
	
	
	
	/**
	 * Imports a project, which is packed in Agent.GUI project file (*.agui)
	 */
	public void projectImport() {
		
		// --- Select a *.agui file -----------------------
		String fileEnd = Application.getGlobalInfo().getFileEndProjectZip();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileEnd + ")", fileEnd);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int answerChooser = fileChooser.showDialog(Application.getMainWindow(), Language.translate("Projekt importieren"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) return;
		Application.getGlobalInfo().setLastSelectedFolder(fileChooser.getCurrentDirectory());
		
		// --- Start the project import controller ---------
		File projectArchiveFile = fileChooser.getSelectedFile();
		if (projectArchiveFile!=null && projectArchiveFile.exists()) {
			// --- Define the Import settings --------------
			ProjectImportSettings pims  = new ProjectImportSettings(projectArchiveFile);
			pims.setExtractInThread(true);
			// --- Start the import -----------------------
			ProjectImportController pic = new ProjectImportController(pims);
			pic.doProjectImport();
		}
		
	}

	/**
	 * Exports a project to a file
	 */
	public void projectExport() {

		// --- If no project is loaded, select a project for export -----------
		Project project = Application.getProjectFocused();
		if (project==null) {
			project = this.selectProjectForExport();
			if (project==null) return;
		}
		
		// --- Get the export settings ----------------------------------------
		ProjectExportController projectExportController = ProjectExportControllerProvider.getProjectExportController();
		ProjectExportSettings exportSettings = projectExportController.getProjectExportSettings(project);
		
		// --- Do the actual export, if not canceled --------------------------
		if (exportSettings!=null) {
			projectExportController.exportProject(project, exportSettings);
		}
	}
	
	/**
	 * Select and load the project to be exported
	 * @return the project
	 */
	private Project selectProjectForExport() {
		String actionTitle = Language.translate("Projekt zum Export auswählen");
		AwbProjectNewOpenDialog newProDia = UiBridge.getInstance().getProjectNewOpenDialog(Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitle, ProjectAction.ExportProject);
		newProDia.setVisible(true);
		if (newProDia.isCanceled() == true) {
			return null;
		} else {
			String projectSubFolder = newProDia.getProjectDirectory();
			newProDia.close();
			newProDia = null;
			return this.add(projectSubFolder);
		}
	}
	
	/**
	 * Exports the currently focused Project to the local repository.
	 */
	public void projectExportToRepository() {
	
		// --- If no project is loaded, select a project for export -----------
		Project project = Application.getProjectFocused();
		if (project==null) {
			project = this.selectProjectForExport();
			if (project==null) return;
		}

		// --------------------------------------------------------------------
		// --- Get the repository export configured ---------------------------
		// --------------------------------------------------------------------
		
		// --- Define the RepositoryEntry -------------------------------------
		RepositoryEntry repositoryEntry = new RepositoryEntry(project);
		
		// --- Define the target file name ------------------------------------
		String repositoryPath = Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_LOCAL_PROJECT_REPOSITORY, null);
		String fileName = repositoryEntry.getFileName();
		File targetFile = new File(repositoryPath + fileName);
		
		// --- Get a ProjectExportController instance -------------------------
		ProjectExportController projectExportController = ProjectExportControllerProvider.getProjectExportController();
		
		// --- Show the ProjectExportDialog -----------------------------------
		AwbProjectExportDialog projectExportDialog = new ProjectExportDialog(project, projectExportController);
		projectExportDialog.setAllowInstallationPackageConfiguration(false);
		projectExportDialog.setVisible(true);
		// - - Does the user action here - - - - - - - - - - - - - - - - - - -
		if (projectExportDialog.isCanceled()==true) return;

		// --- Get the export settings from the dialog ------------------------
		ProjectExportSettings exportSettings = projectExportDialog.getExportSettings();
		exportSettings.setTargetFile(targetFile);
		projectExportDialog.dispose();
		projectExportDialog = null;
		
		// --------------------------------------------------------------------
		// --- Finally define and export the project to the repository --------
		// --------------------------------------------------------------------
		ProjectRepositoryExport pre = new ProjectRepositoryExport(project, projectExportController);
		pre.setRepositoryEntry(repositoryEntry);
		pre.setRepositoryLocationDirectoryPath(repositoryPath);
		pre.setProjectExportSettings(exportSettings);
		pre.setShowUserDialogs(true);
		pre.start();
	}
	
	/**
	 * Can be used in order to delete a project.
	 */
	public void projectDelete() {
		
		String optionMsg = null;
		String optionTitle = null;
		String projectFolder = null;
		boolean exportBeforeDelete = false;
		
		String actionTitel = Language.translate("Projekt löschen");
		Application.setStatusBarMessage(actionTitel + " ...");
		
		// ----------------------------------------------------------
		// --- Open project selection dialog ------------------------
		AwbProjectNewOpenDialog newProDia = UiBridge.getInstance().getProjectNewOpenDialog(Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitel, ProjectAction.DeleteProject);
		newProDia.setVisible(true);
		// === Waiting for closing dialog ===
		if (newProDia.isCanceled()==true || newProDia.getProjectDirectory()==null || newProDia.getProjectDirectory().isEmpty()==true) {
			Application.setStatusBarMessageReady();
			return;
		} else {
			exportBeforeDelete = newProDia.isExportBeforeDelete();
			projectFolder = newProDia.getProjectDirectory();
		}
		newProDia.close();
		newProDia = null;	

	
		// ----------------------------------------------------------
		// --- If a project is open, ask to close this project ------
		if (Application.getProjectFocused()!=null) {
			
			int iProject = this.getIndexByFolderName(projectFolder);
			if (iProject>=0) {
			
				// --- The selected project is open -----------------
				Project currProject = this.get(iProject);	
				optionTitle = "" + currProject.getProjectName() + " - " + Language.translate("Projekt schließen");
				optionMsg = currProject.getProjectName() + ": " + Language.translate("Das Projekt wird nun geschlossen!");
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.OK_CANCEL_OPTION);
				if (answer==JOptionPane.CANCEL_OPTION) {
					Application.setStatusBarMessageReady();
					return;
				}
				// --- Close the project window ---------------------
				while (iProject>=0) {
					currProject = this.get(iProject);
					if (currProject.close()==false) {
						Application.setStatusBarMessageReady();
						return;
					}
					iProject = this.getIndexByFolderName(projectFolder);
				}
			}
		}
		
		// ----------------------------------------------------------
		// --- Export before delete ? -------------------------------
		if (exportBeforeDelete==true) {
			
			Project project = Project.load(projectFolder, false);
			DefaultProjectExportController exportController = (DefaultProjectExportController) ProjectExportControllerProvider.getProjectExportController();
			exportController.setConfirmationDialogDisabled(true);
			ProjectExportSettings exportSettings = exportController.getProjectExportSettings(project);
			
			Thread projectDeleteThread = new ProjectDeleteThread(project, exportController, exportSettings);
			projectDeleteThread.start();
		
		} else {

			// ----------------------------------------------------------
			// --- Delete the folder of the project ---------------------
			this.projectDelete(projectFolder);
			Application.setStatusBarMessageReady();
			
		}

	}
	
	/**
	 * Deletes the specified project sub directory (the last element in the path is enough).
	 * @param projectSubDirectory the project sub directory
	 */
	public void projectDelete(String projectSubDirectory) {
		String projectFolderFullPath = Application.getGlobalInfo().getPathProjects() + projectSubDirectory;
		this.projectDelete(new File(projectFolderFullPath));
	}
	/**
	 * Deletes the specified project sub directory 
	 * @param projectSubDirectory the project sub directory
	 */
	public void projectDelete(File projectSubDirectory) {

		if (projectSubDirectory==null) return;

		// --- Get the files and folders in the project folder ------
		System.out.println(Language.translate("Lösche Verzeichnis") +": " + projectSubDirectory.getAbsolutePath());
		Vector<File> files = this.getFilesAndFolders(projectSubDirectory.getAbsolutePath());
		for (int i = files.size()-1; i>-1 ; i--) {
			File file = files.get(i);
			file.delete();
		}
		// --- Delete the project directory itself ------------------
		projectSubDirectory.delete();
	}

	/**
	 * This method will evaluate the given folder and it's sub-folder recursively.
	 * The containing File objects will be returned in the Vector.
	 *
	 * @param srcFolder the folder to evaluate
	 * @return the files and folders
	 */
	private Vector<File> getFilesAndFolders(String srcFolder) {
		
		Vector<File> filesFound = new Vector<File>();
		
		File folder = new File(srcFolder);
		String listOfFiles[] = folder.list();
		for (int i = 0; i < listOfFiles.length; i++) {
			// --- If the current file should be included -----------
			File sngFileObject = new File(srcFolder + File.separator + listOfFiles[i]);
			filesFound.add(sngFileObject);
			if (sngFileObject.isDirectory()) {
				filesFound.addAll(this.getFilesAndFolders(sngFileObject.getAbsolutePath()));
			}
		} // end for
		return filesFound;
	}
	
	private class ProjectDeleteThread extends Thread{
		private Project project;
		private DefaultProjectExportController exportController;
		private ProjectExportSettings exportSettings;
		
		public ProjectDeleteThread(Project project, DefaultProjectExportController exportController, ProjectExportSettings exportSettings) {
			this.project = project;
			this.exportController = exportController;
			this.exportSettings = exportSettings;
		}

		@Override
		public void run() {
			if (exportSettings != null) {
				exportController.exportProject(project, exportSettings, true, false);
				if (exportController.isExportSuccessful() == true) {
					ProjectsLoaded.this.projectDelete(project.getProjectFolder());
					Application.setStatusBarMessageReady();
				}
			}
		}
		
	}
	
}
