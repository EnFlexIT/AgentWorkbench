package de.enflexit.awb.ws.credential;

public class ApiKeyCredential implements Credential{
	
	private CredentialType credType = CredentialType.API_KEY;	
	private ApiKey apiKey;

	public ApiKeyCredential(ApiKey apiKey) {
		 this.apiKey=apiKey;
	}
	@Override
	public CredentialType getCredentialType() {
		return credType;
	}

	@Override
	public Object getCredentialValues() {
		return apiKey;
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
