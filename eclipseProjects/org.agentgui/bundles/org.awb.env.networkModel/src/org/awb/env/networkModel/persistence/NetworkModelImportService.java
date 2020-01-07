package org.awb.env.networkModel.persistence;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import agentgui.simulationService.environment.AbstractEnvironmentModel;

/**
 * This interface defines the methods a service for importing network topology data from files must provide.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public interface NetworkModelImportService {
	
	/**
	 * Sets the environment controller.
	 * @param graphController the new environment controller
	 */
	public void setGraphController(GraphEnvironmentController graphController);
	
	/**
	 * Implement this method to provide a list of {@link FileFilter}s for files that can be handled by your importer.
	 * @return the file filters
	 */
	public List<FileFilter> getFileFilters();
	
	/**
	 * Performs the actual network model import from the specified file 
	 * @param file the file
	 * @return the imported network model
	 */
	public NetworkModel importNetworkModelFromFile(File file);
	
	/**
	 * This method can be used to provide an additional {@link AbstractEnvironmentModel} with your imported {@link NetworkModel}.
	 * This is not mandatory, your implementation may return null.
	 * @return the abstract environment model
	 */
	public AbstractEnvironmentModel getAbstractEnvironmentModel();
	
	/**
	 * Implement this method to perform some cleanup tasks when the import is done, especially set member variables that may contain large objects to null. 
	 */
	public void cleanupImporter();
	
//	/**
//	 * Gets the network model importer.
//	 * @return the network model importer
//	 */
//	public CSV_FileImporter getNetworkModelImporter();
}
