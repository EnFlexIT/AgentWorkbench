package de.enflexit.awb.ws.core.util;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.enflexit.awb.ws.BundleHelper;

/**
 * The Class WebApplicationUpdate provides static help methods to update the 
 * application hosted on the local web server.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class WebApplicationUpdate {

	/**
	 * Returns the current web application version file path.
	 * @return the current web application version file path
	 */
	public static Path getCurrentWebApplicationVersionFilePath() {
		return BundleHelper.getWebRootDirectory().toPath().resolve(WebApplicationVersion.XM_FILE_NAME);
	}
	/**
	 * Returns the current WebApplicationVersion.
	 * @return the current web application version
	 */
	public static WebApplicationVersion getCurrentWebApplicationVersion() {
		return WebApplicationVersion.load(getCurrentWebApplicationVersionFilePath().toString());
	}
	
	
	/**
	 * Checks if is valid update URL.
	 *
	 * @param updateURLString the update URL string
	 * @return true, if is valid update URL
	 */
	public static boolean isValidUpdateURL(String updateURLString) {
		
		// --- Check URL string ---------------------------
		if (updateURLString==null || updateURLString.isBlank()==true) return false;

		
		// --- Create URL of String -----------------------
		URL updateURL = null;
		try {
			updateURL = new URI(updateURLString).toURL();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		if (updateURL==null) return false;

		
		// --- Try to open version.php --------------------
		WebApplicationUpdateList versionList = null;
		try {
			versionList = WebApplicationUpdateList.loadWebApplicationVersions(updateURL, null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (versionList==null) return false;
		
		return true;
	}
	
	/**
	 * Checks if is valid update URL.
	 *
	 * @param downloadURLString the download URL string
	 * @return true, if is valid update URL
	 */
	public static boolean isValidDownloadURL(String downloadURLString) {
		
		// --- Check URL string ---------------------------
		if (downloadURLString==null || downloadURLString.isBlank()==true) return false;
		
		// --- Create URL of String -----------------------
		URL downloadURL = null;
		try {
			downloadURL = new URI(downloadURLString).toURL();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		if (downloadURL==null) return false;
		
		return true;
	}
	
	
	/**
	 * Returns the available web application versions.
	 *
	 * @param updateURLString the update URL string
	 * @return the web application versions
	 */
	public static List<WebApplicationVersion> getWebApplicationVersions(String updateURLString) {

		// --- Check validity of the update URL ---------------------
		if (WebApplicationUpdate.isValidUpdateURL(updateURLString)==false) return null;
		
		// --- Try getting file list from download server -----------
		WebApplicationUpdateList versionList = null;
		try {
			URL updateURL = new URI(updateURLString).toURL();
			versionList = WebApplicationUpdateList.loadWebApplicationVersions(updateURL, null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (versionList==null || versionList.getFileList().size()==0) return null;
		
		// --- Fill list of available versions ----------------------
		List<WebApplicationVersion> versionsAvailable = new ArrayList<>();
		versionList.getFileList().forEach(fileName -> versionsAvailable.add(new WebApplicationVersion(fileName, updateURLString)));
		return versionsAvailable;
	}
	
	/**
	 * If possible return the newest version update of the web application.
	 *
	 * @param updateURLString the update URL string
	 * @return the web application update
	 */
	public static WebApplicationVersion getWebApplicationUpdate(String updateURLString) {

		// --- Get current version ------------------------
		WebApplicationVersion versionCurrent = WebApplicationUpdate.getCurrentWebApplicationVersion();
		
		// --- Get latest version -------------------------
		List<WebApplicationVersion> versionList = WebApplicationUpdate.getWebApplicationVersions(updateURLString);
		if (versionList==null || versionList.size()==0) return null;
		Collections.sort(versionList);
		WebApplicationVersion versionLatest = versionList.get(versionList.size()-1);
		
		// --- No old version found, return the latest ----
		if (versionCurrent==null) return versionLatest;
		// --- Compare versions ---------------------------
		if (versionLatest.equals(versionCurrent)==true) return null;
		
		return versionLatest;
	}
	
}
