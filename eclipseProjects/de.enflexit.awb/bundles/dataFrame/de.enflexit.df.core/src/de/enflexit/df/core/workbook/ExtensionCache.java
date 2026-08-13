package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.DataWorkbookExtension;
import de.enflexit.df.core.extension.ExtensionManager;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.JToolBarData;

/**
 * The Class ExtensionCache holds the workbook specific extension instances.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExtensionCache {

	private DataWorkbook dataWorkbook;
	private DataController dataController;
	
	private List<DataWorkbookExtension> extensionList;
	
	private List<Component> jComponentListMainToolbar;
	
	
	/**
	 * Instantiates a new extension registry.
	 * @param dataWorkbook the data workbook
	 */
	public ExtensionCache(DataWorkbook dataWorkbook) {
		this.setDataWorkbook(dataWorkbook);
	}

	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	private void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}

	
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Can be used to set the DataController.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}


	/**
	 * Loads all extensions of the current data workbook.
	 */
	public void loadExtensions() {
		this.getDataWorkbook().getWorkbookExtensions().forEach(wbExtName -> this.initializeAndAddNewDataWorkBookExtension(wbExtName));
	}
	/**
	 * Updates the currently loaded extensions by adding new or removing unnecessary extensions.
	 */
	public void updateLoadedExtensions() {

		// --- Get the extensions names of loaded extensions --------
		List<String> wbExtListLoaded = new ArrayList<>();
		this.getExtensionList().forEach(ext -> wbExtListLoaded.add(ext.getExtensionName()));
		
		// --- Load the current/new list of extensions --------------
		for (String wbExtName : this.getDataWorkbook().getWorkbookExtensions()) {
			// --- Check if the extension needs to be loaded --------
			if (wbExtListLoaded.contains(wbExtName)==true) {
				// --- Already loaded and thus fine -----------------
				wbExtListLoaded.remove(wbExtName);
			} else {
				// --- Not loaded yet - load extension --------------
				this.initializeAndAddNewDataWorkBookExtension(wbExtName);
			}
		}
		
		// --- Remove the remaining loaded extensions ---------------
		for (String wbExtName : wbExtListLoaded) {
			this.disposeAndRemoveDataWorkBookExtension(wbExtName);
		}
	}
	/**
	 * Removes the extensions.
	 */
	public void removeExtensions() {
	
		// --- Get the extensions names of loaded extensions --------
		List<String> wbExtListLoaded = new ArrayList<>();
		this.getExtensionList().forEach(ext -> wbExtListLoaded.add(ext.getExtensionName()));
		wbExtListLoaded.forEach(wbExtName -> this.disposeAndRemoveDataWorkBookExtension(wbExtName));
	}
	
	/**
	 * Initializes and adds the DataWorkbookExtension with the specified extension name.
	 * @param wbExtName the name of the DataWorkbookExtension to be loaded
	 */
	private void initializeAndAddNewDataWorkBookExtension(String wbExtName) {
		
		try {
			if (wbExtName==null || this.getDataWorkbookExtensionByExtensionName(wbExtName)!=null) return;
			
			DataWorkbookExtension wbExtensionOrg = ExtensionManager.getDataWorkbookExtension(wbExtName);
			DataWorkbookExtension wbExtensionNew = wbExtensionOrg.newInstance(); 
			wbExtensionNew.initialize(this);
			this.getExtensionList().add(wbExtensionNew);
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_EXTENSION_LOADED, null, AffectedDataObjects.create(this.getDataWorkbook(), wbExtensionNew));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Disposes and removes the DataWorkbookExtension with the specified extension name.
	 * @param wbExtName the extension name to be removed
	 */
	private void disposeAndRemoveDataWorkBookExtension(String wbExtName) {
		
		try {
			if (wbExtName==null) return;
			
			DataWorkbookExtension wbExtension = this.getDataWorkbookExtensionByExtensionName(wbExtName);
			if (wbExtension==null) return;
			
			wbExtension.dispose();
			this.getExtensionList().remove(wbExtension);
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_EXTENSION_REMOVED,  AffectedDataObjects.create(this.getDataWorkbook(), wbExtension), null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the list of extensions currently loaded.
	 * @return the extension list
	 */
	private List<DataWorkbookExtension> getExtensionList() {
		if (extensionList==null) {
			extensionList = new ArrayList<>();
		}
		return extensionList;
	}
	/**
	 * Checks for loaded extensions.
	 * @return true, if successful
	 */
	public boolean hasLoadedExtensions() {
		return this.getExtensionList().size()>0;
	}
	/**
	 * Returns the data workbook extension by extension name.
	 *
	 * @param extensionName the extension name
	 * @return the data workbook extension by extension name
	 */
	private DataWorkbookExtension getDataWorkbookExtensionByExtensionName(String extensionName) {
		for (DataWorkbookExtension ext : this.getExtensionList()) {
			if (ext.getExtensionName().equals(extensionName)==true) {
				return ext;
			}
		}
		return null;
	}
	/**
	 * Returns the extension class name list.
	 * @return the extension class name list
	 */
	private List<String> getExtensionClassNameList() {
		List<String> classNameList = new ArrayList<>();
		this.getExtensionList().forEach(ext -> classNameList.add(ext.getClass().getName()));
		return classNameList;
	}
	/**
	 * Check if an extension of the same class was already added.
	 *
	 * @param wbExtension the DataWorkbookExtension to check for
	 * @return true, if successful
	 */
	public boolean contains(DataWorkbookExtension wbExtension) {
		return this.getExtensionClassNameList().contains(wbExtension.getClass().getName());
	}

	
	
	/**
	 * Returns the main toolbar components that were added by the activated extensions.
	 * @return main toolbar components
	 */
	private List<Component> getMainToolbarComponents() {
		if (jComponentListMainToolbar==null) {
			jComponentListMainToolbar = new ArrayList<>();
		}
		return jComponentListMainToolbar;
	}
	/**
	 * Removes the extension components from the main toolbar.
	 * @param jToolBarData the JToolBarData
	 */
	public void removeMainToolbarComponents(JToolBarData jToolBarData) {
		for (Component comp : this.getMainToolbarComponents()) {
			jToolBarData.remove(comp);
		}
		this.getMainToolbarComponents().clear();
	}
	/**
	 * Adds the main toolbar components from the extensions.
	 * @param jToolBarData the JToolBarData
	 */
	public void addMainToolbarComponents(JToolBarData jToolBarData) {
		
		// --- Remind the components before adding new one ---------- 
		List<Component> componentListBefore = Arrays.asList(jToolBarData.getComponents());
		
		// --- For each extension -----------------------------------
		for (DataWorkbookExtension extension : this.getExtensionList()) {
			try {
				// --- Let the extension add the components --------- 
				extension.addMainToolbarComponents(jToolBarData);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// --- Get the new list of components -----------------------
		List<Component> componentListAfter = Arrays.asList(jToolBarData.getComponents());
		
		// --- Determine the addition ------------------------------- 
		List<Component> componentListAdded = new ArrayList<>(componentListAfter);
		componentListAdded.removeAll(componentListBefore);
		
		this.getMainToolbarComponents().addAll(componentListAdded);
	}

	/**
	 * Will try to get updates for the column description list.
	 * @param columnDescriptionList the column description list
	 */
	public void updateColumnDescriptionList(List<ColumnDescription> columnDescriptionList) {

		if (columnDescriptionList==null || columnDescriptionList.size()==0) return;
		
		for (DataWorkbookExtension extension : this.getExtensionList()) {
			try {
				// --- Let the extension add further descriptions ------------- 
				extension.updateColumnDescriptionList(columnDescriptionList);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
