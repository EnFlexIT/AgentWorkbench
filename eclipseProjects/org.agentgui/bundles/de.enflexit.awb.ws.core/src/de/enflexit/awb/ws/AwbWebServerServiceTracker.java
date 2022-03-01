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
public class AwbWebServerServiceTracker extends ServiceTracker<AwbWebServerService, AwbWebServerService> {

	private boolean debug = false;
	
	/**
	 * Instantiates a new awb web service tracker.
	 *
	 * @param context the context
	 * @param clazz the clazz
	 * @param customizer the customizer
	 */
	public AwbWebServerServiceTracker(BundleContext context, Class<AwbWebServerService> clazz, ServiceTrackerCustomizer<AwbWebServerService, AwbWebServerService> customizer) {
		super(context, clazz, customizer);
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AwbWebServerService addingService(ServiceReference<AwbWebServerService> reference) {
		
		AwbWebServerService awbWebServer = super.addingService(reference);
		if (debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Adding service " + awbWebServer.getClass().getName());
		JettyServerManager.getInstance().addAwbWebServerService(awbWebServer);
		return awbWebServer;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<AwbWebServerService> reference, AwbWebServerService service) {
		super.removedService(reference, service);
	}
	
}
