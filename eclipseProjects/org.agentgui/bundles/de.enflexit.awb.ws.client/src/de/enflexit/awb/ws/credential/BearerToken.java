package de.enflexit.awb.ws.credential;

public class BearerToken implements Credential {
	
	private static final CredentialType type= CredentialType.BEARER_TOKEN;
	
	JwtToken jwtTokeN;
	
	public BearerToken(String jwtToken) {
	   this.jwtTokeN=new JwtToken(jwtToken);
	}

	@Override
	public CredentialType getCredentialType() {
		return type;
	}

	@Override
	public Object getCredentialValues() {
		return jwtTokeN;
	}

}
