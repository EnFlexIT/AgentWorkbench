package org.awb.env.networkModel.adapter.dataModel;

import java.util.TreeMap;
import java.util.Vector;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.DataModelEnDecoder64;

/**
 * The Class DataModelStorageHandlerBase64 handles data models of {@link NetworkComponent}s
 * or {@link GraphNode}s as Base64 encoded strings and saves them in the corresponding string 
 * vector of a {@link NetworkComponent} or GraphNode.<br><br>
 * 
 * This data model storage handler serves as the simplest way to handle the individual data model 
 * of a {@link GraphNode} or a {@link NetworkComponent}.
 * 
 * @see NetworkComponent#setDataModelBase64(Vector)
 * @see GraphNode#setDataModelBase64(Vector)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DataModelStorageHandlerBase64 extends AbstractDataModelStorageHandler {

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler#saveDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public TreeMap<String, String> saveDataModel(DataModelNetworkElement networkElement) {

		if (networkElement==null) return null;
		if (this.isSaveSimulated()==true) return null;
		
		if (networkElement!=null) {
			Object dataModel = networkElement.getDataModel(); 
			if (dataModel==null) {
				networkElement.setDataModelBase64(null);
			} else {
				Vector<String> dataModelBase64 = DataModelEnDecoder64.getDataModelBase64Encoded(dataModel);
				networkElement.setDataModelBase64(dataModelBase64);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler#loadDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public Object loadDataModel(DataModelNetworkElement networkElement) {
		
		Object dataModel = null;
		if (networkElement!=null) {
			try {
				Vector<String> dataModelBase64 = networkElement.getDataModelBase64();
				if (dataModelBase64!=null) {
					dataModel = DataModelEnDecoder64.getDataModelBase64Decoded(dataModelBase64);
				}
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dataModel;
	}

}
