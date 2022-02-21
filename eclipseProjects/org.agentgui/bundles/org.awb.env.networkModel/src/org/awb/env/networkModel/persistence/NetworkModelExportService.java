package org.awb.env.networkModel.persistence;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * This interface defines the methods a service for exporting a {@link NetworkModel}s topology must provide.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg - Essen
 */
public interface NetworkModelExportService {
	
	/**
	 * Sets the current GraphEnvironmentController with the NetworkModel to the export service.
	 * @param graphController the current environment controller
	 */
	public void setGraphController(GraphEnvironmentController graphController);
	
	/**
	 * Implement this method to provide a list of {@link FileFilter}s for files that can be handled by your exporter.
	 * @return the file filters
	 */
	public List<FileFilter> getFileFilters();
	
	/**
	 * Has to perform the actual network model export to the specified file 
	 * @param file the file to export to
	 * @return the imported network model
	 */
	public void exportNetworkModelToFile(File file);
	
	/**
	 * Implement this method to perform some cleanup tasks when the export is done, especially set member variables that may contain large objects to null. 
	 */
	public void cleanupExporter();

	
}
