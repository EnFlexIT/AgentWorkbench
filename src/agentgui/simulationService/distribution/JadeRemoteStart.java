package agentgui.simulationService.distribution;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


import agentgui.core.application.Application;
import agentgui.simulationService.distribution.ontology.RemoteContainerConfig;

public class JadeRemoteStart extends Thread {

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
	private boolean jadeShowGUI = true; 
	private String jadeShowGUIAgentName = "rma."; 
	private String jadeServices = "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService;mas.service.SimulationService;";
	private String jadeHost = "localhost";
	private String jadePort = Application.RunInfo.getJadeLocalPort().toString();
	private String jadeContainerName = "remote";
	
	private final String pathBaseDir = Application.RunInfo.PathBaseDir();
	
	/**
	 * Default-Constructor
	 */
	public JadeRemoteStart() {
		
	}	
	
	public JadeRemoteStart(RemoteContainerConfig reCoCo) {
		
		jadeIsRemoteContainer = true;
		if (reCoCo.getJvmMemAllocInitial()==null && reCoCo.getJvmMemAllocMaximum()==null) {
			this.jvmMemAllocUseDefaults = true;	
			this.jvmMemAllocInitial = this.jvmMemo0032MB;
			this.jvmMemAllocMaximum = this.jvmMemo0256MB;
		} else {
			this.jvmMemAllocUseDefaults = false;
			this.jvmMemAllocInitial = reCoCo.getJvmMemAllocInitial();
			this.jvmMemAllocMaximum = reCoCo.getJvmMemAllocMaximum();
		}
		this.jadeShowGUI = reCoCo.getJadeShowGUI();	
		
		if (reCoCo.getJadeServices()!=null) {
			this.jadeServices = reCoCo.getJadeServices();
		}
		if (reCoCo.getJadeHost()!=null) {
			this.jadeHost = reCoCo.getJadeHost();	
		}
		if (reCoCo.getJadePort()!=null) {
			this.jadePort = reCoCo.getJadePort();	
		}
		if (reCoCo.getJadeContainerName()!=null) {
			this.jadeContainerName = reCoCo.getJadeContainerName();	
		}
		
	}	
	
	/**
	 * Action for the Thread-Start. Starts a new JVM with a new Jade-Instance
	 */
	@Override
	public void run() {
		//this.setName("JADE Remote: " + jadeContainerName);
		this.startJade();
	}
	
	/**
	 * This Method starts a Jade-Platform within a new Java Virtual Machine 
	 */
	public void startJade(){
		
		String os = System.getProperty("os.name");
		
		String java = "";
		String javaVMArgs = "";
		String classPath = "";
		String jade = "";
		String jadeArgs = "";
		
		
		// --------------------------------------
		// --- Java-Config ----------------------
		java = "java";
		if (jvmMemAllocUseDefaults==false) {
			javaVMArgs = "-Xms" + jvmMemAllocInitial + " -Xmx" + jvmMemAllocMaximum;
		} 
		
		// --- Class-Path configuration ---------
		classPath += "-classpath ";
		classPath += ".;";
		// --- Jade  himself ----------
		classPath += "./lib/jade/lib/jade.jar;";
		// --- SimulationService ------
		classPath += "./lib/jade/lib/simulation.jar;";
		// --- Hyperic-Sigar ----------
		classPath += "./lib/hyperic-sigar/sigar-bin/lib/sigar.jar;";
		classPath += "./lib/hyperic-sigar/sigar-bin/lib/log4j.jar;";
		classPath += "./lib/hyperic-sigar/sigar-bin/lib/junit.jar;";

		// ++++++++++++++++++++++++++++
		// +++ Check operating system +
		if (os.toLowerCase().contains("windows")==true) {
			// --- nothing to do here ---
		} else if (os.toLowerCase().contains("linux")==true) {
			classPath = classPath.replaceAll(";", ":");
		}

		
		// --------------------------------------
		// --- Jade-Config ----------------------
		jade += "jade.Boot";
		if (jadeServices!=null) {
			jadeArgs += "-services  " + jadeServices + " ";
		}
		if (jadeIsRemoteContainer) {
			jadeArgs += "-container ";
			jadeArgs += "-container-name " + jadeContainerName + " ";
		} 
		jadeArgs += "-host " + jadeHost + " ";
		jadeArgs += "-port " + jadePort + " ";
		if (jadeShowGUI) {
			jadeArgs += jadeShowGUIAgentName + jadeContainerName + ":jade.tools.rma.rma ";
		}		
		jadeArgs = jadeArgs.trim();
		
		// --------------------------------------
		// --- execute zusammenbauen ------------
		String execute = java + " " + javaVMArgs + " " + classPath + " " + jade  + " " + jadeArgs;
		execute = execute.replace("  ", " ");
		//System.out.println( "=> Remote Execute: " + execute);
		
		// --------------------------------------
		try {
			
			//System.out.println(execute);
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(true);
			proBui.directory(new File(pathBaseDir));
			
			System.out.println("Start Container [" + jadeContainerName + "] ... ");
			Process p = proBui.start();
			
			Scanner in = new Scanner( p.getInputStream() ).useDelimiter( "\\Z" );
			Scanner err = new Scanner( p.getErrorStream() ).useDelimiter( "\\Z" );

			while (in.hasNextLine() || err.hasNextLine() ) {
				if (in.hasNextLine()) {
					System.out.println("[" + jadeContainerName + "]: " + in.nextLine());	
				}
				if (err.hasNextLine()){
					System.err.println("[" + jadeContainerName + "]: " + err.nextLine());	
				}
			}
			System.out.println("Killed Container [" + jadeContainerName + "]");
		    
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
	 * @return the jadeShowGUIAgentName
	 */
	public String getJadeShowGUIAgentName() {
		return jadeShowGUIAgentName;
	}
	/**
	 * @param jadeShowGUIAgentName the jadeShowGUIAgentName to set
	 */
	public void setJadeShowGUIAgentName(String jadeShowGUIAgentName) {
		this.jadeShowGUIAgentName = jadeShowGUIAgentName;
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
