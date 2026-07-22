package de.enflexit.db.hibernate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;

/**
 * The Class HibernateDatabaseServiceTracker.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HibernateDatabaseServiceTracker extends ServiceTracker<HibernateDatabaseService, HibernateDatabaseService> {

	private boolean debug = false;
	
	/**
	 * Instantiates a new HibernateDatabaseConnectionServiceTracker.
	 *
	 * @param context the context
	 * @param customizer the customizer
	 */
	public HibernateDatabaseServiceTracker(BundleContext context, ServiceTrackerCustomizer<HibernateDatabaseService, HibernateDatabaseService> customizer) {
		super(context, HibernateDatabaseService.class, customizer);
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public HibernateDatabaseService addingService(ServiceReference<HibernateDatabaseService> reference) {
		
		HibernateDatabaseService dbService = super.addingService(reference);
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Adding Service " + dbService.getClass().getName());
		
		String driverClass = (String) dbService.getHibernateDefaultPropertySettings().get(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
		if (driverClass!=null) {
			DatabaseConnectionManager.getInstance().startRegisteredDatabaseConnectionsByDriverClass(driverClass);
		}
		return dbService;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<HibernateDatabaseService> reference, HibernateDatabaseService dbConnectionService) {
		
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Removing Service " + dbConnectionService.getClass().getName());
		super.removedService(reference, dbConnectionService);
	}
	
}
