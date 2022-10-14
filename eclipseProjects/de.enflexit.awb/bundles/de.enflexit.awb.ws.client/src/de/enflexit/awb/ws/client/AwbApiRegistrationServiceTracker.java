package de.enflexit.awb.ws.client;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The Class AwbApiRegistrationServiceTracker.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbApiRegistrationServiceTracker extends ServiceTracker<AwbApiRegistrationService, AwbApiRegistrationService> {

	private boolean debug = false;
	

	/**
	 * Instantiates a new awb api registration service tracker.
	 *
	 * @param context the context
	 * @param clazz the clazz
	 * @param customizer the customizer
	 */
	public AwbApiRegistrationServiceTracker(BundleContext context, Class<AwbApiRegistrationService> clazz, ServiceTrackerCustomizer<AwbApiRegistrationService, AwbApiRegistrationService> customizer) {
		super(context, clazz, customizer);
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AwbApiRegistrationService addingService(ServiceReference<AwbApiRegistrationService> reference) {
		
		AwbApiRegistrationService apiRegService = super.addingService(reference);
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Adding service " + apiRegService.getClass().getName());
		WsCredentialStore.getInstance().addApiRegistrationService(apiRegService);
		return apiRegService;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<AwbApiRegistrationService> reference, AwbApiRegistrationService apiRegService) {
		
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Stopping service " + apiRegService.getClass().getName());
		WsCredentialStore.getInstance().removeAwbApiRegistrationService(apiRegService);
		super.removedService(reference, apiRegService);
	}
	
}
