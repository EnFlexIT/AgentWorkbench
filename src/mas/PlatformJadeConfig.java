package mas;

import jade.core.Profile;
import jade.core.ProfileImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import network.PortChecker;

public class PlatformJadeConfig implements Serializable {

	/**
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
	private Integer useLocalPort = 0;
	
	@XmlElementWrapper(name = "serviceList")
	@XmlElement(name="service")			
	private HashSet<String> useServiceList = new HashSet<String>();
	
	
	public PlatformJadeConfig() {
		
	}
	
	public Profile getNewInstanceOfProfilImpl(){
		Profile prof = new ProfileImpl();
		prof = this.setProfileLocalPort(prof);
		prof = this.setProfileServices(prof);
		return prof;
	}
	
	public void addService(String service2add) {
		this.useServiceList.add(service2add);
	}
	public void removeService(String service2remove) {
		this.useServiceList.remove(service2remove);
	}
	
	public void setLocalPort(int port2Use){
		useLocalPort = port2Use;
	}
	private void findFreePort(int portSearchStart){
		// --- Freien Port für die Plattform finden ---------
		PortChecker portCheck = new PortChecker(portSearchStart);
		useLocalPort = portCheck.getFreePort();
	}
	
	public Profile setProfileLocalPort(Profile profile){
		this.findFreePort(useLocalPort);
		profile.setParameter(Profile.LOCAL_PORT, useLocalPort.toString());
		return profile;
	}
	public Profile setProfileServices(Profile profile){
		String serviceListString = this.getServiceListArgument();
		if (serviceListString.equalsIgnoreCase("")==false || serviceListString!=null) {
			profile.setParameter(Profile.SERVICES, serviceListString);	
		}
		return profile;
	}
	private String getServiceListArgument() {
		String serviceListString = "";
		Iterator<String> it = useServiceList.iterator();
		while (it.hasNext()) {
			serviceListString += it.next();
		}
		return serviceListString;
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
	
	
}
