package de.enflexit.awb.ws.core.propertyBusServices;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcess;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcessListener;
import de.enflexit.awb.ws.core.util.WebApplicationVersion;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateWebApp is used to
 * initiate an update for the web application through the 
 * ApplicationPropertyBus.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateWebApp implements PropertyBusService, WebApplicationUpdateProcessListener {
	
	public static final String STATUS = "update.status";
	public static final String MESSAGE = "update.message";
	
	private boolean isUpdateDone;
	private boolean hasInstalledUpdate;
	private Thread workerThread;
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.FRONTEND.EXECUTE";
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
		
		// --- Start the update thread if not already started -------------------------------------
		if (workerThread == null) {
			workerThread = new Thread(this::executeUpdate, "Web Application Update Thread");
			workerThread.start();
			properties.setStringValue(STATUS, "Pending");
			properties.setStringValue(MESSAGE, "");
			return properties;
			
		} else {
			// --- Update thread already started, evaluating results ------------------------------
			if (this.isUpdateDone == false) {
				properties.setStringValue(STATUS, "Pending");
				properties.setStringValue(MESSAGE, "");
				
			} else {
				
				if (this.hasInstalledUpdate == true) {
					properties.setStringValue(STATUS, "Done");
					properties.setStringValue(MESSAGE, "Update installed");
				} else {
					properties.setStringValue(STATUS, "Error");
					properties.setStringValue(MESSAGE, "Nothing to update");
				}
				// --- Reset everything for next call ---------------------------------------------
				workerThread = null;
				isUpdateDone = false;
				hasInstalledUpdate = false;	
			}
		}
		return properties;
	}
	
	/**
	 * Execute update.
	 */
	private void executeUpdate() {
		
		// --- Get the webApplicationSettings ---------------------------------------------------------------
		JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
		JettyWebApplicationSettings webAppSettings = jettyConfig.getWebApplicationSettings();
		// --- Search for the latest version ----------------------------------------------------------------
		WebApplicationVersion latestVersion = WebApplicationUpdate.getWebApplicationUpdate(webAppSettings.getDownloadURL());
		// --- Start the update process if a newer version was found ----------------------------------------
		if (latestVersion != null) {
			 new WebApplicationUpdateProcess(latestVersion, this).start();
		} else {
			this.isUpdateDone = true;
		}
		
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.core.util.WebApplicationUpdateProcessListener#onUpdateProcessFinalized()
	*/
	@Override
	public void onUpdateProcessFinalized() {
		this.isUpdateDone = true;
		this.hasInstalledUpdate = true;
	}

}