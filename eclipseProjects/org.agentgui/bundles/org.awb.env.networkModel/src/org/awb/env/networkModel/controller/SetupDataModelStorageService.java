package org.awb.env.networkModel.controller;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.dataModel.AbstractDataModelStorageHandler;

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
	 * Will be invoked to save the data models within {@link DataModelNetworkElement}s.
	 */
	public void saveNetworkElementDataModels();
	
	/**
	 * Will be invoked to load the data models within {@link DataModelNetworkElement}s.
	 */
	public void loadNetworkElementDataModels();
	
	
}
