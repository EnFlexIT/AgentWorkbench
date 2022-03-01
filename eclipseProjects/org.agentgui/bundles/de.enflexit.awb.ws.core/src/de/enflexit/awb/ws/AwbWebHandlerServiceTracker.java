package de.enflexit.awb.ws;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.enflexit.awb.ws.core.JettyServerManager;

/**
 * The Class AwbWebServiceManager.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebHandlerServiceTracker extends ServiceTracker<AwbWebHandlerService, AwbWebHandlerService> {

	private boolean debug = false;
	
	/**
	 * Instantiates a new AwbWebHandlerServiceTracker.
	 *
	 * @param context the context
	 * @param clazz the clazz
	 * @param customizer the customizer
	 */
	public AwbWebHandlerServiceTracker(BundleContext context, Class<AwbWebHandlerService> clazz, ServiceTrackerCustomizer<AwbWebHandlerService, AwbWebHandlerService> customizer) {
		super(context, clazz, customizer);
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AwbWebHandlerService addingService(ServiceReference<AwbWebHandlerService> reference) {
		
		AwbWebHandlerService wsHandler = super.addingService(reference);
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Adding Service " + wsHandler.getClass().getName());
		JettyServerManager.getInstance().addAwbWebHandlerService(wsHandler);
		return wsHandler;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<AwbWebHandlerService> reference, AwbWebHandlerService service) {
		super.removedService(reference, service);
	}
	
}
