package de.enflexit.awb.ws.credential;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class ApiKeyCredential.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApiKeyCredential", propOrder = {
    "apiKeyPrefix",
    "apiKeyValue"
})
public class ApiKeyCredential extends AbstractCredential {

	private static final long serialVersionUID = -7248358188757558331L;
	
	private String apiKeyPrefix;
	private String apiKeyValue;
	

	/**
	 * Sets the api key name.
	 * @param apiKeyPrefix the new api key name
	 */
	public void setApiKeyPrefix(String apiKeyPrefix) {
		this.apiKeyPrefix = apiKeyPrefix;
	}
	/**
	 * Sets the api key value.
	 * @param apiKeyValue the new api key value
	 */
	public void setApiKeyValue(String apiKeyValue) {
		this.apiKeyValue = apiKeyValue;
	}

	/**
	 * Gets the api key value.
	 * @return the api key value
	 */
	public String getApiKeyValue() {
		return apiKeyValue;
	}

	/**
	 * Gets the api key prefix.
	 *
	 * @return the api key prefix
	 */
	public String getApiKeyPrefix() {
		return apiKeyPrefix;
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.credential.AbstractCredential#equals(java.lang.Object)
	*/
	@Override
	public boolean equals(Object obj) {
		boolean equals=super.equals(obj);
		if(obj instanceof ApiKeyCredential) {
			ApiKeyCredential cred=(ApiKeyCredential) obj;
			if(this.getApiKeyValue()==null || this.getApiKeyPrefix()==null) {
				return equals;
			}
			
			if(cred.getApiKeyValue()==null || cred.getApiKeyPrefix()==null) {
				return equals;
			}
			if(!this.getApiKeyValue().equals(cred.getApiKeyValue())) {
				equals=false;
			}
			
			if(!this.getApiKeyPrefix().equals(cred.getApiKeyPrefix())) {
				equals=false;
			}
		}else {
			equals=false;
		}
		return equals;
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.credential.AbstractCredential#isEmpty()
	*/
	@Override
	public boolean isEmpty() {
		boolean empty=false;
		if(apiKeyPrefix==null || apiKeyValue==null) {
			empty = true;
		}
		else if(apiKeyPrefix.isBlank()) {
			empty=true;
		}else if(apiKeyValue.isBlank()) {
			empty=true;
		}
		return empty;
	}
	
}
