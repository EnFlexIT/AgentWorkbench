package de.enflexit.oidc.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import de.enflexit.awb.baseUI.SeparatorPosition;
import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.oidc.AuthenticationStateListener;
import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCAuthorization.AuthenticationState;
import de.enflexit.oidc.OIDCSettings;

/**
 * Adds a toolbar button and a menu for handling user authentication against keycloak.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCAuthenticationUIExtension extends MainWindowExtension implements ActionListener, AuthenticationStateListener {
	
	private UserAuthenticationStatusButton authenticationStateButton;
	
	private JMenu jMenuAuthentication;
	private JPopupMenu jPopupMenuAuthentication;
	
	private JMenuItem menuItemOIDCSettings;
	private JMenuItem menuItemAccountPanel;
	private JMenuItem menuItemLogOut;
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		this.addToolbarComponent(this.getAuthenticationStateButton(), null, SeparatorPosition.NoSeparator);
		this.addJMenu(this.getJMenuAuthentication(), null);
		
		OIDCSettings oidcSettings = OIDCAuthorization.getInstance().getOIDCSettings();

		OIDCAuthorization.getInstance().setOIDCSettings(oidcSettings);
		
		OIDCAuthorization.getInstance().setResourceURI("https://login.enflex.it");
		try {
			OIDCAuthorization.getInstance().setTrustStore(this.getTrustStoreFile());
		} catch (URISyntaxException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error setting the trust store file!");
			e.printStackTrace();
		}
		
		OIDCAuthorization.getInstance().addAuthenticationStateListener(this);
//		OIDCAuthorization.getInstance().doInitialAuthenticationCheck();
	}
	
	private File getTrustStoreFile() {
		String relativeCacertsPath = "/lib/security/cacerts".replace("/", File.separator);
		String filename = System.getProperty("java.home") + relativeCacertsPath;
		return new File(filename);
	}

	private UserAuthenticationStatusButton getAuthenticationStateButton() {
		if (authenticationStateButton==null) {
			authenticationStateButton = new UserAuthenticationStatusButton();
			authenticationStateButton.addActionListener(this);
		}
		return authenticationStateButton;
	}
	
	private JMenu getJMenuAuthentication() {
		if (jMenuAuthentication==null) {
			jMenuAuthentication = new JMenu("Authentication");
			jMenuAuthentication.add(this.getMenuItemOIDCSettings());
			jMenuAuthentication.add(this.getMenuItemAccountPanel());
		}
		return jMenuAuthentication;
	}
	
	private JPopupMenu getJPopupMenuAuthentication() {
		if (jPopupMenuAuthentication==null) {
			jPopupMenuAuthentication = new JPopupMenu();
			
			jPopupMenuAuthentication.add(this.getMenuItemOIDCSettings());
			jPopupMenuAuthentication.add(this.getMenuItemAccountPanel());
			jPopupMenuAuthentication.add(this.getMenuItemLogOut());
		}
		return jPopupMenuAuthentication;
	}
	
	private  JMenuItem getMenuItemOIDCSettings() {
		if (menuItemOIDCSettings==null) {
			menuItemOIDCSettings = new JMenuItem("Edit KeyCloak / OIDC Settings");
			menuItemOIDCSettings.addActionListener(this);
		}
		return menuItemOIDCSettings;
	}
	
	private JMenuItem getMenuItemAccountPanel() {
		if (menuItemAccountPanel==null) {
			menuItemAccountPanel = new JMenuItem("Open KeyCloak Account Panel");
			menuItemAccountPanel.addActionListener(this);
		}
		return menuItemAccountPanel;
	}
	
	private JMenuItem getMenuItemLogOut() {
		if (menuItemLogOut==null) {
			menuItemLogOut = new JMenuItem("Log out");
			menuItemLogOut.addActionListener(this);
		}
		return menuItemLogOut;
	}
	
	private void showOIDCSettingsDialog() {
//		JDialog settingDialog = new JDialog(Application.getMainWindow());
		JDialog settingDialog = new JDialog();
		settingDialog.setTitle("OpenID Connect Configuration");
		settingDialog.setContentPane(new OIDCSettingsPanel(this.getOIDCSettings(), settingDialog));
		settingDialog.setSize(600, 450);
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(settingDialog, JDialogPosition.ParentCenter);
		settingDialog.setVisible(true);
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
//			JOptionPane.showMessageDialog(Application.getMainWindow(), errorMessage, "Could not open the account panel!", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, errorMessage, "Could not open the account panel!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private OIDCSettings getOIDCSettings() {
		return OIDCAuthorization.getInstance().getOIDCSettings();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.oidc.AuthenticationStateListener#authenticationStateChanged(de.enflexit.oidc.AuthenticationState)
	 */
	@Override
	public void authenticationStateChanged(AuthenticationState authenticationState) {
		this.getAuthenticationStateButton().setAuthenticationState(authenticationState);
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
			
		} else if (ae.getSource()==this.getMenuItemOIDCSettings()) {
			this.showOIDCSettingsDialog();
		} else if (ae.getSource()==this.getMenuItemAccountPanel()) {
			this.openAccountPanel();
		} else if (ae.getSource()==this.getMenuItemLogOut()) {
			OIDCAuthorization.getInstance().triggerRemoteLogout();
		}
	}
	
	private void showPopupMenu() {
		this.getJPopupMenuAuthentication().show(this.getAuthenticationStateButton(), 0, this.getAuthenticationStateButton().getHeight());
	}
	
}
