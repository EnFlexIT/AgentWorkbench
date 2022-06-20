package de.enflexit.awb.ws.client;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.credential.WsApiCredentialService;
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
	
	public List<WsApiCredentialService> getCredentials(String bundleName, String serverName,String functionalityID) {
	String serviceID=bundleName+serverName+functionalityID;
	List<WsApiCredentialService> clientServiceList=new ArrayList<WsApiCredentialService>();
	List<WsApiCredentialService> serviceList = ServiceFinder.findServices(WsApiCredentialService.class);
	for (WsApiCredentialService wsApiCredentialService : serviceList) {
		if(wsApiCredentialService.getCredentialID().equals(serviceID)) {
			clientServiceList.add(wsApiCredentialService);
			break;
		}
	}
	 return clientServiceList;	
	}

}
