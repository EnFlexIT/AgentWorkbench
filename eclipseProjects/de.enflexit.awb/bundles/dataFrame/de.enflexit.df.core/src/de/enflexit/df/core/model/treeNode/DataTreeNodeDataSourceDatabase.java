package de.enflexit.df.core.model.treeNode;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.PaginationDataLoader;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.impl.db.DatabaseDataSource;
import de.enflexit.df.impl.db.JPanelDataSourceConfigurationDatabase;
import de.enflexit.df.impl.db.PaginationDataLoader4DB;

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
	public DataTreeNodeDataSourceDatabase(DataController dataController, DataWorkbook dataWorkbook, DatabaseDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New Database Connection");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("DatabaseBlack.png", "DatabaseGrey.png"));
		this.setTooltipText("Please, configure the database connection ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return this.getConfigurationPanel().getConfigurationToolbarComponents();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JPanelDataSourceConfigurationDatabase getConfigurationPanel() {
		if (jPanelDataSourceConfigurationDatabase==null) {
			jPanelDataSourceConfigurationDatabase = new JPanelDataSourceConfigurationDatabase(this);
			this.getDataController().addPropertyChangeListener(jPanelDataSourceConfigurationDatabase);
		}
		return jPanelDataSourceConfigurationDatabase;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		this.getDataController().removePropertyChangeListener(this.jPanelDataSourceConfigurationDatabase);
		this.jPanelDataSourceConfigurationDatabase = null;
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
