package de.enflexit.df.core.ui.dataSource;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;

/**
 * The Class AbstractJPanelDataSourceConfiguration.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractJPanelDataSourceConfiguration<DSTreeNode extends AbstractDataTreeNodeDataSource<?>> extends JPanel {

	private static final long serialVersionUID = 6426491391209948791L;

	private DataController dataController;
	private DSTreeNode dsTreeNode;
	
	/**
	 * Instantiates a new AbstractJPanelDataSourceConfiguration.
	 *
	 * @param dataController the current data controller
	 * @param dsTreeNode the ds tree node
	 */
	public AbstractJPanelDataSourceConfiguration(DataController dataController, DSTreeNode dsTreeNode) {
		this.setDataController(dataController);
		this.setDataTreeNodeDataSource(dsTreeNode);
	}
	
	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataSourceSettingChanged(String changedValue) {
		if (this.dataController!=null) {
			this.dataController.firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_CHANGED, changedValue, this.getDataTreeNodeDataSource().getDataSource());	
		}
	}
	
	/**
	 * Returns the current {@link AbstractDataTreeNodeDataSource}.
	 * @return the data tree node data source
	 */
	public DSTreeNode getDataTreeNodeDataSource() {
		return dsTreeNode;
	}
	/**
	 * Sets the actual {@link AbstractDataTreeNodeDataSource}.
	 * @param dsTreeNode the new data tree node data source
	 */
	public void setDataTreeNodeDataSource(DSTreeNode dsTreeNode) {
		this.dsTreeNode = dsTreeNode;
	}
	
}
