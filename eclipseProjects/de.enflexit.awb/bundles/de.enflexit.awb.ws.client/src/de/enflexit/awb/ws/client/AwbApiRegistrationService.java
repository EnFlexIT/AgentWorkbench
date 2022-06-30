package de.enflexit.awb.ws.client;

/**
 * The Interface AwbApiRegistrationService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbApiRegistrationService {
	
	/**
	 * Has to return the client bundle name that provides the web service API access.
	 * @return the client bundle name
	 */
	public String getClientBundleName();
	
	/**
	 * Has to return a description for the current API. What is possible to get from this service?
	 * What are required tasks.
	 * 
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Returns the default URL for the access.
	 * @return the default URL
	 */
	public String getDefaultURL();
	
	/**
	 * Has to return the {@link CredentialType} that enables to authenticate a client user at the web service.
	 * @return the credential type
	 */
	public CredentialType getCredentialType();
	
	/**
	 * Has to return the default credential name.
	 * @return the default credential name
	 */
	public String getDefaultCredentialName();
	
}
