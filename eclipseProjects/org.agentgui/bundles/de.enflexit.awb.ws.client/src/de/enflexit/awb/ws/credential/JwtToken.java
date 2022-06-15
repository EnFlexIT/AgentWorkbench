package de.enflexit.awb.ws.credential;

import java.util.Map;

public class JwtToken {

	String jwtToken;
	Map<String,String> header;
	Map<String,String> body;
	String cryptographicSignature;

	public JwtToken(String jwtToken, Map<String,String> header, Map<String,String> body, String cryptographicSignature) {
		this.jwtToken = jwtToken;
		this.header = header;
		this.body = body;
		this.cryptographicSignature = cryptographicSignature;
	}
	
	public JwtToken(String jwtToken) {
		this.jwtToken=jwtToken;
	  JwtTokenParser parser= new JwtTokenParser();
	  JwtToken token=parser.parseJwtToken(jwtToken);
	   this.header=token.getHeader();
	   this.body=token.getBody();
	   this.cryptographicSignature=token.getCryptographicSignature();
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}

	public String getCryptographicSignature() {
		return cryptographicSignature;
	}

	public void setCryptographicSignature(String cryptographicSignature) {
		this.cryptographicSignature = cryptographicSignature;
	}
}
