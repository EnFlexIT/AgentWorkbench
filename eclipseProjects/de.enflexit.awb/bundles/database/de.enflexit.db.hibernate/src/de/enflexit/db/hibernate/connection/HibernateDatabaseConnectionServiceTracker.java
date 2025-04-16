package de.enflexit.db.hibernate.connection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The Class HibernateDatabaseConnectionServiceTracker.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HibernateDatabaseConnectionServiceTracker extends ServiceTracker<HibernateDatabaseConnectionService, HibernateDatabaseConnectionService> {

	private boolean debug = false;
	
	/**
	 * Instantiates a new HibernateDatabaseConnectionServiceTracker.
	 *
	 * @param context the context
	 * @param clazz the clazz
	 * @param customizer the customizer
	 */
	public HibernateDatabaseConnectionServiceTracker(BundleContext context, Class<HibernateDatabaseConnectionService> clazz, ServiceTrackerCustomizer<HibernateDatabaseConnectionService, HibernateDatabaseConnectionService> customizer) {
		super(context, clazz, customizer);
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public HibernateDatabaseConnectionService addingService(ServiceReference<HibernateDatabaseConnectionService> reference) {
		
		HibernateDatabaseConnectionService dbConnectionService = super.addingService(reference);
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Adding Service " + dbConnectionService.getClass().getName());
		DatabaseConnectionManager.getInstance().addHibernateDataBaseConnectionService(dbConnectionService);
		return dbConnectionService;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<HibernateDatabaseConnectionService> reference, HibernateDatabaseConnectionService dbConnectionService) {
		
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Removing Service " + dbConnectionService.getClass().getName());
		DatabaseConnectionManager.getInstance().removeHibernateDataBaseConnectionService(dbConnectionService);
		super.removedService(reference, dbConnectionService);
	}
	
}
