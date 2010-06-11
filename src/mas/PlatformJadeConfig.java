package mas;
/**
 * @author Chriostian Derksen
 */
import jade.core.Profile;
import jade.core.ProfileImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import network.PortChecker;
import application.Application;

public class PlatformJadeConfig implements Serializable {

	/**
	 * With this class, the Profile of a new JADE-Container can be configured.
	 * To use this class, just create a new instance of it and go throw 
	 * configurations like in the example below.
	 * Be aware of the fact, that the default value of the 'UsesDefaults'-Variable 
	 * is true. Therfore you should keep in mind, that for your own configuration, 
	 * you should always set the value of 'UsesDefaults' to false first.
	 * 
	 * After configuration you can use the method 'getNewInstanceOfProfilImpl()'
	 * which returns a new Instance of 'jade.core.Profile'. This can be used to 
	 * create a new JADE-Container.
	 *
	 * EXAMPLE:
	 * 
	 *  PlatformJadeConfig pjc = new PlatformJadeConfig();
	 *	pjc.setUseDefaults(false);
	 *  pjc.runAgentMobilityService(true);	
	 *  pjc.runInterPlatformMobilityService(true);
	 *	pjc.setLocalPort(1234);
	 *	Profile profil = pjc.getNewInstanceOfProfilImpl();
	 *
	 */
	
	private static final long serialVersionUID = -9062155032902746361L;
	// --- Services "Active by default" ---------------------------------------
	private final String SERVICE_AgentMobilityService = "jade.core.mobility.AgentMobilityService;";
	private final String SERVICE_NotificationService = "jade.core.event.NotificationService;"; 
	
	// --- Services "Inactive by default" -------------------------------------
	private final String SERVICE_MainReplicationService = "jade.core.replication.MainReplicationService;";
	private final String SERVICE_FaultRecoveryService = "jade.core.faultRecovery.FaultRecoveryService;";
	private final String SERVICE_AddressNotificationService = "jade.core.replication.AddressNotificationService;";
	private final String SERVICE_TopicManagementService = "jade.core.messaging.TopicManagementService;";
	private final String SERVICE_PersistentDeliveryService = "jade.core.messaging.PersistentDeliveryService;";
	private final String SERVICE_UDPNodeMonitoringServ = "jade.core.nodeMonitoring.UDPNodeMonitoringService;";
	private final String SERVICE_BEManagementService = "jade.imtp.leap.nio.BEManagementService;";
	
	// --- Add-On-Services ----------------------------------------------------
	private final String SERVICE_InterPlatformMobilityService = "jade.core.migration.InterPlatformMobilityService;";
	
	// --- Weitere Vars. ------------------------------------------------------ 
	@XmlElement(name="start4Sim")		
	private boolean start4Simulation = true;
	@XmlElement(name="useAppDefaults")		
	private boolean useDefaults = true;
	@XmlElement(name="useLocalPort")	
	private Integer useLocalPort = Application.RunInfo.getJadeLocalPort();
	
	@XmlElementWrapper(name = "serviceList")
	@XmlElement(name="service")			
	private HashSet<String> useServiceList = new HashSet<String>();
	
	/**
	 * Constructor of this class
	 */
	public PlatformJadeConfig() {
	
	}
	
