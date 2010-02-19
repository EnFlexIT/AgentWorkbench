package application.reflection;

import gui.CoreWindow;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Testklasse zum Auffinden aller Class-Files eines Packages.
 * 
 * @author Wolfgang Heintz
 */
public class Reflection {
	/**
	 * Ermittelt anhand des ClassPath ob die Anwendung in einer IDE ausgeführt
	 * wird.
	 * 
	 * @param aClassPath
	 *            Über Systemproperties ermittelter ClassPath
	 * @return true wenn die Anwendung in einer IDE gestartet wurde
	 */
	protected static boolean isAppRunningInIde(String aClassPath) {
		boolean result = false;
		StringTokenizer tokenizer = new StringTokenizer(aClassPath, File.pathSeparator);
		while (tokenizer.hasMoreTokens()) {
			String classPathEntry = (String) tokenizer.nextToken();
			if (!classPathEntry.endsWith(".jar")) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Ermittelt anhand des ClassPath ob die Anwendung in einer JAR-Datei
	 * ausgeführt wird. In diesem Fall enthält der ClassPath nur einen Eintrag
	 * (= JAR-Datei der ausgeführten Klasse).
	 * 
	 * @param aClassPath
	 *            Über Systemproperties ermittelter ClassPath
	 * @return true wenn die Anwendung in einer JAR-Datei gestartet wurde
	 */
	protected static boolean isAppRunningInJar(String aClassPath) {

		boolean result = false;
		StringTokenizer tokenizer = new StringTokenizer(aClassPath, File.pathSeparator);
		if (tokenizer.countTokens() == 1 && aClassPath.endsWith(".jar")) {
			result = true;
		}
		return result;
	}

	/**
	 * Ermittelt alle Klassen des angegebenen Packages aus den Binary-
	 * Verzeichnissen des übergebenen ClassPath. Auch evtl. enthaltene
	 * JAR-Dateien werden ausgewertet.
	 * 
	 * @param aClassPath
	 *            Über Systemproperties ermittelter ClassPath
	 * @param aPackage
	 *            Package dessen Klassen gesucht werden
	 * @return Liste alle Klassen des angegebenen Packages innerhalb des
	 *         ClassPath
	 * @throws IOException
	 *             wenn eine JAR-Datei nicht geöffnet werden kann
	 */
	protected static List getAllClassesAppRunningIde(String aClassPath, String aPackage) throws IOException {
		
		List ideClasses = new LinkedList();
		String packagename = aPackage.replace('/', '.').replace('\\', '.');
		StringTokenizer tokenizer = new StringTokenizer(aClassPath, File.pathSeparator);

		while (tokenizer.hasMoreTokens()) {
			String nextClassPathDir = tokenizer.nextToken();

			if (nextClassPathDir.endsWith(".jar")) {
				ideClasses.addAll(getAllClassesInJarFile(new JarFile(nextClassPathDir), packagename));
			} else {
				String directoryName = nextClassPathDir + File.separator
						+ packagename.replace('.', File.separatorChar);

				File directory = new File(directoryName);
				String[] classnames = directory.list();

				if (classnames != null) {
					for (int i = 0; i < classnames.length; i++) {
						if (!classnames[i].endsWith(".class")) {
							continue;
						}

						String classname = packagename
								+ "."
								+ classnames[i].substring(0, classnames[i].indexOf('.'));

						ideClasses.add(classname);
					}
				}
			}
		}

		return ideClasses;
	}

	/**
	 * Ermittelt alle Klassen des angegebenen Packages aus der JAR-Datei in der
	 * die aktuelle Klasse steht und allen JAR-Dateien die im ClassPath der
	 * Manifest-Datei stehen.
	 * 
	 * @param aClassPath
	 *            Über Systemproperties ermittelter ClassPath
	 * @param aPackage
	 *            Package dessen Klassen gesucht werden
	 * @return Liste alle Klassen des angegebenen Packages innerhalb des
	 *         ClassPath
	 * @throws IOException
	 *             wenn eine JAR-Datei nicht geöffnet werden kann
	 */
	protected static List getAllClassesAppRunningJar(String aClassPath, String aPackage) throws IOException {
		
		List jarClasses = new LinkedList();
		String packagename = aPackage.replace('/', '.').replace('\\', '.');
		JarFile jarFile = new JarFile(aClassPath);
		Manifest manifest = jarFile.getManifest();

		jarClasses.addAll(getAllClassesInJarFile(jarFile, packagename));

		if (manifest != null) {
			String jarClassPath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
			StringTokenizer tokenizer = new StringTokenizer(jarClassPath, " ");

			while (tokenizer.hasMoreTokens()) {
				String jarFileName = (String) tokenizer.nextToken();
				jarClasses.addAll(getAllClassesInJarFile(new JarFile(jarFileName), packagename));
			}
		}

		return jarClasses;
	}

	/**
	 * Ermittelt alle Klassen einer JAR-Datei, die dem übergebenen Package
	 * angehören.
	 * 
	 * @param aJarFile
	 *            auszuwertende JAR-Datei.
	 * @param aPackage
	 *            Package der gesuchten Klassen.
	 * @return Liste aller Klassen des übergebenen Packages
	 */
	private static List getAllClassesInJarFile(JarFile aJarFile, String aPackage) {
		LinkedList jarClasses = new LinkedList();
		Enumeration jarEntries = aJarFile.entries();

		while (jarEntries.hasMoreElements()) {
			String classname = ((JarEntry) jarEntries.nextElement()).getName();
			int idxClass = classname.indexOf(".class");

			if (idxClass == -1) {
				continue;
			}

			classname = classname.substring(0, idxClass).replace('/', '.')
					.replace('\\', '.');

			if (!extractPackageName(classname).equalsIgnoreCase(aPackage)) {
				continue;
			}

			jarClasses.add(classname);
		}

		return jarClasses;
	}

	/**
	 * Extrahiert aus einem vollständigen Klassennamen mit Packageangabe den
	 * reinen Packagenamen. Als Trennzeichen zwischen den einzelnen Elementen
	 * des Klassennamens sind '/', '\\' und '.' zulässig.
	 * 
	 * @param aClassname
	 *            vollständiger Klassenname mit Packageangabe
	 * @return Packagenamen getrennt durch '.'
	 */
	private static String extractPackageName(String aClassname) {
		String packagename = "";
		String classname = aClassname.replace('/', '.').replace('\\', '.');
		int posClassname = classname.lastIndexOf('.');

		if (posClassname != -1) {
			packagename = classname.substring(0, posClassname);
		}

		return packagename;
	}

	/**
	 * Main-Methode der Klasse Test.
	 * 
	 * @param args
	 *            Startparameter
	 */
	public static void main(String[] args) {
		
		String packagename = CoreWindow.class.getPackage().getName();
		String classPath = System.getProperty("java.class.path");
		List classes = null;

		try {
			if (isAppRunningInIde(classPath)) {
				classes = getAllClassesAppRunningIde(classPath, packagename);
			} else {
				classes = getAllClassesAppRunningJar(classPath, packagename);
			}

			if (classes != null) {
				Iterator itrClasses = classes.iterator();
				while (itrClasses.hasNext()) {
					System.out.println(itrClasses.next().toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
