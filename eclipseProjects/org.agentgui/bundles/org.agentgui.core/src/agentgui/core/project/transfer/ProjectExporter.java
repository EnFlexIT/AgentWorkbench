package agentgui.core.project.transfer;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.gui.ProjectExportDialog;

/**
 * This class is responsible for exporting projects from AgentWorkbench
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExporter {
	
	private ProjectExportDialog projectExportDialog;
	
	private static final String TEMP_FOLDER_NAME = "projectExport_tmp";
	
	public void exportProject(Project project) {
		
		this.projectExportDialog = new ProjectExportDialog(project);
		if(projectExportDialog.isCanceled() == false) {
			ProjectExportSettings exportSettings = projectExportDialog.getExportSettings();
			
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
				exportSettings.setTargetFile(targetFile);
			}
		}
		
	}
	
}
