package de.enflexit.awb.ws.client;

/**
 * The Class WsCredentialStore.
 *
 * @author Timo Brandhorst - SOFTEC - ICB - University of Duisburg-Essen
 */
public class WsCredentialStore {
	
	private static WsCredentialStore instance;
	private WsCredentialStore() {};
	
	/**
	 * Returns the single instance of WsCredentialStore.
	 * @return single instance of WsCredentialStore
	 */
	public static WsCredentialStore getInstance() {
		if(instance==null) {
			instance=new WsCredentialStore();
		}
		return instance;
	}
	

}
