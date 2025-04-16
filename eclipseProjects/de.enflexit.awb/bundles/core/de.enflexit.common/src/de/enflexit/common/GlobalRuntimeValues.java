package de.enflexit.common;

import java.io.File;
import java.time.ZoneId;

/**
 * The Class GlobalRuntimeValues.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class GlobalRuntimeValues {
	
	
	// --------------------------------------------------------------
	// --- Methods for the ZoneIdResolver ---------------------------
	// --------------------------------------------------------------
	private static ZoneIdResolver zoneIdResolver = null;
	
	/**
	 * Sets the zone id resolver. If set to null, the locally available {@link DefaultZonIdResolver}
	 * will be used that always returns {@link ZoneId#systemDefault()}
	 * 
	 * @param newZoneIdResolver the new zone id resolver
	 */
	public static void setZoneIdResolver(ZoneIdResolver newZoneIdResolver) {
		zoneIdResolver = newZoneIdResolver;
	}
	/**
	 * Returns the current zone id resolver.
	 * @return the zone id resolver
	 */
	public static ZoneIdResolver getZoneIdResolver() {
		if (zoneIdResolver==null) {
			zoneIdResolver = new DefaultZonIdResolver();
		}
		return zoneIdResolver;
	}
	/**
	 * Returns the ZoneId of the currently used {@link ZoneIdResolver}.
	 * @return the zone id
	 */
	public static ZoneId getZoneId() {
		return getZoneIdResolver().getZoneId();
	}

	/**
	 * The Class DefaultZonIdResolver that always returns {@link ZoneId#systemDefault()}.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private static class DefaultZonIdResolver implements ZoneIdResolver {
		/* (non-Javadoc)
		 * @see de.enflexit.common.ZoneIdResolver#getZneId()
		 */
		@Override
		public ZoneId getZoneId() {
			return ZoneId.systemDefault();
		}
	}
	
	
	// --------------------------------------------------------------
	// --- Methods for the reminder slot last selected folder -------
	// --------------------------------------------------------------
	private static final String PREF_LAST_SELECTED_DIRECTORY = "LAST_SELECTED_DIRECTORY";
	private static File lastSelectedDirectory;
	
	/**
	 * Sets the last selected directory.
	 * @param newLastSelectedDirectory the new last selected directory
	 */
	public static void setLastSelectedDirectory(File newLastSelectedDirectory) {
		
		if (newLastSelectedDirectory==null) return;
		
		// --- If we've got a file, get parent directory ------------
		if (newLastSelectedDirectory.isDirectory()==false) {
			newLastSelectedDirectory = newLastSelectedDirectory.getParentFile();
		}
		if (newLastSelectedDirectory==null) return;
		
		// --- Remind locally and persisted -------------------------
		lastSelectedDirectory = newLastSelectedDirectory;
		BundleHelper.getEclipsePreferences().put(PREF_LAST_SELECTED_DIRECTORY, lastSelectedDirectory.getAbsolutePath());
	}
	/**
	 * Returns the reminder value of the last selected directory as File object 
	 * @return the lastSelectedDirectory
	 */
	public static File getLastSelectedDirectory() {
		if (lastSelectedDirectory==null) {
			// --- Try to getting persisted value -------------------
			String lastDirPath = BundleHelper.getEclipsePreferences().get(PREF_LAST_SELECTED_DIRECTORY, null);
			if (lastDirPath!=null && lastDirPath.isBlank()==false) {
				File checkLastSelectedDirectory = new File(lastDirPath); 
				if (checkLastSelectedDirectory.exists()==true) {
					lastSelectedDirectory = checkLastSelectedDirectory;		
				}
			}
		}
		return lastSelectedDirectory;	
	}
	
}
