package agentgui.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Zipper {

	private String excludePattern = null;
	private String unzipZipFolder = null;
	private String unzipDestinationFolder = null;
	
	public Zipper() {
		
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
		return excludePattern;
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
		return unzipZipFolder;
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
		return unzipDestinationFolder;
	}

	/**
	 * @param srcFolder: path to the folder to be zipped
	 * @param destZipFile: path to the final zip file
	 */
	public boolean zipFolder(String srcFolder, String destZipFile) {
		
		if (new File(srcFolder).isDirectory()) {

			ZipOutputStream zip = null;
			FileOutputStream fileWriter = null;
			try {
				fileWriter = new FileOutputStream(destZipFile);
				zip = new ZipOutputStream(fileWriter);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

			addFolderToZip("", srcFolder, zip); 
			try {
				zip.flush();
				zip.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Will do the unzipping of the previously specified file
	 * @throws Exception 
	 */
	public void unzipFolder() {
		try {
			if (unzipZipFolder==null) {
				throw new Exception("Use 'setUnzipZipFolder(String)' to specify the zip-file to unzip");
			}
			if (unzipDestinationFolder==null) {
				throw new Exception("Use 'setUnzipDestinationFolder(String)' to specify the destination for unzipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (unzipZipFolder!=null && unzipDestinationFolder!=null) {
			this.unzipFolder(unzipZipFolder, unzipDestinationFolder);	
		}		
	}
	/**
	 * Will do the unzipping of the previously specified file
	 * @throws Exception 
	 */
	public String getRootFolder2Extract() {
		
		try {
			if (unzipZipFolder==null) {
				throw new Exception("Use 'setUnzipZipFolder(String)' to specify the zip-file to unzip");
			}
			if (unzipDestinationFolder==null) {
				throw new Exception("Use 'setUnzipDestinationFolder(String)' to specify the destination for unzipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (unzipZipFolder!=null && unzipDestinationFolder!=null) {
			
			String zipRootFolder = null;
			try {
				ZipFile zf = new ZipFile(unzipZipFolder);
				Enumeration<? extends ZipEntry> zipEnum = zf.entries();
				while (zipEnum.hasMoreElements()) {
					
					ZipEntry item = (ZipEntry) zipEnum.nextElement();
					int cut = item.getName().indexOf("\\", 1);
					zipRootFolder = item.getName().substring(1, cut);
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
	* @param zipFolder the zip file that needs to be unzipped
	* @param destFolder the folder into which unzip the zip file and create the folder structure
	*/
	private void unzipFolder(String zipFolder, String destFolder) {
		
		try {
			ZipFile zf = new ZipFile(zipFolder);
			Enumeration<? extends ZipEntry> zipEnum = zf.entries();
			String dir = destFolder;

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

					InputStream is = zf.getInputStream(item);
					FileOutputStream fos = new FileOutputStream(newfilePath);
					int ch;
					while ((ch = is.read()) != -1) {
						fos.write(ch);
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
			byte[] buf = new byte[1024];
			int len;
			try {
				
				boolean includeFile = false;
				if (excludePattern==null) {
					includeFile=true;
				} else {
					if (srcFile.contains(excludePattern)) {
						includeFile=false;
					} else {
						includeFile=true;
					}
				}
				
				if (includeFile==true) {
					FileInputStream in = new FileInputStream(srcFile);
					zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
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
	
}
