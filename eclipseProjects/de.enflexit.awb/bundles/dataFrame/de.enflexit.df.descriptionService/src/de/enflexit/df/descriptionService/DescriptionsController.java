package de.enflexit.df.descriptionService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;
import de.enflexit.df.descriptionService.db.DataColumnDescription;

/**
 * This class handles some basic management operations for the database-based column descriptions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptionsController implements PropertyChangeListener{
	
	public static final String DESCRIPTION_ADDED_OR_UPDATED = "decsriptionAddedOrUpdated";
	
	private DataController dataController;
	private SessionFactoryCreator sessionFactoryCreator;

	private DescriptionServiceDatabaseHandler databaseHandler;
	
	private List<DataColumnDescription> columnDescriptionsList;
	
	private ArrayList<PropertyChangeListener> changeListeners;

	/**
	 * Instantiates a new data column descriptions controller.
	 * @param dataController the data controller
	 * @param sessionFactoryCreator the session factory creator
	 */
	public DescriptionsController(DataController dataController, SessionFactoryCreator sessionFactoryCreator) {
		super();
		this.dataController = dataController;
		this.sessionFactoryCreator = sessionFactoryCreator;
	}
	
	/**
	 * Gets the column descriptions list.
	 * @return the column descriptions list
	 */
	public List<DataColumnDescription> getColumnDescriptionsList() {
		if (columnDescriptionsList==null) {
			if (this.sessionFactoryCreator==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Unable to load column descriptions from the database, sessionFactoryCreator not available yet");
			} else {
				columnDescriptionsList = this.loadDataColumnDescriptionsFromDB();
			}
		}
		return columnDescriptionsList;
	}
	
	/**
	 * Gets the column description with the specified column and table names.
	 * @param columnName the column name
	 * @param tableName the table name, optional
	 * @return the column description
	 */
	public DataColumnDescription getColumnDescription(String columnName, String tableName) {
		if (this.getColumnDescriptionsList()==null) return null;
		for (DataColumnDescription colDesc : this.getColumnDescriptionsList()) {
			if (colDesc.getColumnName().equals(columnName) && (colDesc.getTableName()==null || colDesc.getTableName().equals(DescriptionServiceHelper.removeAlsoAvailableFromTableName(tableName)))) {
				return colDesc;
			}
		}
		return null;
	}
	
	/**
	 * Gets the database handler.
	 * @return the database handler
	 */
	private DescriptionServiceDatabaseHandler getDatabaseHandler() {
		if (databaseHandler==null) {
			if (this.sessionFactoryCreator!=null) {
				String factoryID = this.sessionFactoryCreator.getFactoryID();
				databaseHandler = new DescriptionServiceDatabaseHandler(this.sessionFactoryCreator.getNewDatabaseSession(factoryID));
			}
		}
		return databaseHandler;
	}
	
	/**
	 * Loads the column descriptions from the database
	 * @return the column descriptions
	 */
	private List<DataColumnDescription> loadDataColumnDescriptionsFromDB() {
		return this.getDatabaseHandler().dbLoadEntityInstanceList(DataColumnDescription.class);
	}
	
	/**
	 * Stores the data column descriptions to the database.
	 */
	public void storeDataColumnDescriptionsToDB() {
		if (this.getColumnDescriptionsList()!=null) {
			for (DataColumnDescription colDesc : this.getColumnDescriptionsList()) {
				this.getDatabaseHandler().dbSaveOrUpdateEntityInstance(colDesc, true);
			}
		}
	}
	
	/**
	 * Store a data column description to the data base
	 * @param colDesc the column description
	 */
	public void storeDataColumnDescriptionToDB(DataColumnDescription colDesc) {
		this.getDatabaseHandler().dbSaveOrUpdateEntityInstance(colDesc, true);
	}
	
	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DESCRIPTION_ADDED_OR_UPDATED)) {
			// --- Store or update in the DB ------------------------
			DataColumnDescription newDescription = (DataColumnDescription) pce.getNewValue();
			this.storeDataColumnDescriptionToDB(newDescription);
			
			// --- Pass on to subsequent change listeners ----------- 
			this.notifyChangeListeners(pce);
		}
	}
	
	/**
	 * Gets the change listeners.
	 * @return the change listeners
	 */
	private ArrayList<PropertyChangeListener> getChangeListeners() {
		if (changeListeners==null) {
			changeListeners = new ArrayList<PropertyChangeListener>();
		}
		return changeListeners;
	}
	
	/**
	 * Adds a change listener.
	 * @param changeListener the change listener
	 */
	public void addChangeListener(PropertyChangeListener changeListener) {
		if (this.getChangeListeners().contains(changeListener)==false) {
			this.getChangeListeners().add(changeListener);
		}
	}
	
	/**
	 * Removes a change listener.
	 * @param changeListener the change listener
	 */
	public void removeChangeListener(PropertyChangeListener changeListener) {
		if (this.getChangeListeners().contains(changeListener)==true) {
			this.getChangeListeners().remove(changeListener);
		}
	}
	
	private void notifyChangeListeners(PropertyChangeEvent pce) {
		for (PropertyChangeListener changeListener : this.getChangeListeners()) {
			changeListener.propertyChange(pce);
		}
	}

}
