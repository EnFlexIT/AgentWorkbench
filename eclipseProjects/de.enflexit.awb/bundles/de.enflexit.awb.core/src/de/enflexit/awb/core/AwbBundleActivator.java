package de.enflexit.awb.core;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.db.hibernate.HibernateUtilities;
import jade.JadeClassLoader;

/**
 * The Class AwbBundleActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbBundleActivator implements BundleActivator, BundleListener {

	public static String localBundleName;
	private boolean debug = false;
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		this.addJadeClassLoadService();
		context.addBundleListener(this);
	}
	/**
	 * Adds the jade class load service.
	 */
	private void addJadeClassLoadService() {
		JadeClassLoader.setClassLoaderService(new JadeClassLoaderService());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		context.removeBundleListener(this);
		this.removeJadeClassLoadService();
	}
	/**
	 * Removes the jade class load service.
	 */
	private void removeJadeClassLoadService() {
		JadeClassLoader.setClassLoaderService(null);
	}
	
	/**
	 * Returns the local bundle name.
	 * @return the local bundle name
	 */
	private String getLocalBundleName() {
		if (localBundleName==null) {
			localBundleName = FrameworkUtil.getBundle(AwbBundleActivator.class).getSymbolicName();
		}
		return localBundleName;
	}
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
	 */
	@Override
	public void bundleChanged(BundleEvent event) {
		
		final Bundle bundle = event.getBundle();
		if (this.debug==true) {
			String symbolicName = bundle.getSymbolicName();
			String type = BundleEvaluator.getBundleEventAsString(event);
			System.out.println(this.getClass().getSimpleName() + "#bundleChanged(event): " + symbolicName + ", event.type: " + type);
		}
		
		// ------------------------------------------------------------------------------
		// --- Make sure that only external bundles will be considered ------------------
		// ------------------------------------------------------------------------------
		if (bundle.getSymbolicName().equals(this.getLocalBundleName())==false) {
			
			switch (event.getType()) {
			case BundleEvent.STARTED:
				// ----------------------------------------------------------------------
				// --- Start search for classes with the BundleEvaluator? ---------------
				// ----------------------------------------------------------------------
				if (Application.getGlobalInfo().isStartBundleEvaluator()==true) {
					// --- Hand over the new bundle to the BundleEvaluator --------------
					new Thread(new Runnable() {
						@Override
						public void run() {
							// --- First, wait for the Hibernate start ----------------------
							HibernateUtilities.waitForSessionFactoryCreation();
							// --- ... search in the new bundle -------------------------
							BundleEvaluator.getInstance().setBundleAdded(bundle);
						}
					}, "BundleEvaluator Starter-Thread for " + bundle.getSymbolicName()).start();
				}
				break;
				
			case BundleEvent.STOPPED:
				// ----------------------------------------------------------------------
				// --- Remove search results from the BundleEvaluator -------------------
				// ----------------------------------------------------------------------
				BundleEvaluator.getInstance().setBundleRemoved(bundle);
				break;

			default:
				break;
			}
		}
	}
	
}