	/**
	 * This Method returns a new Instance of Profil, which   
	 * can be used for starting a new JADE-Container
	 * @return jade.core.Profile
	 */
	public Profile getNewInstanceOfProfilImpl(){
		Profile prof = new ProfileImpl();
		prof = this.setProfileLocalPort(prof);
		prof = this.setProfileServices(prof);
		return prof;
	}
	/**
	 * This Method scans for a free Port, which can be used
	 * for the JADE-Container. It's starts searching for a free
	 * Port on 'portSearchStart'. If not available, it checks
	 * the next higher Port and so on. 
	 * @param portSearchStart
	 */
	private void findFreePort(int portSearchStart){
		// --- Freien Port für die Plattform finden ---------
		PortChecker portCheck = new PortChecker(portSearchStart);
		useLocalPort = portCheck.getFreePort();
	}
	/**
	 * Adds the local configured 'LocalPort' to the input instance of Profile
	 * @param profile
	 * @return jade.core.Profile
	 */
	private Profile setProfileLocalPort(Profile profile){
		this.findFreePort(useLocalPort);
		profile.setParameter(Profile.LOCAL_PORT, useLocalPort.toString());
		return profile;
	}
	/**
	 * Adds the local configured services to the input instance of Profile
	 * @param profile
	 * @return jade.core.Profile
	 */
	private Profile setProfileServices(Profile profile){
		String serviceListString = this.getServiceListArgument();
		if (serviceListString.equalsIgnoreCase("")==false || serviceListString!=null) {
			profile.setParameter(Profile.SERVICES, serviceListString);	
		}
		return profile;
	}
	/**
	 * This method walks through the HashSet of configured Services and retuns them as on String 
	 * @return String
	 */
	private String getServiceListArgument() {
		String serviceListString = "";
		Iterator<String> it = useServiceList.iterator();
		while (it.hasNext()) {
			serviceListString += it.next();
		}
		//TODO: bald mal wieder rausschmeissen ...
		serviceListString +="mas.time.AgentGUIService;";
		return serviceListString;
	}
	/**
	 * Checks if a Service is configured for this instance.
	 * The requested Service can be given with the actual class of the service
	 * @param requestedService
	 * @return boolean
	 */
	public boolean isUsingService(String requestedService) {
		if ( useServiceList.contains(requestedService) == true ) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Counts the number of services which are currently configured 
	 * @return
	 */
	public Integer countUsedServices() {
		return this.useServiceList.size();
	}
	
	/**
	 * With this class the LocalPort, which will be used from a JADE-Container can be set 
	 * @param port2Use
	 */
	public void setLocalPort(int port2Use){
		useLocalPort = port2Use;
	}
	/**
	 * Returns the current Port which is  configured for a JADE-Container 
	 * @return Integer
	 */
	public Integer getLocalPort() {
		return useLocalPort;
	}
	/**
	 * @param start4Simulation the start4Simulation to set
	 */
	public void setStart4Simulation(boolean start4Simulation) {
		this.start4Simulation = start4Simulation;
	}
	/**
	 * @return the start4Simulation
	 */
	@XmlTransient
	public boolean isStart4Simulation() {
		return start4Simulation;
	}
	/**
	 * @param useDefaults the useDefaults to set
	 */
	public void setUseDefaults(boolean useDefaults) {
		this.useDefaults = useDefaults;
	}
	/**
	 * @return the useDefaults
	 */
	@XmlTransient
	public boolean isUseDefaults() {
		return useDefaults;
	}

	// --- Services "Active by default" ---------------------------------------
	public void runAgentMobilityService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_AgentMobilityService);	
		}else {
			this.useServiceList.remove(SERVICE_AgentMobilityService);
		}
	}
	public void runNotificationService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_NotificationService);	
		}else {
			this.useServiceList.remove(SERVICE_NotificationService);
		}
	}
	
	// --- Services "Inactive by default" -------------------------------------
	public void runMainReplicationService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_MainReplicationService);	
		}else {
			this.useServiceList.remove(SERVICE_MainReplicationService);
		}
	}
	public void runFaultRecoveryService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_FaultRecoveryService);	
		}else {
			this.useServiceList.remove(SERVICE_FaultRecoveryService);
		}
	}
	public void runAddressNotificationService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_AddressNotificationService);	
		}else {
			this.useServiceList.remove(SERVICE_AddressNotificationService);
		}
	}
	public void runTopicManagementService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_TopicManagementService);	
		}else {
			this.useServiceList.remove(SERVICE_TopicManagementService);
		}
	}
	public void runPersistentDeliveryService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_PersistentDeliveryService);	
		}else {
			this.useServiceList.remove(SERVICE_PersistentDeliveryService);
		}
	}
	public void runUDPNodeMonitoringServ(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_UDPNodeMonitoringServ);	
		}else {
			this.useServiceList.remove(SERVICE_UDPNodeMonitoringServ);
		}
	}
	public void runBEManagementService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_BEManagementService);	
		}else {
			this.useServiceList.remove(SERVICE_BEManagementService);
		}
	}
	// --- Add-On-Services ----------------------------------------------------
	public void runInterPlatformMobilityService(boolean runService) {
		if (runService==true) {
			this.useServiceList.add(SERVICE_InterPlatformMobilityService);	
		}else {
			this.useServiceList.remove(SERVICE_InterPlatformMobilityService);
		}
	}
	
	// --- Services "Active by default" ---------------------------------------
	public boolean isAgentMobilityService() {
		return this.useServiceList.contains(SERVICE_AgentMobilityService);	
	}
	public boolean isNotificationService() {
		return this.useServiceList.contains(SERVICE_NotificationService);	
	}
	// --- Services "Inactive by default" -------------------------------------
	public boolean isMainReplicationService() {
		return this.useServiceList.contains(SERVICE_MainReplicationService);	
	}
	public boolean isFaultRecoveryService() {
		return this.useServiceList.contains(SERVICE_FaultRecoveryService);	
	}
	public boolean isAddressNotificationService() {
		return this.useServiceList.contains(SERVICE_AddressNotificationService);	
	}
	public boolean isTopicManagementService() {
		return this.useServiceList.contains(SERVICE_TopicManagementService);	
	}
	public boolean isPersistentDeliveryService() {
		return this.useServiceList.contains(SERVICE_PersistentDeliveryService);	
	}
	public boolean isUDPNodeMonitoringServ() {
		return this.useServiceList.contains(SERVICE_UDPNodeMonitoringServ);	
	}
	public boolean isBEManagementService() {
		return this.useServiceList.contains(SERVICE_BEManagementService);	
	}
	// --- Add-On-Services ----------------------------------------------------
	public boolean isInterPlatformMobilityService() {
		return this.useServiceList.contains(SERVICE_InterPlatformMobilityService);	
	}
	
	/**
	 * This Method compares the current instance with another instances  
	 * of this class and returns true, if they are logical identical
	 * @param jadeConfig2
	 * @return boolean
	 */
	public boolean isEqual(PlatformJadeConfig jadeConfig2) {
		
		// --- Selbe Anzahl der ausgewählten Services ? -------------
		if ( this.countUsedServices() != jadeConfig2.countUsedServices() ) {
			return false;
		}
		// --- Sind die ausgewählten Services identisch? ------------ 
		Iterator<String> it = this.useServiceList.iterator();
		while( it.hasNext() ) {
			String currService = it.next();
			if ( jadeConfig2.isUsingService(currService) == false ) {
				return false;
			}
		}
		// --- Soll der selbe Jade LocalPort verwendet werden ? ----
		if ( jadeConfig2.getLocalPort().equals(this.getLocalPort()) ) {
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * This Method returns a String which shows the current 
	 * configuration of this instance
	 * @return String 
	 */
	public String toString() {
		
		String bugOut = ""; 
		bugOut += "Start4Sim:" + start4Simulation + ";";
		bugOut += "UseDefaults:" + useDefaults + ";";
		bugOut += "LocalPort:" + useLocalPort + ";";
		
		bugOut += "Services:";
		Iterator<String> it = this.useServiceList.iterator();
		while( it.hasNext() ) {
			bugOut += it.next();
		}
		return bugOut;
	}
	
	
}
