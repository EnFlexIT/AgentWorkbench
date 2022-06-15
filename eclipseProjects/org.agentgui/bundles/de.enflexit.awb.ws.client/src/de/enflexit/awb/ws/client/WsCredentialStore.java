package de.enflexit.awb.ws.client;

import java.util.List;

import de.enflexit.awb.ws.credential.Credential;
import de.enflexit.common.ServiceFinder;

public class WsCredentialStore {
	
	private static WsCredentialStore instance;
	private WsCredentialStore() {};
	
	
	public static WsCredentialStore getInstance() {
		if(instance==null) {
			instance=new WsCredentialStore();
		}
		return instance;
	}
	
	public Credential getCredentials(String Bundlename) {
	WsApiCredentialService clientService=null;
	List<WsApiCredentialService> serviceList = ServiceFinder.findServices(WsApiCredentialService.class);
	for (WsApiCredentialService wsApiCredentialService : serviceList) {
		if(wsApiCredentialService.getCredentialID().equals(Bundlename)) {
			clientService=wsApiCredentialService;
			break;
		}
	}
	 return null;	
	}

}
