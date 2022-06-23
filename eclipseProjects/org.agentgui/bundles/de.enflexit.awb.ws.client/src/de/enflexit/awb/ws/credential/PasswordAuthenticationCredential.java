package de.enflexit.awb.ws.credential;

import java.net.PasswordAuthentication;

import org.osgi.framework.FrameworkUtil;

public class PasswordAuthenticationCredential implements WsApiCredentialService {
	
	private final CredentialType credType=CredentialType.USERNAME_PLUS_PASSWORD;	
	private PasswordAuthentication passwordAuth;
	private String serverUrl;
	private String serverName;
	private String functionID;
	
	public PasswordAuthenticationCredential(PasswordAuthentication passwordAuth,String serverUrl,String serverName,String functionID) {
		this.passwordAuth=passwordAuth;
		this.serverUrl=serverUrl;
		this.serverName=serverName;
		this.functionID=functionID;
	}
    
	@Override
	public CredentialType getCredentialType() {
		return credType;
	}


	@Override
	public PasswordAuthentication getCredentialValues() {
		return passwordAuth;
	}


	@Override
	public String getClientBundleName() {
		return FrameworkUtil.getBundle(this.getClass()).getSymbolicName();
	}

	@Override
	public String getServerUrl() {
		return serverUrl;
	}


	@Override
	public String getServerName() {
		return serverName;
	}


	@Override
	public String getFunctionalityID() {
		return functionID;
	}

}
