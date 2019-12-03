package org.awb.env.networkModel.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;

/**
 * The BundlingDataModelStorageHandler is used within a {@link BundlingNetworkComponentAdapter4DataModel}
 * to load or save data models for/from individual components.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundlingDataModelStorageHandler extends AbstractDataModelStorageHandler {

	private BundlingNetworkComponentAdapter4DataModel dataModelAdapter;
	
	/**
	 * Instantiates a new bundling data model storage handler.
	 * @param dataModelAdapter the data model adapter
	 */
	public BundlingDataModelStorageHandler(BundlingNetworkComponentAdapter4DataModel dataModelAdapter) {
		this.dataModelAdapter = dataModelAdapter;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler#loadDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public Object loadDataModel(DataModelNetworkElement networkElement) {
		
		TreeMap<String, String> allSettings  = networkElement.getDataModelStorageSettings();
		
		TreeMap<String, Object> dmTreeMap = new TreeMap<>();
		for (int i = 0; i < this.dataModelAdapter.getTitleList().size(); i++) {
			
			String tabTitle = this.dataModelAdapter.getTitleList().get(i);
			Vector<String> partVector64 = this.base64VectorExtract(networkElement, tabTitle);
			TreeMap<String, String> partSettings = this.prefixFilterStorageSettings(allSettings, tabTitle, true);

			DataModelNetworkElement partNetworkElement = this.createTemporaryNetworkElement(networkElement);
			partNetworkElement.setDataModelBase64(partVector64);
			partNetworkElement.setDataModelStorageSettings(partSettings);
			
			NetworkComponentAdapter4DataModel partDataModelAdapter = this.dataModelAdapter.getDataModelAdapterMap().get(tabTitle);
			AbstractDataModelStorageHandler partStorageHandler = partDataModelAdapter.getDataModelStorageHandlerInternal(); 
			Object partDataModel = partStorageHandler.loadDataModel(partNetworkElement);
			
			this.setRequiresPersistenceUpdate(partStorageHandler.isRequiresPersistenceUpdate());
			dmTreeMap.put(tabTitle, partDataModel);
		}
		return dmTreeMap;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler#saveDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public TreeMap<String, String> saveDataModel(DataModelNetworkElement networkElement) {

		if (networkElement.getDataModel()==null) return null;
		
		// --- Define target data types -----------------------------
		TreeMap<String, String> dmSettingsNew = null;
		Vector<String> dmBase64Vector = new Vector<>();
		
		// --- Get data model TreeMap for separated instances -------
		TreeMap<?, ?> dmTreeMap = (TreeMap<?, ?>) networkElement.getDataModel();
		TreeMap<String, String> dmSettingsOld = networkElement.getDataModelStorageSettings();

		// --- Check all possible data models -----------------------
		for (int i = 0; i < this.dataModelAdapter.getTitleList().size(); i++) {
			
			String tabTitle = this.dataModelAdapter.getTitleList().get(i);
			
			// --- Get part data model ------------------------------
			Object partDataModel = dmTreeMap.get(tabTitle);
			TreeMap<String, String> partSettings = this.prefixFilterStorageSettings(dmSettingsOld, tabTitle, true);

			// --- Get the data model adapter -----------------------
			NetworkComponentAdapter4DataModel partDataModelAdapter = this.dataModelAdapter.getDataModelAdapterMap().get(tabTitle);
			if (partDataModelAdapter instanceof NetworkComponentAdapter4Ontology) partDataModelAdapter.save();
			
			// --- Create temporary network element for part model --
			DataModelNetworkElement partNetworkElement = this.createTemporaryNetworkElement(networkElement);
			partNetworkElement.setDataModel(partDataModel);
			partNetworkElement.setDataModelStorageSettings(partSettings);
			
			// --- Call save part model -----------------------------
			partSettings = partDataModelAdapter.getDataModelStorageHandler().saveDataModel(partNetworkElement);

			// --- Adjust part and overall settings -----------------
			this.prefixSetToStorageSettings(partSettings, tabTitle);
			dmSettingsNew = this.mergeStorageSettings(dmSettingsNew, partSettings);
			
			// --- Add to resulting base64 vector -------------------
			this.base64VectorAdd(dmBase64Vector, partNetworkElement, tabTitle);
		
		}
		
		// --- Set the Base64 Vector to network element ------------- 
		if (dmBase64Vector.size()==0) {
			networkElement.setDataModelBase64(null);
		} else {
			networkElement.setDataModelBase64(dmBase64Vector);
		}
		return dmSettingsNew;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler#setSaveSimulated(boolean)
	 */
	@Override
	public void setSaveSimulated(boolean isSimulateSave) {
		super.setSaveSimulated(isSimulateSave);
		// --- Distribute simulation indicator to sub adapter -------
		List<NetworkComponentAdapter4DataModel> dmAdapterList = new ArrayList<>(this.dataModelAdapter.getDataModelAdapterMap().values());
		for (int i = 0; i < dmAdapterList.size(); i++) {
			NetworkComponentAdapter4DataModel partDataModelAdapter = dmAdapterList.get(i);
			AbstractDataModelStorageHandler storageHandler = partDataModelAdapter.getDataModelStorageHandler();
			if (storageHandler!=null) {
				storageHandler.setSaveSimulated(isSimulateSave);
			}
		}
	}
	
}
