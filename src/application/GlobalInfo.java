package application;

import java.io.File;
import java.util.HashSet;

public class GlobalInfo {

	// --- Konstanten ------------------------------------------------------- 
	final private static String LocalAppTitel = "Agent.GUI";
	
	final private static String LocalAppPathSeparatorString = File.separator;
	final private static String LocalAppNewLineString = System.getProperty("line.separator");
	final private static String LocalPathImageIntern = "/img/";
	final private static String LocalJadeVersion = jade.core.Runtime.getVersion();
	
	// --- Variablen --------------------------------------------------------
	private static String LocalAppExecutedOver = "IDE";
	private static String LocalAppLnF = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	
	private static String LocalBaseDir = "";
	private static String LocalPathAgentGUI	= "bin";
	private static String LocalPathJade		= "lib" + LocalAppPathSeparatorString + "jade" +  LocalAppPathSeparatorString + "lib";
	private static String LocalPathBatik	= "lib" + LocalAppPathSeparatorString + "batik";	
	private static String LocalPathProperty = "properties" + LocalAppPathSeparatorString;
	
	private static String LocalFileRunnableJar = "AgentGui.jar";
	private static String LocalFileDictionary  = LocalPathProperty + "dictionary.csv";

	
	// ----------------------------------------------------------------------
	// --- Objekt-Initialisierung -------------------------------------------
	// ----------------------------------------------------------------------
	public GlobalInfo() {
		/**
		 * Ermittlung der lokalen Einstellungen, sobald ein 
		 * Objekt dieser Klasse erzeugt wird.  
		 */
		Integer  CutAt = 0;
		String[] JCP_Files = System.getProperty("java.class.path").split( System.getProperty("path.separator") );
		String[] JCP_Folders = JCP_Files.clone(); 
		HashSet<String> Folders = new HashSet<String>();  
		
		// ------------------------------------------------------------------
		// --- Class-Path untersuchen ---------------------------------------
		for (int i=0; i<JCP_Files.length; i++) {
			if ( JCP_Files[i].endsWith( LocalFileRunnableJar )  ) {
				LocalAppExecutedOver = "Executable";
				CutAt = JCP_Files[i].lastIndexOf( LocalAppPathSeparatorString ) + 1;
				LocalBaseDir =  JCP_Folders[i].substring(0, CutAt);				 
			};
			if ( JCP_Files[i].endsWith(".jar")  ) {
				// ----------------------------------------------------------
				// --- Dateinamen herausnehmen ------------------------------
				CutAt = JCP_Files[i].lastIndexOf( LocalAppPathSeparatorString );
				JCP_Folders[i] = JCP_Folders[i].substring(0, CutAt);
			}	
			Folders.add(JCP_Folders[i]);						
		}
		if ( LocalAppExecutedOver == "IDE"  ) {
			// -------------------------------------------------------------
			// --- Verzeichnis-Einträge eindeutig (unique) machen -----------
			JCP_Folders = (String[])Folders.toArray(new String[Folders.size()]);
			for (int j = 0; j < JCP_Folders.length; j++) {				
				if ( JCP_Folders[j].indexOf( LocalPathAgentGUI ) != -1 ) {
					// --- bin-Verzeichnis gefunden ---					
					CutAt = JCP_Folders[j].lastIndexOf( LocalPathAgentGUI );
					LocalBaseDir =  JCP_Folders[j].substring(0, CutAt);
					break;
				}
			} // -- End 'for' --
			// --------------------------------------------------------------
			// --- Bei Ausführung IDE, eingie Variablen umstellen -----------			
			LocalFileRunnableJar = null;
		}
		// ------------------------------------------------------------------
	}
	// ----------------------------------------------------------------------
	
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	public String AppTitel() {
		/**
		 * Titel der Anwendung zurückgeben 
		 */
		return LocalAppTitel;
	};
	// -------------------------------
	// --- Look and Feel -------------
	// -------------------------------
	public String AppLnF() {
		/**
		 * Aktuelle Look and Feel - Einstellung 
		 */
		return LocalAppLnF;
	};
	public void setAppLnf( String NewLnF ) {
		/**
		 * Look and Feel - Einstellung  ändern
		 */
		LocalAppLnF = NewLnF;
	};
	// -------------------------------
	public String AppExecutedOver() {
		/**
		 * 'IDE' oder 'Executable' (jar-File) 
		 */
		return LocalAppExecutedOver;
	}
	public String AppPathSeparatorString() {
		/**
		 * Verzeichnistrenner: Win='\' Linux='/'
		 */
		return LocalAppPathSeparatorString;
	}
	public String AppNewLineString(){
		/**
		 * String für neue Zeilen in einer Text-Datei
		 */
		return LocalAppNewLineString;
	}

	// --- Allgemeine Verzeichnisangaben ---------------------
	public String PathBaseDir( ) {
		/**
		 * Aktuelles Basisverzeichnis der Anwendung
		 */
		return LocalBaseDir;
	}	
	public String PathJade( Boolean Absolute ){
		/**
		 * Unterverzeichnis für die Jade-Libraries
		 */
		if ( Absolute== true ) { 
			return FilePath2Absolute( LocalPathJade );
		}
		else {
			return LocalPathJade;	
		}		
	}
	public String PathBatik( Boolean Absolute ){
		/**
		 * Unterverzeichnis für die Batik-Libraries
		 */
		if ( Absolute== true ) { 
			return FilePath2Absolute( LocalPathBatik );
		}
		else {
			return LocalPathBatik;	
		}	
	}
	public String PathProperty( Boolean Absolute ){
		/**
		 * Unterverzeichnis für Eigenschaftsdateien
		 */
		if ( Absolute== true ) { 
			return FilePath2Absolute( LocalPathProperty );
		}
		else {
			return LocalPathProperty;	
		}	
	}
	public String PathImageIntern( ){
		/**
		 * Bild-Unterverzeichnis für das Projekt
		 */
		return LocalPathImageIntern;
	}
	
	// --- Allgemeine Dateiangaben ---------------------
	public String AppFileRunnableJar( Boolean Absolute ){
		/**
		 * Standardname *.jar-Bezeichner dieses Projekts (Default: 'AgentGui.jar') 
		 */
		if ( Absolute == true && LocalFileRunnableJar != null ) { 
			return FilePath2Absolute( LocalFileRunnableJar );
		}
		else {
			return LocalFileRunnableJar;	
		}	
	}
	public String FileDictionary( Boolean Absolute ){
		/**
		 * Verweis auf die Dictionary-Datei zur Sprachübersetzung
		 */
		if ( Absolute== true ) { 
			return FilePath2Absolute( LocalFileDictionary );
		}
		else {
			return LocalFileDictionary;	
		}
	}

	// ---------------------------------------------------------
	// ---------------------------------------------------------
	private String FilePath2Absolute( String FilePathRelative ){
		/**
		 * Macht aus einer relativen Verzeichnisangabe eine 
		 * Absolute Verzeichnisangabe
		 */
		String PathAbsolute = LocalBaseDir + FilePathRelative;
		return PathAbsolute;		
	}
	// ---------------------------------------------------------
	// ---------------------------------------------------------

	
	
	// ---------------------------------------------------------
	// --- Laufzeitinformationen zu Jade -----------------------
	// ---------------------------------------------------------
	public String getJadeVersion(){
		return LocalJadeVersion;
	}
	

	
	
}
