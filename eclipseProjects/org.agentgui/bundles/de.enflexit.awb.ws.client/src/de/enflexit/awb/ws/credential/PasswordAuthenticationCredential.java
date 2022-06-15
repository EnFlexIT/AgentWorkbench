package de.enflexit.awb.ws.credential;

import java.net.PasswordAuthentication;

public class PasswordAuthenticationCredential implements Credential {
	
	private CredentialType credType;	
	private PasswordAuthentication passwordAuth;
	
	public PasswordAuthenticationCredential(CredentialType credType, PasswordAuthentication passwordAuth) {
		this.passwordAuth=passwordAuth;
		this.credType=credType;
	}
    

	@Override
	public CredentialType getCredentialType() {
		return credType;
	}


	@Override
	public PasswordAuthentication getCredentialValues() {
		return passwordAuth;
	}

}
