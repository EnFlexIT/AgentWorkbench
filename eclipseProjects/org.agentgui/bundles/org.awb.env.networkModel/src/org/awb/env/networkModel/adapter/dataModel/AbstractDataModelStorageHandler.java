package org.awb.env.networkModel.adapter.dataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.SetupDataModelStorageService;

import agentgui.core.application.Application;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.project.Project;

/**
 * The Class AbstractDataModelStorageHandler serves as base class for storage handler
 * that are able to load or save the individual data model of {@link NetworkComponent}s
 * or {@link GraphNode}s. It's usage can be defined in individual (extended) 
 * {@link NetworkComponentAdapter4DataModel}, where the actual handling will be invoked.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractDataModelStorageHandler {

	public static final String BASE64_MODEL_SEPARATOR_PREFIX = "@";
	
	private boolean requiresPersistenceUpdate;
	private boolean saveSimulated;
	
	/**
	 * Has to load a {@link NetworkComponent}s or {@link GraphNode}s data model, by using the provided storage settings of
	 * the DataModelNetworkElement (see: {@link DataModelNetworkElement#getDataModelStorageSettings()}).
	 *
	 * @param networkElement the network element that is either a GraphNode or a NetworkComponent
	 * @return the data model
	 * @see DataModelNetworkElement#getDataModelStorageSettings()
	 */
	public abstract Object loadDataModel(DataModelNetworkElement networkElement);
	
	/**
	 * Has to save the data model and should return the storage settings. The actual data can be accessed 
	 * by using the getDataModel() method of the data model network element (see: {@link DataModelNetworkElement#getDataModel()}). 
	 *
	 * @param networkElement the network element that is either a GraphNode or a NetworkComponent
	 * @return the tree map with the storage settings
	 * @see DataModelNetworkElement#getDataModel()
	 */
	public abstract TreeMap<String, String> saveDataModel(DataModelNetworkElement networkElement);

	
	// ------------------------------------------------------------------------
	// --- From here methods to simulate the save action can be found ---------
	// ------------------------------------------------------------------------
	/**
	 * Simulates to save the data model of the network element, which means that the actual 
	 * save procedure (saving a file or push data into a database) will be skipped while 
	 * the storage settings may be adjusted. Generally, this allows to check if at least
	 * the storage settings have changed in comparison to previous settings. 
	 *
	 * @param networkElement the network element
	 * @return the storage settings tree map
	 */
	public TreeMap<String, String> saveDataModelSimulated(DataModelNetworkElement networkElement) {
		
		TreeMap<String, String> newSettings = null;
		try {
			this.setSaveSimulated(true);
			newSettings = this.saveDataModel(networkElement);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.setSaveSimulated(false);
		}
		return newSettings;
	}
	/**
	 * Sets to simulate the save invocation so that the new storage settings 
	 * can be provided without doing the actual save procedure (which could be to
	 * save into a file or into a database).
	 * 
	 * @param isSimulateSave the new simulate save
	 */
	public void setSaveSimulated(boolean isSimulateSave) {
		this.saveSimulated = isSimulateSave;
	}
	/**
	 * Checks if the save action is only to be simulated in order to receive the new storage settings.
	 * @return true, if is save simulated
	 */
	public boolean isSaveSimulated() {
		return saveSimulated;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here some helper methods for sub classes ----------------------
	// ------------------------------------------------------------------------
	/**
	 * Can be used to set the indicator that the data model requires a persistence update.
	 * Setting <code>false</code> will have no effect on the local variable. If set <code>true</code>, 
	 * the update will be invoked automatically.
	 * @param requiresPersistenceUpdate the new requires persistence update
	 */
	protected void setRequiresPersistenceUpdate(boolean requiresPersistenceUpdate) {
		if (requiresPersistenceUpdate==true) {
			this.requiresPersistenceUpdate = requiresPersistenceUpdate;
		}
	}
	/**
	 * Checks if a persistence update is required (can only be set from extended classes).
	 * @return true, if is requires persistence update
	 */
	public boolean isRequiresPersistenceUpdate() {
		return requiresPersistenceUpdate;
	}
	
	/**
	 * The update persistence model will directly call {@link #saveDataModel(DataModelNetworkElement)}.
	 * Afterwards, it will adjust all relevant storage settings (Base64 Vector and storage setting TreeMap) of the 
	 * specified {@link DataModelNetworkElement} (that is either a {@link NetworkComponent} or {@link GraphNode}).<br><br>
	 * Typically, this method can be called / may be used, if it realized that an old data structure needs to
	 * converted in a new one (e.g. with an ontology update).
	 *
	 * @param networkElement the network element to update
	 * @param dataModel the current data model instance
	 */
	protected void updatePersistenceModel(DataModelNetworkElement networkElement, Object dataModel) {
		
		if (networkElement==null) return;
		
		networkElement.setDataModel(dataModel);
		TreeMap<String, String> storageSettings = this.saveDataModel(networkElement);
		networkElement.setDataModelStorageSettings(storageSettings);
	}
	
	
	/**
	 * Creates a NetworkElement that temporary may be used for saving 
	 * or loading part models of combined / more complex data models.
	 *
	 * @param networkElement the network element
	 * @return the data model network element
	 */
	public DataModelNetworkElement createTemporaryNetworkElement(DataModelNetworkElement networkElement) {
		DataModelNetworkElement tmpNetworkElement = null;
		if (networkElement instanceof NetworkComponent) {
			NetworkComponent srcNetComp = (NetworkComponent) networkElement;
			tmpNetworkElement = new NetworkComponent(srcNetComp.getId(), srcNetComp.getType(), null);
		} else if (networkElement instanceof GraphNode) {
			tmpNetworkElement = new GraphNode(networkElement.getId(), null);
		}
		return tmpNetworkElement;
	}
	
	/**
	 * Adds the specified prefix and the base64 string vector from the network element to the to the summarizing Base64 vector.
	 *
	 * @param sumBase64Vector the summarizing Base64 vector
	 * @param networkElement the network element
	 * @param prefix the prefix to use as separator within the Vector
	 */
	protected void base64VectorAdd(Vector<String> sumBase64Vector, DataModelNetworkElement networkElement, String prefix) {
		if (networkElement==null || networkElement.getDataModelBase64()==null || networkElement.getDataModelBase64().size()==0) return;
		sumBase64Vector.add(BASE64_MODEL_SEPARATOR_PREFIX + prefix);
		sumBase64Vector.addAll(networkElement.getDataModelBase64());
	}
	
	/**
	 * Extracts the specific Base64 vector from the {@link DataModelNetworkElement}s Base64 model.
	 *
	 * @param networkElement the network element
	 * @param prefix the prefix to use for the extraction of the Base64 model
	 * @return the vector
	 */
	protected Vector<String> base64VectorExtract(DataModelNetworkElement networkElement, String prefix) {
		
		if (networkElement==null || networkElement.getDataModelBase64()==null || networkElement.getDataModelBase64().size()==0) return null;
		
		Vector<String> extractBase64 = new Vector<>();
		
		boolean addSequence = false;
		Vector<String> sourceBase64 = networkElement.getDataModelBase64();
		for (int i = 0; i < sourceBase64.size(); i++) {
			
			String singleBase64 = sourceBase64.get(i);
			if (singleBase64.startsWith(BASE64_MODEL_SEPARATOR_PREFIX)) {
				// --- The new regular way --------------------------
				addSequence = singleBase64.equals(BASE64_MODEL_SEPARATOR_PREFIX + prefix);
				continue;
			} else if (singleBase64.equals(prefix)==true) {
				// --- The old style for combined models ------------
				addSequence = true;
				continue;
			}
			if (addSequence==true) {
				extractBase64.add(singleBase64);
			}
		}
		
		if (extractBase64.size()==0) {
			return null;
		}
		return extractBase64;
	}

	
	/**
	 * Merges two or more storage settings into a single new one.
	 *
	 * @param storesToMerge the stores to merge
	 * @return the tree map
	 */
	@SafeVarargs
	protected final TreeMap<String, String> mergeStorageSettings(TreeMap<String, String> ... storesToMerge) {
		
		if (storesToMerge==null || storesToMerge.length==0) return null;

		TreeMap<String, String> mergedStore = new TreeMap<>();
		for (int store = 0; store < storesToMerge.length; store++) {
			
			if (storesToMerge[store]==null) continue;
			
			TreeMap<String, String> settings = storesToMerge[store];
			// --- Transfer to merged store ---------------
			List<String> keyList = new ArrayList<>(settings.keySet());
			for (int i = 0; i < keyList.size(); i++) {
				
				String key = keyList.get(i);
				String value = settings.get(key);
				mergedStore.put(key, value);
			}
		}
		if (mergedStore.size()==0) {
			return null;
		}
		return mergedStore;
	}
	/**
	 * Filters the specified storage settings and return a new reduced one.
	 *
	 * @param storageSettings the storage settings
	 * @param prefix the prefix
	 * @param removePrefix the remove prefix indicator
	 * @return the tree map
	 */
	protected final TreeMap<String, String> prefixFilterStorageSettings(TreeMap<String, String> storageSettings, String prefix, boolean removePrefix) {
		
		if (storageSettings==null || storageSettings.size()==0 || prefix==null || prefix.isEmpty()==true) return null;
		
		TreeMap<String, String> filteredSettings = new TreeMap<>();
		
		List<String> settingKeys = new ArrayList<String>(storageSettings.keySet());
		for (int i = 0; i < settingKeys.size(); i++) {

			String key = settingKeys.get(i);
			String value = storageSettings.get(key);

			if (key.startsWith(prefix)==false) continue;
			
			String keyToUse = key;
			if (removePrefix==true) {
				keyToUse = key.replace(prefix + ".", "");
			}
			filteredSettings.put(keyToUse, value);
		}
		return filteredSettings;
	}
	/**
	 * Removes the specified prefix from specified storage settings.
	 *
	 * @param storageSettings the storage settings
	 * @param prefix the prefix
	 */
	protected final void prefixRemoveFromStorageSettings(TreeMap<String, String> storageSettings, String prefix) {
		
		if (storageSettings==null || storageSettings.size()==0 || prefix==null || prefix.isEmpty()==true) return;
		
		List<String> settingKeys = new ArrayList<String>(storageSettings.keySet());
		for (int i = 0; i < settingKeys.size(); i++) {

			String key = settingKeys.get(i);
			String keyNew = key.replace(prefix + ".", "");
			String value = storageSettings.get(key);
			
			storageSettings.remove(key);
			storageSettings.put(keyNew, value);
		}
	}
	/**
	 * Sets the specified prefix to the specified storage settings.
	 *
	 * @param storageSettings the storage settings
	 * @param prefix the prefix
	 */
	protected final void prefixSetToStorageSettings(TreeMap<String, String> storageSettings, String prefix) {
		
		if (storageSettings==null || storageSettings.size()==0 || prefix==null || prefix.isEmpty()==true) return;
		
		List<String> settingKeys = new ArrayList<String>(storageSettings.keySet());
		for (int i = 0; i < settingKeys.size(); i++) {

			String key = settingKeys.get(i);
			String keyNew = prefix + "." + key;
			String value = storageSettings.get(key);
			
			storageSettings.remove(key);
			storageSettings.put(keyNew, value);
		}
	}

	
	// ------------------------------------------------------------------------
	// --- From here some helper methods to access the current setup ----------
	// ------------------------------------------------------------------------
	/**
	 * Returns the graph environment controller of the currently open project.
	 * @return the graph environment controller
	 */
	protected GraphEnvironmentController getGraphEnvironmentController() {
		Project project = Application.getProjectFocused();
		if (project!=null) {
			EnvironmentController envController = project.getEnvironmentController();
			if (envController instanceof GraphEnvironmentController) {
				return (GraphEnvironmentController) envController;
			}
		}
		return null;
	}
	/**
	 * If such a setup data model storage service is defined, the method will return the actual instance.
	 * @return the setup data model storage handler
	 */
	protected SetupDataModelStorageService getSetupDataModelStorageService() {
		GraphEnvironmentController graphController = this.getGraphEnvironmentController(); 
		if (graphController!=null) {
			return graphController.getSetupDataModelStorageService(this.getClass());
		}
		return null;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here some STATIC helper methods -------------------------------
	// ------------------------------------------------------------------------
	private static final String STORAGE_SETTINGS_KEY_VALUE_SEPARATOR = ",";
	private static final String STORAGE_SETTINGS_PAIR_SEPARATOR = ";";
	
	/**
	 * Returns the data model storage settings (TreeMap) as string.
	 *
	 * @param networkElement the network element
	 * @return the data model storage settings as string
	 */
	public static String getDataModelStorageSettingsAsString(DataModelNetworkElement networkElement) {
		
		TreeMap<String, String> storageSettings = networkElement.getDataModelStorageSettings();

		if (storageSettings==null || storageSettings.size()==0) return null;
		
		String settingString = null;
		List<String> keyList = new ArrayList<>(storageSettings.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			
			String key   = keyList.get(i);
			String value = storageSettings.get(key);
			String pair  = key + STORAGE_SETTINGS_KEY_VALUE_SEPARATOR + value;
			
			if (settingString==null) {
				settingString  = pair;
			} else {
				settingString += STORAGE_SETTINGS_PAIR_SEPARATOR + pair;
			}
		}
		return settingString;
	}
	
	/**
	 * Returns the data model storage settings from string as TreeMap.
	 *
	 * @param settingString the setting string
	 * @return the data model storage settings from string
	 */
	public static TreeMap<String, String> getDataModelStorageSettingsFromString(String settingString) {
		
		if (settingString==null || settingString.isEmpty()==true) return null;
		
		TreeMap<String, String> settings = new TreeMap<>();
		
		String[] pairArrayString = settingString.split(STORAGE_SETTINGS_PAIR_SEPARATOR);
		for (int i = 0; i < pairArrayString.length; i++) {
			
			String pairString = pairArrayString[i];
			String pairArray[] = pairString.split(STORAGE_SETTINGS_KEY_VALUE_SEPARATOR);
			
			String key = pairArray[0];
			String value = pairArray[1];
			if (key!=null && key.isEmpty()==false) {
				settings.put(key, value);
			}
		}
		
		if (settings.size()==0) {
			return null;
		}
		return settings;
	}
	
}
