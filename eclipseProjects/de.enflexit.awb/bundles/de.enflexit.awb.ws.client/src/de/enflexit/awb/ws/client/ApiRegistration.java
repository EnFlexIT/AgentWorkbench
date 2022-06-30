package de.enflexit.awb.ws.client;

import java.io.Serializable;

/**
 * The Class ApiRegistration is used as storable container to locally save API registrations.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ApiRegistration implements Serializable {

	private static final long serialVersionUID = -4136490503086202993L;

	private String clientBundleName;
	private String description;
	private String serverURL;
	private CredentialType credentialType;
	private String defaultCredentialName;
	
	
	/**
	 * Empty default constructor for a new ApiRegistration.
	 */
	public ApiRegistration() { }
	
	/**
	 * Instantiates a new  ApiRegistration.
	 *
	 * @param clientBundleName the client bundle name
	 * @param description the description
	 * @param credentialType the credential type
	 * @param defaultServerApiToCredentialID the credential default name
	 */
	public ApiRegistration(String clientBundleName, String description, CredentialType credentialType, String serverURL) { 
		this.setClientBundleName(clientBundleName);
		this.setDescription(description);
		this.setServerURL(serverURL);
		this.setCredentialType(credentialType);
	}
	/**
	 * Instantiates a new  ApiRegistration.
	 * @param apiRegistrationService the api registration service
	 */
	public ApiRegistration(AwbApiRegistrationService apiRegistrationService) {
		this.setClientBundleName(apiRegistrationService.getClientBundleName());
		this.setDescription(apiRegistrationService.getDescription());
		this.setCredentialType(apiRegistrationService.getCredentialType());
		this.setServerURL(apiRegistrationService.getDefaultURL());
	}
	
	/**
	 * Gets the client bundle name.
	 * @return the client bundle name
	 */
	public String getClientBundleName() {
		return clientBundleName;
	}
	/**
	 * Sets the client bundle name.
	 * @param clientBundleName the new client bundle name
	 */
	public void setClientBundleName(String clientBundleName) {
		this.clientBundleName = clientBundleName;
	}
	
	
	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description.
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * Returns the server URL.
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
	
	
	/**
	 * Gets the credential type.
	 * @return the credential type
	 */
	public CredentialType getCredentialType() {
		return credentialType;
	}
	/**
	 * Sets the credential type.
	 * @param credentialType the new credential type
	 */
	public void setCredentialType(CredentialType credentialType) {
		this.credentialType = credentialType;
	}
	
	
	/**
	 * Returns the default credential name.
	 * @return the default credential name
	 */
	public String getDefaultCredentialName() {
		return defaultCredentialName;
	}
	/**
	 * Sets the default credential name.
	 * @param defaultCredentialName the new default credential name
	 */
	public void setDefaultCredentialName(String defaultCredentialName) {
		this.defaultCredentialName = defaultCredentialName;
	}
	
}
