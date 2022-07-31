package de.enflexit.db.hibernate;

import org.osgi.framework.BundleContext;

import de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService;
import de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionServiceTracker;

/**
 * The Class HibernateBundleActivator extends the regular OSGI bundle activator
 * of Hibernate, but adds a the {@link HibernateDatabaseConnectionServiceTracker}. 
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HibernateBundleActivator extends org.hibernate.osgi.HibernateBundleActivator {

	private HibernateDatabaseConnectionServiceTracker dbConnectionsTracker;
	
	/* (non-Javadoc)
	 * @see org.hibernate.osgi.HibernateBundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// --- Call super method --------------------------
		super.start(context);
		// --- Track service registration -----------------
		if (dbConnectionsTracker==null) {
			dbConnectionsTracker = new HibernateDatabaseConnectionServiceTracker(context, HibernateDatabaseConnectionService.class, null);
		}
		dbConnectionsTracker.open();
	}

	/* (non-Javadoc)
	 * @see org.hibernate.osgi.HibernateBundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// --- Stop tracking corresponding OSG-services ---
		if (dbConnectionsTracker!=null) {
			dbConnectionsTracker.close();
			dbConnectionsTracker = null;
		}
		// --- Call super method --------------------------
		super.stop(context);
	}
	
}
