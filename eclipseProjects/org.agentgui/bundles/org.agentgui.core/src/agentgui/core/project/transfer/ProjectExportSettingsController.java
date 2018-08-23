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
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import agentgui.core.application.Application;
import agentgui.core.project.Project;

/**
 * This class can be used to manage {@link ProjectExportSettings}. 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportSettingsController {
	
	private ProjectExportSettings projectExportSettings;
	
	private Project project;
	private ProjectExportController projectExportController;
	
	private HashMap<File, String> fileToSetupHash;
	private HashMap<String, ArrayList<File>> setupToFilesHash;
	
	/**
	 * Instantiates a new project export settings controller.
	 * @param project the project
	 */
	public ProjectExportSettingsController(Project project) {
		this(project, null);
	}
	
	/**
	 * Instantiates a new project export settings controller.
	 * @param project the project
	 * @param projectExportController the project export controller
	 */
	public ProjectExportSettingsController(Project project, ProjectExportController projectExportController) {
		this.project = project;
		this.projectExportController = projectExportController;
		this.evaluateSetupFiles();
	}

	/**
	 * Gets the project.
	 * @return the project
	 */
	private Project getProject() {
		return project;
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
			fileToSetupHash = new HashMap<>();
		}
		return fileToSetupHash;
	}
	
	/**
	 * Gets the setup to files hash.
	 * @return the setup to files hash
	 */
	private HashMap<String, ArrayList<File>> getSetupToFilesHash() {
		if (setupToFilesHash==null) {
			setupToFilesHash = new HashMap<>();
		}
		return setupToFilesHash;
	}
	
	
	/**
	 * Evaluates the available setup files.
	 */
	private void evaluateSetupFiles() {
		
		this.getFileToSetupHash().clear();
		this.getSetupToFilesHash().clear();

		String setupPath = this.getProject().getSubFolder4Setups(true);
		String envModelPath = this.getProject().getEnvironmentController().getEnvFolderPath();
		
		// --- Check all setups -------------------------------------
		List<String> simSetups = new ArrayList<>(this.getProject().getSimulationSetups().keySet());
		for (int i = 0; i < simSetups.size(); i++) {
			
			// --- Get all files related to the setup ---------------
			final String setupName = simSetups.get(i);
			String fileNameXML = this.getProject().getSimulationSetups().get(setupName);
			String fileNameBIN = Application.getGlobalInfo().getBinFileNameFromXmlFileName(fileNameXML);
			
			File fileXML = this.getFileFromPath(setupPath + fileNameXML);
			File fileBIN = this.getFileFromPath(setupPath + fileNameBIN);
			
			File envDirectory = new File(envModelPath);
			File[] envFiles =envDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(setupName + ".");
				}
			});

			// --- Remind relation setup to file and vice versa -----
			ArrayList<File> setupFiles = new ArrayList<>();
			if (fileXML!=null) setupFiles.add(fileXML);
			if (fileBIN!=null) setupFiles.add(fileBIN);

			this.getFileToSetupHash().put(fileXML, setupName);
			this.getFileToSetupHash().put(fileBIN, setupName);
			for (int j = 0; j < envFiles.length; j++) {
				this.getFileToSetupHash().put(envFiles[j], setupName);
				setupFiles.add(envFiles[j]);
			}
			
			this.getSetupToFilesHash().put(setupName, setupFiles);
			
			// --- Remind additional setup files, too --------------- 
			if (this.getProjectExportController()!=null) {
				ArrayList<File> additionalSetupFiles = this.getProjectExportController().getAdditionalSetupFiles(setupName);
				if (additionalSetupFiles!=null && additionalSetupFiles.size()>0) {
					// --- Add to the list of setups --------------------
					setupFiles.addAll(additionalSetupFiles);
					// --- Add to Hash File <-> setup -------------------
					for (int j = 0; j < additionalSetupFiles.size(); j++) {
						this.getFileToSetupHash().put(additionalSetupFiles.get(j), setupName);
					}
				}
			}
			
		} // end setup loop
	}
	
	/**
	 * Returns the file specified by the path and check if it exists.
	 * @param filePath the file path
	 * @return the file from path
	 */
	private File getFileFromPath(String filePath) {
		File file = new File(filePath);
		if (file.exists()==false) return null;
		return file;
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
		List<Path> excludeList = this.getProjectExportSettings().getFileExcludeList();
		for (int i=0; i<setupFiles.size(); i++) {
			Path setupFilePath = setupFiles.get(i).toPath();
			if (excludeList.contains(setupFilePath)) {
				excludeList.remove(setupFilePath);
			}
		}
	}
	
	/**
	 * Excludes the specified simulation setup from the export
	 * @param setupName the setup name
	 */
	public void excludeSimulationSetup(String setupName) {
		// --- Add all files related to the setup to the exclude list ---------
		List<File> setupFiles = this.getFilesForSetup(setupName);
		List<Path> excludeList = this.getProjectExportSettings().getFileExcludeList();
		for (int i=0; i<setupFiles.size(); i++) {
			Path setupFilePath = setupFiles.get(i).toPath();
			if (excludeList.contains(setupFilePath)==false) {
				excludeList.add(setupFilePath);
			}
		}
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

	public void addDefaultsToExcludeList() {
		this.getProjectExportSettings().getFileExcludeList().addAll(this.getProjectExportController().getDefaultExcludeList());
	}
}
