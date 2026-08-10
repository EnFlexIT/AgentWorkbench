package de.enflexit.df.impl.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import de.enflexit.db.hibernate.HibernateUtilities;
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
	 * Checks for valid database settings.
	 * @return true, if successful
	 */
	public boolean hasValidDatabaseSettings() {
		return DatabaseHelper.providesValidDatabaseSettings(this.dataSource.toDatabaseSettings());
	}
	
	/**
	 * Returns the catalog that is currently used.
	 * @return the catalog
	 */
	public String getCatalog() {
		return this.dataSource.getDbName();
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
			connection = HibernateUtilities.getDatabaseConnection(this.dataSource.toDatabaseSettings(), false);
		}
		return connection;
	}
	
	/**
	 * Returns the table dictionary of the current connection.
	 * @return the table dictionary
	 */
	public synchronized TableDictionary getTableDictionary() {
		if (tableDictionary==null && this.getConnection()!=null) {
			tableDictionary = new TableDictionary(this, this.dataSource.getDbName());
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
