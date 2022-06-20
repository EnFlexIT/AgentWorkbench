package de.enflexit.awb.ws.credential;

public interface WsApiCredentialService {
	
	public CredentialType getCredentialType();
	
	public Object getCredentialValues();
	
	public String getCredentialID();
	
	public String getServerUrl();
	
	public String getServerName();
	
	public String getFunctionalityID();
}
