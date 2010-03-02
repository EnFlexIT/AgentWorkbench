package application.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import application.Application;

public class ReflectClassFiles extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> ClazzList = null;
	private String SearchINReference = null;
	private String[] SearchINPathParts = null;
		
		
	public ReflectClassFiles( String SearchReference ) {
		// --- Verzeichnis, in dem die Ontologie liegt auslesen ---
		SearchINReference = SearchReference;
		if ( !(SearchINReference == null) ) {
			SearchINPathParts = SearchINReference.split("\\.");
		}
		this.getClazzes();
		this.addAll( ClazzList );
	}

	private void getClazzes() {
		/**
		 * Initial detection of the available classe by using the 'SearchReference'
		 */
		String SearchPath = null;
		
		if ( Application.RunInfo.AppExecutedOver() == "IDE" ) {
			// ------------------------------------------------------------------------
			// --- Read Classes from the IDE environment ------------------------------
			SearchPath = Application.RunInfo.PathBaseDirIDE_BIN();
			ClazzList = getIDEClasses( SearchPath, SearchPath );
		} else {
			// ------------------------------------------------------------------------
			// --- Read Classes from the inside of a jar  -----------------------------
			ClazzList = getJARClasses( Application.RunInfo.AppFileRunnableJar(true) );
		}		
	}

	private ArrayList<String> getJARClasses(String jarName) {
		/**
		 * Reading the Classes from the inside of a jar-file
		 */
		ArrayList<String> classes = new ArrayList<String>();
		
		String CurrClass   = "";
		String packageName = "";
		
		
		packageName = packageName.replaceAll("\\.", "/");
		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName) );
			JarEntry jarEntry;
			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
					CurrClass = jarEntry.getName().replaceAll("/", "\\.");
					CurrClass = CurrClass.substring(0, CurrClass.length() - (".class").length());
					// --- Klasse in die Auflistung aufnehmen ? ---
					if ( SearchINReference == null ) {
						classes.add( CurrClass );	
					} else {
						if (CurrClass.startsWith( SearchINReference ) ) {
							classes.add( CurrClass );
						}		
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	
	private ArrayList<String> getIDEClasses(String BasePath, String SearchPath) {
		/**
		 * Reading the Classes from the IDE area		
		 */
		ArrayList<String> FileList = new ArrayList<String>();
		
		int CutBegin = BasePath.toString().length();
		int CutEnd   = 0;
		String CurrClass = "";
		
		File dir = new File(SearchPath);
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
				if ( files[i].isDirectory() ) {
					// ----------------------------------------------------------------
					// --- System.out.print(" (Unteriordner)\n");
					if ( SearchINReference == null ) {
						// ------------------------------------------------------------
						// --- Falls nach nichts konkretem gesucht wird, dann --------- 
						// --- alles in die Ergebnisliste aufnehmen 		  ---------
						FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
					} else {
						// ------------------------------------------------------------
						// --- Nur das durchsuchen, was auch wirklich interessiert ----
						boolean MoveDeeper = false;
						String SearchINPath = null;
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						for (int j=0; j<SearchINPathParts.length; j++) {
							if ( SearchINPath == null ) {
								SearchINPath = SearchINPathParts[j];	
							} else {
								SearchINPath = SearchINPath +  Application.RunInfo.AppPathSeparatorString() + SearchINPathParts[j];
							}								
							// --- Aktuellen Pfad untersuchen / vergleichen -----------
							if ( files[i].getAbsolutePath().endsWith( SearchINPath) ) {
								MoveDeeper = true;
								break;
							}							
						}
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						if ( MoveDeeper == true ) {
							// --- eine Verzeichnisebene tiefer suchen ----------------
							FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
						}
						// ------------------------------------------------------------
						// ------------------------------------------------------------
						}					 
				} else {
					// ----------------------------------------------------------------
					// --- System.out.println("Datei: " + CurrClass );
					CurrClass = files[i].getAbsolutePath().toString();
					if ( CurrClass.endsWith(".class") ) {
						// --- String der Klassendatei anpassen -----------------------
						CutEnd    = CurrClass.length() - (".class").length();						
						CurrClass = CurrClass.substring(CutBegin, CutEnd);
						CurrClass = CurrClass.replace('/', '.').replace('\\', '.');						
						// --- Klasse in die Auflistung aufnehmen ? -------------------
						if ( SearchINReference == null ) {
							FileList.add( CurrClass );	
						} else {
							if (CurrClass.startsWith( SearchINReference ) ) {
								FileList.add( CurrClass );
							}		
						}
						// ------------------------------------------------------------
					}
				}
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
			}		
		}
		return FileList;
	}
	
	
	
	
}
