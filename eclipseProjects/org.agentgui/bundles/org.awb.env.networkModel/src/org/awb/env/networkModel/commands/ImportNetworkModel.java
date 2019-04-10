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
package org.awb.env.networkModel.commands;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.AbstractNetworkModelFileImporter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.simulationService.environment.AbstractEnvironmentModel;

/**
 * The AbstractUndoableEdit 'ImportNetworkModel' imports a selected file.
 */
public class ImportNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -409810728677898514L;

	private GraphEnvironmentController graphController;
	
	private boolean canceled = false;
	private AbstractNetworkModelFileImporter abstractNetworkModelFileImporter;
	private File networkModelFileSelected;
	
	private NetworkModel newNetworkModel;
	private NetworkModel oldNetworkModel; 

	private AbstractEnvironmentModel newAbstractEnvModel;
	private AbstractEnvironmentModel oldAbstractEnvModel;
	
	/**
	 * Instantiates a new import network model.
	 * @param graphController the graph controller
	 */
	public ImportNetworkModel(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		
		// --- Define the import adapter and the file to import -----
		this.selectFile();
		if (this.abstractNetworkModelFileImporter!=null && this.networkModelFileSelected!=null) {
			this.oldNetworkModel = this.graphController.getNetworkModel().getCopy();
			this.oldAbstractEnvModel = this.graphController.getAbstractEnvironmentModel().getCopy();
			this.doEdit();
		} else {
			this.setCanceled(true);
		}
	}
	
	/**
	 * Does the edit.
	 */
	private void doEdit() {
		
		if (this.isCanceled()==false && this.abstractNetworkModelFileImporter!=null && this.networkModelFileSelected!=null) {
			
			this.graphController.getAgents2Start().clear();
			this.graphController.setDisplayEnvironmentModel(null);
			
			if (this.newNetworkModel==null) {
				try {
					// --- Import directly from the selected file ---------------------------------
					this.newNetworkModel = this.abstractNetworkModelFileImporter.importNetworkModelFromFile(this.networkModelFileSelected);
					// --- Do we have an AbstractEnvironmentModel also? --------------------------- 
					this.newAbstractEnvModel = this.abstractNetworkModelFileImporter.getAbstractEnvironmentModel();
					// --- Invoke to cleanup the importer -----------------------------------------
					this.abstractNetworkModelFileImporter.cleanupImporter();
					// --- Set new instances to GraphEnvironmentController ------------------------
					this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
					if (this.newAbstractEnvModel!=null) {
						this.graphController.setAbstractEnvironmentModel(this.newAbstractEnvModel);
					}
					// ----------------------------------------------------------------------------
					// --- The following has to be done only once, directly after the import !!! --
					// ----------------------------------------------------------------------------
					// --- Base64 encode the model elements --------------------------------------- 
					this.graphController.setNetworkComponentDataModelBase64Encoded();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
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
		// --- Button Import graph from file -----------------------
		JFileChooser graphFC = new JFileChooser();
		graphFC.removeChoosableFileFilter(graphFC.getAcceptAllFileFilter());
		
		// --- Add defined FileFilters ------------------------------
		for (int i = 0; i < this.graphController.getImportAdapter().size(); i++) {
			AbstractNetworkModelFileImporter importer = this.graphController.getImportAdapter().get(i);
			graphFC.addChoosableFileFilter(importer.getFileFilter());
		}
		
		// --- Check for file filter --------------------------------
		if (graphFC.getChoosableFileFilters().length==0) {
			// --- Show message that no importer was defined --------
			String title =  Language.translate("Keine Import-Module vorhanden!");
			String message =  Language.translate("Derzeit sind keine Module für den Import von Netzwerkmodellen definiert.");
			JOptionPane.showMessageDialog(this.graphController.getGraphEnvironmentControllerGUI(), message, title, JOptionPane.ERROR_MESSAGE);
			
		} else {
			// --- Select file -------------------------------------- 
			graphFC.setFileFilter(graphFC.getChoosableFileFilters()[0]);
			graphFC.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			
			// --- Show FileChooser ---------------------------------
			int dialogAnswer = graphFC.showOpenDialog(Application.getMainWindow()); 
			if (dialogAnswer==JFileChooser.APPROVE_OPTION) {
				Application.getGlobalInfo().setLastSelectedFolder(graphFC.getCurrentDirectory());
				File selectedFile = graphFC.getSelectedFile();
				FileFilter selectedFileFilter = graphFC.getFileFilter();
				for (int i = 0; i < this.graphController.getImportAdapter().size(); i++) {
					AbstractNetworkModelFileImporter importer = this.graphController.getImportAdapter().get(i);
					if (selectedFileFilter==importer.getFileFilter()) {
						this.networkModelFileSelected = selectedFile;
						this.abstractNetworkModelFileImporter = importer;
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
