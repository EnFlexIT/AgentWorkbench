package de.enflexit.awb.ws.credential;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class UserPasswordCredential.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserPasswordCredential", propOrder = {
    "userName",
    "password"
})
public class UserPasswordCredential extends AbstractCredential {
	
	private static final long serialVersionUID = -4205649257056012094L;
	
	private String userName;
	private String password;
		
	/**
	 * Gets the user name.
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the user name.
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Gets the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password.
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals=super.equals(obj);
		if(obj instanceof UserPasswordCredential) {
			UserPasswordCredential cred=(UserPasswordCredential) obj;
			if(this.getPassword()==null || this.getPassword()==null) {
				return equals;
			}
			
			if(cred.getUserName()==null || cred.getUserName()==null) {
				return equals;
			}
			if(cred.getPassword()==null || cred.getPassword()==null) {
				return equals;
			}
			if(this.getUserName().equals(cred.getUserName())) {
				if(this.getPassword().equals(cred.getPassword())) {
					equals=true;
				}
			}
			

		}else {
			equals=false;
		}
		return equals;
	}

	@Override
	public boolean isEmpty() {
		boolean empty=false;
		if(userName==null|| password==null) {
			empty = true;
		}
		if(userName.isBlank()) {
			empty=true;
		}
		if(password.isBlank()) {
			empty=true;
		}
		return empty;
	}
}
