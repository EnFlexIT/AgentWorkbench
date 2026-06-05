package de.enflexit.awb.ws.core.security.jwt;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.security.SingleUserSecurityHandler;
import de.enflexit.awb.ws.core.session.UserSessionFilter;
import de.enflexit.common.NumberHelper;
import jakarta.servlet.DispatcherType;

/**
 * The Class JwtSingleUserSecurityService describes the {@link SingleUserSecurityHandler}
 * as a service for the local AWB-WS bundle.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSingleUserSecurityService implements AwbSecurityHandlerService {

	
	public enum JwtParameter {
		UserName("User Name", String.class),
		Password("Password", String.class),
		JwtSecrete("JWT Secrete", String.class),
		JwtIssuer("JWT Issuer", String.class),
		JwtValidityPeriod("JWT Token - Validity Period [Min]", Integer.class),
		JwtRenew("JWT Token - Renew with every call", Boolean.class),
		JwtVerbose("JWT Verbose", Boolean.class);
		
		private String displayText;
		private Class<?> keyType;
		
		private JwtParameter(String displayText, Class<?> keyType) {
			this.displayText = displayText;
			this.keyType = keyType;
		}
		public String getKey() {
			return displayText;
		}
		public Class<?> getKeyType() {
			return keyType;
		}
		
		public static JwtParameter fromKey(String key) {
			for (JwtParameter jwtPara : JwtParameter.values()) {
				if (jwtPara.getKey().equals(key)==true) {
					return jwtPara;
				}
			}
			return null;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getSecurityHandlerName()
	 */
	@Override
	public String getSecurityHandlerName() {
		return JwtSingleUserSecurityHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getConfigurationKeys()
	 */
	@Override
	public String[] getConfigurationKeys() {
		
		List<String> configList = new ArrayList<>();
		for (JwtParameter jwtParameter : JwtParameter.values()) {
			configList.add(jwtParameter.getKey());
		}
		return configList.toArray(new String[configList.size()]);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getKeyType(java.lang.String)
	 */
	@Override
	public Class<?> getKeyType(String key) {
		
		JwtParameter jwtParameter = JwtParameter.fromKey(key);
		if (jwtParameter!=null) {
			return jwtParameter.getKeyType();
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getNewSecurityHandler(java.util.TreeMap)
	 */
	@Override
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {

		// --- Set JWT configuration to system environment ----------
		Map<String, String> jwtConfig = new HashMap<>();
		jwtConfig.put(JwtAuthenticator.JWT_SECRET, securityHandlerConfiguration.get(JwtParameter.JwtSecrete.getKey()));
		jwtConfig.put(JwtAuthenticator.JWT_ISSUER, securityHandlerConfiguration.get(JwtParameter.JwtIssuer.getKey()));
		
		jwtConfig.put(JwtAuthenticator.JWT_VALIDITY_PERIOD, securityHandlerConfiguration.get(JwtParameter.JwtValidityPeriod.getKey()));
		jwtConfig.put(JwtAuthenticator.JWT_RENEW_EACH_CALL, securityHandlerConfiguration.get(JwtParameter.JwtRenew.getKey()));
		
		jwtConfig.put(JwtAuthenticator.JWT_VERBOSE, securityHandlerConfiguration.get(JwtParameter.JwtVerbose.getKey()));
		
		// --- Return the new instance of the SecurtiyHandler -------
		return new JwtSingleUserSecurityHandler(securityHandlerConfiguration, jwtConfig);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#customizeServletContextHandler(de.enflexit.awb.ws.core.JettyConfiguration, org.eclipse.jetty.ee10.servlet.ServletContextHandler)
	 */
	@Override
	public void customizeServletContextHandler(JettyConfiguration jConfiguration, ServletContextHandler serCtxHandle) {
		
		// --- Get the required parameter ---------------------------
		ServletSecurityConfiguration ssc = jConfiguration.getSecuritySettings().getSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY);
		Integer validityPeriod = NumberHelper.parseInteger(ssc.getSecurityHandlerConfiguration().get(JwtParameter.JwtValidityPeriod.getKey())) * 60;
		
		FilterHolder refreshFilter = new FilterHolder(UserSessionFilter.class);
		refreshFilter.setInitParameter(UserSessionFilter.SECURITY_HANDLER_SERVICE, this.getClass().getName());
		refreshFilter.setInitParameter(UserSessionFilter.USER_SESSION_LENGTH_IN_SECONDS, validityPeriod.toString()); 

		// --- Apply to secured paths -------------------------------
		serCtxHandle.addFilter(refreshFilter, "/*", EnumSet.of(DispatcherType.REQUEST));
		
	}
	
}
