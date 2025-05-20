package de.enflexit.db.derby.server;

import java.nio.file.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.enflexit.db.derby.tools.DerbyPathHandling;

/**
 * The Class DerbyBundleActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DerbyBundleActivator implements BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		// --- Bridge logging to SLF4J --------------------
		//System.getProperties().setProperty("derby.stream.error.method", de.enflexit.db.derby.tools.DerbyBridgeToSLF4J.class.getName());
		
		// --- Get derby root path ------------------------
		if (System.getProperties().getProperty("derby.system.home")==null) {
			Path dbPath = DerbyPathHandling.getDatabasePath(null, true);
			System.getProperties().setProperty("derby.system.home", dbPath.toString());
		}
		// --- Start the derby server, if configured so ---
		DerbyNetworkServer.execute();
	}
	
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		
		// --- Shutdown the database server? ----------------------------------
		if (DerbyNetworkServer.isExecuted()==true) {
			// --- Ensure that the Hibernate bundle is stopped before --------- 
			try {
				Bundle hibernateBundle = this.getBundleByName(context.getBundles(), "de.enflexit.db.hibernate");
				if (hibernateBundle!=null) {
					int dState = hibernateBundle.getState();
					if (dState==Bundle.ACTIVE || dState==Bundle.STOPPING || dState==Bundle.STOP_TRANSIENT) {
						hibernateBundle.stop();
					}
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			// --- Terminate Derby database server ----------------------------
			DerbyNetworkServer.terminate();
		}
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
	
}
