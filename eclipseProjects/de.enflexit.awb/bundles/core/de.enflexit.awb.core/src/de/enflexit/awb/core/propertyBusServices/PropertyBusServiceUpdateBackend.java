package de.enflexit.awb.core.propertyBusServices;

import org.eclipse.core.runtime.IProgressMonitor;

import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceExecuteBackendUpdate.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateBackend implements PropertyBusService{
	
	public static final String STATUS = "update.status";
	public static final String PROGRESS = "update.progress";
	public static final String MESSAGE = "update.message";

	private IProgressMonitor progressMonitor;
	private int pmTotalWorkCount;
	private int pmProgressCount;
	private int pmProgressPercent;
	
	private String pmTask;
	private String pmSubTask;
	
	private AWBUpdater awbUpdater;
	
	
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
		
		if (awbUpdater==null) {
			// --- Start the Update process -------------------------
			awbUpdater = new AWBUpdater(true, true);
			awbUpdater.setProgressMonitor(this.getProgressMonitor());
			awbUpdater.start();

			properties.setStringValue(STATUS, "Pending");
			properties.setIntegerValue(PROGRESS, 0);
			properties.setStringValue(MESSAGE, "");
			
		} else {
			
			if (awbUpdater.isUpdateDone()==false) {
				// --- Running update -------------------------------
				properties.setStringValue(STATUS, "Pending");
				properties.setIntegerValue(PROGRESS, this.pmProgressPercent);
				properties.setStringValue(MESSAGE, this.pmTask + ": " + this.pmSubTask);
				
			} else {
				// --- Finalized update -----------------------------
				if (awbUpdater.hasInstalledUpdate()==true) {
					properties.setStringValue(STATUS, "Done");
				} else {
					properties.setStringValue(STATUS, "Error");
				}
				properties.setIntegerValue(PROGRESS, 100);
				properties.setStringValue(MESSAGE, awbUpdater.getFinalMessage());
				
				// --- Destroy AwbUpdater ---------------------------
				awbUpdater = null;
			}
		}
		
		return properties;
	}

	
	/**
	 * Returns the progress monitor.
	 * @return the progress monitor
	 */
	private IProgressMonitor getProgressMonitor() {
		if (progressMonitor==null) {
			progressMonitor = new IProgressMonitor() {
				@Override
				public void beginTask(String name, int totalWork) {
					this.setTaskName(name);
					PropertyBusServiceUpdateBackend.this.pmTotalWorkCount = totalWork;
				}
				@Override
				public void worked(int work) {
					PropertyBusServiceUpdateBackend.this.pmProgressCount += work;
					Long percentLong = Math.round(((double)PropertyBusServiceUpdateBackend.this.pmProgressCount / PropertyBusServiceUpdateBackend.this.pmTotalWorkCount) * 100.0);
					PropertyBusServiceUpdateBackend.this.pmProgressPercent = percentLong.intValue();
				}
				@Override
				public void setTaskName(String name) {
					PropertyBusServiceUpdateBackend.this.pmTask = name;
				}
				@Override
				public void subTask(String name) {
					PropertyBusServiceUpdateBackend.this.pmSubTask = name;
				}
				@Override
				public void done() {
					PropertyBusServiceUpdateBackend.this.pmTotalWorkCount = 0;
					PropertyBusServiceUpdateBackend.this.pmProgressCount =  0;
					PropertyBusServiceUpdateBackend.this.pmProgressPercent = 100;
				}
				
				@Override
				public void internalWorked(double work) { }
				@Override
				public void setCanceled(boolean value) { }
				@Override
				public boolean isCanceled() {
					return false;
				}
			};
		}
		return progressMonitor;
	}
	
}