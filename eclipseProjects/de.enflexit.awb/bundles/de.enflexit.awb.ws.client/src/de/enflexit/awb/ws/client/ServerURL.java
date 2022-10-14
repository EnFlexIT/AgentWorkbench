package de.enflexit.awb.ws.client;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class ServerURL saves the URL of a Server.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServerURL", propOrder = {
    "id",
    "serverURL"
})
public class ServerURL implements Serializable {

	private static final long serialVersionUID = 8200942503674357869L;

	private UUID id;
	private String serverURL;
	
	/**
	 * Instantiates a new server URL.
	 */
	public ServerURL() {
		getID();
	}
	
	/**
	 * Instantiates a new server URL.
	 * @param serverURL the server URL
	 */
	public ServerURL(String serverURL) {
		getID();
		this.setServerURL(serverURL);
	}
		
	/**
	 * Returns the id of a credential.
	 * @return the credential id
	 */
	public UUID getID() {
		if (id==null) {
			id = UUID.randomUUID();
		}
		return id;
	}
	/**
	 * Gets the server URL.
	 * @return the server URL
	 */
	public String getServerURL() {
		return serverURL;
	}
	/**
	 * Sets the server URL.
	 * @param serverURL the new server URL
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals = super.equals(obj);
		if (obj instanceof ServerURL) {
			ServerURL url = (ServerURL) obj;
			
			if (!this.getID().equals(url.getID())) {
				equals = false;
			}
			
			if (!this.getServerURL().equals(url.getServerURL())) {
				equals = false;
			}
		} else {
			equals = false;
		}
		return equals;
	}
	
	@Override
	public String toString() {
		return getServerURL();
	}
}
