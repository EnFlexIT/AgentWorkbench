package application.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import application.Application;

public class Reflect {

	private ArrayList<String> ClazzList = null;
	
	public Reflect(String Folder) {
		// --- Verzeichnis, in dem die Ontologie liegt auslesen ---
		getClazzes();		
	}
	
	private ArrayList<String> getClazzes() {
		/**
		 * 
		 */
		if ( Application.RunInfo.AppExecutedOver() == "IDE" ) {
			// --- Read Classes from the IDE environment ----
			String SearchPath = Application.RunInfo.PathBaseDirIDE_BIN();
			ClazzList = getIDEClasses( SearchPath, SearchPath );			
		} else {
			// --- Read Classes from the inside of a jar  ---
			ClazzList = getJARClasses( Application.RunInfo.AppFileRunnableJar(true) );
		}		
		// --- nix gefunden, dann raus hier ---
		if (ClazzList == null || ClazzList.size() == 0 ) {
			return  null;
		};
		
		
		// --- Iterator für ClazzList ---
		String Klassenname;
		Iterator<String> ClLi = ClazzList.listIterator();
	    while ( ClLi.hasNext() ) {
	    	Klassenname = ClLi.next();
	        System.out.println( "=> " + Klassenname );
	      }

		
		
		// --- Testausgaben ---------------------------------
		int i = 8;
		System.out.println("KLasse No. "+i+": " + ClazzList.get(i-1).toString() );
		try {
            Class c = Class.forName(ClazzList.get(i-1).toString());
            Method m[] = c.getDeclaredMethods();
            for (int j = 0; j < m.length; j++)
            System.out.println(m[j].toString());
        }
        catch (Throwable e) {
        	System.err.println(e);
        }	
		
		//Application.class.getPackage()
		return ClazzList;
		 
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
					classes.add( CurrClass );
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
				if (files[i].isDirectory()) {
					//System.out.print(" (Ordner)\n");
					FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) ); 
					}
				else {
					CurrClass = files[i].getAbsolutePath().toString();
					if ( CurrClass.endsWith(".class") ) {
						//System.out.println("Datei: " + CurrClass );
						CutEnd    = CurrClass.length() - (".class").length();						
						CurrClass = CurrClass.substring(CutBegin, CutEnd);
						CurrClass = CurrClass.replace('/', '.').replace('\\', '.');
						FileList.add( CurrClass );
					}
				}
			}		
		}
		return FileList;
	}
	
	
	
	
}
