package org.awb.env.networkModel.dataModel;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * The Class AbstractSetupStorageHandler serves as base class for storage handler
 * that will be invoked during the load and save action of the {@link GraphEnvironmentController}.
 * Thus, it is possible to save individual data types that are used within a {@link DataModelNetworkElement}
 * (which is a {@link NetworkComponent} or a {@link GraphNode}).
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractSetupStorageHandler {

	/**
	 * Will be invoked to save the data models within {@link DataModelNetworkElement}s.
	 */
	public abstract void saveNetworkElementDataModels();
	
	/**
	 * Will be invoked to load the data models within {@link DataModelNetworkElement}s.
	 */
	public abstract void loadNetworkElementDataModels();
	
	
}
