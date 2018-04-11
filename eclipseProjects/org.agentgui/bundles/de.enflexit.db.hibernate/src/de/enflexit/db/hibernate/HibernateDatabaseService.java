package de.enflexit.db.hibernate;

import java.util.Properties;
import java.util.Vector;

/**
 * The Interface HibernateDatabaseService describes the requires methods
 * to configure, test and interact with a database system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public interface HibernateDatabaseService {

	/**
	 * Has to return the driver class as an ID for a hibernate database service.
	 * @return the driver class
	 */
	public String getDriverClassName();
	
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
	
}
