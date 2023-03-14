package de.enflexit.awb.samples.ws.restapi.client;

import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.ws.client.AwbApiRegistrationService;
import de.enflexit.awb.ws.client.CredentialType;

/**
 * The Class ApiRegistrationService.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ApiRegistrationService implements AwbApiRegistrationService {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.client.AwbApiRegistrationService#getClientBundleName()
	 */
	@Override
	public String getClientBundleName() {
		return FrameworkUtil.getBundle(this.getClass()).getSymbolicName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.client.AwbApiRegistrationService#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Rest Api-Client for the Rest-Api of Agent.Workbench in bundle de.enflexit.awb.ws.restapi";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.client.AwbApiRegistrationService#getDefaultURL()
	 */
	@Override
	public String getDefaultServerURL() {
		return "https://127.0.0.1:8080/api";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.client.AwbApiRegistrationService#getCredentialType()
	 */
	@Override
	public CredentialType getCredentialType() {
		return CredentialType.API_KEY;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.client.AwbApiRegistrationService#getDefaultCredentialName()
	 */
	@Override
	public String getDefaultCredentialName() {
		return this.getClientBundleName() + " Api-Key - Authentification";
	}
}
