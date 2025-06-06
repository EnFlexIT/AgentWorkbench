package de.enflexit.awb.core.project.transfer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;


/**
 * This class can be used to manage {@link ProjectExportSettings}. 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportSettingsController {
	
	private ProjectExportSettings projectExportSettings;
	
	private Project project;
	private Path projectFolderPath;
	private ProjectExportController projectExportController;
	
	private HashMap<File, String> fileToSetupHash;
	private HashMap<String, ArrayList<File>> setupToFilesHash;
	
	private List<Path> fileExcludeList;
	
	
	/**
	 * Instantiates a new project export settings controller.
	 * @param project the project
	 */
	public ProjectExportSettingsController(Project project) {
		this(project, null, null);
	}
	/**
	 * Instantiates a new project export settings controller.
	 * @param projectExportSettings the project export settings
	 */
	public ProjectExportSettingsController(ProjectExportSettings projectExportSettings) {
		this(null, projectExportSettings, null);
	}
	/**
	 * Instantiates a new project export settings controller.
	 * @param project the project
	 * @param projectExportController the project export controller
	 */
	public ProjectExportSettingsController(Project project, ProjectExportController projectExportController) {
		this(project, null, projectExportController);
	}
	
	/**
	 * Instantiates a new project export settings controller.
	 * @param project the project
	 * @param projectExportSettings the project export settings
	 * @param projectExportController the project export controller
	 */
	public ProjectExportSettingsController(Project project, ProjectExportSettings projectExportSettings, ProjectExportController projectExportController) {
		this.project = project;
		this.projectExportSettings = projectExportSettings;
		this.projectExportController = projectExportController;
	}

	/**
	 * Gets the project.
	 * @return the project
	 */
	private Project getProject() {
		if (project==null) {
			project = Application.getProjectFocused();
		}
		return project;
	}
	
	/**
	 * Gets the project folder path.
	 * @return the project folder path
	 */
	private Path getProjectFolderPath() {
		if (projectFolderPath==null) {
			projectFolderPath = new File(this.getProject().getProjectFolderFullPath()).toPath();
		}
		return projectFolderPath;
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
	 * Gets the file to setup hash.
	 * @return the file to setup hash
	 */
	private HashMap<File, String> getFileToSetupHash() {
		if (fileToSetupHash==null) {
			this.evaluateSetupFiles();
		}
		return fileToSetupHash;
	}
	/**
	 * Gets the setup to files hash.
	 * @return the setup to files hash
	 */
	private HashMap<String, ArrayList<File>> getSetupToFilesHash() {
		if (setupToFilesHash==null) {
			this.evaluateSetupFiles();
		}
		return setupToFilesHash;
	}
	/**
	 * Evaluates the available setup files.
	 */
	private void evaluateSetupFiles() {
		
		boolean isDebug = false;
		fileToSetupHash = new HashMap<>();
		setupToFilesHash = new HashMap<>();
		
		// --- Check all setups -------------------------------------
		List<String> simSetups = new ArrayList<>(this.getProject().getSimulationSetups().keySet());
		for (int i = 0; i < simSetups.size(); i++) {
			
			String setupName = simSetups.get(i);
			
			// --- Get all files related to the setup ---------------
			List<File> setupFiles = this.getProject().getSimulationSetups().getSetupFiles(setupName).stream().distinct().collect(Collectors.toList());
			if (isDebug) System.out.println((i>0 ? "\n" : "") + "[" + this.getClass().getSimpleName() + "] Files for Setup '" + setupName + "' (" + setupFiles.size() + " files found):");
			
			ArrayList<File> setupFileListFound = new ArrayList<>();
			for (int j = 0; j < setupFiles.size(); j++) {
				File setupFile = setupFiles.get(j);
				if (isDebug) System.out.println("[" + this.getClass().getSimpleName() + "] " + setupFile.toString());
				
				if (setupFile.exists()==true) {
					// --- Remind setup to file and vice versa ------
					setupFileListFound.add(setupFile);
					this.getFileToSetupHash().put(setupFile, setupName);
				}
			}
			this.getSetupToFilesHash().put(setupName, setupFileListFound);
			
			// --- Remind additional setup files, too --------------- 
			if (this.getProjectExportController()!=null) {
				ArrayList<File> additionalSetupFiles = this.getProjectExportController().getAdditionalSetupFiles(setupName);
				if (additionalSetupFiles!=null && additionalSetupFiles.size()>0) {
					// --- Add to the list of setups --------------------
					setupFileListFound.addAll(additionalSetupFiles);
					// --- Add to Hash File <-> setup -------------------
					for (int j = 0; j < additionalSetupFiles.size(); j++) {
						this.getFileToSetupHash().put(additionalSetupFiles.get(j), setupName);
					}
				}
			}
			
		} // end setup loop
	}
	
	/**
	 * Gets the files for setup.
	 * @param setupName the setup name
	 * @return the files for setup
	 */
	public List<File> getFilesForSetup(String setupName) {
		return this.getSetupToFilesHash().get(setupName);
	}
	
	/**
	 * Gets the setup for file.
	 * @param file the file
	 * @return the setup for file
	 */
	public String getSetupForFile(File file) {
		return this.getFileToSetupHash().get(file);
	}

	/**
	 * Gets the project export settings.
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings() {
		if (projectExportSettings==null) {
			projectExportSettings = new ProjectExportSettings();
		}
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
	 * Includes the specified simulation setup in the export
	 * @param setupName the setup name
	 */
	public void includeSimulationSetup(String setupName) {
		// --- Remove all files related to the setup from the exclude list ----
		List<File> setupFiles = this.getFilesForSetup(setupName);
		for (int i=0; i<setupFiles.size(); i++) {
			Path setupFilePath = setupFiles.get(i).toPath();
			if (this.getFileExcludeList().contains(setupFilePath)) {
				this.removeFileFromExcludeList(setupFilePath);
			}
		}
		// --- Add the setup to the list of included setups -------------------
		this.projectExportSettings.getSimSetups().add(setupName);
	}
	
	/**
	 * Excludes the specified simulation setup from the export
	 * @param setupName the setup name
	 */
	public void excludeSimulationSetup(String setupName) {
		// --- Add all files related to the setup to the exclude list ---------
		List<File> setupFiles = this.getFilesForSetup(setupName);
		for (int i=0; i<setupFiles.size(); i++) {
			Path setupFilePath = setupFiles.get(i).toPath();
			if (this.getFileExcludeList().contains(setupFilePath)==false) {
				this.addFileToExcludeList(setupFilePath);
			}
		}
		// --- Remove the setup from the list of included setups --------------
		this.projectExportSettings.getSimSetups().remove(setupName);
	}
	
	/**
	 * Sets the include all setups.
	 * @param incluceAllSetups the new include all setups
	 */
	public void setIncludeAllSetups(boolean incluceAllSetups) {
		List<String> setupsList = new ArrayList<>(this.getProject().getSimulationSetups().keySet());
		for (int i=0; i<setupsList.size(); i++) {
			if (incluceAllSetups==true) {
				this.includeSimulationSetup(setupsList.get(i));
			} else {
				this.excludeSimulationSetup(setupsList.get(i));
			}
		}
	}
	/**
	 * Include installation package.
	 * @param targetOS the target OS
	 */
	public void includeInstallationPackage(String targetOS) {
		this.getProjectExportSettings().setIncludeInstallationPackage(true);
		this.getProjectExportSettings().setTargetOS(targetOS);
	}
	
	/**
	 * Exclude installation package.
	 */
	public void excludeInstallationPackage() {
		this.getProjectExportSettings().setIncludeInstallationPackage(false);
		this.getProjectExportSettings().setTargetOS(null);
	}
	
	/**
	 * Sets the export target file.
	 * @param targetFile the new export target file
	 */
	public void setTargetFile(File targetFile) {
		this.getProjectExportSettings().setTargetFile(targetFile);
	}
	
	/**
	 * Sets the include installation package.
	 * @param includeInstallationPackage the new include installation package
	 */
	public void setIncludeInstallationPackage(boolean includeInstallationPackage) {
		this.getProjectExportSettings().setIncludeInstallationPackage(includeInstallationPackage);
	}
	
	/**
	 * Gets the export target file.
	 * @return the export target file
	 */
	public File getExportTargetFile() {
		return this.getProjectExportSettings().getTargetFile();
	}

	/**
	 * Adds the default files to the exclude list.
	 */
	public void addDefaultsToExcludeList() {
		List<Path> defaultExcludes = this.getProjectExportController().getDefaultExcludeList();
		for (int i=0; i<defaultExcludes.size(); i++) {
			this.addFileToExcludeList(defaultExcludes.get(i));
		}
	}
	
	/**
	 * Gets the file exclude list.
	 * @return the file exclude list
	 */
	public List<Path> getFileExcludeList(){
		if (fileExcludeList==null) {
			fileExcludeList = new ArrayList<>();
			// --- Populate with the entries from the settings -----------
			List<String> excludeListInternal = this.getProjectExportSettings().getFileExcludeListInternal();
			for(int i=0; i<excludeListInternal.size(); i++) {
				Path excludeFilePath =  this.getProjectFolderPath().resolve(excludeListInternal.get(i));
				fileExcludeList.add(excludeFilePath);
			}
		}
		return fileExcludeList;
	}
	
	/**
	 * Adds a file to the exclude list.
	 * @param file the file
	 */
	public void addFileToExcludeList(File file) {
		this.addFileToExcludeList(file.toPath());
	}
	
	/**
	 * Adds a file to the exclude list.
	 * @param filePath the file path
	 */
	public void addFileToExcludeList(Path filePath) {
		this.getFileExcludeList().add(filePath);
		Path relativePath = this.getProjectFolderPath().relativize(filePath);
		this.projectExportSettings.getFileExcludeListInternal().add(relativePath.toString());
	}
	
	/**
	 * Adds the files to exclude list.
	 * @param files the files
	 */
	public void addFilesToExcludeList(List<Path> files) {
		for (int i=0; i<files.size(); i++) {
			this.addFileToExcludeList(files.get(i));
		}
	}
	
	/**
	 * Removes a file from the exclude list.
	 * @param file the file
	 */
	public void removeFileFromExcludeList(File file) {
		this.removeFileFromExcludeList(file.toPath());
	}
	
	/**
	 * Removes a file from the exclude list.
	 * @param filePath the file path
	 */
	public void removeFileFromExcludeList(Path filePath) {
		this.getFileExcludeList().remove(filePath);
		Path relativePath = this.getProjectFolderPath().relativize(filePath);
		this.projectExportSettings.getFileExcludeListInternal().remove(relativePath.toString());
	}
}
