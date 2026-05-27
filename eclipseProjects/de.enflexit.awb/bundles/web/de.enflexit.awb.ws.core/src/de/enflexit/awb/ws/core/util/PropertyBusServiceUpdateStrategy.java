package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings.UpdateStrategy;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateStrategy.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateStrategy implements PropertyBusService {

	public static final String UPDATE_STRATEGY = "isautoupdate";

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.STRATEGY";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		
		// --- Find out whether the strategy is going to change ---------------------------------------------
		int currentStrategy = Application.getGlobalInfo().getUpdateAutoConfiguration();
		int newUpdateStrategy = properties.getBooleanValue(UPDATE_STRATEGY) == true ? AWBUpdater.UPDATE_MODE_AUTOMATIC : AWBUpdater.UPDATE_MODE_ASK;
		
		if (currentStrategy != newUpdateStrategy) {
			// --- Get the webAppSettings -------------------------------------------------------------------
			JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
			JettyWebApplicationSettings webAppSettings = jettyConfig.getWebApplicationSettings();
			
			// --- Set the desired value --------------------------------------------------------------------
			if (properties.getBooleanValue(UPDATE_STRATEGY) == true) {
				Application.getGlobalInfo().setUpdateAutoConfiguration(AWBUpdater.UPDATE_MODE_AUTOMATIC);
				webAppSettings.setUpdateStrategy(UpdateStrategy.Automatic);
			}				
			else {
				Application.getGlobalInfo().setUpdateAutoConfiguration(AWBUpdater.UPDATE_MODE_ASK);
				webAppSettings.setUpdateStrategy(UpdateStrategy.AskUser);
			}
			Application.getGlobalInfo().doSaveConfiguration();
			jettyConfig.save();
		}
		
		return true;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		boolean isAutoUpdate = Application.getGlobalInfo().getUpdateAutoConfiguration() == AWBUpdater.UPDATE_MODE_AUTOMATIC? true: false;
		properties.setBooleanValue(UPDATE_STRATEGY, isAutoUpdate);
		
		return properties;
	}

}