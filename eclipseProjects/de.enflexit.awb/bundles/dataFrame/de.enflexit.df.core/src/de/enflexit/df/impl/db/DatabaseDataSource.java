package de.enflexit.df.impl.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.enflexit.common.NumberHelper;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.gui.DatabaseSettings;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
public class DatabaseDataSource extends DefaultDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	public final static String KEY_FACTORY_ID = "FactoryID";

	public final static String KEY_DBMS_NAME = "DBMS-Name";
	public final static String KEY_CONNECTION_URL = "connectionURL";
	public final static String KEY_DB_NAME = "DB-Name";
	public final static String KEY_USER_NAME = "UserName";
	public final static String KEY_PASSWORD = "Password";

	private String factoryID;

	private String dbmsName;
	private String connectionURL;
	private String dbName;
	private String userName;
	private String password;

	private DatabaseDataSourceIntegration dbDataSourceIntegration;

	private List<DatabaseQuery> databaseQueryList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.df.core.dataSources.DataSource#newInstance()
	 */
	@Override
	public DatabaseDataSource newInstance() {
		return new DatabaseDataSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.enflexit.df.core.dataSources.DefaultDataSource#getDataSourceIntegration(de
	 * .enflexit.df.core.model.DataController,
	 * de.enflexit.df.core.workbook.DataWorkbook)
	 */
	@Override
	public AbstractDataSourceIntegration<?> getDataSourceIntegration(DataController dataController,
			DataWorkbook dataWorkbook) {
		if (dbDataSourceIntegration == null) {
			dbDataSourceIntegration = new DatabaseDataSourceIntegration(dataController, dataWorkbook, this);
		}
		return dbDataSourceIntegration;
	}

	/**
	 * Returns the factory ID to be used instead of a complete connection
	 * configuration.
	 * 
	 * @return the factory ID´or <code>null</code>
	 */
	public String getFactoryID() {
		return factoryID;
	}

	/**
	 * Sets the factory ID.
	 * 
	 * @param factoryID the new factory ID
	 */
	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
	}

	/**
	 * Returns the DBMS name (e.g. MariaDB or PostGres).
	 * 
	 * @return the DBMS name
	 */
	public String getDBMSName() {
		return dbmsName;
	}

	/**
	 * Sets the DBMS name.
	 * 
	 * @param dbmsName the new DBMS name
	 */
	public void setDBMSName(String dbmsName) {
		this.dbmsName = dbmsName;
	}

	/**
	 * Returns the host or IP.
	 * 
	 * @return the host or IP
	 */
	public String getConnectionURL() {
		return connectionURL;
	}

	/**
	 * Sets the connection URL.
	 * 
	 * @param connectionURL the new connection URL
	 */
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	/**
	 * Returns the database name.
	 * 
	 * @return the database name
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * Sets the database name.
	 * 
	 * @param dbName the new database name
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Returns the database user name.
	 * 
	 * @return the database user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the database user name.
	 * 
	 * @param userName the new database user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the database password.
	 * 
	 * @return the database password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the database password.
	 * 
	 * @param databasePassword the new database password
	 */
	public void setPassword(String databasePassword) {
		this.password = databasePassword;
	}

	// ----------------------------------------------------------------------------------
	// --- From here, single String configuration conversion methods
	// --------------------
	// ----------------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.df.core.dataSources.DataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {

		String config = new String();

		config = DatabaseDataSource.addConfigValue(config, KEY_ID, (this.getId() + ""));
		config = DatabaseDataSource.addConfigValue(config, KEY_NAME, this.getName());
		config = DatabaseDataSource.addConfigValue(config, KEY_DESCRIPTION, this.getDescription());
		config = DatabaseDataSource.addConfigValue(config, KEY_ROWS_PER_PAGE, this.getRowsPerPage() + "");

		if (this.getFactoryID() != null) {
			config = DatabaseDataSource.addConfigValue(config, KEY_FACTORY_ID, this.getFactoryID());
		} else {
			config = DatabaseDataSource.addConfigValue(config, KEY_DBMS_NAME, this.getDBMSName());
			config = DatabaseDataSource.addConfigValue(config, KEY_CONNECTION_URL, this.getConnectionURL());
			config = DatabaseDataSource.addConfigValue(config, KEY_DB_NAME, this.getDbName());
			config = DatabaseDataSource.addConfigValue(config, KEY_USER_NAME, this.getUserName());
			config = DatabaseDataSource.addConfigValue(config, KEY_PASSWORD, this.getPassword());
		}

		if (config.isBlank() == true) {
			config = null;
		}
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.enflexit.df.core.dataSources.DataSource#fromConfigurationString(java.lang.
	 * String)
	 */
	@Override
	public DatabaseDataSource fromConfigurationString(String config) {

		if (config == null || config.isBlank() == true)
			return this;

		String[] keyValuePairs = config.split("\\|");
		if (keyValuePairs.length == 0)
			return this;

		// --- Create new instance ----------------------------------
		for (String keyValuePair : keyValuePairs) {

			int idxTagOpen = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");

			String key = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank() == true)
				continue;

			switch (key) {
			case KEY_ID:
				Integer configID = NumberHelper.parseInteger(value);
				if (this.getId() == 0 && configID != null && configID != 0) {
					this.setId(configID);
				}
				break;
			case KEY_NAME:
				this.setName(value);
				break;
			case KEY_DESCRIPTION:
				this.setDescription(value);
				break;
			case KEY_ROWS_PER_PAGE:
				Integer rowsPerPage = NumberHelper.parseInteger(value);
				if (rowsPerPage != null)
					this.setRowsPerPage(rowsPerPage);
				break;

			case KEY_FACTORY_ID:
				this.setFactoryID(value);
				break;

			case KEY_DBMS_NAME:
				this.setDBMSName(value);
				break;
			case KEY_CONNECTION_URL:
				this.setConnectionURL(value);
				break;
			case KEY_DB_NAME:
				this.setDbName(value);
				break;
			case KEY_USER_NAME:
				this.setUserName(value);
				break;
			case KEY_PASSWORD:
				this.setPassword(value);
				break;
			}
		} // end for
		return this;
	}

	// ----------------------------------------------------------------------------------
	// --- From here, DatabaseSettings conversion methods
	// -------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Converts the current {@link DatabaseDataSource} into
	 * {@link DatabaseSettings}.
	 * 
	 * @return the database settings
	 */
	public DatabaseSettings toDatabaseSettings() {
		return toDatabaseSettings(this);
	}

	/**
	 * Converts the specified {@link DatabaseDataSource} to
	 * {@link DatabaseSettings}.
	 * 
	 * @param dbDataSource the DatabaseDataSource to convert
	 * @return the database settings
	 */
	public static DatabaseSettings toDatabaseSettings(DatabaseDataSource dbDataSource) {

		if (dbDataSource == null)
			return null;

		DatabaseSettings dbSettings = new DatabaseSettings();

		dbSettings.setDatabaseSystemName(dbDataSource.getDBMSName());
		dbSettings.setHibernateDatabaseSettings(new Properties());

		if (dbDataSource.getConnectionURL() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_URL,
					dbDataSource.getConnectionURL());
		if (dbDataSource.getDbName() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog,
					dbDataSource.getDbName());

		if (dbDataSource.getUserName() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName,
					dbDataSource.getUserName());
		if (dbDataSource.getPassword() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Password,
					dbDataSource.getPassword());

		if (dbDataSource.getFactoryID() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_FACTORY_ID,
					dbDataSource.getFactoryID());

		// --- Super class attributes -----------
		if (dbDataSource.getId() != 0)
			dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_ID, dbDataSource.getId() + "");
		if (dbDataSource.getName() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_NAME, dbDataSource.getName());
		if (dbDataSource.getDescription() != null)
			dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_DESCRIPTION,
					dbDataSource.getDescription());
		if (dbDataSource.getRowsPerPage() != 0)
			dbSettings.getHibernateDatabaseSettings().setProperty(DatabaseDataSource.KEY_ROWS_PER_PAGE,
					dbDataSource.getRowsPerPage() + "");

		if (dbSettings.isEmpty() == true) {
			return null;
		}
		return dbSettings;
	}

	/**
	 * Converts the specified DatabaseSettings into a new instance of a
	 * {@link DatabaseDataSource}.
	 * 
	 * @param dbSettings the DatabaseSettings to convert
	 * @return the DatabaseDataSource
	 */
	public static DatabaseDataSource fromDatabaseSettings(DatabaseSettings dbSettings) {

		if (dbSettings == null)
			return null;

		DatabaseDataSource dbDS = new DatabaseDataSource();
		DatabaseDataSource.intoDataSource(dbSettings, dbDS);
		return dbDS;
	}

	/**
	 * Places the specified DatabaseSettings into the current
	 * {@link DatabaseDataSource}.
	 * 
	 * @param dbDS the DatabaseDataSource to edit
	 */
	public void intoDataSource(DatabaseSettings dbSettings) {
		intoDataSource(dbSettings, this);
	}

	/**
	 * Places the specified DatabaseSettings into the specified DatabaseDataSource.
	 *
	 * @param dbSettings the db settings
	 * @param dbDS       the DatabaseDataSource to edit
	 */
	public static void intoDataSource(DatabaseSettings dbSettings, DatabaseDataSource dbDS) {

		if (dbDS == null || dbSettings == null)
			return;

		dbDS.setDBMSName(dbSettings.getDatabaseSystemName());

		dbDS.setConnectionURL(
				dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_URL));
		dbDS.setDbName(dbSettings.getHibernateDatabaseSettings()
				.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog));

		dbDS.setUserName(dbSettings.getHibernateDatabaseSettings()
				.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName));
		dbDS.setPassword(dbSettings.getHibernateDatabaseSettings()
				.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Password));

		dbDS.setFactoryID(dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_FACTORY_ID));

		// ----------------------------------------------------------
		// --- Super class attributes -------------------------------
		String idString = dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_ID);
		if (idString != null) {
			Integer id = NumberHelper.parseInteger(idString);
			if (id != null)
				dbDS.setId(id);
		}
		String name = dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_NAME);
		if (name != null)
			dbDS.setName(name);

		String description = dbSettings.getHibernateDatabaseSettings().getProperty(DatabaseDataSource.KEY_DESCRIPTION);
		if (description != null)
			dbDS.setDescription(description);

		String rowsPerPageString = dbSettings.getHibernateDatabaseSettings()
				.getProperty(DatabaseDataSource.KEY_ROWS_PER_PAGE);
		if (rowsPerPageString != null) {
			Integer rowsPerPage = NumberHelper.parseInteger(rowsPerPageString);
			if (rowsPerPage != null)
				dbDS.setRowsPerPage(rowsPerPage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.enflexit.df.core.dataSources.DefaultDataSource#requiresSubConfiguration()
	 */
	@Override
	public boolean requiresSubConfiguration() {
		return true;
	}

	/**
	 * Returns the database query list.
	 * 
	 * @return the database query list
	 */
	public List<DatabaseQuery> getDatabaseQueryList() {
		if (databaseQueryList == null) {
			databaseQueryList = new ArrayList<>();
			for (int i = 0; i < this.getDataSourceSubConfigurations().size(); i++) {
				String subConfiguration = this.getDataSourceSubConfigurations().get(i);
				DatabaseQuery dbQuery = DatabaseQuery.fromConfigurationString(subConfiguration);
				databaseQueryList.add(dbQuery);
			}
		}
		return databaseQueryList;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#updateSubConfigurations()
	 */
	@Override
	public void updateSubConfigurations() {
		this.saveDatabaseQueryList();
	}
	/**
	 * Saves the current list of DatabaseQuery's.
	 */
	private void saveDatabaseQueryList() {
		this.getDataSourceSubConfigurations().clear();
		for (int i = 0; i < this.getDatabaseQueryList().size(); i++) {
			String subConfiguration = this.getDatabaseQueryList().get(i).toConfigurationString();
			this.getDataSourceSubConfigurations().add(subConfiguration);
		}
	}


}
