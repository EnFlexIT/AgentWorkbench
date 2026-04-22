package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.FileSelection;
import de.enflexit.df.core.model.DataController;

/**
 * The Class DataWorkbook4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4DB extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookFile()
	 */
	@Override
	public File getDataWorkbookFile() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookLocation()
	 */
	@Override
	public DataWorkbookLocation getDataWorkbookLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Load from data work book location.
	 *
	 * @param dwLocation the dw location
	 * @return the data workbook
	 */
	public static DataWorkbook loadFromDataWorkBookLocation(DataWorkbookLocation dwLocation) {
		
		if (dwLocation==null || dwLocation.getDataWorkbookLocation()==null || dwLocation.getDataWorkbookLocation().isEmpty()==true) return null;

		// TODO
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#save()
	 */
	@Override
	public boolean save() {
		return DataWorkbook4DB.saveToDatabase(this);
	}
	
	
	/**
	 * Saves the specified DataWorkbook to the database.
	 *
	 * @param dataWorkbook the data workbook
	 * @param fileToSaveTo the file to save to
	 * @return true, if successful
	 */
	public static boolean saveToDatabase(DataWorkbook dataWorkbook) {
		
		try {
			
			
			
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Loads the specified DataWorkbook from the database.
	 *
	 * @param fileToOpen the file to open
	 * @return the data workbook 4 XML
	 */
	public static DataWorkbook4DB loadFromDatabase() {
		
		DataWorkbook4DB dwb = null;
		try {
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return dwb;
	}

	/**
	 * Creates a DB DataWorkbook by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param component the component
	 * @return the DataWorkbook4DB
	 */
	public static DataWorkbook4DB create(DataController dataController, Component component) {
		return DataWorkbook4DB.create(dataController, OwnerDetection.getOwnerWindowForComponent(component));
	}
	/**
	 * Creates a DB DataWorkbook by asking for a storage location.
	 *
	 * @param dataController the data controller
	 * @param owner the asking component owner window
	 * @return the DataWorkbook4DB
	 */
	public static DataWorkbook4DB create(DataController dataController, Window owner) {
		
		File xmlFile = FileSelection.selectJsonFile(owner, JFileChooser.SAVE_DIALOG, "Create Workbook", "Create JSON DataWorkbook", null, null);
		if (xmlFile==null) return null;
		
		// TODO
		DataWorkbookLocation dwLocation = new DataWorkbookLocation(DataWorkbook4DB.class, xmlFile.getAbsolutePath());
		if (dataController.getDataWorkbookReminder().getDataWorkbookLocationList().contains(dwLocation)==true) {
			AwbMessageDialog.showMessageDialog(owner, "The Data Workbook '" + xmlFile.getName() + "' was already loaded to the data viewer and thus cannot be overwritten!", "Data Workbook already loaded!", AwbMessageDialog.ERROR_MESSAGE);
			return null;
		} 
		
		
		DataWorkbook4DB dwbDB = new DataWorkbook4DB();
		
		
		return dwbDB;
	}


	
}
