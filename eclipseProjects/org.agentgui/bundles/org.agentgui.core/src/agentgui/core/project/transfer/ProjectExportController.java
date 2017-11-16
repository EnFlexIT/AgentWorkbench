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
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.agentgui.gui.ProjectNewOpenDialog;
import org.agentgui.gui.ProjectNewOpenDialog.ProjectAction;
import org.agentgui.gui.UiBridge;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.transfer.gui.ProjectExportDialog;
import de.enflexit.common.swing.ProgressMonitor;
import de.enflexit.common.transfer.ArchiveFileHandler;
import de.enflexit.common.transfer.RecursiveFolderCopier;
import de.enflexit.common.transfer.RecursiveFolderDeleter;

/**
 * This class is responsible for exporting projects from AgentWorkbench.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportController {

	private static final String FILE_NAME_FOR_INSTALLATION_PACKAGE = "agentgui";

	private Project project;
	private ProjectExportSettings exportSettings;
	private boolean isShowUserDialogs=true;
	
	private Path tempExportFolderPath;

	private ProgressMonitor progressMonitor;


	/**
	 * Instantiates a new project export controller.
	 * @param project The project to be exported. Maybe <code>null</code> also.
	 */
	public ProjectExportController(Project project) {
		this.project = project;
	}

	/**
	 * Exports the current project. The export settings are requested from the user.
	 */
	public void exportProject() {

		// --- If the project is not set, select the project first --
		if (this.project == null) {
			this.project = this.selectProjectForExport();
			// --- Return if the project selection was canceled -----
			if (this.project==null) return;
		}

		// --- Show a dialog to configure the export ----------------
		ProjectExportDialog projectExportDialog = new ProjectExportDialog(project);

		if (projectExportDialog.isCanceled() == false) {

			// --- Get the export settings from the dialog ----------
			this.exportSettings = projectExportDialog.getExportSettings();

			// --- Select the export destination --------------------
			JFileChooser chooser = this.getJFileChooser();
			if (chooser.showSaveDialog(Application.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
				
				File targetFile = chooser.getSelectedFile();
				Application.getGlobalInfo().setLastSelectedFolder(targetFile.getParentFile());

				// --- Check if the file already exists -------------
				if (targetFile.exists() == true) {
					String optionTitle = targetFile.getName() + ": " + Language.translate("Datei überschreiben?");
					String optionMsg = Language.translate("Die Datei existiert bereits. Wollen Sie diese Datei überschreiben?");
					int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						targetFile.delete();
					} else {
						return;
					}
				}

				// --- Set the target file --------------------------
				this.exportSettings.setTargetFile(targetFile);
				// --- Do the export --------------------------------
				this.exportProject(exportSettings);
			}
		}
	}

	/**
	 * Exports the current project using the provided {@link ProjectExportSettings} 
	 * and showing the user dialogs, if necessary.
	 * @param exportSettings The {@link ProjectExportSettings}
	 */
	public void exportProject(ProjectExportSettings exportSettings) {
		this.exportProject(exportSettings, true, true);
	}
	/**
	 * Exports the current project using the provided {@link ProjectExportSettings}.
	 *
	 * @param exportSettings The {@link ProjectExportSettings}
	 * @param isShowUserDialogs the is show user dialogs
	 * @param useConcurrentThread set true, if you want to export the project by a concurrent thread
	 */
	public void exportProject(ProjectExportSettings exportSettings, boolean isShowUserDialogs, boolean useConcurrentThread) {
		this.exportSettings = exportSettings;
		this.isShowUserDialogs = isShowUserDialogs;
		if (useConcurrentThread==true) {
			ProjectExportThread exportThread = new ProjectExportThread();
			exportThread.start();
		} else {
			this.doProjectExport();
		}
	}

	/**
	 * Shows a dialog for project selection, loads the selected project
	 * 
	 * @return the selected project
	 */
	private Project selectProjectForExport() {
		String actionTitle = Language.translate("Projekt zum Export auswählen");
		ProjectNewOpenDialog newProDia = UiBridge.getInstance().getProjectNewOpenDialog(Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitle, ProjectAction.ExportProject);
		newProDia.setVisible(true);
		if (newProDia.isCanceled() == true) {
			return null;
		} else {
			String projectSubFolder = newProDia.getProjectDirectory();
			newProDia.close();
			newProDia = null;

			String projectFolderFullPath = Application.getGlobalInfo().getPathProjects() + projectSubFolder;
			return Project.load(new File(projectFolderFullPath), false);
		}
	}

	/**
	 * Creates and initialized a {@link JFileChooser} for selecting the export target
	 * 
	 * @return the {@link JFileChooser}
	 */
	private JFileChooser getJFileChooser() {

		// --- Prepare file type filters -----------------------------
		String projectFileSuffix = Application.getGlobalInfo().getFileEndProjectZip();
		FileNameExtensionFilter projectFileFilter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + projectFileSuffix + ")", projectFileSuffix);
		FileNameExtensionFilter zipFileFilter = new FileNameExtensionFilter(Language.translate("Zip-Datei") + " (*.zip)", "zip");
		FileNameExtensionFilter tarGzFileFilter = new FileNameExtensionFilter(Language.translate("Tar.gz-Datei") + " (*.tar.gz)", "tar.gz");

		// --- Create and initialize the JFileChooser -------
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.addChoosableFileFilter(projectFileFilter);
		jFileChooser.addChoosableFileFilter(zipFileFilter);
		jFileChooser.addChoosableFileFilter(tarGzFileFilter);
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setMultiSelectionEnabled(false);
		jFileChooser.setAcceptAllFileFilterUsed(false);

		// --- Generate a file name proposition based on the export settings
		// ------------
		StringBuffer proposedFileName = new StringBuffer();
		proposedFileName.append(Application.getGlobalInfo().getLastSelectedFolderAsString());

		if (this.exportSettings.isIncludeInstallationPackage() == true) {

			// --- Installation package ---------
			proposedFileName.append(FILE_NAME_FOR_INSTALLATION_PACKAGE);
			if (this.exportSettings.getInstallationPackage().getPacakgeFile().getName().endsWith(".zip")) {
				proposedFileName.append(".zip");
				jFileChooser.setFileFilter(zipFileFilter);
			} else {
				proposedFileName.append(".tar.gz");
				jFileChooser.setFileFilter(tarGzFileFilter);
			}

		} else {

			// --- Project only -----------------
			proposedFileName.append(project.getProjectFolder() + '.' + Application.getGlobalInfo().getFileEndProjectZip());
			jFileChooser.setFileFilter(projectFileFilter);

		}

		File proposedFile = new File(proposedFileName.toString());
		jFileChooser.setSelectedFile(proposedFile);
		jFileChooser.setCurrentDirectory(proposedFile);

		return jFileChooser;
	}

	/**
	 * Copies all required project data to a temporary folder next to the selected
	 * 
	 * @return Copying successful?
	 */
	private boolean copyProjectDataToTempFolder() {

		// --- Determine the source path --------------
		Path sourcePath = new File(project.getProjectFolderFullPath()).toPath();

		// --- Specify folders that should not be copied ------
		List<Path> skipList = new ArrayList<>();
		skipList.add(sourcePath.resolve("~tmp"));
		if (this.exportSettings.isIncludeAllSetups() == false) {
			// --- If only selected setups should be exported, copying setup data is done in
			// a separate step -----------
			skipList.add(sourcePath.resolve("setups"));
			skipList.add(sourcePath.resolve("setupsEnv"));
		}

		// --- Copy the required files to the temporary folder --------
		try {
			Files.createDirectories(this.getTempExportFolderPath());
			RecursiveFolderCopier rfc = CommonComponentFactory.getNewRecursiveFolderCopier();
			rfc.copyFolder(sourcePath, this.getTempExportFolderPath(), skipList);
		} catch (IOException e) {
			System.err.println("Error copying project data!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Copies the required files for the selected simulation setups. Based on a negative list approach, i.e. all files from the setup folders except those of the setups not being exported will be copied.
	 * 
	 * @return Copying successful?
	 */
	private boolean copyRequiredSimulationSetupFiles() {

		// --- Determine source folders -----------------
		Path setupsSubFolderSourcePath = new File(project.getSubFolder4Setups(true)).toPath();
		Path setupEnvironmentsSubFolderSourcePath = new File(project.getProjectFolder()).toPath().resolve(project.getEnvSetupPath());

		// --- Create target folders --------------------
		Path setupsSubFolderTargetPath = this.getTempExportFolderPath().resolve(project.getSubFolder4Setups(false));
		Path setupEnvironmentsSubFolderTargetPath = this.getTempExportFolderPath().resolve(project.getEnvSetupPath(false));
		try {
			if (setupsSubFolderTargetPath.toFile().exists() == false) {
				Files.createDirectory(setupsSubFolderTargetPath);
			}
			if (setupEnvironmentsSubFolderTargetPath.toFile().exists() == false) {
				Files.createDirectory(setupEnvironmentsSubFolderTargetPath);
			}
		} catch (IOException e) {
			System.err.println("Error creating supfolders for simulation setups!");
			e.printStackTrace();
			return false;
		}

		// --- Create a list of setups not to be exported --------------
		List<String> setupsNegativeList = new ArrayList<>(this.project.getSimulationSetups().keySet());
		setupsNegativeList.removeAll(this.exportSettings.getSimSetups());

		// --- Prepare negative lists for setup and environment files ---
		List<String> setupFilesNegativeList = new ArrayList<>();
		List<String> environmentFilesNegativeList = new ArrayList<>();

		// --- Add the file base names (without suffix) of the undesired setups to the
		// negative lists -----------
		for (String excludedSetupName : setupsNegativeList) {

			// --- Load the setup -------------
			SimulationSetup excludedSetup = null;
			try {
				excludedSetup = this.loadSimSetup(excludedSetupName);
			} catch (JAXBException | IOException e) {
				System.err.println("Error loading simulation setup data!");
				e.printStackTrace();
				return false;
			}

			// --- Add the setup's files to the negative lists ----------
			if (excludedSetup != null) {

				// --- Setup files ------------
				String setupFileName = this.project.getSimulationSetups().get(excludedSetupName);
				String setupFileBaseName = setupFileName.substring(0, setupFileName.lastIndexOf('.'));
				setupFilesNegativeList.add(setupFileBaseName);

				// --- Environment files --------------
				String envFileName = excludedSetup.getEnvironmentFileName();
				String setupEnvironmentFileBaseName = envFileName.substring(0, envFileName.lastIndexOf('.'));
				environmentFilesNegativeList.add(setupEnvironmentFileBaseName);

			}

		}

		try {

			// --- Copy the required setup files to the export folder ---------------
			FileBaseNameNegativeListFilter setupFilesNegativeListFilter = new FileBaseNameNegativeListFilter(setupFilesNegativeList);
			DirectoryStream<Path> setupDirectoryStream = Files.newDirectoryStream(setupsSubFolderSourcePath, setupFilesNegativeListFilter);
			for (Path sourcePath : setupDirectoryStream) {
				Path targetPath = setupsSubFolderTargetPath.resolve(sourcePath.getFileName());
				Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}

			// --- Copy the required environment files to the export folder -----------
			FileBaseNameNegativeListFilter environmentFIlesNegativeListFilter = new FileBaseNameNegativeListFilter(environmentFilesNegativeList);
			DirectoryStream<Path> setupEnvironmentDirectoryStream = Files.newDirectoryStream(setupEnvironmentsSubFolderSourcePath, environmentFIlesNegativeListFilter);
			for (Path sourcePath : setupEnvironmentDirectoryStream) {
				Path targetPath = setupEnvironmentsSubFolderTargetPath.resolve(sourcePath.getFileName());
				Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			System.err.println("Error copying simulation setup files");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Loads the simulation setup with the specified name
	 * 
	 * @param setupName The setup to be loaded
	 * @return The setup
	 * @throws JAXBException Parsing the setup file failed
	 * @throws IOException Reading the setup file failed
	 */
	private SimulationSetup loadSimSetup(String setupName) throws JAXBException, IOException {
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
	 * Removes all setups that are not selected for export from the setup list
	 */
	private void removeUnexportedSetupsFromList() {

		// --- Get the list of setups from the project file --------------
		Project exportedProject = Project.load(this.tempExportFolderPath.toFile(), false);
		Set<String> setupNames = new HashSet<>(exportedProject.getSimulationSetups().keySet());

		// --- Remove all setups that are not exported --------------------
		for (String setupName : setupNames) {
			if (exportSettings.getSimSetups().contains(setupName) == false) {
				exportedProject.getSimulationSetups().remove(setupName);
			}
		}

		// --- If the currently selected setup is not exported, set the first exported setup as selected instead -----
		if (this.exportSettings.getSimSetups().contains(exportedProject.getSimulationSetupCurrent()) == false) {
			if (exportedProject.getSimulationSetups().size() > 0) {
				exportedProject.setSimulationSetupCurrent(this.exportSettings.getSimSetups().get(0));
			} else {
				exportedProject.setSimulationSetupCurrent(null);
			}
		}

		// --- Save the changes ------------
		exportedProject.save(this.tempExportFolderPath.toFile(), false);
	}

	/**
	 * Gets the temporary export folder path
	 * 
	 * @return the temporary export folder path
	 */
	private Path getTempExportFolderPath() {

		if (tempExportFolderPath == null) {

			// --- Determine the path for the temporary export folder, based on the selected
			// target file --------------
			File targetFile = this.exportSettings.getTargetFile();
			Path containingFolder = targetFile.getParentFile().toPath();
			tempExportFolderPath = containingFolder.resolve(project.getProjectFolder());
		}

		return this.tempExportFolderPath;
	}

	/**
	 * Integrate project into installation package.
	 * @return true, if successful
	 */
	private boolean integrateProjectIntoInstallationPackage() {

		File installationPackageFile = this.exportSettings.getInstallationPackage().getPacakgeFile();
		try {
			ArchiveFileHandler newZipper = new ArchiveFileHandler();
			newZipper.appendFolderToArchive(tempExportFolderPath.toFile(), installationPackageFile, exportSettings.getTargetFile(), "agentgui/projects");
			
		} catch (IOException ioEx) {
			System.err.println("Error integrating project into installation package!");
			ioEx.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets the progress monitor.
	 * @return the progress monitor
	 */
	private ProgressMonitor getProgressMonitor() {
		if (this.progressMonitor==null && this.isShowUserDialogs==true) {
			String title = Language.translate("Projekt-Export");
			String header = Language.translate("Exportiere Projekt") + " " + project.getProjectName();
			String progress = Language.translate("Exportiere") + "...";
			this.progressMonitor = CommonComponentFactory.getNewProgressMonitor(title, header, progress);
		}
		return this.progressMonitor;
	}
	/**
	 * Updates the progress monitor.
	 * @param currentProgress the current progress
	 */
	private void updateProgressMonitor(int currentProgress) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				ProgressMonitor pm = getProgressMonitor();
				if (pm==null) return;
				// --- Show progress monitor if not visible ---------
				if (pm.isVisible()==false) {
					pm.setVisible(true);
					pm.validate();
					pm.repaint();
				}
				// --- Set progress ---------------------------------
				pm.setProgress(currentProgress);
			}
		});
		
	}
	/**
	 * Disposes the progress monitor.
	 */
	private void disposeProgressMonitor() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ProgressMonitor pm = getProgressMonitor();
				if (pm==null) return;
				pm.setVisible(false);
				pm.dispose();
			}
		});
	}

	/**
	 * This {@link DirectoryStream.Filter} implementation matches all directory entries whose file base name (without extension) is not contained in a negative list.
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class FileBaseNameNegativeListFilter implements DirectoryStream.Filter<Path> {

		private List<String> negativeList;

		/**
		 * Constructor
		 * 
		 * @param negativeList the negative list
		 */
		public FileBaseNameNegativeListFilter(List<String> negativeList) {
			this.negativeList = negativeList;
		}

		@Override
		public boolean accept(Path entry) throws IOException {

			// --- Get the file base name (without suffix) -------------
			String fileBaseName = entry.toFile().getName().substring(0, entry.toFile().getName().lastIndexOf('.'));

			// --- Check if the negative list contains this file's base name -------
			if (negativeList.contains(fileBaseName)) {
				return false;
			} else {
				return true;
			}
		}

	}

	
	/**
	 * This Thread does the actual export.
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ProjectExportThread extends Thread {
		@Override
		public void run() {
			doProjectExport();
		}
	}
	/**
	 * Does the actual project export.
	 */
	private void doProjectExport() {
		
		this.updateProgressMonitor(0);
		
		// --- Copy the required data to a temporary folder -------------------
		boolean success = ProjectExportController.this.copyProjectDataToTempFolder();
		this.updateProgressMonitor(10);

		if (success==true) {

			// --- Handle export of simulation setups if necessary ------------
			if (ProjectExportController.this.exportSettings.isIncludeAllSetups() == false) {
				// --- Copy the required files for the selected setups --------
				if (ProjectExportController.this.exportSettings.getSimSetups().size() > 0) {
					success = ProjectExportController.this.copyRequiredSimulationSetupFiles();
				}
				// --- Remove the non-exported setups from the list -----------
				if (success==true) {
					ProjectExportController.this.removeUnexportedSetupsFromList();
				}
			}
			this.updateProgressMonitor(30);

			if (ProjectExportController.this.exportSettings.isIncludeInstallationPackage()) {
				// --- Integrate the project into the installation package ------
				success = ProjectExportController.this.integrateProjectIntoInstallationPackage();
			} else {
				// --- Zip the temporary folder --------------
				ArchiveFileHandler newZipper = new ArchiveFileHandler();
				success = newZipper.compressFolder(tempExportFolderPath.toFile(), ProjectExportController.this.exportSettings.getTargetFile());
			}
			this.updateProgressMonitor(80);

			// --- Remove the temporary export folder -------------
			try {
				RecursiveFolderDeleter folderDeleter = CommonComponentFactory.getNewRecursiveFolderDeleter();
				folderDeleter.deleteFolder(tempExportFolderPath);
			} catch (IOException e) {
				System.err.println("Error deleting temoprary export folder");
				e.printStackTrace();
			}
			this.updateProgressMonitor(100);
			this.disposeProgressMonitor();

			// --- Show a feedback message to the user --------------------
			if (success == true) {
				System.out.println("Project '" + project.getProjectName() + "' export successful!");
				if (this.isShowUserDialogs==true) {
					String messageTitle = Language.translate("Export erfolgreich");
					String messageContent = Language.translate("Projekt") + " " + project.getProjectName() + " " + Language.translate("erfolgreich exportiert!");
					JOptionPane.showMessageDialog(null, messageContent, messageTitle, JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				System.err.println("Project '" + project.getProjectName() + "' export failed!");
				if (this.isShowUserDialogs==true) {
					String message = Language.translate("Export fehlgeschlagen");
					JOptionPane.showMessageDialog(null, message, message, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
}
