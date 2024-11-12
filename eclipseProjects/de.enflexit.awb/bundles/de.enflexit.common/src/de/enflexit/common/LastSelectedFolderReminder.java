package de.enflexit.common;

import java.io.File;

/**
 * The Interface for a LastSelectedFolderReminder.
 */
public interface LastSelectedFolderReminder {

	/**
	 * Return the last selected folder in the context of the application.
	 * @return the last selected folder
	 */
	File getLastSelectedFolder();
	
	/**
	 * Sets the last selected folder path and should remind it.
	 * @param lastSelectedFolder the new last selected folder
	 */
	void setLastSelectedFolder(File lastSelectedFolder);
	
}
