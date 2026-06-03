package de.enflexit.awb.core.propertyBusServices;

import org.eclipse.core.runtime.IStatus;

import de.enflexit.awb.core.Application;
import de.enflexit.common.ExecutionEnvironment;
import de.enflexit.common.p2.P2OperationsHandler;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceExecuteBackendUpdate.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateBackend implements PropertyBusService{
	
	public static final String STATUS = "update.status";
	public static final String RESTARTREQUIRED = "isrestartrequired";

	private Thread workerThread;
	private IStatus result;
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.BACKEND.EXECUTE";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		if (Application.getGlobalInfo().getExecutionEnvironment() == ExecutionEnvironment.ExecutedOverIDE) {
			return properties;
		}
		
		if (workerThread == null && result == null) {
			workerThread = new Thread(this::doUpdate, Application.getGlobalInfo().getApplicationTitle() + "-Updater");
			workerThread.start();
			properties.setStringValue(STATUS, "Pending");
			
		} else if(workerThread == null && result != null && result.isOK()) {
			// TODO Method to find out if a restart is really required?
			// TODO Set flag that a restart is required?
			properties.setStringValue(STATUS, "Done");
			properties.setBooleanValue(RESTARTREQUIRED, true);
		}
		return properties;
	}
	
	/**
	 * Install the update.
	 */
	private void doUpdate() {
		result = P2OperationsHandler.getInstance().installAvailableUpdates();
		workerThread = null;
	}

}