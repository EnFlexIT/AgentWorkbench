package de.enflexit.awb.ws.credential;

import java.io.Serializable;
import java.util.UUID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import de.enflexit.awb.ws.client.CredentialType;

/**
 * The Class AbstractCredential.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractCredential", propOrder = {
    "id",
    "name"
})
@XmlSeeAlso({ApiKeyCredential.class, BearerTokenCredential.class,UserPasswordCredential.class})
public class AbstractCredential implements Serializable {

	private static final long serialVersionUID = -5179570740645614521L;

	private UUID id;
	private String name;
	
	public AbstractCredential() {
		getID();
	}
	
	/**
	 * Returns the id of a credential.
	 * @return the credential id
	 */
	public UUID getID() {
		if (id==null) {
			id = UUID.randomUUID();
		}
		return id;
	}
	
	/**
	 * Returns a name (short description) for the credential.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty(){	
		if(name!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public CredentialType getCredentialType() {
		if (this instanceof ApiKeyCredential) {
			return CredentialType.API_KEY;
		} else if (this instanceof BearerTokenCredential) {
			return CredentialType.BEARER_TOKEN;
		} else if (this instanceof UserPasswordCredential) {
			return CredentialType.USERNAME_PASSWORD;
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = true;
		if (obj instanceof AbstractCredential) {
			AbstractCredential abstCred = (AbstractCredential) obj;
			if (abstCred.getID() != this.getID()) {
				equals = false;
			}
			if (abstCred.getName() != null) {
				if (!abstCred.getName().equals(this.getName())) {
					equals = false;
				}
			}
		} else {
			equals = false;
		}

		return equals;
	}
	
	@Override
	public String toString() {
		if(this.getName()!=null) {
			return this.getName();
		}
		return super.toString();
	}
}
