package de.enflexit.awb.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import agentgui.core.application.Application;
import agentgui.core.common.AbstractUserObject;
import agentgui.core.project.Project;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.common.Observable;
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
	    "serverURLList",
	    "credentialList"
	})
public class WsCredentialStore implements Serializable {
	
	private static final long serialVersionUID = -2711360698936471113L;
	private static final String FILE_ENCODING = "UTF-8";
	
	// ---------------------------------------------------
	// --- From here the singleton definition -------------
	// ----------------------------------------------------
	private static WsCredentialStore instance;
	private static String WS_CREDENTIAL_STORE_FILE="WsCredentialStore.xml";
	private static File credentialStoreFile;
	
	/**
	 * Returns the single instance of WsCredentialStore.
	 * @return single instance of WsCredentialStore
	 */
	public static synchronized WsCredentialStore getInstance() {
		
		if (instance==null) {
			credentialStoreFile = WsCredentialStore.getWsCredentialStoreFile();
			instance = WsCredentialStore.load(credentialStoreFile);
			if(instance==null) {
				instance=new WsCredentialStore();
			}
			
		} else {
			if (WsCredentialStore.getWsCredentialStoreFile().equals(credentialStoreFile)==false) {
				WsCredentialStore.save(instance, credentialStoreFile);
				instance = WsCredentialStore.load(credentialStoreFile);
				if(instance==null) {
					instance=new WsCredentialStore();
				}
			}
			
		}
		return instance;
	}

	// ----------------------------------------------------
	// --- From here attributes and access methods --------
	// ----------------------------------------------------
	private List<ServerURL> serverURLList;
	
	@XmlElementWrapper(name="credentials")
	@XmlElement(name="credential")
	private List<AbstractCredential> credentialList;
	
	private List<CredentialAssignment> credentialAssignmentList;
	
	@XmlTransient
	private List<AwbApiRegistrationService> apiRegistrationServiceList;
	
	/**
	 * Returns the api registration service list.
	 * 
	 * @return the api registration service list
	 */
	public List<AwbApiRegistrationService> getApiRegistrationServiceList() {
		if (apiRegistrationServiceList == null) {
			apiRegistrationServiceList = new ArrayList<>();
		}
		apiRegistrationServiceList.clear();
		// --- Fill the list ------------------------------------
		List<AwbApiRegistrationService> registeredServiceList = ServiceFinder.findServices(AwbApiRegistrationService.class);
		for (int i = 0; i < registeredServiceList.size(); i++) {
			apiRegistrationServiceList.add(registeredServiceList.get(i));
		}
		return apiRegistrationServiceList;
	}

