package de.enflexit.df.core.ui.dataSource;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataSourceDatabase;

/**
 * The Class JPanelDataSourceConfigurationDatabase.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <DataTreeNodeDataSourceDatabase> the generic type
 */
public class JPanelDataSourceConfigurationDatabase  extends AbstractJPanelDataSourceConfiguration<DataTreeNodeDataSourceDatabase> {

	private static final long serialVersionUID = 2214513797513629518L;

	/**
	 * Instantiates a new JPanelDataSourceConfigurationDatabase.
	 *
	 * @param dataController the data controller
	 * @param dsTreeNode the JPanelDataSourceConfigurationDatabase
	 */
	public JPanelDataSourceConfigurationDatabase(DataController dataController, DataTreeNodeDataSourceDatabase dsTreeNode) {
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
