package de.enflexit.awb.ws.credential;

public enum CredentialType {
	
	
	API_KEY("X-API-KEY"),
	USERNAME_PLUS_PASSWORD("USERNAME_PLUS_PASSWORD"),
	BEARER_TOKEN("BEARER_TOKEN"),
	HTTP_AUTHENTICATION("HTTP_AUTHENTICATION"),
	OAUTH_2("OAUTH_2"),
	OPENID_CONNECT_DISCOVERY("OPENID_CONNECT_DISCOVERY");
	
    private final String credType;

	private CredentialType(String credType) {
      this.credType=credType;
	}
	
	public String getCredTypeAsString() {
		return credType;
	}

}
