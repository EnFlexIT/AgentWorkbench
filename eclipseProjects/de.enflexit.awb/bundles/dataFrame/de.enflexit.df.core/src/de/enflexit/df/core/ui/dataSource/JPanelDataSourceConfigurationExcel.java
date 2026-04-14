package de.enflexit.df.core.ui.dataSource;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataSourceExcel;

/**
 * The Class JPanelDataSourceConfigurationExcel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <DataTreeNodeDataSourceCsv> the generic type
 */
public class JPanelDataSourceConfigurationExcel  extends AbstractJPanelDataSourceConfiguration<DataTreeNodeDataSourceExcel> {

	private static final long serialVersionUID = 2214513797513629518L;

	/**
	 * Instantiates a new JPanelDataSourceConfigurationExcel.
	 *
	 * @param dataController the data controller
	 * @param dsTreeNode the DataTreeNodeDataSourceExcel
	 */
	public JPanelDataSourceConfigurationExcel(DataController dataController, DataTreeNodeDataSourceExcel dsTreeNode) {
		super(dataController, dsTreeNode);
		this.initialize();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		return this;
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		
	}

	

}
