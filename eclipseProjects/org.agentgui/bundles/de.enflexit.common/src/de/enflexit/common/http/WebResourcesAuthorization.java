
package de.enflexit.common.http;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Class WebResourcesAuthorization is used as an entity for details required by authentication.
 * 
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class WebResourcesAuthorization {

	public enum AuthorizationType {
		NONE, BASIC, BEARER
	}

	@XmlElement(name = "authorizationType")
	private AuthorizationType type;
	@XmlElement(name = "authorizationUsername")
	private String username;
	@XmlElement(name = "authorizationPassword")
	private String password;

	/**
	 * Instantiates a new project repository authorization.
	 */
	public WebResourcesAuthorization() {
		this(AuthorizationType.NONE, null, null);
	}

	/**
	 * Instantiates a new authentication details.
	 *
	 * @param type     the AuthorizationType
	 * @param username the user ame
	 * @param password the password
	 */
	public WebResourcesAuthorization(AuthorizationType type, String username, String password) {
		this.type = type;
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the encoded header for the authentication.
	 * 
	 * @return the encoded header
	 */
	public String getEncodedHeader() {
		switch (type) {
		case BASIC:
			String auth = username + ":" + password;
			auth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
			auth = "Basic " + auth;
			return auth;
		case BEARER:
			return "";
		default:
			return "";

		}
	}

	/**
	 * Returns the {@link AuthorizationType}.
	 * @return the type
	 */
	public AuthorizationType getType() {
		return type;
	}
	/**
	 * Sets the AuthorizationType.
	 * @param type the type to set
	 */
	@XmlTransient
	public void setType(AuthorizationType type) {
		this.type = type;
	}

	/**
	 * Returns the user name.
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Sets the user name.
	 * @param username the user name to set
	 */
	@XmlTransient
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	@XmlTransient
	public void setPassword(String password) {
		this.password = password;
	}

}