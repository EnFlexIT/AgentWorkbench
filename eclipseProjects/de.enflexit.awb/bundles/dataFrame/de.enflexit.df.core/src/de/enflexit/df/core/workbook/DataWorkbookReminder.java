package de.enflexit.df.core.workbook;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.Application;
import de.enflexit.common.crypto.SecuredProperties;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;

/**
 * The Class DataWorkbookReminder.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbookReminder implements PropertyChangeListener {

	private static final String DATA_WORKBOOK_LOCATION_PREFIX = "dwLocation";
	
	private DataController dataController;
	private String secPropsNodeName;
	
	private List<DataWorkbookLocation> dataWorkbookLocationList;
	
	
	/**
	 * Instantiates a new data workbook reminder.
	 * @param dataController the data controller
	 */
	public DataWorkbookReminder(DataController dataController) {
		this.setDataController(dataController);
		this.loadFromSecuredStorage();
		this.loadDataWorkbookLocationListToDataController();
		this.attachPropertyChangeListener(true);
	}
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Attaches or removes the local property change listener.
	 * @param isDoAttach the is do attach
	 */
	private void attachPropertyChangeListener(boolean isDoAttach) {
		if (isDoAttach==true) {
			if (this.dataController!=null) {
				this.dataController.addPropertyChangeListener(this);
			}
		} else {
			if (this.dataController!=null) {
				this.dataController.removePropertyChangeListener(this);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals(DataController.DC_ADDED_DATA_WORKBOOK)==true) {
			DataWorkbook dw = ((AffectedDataObjects) evt.getNewValue()).getDataWorkbook();
			this.addDataWorkbookLocation(dw.getDataWorkbookLocation(), true);
			this.saveToSecuredStorage();
		} else if (evt.getPropertyName().equals(DataController.DC_REMOVED_DATA_WORKBOOK)==true) {
			DataWorkbook dw = ((AffectedDataObjects) evt.getOldValue()).getDataWorkbook();
			this.removeDataWorkbookLocation(dw.getDataWorkbookLocation(), true);
			this.saveToSecuredStorage();
		}
	}
	
	
	/**
	 * Returns the data workbook location list.
	 * @return the data workbook location list
	 */
	public List<DataWorkbookLocation> getDataWorkbookLocationList() {
		if (dataWorkbookLocationList==null) {
			dataWorkbookLocationList = new ArrayList<>();
		}
		return dataWorkbookLocationList;
	}
	/**
	 * Adds the data workbook location and saves the location settings.
	 *
	 * @param dwLocation the dw location
	 * @param isDoSave the indicator to save
	 * @return true, if successfully added
	 */
	private boolean addDataWorkbookLocation(DataWorkbookLocation dwLocation, boolean isDoSave) {
		if (dwLocation!=null && this.getDataWorkbookLocationList().contains(dwLocation)==false) {
			boolean added = this.getDataWorkbookLocationList().add(dwLocation); 
			if (isDoSave==true && added==true) this.saveToSecuredStorage();
			return added;
		}
		return false;
	}
	/**
	 * Removes the data workbook location and saves the location settings.
	 *
	 * @param dwLocation the dw location
	 * @param isDoSave the indicator to save
	 * @return true, if successfully removed
	 */
	private boolean removeDataWorkbookLocation(DataWorkbookLocation dwLocation, boolean isDoSave) {
		if (dwLocation!=null) {
			boolean removed = this.getDataWorkbookLocationList().remove(dwLocation); 
			if (isDoSave==true && removed==true) this.saveToSecuredStorage();
			return removed;
		}
		return false;
	}

	/**
	 * Loads the data workbook location list to the {@link DataController}.
	 */
	private void loadDataWorkbookLocationListToDataController() {

		// --- Load the files to the data controller ----------------
		for (DataWorkbookLocation dwLocation : this.getDataWorkbookLocationList()) {
			DataWorkbook dw = null;
			if (dwLocation.getDataWorkbookClassName().equals(DataWorkbook4XML.class.getName())) {
				dw = DataWorkbook4XML.loadFromDataWorkbookLocation(dwLocation);
			} else if (dwLocation.getDataWorkbookClassName().equals(DataWorkbook4JSON.class.getName())) {
				dw = DataWorkbook4JSON.loadFromDataWorkBookLocation(dwLocation);
			} else if (dwLocation.getDataWorkbookClassName().equals(DataWorkbook4DB.class.getName())) {
				dw = DataWorkbook4DB.loadFromDataWorkBookLocation(dwLocation);
			}
			this.getDataController().addDataWorkbook(dw);
		}
	}
	
	/**
	 * Returns the secured properties.
	 * @return the secured properties
	 */
	private SecuredProperties getSecuredProperties() {
		return Application.getGlobalInfo().getSecuredProperties();
	}
	/**
	 * Saves the current settings.
	 * @return true, if successful
	 */
	private boolean saveToSecuredStorage() {
		
		// --- Save all registered DataWorkbookLocation ----------------------- 
		int i;
		for (i = 0; i < this.getDataWorkbookLocationList().size(); i++) {
			
			DataWorkbookLocation dwLocation = this.getDataWorkbookLocationList().get(i);
			String locationKey = DATA_WORKBOOK_LOCATION_PREFIX + "[" + i + "]"; 
			String locationValue = dwLocation.getDataWorkbookClassName() + "|" + dwLocation.getDataWorkbookLocation();
			String locationValue64 = Base64.getEncoder().encodeToString(locationValue.getBytes(StandardCharsets.UTF_8));      
			this.getSecuredProperties().putString(this.getSecuredPropertiesNodeName(), locationKey, locationValue64);
		}
		
		// --- Remove locations that are not longer valid ---------------------
		int searchRange = 100;
		int jEnd = i + searchRange;
		for (int j = i; j < jEnd; j++) {
			this.getSecuredProperties().remove(this.getSecuredPropertiesNodeName(), DATA_WORKBOOK_LOCATION_PREFIX + "[" + j + "]");
		}
		
		// --- Save the secured storage ---------------------------------------
		this.getSecuredProperties().save();
		
		return true;
	}
	/**
	 * Loads the recently opened DataWorkbookLocations.
	 * @return true, if successful
	 */
	private boolean loadFromSecuredStorage() {
		
		// --- Search for location keys until nothing can be found anymore ----
		int i = 0;
		while (true) {

			String locationKey = DATA_WORKBOOK_LOCATION_PREFIX + "[" + i + "]"; 
			String locationValue64 = this.getSecuredProperties().getString(this.getSecuredPropertiesNodeName(), locationKey, null);
			if (locationValue64!=null) {
				String locationValue = new String(Base64.getDecoder().decode(locationValue64), StandardCharsets.UTF_8);
				String locationArray[] = locationValue.split("\\|");
				DataWorkbookLocation dwLocation = new DataWorkbookLocation(locationArray[0], locationArray[1]);
				this.addDataWorkbookLocation(dwLocation, false);
			} else {
				break;
			}
			i++;
		}
		return true;
	}
	/**
	 * Returns the secured properties node name.
	 * @return the secured properties node name
	 */
	private String getSecuredPropertiesNodeName() {
		if (secPropsNodeName==null) {
			secPropsNodeName = FrameworkUtil.getBundle(DataController.class).getSymbolicName().replace(".", "/");
		}
		return secPropsNodeName;
	}
}
