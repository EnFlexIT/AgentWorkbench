package de.enflexit.df.core.extension;

import java.util.List;

import de.enflexit.df.core.ui.JToolBarData;
import de.enflexit.df.core.workbook.ExtensionCache;

/**
 * The Interface DataWorkbookExtension.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataWorkbookExtension {

	/**
	 * Has to return the extension name.
	 * @return the extension name
	 */
	public String getExtensionName(); 
	
	/**
	 * Has to return the extensions description.
	 * @return the extension description
	 */
	public String getExtensionDescription();

	/**
	 * Has to create a new instance of the extension.
	 * @return the data workbook extension
	 */
	public DataWorkbookExtension newInstance();

	/**
	 * Can be used to Initialize your extension.
	 * @param extensionCache the actual {@link ExtensionCache} of the corresponding workbook
	 * 
	 * @see ExtensionCache#getDataController()
	 * @see ExtensionCache#getDataWorkbook()
	 */
	public void initialize(ExtensionCache extensionCache);

	/**
	 * Can be used to dispose the current extension instance and its related elements.
	 * Thus, individual listener can be removed and so on. 
	 */
	public void dispose();
	

	/**
	 * Can be used to add own components to the main toolbar.
	 * @param jToolBarData the j tool bar data
	 */
	public void addMainToolbarComponents(JToolBarData jToolBarData);

	
	/**
	 * Can be used to update the specified column description list.
	 * @param columnDescriptionList the column description list
	 */
	public void updateColumnDescriptionList(List<ColumnDescription> columnDescriptionList);
	
	
	
	
	
	// ------------------------------------------------------------------------
	// --- Consideration: What are possible extensions ------------------------
	// ------------------------------------------------------------------------
	// --- Main Toolbar components 
	// --- Column Description Service 
	// --- Database structure extension
	
	
	
	
	
}
