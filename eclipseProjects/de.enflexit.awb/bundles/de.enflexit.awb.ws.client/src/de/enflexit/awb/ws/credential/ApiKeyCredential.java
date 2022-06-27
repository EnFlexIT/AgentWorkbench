package de.enflexit.awb.ws.credential;

/**
 * The Class ApiKeyCredential.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ApiKeyCredential extends AbstractCredential {

	private static final long serialVersionUID = -7248358188757558331L;
	
	private String apiKeyName;
	private String apiKeyValue;
	

	/**
	 * Sets the api key name.
	 * @param apiKeyName the new api key name
	 */
	public void setApiKeyName(String apiKeyName) {
		this.apiKeyName = apiKeyName;
	}
	/**
	 * Sets the api key value.
	 * @param apiKeyValue the new api key value
	 */
	public void setApiKeyValue(String apiKeyValue) {
		this.apiKeyValue = apiKeyValue;
	}

	/**
	 * Gets the api key value.
	 * @return the api key value
	 */
	public String getApiKeyValue() {
		return apiKeyValue;
	}
	/**
	 * Gets the api key name.
	 * @return the api key name
	 */
	public String getApiKeyName() {
		return apiKeyName;
	}
	
}
