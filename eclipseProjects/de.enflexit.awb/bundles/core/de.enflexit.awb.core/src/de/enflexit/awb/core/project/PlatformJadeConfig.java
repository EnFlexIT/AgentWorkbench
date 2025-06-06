package de.enflexit.awb.core.project;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.awb.core.jade.NetworkAddresses;
import de.enflexit.awb.core.jade.NetworkAddresses.NetworkAddress;
import de.enflexit.awb.core.jade.PortChecker;
import de.enflexit.awb.simulation.LoadService;
import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.logging.DebugService;
import de.enflexit.language.Language;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * With this class, the Profile of a new JADE-Container can be configured.
 * To use this class, just create a new instance of it and go throw 
 * configurations like in the example below.<br>
 * After configuration you can use the method 'getNewInstanceOfProfilImpl()'
 * which returns a new Instance of 'jade.core.Profile'. This can be used to 
 * create a new JADE-Container.<br>
 * <br>
 * EXAMPLE:<br>
 * <blockquote><code>
 *  PlatformJadeConfig pjc = new PlatformJadeConfig();<br>
 *	pjc.setLocalPort(1099);<br>
 *	pjc.addService(PlatformJadeConfig.SERVICE_AgentGUI_LoadService);<br>
 *  pjc.addService(PlatformJadeConfig.SERVICE_AgentGUI_SimulationService);<br>
 *  pjc.addService(PlatformJadeConfig.SERVICE_NotificationService);<br>
 *  <br>
 *	Profile profile = pjc.getNewInstanceOfProfilImpl();<br>
 * </code></blockquote>
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PlatformJadeConfig implements Serializable {
	
	private static final long serialVersionUID = -9062155032902746361L;
	
	private static final boolean debug = false;
	
	/**The enumeration MTP_Creation describes the possibilities, how the MTP-address can be configured. */
	public static enum MTP_Creation {
		ConfiguredByJADE,
		ConfiguredByIPandPort
	}
	public static String MTP_IP_AUTO_Config = "Auto-Configuration";
	
	// --- Services 'Activated automatically' ---------------------------------
	public static final String SERVICE_MessagingService = jade.core.messaging.MessagingService.class.getName();
	public static final String SERVICE_AgentManagementService = jade.core.management.AgentManagementService.class.getName();
	
	// --- Services 'Active by default' ---------------------------------------
	public static final String SERVICE_AgentMobilityService = jade.core.mobility.AgentMobilityService.class.getName();
	public static final String SERVICE_NotificationService = jade.core.event.NotificationService.class.getName(); 
	
	// --- Services 'Inactive by default' -------------------------------------
	public static final String SERVICE_MainReplicationService = jade.core.replication.MainReplicationService.class.getName();
	public static final String SERVICE_FaultRecoveryService = jade.core.faultRecovery.FaultRecoveryService.class.getName();
	public static final String SERVICE_AddressNotificationService = jade.core.replication.AddressNotificationService.class.getName();
	public static final String SERVICE_TopicManagementService = jade.core.messaging.TopicManagementService.class.getName();
	public static final String SERVICE_PersistentDeliveryService = jade.core.messaging.PersistentDeliveryService.class.getName();
	public static final String SERVICE_UDPNodeMonitoringServ = jade.core.nodeMonitoring.UDPNodeMonitoringService.class.getName();
	public static final String SERVICE_BEManagementService = jade.imtp.leap.nio.BEManagementService.class.getName();
	
	// --- Agent.Workbench-Services -------------------------------------------
	public static final String SERVICE_DebugService = de.enflexit.awb.simulation.logging.DebugService.class.getName();
	public static final String SERVICE_AgentGUI_LoadService = de.enflexit.awb.simulation.LoadService.class.getName();
	public static final String SERVICE_AgentGUI_SimulationService = de.enflexit.awb.simulation.SimulationService.class.getName();
	
		
	/** Array of services, which will be started with JADE in every case */
	private static final String[] autoServices = {SERVICE_MessagingService, SERVICE_AgentManagementService};
	private static final String AUTOSERVICE_TextAddition = "Startet automatisch !";
	
	// --- Runtime variables -------------------------------------------------- 
	@XmlTransient
	private Project currProject;
	
	@XmlElement(name="skipUserRequestForJadeStart")
	private boolean skipUserRequestForJadeStart;
	
	@XmlElement(name="useLocalPort")	
	private Integer useLocalPort = Application.getGlobalInfo().getJadeLocalPort();
	@XmlElement(name="useLocalMtpPort")
	private Integer useLocalMtpPort = Application.getGlobalInfo().getJadeLocalPortMTP();
	
	@XmlElement(name="mtpCreation")
	private MTP_Creation mtpCreation = MTP_Creation.ConfiguredByJADE;
	@XmlElement(name="mtpIpAddress")
	private String mtpIpAddress = MTP_IP_AUTO_Config;

	@XmlElement(name="mtpProtocol")
	private MtpProtocol mtpProtocol = MtpProtocol.HTTP;
	
	@XmlElement(name="keyStoreFile")
	private String keyStoreFile;
	@XmlElement(name="keyStorePassword")
	private String keyStorePasswordEncrypted;

	@XmlElement(name="trustStoreFile")
	private String trustStoreFile;
	@XmlElement(name="trustStorePassword")
	private String trustStorePasswordEncrypted;
	
	
	@XmlElementWrapper(name = "serviceList")
	@XmlElement(name="service")			
	private ArrayList<String> serviceList;
	@XmlTransient
	private boolean isCorrectedServiceList;
	@XmlTransient
	private DefaultListModel<String> listModelServices;
	
	/**
	 * Default constructor of this class.
	 */
	public PlatformJadeConfig() {
	}
	
	/**
	 * Returns the current project.
	 * @return the project
	 */
	@XmlTransient
	public Project getProject() {
		return this.currProject;
	}
	/**
	 * Sets the current project.
	 * @param project the new project
	 */
	public void setProject(Project project) {
		this.currProject = project;
	}
	/**
	 * Sets the current project changed.
	 */
	private void setProjectChanged() {
		if (this.currProject!=null) {
			this.currProject.setChangedAndNotify(Project.CHANGED_JadeConfiguration);
		}
	}
	
	/**
	 * This method returns the TextAddition if a Service is an automatically starting service of JADE.
	 * @return the auto service text addition
	 */
	public static String getAutoServiceTextAddition() {
		return " " + Language.translate(AUTOSERVICE_TextAddition) + " ";
	}
	/**
	 * Returns if a service generally starts while JADE is starting.
	 * @param serviceReference the service reference
	 * @return true, if is auto service
	 */
	public static boolean isAutoService(String serviceReference) {
		for (int i = 0; i < autoServices.length; i++) {
			if (autoServices[i].equalsIgnoreCase(serviceReference)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This Method returns a new Instance of {@link ProfileImpl} that is used to start JADE-Container.
	 * @return jade.core.Profile
	 */
	public ProfileImpl getNewInstanceOfProfilImpl(){
		ProfileImpl profile = new ProfileImpl();
		if (debug) {
			this.setProfileDumOptions(profile);
		}
		this.setFileDir(profile);
		this.setProfileLocalHost(profile);
		this.setProfileLocalPort(profile);
		this.setProfileLocalPortMTP(profile);
		this.setProfileServices(profile);
		return profile;
	}
	
	/**
	 * Sets the file directory for the files .
	 * @param profile the new file directory
	 */
	private void setFileDir(ProfileImpl profile) {
		String baseDir = Application.getGlobalInfo().getPathBaseDir().toString();
		if (baseDir.endsWith(File.separator)==false) baseDir += File.separator; 
		profile.setParameter(Profile.FILE_DIR, baseDir);
	}
	/**
	 * Adds the local configured DUMP_OPTIONS to the input instance of Profile.
	 * @param profile the profile to work on
	 */
	private void setProfileDumOptions(Profile profile){
		profile.setParameter(Profile.DUMP_OPTIONS, "true");
	}	
	/**
	 * Adds the configured 'LOCAL_HOST' to the input instance of Profile 
	 * in case that the localhost hosts the server.master too. In that case
	 * the platform URL should exactly match the configuration.
	 * @param profile the profile to work on
	 */
	private void setProfileLocalHost(Profile profile){
		if (Application.getGlobalInfo().getJadeUrlConfigurationForMaster().isLocalhost()) {
			profile.setParameter(Profile.LOCAL_HOST, Application.getGlobalInfo().getServerMasterURL());	
		}
	}
	/**
	 * Adds the configured 'LOCAL_PORT' to the input instance of Profile.
	 * @param profile the profile to work on
	 */
	private void setProfileLocalPort(Profile profile){
		Integer freePort = new PortChecker(this.useLocalPort).getFreePort();
		profile.setParameter(Profile.LOCAL_PORT, freePort.toString());
	}
	/**
	 * Adds the configured 'jade_mtp_http_port' to the input instance of Profile.
	 * @param profile the new profile local port mtp
	 */
	private void setProfileLocalPortMTP(ProfileImpl profile) {

		GlobalInfo globalInfo = Application.getGlobalInfo();
		ExecutionMode execMode = globalInfo.getExecutionMode();
		
		// --------------------------------------------------------------------
		// --- Get MTP-settings from global info as default -------------------
		// --------------------------------------------------------------------
		MTP_Creation mtpCreation = globalInfo.getOwnMtpCreation();
		String mtpIpAddress = globalInfo.getOwnMtpIP();
		Integer mtpPort = globalInfo.getOwnMtpPort();
		MtpProtocol mtpProtocol = globalInfo.getMtpProtocol();
		String keyStoreFile = globalInfo.getKeyStoreFile();
		String keyStorePassword = globalInfo.getKeyStorePassword();
		String trustStoreFile = globalInfo.getTrustStoreFile();
		String trustStorePassword = globalInfo.getTrustStorePassword();
		
		// --- Replace configuration by project settings? ---------------------
		if (this.getProject()!=null) {
			mtpCreation = this.getMtpCreation();
			mtpIpAddress = this.getMtpIpAddress();
			mtpPort = this.getLocalPortMTP();
			mtpProtocol = this.getMtpProtocol();
			keyStoreFile = this.getKeyStoreFile();
			keyStorePassword = this.getKeyStorePassword();
			trustStoreFile = this.getTrustStoreFile();
			trustStorePassword = this.getTrustStorePassword();
		}
		
		// --------------------------------------------------------------------
		// --- Apply MTP-settings to profile ----------------------------------
		// --------------------------------------------------------------------
		if (mtpCreation==MTP_Creation.ConfiguredByIPandPort) {

			String ipAddress = null;
			if (mtpIpAddress==null || mtpIpAddress.equals("") || mtpIpAddress.equals(MTP_IP_AUTO_Config)) {
				// --- Auto configuration of the IP address -------------------
				ipAddress = this.getAutomaticIP();
			} else {
				// --- Use configured IP address, if possible -----------------
				if (this.isAvailableNetworkAddress(mtpIpAddress)==false) {
					// --- The configured IP is INVALID -----------------------
					ipAddress = this.getAutomaticIP();
					System.err.println("=> The configured IP address '" + mtpIpAddress +  "' is invalid! The JADE profile will be corrected with the available IP '" + ipAddress + "' instead.");
				} else {
					// --- The configured IP is VALID -------------------------- 
					ipAddress = mtpIpAddress;
				}
			}
			
			// --- Set the MTP address ----------------------------------------
			if (ipAddress!=null) {
				profile.setParameter(Profile.LOCAL_HOST, ipAddress);
				Integer freePort = new PortChecker(mtpPort, ipAddress).getFreePort();
				if (mtpProtocol==MtpProtocol.HTTP) {
					// --- Regular HTTP protocol ------------------------------ 
					profile.setParameter(Profile.MTPS, jade.mtp.http.MessageTransportProtocol.class.getName() + "(http://" + ipAddress + ":" + freePort + "/acc)");
					profile.setParameter("jade_mtp_http_https_keyStoreFile", keyStoreFile);
					profile.setParameter("jade_mtp_http_https_keyStorePass", keyStorePassword);
					profile.setParameter("jade_mtp_http_https_trustManagerClass", jade.mtp.http.https.FriendListAuthentication.class.getName());
					profile.setParameter("jade_mtp_http_https_friendListFile", trustStoreFile);
					profile.setParameter("jade_mtp_http_https_friendListFilePass", trustStorePassword);
					
				} else if(mtpProtocol==MtpProtocol.HTTPS) {
					// --- A secure HTTPS protocol ---------------------------- 
					profile.setParameter(Profile.MTPS, jade.mtp.http.MessageTransportProtocol.class.getName() + "(https://" + ipAddress + ":" + freePort + "/acc)");
					profile.setParameter("jade_mtp_http_https_keyStoreFile", keyStoreFile);
					profile.setParameter("jade_mtp_http_https_keyStorePass", keyStorePassword);
					profile.setParameter("jade_mtp_http_https_trustManagerClass", jade.mtp.http.https.FriendListAuthentication.class.getName());
					profile.setParameter("jade_mtp_http_https_friendListFile", trustStoreFile);
					profile.setParameter("jade_mtp_http_https_friendListFilePass", trustStorePassword);
				
				}
			}
		}
		
		// --------------------------------------------------------------------
		// --- Consider SERVER execution mode --------------------------------- 
		// --------------------------------------------------------------------
		if (this.getProject()==null && globalInfo.getJadeUrlConfigurationForMaster().isLocalhost()) {
			// --- Set MTP port in case of SERVER on local machines -------
			switch (execMode) {
			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --------------------------------------------------------
				// --- See if the configure port for MTP is free ----------
				// --- May happen if a slave is executed, while ----------- 
				// --- a master is already running ------------------------
				// --------------------------------------------------------
				Integer freePort = new PortChecker(globalInfo.getServerMasterPort4MTP(), globalInfo.getServerMasterURL()).getFreePort();
				profile.setParameter("jade_mtp_http_port", freePort.toString());	
				break;

			default:
				break;
			}	
		}
		
	}
	/**
	 * Return the currently know network addresses.
	 * @return the network addresses
	 */
	private NetworkAddresses getNetworkAddresses() {
		return Application.getJadePlatform().getNetworkAddresses();
	}
	/**
	 * Checks if the specified network address is currently available.
	 *
	 * @param ipAddressToCheck the ip address to check
	 * @return true, if is available network address
	 */
	private boolean isAvailableNetworkAddress(String ipAddressToCheck) {
		return getNetworkAddresses().isAvailableIP(ipAddressToCheck);
	}
	/**
	 * Automatically determines the IP address to use for the platform and the MTP
	 * @return the automatic IP
	 */
	private String getAutomaticIP() {
	
		String ipAddress = null;
		InetAddress inetAddress = getNetworkAddresses().getPreferredInetAddress();
		if (inetAddress==null || Application.isNetworkConnected()==false) {
			// --- No network connection, take loopBack address -------
			Vector<NetworkAddress> loopBackAddresses = getNetworkAddresses().getInet4AddressesLoopBack();
			if (loopBackAddresses!=null && loopBackAddresses.size()>0) {
				ipAddress = loopBackAddresses.get(0).getInetAddress().getHostAddress(); 
			}
			
		} else {
			// --- Take the preferred IP address ----------------------
			ipAddress = inetAddress.getHostAddress();
		}
		return ipAddress;
	}
	
	/**
	 * Adds the local configured SERVICES to the input instance of Profile.
	 * @param profile the profile to work on
	 */
	private void setProfileServices(Profile profile){
		String serviceListString = this.getServiceListArgument();
		if (serviceListString.equalsIgnoreCase("")==false || serviceListString!=null) {
			profile.setParameter(Profile.SERVICES, serviceListString);
		}
	}	
	
	
	/**
	 * Returns the current list.
	 * @return the service list
	 */
	public ArrayList<String> getServiceList() {
		if (serviceList==null) {
			serviceList = new ArrayList<>();
		}
		if (this.isCorrectedServiceList==false) {
			// --- Check if the entries needs to be revised -------------------
			for (int i = 0; i < serviceList.size(); i++) {
				String serviceClassName = serviceList.get(i);
				if (serviceClassName.startsWith("agentgui")==true) {
					if (serviceClassName.endsWith(DebugService.class.getSimpleName())==true) {
						serviceClassName = DebugService.class.getName();
					} else if (serviceClassName.endsWith(SimulationService.class.getSimpleName())==true) {
						serviceClassName = SimulationService.class.getName();
					} else if (serviceClassName.endsWith(LoadService.class.getSimpleName())==true) {
						serviceClassName = LoadService.class.getName();
					}
					serviceList.set(i, serviceClassName);
				}
			}
			
			this.isCorrectedServiceList = true;
		}
		return serviceList;
	}
	/**
	 * Can be used to add a class reference to an extended JADE-BaseService.
	 * @param serviceClassReference the service class reference
	 */
	public void addService(String serviceClassReference) {
		if (this.getServiceList().contains(serviceClassReference)==false && serviceClassReference.contains(getAutoServiceTextAddition())==false) {
			// --- Add to the local HashSet -------------------------
			this.getServiceList().add(serviceClassReference);
			// --- add to the DefaultListModel ----------------------
			this.getListModelServices().addElement(serviceClassReference);
			// --- sort the ListModel -------------------------------
			this.sortListModelServices();
			// --- if set, set project changed and unsaved ----------
			this.setProjectChanged();
		}
	}
	/**
	 * Can be used to remove a class reference to an extended JADE-BaseService.
	 * @param serviceClassReference the service class reference
	 */
	public void removeService(String serviceClassReference) {
		if (this.getServiceList().contains(serviceClassReference)==true) {
			// --- remove from the local HashSet --------------------
			this.getServiceList().remove(serviceClassReference);
			// --- remove from the DefaultListModel -----------------
			this.getListModelServices().removeElement(serviceClassReference);
			// --- if set, set project changed and unsaved ----------
			this.setProjectChanged();
		}
	}
	/**
	 * This method will remove all Services from the current Profile.
	 */
	public void removeAllServices() {
		this.getServiceList().clear();
		this.getListModelServices().removeAllElements();
		// --- if set, set project changed and unsaved ----------
		this.setProjectChanged();
	}
	/**
	 * This method walks through the HashSet of configured Services and returns them
	 * as a String separated with a semicolon (';').
	 *
	 * @return String
	 */
	public String getServiceListArgument() {
		String serviceListString = "";
		List<String> serviceList = this.getServiceList();
		for (int i = 0; i < serviceList.size(); i++) {
			String singeleService = serviceList.get(i);
			if (singeleService.endsWith(";")==true) {
				serviceListString += singeleService;
			} else {
				serviceListString += singeleService + ";";	
			}
		}
		return serviceListString;
	}
	
	
	/**
	 * Sets the skip user request for jade start.
	 * @param skipUserRequestForJadeStart the new skip user request for jade start
	 */
	public void setSkipUserRequestForJadeStart(boolean skipUserRequestForJadeStart) {
		this.skipUserRequestForJadeStart = skipUserRequestForJadeStart;
		this.setProjectChanged();
	}
	/**
	 * Checks if is skip user request for jade start.
	 * @return true, if is skip user request for jade start
	 */
	@XmlTransient
	public boolean isSkipUserRequestForJadeStart() {
		return skipUserRequestForJadeStart;
	}
	
	
	/**
	 * With this class the LocalPort, which will be used from a JADE-Container can be set.
	 * @param port2Use the new local port
	 */
	public void setLocalPort(int port2Use){
		this.useLocalPort = port2Use;
		this.setProjectChanged();
	}
	/**
	 * Returns the current Port which is  configured for a JADE-Container.
	 * @return the local port on which JADE is running
	 */
	@XmlTransient
	public Integer getLocalPort() {
		return useLocalPort;
	}

	/**
	 * Sets the use local port to use for the JADE MTP
	 * @param newMTPport the new MTP port to use
	 */
	public void setLocalPortMTP(Integer newMTPport) {
		this.useLocalMtpPort = newMTPport;
		this.setProjectChanged();
	}
	/**
	 * Returns the current Port which is configured for the MTP of the JADE main container.
	 * @return the local MTP port 
	 */
	@XmlTransient
	public Integer getLocalPortMTP() {
		return useLocalMtpPort;
	}

	/**
	 * Sets how the MTP settings have to be created.
	 * @param mtpCreation the new {@link MTP_Creation}
	 */
	public void setMtpCreation(MTP_Creation mtpCreation) {
		this.mtpCreation = mtpCreation;
		this.setProjectChanged();
	}
	/**
	 * Returns how the MTP settings have to be created.
	 * @return the mtp usage
	 */
	@XmlTransient
	public MTP_Creation getMtpCreation() {
		return mtpCreation;
	}
	
	
	
	/**
	 * Sets the MTP IP-address.
	 * @param mtpIpAddress the new MTP IP-address
	 */
	public void setMtpIpAddress(String mtpIpAddress) {
		this.mtpIpAddress = mtpIpAddress;
		this.setProjectChanged();
	}
	/**
	 * Returns the MTP IP-address.
	 * @return the MTP IP-address.
	 */
	@XmlTransient
	public String getMtpIpAddress() {
		return mtpIpAddress;
	}
	
	/**
	 * Sets the MTP Protocol.
	 * @param mtpProtool the new MTP Protocol
	 */
	public void setMtpProtocol(MtpProtocol mtpProtool){
		this.mtpProtocol = mtpProtool;
		this.setProjectChanged();
	}
	/**
	 * Gets the MTP Protocol.
	 * @return the MTP Protocol
	 */
	@XmlTransient
	public MtpProtocol getMtpProtocol(){
		return mtpProtocol;
	}
	
	
	/**
	 * Sets the KeyStoreFile.
	 * @param neKeyStoreFile the new KeyStoreFile
	 */
	public void setKeyStoreFile(String neKeyStoreFile) {
		if (this.getProject()!=null && neKeyStoreFile!=null) {
			neKeyStoreFile = neKeyStoreFile.replace(this.getProject().getProjectSecurityFolderFullPath(), ""); 
		}
		this.keyStoreFile = neKeyStoreFile;
		this.setProjectChanged();
	}
	/**
	 * Gets the KeyStoreFile.
	 * @return the keyStoreFile
	 */
	@XmlTransient
	public String getKeyStoreFile(){
		if (this.getProject()!=null && this.keyStoreFile!=null) {
			return this.getProject().getProjectSecurityFolderFullPath() + this.keyStoreFile;
		}
		return this.keyStoreFile;
	}
	/**
	 * Return the key store file internal, which means without security path.
	 * @return the key store file internal
	 */
	public String getKeyStoreFileInternal(){
		return this.keyStoreFile;
	}
	/**
	 * Sets the KeyStorePassword.
	 * @param keyStorePassword the new KeyStorePassword
	 */
	public void setKeyStorePassword(String keyStorePassword){
		this.setKeyStorePasswordEncrypted(Application.getGlobalInfo().pwEncrypt(keyStorePassword));
		this.setProjectChanged();
	}
	/**
	 * Gets the KeyStorePassword.
	 * @return the keyStorePassword
	 */
	@XmlTransient
	public String getKeyStorePassword(){
		return Application.getGlobalInfo().pwDecrypt(this.getKeyStorePasswordEncrypted());
	}
	/**
	 * Sets the KeyStorePasswordEncrypted.
	 * @param keyStorePasswordEncrypted the KeyStorePasswordEncrypted
	 */
	public void setKeyStorePasswordEncrypted(String keyStorePasswordEncrypted) {
		this.keyStorePasswordEncrypted = keyStorePasswordEncrypted;
	}
	/**
	 * Gets the KeyStorePasswordEncrypted.
	 * @return the keyStorePasswordEncrypted
	 */
	@XmlTransient
	public String getKeyStorePasswordEncrypted() {
		return keyStorePasswordEncrypted;
	}
	
	
	/**
	 * Sets the TrustStoreFile.
	 * @param newTrustStoreFile the new TrustStoreFile
	 */
	public void setTrustStoreFile(String newTrustStoreFile){
		if (this.getProject()!=null && newTrustStoreFile!=null) {
			newTrustStoreFile = newTrustStoreFile.replace(this.getProject().getProjectSecurityFolderFullPath(), ""); 
		}
		this.trustStoreFile = newTrustStoreFile;
		this.setProjectChanged();
	}
	/**
	 * Gets the TrustStoreFile.
	 * @return the trustStoreFile
	 */
	@XmlTransient
	public String getTrustStoreFile(){
		if (this.getProject()!=null && this.trustStoreFile!=null) {
			return this.getProject().getProjectSecurityFolderFullPath() + this.trustStoreFile;
		}
		return this.trustStoreFile;
	}
	/**
	 * Return the trust store file internal, which means without security path.
	 * @return the trust store file internal
	 */
	public String getTrustStoreFileInternal(){
		return this.trustStoreFile;
	}
	/**
	 * Sets the TrustStorePassword.
	 * @param trustStorePassword the new TrustStorePassword
	 */
	public void setTrustStorePassword(String trustStorePassword){
		this.setTrustStorePasswordEncrypted(Application.getGlobalInfo().pwEncrypt(trustStorePassword));
		this.setProjectChanged();
	}
	/**
	 * Gets the TrustStorePassword.
	 * @return the trustStorePassword
	 */
	@XmlTransient
	public String getTrustStorePassword(){
		return Application.getGlobalInfo().pwDecrypt(this.getTrustStorePasswordEncrypted());
	}
	/**
	 * Gets the trust store password encrypted.
	 * @return the trust store password encrypted
	 */
	public String getTrustStorePasswordEncrypted() {
		return trustStorePasswordEncrypted;
	}
	/**
	 * Sets the TrustStorePasswordEncrypted.
	 * @param trustStorePasswordEncrypted the new TrustStorePasswordEncrypted
	 */
	@XmlTransient
	public void setTrustStorePasswordEncrypted(String trustStorePasswordEncrypted) {
		this.trustStorePasswordEncrypted = trustStorePasswordEncrypted;
	}
	
	
	/**
	 * Gets the list model services.
	 * @return the listModelServices
	 */
	@XmlTransient
	public DefaultListModel<String> getListModelServices() {
		if (listModelServices==null) {
			listModelServices = new DefaultListModel<String>();
			this.sortListModelServices();
		}
		return listModelServices;
	}
	/**
	 * This method will sort the current list model for the chosen services.
	 */
	private void sortListModelServices() {
		
		if (this.getServiceList().size()==0) return;
		
		this.listModelServices.removeAllElements();

		Collections.sort(this.getServiceList()); 
		this.getServiceList().forEach(serviceClass -> listModelServices.addElement(serviceClass));
	}

	/**
	 * This Method returns a String which shows the current
	 * configuration of this instance.
	 *
	 * @return String
	 */
	public String toString() {
		String bugOut = ""; 
		bugOut += "LocalPort:" + useLocalPort + ";";
		bugOut += "Services:" + getServiceListArgument();
		return bugOut;
	}

	
}
