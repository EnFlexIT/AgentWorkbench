package agentgui.core.common;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderUtil {

	// Log object

	// Parameters
	private static final Class<? extends Object>[] parameters = new Class<?>[] { URL.class };

	public static Vector<String> getPackageNames(Vector<String> ressources,	String relativProjectPath, String completePath) throws Exception {

		Vector<String> result = new Vector<String>();		
		for (String ressource : ressources) {
			
			ressource = ClassLoaderUtil.adjustPathForLoadin(ressource,relativProjectPath, completePath);
			File file = new File(ressource);
			
			if (!file.isDirectory()) {

				URL jar = file.toURI().toURL();
				jar = new URL("jar:" + jar.toExternalForm() + "!/");
				JarURLConnection conn = (JarURLConnection) jar.openConnection();
				conn.setUseCaches(false);
				JarFile jarFile = conn.getJarFile();
				Enumeration<JarEntry> e = jarFile.entries();

				while (e.hasMoreElements()) {

					JarEntry entry = e.nextElement();
					String entryName = entry.getName();

					if (!entryName.contains("META-INF")) {
						//if (entry.isDirectory()) {
						// --- package found ---    						
						String packpageName = ClassLoaderUtil.getPackageName(entryName);
						if (packpageName != null && packpageName.length() != 0&& result.contains(packpageName) == false) {
							//System.out.println( "=> " + packpageName );
							result.add(packpageName);
							//    						}
						}//directory 
					}//meta-inf
				}//while
				jarFile.close();

			}
		}
		return result;
	}

	private static String getPackageName(String jarEntry) {

		String packpageName = new String(jarEntry);
		String[] testDepthArray = packpageName.split("/");
		if (testDepthArray.length > 3) {
			return null;
		} else {
			if (packpageName.endsWith(".class")==false) {
				System.out.println("" + packpageName);
				int index = packpageName.lastIndexOf("/");
				if (index==-1) {
					return null;
				}
				packpageName = packpageName.substring(0, index);
				packpageName = packpageName.replaceAll("/", ".");
				return packpageName;	
			} else {
				return null;
			}
		}
	}

	/**
	 * Add file to CLASSPATH
	 * @param s File name
	 * @throws IOException  IOException
	 */
	public static void addFile(String s) throws IOException {
		File f = new File(s);
		addFile(f);
	}

	public static void printClassPath() {
		System.out.println(System.getProperty("java.class.path"));
	}

	public static void addJarToClassPath(String name) {
		String path = System.getProperty("java.class.path")	+ System.getProperty("path.separator") + name;
		path = path.replace(";;", ";");
		System.setProperty("java.class.path", path);
	}

	public static void removeJarFromClassPath(String jarFile) {
		String path = System.getProperty("java.class.path");
		if ((path.indexOf(jarFile) + jarFile.length()) < path.length()) {

			path = path.replace(jarFile + System.getProperty("path.separator"),	System.getProperty("path.separator"));

		} else {

			path = path.replace(jarFile, "");
			path = path.trim();
		}
		path = path.replace(";;", ";");
		System.setProperty("java.class.path", path);
	}

	public static void removeFile(String[] files) throws RuntimeException,	NoSuchFieldException, IllegalAccessException {
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Field[] fields = null;
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		fields = sysclass.getDeclaredFields();
		java.lang.reflect.Field ucp = sysclass.getDeclaredField("ucp");
		ucp.setAccessible(true);
		Object sun_misc_URLClassPath = ucp.get(sysLoader);
		Class c = sun_misc_URLClassPath.getClass();
		fields = c.getDeclaredFields();
		Field path = c.getDeclaredField("path");
		path.setAccessible(true);
		Field urlsField = c.getDeclaredField("urls");
		Object tmpObject = new Object();
		urlsField.setAccessible(true);
		tmpObject = path.get(sun_misc_URLClassPath);
		Object stack = urlsField.get(sun_misc_URLClassPath);
		Stack myStack = (Stack) stack;
		ArrayList list = (ArrayList) tmpObject;
		ArrayList<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < list.size(); i++) {

			URL url = (URL) list.get(i);
			for (String jarFile : files) {

				jarFile = jarFile.replace("\\", "/");
				jarFile = "/" + jarFile;
				if (url.getPath().equals(jarFile)) {

					urls.add(url);

				}
			}
		}

		for (URL url : urls) {
			list.remove(url);
			myStack.remove(url);
		}

		path.set(sun_misc_URLClassPath, list);
		ucp.set(ClassLoader.getSystemClassLoader(), sun_misc_URLClassPath);

	}

	public static void removeFile(String jarFile) throws RuntimeException,	NoSuchFieldException, IllegalAccessException {
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Field[] fields = null;
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		fields = sysclass.getDeclaredFields();
		java.lang.reflect.Field ucp = sysclass.getDeclaredField("ucp");
		ucp.setAccessible(true);
		Object sun_misc_URLClassPath = ucp.get(sysLoader);
		Class c = sun_misc_URLClassPath.getClass();
		fields = c.getDeclaredFields();
		Field path = c.getDeclaredField("path");
		path.setAccessible(true);
		Field urlsField = c.getDeclaredField("urls");
		Object tmpObject = new Object();
		urlsField.setAccessible(true);
		tmpObject = path.get(sun_misc_URLClassPath);
		Object stack = urlsField.get(sun_misc_URLClassPath);
		Stack myStack = (Stack) stack;
		ArrayList list = (ArrayList) tmpObject;
		ArrayList<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < list.size(); i++) {

			URL url = (URL) list.get(i);
			String jarFileTest = jarFile.replace("\\", "/");
			jarFileTest = "/" + jarFileTest;
			if (url.getPath().equals(jarFileTest)) {

				urls.add(url);

			}

		}

		for (URL url : urls) {
			list.remove(url);
			myStack.remove(url);
		}

		path.set(sun_misc_URLClassPath, list);
		ucp.set(ClassLoader.getSystemClassLoader(), sun_misc_URLClassPath);

	}

	/**
	 * Add file to CLASSPATH
	 * @param f  File object
	 * @throws IOException IOException
	 */
	public static void addFile(File f) throws IOException {
		addURL(f.toURI().toURL());
	}

	/**
	 * Add URL to CLASSPATH
	 * @param u URL
	 * @throws IOException IOException
	 */
	public static void addURL(URL u) throws IOException {

		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}
	}
	

	public static String adjustPathForLoadin(String selectedJar,String projectFolder, String fullProjectFolderPath) {
		if (selectedJar.contains(projectFolder)) {
			int index = selectedJar.indexOf("\\");
			int index2 = selectedJar.indexOf("\\", index + 1);
			selectedJar = fullProjectFolderPath	+ selectedJar.substring(index2 + 1);
		}
		return selectedJar;

	}

	public static Vector<AgentController> loadAgentsIntoContainer(Vector<String> allClasses, AgentContainer container) throws ClassNotFoundException, StaleProxyException {
		Vector<AgentController> controller = new Vector<AgentController>();

		for (String names : allClasses) {

			if (names.contains(".agents")) {
				
				if (!names.contains("$")) {

					Class<? extends Object> c = Class.forName(names); //urlClassLoaderUtil.getClassByName(names);

					if (c.getGenericSuperclass().toString().contains("Agent")) {

						controller.add(container.createNewAgent(names, names, null));

					}

				}

			}
		}
		return controller;

	}

	public static Vector<String> getClassNamesFromJar(URL jar) throws IOException {

		Vector<String> result = new Vector<String>();
		jar = new URL("jar:" + jar.toExternalForm() + "!/");
		JarURLConnection conn = (JarURLConnection) jar.openConnection();
		JarFile jarFile = conn.getJarFile();
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {

			JarEntry entry = e.nextElement();

			if (!entry.isDirectory()) {
				String entryName = entry.getName();
				if (entryName.contains(".class")) {
					entryName = entryName.replace("/", ".");
					entryName = entryName.replace(".class", " ");
					entryName = entryName.trim();

					result.add(entryName);
				}
			}

		}
		jarFile.close();
		return result;
	}

}
