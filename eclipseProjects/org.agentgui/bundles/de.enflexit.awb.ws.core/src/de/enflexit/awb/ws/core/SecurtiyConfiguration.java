package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class SecurtiyConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurtiyConfiguration", propOrder = {
    "servletHandlerID",
	"securityHandlerName", 
	"securityHandlerActivated",
	"securtyHandlerConfiguration" 
})
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
