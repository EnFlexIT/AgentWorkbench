package de.enflexit.awb.ws.credential;

public interface Credential {
	
	public CredentialType getCredentialType();
	
	public Object getCredentialValues();
}
