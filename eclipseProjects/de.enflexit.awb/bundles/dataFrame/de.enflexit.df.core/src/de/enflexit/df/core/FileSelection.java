package de.enflexit.df.core;

import java.awt.Window;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.enflexit.awb.core.Application;

/**
 * The Class FileSelection.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FileSelection {

	
	/**
	 * Enables to select a CSV file.
	 *
	 * @param owner the owner window
	 * @param previousFile the previous selected file
	 * @param predefinedPath the predefined path
	 * @return the file
	 */
	public static File selectCsvFile(Window owner, File previousFile, Path predefinedPath) {
		
		FileFilter fFilter = new FileNameExtensionFilter("CSV-File", "csv");
		String title = "Select a CSV-File";
		
		return FileSelection.selectFile(owner, title, fFilter, previousFile, predefinedPath);
	}
	
	/**
	 * Select file.
	 *
	 * @param owner the owner
	 * @param fileFilter the file filter
	 * @param predefinedPath the predefined path
	 * @param title the title
	 * @return the file
	 */
	public static File selectFile(Window owner, String title, FileFilter fileFilter, File previousFile, Path predefinedPath) {
		
		JFileChooser jFileChooserImportCSV = new JFileChooser();
		
		// --- Consider previously selecte4d file -------------------
		if (previousFile==null) {
			jFileChooserImportCSV.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		} else {
			jFileChooserImportCSV.setCurrentDirectory(previousFile.getParentFile());
			jFileChooserImportCSV.setSelectedFile(previousFile);
		}
		
		// --- Consider individual title ----------------------------
		if (title!=null && title.isBlank()==false) {
			jFileChooserImportCSV.setDialogTitle(title);
		} else {
			jFileChooserImportCSV.setDialogTitle("Select a file");
		}
		
		// -- Apply file filter -------------------------------------
		if (fileFilter!=null) {
			jFileChooserImportCSV.setFileFilter(fileFilter);
		}
		
		// --- Open file selection dialog ---------------------------
		if (jFileChooserImportCSV.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = jFileChooserImportCSV.getSelectedFile();
			Application.getGlobalInfo().setLastSelectedFolder(selectedFile.getParent());
			
			if (FileSelection.isInPredefinedDirectory(predefinedPath, selectedFile)==false) {
				JOptionPane.showMessageDialog(owner, "Please choose a file that is located inside the predefined directory!", "Invalid location", JOptionPane.WARNING_MESSAGE);
			} else {
				if (selectedFile!=null && selectedFile.exists()) {
					return selectedFile;
				}
			}
		}
		return null;
	}
	/**
	 * Checks if is in predefined directory.
	 *
	 * @param predefinedPath the predefined path
	 * @param fileToCheck the file to check
	 * @return true, if the file is inside the project folder
	 */
	public static boolean isInPredefinedDirectory(Path predefinedPath, File fileToCheck) {
		if (predefinedPath==null) return true;
		if (fileToCheck==null) return false;
		Path filePath = fileToCheck.toPath();
		return filePath.startsWith(predefinedPath);
	}
	/**
	 * Checks if is in predefined directory.
	 *
	 * @param predefinedPath the predefined path
	 * @param filePathToCheck the file path
	 * @return true, if the file is inside the project folder
	 */
	public static boolean isInPredefinedDirectory(Path predefinedPath, Path filePathToCheck) {
		if (predefinedPath==null) return true;
		if (filePathToCheck==null) return false;
		return filePathToCheck.startsWith(predefinedPath);
	}
	
}
