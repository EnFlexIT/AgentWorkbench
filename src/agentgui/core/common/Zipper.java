package agentgui.core.common;

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

import javax.swing.SwingUtilities;

import agentgui.core.application.Application;

public class Zipper extends Thread {

	private int kindOfExec = 0;
	private final int execZip = 1;
	private final int execUnZip = 2;
	
	private ZipperMonitor zipMonitor = null;
	
	private String excludePattern = null;
	private Vector<File> fileList = new Vector<File>();
	private String zipFolder = null;
	private String zipSourceFolder = null;
	
	private String unzipZipFolder = null;
	private String unzipDestinationFolder = null;
	
	private String projectFolder2Open = null;
	
	/**
	 * Constructor of this class
	 */
	public Zipper() {
		this.setName("Zipper");
		this.zipMonitor = new ZipperMonitor(Application.MainWindow);
	}
	
	@Override
	public void run() {
		if (kindOfExec==execZip) {
			this.zipFolder(zipSourceFolder, zipFolder);
		} else if (kindOfExec==execUnZip) {
			this.unzipFolder(unzipZipFolder, unzipDestinationFolder);
		}

		// --- Hide zipMonitor ------------------------
		this.zipMonitor.setVisible(false);
		
		// --- if specified open extracted project ----
		if (this.projectFolder2Open!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Application.Projects.add(projectFolder2Open);
				}
			});
		}
		
	}
	
	/**
	 * @param excludePattern the excludePattern to set
	 */
	public void setExcludePattern(String excludePattern) {
		this.excludePattern = excludePattern;
	}
	/**
	 * @return the excludePattern
	 */
	public String getExcludePattern() {
		return this.excludePattern;
	}

	/**
	 * @param zipZipFolder the zipZipFolder to set
	 */
	public void setZipFolder(String zipFolder) {
		this.zipFolder = zipFolder;
	}
	/**
	 * @return the zipZipFolder
	 */
	public String getZipFolder() {
		return this.zipFolder;
	}

	/**
	 * @param zipSourceFolder the zipSourceFolder to set
	 */
	public void setZipSourceFolder(String zipSourceFolder) {
		this.zipSourceFolder = zipSourceFolder;
	}
	/**
	 * @return the zipSourceFolder
	 */
	public String getZipSourceFolder() {
		return this.zipSourceFolder;
	}

	/**
	 * @param unzipZipFolder the unzipZipFolder to set
	 */
	public void setUnzipZipFolder(String zipFolder) {
		this.unzipZipFolder = zipFolder;
	}
	/**
	 * @return the unzipZipFolder
	 */
	public String getUnzipZipFolder() {
		return this.unzipZipFolder;
	}

	/**
	 * @param unzipDestinationFolder the unzipDestinationFolder to set
	 */
	public void setUnzipDestinationFolder(String destinationFolder) {
		this.unzipDestinationFolder = destinationFolder;
	}
	/**
	 * @return the unzipDestinationFolder
	 */
	public String getUnzipDestinationFolder() {
		return this.unzipDestinationFolder;
	}

	/**
	 * @return the projectFolder2Open
	 */
	public String getProjectFolder2Open() {
		return projectFolder2Open;
	}
	/**
	 * @param projectFolder2Open the projectFolder2Open to set
	 */
	public void setProjectFolder2Open(String projectFolder2Open) {
		this.projectFolder2Open = projectFolder2Open;
	}

	/**
	 * Will do the unzipping of the previously specified file
	 * @throws Exception 
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
	 * Will do the unzipping of the previously specified zip-file.
	 * Afterwards the specified project will be opened
	 * @param projectFolder
	 */
	public void doUnzipProject(String projectFolder) {
		this.setProjectFolder2Open(projectFolder);
		this.doUnzipFolder();
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
			this.kindOfExec = execUnZip;
			this.start();	
		}		
	}
	/**
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
			this.zipMonitor.setNumberOfFilesMax(zf.size());
			this.zipMonitor.setProcessDescription(false, zipFolder);
			this.zipMonitor.setVisible(true);
			// --------------------------------------------
			
			while (zipEnum.hasMoreElements()) {
			
				ZipEntry item = (ZipEntry) zipEnum.nextElement();
				if (item.isDirectory()) {
					File newdir = new File(dir + File.separator + item.getName());
					newdir.mkdir();
				} else {
					String newfilePath = dir + File.separator + item.getName();
					File newFile = new File(newfilePath);
					if (!newFile.getParentFile().exists()) {
						newFile.getParentFile().mkdirs();
					}
					// --- Consider the zipMonitor --------
					zipMonitor.setNumberNextFile();
					zipMonitor.setCurrentJobFile(newfilePath);
					if (zipMonitor.isCanceled()) {
						// --- Cancel extraction ----------
						this.setProjectFolder2Open(null);
						zf.close();
						File destFile = new File(destFolder + this.getRootFolder2Extract() + File.separator);
						if (destFile.isDirectory()) {
							destFile.delete();	
						}
						return;
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
	 * 
	 * @return
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
			this.start();
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
			this.zipMonitor.setNumberOfFilesMax(this.fileList.size());
			this.zipMonitor.setProcessDescription(true, srcFolder);
			this.zipMonitor.setVisible(true);
			// --------------------------------------------
			
			ZipOutputStream zip = null;
			FileOutputStream fileWriter = null;
			try {
				fileWriter = new FileOutputStream(destZipFile);
				zip = new ZipOutputStream(fileWriter);
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}

			addFolderToZip("", srcFolder, zip); 
			try {
				zip.flush();
				zip.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// --- Consider the zipMonitor ------------------
			if (zipMonitor.isCanceled()) {
				File destFileZip = new File(destZipFile);
				destFileZip.delete();
			}

		} 
		
	}
	
	/**
	 * Adds a single source to the zip-file
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
					this.zipMonitor.setNumberNextFile();
					this.zipMonitor.setCurrentJobFile(srcFile);
					if (zipMonitor.isCanceled()) {
						return;
					}
					FileInputStream in = new FileInputStream(srcFile);
					zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						zip.write(buf, 0, len);
					}
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
	 * This method will evaluate the given folder and it's subfolder.
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
