package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.db.DataWorkbookDatabaseHandler;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;
import de.enflexit.df.impl.db.DatabaseDataSource;

/**
 * The Class DataWorkbook4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4DB extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	public static final String CONNECTION_MASK_CONFIGURATION = "CONNECTION::[Configuration]::[Extensions]";
	public static final String TAG_CONFIGURATION = "[Configuration]";
	public static final String TAG_EXTENSIONS = "[Extensions]";
	
	private DatabaseDataSource workbookDataSource;
	
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
		if (workbookDataSource==null) {
			workbookDataSource = new DatabaseDataSource();
			workbookDataSource.setId(this.getID());
			workbookDataSource.setName(this.getName());
			workbookDataSource.setDescription(this.getDescription());
		}
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
	 * @see de.enflexit.df.core.workbook.DataWorkbook#removeDataSource(de.enflexit.df.core.dataSources.DefaultDataSource)
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
		
		if (this.getWorkbookDataSource()==null) return null;
		
		String dbConfigString   = this.getWorkbookDataSource().toConfigurationString();
		String extensionsString = String.join(",", this.getWorkbookExtensions());  
		
		String locationDescription = CONNECTION_MASK_CONFIGURATION;
		locationDescription = locationDescription.replace(TAG_CONFIGURATION, dbConfigString);
		locationDescription = locationDescription.replace(TAG_EXTENSIONS, extensionsString);
		
		return new DataWorkbookLocation(this.getID(), this.getClass(), locationDescription);
	}
	/**
	 * Load the DataWorkbook from the specified DataWorkbookLocation.
	 *
	 * @param dwLocation the DataWorkbookLocation
	 * @return the data workbook
	 */
	public static DataWorkbook loadFromDataWorkbookLocation(DataWorkbookLocation dwLocation) {
		
		if (dwLocation==null || dwLocation.getDataWorkbookLocation()==null || dwLocation.getDataWorkbookLocation().isEmpty()==true) return null;

		String location = dwLocation.getDataWorkbookLocation();
		String[] wbPart = location.split("::");
		
		String dbConfigString   = wbPart[1];
		String extensionsString = wbPart.length>=3 ? wbPart[2] : null;
		
		// --- Create DataSource instance ---------------------------
		@SuppressWarnings("resource")
		DatabaseDataSource ds = new DatabaseDataSource().fromConfigurationString(dbConfigString);
		// --- Create list of extensions? ---------------------------
		List<String> extensionList = null;
		if (extensionsString!=null && extensionsString.isBlank()==false) {
			extensionList = Arrays.asList(extensionsString.split(","));
		}
		
		// --- Create DataWorkbook4  instance -----------------------		
		DataWorkbook4DB dataWorkbook = new DataWorkbook4DB();
		dataWorkbook.setID(dwLocation.getID());
		dataWorkbook.setName(ds.getName());
		dataWorkbook.setDescription(ds.getDescription());
		dataWorkbook.setWorkbookDataSource(ds);
		dataWorkbook.setWorkbookExtensions(extensionList);
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
		return dwbDB;
	}
	
}
