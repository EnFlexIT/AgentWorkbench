package org.agentgui.bundle;

import java.io.File;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 * The Class BundleLoader.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleLoader {

	
	
	/**
	 * Install and start bundle.
	 *
	 * @param bundleName the bundle name
	 * @throws BundleException the bundle exception
	 */
	public void installAndStartBundle(String bundleName) throws BundleException {
	    
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	    
		Bundle b = bundleContext.installBundle("file:plugins" + File.separator + bundleName);
//		Bundle b = bundleContext.installBundle("reference:file:./test.ServiceA");
	    b.start();
	    
	}
	
	/**
	 * Stop and uninstall bundle.
	 */
	public void stopAndUninstallBundle() {
	
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		
		
	}
	
	
}
