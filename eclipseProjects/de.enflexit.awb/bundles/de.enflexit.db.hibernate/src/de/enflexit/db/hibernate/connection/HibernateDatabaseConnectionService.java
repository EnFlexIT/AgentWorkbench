package de.enflexit.db.hibernate.connection;

import org.hibernate.cfg.Configuration;

/**
 * The Interface HibernateDatabaseConnectionService can be used to define and establish 
 * a Hibernate database connection for a specific bundle.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface HibernateDatabaseConnectionService {

	/**
	 * Has to return the factory ID for the database connection.
	 * @return the factory ID
	 */
	public String getFactoryID();

	/**
	 * Has to return the Hibernate configuration to create the database connection.
	 * @return the configuration of the database connection
	 */
	public Configuration getConfiguration();

}
