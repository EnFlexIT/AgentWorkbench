package de.enflexit.awb.ws.client.configuration;

/**
 * @author Timo Brandhorst
 *
 */
public class ClientConfiguration {

	private String userName;	
	
	private String password;

	private String apiKey;

	private String serverUrl;


	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}


}
