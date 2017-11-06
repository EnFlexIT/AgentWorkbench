package agentgui.core.project.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
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
			
//			if (this.exportSettings.isIncludeInstallationPackage()) {
//				JOptionPane.showMessageDialog(null, "Exporting of installation packages is not implemented yet", "Under Construction", JOptionPane.WARNING_MESSAGE);
//				return;
//			}
			
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
				
				Application.getGlobalInfo().setLastSelectedFolder(targetFile.getParentFile());
				
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
						success = this.copyRequiredSimulationSetupFiles();
						
						// --- Remove the non-exported setups from the list --------------
						if (success == true) {
							this.removeUnexportedSetupsFromList();
						}
						
					}
					
					if (this.exportSettings.isIncludeInstallationPackage()) {

						// --- Integrate the project into the installation package ------
						
						this.addProjectToInstallationPackage();
						
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
	
	private boolean copyRequiredSimulationSetupFiles() {
		
		// --- Determine source folders -----------------
		Path setupsSubFolderSourcePath = new File(project.getSubFolder4Setups(true)).toPath();
		Path setupEnvironmentsSubFolderSourcePath = new File(project.getProjectFolder()).toPath().resolve(project.getEnvSetupPath());
		
		// --- Create target folders --------------------
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
		
		// --- Create a list of setups not to be exported --------------
		List<String> setupsNegativeList = new ArrayList<>(this.project.getSimulationSetups().keySet());
		setupsNegativeList.removeAll(this.exportSettings.getSimSetups());
		
		// --- Prepare negative lists for setup and environment files ---
		List<String> setupFilesNegativeList = new ArrayList<>();
		List<String> environmentFilesNegativeList = new ArrayList<>();
		
		// --- Add the file base names (without suffix) of the undesired setups to the negative lists -----------
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
		for(String setupName : setupNames) {
			if (exportSettings.getSimSetups().contains(setupName) == false) {
				exportedProject.getSimulationSetups().remove(setupName);
			}
		}
		
		// --- If the currently selected setup is not exported, set the first exported setup as selected instead -----
		if (this.exportSettings.getSimSetups().contains(exportedProject.getSimulationSetupCurrent()) == false) {
			exportedProject.setSimulationSetupCurrent(this.exportSettings.getSimSetups().get(0));
		}
		
		// --- Save the changes ------------
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
	
	private boolean addProjectToInstallationPackage() {
		
		ArchiveInputStream zais = null;
		ArchiveOutputStream aos = null;
		try {
			File installationPackageFile = this.exportSettings.getInstallationPackage().getPacakgeFile();
			String packageFileName = installationPackageFile.getName();
			Path copyTargetPath = this.getTempExportFolderPath().resolve("..").resolve(packageFileName);
			Files.copy(installationPackageFile.toPath(), copyTargetPath, StandardCopyOption.REPLACE_EXISTING);
			
			aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new FileOutputStream(copyTargetPath.toFile()));
			zais = (ZipArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new FileInputStream(installationPackageFile));

			ArchiveEntry entry;
			
			byte[] buffer = new byte[1024];
			while((entry = zais.getNextEntry()) != null) {
				aos.putArchiveEntry(entry);
				int bytesRead;
				while((bytesRead = zais.read(buffer)) >0 ) {
					aos.write(buffer, 0, bytesRead);
				}
				aos.closeArchiveEntry();
			}
			
			
			Path testFile = getTempExportFolderPath().resolve("..").resolve("test.txt");
			byte[] data = Files.readAllBytes(testFile);
			
			
			ZipArchiveEntry archiveEntry = new ZipArchiveEntry("agentgui/projects/hygrid/test.txt");
			archiveEntry.setSize(data.length);
			
			aos.putArchiveEntry(archiveEntry);
			aos.write(data);
			aos.closeArchiveEntry();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArchiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(aos != null) {
						aos.close();
				}
				if(zais != null) {
					zais.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing streams!");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * This {@link DirectoryStream.Filter} implementation matches all directory entries whose file
	 * base name (without extension) is not contained in a negative list.
	 *  
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class FileBaseNameNegativeListFilter implements DirectoryStream.Filter<Path>{
		
		private List<String> negativeList;
		
		/**
		 * Constructor
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
			if(negativeList.contains(fileBaseName)) {
				return false;
			} else {
				return true;
			}
		}
		
	}
	
}
