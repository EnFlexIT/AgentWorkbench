package de.enflexit.df.core.model.treeNode;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.data.PaginationDataLoader;
import de.enflexit.df.core.data.PaginationDataLoader4DB;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationDatabase;

/**
 * The Class DataTreeNodeDataSourceDatabase.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceDatabase extends AbstractDataTreeNodeDataSource<DatabaseDataSource> {

	private JPanelDataSourceConfigurationDatabase jPanelDataSourceConfigurationDatabase;
	
	private PaginationDataLoader4DB paginationDataLoader4DB;
	
	/**
	 * Instantiates a new data tree node data source database.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceDatabase(DataController dataController, DatabaseDataSource dataSource) {
		super(dataController, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New Database Connection");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("DatabaseBlack.png", "DatabaseGrey.png"));
		this.setTooltipText("Please, configure the database connection ...");
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
		if (jPanelDataSourceConfigurationDatabase==null) {
			jPanelDataSourceConfigurationDatabase = new JPanelDataSourceConfigurationDatabase(this.getDataController(), this);
		}
		return jPanelDataSourceConfigurationDatabase;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource#getPaginationDataLoader()
	 */
	@Override
	public PaginationDataLoader<DatabaseDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4DB==null) {
			paginationDataLoader4DB = new PaginationDataLoader4DB(this.getDataSource());
		}
		return paginationDataLoader4DB;
	}

}
