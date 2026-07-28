package de.enflexit.df.core.ui;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class AbstractJPanelDataSourceConfiguration.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractJPanelDataSourceConfiguration<DSTreeNode extends AbstractDataTreeNodeDataSource<?>> extends JPanel {

	private static final long serialVersionUID = 6426491391209948791L;

	private DSTreeNode dsTreeNode;
	
	/**
	 * Instantiates a new AbstractJPanelDataSourceConfiguration.
	 *
	 * @param dataController the current data controller
	 * @param dsTreeNode the ds tree node
	 */
	public AbstractJPanelDataSourceConfiguration(DSTreeNode dsTreeNode) {
		this.setDataTreeNodeDataSource(dsTreeNode);
	}
	/**
	 * Sets the actual {@link AbstractDataTreeNodeDataSource}.
	 * @param dsTreeNode the new data tree node data source
	 */
	public void setDataTreeNodeDataSource(DSTreeNode dsTreeNode) {
		this.dsTreeNode = dsTreeNode;
	}
	/**
	 * Returns the current {@link AbstractDataTreeNodeDataSource}.
	 * @return the data tree node data source
	 */
	public DSTreeNode getDataTreeNodeDataSource() {
		return dsTreeNode;
	}
	
	
	// --- Derived from the DataTreeNode ----------------------------
	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return getDataTreeNodeDataSource().getDataController();
	}
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataSourceSettingChanged(String changedValue) {
		if (this.getDataController()!=null) {
			this.getDataController().firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_CHANGED, changedValue, this.getDataTreeNodeDataSource().getDataSource());	
		}
	}
	
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	protected DataWorkbook getDataWorkbook() {
		return this.getDataTreeNodeDataSource().getDataWorkbook();
	}
	
}
