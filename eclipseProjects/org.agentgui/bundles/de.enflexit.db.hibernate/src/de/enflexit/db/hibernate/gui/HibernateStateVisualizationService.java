package de.enflexit.db.hibernate.gui;

import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

/**
 * The Interface HibernateStateVisualizationService is an OSGI service interface
 * that can be implemented to visualize states of Session Factories build through
 * the hibernate bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public interface HibernateStateVisualizationService {

	/**
	 * Will be invoked to sets the session factory state to the visualization.
	 *
	 * @param factoryID the factory ID
	 * @param sessionFactoryState the session factory state
	 */
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState);
	
}
