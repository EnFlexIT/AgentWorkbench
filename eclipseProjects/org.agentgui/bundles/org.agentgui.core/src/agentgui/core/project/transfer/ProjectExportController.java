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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.agentgui.gui.ProjectNewOpenDialog;
import org.agentgui.gui.UiBridge;
import org.agentgui.gui.ProjectNewOpenDialog.ProjectAction;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.transfer.gui.ProjectExportDialog;
import de.enflexit.common.transfer.RecursiveFolderCopier;
import de.enflexit.common.transfer.RecursiveFolderDeleter;
import de.enflexit.common.transfer.Zipper;

/**
 * This class is responsible for exporting projects from AgentWorkbench
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportController {
	
	private ProjectExportDialog projectExportDialog;
	
	private Project project;
	private ProjectExportSettings exportSettings;
	
	private Path tempExportFolderPath;
	
	/**
	 * Constructor
	 * @param project The project to be exported
	 */
	public ProjectExportController(Project project) {
		this.project = project;
	}
	
	/**
	 * Exports the current project
	 */
	public void exportProject() {
		
		// --- If the current project is not set, select the project to be exported -------- 
		if(this.project == null) {
			this.project = selectProjectForExport();

			// --- Return if the project selection was canceled ---------
			if (this.project == null) {
				return;
			}
		}
		
		// --- Show a dialog to configure the export ----------------
		this.projectExportDialog = new ProjectExportDialog(project);
		
		if(projectExportDialog.isCanceled() == false) {
			
			// --- Get the export settings from the dialog -----------------
			this.exportSettings = projectExportDialog.getExportSettings();
			
			// --- Create file name suggestion -----------------------------
			String fileSuffix = Application.getGlobalInfo().getFileEndProjectZip();
			String proposedFileName = Application.getGlobalInfo().getLastSelectedFolderAsString() + project.getProjectFolder() + "." + fileSuffix ;
			File proposedFile = new File(proposedFileName );
			FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileSuffix + ")", fileSuffix);
			
			// --- Prepare a JFileChooser for selecting the export destination -------
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(proposedFile);
			chooser.setCurrentDirectory(proposedFile);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			chooser.setAcceptAllFileFilterUsed(false);
			
			if(chooser.showSaveDialog(Application.getMainWindow()) == JFileChooser.APPROVE_OPTION){
				File targetFile = chooser.getSelectedFile();
				
				// --- Check if the file already exists ----------
				if (targetFile.exists()==true) {
					String optionTitle = targetFile.getName() + ": " + Language.translate("Datei überschreiben?");
					String optionMsg = Language.translate("Die Datei existiert bereits. Wollen Sie diese Datei überschreiben?");
					int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
					if (answer==JOptionPane.YES_OPTION) {
						targetFile.delete();
					} else {
						return;
					}
				}
				
				// --- Set the target file ---------------
				this.exportSettings.setTargetFile(targetFile);
				
				// --- Copy the required data to a temporary folder -------------
				boolean success = this.copyProjectDataToTempFolder();
				
				if(success == true) {
					
					// --- Handle export of simulation setups if necessary ---------
					if(this.exportSettings.isIncludeAllSetups() == false) {
						
						// --- Copy the required files for the selected setups -----------
						success = this.copySelectedSimSetups();
						
						// --- Remove the non-exported setups from the list --------------
						if (success == true) {
							this.removeUnexportedSetupsFromList();
						}
						
					}
					
					if (this.exportSettings.isIncludeInstallationPackage()) {
						// --- Integrate the project into the installation package ------
						//TODO implement
					} else {
						
						// --- Create a zipped file --------------------
						Zipper zipper = CommonComponentFactory.getNewZipper(Application.getMainWindow());
						zipper.setZipFolder(targetFile.getAbsolutePath());
						zipper.setZipSourceFolder(tempExportFolderPath.toFile().getAbsolutePath());
						zipper.doZipFolder();
						
						// --- Wait for the zipper --------------
						while(zipper.isDone() == false) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// --- Nothing to do here --------------
							}
						}
						
						zipper = null;
						
						try {
							// --- Remove the temporary export folder -------------
							RecursiveFolderDeleter folderDeleter = CommonComponentFactory.getNewRecursiveFolderDeleter();
							folderDeleter.deleteFolder(tempExportFolderPath);
						} catch (IOException e) {
							System.err.println("Error exporting temoprary export folder");
							e.printStackTrace();
						}
						
					}
					
					// --- Show a feedback message to the user --------------------
					if(success == true) {
						System.out.println("Project " + project.getProjectName() + " export successful!");
						String messageTitle = Language.translate("Export erfolgreich");
						String messageContent = Language.translate("Projekt") + " " + project.getProjectName() + " " + Language.translate("erfolgreich exportiert!");
						JOptionPane.showMessageDialog(null, messageContent, messageTitle, JOptionPane.INFORMATION_MESSAGE);
					}else {
						System.err.println("Project " + project.getProjectName() + " export failed!");
						String message = Language.translate("Export fehlgeschlagen");
						JOptionPane.showMessageDialog(null, message, message, JOptionPane.ERROR_MESSAGE);
						//TODO implement some cleanup
					}
				}
			}
				
		}
		
		
	}
	
	/**
	 * Shows a dialog for project selection, loads the selected project
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
	 * Copies all required project data to a temporary folder next to the selected 
	 * @return Copying successful?
	 */
	private boolean copyProjectDataToTempFolder() {
		
		// --- Determine the source path --------------
		Path sourcePath = new File(project.getProjectFolderFullPath()).toPath();
		
		// --- Specify folders that should not be copied ------
		List<Path> skipList = new ArrayList<>();
		skipList.add(sourcePath.resolve("~tmp"));
		if(this.exportSettings.isIncludeAllSetups() == false) {
			// --- If only selected setups should be exported, copying setup data is done in a separate step -----------
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
	 * Copies the files for the selected simulation setups to the temporary export folder
	 * @return Copying successful?
	 */
	private boolean copySelectedSimSetups() {
		
		// --- Create folders ------------
		Path setupsSubFolderTargetPath = this.getTempExportFolderPath().resolve(project.getSubFolder4Setups(false));
		Path setupEnvironmentsSubFolderTargetPath = this.getTempExportFolderPath().resolve(project.getEnvSetupPath(false));
		
		try {
			if(setupsSubFolderTargetPath.toFile().exists() == false) {
					Files.createDirectory(setupsSubFolderTargetPath);
			}
			if(setupEnvironmentsSubFolderTargetPath.toFile().exists() == false) {
				Files.createDirectory(setupEnvironmentsSubFolderTargetPath);
			}
		} catch (IOException e) {
			System.err.println("Error creating supfolders for simulation setups!");
			e.printStackTrace();
			return false;
		}
		
		// --- Determine source folders -----------------
		Path setupsSubFolderSourcePath = new File(project.getSubFolder4Setups(true)).toPath();
		Path setupEnvironmentsSubFolderSourcePath = new File(project.getProjectFolder()).toPath().resolve(project.getEnvSetupPath());
		
		// --- Iterate over the selected setups ----------------
		for(String setupName : this.exportSettings.getSimSetups()) {
			
			// --- Determine the setup file path ----------
			String setupFileName = this.project.getSimulationSetups().get(setupName);
			String setupFileFullPath = this.project.getSubFolder4Setups(true) + File.separator + setupFileName;
			File setupFile = new File(setupFileFullPath);
			
			// --- Load the setup -------------
			JAXBContext pc;
			SimulationSetup simSetup = null;
			try {
				pc = JAXBContext.newInstance(this.project.getSimulationSetups().getCurrSimSetup().getClass());
				Unmarshaller um = pc.createUnmarshaller();
				FileReader fr = new FileReader(setupFile);
				simSetup = (SimulationSetup) um.unmarshal(fr);
				fr.close();
			} catch (JAXBException | IOException e) {
				System.err.println("Error loading simulation setup data!");
				e.printStackTrace();
				return false;
			}
			
			// --- Copy setup and environment files ----------------	
			if(simSetup != null) {

				try {
					
					// --- Copy simulation setup files --------------
					String setupFileBaseName = setupFileName.substring(0, setupFileName.lastIndexOf('.'));
					DirectoryStream<Path> stream1;
					stream1 = Files.newDirectoryStream(setupsSubFolderSourcePath, setupFileBaseName + ".*");
					for(Path sourcePath : stream1) {
						Path targetPath = setupsSubFolderTargetPath.resolve(sourcePath.getFileName());
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
					}
					
					// --- Copy setup environment files --------------
					String envFileName = simSetup.getEnvironmentFileName();
					String setupEnvironmentFileBaseName = envFileName.substring(0, envFileName.lastIndexOf('.'));
					DirectoryStream<Path> stream2 = Files.newDirectoryStream(setupEnvironmentsSubFolderSourcePath, setupEnvironmentFileBaseName + ".*");
					for(Path sourcePath : stream2) {
						Path targetPath = setupEnvironmentsSubFolderTargetPath.resolve(sourcePath.getFileName());
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
					}
					
				} catch (IOException e) {
					System.err.println("Error copying simulation setup files!");
					e.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Removes all setups that are not selected for export from the setup list
	 */
	private void removeUnexportedSetupsFromList() {
		Project exportedProject = Project.load(this.tempExportFolderPath.toFile(), false);
		Set<String> setupNames = new HashSet<>(exportedProject.getSimulationSetups().keySet());
		
		for(String setupName : setupNames) {
			if (exportSettings.getSimSetups().contains(setupName) == false) {
				exportedProject.getSimulationSetups().remove(setupName);
			}
		}
		
		exportedProject.save(this.tempExportFolderPath.toFile(), false);
	}
	
	/**
	 * Gets the temporary export folder
	 * @return the temporary export folder
	 */
	private Path getTempExportFolderPath() {
		
		if(tempExportFolderPath == null) {

			// --- Determine the path for the temporary export folder, based on the selected target file -------------- 
			File targetFile = this.exportSettings.getTargetFile();
			String targetFileBaseName = targetFile.getName().substring(0, targetFile.getName().lastIndexOf("."));
			Path containingFolder = targetFile.getParentFile().toPath();
			tempExportFolderPath = containingFolder.resolve(targetFileBaseName);	
		}
		
		return this.tempExportFolderPath;
	}
	
}
