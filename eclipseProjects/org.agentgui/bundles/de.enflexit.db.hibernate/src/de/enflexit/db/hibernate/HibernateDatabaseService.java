package de.enflexit.db.hibernate;

import java.util.Properties;
import java.util.Vector;

import de.enflexit.db.hibernate.gui.DatabaseSettingPanel;

/**
 * The Interface HibernateDatabaseService describes the requires methods
 * to configure, test and interact with a database system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public interface HibernateDatabaseService {

	/**
	 * Has to return the database system name (e.g. MySQl, Oracle or PostgreSQL)
	 * and will be returned as selection base for a database system to use.
	 * @return the database system name
	 */
	public String getDatabaseSystemName();
	
	/**
	 * Has to return the driver class as an ID for a hibernate database service.
	 * @return the driver class
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
	 * Will be called to check if a database is accessible.
	 *
	 * @param hibernateProperties the hibernate properties
	 * @param doSilentConnectionCheck set true, if a silent connection check has to be done
	 * @return true, if successful
	 */
	public boolean isDatabaseAccessible(Properties hibernateProperties, boolean doSilentConnectionCheck);

	/**
	 * Has to return the hibernate setting panel.
	 * @return the hibernate setting panel
	 */
	public DatabaseSettingPanel getHibernateSettingPanel();

	
}
