package de.enflexit.df.core.extension;

import java.util.List;

import org.hibernate.cfg.Configuration;

import de.enflexit.df.core.ui.JToolBarData;
import de.enflexit.df.core.workbook.ExtensionCache;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;

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

	/**
	 * Can be used to extend the data structure of a database workbook.
	 *
	 * @param sessionFactoryCreator the session factory creator that enables to get DB Session instance
	 * @param conf the Hibernate {@link Configuration} to which the classes can be added
	 * 
	 * @see Configuration#addAnnotatedClass(Class)
	 * @see SessionFactoryCreator#getNewDatabaseSession(String)
	 */
	public void addAnnotatedClassesToDataWorkbook4DB(SessionFactoryCreator sessionFactoryCreator, Configuration conf);
	
	
}
