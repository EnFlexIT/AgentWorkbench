package de.enflexit.awb.ws.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.common.ServiceFinder;

/**
 * The Class WsCredentialStore.
 *
 * @author Timo Brandhorst - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class WsCredentialStore implements Serializable {
	
	private static final long serialVersionUID = -2711360698936471113L;
	
	// ----------------------------------------------------
	// --- From here the singleton definition -------------
	// ----------------------------------------------------
	private static WsCredentialStore instance;
	private WsCredentialStore() {};
	
	/**
	 * Returns the single instance of WsCredentialStore.
	 * @return single instance of WsCredentialStore
	 */
	public static WsCredentialStore getInstance() {
		if (instance==null) {
			instance=new WsCredentialStore();
		}
		return instance;
	}

	// ----------------------------------------------------
	// --- From here attributes and access methods --------
	// ----------------------------------------------------
	private List<ApiRegistration> apiRegistrationServiceList;
	private List<ServerURL> serverURLList;
	private List<AbstractCredential> credentialList;
	private List<CredentialAssignment> credentialAssignmentList;
	
	/**
	 * Returns the api registration service list.
	 * @return the api registration service list
	 */
	public List<ApiRegistration> getApiRegistrationServiceList() {
		if (apiRegistrationServiceList==null) {
			apiRegistrationServiceList = new ArrayList<>();
			// --- Fill the list ------------------------------------
			List<AwbApiRegistrationService> registeredServiceList = ServiceFinder.findServices(AwbApiRegistrationService.class);
			for (int i = 0; i < registeredServiceList.size(); i++) {
				apiRegistrationServiceList.add(new ApiRegistration(registeredServiceList.get(i)));
			}
		}
		return apiRegistrationServiceList;
	}
	/**
	 * Sets the api registration service list.
	 * @param apiRegistrationServiceList the new api registration service list
	 */
	public void setApiRegistrationServiceList(List<ApiRegistration> apiRegistrationServiceList) {
		this.apiRegistrationServiceList = apiRegistrationServiceList;
	}
	/**
	 * Returns the api registration.
	 *
	 * @param bundleName the bundle name
	 * @return the api registration
	 */
	public ApiRegistration getApiRegistration(String bundleName) {
		if (bundleName==null || bundleName.isBlank()==true) return null;
		for (ApiRegistration apiReg : this.getApiRegistrationServiceList()) {
			if (apiReg.getClientBundleName().equals(bundleName)) {
				return apiReg;
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the server URL list.
	 * @return the server URL list
	 */
	public List<ServerURL> getServerURLList() {
		if (serverURLList==null) {
			serverURLList = new ArrayList<>();
		}
		return serverURLList;
	}
	/**
	 * Sets the server URL list.
	 * @param serverURLList the new server URL list
	 */
	public void setServerURLList(List<ServerURL> serverURLList) {
		this.serverURLList = serverURLList;
	}
	/**
	 * Returns the api registration.
	 *
	 * @param url the bundle name
	 * @return the api registration
	 */
	public ServerURL getServerURL(String url) {
		if (url==null || url.isBlank()==true) return null;
		for (ServerURL serverURL : this.getServerURLList()) {
			if (serverURL.getServerURL().equals(url)) {
				return serverURL;
			}
		}
		return null;
	}
	
	/**
	 * Returns the credential list.
	 * @return the credential list
	 */
	public List<AbstractCredential> getCredentialList() {
		if (credentialList==null) {
			credentialList = new ArrayList<>();
		}
		return credentialList;
	}
	/**
	 * Sets the credential list.
	 * @param credentialList the new credential list
	 */
	public void setCredentialList(List<AbstractCredential> credentialList) {
		this.credentialList = credentialList;
	}
	/**
	 * Return the credential specified by its name.
	 *
	 * @param credentialName the credential name
	 * @return the credential
	 */
	public AbstractCredential getCredential(String credentialName) {
		if (credentialName==null || credentialName.isBlank()==true) return null;
		for (AbstractCredential credential : this.getCredentialList()) {
			if (credential.getName().equals(credentialName)) {
				return credential;
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the credential assignment list.
	 * @return the credential assignment list
	 */
	public List<CredentialAssignment> getCredentialAssignmentList() {
		if (credentialAssignmentList==null) {
			credentialAssignmentList = new ArrayList<>();
		}
		return credentialAssignmentList;
	}
	/**
	 * Sets the credential assignment list.
	 * @param credentialAssignmentList the new credential assignment list
	 */
	public void setCredentialAssignmentList(List<CredentialAssignment> credentialAssignmentList) {
		this.credentialAssignmentList = credentialAssignmentList;
	}
	
	
	// ----------------------------------------------------
	// --- From here attributes and access methods --------
	// ----------------------------------------------------

	/**
	 * Returns the credential for the specified and registered {@link AwbApiRegistrationService}.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param apiRegistrationClass the api registration
	 * @return the credential
	 */
	public <T> T getCredential(T type, Class<? extends AwbApiRegistrationService> apiRegistrationClass) {
		
		List<AwbApiRegistrationService> registeredServiceList = ServiceFinder.findServices(AwbApiRegistrationService.class);
		for (int i = 0; i < registeredServiceList.size(); i++) {
			AwbApiRegistrationService serviceInstance = registeredServiceList.get(i);
			if (serviceInstance.getClass().equals(apiRegistrationClass)) {
				
				String clientBundleName = serviceInstance.getClientBundleName();
				String defaultURL = serviceInstance.getDefaultURL();
				String defaultCredentialName = serviceInstance.getDefaultCredentialName();
				
				AbstractCredential credentiaLFound = this.getCredential(defaultCredentialName);
				if (credentiaLFound==null) {
					// --- Not defined yet - Define the necessary CredentialAssignment -----
					ApiRegistration apiRegistration = this.getApiRegistration(clientBundleName);
					if (apiRegistration==null) {
						//
					}
					ServerURL serverURL = this.getServerURL(defaultURL);
					if (serverURL==null) {
						ServerURL newServerURL = new ServerURL(defaultURL);
						if (this.getServerURLList().contains(newServerURL)==false) {
							this.getServerURLList().add(newServerURL);
						}
					}
					
				}
				
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the credential.
	 *
	 * @param clientBundle the client bundle
	 * @param serverURL the server URL
	 * @param defaultCredentialName the default credential name
	 * @return the credential
	 */
	public AbstractCredential getCredential(String clientBundle, String serverURL, String defaultCredentialName) {
		
		AbstractCredential credentiaLFound = this.getCredential(defaultCredentialName);
		if (credentiaLFound==null) {
			// --- Not defined yet - Define the  
			
		}
		
		this.getCredentialList();
		
		
		return credentiaLFound;
	}
	
	
}
