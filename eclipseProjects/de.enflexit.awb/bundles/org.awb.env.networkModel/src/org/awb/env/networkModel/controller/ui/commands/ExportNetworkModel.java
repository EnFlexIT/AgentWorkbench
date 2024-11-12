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
package org.awb.env.networkModel.controller.ui.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.NetworkModelExportService;

import agentgui.core.application.Application;
import de.enflexit.language.Language;

/**
 * The AbstractUndoableEdit 'ImportNetworkModel' imports a selected file.
 */
public class ExportNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -409810728677898514L;
	
	private static FileFilter lastSlectedFileFilter;
	
	private GraphEnvironmentController graphController;
	
	private boolean canceled = false;
	private NetworkModelExportService exportService;
	private File networkModelFileSelected;
	
	
	/**
	 * Instantiates a new import network model.
	 * @param graphController the graph controller
	 */
	public ExportNetworkModel(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		
		// --- Define the import adapter and the file to import -----
		this.selectFile();
		if (this.exportService!=null && this.networkModelFileSelected!=null) {
			this.doEdit();
		} else {
			this.setCanceled(true);
		}
	}
	
	/**
	 * Do the edit action.
	 */
	private void doEdit() {
		
		if (this.isCanceled()==false && this.exportService!=null && this.networkModelFileSelected!=null) {
			// --- Export the NetworkModel using a dedicated thread -----------
			this.exportToFileUsingDedicatedThread();
		}
	}
	/**
	 * Import from file using a dedicated thread.
	 */
	private void exportToFileUsingDedicatedThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ExportNetworkModel.this.exportToFile();
			}
		}, this.getClass().getSimpleName() + "Thread").start();
	}
	/**
	 * Does the Import from the locally specified file.
	 */
	private void exportToFile() {
		
		try {
		
			Application.setStatusBarMessage("Export network model to file(s) ... ");
			// --- Export to selected file --------------------------
			this.exportService.exportNetworkModelToFile(this.networkModelFileSelected);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// --- Invoke to cleanup the exporter -------------------
			this.exportService.cleanupExporter();
			Application.setStatusBarMessageReady();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.doEdit();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkmodell exportieren");
	}
	
	/**
	 * Select the file to import.
	 */
	private void selectFile() {
		
		// ----------------------------------------------------------
		// --- Button Import graph from file ------------------------
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(Language.translate("Export NetworkModel to file", Language.EN) + ":");
		fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
		
		// --- Add all filters to a HashMap ---------------------
		Vector<NetworkModelExportService> exportServices = this.graphController.getExportAdapter(); 
		
		HashMap<String, FileFilter> fileFiltersHash = new HashMap<>(); 
		for (int i = 0; i < exportServices.size(); i++) {
			
			// --- Get the current import adapter's FileFilters ---------------
			NetworkModelExportService importer = exportServices.get(i);
			List<FileFilter> fileFilters = importer.getFileFilters();
			
			for (int j=0; j<fileFilters.size(); j++) {
				FileFilter fileFilter = fileFilters.get(j);
				fileFiltersHash.put(fileFilter.getDescription(), fileFilter);
				// --- Check if is last selected file filter ------------
				if (lastSlectedFileFilter!=null && fileFilter.getDescription().equals(lastSlectedFileFilter.getDescription())) {
					// --- To be sure to have the right instance --------
					lastSlectedFileFilter = fileFilter;
				}
			}
		}
		
		// --- Add all filters in alphabetical order ---------------- 
		List<String> filterDescriptions = new ArrayList<String>(fileFiltersHash.keySet());
		Collections.sort(filterDescriptions);
		for (int i=0; i<filterDescriptions.size(); i++) {
			fileChooser.addChoosableFileFilter(fileFiltersHash.get(filterDescriptions.get(i)));
		}
		// --- Check for file filter --------------------------------
		if (fileChooser.getChoosableFileFilters().length==0) {
			// --- Show message that no importer was defined --------
			String title =  Language.translate("Keine Export-Modul vorhanden!");
			String message =  Language.translate("Derzeit sind keine Module für den Export von Netzwerkmodellen definiert.");
			JOptionPane.showMessageDialog(this.graphController.getGraphEnvironmentControllerGUI(), message, title, JOptionPane.ERROR_MESSAGE);
			
		} else {
			// --- Pre-Select FileFilter and directory  -------------
			fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
			if (lastSlectedFileFilter!=null) {
				fileChooser.setFileFilter(lastSlectedFileFilter);
			}
			fileChooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			
			// --- Preset file name proposal ------------------------
			String setupName = this.graphController.getProject().getSimulationSetupCurrent();
			if (setupName!=null && setupName.isBlank()==false) {
				String directory = fileChooser.getCurrentDirectory().getAbsolutePath();
				String fileName = directory.endsWith(File.separator) ? directory + setupName : directory + File.separator + setupName;
				File fileSelected = new File(fileName);
				fileChooser.setSelectedFile(fileSelected);
			}
			
			// --- Show FileChooser ---------------------------------
			int dialogAnswer = fileChooser.showSaveDialog(Application.getMainWindow()); 
			if (dialogAnswer==JFileChooser.APPROVE_OPTION) {
				Application.getGlobalInfo().setLastSelectedFolder(fileChooser.getCurrentDirectory());
				File selectedFile = fileChooser.getSelectedFile();
				FileFilter selectedFileFilter = fileChooser.getFileFilter();
				for (int i = 0; i < exportServices.size(); i++) {
					NetworkModelExportService exporter = exportServices.get(i);
					if (exporter.getFileFilters().contains(selectedFileFilter)) {
						this.networkModelFileSelected = selectedFile;
						this.exportService = exporter;
						lastSlectedFileFilter = selectedFileFilter;
						break;
					}
				}
				
			} else {
				this.setCanceled(true);
			}
		}
		
	}

	/**
	 * Sets if this action was canceled.
	 * @param canceled the new canceled
	 */
	private void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Returns true, if this action was canceled.
	 * @return true, if successful
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
}
