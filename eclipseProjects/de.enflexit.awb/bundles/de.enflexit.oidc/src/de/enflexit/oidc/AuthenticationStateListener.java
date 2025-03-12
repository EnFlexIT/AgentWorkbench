package de.enflexit.oidc;

/**
 * Implement this interace if you want to react to changes of the authentication state.
 */
public interface AuthenticationStateListener {
	
	/**
	 * This method is invoked whenever the authentication state has changed.
	 * @param authenticationState the new authentication state
	 */
	public void authenticationStateChanged(AuthenticationState authenticationState);
}
