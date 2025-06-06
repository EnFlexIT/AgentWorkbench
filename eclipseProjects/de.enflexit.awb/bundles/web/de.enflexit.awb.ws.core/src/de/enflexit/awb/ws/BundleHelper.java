package de.enflexit.awb.ws;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import javax.swing.ImageIcon;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.ws.server.AwbServer;

/**
 * The Class BunldeHelper provides static help methods.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BundleHelper {

private static final String imagePackage = "/icons/";
	
	/**
	 * Gets the image package location as String.
	 * @return the image package
	 */
	public static String getImagePackage() {
		return imagePackage;
	}
	/**
	 * Gets the image icon for the specified image.
	 *
	 * @param fileName the file name
	 * @return the image icon
	 */
	public static ImageIcon getImageIcon(String fileName) {
		String imagePackage = getImagePackage();
		ImageIcon imageIcon=null;
		try {
			imageIcon = new ImageIcon(BundleHelper.class.getResource((imagePackage + fileName)));
		} catch (Exception err) {
			System.err.println("Error while searching for image file '" + fileName + "' in " + imagePackage);
			err.printStackTrace();
		}	
		return imageIcon;
	}

	
	/**
	 * Returns the path to the AWB property directory with an ending file separator.
	 * @return the path reference to the property folder with an ending file separator
	 */
	public static String getPathProperties() {
		String propPathAbs = Application.getGlobalInfo().getPathProperty(true).toString();
		propPathAbs += propPathAbs.endsWith(File.separator)==false ? File.separator : ""; 
		return propPathAbs;
	}
	
	/**
	 * Returns the 'awbHome' web root directory.
	 * @return the web root directory
	 */
	public static File getWebRootDirectory() {
		return getWebRootDirectory(false);
	}
	/**
	 * Returns the 'awbHome' web root directory.
	 *
	 * @param createIfNotExist the indicator to create the directory if it does not exist
	 * @return the web root directory
	 */
	public static File getWebRootDirectory(boolean createIfNotExist) {
		
		File webRootDir = null;
		
		switch (Application.getGlobalInfo().getExecutionEnvironment()) {
		case ExecutedOverIDE:
			// ------------------------------------------------------
			// --- For the IDE environment --------------------------
			// ------------------------------------------------------
			Bundle bundle = FrameworkUtil.getBundle(BundleHelper.class);
			String bLocation = bundle.getLocation();
			
			// --- Should never happen ------------------------------
			if (bLocation.equals("System Bundle")==true || bLocation.startsWith("System Bundle")) return null;
			if (bLocation.startsWith("initial@reference:file:")) return null;
			
			try {
				// --- Get the file object of the bundle ------------
				File bLocationFile = null;
				if (bLocation.startsWith("reference:file:")) {
					bLocation = bLocation.substring(("reference:file:".length()), bLocation.length());
					bLocationFile = new File(bLocation);
				
				} else {
					URL bundleLocationURL = new URI(bundle.getLocation()).toURL();
					bLocationFile = new File(bundleLocationURL.toURI());	
				}
				
				// --- Add the AWB_SERVER_ROOT_PATH -----------------
				String webRootPath = bLocationFile.getAbsolutePath();
				webRootPath += webRootPath.endsWith(File.separator) ? AwbServer.AWB_SERVER_ROOT_PATH : File.separator + AwbServer.AWB_SERVER_ROOT_PATH; 
				webRootDir = new File(webRootPath);
				
			} catch (MalformedURLException | URISyntaxException ex) {
				ex.printStackTrace();
			}
			break;
			
		case ExecutedOverProduct:
			// ------------------------------------------------------
			// --- For the product runtime environment --------------
			// ------------------------------------------------------
			String webRootPath = Application.getGlobalInfo().getPathBaseDir().toString();
			webRootPath += webRootPath.endsWith(File.separator) ? AwbServer.AWB_SERVER_ROOT_PATH : File.separator + AwbServer.AWB_SERVER_ROOT_PATH;
			webRootDir = new File(webRootPath);
			break;
		}
		
		// --- Create the directory ? -------------------------------
		if (createIfNotExist==true && webRootDir.exists()==false) {
			webRootDir.mkdir();
		}
		return webRootDir;
		
	}
	
	/**
	 * Returns the web download directory.
	 * @return the web download directory
	 */
	public static File getWebDownloadDirectory() {
		return getWebDownloadDirectory(false);
	}
	/**
	 * Returns the web download directory.
	 *
	 * @param createIfNotExists the create if not exist
	 * @return the web download directory
	 */
	public static File getWebDownloadDirectory(boolean createIfNotExists) {
		Path webBaseDirectory = getWebRootDirectory().getParentFile().toPath();
		webBaseDirectory = webBaseDirectory.resolve(AwbServer.AWB_SERVER_DOWNLOAD_PATH);
		File webBaseDirectoryFile = webBaseDirectory.toFile();
		if (createIfNotExists==true && webBaseDirectoryFile.exists()==false) {
			webBaseDirectoryFile.mkdirs();
		}
		return webBaseDirectoryFile;
	}
	
	/**
	 * Returns the web application archive directory.
	 * @return the web application archive directory
	 */
	public static File getWebApplicationArchiveDirectory() {
		return getWebApplicationArchiveDirectory(false);
	}
	/**
	 * Returns the web application archive directory.
	 *
	 * @param createIfNotExists the create if not exist
	 * @return the web application archive directory
	 */
	public static File getWebApplicationArchiveDirectory(boolean createIfNotExists) {
		Path webBaseDirectory = getWebRootDirectory().getParentFile().toPath();
		webBaseDirectory = webBaseDirectory.resolve(AwbServer.AWB_SERVER_WEB_APP_ARCHIVE_PATH);
		File webBaseDirectoryFile = webBaseDirectory.toFile();
		if (createIfNotExists==true && webBaseDirectoryFile.exists()==false) {
			webBaseDirectoryFile.mkdirs();
		}
		return webBaseDirectoryFile;
	}

	// ----------------------------------------------------
	// --- From here, debug print methods -----------------
	// ----------------------------------------------------
	/**
	 * System println.
	 *
	 * @param messenger the messenger object
	 * @param message the message
	 * @param isError the indicator, if the message describes an error
	 */
	public static void systemPrintln(Object messenger, String message, boolean isError) {
		message = "[" + messenger.getClass().getSimpleName() + "] " + message;
		if (isError==true) {
			System.err.println(message);
		} else {
			System.out.println(message);
		}
	}
	
}
