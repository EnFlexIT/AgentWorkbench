package de.enflexit.awb.timeSeriesDataProvider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.enflexit.awb.core.Application;


/**
 * This activator class simply registers the time series provider as application listener.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProviderActivator implements BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		Application.addApplicationListener(TimeSeriesDataProvider.getInstance());
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Application.removeApplicationListener(TimeSeriesDataProvider.getInstance());
	}

}
