package de.enflexit.df.core.model.treeNode;

import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationDatabase;

/**
 * The Class DataTreeNodeDataSourceDatabase.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceDatabase extends AbstractDataTreeNodeDataSource<DatabaseDataSource> {

	private JPanelDataSourceConfigurationDatabase jPanelDataSourceConfigurationDatabase;
	
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
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#getJPanelConfiguration()
	 */
	@Override
	public JPanelDataSourceConfigurationDatabase getJPanelConfiguration() {
		if (jPanelDataSourceConfigurationDatabase==null) {
			jPanelDataSourceConfigurationDatabase = new JPanelDataSourceConfigurationDatabase(this.getDataController(), this);
		}
		return jPanelDataSourceConfigurationDatabase;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#loadData()
	 */
	@Override
	public boolean loadData() {
		// TODO Auto-generated method stub
		return false;
	}

}
