package de.enflexit.df.core.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class ExtensionManager.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExtensionManager {

	private static boolean includeTestExtensions = true;
	
	/**
	 * Returns the list of OSGI-registered {@link DataWorkbookExtension} instances.
	 * @return the data workbook extension list
	 */
	public static List<DataWorkbookExtension> getDataWorkbookExtensionList() {
		return ExtensionManager.getDataWorkbookExtensionList(false);
	}
	/**
	 * Returns the list of OSGI-registered {@link DataWorkbookExtension} instances.
	 *
	 * @param ordered the indicator to sort the list by its extension name
	 * @return the data workbook extension list
	 */
	public static List<DataWorkbookExtension> getDataWorkbookExtensionList(boolean ordered) {
		
		List<DataWorkbookExtension> dwes = ServiceFinder.findServices(DataWorkbookExtension.class); 
		if (dwes.size()>0 && includeTestExtensions==false) {
			// --- Create a new list without test extensions --------
			List<DataWorkbookExtension> dwesNoTest = new ArrayList<>();
			for (DataWorkbookExtension extension : dwes) {
				if (extension.getExtensionName().toLowerCase().startsWith("test.")==false) {
					dwesNoTest.add(extension);
				}
			}
			// --- Assign as result ---------------------------------
			dwes = dwesNoTest;
		}
		
		// --- Sort list? ------------------------------------------- 
		if (ordered==true) {
			try {
				// --- In case of extension without names -----------
				Collections.sort(dwes, new Comparator<DataWorkbookExtension>() {
					@Override
					public int compare(DataWorkbookExtension dwes1, DataWorkbookExtension dwes2) {
						return dwes1.getExtensionName().compareTo(dwes2.getExtensionName());
					}
				});
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dwes;
	}
	
	/**
	 * Returns the DataWorkbookExtension specified by its name.
	 *
	 * @param extensionName the extension name
	 * @return the data workbook extension
	 */
	public static DataWorkbookExtension getDataWorkbookExtension(String extensionName) {
		for (DataWorkbookExtension extension : getDataWorkbookExtensionList()) {
			if (extension.getExtensionName().equalsIgnoreCase(extensionName)==true) {
				return extension;
			}
		}
		return null;
	}
	
	/**
	 * Checks if the specified extension service is used within the specified data workbook.
	 *
	 * @param dwes the DataWorkbookExtension
	 * @param dataWorkbook the data workbook
	 * @return true, if the service is selected for data workbook
	 */
	public static boolean isSelectedForDataWorkbook(DataWorkbookExtension dwes, DataWorkbook dataWorkbook) {
		
		if (dataWorkbook!=null && dwes!=null) {
			return dataWorkbook.getWorkbookExtensions().contains(dwes.getExtensionName());
		}
		return false;
	}
	
}
