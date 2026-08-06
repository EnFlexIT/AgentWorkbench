package de.enflexit.df.impl.db;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class DatabaseDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-EssenS
 */
public class DatabaseDataSource4SQL extends DatabaseDataSource {

	private static final long serialVersionUID = 6704254616526361690L;

	private DatabaseDataSource databaseDataSource;
	private DatabaseQuery databaseQuery;
	
	private DatabaseDataSourceIntegration4SQL dbDataSourceIntegration;

	
	/**
	 * Instantiates a new database data source 4 SQL.
	 */
	public DatabaseDataSource4SQL() {
		this(null, null);
	}
	/**
	 * Instantiates a new database data source 4 SQL.
	 *
	 * @param dbDataSource the DatabaseDataSource to work on
	 * @param dbQuery the DatabaseQuery containing the SQL statement
	 */
	public DatabaseDataSource4SQL(DatabaseDataSource dbDataSource, DatabaseQuery dbQuery) {
		this.setDatabaseDataSource(dbDataSource);
		this.setDatabaseQuery(dbQuery);
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#newInstance()
	 */
	@Override
	public DatabaseDataSource4SQL newInstance() {
		return new DatabaseDataSource4SQL();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#getDataSourceIntegration(de.enflexit.df.core.model.DataController, de.enflexit.df.core.workbook.DataWorkbook)
	 */
	@Override
	public AbstractDataSourceIntegration<?> getDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook) {
		if (dbDataSourceIntegration == null) {
			dbDataSourceIntegration = new DatabaseDataSourceIntegration4SQL(dataController, dataWorkbook, this);
		}
		return dbDataSourceIntegration;
	}

	/**
	 * Returns the database data source.
	 * @return the database data source
	 */
	public DatabaseDataSource getDatabaseDataSource() {
		return databaseDataSource;
	}
	/**
	 * Sets the database data source.
	 * @param databaseDataSource the new database data source
	 */
	public void setDatabaseDataSource(DatabaseDataSource databaseDataSource) {
		this.databaseDataSource = databaseDataSource;
	}

	/**
	 * Returns the database query.
	 * @return the database query
	 */
	public DatabaseQuery getDatabaseQuery() {
		return databaseQuery;
	}
	/**
	 * Sets the database query.
	 * @param databaseQuery the new database query
	 */
	public void setDatabaseQuery(DatabaseQuery databaseQuery) {
		this.databaseQuery = databaseQuery;
	}
	
	
	/**
	 * Returns the factory ID to be used instead of a complete connection onfiguration.
	 * @return the factory ID´or <code>null</code>
	 */
	public String getFactoryID() {
		return this.getDatabaseDataSource().getFactoryID();
	}

	/**
	 * Returns the DBMS name (e.g. MariaDB or PostGres).
	 * @return the DBMS name
	 */
	public String getDBMSName() {
		return this.getDatabaseDataSource().getDBMSName();
	}

	/**
	 * Returns the host or IP.
	 * @return the host or IP
	 */
	public String getConnectionURL() {
		return this.getDatabaseDataSource().getConnectionURL();
	}

	/**
	 * Returns the database name.
	 * @return the database name
	 */
	public String getDbName() {
		return this.getDatabaseDataSource().getDbName();
	}

	/**
	 * Returns the database user name.
	 * @return the database user name
	 */
	public String getUserName() {
		return this.getDatabaseDataSource().getUserName();
	}

	/**
	 * Returns the database password.
	 * @return the database password
	 */
	public String getPassword() {
		return this.getDatabaseDataSource().getPassword();
	}

	// ----------------------------------------------------------------------------------
	// --- From here, single String configuration conversion methods --------------------
	// ----------------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.df.core.dataSources.DataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#fromConfigurationString(java.lang.String)
	 */
	@Override
	public DatabaseDataSource4SQL fromConfigurationString(String config) {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#requiresSubConfiguration()
	 */
	@Override
	public boolean requiresSubConfiguration() {
		return false;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#updateSubConfigurations()
	 */
	@Override
	public void updateSubConfigurations() {
	}

}
