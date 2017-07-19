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
package agentgui.envModel.graph.commands;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.NetworkModelFileImporter;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The AbstractUndoableEdit 'ImportNetworkModel' imports a selected file.
 */
public class ImportNetworkModel extends AbstractUndoableEdit {

	private static final long serialVersionUID = -409810728677898514L;

	private GraphEnvironmentController graphController = null;
	
	private boolean canceled = false;
	private NetworkModelFileImporter networkModelFileImporter = null;
	private File networkModelFileSelected = null;
	
	private NetworkModel newNetworkModel = null;
	private NetworkModel oldNetworkModel = null; 

	
	/**
	 * Instantiates a new import network model.
	 * @param graphController the graph controller
	 */
	public ImportNetworkModel(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		
		// --- Define the import adapter and the file to import -----
		this.selectFile();
		if (this.networkModelFileImporter!=null && this.networkModelFileSelected!=null) {
			this.oldNetworkModel = this.graphController.getNetworkModel().getCopy();
			this.doEdit();
		} else {
			this.setCanceled(true);
		}
	}
	
	private void doEdit() {
		
		if (this.networkModelFileImporter!=null && this.networkModelFileSelected!=null) {
			
			this.graphController.getAgents2Start().clear();
			this.graphController.setDisplayEnvironmentModel(null);
			
			if (this.newNetworkModel==null) {
				// --- Import directly from the selected file -------------------------------------
				try {
					this.newNetworkModel = this.networkModelFileImporter.importGraphFromFile(this.networkModelFileSelected);
					// --- The following has to be done only once, directly after the import !!! --
					this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
					this.graphController.setNetworkComponentDataModelBase64Encoded();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			} else {
				// --- The preparation for saving the Network were already done before (above) ---- 
				this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
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
		
		// ------------------------------------------------------
		// --- Button Import graph from file --------------------
		JFileChooser graphFC = new JFileChooser();
		graphFC.removeChoosableFileFilter(graphFC.getAcceptAllFileFilter());
		// --- Add defined FileFilters --------------------------
		for (NetworkModelFileImporter importer : this.graphController.getImportAdapter()){
			graphFC.addChoosableFileFilter(importer.getFileFilter());
		}
		graphFC.setFileFilter(graphFC.getChoosableFileFilters()[0]);
		graphFC.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		
		// --- Show FileChooser ---------------------------------
		if(graphFC.showOpenDialog(Application.getMainWindow())==JFileChooser.APPROVE_OPTION){
			Application.getGlobalInfo().setLastSelectedFolder(graphFC.getCurrentDirectory());
			File selectedFile = graphFC.getSelectedFile();
			FileFilter selectedFileFilter = graphFC.getFileFilter();
			for (NetworkModelFileImporter importer : this.graphController.getImportAdapter()){
				if (selectedFileFilter==importer.getFileFilter()) {
					this.networkModelFileSelected = selectedFile;
					this.networkModelFileImporter = importer;
					break;
				}
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
