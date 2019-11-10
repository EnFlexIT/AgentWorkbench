package org.awb.env.networkModel.persistence;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * The Class SetupDataModelStorageService serves as base interface for storage handler
 * that will be invoked during the load and save action of the {@link GraphEnvironmentController}.
 * Thus, it is possible to save individual data types that are used within a {@link DataModelNetworkElement}
 * (which is a {@link NetworkComponent} or a {@link GraphNode}) for one setup at once.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface SetupDataModelStorageService {
	
	/**
	 * Describes the possible actions of a SetupDataModelStorageService.
	 */
	public enum DataModelServiceAction {
		SaveSetup,
		LoadSetup,
		RenameSetup,
		RemoveSetup
	}

	
	/**
	 * Will be invoked during initialization and will receive the current graph controller instance.
	 * @param graphController the new graph environment controller
	 */
	public void setGraphEnvironmentController(GraphEnvironmentController graphController);
	
	/**
	 * Has to return the class of type {@link AbstractDataModelStorageHandler}
	 * that will be used to access the individual data models of {@link DataModelNetworkElement}s.
	 * This mandatory class will be used to set or get the individual data model from the
	 * setup scope to the individual scope or vice versa from the individual scope to the setup scope.
	 * In turn, the here specified class can call a setup storage handler by simply using the method
	 * {@link AbstractDataModelStorageHandler#getSetupDataModelStorageService()}
	 *
	 * @return the data model storage handler class
	 * @see AbstractDataModelStorageHandler#getSetupDataModelStorageService()
	 */
	public Class<? extends AbstractDataModelStorageHandler> getDataModelStorageHandlerClass();
	
	
	
	/**
	 * Will be invoked to save the data models of the locally known {@link DataModelNetworkElement}s.
	 *
	 * @param destinationDirectory the destination directory to use
	 * @param setupName the current setup name
	 */
	public void saveNetworkElementDataModels(String destinationDirectory, String setupName);
	
	/**
	 * Will be invoked to load the data models of the locally known {@link DataModelNetworkElement}s.
	 * 
	 * @param destinationDirectory the destination directory to use
	 * @param setupName the current setup name
	 */
	public void loadNetworkElementDataModels(String destinationDirectory, String setupName);
	
	/**
	 * Will be invoked to remove the data models file of {@link DataModelNetworkElement}s.
	 * 
	 * @param destinationDirectory the destination directory to use
	 * @param setupName the current setup name
	 */
	public void removeNetworkElementDataModels(String destinationDirectory, String setupName);
	
	/**
	 * Will be invoked to rename the data models file of {@link DataModelNetworkElement}s.
	 *
	 * @param destinationDirectory the destination directory to use
	 * @param oldSetupName the current setup name
	 * @param newSetupName the new setup name
	 */
	public void renameNetworkElementDataModels(String destinationDirectory, String oldSetupName, String newSetupName);
	
}
