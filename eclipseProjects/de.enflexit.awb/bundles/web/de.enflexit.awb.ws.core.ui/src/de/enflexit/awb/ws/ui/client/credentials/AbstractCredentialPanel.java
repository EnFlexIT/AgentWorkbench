package de.enflexit.awb.ws.ui.client.credentials;

import javax.swing.JPanel;

import de.enflexit.awb.ws.credential.AbstractCredential;

/**
 * The abstract class AbstractCredentialPanel is a foundation for  the Credential creation
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 * @param <T> the generic type
 */
public abstract class AbstractCredentialPanel<T extends AbstractCredential> extends JPanel {
	public AbstractCredentialPanel() {
	}

	private static final long serialVersionUID = -5414771661115782779L;
	
	public T credential;

	/**
	 * Gets the created credential.
	 *
	 * @return the credential
	 */
	public  T getCredential() {
		return credential;
	}
	
	
	/**
	 * Sets the credential.
	 *
	 * @param credential the new credential
	 */
	public  void setCredential(T credential) {
		 this.credential=credential;
	};
	
}
