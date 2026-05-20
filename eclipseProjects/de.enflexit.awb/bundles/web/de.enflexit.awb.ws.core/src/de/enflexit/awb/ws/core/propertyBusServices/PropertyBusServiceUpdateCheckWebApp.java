package de.enflexit.awb.ws.core.propertyBusServices;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationVersion;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateCheckFrontend.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateCheckWebApp implements PropertyBusService {

	private static final String ISUPDATEAVAILABLE = "updatecheck.frontend.isavailable";
	private static final String LASTCHECK = "updatecheck.frontend.lastcheck";
	private static final String VERSION = "updatecheck.frontend.version";
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.CHECK";
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
		// --- Get the download URL from webAppSettings -----------------------------------------------------
		JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
		JettyWebApplicationSettings webAppSettings = jettyConfig.getWebApplicationSettings();
		// --- Search for a new version ---------------------------------------------------------------------
		WebApplicationVersion newVersion = WebApplicationUpdate.getWebApplicationUpdate(webAppSettings.getDownloadURL());
		long latestCheckDate = System.currentTimeMillis();

		// --- Null means no new version available ----------------------------------------------------------
		if (newVersion == null) {
			properties.setBooleanValue(ISUPDATEAVAILABLE, false);
			properties.setStringValue(VERSION, WebApplicationUpdate.getCurrentWebApplicationVersion().getVersion().toString());
			properties.setStringValue(LASTCHECK,new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(latestCheckDate)));
		} else {
			properties.setBooleanValue(ISUPDATEAVAILABLE, true);
			properties.setStringValue(VERSION, newVersion.getVersion().toString());
			properties.setStringValue(LASTCHECK,new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(latestCheckDate)));
		}
		webAppSettings.setUpdateLastCheck(latestCheckDate);
		jettyConfig.save();
		return properties;
	}

}