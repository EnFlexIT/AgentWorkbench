package de.enflexit.awb.ws.credential;

import org.osgi.framework.FrameworkUtil;

public class BearerToken implements WsApiCredentialService {
	
	private static final CredentialType type= CredentialType.BEARER_TOKEN;
	
	JwtToken jwtTokeN;
	String serverUrl;
	String serverName;
	String functionToAccessID;
	
	public BearerToken(String jwtToken,String serverUrl) {
	   this.jwtTokeN=new JwtToken(jwtToken);
	   this.serverUrl=serverUrl;
	}

	@Override
	public CredentialType getCredentialType() {
		return type;
	}

	@Override
	public Object getCredentialValues() {
		return jwtTokeN;
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
		return functionToAccessID;
	}

}
