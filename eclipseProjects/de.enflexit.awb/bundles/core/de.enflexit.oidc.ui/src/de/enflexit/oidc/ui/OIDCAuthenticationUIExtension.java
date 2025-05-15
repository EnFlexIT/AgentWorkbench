package de.enflexit.oidc.ui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCSettings;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;

/**
 * Adds a toolbar button and a menu for handling user authentication against keycloak.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCAuthenticationUIExtension extends MainWindowExtension implements ActionListener {
	
	private static final String ACTION_COMMAND_OIDC_SETTINGS = "Edit KeyCloak / OIDC Settings";
	private static final String ACTION_COMMAND_ACCOUNT_PANEL = "Open KeyCloak Account Panel";
	private static final String ACTION_COMMAND_LOGOUT = "Log out";
	
	private UserAuthenticationStatusButton authenticationStateButton;
	
	private JPopupMenu jPopupMenuAuthentication;
	
	private Action actionOidcSettings;
	private Action actionAccountPanel;
	private Action actionLogOut;
	
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		this.setJButtonIdentityProvider(this.getAuthenticationStateButton());
		OIDCAuthorization.getInstance().addAuthenticationStateListener(this.getAuthenticationStateButton());
	}
	
	private UserAuthenticationStatusButton getAuthenticationStateButton() {
		if (authenticationStateButton==null) {
			authenticationStateButton = new UserAuthenticationStatusButton();
			authenticationStateButton.setPreferredSize(new Dimension(120, 26));
			authenticationStateButton.setMinimumSize(new Dimension(120, 26));
			authenticationStateButton.addActionListener(this);
		}
		return authenticationStateButton;
	}
	
	private JPopupMenu getJPopupMenuAuthentication() {
		if (jPopupMenuAuthentication==null) {
			jPopupMenuAuthentication = new JPopupMenu();
			
			jPopupMenuAuthentication.add(this.getActionOidcSettings());
			jPopupMenuAuthentication.add(this.getActionAccountPanel());
			jPopupMenuAuthentication.add(this.getActionLogOut());
		}
		return jPopupMenuAuthentication;
	}
	
	
	private Action getActionOidcSettings() {
		if (actionOidcSettings==null) {
			actionOidcSettings = new MenuAction(ACTION_COMMAND_OIDC_SETTINGS);
		}
		return actionOidcSettings;
	}
	
	private Action getActionAccountPanel() {
		if (actionAccountPanel==null) {
			actionAccountPanel = new MenuAction(ACTION_COMMAND_ACCOUNT_PANEL);
		}
		return actionAccountPanel;
	}
	
	private Action getActionLogOut() {
		if (actionLogOut==null) {
			actionLogOut = new MenuAction(ACTION_COMMAND_LOGOUT);
		}
		return actionLogOut;
	}
	
	private void showOIDCSettingsDialog() {
		AgentWorkbenchUiManager.getInstance().showModalOptionsDialog(OIDCSettingsOptionTab.OPTIONS_TAB_TITLE);
	}
	
	private void openAccountPanel() {
		
		String realmPart = "";
		if (this.getOIDCSettings().getRealmID()!=null && this.getOIDCSettings().getRealmID().isBlank()==false) {
			realmPart = "/realms/" + this.getOIDCSettings().getRealmID();
		}
		
		try {
			URI accountPanelURI = new URI(this.getOIDCSettings().getIssuerURL()).resolve(realmPart + "/account");
			Desktop.getDesktop().browse(accountPanelURI);
			
		} catch (URISyntaxException | IOException e) {
			String errorMessage = "Could not open the account panel! Please check your issuer URI and realm ID in the OIDC settings!";
			System.err.println("[" + this.getClass().getSimpleName() + "] " + errorMessage);
			Window owner = OwnerDetection.getOwnerWindowForComponent(this.getAuthenticationStateButton());
			JOptionPane.showMessageDialog(owner, errorMessage, "Could not open the account panel!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private OIDCSettings getOIDCSettings() {
		return OIDCAuthorization.getInstance().getOIDCSettings();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getAuthenticationStateButton()) {
			
			switch(OIDCAuthorization.getInstance().getAuthenticationState()) {
			case LOGGED_IN:
				this.showPopupMenu();
				break;
			case LOGGED_OUT:
				OIDCAuthorization.getInstance().requestAuthorizationCode(true);
				break;
			case PENDING:
				OIDCAuthorization.getInstance().cancelLogIn();
				break;
			}
		}
	}
	
	/**
	 * Shows the popup menu.
	 */
	private void showPopupMenu() {
		// --- Align the menu to the right with the button ----------
		int moveLeft = this.getJPopupMenuAuthentication().getPreferredSize().width - this.getAuthenticationStateButton().getWidth();
		this.getJPopupMenuAuthentication().show(this.getAuthenticationStateButton(), -moveLeft, this.getAuthenticationStateButton().getHeight());
	}

	/**
	 * Simple {@link Action} implementation, to allow using the same item in both menus.
	 */
	private class MenuAction extends AbstractAction {
		
		private static final long serialVersionUID = 7297945247474043596L;

		/**
		 * Instantiates a new menu action.
		 *
		 * @param name the name
		 */
		public MenuAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getActionCommand().equals(ACTION_COMMAND_OIDC_SETTINGS)) {
				OIDCAuthenticationUIExtension.this.showOIDCSettingsDialog();
			} else if (ae.getActionCommand().equals(ACTION_COMMAND_ACCOUNT_PANEL)) {
				OIDCAuthenticationUIExtension.this.openAccountPanel();
			} else if (ae.getActionCommand().equals(ACTION_COMMAND_LOGOUT)) {
				OIDCAuthorization.getInstance().triggerRemoteLogout();
			}
		}
		
	}
	
}
