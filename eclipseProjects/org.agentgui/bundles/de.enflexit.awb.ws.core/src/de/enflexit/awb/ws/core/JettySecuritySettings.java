package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class JettySecuritySettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettySecuritySettings", propOrder = {
    "securityConfigurationTreeMap"
})
public class JettySecuritySettings implements Serializable {

	private static final long serialVersionUID = -7243999870329375788L;

	public static final String ID_SERVER_SECURITY = "_Server_Security";
	
	private TreeMap<String, SecurtiyConfiguration> securityConfigurationTreeMap;
	
	/**
	 * Returns the security configuration tree map.
	 * @return the security configuration tree map
	 */
	private TreeMap<String, SecurtiyConfiguration> getSecurityConfigurationTreeMap() {
		if (securityConfigurationTreeMap==null) {
			securityConfigurationTreeMap = new TreeMap<>();
		}
		return securityConfigurationTreeMap;
	}
	/**
	 * Returns the security configuration for the specified handler.
	 *
	 * @param servletHandlerID the servlet handler ID
	 * @return the security configuration or <code>null</code>
	 */
	public SecurtiyConfiguration getSecurityConfiguration(String servletHandlerID) {
		if (servletHandlerID==null || servletHandlerID.isBlank()==true) return null;
		return this.getSecurityConfigurationTreeMap().get(servletHandlerID);
	}
	/**
	 * Sets the security configuration for the specified servlet handler.
	 *
	 * @param servletHandlerID the servlet handler ID
	 * @param securityConfig the security configuration
	 * @return the previous SecurityConfiguration or <code>null</code>
	 */
	public SecurtiyConfiguration setSecurityConfiguration(String servletHandlerID, SecurtiyConfiguration securityConfig) {
		if (securityConfig==null || servletHandlerID==null || servletHandlerID.isBlank()==true) return null;
		return this.getSecurityConfigurationTreeMap().put(servletHandlerID, securityConfig);
	}
	/**
	 * Removes the security configuration for the specified servlet handler.
	 *
	 * @param servletHandlerID the servlet handler ID
	 * @return the removed SecurityConfiguration
	 */
	public SecurtiyConfiguration removeSecurityConfiguration(String servletHandlerID) {
		return this.getSecurityConfigurationTreeMap().remove(servletHandlerID);
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
