package de.enflexit.common.fileConfiguration;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Class FileConfigurationServiceTracker is used to track 
 * added and removed FileConfiguration services.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceTracker extends ServiceTracker<FileConfigurationService, FileConfigurationService> {

	public FileConfigurationServiceTracker(BundleContext context) {
		super(context, FileConfigurationService.class, null);
	}
	
	/* (non-Javadoc)
	* @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	*/
	@Override
	public FileConfigurationService addingService(ServiceReference<FileConfigurationService> reference) {
		
		FileConfigurationService fcs = super.addingService(reference);
		FileConfigurationServiceManager.getInstance().addFileConfigurationService(fcs);
		return fcs;
		
	}
	
	/* (non-Javadoc)
	* @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	*/
	@Override
	public void removedService(ServiceReference<FileConfigurationService> reference, FileConfigurationService fcs) {
		super.removedService(reference, fcs);
		FileConfigurationServiceManager.getInstance().removeFileConfigurationService(fcs);
	}
}