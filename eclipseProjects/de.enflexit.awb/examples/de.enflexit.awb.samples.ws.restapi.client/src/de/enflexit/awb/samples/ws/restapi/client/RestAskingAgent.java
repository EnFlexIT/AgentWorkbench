package de.enflexit.awb.samples.ws.restapi.client;

import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ServerConfiguration;
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

		try {

			ApiKeyCredential apiKeyCredential = WsCredentialStore.getInstance().getCredential(new ApiKeyCredential(), ApiRegistrationService.class);
//			apiKeyCredential = WsCredentialStore.getInstance().getCredential(new ApiKeyCredential(), "de.enflexit.awb.samples.ws.restapi.client", );
			
			AdminsApi api = new AdminsApi();
			ApiClient apiClient = api.getApiClient();
			if(apiKeyCredential.getApiKeyName()!=null) {
				if(apiKeyCredential.getApiKeyValue()!=null) {
				apiClient.setApiKeyPrefix(apiKeyCredential.getApiKeyName());
				apiClient.setApiKey(apiKeyCredential.getApiKeyValue());
				}
			}
			else {
				System.out.println();
			}
			
			ServerConfiguration newServerConfig = new ServerConfiguration("https://schlagmichtod.de/api", null, null);
			
			int serversSize = apiClient.getServers().size(); 
			apiClient.getServers().add(newServerConfig);
			apiClient.setServerIndex(serversSize);
			
			
			SystemInformation sysInfo = api.infoGet();
			SystemLoad sysLoad = api.loadGet();
			ExecutionState execState =  api.stateGet();
			
			System.out.println("Info from agent " + this.getLocalName() + ": Nice ;-):");
			System.out.println(sysInfo);
			System.out.println(sysLoad);
			System.out.println(execState);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}
	
	
}
