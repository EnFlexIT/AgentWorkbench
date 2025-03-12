package de.enflexit.oidc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.agentgui.gui.swing.MainWindowExtension;

import de.enflexit.oidc.AuthenticationState;
import de.enflexit.oidc.AuthenticationStateListener;
import de.enflexit.oidc.OIDCAuthorization;

/**
 * Adds a button for user authentication to the toolbar.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCAuthenticationUIExtension extends MainWindowExtension implements ActionListener, AuthenticationStateListener {
	
	private UserAuthenticationStatusButton authenticationStateButton;

	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		this.addToolbarComponent(this.getAuthenticationStateButton(), null, SeparatorPosition.NoSeparator);
		
		OIDCAuthorization.getInstance().setIssuerURI("https://login.enflex.it");
		OIDCAuthorization.getInstance().setRealmName("enflexService");
		OIDCAuthorization.getInstance().setClientID("AWB-Test");
		OIDCAuthorization.getInstance().setClientSecret("PeQ5NZeH4aGpr58knm2MviLA5IJ5uvY3");
		OIDCAuthorization.getInstance().setLocalCallbackURL("http://localhost:8888/oauth/callback/");
		
		OIDCAuthorization.getInstance().setResourceURI("https://login.enflex.it");
		try {
			OIDCAuthorization.getInstance().setTrustStore(this.getTrustStoreFile());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OIDCAuthorization.getInstance().addAuthenticationStateListener(this);
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

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getAuthenticationStateButton()) {
			
			try {
				OIDCAuthorization.getInstance().startAuthenticationCodeRequest();
			} catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | URISyntaxException | IOException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Exception during authorizaiton attempt");
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.oidc.AuthenticationStateListener#authenticationStateChanged(de.enflexit.oidc.AuthenticationState)
	 */
	@Override
	public void authenticationStateChanged(AuthenticationState authenticationState) {
		this.getAuthenticationStateButton().setAuthenticationState(authenticationState);
	}

}
