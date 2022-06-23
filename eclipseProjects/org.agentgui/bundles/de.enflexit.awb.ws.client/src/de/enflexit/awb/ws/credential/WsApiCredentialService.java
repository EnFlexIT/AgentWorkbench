package de.enflexit.awb.ws.credential;

public interface WsApiCredentialService {
	public String getClientBundleName();
	
	public CredentialType getCredentialType();
	
	public Object getCredentialValues();
		
	public String getServerUrl();
	
	public String getServerName();
	
	public String getFunctionalityID();
}
