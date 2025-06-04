/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package de.enflexit.oidc.httpCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

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
		LOGIN_SUCCESS_HTML("LogInSuccess.html"),
		LOGO_PNG("logo.png"),
		BACKGROUND_JPG("background.jpg"),
		FAVICON_PNG("favicon.png")
		;
		
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
	
	private ArrayList<FileToProvide> filesFromBundle;
	
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
			propertyDirectoryInBundle = "/" + PathHandling.getPropertiesPath(false);
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
		Path pathProperties = PathHandling.getPropertiesPath(true);
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
				
				this.getFilesFromBundle().add(fileToProvide);
				
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
	
	/**
	 * Gets the files of files that were extracted from the bundle.
	 * @return the files from bundle
	 */
	private ArrayList<FileToProvide> getFilesFromBundle() {
		if (filesFromBundle==null) {
			filesFromBundle = new ArrayList<PropertyContentProvider.FileToProvide>();
		}
		return filesFromBundle;
	}
	
	/**
	 * Deletes a file if it was previously extracted from the bundle. If not, the file is ignored.
	 * Optionally, the folder containing the extracted files can be deleted to, if it is empty.
	 * @param fileToProvide the file to be deleted
	 * @param deleteFolderIfEmpty specifies if the folder should be deleted, too
	 */
	public void deleteIfFromBundle(FileToProvide fileToProvide, boolean deleteFolderIfEmpty) {
		
		// --- Only do something if the file was actually extracted from the bundle -----
		if (this.getFilesFromBundle().contains(fileToProvide)) {
			
			// --- Delete the specified file ------------------------
			Path propsFolderPath = PathHandling.getPropertiesPath(true);
			File fileToDelete = propsFolderPath.resolve(fileToProvide.toString()).toFile();
			if (fileToDelete.exists()) {
				fileToDelete.delete();
			}
			
			// --- If specified so, also delete the folder if it is empty -----  
			if (deleteFolderIfEmpty==true) {
				File propsFolder = propsFolderPath.toFile();
				if (propsFolder.listFiles().length==0) {
					propsFolder.delete();
				}
			}
		}
	}
	
}
