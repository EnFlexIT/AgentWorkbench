package de.enflexit.db.derby.server;

import java.nio.file.Path;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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
		
		if (System.getProperties().getProperty("derby.system.home")==null) {
			Path dbPath = PathHandling.getDatabasePath(true);
			System.getProperties().setProperty("derby.system.home", dbPath.toString());
		}
		DerbyNetworkServer.execute();
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		DerbyNetworkServer.terminate();
	}

}
