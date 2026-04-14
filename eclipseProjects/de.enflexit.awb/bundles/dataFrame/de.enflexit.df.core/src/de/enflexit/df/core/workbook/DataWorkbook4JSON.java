package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.FileSelection;

/**
 * The Class DataWorkbook for XML files.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4JSON extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	private transient File workbookJsonFile;
	
	/**
	 * Instantiates a new data workbook 4 JSON.
	 */
	public DataWorkbook4JSON() {
		this(null);
	}
	/**
	 * Instantiates a new data workbook 4 JSON.
	 * @param workbookJsonFile the workbook json file
	 */
	public DataWorkbook4JSON(File workbookJsonFile) {
		this.setWorkbookJsonFile(workbookJsonFile);
	}
	
	/**
	 * Returns the workbook json file.
	 * @return the workbook json file
	 */
	public File getWorkbookJsonFile() {
		return workbookJsonFile;
	}
	/**
	 * Sets the workbook Json file.
	 * @param workbookJsonFile the new workbook Json file
	 */
	public void setWorkbookJsonFile(File workbookXmlFile) {
		this.workbookJsonFile = workbookXmlFile;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#save()
	 */
	@Override
	public boolean save() {
		return DataWorkbook4JSON.saveToFile(this, this.getWorkbookJsonFile());
	}
	/**
	 * Saves the specified DataWorkbook to a JSON file.
	 *
	 * @param dataWorkbook the data workbook
	 * @param fileToSaveTo the file to save to
	 * @return true, if successful
	 */
	public static boolean saveToFile(DataWorkbook dataWorkbook, File fileToSaveTo) {
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileToSaveTo);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(dataWorkbook, fw);
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fw!=null) {
				try {
					fw.close();
				} catch (IOException ioEx) {
				}
			}
		}
		return false;
	}
	
	/**
	 * Loads a DataWorkbook from a XML file that is to be selected by the user.
	 *
	 * @param component the asking component owner window
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4JSON loadFromFile(Component component) {
		return DataWorkbook4JSON.loadFromFile(OwnerDetection.getOwnerWindowForComponent(component));
	}
	/**
	 * Loads a DataWorkbook from a XML file that is to be selected by the user.
	 *
	 * @param owner the owner window
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4JSON loadFromFile(Window owner) {
		
		File xmlFile = FileSelection.selectJsonFile(owner, JFileChooser.OPEN_DIALOG, "Open Workbook", "Open JSON DataWorkbook", null, null);
		if (xmlFile==null) return null;
		
		return DataWorkbook4JSON.loadFromFile(xmlFile);
	}
	/**
	 * Loads the specified DataWorkbook from a JSON file.
	 *
	 * @param fileToOpen the file to open
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4JSON loadFromFile(File fileToOpen) {
		
		DataWorkbook4JSON dwb = null;
		FileReader reader = null;
		try {
			reader = new FileReader(fileToOpen);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			dwb = gson.fromJson(reader, DataWorkbook4JSON.class);
			dwb.setWorkbookJsonFile(fileToOpen);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return dwb;
	}
	
	/**
	 * Creates a XML DataWorkbok by asking for a storage location.
	 *
	 * @param owner the asking component owner window
	 * @return the DataWorkbook4XML
	 */
	public static DataWorkbook4JSON create(Component component) {
		return DataWorkbook4JSON.create(OwnerDetection.getOwnerWindowForComponent(component));
	}
	/**
	 * Creates a XML DataWorkbok by asking for a storage location.
	 *
	 * @param owner the owner window
	 * @return the DataWorkbook4XML
	 */
	public static DataWorkbook4JSON create(Window owner) {
		
		File xmlFile = FileSelection.selectJsonFile(owner, JFileChooser.SAVE_DIALOG, "Create Workbook", "Create JSON DataWorkbook", null, null);
		if (xmlFile==null) return null;
		
		DataWorkbook4JSON dwbJSON = new DataWorkbook4JSON(xmlFile);
		dwbJSON.createRandomID();
		dwbJSON.setName(xmlFile.getName());
		dwbJSON.setDescription("Description of the XML DataWorkbook");
		dwbJSON.save();
		return dwbJSON;
	}
	
}
