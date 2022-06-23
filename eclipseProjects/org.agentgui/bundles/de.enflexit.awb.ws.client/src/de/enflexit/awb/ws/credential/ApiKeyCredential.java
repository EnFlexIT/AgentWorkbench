package de.enflexit.awb.ws.credential;

import org.osgi.framework.FrameworkUtil;

public class ApiKeyCredential implements WsApiCredentialService{
	
	private CredentialType credType = CredentialType.API_KEY;	
	private ApiKey apiKey;
	private String serverUrl;
	private String serverName;
	private String functionalityID;

	public ApiKeyCredential(ApiKey apiKey,String serverUrl,String serverName, String functionalityID) {
		 this.apiKey=apiKey;
		 this.serverUrl=serverUrl;
		 this.serverName=serverName;
	}
	@Override
	public CredentialType getCredentialType() {
		return credType;
	}

	@Override
	public Object getCredentialValues() {
		return apiKey;
	}
	
	@Override
	public String getServerUrl() {
		return serverUrl;
	}
	
	@Override
	public String getServerName() {
		return serverName;
	}
	
	@Override
	public String getFunctionalityID() {
		return functionalityID;
	}
	
	@Override
	public String getClientBundleName() {
		return FrameworkUtil.getBundle(this.getClass()).getSymbolicName();
	}
	
	//-------------------------------------------------------
	//Class to handle an APIKey and save its values
	//-------------------------------------------------------
	
	public class ApiKey {

		private String apiKey;
		private LocationInRequest location;

		public ApiKey(String apiKey, LocationInRequest location) {
			this.apiKey = apiKey;
			this.location = location;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public void setLocationInRequest(LocationInRequest location) {
			this.location = location;
		}

		public LocationInRequest getLocationInRequest() {
			return location;
		}
	}
}
