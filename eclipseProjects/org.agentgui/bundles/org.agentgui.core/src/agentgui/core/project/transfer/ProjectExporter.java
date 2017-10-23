package agentgui.core.project.transfer;

import agentgui.core.project.Project;
import agentgui.core.project.transfer.gui.ProjectExportDialog;

public class ProjectExporter {
	
	private ProjectExportDialog projectExportDialog;
	
	public void exportProject(Project project) {
		this.projectExportDialog = new ProjectExportDialog(project);
		projectExportDialog.showProjectExportDialog();
		if(projectExportDialog.isCanceled() == false) {
			ProjectExportSettings exportSettings = projectExportDialog.getExportSettings();
			//TODO trigger actual export based on the settings
		}
	}
	
	
}
