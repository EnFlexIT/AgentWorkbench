package de.enflexit.awb.core.config;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

import agentgui.core.charts.timeseriesChart.StaticTimeSeriesChartConfiguration;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentType;
import de.enflexit.awb.core.environment.EnvironmentTypeServiceFinder;
import de.enflexit.awb.core.environment.EnvironmentTypes;
import de.enflexit.awb.core.jade.JadeUrlConfiguration;
import de.enflexit.awb.core.project.PlatformJadeConfig;
import de.enflexit.awb.core.project.PlatformJadeConfig.MTP_Creation;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelDateBased;
import de.enflexit.awb.core.project.Project;
import de.enflexit.common.ExecutionEnvironment;
import de.enflexit.common.GlobalRuntimeValues;
import de.enflexit.common.PathHandling;
import de.enflexit.common.VersionInfo;
import de.enflexit.common.ZoneIdResolver;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.language.Language;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;

/**
 * This class is for constant values or variables that can be accessed or used application wide.<br>
 * In the Application class the running instance can be accessed by using {@link Application#getGlobalInfo()}. 
 * 
 * @see Application#getGlobalInfo()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GlobalInfo implements ZoneIdResolver {

	// --- Constant values ------------------------------------------ 
	private static String localAppTitle = "Agent.Workbench";
	
	private final static String localPathImageAWB = "/icons/";
	private final static String localPathImageIntern = "/icons/core/";
	
	public final static String DEFAULT_AWB_PROJECT_REPOSITORY = "https://p2.enflex.it/awbProjectRepository/";
	public static final String DEFAULT_OIDC_ISSUER_URI = "https://se238124.zim.uni-due.de:8443/auth/realms/EOMID/";

	private final static String newLineSeparator = System.getProperty("line.separator");
	
	private static Color localColorMenuHighLight;
	
	
	private Integer localeJadeLocalPort = 1099;
	private Integer localeJadeLocalPortMTP = 7778;
	private JadeUrlConfiguration urlConfigurationForMaster;
	
	// --- Variables ------------------------------------------------
	private static ExecutionEnvironment localExecutionEnvironment;
	
	public final static String DEFAUL_LOOK_AND_FEEL_CLASS = AwbLookAndFeelAdjustments.DEFAUL_LOOK_AND_FEEL_CLASS;
	private static String localAppLnF = DEFAUL_LOOK_AND_FEEL_CLASS;
	private static Class<?> localAppLnFClass;
	
	private static Path localBaseDir;
	
	private static String localFileProperties  = "agentgui.ini";
	private static String localFileNameProject = "agentgui.xml";
	private static String localFileNameProjectUserObjectBinFile = "agentgui.bin";
	private static String localFileNameProjectUserObjectXmlFile = "agentgui-UserObject.xml";
	private static String localFileEndProjectZip = "agui";
	
	// --- Known EnvironmentTypes of Agent.Workbench ----------------
	private EnvironmentTypes knownEnvironmentTypes;
	
	// --- PropertyContentProvider ----------------------------------
	private PropertyContentProvider propertyContentProvider;
	
	// --- File-Properties ------------------------------------------
	private AWBProduct awbProduct;
	private ExecutionMode fileExecutionMode;
	private String processID;
	
	private Boolean isUpdatedAwbCoreBundle;
	
	private String filePropProjectsDirectory;
	
	private float filePropBenchValue = 0;
	private String filePropBenchExecOn;
	private boolean filePropBenchAlwaysSkip; 
	
	private boolean maximizeMainWindow = false;
	
	private boolean filePropLoggingEnabled;
	private String filePropLoggingBasePath;
	
	private boolean filePropServerAutoRun = true;
	private String filePropServerMasterURL;
	private Integer filePropServerMasterPort = this.localeJadeLocalPort;
	private Integer filePropServerMasterPort4MTP = 7778;
	private MtpProtocol filePropServerMasterProtocol = MtpProtocol.HTTP;
	
	private MTP_Creation filePropOwnMtpCreation = MTP_Creation.ConfiguredByJADE;
	private String filePropOwnMtpIP = PlatformJadeConfig.MTP_IP_AUTO_Config;
	private Integer filePropOwnMtpPort = 7778;
	private MtpProtocol filePropMtpProtocol = MtpProtocol.HTTP;
	
	private Integer updateAutoConfiguration = 0;
	private Integer updateKeepDictionary = 1;
	private long updateDateLastChecked = 0;
	
	private String deviceServiceProjectFolder;
	private DeviceSystemExecutionMode deviceServiceExecutionMode = DeviceSystemExecutionMode.SETUP;
	private String deviceServiceSetupSelected;  
	private Vector<DeviceAgentDescription> deviceServiceAgents;
	private EmbeddedSystemAgentVisualisation deviceServiceAgentVisualisation = EmbeddedSystemAgentVisualisation.TRAY_ICON;
	
	private String filePropKeyStoreFile;
	private String filePropKeyStorePasswordEncrypted;
	private String filePropTrustStoreFile;
	private String filePropTrustStorePasswordEncrypted;
	
	private String oidcUsername;
	private String oidcIssuerURI;

	// --- BundleProperties and VersionInfo -------------------------
	private BundleProperties bundleProperties;
	private VersionInfo versionInfo;

	/**
	 * The Enumeration of possible products of Agent.Workbench.
	 * In order to get the current execution mode use  
	 * the method {@link GlobalInfo#getExecutionMode()} and {@link GlobalInfo#getExecutionModeDescription()} 
	 * @see GlobalInfo#getExecutionMode()
	 */
	public enum AWBProduct {
		WEB("de.enflexit.awb.ws.core.product", "Web"),
		DESKTOP_SWING("de.enflexit.awb.core.product", "Desktop"), 
		DESKTOP_SWT("de.enflexit.awb.desktop.swt.product", "RCP-Desktop"); 

		private final String productID;
		private final String productName;
		
		private AWBProduct(String productID, String productName) {
			this.productID   = productID;
			this.productName = productName;
		}
		public String getProductID() {
			return productID;
		}
		public String getProductName() {
			return productName;
		}
	}
	
	/**
	 * The Enumeration of possible ExecutionModes.
	 * In order to get the current execution mode use  
	 * the method {@link GlobalInfo#getExecutionMode()} and {@link GlobalInfo#getExecutionModeDescription()} 
	 * @see GlobalInfo#getExecutionMode()
	 */
	public enum ExecutionMode {
		APPLICATION, SERVER, SERVER_MASTER, SERVER_SLAVE, DEVICE_SYSTEM
	}

	/**
	 * The Enumeration of possible ExecutionModes. In order to get the current execution mode use  
	 * the method {@link GlobalInfo#getExecutionMode()} and {@link GlobalInfo#getExecutionModeDescription()} 
	 * @see GlobalInfo#getExecutionMode()
	 */
	public enum DeviceSystemExecutionMode {
		SETUP, AGENT
	}

	/**
	 * The Enumeration EmbeddedSystemAgentVisualisation indicates
	 * if and how visualizations are to be displayed in case that
	 * the {@link ExecutionMode} is set to {@link ExecutionMode#DEVICE_SYSTEM}
	 * or embedded system agent respectively.
	 */
	public enum EmbeddedSystemAgentVisualisation {
		NONE, TRAY_ICON
	}
	
	/**
	 * The Enumeration MtpProtocol describes the usable protocols
	 * for the message exchange between agent platforms/container
	 */
	public enum MtpProtocol {
		HTTP, HTTPS
	}
	
	/**
	 * Constructor of this class. 
	 */
	public GlobalInfo() {
		this.initialize();
	}
	/**
	 * Initializes this class by reading the file properties and the current version information.
	 */
	public void initialize() {
		
		try {
			// --- Do the necessary things ----------------
			GlobalRuntimeValues.setZoneIdResolver(this);
			StaticTimeSeriesChartConfiguration.setStaticTimeSeriesSettingEvaluator(new TimeSeriesSettingEvaluator());
			
			this.getVersionInfo();
			this.getBundleProperties();
			this.setApplicationsLookAndFeel();
			
			// --- Some debug stuff -----------------------
			boolean debug = false;
			if (debug==true) {
				System.out.println(localAppTitle + " => BasePath: " + this.getPathBaseDir().toString() + ", ExecEnv: " + this.getExecutionEnvironment().name());
				GlobalInfo.printPlatformLocations();
				GlobalInfo.println4SysProps();
				GlobalInfo.println4EnvProps();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Checks if the currently running AWB core bundle is an updated version in comparison 
	 * to the previously executes version.
	 * 
	 * @return true, if the current core bundle version is different to the previous execution
	 */
	public boolean isUpdatedAwbCoreBundle() {
		if (isUpdatedAwbCoreBundle==null) {
			// --- Get the current bundle version -------------------
			Bundle bundle = FrameworkUtil.getBundle(GlobalInfo.class);
			if (bundle==null) return false;
			
			Version version = bundle.getVersion();
			String bundleVersionString = version.toString();
			// --- Get the reminded bundle version ------------------
			String propsVersionString = this.getStringFromConfiguration(BundleProperties.DEF_AWG_CORE_BUNLDE_VERSION, null);
			if (propsVersionString==null || propsVersionString.isEmpty()==true || propsVersionString.equals(bundleVersionString)==false) {
				// --- A different AWB core bundle was executed -----
				this.putStringToConfiguration(BundleProperties.DEF_AWG_CORE_BUNLDE_VERSION, bundleVersionString);
				isUpdatedAwbCoreBundle = true;
				
			} else {
				isUpdatedAwbCoreBundle = false;
			}
		}
		return isUpdatedAwbCoreBundle;
	}
	
	/**
	 * Prints all locations provided by the {@link Platform} instance.
	 */
	public static void printPlatformLocations() {
		
		System.out.println();
		System.out.println("------------------------------------");  
		System.out.println("------- Platform Locations: --------");  
		System.out.println("------------------------------------");
		
		System.out.println("Location	=> " + Platform.getLocation().toString());
		System.out.println("Configuration	=> " + Platform.getConfigurationLocation().getURL()); 
		System.out.println("InstallLocation	=> " + Platform.getInstallLocation().getURL());
		System.out.println("InstanceLocation	=> " + Platform.getInstanceLocation().getURL());
		System.out.println("LogFileLocation	=> " + Platform.getLogFileLocation().toString());
		System.out.println("UserLocation	=> " + Platform.getUserLocation().getURL());
		System.out.println();
	}
	/**
	 * This method prints out every available value of the system properties
	 */
	public static void println4SysProps() {

		System.out.println();
		System.out.println("------------------------------------");  
		System.out.println("------- System Properties: ---------");  
		System.out.println("------------------------------------");

		ArrayList<Object> propertiesKeys = new ArrayList<>(System.getProperties().keySet());
		Collections.sort(propertiesKeys, new Comparator<Object>() {
			@Override
			public int compare(Object prop1, Object prop2) {
				return prop1.toString().compareTo(prop2.toString());
			}
		});

		for (int i = 0; i < propertiesKeys.size(); i++) {
			String property = (String) propertiesKeys.get(i);
			String propertyValue = System.getProperty(property);
			if (propertyValue!=null) propertyValue = propertyValue.replace("\n", " ");
			System.out.println(property + " = \t" + propertyValue);
		}
	}
	/**
	 * This method prints out every available value of the system environment
	 */
	public static void println4EnvProps() {
		
		System.out.println();
		System.out.println("------------------------------------");  
		System.out.println("-------  System Environment: -------"); 
		System.out.println("------------------------------------");
		
		ArrayList<String> propertiesKeys = new ArrayList<>(System.getenv().keySet());
		Collections.sort(propertiesKeys, new Comparator<Object>() {
			@Override
			public int compare(Object prop1, Object prop2) {
				return prop1.toString().compareTo(prop2.toString());
			}
		});
		
		
		for (int i = 0; i < propertiesKeys.size(); i++) {
			String property = (String) propertiesKeys.get(i);
			String propertyValue = System.getenv(property);
			if (propertyValue!=null) propertyValue = propertyValue.replace("\n", " ");
			System.out.println(property + " = \t" + propertyValue);  
		} 
	}
	
	/**
	 * Returns the system information that is to be shown in the {@link AboutDialog}.
	 * @return the system information
	 */
	public String getSystemInformation() {
		
		StringBuilder sysInfo = new StringBuilder();
		
		sysInfo.append(this.getVersionInfo().getFullVersionInfo(true, true, "") + newLineSeparator);
		sysInfo.append(this.getVersionInfo().getJavaInfo() + newLineSeparator);
		sysInfo.append(newLineSeparator);

		sysInfo.append("ExecutionMode: 	" + this.getExecutionModeDescription() + newLineSeparator);
		sysInfo.append("Executed Over: 	" + this.getExecutionEnvironment() + newLineSeparator);
		sysInfo.append("AWB Base Directory: 	" + this.getPathBaseDir() + newLineSeparator);
		sysInfo.append("Property Directory: 	" + this.getPathProperty(true).toString() + newLineSeparator);
		sysInfo.append("Project Directory: 	" + this.getPathProjects() + newLineSeparator);
		
		return sysInfo.toString();
	}
	
	
	/**
	 * Returns the current AWB product that is currently executed.
	 * @return the AWB product
	 */
	public AWBProduct getAWBProduct() {
		if (awbProduct==null) {
			String currProductID = Platform.getProduct().getId();
			for (AWBProduct awbProductCheck : AWBProduct.values()) {
				if (awbProductCheck.getProductID().equals(currProductID)==true) {
					this.awbProduct = awbProductCheck;
					break;
				}
			}
		}
		return awbProduct;
	}
	
	
	/**
	 * Sets the execution mode.
	 * @param executionMode the new execution mode
	 */
	public void setExecutionMode(ExecutionMode executionMode) {
		this.fileExecutionMode = executionMode;
	}
	
	/**
	 * Returns the execution mode from configuration (e.g. {@link ExecutionMode#APPLICATION}).
	 * @return the execution mode from configuration
	 * @see ExecutionMode
	 */
	public ExecutionMode getExecutionMode() {
	
		ExecutionMode execMode = this.fileExecutionMode; 
		if (execMode==ExecutionMode.SERVER) {
			// ------------------------------------------------------
			// --- Does JADE run? -----------------------------------
			// ------------------------------------------------------			
			AgentContainer mainContainer = Application.getJadePlatform().getMainContainer();
			if (mainContainer!=null) {
				// --------------------------------------------------
				// --- JADE is running ------------------------------
				// --------------------------------------------------
				// --- Default: Running as Server [Slave] -----------
				execMode = ExecutionMode.SERVER_SLAVE;

				JadeUrlConfiguration masterConfigured = this.getJadeUrlConfigurationForMaster();
				if (masterConfigured.isLocalhost()==true) {
					// --- Compare to MainContainer address ---------
					JadeUrlConfiguration platformRunning = new JadeUrlConfiguration(mainContainer.getPlatformName());
					if (platformRunning.isEqualJadePlatform(masterConfigured)==true) {
						// --- Running as Server [Master] -----------
						execMode = ExecutionMode.SERVER_MASTER;
					}
				}
				
			}
			// ------------------------------------------------------
		}
		return execMode;
	}
	
	/**
	 * Returns the systems process ID of the JVM.
	 * @return the process ID
	 */
	public String getProcessID() {
		if (processID==null) {
			String localProcessName = ManagementFactory.getRuntimeMXBean().getName();
			processID = "PID_" + localProcessName.substring(0, localProcessName.indexOf("@"));
		}
		return processID;
	}
	
	/**
	 * Gets the execution mode description of the current {@link ExecutionMode}.
	 * @return the execution mode description
	 */
	public String getExecutionModeDescription() {
		return this.getExecutionModeDescription(this.getExecutionMode());
	}
	/**
	 * Returns the execution mode description.
	 *
	 * @param executionMode the execution mode
	 * @return the execution mode description
	 */
	public String getExecutionModeDescription(ExecutionMode executionMode) {

		String executionModeDescription = null;
		switch (executionMode) {
		case APPLICATION:
			executionModeDescription = Language.translate("Anwendung");
			break;
		case SERVER:
			executionModeDescription = "Server";
			break;
		case SERVER_MASTER:
			executionModeDescription = "Server [Master]";
			break;
		case SERVER_SLAVE:
			executionModeDescription = "Server [Slave]";
			break;
		case DEVICE_SYSTEM:
			executionModeDescription = Language.translate("Dienst / Embedded System Agent");
			break;
		}
		return executionModeDescription;
	}
	
	/**
	 * Checks/Returns if the {@link BundleEvaluator} has to be started.
	 * @return true, if the BundleEvaluator needs to started
	 */
	public boolean isStartBundleEvaluator() {
		boolean isApplication = this.getExecutionMode()==ExecutionMode.APPLICATION;
		boolean isDeviceSystem = this.getExecutionMode()==ExecutionMode.DEVICE_SYSTEM;
		//boolean isDeviceSystemAgent = this.getDeviceServiceExecutionMode()==DeviceSystemExecutionMode.AGENT;
		//boolean isDeviceSystemProjectSetup = this.getDeviceServiceExecutionMode()==DeviceSystemExecutionMode.SETUP;
		return isApplication || isDeviceSystem;
	}
	
	
	/**
	 * Returns the title/name of the application
	 * @return the current application title
	 */
	public String getApplicationTitle() {
		return localAppTitle;
	};
	/**
	 * This method can be used in order to set a new application title.
	 * For the actual renaming of the Application, use the method in the
	 * class agentgui.core.Application
	 * 
	 * @param newApplicationTitle the Application Title to set
	 * @see Application#setApplicationTitle(String)
	 */
	public void setApplicationTitle(String newApplicationTitle) {
		GlobalInfo.localAppTitle = newApplicationTitle;
	}
	
	// -------------------------------
	// --- Look and Feel -------------
	// -------------------------------
	/**
	 * Sets the applications look and feel.
	 */
	private void setApplicationsLookAndFeel() {
		AwbLookAndFeelAdjustments.setLookAndFeel(this.getAppLookAndFeelClassName(), null);
	}
	/**
	 * This method will return the current Look and Feel (LnF) for Java Swing.<br>
	 * Also the class reference will be validated. If the class could not be found the 
	 * method will return null.
	 * 
	 * @return the class reference for the current look and feel 
	 * or null, if the class could not be found
	 */
	public String getAppLookAndFeelClassName() {
		if (localAppLnFClass==null) {
			try {
				// --- Check current Look and Feel class --
				localAppLnFClass = ClassLoadServiceUtility.forName(localAppLnF);
			} catch (ClassNotFoundException cnfEx) {
				//cnfEx.printStackTrace();
			}
		}
		if (localAppLnFClass!=null) {
			return localAppLnFClass.getName();
		}
		return null;
	}
	/**
	 * Set the current Look and Feel (LnF) for Java Swing by using its class reference.
	 * @param newLnF the new Swing - Look and Feel 
	 */
	public void setAppLookAndFeelClassName(String newLnF) {
		localAppLnF = newLnF;
		localAppLnFClass = null;
	}
	// -------------------------------

	
	
	/**
	 * This method can be used in order to evaluate how Agent.GUI is currently executed
	 * @return - 'IDE', if Agent.GUI is executed out of the IDE, where Agent.GUI is developed OR<br>
	 * - 'Executable', if Agent.GUI is running as executable jar-File
	 */
	public ExecutionEnvironment getExecutionEnvironment() {
		if (localExecutionEnvironment==null) {
			localExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment(this.getClass());
		}
		return localExecutionEnvironment;
	}
	
	/**
	 * This method returns the actual String for a new line string
	 * @return a String that can be used for new lines in text  
	 */
	public String getNewLineSeparator(){
		return newLineSeparator;
	}

	// ----------------------------------------------------
	// --- General Directory information ------------------
	// ----------------------------------------------------
	/**
	 * This method returns the base path of the application (e. g. 'C:\Program Files\Agent.Workbench\')
	 * @return the base path of the application
	 */
	public Path getPathBaseDir( ) {
		if (localBaseDir==null) {
			localBaseDir = PathHandling.getApplicationBasePath(this.getClass());
		}
		return localBaseDir;
	}	

	
	/**
	 * Creates the directory if not already there and thus required.
	 * @param pathString the path string
	 */
	private void createDirectoryIfRequired(String pathString) {
		// --- Check, if the folder exists. If not create -----------
		File testDirectory = new File(pathString);
		//System.err.println(testDirectory.getAbsolutePath());
		if (testDirectory.exists()==false) {
			testDirectory.mkdir();
		}
	}
	
	/**
	 * This method can be invoked in order to get the path to the property folder 'properties\'.
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path reference to the property folder
	 */
	public Path getPathProperty(boolean absolute){
		
		Path pathProperties = PathHandling.getPropertiesPath(this.getClass(), absolute);
		// --- Do check with PropertyContentProvider ------
		this.getPropertyContentProvider(pathProperties.toFile());
		return pathProperties;
	}
	/**
	 * Returns the property content provider.
	 * @return the property content provider
	 */
	public PropertyContentProvider getPropertyContentProvider() {
		if (propertyContentProvider==null) {
			propertyContentProvider = this.getPropertyContentProvider(this.getPathProperty(true).toFile());
		}
		return propertyContentProvider;
	}
	
	/**
	 * Returns the property content provider.
	 *
	 * @param propertiesFile the properties file
	 * @return the property content provider
	 */
	private PropertyContentProvider getPropertyContentProvider(File propertiesFile) {
		if (propertyContentProvider==null) {
			propertyContentProvider = new PropertyContentProvider(propertiesFile);
			try {
				propertyContentProvider.checkAndProvideFullPropertyContent();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return propertyContentProvider;
	}
	
	/**
	 * This method will return the concrete path to the property file 'agentgui.ini' relatively or absolute
	 * @param absolute set true if you want to get the full path to this
	 * @return the path reference to the property file agentgui.ini
	 */
	public String getPathConfigFile(boolean absolute) {
		return this.getPathProperty(absolute).resolve(localFileProperties).toString();
	}
	
	
	/**
	 * Sets the 'projects' base directory.
	 * @param newProjectsDirectory the new path to AWB projects
	 */
	public void setPathProjects(String newProjectsDirectory) {
		this.filePropProjectsDirectory = newProjectsDirectory;
	}
	/**
	 * This method will return the absolute path to the base projects directory ('projects\').
	 * If the path does not exists, the path will be created.
	 * @return the path to the project folder
	 */
	public String getPathProjects(){
		return this.getPathProjects(true);
	}
	/**
	 * This method will return the path to the project folder ('./projects').
	 *
	 * @param forcePathCreation if true, the path will be created if it not already exists
	 * @return the path to the project folder
	 */
	public String getPathProjects(boolean forcePathCreation) {
		if (filePropProjectsDirectory==null) {
			filePropProjectsDirectory = this.getDefaultProjectsDirectory();
		}
		if (filePropProjectsDirectory.endsWith(File.separator)==false) {
			filePropProjectsDirectory += File.separator;
		}
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(filePropProjectsDirectory);
		}
		
		// --- Do a final check if the directory exists ---
		File projectsDir = new File(filePropProjectsDirectory);
		if (projectsDir.exists()==false) {
			// --- Switch to default location -------------
			System.err.println("[projects directory] Could not find directory '" + filePropProjectsDirectory + "'");
			filePropProjectsDirectory = this.getDefaultProjectsDirectory();
			this.createDirectoryIfRequired(filePropProjectsDirectory);
			System.err.println("[projects directory] => Switched to default location '" + filePropProjectsDirectory + "'");
			
		} else {
			// --- Convert to canonical file object -------
			try {
				projectsDir = projectsDir.getCanonicalFile();
				filePropProjectsDirectory = projectsDir.getAbsolutePath();
				if (filePropProjectsDirectory.endsWith(File.separator)==false) {
					filePropProjectsDirectory += File.separator;
				}
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return filePropProjectsDirectory;
	}
	/**
	 * Will return the path to the project folder ('./projects').
	 * @return the path to the project folder
	 */
	public Path getPathProjectsAsPath() {
		return Path.of(this.getPathProjects(true));
	}
	
	/**
	 * Returns the default projects directory.
	 * @return the default projects directory
	 */
	public String getDefaultProjectsDirectory() {
		
		String baseDir = this.getPathBaseDir().toString() + File.separator;
		if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
			// --- If in the IDE environment, the git structure has to be considered ----
			String relPathFromBaseDirGit = "../../../../awbProjects";
			File ideProjectsDir = new File(baseDir + relPathFromBaseDirGit);
			if (ideProjectsDir.exists()==true) {
				try {
					return ideProjectsDir.getCanonicalPath();
				} catch (IOException ioEx) {
					//ioEx.printStackTrace();
				}
			}
		}
		return baseDir + "projects" + File.separator;
	}

	/**
	 * Returns the list of project folders, which are located in the root project folder
	 * @return Array of project folders
	 */
	public String[] getProjectSubDirectories(){		
		// --- Search for sub folders ---
		String[] localProjects = null;
		File maindir = new File(this.getPathProjects()) ;
		File files[] = maindir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory() && !files[i].getName().substring(0, 1).equals(".") ) {
				if (localProjects == null) {						
					String[] AddEr = { files[i].getName() };	
					localProjects = AddEr;	
				} else {
					String[] AddEr = new String[localProjects.length+1];
					System.arraycopy( localProjects, 0, AddEr, 0, localProjects.length );
					AddEr[AddEr.length-1] = files[i].getName();
					localProjects = AddEr;
				}
			} 
		}
		return localProjects;		
	}	
	
	// ---- Methods to get local path information for the resource distribution ---------
	/**
	 * Returns the absolute base path for the resource distribution server.
	 *
	 * @param forceDirectoryCreation the indicator to force the directory creation
	 * @return the server path for the resource distribution
	 */
	public String getResourceDistributionServerPath(boolean forceDirectoryCreation) {
		String subPathServer = "resServer";
		String returnPath = this.getFilePathAbsolute(subPathServer).toString();
		returnPath = returnPath.endsWith(File.separator)==true ? returnPath : returnPath + File.separator;
		if (forceDirectoryCreation==true) this.createDirectoryIfRequired(returnPath);
		return returnPath;
	}
	/**
	 * This method returns the folder, where download files, coming from an applications (server.client) 
	 * can be stored locally. If the folder doesn't exists, it will be created.
	 *
	 * @param forceDirectoryCreation set true, if the directory should be created
	 * @return path to the folder, where downloads will be saved ('/AgentGUI/fmDownload[PID]/')
	 */
	public String getResourceDistributionDownloadPath(boolean forceDirectoryCreation) {
		String pathDownload = "resDownload";
		String returnPath = this.getFilePathAbsolute(pathDownload).toString();
		returnPath = returnPath.endsWith(File.separator)==true ? returnPath : returnPath + File.separator;
		if (forceDirectoryCreation==true) this.createDirectoryIfRequired(returnPath);
		return returnPath;
	}

	
	/**
	 * Returns the eclipse launcher file.
	 * @return the eclipse launcher
	 */
	public File getEclipseLauncher() {
		
		File eclipseLauncher = null;
		if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
			// ------------------------------------------------------
			// --- Executed in the IDE ------------------------------
			// ------------------------------------------------------
			String instDirPath = this.getStringFromConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, null);
			if (instDirPath==null) return null;
			
			// --- Check if directory exists ------------------------
			File execDirectory = new File(instDirPath);
			if (execDirectory.exists()==false) return null;
			
			// --- Get executable files in directory ----------------
			List<File> execFileList = new ArrayList<>();
			File[] filesInDir =  execDirectory.listFiles();
			for (int i = 0; i < filesInDir.length; i++) {
				File checkFile = filesInDir[i];
				if (checkFile.isDirectory()==false && checkFile.canExecute()==true) {
					String checkFileName = checkFile.getName();
					boolean isAgentGuiExecutable = checkFileName.startsWith("Agent") && checkFileName.endsWith(".ini")==false && checkFileName.endsWith(".bat")==false;
					if (isAgentGuiExecutable==true ) {
						execFileList.add(checkFile);
					}
				}
			}
			// --- Define return value ------------------------------
			if (execFileList.size()>0) {
				eclipseLauncher = execFileList.get(0);
			}
			
		} else {
			// ------------------------------------------------------
			// --- Executed in the product --------------------------
			// ------------------------------------------------------
			eclipseLauncher = new File(System.getProperty("eclipse.launcher"));
		}
		return eclipseLauncher;
	}
	
	/**
	 * This method can be used in order to get the applications executable 
	 * jar-file that is in fact the equinox launcher.
	 * @return the absolute path to the executable jar file
	 */
	public String getFileRunnableJar(){
		return this.getFileRunnableJar(null, null);
	}
	
	/**
	 * This method can be used in order to get the applications executable jar-file that is in fact the equinox launcher.
	 * Set the installation path parameter to <code>null</code> to receive the executable for this application. Set this 
	 * parameter to a specific one, in order to check any directory for an installation.  
	 *
	 * @param installationPath an initial installation path that allows to check any directory
	 * @param statusMessageList define an ArrayList here to receive the errors found by the method 
	 * @return the absolute path to the executable jar file
	 */
	public String getFileRunnableJar(String installationPath, ArrayList<String> statusMessageList){
		
		String execJarFile = null;
		try {
			// --- Get installation directory -----------------------
			String instDirPath = null;
			if (installationPath!=null) {
				instDirPath = installationPath;
			} else {
				// --- Case separation IDE / product ----------------
				if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
					// --- For the IDE environment ------------------
					instDirPath = this.getStringFromConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, null);
					
				} else if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverProduct) {
					// --- For the product --------------------------
					File installDir = null;
					try {
						installDir = new File(Platform.getInstallLocation().getURL().toURI());
					} catch (URISyntaxException uriEx) {
						//uriEx.printStackTrace();
						installDir = new File(Platform.getInstallLocation().getURL().getPath());
					}
					if (installDir==null || installDir.exists()==false) {
						// --- Backup solution ----------------------
						File launcherfile = new File(System.getProperty("eclipse.launcher"));
						instDirPath = launcherfile.getParentFile().getAbsolutePath();
					} else {
						instDirPath = installDir.getAbsolutePath();
					}
				}
				
			}

			if (instDirPath==null || instDirPath.isEmpty()==true) {
				this.setErrorMessage(Language.translate("Could not find directory for an executable installation.", Language.EN), statusMessageList);
				return null;
			}

			// --- Get File object of installation directory --------
			File instDir = new File(instDirPath);
			if (instDir==null || instDir.exists()==false) {
				this.setErrorMessage(Language.translate("Could not find the installation directory", Language.EN) + " '" + instDirPath + "'.", statusMessageList);
				return null;
			}
			
			// --- Find the plugins directory -----------------------
			File[] searchResult =  instDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return (file.isDirectory() && file.getName().equals("plugins"));
				}
			});
			if (searchResult.length==0) {
				this.setErrorMessage(Language.translate("Could not find sub directory 'plugins'.", Language.EN) + " (in " +  instDir.getAbsolutePath() + ")", statusMessageList);
				return null;
			}  
			
			// --- Find the equinox launcher ------------------------
			File plugins = searchResult[0];
			searchResult =  plugins.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()==false && file.getName().startsWith("org.eclipse.equinox.launcher")) return true;
					return false;
				}
			});
			if (searchResult.length==0) {
				this.setErrorMessage(Language.translate("Could not find the equinox launcher within the plugins-directory of the installed product.", Language.EN), statusMessageList);
				return null;
			}
			execJarFile = searchResult[0].getAbsolutePath(); 
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return execJarFile;
	}
	/**
	 * Sets or prints an error message (used in the above method).
	 * @param message the message
	 * @param statusMessageList the status message list
	 * @see #getFileRunnableJar(String, ArrayList)
	 */
	private void setErrorMessage(String message, ArrayList<String> statusMessageList) {
		if (message!=null && message.equals("")==false) {
			if (statusMessageList==null) {
				System.err.println(message);
			} else {
				statusMessageList.add(message);
			}
		}
	}
	
	
	/**
	 * Returns the file name of the xml-file, which contains the project configuration (file: 'agentgui.xml')
	 * @return file name of the project configuration (agentgui.xml)
	 */
	public String getFileNameProject() {
		return localFileNameProject;
	}
	/**
	 * Returns the file name of the XML file that contains the user object of a Project(file: 'agentguiUserObject.xml')
	 * @return file name of the project user object binary. (agentgui.bin)
	 */
	public String getFileNameProjectUserObjectXmlFile() {
		return localFileNameProjectUserObjectXmlFile;
	}
	/**
	 * Returns the file name of the binary file, which contains the serializable user object of the Project(file: 'agentgui.bin')
	 * @return file name of the project user object binary. (agentgui.bin)
	 */
	public String getFilenameProjectUserObjectBinFile(){
		return localFileNameProjectUserObjectBinFile;
	}
	/**
	 * This method returns the file suffix for Agent.GUI project files 
	 * ('agui'), which are used for the project exchange within the application
	 *  
	 * @return The file suffix for zipped Agent.GUI projects.
	 */
	public String getFileEndProjectZip(){
		return localFileEndProjectZip;
	}

	// ---------------------------------------------------------
	// ---------------------------------------------------------
	/**
	 * This method will convert relative AWB paths to absolute paths. 
	 * (e. g. './AgentGUI/properties/' will be converted to 'D:/MyWorkspace/AgentGUI/properties')    
	 * @param filePathRelative The relative path to convert
	 * @return The absolute path of the given relative one 
	 */
	private Path getFilePathAbsolute(String filePathRelative) {
		return this.getPathBaseDir().resolve(filePathRelative);		
	}
	// ---------------------------------------------------------
	// ---------------------------------------------------------

	/**
	 * This method can be used in order to set the port on which JADE is running (by default: 1099)
	 * @param localeJadeDefaultPort the localeJadeDefaultPort to set
	 */
	public void setJadeLocalPort(int localeJadeDefaultPort) {
		localeJadeLocalPort = localeJadeDefaultPort;
	}
	/**
	 * Returns the port on which JADE will run. 
	 * @return the localeJadeDefaultPort
	 */
	public Integer getJadeLocalPort() {
		return localeJadeLocalPort;
	}
	
	/**
	 * This method can be used in order to set the MTP port on which JADE is running (by default: 1099).
	 * @param newMtpPort the new local jade MTP port 
	 */
	public void setJadeLocalPortMTP(int newMtpPort) {
		localeJadeLocalPortMTP = newMtpPort;
	}
	/**
	 * Returns the MTP port on which JADE will run. 
	 * @return the localeJadeDefaultPort
	 */
	public Integer getJadeLocalPortMTP() {
		return localeJadeLocalPortMTP;
	}
	
	/**
	 * Sets the {@link JadeUrlConfiguration} for the server.master.
	 * @param newJadeUrlConfiguration the new {@link JadeUrlConfiguration} for master
	 */
	public void setJadeUrlConfigurationForMaster(JadeUrlConfiguration newJadeUrlConfiguration) {
		this.urlConfigurationForMaster = newJadeUrlConfiguration;
	}
	/**
	 * Returns the {@link JadeUrlConfiguration} for the server.master.
	 * @return the JadeUrlConfiguration
	 */
	public JadeUrlConfiguration getJadeUrlConfigurationForMaster() {
		if (urlConfigurationForMaster==null) {
			// --- Define the address of the server.master platform -----------
			urlConfigurationForMaster = new JadeUrlConfiguration(this.getServerMasterURL());
			if(urlConfigurationForMaster.hasErrors() == false){
				urlConfigurationForMaster.setPort(this.getServerMasterPort());
				urlConfigurationForMaster.setPort4MTP(this.getServerMasterPort4MTP());
				urlConfigurationForMaster.setMtpProtocol(this.getServerMasterProtocol().toString());
			}
		}
		return urlConfigurationForMaster;
	}
	
	/**
	 * This method return the default platform configuration for JADE
	 * @return instance of class 'PlatformJadeConfig', which holds the configuration of JADE
	 * @see PlatformJadeConfig
	 */
	public PlatformJadeConfig getJadeDefaultPlatformConfig() {
		
		// --- Here the default-values can be configured ------------
		PlatformJadeConfig jadeConfig = new PlatformJadeConfig();
		jadeConfig.setLocalPort(this.getJadeLocalPort());
		
		jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentGUI_LoadService);
		jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentGUI_SimulationService);
		jadeConfig.addService(PlatformJadeConfig.SERVICE_NotificationService);
		
		if (Application.isRunningAsServer()==false) {
			// --- Running as application ---------------------------
			jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentMobilityService);
		}
		return jadeConfig;
	}
	/**
	 * This Method returns the default JADE container-profile
	 * of the AgentGUI-Application
	 *  
	 * @return The local JADE default profile
	 * @see ProfileImpl
	 */
	public ProfileImpl getJadeDefaultProfile() {
		PlatformJadeConfig jadeConfig = this.getJadeDefaultPlatformConfig();
		return jadeConfig.getNewInstanceOfProfilImpl();
	}

	// ---------------------------------------------------------
	// --- Visualization stuff ---------------------------------
	// ---------------------------------------------------------
	/**
	 * Due this method the color for menu highlighting an be get
	 * @return color for highlighted menus
	 */
	public Color ColorMenuHighLight () {
		if (localColorMenuHighLight==null) {
			localColorMenuHighLight =  new Color(0, 0, 192);
		}
		return localColorMenuHighLight;
	}


	// ---------------------------------------------------------
	// --- File-Properties -------------------------------------
	// ---------------------------------------------------------
	/**
	 * Gets the version info.
	 * @return the version info
	 */
	public VersionInfo getVersionInfo() {
		if (this.versionInfo==null) {
			Bundle bundle = FrameworkUtil.getBundle(GlobalInfo.class);
			AWBProduct currAWBProduct = this.getAWBProduct();
			this.versionInfo = new VersionInfo(bundle.getSymbolicName(), this.getApplicationTitle(), currAWBProduct==null ? null : currAWBProduct.getProductName());
		}
		return this.versionInfo;
	}
	
	/**
	 * Returns the bundle properties that are in fact eclipse preferences.
	 * @return the bundle properties
	 */
	public BundleProperties getBundleProperties() {
		return this.getBundleProperties(true);
	}
	/**
	 * Returns the bundle properties that are in fact eclipse preferences.
	 * @param isSetPreferencesToGlobal set <code>true</code> in order to directly set the preferences to the {@link GlobalInfo}
	 * @return the bundle properties
	 */
	public BundleProperties getBundleProperties(boolean isSetPreferencesToGlobal) {
		if (bundleProperties==null) {
			bundleProperties = new BundleProperties(this, isSetPreferencesToGlobal);
		}
		return bundleProperties;
	}
	/**
	 * Persist (saves) the current configuration by saving the preferences.
	 */
	public void doSaveConfiguration() {
		this.getBundleProperties().save();
	}
	
	public void putStringToConfiguration(String key, String value) {
		this.getBundleProperties().getEclipsePreferences().put(key, value);
	}
	public void putBooleanToConfiguration(String key, boolean value) {
		this.getBundleProperties().getEclipsePreferences().putBoolean(key, value);
	}
	public void putIntToConfiguration(String key, int value) {
		this.getBundleProperties().getEclipsePreferences().putInt(key, value);
	}
	public void putLongToConfiguration(String key, long value) {
		this.getBundleProperties().getEclipsePreferences().putLong(key, value);
	}
	public void putFloatToConfiguration(String key, float value) {
		this.getBundleProperties().getEclipsePreferences().putFloat(key, value);
	}
	public void putDoubleToConfiguration(String key, double value) {
		this.getBundleProperties().getEclipsePreferences().putDouble(key, value);
	}
	public void putByteArryToConfiguration(String key, byte[] value) {
		this.getBundleProperties().getEclipsePreferences().putByteArray(key, value);
	}
	
	public String getStringFromConfiguration(String key, String defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().get(key, defaultValue);
	}
	public boolean getBooleanFromConfiguration(String key, boolean defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getBoolean(key, defaultValue);
	}
	public int getIntFromConfiguration(String key, int defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getInt(key, defaultValue);
	}
	public long getLongFromConfiguration(String key, long defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getLong(key, defaultValue);
	}
	public float getFloatFromConfiguration(String key, float defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getFloat(key, defaultValue);
	}
	public double getDoubleFromConfiguration(String key, double defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getDouble(key, defaultValue);
	}
	public byte[] getByteArryFromConfiguration(String key, byte[] defaultValue) {
		return this.getBundleProperties().getEclipsePreferences().getByteArray(key, defaultValue);
	}
	
	/**
	 * Returns the symbolic bundle name of the core bundle.
	 * @return the symbolic bundle name
	 */
	public static String getSymbolicBundleName() {
		
		Bundle thisBundle = FrameworkUtil.getBundle(GlobalInfo.class);
		if (thisBundle!=null) {
			return thisBundle.getSymbolicName();
		}
		return null;
	}
	
	// ---- SciMark 2.0 Benchmark ----------------------------
	/**
	 * Returns the benchmark value of the current execution of Agent.GUI that 
	 * will be measured at the initial program execution (if required)
	 * @return The benchmark value, stored in the file properties
	 * @see BenchmarkMeasurement
	 * @see BundleProperties
	 */
	public Float getBenchValue() {
		return filePropBenchValue;
	}
	/**
	 * Can be used in order to set the benchmark value in the file properties
	 * @param benchValue The result of the initial benchmark
	 * @see BenchmarkMeasurement
	 * @see BundleProperties
	 */
	public void setBenchValue(float benchValue) {
		this.filePropBenchValue = benchValue;
	}

	/**
	 * Here the reminder value can be get, which stores the computer/host name on which Agent.GUI 
	 * was executed the last time (evaluated due: <code>InetAddress.getLocalHost().getCanonicalHostName())</code>.<br>
	 * This can prevent the execution of the benchmark measurement, if the application will be 
	 * executed on the same machine next time.  
	 *   
	 * @return the filePropBenchExecOn
	 * @see BenchmarkMeasurement
	 * @see BundleProperties
	 */
	public String getBenchExecOn() {
		return filePropBenchExecOn;
	}
	/**
	 * This method can be used in order to set the reminder value for the computer/host name on which
	 * the benchmark test was executed the last time (With Agent.GUI evaluated due: <code>InetAddress.getLocalHost().getCanonicalHostName()</code>)
	 *  
	 * @param benchExecOn the filePropBenchExecOn to set
	 * @see BenchmarkMeasurement
	 * @see BundleProperties
	 */
	public void setBenchExecOn(String benchExecOn) {
		this.filePropBenchExecOn = benchExecOn;
	}

	/**
	 * Returns if the file properties are configured to always skip the benchmark
	 * @return the filePropBenchAllwaySkip
	 * @see BenchmarkMeasurement
	 */
	public boolean isBenchAlwaysSkip() {
		return filePropBenchAlwaysSkip;
	}
	/**
	 * Can be used in order to set the file properties to always skip the benchmark
	 * @param benchAlwaysSkip the filePropBenchAlwaySkip to set
	 * @see BenchmarkMeasurement
	 */
	public void setBenchAlwaysSkip(boolean benchAlwaysSkip) {
		this.filePropBenchAlwaysSkip = benchAlwaysSkip;
	}


	/**
	 * Sets to initially maximize the {@link MainWindow}.
	 * @param isMaximzeMainWindow the new maximze indicator
	 */
	public void setMaximzeMainWindow(boolean isMaximzeMainWindow) {
		maximizeMainWindow = isMaximzeMainWindow;
	}
	/**
	 * Returns if the {@link MainWindow} has to be maximized at start up.
	 * @return true, if the main window is to be maximized 
	 */
	public boolean isMaximzeMainWindow() {
		return maximizeMainWindow;
	}
	
	
	// ---- Logging configuration -----------------------------------
	/**
	 * Sets the logging enabled (or not).
	 * @param enableLogging the new logging enabled
	 */
	public void setLoggingEnabled(boolean enableLogging) {
		this.filePropLoggingEnabled = enableLogging;
	}
	/**
	 * Checks if the file logging is enabled.
	 * @return true, if file logging is enabled
	 */
	public boolean isLoggingEnabled() {
		return this.filePropLoggingEnabled;
	}
	
	/**
	 * Sets the logging base path.
	 * @param newLoggingbasePath the new logging base path
	 */
	public void setLoggingBasePath(String newLoggingbasePath) {
		this.filePropLoggingBasePath = newLoggingbasePath;
	}
	/**
	 * Returns the relative logging base path. If not set differently, the method will return the default path.
	 * @return the logging base path
	 */
	public String getLoggingBasePath() {
		return this.getLoggingBasePath(false);
	}
	/**
	 * Returns the relative logging base path. If not set differently, the method will return the default path.
	 * @param forcePathCreation set true, if the directory should be created
	 * @return the logging base path
	 */
	public String getLoggingBasePath(boolean forcePathCreation) {
		if (filePropLoggingBasePath==null) {
			filePropLoggingBasePath = PathHandling.getLoggingFilesBasePathDefault().toString();
		}
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(filePropLoggingBasePath);
		}
		return filePropLoggingBasePath;
	}
	
	/**
	 * Returns the logging directory by month.
	 * @param timeStamp the timestamp
	 * @param forcePathCreation set true, if the directory should be created 
	 * @return the logging directory by month
	 */
	public String getLoggingPathByMonth(long timeStamp, boolean forcePathCreation){
		Date date = new Date(timeStamp);
		String monthDescriptor = new SimpleDateFormat("MM").format(date) + "_" + new SimpleDateFormat("MMM").format(date);
		String logPathByMonth = this.getLoggingBasePath(forcePathCreation) + File.separator + monthDescriptor + File.separator; 
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(logPathByMonth);
		}
		return logPathByMonth;
	}
	/**
	 * Returns the day prefix for log files using the specified timestamp (e.g. DAY_18).
	 * @param timeStamp the time stamp
	 * @return the time stamp prefix
	 */
	public String getLoggingDayPrefix(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return "DAY_" + sdf.format(new Date(timeStamp));
	}
	
	
	// ---- Connection to the Master-Server -------------------------
	/**
	 * Can be used in order to start the active server mode immediately after the program execution.
	 * This applies only if the current execution mode is set to server (server.master or server.slave)
	 * @param serverAutoRun the filePropAutoRun to set
	 * @see BundleProperties
	 */
	public void setServerAutoRun(boolean serverAutoRun) {
		this.filePropServerAutoRun = serverAutoRun;
	}

	/**
	 * This method returns the current setting for the file property '10_AUTOSTART', which is 
	 * used for whether or not to start the server execution mode immediately after the program
	 * execution. This applies only if the current execution mode is set to server (server.master 
	 * or server.slave)
	 * 
	 * @return the filePropAutoRun
	 * @see BundleProperties
	 * @see GlobalInfo#setServerAutoRun(boolean)
	 */
	public boolean isServerAutoRun() {
		return this.filePropServerAutoRun;
	}
	/**
	 * Here the URLS or IP of the server.master can be set 
	 * @param serverMasterURL the filePropMasterURL to set
	 * @see BundleProperties
	 */
	public void setServerMasterURL(String serverMasterURL) {
		this.filePropServerMasterURL = serverMasterURL;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * Here the URL or IP of the server.master can be get
	 * @return the filePropMasterURL
	 * @see BundleProperties
	 */
	public String getServerMasterURL() {
		return this.filePropServerMasterURL;
	}
	/**
	 * Here the port for the server.master can be set
	 * @param serverMasterPort the filePropMasterPort to set
	 * @see BundleProperties
	 */
	public void setServerMasterPort(Integer serverMasterPort) {
		this.filePropServerMasterPort = serverMasterPort;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * This method returns the port on which the server.master can be reached 
	 * @return The port of the server.master
	 * @see BundleProperties
	 */
	public Integer getServerMasterPort() {
		return this.filePropServerMasterPort;
	}
	/**
	 * This method can be used in order to set the MTP port of the server.master
	 * @param serverMasterPort4MTP the filePropMasterPort to set
	 * @see BundleProperties
	 */
	public void setServerMasterPort4MTP(Integer serverMasterPort4MTP) {
		this.filePropServerMasterPort4MTP = serverMasterPort4MTP;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * Returns the MTP port of the server.master 
	 * @return the filePropMasterPort
	 * @see BundleProperties
	 */
	public Integer getServerMasterPort4MTP() {
		return this.filePropServerMasterPort4MTP;
	}
	
	/**
	 * Gets the filePropServerMasterProtocol.
	 * @return the file prop server master protocol
	 * @see BundleProperties
	 */
	public MtpProtocol getServerMasterProtocol() {
		return filePropServerMasterProtocol;
	}
	
	/**
	 * Sets the filePropServerMasterProtocol.
	 * @param filePropServerMasterProtocol the new filePropServerMasterProtocol
	 * @see BundleProperties
	 */
	public void setServerMasterProtocol(MtpProtocol filePropServerMasterProtocol) {
		this.filePropServerMasterProtocol = filePropServerMasterProtocol;
	}
	
	/**
	 * Sets the own MTP creation settings.
	 * @param ownMtpCreation the new own MTP creation
	 */
	public void setOwnMtpCreation(MTP_Creation ownMtpCreation) {
		this.filePropOwnMtpCreation = ownMtpCreation;
	}
	/**
	 * Returns the setting for the own MTP creation.
	 * @return the own MTP creation settings
	 */
	public MTP_Creation getOwnMtpCreation() {
		return filePropOwnMtpCreation;
	}
	/**
	 * Sets the own IP-address that should be used with the MTP.
	 * @param filePropOwnMtpIP the new own MTP IP-Address
	 */
	public void setOwnMtpIP(String filePropOwnMtpIP) {
		this.filePropOwnMtpIP = filePropOwnMtpIP;
	}
	/**
	 * Returns the own MTP IP-address.
	 * @return the own MTP IP-address
	 */
	public String getOwnMtpIP() {
		return filePropOwnMtpIP;
	}
	/**
	 * Sets the own MTP port.
	 * @param newOwnMtpPort the new own MTP port
	 */
	public void setOwnMtpPort(Integer newOwnMtpPort) {
		this.filePropOwnMtpPort = newOwnMtpPort;
	}
	/**
	 * Returns the own MTP port.
	 * @return the own MTP port
	 */
	public Integer getOwnMtpPort() {
		return filePropOwnMtpPort;
	}
	
	/**
	 * Sets the MTP protocol.
	 * @param newMtpProtocol the new MTP protocol
	 */
	public void setMtpProtocol(MtpProtocol newMtpProtocol) {
		this.filePropMtpProtocol = newMtpProtocol;
	}
	
	/**
	 * Gets the MTP protocol.
	 * @return the MTP protocol
	 */
	public MtpProtocol getMtpProtocol() {
		return filePropMtpProtocol;
	}
	
	// ---- Methods for the reminder of the last selected folder ----
	/**
	 * This method can be used in order to remind the last folder 
	 * in which a file was selected (e. g. while using a JFileChooser) 
	 * @param lastSelectedFolderPath the new last selected folder
	 */
	public void setLastSelectedFolder(String lastSelectedFolderPath) {
		if (lastSelectedFolderPath!=null) {
			File lastSelectedFile = new File(lastSelectedFolderPath);
			if (lastSelectedFile.exists()==true) {
				this.setLastSelectedFolder(lastSelectedFile);
			}
		}
	}
	/**
	 * This method can be used in order to remind the last folder 
	 * in which a file was selected (e. g. while using a JFileChooser) 
	 * @param lastSelectedFolder the lastSelectedFolder to set
	 */
	public void setLastSelectedFolder(File lastSelectedFolder) {
		GlobalRuntimeValues.setLastSelectedDirectory(lastSelectedFolder);
	}
	/**
	 * Returns the reminder value of the last selected folder as File object 
	 * @return the lastSelectedFolder
	 */
	public File getLastSelectedFolder() {
		return GlobalRuntimeValues.getLastSelectedDirectory();
	}
	/**
	 * Returns the reminder value of the last selected folder as String
	 * @return the lastSelectedFolder as String
	 */
	public String getLastSelectedFolderAsString() {
		File lastSelectedFolder = this.getLastSelectedFolder();
		if (lastSelectedFolder==null) {
			lastSelectedFolder = this.getPathBaseDir().toFile();
		}
		String returnFolder = lastSelectedFolder.getAbsolutePath();
		if (returnFolder.endsWith(File.separator)==false) {
			returnFolder+=File.separator;
		}
		return returnFolder;
	}
	
	// -------------------------------------------------------------------------
	// ---- Methods for EnvironmentTypes ---------------------------------------
	// -------------------------------------------------------------------------
	/**
	 * This method returns all EnvironmentTypes known by Agent.GUI 
	 * @return the knowEnvironmentTypes
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public EnvironmentTypes getKnownEnvironmentTypes() {
		if (knownEnvironmentTypes==null) {
			knownEnvironmentTypes = new EnvironmentTypes();
			// --- Define 'No environment' ------------------------------------
			String envKey = "none";
			String envDisplayName = "Kein vordefiniertes Umgebungsmodell verwenden";
			String envDisplayNameLanguage = Language.DE;
			Class<? extends EnvironmentController> envControllerClass = null;
			Class<? extends Agent> displayAgentClass = null;
			knownEnvironmentTypes.add(new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass));
		}
		// --- Check for environments in the dynamic OSGI environment ---------
		List<EnvironmentType> envTypeList = EnvironmentTypeServiceFinder.findEnvironmentTypeServices();
		for (int i = 0; i < envTypeList.size(); i++) {
			if (knownEnvironmentTypes.contains(envTypeList.get(i))==false) {
				knownEnvironmentTypes.add(envTypeList.get(i));
			}
		}
		return knownEnvironmentTypes;
	}
	
	/**
	 * This method can be used in order to add a tailored environment type (assume for example a 3D-environment model).
	 *
	 * @param envType the EnvironmentType definition to add
	 * @see EnvironmentType
	 */
	public void addEnvironmentType(EnvironmentType envType) {
		if (envType==null) return;
		if (this.getKnownEnvironmentTypes().contains(envType)==false) {
			this.getKnownEnvironmentTypes().add(envType);
		}
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envType The EnvironmentType instance
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(EnvironmentType envType) {
		if (envType==null) return;
		this.getKnownEnvironmentTypes().remove(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envTypeKey The key expression of the environment type
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(String envTypeKey) {
		EnvironmentType envType = this.getKnownEnvironmentTypes().getEnvironmentTypeByKey(envTypeKey);
		this.removeEnvironmentType(envType);
	}
	

	
	/**
	 * Sets the OIDC issuer URI.
	 * @param OIDCIssuerURI the new OIDC issuer URI
	 */
	public void setOIDCIssuerURI(String OIDCIssuerURI) {
		this.oidcIssuerURI = OIDCIssuerURI;
	}
	
	/**
	 * Returns the OIDC issuer URI.
	 * @return the OIDCIssuerURI
	 */
	public String getOIDCIssuerURI() {
		return oidcIssuerURI;
	}

	/**
	 * Sets the update auto configuration.
	 * @param updateAutoConfiguration the new update auto configuration
	 */
	public void setUpdateAutoConfiguration(Integer updateAutoConfiguration) {
		this.updateAutoConfiguration = updateAutoConfiguration;
	}
	/**
	 * Returns the update auto configuration.
	 * @return the update auto configuration
	 */
	public Integer getUpdateAutoConfiguration() {
		return updateAutoConfiguration;
	}
	
	/**
	 * Sets the update keep dictionary.
	 * @param updateKeepDictionary the new update keep dictionary
	 */
	public void setUpdateKeepDictionary(Integer updateKeepDictionary) {
		this.updateKeepDictionary = updateKeepDictionary;
	}
	/**
	 * Gets the update keep dictionary.
	 * @return the update keep dictionary
	 */
	public Integer getUpdateKeepDictionary() {
		return updateKeepDictionary;
	}
	
	/**
	 * Sets the update date last checked.
	 * @param updateDateLastChecked the new update date last checked
	 */
	public void setUpdateDateLastChecked(long updateDateLastChecked) {
		this.updateDateLastChecked = updateDateLastChecked;
	}
	/**
	 * Gets the update date last checked.
	 * @return the update date last checked
	 */
	public Long getUpdateDateLastChecked() {
		return updateDateLastChecked;
	}
	
	/**
	 * Gets the device service project folder.
	 * @return the device service project folder
	 */
	public String getDeviceServiceProjectFolder() {
		return deviceServiceProjectFolder;
	}
	/**
	 * Sets the device service project folder.
	 * @param deviceServiceProjectFolder the new device service project folder
	 */
	public void setDeviceServiceProjectFolder(String deviceServiceProjectFolder) {
		this.deviceServiceProjectFolder = deviceServiceProjectFolder;
	}
	
	/**
	 * Gets the device service execution mode.
	 * @return the device service execution mode
	 */
	public DeviceSystemExecutionMode getDeviceServiceExecutionMode() {
		return deviceServiceExecutionMode;
	}
	/**
	 * Sets the device service execution mode.
	 * @param deviceServiceExecutionMode the new device service execution mode
	 */
	public void setDeviceServiceExecutionMode(DeviceSystemExecutionMode deviceServiceExecutionMode) {
		this.deviceServiceExecutionMode = deviceServiceExecutionMode;
	}
	
	/**
	 * Gets the device service setup selected.
	 * @return the device service setup selected
	 */
	public String getDeviceServiceSetupSelected() {
		return deviceServiceSetupSelected;
	}
	/**
	 * Sets the device service setup selected.
	 * @param deviceServiceSetupSelected the new device service setup selected
	 */
	public void setDeviceServiceSetupSelected(String deviceServiceSetupSelected) {
		this.deviceServiceSetupSelected = deviceServiceSetupSelected;
	}
	
	/**
	 * Returns the device service agents as Vector.
	 * @return the device service agents 
	 */
	public Vector<DeviceAgentDescription> getDeviceServiceAgents() {
		if (deviceServiceAgents==null) {
			deviceServiceAgents = new Vector<>();
		}
		return deviceServiceAgents;
	}
	/**
	 * Sets the device service agents.
	 * @param deviceServiceAgents the new device service agent name
	 */
	public void setDeviceServiceAgents(Vector<DeviceAgentDescription> deviceServiceAgents) {
		this.deviceServiceAgents = deviceServiceAgents;
	}
	
	/**
	 * Gets the device service agent visualisation.
	 * @return the device service agent visualisation
	 */
	public EmbeddedSystemAgentVisualisation getDeviceServiceAgentVisualisation() {
		return deviceServiceAgentVisualisation;
	}
	/**
	 * Sets the device service agent visualisation.
	 * @param deviceServiceAgentVisualisation the new device service agent visualisation
	 */
	public void setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation deviceServiceAgentVisualisation) {
		this.deviceServiceAgentVisualisation = deviceServiceAgentVisualisation;
	}
	
	/**
	 * Gets the KeyStoreFile.
	 * @return KeyStoreFile
	 */
	public String getKeyStoreFile() {
		return filePropKeyStoreFile;
	}
	
	/**
	 * Sets the KeyStoreFile.
	 * @param filePropKeyStoreFile the new KeyStoreFile
	 */
	public void setKeyStoreFile(String filePropKeyStoreFile) {
		this.filePropKeyStoreFile = filePropKeyStoreFile;
	}

	/**
	 * Gets the TrustStore file.
	 * @return the TrustStore file
	 */
	public String getTrustStoreFile() {
		return filePropTrustStoreFile;
	}
	/**
	 * Sets the TrustStore file.
	 * @param filePropTrustStoreFile the new TrustStore file
	 */
	public void setTrustStoreFile(String filePropTrustStoreFile) {
		this.filePropTrustStoreFile = filePropTrustStoreFile;
	}
	
	/**
	 * Gets the KeyStorePassword
	 * @return the KeyStorePassword
	 */
	public String getKeyStorePassword() {
		return this.pwDecrypt(this.getKeyStorePasswordEncrypted());
	}
	/**
	 * Sets the KeyStore password.
	 * @param newPassword the new KeyStore password
	 */
	public void setKeyStorePassword(String newPassword) {
		this.setKeyStorePasswordEncrypted(this.pwEncrypt(newPassword));
	}
	
	/**
	 * Gets the KeyStorePasswordEncrypted.
	 * @return the filePropKeyStorePasswordEncrypted
	 */
	public String getKeyStorePasswordEncrypted() {
		return filePropKeyStorePasswordEncrypted;
	}
	
	/**
	 * Sets the KeyStorePasswordEncrypted.
	 * @param filePropKeyStorePasswordEncrypted the new KeyStorePasswordEncrypted
	 */
	public void setKeyStorePasswordEncrypted(String filePropKeyStorePasswordEncrypted) {
		this.filePropKeyStorePasswordEncrypted = filePropKeyStorePasswordEncrypted;
	}
	/**
	 * Gets the TrustStore password.
	 * @return the TrustStore password
	 */
	public String getTrustStorePassword() {
		return this.pwDecrypt(this.getTrustStorePasswordEncrypted());
	}
	/**
	 * Sets the TrustStore password.
	 * @param newPassword the new TrustStore password
	 */
	public void setTrustStorePassword(String newPassword) {
		this.setTrustStorePasswordEncrypted(this.pwEncrypt(newPassword));
	}
	
	/**
	 * Sets the trust store password encrypted.
	 * @param filePropTrustStorePasswordEncrypted the new trust store password encrypted
	 */
	public void setTrustStorePasswordEncrypted(String filePropTrustStorePasswordEncrypted) {
		this.filePropTrustStorePasswordEncrypted = filePropTrustStorePasswordEncrypted;
	}
	
	/**
	 * Gets the TrustStorePasswordEncrypted.
	 * @return the TrustStorePasswordEncrypted
	 */
	public String getTrustStorePasswordEncrypted() {
		return filePropTrustStorePasswordEncrypted;
	}
	
	/**
	 * Sets the OpenID Connect user name
	 * @param oidcUsername the user name for the OpenID Connect protocol
	 */
	public void setOIDCUsername(String oidcUsername) {
		this.oidcUsername=oidcUsername;		
	}
	
	/**
	 * Gets the OpenID Connect user name
	 * @return the user name for the OpenID Connect protocol
	 */
	public String getOIDCUsername() {
		return oidcUsername;
	}
	
	/**
	 * Returns the path to the base image directory of Agent.Workbench.
	 * @return path to the images, which are located in our project
	 */
	public static String getPathImageAWB(){
		return localPathImageAWB;
	}
	/**
	 * Returns the path to the internal image directory of Agent.Workbench. 
	 * @return path to the images, which are located in our project
	 */
	public static String getPathImageIntern(){
		return localPathImageIntern;
	}
	/**
	 * Returns one of the internal images as ImageIcon, specified by its file name.
	 * @param imageFileName the image file name
	 * @return the internal image icon
	 */
	public static javax.swing.ImageIcon getInternalImageIcon(String imageFileName) {
		return GlobalInfo.getInternalImageIcon(null, imageFileName);
	}
	/**
	 * Returns one of the internal images as ImageIcon, specified by its file name.
	 * @param internalImagePath the internal image path to use (if null, the internal image path will be used {@link #getPathImageIntern()})
	 * @param imageFileName the image file name
	 * @return the internal image icon
	 */
	public static javax.swing.ImageIcon getInternalImageIcon(String internalImagePath, String imageFileName) {
		
		if (internalImagePath==null) internalImagePath = getPathImageIntern();
		String imagePath = internalImagePath + imageFileName;
		URL imageURL = GlobalInfo.class.getResource(imagePath);
		if (imageURL!=null) {
			return new javax.swing.ImageIcon(imageURL);
		} else {
			System.err.println(GlobalInfo.class.getSimpleName() + ": Could not find ImageIcon '" + imagePath + "'");
		}
		return null;
	}
	/**
	 * Returns one of the internal images specified by its file name.
	 * @param imageFileName the image file name
	 * @return the internal image
	 */
	public static java.awt.Image getInternalImage(String imageFileName) {
		javax.swing.ImageIcon imageIcon = getInternalImageIcon(imageFileName);
		if (imageIcon!=null) {
			return imageIcon.getImage();
		}
		return null;
	}
	/**
	 * Returns the internal image icon of Agent.Workbench sized 16x16.
	 * @return the internal image icon awb icon 16
	 */
	public static javax.swing.ImageIcon getInternalImageIconAwbIcon16() {
		return getInternalImageIcon(getPathImageAWB(), "awb16.png");
	}
	/**
	 * Returns the internal image icon of Agent.Workbench sized 48x48.
	 * @return the internal image icon awb icon 16
	 */
	public static javax.swing.ImageIcon getInternalImageIconAwbIcon48() {
		return getInternalImageIcon(getPathImageAWB(), "awb48.png");
	}
	/**
	 * Returns the internal image icon of Agent.Workbench sized 16x16.
	 * @return the internal image icon awb icon 16
	 */
	public static java.awt.Image getInternalImageAwbIcon16() {
		javax.swing.ImageIcon imageIcon = getInternalImageIconAwbIcon16();
		if (imageIcon!=null) {
			return imageIcon.getImage();
		}
		return null;
	}
	/**
	 * Returns the internal image icon of Agent.Workbench sized 48x48.
	 * @return the internal image icon awb icon 16
	 */
	public static java.awt.Image getInternalImageAwbIcon48() {
		javax.swing.ImageIcon imageIcon = getInternalImageIconAwbIcon48();
		if (imageIcon!=null) {
			return imageIcon.getImage();
		}
		return null;
	}
	
	
	/**
	 * Returns the specified bytes in a human readable byte count.
	 *
	 * @param bytes the bytes
	 * @param si set true, if you want to use SI conversion
	 * @return the human readable byte count
	 */
	public static String getHumanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	/**
	 * Returns the next midnight time stamp for the specified time.
	 *
	 * @param timeStamp the time stamp
	 * @return the next midnight time stamp
	 */
	public static long getNextMidnightFromTimeStamp(long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * This method is used to decrypt password.
	 * @param encryptedPSWD the encrypted password
	 * @return the password decrypted
	 */
	public String pwDecrypt(String encryptedPSWD) {
		String pwDecrypted = null;
		if (encryptedPSWD!=null && encryptedPSWD.trim().equals("")==false) {
			try {
				pwDecrypted = new String(Base64.decodeBase64(encryptedPSWD.getBytes()), "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return pwDecrypted;
	}
	
	/**
	 * This method is used to encrypt password.
	 * @param pswd the password
	 * @return the password encrypted
	 */
	public String pwEncrypt(String pswd) {
		String encryptedPSWD = null;
		if (pswd!=null && pswd.trim().equals("")==false) {
			try {
				encryptedPSWD = new String(Base64.encodeBase64(pswd.getBytes("UTF8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return encryptedPSWD;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.ZoneIdResolver#getZoneId()
	 */
	@Override
	public ZoneId getZoneId() {

		// --- Check for an open project ----------------------------
		Project project = Application.getProjectFocused();
		if (project!=null) {
			// --- Check the TimeModel ------------------------------
			TimeModel timeModel = project.getTimeModelController().getTimeModel();
			if (timeModel!=null && timeModel instanceof TimeModelDateBased) {
				TimeModelDateBased tmDate = (TimeModelDateBased) timeModel;
				return tmDate.getZoneId();
			}
		}
		return ZoneId.systemDefault();
	}
	
}
