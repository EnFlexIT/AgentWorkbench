package agentgui.core.config;

import jade.core.Profile;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.jade.PlatformJadeConfig;

public class GlobalInfo {

	// --- Konstanten ------------------------------------------------------- 
	final private static String localAppTitel = "Agent.GUI";
	final private static String localAppVersion = "0.7";
	
	final private static String localAppPathSeparatorString = File.separator;
	final private static String localAppNewLineString = System.getProperty("line.separator");
	final private static String localAppNewLineStringReplacer = "<br>";
	final private static String localPathImageIntern = "/agentgui/core/gui/img/";

	private boolean localAppUseInternalConsole = false;
	
	// --- JADE-Variablen ---------------------------------------------------
	private Integer localeJadeLocalPort = 1099;
	
	final private static Color localColorMenuHighLight =  new Color(0,0,192);
	
	// --- Variablen --------------------------------------------------------
	private static Vector<String> localClassPathEntries = new Vector<String>();
	private static String localAppExecutedOver = "IDE";
	private static String localAppLnF = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	private static String localBaseDir = "";
	private static String localPathAgentGUI	= "bin";
	private static String localPathJade		= "lib" + localAppPathSeparatorString + "jade" +  localAppPathSeparatorString + "lib";
	private static String localPathBatik	= "lib" + localAppPathSeparatorString + "batik";	
	private static String localPathProperty = "properties" + localAppPathSeparatorString;
	private static String localPathProjects =  "projects" + localAppPathSeparatorString;
	private static String localPathDownloads =  "downloads" + localAppPathSeparatorString;
	
	private static String localPathProjectsIDE =  localPathProjects;
	private static String[] localProjects = null;
	
	private static String localFileRunnableJar = "AgentGui.jar";
	private static String localFileDictionary  = localPathProperty + "dictionary.csv";
	private static String localFileProperties  = "agentgui.ini";
	private static String localFileNameProject = "agentgui.xml";
	private static String localFileNameProjectOntology = "AgentGUIProjectOntology";
	
	// --- File-Properties --------------------------------------------------
	private boolean filePropRunAsServer = false;
	
	private float filePropBenchValue = 0;
	private String filePropBenchExecOn = null;
	private boolean filePropBenchAllwaysSkip = false; 
	
	private boolean filePropServerAutoRun = false;
	private String filePropServerMasterURL = null;
	private Integer filePropServerMasterPort = this.localeJadeLocalPort;
	private Integer filePropServerMasterPort4MTP = 7778;
	
	private String filePropServerMasterDBHost = null;
	private String filePropServerMasterDBName = null;
	private String filePropServerMasterDBUser = null;
	private String filePropServerMasterDBPswd = null;
	
