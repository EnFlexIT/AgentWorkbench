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

/**
 * The Class DataController.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataController {

	// ------------------------------------------------------------------------
	// --- From here, PropertyChangeSupport -----------------------------------
	// ------------------------------------------------------------------------	
	public static final String DC_ADDED_DATA_WORKBOOK = "DC_ADDED_DATA_WORKBOOK";
	public static final String DC_REMOVED_DATA_WORKBOOK = "DC_REMOVED_DATA_WORKBOOK";
	
	public static final String DC_ADDED_DATA_SOURCE = "DC_ADDED_DATA_SOURCE";
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
	// --- From here, runtime objects -----------------------------------------
	// ------------------------------------------------------------------------	
	private DataControllerSelectionModel selectionModel;
	
	private List<DataWorkbook> dataWorkbooks;
	private List<AbstractDataSource> dataSources;
	private DataTreeModel dataTreeModel; 

	
	public DataControllerSelectionModel getSelectionModel() {
		if (selectionModel==null) {
			selectionModel = new DataControllerSelectionModel(this);
		}
		return selectionModel;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, Data Workbook handling ----------------------------------
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
	 * Adds the specified {@link DataWorkbook}.
	 * @param dataWorkbook the data workbook
	 */
	public boolean addDataWorkbook(DataWorkbook dataWorkbook) {
		if (dataWorkbook!=null && this.getDataWorkbooks().contains(dataWorkbook)==false) {
			boolean success = this.getDataWorkbooks().add(dataWorkbook);
			this.getPropertyChangeSupport().firePropertyChange(DC_ADDED_DATA_WORKBOOK, null, dataWorkbook);
			return success;
		}
		return false;
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
			this.getPropertyChangeSupport().firePropertyChange(DC_REMOVED_DATA_WORKBOOK, dataWorkbook, null);
			return success;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// --- From here, Data Source handling ------------------------------------
	/**
	 * Returns the data sources.
	 * @return the data sources
	 */
	public List<AbstractDataSource> getDataSources() {
		if (dataSources==null) {
			dataSources = new ArrayList<>();
		}
		return dataSources;
	}
	/**
	 * Sets the data sources.
	 * @param dataSources the new data sources
	 */
	public void setDataSources(List<AbstractDataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	/**
	 * Adds the specified data source.
	 * @param dataSource the data source
	 * @return true, if successfully added
	 */
	public boolean addDataSource(AbstractDataSource dataSource) {
		if (dataSource!=null && this.getDataSources().contains(dataSource)==false) {
			boolean success = this.getDataSources().add(dataSource);
			this.getPropertyChangeSupport().firePropertyChange(DC_ADDED_DATA_SOURCE, null, dataSource);
			return success;
		}
		return false;
	}
	/**
	 * Removes the specified data source.
	 * @param dataSource the data source
	 * @return true, if successfully removed
	 */
	public boolean removeDataSource(AbstractDataSource dataSource) {
		if (dataSource!=null) {
			boolean success = this.getDataSources().remove(dataSource);
			// --- Inform property change listener ------------------
			this.getPropertyChangeSupport().firePropertyChange(DC_REMOVED_DATA_SOURCE, dataSource, null);
			return success;
		}
		return false;
	}
	/**
	 * Removes the data source ask user.
	 *
	 * @param owner the owner
	 * @param dSource the data source to delete
	 * @param dataSourceCaption the data source caption
	 */
	public boolean removeDataSourceAskUser(Window owner, AbstractDataSource dSource, String dataSourceCaption) {
		
		String message = "Would you like to delete the selected data source '" + dataSourceCaption + "'?";
		int userAnswer = AwbMessageDialog.showConfirmDialog(owner, message, "Delete Data Source?", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (userAnswer==JOptionPane.NO_OPTION) return false;
		// --- Delete data source -------------------------
		return this.removeDataSource(dSource);
	}
	
	/**
	 * Returns the data source by search phrase.
	 *
	 * @param searchPhrase the search phrase
	 * @return the data source by search phrase
	 */
	public List<AbstractDataSource> getDataSourceBySearchPhrase(String searchPhrase) {
		String praseToUse = searchPhrase.toLowerCase();
		List<AbstractDataSource> dsFound = new ArrayList<>();
		for (AbstractDataSource ds : this.getDataSources()) {
			if (ds.getName().toLowerCase().contains(praseToUse)==true) {
				dsFound.add(ds);
			}
		}
		return dsFound;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, DataStructure structure handling ------------------------
	public DataTreeModel getDataTreeModel() {
		if (dataTreeModel==null) {
			dataTreeModel = new DataTreeModel(this);
		}
		return dataTreeModel;
	}
	
}
