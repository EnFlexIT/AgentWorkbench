package de.enflexit.awb.simulation.distribution;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import de.enflexit.common.ExecutionEnvironment;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.common.transfer.ArchiveFileHandler;
import de.enflexit.common.transfer.ArchiveFileHandler.ArchiveFormat;
import de.enflexit.common.transfer.Download;
import jade.core.Profile;
import jade.util.leap.ArrayList;

/**
 * This class is only used by the {@link ServerSlaveAgent} of the background
 * system. It enables the agent to start a new process in order to extend
 * a remote JADE platform and join that platform with a new container.
 * 
 * @see ServerSlaveAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeRemoteStart {

	public static final String jvmMemo16MB 	= "16m";
	public static final String jvmMemo32MB 	= "32m";
	public static final String jvmMemo64MB 	= "64m";
	public static final String jvmMemo128MB = "128m";
	public static final String jvmMemo256MB = "256m";
	public static final String jvmMemo512MB = "512m";

	public static final String jvmMemo1GB 	= "1g";
	public static final String jvmMemo2GB 	= "2g";
	public static final String jvmMemo4GB 	= "4g";
	public static final String jvmMemo8GB 	= "8g";
	public static final String jvmMemo16GB 	= "16g";
	public static final String jvmMemo32GB 	= "32g";
	public static final String jvmMemo48GB 	= "48g";
	public static final String jvmMemo64GB 	= "64g";
	
	
	private boolean debug = false;
	
	private RemoteContainerConfig reCoCo;
	
	private boolean jvmMemAllocUseDefaults = true;
	private String jvmMemAllocInitial = JadeRemoteStart.jvmMemo128MB;
	private String jvmMemAllocMaximum = JadeRemoteStart.jvmMemo2GB;
	
	private boolean jadeIsRemoteContainer = true; 
	private boolean jadeShowGUI = true; 
	private String jadeShowGUIAgentName = "rma."; 
	private String jadeServices = "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService;de.enflexit.awb.simulation.SimulationService;de.enflexit.awb.simulation.LoadService";
	private String jadeHost = "localhost";
	private String jadePort = Application.getGlobalInfo().getJadeLocalPort().toString();
	private String jadeContainerName = "remote";
	
	private File rcProjectDirectory = null; 

	// --- Remote debug default options ---------
	private boolean isEnabledRemoteDebugging = false;
	private String jvmRemDebugTransport = "dt_socket";
	private String jvmRemDebugAddress = "8000";
	private String jvmRemDebugServer = "y";			
	private String jvmRemDebugSuspend = "y";
	
	
	/**
	 * Instantiates a new jade remote start.
	 *
	 * @param agent the current agent that uses this class
	 * @param remoteStartConfiguration the remote start configuration
	 */
	public JadeRemoteStart(JadeRemoteStartConfiguration remoteStartConfiguration) {
		this(remoteStartConfiguration.getRemoteContainerConfig());
		this.rcProjectDirectory = remoteStartConfiguration.getProjectPath();
	}
	/**
	 * Instantiates a new jade remote start.
	 *
	 * @param agent the current agent that uses this class
	 * @param remoteContainerConfig the RemoteContainerConfig
	 */
	public JadeRemoteStart(RemoteContainerConfig remoteContainerConfig) {
		
		this.reCoCo = remoteContainerConfig;
		
		if (this.debug) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Starting in debug modus !");
		}
		
		// --- Configure JVM arguments ------------------------------
		if (this.reCoCo.getJvmMemAllocInitial()==null && this.reCoCo.getJvmMemAllocMaximum()==null) {
			this.jvmMemAllocUseDefaults = true;	
			this.jvmMemAllocInitial = JadeRemoteStart.jvmMemo32MB;
			this.jvmMemAllocMaximum = JadeRemoteStart.jvmMemo128MB;
		} else {
			this.jvmMemAllocUseDefaults = false;
			this.jvmMemAllocInitial = this.reCoCo.getJvmMemAllocInitial();
			this.jvmMemAllocMaximum = this.reCoCo.getJvmMemAllocMaximum();
		}

		// --- Configure Jade settings ------------------------------
		this.jadeIsRemoteContainer = this.reCoCo.getJadeIsRemoteContainer();
		this.jadeShowGUI = this.reCoCo.getJadeShowGUI();	
		
		if (this.reCoCo.getJadeServices()!=null) {
			this.jadeServices = this.reCoCo.getJadeServices();
		}
		if (this.reCoCo.getJadeHost()!=null) {
			this.jadeHost = this.reCoCo.getJadeHost();	
		}
		if (this.reCoCo.getJadePort()!=null) {
			this.jadePort = this.reCoCo.getJadePort();	
		}
		if (this.reCoCo.getJadeContainerName()!=null) {
			this.jadeContainerName = this.reCoCo.getJadeContainerName();	
		}
	}	
	
	/**
	 * Checks if is ready to start remote container.
	 * @return true, if is ready to start remote container
	 */
	public boolean isReadyToStartRemoteContainer() {
		
		// --- If not already available, download project files -----
		if (this.rcProjectDirectory==null) {
			// --- Get download information -------------------------
			ArrayList httpDownloadFiles = (ArrayList) this.reCoCo.getHttpDownloadFiles();
			if (httpDownloadFiles==null || httpDownloadFiles.size()==0) {
				// --- No project for download ----------------------
				return true;
			}
			// --- Download the first file only (project file) ------
			this.rcProjectDirectory = this.downloadFileFromHTTPServer((String) httpDownloadFiles.get(0));
		}
		
		// --- If a remote container project can be found ----------- 
		if (this.rcProjectDirectory!=null) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Found installed remote project '" + this.rcProjectDirectory.getName() + "'");
			// --- Load project XML and check required features -----
			final Project remoteProject = Project.loadProjectXml(this.rcProjectDirectory);
			if (Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverProduct && remoteProject.requiresFeatureInstallation()==true) {
				// --- Remind the start configuration ---------------
				String configFilePath = JadeRemoteStartConfiguration.getDefaultConfigurationFile().getAbsolutePath();
				boolean isSavedConfig = JadeRemoteStartConfiguration.saveRemoteStartConfiguration(new JadeRemoteStartConfiguration(this.rcProjectDirectory, this.reCoCo));
				if (isSavedConfig==true) {
					System.out.println("[" + this.getClass().getSimpleName() + "] Saved remote start configuration to " + configFilePath);
				}
				// --- Install the required features ---------------- 
				System.out.println(this.getClass().getSimpleName() + ": Installing required project features ...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							remoteProject.installRequiredFeatures();
						} catch (Exception e) {
							System.err.println("[" + this.getClass().getSimpleName() + "] Not all required features have been installed successfully:");
							System.err.println(e.getMessage());
							//TODO figure out how to handle this
						}
					}
				}).start();
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This Method starts a Jade-Platform within a new Java Virtual Machine.
	 */
	public void startJade() {
		
		String os = SystemEnvironmentHelper.getOperatingSystem();
		
		String java = "";
		String javaVMArgs = "";
		String equinoxLauncherJar = "";
		String project = "";
		String jade = "";
		String jadeArgs = "";
		
		// --------------------------------------
		// --- Java-Config ----------------------
		java = "java";
		if (this.jvmMemAllocUseDefaults==false) {
			javaVMArgs = "-Xms" + this.jvmMemAllocInitial + " -Xmx" + this.jvmMemAllocMaximum;
		} 
		// --- Enable remote debugging ----------
		if (this.isEnabledRemoteDebugging==true) {
			javaVMArgs += " -Xrunjdwp:transport=" + this.jvmRemDebugTransport + ",address=" + this.jvmRemDebugAddress + ",server=" + this.jvmRemDebugServer + ",suspend=" + this.jvmRemDebugSuspend + "";
		}
		
		// --------------------------------------
		// --- Class-Path configuration ---------
		equinoxLauncherJar = this.getEquinoxLauncherJar();
		// +++ Check for operating system +++
		if (os.toLowerCase().contains("windows")==true) {
			// --- nothing to do here ---
		} else if (os.toLowerCase().contains("linux")==true) {
			equinoxLauncherJar = equinoxLauncherJar.replaceAll(";", ":");
		}

		// --------------------------------------
		// --- The project to open --------------
		if (this.rcProjectDirectory!=null) {
			project += "-project " + this.rcProjectDirectory.getName();
		}
		
		// --------------------------------------
		// --- Jade configuration ---------------
		jade += "-jade" + " ";
		if (this.jadeServices!=null) {
			jadeArgs += "-services " + this.jadeServices + " ";
		}
		if (this.jadeIsRemoteContainer) {
			jadeArgs += "-container ";
			jadeArgs += "-container-name " + this.jadeContainerName + " ";
		} 
		jadeArgs += "-host " + this.jadeHost + " ";
		jadeArgs += "-port " + this.jadePort + " ";
		// -- Configure -local-host -------------
		String localHost = Application.getGlobalInfo().getJadeDefaultProfile().getProperties().getProperty(Profile.LOCAL_HOST);
		if (localHost!=null && localHost.equals("")==false) {
			jadeArgs += "-local-host " + localHost + " ";	
		}
		// --- Show RMA UI? ---------------------
		if (this.jadeShowGUI==true) {
			jadeArgs += this.jadeShowGUIAgentName + this.jadeContainerName + ":jade.tools.rma.rma ";
		}		
		jadeArgs = jadeArgs.trim();
		
		// --------------------------------------
		// --- merge execute statement ----------
		String execute = java + " " + javaVMArgs + " " + equinoxLauncherJar +  " " + project + " " + jade  + " " + jadeArgs;
		execute = execute.replace("  ", " ");
		System.out.println("[" + this.getClass().getSimpleName() + "] Execute: " + execute);
		
		// --------------------------------------
		Scanner in = null;
		Scanner err = null;
		try {
			
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(true);
			proBui.directory(Application.getGlobalInfo().getPathBaseDir().toFile());
			
			Process process = proBui.start();
			
			in = new Scanner(process.getInputStream());
			in.useDelimiter("\\Z");
			
			err = new Scanner(process.getErrorStream());
			err.useDelimiter("\\Z");
			
			while (in.hasNextLine() || err.hasNextLine() ) {
				if (in.hasNextLine()) {
					System.out.println("[" + this.jadeContainerName + "]: " + in.nextLine());	
				}
				if (err.hasNextLine()){
					System.err.println("[" + this.jadeContainerName + "]: " + err.nextLine());	
				}
			}
			System.out.println("Killed Container [" + this.jadeContainerName + "]");
		    
			// ------------------------------------------------------
			// --- Remove the 'rc-*' project directory --------------
			// ------------------------------------------------------
			Application.getProjectsLoaded().projectDelete(this.rcProjectDirectory);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in!=null) in.close();
			if (err!=null) err.close();	
		}

	}

	/**
	 * Returns the command line part for the equinox launcher jar.
	 * @return the equinox launcher jar
	 */
	private String getEquinoxLauncherJar() {
		// --- Get the equinox launcher ------------------------
		String execJar = Application.getGlobalInfo().getFileRunnableJar();
		if (execJar==null) return null;
		execJar = execJar.replace("\\", "/");
		return "-jar " + execJar;
	}
	
	/**
	 * Checks if is memory allocation for the new JVM will be the default one.
	 *
	 * @return the jvmMemAllocUseDefaults
	 */
	public boolean isJVMMemAllocUseDefaults() {
		return jvmMemAllocUseDefaults;
	}
	/**
	 * Sets the memory allocation for the new JVM to be the default value or not.
	 *
	 * @param useDefaults the new jVM mem alloc use defaults
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

	
	/**
	 * Downloads the specified file from a HTTP-server.
	 *
	 * @param httpDownloadURL the file download URL
	 * @param localFileName the local file name
	 * @return true, if successful
	 */
	private File downloadFileFromHTTPServer(String httpDownloadURL) {
		
		if (httpDownloadURL==null || httpDownloadURL.isBlank()==true) return null;
		
		// ------------------------------------------------------------------------------
		// --- Download the specified file ----------------------------------------------
		// ------------------------------------------------------------------------------		
		// --- As example: http://192.168.178.38:8081/peak_virtual_pilot.agui ----
		String localFileNameWithoutHTTP = httpDownloadURL.substring( httpDownloadURL.indexOf(":") + 3);
		String localFileName = localFileNameWithoutHTTP.substring(localFileNameWithoutHTTP.indexOf("/") + 1);
		localFileName = localFileName.replace("/", File.separator);
		
		String dirPathDownload = Application.getGlobalInfo().getResourceDistributionDownloadPath(true);
		String filePathDownload = dirPathDownload + localFileName;
		
		Download download = new Download(httpDownloadURL, filePathDownload);
		System.out.println("[" + this.getClass().getSimpleName() + "] Starting download of " + httpDownloadURL + " to " + filePathDownload + " ... ");
		download.startDownload();
		
		
		// ------------------------------------------------------------------------------
		// --- Extract the zip-file *.agui ----------------------------------------------
		// ------------------------------------------------------------------------------
		File targetProjectDirectory = null;
		File dirDownload = new File(dirPathDownload);
		File fileDownload = new File(filePathDownload);
		if (dirDownload.exists()==true && fileDownload.exists()==true) {
			
			// --- Get the regular projects sub directory name ------
			String projectSubDirectoryPath = fileDownload.getName();
			int pos = projectSubDirectoryPath.lastIndexOf(".");
			if (pos > 0) {
				projectSubDirectoryPath = projectSubDirectoryPath.substring(0, pos);
			}
			
			// --- Define target directory --------------------------
			Integer prefixCounter = 1;
			String dirPathProjects = Application.getGlobalInfo().getPathProjects();
			while (targetProjectDirectory==null || targetProjectDirectory.exists()==true) {
				String projectPrefix = "rc" + String.format("%02d", prefixCounter) + "-";
				targetProjectDirectory = new File(dirPathProjects + projectPrefix + projectSubDirectoryPath);
				prefixCounter++;
			}
			
			// --- Extract project file into download directory -----
			ArchiveFileHandler extractor = new ArchiveFileHandler();
			System.out.println("[" + this.getClass().getSimpleName() + "] Decompress file " + fileDownload.getName() + " to " + targetProjectDirectory.getParentFile() + " ... ");
			extractor.decompressFolder(fileDownload, targetProjectDirectory.getParentFile(), ArchiveFormat.ZIP, targetProjectDirectory.getName());
			
			// --- Remove downloaded project file -------------------
			if (fileDownload.delete()==false) {
				fileDownload.deleteOnExit();
			}
			
		}
		return targetProjectDirectory;
	}
	
}
