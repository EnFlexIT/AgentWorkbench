package de.enflexit.df.core.ui.dataSource;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataTreeNodeDataSource;
import de.enflexit.df.core.ui.ConfigurationPanel;

/**
 * The Class JPanelDataSourceConfiguration.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class JPanelDataSourceConfiguration<DSTreeNode extends DataTreeNodeDataSource<?>> extends JPanel implements ConfigurationPanel {

	private static final long serialVersionUID = 6426491391209948791L;

	private DSTreeNode dsTreeNode;
	
	/**
	 * Instantiates a new j panel csv configuration.
	 */
	public JPanelDataSourceConfiguration(DSTreeNode dsTreeNode) {
		this.setDataTreeNodeDataSource(dsTreeNode);
	}
	
	/**
	 * Returns the current {@link DataTreeNodeDataSource}.
	 * @return the data tree node data source
	 */
	public DSTreeNode getDataTreeNodeDataSource() {
		return dsTreeNode;
	}
	/**
	 * Sets the actual {@link DataTreeNodeDataSource}.
	 * @param dsTreeNode the new data tree node data source
	 */
	public void setDataTreeNodeDataSource(DSTreeNode dsTreeNode) {
		this.dsTreeNode = dsTreeNode;
	}
	
}
