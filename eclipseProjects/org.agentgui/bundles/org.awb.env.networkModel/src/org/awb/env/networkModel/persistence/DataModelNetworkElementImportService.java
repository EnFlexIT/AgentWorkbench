package org.awb.env.networkModel.persistence;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * This interface can be implemented to provide an importer for DataModelNetworkElements
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public interface DataModelNetworkElementImportService {
	/**
	 * Will be invoked during initialization and will receive the current {@link GraphEnvironmentController} instance.
	 * @param graphController the new graph environment controller
	 */
	public void setGraphEnvironmentController(GraphEnvironmentController graphController);
	
	/**
	 * Provides a list of file filters to select appropriate files
	 * @return the file name extension filters
	 */
	public List<FileFilter> getFileNameExtensionFilters();

	/**
	 * Import data from file.
	 * @param importFile the import file
	 * @return true, if successful
	 */
	public void importDataFromFile(File importFile);
	
}
