package de.enflexit.df.descriptionService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;
import de.enflexit.df.descriptionService.db.DataColumnDescription;

public class DescriptionsController implements PropertyChangeListener{
	
	public static final String DESCRIPTION_ADDED_OR_UPDATED = "decsriptionAddedOrUpdated";
	
	private DataController dataController;
	private SessionFactoryCreator sessionFactoryCreator;

	private DescriptionServiceDatabaseHandler databaseHandler;
	
	private HashMap<String, DataColumnDescription> columnDescriptions;

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
	 * Gets the data column descriptions.
	 * @return the data column descriptions
	 */
	public HashMap<String, DataColumnDescription> getColumnDescriptions() {
		
		if (columnDescriptions==null) {
			if (this.sessionFactoryCreator==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Unable to load column descriptions from the database, sessionFactoryCreator not available yet");
			} else {
				columnDescriptions = this.loadDataCOlumnDescriptionsFromDB();
			}
		}
		
		return columnDescriptions;
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
	private HashMap<String, DataColumnDescription> loadDataCOlumnDescriptionsFromDB() {
		
		HashMap<String, DataColumnDescription> colDescsMap = new HashMap<String, DataColumnDescription>(); 
		List<DataColumnDescription> desriptionsFromDB = this.getDatabaseHandler().dbLoadEntityInstanceList(DataColumnDescription.class);
		for (DataColumnDescription colDesc : desriptionsFromDB) {
			colDescsMap.put(colDesc.getColumnName(), colDesc);
		}
		return colDescsMap;
	}
	
	/**
	 * Stores the data column descriptions to the database.
	 */
	public void storeDataColumnDescriptionsToDB() {
		for (DataColumnDescription colDesc : this.getColumnDescriptions().values()) {
			this.getDatabaseHandler().dbSaveOrUpdateEntityInstance(colDesc, true);
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
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(DESCRIPTION_ADDED_OR_UPDATED)) {
			DataColumnDescription newDecsription = (DataColumnDescription) evt.getNewValue();
			this.storeDataColumnDescriptionToDB(newDecsription);
		}
	}

}
