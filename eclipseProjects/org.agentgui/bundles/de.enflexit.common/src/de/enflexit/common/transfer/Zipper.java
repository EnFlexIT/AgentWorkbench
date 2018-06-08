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
package de.enflexit.common.transfer;

import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import de.enflexit.common.PathHandling;

/**
 * This class can be used in order zip or unzip a folder structure.<br><br>
 * Example of the usage for zipping:<br>
 * <blockquote><code>
 * 		Zipper zipper = new Zipper();<br>
 * 		zipper.setExcludePattern(".svn");<br>
 * 		zipper.setZipFolder(zipFolder);<br>
 *		zipper.setZipSourceFolder(srcFolder);<br>
 *		zipper.doZipFolder();<br>
 * </code></blockquote>   
 * <br>
 * Example of the usage for unzipping:<br>
 * <blockquote><code> 
 * 		Zipper zipper = new Zipper();<br>
 * 		zipper.setUnzipZipFolder(zipFolder);<br>
 * 		zipper.setUnzipDestinationFolder(destFolder);<br>
 * 		zipper.doUnzipFolder();<br>
 * </code></blockquote>
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Zipper extends Thread {

	private Frame owner;
	
	private String applicationName;
	private Image iconImage;
	private String lookAndFeelClassName;
	
	private Runnable afterJobTask;
	
	private boolean runInThread=true;
	private boolean done = false;
	
	private int kindOfExec = 0;
	private final int execZip = 1;
	private final int execUnZip = 2;
	
	private boolean isHeadlessOperation;
	private ZipperMonitor zipMonitor;
	
	private String excludePattern;
	private Vector<File> fileList = new Vector<File>();
	private String zipFolder;
	private String zipSourceFolder;
	
	private String unzipZipFolder;
	private String unzipDestinationFolder;
	
	
	/**
	 * Instantiates a new Zipper.
	 */
	public Zipper() {
		this(null);	
	}
	/**
	 * Instantiates a new Zipper.
	 * @param owner the owner frame of the zipper
	 */
	public Zipper(Frame owner) {
		this.owner = owner;
		this.setName("Zipper");
	}
	
	/**
	 * Returns the monitoring visualization.
	 * @return the monitoring visualization
	 */
	private ZipperMonitor getZipperMonitor() {
		if (this.zipMonitor==null && this.isHeadlessOperation()==false) {
			this.zipMonitor = new ZipperMonitor(this.owner, this.getApplicationName(), this.getIconImage(), this.getLookAndFeelClassName());	
		}
		return this.zipMonitor;
	}
	
	/**
	 * Sets to execute the defined process in an own thread or not (the default is true).
	 * @param isRunInThread the new extract in thread
	 */
	public void setRunInThread(boolean isRunInThread) {
		this.runInThread = isRunInThread;
	}
	/**
	 * Return if the defined process is to be executed in an own thread.
	 * @return true, if is extract in thread
	 */
	public boolean isRunInThread() {
		return runInThread;
	}
	
	/**
	 * Sets that the operation is done.
	 * @param done the new done
	 */
	public synchronized void setDone(boolean done) {
		this.done = done;
	}
	/**
	 * Checks if the operation is done.
	 * @return true, if is done
	 */
	public synchronized boolean isDone() {
		return done;
	}
	
	/**
	 * Starts the execution of the zip- or unzip process in the current thread.<br>
	 * During the process a Zip-Monitor will be shown to the user.
	 */
	@Override
	public void run() {
		this.startInCurrentThread();
	}
	
	/**
	 * Does the actual {@link Zipper} action. Call this method, if you want to do the 
	 * zipping or unzipping in the current thread. Otherwise use the {@link #start()}
	 * method of the thread 
	 * want to do the action 
	 */
	public void startInCurrentThread() {
		
		this.setDone(false);
		
		// --- Do the specified job -------------------
		if (this.kindOfExec==this.execZip) {
			this.zipFolder(this.zipSourceFolder, this.zipFolder);
		} else if (this.kindOfExec==this.execUnZip) {
			this.unzipFolder(this.unzipZipFolder, this.unzipDestinationFolder);
		}
		// --- Done -----------------------------------
		this.setDone(true);
		
		// --- Hide zipMonitor ------------------------
		if (this.getZipperMonitor()!=null) {
			this.getZipperMonitor().setVisible(false);	
		}
		
		// --- Do the specified after job task ----
		if (this.getAfterJobTask()!=null) {
			this.getAfterJobTask().run();
		}
	}
	
	/**
	 * Sets the application name.
	 * @param applicationName the new application name
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * Gets the application name.
	 * @return the application name
	 */
	private String getApplicationName() {
		return applicationName;
	}

	/**
	 * Sets the icon image for the visualization of the zip progress.
	 * @param iconImage the new icon image
	 */
	public void setIconImage(Image iconImage) {
		this.iconImage = iconImage;
	}
	/**
	 * Returns the icon image for the visualization of the zip progress.
	 * @return the icon image
	 */
	public Image getIconImage() {
		return iconImage;
	}
	
	/**
	 * Sets the look and feel class reference.
	 * @param lookAndFeelClassName the new look and feel class reference
	 */
	public void setLookAndFeelClassName(String lookAndFeelClassReference) {
		this.lookAndFeelClassName = lookAndFeelClassReference;
	}
	/**
	 * Returns the look and feel class reference.
	 * @return the look and feel class reference
	 */
	public String getLookAndFeelClassName() {
		return lookAndFeelClassName;
	}
	
	/**
	 * Sets the after job task.
	 * @param afterJobTask the new after job task
	 */
	public void setAfterJobTask(Runnable afterJobTask) {
		this.afterJobTask = afterJobTask;
	}
	/**
	 * Returns the after job task.
	 * @return the after job task
	 */
	private Runnable getAfterJobTask() {
		return afterJobTask;
	}
	
	/**
	 * This exclude pattern can be used in oder to prevent some 
	 * types of files to be packed. We use this method to prevent
	 * zipping the svn-folder structure during the project export.
	 * 
	 * @param excludePattern the excludePattern to set
	 */
	public void setExcludePattern(String excludePattern) {
		this.excludePattern = excludePattern;
	}
	/**
	 * This method returns the current String, which is used for excluding files to be packed to the zip-file.
	 * @return the excludePattern
	 */
	public String getExcludePattern() {
		return this.excludePattern;
	}

	/**
	 * This method can be used in order to specify the zip-file, which has to be packed
	 * @param zipFolder the zip-folder to set
	 */
	public void setZipFolder(String zipFolder) {
		this.zipFolder = zipFolder;
	}
	/**
	 * Returns the path to the current zip-file for zipping a folder structure
	 * @return the zipZipFolder
	 */
	public String getZipFolder() {
		return this.zipFolder;
	}

	/**
	 * Define the source folder, which has to be packed here.
	 * @param zipSourceFolder the zipSourceFolder to set
	 */
	public void setZipSourceFolder(String zipSourceFolder) {
		this.zipSourceFolder = zipSourceFolder;
	}
	/**
	 * Get the current source folder for packing into a zip-file 
	 * @return the zipSourceFolder
	 */
	public String getZipSourceFolder() {
		return this.zipSourceFolder;
	}

	/**
	 * Specify the zip-File/Folder here, which has to unpacked
	 * @param zipFolder the zip-folder to unzip
	 */
	public void setUnzipZipFolder(String zipFolder) {
		this.unzipZipFolder = zipFolder;
	}
	/**
	 * Get the currently specified zipFile/Folder here 
	 * @return the zip-folder to unzip
	 */
	public String getUnzipZipFolder() {
		return this.unzipZipFolder;
	}

	/**
	 * Set the destination folder for unzipping a zip-File/Folder here
	 * @param unzipDestinationFolder the destination folder for unzipping
	 */
	public void setUnzipDestinationFolder(String unzipDestinationFolder) {
		this.unzipDestinationFolder = unzipDestinationFolder;
	}
	/**
	 * Get the current destination folder for unzipping here
	 * @return the unzipDestinationFolder
	 */
	public String getUnzipDestinationFolder() {
		return this.unzipDestinationFolder;
	}

	/**
	 * Sets the zip monitor visible or not.
	 * @param isHeadlessOperation the flag for headless operation or not
	 */
	public void setHeadlessOperation(boolean isHeadlessOperation) {
		this.isHeadlessOperation = isHeadlessOperation;
	}
	/**
	 * Checks if is headless (un)zip operation.
	 * @return true, if is a headless (un)zip operation
	 */
	public boolean isHeadlessOperation() {
		return isHeadlessOperation;
	}
	
	/**
	 * This method will evaluate the zip-file in order to find the root-folder of the zip
	 * @return Returns the root folder name of the zip-file
	 */
	public String getRootFolder2Extract() {
		
		try {
			if (this.unzipZipFolder==null) {
				throw new Exception("Use 'setUnzipZipFolder(String)' to specify the zip-file to unzip");
			}
			if (this.unzipDestinationFolder==null) {
				throw new Exception("Use 'setUnzipDestinationFolder(String)' to specify the destination for unzipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (this.unzipZipFolder!=null && this.unzipDestinationFolder!=null) {
			
			String zipRootFolder = null;
			try {
				ZipFile zf = new ZipFile(this.unzipZipFolder);
				Enumeration<? extends ZipEntry> zipEnum = zf.entries();
				while (zipEnum.hasMoreElements()) {
					
					ZipEntry item = (ZipEntry) zipEnum.nextElement();
					String entryName = item.getName();
					
					String fileSeperator = null;
					if (entryName.contains("\\")) {
						fileSeperator =  "\\";
					} else if (entryName.contains("/")) {
						fileSeperator = "/";
					}
					
					int cut = entryName.indexOf(fileSeperator, 1);
					zipRootFolder = entryName.substring(0, cut);
					String[] zipRootFolderArr = zipRootFolder.split("|");
					zipRootFolder = "";
					for (int i = 0; i < zipRootFolderArr.length; i++) {
						if (zipRootFolderArr[i].equals(fileSeperator)) {
							zipRootFolderArr[i] = "";
						}
						zipRootFolder += zipRootFolderArr[i]; 
					}
					zipRootFolder.replace(fileSeperator, "");
					zipRootFolder.trim();
					break;
				}
				zf.close();
				return zipRootFolder;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
			
		} else {
			return null;
		}
		
	}
	
	/**
	 * Will do the unzipping of the previously specified zip-file
	 * @throws Exception 
	 */
	public void doUnzipFolder() {
		try {
			if (this.unzipZipFolder==null) {
				throw new Exception("Use 'setUnzipZipFolder(String)' to specify the zip-file to unzip");
			}
			if (this.unzipDestinationFolder==null) {
				throw new Exception("Use 'setUnzipDestinationFolder(String)' to specify the destination for unzipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.unzipZipFolder!=null && this.unzipDestinationFolder!=null) {
			this.kindOfExec = this.execUnZip;
			if (this.isRunInThread()==true) {
				this.start();	
			} else {
				this.startInCurrentThread();
			}
		}		
	}
	/**
	* Will do the actual unzipping of a zip file. 
	* @param zipFolder the zip file that needs to be unzipped
	* @param destFolder the folder into which unzip the zip file and create the folder structure
	*/
	private void unzipFolder(String zipFolder, String destFolder) {
		
		try {
			ZipFile zf = new ZipFile(zipFolder);
			Enumeration<? extends ZipEntry> zipEnum = zf.entries();
			String dir = destFolder;

			// --------------------------------------------
			// --- configure/show zipMonitor ---------
			if (this.getZipperMonitor()!=null) {
				this.getZipperMonitor().setNumberOfFilesMax(zf.size());
				this.getZipperMonitor().setProcessDescription(false, zipFolder);
				this.getZipperMonitor().setVisible(true);	
			}
			// --------------------------------------------
			while (zipEnum.hasMoreElements()) {
			
				ZipEntry item = (ZipEntry) zipEnum.nextElement();
				String itemName = PathHandling.getPathName4LocalOS(item.getName());
				if (item.isDirectory()) {
					File newdir = new File(dir + File.separator + itemName);
					newdir.mkdir();
				} else {
					String newfilePath = dir + File.separator + itemName;
					File newFile = new File(newfilePath);
					if (!newFile.getParentFile().exists()) {
						newFile.getParentFile().mkdirs();
					}
					// --- Consider the zipMonitor --------
					if (this.getZipperMonitor()!=null) {
						this.getZipperMonitor().setNumberNextFile();
						this.getZipperMonitor().setCurrentJobFile(newfilePath);
						if (this.getZipperMonitor().isCanceled()) {
							// --- Cancel extraction ----------
							zf.close();
							File destFile = new File(destFolder + this.getRootFolder2Extract() + File.separator);
							if (destFile.isDirectory()) {
								destFile.delete();	
							}
							return;
						}	
					}
					
					InputStream is = zf.getInputStream(item);
					FileOutputStream fos = new FileOutputStream(newfilePath);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					is.close();
					fos.close();
				}
			}
			zf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method can be invoked after the parameters are set for zipping a folder.<br>
	 * See example above.
	 */
	public void doZipFolder() {
		try {
			if (this.zipFolder==null) {
				throw new Exception("Use 'setZipFolder(String)' to specify the zip-file to zip");
			}
			if (this.zipSourceFolder==null) {
				throw new Exception("Use 'setZipSourceFolder(String)' to specify the destination for unzipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.zipFolder!=null && this.zipSourceFolder!=null) {
			this.kindOfExec = execZip;
			if (this.isRunInThread()==true) {
				this.start();	
			} else {
				this.startInCurrentThread();
			}
		}
	}
	
	/**
	 * @param srcFolder: path to the folder to be zipped
	 * @param destZipFile: path to the final zip file
	 */
	private void zipFolder(String srcFolder, String destZipFile) {
		
		if (new File(srcFolder).isDirectory()) {

			// --------------------------------------------
			// --- Evaluate the folder structure -----
			this.evaluateFolder(srcFolder);
			// --- configure/show zipMonitor ---------
			if (this.getZipperMonitor()!=null) {
				this.getZipperMonitor().setNumberOfFilesMax(this.fileList.size());
				this.getZipperMonitor().setProcessDescription(true, srcFolder);
				this.getZipperMonitor().setVisible(true);	
			}
			// --------------------------------------------
			
			ZipOutputStream zip = null;
			FileOutputStream fileWriter = null;
			try {
				fileWriter = new FileOutputStream(destZipFile);
				zip = new ZipOutputStream(fileWriter);
				addFolderToZip("", srcFolder, zip);
				zip.flush();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (zip!=null) zip.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				try {
					if (fileWriter!=null) fileWriter.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}

			// --- Consider the zipMonitor ------------------
			if (this.getZipperMonitor()!=null && this.getZipperMonitor().isCanceled()) {
				File destFileZip = new File(destZipFile);
				destFileZip.delete();
			}

		} 
		
	}
	
	/**
	 * Adds a single source file to the zip-file
	 * @param path
	 * @param srcFile
	 * @param zip
	 */
	private void addToZip(String path, String srcFile, ZipOutputStream zip) {
		
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			this.addFolderToZip(path, srcFile, zip);
		} else {
			
			try {
				// --- consider the exclude pattern ---------------------
				boolean includeFile = false;
				if (this.excludePattern==null) {
					includeFile=true;
				} else {
					if (srcFile.contains(this.excludePattern)) {
						includeFile=false;
					} else {
						includeFile=true;
					}
				}
				// --- If the current file should be included -----------
				if (includeFile==true) {
					// --- Consider the zipMonitor ------------------
					if (this.getZipperMonitor()!=null) {
						this.getZipperMonitor().setNumberNextFile();
						this.getZipperMonitor().setCurrentJobFile(srcFile);
						if (this.getZipperMonitor().isCanceled()) {
							return;
						}
					}
					FileInputStream in = new FileInputStream(srcFile);
					zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						zip.write(buf, 0, len);
					}
					in.close();
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Adds a complete folder to the zip-file 
	 * @param path
	 * @param srcFolder
	 * @param zip
	 */
	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) {
		File folder = new File(srcFolder);
		String listOfFiles[] = folder.list();
		try {
			for (int i = 0; i < listOfFiles.length; i++) {
				this.addToZip(path + File.separator + folder.getName(), srcFolder + File.separator + listOfFiles[i], zip);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method will evaluate the given folder and it's sub-folder.
	 * The containing File objects will be stored in the Vector 'fileList'
	 * @param folder
	 */
	private void evaluateFolder(String srcFolder) {
		
		File folder = new File(srcFolder);
		String listOfFiles[] = folder.list();
		for (int i = 0; i < listOfFiles.length; i++) {
			
			// --- consider the exclude pattern ---------------------
			boolean includeFile = false;
			if (excludePattern==null) {
				includeFile=true;
			} else {
				if (listOfFiles[i].contains(excludePattern)) {
					includeFile=false;
				} else {
					includeFile=true;
				}
			}
			
			// --- If the current file should be included -----------
			if (includeFile==true) {
				File sngFileObject = new File(srcFolder + File.separator + listOfFiles[i]);
				if (sngFileObject.isDirectory()) {
					this.evaluateFolder(sngFileObject.getAbsolutePath());
				} else {
					this.fileList.add(sngFileObject);
				}
			}
		} // end for
		
	}

}
