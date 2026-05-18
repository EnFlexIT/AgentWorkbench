package de.enflexit.df.core.workbook;

import java.awt.Component;
import java.awt.Window;
import java.io.File;

import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.model.DataController;

/**
 * The Class DataWorkbook4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataWorkbook4DB extends DataWorkbook {

	private static final long serialVersionUID = 5010880029903092936L;
	
	private DatabaseDataSource dataSource;
	private String factoryID;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.workbook.DataWorkbook#getDataWorkbookFile()
	 */
	@Override
	public File getDataWorkbookFile() {
		// --- Nothing further to do here -------
		return null;
	}
	
	
	/**
	 * Returns the workbooks data source.
	 * @return the workbook data source
	 */
	public DatabaseDataSource getWorkbookDataSource() {
		return dataSource;
	}
	/**
	 * Sets the workbooks data source.
	 * @param dataSource the new workbook data source
	 */
	public void setWorkbookDataSource(DatabaseDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Returns the factory ID to be used for database connections.
	 * @return the factory ID
	 */
	public String getFactoryID() {
		return factoryID;
	}
	/**
	 * Sets the factory ID to be used for database connections.
	 * @param factoryID the new factory ID
	 */
	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
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
		
		DataWorkbook4DB dwbDB = new DataWorkbook4DB();
		
		dwbDB.createRandomID();
		dwbDB.setName("Database Data Workbook");
		dwbDB.setDescription("Description of the DataWorkbook, stored in a database");
		return dwbDB;
	}

	
}
