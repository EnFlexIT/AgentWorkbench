package de.enflexit.awb.ws.ui.client.credentials;

import javax.swing.JPanel;

import de.enflexit.awb.ws.credential.AbstractCredential;

public abstract class AbstractCredentialPanel<T extends AbstractCredential> extends JPanel {

	private static final long serialVersionUID = -5414771661115782779L;
	
	private T credential;

	public  T getCredential() {
		return credential;
	}
	
	public  void setCredential(T credential) {
		 this.credential=credential;
	};
}
