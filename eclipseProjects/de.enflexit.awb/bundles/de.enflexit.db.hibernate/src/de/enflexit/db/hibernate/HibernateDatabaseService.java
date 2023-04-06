package de.enflexit.db.hibernate;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel;

/**
 * The Interface HibernateDatabaseService describes the required methods
 * to configure, test and interact with a database system. It can bes used 
 * to add a new database system to AWB and its hibernate bundle. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public interface HibernateDatabaseService {

	public static final String HIBERNATE_PROPERTY_DriverClass = "hibernate.connection.driver_class";
	public static final String HIBERNATE_PROPERTY_Catalog = "hibernate.default_catalog";
	public static final String HIBERNATE_PROPERTY_URL = "hibernate.connection.url";
	public static final String HIBERNATE_PROPERTY_UserName = "hibernate.connection.username";
	public static final String HIBERNATE_PROPERTY_Password = "hibernate.connection.password";
	
	
	/**
	 * Has to return the database system name (e.g. MySQl, Oracle or PostgreSQL)
	 * and will be returned as selection base for a database system to use.
	 * @return the database system name
	 */
	public String getDatabaseSystemName();
	
	/**
	 * Has to return the driver class as an ID for a hibernate database service.
	 * @return the driver class name
	 */
	public String getDriverClassName();
	
	/**
	 * Has to return the default property settings for hibernate.
	 * @return the default property settings
	 */
	public Properties getHibernateDefaultPropertySettings();
	
	/**
	 * Has to return the property names that have to configured for a hibernate configuration, in
	 * order to check the database connection on a JDBC level (e.g. connection host or IP, port,
	 * user name and password).
	 *
	 * @return the hibernate configuration property name
	 */
	public Vector<String> getHibernateConfigurationPropertyNamesForDbCheckOnJDBC();
	
	/**
	 * Based on the specified properties, has to return the database connection.
	 * 
	 * @param hibernateProperties the hibernate properties
	 * @param userMessageVector the vector in which messages to users can be stored
	 * @param isPrintToConole set true, if information should be printed to console

	 * @return the database connection
	 */
	public Connection getDatabaseConnection(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole);
	
	/**
	 * Has to return the list of databases database list.
	 *
	 * @param connection the connection
	 * @return the database list
	 */
	public List<String> getDatabaseList(Connection connection);
	
	/**
	 * Has to return, if the database specified by its name exists by suing the current connection.
	 *
	 * @param connection the connection
	 * @param dbNameToCheck the db name to check
	 * @return true, if successful
	 */
	public boolean databaseExists(Connection connection, String dbNameToCheck);
	
	/**
	 * Has to create a database with the specified name.
	 *
	 * @param connection the connection
	 * @param dbToCreate the name of the database to create
	 * @return true, if successful
	 */
	public boolean createDatabase(Connection connection, String dbToCreate);
	
	/**
	 * Has to drop the database that is specified by its name.
	 *
	 * @param connection the connection
	 * @param dbToDrop the name of the database to drop
	 * @return true, if successful
	 */
	public boolean dropDatabase(Connection connection, String dbToDrop);
	
	/**
	 * Will be called to check if a database is accessible.
	 *
	 * @param hibernateProperties the hibernate properties
	 * @param userMessageVector the vector in which messages to users can be stored
	 * @param isPrintToConole set true, if information should be printed to console
	 * @return true, if successful
	 */
	public boolean isDatabaseAccessible(Properties hibernateProperties, Vector<String> userMessageVector, boolean isPrintToConole);

	/**
	 * Has to return the hibernate setting panel for the specific database system.
	 * @return the hibernate setting panel
	 */
	public AbstractDatabaseSettingsPanel getHibernateSettingsPanel();

}
