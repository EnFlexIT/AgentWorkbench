package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * The Class JettySecuritySettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
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
	
	
	/**
	 * The Class SecurtiyConfiguration.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class SecurtiyConfiguration implements Serializable {
		
		private static final long serialVersionUID = -8676645162096989674L;
		
		private String servletHandlerID;
		private String securityHandlerName; 
		private boolean securityHandlerActivated;
		private TreeMap<String, String> securtyHandlerConfiguration; 
		
		
		/**
		 * Returns the servlet handler ID.
		 * @return the servlet handler ID
		 */
		public String getServletHandlerID() {
			return servletHandlerID;
		}
		/**
		 * Sets the servlet handler ID.
		 * @param servletHandlerID the new servlet handler ID
		 */
		public void setServletHandlerID(String servletHandlerID) {
			this.servletHandlerID = servletHandlerID;
		}
		
		/**
		 * Returns the security handler name.
		 * @return the security handler name
		 */
		public String getSecurityHandlerName() {
			return securityHandlerName;
		}
		/**
		 * Sets the security handler name.
		 * @param securityHandlerName the new security handler name
		 */
		public void setSecurityHandlerName(String securityHandlerName) {
			this.securityHandlerName = securityHandlerName;
		}
		
		/**
		 * Checks if the security handler should be activated.
		 * @return true, if is security handler activated
		 */
		public boolean isSecurityHandlerActivated() {
			return securityHandlerActivated;
		}
		/**
		 * Sets the security handler activated or deactivated.
		 * @param securityHandlerActivated the new security handler activated
		 */
		public void setSecurityHandlerActivated(boolean securityHandlerActivated) {
			this.securityHandlerActivated = securityHandlerActivated;
		}
		
		/**
		 * Returns the security handler configuration.
		 * @return the security handler configuration
		 */
		public TreeMap<String, String> getSecurityHandlerConfiguration() {
			if (securtyHandlerConfiguration==null) {
				securtyHandlerConfiguration = new TreeMap<>();
			}
			return securtyHandlerConfiguration;
		}
		/**
		 * Sets the security handler configuration.
		 * @param securtyHandlerConfiguration the security handler configuration
		 */
		public void setSecurityHandlerConfiguration(TreeMap<String, String> securityHandlerConfiguration) {
			this.securtyHandlerConfiguration = securityHandlerConfiguration;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object compObj) {
			
			if (compObj==null || (! (compObj instanceof SecurtiyConfiguration))) return false;
			if (compObj==this) return true;

			SecurtiyConfiguration scComp = (SecurtiyConfiguration) compObj;
			
			if (scComp.getServletHandlerID().equals(this.getServletHandlerID())==false) return false;
			if (scComp.getSecurityHandlerName().equals(this.getSecurityHandlerName())==false) return false;
			if (scComp.isSecurityHandlerActivated()!=this.isSecurityHandlerActivated()) return false;
			if (scComp.getSecurityHandlerConfiguration().equals(this.getSecurityHandlerConfiguration())==false) return false;
			
			return true;
		}
	}
	
}
