package de.enflexit.awb.ws.dynSiteApi;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.server.AwbServer;

/**
 * The Class AwbWebServerAccess provides access to the {@link AwbServer} and the underlying information structure.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebServerAccess {

	/**
	 * Returns the current instance of the AwbWebServerServiceWrapper.
	 * @return the AwbWebServerServiceWrapper
	 */
	public static AwbWebServerServiceWrapper getAwbServerServiceWrapper() {
		return JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME);
	}
	
	/**
	 * Return the {@link AwbServer} instance.
	 * @return the AwbServer
	 */
	public static AwbServer getAwbServer() {
		AwbWebServerServiceWrapper serverServiceWrapper = AwbWebServerAccess.getAwbServerServiceWrapper();
		if (serverServiceWrapper!=null) {
			return (AwbServer) serverServiceWrapper.getWebServerService();
		}
		return null;
	}
	
	/**
	 * Return the current JettyConfiguration that can be used to change server settings.
	 * @return the jetty configuration
	 */
	public static JettyConfiguration getJettyConfiguration() {
		AwbWebServerServiceWrapper serverServiceWrapper = AwbWebServerAccess.getAwbServerServiceWrapper();
		if (serverServiceWrapper!=null) {
			return serverServiceWrapper.getJettyConfiguration();
		}
		return null;
	}
	/**
	 * Returns the current ServletSecurityConfiguration.
	 * @return the servlet security configuration
	 */
	public static ServletSecurityConfiguration getServletSecurityConfiguration() {
		JettyConfiguration jettyConfig = AwbWebServerAccess.getJettyConfiguration();
		if (jettyConfig!=null) {
			return jettyConfig.getSecuritySettings().getActivedServletSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY);
		}
		return null;
	}
	/**
	 * Saves the current JettyConfiguration.
	 */
	public static void saveJettyConfiguration() {
		AwbWebServerServiceWrapper serverServiceWrapper = AwbWebServerAccess.getAwbServerServiceWrapper();
		if (serverServiceWrapper!=null) {
			serverServiceWrapper.save();
		}
	}
	
}
