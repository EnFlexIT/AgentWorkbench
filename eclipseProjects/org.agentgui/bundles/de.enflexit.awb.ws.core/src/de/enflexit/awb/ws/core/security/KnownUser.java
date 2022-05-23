package de.enflexit.awb.ws.core.security;

import java.nio.file.attribute.UserPrincipal;

import org.eclipse.jetty.util.security.Credential;

/**
 * The Class KnownUser.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class KnownUser implements UserPrincipal {

	private final String _name;
	private final Credential _credential;
	
	/**
	 * Instantiates a new known user.
	 *
	 * @param name the name
	 * @param credential the credential
	 */
	public KnownUser(String name, Credential credential) {
		_name = name;
		_credential = credential;
	}
	public boolean authenticate(Object credentials) {
		return _credential != null && _credential.check(credentials);
	}
	public boolean isAuthenticated() {
		return true;
	}
	public String toString() {
		return _name;
	}
	@Override
	public String getName() {
		return _name;
	}

}
