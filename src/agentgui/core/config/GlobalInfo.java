/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.config;

import jade.core.Agent;
import jade.core.Profile;
import jade.wrapper.AgentContainer;

import java.awt.Color;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.charts.timeseriesChart.TimeSeriesVisualisation;
import agentgui.core.charts.xyChart.XyChartVisualisation;
import agentgui.core.common.ClassLoaderUtil;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.environment.EnvironmentTypes;
import agentgui.core.jade.Platform;
import agentgui.core.network.JadeUrlChecker;
import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.visualisation.DisplayAgent;

/**
 * This class is for constant values or variables, which can
 * be accessed or used application wide.<br>
 * In the Application class the running instance can be accessed 
 * by using the reference {@link Application#getGlobalInfo()}. 
 * 
 * @see Application#getGlobalInfo()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GlobalInfo {

	// --- constant values -------------------------------------------------- 
	private static String localAppTitle = "Agent.GUI";
	
	private final static String localFileRunnableJar = "AgentGui.jar";
	private final static String localFileRunnableUpdater = "AgentGuiUpdate.jar";
	private final static String localPathImageIntern = "/agentgui/core/gui/img/";
	private final static String fileSeparator = File.separator;
	private final static String newLineSeparator = System.getProperty("line.separator");
	private final static String newLineSeparatorReplacer = "<br>";

	private final static Color localColorMenuHighLight =  new Color(0,0,192);
	
	// --- JADE-Variables ---------------------------------------------------
	private Integer localeJadeLocalPort = 1099;
	
	// --- Variables --------------------------------------------------------
	public final static String ExecutedOverIDE = "IDE";
	public final static String ExecutedOverAgentGuiJar = "Executable";
	private static String localAppExecutedOver = ExecutedOverIDE;
	
	private static String localAppLnF = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	private static String localBaseDir = "";
	private static String localPathAgentGUI	 = "bin";
	private static String localPathJade		 = "lib" + fileSeparator + "jade" +  fileSeparator + "lib";
	private static String localPathProperty  = "properties" + fileSeparator;
	private static String localPathProjects  = "projects" + fileSeparator;
	private static String localPathWebServer = "server" + fileSeparator;
	private static String localPathDownloads = "download" + fileSeparator;
	
	private static String localFileDictionary  = localPathProperty + "dictionary";
	private static String localFileProperties  = "agentgui.ini";
	private static String localFileNameProject = "agentgui.xml";
	private static String localFileNameProjectUserObject = "agentgui.bin";
	private static String localXML_FilePostfix = ".xml";
	private static String localBIN_FilePostfix = ".bin";
	private static String localFileEndProjectZip = "agui";
	private static String localFileNameProjectOntology = "AgentGUIProjectOntology";
	
	private static String localFileJade = "jade.jar";
	
	// --- Known EnvironmentTypes of Agent.GUI ------------------------------
	private EnvironmentTypes knownEnvironmentTypes =  new EnvironmentTypes();
	
	// --- Known OntologyClassVisualisation's of Agent.GUI ------------------
	private Vector<OntologyClassVisualisation> knownOntologyClassVisualisation = null;
	
	// --- File-Properties --------------------------------------------------
	private ExecutionMode fileExecutionMode = null;
	
	private float filePropBenchValue = 0;
	private String filePropBenchExecOn = null;
	private boolean filePropBenchAllwaysSkip = false; 
	
	private String filePropLanguage = null;
	
	private boolean filePropServerAutoRun = false;
	private String filePropServerMasterURL = null;
	private Integer filePropServerMasterPort = this.localeJadeLocalPort;
	private Integer filePropServerMasterPort4MTP = 7778;
	
	private String filePropServerMasterDBHost = null;
	private String filePropServerMasterDBName = null;
	private String filePropServerMasterDBUser = null;
	private String filePropServerMasterDBPswd = null;
	
	private String googleKey4API = null;
	private String googleHTTPref = null;
	
	private String updateSite = null;
	private Integer updateAutoConfiguration = 0;
	private Integer updateKeepDictionary = 1;
	private long updateDateLastChecked = 0;
	
	private String deviceServiceProjectFolder = null;
	private DeviceSystemExecutionMode deviceServiceExecutionMode = DeviceSystemExecutionMode.SETUP;
	private String deviceServiceSetupSelected = null;  
	private String deviceServiceAgentSelected = null;
	private EmbeddedSystemAgentVisualisation deviceServiceAgentVisualisation = EmbeddedSystemAgentVisualisation.TRAY_ICON;
	
	
	// --- Reminder information for file dialogs ----------------------------
	private File lastSelectedFolder = null; 
	
	// --- FileProperties and VersionInfo -----------------------------------
	/** Holds the instance of the file properties which are defined in '/properties/agentgui.ini' */
	private FileProperties fileProperties = null;
	/** Can be used in order to access the version information */
	private VersionInfo versionInfo = null;

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
	 * if and how visualisations are to be displayed in case that
	 * the {@link ExecutionMode} is set to {@link ExecutionMode#DEVICE_SYSTEM}
	 * or embedded system agent respectively.
	 */
	public enum EmbeddedSystemAgentVisualisation {
		NONE, TRAY_ICON
	}
	
	/**
	 * Constructor of this class. 
	 */
	public GlobalInfo() {

		Integer  cutAt = 0;
		String[] JCP_Files = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		String[] JCP_Folders = JCP_Files.clone(); 
		HashSet<String> Folders = new HashSet<String>();  
		
		File thisFile = new File(GlobalInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		try {
			thisFile = new File(GlobalInfo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException uriEx) {
			uriEx.printStackTrace();
		}
		localBaseDir = thisFile.getParent() + File.separator;
		
		// ------------------------------------------------------------------
		// --- Class-Path untersuchen ---------------------------------------
		for (int i=0; i<JCP_Files.length; i++) {

			if (JCP_Files[i].endsWith(localFileRunnableJar)) {
				localAppExecutedOver = ExecutedOverAgentGuiJar;
				
				File agentGuiJar = new File(JCP_Files[i]);
				String agentGuiJarPath = agentGuiJar.getAbsolutePath(); 
				cutAt = agentGuiJarPath.lastIndexOf(fileSeparator) + 1;
				localBaseDir = agentGuiJarPath.substring(0, cutAt);	

				// --- jade.jar in die ClassLoaderUtility einbinden ----------
				String jadeJar = this.PathJade(true) + File.separator + localFileJade;
				ClassLoaderUtil.addJarToClassPath(jadeJar);
				
			};
			if (JCP_Files[i].endsWith(".jar")) {
				// ----------------------------------------------------------
				// --- Dateinamen herausnehmen ------------------------------
				cutAt = JCP_Files[i].lastIndexOf(fileSeparator);
				if(cutAt!=-1){ //only if seperator was actually found
					JCP_Folders[i] = JCP_Folders[i].substring(0, cutAt);
				}
			}	
			Folders.add(JCP_Folders[i]);						
		} // end for
		
		if ( localAppExecutedOver.equals(ExecutedOverIDE)) {
			// --------------------------------------------------------------
			// --- Verzeichnis-Eintraege eindeutig (unique) machen ----------
			JCP_Folders = (String[])Folders.toArray(new String[Folders.size()]);
			for (int j = 0; j < JCP_Folders.length; j++) {				
				if (JCP_Folders[j].endsWith(localPathAgentGUI)) {
					// --- bin-Verzeichnis gefunden ---					
					cutAt = JCP_Folders[j].lastIndexOf(localPathAgentGUI);
					localBaseDir = JCP_Folders[j].substring(0, cutAt);
					break;
				}
			} // -- End 'for' --

		}
		// ------------------------------------------------------------------
		
		// ------------------------------------------------------------------
		// --- Define the known EnvironmentTypes of Agent.GUI ---------------
		String envKey = null;
		String envDisplayName = null;
		String envDisplayNameLanguage = null;
		Class<? extends EnvironmentController> envControllerClass = null;
		Class<? extends Agent> displayAgentClass = null;
		EnvironmentType envType = null;
		
		// --- No environment -----------------------------------------------
		envKey = "none";
		envDisplayName = "Kein vordefiniertes Umgebungsmodell verwenden";
		envDisplayNameLanguage = Language.DE;
		envControllerClass = null;
		displayAgentClass = null;
		envType = new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
		addEnvironmentType(envType);
		
		// --- Grid Environment ---------------------------------------------
		envKey = "gridEnvironment";
		envDisplayName = "Graph bzw. Netzwerk";
		envDisplayNameLanguage = Language.DE;
		envControllerClass = GraphEnvironmentController.class;
		displayAgentClass = DisplayAgent.class;
		envType = new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
		addEnvironmentType(envType);
		
		
		// ------------------------------------------------------------------
		// --- Add the known OntologyClassVisualisation's of Agent.GUI ------
		this.registerOntologyClassVisualisation(TimeSeriesVisualisation.class.getName());
		this.registerOntologyClassVisualisation(XyChartVisualisation.class.getName());
		
	}
	/**
	 * Initialises this class by reading the file properties and the current version information.
	 */
	public void initialize() {
		this.getFileProperties();
		this.getVersionInfo();
	}
	
	/**
	 * Sets the execution mode.
	 * @param executionMode the new execution mode
	 */
	public void setExecutionMode(ExecutionMode executionMode) {
		this.fileExecutionMode = executionMode;
	}
	
	/**
	 * Gets the execution mode from configuration.
	 * @return the execution mode from configuration
	 */
	public ExecutionMode getExecutionMode() {
	
		ExecutionMode execMode = this.fileExecutionMode; 
		if (execMode==ExecutionMode.SERVER) {
			// ------------------------------------------------------
			// --- Does JADE run? -----------------------------------
			// ------------------------------------------------------			
			Platform platform = Application.getJadePlatform();
			AgentContainer mainContainer = platform.jadeGetMainContainer();
			if (mainContainer!=null) {
				// --- JADE is running ------------------------------
				JadeUrlChecker urlConfigured = new JadeUrlChecker(this.getServerMasterURL());
				urlConfigured.setPort(this.getServerMasterPort());
				urlConfigured.setPort4MTP(this.getServerMasterPort4MTP());
				
				JadeUrlChecker urlCurrent = new JadeUrlChecker(mainContainer.getPlatformName());	
				if (urlCurrent.getHostIP().equalsIgnoreCase(urlConfigured.getHostIP()) && urlCurrent.getPort().equals(urlConfigured.getPort()) ) {
					// --- Running as Server [Master] ---------------
					execMode = ExecutionMode.SERVER_MASTER;
				} else {
					// --- Running as Server [Slave] ----------------
					execMode = ExecutionMode.SERVER_SLAVE;
				}
			}
			// ------------------------------------------------------
		}
		return execMode;
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
	 * This method will return the current Look and Feel (LnF) for Java Swing.<br>
	 * Also the class reference will be validated. If the class could not be found the 
	 * method will return null.
	 * 
	 * @return the class reference for the current look and feel 
	 * or null, if the class could not be found
	 */
	public String getAppLnF() {
		try {
			@SuppressWarnings("unused")
			Class<?> currOntoClass = Class.forName(localAppLnF);
			return localAppLnF;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
		return null;
	};
	/**
	 * Set the current Look and Feel (LnF) for Java Swing by using its class reference
	 */
	public void setAppLnf( String NewLnF ) {
		localAppLnF = NewLnF;
	};
	// -------------------------------

	/**
	 * This method can be used in order to evaluate how Agent.GUI is currently executed
	 * @return - 'IDE', if Agent.GUI is executed out of the IDE, where Agent.GUI is developed OR<br>
	 * - 'Executable', if Agent.GUI is running as executable jar-File
	 */
	public String AppExecutedOver() {
		return localAppExecutedOver;
	}
	/**
	 * This method returns the actual String for a new line string
	 * @return a String that can be used for new lines in text  
	 */
	public String getNewLineSeparator(){
		return newLineSeparator;
	}
	/**
	 * This method will return a substitute String for the new line String.
	 * It is used, for example, in the dictionary, so that it is possible
	 * to write text with several lines in a single one.<br>
	 * Basically, the HTML-tag <<i>br</i>> is used here.
	 * 
	 * @return a substitute String for a new line
	 */
	public String getNewLineSeparatorReplacer(){
		return newLineSeparatorReplacer;
	}

	// --- Allgemeine Verzeichnisangaben ---------------------
	/**
	 * This method returns the base path of the application (e. g. 'C:\Java_Workspace\AgentGUI\')
	 * @return the base path of the application
	 */
	public String PathBaseDir( ) {
		return localBaseDir;
	}	
	/**
	 * Aktuelles KLassenverzeichnis der Anwendung fuer die IDE-Umgebung
	 */
	/**
	 * This method returns the base path of the application, when Agent.GUI is running
	 * in its development environment / IDE (e. g. 'C:\Java_Workspace\AgentGUI\bin\')
	 * @return the binary- or bin- base path of the application
	 */
	public String PathBaseDirIDE_BIN( ) {
		return localBaseDir + localPathAgentGUI + fileSeparator;
	}	
	/**
	 * This method can be invoked in order to get the path to the JADE libraries (e. g. 'lib\jade\lib').
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path to the JADE libraries
	 */
	public String PathJade(boolean absolute){
		if (absolute == true) { 
			return FilePath2Absolute( localPathJade );
		}
		else {
			return localPathJade;	
		}		
	}
	/**
	 * This method can be invoked in order to get the path to the property folder 'properties\'.
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path reference to the property folder
	 */
	public String PathProperty(boolean absolute){
		if ( absolute == true ) { 
			return FilePath2Absolute( localPathProperty );
		}
		else {
			return localPathProperty;	
		}	
	}
	/**
	 * This method will return the concrete path to the property file 'agentgui.ini' relatively or absolute
	 * @param absolute set true if you want to get the full path to this
	 * @return the path reference to the property file agentgui.ini
	 */
	public String PathConfigFile(boolean absolute) {
		if (absolute==true) {
			return PathProperty(true) + localFileProperties;
		} else {
			return PathProperty(false) + localFileProperties;
		}
	}
	
	/**
	 * This method will return the path to the project folder ('project\').
	 * If the path does not exists, the path will be created.
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the project folder
	 */
	public String PathProjects(boolean absolute){
		return this.PathProjects(absolute, true);
	}
	
	/**
	 * This method will return the path to the project folder ('project\').
	 *
	 * @param absolute set true if you want to get the full path to this
	 * @param forcePathCreation if true, the path will be created if it not already exists
	 * @return the path to the project folder
	 */
	public String PathProjects(boolean absolute, boolean forcePathCreation){
		String returnPath = null;
		if (absolute == true) { 
			returnPath = FilePath2Absolute(localPathProjects);
		} else {
			returnPath = localPathProjects;	
		}
		// --- See if the folder exists. If not create ---------
		File testFile = new File(returnPath);
		if (testFile.exists()==false) {
			testFile.mkdir();
		}
		return returnPath;
	}
	
	/**
	 * Returns the list of project folders, which are located in the root project folder
	 * @return Array of project folders
	 */
	public String[] getProjectSubDirectories( ){		
		// --- Search for sub folders ---
		String[] localProjects = null;
		File maindir = new File(this.PathProjects(true)) ;
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
	
	/**
	 * Returns the path to the internal img-folder of Agent.GUI (agentgui.core.gui.img)
	 * @return path to the images, which are located in our project
	 */
	public String PathImageIntern( ){
		return localPathImageIntern;
	}

	/**
	 * Here the local root folder for the download-server can be get. In this folder 
	 * sources will be stored, which can be loaded to remote containers in order to 
	 * add them to the ClassPath.   
	 * If the folder doesn't exists, it will be created.
	 * @return Local path to the download server of Agent.GUI ('/AgentGUI/server/')
	 */
	public String PathWebServer(boolean absolute) {
		
		String returnPath = null;
		if (absolute==true) { 
			returnPath = FilePath2Absolute(localPathWebServer);
		}
		else {
			returnPath = localPathWebServer;	
		}
		
		File dir = new File(returnPath);
		if (dir.exists()==false) {
			dir.mkdir();
		}
		return returnPath;
	}
	
	/**
	 * This method returns the folder, where downloaded files, coming from the applications
	 * web-server (server.client) can be stored locally.
	 * If the folder doesn't exists, it will be created.
	 * @return Local path to the folder, where downloads will be saved ('/AgentGUI/download/')
	 */
	public String PathDownloads(boolean absolute) {
		
		String returnPath = null;
		if (absolute==true) { 
			returnPath = FilePath2Absolute(localPathDownloads);
		}
		else {
			returnPath = localPathDownloads;	
		}
		
		File dir = new File(returnPath);
		if (dir.exists()==false) {
			dir.mkdir();
		}
		return returnPath;
	}
	
	/**
	 * Returns the common file postfix for XML-files
	 * @return the xmlFilepostfix
	 */
	public String getXmlFilePostfix() {
		return localXML_FilePostfix;
	}
	/**
	 * Returns the common file postfix for bin-files
	 * @return the xmlFilepostfix
	 */
	public String getBinFilePostfix() {
		return localBIN_FilePostfix;
	}
	/**
	 * Returns the path or file name for a given XML-file name
	 * @param xmlFileName
	 * @return filename with the bin-suffix
	 */
	public String getBinFileNameFromXmlFileName(String xmlFileName) {
		
		String fileName = xmlFileName;
		if(fileName.endsWith(getXmlFilePostfix())) {
			fileName = fileName.substring( 0, (fileName.length()-getXmlFilePostfix().length()) );
			fileName = fileName + getBinFilePostfix();
		}
		
		if (fileName.equals(xmlFileName)==false) {
			return fileName;	
		} else {
			return null;
		}
		
	}
	/**
	 * This method can be used in order to get the applications executable 
	 * jar-file. (by default it is the file 'AgentGui.jar')
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the executable jar file
	 */
	public String getFileRunnableJar(boolean absolute){
		
		if (localAppExecutedOver.equals(ExecutedOverAgentGuiJar)) {
			// --- The current instance was executed over an AgentGui.jar -----
			if (absolute == true && localFileRunnableJar != null) { 
				return FilePath2Absolute(localFileRunnableJar);
			}else {
				return localFileRunnableJar;	
			}	
		} else {
			// --- The current instance was executed by using the IDE ---------
			String path2Jar = "exec" + fileSeparator + "AgentGUI" + fileSeparator + localFileRunnableJar; 
			if (absolute == true) { 
				return FilePath2Absolute(path2Jar);
			} else {
				return path2Jar;	
			}
		}
		
	}
	/**
	 * This method can be use in order to get the path to one of the dictionary files (Base64: '*.bin' | CSV-version: '*.csv'). 
	 * 
	 * @param base64 if set true, the method will return the path to the 
	 * Base64-encoded dictionary file, otherwise the path to the csv-version of the dictionary 
	 * 
	 * @param absolute set true if you want to get the full path to this
	 * @return path to the designated dictionary file
	 */
	public String getFileDictionary(boolean base64, boolean absolute ){
		
		// --- TXT-Version or Base64-encoded dictionary ---
		String fileName = null;
		if (base64==true) {
			fileName = localFileDictionary + ".bin";
		} else {
			fileName = localFileDictionary + ".csv";
		}
		// --- Absolute Path of the dictionary file? ------
		if ( absolute== true ) { 
			return FilePath2Absolute(fileName);
		}
		else {
			return fileName;	
		}
	}
	
	
	/**
	 * Gets the file name of the updater (AgentGuiUpdate.jar).
	 * @param absolute the absolute
	 * @return the file updater
	 */
	public String getFileNameUpdater(boolean absolute) {
		if (absolute == true) { 
			return FilePath2Absolute(localFileRunnableUpdater);
		} else {
			return localFileRunnableUpdater;	
		}
	}
	
	/**
	 * Returns the file name of the xml-file, which contains the project configuration (file: 'agentgui.xml')
	 * @return file name of the project configuration (agentgui.xml)
	 */
	public String getFileNameProject() {
		return localFileNameProject;
	};
	/**
	 * Returns the file name of the binary file, which contains the serializable user object of the Project(file: 'agentgui_userobject.bin')
	 * @return file name of the project user object binary. (agentgui_userobject.bin)
	 */
	public String getFilenameProjectUserObject(){
		return localFileNameProjectUserObject;
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
	/**
	 * This method will return the name of the predefined ontology, which can be used in order 
	 * to merge different ontology's to a single one. ('AgentGUIProjectOntology')
	 * @return name of the predefined meta ontology   
	 */
	public String getFileNameProjectOntology() {
		return localFileNameProjectOntology;
	}


	// ---------------------------------------------------------
	// ---------------------------------------------------------
	/**
	 * This method will convert relative Agent.GUI paths to absolute paths. 
	 * (e. g. './AgentGUI/properties/' will be converted to 'D:/MyWorkspace/AgentGUI/properties')    
	 * @param filePathRelative The relative path to convert
	 * @return The absolute path of the given relative one 
	 */
	private String FilePath2Absolute(String filePathRelative){
		String pathAbsolute = localBaseDir + filePathRelative;
		return pathAbsolute;		
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
	 * This method return the default platform configuration for JADE
	 * @return instance of class 'PlatformJadeConfig', which holds the configuration of JADE
	 * @see PlatformJadeConfig
	 */
	public PlatformJadeConfig getJadeDefaultPlatformConfig() {
		
		// --- Here the default-values can be configured ------------
		PlatformJadeConfig jadeConfig = new PlatformJadeConfig();
		jadeConfig.setLocalPort(localeJadeLocalPort);
		
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
	 * @see Profile
	 */
	public Profile getJadeDefaultProfile() {
		PlatformJadeConfig jadeConfig = getJadeDefaultPlatformConfig();
		return jadeConfig.getNewInstanceOfProfilImpl();
	}

	// ---------------------------------------------------------
	// --- Laufzeitinformationen zu JADE -----------------------
	// ---------------------------------------------------------
	/**
	 * Returns the JADE version
	 * @return String, which contains the version of JADE
	 */
	public String getJadeVersion(){
		return jade.core.Runtime.getVersion();
	}
	
	// ---------------------------------------------------------
	// --- Farbvariablen ---------------------------------------
	// ---------------------------------------------------------
	/**
	 * Due this method the colour for menu highlighting an be get
	 * @return colour for highlighted menus
	 */
	public Color ColorMenuHighLight () {
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
			this.versionInfo = new VersionInfo();
		}
		return this.versionInfo;
	}
	/**
	 * Gets the file properties.
	 * @return the file properties
	 */
	public FileProperties getFileProperties() {
		if (this.fileProperties==null) {
			this.fileProperties = new FileProperties(this);
		}
		return this.fileProperties;
	}

	// ---- SciMark 2.0 Benchmark ----------------------------
	/**
	 * Returns the benchmark value of the current execution of Agent.GUI that 
	 * will be measured at the initial program execution (if required)
	 * @return The benchmark value, stored in the file properties
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public Float getBenchValue() {
		return filePropBenchValue;
	}
	/**
	 * Can be used in order to set the benchmark value in the file properties
	 * @param benchValue The result of the initial benchmark
	 * @see BenchmarkMeasurement
	 * @see FileProperties
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
	 * @see FileProperties
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
	 * @see FileProperties
	 */
	public void setBenchExecOn(String benchExecOn) {
		this.filePropBenchExecOn = benchExecOn;
	}

	/**
	 * Returns if the file properties are configured to always skip the benchmark
	 * @return the filePropBenchAllwaySkip
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public boolean isBenchAllwaysSkip() {
		return filePropBenchAllwaysSkip;
	}
	/**
	 * Can be used in order to set the file properties to always skip the benchmark
	 * @param benchAllwaySkip the filePropBenchAllwaySkip to set
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setBenchAllwaysSkip(boolean benchAllwaySkip) {
		this.filePropBenchAllwaysSkip = benchAllwaySkip;
	}

	// ---- Currently used language ------------------------------------
	/**
	 * This method can be used in order to set the current application language
	 * to the property file. 
	 * @param currentLanguage the filePropLanguage to set
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setLanguage(String currentLanguage) {
		this.filePropLanguage = currentLanguage;
	}
	/**
	 * Returns the currently configured language of the file properties
	 * @return the filePropLanguage
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public String getLanguage() {
		return filePropLanguage;
	}

	// ---- Connection to the Master-Server -------------------------
	/**
	 * Can be used in order to start the active server mode immediately after the program execution.
	 * This applies only if the current execution mode is set to server (server.master or server.slave)
	 * @param serverAutoRun the filePropAutoRun to set
	 * @see FileProperties
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
	 * @see FileProperties
	 * @see GlobalInfo#setServerAutoRun(boolean)
	 */
	public boolean isServerAutoRun() {
		return this.filePropServerAutoRun;
	}
	/**
	 * Here the URLS or IP of the server.master can be set 
	 * @param serverMasterURL the filePropMasterURL to set
	 * @see FileProperties
	 */
	public void setServerMasterURL(String serverMasterURL) {
		this.filePropServerMasterURL = serverMasterURL;
	}
	/**
	 * Here the URL or IP of the server.master can be get
	 * @return the filePropMasterURL
	 * @see FileProperties
	 */
	public String getServerMasterURL() {
		return this.filePropServerMasterURL;
	}
	/**
	 * Here the port for the server.master can be set
	 * @param serverMasterPort the filePropMasterPort to set
	 * @see FileProperties
	 */
	public void setServerMasterPort(Integer serverMasterPort) {
		this.filePropServerMasterPort = serverMasterPort;
	}
	/**
	 * This method returns the port on which s�the server.master can be reached 
	 * @return The port of the server.master
	 * @see FileProperties
	 */
	public Integer getServerMasterPort() {
		return this.filePropServerMasterPort;
	}
	/**
	 * This method can be used in order to set the MTP port of the server.master
	 * @param serverMasterPort4MTP the filePropMasterPort to set
	 * @see FileProperties
	 */
	public void setServerMasterPort4MTP(Integer serverMasterPort4MTP) {
		this.filePropServerMasterPort4MTP = serverMasterPort4MTP;
	}
	/**
	 * Returns the MTP port of the server.master 
	 * @return the filePropMasterPort
	 * @see FileProperties
	 */
	public Integer getServerMasterPort4MTP() {
		return this.filePropServerMasterPort4MTP;
	}

	/**
	 * Database property for the server.nmaster
	 * @param newDBHost the filePropServerMasterDBHost to set
	 * @see FileProperties
	 */
	public void setServerMasterDBHost(String newDBHost) {
		this.filePropServerMasterDBHost = newDBHost;
	}
	/**
	 * Database property for the server.nmaster 
	 * @return the filePropServerMasterDBHost
	 * @see FileProperties
	 */
	public String getServerMasterDBHost() {
		return filePropServerMasterDBHost;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBName the filePropServerMasterDBName to set
	 * @see FileProperties
	 */
	public void setServerMasterDBName(String newDBName) {
		this.filePropServerMasterDBName = newDBName;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBName
	 * @see FileProperties
	 */
	public String getServerMasterDBName() {
		return filePropServerMasterDBName;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBUser the filePropServerMasterDBUser to set
	 * @see FileProperties
	 */
	public void setServerMasterDBUser(String newDBUser) {
		this.filePropServerMasterDBUser = newDBUser;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBUser
	 * @see FileProperties
	 */
	public String getServerMasterDBUser() {
		return filePropServerMasterDBUser;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBPswd the filePropServerMasterDBPswd to set
	 * @see FileProperties
	 */
	public void setServerMasterDBPswd(String newDBPswd) {
		this.filePropServerMasterDBPswd = newDBPswd;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBPswd
	 * @see FileProperties
	 */
	public String getServerMasterDBPswd() {
		return filePropServerMasterDBPswd;
	}

	// ---- Methods for the reminder of the last selected folder ----
	/**
	 * This method can be used in order to remind the last folder 
	 * in which a file was selected (e. g. while using a JFileChooser) 
	 * @param lastSelectedFolder the lastSelectedFolder to set
	 */
	public void setLastSelectedFolder(File lastSelectedFolder) {
		this.lastSelectedFolder = lastSelectedFolder;
	}
	/**
	 * Returns the reminder value of the last selected folder as File object 
	 * @return the lastSelectedFolder
	 */
	public File getLastSelectedFolder() {
		if (lastSelectedFolder==null) {
			lastSelectedFolder = new File(this.PathBaseDir());
		} 
		return lastSelectedFolder;	
	}
	/**
	 * Returns the reminder value of the last selected folder as String
	 * @return the lastSelectedFolder as String
	 */
	public String getLastSelectedFolderAsString() {
		String returnFolder = this.getLastSelectedFolder().getAbsolutePath();
		if (returnFolder.endsWith(File.separator)==false) {
			returnFolder+=File.separator;
		}
		return returnFolder;
	}
	
	// -------------------------------------------------------------------------
	// ---- Methods for EnvironmentTypes ---------------------------------------
	// -------------------------------------------------------------------------
	/**
	 * This method can be used in order to define the predefined environment types of Agent.GUI
	 * @param knownEnvironmentTypes the knowEnvironmentTypes to set
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public void setKnownEnvironmentTypes(EnvironmentTypes knownEnvironmentTypes) {
		this.knownEnvironmentTypes = knownEnvironmentTypes;
	}
	/**
	 * This method returns all EnvironmentTypes known by Agent.GUI 
	 * @return the knowEnvironmentTypes
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public EnvironmentTypes getKnownEnvironmentTypes() {
		return knownEnvironmentTypes;
	}
	/**
	 * This method can be used in order to add a tailored environment type
	 * (assume for example a 3D-environment model)  
	 * @param envType
	 * @see EnvironmentType
	 */
	public void addEnvironmentType(EnvironmentType envType ) {
		this.knownEnvironmentTypes.add(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envType The EnvironmentType instance
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(EnvironmentType envType) {
		this.knownEnvironmentTypes.remove(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envTypeKey The key expression of the environment type
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(String envTypeKey) {
		EnvironmentType envType = this.knownEnvironmentTypes.getEnvironmentTypeByKey(envTypeKey);
		this.removeEnvironmentType(envType);
	}
	
	// -------------------------------------------------------------------------
	// ---- Methods for OntologyClassVisualisations ----------------------------
	// -------------------------------------------------------------------------
	/**
	 * Register an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public OntologyClassVisualisation registerOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		
		try {
			if (this.isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==false) {
				Class<?> clazz = Class.forName(classNameOfOntologyClassVisualisation);
				OntologyClassVisualisation ontoClassVisualisation = (OntologyClassVisualisation) clazz.newInstance();
				this.getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
				return ontoClassVisualisation;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public void unregisterOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		if (this.isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==true) {
			OntologyClassVisualisation ontoClassVisualisation=getOntologyClassVisualisation(classNameOfOntologyClassVisualisation);
			this.getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
		}
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param ontologyClassVisualisation the OntologyClassVisualisation to unregister
	 */
	public void unregisterOntologyClassVisualisation(OntologyClassVisualisation ontologyClassVisualisation) {
		if (this.isOntologyClassVisualisation(ontologyClassVisualisation)==true) {
			this.getKnownOntologyClassVisualisations().add(ontologyClassVisualisation);	
		}
	}
	
	/**
	 * Returns the known ontology class visualisations.
	 * @return the Vector of known ontology class visualisations
	 */
	public Vector<OntologyClassVisualisation> getKnownOntologyClassVisualisations() {
		if (knownOntologyClassVisualisation==null) {
			knownOntologyClassVisualisation = new Vector<OntologyClassVisualisation>();
		}
		return knownOntologyClassVisualisation;
	}
	
	/**
	 * Checks if a given object can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkObject the object to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(Object checkObject) {
		if (checkObject==null) return false;
		return this.isOntologyClassVisualisation(checkObject.getClass());
	}
	/**
	 * Checks if a given class can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkClass the class to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(Class<?> checkClass) {
		if (checkClass==null) return false;
		return this.isOntologyClassVisualisation(checkClass.getName());
	}
	/**
	 * Checks class, given by its name, can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param className the object to check
	 * @return true, if the given className is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(String className) {
		
		boolean isVisClass = false;
		
		Vector<OntologyClassVisualisation> ontoClassViss = this.getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String visClassName = ontoClassVis.getOntologyClass().getName();
			if (visClassName.equals(className)) {
				isVisClass=true;
				break;
			}
		}
		return isVisClass;
	}
	
	/**
	 * Returns the OntologyClassVisualisation for a given object.
	 *
	 * @param checkObject the check object
	 * @return the ontology class visualisation
	 */
	public OntologyClassVisualisation getOntologyClassVisualisation(Class<?> checkObject) {
		return this.getOntologyClassVisualisation(checkObject.getName());
	}
	/**
	 * Returns the OntologyClassVisualisation for a given class, specified by its name.
	 *
	 * @param className the class name
	 * @return the OntologyClassVisualisation
	 */
	public OntologyClassVisualisation getOntologyClassVisualisation(String className) {
		
		OntologyClassVisualisation ontoClassVisFound = null;
		Vector<OntologyClassVisualisation> ontoClassViss = this.getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String compareWith = ontoClassVis.getOntologyClass().getName();
			if (compareWith.equals(className)==true) {
				ontoClassVisFound=ontoClassVis;
				break;
			}
		}
		return ontoClassVisFound;
	}
	

	// -------------------------------------------------------------------------
	// ---- Methods for the API Key for the Google Translation -----------------
	// -------------------------------------------------------------------------
	/**
	 * Returns the API key for Google.
	 * @return the API key for Google
	 */
	public String getGoogleKey4API() {
		return this.googleKey4API;
	}
	/**
	 * Sets the API key for Google.
	 * @param key4API the new API key for Google.
	 */
	public void setGoogleKey4API(String key4API) {
		this.googleKey4API = key4API;
	}
	/**
	 * Returns the HTTP-Reference for Google translate.
	 * @return the HTTP-Reference for Google translate
	 */
	public String getGoogleHttpRef() {
		return this.googleHTTPref;
	}
	/**
	 * Sets the HTTP-Reference for Google translate.
	 * @param httpRef the new HTTP-Reference for Google translate.
	 */
	public void setGoogleHttpRef(String httpRef) {
		this.googleHTTPref = httpRef;
	}

	/**
	 * Sets the update site.
	 * @param updateSite the new update site
	 */
	public void setUpdateSite(String updateSite) {
		this.updateSite = updateSite;
	}
	/**
	 * Returns the update site.
	 * @return the update site
	 */
	public String getUpdateSite() {
		return updateSite;
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
	 * Gets the device service agent selected.
	 * @return the device service agent selected
	 */
	public String getDeviceServiceAgentSelected() {
		return deviceServiceAgentSelected;
	}
	/**
	 * Sets the device service agent selected.
	 * @param deviceServiceAgentSelected the new device service agent selected
	 */
	public void setDeviceServiceAgentSelected(String deviceServiceAgentSelected) {
		this.deviceServiceAgentSelected = deviceServiceAgentSelected;
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
	
	
}
