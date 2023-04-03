package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class JettySecuritySettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettySecuritySettings", propOrder = {
    "servletSecurityConfigurations"
})
public class JettySecuritySettings implements Serializable {

	private static final long serialVersionUID = -7243999870329375788L;

	public static final String ID_SERVER_SECURITY = "_ServerWideSecurityConfiguration";
	public static final String ID_NO_SECURITY_HANDLER = "NONE";
	
	private TreeMap<String, ServletSecurityConfiguration> servletSecurityConfigurations;
	
	/**
	 * Returns the security configuration tree map.
	 * @return the security configuration tree map
	 */
	private TreeMap<String, ServletSecurityConfiguration> getSecurityConfigurationTreeMap() {
		if (servletSecurityConfigurations==null) {
			servletSecurityConfigurations = new TreeMap<>();
		}
		return servletSecurityConfigurations;
	}
	/**
	 * Returns the security configuration for the specified handler.
	 *
	 * @param contextPath the context path to secure
	 * @return the security configuration or <code>null</code>
	 */
	public ServletSecurityConfiguration getSecurityConfiguration(String contextPath) {
		if (contextPath==null || contextPath.isBlank()==true) return null;
		return this.getSecurityConfigurationTreeMap().get(contextPath);
	}
	/**
	 * Sets the security configuration for the specified servlet handler.
	 *
	 * @param contextPath the context path to secure
	 * @param securityConfig the security configuration
	 * @return the previous ServletSecurityConfiguration or <code>null</code>
	 */
	public ServletSecurityConfiguration setSecurityConfiguration(String contextPath, ServletSecurityConfiguration securityConfig) {
		if (securityConfig==null || contextPath==null || contextPath.isBlank()==true) return null;
		return this.getSecurityConfigurationTreeMap().put(contextPath, securityConfig);
	}
	/**
	 * Removes the security configuration for the specified servlet handler.
	 *
	 * @param contextPath the context path to secure
	 * @return the removed ServletSecurityConfiguration
	 */
	public ServletSecurityConfiguration removeSecurityConfiguration(String contextPath) {
		return this.getSecurityConfigurationTreeMap().remove(contextPath);
	}
	
	
	/**
	 * Return the activated {@link ServletSecurityConfiguration} that is to be used for the specified context path.
	 *
	 * @param contextPath the context path to secure 
	 * @return the active servlet security configuration
	 */
	public ServletSecurityConfiguration getActivedServletSecurityConfiguration(String contextPath) {

		// --- Check to use the security handler configured for the context path -------- 
		ServletSecurityConfiguration ssc = this.getSecurityConfiguration(contextPath);
		if (ssc!=null && ssc.isSecurityHandlerActivated()==true) {
			return ssc;
		}
		// --- Check the server-wide configuration --------------------------------------
		ssc = this.getSecurityConfiguration(ID_SERVER_SECURITY);
		if (ssc!=null && ssc.isSecurityHandlerActivated()==true) {
			return ssc;
		}
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || (! (compObj instanceof JettySecuritySettings))) return false;
		if (compObj==this) return true;

		JettySecuritySettings jssComp = (JettySecuritySettings) compObj;
		if (jssComp.getSecurityConfigurationTreeMap().equals(this.getSecurityConfigurationTreeMap())==false) return false;
		
		return true;
	}

	
}
