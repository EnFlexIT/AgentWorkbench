package de.enflexit.common.properties.bus;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Class PropertyBusServiceTracker.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyBusServiceTracker extends ServiceTracker<PropertyBusService, PropertyBusService> {

	/**
	 * Instantiates a new property bus service tracker.
	 *
	 * @param context the context
	 * @param clazz the to be tracked
	 * @param customizer the customizer
	 */
	public PropertyBusServiceTracker(BundleContext context ) {
		super(context, PropertyBusService.class, null);
	}

	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public PropertyBusService addingService(ServiceReference<PropertyBusService> reference) {
				
		PropertyBusService pbs = super.addingService(reference);
		ApplicationPropertyBus.getInstance().addPropertyBusService(pbs);
		return pbs;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<PropertyBusService> reference, PropertyBusService pbs) {
		super.removedService(reference, pbs);
		ApplicationPropertyBus.getInstance().removePropertyBusService(pbs);
	}
	
}
