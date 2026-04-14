package de.enflexit.df.core.workbook;

import java.io.File;

/**
 * The Class DataWorkbook4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4DB extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	

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
	public static DataWorkbook4DB loadFromDatabase(File fileToOpen) {
		
		DataWorkbook4DB dwb = null;
		try {
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return dwb;
	}
}
