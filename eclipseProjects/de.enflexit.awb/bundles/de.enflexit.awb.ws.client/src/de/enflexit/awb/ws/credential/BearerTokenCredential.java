package de.enflexit.awb.ws.credential;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class BearerTokenCredential.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BearerTokenCredential", propOrder = {
    "jwtToken"
})
public class BearerTokenCredential extends AbstractCredential {
	
	private static final long serialVersionUID = 5553518664784944192L;
	
	private JwtToken jwtToken;


	/**
	 * Gets the {@link JwtToken}.
	 * @return the jwt token
	 */
	public JwtToken getJwtToken() {
		return jwtToken;
	}
	/**
	 * Sets the {@link JwtToken}.
	 * @param jwtToken the new jwt token
	 */
	public void setJwtToken(JwtToken jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals=super.equals(obj);
		if(obj instanceof BearerTokenCredential) {
			BearerTokenCredential cred=(BearerTokenCredential) obj;
			if (cred.getJwtToken() == null || this.getJwtToken()==null) {
				return equals;
			}
			if(!this.getJwtToken().equals(cred.getJwtToken())) {
				equals=false;
			}
		}else {
			equals=false;
		}
		return equals;
	}
	
	@Override
	public boolean isEmpty() {
		boolean empty=false;
		if(jwtToken==null) {
			empty = true;
		}
		else if(jwtToken.getJwtToken().isBlank()) {
			empty=true;
		}
		return empty;
	}
	
}
