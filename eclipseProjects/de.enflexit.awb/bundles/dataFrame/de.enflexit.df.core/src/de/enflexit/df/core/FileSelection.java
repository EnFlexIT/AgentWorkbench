package de.enflexit.df.core;

import java.awt.Window;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ui.AwbMessageDialog;

/**
 * The Class FileSelection.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FileSelection {

	/**
	 * Enables to select a CSV file.
	 *
	 * @param owner the owner window
	 * @param dialogType the dialog type
	 * @param approveButtonText the approve button text
	 * @param title the title
	 * @param previousFile the previous selected file
	 * @param predefinedPath the predefined path
	 * @return the file
	 */
	public static File selectCsvFile(Window owner, int dialogType, String approveButtonText, String title, File previousFile, Path predefinedPath) {
		
		FileFilter fFilter = new FileNameExtensionFilter("CSV-File", "csv");
		if (title==null) title = "Select a CSV-File";
		
		return FileSelection.selectFile(owner, dialogType, approveButtonText, title, fFilter, previousFile, predefinedPath);
	}
	
	/**
	 * Enables to select a CSV file.
	 *
	 * @param owner the owner window
	 * @param dialogType the dialog type
	 * @param approveButtonText the approve button text
	 * @param title the title
	 * @param previousFile the previous selected file
	 * @param predefinedPath the predefined path
	 * @return the file
	 */
	public static File selectXMLFile(Window owner, int dialogType, String approveButtonText, String title, File previousFile, Path predefinedPath) {
		
		FileFilter fFilter = new FileNameExtensionFilter("XML-File", "xml");
		if (title==null) title = "Select a XML-File";
		
		return FileSelection.selectFile(owner, dialogType, approveButtonText, title, fFilter, previousFile, predefinedPath);
	}
	
	/**
	 * Enables to select a JSON file.
	 *
	 * @param owner the owner window
	 * @param dialogType the dialog type
	 * @param approveButtonText the approve button text
	 * @param title the title
	 * @param previousFile the previous selected file
	 * @param predefinedPath the predefined path
	 * @return the file
	 */
	public static File selectJsonFile(Window owner, int dialogType, String approveButtonText, String title, File previousFile, Path predefinedPath) {
		
		FileFilter fFilter = new FileNameExtensionFilter("JSON-File", "json");
		if (title==null) title = "Select a JSON-File";
		
		return FileSelection.selectFile(owner, dialogType, approveButtonText, title, fFilter, previousFile, predefinedPath);
	}
	
	/**
	 * Select file.
	 *
	 * @param owner the owner
	 * @param dialogType the dialog type: either {@link JFileChooser#OPEN_DIALOG}, {@link JFileChooser#SAVE_DIALOG} or {@link JFileChooser#CUSTOM_DIALOG}
	 * @param approveButtonText the approve button text
	 * @param title the title
	 * @param fileFilter the file filter
	 * @param previousFile the previous file
	 * @param predefinedPath the predefined path
	 * @return the file
	 */
	public static File selectFile(Window owner, int dialogType, String approveButtonText, String title, FileFilter fileFilter, File previousFile, Path predefinedPath) {
		
		JFileChooser jfc = new JFileChooser();
		
		// --- Consider previously selecte4d file -------------------
		if (previousFile==null) {
			jfc.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		} else {
			jfc.setCurrentDirectory(previousFile.getParentFile());
			jfc.setSelectedFile(previousFile);
		}
		
		// --- Consider individual title ----------------------------
		if (title!=null && title.isBlank()==false) {
			jfc.setDialogTitle(title);
		} else {
			jfc.setDialogTitle("Select a file");
		}
		
		// -- Apply file filter -------------------------------------
		if (fileFilter!=null) {
			jfc.setFileFilter(fileFilter);
		}
		
		// --- Set the dialog type ----------------------------------
		if (approveButtonText!=null) {
			jfc.setApproveButtonText(approveButtonText);
		}
		
		// --- Open file selection dialog ---------------------------
		int userAnswer = 0;
		switch (dialogType) {
		case JFileChooser.OPEN_DIALOG: 
			userAnswer = jfc.showOpenDialog(owner);
			break;
		case JFileChooser.SAVE_DIALOG:
			userAnswer = jfc.showSaveDialog(owner);
			break;
		case JFileChooser.CUSTOM_DIALOG:
			userAnswer = jfc.showDialog(owner, approveButtonText);
			break;
		}
		if (userAnswer==JFileChooser.APPROVE_OPTION) {
			
			// --- Get selected file --------------------------------
			File selectedFile = jfc.getSelectedFile();
			
			// --- Check for file extension -------------------------
			String path = selectedFile.getAbsolutePath();
			String extension = FileSelection.getExtensionForFilter(jfc.getFileFilter());
			if (path.endsWith("." + extension)==false) {
				selectedFile = new File(path + "." + extension);
			}
			
			// --- Remind last selected folder ----------------------
			if (selectedFile.isDirectory()==true) {
				Application.getGlobalInfo().setLastSelectedFolder(selectedFile);
			} else {
				Application.getGlobalInfo().setLastSelectedFolder(selectedFile.getParent());
			}
			
			// --- Check if file exists -----------------------------
			if (dialogType==JFileChooser.SAVE_DIALOG && selectedFile.exists()==true) {
				int dialogAnswer = AwbMessageDialog.showConfirmDialog(owner, "The selected file '" + selectedFile.getName() + "' already exists.\nWould you like to override the file?", "Override file", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
				if (dialogAnswer==JOptionPane.NO_OPTION) return null;
			}
			
			// --- Further checks -----------------------------------
			if (FileSelection.isInPredefinedDirectory(predefinedPath, selectedFile)==false) {
				AwbMessageDialog.showMessageDialog(owner, "Please choose a file that is located inside the predefined directory!", "Invalid location", JOptionPane.WARNING_MESSAGE);
			} else {
				if (selectedFile!=null) {
					if (dialogType==JFileChooser.OPEN_DIALOG && selectedFile.exists()==false) {
						return null;
					}
					return selectedFile;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the extension for filter.
	 *
	 * @param fileFilter the file filter
	 * @return the extension for filter
	 */
	private static String getExtensionForFilter(FileFilter fileFilter) {
		if (fileFilter instanceof FileNameExtensionFilter) {
			FileNameExtensionFilter fnef = (FileNameExtensionFilter)fileFilter;
			return fnef.getExtensions()[0];
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
