package agentgui.simulationService.distribution;

import jade.util.leap.ArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import agentgui.core.application.Application;
import agentgui.core.jade.PlatformJadeConfig;
import agentgui.simulationService.ontology.RemoteContainerConfig;

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
	
	private ArrayList jadeJarInclude = null;
	private ArrayList jadeJarIncludeClassPath = new ArrayList();
	private File extJarFolder = null; 
	
	private final String pathBaseDir = Application.RunInfo.PathBaseDir();
	
	/**
	 * Default constructor	
	 * @param reCoCo
	 */
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
		if (reCoCo.getJadeJarIncludeList()!=null) {
			this.jadeJarInclude = (ArrayList) reCoCo.getJadeJarIncludeList();	
			this.handelExternalJars();
		}
	}	
	
	/**
	 * If the request of a remote container contains external jars, download
	 * them and include them in the current CLASSPATH
	 */
	private void handelExternalJars() {
		
		String pathSep = Application.RunInfo.AppPathSeparatorString();
		String destinPath = Application.RunInfo.PathDownloads(false);
		String projectSubFolder = null;
		String downloadProtocol = "";
		
		for (int i = 0; i < jadeJarInclude.size(); i++) {
			
			String httpJarFile = (String) jadeJarInclude.get(i);
			if (projectSubFolder==null) {
				// --- Find sub-folder -------------------------
				projectSubFolder = httpJarFile.replace("http://", "");
				int cut = projectSubFolder.indexOf("/")+1;
				projectSubFolder = projectSubFolder.substring(cut, projectSubFolder.length());
				cut = projectSubFolder.indexOf("/");
				projectSubFolder = projectSubFolder.substring(0, cut);
				projectSubFolder+= pathSep;
				
				// --- Correct the Path for the download -------
				destinPath = destinPath + projectSubFolder;
				
				// --- Check if this Folder exists -------------
				extJarFolder = new File(destinPath);
				if (extJarFolder.exists()) {
					deleteFolder(extJarFolder);
					extJarFolder.delete();
				}
				extJarFolder.mkdir();
			}
			
			// --- Define Destination-File ---------------------
			File remoteFile = new File(httpJarFile);
			String destinFile = destinPath + remoteFile.getAbsoluteFile().getName();
			
			// --- Start the download --------------------------
			new Download(httpJarFile, destinFile);

			// --- Reminder für den ClassPath setzen -----------
			String ClassPathEntry = "./" + destinFile.replace(pathSep, "/") + ";";
			jadeJarIncludeClassPath.add(ClassPathEntry);
			
			// --- Download-Protocoll --------------------------
			if (downloadProtocol.equals("")==false) {
				downloadProtocol += "|";
			}
			downloadProtocol += remoteFile.getAbsoluteFile().getName();
			
		} // --- end for
		if (downloadProtocol.equals("")==false) {
			downloadProtocol = "Download to '" + destinPath + "': " + downloadProtocol + "";
			System.out.println(downloadProtocol);
		}
	}
	 /**
     * Deletes a folder and all subelements
     * @param directory
     */
    private void deleteFolder(File directory) {
    	
    	for (File file : directory.listFiles()) {
    		if (file.isDirectory()) {
	    		deleteFolder(file);
	    	}
	    	file.delete();
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
		
		// --------------------------------------
		// --- Class-Path configuration ---------
		classPath = getClassPath(jadeServices);
		// +++ Check for operating system +++
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
//		System.out.println( "=> Remote Execute: " + execute);
		
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
		    
			// --- Remove external jars from the download-folder ----
			if (extJarFolder!=null ) {
        		if (extJarFolder.exists()==true) {
        			deleteFolder(extJarFolder);
            		extJarFolder.delete();	
        		}
        	}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method configures the CLASSPATH for the remote-container  
	 * @param ServiceList
	 * @return
	 */
	private String getClassPath(String ServiceList) {
		
		String classPath = "";
		
		// -----------------------------------------------------
		// --- Basic - Configuration ---------------------------
		classPath += "-classpath ";
		classPath += ".;";
		
		// -----------------------------------------------------
		// --- Agent.GUI related -----
		classPath += "./lib/jade/lib/envModelGraph.jar;";
		// --- JUNG einbinden ------------------------
		classPath += "./lib/jung/collections-generic-4.01.jar;";
		classPath += "./lib/jung/colt-1.2.0.jar;";
		classPath += "./lib/jung/concurrent-1.3.4.jar;";
		classPath += "./lib/jung/jung-algorithms-2.0.1.jar;";
		classPath += "./lib/jung/jung-api-2.0.1.jar;";
		classPath += "./lib/jung/jung-graph-impl-2.0.1.jar;";
		classPath += "./lib/jung/jung-io-2.0.1.jar;";
		classPath += "./lib/jung/jung-jai-2.0.1.jar;";
		classPath += "./lib/jung/jung-samples-2.0.1.jar;";
		classPath += "./lib/jung/jung-visualization-2.0.1.jar;";
		classPath += "./lib/jung/stax-api-1.0.1.jar;";
		classPath += "./lib/jung/vecmath-1.3.1.jar;";
		classPath += "./lib/jung/wstx-asl-3.2.6.jar;";

		// -----------------------------------------------------
		// --- Jade  himself ----------
		classPath += "./lib/jade/lib/jade.jar;";
		classPath += "./lib/jade/lib/XMLCodec.jar;"; 							// xml-codec
		classPath += "./lib/jade/lib/commons-codec/commons-codec-1.3.jar;"; 	// commons-codec
		
		// -----------------------------------------------------
		// --- Configuration in relation to the JADE-Services --
		if (ServiceList.contains(PlatformJadeConfig.SERVICE_InterPlatformMobilityService)) {
			classPath += "./lib/jade/lib/migration.jar;";						// Mobility
		}
		if (ServiceList.contains(PlatformJadeConfig.SERVICE_AgentGUI_SimulationService) || ServiceList.contains(PlatformJadeConfig.SERVICE_AgentGUI_LoadService)) {
			classPath += "./lib/jade/lib/simulation.jar;";						// Load and Simulation
			// --- Hyperic-Sigar ----------
			classPath += "./lib/hyperic-sigar/sigar-bin/lib/sigar.jar;";
			classPath += "./lib/hyperic-sigar/sigar-bin/lib/log4j.jar;";
			classPath += "./lib/hyperic-sigar/sigar-bin/lib/junit.jar;";
		}
		if (ServiceList.contains(PlatformJadeConfig.SERVICE_DebugService)) {
			classPath += "./lib/jade/lib/debugging.jar;";						// Debugging
		}
		if (ServiceList.contains(PlatformJadeConfig.SERVICE_AgentGUI_P2DEnvironmentProviderService)) {
			classPath += "./lib/jade/lib/envModelP2Dsvg.jar;";
			// --- Batik einbinden ------------------------
			classPath += "./lib/batik/batik-rasterizer.jar;";
			classPath += "./lib/batik/batik-slideshow.jar;";
			classPath += "./lib/batik/batik-squiggle.jar;";
			classPath += "./lib/batik/batik-svgpp.jar;";
			classPath += "./lib/batik/batik-ttf2svg.jar;";
			classPath += "./lib/batik/batik.jar;";
			classPath += "./lib/batik/lib/xml-apis-ext.jar;";
			classPath += "./lib/batik/lib/batik-swing.jar;";
			classPath += "./lib/batik/lib/batik-util.jar;";
			classPath += "./lib/batik/lib/batik-svg-dom.jar;";
			classPath += "./lib/batik/lib/batik-transcoder.jar;";
			classPath += "./lib/batik/lib/batik-bridge.jar;";
			classPath += "./lib/batik/lib/batik-script.jar;";
			classPath += "./lib/batik/lib/batik-css.jar;";
			classPath += "./lib/batik/lib/batik-dom.jar;";
			classPath += "./lib/batik/lib/batik-ext.jar;";
			classPath += "./lib/batik/lib/xercesImpl-2.7.1.jar;";
		}
		
		// -----------------------------------------------------
		// --- Configure external jar-files -------------------- 
		if (jadeJarIncludeClassPath.size()>0) {
			for (int i=0; jadeJarIncludeClassPath.size()>i; i++) {
				String jar = (String) jadeJarIncludeClassPath.get(i);
				classPath += jar;
			}
		}
		System.out.println("Configured CLASSPATH-entry: " + classPath);
		return classPath;
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
