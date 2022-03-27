package de.enflexit.awb.ws;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.JettyServerManager;

/**
 * The Class WsActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWsActivator implements BundleActivator, ApplicationListener {

	private boolean debug = false;
	
	private AwbWebServerServiceTracker webServerServiceTracker;
	private AwbWebHandlerServiceTracker webHandlerServiceTracker;
	
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		// --- Add a AWB-ApplicationListener --------------
		Application.addApplicationListener(this);
		
		// --- Track service registration -----------------
		if (this.debug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Starting AWB Webservice tracker ... ");
		if (webServerServiceTracker==null) {
			webServerServiceTracker = new AwbWebServerServiceTracker(context, AwbWebServerService.class, null);
		}
		webServerServiceTracker.open();
		if (webHandlerServiceTracker==null) {
			webHandlerServiceTracker = new AwbWebHandlerServiceTracker(context, AwbWebHandlerService.class, null);
		}
		webHandlerServiceTracker.open();
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		
		// --- Stop tracking corresponding OSG-services ---
		if (webServerServiceTracker!=null) {
			webServerServiceTracker.close();
			webServerServiceTracker = null;
		}
		if (webHandlerServiceTracker!=null) {
			webHandlerServiceTracker.close();
			webHandlerServiceTracker = null;
		}
		// --- Remove AWB-ApplicationListener -------------
		Application.removeApplicationListener(this);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.application.ApplicationListener#onApplicationEvent(agentgui.core.application.ApplicationListener.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		switch (event.getApplicationEvent()) {
		case ApplicationEvent.AWB_START:
			JettyServerManager.getInstance().doServerStart(StartOn.AwbStart);
			break;
		case ApplicationEvent.AWB_STOP:
			JettyServerManager.getInstance().doServerStop(StartOn.AwbStart);
			break;
			
		case ApplicationEvent.PROJECT_FOCUSED:
			if (event.getEventObject()!=null) JettyServerManager.getInstance().doServerStart(StartOn.ProjectLoaded);
			break;
		case ApplicationEvent.PROJECT_CLOSED:
			if (event.getEventObject()!=null) JettyServerManager.getInstance().doServerStop(StartOn.ProjectLoaded);
			break;
			
		case ApplicationEvent.JADE_START:
			JettyServerManager.getInstance().doServerStart(StartOn.JadeStartup);
			break;
		case ApplicationEvent.JADE_STOP:
			JettyServerManager.getInstance().doServerStop(StartOn.JadeStartup);
			break;
		}
	}
	
}
