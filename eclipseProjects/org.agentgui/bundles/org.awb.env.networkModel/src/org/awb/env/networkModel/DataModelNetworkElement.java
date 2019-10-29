package org.awb.env.networkModel;

import java.util.TreeMap;
import java.util.Vector;

import org.awb.env.networkModel.dataModel.AbstractDataModelStorageHandler;

/**
 * The Interface DataModelNetworkElement describes {@link GraphElement}s and {@link NetworkComponent}s
 * that may hold individual data models to be loaded or saved within a setup.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface DataModelNetworkElement {
	
	/**
	 * Gets the id of the network element.
	 * @return the id of the network element
	 */
	public String getId();
	
	/**
	 * Gets the data model instance.
	 * @return the data model instance
	 */
	public Object getDataModel();
	/**
	 * Sets the data model instance.
	 * @param dataModel the new data model instance
	 */
	public void setDataModel(Object dataModel);
	
	/**
	 * Returns the data model storage settings that are to be used by extended {@link AbstractDataModelStorageHandler}.
	 * @return the data model storage settings
	 * @see AbstractDataModelStorageHandler
	 */
	public TreeMap<String, String> getDataModelStorageSettings();
	/**
	 * Sets the data model storage settings for individual storage handler.
	 * @param dataModelStorageSettings the data model storage settings
	 * @see AbstractDataModelStorageHandler
	 */
	public void setDataModelStorageSettings(TreeMap<String, String> dataModelStorageSettings);
	
	/**
	 * Returns the data model Base64 encoded.
	 * @return the dataModelBase64
	 */
	public Vector<String> getDataModelBase64();
	/**
	 * Sets the data model Base64 encoded.
	 * @param dataModelBase64 the dataModelBase64 to set
	 */
	public void setDataModelBase64(Vector<String> dataModelBase64);
	
}
