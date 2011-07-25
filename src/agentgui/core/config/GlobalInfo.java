/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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

import jade.core.Profile;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.common.ClassLoaderUtil;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.environment.EnvironmentTypes;
import agentgui.core.jade.PlatformJadeConfig;
import agentgui.graphEnvironment.controller.GraphEnvironmentControllerGUI;
import agentgui.physical2Denvironment.controller.Physical2DEnvironmentControllerGUI;

/**
 * This class is for constant values or variables, which can
 * be accessed or used application wide.<br>
 * In the Application class the running instance can be accessed 
 * by using the reference Application.RunInfo. 
 * 
 * @see Application#RunInfo
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GlobalInfo {

	// --- constant values -------------------------------------------------- 
	private static String localAppTitle = "Agent.GUI";
	private final static String localAppVersion = "0.98";
	
	private final static String localAppPathSeparatorString = File.separator;
	private final static String localAppNewLineString = System.getProperty("line.separator");
	private final static String localAppNewLineStringReplacer = "<br>";
	private final static String localPathImageIntern = "/agentgui/core/gui/img/";

	private final static Color localColorMenuHighLight =  new Color(0,0,192);
	
	private boolean localAppUseInternalConsole = false;
	
	// --- JADE-Variables ---------------------------------------------------
	private Integer localeJadeLocalPort = 1099;
	
	// --- Variablen --------------------------------------------------------
	private static Vector<String> localClassPathEntries = new Vector<String>();
	private static String localAppExecutedOver = "IDE";
	private static String localAppLnF = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	private static String localBaseDir = "";
	private static String localPathAgentGUI	= "bin";
	private static String localPathJade		= "lib" + localAppPathSeparatorString + "jade" +  localAppPathSeparatorString + "lib";
	private static String localPathBatik	= "lib" + localAppPathSeparatorString + "batik";	
	private static String localPathProperty = "properties" + localAppPathSeparatorString;
	private static String localPathProjects = "projects" + localAppPathSeparatorString;
	private static String localPathServer   = "server" + localAppPathSeparatorString;
	private static String localPathDownloads= "download" + localAppPathSeparatorString;
	
	private static String localPathProjectsIDE =  localPathProjects;
	private static String[] localProjects = null;
	
	private static String localFileRunnableJar = "AgentGui.jar";
	private static String localFileDictionary  = localPathProperty + "dictionary";
	private static String localFileProperties  = "agentgui.ini";
	private static String localFileNameProject = "agentgui.xml";
	private static String localFileNameProjectUserObject = "agentgui.bin";
	private static String localFileEndProjectZip = "agui";
	private static String localFileNameProjectOntology = "AgentGUIProjectOntology";
	
	private static String localFileJade = "jade.jar";
	
	// --- Known EnvironmentTypes of Agent.GUI ------------------------------
	private EnvironmentTypes knowEnvironmentTypes =  new EnvironmentTypes();
	
	// --- File-Properties --------------------------------------------------
	private boolean filePropRunAsServer = false;
	
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
	
	// --- Reminder information for file dialogs ----------------------------
	private File lastSelectedFolder = null; 
	
	/**
	 * Constructor of this class. 
	 */
	public GlobalInfo() {

		Integer  CutAt = 0;
		String[] JCP_Files = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		String[] JCP_Folders = JCP_Files.clone(); 
		HashSet<String> Folders = new HashSet<String>();  
		localClassPathEntries.addAll( Arrays.asList( JCP_Files ) );		
		
		// ------------------------------------------------------------------
		// --- Class-Path untersuchen ---------------------------------------
		for (int i=0; i<JCP_Files.length; i++) {

			if ( JCP_Files[i].endsWith( localFileRunnableJar )  ) {
				localAppExecutedOver = "Executable";
				// --- Bei jar, immer interne Console verwenden -------------
				this.setAppUseInternalConsole(true);
				CutAt = JCP_Files[i].lastIndexOf( localAppPathSeparatorString ) + 1;
				localBaseDir = JCP_Folders[i].substring(0, CutAt);	

				// --- jade.jar in die ClassLoaderUtility einbinden ----------
				String jadeJar = this.PathJade(true) + File.separator + localFileJade;
				ClassLoaderUtil.addJarToClassPath(jadeJar);
				
			};
			if ( JCP_Files[i].endsWith(".jar")  ) {
				// ----------------------------------------------------------
				// --- Dateinamen herausnehmen ------------------------------
				CutAt = JCP_Files[i].lastIndexOf( localAppPathSeparatorString );
				if(CutAt!=-1){ //only if seperator was actually found
					JCP_Folders[i] = JCP_Folders[i].substring(0, CutAt);
				}
			}	
			Folders.add(JCP_Folders[i]);						
		}
		
		if ( localAppExecutedOver.equals("IDE")) {
			// --------------------------------------------------------------
			// --- Verzeichnis-Eintraege eindeutig (unique) machen ----------
			JCP_Folders = (String[])Folders.toArray(new String[Folders.size()]);
			for (int j = 0; j < JCP_Folders.length; j++) {				
				if ( JCP_Folders[j].endsWith( localPathAgentGUI ) ) {
					// --- bin-Verzeichnis gefunden ---					
					CutAt = JCP_Folders[j].lastIndexOf( localPathAgentGUI );
					localBaseDir =  JCP_Folders[j].substring(0, CutAt);
					break;
				}
			} // -- End 'for' --

			// --------------------------------------------------------------
			// --- Bei Ausfuehrung IDE, einige Variablen umstellen ----------			
			localFileRunnableJar = null;
			localPathProjects =  localPathProjectsIDE;
			
		}
		// ------------------------------------------------------------------
		
		// ------------------------------------------------------------------
		// --- Define the known EnvironmentTypes of Agent.GUI ---------------
		String envKey = null;
		String envDisplayName = null;
		Class<? extends EnvironmentPanel> envPanelClass = null;
		EnvironmentType envType = null;
		
		// --- No environment -----------------------------------------------
		envKey = "none";
		envDisplayName = "Kein vordefiniertes Umgebungsmodell verwenden";
		envPanelClass = null;
		envType = new EnvironmentType(envKey, envDisplayName, envPanelClass);
		addEnvironmentType(envType);
		
		// --- Continuous 2D environment ------------------------------------
		envKey = "continous2Denvironment";
		envDisplayName = "Kontinuierliches 2D-Umgebungsmodell";
		envPanelClass = Physical2DEnvironmentControllerGUI.class;
		envType = new EnvironmentType(envKey, envDisplayName, envPanelClass);
		addEnvironmentType(envType);
		
		// --- Grid Environment ---------------------------------------------
		envKey = "gridEnvironment";
		envDisplayName = "Graph bzw. Netzwerk";
		envPanelClass = GraphEnvironmentControllerGUI.class;
		envType = new EnvironmentType(envKey, envDisplayName, envPanelClass);
		addEnvironmentType(envType);
		
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
	
	/**
	 * This method returns the current version number of the application 
	 * @return the version number of the current application 
	 */
	public String AppVersion() {
		return localAppVersion;
	}
	/**
	 * Here the use of the applications internal console can be set
	 * @param useInternalConsole 
	 */
	public void setAppUseInternalConsole(boolean useInternalConsole) {
		this.localAppUseInternalConsole = useInternalConsole;
	}
	/**
	 * This method can be used in order to evaluate if the internal 
	 * applications console is used 
	 * @return true or false
	 */
	public boolean isAppUseInternalConsole() {
		return localAppUseInternalConsole;
	};
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
	/**
	 * This method returns the ClassPath-Entries of the application 
	 * @return the ClassPath-Entries 
	 */
	public Vector<String> getClassPathEntries() {
		return localClassPathEntries;
	}
	/**
	 * This Method returns all jar-Files, which are part of the current ClassPath
	 * @return Returns a Vector<String> with all external jars
	 */
	public Vector<String> getClassPathJars() {
		Vector<String> jarFileInCP = new Vector<String>();
		for (String classPathEntry : localClassPathEntries) {
			if ( classPathEntry.endsWith(".jar")==true ){
				jarFileInCP.add(classPathEntry);
			}
		}
		return jarFileInCP;
	}
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
	 * Returns the separator String for files like File.separator 
	 * @return the file separator like '\' for Windows OR '/' for Linux
	 */
	public String AppPathSeparatorString() {
		return localAppPathSeparatorString;
	}
	/**
	 * This method returns the actual String for a new line string
	 * @return a String that can be used for new lines in text  
	 */
	public String AppNewLineString(){
		return localAppNewLineString;
	}
	/**
	 * This method will return a substitute String for the new line String.
	 * It is used, for example, in the dictionary, so that it is possible
	 * to write text with several lines in a single one.<br>
	 * Basically, the HTML-tag <<i>br</i>> is used here.
	 * 
	 * @return a substitute String for a new line
	 */
	public String AppNewLineStringReplacer(){
		return localAppNewLineStringReplacer;
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
		return localBaseDir + localPathAgentGUI + localAppPathSeparatorString;
	}	
	/**
	 * This method can be invoked in order to get the path to the JADE libraries (e. g. 'lib\jade\lib').
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path to the JADE libraries
	 */
	public String PathJade(boolean absolute){
		if ( absolute == true ) { 
			return FilePath2Absolute( localPathJade );
		}
		else {
			return localPathJade;	
		}		
	}
	/**
	 * This method can be invoked in order to get the path to the BATIK libraries (e. g. 'lib\batik').
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path to the BATIK libraries
	 */
	public String PathBatik(boolean absolute){
		if (absolute == true) { 
			return FilePath2Absolute( localPathBatik );
		}
		else {
			return localPathBatik;	
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
	 * This method will return the path to the project folder ('project\')
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the project folder
	 */
	public String PathProjects(boolean absolute){
		if ( absolute == true ) { 
			return FilePath2Absolute( localPathProjects );
		}
		else {
			return localPathProjects;	
		}	
	}
	
	/**
	 * Returns the list of project folders, which are located in the root project folder
	 * @return Array of project folders
	 */
	public String[] getIDEProjects( ){		
		// --- Search for sub folders ---
		localProjects = null;
		File maindir = new File( this.PathProjects( true ) ) ;
		File files[] = maindir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory() && !files[i].getName().substring(0, 1).equals(".") ) {
				if (localProjects == null) {						
					String[] AddEr = { files[i].getName() };	
					localProjects = AddEr;	
				}
				else {
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
	public String PathServer(boolean absolute) {
		
		String returnPath = null;
		if (absolute==true) { 
			returnPath = FilePath2Absolute(localPathServer);
		}
		else {
			returnPath = localPathServer;	
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
	 * This method can be used in order to get the applications executable 
	 * jar-file. (by default it is the file 'AgentGui.jar')
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the executable jar file
	 */
	public String AppFileRunnableJar(boolean absolute){
		if ( absolute == true && localFileRunnableJar != null ) { 
			return FilePath2Absolute( localFileRunnableJar );
		}
		else {
			return localFileRunnableJar;	
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
	public String FileDictionary(boolean base64, boolean absolute ){
		
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
		jadeConfig.runNotificationService(true);
		jadeConfig.runLoadService(true);
		jadeConfig.runSimulationService(true);
		jadeConfig.setLocalPort(localeJadeLocalPort);
		
		if (Application.isServer==false) {
			// --- Running as application ---------------------------
			jadeConfig.runAgentMobilityService(true);
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
	 * This method can be used in order to configure the current execution 
	 * of Agent.GUI as an server tool (for 'server.master' or 'server.slave') 
	 * @param runAsServer The boolean to set
	 */
	public void setRunAsServer(boolean runAsServer) {
		this.filePropRunAsServer = runAsServer;
		Application.isServer = runAsServer;
	}
	/**
	 * Can be accessed in order to find out whether the current execution 
	 * of Agent.GUI is running as server or as application
	 * @return true if the application is running in server mode - otherwise false
	 */
	public boolean isRunAsServer() {
		return this.filePropRunAsServer;
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
	 * This method returns the port on which s´the server.master can be reached 
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
	
	// ---- Methods for EnvironmentTypes ----------------------------
	/**
	 * This method can be used in order to define the predefined environment types of Agent.GUI
	 * @param knowEnvironmentTypes the knowEnvironmentTypes to set
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public void setKnowEnvironmentTypes(EnvironmentTypes knowEnvironmentTypes) {
		this.knowEnvironmentTypes = knowEnvironmentTypes;
	}
	/**
	 * This method returns all EnvironmentTypes known by Agent.GUI 
	 * @return the knowEnvironmentTypes
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public EnvironmentTypes getKnowEnvironmentTypes() {
		return knowEnvironmentTypes;
	}
	/**
	 * This method can be used in order to add a tailored environment type
	 * (assume for example a 3D-environment model)  
	 * @param envType
	 * @see EnvironmentType
	 */
	public void addEnvironmentType(EnvironmentType envType ) {
		this.knowEnvironmentTypes.add(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envType The EnvironmentType instance
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(EnvironmentType envType) {
		this.knowEnvironmentTypes.remove(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envTypeKey The key expression of the environment type
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(String envTypeKey) {
		EnvironmentType envType = this.knowEnvironmentTypes.getEnvironmentTypeByKey(envTypeKey);
		this.knowEnvironmentTypes.remove(envType);
	}
	
}
