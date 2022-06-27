package de.enflexit.common;

import java.awt.GraphicsEnvironment;

/**
 * The Class SystemEnvironmentHelper provides some (hopeful) useful methods to information .
 * about the execution environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemEnvironmentHelper {

	
	/**
	 * Returns the operating system by calling <code>System.getProperty("os.name")</code>.
	 * @return the operating system
	 */
	public static String getOperatingSystem() {
		return System.getProperty("os.name");
	}
	/**
	 * Checks if the current operating system is windows.
	 * @return true, if is windows operating system
	 */
	public static boolean isWindowsOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("windows");
	}
	/**
	 * Checks if the current operating system is Linux.
	 * @return true, if is Linux operating system
	 */
	public static boolean isLinuxOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("linux");
	}
	/**
	 * Checks if the current operating system is Mac OS X.
	 * @return true, if is Mac OS X operating system
	 */
	public static boolean isMacOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("mac");
	}

	/**
	 * Return the version of the operating systems by calling <code>System.getProperty("os.version")</code>.
	 * @return the operating systems version
	 */
	public static String getOperatingSystemsVersion() {
		return System.getProperty("os.version");
	}
	/**
	 * Return the architecture of the operating systems by calling <code>System.getProperty("os.arch")</code>.
	 * @return the operating systems version
	 */
	public static String getOperatingSystemsArchitecture() {
		return System.getProperty("os.arch");
	}

	
	/**
	 * Checks if the current system environment is executed headless (without any GUI).
	 * @return true, if is headless operation
	 */
	public static boolean isHeadlessOperation() {
		boolean headlessOperation = GraphicsEnvironment.isHeadless();
//		headlessOperation = GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
		return headlessOperation;
	}
	
}