	// ----------------------------------------------------------------------
	// --- Objekt-Initialisierung -------------------------------------------
	// ----------------------------------------------------------------------
	/**
	 * Constructor of this class
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
	}
	// ----------------------------------------------------------------------
	
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	/**
	 * Returns the titel of the application 
	 */
	public String AppTitel() {
		return localAppTitel;
	};
	/**
	 * @return the localappversion
	 */
	public String AppVersion() {
		return localAppVersion;
	}
	/**
	 * Here the use of the application internal console can be set
	 * @param useInternalConsole 
	 */
	public void setAppUseInternalConsole(boolean useInternalConsole) {
		this.localAppUseInternalConsole = useInternalConsole;
	}
	/**
	 * Returns if the System-Output ('Out' or 'Err') goe's throw this application 
	 */
	public boolean isAppUseInternalConsole() {
		return localAppUseInternalConsole;
	};
	// -------------------------------
	// --- Look and Feel -------------
	// -------------------------------
	/**
	 * Aktuelle Look and Feel - Einstellung 
	 */
	public String AppLnF() {
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
	 * Look and Feel - Einstellung  aendern
	 */
	public void setAppLnf( String NewLnF ) {
		localAppLnF = NewLnF;
	};
	/**
	 * @return the ClassPath-Entries of the application
	 */
	public Vector<String> getClassPathEntries() {
		return localClassPathEntries;
	}
	/**
	 * This Method returns all external jar-Files, which 
	 * are part of the ClassPath
	 * @return
	 */
	public Vector<String> getClassPathExternalJars() {
		Vector<String> external = new Vector<String>();
		for (String classPathEntry : localClassPathEntries) {
			if ( classPathEntry.endsWith(".jar")==true ){
				if (localFileRunnableJar==null) {
					external.add(classPathEntry);
				} else {
					if (classPathEntry.endsWith(localFileRunnableJar)==false) {
						external.add(classPathEntry);
					}
				}
			}
		}
		return external;
	}
	// -------------------------------
	/**
	 * 'IDE' oder 'Executable' (jar-File) 
	 */
	public String AppExecutedOver() {
		return localAppExecutedOver;
	}
	/**
	 * Verzeichnistrenner: Win='\' Linux='/'
	 */
	public String AppPathSeparatorString() {
		return localAppPathSeparatorString;
	}
	/**
	 * String for the 'new line' charakter inside of this application
	 */
	public String AppNewLineString(){
		return localAppNewLineString;
	}
	/**
	 * String for the 'new line' charakter inside of a text file
	 */
	public String AppNewLineStringReplacer(){
		return localAppNewLineStringReplacer;
	}

	// --- Allgemeine Verzeichnisangaben ---------------------
	/**
	 * Aktuelles Basisverzeichnis der Anwendung
	 */
	public String PathBaseDir( ) {
		return localBaseDir;
	}	
	/**
	 * Aktuelles KLassenverzeichnis der Anwendung fuer die IDE-Umgebung
	 */
	public String PathBaseDirIDE_BIN( ) {
		return localBaseDir + localPathAgentGUI + localAppPathSeparatorString;
	}	
	/**
	 * Unterverzeichnis fuer die JADE-Libraries
	 */
	public String PathJade( Boolean Absolute ){
		if ( Absolute == true ) { 
			return FilePath2Absolute( localPathJade );
		}
		else {
			return localPathJade;	
		}		
	}
	/**
	 * Unterverzeichnis fuer die Batik-Libraries
	 */
	public String PathBatik( Boolean Absolute ){
		if ( Absolute == true ) { 
			return FilePath2Absolute( localPathBatik );
		}
		else {
			return localPathBatik;	
		}	
	}
	/**
	 * Unterverzeichnis fuer Eigenschaftsdateien
	 */
	public String PathProperty( Boolean Absolute ){
		if ( Absolute == true ) { 
			return FilePath2Absolute( localPathProperty );
		}
		else {
			return localPathProperty;	
		}	
	}
	/**
	 * Unterverzeichnis für die Properties-Datei
	 */
	public String getPathConfigFile() {
		return PathProperty(false) + localFileProperties;
	}
	/**
	 * Unterverzeichnis fuer Projekte
	 */
	public String PathProjects( boolean Absolute, boolean UseImportSpecification ){

		if ( UseImportSpecification == true ) {
			String ImportPath = localPathProjects;
			ImportPath = ImportPath.replace( localAppPathSeparatorString, "." );
			ImportPath = ImportPath.replace("src.","");
			return ImportPath;
		}
		if ( Absolute == true ) { 
			return FilePath2Absolute( localPathProjects );
		}
		else {
			return localPathProjects;	
		}	
	}
	
	/**
	 * Returns Main-Projectfolder, which are located in "./src/mas/projects"
	 */
	public String[] getIDEProjects( ){		
		// --- Projektverzeichnis nach Unterverzeichnissen durchsuchen --
		localProjects = null;
		File maindir = new File( PathProjects( true, false ) ) ;
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
	 * Bild-Unterverzeichnis fuer das Projekt
	 */
	public String PathImageIntern( ){
		return localPathImageIntern;
	}
	
	/**
	 * Verzeichnis zum Download von Projekt-Ressourcen
	 * @return
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
	
	// --- Allgemeine Dateiangaben ---------------------
	/**
	 * Standardname *.jar-Bezeichner dieses Projekts (Default: 'AgentGui.jar') 
	 */
	public String AppFileRunnableJar( boolean Absolute ){
		if ( Absolute == true && localFileRunnableJar != null ) { 
			return FilePath2Absolute( localFileRunnableJar );
		}
		else {
			return localFileRunnableJar;	
		}	
	}
	/**
	 * Verweis auf die Dictionary-Datei zur Sprachuebersetzung
	 */
	public String FileDictionary( Boolean Absolute ){
		if ( Absolute== true ) { 
			return FilePath2Absolute( localFileDictionary );
		}
		else {
			return localFileDictionary;	
		}
	}
	public String getFileNameProject() {
		return localFileNameProject;
	};
	/**
	 * @return the localFileProjectOntology
	 */
	public String getFileNameProjectOntology() {
		return localFileNameProjectOntology;
	}


	// ---------------------------------------------------------
	// ---------------------------------------------------------
	/**
	 * Macht aus einer relativen Verzeichnisangabe eine 
	 * Absolute Verzeichnisangabe
	 */
	private String FilePath2Absolute( String FilePathRelative ){
		String PathAbsolute = localBaseDir + FilePathRelative;
		return PathAbsolute;		
	}
	// ---------------------------------------------------------
	// ---------------------------------------------------------

	
	/**
	 * @param localeJadeDefaultPort the localeJadeDefaultPort to set
	 */
	public void setJadeLocalPort(int localeJadeDefaultPort) {
		localeJadeLocalPort = localeJadeDefaultPort;
	}
	/**
	 * @return the localeJadeDefaultPort
	 */
	public Integer getJadeLocalPort() {
		return localeJadeLocalPort;
	}
	
	/**
	 * This method return the default Platform-Configuration
	 * @return instance of class 'PlatformJadeConfig'
	 */
	public PlatformJadeConfig getJadeDefaultPlatformConfig() {
		
		// --- Here the default-values can be configured ------------
		PlatformJadeConfig jadeConfig = new PlatformJadeConfig();
		jadeConfig.runNotificationService(true);
		jadeConfig.runSimulationService(true);
		jadeConfig.setLocalPort(localeJadeLocalPort);
		
		if (Application.isServer==false) {
			// --- Running as application ---------------------------
			jadeConfig.runAgentMobilityService(true);
		}
		return jadeConfig;
	}
	/**
	 * This Method returns the default Jade-Container-Profile
	 * of the AgentGUI-Application 
	 * @return the localeJadeDefaultProfile
	 */
	public Profile getJadeDefaultProfile() {
		PlatformJadeConfig jadeConfig = getJadeDefaultPlatformConfig();
		return jadeConfig.getNewInstanceOfProfilImpl();
	}

	// ---------------------------------------------------------
	// --- Laufzeitinformationen zu JADE -----------------------
	// ---------------------------------------------------------
	public String getJadeVersion(){
		return jade.core.Runtime.getVersion();
	}
	
	// ---------------------------------------------------------
	// --- Farbvariablen ---------------------------------------
	// ---------------------------------------------------------
	public Color ColorMenuHighLight () {
		return localColorMenuHighLight;
	}


	// ---------------------------------------------------------
	// --- File-Properties -------------------------------------
	// ---------------------------------------------------------
	/**
	 * @param filePropRunAsServer the filePropRunAsServer to set
	 */
	public void setRunAsServer(boolean runAsServer) {
		this.filePropRunAsServer = runAsServer;
		Application.isServer = runAsServer;
	}
	/**
	 * @return the filePropRunAsServer
	 */
	public boolean isRunAsServer() {
		return this.filePropRunAsServer;
	}

	// ---- SciMark 2.0 Benchmark ----------------------------
	/**
	 * @return the filePropBenchValue
	 */
	public Float getBenchValue() {
		return filePropBenchValue;
	}
	/**
	 * @param filePropBenchValue the filePropBenchValue to set
	 */
	public void setBenchValue(float benchValue) {
		this.filePropBenchValue = benchValue;
	}

	/**
	 * @return the filePropBenchExecOn
	 */
	public String getBenchExecOn() {
		return filePropBenchExecOn;
	}
	/**
	 * @param filePropBenchExecOn the filePropBenchExecOn to set
	 */
	public void setBenchExecOn(String benchExecOn) {
		this.filePropBenchExecOn = benchExecOn;
	}

	/**
	 * @return the filePropBenchAllwaySkip
	 */
	public boolean isBenchAllwaysSkip() {
		return filePropBenchAllwaysSkip;
	}
	/**
	 * @param filePropBenchAllwaySkip the filePropBenchAllwaySkip to set
	 */
	public void setBenchAllwaysSkip(boolean benchAllwaySkip) {
		this.filePropBenchAllwaysSkip = benchAllwaySkip;
	}

	// ---- Connection to the Master-Server ----------------------------
	/**
	 * @param filePropAutoRun the filePropAutoRun to set
	 */
	public void setServerAutoRun(boolean serverAutoRun) {
		this.filePropServerAutoRun = serverAutoRun;
	}
	/**
	 * @return the filePropAutoRun
	 */
	public boolean isServerAutoRun() {
		return this.filePropServerAutoRun;
	}
	/**
	 * @param filePropMasterURL the filePropMasterURL to set
	 */
	public void setServerMasterURL(String serverMasterURL) {
		this.filePropServerMasterURL = serverMasterURL;
	}
	/**
	 * @return the filePropMasterURL
	 */
	public String getServerMasterURL() {
		return this.filePropServerMasterURL;
	}
	/**
	 * @param filePropMasterPort the filePropMasterPort to set
	 */
	public void setServerMasterPort(Integer serverMasterPort) {
		this.filePropServerMasterPort = serverMasterPort;
	}
	/**
	 * @return the filePropMasterPort
	 */
	public Integer getServerMasterPort() {
		return this.filePropServerMasterPort;
	}
	/**
	 * @param filePropMasterPort the filePropMasterPort to set
	 */
	public void setServerMasterPort4MTP(Integer serverMasterPort) {
		this.filePropServerMasterPort4MTP = serverMasterPort;
	}
	/**
	 * @return the filePropMasterPort
	 */
	public Integer getServerMasterPort4MTP() {
		return this.filePropServerMasterPort4MTP;
	}

	/**
	 * @param filePropServerMasterDBHost the filePropServerMasterDBHost to set
	 */
	public void setServerMasterDBHost(String newDBHost) {
		this.filePropServerMasterDBHost = newDBHost;
	}
	/**
	 * @return the filePropServerMasterDBHost
	 */
	public String getServerMasterDBHost() {
		return filePropServerMasterDBHost;
	}
	/**
	 * @param filePropServerMasterDBName the filePropServerMasterDBName to set
	 */
	public void setServerMasterDBName(String newDBName) {
		this.filePropServerMasterDBName = newDBName;
	}
	/**
	 * @return the filePropServerMasterDBName
	 */
	public String getServerMasterDBName() {
		return filePropServerMasterDBName;
	}
	/**
	 * @param filePropServerMasterDBUser the filePropServerMasterDBUser to set
	 */
	public void setServerMasterDBUser(String newDBUser) {
		this.filePropServerMasterDBUser = newDBUser;
	}
	/**
	 * @return the filePropServerMasterDBUser
	 */
	public String getServerMasterDBUser() {
		return filePropServerMasterDBUser;
	}
	/**
	 * @param filePropServerMasterDBPswd the filePropServerMasterDBPswd to set
	 */
	public void setServerMasterDBPswd(String newDBPswd) {
		this.filePropServerMasterDBPswd = newDBPswd;
	}
	/**
	 * @return the filePropServerMasterDBPswd
	 */
	public String getServerMasterDBPswd() {
		return filePropServerMasterDBPswd;
	}

}
