package de.enflexit.awb.ws.restapi;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettySessionSettings;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.security.OIDCSecurityHandler;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityHandler;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.common.NumberHelper;

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
	 * Returns the JettySessionSettings.
	 * @return the jetty session settings
	 */
	public static JettySessionSettings getJettySessionSettings() {
		JettyConfiguration jettyConfig = AwbWebServerAccess.getJettyConfiguration();
		if (jettyConfig!=null) {
			return jettyConfig.getSessionSettings();
		}
		return null;
	}
	/**
	 * If defined and used, returns the user session length in seconds (For JWT and OIDC only).
	 * @return the user session length in seconds
	 */
	public static Integer getUserSessionLengthInSeconds() {

		Integer userSessionLengthInSeconds = null;
		
		ServletSecurityConfiguration ssc = AwbWebServerAccess.getServletSecurityConfiguration();
		if (ssc==null) return null;
		
		String shName = ssc.getSecurityHandlerName();
		if (shName.equals(JwtSingleUserSecurityHandler.class.getSimpleName())==true) {
			userSessionLengthInSeconds = NumberHelper.parseInteger(ssc.getSecurityHandlerConfiguration().get(JwtParameter.JwtValidityPeriod.getKey()));
			if (userSessionLengthInSeconds!=null) userSessionLengthInSeconds = userSessionLengthInSeconds * 60;
			
		} else if (shName.equals(OIDCSecurityHandler.class.getSimpleName())==true) {
			JettySessionSettings sessSettings = AwbWebServerAccess.getJettySessionSettings();
			if (sessSettings!=null) {
				userSessionLengthInSeconds = (int) sessSettings.getSessionAttribute(JettySessionSettings.KEY_SET_MAX_INACTIVE_INTERVAL).getValue();
			}
			
		}
		return userSessionLengthInSeconds;
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
