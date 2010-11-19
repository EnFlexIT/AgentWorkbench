package agentgui.core.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarFileCreater {

	public static int BUFFER_SIZE = 10240;

	private String baseDir;
	private String baseDir4Search;
	
	private File[] fileList;
	
	public JarFileCreater(String binDirBase, String binSubDirProject) {
		
		baseDir = binDirBase;
		if (binSubDirProject==null || binSubDirProject.equals("")) {
			baseDir4Search = binDirBase;
		} else {
			baseDir4Search = binDirBase + binSubDirProject;	
		}
		
		// --- Search Files -------------------------------
		File searchIn = new File(baseDir4Search);
		ArrayList<File> fileArrayList = this.findFiles(searchIn, ".class");
		// --- store result locally -----------------------
		fileList = new File[fileArrayList.size()];
		fileList = fileArrayList.toArray(fileList);
	}
	
	/**
	 * This Method should find all available .class - files 
	 * @param dir
	 * @param find
	 * @return
	 */
	private ArrayList<File> findFiles(File dir, String find) {

		File[] files = dir.listFiles();
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// --- Search sub-directories ---------
					matches.addAll(findFiles(files[i], find)); 
				} else {
					// --- filter single file for 'find' ------
					if (files[i].getName().contains(find)) {
						matches.add(files[i]);
					}
				}
			}
		}
		return matches;
	}
	
	protected void createJarArchive(File archiveFile) {
		this.createJarArchive(archiveFile, fileList);
	}
	protected void createJarArchive(File archiveFile, File[] tobeJared) {

		try {
			
			byte buffer[] = new byte[BUFFER_SIZE];
			
			// --- Open archive file ----------------------
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());

			for (int i = 0; i < tobeJared.length; i++) {
				
				if (tobeJared[i] == null || !tobeJared[i].exists() || tobeJared[i].isDirectory()) {
					continue; // --- Just in case...
				}
					
				String subfolder = "";
				if (baseDir!=null) {
					String elementFolder = tobeJared[i].getParent();
					subfolder = elementFolder.substring(baseDir.length(), elementFolder.length());
					subfolder = subfolder.replace("\\", "/");
					if (subfolder.endsWith("/")==false) {
						subfolder += "/";
					}					
				}
				
				// --- Add archive entry ------------------
				String jarEntryName = subfolder + tobeJared[i].getName();
				JarEntry jarAdd = new JarEntry(jarEntryName);
				jarAdd.setTime(tobeJared[i].lastModified());
				out.putNextEntry(jarAdd);

				// --- Write file to archive --------------
				FileInputStream in = new FileInputStream(tobeJared[i]);
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