	/**
	 * Returns the api registration.
	 *
	 * @param bundleName the bundle name
	 * @return the api registration
	 */
	public AwbApiRegistrationService getApiRegistration(String bundleName) {
		if (bundleName==null || bundleName.isBlank()==true) return null;
		for (AwbApiRegistrationService apiReg : this.getApiRegistrationServiceList()) {
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
			AwbApiRegistrationService apiRegistration = this.getApiRegistration(clientBundleName);
			ServerURL serverURL = this.getServerURL(defaultURL);

			// --- Define the server URL
			if (serverURL == null) {
				serverURL = new ServerURL(defaultURL);
				if (this.getServerURLList().contains(serverURL) == false) {
					this.getServerURLList().add(serverURL);
				}
			}

			// --- Define a empty credential of the same type, which can be filled by the user
			credentiaLFound = createCredential(type, clientBundleName, defaultURL);
			if (this.getCredentialList().contains(credentiaLFound) == false) {
				this.getCredentialList().add(credentiaLFound);
			}

			// --- Define the credential assignment for a clear hierarchy
			if(apiRegistration!=null && serverURL!=null && credentiaLFound!=null) {
				CredentialAssignment credAssgn = new CredentialAssignment();
				credAssgn.setIdServerURL(serverURL.getID());
				credAssgn.setIdApiRegistrationDefaultBundleName(apiRegistration.getClientBundleName());
				credAssgn.setIdCredential(credentiaLFound.getID());
				this.getCredentialAssignmentList().add(credAssgn);
			}else {
				credentiaLFound=null;
			}
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
	

	/**
	 * Update credential in credential list.
	 *
	 * @param abstrCred the abstract credential
	 */
	public void updateCredentialInCredentialList(AbstractCredential abstrCred) {
		    AbstractCredential oldCred=this.getCredential(abstrCred.getName());
        	if(oldCred!=null) {
        		credentialList.remove(oldCred);
        		this.putInCredentialList(abstrCred);
        	}else {
        	  oldCred=this.getCredentialWithID(abstrCred.getID());
        	  if(oldCred!=null) {
        		credentialList.remove(oldCred);
          		this.putInCredentialList(abstrCred); 
        	  }
        	}
	}
	
	/**
	 * Checks if credentials with the given {@link CredentialType} are in the
	 * CredentialList If not the credential will be added to the list
	 * 
	 * @param credType corresponding {@link CredentialType}
	 */
	public List<AbstractCredential> getAllCredentialsOfaType(CredentialType credType) {
		List<AbstractCredential> credList = getCredentialList();
		List<AbstractCredential> credListSameType = new ArrayList<AbstractCredential>();
		for (Iterator<AbstractCredential> iterator = credList.iterator(); iterator.hasNext();) {
			AbstractCredential abstrCred = (AbstractCredential) iterator.next();
			if (abstrCred instanceof ApiKeyCredential) {
				if (credType.equals(CredentialType.API_KEY)) {
					credListSameType.add(abstrCred);
				}
			} else if (abstrCred instanceof BearerTokenCredential) {
				if (credType.equals(CredentialType.BEARER_TOKEN)) {
					credListSameType.add(abstrCred);
				}
			} else if (abstrCred instanceof UserPasswordCredential) {
				if (credType.equals(CredentialType.USERNAME_PASSWORD)) {
					credListSameType.add(abstrCred);
				}
			}
		}
		return credListSameType;
	}
	
	/**
	 * Gets the credential with the specified name.
	 *
	 * @param credName the credential name
	 * @return the credential with name, null if the credential with the specific name could not be found
	 */
	public AbstractCredential getCredentialWithName(String credName) {
		List<AbstractCredential> credList = getCredentialList();
		System.out.println("Here");
		for (Iterator<AbstractCredential> iterator = credList.iterator(); iterator.hasNext();) {
			AbstractCredential abstrCred = (AbstractCredential) iterator.next();
			if (credName.equals(abstrCred.getName())) {
				return abstrCred;
			} 
		}
		return null;
	}
	
	/**
	 * Gets the credential with ID.
	 *
	 * @param ID the id
	 * @return the credential with ID
	 */
	public AbstractCredential getCredentialWithID(Integer ID) {
		List<AbstractCredential> credList = getCredentialList();
		for (Iterator<AbstractCredential> iterator = credList.iterator(); iterator.hasNext();) {
			AbstractCredential abstrCred = (AbstractCredential) iterator.next();
			if (ID.equals(abstrCred.getID())) {
				return abstrCred;
			} 
		}
		return null;
	}
	
	// ------------------------------------------------------------
	// --- From here access methods for AwbApiRegistration --------
	// ------------------------------------------------------------
	
	/**
	 * Get the {@link ApiRegistration} representation from a {@link AwbApiRegistrationService} of the ApiRegistrationList
	 * @param awbApiRegService
	 * @return <code>null</code> if there is no {@link ApiRegistration} representation of given {@link AwbApiRegistrationService else the corresponding ApiRegistration instance
	 */
	public ApiRegistration getApiRegistrationFromService(ApiRegistration apiReg) {
		ApiRegistration sameObject = null;
		for (int i = 0; i < apiRegistrationServiceList.size(); i++) {
			AwbApiRegistrationService awbApiRegService = apiRegistrationServiceList.get(i);
			if (apiReg.getClientBundleName().equals(awbApiRegService.getClientBundleName())) {
				if (apiReg.getCredentialType().equals(awbApiRegService.getCredentialType())) {
					if (apiReg.getDefaultCredentialName().equals(awbApiRegService.getDefaultCredentialName())) {
						if (apiReg.getDescription().equals(awbApiRegService.getDescription())) {
							if (apiReg.getDefaultURL().equals(awbApiRegService.getDefaultURL())) {
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
	
	// ----------------------------------------------------------------------------------
	// --- From here methods to save or load a WsCredentialStore -----------------------
	// ----------------------------------------------------------------------------------
	
	
	/**
	 * Gets the WsCredentialStore-File
	 * 
	 * @param project the current project
	 * @return File of WsCredentialStoree
	 */
	public static File getWsCredentialStoreFile() {

		File wsCredentialFile = null;
		Project project = Application.getProjectFocused();
		if (project!=null) {
			// --- File in currently opened Project -------
			wsCredentialFile = new File(project.getProjectFolderFullPath() + WS_CREDENTIAL_STORE_FILE);
		} else {
			// --- File in AWB property folder ------------
			wsCredentialFile = new File(BundleHelper.getPathProperties() + WS_CREDENTIAL_STORE_FILE);
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
	 * @param wsCredentialStore the ws credential store
	 * @return true, if successful
	 */
	public static boolean save(WsCredentialStore wsCredentialStore) {
		return save(wsCredentialStore, getWsCredentialStoreFile());
	}
	
	/**
	 * Saves the specified WsCredentialStore to the specified file.
	 *
	 * @param wsCredentialStore the ws credential store
	 * @param file              the file
	 * @return true, if successful
	 */
	public static boolean save(WsCredentialStore wsCredentialStore, File file) {
		
		boolean success = true;

		// --- Check the JettyConfiguration instance ------
		if (wsCredentialStore == null) {
			System.err.println("[" + WsCredentialStore.class.getSimpleName()+ "] No WsCredentialStore instance was specified to be saved!");
			return false;
		}

		// --- Where to save? -----------------------------
		if (file == null) {
			System.err.println("[" + WsCredentialStore.class.getSimpleName()+ "] The path for saving a configuration as XML file is not allowed to be null!");
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
		System.out.println(file.getAbsolutePath());
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.common.Observer#update(de.enflexit.common.Observable,
	 * java.lang.Object)
	 */
	public void update(Observable observable, Object updateObject) {

		if (!(observable instanceof Project)) return;
		
		if (updateObject.equals(Project.PREPARE_FOR_SAVING)) {
			save(this);
		} 
	}
}