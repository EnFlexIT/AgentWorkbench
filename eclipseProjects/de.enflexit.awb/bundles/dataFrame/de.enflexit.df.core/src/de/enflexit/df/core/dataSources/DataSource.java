package de.enflexit.df.core.dataSources;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The interface DataSource serves as the base interface for any kind of data source.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataSource extends Serializable, Closeable {

	public final static String KEY_ID = "ID";
	public final static String KEY_NAME = "Name";
	public final static String KEY_DESCRIPTION = "Description";
	public final static String KEY_ROWS_PER_PAGE = "RowsPerPage";
	
	public static final String CHANGED_ID = "CHANGED_ID";
	public static final String CHANGED_NAME = "CHANGED_NAME";
	public static final String CHANGED_DESCRIPTION = "CHANGED_DESCRIPTION";
	
	
	/**
	 * Has to return a data source identifier.
	 * @return the data source identifier
	 */
	public String getDataSourceIdentifier();
	
	/**
	 * Has to create a new instance of the actual {@link DataSource}.
	 * @return the abstract data source
	 */
	public DefaultDataSource newInstance();
	
	/**
	 * Has to open the current data source.
	 * @return true, if successful
	 */
	public boolean open();
	
	/**
	 * Has to return a new instance of the actual {@link DataSourceIntegration} for the current DataSource
	 * that is located in the specified DataWorkbook and controlled by the specified {@link DataController}.
	 * @return the individual DataSourceIntegration 
	 */
	public DataSourceIntegration<?> getDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook);
	
	
	
	/**
	 * Has to return the id.
	 * @return the id
	 */
	public int getId();
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(int id);
	
	
	/**
	 * Has to return the name of the data source.
	 * @return the name
	 */
	public String getName();
	/**
	 * Sets the name of the data source.
	 * @param name the new name
	 */
	public void setName(String name);
	
	
	/**
	 * Has to return the description for the data source.
	 * @return the description
	 */
	public String getDescription();
	/**
	 * Sets the description of the data source.
	 * @param description the new description
	 */
	public void setDescription(String description);

	
	/**
	 * Returns the number of rows per page, if data pagination is implemented, where
	 * a number smaller or equal 0 means to deactivate pagination, while a number bigger 0
	 * represents the actual number of rows to load per page. 
	 *  
	 * @return the rows per page
	 */
	public int getRowsPerPage();
	/**
	 * Sets the rows per page that are to be loaded in case of pagination. Here, 
	 * a number smaller or equal 0 means to deactivate pagination, while a number bigger 0
	 * represents the actual number of rows to load per page.
	 *  
	 * @param rowsPerPage the new rows per page
	 */
	public void setRowsPerPage(int rowsPerPage);
	
	
	/**
	 * Method to Convert from a DataSource instance to a single configuration String.
	 * @return the configuration string or <code>null</code>
	 */
	public String toConfigurationString();
	/**
	 * Method to Convert from a single configuration String to a DataSource instance.
	 *
	 * @param config the configuration string to convert
	 * @return the converted DatabaseDataSource or <code>null</code>
	 */
	public DataSource fromConfigurationString(String configurationString);

	
	
	/**
	 * Requires sub configuration.
	 * @return true, if a sub configuration is required 
	 */
	public boolean requiresSubConfiguration();
	
	/**
	 * Will be invoked to enforce an update of the sub configurations 
	 * that are to be returned by {@link #getDataSourceSubConfigurations()}.
	 */
	public void updateSubConfigurations();
	
	/**
	 * Returns the list of sub configurations that are based on the current DataSource.
	 * @return the list of sub configurations
	 */
	public List<String> getDataSourceSubConfigurations();
	
	
}
