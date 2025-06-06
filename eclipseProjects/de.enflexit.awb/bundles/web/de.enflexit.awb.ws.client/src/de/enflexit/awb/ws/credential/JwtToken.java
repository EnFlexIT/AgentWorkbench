package de.enflexit.awb.ws.credential;

import java.io.Serializable;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class JwtToken.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JwtToken", propOrder = {
    "jwtToken",
    "header",
    "body",
    "cryptographicSignature",
})
public class JwtToken implements Serializable {

	private static final long serialVersionUID = 5587910146851984549L;
	
	private String jwtToken;
	private Map<String, String> header;
	private Map<String, String> body;
	private String cryptographicSignature;

	public JwtToken(String jwtToken, Map<String, String> header, Map<String, String> body,String cryptographicSignature) {
		this.jwtToken = jwtToken;
		this.header = header;
		this.body = body;
		this.cryptographicSignature = cryptographicSignature;
	}

	public JwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
		JwtTokenParser parser = new JwtTokenParser();
		JwtToken token = parser.parseJwtToken(jwtToken);
		this.header = token.getHeader();
		this.body = token.getBody();
		this.cryptographicSignature = token.getCryptographicSignature();
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
