package de.enflexit.awb.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import agentgui.core.application.Application;
import agentgui.core.common.AbstractUserObject;
import agentgui.core.project.Project;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.common.ServiceFinder;

/**
 * The Class WsCredentialStore.
 *
 * @author Timo Brandhorst - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettyConfiguration", propOrder = {
		"credentialAssignmentList",
	    "apiRegistrationServiceList",
	    "serverURLList",
	    "credentialList"
	})

public class WsCredentialStore implements Serializable {
	
	private static final long serialVersionUID = -2711360698936471113L;
	private static final String FILE_ENCODING = "UTF-8";
	
	// ---------------------------------------------------
	// --- From here the singleton definition -------------
	// ----------------------------------------------------
	@XmlTransient
	private static WsCredentialStore instance;
	private static String WS_CREDENTIAL_STORE_FILE="WsCredentialStore.xml";
	
	/**
	 * Returns the single instance of WsCredentialStore.
	 * @return single instance of WsCredentialStore
	 */
	public static synchronized WsCredentialStore getInstance() {
		if (instance==null) {
			WsCredentialStore.load(WsCredentialStore.getWsCredentialStoreFile(Application.getProjectFocused()));
			if(instance==null) {
				instance= new WsCredentialStore();
			}
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
	// --- From here access methods for credentials --------
	// ----------------------------------------------------

	/**
	 * Returns the credential for the specified and registered {@link AwbApiRegistrationService}.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param apiRegistrationClass the api registration
	 * @return the credential
	 */
	@SuppressWarnings("unchecked")
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
			     credentiaLFound=this.getCredential(type, clientBundleName, defaultURL, defaultCredentialName);
				}
                return (T) credentiaLFound;
			}
		}
		
		return null;
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
	 * Return the credential specified by its ID.
	 *
	 * @param credentialId the credential ID
	 * @return the credential
	 */
	public AbstractCredential getCredential(int id) {
		for (AbstractCredential credential : this.getCredentialList()) {
			if (credential.getID().equals(id)) {
				return credential;
			}
		}
		return null;
	}
	
	private <T> AbstractCredential createCredential(T type, String clientBundleName, String defaultURL) {
		AbstractCredential cred = null;
		if(type.getClass().equals(ApiKeyCredential.class)) {
			cred= new ApiKeyCredential();	
		}
		if(type.getClass().equals(BearerTokenCredential.class)) {
			cred= new BearerTokenCredential();
		}
		
		if(type.getClass().equals(UserPasswordCredential.class)) {
			cred= new UserPasswordCredential();
		}	
		return cred;
	}
	
	private <T> ApiRegistration createNewApiRegistration(T type, String clientBundleName, String defaultURL) {
		ApiRegistration apiRegistration = new ApiRegistration();
		if(type.getClass().equals(ApiKeyCredential.class)) {
		apiRegistration= new ApiRegistration(clientBundleName, "client for Server-API with following URL: "  + defaultURL,CredentialType.API_KEY, defaultURL);						
		}
		
		if(type.getClass().equals(BearerTokenCredential.class)) {
		apiRegistration= new ApiRegistration(clientBundleName, "client for Server-API with following URL: "  + defaultURL, CredentialType.BEARER_TOKEN, defaultURL);
		}
		
		if(type.getClass().equals(UserPasswordCredential.class)) {
		apiRegistration= new ApiRegistration(clientBundleName, "client for Server-API with following URL: "  + defaultURL, CredentialType.USERNAME_PASSWORD, defaultURL);
		}
		
		return apiRegistration;
	}
	
	
	
	/**
	 * Returns the credential.
	 *
	 * @param clientBundle          the client bundle
	 * @param serverURL             the server URL
	 * @param defaultCredentialName the default credential name
	 * @return the credential
	 */
	public <T> AbstractCredential getCredential(T type, String clientBundleName, String defaultURL,String defaultCredentialName) {

		AbstractCredential credentiaLFound = this.getCredential(defaultCredentialName);
		if (credentiaLFound == null) {
			ApiRegistration apiRegistration = this.getApiRegistration(clientBundleName);
			ServerURL serverURL = this.getServerURL(defaultURL);

			// --- Define the server URL
			if (serverURL == null) {
				serverURL = new ServerURL(defaultURL);
				if (this.getServerURLList().contains(serverURL) == false) {
					this.getServerURLList().add(serverURL);
				}
			}

			// --- Define the Server api and registrate it
			if (apiRegistration == null) {
				apiRegistration = createNewApiRegistration(type, clientBundleName, serverURL.getServerURL());
				if (this.getApiRegistrationServiceList().contains(apiRegistration) == false) {
					this.getApiRegistrationServiceList().add(apiRegistration);
				}
			}

			// --- Define a empty credential of the same type, which can be filled by the
			// user
			credentiaLFound = createCredential(type, clientBundleName, defaultURL);
			if (this.getCredentialList().contains(credentiaLFound) == false) {
				this.getCredentialList().add(credentiaLFound);
			}

			// --- Define the credential assignment for a clear hierarchy
			CredentialAssignment credAssgn = new CredentialAssignment();
			credAssgn.setIdServerURL(serverURL.getID());
			credAssgn.setIdApiRegistration(apiRegistration.getID());
			credAssgn.setIdCredential(credentiaLFound.getID());
			this.getCredentialAssignmentList().add(credAssgn);

		}
		return credentiaLFound;
	}
	
	/**
	 * Checks if the given {@link AbstractCredential} is in the CredentialList
	 * If not the credential will be added to the list
	 * @param abstrCred corresponding {@link AbstractCredential}
	 */
	public void putInCredentialList(AbstractCredential abstrCred) {
        if(!credentialList.contains(abstrCred)) {
        	credentialList.add(abstrCred);
        }
	}
	
	// ------------------------------------------------------------
	// --- From here access methods for AwbApiRegistration --------
	// ------------------------------------------------------------
	
	/**
	 * Get the {@link ApiRegistration} representation from a {@link AwbApiRegistrationService} of the ApiRegistrationList
	 * @param awbApiRegService
	 * @return <code>null</code> if there is no {@link ApiRegistration} representation of given {@link AwbApiRegistrationService else the corresponding ApiRegistration instance
	 */
	public ApiRegistration getApiRegistrationFromService(AwbApiRegistrationService awbApiRegService) {
		ApiRegistration sameObject = null;
		for (int i = 0; i < apiRegistrationServiceList.size(); i++) {
			ApiRegistration apiReg = apiRegistrationServiceList.get(i);
			if (apiReg.getClientBundleName().equals(awbApiRegService.getClientBundleName())) {
				if (apiReg.getCredentialType().equals(awbApiRegService.getCredentialType())) {
					if (apiReg.getDefaultCredentialName().equals(awbApiRegService.getDefaultCredentialName())) {
						if (apiReg.getDescription().equals(awbApiRegService.getDescription())) {
							if (apiReg.getServerURL().equals(awbApiRegService.getDefaultURL())) {
								sameObject = apiReg;
								break;
							}
						}
					}
				}
			}
		}
		return sameObject;
	}
	
	/**
	 * Checks if the given {@link AwbApiRegistrationService} is in the ApiRegistrationList
	 * If not the service will be added to the list
	 * @param awbApiRegService corresponding {@link AwbApiRegistrationService}
	 */
	public void putInApiRegistrationList(AwbApiRegistrationService awbApiRegService) {
        if(!containedInApiRegistrationList(awbApiRegService)) {
        	apiRegistrationServiceList.add(new ApiRegistration(awbApiRegService));
        }
	}
	
	/**
	 * Checks if a {@link AwbApiRegistrationService} is represented by a {@link ApiRegistration} class in the ApiRegistrationList
	 * @param awbApiRegService the {@link AwbApiRegistrationService} to check
	 * @return true if the given awbApiRegService is contained in the ApiRegistrationList
	 */
	public boolean containedInApiRegistrationList(AwbApiRegistrationService awbApiRegService) {
		boolean contains = false;
		if(apiRegistrationServiceList==null) {
			getApiRegistrationServiceList();
		}
		
		for (int i = 0; i < apiRegistrationServiceList.size(); i++) {
			ApiRegistration apiReg = apiRegistrationServiceList.get(i);
			if (apiReg.getClientBundleName().equals(awbApiRegService.getClientBundleName())) {
				if (apiReg.getCredentialType().equals(awbApiRegService.getCredentialType())) {
					if (apiReg.getDefaultCredentialName().equals(awbApiRegService.getDefaultCredentialName())) {
						if (apiReg.getDescription().equals(awbApiRegService.getDescription())) {
							if (apiReg.getServerURL().equals(awbApiRegService.getDefaultURL())) {
								contains = true;
								break;
							}
						}
					}

				}
			}
		}
		return contains;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here methods to save or load a WsCredentialStore -----------------------
	// ----------------------------------------------------------------------------------
	
	/**
	 * Gets the WsCredentialStore-File
	 * @param project the current project
	 * @return File of WsCredentialStoree
	 */
	public static File getWsCredentialStoreFile(Project project) {	
		String awbSetup=null;
		File wsCredentialFile=null;
		
	    if(Application.getProjectFocused()!=null) {
		awbSetup = Application.getProjectFocused().getSimulationSetupCurrent();
		wsCredentialFile = new File (Application.getProjectFocused().getProjectFolderFullPath() + File.separator + "Client" + File.separator +  awbSetup + File.separator + WS_CREDENTIAL_STORE_FILE);	
	    }else {
	    if (project==null) return null;
	    awbSetup=project.getSimulationSetupCurrent();
	    wsCredentialFile = new File (project.getProjectFolderFullPath() + File.separator + "Client" + File.separator +  awbSetup + File.separator + WS_CREDENTIAL_STORE_FILE);		
	    }			
		//if parent directory does not exist create it
		File parentFile=wsCredentialFile.getParentFile();
		if(!parentFile.exists()) {
			parentFile.mkdirs();
		}
		
		return wsCredentialFile;
	}
	
	/**
	 * Saves the current configuration.
	 * 
	 * @return true, if successful
	 */
	public boolean save() {
		return save(this);
	}

	/**
	 * Saves the specified WsCredentialStore to the specified file.
	 *
	 * @param file              the file
	 * @param WsCredentialStore the wsCredentialStore
	 * @return true, if successful
	 */
	public static boolean save(WsCredentialStore wsCredentialStore) {

		boolean success = true;

		// --- Check the JettyConfiguration instance ------
		if (wsCredentialStore == null) {
			System.err.println("[" + WsCredentialStore.class.getSimpleName()
					+ "] No WsCredentialStore instance was specified to be saved!");
			return false;
		}

		// --- Where to save? -----------------------------
		File file = getWsCredentialStoreFile(Application.getProjectFocused());
		if (file == null) {
			System.err.println("[" + WsCredentialStore.class.getSimpleName()
					+ "] The path for saving a configuration as XML file is not allowed to be null!");
			return false;
		}

		FileWriter fileWriter = null;
		try {
			// --- Define the JAXB context ------------
			JAXBContext pc = JAXBContext.newInstance(WsCredentialStore.class);
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, FILE_ENCODING);
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// --- Write instance to xml-File ---------
			fileWriter = new FileWriter(file);
			pm.marshal(wsCredentialStore, fileWriter);
			success = true;

		} catch (Exception ex) {
			System.err.println("[" + WsCredentialStore.class.getSimpleName()
					+ "] Error while saving the user object as XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return success;
	}

	/**
	 * Loads a WsCredentialStore from the specified file.
	 *
	 * @param file the file
	 * @return the WsCredentialStore configuration
	 */
	public static WsCredentialStore load(File file) {
		
		if (file==null || file.exists()==false) return null;
		
		WsCredentialStore wsCredStore = null;
		InputStream inputStream = null;
		InputStreamReader isReader = null;
		try {
			
			JAXBContext context = JAXBContext.newInstance(WsCredentialStore.class);
			Unmarshaller unMarsh = context.createUnmarshaller();
			
			inputStream = new FileInputStream(file);
			isReader  = new InputStreamReader(inputStream, FILE_ENCODING);
			
			Object jaxbObject = unMarsh.unmarshal(isReader);
			if (jaxbObject!=null && jaxbObject instanceof WsCredentialStore) {
				wsCredStore = (WsCredentialStore)jaxbObject;
			}
			
		} catch (Exception ex) {
			System.out.println("[" + AbstractUserObject.class.getSimpleName() + "] Error while loading the user object from XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (isReader!=null) isReader.close();
				if (inputStream!=null) inputStream.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}	
		}
		return wsCredStore;
	}
}