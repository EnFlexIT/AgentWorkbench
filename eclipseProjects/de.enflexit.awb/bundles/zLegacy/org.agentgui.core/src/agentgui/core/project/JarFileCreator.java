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
package agentgui.core.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * With this class file-resources (e. g. a bin folder created by Eclipse) can be 
 * packed and transfered to a specified destination folder.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JarFileCreator {

	public static int BUFFER_SIZE = 10240;

	private String baseDir;
	private String baseDir4Search;
	
	private File[] fileList;
	
	
	/**
	 * Instantiates a new jar file creator. During instantiation, the
	 * specified folder will be explored.
	 *
	 * @param binDirBase the directory to search for files
	 */

	public JarFileCreator(String binDirBase) {
		this.initiate(binDirBase, null);
	}
	
	/**
	 * Instantiates a new jar file creator. During instantiation, the 
	 * specified folder will be explored.
	 *
	 * @param binDirBase the directory to search for files
	 * @param binDirSubFolder a sub directory of binDirBase
	 */
	public JarFileCreator(String binDirBase, String binDirSubFolder) {
		this.initiate(binDirBase, binDirSubFolder);
	}
	
	/**
	 * Initiate.
	 *
	 * @param binDirBase the directory to search for files
	 * @param binDirSubFolder a sub directory of binDirBase
	 */
	private void initiate(String binDirBase, String binDirSubFolder) {
		
		this.baseDir = binDirBase;
		if (binDirSubFolder==null || binDirSubFolder.equals("")) {
			this.baseDir4Search = this.baseDir;
		} else {
			this.baseDir4Search = this.baseDir + binDirSubFolder;	
		}
		
		// --- Search Files -------------------------------
		File searchIn = new File(this.baseDir4Search);
		ArrayList<File> fileArrayList = this.findFiles(searchIn, this.getAllowedFileSuffixes());
		// --- store result locally -----------------------
		this.fileList = new File[fileArrayList.size()];
		this.fileList = fileArrayList.toArray(this.fileList);
	}
	
	/**
	 * Gets the positive file suffixes for a *.jar-file.
	 * @return the positive file suffixes
	 */
	private ArrayList<String> getAllowedFileSuffixes() {
		
		ArrayList<String> positiveFileSuffixes = new ArrayList<String>();
		positiveFileSuffixes.add(".class");
		positiveFileSuffixes.add(".png");
		positiveFileSuffixes.add(".bmp");
		positiveFileSuffixes.add(".jpg");
		positiveFileSuffixes.add(".jepg");
		positiveFileSuffixes.add(".gif");
		return positiveFileSuffixes;
	}
	
	/**
	 * This method will find all available files specified by the find String.
	 *
	 * @param directory the directory
	 * @param allowedFileSuffixes the allowed file suffixes
	 * @return the array list
	 */
	private ArrayList<File> findFiles(File directory, ArrayList<String> allowedFileSuffixes) {

		File[] files = directory.listFiles();
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// --- Search sub-directories ---------
					matches.addAll(findFiles(files[i], allowedFileSuffixes)); 
				} else {
					// --- filter single file for 'find' ------
					int lastDotPos = files[i].getName().lastIndexOf('.');
					if (lastDotPos>0) {
						String fileSuffix = files[i].getName().substring(lastDotPos);
					    if (allowedFileSuffixes.contains(fileSuffix)) {
					    	matches.add(files[i]);
					    }
					}
				}
			}
		}
		return matches;
	}
	
	/**
	 * Creates the jar archive.
	 *
	 * @param archiveFile the archive file
	 */
	public void createJarArchive(File archiveFile) {
		this.createJarArchive(archiveFile, fileList);
	}
	
	/**
	 * Creates the jar archive.
	 *
	 * @param archiveFile the archive file
	 * @param toBeJared the files that has to be packed
	 */
	public void createJarArchive(File archiveFile, File[] toBeJared) {

		try {
			
			byte buffer[] = new byte[BUFFER_SIZE];
			
			// --- Open archive file ----------------------
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());

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
			System.out.println("Error: " + ex.getMessage());
		}
	}
}
