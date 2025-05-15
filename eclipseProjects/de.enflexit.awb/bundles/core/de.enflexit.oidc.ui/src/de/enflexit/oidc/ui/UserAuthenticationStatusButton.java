package de.enflexit.oidc.ui;

import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.oidc.AuthenticationStateListener;
import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCAuthorization.AuthenticationState;

/**
 * A custom toolbar button that shows the current authentication state of the user, and allows logging in/out.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class UserAuthenticationStatusButton extends JButton implements AuthenticationStateListener {
	
	private static final String ICON_PATH_LOGGED_IN ="/icons/UserLoggedIn.png";
	private static final String ICON_PATH_LOGGED_OUT_LIGHT = "/icons/UserLoggedOutLight.png";
	private static final String ICON_PATH_LOGGED_OUT_DARK = "/icons/UserLoggedOutDark.png";
	private static final String ICON_PATH_LOGIN_PENDING = "/icons/LoginPending.png";
	
	private static final String BUTTON_TEXT_LOGGED_IN = "Logged in";
	private static final String BUTTON_TEXT_LOGGED_OUT = "Log in";
	private static final String BUTTON_TEXT_LOGIN_PENDING = "Cancel";
	
	private static final String TOOLTIP_TEXT_LOGGED_IN = "Logged in as ";
	private static final String TOOLTIP_TEXT_LOGGED_OUT = "Not logged in";
	private static final String TOOLTIP_TEXT_LOGIN_PENDING = "The log in page was opened in your browser!";

	private static final long serialVersionUID = 8112353415174825641L;
	
	private ImageIcon imageIconLoggedIn;
	private ImageIcon imageIconLoginPending;
	
	private AuthenticationState authenticationState;
	
	/**
	 * Instantiates a new user authentication status button.
	 */
	public UserAuthenticationStatusButton() {
		super();
		this.setAuthenticationState(OIDCAuthorization.getInstance().getAuthenticationState());
	}

	private ImageIcon getImageIconLoggedIn() {
		if (imageIconLoggedIn==null) {
			imageIconLoggedIn = new ImageIcon(this.getClass().getResource(ICON_PATH_LOGGED_IN));
		}
		return imageIconLoggedIn;
	}

	private ImageIcon getImageIconLoggedOut() {
		if (this.isInDarkMode()) {
			return new ImageIcon(this.getClass().getResource(ICON_PATH_LOGGED_OUT_DARK));
		} else {
			return new ImageIcon(this.getClass().getResource(ICON_PATH_LOGGED_OUT_LIGHT));
		}
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
		
		this.authenticationState = authenticationState;
		
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
			this.setText(BUTTON_TEXT_LOGGED_IN);
			this.setToolTipText(TOOLTIP_TEXT_LOGGED_IN + username);
			break;
		case LOGGED_OUT:
			this.setIcon(this.getImageIconLoggedOut());
			this.setText(BUTTON_TEXT_LOGGED_OUT);
			this.setToolTipText(TOOLTIP_TEXT_LOGGED_OUT);
			break;
		case PENDING:
			this.setIcon(this.getImageIconLoginPending());
			this.setText(BUTTON_TEXT_LOGIN_PENDING);
			this.setToolTipText(TOOLTIP_TEXT_LOGIN_PENDING);
		}
		
	}
	
	/**
	 * Checks if the application is currently in dark mode.
	 * @return true, if is in dark mode
	 */
	private boolean isInDarkMode() {
		return AwbLookAndFeelAdjustments.isDarkLookAndFeel();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JButton#updateUI()
	 */
	@Override
	public void updateUI() {
		super.updateUI();
		if (this.authenticationState==AuthenticationState.LOGGED_OUT) {
			// --- If in logged out state, update the icon (other state icons are the same for light and dark mode)
			this.setIcon(this.getImageIconLoggedOut());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.oidc.AuthenticationStateListener#authenticationStateChanged(de.enflexit.oidc.OIDCAuthorization.AuthenticationState)
	 */
	@Override
	public void authenticationStateChanged(AuthenticationState authenticationState) {
		this.setAuthenticationState(authenticationState);
	}
	
}
