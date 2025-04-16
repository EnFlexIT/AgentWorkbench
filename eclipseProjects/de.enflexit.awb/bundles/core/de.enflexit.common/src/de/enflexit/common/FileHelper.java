package de.enflexit.common;

import java.io.File;
import java.io.IOException;

/**
 * The Class FileHelper provides static help methods for the handling of
 * {@link File}s.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FileHelper {

	/**
	 * Checks if the specified file to check is in the specified base directory.
	 *
	 * @param baseDirectory the base directory
	 * @param fileToCheck the file to check
	 * @return true, if is in directory
	 */
	public static boolean isInDirectory(File baseDirectory, File fileToCheck) {
		
		if (baseDirectory==null || baseDirectory.isDirectory()==false) {
			System.err.println("[" + FileHelper.class.getSimpleName() + "] The specified file for the base directory is not a direcotry!");
			return false;
		}
		
		if (fileToCheck==null || fileToCheck.exists()==false || fileToCheck.isDirectory()==true) {
			System.err.println("[" + FileHelper.class.getSimpleName() + "] No file was specified to check for its position in direcory '" + baseDirectory.getAbsolutePath() + "'!");
			return false;
		}
		
		
		try {
			baseDirectory = baseDirectory.getCanonicalFile();
			fileToCheck = fileToCheck.getCanonicalFile();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		File parentFile = fileToCheck;
		while (parentFile != null) {
			if (baseDirectory.equals(parentFile)) {
				return true;
			}
			parentFile = parentFile.getParentFile();
		}
		return false;
	}

	/**
	 * Returns the relative path of the specified file .
	 *
	 * @param baseDirectory the base directory
	 * @param fileWithinDirectory the file that has to be in the (sub-)directory
	 * @return the relative path
	 */
	public static String getRelativePath(File baseDirectory, File fileWithinDirectory) {

		if (isInDirectory(baseDirectory, fileWithinDirectory)==false) {
			System.err.println("[" + FileHelper.class.getSimpleName() + "] The specified file is not in the specified (sub-) directory. Thus, no relative path can be determined!");
			return null;
		}
		return baseDirectory.toURI().relativize(fileWithinDirectory.toURI()).getPath();
	}

}
