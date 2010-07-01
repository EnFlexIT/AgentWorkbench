package distribution;

import java.io.IOException;

import application.Application;

public class JadeRemoteStart {

	public final String jvmMemo0016MB = "16m";
	public final String jvmMemo0032MB = "32m";
	public final String jvmMemo0064MB = "64m";
	public final String jvmMemo0128MB = "128m";
	public final String jvmMemo0256MB = "256m";
	public final String jvmMemo0512MB = "512m";
	public final String jvmMemo1024MB = "1024m";
	
	private boolean jvmMemAllocUseDefaults = true;
	private String jvmMemAllocInitial = this.jvmMemo0032MB;
	private String jvmMemAllocMaximum = this.jvmMemo0128MB;
	
	private boolean jadeIsRemoteContainer = true; 
	private boolean jadeShowGUI = false; 
	private String jadeHost = "localhost";
	private String jadePort = Application.RunInfo.getJadeLocalPort().toString();
	private String jadeContainerName = "RemoteContainer";
	private String jadeServices = null;

	/**
	 * This Method starts a Jade-Platdorm within a new Java Virtual Machine 
	 */
	public void start(){
		
		String execute = null;
		
		// --- Java-Config ----------------------
		execute  = "java ";
		if (jvmMemAllocUseDefaults==false) {
			execute += "-Xms" + jvmMemAllocInitial + " -Xmx" + jvmMemAllocMaximum + " ";	
		}		
		execute += "-classpath ./lib/jade/lib/jade.jar; ";
		execute += "jade.Boot ";
		
		// --- Jade-Config ----------------------
		if (jadeIsRemoteContainer) {
			execute += "-container ";
			execute += "-container-name " + jadeContainerName + " ";
		} else {
			if (jadeServices!=null) {
				execute += "-services  " + jadeServices + " ";
			}
			if (jadeShowGUI) {
				execute += "-gui ";
			}
		}
		execute += "-host " + jadeHost + " ";
		execute += "-port " + jadePort + " ";
		
		System.out.println(execute); 
		
		// --- Execute the command-line --------- 
		try {
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime ().exec (execute);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return the jvmMemAllocUseDefaults
	 */
	public boolean isJVMMemAllocUseDefaults() {
		return jvmMemAllocUseDefaults;
	}
	/**
	 * @param jvmMemAllocUseDefaults the jvmMemAllocUseDefaults to set
	 */
	public void setJVMMemAllocUseDefaults(boolean useDefaults) {
		this.jvmMemAllocUseDefaults = useDefaults;
	}

	/**
	 * @return the jvmMemAllocInitial
	 */
	public String getJVMMemAllocInitial() {
		return jvmMemAllocInitial;
	}
	/**
	 * @param jvmMemAllocInitial the jvmMemAllocInitial to set
	 */
	public void setJVMMemAllocInitial(String jvmMemAllocInitial) {
		this.jvmMemAllocInitial = jvmMemAllocInitial;
	}

	/**
	 * @return the jvmMemAllocMaximum
	 */
	public String getJVMMemAllocMaximum() {
		return jvmMemAllocMaximum;
	}
	/**
	 * @param jvmMemAllocMaximum the jvmMemAllocMaximum to set
	 */
	public void setJVMMemAllocMaximum(String jvmMemAllocMaximum) {
		this.jvmMemAllocMaximum = jvmMemAllocMaximum;
	}

	/**
	 * @return the jadeIsRemoteContainer
	 */
	public boolean isJADEIsRemoteContainer() {
		return jadeIsRemoteContainer;
	}
	/**
	 * @param jadeIsRemoteContainer the jadeIsRemoteContainer to set
	 */
	public void setJADEIsRemoteContainer(boolean jadeIsRemoteContainer) {
		this.jadeIsRemoteContainer = jadeIsRemoteContainer;
	}

	/**
	 * @return the jadeShowGUI
	 */
	public boolean isJADEShowGUI() {
		return jadeShowGUI;
	}
	/**
	 * @param jadeShowGUI the jadeShowGUI to set
	 */
	public void setJADEShowGUI(boolean jadeShowGUI) {
		this.jadeShowGUI = jadeShowGUI;
	}

	/**
	 * @return the jadeHost
	 */
	public String getJADEHost() {
		return jadeHost;
	}
	/**
	 * @param jadeHost the jadeHost to set
	 */
	public void setJADEHost(String jadeHost) {
		this.jadeHost = jadeHost;
	}

	/**
	 * @return the jadePort
	 */
	public String getJADEPort() {
		return jadePort;
	}
	/**
	 * @param jadePort the jadePort to set
	 */
	public void setJADEPort(String jadePort) {
		this.jadePort = jadePort;
	}

	/**
	 * @return the jadeContainerName
	 */
	public String getJADEContainerName() {
		return jadeContainerName;
	}
	/**
	 * @param jadeContainerName the jadeContainerName to set
	 */
	public void setJADEContainerName(String jadeContainerName) {
		this.jadeContainerName = jadeContainerName;
	}

	/**
	 * @param jadeServices the jadeServices to set
	 */
	public void setJADEServices(String jadeServices) {
		this.jadeServices = jadeServices;
	}
	/**
	 * @return the jadeServices
	 */
	public String getJADEServices() {
		return jadeServices;
	}
	
}
