package de.enflexit.awb.ws.client;

import org.osgi.framework.BundleContext;

/**
 * The Class BundleActivator activates the {@link WsCredentialStore}
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class BundleActivator implements org.osgi.framework.BundleActivator {
	
	private boolean debug = false;
	
	private AwbApiRegistrationServiceTracker apiRegistrationServiceTracker;
	
	/* (non-Javadoc)
	* @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	*/
	@Override
	public void start(BundleContext context) throws Exception {
     
		// --- Track service registration -----------------
		if (this.debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Starting AWB Webservice tracker ... ");
		if (apiRegistrationServiceTracker==null) {
			apiRegistrationServiceTracker = new AwbApiRegistrationServiceTracker(context, AwbApiRegistrationService.class, null);
		}
		apiRegistrationServiceTracker.open();
		
		// --- Initialize the credential store ------------
		WsCredentialStore.getInstance();
	}

	/* (non-Javadoc)
	* @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	*/
	@Override
	public void stop(BundleContext context) throws Exception {
		// --- Stop tracking corresponding OSG-services ---
		if (apiRegistrationServiceTracker!=null) {
			apiRegistrationServiceTracker.close();
			apiRegistrationServiceTracker = null;
		}
	}

}
