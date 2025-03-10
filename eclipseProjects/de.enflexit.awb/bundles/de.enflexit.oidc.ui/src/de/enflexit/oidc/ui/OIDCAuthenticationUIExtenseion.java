package de.enflexit.oidc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.agentgui.gui.swing.MainWindowExtension;

import de.enflexit.oidc.OIDCAuthorization;

public class OIDCAuthenticationUIExtenseion extends MainWindowExtension implements ActionListener {
	
	private UserAuthenticationStatusButton authenticationStateButton;

	@Override
	public void initialize() {
		this.addToolbarComponent(this.getAuthenticationStateButton(), null, SeparatorPosition.SeparatorInFrontOf);
	}
	
	private UserAuthenticationStatusButton getAuthenticationStateButton() {
		if (authenticationStateButton==null) {
			authenticationStateButton = new UserAuthenticationStatusButton();
			authenticationStateButton.addActionListener(this);
		}
		return authenticationStateButton;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getAuthenticationStateButton()) {
			
			try {
				OIDCAuthorization.getInstance().authorizeByByAuthenticationCode();
			} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | URISyntaxException | IOException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Exception during authorizaiton attempt");
				e.printStackTrace();
			}
		}
	}

}
