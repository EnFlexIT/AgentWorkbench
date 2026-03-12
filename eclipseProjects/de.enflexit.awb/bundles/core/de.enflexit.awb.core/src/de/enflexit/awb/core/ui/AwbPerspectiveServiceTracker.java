package de.enflexit.awb.core.ui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Class AwbPerspectiveServiceTraacker.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbPerspectiveServiceTracker extends ServiceTracker<AwbPerspectiveService, AwbPerspectiveService> {

	/**
	 * Instantiates a new property bus service tracker.
	 *
	 * @param context the context
	 * @param clazz the to be tracked
	 * @param customizer the customizer
	 */
	public AwbPerspectiveServiceTracker(BundleContext context ) {
		super(context, AwbPerspectiveService.class, null);
	}

	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public AwbPerspectiveService addingService(ServiceReference<AwbPerspectiveService> reference) {	
		AwbPerspectiveService pbs = super.addingService(reference);
		AwbPerspective.notifyPerspectiveServiceListener(pbs, true);
		return pbs;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<AwbPerspectiveService> reference, AwbPerspectiveService pbs) {
		super.removedService(reference, pbs);
		AwbPerspective.notifyPerspectiveServiceListener(pbs, false);
	}
	
}
