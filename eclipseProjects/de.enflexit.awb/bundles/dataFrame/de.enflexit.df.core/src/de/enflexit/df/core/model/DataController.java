package de.enflexit.df.core.model;

import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbookLocation;
import de.enflexit.df.core.workbook.DataWorkbookReminder;

/**
 * The Class DataController.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataController {

	/**
	 * Instantiates a new data controller.
	 */
	public DataController() {
		this.getDataTreeModel();
		this.getDataWorkbookReminder();
	}
	
	// ------------------------------------------------------------------------
	// --- From here, PropertyChangeSupport -----------------------------------
	// ------------------------------------------------------------------------	
	public static final String DC_ADDED_DATA_WORKBOOK = "DC_ADDED_DATA_WORKBOOK";
	public static final String DC_OPENED_DATA_WORKBOOK = "DC_OPENED_DATA_WORKBOOK";
	public static final String DC_CLOSED_DATA_WORKBOOK = "DC_CLOSED_DATA_WORKBOOK";
	public static final String DC_REMOVED_DATA_WORKBOOK = "DC_REMOVED_DATA_WORKBOOK";
	
	public static final String DC_DATA_WORKBOOK_CONFIGURATION_CHANGED = "DC_DATA_WORKBOOK_CONFIGURATION_CHANGED";
	
	public static final String DC_ADDED_DATA_SOURCE = "DC_ADDED_DATA_SOURCE";
	public static final String DC_OPENED_DATA_SOURCE = "DC_OPENED_DATA_SOURCE";
	public static final String DC_CLOSED_DATA_SOURCE = "DC_CLOSED_DATA_SOURCE";
	public static final String DC_REMOVED_DATA_SOURCE = "DC_REMOVED_DATA_SOURCE";

	public static final String DC_NEW_TREE_PATH_SELECTED = "DC_NEW_TREE_PATH_SELECTED";
	public static final String DC_DATA_SOURCE_CONFIGURATION_SHOW = "DC_DATA_SOURCE_CONFIGURATION_SHOW";
	public static final String DC_DATA_SOURCE_CONFIGURATION_CHANGED = "DC_DATA_SOURCE_CONFIGURATION_CHANGED";
	
	public static final String DC_DATA_LOADED = "DC_DATA_LOADED";
	
	
	private PropertyChangeSupport pcs;
	
	/**
	 * Returns the property change support.
	 * @return the property change support
	 */
	private PropertyChangeSupport getPropertyChangeSupport() {
		if (pcs == null) {
			pcs = new PropertyChangeSupport(this);
		}
		return pcs;
	}
	/**
	 * Adds the property change listener.
	 * @param listener the listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.getPropertyChangeSupport().addPropertyChangeListener(listener);
	}
	/**
	 * Removes the property change listener.
	 * @param listener the listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.getPropertyChangeSupport().removePropertyChangeListener(listener);
	}
	/**
	 * Fires a property change event.
	 *
	 * @param propertyName the property name
	 * @param oldValue the old value
	 * @param newValue the new value
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		this.getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
	}

	// ------------------------------------------------------------------------
	// --- From here, workbook reminder ---------------------------------------
	// ------------------------------------------------------------------------	
	private DataWorkbookReminder dataWorkbookReminder;
	
	/**
	 * Returns the DataWorkbookReminder.
	 * @return the data workbook reminder
	 */
	public DataWorkbookReminder getDataWorkbookReminder() {
		if (dataWorkbookReminder==null) {
			dataWorkbookReminder = new DataWorkbookReminder(this);
		}
		return dataWorkbookReminder;
	}

	
	// ------------------------------------------------------------------------
	// --- From here, runtime objects -----------------------------------------
	// ------------------------------------------------------------------------	
	private DataControllerSelectionModel selectionModel;
	
	private List<DataWorkbook> dataWorkbooks;
	private DataTreeModel dataTreeModel; 

	
	/**
	 * Returns the {@link DataControllerSelectionModel}.
	 * @return the selection model
	 */
	public DataControllerSelectionModel getSelectionModel() {
		if (selectionModel==null) {
			selectionModel = new DataControllerSelectionModel(this);
		}
		return selectionModel;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, Data Workbook handling ----------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the list of data workbooks.
	 * @return the data workbooks
	 */
	public List<DataWorkbook> getDataWorkbooks() {
		if (dataWorkbooks==null) {
			dataWorkbooks = new ArrayList<>();
		}
		return dataWorkbooks;
	}
	/**
	 * Checks for an equal or duplicate DataWorkbook in the list of DataWorkbooks.
	 *
	 * @param dwSearch the DataWorkbook to search for
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook(DataWorkbook dwSearch) {
		for (DataWorkbook dw : this.getDataWorkbooks()) {
			if (dw.equals(dwSearch)==true || dw==dwSearch) {
				return dw;
			}
		}
		return null;
	}
	/**
	 * Returns the DataWorkbook that matches the specified {@link DataWorkbookLocation}.
	 *
	 * @param dwLocation the DataWorkbookLocation to search with
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook(DataWorkbookLocation dwLocation) {
		for (DataWorkbook dw : this.getDataWorkbooks()) {
			if (dw.getDataWorkbookLocation().equals(dwLocation)==true) {
				return dw;
			}
		}
		return null;
	}
	
	/**
	 * Adds the specified {@link DataWorkbook}.
	 * @param dataWorkbook the data workbook
	 */
	public boolean addDataWorkbook(DataWorkbook dataWorkbook) {
		if (dataWorkbook!=null && this.getDataWorkbooks().contains(dataWorkbook)==false) {
			boolean success = this.getDataWorkbooks().add(dataWorkbook);
			this.getPropertyChangeSupport().firePropertyChange(DC_ADDED_DATA_WORKBOOK, null, AffectedDataObjects.create(dataWorkbook));
			return success;
		}
		return false;
	}
	/**
	 * Open data workbook.
	 * @param dataWorkbook the data workbook
	 */
	public void openDataWorkbook(DataWorkbook dataWorkbook) {

		if (dataWorkbook==null) return;
		
		// --- Check if we have a workbook with this settings -------
		DataWorkbook dataWorkbookLocal = this.getDataWorkbook(dataWorkbook); 
		DataWorkbook dataWorkbookWork  = dataWorkbookLocal==null ? dataWorkbook : dataWorkbookLocal;  
		
		// --- If not loaded yet, load the data workbook first ------
		if (dataWorkbookLocal==null) {
			this.addDataWorkbook(dataWorkbookWork);
		}

		// --- Open the data sources --------------------------------
		for (AbstractDataSource ds : dataWorkbookWork.getDataSources()) {
			this.openDataSource(dataWorkbookWork, ds);
		}
		this.getPropertyChangeSupport().firePropertyChange(DC_OPENED_DATA_WORKBOOK, null, AffectedDataObjects.create(dataWorkbookWork));
	}
	/**
	 * Close data workbook.
	 * @param dataWorkbook the data workbook
	 */
	public void closeDataWorkbook(DataWorkbook dataWorkbook) {
		
		if (dataWorkbook==null) return;

		if (this.getDataWorkbooks().contains(dataWorkbook)==true) {
			// --- Close the data sources ---------------------------
			for (AbstractDataSource ds : dataWorkbook.getDataSources()) {
				this.closeDataSource(dataWorkbook, ds);
			}
			this.getPropertyChangeSupport().firePropertyChange(DC_CLOSED_DATA_WORKBOOK, null, AffectedDataObjects.create(dataWorkbook));
		}
	}
	/**
	 * Removes the {@link DataWorkbook}.
	 *
	 * @param dataWorkbook the data workbook
	 * @return true, if successful
	 */
	public boolean removeDataWorkbook(DataWorkbook dataWorkbook) {
		if (dataWorkbook!=null) {
			boolean success = this.getDataWorkbooks().remove(dataWorkbook);
			// --- Inform property change listener ------------------
			this.getPropertyChangeSupport().firePropertyChange(DC_REMOVED_DATA_WORKBOOK, AffectedDataObjects.create(dataWorkbook), null);
			return success;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// --- From here, Data Source handling ------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Adds the specified data source.
	 *
	 * @param dw the DataWorkbook to work on
	 * @param dataSource the data source
	 * @return true, if successfully added
	 */
	public boolean addDataSource(DataWorkbook dw, AbstractDataSource dataSource) {
		if (dw!=null && dataSource!=null && dw.getDataSources()!=null && dw.getDataSources().contains(dataSource)==false) {
			boolean success = dw.getDataSources().add(dataSource);
			this.getPropertyChangeSupport().firePropertyChange(DC_ADDED_DATA_SOURCE, null, AffectedDataObjects.create(dw, dataSource));
			return success;
		}
		return false;
	}
	/**
	 * Opens the specified data source.
	 *
	 * @param dw the DataWorkbook to work on
	 * @param dataSource the data source
	 * @return true, if successfully opened
	 */
	public boolean openDataSource(DataWorkbook dw, AbstractDataSource dataSource) {
		
		if (dw==null || dataSource==null || dw.getDataSources()==null) return false;
		
		if (dw.getDataSources().contains(dataSource)==false) {
			return this.addDataSource(dw, dataSource);
		} 
		this.getPropertyChangeSupport().firePropertyChange(DC_OPENED_DATA_SOURCE, null, AffectedDataObjects.create(dw, dataSource));
		return true;
	}
	/**
	 * Closes the specified data source.
	 *
	 * @param dw the DataWorkbook to work on
	 * @param dataSource the data source
	 * @return true, if successfully opened
	 */
	public boolean closeDataSource(DataWorkbook dw, AbstractDataSource dataSource) {
		
		if (dw==null || dataSource==null || dw.getDataSources()==null) return false;
		
		this.getPropertyChangeSupport().firePropertyChange(DC_CLOSED_DATA_SOURCE, AffectedDataObjects.create(dw, dataSource), null);
		return true;
	}
	/**
	 * Removes the specified data source.
	 *
	 * @param dw the DataWorkbook to work on
	 * @param dataSource the data source
	 * @return true, if successfully removed
	 */
	public boolean removeDataSource(DataWorkbook dw, AbstractDataSource dataSource) {
		if (dw!=null && dataSource!=null && dw.getDataSources()!=null) {
			boolean success = dw.getDataSources().remove(dataSource);
			// --- Inform property change listener ------------------
			this.getPropertyChangeSupport().firePropertyChange(DC_REMOVED_DATA_SOURCE, AffectedDataObjects.create(dw, dataSource), null);
			return success;
		}
		return false;
	}
	
	/**
	 * Removes the data source ask user.
	 *
	 * @param owner the owner
	 * @param dw the DataWorkbook to work on
	 * @param dSource the data source to delete
	 * @param dataSourceCaption the data source caption
	 * @return true, if successful
	 */
	public boolean removeDataSourceAskUser(Window owner, DataWorkbook dw, AbstractDataSource dSource, String dataSourceCaption) {
		
		String message = "Would you like to delete the selected data source '" + dataSourceCaption + "'?";
		int userAnswer = AwbMessageDialog.showConfirmDialog(owner, message, "Delete Data Source?", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (userAnswer==JOptionPane.NO_OPTION) return false;
		// --- Delete data source -------------------------
		return this.removeDataSource(dw, dSource);
	}
	
	/**
	 * Returns the data source by search phrase.
	 *
	 * @param dw the DataWorkbook to work on
	 * @param searchPhrase the search phrase
	 * @return the data source by search phrase
	 */
	public List<AbstractDataSource> getDataSourceBySearchPhrase(DataWorkbook dw, String searchPhrase) {
		String praseToUse = searchPhrase.toLowerCase();
		List<AbstractDataSource> dsFound = new ArrayList<>();
		for (AbstractDataSource ds : dw.getDataSources()) {
			if (ds.getName().toLowerCase().contains(praseToUse)==true) {
				dsFound.add(ds);
			}
		}
		return dsFound;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, DataStructure structure handling ------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the current DataTreeModel.
	 * @return the data tree model
	 */
	public DataTreeModel getDataTreeModel() {
		if (dataTreeModel==null) {
			dataTreeModel = new DataTreeModel(this);
		}
		return dataTreeModel;
	}
	
}
