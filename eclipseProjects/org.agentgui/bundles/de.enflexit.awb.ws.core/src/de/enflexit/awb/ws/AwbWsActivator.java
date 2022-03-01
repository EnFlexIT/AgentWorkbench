package de.enflexit.awb.ws;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The Class WsActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWsActivator implements BundleActivator {

	private AwbWebServerServiceTracker webServerServiceTracker;
	private AwbWebHandlerServiceTracker webHandlerServiceTracker;
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		if (webServerServiceTracker==null) {
			webServerServiceTracker = new AwbWebServerServiceTracker(context, AwbWebServerService.class, null);
			webServerServiceTracker.open();
		}
		if (webHandlerServiceTracker==null) {
			webHandlerServiceTracker = new AwbWebHandlerServiceTracker(context, AwbWebHandlerService.class, null);
			webHandlerServiceTracker.open();
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		webServerServiceTracker.close();
		webHandlerServiceTracker.close();
	}
	
}
