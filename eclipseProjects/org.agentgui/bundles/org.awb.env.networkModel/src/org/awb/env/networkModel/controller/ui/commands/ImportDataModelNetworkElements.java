package org.awb.env.networkModel.controller.ui.commands;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.DataModelNetworkElementImportService;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import de.enflexit.common.ServiceFinder;

/**
 * This class implements an UndoableEdit for importing operational data to a network model.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ImportDataModelNetworkElements extends AbstractUndoableEdit{

	private static final long serialVersionUID = 3828733618007282507L;
	
	private GraphEnvironmentController graphController;
	
	private static FileFilter lastSlectedFileFilter;
	private DataModelNetworkElementImportService importService;
	
	private File importFile;
	private boolean canceled = false;
	
	private NetworkModel oldNetworkModel; 
	private AbstractEnvironmentModel oldAbstractEnvModel;
	
	private NetworkModel newNetworkModel; 
	private AbstractEnvironmentModel newAbstractEnvModel;
	
	
	/**
	 * Instantiates a new import data model network elements.
	 * @param graphController the graph controller
	 */
	public ImportDataModelNetworkElements(GraphEnvironmentController graphController) {
		super();
		this.graphController = graphController;
		this.selectFileForImport();
		if (this.canceled==false) {
			this.doEdit();
		}
	}
	
	/**
	 * Select file for import.
	 */
	private void selectFileForImport() {
		
		List<DataModelNetworkElementImportService> servicesList = ServiceFinder.findServices(DataModelNetworkElementImportService.class);
		if (servicesList.size()==0) {
			// --- No import services found -------------------------
			String title =  Language.translate("Keine Import-Module vorhanden!");
			String message =  Language.translate("Derzeit sind keine Module für den Import von Laufzeit-Daten definiert.");
			JOptionPane.showMessageDialog(this.graphController.getGraphEnvironmentControllerGUI(), message, title, JOptionPane.ERROR_MESSAGE);
		} else {
			
			// --- Prepare the FileChooser --------------------------
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			
			// --- Add file filters for all import services --------- 
			HashMap <FileFilter,DataModelNetworkElementImportService> servicesHashMap = new HashMap<>();
			fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
			for (int i=0; i<servicesList.size(); i++) {
				DataModelNetworkElementImportService service = servicesList.get(i);
				for (FileFilter fileFilter : service.getFileNameExtensionFilters()) {
					fileChooser.addChoosableFileFilter(fileFilter);
					// --- Remember which service belongs to which filter -----
					servicesHashMap.put(fileFilter, service);
				}
			}
			
			// --- Set initial filter -----------------------------------------
			if (lastSlectedFileFilter!=null) {
				fileChooser.setFileFilter(lastSlectedFileFilter);
			} else {
				fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
			}

			// --- Show the dialog --------------------------------------------
			int dialogAnswer = fileChooser.showOpenDialog(Application.getMainWindow());
			if (dialogAnswer== JFileChooser.APPROVE_OPTION) {
				Application.getGlobalInfo().setLastSelectedFolder(fileChooser.getCurrentDirectory());
				this.importFile = fileChooser.getSelectedFile();
				// --- Select the right service for the selected file ---------
				this.importService = servicesHashMap.get(fileChooser.getFileFilter());
				this.importService.setGraphEnvironmentController(this.graphController);
				this.canceled = false;
			} else {
				this.canceled = true;
			}
			
		}
		
	}
	
	private void doEdit() {
		if (this.newNetworkModel==null) {
			// --- First time import ----------------------
			this.importFromFileUsingDedicatedThread();
		} else {
			// --- Import was done before (redo), use previously imported data
			this.graphController.setDisplayEnvironmentModel(this.newNetworkModel);
			if (this.newAbstractEnvModel!=null) {
				this.graphController.setAbstractEnvironmentModel(this.newAbstractEnvModel);
			}
		}
		
		this.graphController.setProjectUnsaved();
	}
	
	/**
	 * Import from file using a dedicated thread.
	 */
	private void importFromFileUsingDedicatedThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ImportDataModelNetworkElements.this.importFromFile();
			}
		}, this.getClass().getSimpleName() + "Thread").start();
	}
	
	
	/**
	 * Import the {@link DataModelNetworkElement}s from the selected file.
	 */
	private void importFromFile() {
		
		if (this.importFile!=null && this.importFile.exists()) {
			Application.setStatusBarMessage("Import network model from file(s) ... ");
			
			// --- Remember the previous network model for undo state ---------
			this.oldNetworkModel = this.graphController.getNetworkModel().getCopy();
			this.oldAbstractEnvModel = this.graphController.getAbstractEnvironmentModel().getCopy();
			
			// --- Delegate the actual import to the service ------------------
			this.importService.importDataFromFile(importFile);
			
			// --- Notify the observers to enable the undo button -------------
			this.graphController.getNetworkModelUndoManager().updateComponentDataModels();
			
			Application.setStatusBarMessageReady();
		}
		
	}


	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.newNetworkModel = (NetworkModel) this.graphController.getDisplayEnvironmentModel();
		this.graphController.setDisplayEnvironmentModel(this.oldNetworkModel);
		if (this.oldAbstractEnvModel!=null) {
			this.newAbstractEnvModel = this.graphController.getAbstractEnvironmentModel();
			this.graphController.setAbstractEnvironmentModel(this.oldAbstractEnvModel);
		}
		this.graphController.setProjectUnsaved();
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
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Import operational data", Language.EN);
	}

	/**
	 * Checks if the action was canceled
	 * @return true if canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	
}
