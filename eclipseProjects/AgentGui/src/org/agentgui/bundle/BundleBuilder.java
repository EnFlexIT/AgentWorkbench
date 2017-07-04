package org.agentgui.bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.osgi.framework.Constants;

import agentgui.core.application.Application;
import agentgui.core.project.Project;

/**
 * The Class BundleBuilder is able to create OSGI bundles that can be loaded by the {@link BundleLoader}.<br><br>
 * Therefore, several use cases were considered for the builder:<br>
 * - the case that the a bin folder of a Java project will be linked as external resource<br>    
 * - the inclusion of external jar files (also in combination with the above case) or<br> 
 * - the case of already well defined bundles.<br>
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundleBuilder {

	/**
	 * This Enumeration is used as return type for the method {@link BundleBuilder#getFileType(File)}.
	 */
	private enum FileType {
		FILE_NOT_FOUND,
		DIRECTORY,
		NON_JAR_FILE,
		JAR_FILE,
		OSGI_BUNDLE
	}
	
	private ArrayList<String> allowedFileSuffixes;
	
	private Vector<File> filesOrDirectoriesToBundle;
	
	private String symbolicBundleName;
	private File bundleDirectory;
	
	/**
	 * Instantiates a new bundle builder.
	 *
	 * @param filesOrDirectoriesToBundle the folder or directory to bundle.
	 * @param symbolicBbundleName the symbolic bundle name
	 */
	public BundleBuilder(File fileOrDirectoryToBundle, String symbolicBundleName) {
		// --- Check the arguments first --------
		if (fileOrDirectoryToBundle==null) {
			throw new IllegalArgumentException("No file or directory was specified for the bundle build process!");
		} else if (symbolicBundleName==null || symbolicBundleName.equals("")) {
			throw new IllegalArgumentException("No symbolic bundle name was specified for the bundle build process!");
		}
		// --- Remind the file to bundle --------
		this.getFilesOrDirectoriesToBundle().add(fileOrDirectoryToBundle);
		this.symbolicBundleName = symbolicBundleName;
	}
	/**
	 * Instantiates a new bundle builder.
	 *
	 * @param filesOrDirectoriesToBundle the folders or directories to bundle.
	 * @param symbolicBbundleName the symbolic bundle name
	 */
	public BundleBuilder(Vector<File> filesOrDirectoriesToBundle, String symbolicBundleName) {
		// --- Check the argument first ---------
		if (filesOrDirectoriesToBundle==null || filesOrDirectoriesToBundle.size()==0) {
			throw new IllegalArgumentException("No files or directories were specified for the bundle build process!");
		} else if (symbolicBundleName==null || symbolicBundleName.equals("")) {
			throw new IllegalArgumentException("No symbolic bundle name was specified for the bundle build process!");
		}
		// --- Remind the file to bundle --------
		this.filesOrDirectoriesToBundle = filesOrDirectoriesToBundle;
		this.symbolicBundleName = symbolicBundleName;
	}

	/**
	 * Returns the files or directories to bundle.
	 * @return the files or directories to bundle
	 */
	public Vector<File> getFilesOrDirectoriesToBundle() {
		if (filesOrDirectoriesToBundle==null) {
			filesOrDirectoriesToBundle = new Vector<>();
		}
		return filesOrDirectoriesToBundle;
	}
	/**
	 * Adds the file or directory to bundle.
	 * @param fileOrDirectoryToBundle the file or directory to bundle
	 */
	public void addFileOrDirectoryToBundle(File fileOrDirectoryToBundle) {
		if (fileOrDirectoryToBundle!=null) {
			this.getFilesOrDirectoriesToBundle().add(fileOrDirectoryToBundle);
		} else {
			this.printError("#addFileOrDirectoryToBundle(File): specified file was null!");
		}
	}
	
	/**
	 * Returns the bundle directory.
	 * @return the bundle directory
	 */
	public File getBundleDirectory() {
		if (bundleDirectory==null) {
			//TODO
			String bundlePath = ""; 
			Project project = Application.getProjectFocused();
			if (project==null) {
				bundlePath = Application.getGlobalInfo().getPathWebServer();
			} else {
				bundlePath = project.getProjectTempFolderFullPath();
			}
			bundleDirectory = new File(bundlePath);
		}
		return bundleDirectory;
	}
	/**
	 * Sets the current bundle directory, if this was not already set. Thus, it can only 
	 * be used once ahead the build process. Otherwise, the use of this method has no effect.
	 * @param bundleDirectory the new bundle file
	 */
	public void setBundleDirectory(File bundleFile) {
		if (this.bundleDirectory==null) {
			this.bundleDirectory = bundleFile;
		} else {
			this.printError("#setBundleDirectory(File): Invocation has no effect, since bundle directory was already set.");
		}
	}
	
	/**
	 * Returns the symbolic bundle name.
	 * @return the symbolic bundle name
	 */
	public String getSymbolicBundleName() {
		return symbolicBundleName;
	}
	/**
	 * Sets the symbolic bundle name.
	 * @param symbolicBundleName the new symbolic bundle name
	 */
	public void setSymbolicBundleName(String symbolicBundleName) {
		this.symbolicBundleName = symbolicBundleName;
	}
	
	/**
	 * Builds the specified bundle.
	 */
	public void build() {
		
		// --- Create the bundle first ------------------------------
		if (this.createBundle()==false) return;
		
		// --- Identify, which build strategy has to be considered --
		for (int i = 0; i < this.getFilesOrDirectoriesToBundle().size(); i++) {
			
			File file2Bundle = this.getFilesOrDirectoriesToBundle().get(i);
			switch (getFileType(file2Bundle)) {
			case FILE_NOT_FOUND:
			case NON_JAR_FILE:
			case OSGI_BUNDLE:
				// +++ Nothing to do here +++++++++++++++++++++++++++++++
				break;

			case DIRECTORY:
				// --- A bin folder has to be bundled -------------------
				this.createJarArchive(file2Bundle);
				break;
				
			case JAR_FILE:
				// --- A single jar has to be bundled -------------------
				
				break;
			}
		}
	}
	
	/**
	 * Creates the bundle structure first.
	 * @return true, if successful
	 */
	private boolean createBundle() {
		
		boolean created = false;
		//TODO
//		File bundleDirectory = this.getBundleDirectory();
		
		
		
		return created;
	}
	
	/**
	 * Creates a jar archive from the specified directory.
	 * @param searchDirectory the archive file
	 */
	private void createJarArchive(File searchDirectory) {
		
		if (searchDirectory==null || searchDirectory.isDirectory()==false) {
			this.printError("#createJarArchive(File): the speciied file object was null or not a directory!");
			return;
		} 
		
		// --- Search Files and convert to array ----------
		ArrayList<File> fileArrayList = this.findFiles(searchDirectory);
		File[] fileArray = new File[fileArrayList.size()];
		fileArray = fileArrayList.toArray(fileArray);
		
		// --- Define the jar-file name -------------------  
		File jarFile = new File("");
		// --- Create the jar file ------------------------- 
		this.createJarArchive(searchDirectory, jarFile, fileArray);
	}
	
	/**
	 * Creates the jar archive.
	 *
	 * @param archiveFile the archive file
	 * @param toBeJared the files that has to be packed
	 */
	private void createJarArchive(File searchDirectory, File archiveFile, File[] toBeJared) {

		if (searchDirectory==null || archiveFile==null || toBeJared==null || toBeJared.length==0) return;
		
		try {
			
			byte buffer[] = new byte[10240];
			
			// --- Open archive file ----------------------
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());
			String baseDir = searchDirectory.getAbsolutePath();

			for (int i = 0; i < toBeJared.length; i++) {
				
				if (toBeJared[i] == null || !toBeJared[i].exists() || toBeJared[i].isDirectory()) {
					continue; // --- Just in case...
				}
					
				String subfolder = "";
				if (baseDir!=null) {
					String elementFolder = toBeJared[i].getParent();
					subfolder = elementFolder.substring(baseDir.length(), elementFolder.length());
					subfolder = subfolder.replace(File.separator, "/");
					if (subfolder.startsWith("/")) {
						subfolder = subfolder.substring(1);
					}
					if (subfolder.endsWith("/")==false) {
						subfolder += "/";
					}					
				}
				
				// --- Add archive entry ------------------
				String jarEntryName = subfolder + toBeJared[i].getName();
				JarEntry jarAdd = new JarEntry(jarEntryName);
				jarAdd.setTime(toBeJared[i].lastModified());
				out.putNextEntry(jarAdd);

				// --- Write file to archive --------------
				FileInputStream in = new FileInputStream(toBeJared[i]);
				while (true) {
					int nRead = in.read(buffer, 0, buffer.length);
					if (nRead <= 0)
						break;
					out.write(buffer, 0, nRead);
				}
				in.close();
			}

			out.close();
			stream.close();
			System.out.println("Creating '" + archiveFile.getAbsoluteFile().getName() + "' completed!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			this.printError("#createJarArchive(File, File, File[]): " + ex.getMessage());
		}
	}
	
	/**
	 * This method will find all available files specified by the find String.
	 *
	 * @param directory the directory
	 * @param allowedFileSuffixes the allowed file suffixes
	 */
	private ArrayList<File> findFiles(File directory) {

		File[] files = directory.listFiles();
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// --- Search sub-directories ---------
					matches.addAll(this.findFiles(files[i])); 
				} else {
					// --- filter single file for 'find' ------
					int lastDotPos = files[i].getName().lastIndexOf('.');
					if (lastDotPos>0) {
						String fileSuffix = files[i].getName().substring(lastDotPos);
					    if (this.getAllowedFileSuffixes().contains(fileSuffix)) {
					    	matches.add(files[i]);
					    }
					}
				}
			}
		}
		return matches;
	}
	/**
	 * Returns the allowed file suffixes for a *.jar-file to be created.
	 * @return the positive file suffixes
	 */
	private ArrayList<String> getAllowedFileSuffixes() {
		if (allowedFileSuffixes==null) {
			allowedFileSuffixes = new ArrayList<String>();
			allowedFileSuffixes.add(".class");
			allowedFileSuffixes.add(".png");
			allowedFileSuffixes.add(".bmp");
			allowedFileSuffixes.add(".jpg");
			allowedFileSuffixes.add(".jepg");
			allowedFileSuffixes.add(".gif");
		}
		return allowedFileSuffixes;
	}
	
	/**
	 * Returns the {@link FileType} of the specified file object. If the specified 
	 * file object is null the method also return null. Otherwise, it checks and returns 
	 * the {@link FileType} of the specified file.
	 *
	 * @param file2Check the file object to check
	 * @return the file type
	 */
	private FileType getFileType(File file2Check) {
		
		if (file2Check==null) {
			return null;
		} else if(file2Check.exists()==false) {
			return FileType.FILE_NOT_FOUND;
		} else if (file2Check.isDirectory()==true) {
			return FileType.DIRECTORY;
		} 
		
		// --- Try to open the jar file -------------------------
		FileType fileType = FileType.NON_JAR_FILE;
		JarFile jar = null;
		try {
			jar = new JarFile(file2Check);
			Manifest manifest = jar.getManifest();
			if (manifest!=null) {
				// --- At least we found a jar file -------------
				fileType = FileType.JAR_FILE;

				// --- Check for an OSGI bundle -----------------
				Attributes mainAttributes = manifest.getMainAttributes();
				String symbolicName = (String) mainAttributes.get(Constants.BUNDLE_SYMBOLICNAME);
				if (symbolicName!=null && symbolicName.equals("")==false)  {
					fileType = FileType.OSGI_BUNDLE;
				}
			}
			
		} catch (IOException ioe) {
			//ioe.printStackTrace();
			this.printError("#getFileType(File): " + ioe.getMessage());
			
		} finally {
			// --- Make sure that the file will be closed -------
			try {
				if (jar!=null) jar.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return fileType;
	}

	/**
	 * Prints the specified message as error.
	 * @param message the message
	 */
	private void printError(String message) {
		System.err.println(this.getClass().getSimpleName() + " " + message);
	}
	
}
