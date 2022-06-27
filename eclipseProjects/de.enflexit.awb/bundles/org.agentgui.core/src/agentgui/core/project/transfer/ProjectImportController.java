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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import de.enflexit.common.transfer.Zipper;

/**
 * The Class ProjectImportController can be used to configure and 
 * import a zipped (*.agui) project file.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectImportController {

	private ProjectImportSettings projectImportSettings;
	
	/**
	 * Instantiates a new project import controller.
	 * @param projectImportSettings the project import settings
	 */
	public ProjectImportController(ProjectImportSettings projectImportSettings) {
		this.setProjectImportSettings(projectImportSettings);
	}
	
	/**
	 * Returns the project import settings.
	 * @return the project import settings
	 */
	public ProjectImportSettings getProjectImportSettings() {
		return projectImportSettings;
	}
	/**
	 * Sets the project import settings.
	 * @param projectImportSettings the new project import settings
	 */
	public void setProjectImportSettings(ProjectImportSettings projectImportSettings) {
		this.projectImportSettings = projectImportSettings;
	}
	
	/**
	 * Does the project import according to the current {@link ProjectImportSettings}.
	 * @return true, if successful
	 */
	public boolean doProjectImport() {
		
		boolean successful = false;
		
		// --- Check if the setting are complete --------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			String errMsg = "[" + this.getClass().getSimpleName() + "] " + configError + " - Cancel update check.";
			throw new IllegalArgumentException(errMsg);
		}

		// --- Prepare to extract the archive -----------------------
		File projectFile = this.getProjectImportSettings().getProjectArchiveFile();
		String destFolder = Application.getGlobalInfo().getPathProjects();
		String zipFolder = projectFile.getAbsolutePath();
		
		// --- Define the Zipper instance ---------------------------
		Zipper zipper = CommonComponentFactory.getNewZipper(Application.getMainWindow());
		zipper.setUnzipZipFolder(zipFolder);
		zipper.setUnzipDestinationFolder(destFolder);
		zipper.setRunInThread(this.getProjectImportSettings().isExtractInThread());
		
		// --- Error-Handling ---------------------------------------
		final String rootFolder2Extract = zipper.getRootFolder2Extract();
		String testFolder = destFolder + rootFolder2Extract;
		File testFile = new File(testFolder);
		if (this.getProjectImportSettings().isOverwriteExistingVersion()==false && testFile.exists()) {
			String newLine = Application.getGlobalInfo().getNewLineSeparator();
			String optionTitle = rootFolder2Extract + ": " + Language.translate("Verzeichnis bereits vorhanden!");
			String optionMsg = Language.translate("Verzeichnis") + ": " + testFolder + newLine;
			optionMsg+= Language.translate("Das Verzeichnis existiert bereits. Der Import wird unterbrochen.");
			JOptionPane.showMessageDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		// --- Define task after the unzip action -------------------
		Runnable afterJobTask = this.getProjectImportSettings().getAfterImportTask();
		if (afterJobTask==null) {
			// --- Apply default action: open the project -----------
			afterJobTask = new Runnable() {
				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Application.getProjectsLoaded().add(rootFolder2Extract);
						}
					});
				}
			};
		}
		zipper.setAfterJobTask(afterJobTask);
		
		// --- Finally unzip --------------------------
		zipper.doUnzipFolder();
		successful = zipper.isDone();
		return successful;
	}
	
	
	/**
	 * Return the configuration error as string, if there is an error.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		if (this.getProjectImportSettings()==null) {
			return "No import settings have been specified !";
		}
		return null;
	}
	
}
