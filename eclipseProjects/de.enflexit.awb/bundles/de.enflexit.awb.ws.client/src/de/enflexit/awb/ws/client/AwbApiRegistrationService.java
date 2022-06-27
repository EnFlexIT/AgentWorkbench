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
	 * Has to return the {@link CredentialType} that enables to authenticate a client user at the web service.
	 * @return the credential type
	 */
	public CredentialType getCredentialType();
	
	/**
	 * has to return the credential default name.
	 * @return the credential default name
	 */
	public String getCredentialDefaultName();
	
}
