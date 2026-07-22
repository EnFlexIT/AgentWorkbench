package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.List;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.db.dataSources.DefaultDataSource;
import de.enflexit.db.dataSources.DatabaseDataSource;
import de.enflexit.df.core.db.DataWorkbookDatabaseHandler;
import de.enflexit.df.core.db.SessionFactoryCreator;
import de.enflexit.df.core.model.DataController;

/**
 * The Class DataWorkbook4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4DB extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	public static final String CONNECTION_MASK_CONFIGURATION = "CONNECTION::[Configuration]";
	public static final String TAG_CONFIGURATION = "[Configuration]";

	public static final String CONNECTION_MASK_FACTORY = "FACTORY::name[Name]|description[Description]|factoryID[FactoryID]";
	public static final String KEY_NAME = "name";
	public static final String TAG_NAME = "Name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String TAG_DESCRIPTION = "Description";
	public static final String KEY_FACTORY_ID = "factoryID";
	public static final String TAG_FACTORY_ID = "FactoryID";
	
	private DatabaseDataSource workbookDataSource;
	private String factoryID;
	
	private SessionFactoryCreator sessionFactoryCreator;
	private DataWorkbookDatabaseHandler dataWorkbookDatabaseHandler;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookFile()
	 */
	@Override
	public File getDataWorkbookFile() {
		// --- Nothing to do here ---------------
		return null;
	}
	
	
	/**
	 * Returns the workbooks data source.
	 * @return the workbook data source
	 */
	public DatabaseDataSource getWorkbookDataSource() {
		return workbookDataSource;
	}
	/**
	 * Sets the workbooks data source.
	 * @param workbookDataSource the new workbook data source
	 */
	public void setWorkbookDataSource(DatabaseDataSource dataSource) {
		this.workbookDataSource = dataSource;
		// --- Synchronize general information ------------ 
		if (this.workbookDataSource!=null) {
			this.workbookDataSource.setId(this.getID());
			this.workbookDataSource.setName(this.getName());
			this.workbookDataSource.setDescription(this.getDescription());
			this.workbookDataSource.setRowsPerPage(0);
		}
	}
	
	/**
	 * Returns the factory ID to be used for database connections.
	 * @return the factory ID
	 */
	public String getFactoryID() {
		return factoryID;
	}
	/**
	 * Sets the factory ID to be used for database connections.
	 * @param factoryID the new factory ID
	 */
	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
	}

	
	/**
	 * Returns the session factory creator.
	 * @return the session factory creator
	 */
	private SessionFactoryCreator getSessionFactoryCreator() {
		if (sessionFactoryCreator==null) {
			sessionFactoryCreator = new SessionFactoryCreator(this.getID());
		}
		return sessionFactoryCreator;
	}
	/**
	 * Sets the SessionFactoryCreator of the DataWorkbook.
	 * @param sessionFactoryCreator the new session factory creator
	 */
	private void setSessionFactoryCreator(SessionFactoryCreator sessionFactoryCreator) {
		if (this.sessionFactoryCreator!=null) {
			this.sessionFactoryCreator.closeSessionFactory();
		}
		this.sessionFactoryCreator = sessionFactoryCreator;
	}
	
	/**
	 * Creates the DataWorkbookDatabaseHandler.
	 * @return the data frame database handler
	 */
	private DataWorkbookDatabaseHandler createDataWorkbookDatabaseHandler() {
		if (this.getWorkbookDataSource()!=null) {
			return this.getSessionFactoryCreator().createDataWorkbookDatabaseHandler(this.getWorkbookDataSource());
		} else if (this.getFactoryID()!=null) {
			return this.getSessionFactoryCreator().createDataWorkbookDatabaseHandler(this.getFactoryID());
		}
		return null;
	}
	/**
	 * Returns the DataWorkbookDatabaseHandler.
	 * @return the data frame database handler
	 */
	public DataWorkbookDatabaseHandler getDataWorkbookDatabaseHandler() {
		if (dataWorkbookDatabaseHandler==null) {
			dataWorkbookDatabaseHandler = this.createDataWorkbookDatabaseHandler();
		}
		return dataWorkbookDatabaseHandler;
	}
	/**
	 * Sets the data workbook database handler.
	 * @param dataWorkbookDatabaseHandler the new data workbook database handler
	 */
	private void setDataWorkbookDatabaseHandler(DataWorkbookDatabaseHandler dataWorkbookDatabaseHandler) {
		if (this.dataWorkbookDatabaseHandler!=null) {
			this.dataWorkbookDatabaseHandler.dispose();
		}
		this.dataWorkbookDatabaseHandler = dataWorkbookDatabaseHandler;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataSources()
	 */
	@Override
	public List<DefaultDataSource> getDataSources() {
		if (dataSources==null) {
			// --- Load from database, if not already done ----------
			DataWorkbookDatabaseHandler dbHandler = this.getDataWorkbookDatabaseHandler();
			if (dbHandler!=null) {
				dataSources = dbHandler.loadDataSources();
				if (dataSources==null) {
					dataSources = super.getDataSources();
				} else {
					this.setDataSourcesFromStorageConfiguration();
				}
			}
		}
		return dataSources;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#removeDataSource(de.enflexit.db.dataSources.DefaultDataSource)
	 */
	@Override
	public boolean removeDataSource(DefaultDataSource dataSource) {
		return super.removeDataSource(dataSource) && this.getDataWorkbookDatabaseHandler().deleteDataSource(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#save()
	 */
	@Override
	public boolean save() {

		try {
			if (this.dataWorkbookDatabaseHandler==null) {
				// --- No DB connection yet - try loading data sources --------
				this.getDataSources();
			} else {
				// --- If DB-handler is available, save the data sources ------
				this.setDataSourcesToStorageConfiguration();
				this.getDataWorkbookDatabaseHandler().saveDataSources(this.getDataSources());
				return true;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#close()
	 */
	@Override
	public void close() {
		this.setDataWorkbookDatabaseHandler(null);
		this.setSessionFactoryCreator(null);
		this.setDataSources(null);
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookLocation()
	 */
	@Override
	public DataWorkbookLocation getDataWorkbookLocation() {
		
		if (this.getWorkbookDataSource()==null && this.getFactoryID()==null) return null;
		
		String locationDescription = null;
		if (this.getFactoryID()==null) {
			// --- Manual connection Settings -----------------------
			String configString = this.getWorkbookDataSource().toConfigurationString();
			locationDescription = CONNECTION_MASK_CONFIGURATION.replace(TAG_CONFIGURATION, configString);
			
		} else {
			// --- Settings according to factory --------------------
			locationDescription = CONNECTION_MASK_FACTORY.replace(TAG_NAME,    (this.getName()==null ? "" : this.getName()));
			locationDescription = locationDescription.replace(TAG_DESCRIPTION, (this.getDescription()==null ? "" : this.getDescription()));
			locationDescription = locationDescription.replace(TAG_FACTORY_ID,  (this.getFactoryID()==null ? "" : this.getFactoryID()));
			
		}
		return new DataWorkbookLocation(this.getID(), this.getClass(), locationDescription);
	}
	/**
	 * Load the DataWorkbook from the specified DataWorkbookLocation.
	 *
	 * @param dwLocation the DataWorkbookLocation
	 * @return the data workbook
	 */
	public static DataWorkbook loadFromDataWorkBookLocation(DataWorkbookLocation dwLocation) {
		
		if (dwLocation==null || dwLocation.getDataWorkbookLocation()==null || dwLocation.getDataWorkbookLocation().isEmpty()==true) return null;

		String location = dwLocation.getDataWorkbookLocation();
		String connectionType = location.substring(0, location.indexOf(":")); 
		
		int cutStart  = location.indexOf("::") + 2;
		int cutStop = location.length();
		
		String valueString = location.substring(cutStart, cutStop);
		
		DataWorkbook4DB dataWorkbook = new DataWorkbook4DB();
		dataWorkbook.setID(dwLocation.getID());
		
		if (connectionType.toLowerCase().equals("CONNECTION".toLowerCase())==true) {
			// --- Manual connection Settings -----------------------
			DatabaseDataSource ds = new DatabaseDataSource().fromConfigurationString(valueString);
			dataWorkbook.setName(ds.getName());
			dataWorkbook.setDescription(ds.getDescription());

			dataWorkbook.setWorkbookDataSource(ds);
			dataWorkbook.setFactoryID(null);
			
		} else {
			// --- Settings according to factory --------------------
			String[] keyValuePairs = valueString.split("\\|");
			for (String keyValuePair : keyValuePairs) {
				
				int idxTagOpen  = keyValuePair.indexOf("[");
				int idxTagClose = keyValuePair.indexOf("]");
				if (idxTagClose==-1 || idxTagClose==-1) continue;
				
				String key   = keyValuePair.substring(0, idxTagOpen);
				String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
				if (value.isBlank()==true) continue;
				
				switch (key) {
				case KEY_NAME:
					dataWorkbook.setName(value);
					break;
				case KEY_DESCRIPTION:
					dataWorkbook.setDescription(value);
					break;
				case KEY_FACTORY_ID:
					dataWorkbook.setFactoryID(value);
					break;
				}
			}
			dataWorkbook.setWorkbookDataSource(null);
			
			// --- No factory ID, no valid return value -------------
			if (dataWorkbook.getFactoryID()==null) {
				return null;
			}
			
		}
		return dataWorkbook;
	}
	
	
	/**
	 * Creates a DB DataWorkbook by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param component the component
	 * @return the DataWorkbook4DB
	 */
	public static DataWorkbook4DB create(DataController dataController, Component component) {
		return DataWorkbook4DB.create(dataController, OwnerDetection.getOwnerWindowForComponent(component));
	}
	/**
	 * Creates a DB DataWorkbook by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param owner the asking component owner window
	 * @return the DataWorkbook4DB
	 */
	public static DataWorkbook4DB create(DataController dataController, Window owner) {
		
		DataWorkbook4DB dwbDB = new DataWorkbook4DB();
		
		dwbDB.getID();
		dwbDB.setName("Database Data Workbook");
		dwbDB.setDescription("Description of the DataWorkbook, stored in a database");
		dwbDB.setWorkbookDataSource(new DatabaseDataSource());
		return dwbDB;
	}
	
}
