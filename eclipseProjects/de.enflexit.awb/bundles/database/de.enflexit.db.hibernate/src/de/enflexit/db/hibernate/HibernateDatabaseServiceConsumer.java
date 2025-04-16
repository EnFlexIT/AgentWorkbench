package de.enflexit.db.hibernate;

/**
 * The Class HibernateDatabaseServiceConsumer is used to receive bind
 * registrations for {@link HibernateDatabaseService}s and will forward 
 * them to the {@link HibernateUtilities}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class HibernateDatabaseServiceConsumer {

	/**
	 * Register the specified HibernateDatabaseService 
	 * @param dbService the HibernateDatabaseService to register
	 */
	public synchronized void registerDatabaseService(HibernateDatabaseService dbService) {
		HibernateUtilities.registerDatabaseService(dbService);
	}
	/**
	 * Unregister the specified HibernateDatabaseService.
	 * @param dbService the HibernateDatabaseService to register
	 */
	public synchronized void unregisterDatabaseService(HibernateDatabaseService dbService) {
		HibernateUtilities.unregisterDatabaseService(dbService);
	}
	
}
