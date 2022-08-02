package de.enflexit.awb.ws.client;

import java.io.Serializable;

/**
 * The Class ApiRegistration is used as storable container to locally save API registrations.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ApiRegistration implements Serializable {

	private static final long serialVersionUID = -4136490503086202993L;
	
	private AwbApiRegistrationService apiRegistrationService;
	
	
	/**
	 * Instantiates a new  ApiRegistration.
	 * @param apiRegistrationService the api registration service
	 */
	public ApiRegistration(AwbApiRegistrationService apiRegistrationService) {
		this.apiRegistrationService = apiRegistrationService;
	}
	
	/**
	 * Gets the client bundle name.
	 * @return the client bundle name
	 */
	public String getClientBundleName() {
		return this.apiRegistrationService.getClientBundleName();
	}
	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return this.apiRegistrationService.getDescription();
	}
	/**
	 * Returns the server URL.
	 * @return the server URL
	 */
	public String getDefaultURL() {
		return this.apiRegistrationService.getDefaultURL();
	}
	/**
	 * Gets the credential type.
	 * @return the credential type
	 */
	public CredentialType getCredentialType() {
		return this.apiRegistrationService.getCredentialType();
	}
	/**
	 * Returns the default credential name.
	 * @return the default credential name
	 */
	public String getDefaultCredentialName() {
		return this.apiRegistrationService.getDefaultCredentialName();
	}
	
	@Override
	public String toString() {
		return "[" + this.getCredentialType().name() + "] " + this.getClientBundleName();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = super.equals(obj);
		if (obj instanceof ApiRegistration) {
			
			ApiRegistration apiRegistration = (ApiRegistration) obj;

			if (apiRegistration.getClientBundleName().equals(this.getClientBundleName())) {
				equals = false;
			}

			if (!apiRegistration.getDescription().equals(this.getDescription())) {
				equals = false;
			}

			if (!apiRegistration.getDefaultURL().equals(this.getDefaultURL())) {
				equals = false;
			}

			if (!apiRegistration.getCredentialType().equals(this.getCredentialType())) {
				equals = false;
			}

			if (!apiRegistration.getDefaultCredentialName().equals(this.getDefaultCredentialName())) {
				equals = false;
			}
			
		} else {
			equals = false;
		}

		return equals;
	}
	
}
