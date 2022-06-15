package de.enflexit.awb.ws.client;

import java.util.List;

import de.enflexit.awb.ws.credential.Credential;

/**
 * The interface WsCredentialService describes the information to be provided 
 * by an implementation to configure an individual Client instance. 
 *
 * @author Timo Brandhorst - SOFTEC 
 */
public interface WsApiCredentialService  {
 
	/**
	 * 
	 * @return Bundlename of client
	 */
	public String getCredentialID();
	
	/**
	 * 
	 * @return all required Credentials
	 */
	public List<Credential> getCredentials();
	
	/**
	 * 
	 * @return returns the AuthenticationMethod
	 */
	public String getAuthenticationMethod();
	
}
