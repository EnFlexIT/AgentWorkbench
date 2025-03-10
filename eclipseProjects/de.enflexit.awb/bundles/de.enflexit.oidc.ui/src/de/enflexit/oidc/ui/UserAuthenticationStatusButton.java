package de.enflexit.oidc.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A custom toolbar button that shows the current authentication state of the user, and allows logging in/out.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class UserAuthenticationStatusButton extends JButton {
	
	private static final String ICON_PATH_LOGGED_IN ="/icons/UserLoggedIn_16x16.png";
	private static final String ICON_PATH_LOGGED_OUT = "/icons/UserLoggedOut_16x16.png";
	
	private static final String BUTTON_TEXT_LOGGED_OUT = "Not logged in";
	private static final String BUTTON_TEXT_LOGGED_IN = "Logged in as ";

	private static final long serialVersionUID = 8112353415174825641L;
	
	private ImageIcon imageIconLoggedIn;
	private ImageIcon imageIconLoggedOut;
	
	private boolean userLoggedIn;
	
	/**
	 * Instantiates a new user authentication status button.
	 */
	public UserAuthenticationStatusButton() {
		super();
		this.setButtonState(this.userLoggedIn);
		
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
	
	/**
	 * Sets the the icon and text on the button, according to the current authentication state.
	 * @param authorizationState the new button state
	 */
	private void setButtonState(boolean authorizationState) {
		if (authorizationState==true) {
			String username = "Some user";	//TODO get user name from authentication
			this.setIcon(this.getImageIconLoggedIn());
			this.setText(BUTTON_TEXT_LOGGED_IN + username);
		} else {
			this.setIcon(this.getImageIconLoggedOut());
			this.setText(BUTTON_TEXT_LOGGED_OUT);
		}
	}
	

}
