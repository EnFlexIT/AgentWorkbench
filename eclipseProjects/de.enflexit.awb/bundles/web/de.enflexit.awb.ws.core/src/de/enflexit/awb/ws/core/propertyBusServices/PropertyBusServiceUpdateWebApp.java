package de.enflexit.awb.ws.core.propertyBusServices;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcess;
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
public class PropertyBusServiceUpdateWebApp implements PropertyBusService {
	
	public static final String STATUS = "update.status";
	
	private WebApplicationUpdateProcess updateProcess;
	
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
		// --- Get the webApplicationSettings ---------------------------------------------------------------
		JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
		JettyWebApplicationSettings webAppSettings = jettyConfig.getWebApplicationSettings();
		// --- Search for the latest version ----------------------------------------------------------------
		WebApplicationVersion latestVersion = WebApplicationUpdate.getWebApplicationUpdate(webAppSettings.getDownloadURL());
		
		if (latestVersion == null) {
			properties.setStringValue(STATUS, "No update available");
			return properties;
		} else {
			if (updateProcess == null) {
			updateProcess = new WebApplicationUpdateProcess(latestVersion);
			updateProcess.start();
			}
		}
		return properties;
	}

}