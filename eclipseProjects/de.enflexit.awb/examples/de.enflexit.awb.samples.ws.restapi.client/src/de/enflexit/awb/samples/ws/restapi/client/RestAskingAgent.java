package de.enflexit.awb.samples.ws.restapi.client;

import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiException;
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

		AdminsApi api = new AdminsApi();
			try {
	
				ApiKeyCredential apiKeyCredential = WsCredentialStore.getInstance().getCredential(new ApiKeyCredential(),ApiRegistrationService.class);
	
				ApiClient apiClient = api.getApiClient();
				apiClient.setApiKeyPrefix(apiKeyCredential.getApiKeyPrefix());	
			    apiClient.setApiKey(apiKeyCredential.getApiKeyValue());
			    
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			}
			
			SystemInformation sysInfo;
			SystemLoad sysLoad;
			ExecutionState execState;
			try {
				sysInfo = api.infoGet();
				sysLoad = api.loadGet();
				execState = api.stateGet();
				
				System.out.println("Info from agent " + this.getLocalName() + ": Nice ;-):");
				System.out.println(sysInfo);
				System.out.println(sysLoad);
				System.out.println(execState);
			} catch (ApiException e) {
				e.printStackTrace();
			}
			
	}
	
	
}
