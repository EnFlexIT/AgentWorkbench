package de.enflexit.df.impl.db;

import java.io.IOException;

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
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSource#getId()
	 */
	@Override
	public int getId() {
		return this.getDatabaseDataSource().getId();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSource#getName()
	 */
	@Override
	public String getName() {
		return this.getDatabaseDataSource().getName();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSource#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.getDatabaseDataSource().getDescription();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSource#getRowsPerPage()
	 */
	@Override
	public int getRowsPerPage() {
		return this.getDatabaseDataSource().getRowsPerPage();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getFactoryID()
	 */
	@Override
	public String getFactoryID() {
		return this.getDatabaseDataSource().getFactoryID();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getDBMSName()
	 */
	@Override
	public String getDBMSName() {
		return this.getDatabaseDataSource().getDBMSName();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getConnectionURL()
	 */
	@Override
	public String getConnectionURL() {
		return this.getDatabaseDataSource().getConnectionURL();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getDbName()
	 */
	@Override
	public String getDbName() {
		return this.getDatabaseDataSource().getDbName();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getUserName()
	 */
	@Override
	public String getUserName() {
		return this.getDatabaseDataSource().getUserName();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.impl.db.DatabaseDataSource#getPassword()
	 */
	@Override
	public String getPassword() {
		return this.getDatabaseDataSource().getPassword();
	}

	
	// ----------------------------------------------------------------------------------
	// --- From here, clean-up for closing a data source --------------------------------
	// ----------------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#open()
	 */
	@Override
	public boolean open() {
		// TODO Auto-generated method stub
		
		return super.open();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
		
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
