package de.enflexit.awb.ws.client;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class ServerURL implements Serializable {

	private static final long serialVersionUID = 8200942503674357869L;

	private Integer id;
	private String serverURL;
	
	/**
	 * Instantiates a new server URL.
	 */
	public ServerURL() {}
	/**
	 * Instantiates a new server URL.
	 * @param serverURL the server URL
	 */
	public ServerURL(String serverURL) {
		this.setServerURL(serverURL);
	}
	
	
	/**
	 * Returns the id of a credential.
	 * @return the credential id
	 */
	public Integer getID() {
		if (id==null) {
			// --- Randomize an ID ------------------
			int min = 1000000;
			int max = Integer.MAX_VALUE;
			id = ThreadLocalRandom.current().nextInt(min, max);
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
	
}
