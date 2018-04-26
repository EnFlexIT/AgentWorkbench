package de.enflexit.db.hibernate.gui;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class HibernateDatabaseServiceConsumer is used to receive bind
 * registrations for {@link HibernateDatabaseService}s and will forward 
 * them to the {@link HibernateUtilities}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class HibernateStateVisualizationServiceConsumer {

	/**
	 * Register the specified HibernateStateVisualizationService.
	 * @param visService the HibernateStateVisualizationService to register
	 */
	public synchronized void registerVisualizationService(HibernateStateVisualizationService visService) {
		StateVisualizer.registerStateVisualizationService(visService);
	}
	/**
	 * Unregister the specified HibernateStateVisualizationService.
	 * @param visService the HibernateStateVisualizationService to register
	 */
	public synchronized void unregisterVisualizationService(HibernateStateVisualizationService visService) {
		StateVisualizer.unregisterStateVisualizationService(visService);
	}
	
}
