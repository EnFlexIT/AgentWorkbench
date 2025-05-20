package de.enflexit.db.hibernate;

import org.osgi.framework.Bundle;
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
		
		// --- Ensure that the Derby bundle is started before ------- 
		try {
			Bundle derbyBundle = this.getBundleByName(context.getBundles(), "de.enflexit.db.derby");
			if (derbyBundle!=null) {
				int dState = derbyBundle.getState();
				if (dState==Bundle.RESOLVED || dState==Bundle.INSTALLED || dState==Bundle.STARTING) {
					derbyBundle.start();
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Start the database connections -----------------------
		HibernateUtilities.start();
	}
	/**
	 * If to be found, returns the bundle instance for the specified bundle name.
	 *
	 * @param bundles the bundles
	 * @param symBundleName the sym bundle name
	 * @return the bundle by name
	 */
	private Bundle getBundleByName(Bundle[] bundles, String symBundleName) {
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName().equals(symBundleName)==true) {
				return bundle;
			}
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// --- Stop / Destroy the database connections --------------
		HibernateUtilities.stop();
	}

}
