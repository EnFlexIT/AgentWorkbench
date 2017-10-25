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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.transfer.gui.ProjectExportDialog;
import de.enflexit.common.transfer.RecursiveFolderCopier;

/**
 * This class is responsible for exporting projects from AgentWorkbench
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExporter {
	
	private ProjectExportDialog projectExportDialog;
	
	private Project project;
	private ProjectExportSettings exportSettings;
	
	private Path tempFolderPath;
	
	public void exportProject(Project project) {
		
		this.project = project;
		
		this.projectExportDialog = new ProjectExportDialog(project);
		if(projectExportDialog.isCanceled() == false) {
			this.exportSettings = projectExportDialog.getExportSettings();
			
			String fileSuffix = Application.getGlobalInfo().getFileEndProjectZip();
			String proposedFileName = Application.getGlobalInfo().getLastSelectedFolderAsString() + project.getProjectFolder() + "." + fileSuffix ;
			File proposedFile = new File(proposedFileName );
			FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileSuffix + ")", fileSuffix);
			
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(proposedFile);
			chooser.setCurrentDirectory(proposedFile);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			chooser.setAcceptAllFileFilterUsed(false);
			
			if(chooser.showSaveDialog(Application.getMainWindow()) == JFileChooser.APPROVE_OPTION){
				File targetFile = chooser.getSelectedFile();
				this.exportSettings.setTargetFile(targetFile);
				
				boolean success = this.copyProjectDataToTempFolder(targetFile);
				
				if(success == true) {
					
					// --- Handle export of simulation setups if necessary ---------
					if(this.exportSettings.isIncludeAllSetups() == false) {
						
						// --- Copy files ----------------
						success = this.copySelectedSimSetups();
						
						// --- Edit setup list -----------
						if (success == true) {
							this.editSetupList();
						}
						
					}
					
					if (this.exportSettings.isIncludeInstallationPackage()) {
						// --- Integrate the project into the installation package ------
						//TODO implement
					} else {
						// --- Zip the temp folder to an agui file --------
						//TODO implement / reuse from ProjectsLoaded
					}
				}
				System.out.println("Project " + project.getProjectName() + " successfully exported!");
			}
				
		}
		
	}
	
	private boolean copyProjectDataToTempFolder(File targetFile) {
		
		Path sourcePath = new File(project.getProjectFolderFullPath()).toPath();
		
		// --- Specify folders that should not be copied ------
		List<Path> skipList = new ArrayList<>();
		skipList.add(sourcePath.resolve("~tmp"));
		if(this.exportSettings.isIncludeAllSetups() == false) {
			skipList.add(sourcePath.resolve("setups"));
			skipList.add(sourcePath.resolve("setupsEnv"));
		}
		
		try {
			Files.createDirectories(this.getTempFolderPath());
			RecursiveFolderCopier rfc = CommonComponentFactory.getNewRecursiveFolderCopier();
			rfc.copyFolder(sourcePath, this.getTempFolderPath(), skipList);
		} catch (IOException e) {
			System.err.println("Error copying project data!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean copySelectedSimSetups() {
		
		// --- Create folders ------------
		Path setupsSubFolderTargetPath = this.getTempFolderPath().resolve(project.getSubFolder4Setups(false));
		Path setupEnvironmentsSubFolderTargetPath = this.getTempFolderPath().resolve(project.getEnvSetupPath(false));
		
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
		
		Path setupsSubFolderSourcePath = new File(project.getSubFolder4Setups(true)).toPath();
		Path setupEnvironmentsSubFolderSourcePath = new File(project.getProjectFolder()).toPath().resolve(project.getEnvSetupPath());
		
		for(String setupName : this.exportSettings.getSimSetups()) {
			String setupFileName = this.project.getSimulationSetups().get(setupName);
			String setupFileFullPath = this.project.getSubFolder4Setups(true) + File.separator + setupFileName;
			File setupFile = new File(setupFileFullPath);
			
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
	
	private void editSetupList() {
		Project exportedProject = Project.load(this.tempFolderPath.toFile(), false);
		Set<String> setupNames = new HashSet<>(exportedProject.getSimulationSetups().keySet());
		
		for(String setupName : setupNames) {
			if (exportSettings.getSimSetups().contains(setupName) == false) {
				exportedProject.getSimulationSetups().remove(setupName);
			}
		}
		
		exportedProject.save(this.tempFolderPath.toFile(), false);
	}
	
	private Path getTempFolderPath() {
		if(tempFolderPath == null) {
			File targetFile = this.exportSettings.getTargetFile();
			String targetFileBaseName = targetFile.getName().substring(0, targetFile.getName().lastIndexOf("."));
			
			// --- Create a temporary folder, copy everything there
			Path containingFolder = targetFile.getParentFile().toPath();
			tempFolderPath = containingFolder.resolve(targetFileBaseName);	
		}
		return this.tempFolderPath;
	}
	
}
