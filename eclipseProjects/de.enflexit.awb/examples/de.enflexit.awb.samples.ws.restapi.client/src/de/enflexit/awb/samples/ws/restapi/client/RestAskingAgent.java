package de.enflexit.awb.samples.ws.restapi.client;

import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.ExecutionState;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemInformation;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemLoad;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import jade.core.Agent;

/**
 * The Class RestAskingAgent.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class RestAskingAgent extends Agent {

	private static final long serialVersionUID = -9754393650175834L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		SystemInformation sysInfo = null;
		SystemLoad sysLoad = null;
		ExecutionState execState = null;
		try {
			// --- Define an ApiClient ----------
			JerseyApiClient jApiClient = new JerseyApiClient();
			jApiClient.setBasePath("http://localhost:8080/sample-rest-api");
			this.setApiCredentials(jApiClient);
			
			// --- Define the API ---------------
			AdminsApi api = new AdminsApi(jApiClient);
			
			// --- Request the API --------------
			sysInfo = api.infoGet();
			sysLoad = api.loadGet();
			execState = api.stateGet();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("Info from agent " + this.getLocalName() + ": Nice ;-):");
			System.out.println(sysInfo);
			System.out.println(sysLoad);
			System.out.println(execState);
		}

	}
	
	/**
	 * Sets the credentials to access the API.
	 * @param apiClient the new api credentials
	 */
	private void setApiCredentials(ApiClient apiClient) {
		
		try {
			// --- Get the required information from the WsCredentialStore
			ApiKeyCredential apiKeyCredential = WsCredentialStore.getInstance().getCredential(new ApiKeyCredential(), ApiRegistrationService.class);
			if (apiKeyCredential!=null) {
				apiClient.setApiKeyPrefix(apiKeyCredential.getApiKeyPrefix());	
			    apiClient.setApiKey(apiKeyCredential.getApiKeyValue());	
			}
		    
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
