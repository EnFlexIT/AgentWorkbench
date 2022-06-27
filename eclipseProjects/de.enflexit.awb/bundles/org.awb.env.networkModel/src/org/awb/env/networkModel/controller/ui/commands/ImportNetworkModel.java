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

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.NetworkModelImportService;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.simulationService.environment.AbstractEnvironmentModel;

/**
 * The AbstractUndoableEdit 'ImportNetworkModel' imports a selected file.
 */
public class ImportNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -409810728677898514L;
	
	// --- Possible results from the option dialog shown by this.askIfReplaceOrMerge()
	@SuppressWarnings("unused")
	private static final int REPLACE_OPTION = 0;
	private static final int MERGE_OPTION = 1;
	private static final int CANCEL_OPTION = 2;

	private static FileFilter lastSlectedFileFilter;
	
	private GraphEnvironmentController graphController;
	
	private boolean canceled = false;
	private NetworkModelImportService importService;
	private File networkModelFileSelected;
	
	private NetworkModel newNetworkModel;
	private NetworkModel oldNetworkModel; 

	private AbstractEnvironmentModel newAbstractEnvModel;
	private AbstractEnvironmentModel oldAbstractEnvModel;
	
	private boolean keepExistingComponents = false;
	
	
	/**
	 * Instantiates a new import network model.
	 * @param graphController the graph controller
	 */
	public ImportNetworkModel(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		
		// --- Define the import adapter and the file to import -----
		this.selectFile();
		if (this.importService!=null && this.networkModelFileSelected!=null) {
			Application.setStatusBarMessage("Copy current settings Import network model from file(s) ... ");
			this.oldNetworkModel = this.graphController.getNetworkModel();					// removed .getCopy()
			this.oldAbstractEnvModel = this.graphController.getAbstractEnvironmentModel();	// removed .getCopy()
			this.doEdit();
		} else {
			this.setCanceled(true);
		}
	}
	
	/**
	 * Does the edit.
	 */
	private void doEdit() {
		
		if (this.isCanceled()==false && this.importService!=null && this.networkModelFileSelected!=null) {
			
			// --- If there current model is not empty, ask the user if the imported one should replace or extend it
			if (this.graphController.getNetworkModel().getGraphElements().size()>0) {
				int dialogAnswer = this.askIfReplaceOrMerge();
				this.keepExistingComponents = (dialogAnswer==MERGE_OPTION);
				this.setCanceled(dialogAnswer==CANCEL_OPTION);
			}
			
			if (this.isCanceled()==false) {
				
				this.graphController.getAgents2Start().clear();
				this.graphController.setDisplayEnvironmentModel(null);
				
				if (this.newNetworkModel==null) {
					// --- Import NetworkModel using a dedicated thread -------------------------------
					this.importFromFileUsingDedicatedThread();
					
				} else {
					// --- The preparation for saving the Network were already done before (above) ----
					this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
					if (this.newAbstractEnvModel!=null) {
						this.graphController.setAbstractEnvironmentModel(this.newAbstractEnvModel);
					}
				}
				this.graphController.setProjectUnsaved();
			}
		}
	}
	
	/**
	 * Shows an option dialog asking how to proceed if the current model is not empty  
	 * @return the users answer
	 */
	private int askIfReplaceOrMerge() {
		String message = Language.translate("The network model already contains components - replace or merge?", Language.EN);
		String title = Language.translate("Replace or merge?", Language.EN);
		Object[] options = {
				Language.translate("Replace", Language.EN), 
				Language.translate("Merge", Language.EN), 
				Language.translate("Cancel", Language.EN)
			};
		return JOptionPane.showOptionDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
	
	/**
	 * Import from file using a dedicated thread.
	 */
	private void importFromFileUsingDedicatedThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ImportNetworkModel.this.importFromFile();
			}
		}, this.getClass().getSimpleName() + "Thread").start();
	}
	/**
	 * Does the Import from the locally specified file.
	 */
	private void importFromFile() {
		
		try {
		
			Application.setStatusBarMessage("Import network model from file(s) ... ");
			// --- Import directly from the selected file ---------------------------------
			NetworkModel importedNetworkModel = this.importService.importNetworkModelFromFile(this.networkModelFileSelected);
			// --- Do we have an AbstractEnvironmentModel also? ---------------------------
			AbstractEnvironmentModel importedAbstractEnvironmentModel = this.importService.getAbstractEnvironmentModel();
		
			if (this.keepExistingComponents==true) {
				// --- Merge into existing model --------------------
				NetworkModel mergedNetworkModel = this.oldNetworkModel.getCopy();
				mergedNetworkModel.mergeNetworkModel(importedNetworkModel, null, true, false);
				for (NetworkComponent networkComponentPasted : importedNetworkModel.getNetworkComponents().values()) {
					this.graphController.addAgent(networkComponentPasted);
				}
				this.newNetworkModel = mergedNetworkModel;
				this.newAbstractEnvModel = importedAbstractEnvironmentModel;
				
			} else {
				// --- Replace exiting model ------------------------
				this.newNetworkModel = importedNetworkModel;
				this.newAbstractEnvModel = importedAbstractEnvironmentModel;
			}
			
			// --- Set new instances to GraphEnvironmentController ------------------------
			this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
			if (this.newAbstractEnvModel!=null) {
				this.graphController.setAbstractEnvironmentModel(this.newAbstractEnvModel);
			}
			// ----------------------------------------------------------------------------
			// --- The following has to be done only once, directly after the import !!! --
			// ----------------------------------------------------------------------------
			// --- Save data models of network elements -----------------------------------
			if (this.importService.requiresToStoreNetworkElements()==true) {
				// --- Check which network elements are to be saved -----------------------
				Vector<DataModelNetworkElement> networkElementsToSave = this.importService.getDataModelNetworkElementToSave();
				Integer maxNumberOfThreads = this.importService.getMaxNumberOfThreadsForSaveAction();
				if (networkElementsToSave==null || networkElementsToSave.size()==0) {
					this.graphController.saveDataModelNetworkElements(null, null, true, null, maxNumberOfThreads);
				} else {
					this.graphController.saveDataModelNetworkElements(null, null, true, networkElementsToSave, maxNumberOfThreads);
				}
			}
			// --- Invoke to cleanup the importer -----------------------------------------
			this.importService.cleanupImporter();
			this.graphController.setProjectUnsaved();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		this.graphController.setDisplayEnvironmentModel(this.oldNetworkModel);
		if (this.oldAbstractEnvModel!=null) {
			this.graphController.setAbstractEnvironmentModel(this.oldAbstractEnvModel);
		}
		this.graphController.setProjectUnsaved();
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkmodell importieren");
	}
	
	/**
	 * Select the file to import.
	 */
	private void selectFile() {
		
		// ----------------------------------------------------------
		// --- Button Import graph from file ------------------------
		JFileChooser graphFC = new JFileChooser();
		graphFC.setDialogTitle(Language.translate("Import NetworkModel from file", Language.EN) + ":");
		graphFC.removeChoosableFileFilter(graphFC.getAcceptAllFileFilter());
		
		
		// --- Add all filters to a HashMap ---------------------
		Vector<NetworkModelImportService> importServices = this.graphController.getImportAdapter(); 
		
		HashMap<String, FileFilter> fileFiltersHash = new HashMap<>(); 
		for (int i = 0; i < importServices.size(); i++) {
			
			// --- Get the current import adapter's FileFilters ---------------
			NetworkModelImportService importer = importServices.get(i);
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
			graphFC.addChoosableFileFilter(fileFiltersHash.get(filterDescriptions.get(i)));
		}
		// --- Check for file filter --------------------------------
		if (graphFC.getChoosableFileFilters().length==0) {
			// --- Show message that no importer was defined --------
			String title =  Language.translate("Keine Import-Module vorhanden!");
			String message =  Language.translate("Derzeit sind keine Module für den Import von Netzwerkmodellen definiert.");
			JOptionPane.showMessageDialog(this.graphController.getGraphEnvironmentControllerGUI(), message, title, JOptionPane.ERROR_MESSAGE);
			
		} else {
			// --- Pre-Select FileFilter and directory  -------------
			graphFC.setFileFilter(graphFC.getChoosableFileFilters()[0]);
			if (lastSlectedFileFilter!=null) {
				graphFC.setFileFilter(lastSlectedFileFilter);
			}
			graphFC.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			
			// --- Show FileChooser ---------------------------------
			int dialogAnswer = graphFC.showOpenDialog(Application.getMainWindow()); 
			if (dialogAnswer==JFileChooser.APPROVE_OPTION) {
				Application.getGlobalInfo().setLastSelectedFolder(graphFC.getCurrentDirectory());
				File selectedFile = graphFC.getSelectedFile();
				FileFilter selectedFileFilter = graphFC.getFileFilter();
				for (int i = 0; i < importServices.size(); i++) {
					NetworkModelImportService importer = importServices.get(i);
					if (importer.getFileFilters().contains(selectedFileFilter)) {
						this.networkModelFileSelected = selectedFile;
						this.importService = importer;
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
