package de.enflexit.awb.ws.client;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class CredentialAssignment.
 * @author Christian Derksen - SOFTEC 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CredentialAssignment", propOrder = {
    "id",
    "idServerURL",
    "idApiRegistrationDefaultBundleName",
    "idCredential"
})
public class CredentialAssignment implements Serializable {
	
	public CredentialAssignment() {
		getID();
	}


	private static final long serialVersionUID = -7854620281999217458L;
	
	private UUID id;
	private UUID idServerURL;
	private String idApiRegistrationDefaultBundleName;
	private UUID idCredential;
	
	/**
	 * Returns the id of a credential.
	 * @return the credential id
	 */
	public UUID getID() {
		if (id==null) {
			// --- Randomize an ID ------------------
			id = UUID.randomUUID();
		}
		return id;
	}
	
	
	/**
	 * Gets the id of the server URL.
	 *
	 * @return the id of the server URL
	 */
	public UUID getIdServerURL() {
		return idServerURL;
	}


	/**
	 * Sets the id of the server URL.
	 *
	 * @param idServerURL the new id server URL
	 */
	public void setIdServerURL(UUID idServerURL) {
		this.idServerURL = idServerURL;
	}


	/**
	 * Gets the id of api registration.
	 *
	 * @return the id of the api registration
	 */
	public String getIdApiRegistrationDefaultBundleName() {
		return idApiRegistrationDefaultBundleName;
	}


	/**
	 * Sets the id of the api registration.
	 *
	 * @param idApiRegistration the new id of the api registration
	 */
	public void setIdApiRegistrationDefaultBundleName(String idApiRegistration) {
		this.idApiRegistrationDefaultBundleName = idApiRegistration;
	}


	/**
	 * Gets the id of the credential.
	 *
	 * @return the id of the credential
	 */
	public UUID getIdCredential() {
		return idCredential;
	}


	/**
	 * Sets the id of the credential.
	 *
	 * @param idCredential the new id of the credential
	 */
	public void setIdCredential(UUID idCredential) {
		this.idCredential = idCredential;
	}
    //---------------------------------------------------------------
	//--------------- Method to compare something with this class----
	//--------------------------------------------------------------


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {

		if (compObj == null) {
			return false;
		}

		if (compObj.getClass() != this.getClass()) {
			return false;
		}

		if (compObj instanceof CredentialAssignment) {
			CredentialAssignment credAssg = (CredentialAssignment) compObj;
			boolean equals = super.equals(compObj);
			if (this.getID() != credAssg.getID()) {
				equals = false;
			}

			if (this.getIdApiRegistrationDefaultBundleName() != credAssg.getIdApiRegistrationDefaultBundleName()) {
				equals = false;
			}

			if (this.getIdCredential() != credAssg.getIdCredential()) {
				equals = false;
			}

			if (this.getIdServerURL() != credAssg.getIdServerURL()) {
				equals = false;
			}

			return equals;
		} else {
			return false;
		}
	}
	
}
