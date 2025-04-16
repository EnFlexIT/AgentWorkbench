package org.awb.env.networkModel.persistence;

import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import de.enflexit.awb.simulation.environment.AbstractEnvironmentModel;


/**
 * This interface defines the methods of a service for importing network topology data from files must provide.
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
	 * Has to return, if the detail models of {@link NetworkComponent}s and {@link GraphNode}s needs to be saved after the import.<p> 
	 * If this method return <code>true</code>, the storing procedure for {@link DataModelNetworkElement}s will be executed right after the import. 
	 * If an implementation returns <code>false</code>, the import implementation should have saved the detail data models already.    
	 * 
	 * @return true, if the storing procedure for {@link NetworkComponent}s and {@link GraphNode}s should be executed right after the import.
	 */
	public boolean requiresToStoreNetworkElements();

	/**
	 * Has to return the vector of {@link DataModelNetworkElement}s that are to be saved or <code>null</code>.
	 * If <code>null</code> or an empty vector is returned, all elements will be saved - otherwise only the returned elements.
	 * @return the data model network element to save or <code>null</code>
	 */
	public Vector<DataModelNetworkElement> getDataModelNetworkElementToSave();
	
	/**
	 * Has to return the number of threads that are to be used for saving {@link DataModelNetworkElement}'s  
	 * @return the number of threads to be used for saving the data models of {@link NetworkComponent}s or {@link GraphNode}s.  
	 */
	public Integer getMaxNumberOfThreadsForSaveAction();
	
	
	/**
	 * Implement this method to perform some cleanup tasks when the import is done, especially set member variables that may contain large objects to null. 
	 */
	public void cleanupImporter();

	
}
