package de.enflexit.awb.bgSystem.db.dataModel;

import java.io.Serializable;
import java.util.Calendar;

/**
 * The Class BgSystemPlatform serves as structure class for database table and thus its entries.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BgSystemPlatform implements Serializable {

	private static final long serialVersionUID = 2016628115417106936L;

	private String contactAgent;	// DB: Primary Key

	private String platformName;
	private boolean server;
	private String ipAddress;
	private String url;
	private int jadePort;
	private String http4mtp;
	
	private int versionMajor;
	private int versionMinor;
	private int versionMicro;
	private String versionBuild;
	
	private String osName;
	private String osVersion;
	private String osArchitecture;
	
	private String cpuProcessorName;
	private int cpuNoOfLogical;
	private int cpuNoOfPhysical;
	private int cpuSpeedMHz;
	private int memoryMB;
	
	private double benchmarkValue;
	
	private Calendar timeOnlineSince;
	private Calendar timeLastContact;
	private Calendar localTimeOnlineSince;
	private Calendar localTimeLastContact;
				
	private boolean currentlyAvailable;
	private double currentLoadCPU; 
	private double currentLoadMemory;
	private double currentLoadMemoryJVM;
	
	private int currentLoadNoOfThreads;
	private boolean currentLoadThresholdExceeded;
	
	
	
	public String getContactAgent() {
		return contactAgent;
	}
	public void setContactAgent(String contactAgent) {
		this.contactAgent = contactAgent;
	}
	
	
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	
	public boolean isServer() {
		return server;
	}
	public void setServer(boolean isServer) {
		this.server = isServer;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getJadePort() {
		return jadePort;
	}
	public void setJadePort(int jadePort) {
		this.jadePort = jadePort;
	}
	
	public String getHttp4mtp() {
		return http4mtp;
	}
	public void setHttp4mtp(String http4mtp) {
		this.http4mtp = http4mtp;
	}
	
	
	public int getVersionMajor() {
		return versionMajor;
	}
	public void setVersionMajor(int versionMajor) {
		this.versionMajor = versionMajor;
	}
	
	public int getVersionMinor() {
		return versionMinor;
	}
	public void setVersionMinor(int versionMinor) {
		this.versionMinor = versionMinor;
	}
	
	public int getVersionMicro() {
		return versionMicro;
	}
	public void setVersionMicro(int versionMicro) {
		this.versionMicro = versionMicro;
	}
	
	public String getVersionBuild() {
		return versionBuild;
	}
	public void setVersionBuild(String versionBuild) {
		this.versionBuild = versionBuild;
	}
	
	
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	
	public String getOsVersion() {
		return osVersion;
	}
	
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getOsArchitecture() {
		return osArchitecture;
	}
	public void setOsArchitecture(String osArchitecture) {
		this.osArchitecture = osArchitecture;
	}
	
	
	public String getCpuProcessorName() {
		return cpuProcessorName;
	}
	public void setCpuProcessorName(String cpuProcessorName) {
		this.cpuProcessorName = cpuProcessorName;
	}
	
	public int getCpuNoOfLogical() {
		return cpuNoOfLogical;
	}
	public void setCpuNoOfLogical(int cpuNoOfLogical) {
		this.cpuNoOfLogical = cpuNoOfLogical;
	}
	
	public int getCpuNoOfPhysical() {
		return cpuNoOfPhysical;
	}
	public void setCpuNoOfPhysical(int cpuNoOfPhysical) {
		this.cpuNoOfPhysical = cpuNoOfPhysical;
	}
	
	public int getCpuSpeedMHz() {
		return cpuSpeedMHz;
	}
	public void setCpuSpeedMHz(int cpuSpeedMHz) {
		this.cpuSpeedMHz = cpuSpeedMHz;
	}
	
	public int getMemoryMB() {
		return memoryMB;
	}
	public void setMemoryMB(int memoryMB) {
		this.memoryMB = memoryMB;
	}
	
	
	public double getBenchmarkValue() {
		return benchmarkValue;
	}
	public void setBenchmarkValue(double benchmarkValue) {
		this.benchmarkValue = benchmarkValue;
	}
	
	
	public Calendar getTimeOnlineSince() {
		if (timeOnlineSince==null) {
			timeOnlineSince = Calendar.getInstance();
		}
		return timeOnlineSince;
	}
	public void setTimeOnlineSince(Calendar timeOnlineSince) {
		this.timeOnlineSince = timeOnlineSince;
	}
	
	public Calendar getTimeLastContact() {
		if (timeLastContact==null) {
			timeLastContact = Calendar.getInstance();
		}
		return timeLastContact;
	}
	public void setTimeLastContact(Calendar timeLastContact) {
		this.timeLastContact = timeLastContact;
	}
	
	public Calendar getLocalTimeOnlineSince() {
		if (localTimeOnlineSince==null) {
			localTimeOnlineSince = Calendar.getInstance();
		}
		return localTimeOnlineSince;
	}
	public void setLocalTimeOnlineSince(Calendar localTimeOnlineSince) {
		this.localTimeOnlineSince = localTimeOnlineSince;
	}
	
	public Calendar getLocalTimeLastContact() {
		if (localTimeLastContact==null) {
			localTimeLastContact = Calendar.getInstance();
		}
		return localTimeLastContact;
	}
	public void setLocalTimeLastContact(Calendar localTimeLastContact) {
		this.localTimeLastContact = localTimeLastContact;
	}
	
	
	public boolean isCurrentlyAvailable() {
		return currentlyAvailable;
	}
	public void setCurrentlyAvailable(boolean isCurrentlyAvailable) {
		this.currentlyAvailable = isCurrentlyAvailable;
	}
	
	public double getCurrentLoadCPU() {
		return currentLoadCPU;
	}
	public void setCurrentLoadCPU(double currentLoadCPU) {
		this.currentLoadCPU = currentLoadCPU;
	}
	
	public double getCurrentLoadMemory() {
		return currentLoadMemory;
	}
	public void setCurrentLoadMemory(double currentLoadMemory) {
		this.currentLoadMemory = currentLoadMemory;
	}
	
	public double getCurrentLoadMemoryJVM() {
		return currentLoadMemoryJVM;
	}
	public void setCurrentLoadMemoryJVM(double currentLoadMemoryJVM) {
		this.currentLoadMemoryJVM = currentLoadMemoryJVM;
	}
	
	public int getCurrentLoadNoOfThreads() {
		return currentLoadNoOfThreads;
	}
	public void setCurrentLoadNoOfThreads(int currentLoadNoOfThreads) {
		this.currentLoadNoOfThreads = currentLoadNoOfThreads;
	}
	
	public boolean isCurrentLoadThresholdExceeded() {
		return currentLoadThresholdExceeded;
	}
	public void setCurrentLoadThresholdExceeded(boolean isCurrentLoadThresholdExceeded) {
		this.currentLoadThresholdExceeded = isCurrentLoadThresholdExceeded;
	}

	
	/**
	 * Returns the residual calculation capability in Mflops/s.
	 * @return the residual calculation capability
	 */
	public double getResidualCalculationCapability() {
		return this.getBenchmarkValue() - (this.getBenchmarkValue() * (this.getCurrentLoadCPU()/100));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null) return false;
		if (compObject instanceof BgSystemPlatform == false) return false;
		
		BgSystemPlatform compPlatform = (BgSystemPlatform) compObject;
		return compPlatform.getContactAgent().equals(this.getContactAgent());
	}
	
}
