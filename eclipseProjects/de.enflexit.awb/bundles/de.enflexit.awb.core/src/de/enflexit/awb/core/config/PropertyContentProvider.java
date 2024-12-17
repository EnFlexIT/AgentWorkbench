package de.enflexit.awb.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.Application;
import de.enflexit.common.PathHandling;

/**
 * The Class PropertyContentProvider unpacks required files from 
 * the bundle into the local file system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PropertyContentProvider {
	
	/**
	 * The enumeration that describes the FileToProvide.
	 */
	public enum FileToProvide {
		DICTIONARY_BIN("dictionary.bin"),
		DICTIONARY_CSV("dictionary.csv"),
		OIDC_TRUST_STORE_JKS("oidcTrustStore.jks");
		
		private final String fileName;
		
		private FileToProvide(final String fileName) {
			this.fileName = fileName;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return fileName;
		}
	}
	
	private boolean debug = false;
	private String propertyDirectoryInBundle;
	private File propertyDirectory;
	
	/**
	 * Instantiates a new property content provider.
	 * @param propertyDirectory the property directory
	 */
	public PropertyContentProvider(File propertyDirectory) {
		this.propertyDirectory = propertyDirectory;
		this.getPropertyDirectoryInBundle();
	}
	/**
	 * Gets the property directory within bundle.
	 * @return the property directory in bundle
	 */
	private String getPropertyDirectoryInBundle() {
		if (propertyDirectoryInBundle==null) {
			propertyDirectoryInBundle = "/" + PathHandling.SUB_PATH_PROPERTIES;
			propertyDirectoryInBundle = propertyDirectoryInBundle.replace("\\", "/");
		}
		return propertyDirectoryInBundle;
	}
	/**
	 * Checks and provides the full property content.
	 */
	public void checkAndProvideFullPropertyContent() {
		for (FileToProvide fileToProvide : FileToProvide.values()) {
			this.checkAndProvidePropertyContent(fileToProvide);
		}
	}
	/**
	 * Checks and provides the specified property content.
	 * @param fileToProvide the {@link FileToProvide}
	 */
	public void checkAndProvidePropertyContent(FileToProvide fileToProvide) {
		this.checkAndProvidePropertyContent(fileToProvide, false);
	}
	/**
	 * Checks and provides the specified property content.
	 * @param fileToProvide the {@link FileToProvide}
	 * @param overwriteExistingFile the overwrite existing file
	 */
	public void checkAndProvidePropertyContent(FileToProvide fileToProvide, boolean overwriteExistingFile) {
		
		// --- Try to find the file in the properties ----- 
		final String fileNameToMatch = fileToProvide.toString();
		File[] fileFound = this.propertyDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equals(fileNameToMatch);
			}
		});
		
		// --- Some debug output --------------------------
		if (this.debug) {
			if (fileFound==null || fileFound.length==0) {
				System.out.println("Could not find file " + fileNameToMatch + " in " + this.propertyDirectory.getAbsolutePath() + " => File is to be extracted");	
			} else {
				for (int i = 0; i < fileFound.length; i++) {
					System.out.println("Found file " + fileFound[i].getAbsolutePath());	
				}
			}
		}
		
		// --- Could the file be found? -------------------
		if (overwriteExistingFile==true || fileFound==null || fileFound.length==0) {
			// --- File not found => extract from bundle --
			this.extractFromBundle(fileToProvide);
		}
	}

	/**
	 * Extract from bundle.
	 * @param fileToProvide the file to provide
	 */
	private void extractFromBundle(FileToProvide fileToProvide) {

		String fileName = fileToProvide.toString();
		Path pathProperties = Application.getGlobalInfo().getPathProperty(true);
		String newfilePath = pathProperties.resolve(fileName).toString();
		
		if (this.debug) {
			System.out.println("Extract '" + fileName + "' to " + newfilePath);
		}
		
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			URL fileURL = bundle.getResource(this.getPropertyDirectoryInBundle() + "/" + fileName);
			if (fileURL!=null) {
				// --- Write file to directory ------------
				is = fileURL.openStream();
				fos = new FileOutputStream(newfilePath);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				
			} else {
				// --- Could not find fileURL -------------
				System.err.println(this.getClass().getSimpleName() + " could not find resource for '" + fileName + "'");
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fos!=null) fos.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			try {
				if (is!=null) is.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		
	}
	
}