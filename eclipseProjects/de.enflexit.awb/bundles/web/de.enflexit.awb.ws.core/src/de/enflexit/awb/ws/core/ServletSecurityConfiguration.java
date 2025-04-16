package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class ServletSecurityConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServletSecurityConfiguration", propOrder = {
    "contextPath",
	"securityHandlerName", 
	"securityHandlerActivated",
	"securityHandlerConfiguration" 
})
public class ServletSecurityConfiguration implements Serializable {
	
	private static final long serialVersionUID = -8676645162096989674L;
	
	private String contextPath;
	private String securityHandlerName; 
	private boolean securityHandlerActivated;
	private TreeMap<String, String> securityHandlerConfiguration; 
	
	
	/**
	 * Returns the context path that is secured with the current settings.
	 * @return the context path
	 */
	public String getContextPath() {
		return contextPath;
	}
	/**
	 * Sets the context path that is secured with the current settings.
	 * @param contextPath the contextPath to secure
	 */
	public void setContextPath(String servletHandlerID) {
		this.contextPath = servletHandlerID;
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
		if (securityHandlerConfiguration==null) {
			securityHandlerConfiguration = new TreeMap<>();
		}
		return securityHandlerConfiguration;
	}
	/**
	 * Sets the security handler configuration.
	 * @param securityHandlerConfiguration the security handler configuration
	 */
	public void setSecurityHandlerConfiguration(TreeMap<String, String> securityHandlerConfiguration) {
		this.securityHandlerConfiguration = securityHandlerConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || (! (compObj instanceof ServletSecurityConfiguration))) return false;
		if (compObj==this) return true;

		ServletSecurityConfiguration scComp = (ServletSecurityConfiguration) compObj;
		
		if (scComp.getContextPath().equals(this.getContextPath())==false) return false;
		if (scComp.getSecurityHandlerName().equals(this.getSecurityHandlerName())==false) return false;
		if (scComp.isSecurityHandlerActivated()!=this.isSecurityHandlerActivated()) return false;
		if (scComp.getSecurityHandlerConfiguration().equals(this.getSecurityHandlerConfiguration())==false) return false;
		
		return true;
	}
}
