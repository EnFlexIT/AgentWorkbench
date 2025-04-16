package de.enflexit.awb.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ApplicationListener;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;
import de.enflexit.common.AbstractUserObject;
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
	    "credentialList",
	    "cacheCredentialAssignmentList"
	})
public class WsCredentialStore implements ApplicationListener,Serializable {
	
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
			Application.addApplicationListener(instance);
		} else {
			if (WsCredentialStore.getWsCredentialStoreFile().equals(credentialStoreFile)==false) {
				WsCredentialStore.save(instance, credentialStoreFile);
				instance = WsCredentialStore.load(credentialStoreFile);
				if(instance==null) {
					instance=new WsCredentialStore();
				}
			}
			Application.addApplicationListener(instance);
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
	
	@XmlElementWrapper(name="credentialAssignments")
	@XmlElement(name="credentialAssignment")
	private List<CredentialAssignment> credentialAssignmentList;
	@XmlElement(name="cachedCredentialAssignments")
	private List<CredentialAssignment> cacheCredentialAssignmentList;
	@XmlTransient
	private Map<String,List<CredentialAssignment>> bundleCredAssgns;

	@XmlTransient
	private List<AwbApiRegistrationService> apiRegistrationServiceList;
	
	
	/**
	 * Returns the api registration service list.
	 * 
	 * @return the api registration service list
	 */
	public synchronized List<AwbApiRegistrationService> getApiRegistrationServiceList() {
		if (apiRegistrationServiceList == null) {
			apiRegistrationServiceList = new ArrayList<>();
		}
		apiRegistrationServiceList.clear();
		// --- Fill the list ------------------------------------
		List<String> defaultClientBundleNames= new ArrayList<>();
		List<AwbApiRegistrationService> registeredServiceList = ServiceFinder.findServices(AwbApiRegistrationService.class);
		for (int i = 0; i < registeredServiceList.size(); i++) {
			apiRegistrationServiceList.add(registeredServiceList.get(i));
			defaultClientBundleNames.add(registeredServiceList.get(i).getClientBundleName());
		}
		//Check if a service was deleted
	    List<CredentialAssignment> credAssgn= this.getCredentialAssignmentList();
	    for (Iterator<CredentialAssignment> iterator = credAssgn.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			if(!defaultClientBundleNames.contains(credentialAssignment.getIdApiRegistrationDefaultBundleName())) {
				//Cache the changes until they are deleted by the user
				this.getCacheCredentialAssignmentList().add(credentialAssignment);
		        this.putInBundleCredAssgnMap(credentialAssignment);
			}
		}
	    this.getCredentialAssignmentList().removeAll(this.getCacheCredentialAssignmentList());
	    //Check if a service was deleted and is now added again
	    List<CredentialAssignment> credAssgnCache= this.getCacheCredentialAssignmentList();
	 		    for (Iterator<CredentialAssignment> iteratorCredAssgn = credAssgnCache.iterator(); iteratorCredAssgn.hasNext();) {
				CredentialAssignment credentialAssignment = (CredentialAssignment) iteratorCredAssgn.next();
				if(defaultClientBundleNames.contains(credentialAssignment.getIdApiRegistrationDefaultBundleName())) {
					//Reactivate the cached CredentialAssignment
					this.getCredentialAssignmentList().add(credentialAssignment);
			        this.getBundleCredAssgnsMap().remove(credentialAssignment.getIdApiRegistrationDefaultBundleName(), this.getBundleCredAssgnsMap().get(credentialAssignment.getIdApiRegistrationDefaultBundleName()));
				}
			}
	 		this.getCacheCredentialAssignmentList().removeAll(this.getCredentialAssignmentList());
		
		return apiRegistrationServiceList;
	}
	
	/**
	 * Put in bundle cred assgn list.
	 *
	 * @param ca the {@link CredentialAssignment}
	 */
	public void putInBundleCredAssgnMap(CredentialAssignment ca) {
		Map<String, List<CredentialAssignment>> bundleCredAssgnMap=this.getBundleCredAssgnsMap();
		if(bundleCredAssgnMap.get(ca.getIdApiRegistrationDefaultBundleName())== null) {
			List<CredentialAssignment> credAssgn=new ArrayList<CredentialAssignment>();
			credAssgn.add(ca);
		    bundleCredAssgnMap.put(ca.getIdApiRegistrationDefaultBundleName(), credAssgn);
		}else {
			List<CredentialAssignment> credAssgn=bundleCredAssgnMap.get(ca.getIdApiRegistrationDefaultBundleName());
			if(!credAssgn.contains(ca)) {
				credAssgn.add(ca);
			}
		    bundleCredAssgnMap.put(ca.getIdApiRegistrationDefaultBundleName(), credAssgn);
		}
		
	}
	
	/**
	 * Gets the Bundle CredentialAssignment map. Maps clientBundleName with its corresponding CredentialAssignments
	 *
	 * @return the Bundle CredentialAssignment map
	 */
	public Map<String, List<CredentialAssignment>> getBundleCredAssgnsMap() {
		if(this.bundleCredAssgns==null) {
			bundleCredAssgns=new HashMap<String,List<CredentialAssignment>>();
			if(this.getCacheCredentialAssignmentList().size()>0) {
				for (Iterator<CredentialAssignment> iterator = this.getCacheCredentialAssignmentList().iterator(); iterator.hasNext();) {
					CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
					this.putInBundleCredAssgnMap(credentialAssignment);
				}
			}
		}
		return bundleCredAssgns;
	}
	/**
	 * Adds the api registration service.
	 *
	 * @param apiRegService the api reg service
	 */
	public synchronized void addApiRegistrationService(AwbApiRegistrationService apiRegService) {
		AwbApiRegistrationService apiReg=this.getApiRegistration(apiRegService.getClientBundleName());
		if(apiReg==null) {
			this.getApiRegistrationServiceList().add(apiRegService);
			this.setupApiRegistrationService(apiRegService);
		}else {
			this.setupApiRegistrationService(apiRegService);
		}
	}

	/**
	 * Sets the up api registration service.
	 *
	 * @param apiRegService the new up api registration service
	 */
	private void setupApiRegistrationService(AwbApiRegistrationService apiRegService) {
		 AbstractCredential cred=this.getSpecifiedCredFromApiRegService(apiRegService);
		 ServerURL serverUrl=this.getServerOrCreateIt(apiRegService);	
		
			if (cred == null) {

				// Check if a former credential of the bundle is still stored in the WsCredentialStore
				
				List<AbstractCredential> allCredOfTypeOfService = this.getAllCredentialsOfaType(apiRegService.getCredentialType());
				for (Iterator<AbstractCredential> iterator = allCredOfTypeOfService.iterator(); iterator.hasNext();) {
					AbstractCredential abstractCredential = (AbstractCredential) iterator.next();
					if (abstractCredential.getName().contains(apiRegService.getClientBundleName())) {
						cred = abstractCredential;
						break;
					}
				}
				
				// Create empty credential if no former credential could be found
				if (cred == null) {
					cred = createEmptyCredential(apiRegService);
					this.putInCredentialList(cred);
				}
				
				//Init the default CredentialAssignment
				if (cred != null) {
					CredentialAssignment ca = new CredentialAssignment();
					ca.setIdApiRegistrationDefaultBundleName(apiRegService.getClientBundleName());
					ca.setIdCredential(cred.getID());
					ca.setIdServerURL(serverUrl.getID());
					this.putCredAssgnInCredAssgnList(ca);
				}
			
		 }else {
				if(this.putInCredentialList(cred)){
					CredentialAssignment ca = new CredentialAssignment();
					ca.setIdApiRegistrationDefaultBundleName(apiRegService.getClientBundleName());
					ca.setIdCredential(cred.getID());
					ca.setIdServerURL(serverUrl.getID());
					this.putCredAssgnInCredAssgnList(ca);
				}
			}
		 this.save();
	}

	/**
	 * Removes the awb api registration service.
	 *
	 * @param apiRegService the api reg service
	 */
	public void removeAwbApiRegistrationService(AwbApiRegistrationService apiRegService) {
		if(this.getApiRegistrationServiceList().contains(apiRegService)){
			this.getApiRegistrationServiceList().remove(apiRegService);
		}
		
		ServerURL server=this.getServerURL(apiRegService.getDefaultServerURL());
		if(server!=null) {
			List<CredentialAssignment>credAssgns=this.getCredentialAssignmentWithServer(server);
			this.getCredentialAssignmentList().removeAll(credAssgns);
		    this.save();
		}
	}

	/**
	 * Returns the api registration.
	 *
	 * @param bundleName the bundle name
	 * @return the api registration
	 */
	public AwbApiRegistrationService getApiRegistration(String bundleName) {
		if (bundleName==null || bundleName.isBlank()==true) return null;
		for (Iterator<AwbApiRegistrationService> iterator = this.getApiRegistrationServiceList().iterator(); iterator.hasNext();) {
			AwbApiRegistrationService apiReg = (AwbApiRegistrationService) iterator.next();
			if (apiReg.getClientBundleName().equals(bundleName)) {
				return apiReg;
			}
		}
		return null;
	}
	
	/**
	 * Gets the cache credential assignment list.
	 *
	 * @return the cache credential assignment list
	 */
	public List<CredentialAssignment> getCacheCredentialAssignmentList() {
		if(cacheCredentialAssignmentList==null) {
			cacheCredentialAssignmentList=new ArrayList<CredentialAssignment>();
		}
		return cacheCredentialAssignmentList;
	}

	/**
	 * Sets the cache credential assignment list.
	 *
	 * @param cacheCredentialAssignmentList the new cache credential assignment list
	 */
	public void setCacheCredentialAssignmentList(List<CredentialAssignment> cacheCredentialAssignmentList) {
		this.cacheCredentialAssignmentList = cacheCredentialAssignmentList;
	}
	
	
	/**
	 * Returns the server URL list.
	 * @return the server URL list
	 */
	public synchronized List<ServerURL> getServerURLList() {
		if (serverURLList==null) {
			serverURLList = new ArrayList<>();
		}
		return serverURLList;
	}
	/**
	 * Sets the server URL list.
	 * @param serverURLList the new server URL list
	 */
	public synchronized void setServerURLList(List<ServerURL> serverURLList) {
		this.serverURLList = serverURLList;
	}
	

	/**
	 * Checks if a {@link ServerURL} is in the server url list.
	 *
	 * @param serverURL the server URL
	 * @return true, if is in server url list
	 */
	public boolean isInServerUrlList(String serverURL) {
		boolean inServerUrlList= false;
		List<ServerURL> serverURLs=getServerURLList();
		for (ServerURL server : serverURLs) {
		    if(server.getServerURL().equals(serverURL)) {
		    	inServerUrlList=true;
		    	break;
		    }
		}
        
		return inServerUrlList;
	}
	
	/**
	 * Gets the {@link ServerURL} or create it.
	 *
	 * @param apiRegService the {@link AwbApiRegistrationService}
	 * @return the server or create it
	 */
	private synchronized ServerURL getServerOrCreateIt(AwbApiRegistrationService apiRegService) {
		ServerURL apiRegServer;
		if(!isInServerUrlList(apiRegService.getDefaultServerURL())) {
			apiRegServer= new ServerURL();
			apiRegServer.setServerURL(apiRegService.getDefaultServerURL());
			this.getServerURLList().add(apiRegServer);
		}else {
			apiRegServer=this.getServerURL(apiRegService.getDefaultServerURL());
		}
		return apiRegServer;
	}
	
	/**
	 * Gets the server URL.
	 *
	 * @param url the url
	 * @return the server URL
	 */
	public synchronized ServerURL getServerURL(String url) {
		if (url==null || url.isBlank()==true) return null;
		for (ServerURL serverURL : this.getServerURLList()) {
			if (serverURL.getServerURL().equals(url)) {
				return serverURL;
			}
		}
		return null;
	}
	
	/**
	 * Gets the server URL.
	 *
	 * @param url the url
	 * @return the server URL
	 */
	public synchronized ServerURL getServerURL(UUID id) {
		if (id==null) return null;
		for (ServerURL serverURL : this.getServerURLList()) {
			if (serverURL.getID().equals(id)) {
				return serverURL;
			}
		}
		return null;
	}
	
	/**
	 * Returns the credential list.
	 * @return the credential list
	 */
	public synchronized List<AbstractCredential> getCredentialList() {
		if (credentialList==null) {
			credentialList = new ArrayList<>();
		}
		return credentialList;
	}
	/**
	 * Sets the credential list.
	 * @param credentialList the new credential list
	 */
	public synchronized void setCredentialList(List<AbstractCredential> credentialList) {
		this.credentialList = credentialList;
	}
	
	/**
	 * Returns the credential assignment list.
	 * @return the credential assignment list
	 */
	public synchronized List<CredentialAssignment> getCredentialAssignmentList() {
		if (credentialAssignmentList==null) {
			credentialAssignmentList = new ArrayList<>();
		}
		return credentialAssignmentList;
	}
	/**
	 * Sets the credential assignment list.
	 * @param credentialAssignmentList the new credential assignment list
	 */
	public synchronized void setCredentialAssignmentList(List<CredentialAssignment> credentialAssignmentList) {
		this.credentialAssignmentList = credentialAssignmentList;
	}
	
	
	// ----------------------------------------------------
	// --- From here access methods for credentials --------
	// ----------------------------------------------------

	/**
	 * Returns the credential for the specified and registered {@link AwbApiRegistrationService}.
	 * Search first for the Credential with the default name defined in the {@link AwbApiRegistrationService}. 
	 * If this credential is not suitable (for instance empty, other type needed or null), it searches for other assigned credential, which is suitable.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param apiRegistrationClass the api registration
	 * @return the credential
	 * @throws IllegalArgumentException the illegal argument exception if the credential is empty
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCredential(T type, Class<? extends AwbApiRegistrationService> apiRegistrationClass) throws IllegalArgumentException{
		
		List<AwbApiRegistrationService> registeredServiceList = ServiceFinder.findServices(AwbApiRegistrationService.class);
		for (int i = 0; i < registeredServiceList.size(); i++) {
			AwbApiRegistrationService serviceInstance = registeredServiceList.get(i);
			if (serviceInstance.getClass().equals(apiRegistrationClass)) {
				
				String clientBundleName = serviceInstance.getClientBundleName();
				String defaultURL = serviceInstance.getDefaultServerURL();
				String defaultCredentialName = serviceInstance.getDefaultCredentialName();
				
				AbstractCredential credentiaLFound = this.getCredential(defaultCredentialName);
				if (credentiaLFound==null) {
			     credentiaLFound=this.getCredential(type, clientBundleName, defaultURL, defaultCredentialName);
				}
				if(credentiaLFound.isEmpty()) {
					throw new IllegalArgumentException("The "+ credentiaLFound.getName()+" is empty, it needs to be filled!");
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
	public AbstractCredential getCredential(UUID id) {
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
	 * Tries to get the Credential by the defaultCredentialName. If this is not possible creates an empty credential and assigns it to the clientBundle-.
	 *
	 * @param clientBundle          the client bundle
	 * @param serverURL             the server URL
	 * @param defaultCredentialName the default credential name
	 * @return the credential
	 */
	public <T> AbstractCredential getCredential(T type, String clientBundleName, String defaultURL,
			String defaultCredentialName) {

		AbstractCredential credentiaLFound = this.getCredential(defaultCredentialName);
		if (credentiaLFound == null) {
				AwbApiRegistrationService apiRegistration = this.getApiRegistration(clientBundleName);
				ServerURL serverURL = this.getServerURL(defaultURL);
			
					  if(apiRegistration!=null) {
						   if(serverURL!=null) {
							   //Checks if credential which is suitable already exists and is already assigned to the clientBundle
							    List<CredentialAssignment> credAssgns=this.getCredentialAssignmentWithServer(serverURL);
							    
							    for (Iterator<CredentialAssignment> iterator = credAssgns.iterator(); iterator.hasNext();) {
									CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
									if(credentialAssignment.getIdApiRegistrationDefaultBundleName().equals(apiRegistration.getClientBundleName())) {
										AbstractCredential credAssgn=this.getCredential(credentialAssignment.getIdCredential());
										if(credAssgn.getCredentialType().equals(apiRegistration.getCredentialType())){
											credentiaLFound=credAssgn;
											break;
										}
									}
								}		   
					   }else {
						    // --- Case serverUrl is null
						   
						    // --- Define the server URL
							serverURL = new ServerURL(defaultURL);
							if (this.getServerURLList().contains(serverURL) == false) {
								this.getServerURLList().add(serverURL);
							}
					    }
					 }	
		
		// Case: No suitable credential could be found, create an empty one and assign it to the clientBundle
		if (credentiaLFound == null) {	
			
				// --- Define a empty credential of the same type, which can be filled by the user
				credentiaLFound = createCredential(type, clientBundleName, defaultURL);
				if (this.getCredentialList().contains(credentiaLFound) == false) {
					this.getCredentialList().add(credentiaLFound);
				}

				// --- Define the credential assignment for a clear hierarchy
				if (apiRegistration != null && serverURL != null && credentiaLFound != null) {
					CredentialAssignment credAssgn = new CredentialAssignment();
					credAssgn.setIdServerURL(serverURL.getID());
					credAssgn.setIdApiRegistrationDefaultBundleName(apiRegistration.getClientBundleName());
					credAssgn.setIdCredential(credentiaLFound.getID());
					this.getCredentialAssignmentList().add(credAssgn);
					this.save();
				} else {
					credentiaLFound = null;
				}
			}
		}
		return credentiaLFound;
	}
	
	/**
	 * Checks if the given {@link AbstractCredential} is in the CredentialList
	 * If not the credential will be added to the list.
	 *
	 * @param abstrCred corresponding {@link AbstractCredential}
	 * @return true, if successful
	 */
	public boolean putInCredentialList(AbstractCredential abstrCred) {
        if(!this.getCredentialList().contains(abstrCred)) {
        	this.getCredentialList().add(abstrCred);
        	return true;
        }
        return false;
	}
	

	/**
	 * Updates a credential in credential list during runtime. The updatedCred must be initialized, else nothing happens.
	 *
	 * @param updatedCred the updated credential
	 */
	public void updateCredentialInCredentialList(AbstractCredential updatedCred) {
        	if(updatedCred!=null) {
        		if(this.getCredentialList().contains(updatedCred)) {
        			this.getCredentialList().remove(updatedCred);
					this.getCredentialList().add(updatedCred);
        		}else {
        		    this.putInCredentialList(updatedCred);
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
	public AbstractCredential getCredentialWithID(UUID Id) {
		List<AbstractCredential> credList = getCredentialList();
		for (Iterator<AbstractCredential> iterator = credList.iterator(); iterator.hasNext();) {
			AbstractCredential abstrCred = (AbstractCredential) iterator.next();
			if (Id.equals(abstrCred.getID())) {
				return abstrCred;
			} 
		}
		return null;
	}
	
	/**
	 * Creates an empty {@link AbstractCredential} based on information of the {@link AwbApiRegistrationService}.
	 *
	 * @param apiRegService the api reg service
	 * @return the abstract credential
	 */
	private AbstractCredential createEmptyCredential(AwbApiRegistrationService apiRegService) {
		AbstractCredential emptyCred=null;
		 switch (apiRegService.getCredentialType()) {
		case API_KEY:
			emptyCred=new ApiKeyCredential();
			this.giveCredentialUniqueName(emptyCred,apiRegService.getDefaultCredentialName());
			break;
		case BEARER_TOKEN:
			emptyCred= new BearerTokenCredential();
			this.giveCredentialUniqueName(emptyCred,apiRegService.getDefaultCredentialName());
			break;
		case USERNAME_PASSWORD:
			emptyCred= new UserPasswordCredential();
			this.giveCredentialUniqueName(emptyCred,apiRegService.getDefaultCredentialName());
			break;

		default:
			break;
		}
		 return emptyCred;
	}
	
	/**
	 * Give a {@link AbstractCredential} unique name on basis of the string name.
	 *
	 * @param cred the cred
	 * @param name the name should not be empty. Should be the basis for refactoring of the name, if it is already in use.
	 * @return the abstract credential
	 */
	public AbstractCredential giveCredentialUniqueName(AbstractCredential cred,String name) {
		int cnt=1;
		while(this.isCredentialnameAlreadyUsed(name)){
		   name="["+cnt+"]"+ name; 
		}
		cred.setName(name);
		return cred;
	}
	
	/**
	 * Check if a name of a {@link AbstractCredential} is already used.
	 *
	 * @param name the name of a {@link AbstractCredential}
	 * @return true, if it is already in use
	 */
	public boolean isCredentialnameAlreadyUsed(String name) {
		List<AbstractCredential> allCreds=WsCredentialStore.getInstance().getCredentialList();
		for (Iterator<AbstractCredential> iterator = allCreds.iterator(); iterator.hasNext();) {
			AbstractCredential abstractCredential = (AbstractCredential) iterator.next();
			if(abstractCredential.getName().equals(name)) {
				return true;
			}
		}
		return false;
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
		for (int i = 0; i < this.getApiRegistrationServiceList().size(); i++) {
			AwbApiRegistrationService awbApiRegService = this.getApiRegistrationServiceList().get(i);
			if (apiReg.getClientBundleName().equals(awbApiRegService.getClientBundleName())) {
				if (apiReg.getCredentialType().equals(awbApiRegService.getCredentialType())) {
					if (apiReg.getDefaultCredentialName().equals(awbApiRegService.getDefaultCredentialName())) {
						if (apiReg.getDescription().equals(awbApiRegService.getDescription())) {
							if (apiReg.getDefaultURL().equals(awbApiRegService.getDefaultServerURL())) {
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
	
	// ------------------------------------------------------------
	// --- From here methods for CredentialAssignment --------
	// ------------------------------------------------------------

	/**
	 * Gets one unique credential assignment.
	 *
	 * @param apiReg    the api reg
	 * @param cred      the credn
	 * @param serverURL the server URL
	 * @return the credential assignment
	 */
	public CredentialAssignment getCredentialAssignment(ApiRegistration apiReg, AbstractCredential cred,
			ServerURL serverURL) {
		CredentialAssignment credAssigned = null;
		List<CredentialAssignment> credentialAssignments = getCredentialAssignmentList();
		for (CredentialAssignment credAssgn : credentialAssignments) {
			if (credAssgn.getIdApiRegistrationDefaultBundleName() != null && credAssgn.getIdCredential() != null
					&& credAssgn.getIdServerURL() != null) {
				if (credAssgn.getIdApiRegistrationDefaultBundleName().equals(apiReg.getClientBundleName())) {
					if (credAssgn.getIdCredential().equals(cred.getID())) {
						if (credAssgn.getIdServerURL().equals(serverURL.getID())) {
							credAssigned = credAssgn;
						}
					}
				}
			}
		}
		return credAssigned;
	}
	
	/**
	 * Gets the credential assignments with one credential.
	 *
	 * @param apiReg the api reg
	 * @param cred the cred
	 * @return the credential assignments with one credential
	 */
	public List<CredentialAssignment> getCredentialAssignmentsWithOneCredential(ApiRegistration apiReg,AbstractCredential cred) {
		List<CredentialAssignment> credentialAssignments=new ArrayList<CredentialAssignment>();
		for (CredentialAssignment credAssgn : this.getCredentialAssignmentList()) {
			if(credAssgn.getIdApiRegistrationDefaultBundleName().equals(apiReg.getClientBundleName())){
				if(credAssgn.getIdCredential().equals(cred.getID())) {
					credentialAssignments.add(credAssgn);
				}
			}
		}
		return credentialAssignments;
	}
	
	/**
	 * Gets the credential assignment with credential.
	 *
	 * @param apiReg the api reg
	 * @param cred the cred
	 * @return the credential assignment with credential
	 */
	public List<CredentialAssignment> getCredentialAssignmentWithCredential(AbstractCredential cred) {
		List<CredentialAssignment> credentialAssignments=new ArrayList<CredentialAssignment>();
		for (CredentialAssignment credAssgn : this.getCredentialAssignmentList()) {
			if(credAssgn.getIdCredential().equals(cred.getID())){
					credentialAssignments.add(credAssgn);
			}
		}
		return credentialAssignments;
	}
	
	/**
	 * Gets the credential assignment with credential.
	 *
	 * @param apiReg the api reg
	 * @param cred the cred
	 * @return the credential assignment with credential
	 */
	public List<CredentialAssignment> getCredentialAssignmentWithServer(ServerURL server) {
		List<CredentialAssignment> credentialAssignments=new ArrayList<CredentialAssignment>();
		for (Iterator<CredentialAssignment> iterator = this.getCredentialAssignmentList().iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssgn = (CredentialAssignment) iterator.next();
			if(credentialAssgn.getIdServerURL().equals(server.getID())){
					credentialAssignments.add(credentialAssgn);
			}
		}
		return credentialAssignments;
	}
	
	/**
	 * Put the {@link CredentialAssignment} in the CredentialAssignmentList.
	 *
	 * @param credAssgn the cred assgn
	 * @return true, if successful 
	 *         false, if {@link CredentialAssignment} is already in the CredentialAssignmentList
	 */
	public boolean putCredAssgnInCredAssgnList(CredentialAssignment credAssgn) {
	 boolean put=false;
	 if(!isCredAssignmentInCredAssgnList(credAssgn)) {
		 this.getCredentialAssignmentList().add(credAssgn);
		put=true;
	 }
	 return put;
	}
	
	/**
	 * Checks if is cred assignment in cred assgn list.
	 *
	 * @param credAssgn the cred assgn
	 * @return true, if is cred assignment in cred assgn list
	 */
	public boolean isCredAssignmentInCredAssgnList(CredentialAssignment credAssgn) {
		boolean contains =false;
	    List<CredentialAssignment> credAssignments=this.getCredentialAssignmentList();
	    for (Iterator<CredentialAssignment> iterator = credAssignments.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			if(credentialAssignment.getIdApiRegistrationDefaultBundleName().equals(credAssgn.getIdApiRegistrationDefaultBundleName())) {
				if(credentialAssignment.getIdCredential().equals(credAssgn.getIdCredential())) {
					if(credentialAssignment.getIdServerURL().equals(credAssgn.getIdServerURL())) {
						contains= true;
						break;
					}
				}
			}
		}
		return contains;
	}
	
	/**
	 * Gets the specified {@link AbstractCredential} from
	 * {@link AwbApiRegistrationService}.
	 *
	 * @param apiRegService the {@link AwbApiRegistrationService}
	 * @return the specified {@link AbstractCredential} from the
	 *         {@link AwbApiRegistrationService}
	 */
	private AbstractCredential getSpecifiedCredFromApiRegService(AwbApiRegistrationService apiRegService) {
		AbstractCredential specifiedCred = null;
		ServerURL apiRegServer = getServerURL(apiRegService.getDefaultServerURL());
		if (apiRegServer != null) {
			List<CredentialAssignment> crdAssgnList = this.getCredentialAssignmentWithServer(apiRegServer);
			for (CredentialAssignment credentialAssignment : crdAssgnList) {
				AbstractCredential cred = this.getCredential(credentialAssignment.getIdCredential());
				if (cred != null) {
					if (cred.getCredentialType().equals(apiRegService.getCredentialType())) {
						specifiedCred = cred;
					}
				}
			}
		}
		return specifiedCred;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here methods for cached credentials-------------- -----------------------
	// ----------------------------------------------------------------------------------
	
	/**
	 * Gets the cached credential assignment of a bundle with one credential.
	 *
	 * @param bundleName the bundle name
	 * @param cred the cred
	 * @return the cached credential assignment with one credential
	 */
	public List<CredentialAssignment> getCachedCredentialAssignmentOfABundleWithOneCredential(String bundleName,AbstractCredential cred) {
		List<CredentialAssignment> credentialAssignments=new ArrayList<CredentialAssignment>();
		for (CredentialAssignment credAssgn : this.getCredentialAssignmentList()) {
			if(credAssgn.getIdApiRegistrationDefaultBundleName().equals(bundleName)){
				if(credAssgn.getIdCredential()==cred.getID()) {
					credentialAssignments.add(credAssgn);
				}
			}
		}
		return credentialAssignments;
	}
	
	/**
	 * Gets the cached credential assignments with one credential.
	 *
	 * @param cred the cred
	 * @return the cached credential assignments with one credential
	 */
	public List<CredentialAssignment> getCachedCredentialAssignmentsWithOneCredential(AbstractCredential cred) {
		List<CredentialAssignment> credentialAssignments=new ArrayList<CredentialAssignment>();
		for (CredentialAssignment credAssgn : this.getCacheCredentialAssignmentList()) {
				if(credAssgn.getIdCredential().equals(cred.getID())) {
					credentialAssignments.add(credAssgn);
				}
		}
		return credentialAssignments;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here methods to save or load a WsCredentialStore -----------------------
	// ----------------------------------------------------------------------------------
	
	/**
	 * Reset and reload the {@link WsCredentialStore}.
	 *
	 * @return the file
	 */
	public synchronized void resetAndReloadWsCredStore() {
		credentialStoreFile = WsCredentialStore.getWsCredentialStoreFile();
		instance = WsCredentialStore.load(credentialStoreFile);
		if(instance==null) {
			instance=new WsCredentialStore();
		}
		this.getApiRegistrationServiceList();
		this.getCredentialAssignmentList();
		this.getCredentialList();
		this.getServerURLList();
		
	}
	
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
	private static boolean save(WsCredentialStore wsCredentialStore) {
		return save(wsCredentialStore, getWsCredentialStoreFile());
	}
	
	/**
	 * Saves the specified WsCredentialStore to the specified file.
	 *
	 * @param wsCredentialStore the ws credential store
	 * @param file              the file
	 * @return true, if successful
	 */
	private static boolean save(WsCredentialStore wsCredentialStore, File file) {
		
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

	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		if (event.getApplicationEvent().equals(ApplicationEvent.PROJECT_CLOSED)) {
             this.resetAndReloadWsCredStore();
		} else if (event.getApplicationEvent().equals(ApplicationEvent.PROJECT_LOADED)) {
             //TODO:Check if something needs to be done here
		} else if (event.getApplicationEvent().equals(ApplicationEvent.PROJECT_FOCUSED)) {
			this.resetAndReloadWsCredStore();
		}
	}
}