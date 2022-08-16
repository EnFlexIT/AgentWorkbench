package de.enflexit.awb.ws.credential;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

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
			if(!this.getUserName().equals(cred.getUserName())) {
				equals=false;
			}
			
			if(!this.getPassword().equals(cred.getPassword())) {
				equals=false;
			}
		}else {
			equals=false;
		}
		return equals;
	}

}
