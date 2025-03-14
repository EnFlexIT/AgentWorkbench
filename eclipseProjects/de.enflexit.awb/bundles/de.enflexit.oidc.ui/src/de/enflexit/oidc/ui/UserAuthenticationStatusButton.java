package de.enflexit.oidc.ui;

import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCAuthorization.AuthenticationState;

/**
 * A custom toolbar button that shows the current authentication state of the user, and allows logging in/out.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class UserAuthenticationStatusButton extends JButton {
	
	private static final String ICON_PATH_LOGGED_IN ="/icons/UserLoggedIn.png";
	private static final String ICON_PATH_LOGGED_OUT = "/icons/UserLoggedOut.png";
	private static final String ICON_PATH_LOGIN_PENDING = "/icons/LoginPending.png";
	
	private static final String BUTTON_TEXT_LOGGED_OUT = "Not logged in";
	private static final String BUTTON_TEXT_LOGIN_PENDING = "Authentication pending";
	private static final String BUTTON_TEXT_LOGGED_IN = "Logged in as ";

	private static final long serialVersionUID = 8112353415174825641L;
	
	private ImageIcon imageIconLoggedIn;
	private ImageIcon imageIconLoggedOut;
	private ImageIcon imageIconLoginPending;
	
	private boolean userLoggedIn;
	
	/**
	 * Instantiates a new user authentication status button.
	 */
	public UserAuthenticationStatusButton() {
		super();
		this.setAuthenticationState(this.userLoggedIn ? AuthenticationState.LOGGED_IN : AuthenticationState.LOGGED_OUT);
	}

	private ImageIcon getImageIconLoggedIn() {
		if (imageIconLoggedIn==null) {
			imageIconLoggedIn = new ImageIcon(this.getClass().getResource(ICON_PATH_LOGGED_IN));
		}
		return imageIconLoggedIn;
	}

	private ImageIcon getImageIconLoggedOut() {
		if (imageIconLoggedOut==null) {
			imageIconLoggedOut = new ImageIcon(this.getClass().getResource(ICON_PATH_LOGGED_OUT));
		}
		return imageIconLoggedOut;
	}
	
	private ImageIcon getImageIconLoginPending() {
		if (imageIconLoginPending==null) {
			imageIconLoginPending = new ImageIcon(this.getClass().getResource(ICON_PATH_LOGIN_PENDING));
		}
		return imageIconLoginPending;
	}
	
	/**
	 * Sets the the icon and text on the button, according to the current authentication state.
	 * @param authorizationState the new button state
	 */
	public void setAuthenticationState(AuthenticationState authenticationState) {
		
		switch(authenticationState) {
		case LOGGED_IN:
			String username;
			try {
				username = OIDCAuthorization.getInstance().getUserFullName();
			} catch (URISyntaxException e) {
				username = "unknown";
				System.err.println("[" + this.getClass().getSimpleName() + "] Error obtaining the user's name from the ID token!");
				e.printStackTrace();
			}
			this.setIcon(this.getImageIconLoggedIn());
			this.setText(BUTTON_TEXT_LOGGED_IN + username);
			this.setEnabled(true);
			this.setToolTipText("Click to log out");
			break;
		case LOGGED_OUT:
			this.setIcon(this.getImageIconLoggedOut());
			this.setText(BUTTON_TEXT_LOGGED_OUT);
			this.setEnabled(true);
			this.setToolTipText("Click to log in");
			break;
		case PENDING:
			this.setIcon(this.getImageIconLoginPending());
			this.setText(BUTTON_TEXT_LOGIN_PENDING);
			this.setEnabled(false);
			this.setToolTipText("The log in page was opened in your browser!");
		}
		
	}
	

}
