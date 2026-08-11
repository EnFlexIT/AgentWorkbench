package de.enflexit.df.impl.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.gui.DatabaseSettings;
import de.enflexit.df.core.DatabaseHelper;

/**
 * The Class DatabaseConnection serves as provider class that maps a {@link DatabaseDataSource} to a SQL connection instance.
 * Beyond, it provides - if needed - a TableDictionary thats checks for available relations (tables and views)
 * with their belonging columns.  
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * 
 *  @see #getConnection()
 *  @see #getTableDictionary() 
 */
public class DatabaseConnection {

	private DatabaseDataSource dataSource;
	private String catalog;
	
	private HibernateDatabaseService dbService;
	private Connection connection;
	
	private TableDictionary tableDictionary;
	
	
	/**
	 * Instantiates a new database connector.
	 * @param dataSource the data source
	 */
	public DatabaseConnection(DatabaseDataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * Updates the local catalog attribute.
	 * @param dbSettings the DatabaseSettings to consider
	 */
	private void updateCatalog(DatabaseSettings dbSettings) {
		this.catalog = dbSettings.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
	}
	/**
	 * Returns the catalog.
	 * @return the catalog
	 */
	private String getCatalog() {
		return catalog;
	}
	/**
	 * Checks for valid database settings.
	 * @return true, if successful
	 */
	public boolean hasValidDatabaseSettings() {
		DatabaseSettings dbSettings = this.dataSource.toDatabaseSettings();
		this.updateCatalog(dbSettings);
		return DatabaseHelper.providesValidDatabaseSettings(dbSettings);
	}

	/**
	 * Returns the database service that corresponds to the current {@link DatabaseDataSource}.
	 * @return the database service
	 */
	private HibernateDatabaseService getDatabaseService() {
		if (dbService==null) {
			dbService = HibernateUtilities.getDatabaseService(this.dataSource.toDatabaseSettings().getDatabaseSystemName());
		}
		return dbService;
	}
	/**
	 * Applies offset and limit to the specified SQL statement.
	 *
	 * @param sqlStatement the sql statement
	 * @param offset the offset
	 * @param limit the limit
	 * @return the string
	 */
	public String applyOffsetAndLimitToSqlStatement(String sqlStatement, int offset, int limit) {
		return this.getDatabaseService().applyOffsetAndLimitToSqlStatement(sqlStatement, offset, limit);
	}
	
	/**
	 * If established, returns the database connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		if (connection!=null) {
			try {
				if (connection.isClosed()==true) {
					connection = null;
				}
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
		
		if (connection==null) {
			DatabaseSettings dbSettings = this.dataSource.toDatabaseSettings();
			this.updateCatalog(dbSettings);
			connection = HibernateUtilities.getDatabaseConnection(dbSettings, false);
		}
		return connection;
	}
	
	/**
	 * Returns the table dictionary of the current connection.
	 * @return the table dictionary
	 */
	public synchronized TableDictionary getTableDictionary() {
		if (tableDictionary==null && this.getConnection()!=null) {
			tableDictionary = new TableDictionary(this, this.getCatalog());
		}
		return tableDictionary;
	}
	
	/**
	 * Will close the current database connection.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void close() {
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
		this.tableDictionary = null;
	}
}
