package de.enflexit.df.core.dataSources;

import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.DataSourceConfigurationPanel;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Interface DataSourceIntegration describes the structure for 
 * the integration of a specific DataSource into the AWB Data Frame .
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataSourceIntegration<DS extends DefaultDataSource> extends DataSourceConfigurationPanel {

	
	/**
	 * Will be used to set the current DataController.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController);
	/**
	 * Has to return the current DataController.
	 * @return the data controller
	 */
	public DataController getDataController();
	
	
	/**
	 *  Will be used to set the current DataWorkbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook);
	/**
	 * Has to return the current DataWorkbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook();
	
	
	/**
	 *  Will be used to set the current DataSource.
	 * @param dataSource the new data source
	 */
	public void setDataSource(DS dataSource);
	/**
	 * Has to return the current DataSource.
	 * @return the data source
	 */
	public DS getDataSource();
	
	
	/**
	 * Has to return DTNO, the 'data tree node object' that extends the AbstractDataSourceDTNO.
	 * @return the data tree node
	 */
	public AbstractDataSourceDTNO<DS> getDTNO();

	
	/**
	 * Will be invoked if the tree node of the DataSource was created.
	 * @param treeNode the new data tree node
	 */
	public void setDataTreeNode(DefaultMutableTreeNode treeNode);
	/**
	 * Has to return the tree node of the data source that was given 
	 * by the method {@link #setDataTreeNode(DefaultMutableTreeNode)}.
	 * 
	 * @return the data tree node
	 */
	public DefaultMutableTreeNode getDataTreeNode();
	
	/**
	 * Has to add all data tree sub nodes.
	 */
	public void addDataTreeSubNodes();
	
}
