package de.enflexit.db.hibernate;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The Class HibernateActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HibernateActivator implements BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		// --- Start the database connections -------------
		HibernateUtilities.start();
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
