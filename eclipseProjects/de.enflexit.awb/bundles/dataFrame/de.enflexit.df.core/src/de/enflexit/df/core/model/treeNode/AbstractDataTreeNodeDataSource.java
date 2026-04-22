package de.enflexit.df.core.model.treeNode;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.ConfigurationPanel;
import tech.tablesaw.api.Table;

/**
 * The Class AbstractDataTreeNodeDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDataTreeNodeDataSource<DS extends AbstractDataSource> extends DataTreeNodeObjectBase implements ConfigurationPanel {

	private DataController dataController;
	private DS dataSource;

	private Table table;
	
	private boolean isLoading;
	
	/**
	 * Instantiates a new data tree node data source.
	 * @param dataSource the data source
	 */
	public AbstractDataTreeNodeDataSource(DataController dataController, DS dataSource) {
		this.setDataController(dataController);
		this.setDataSource(dataSource);
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
	 * Returns the data source description.
	 * @return the data source
	 */
	public DS getDataSource() {
		return dataSource;
	}
	/**
	 * Sets the data source.
	 * @param dataSource the new data source
	 */
	public void setDataSource(DS dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Will be invoked to load the data bases on the local data source.
	 */
	public abstract boolean loadData();
	
	/**
	 * Will call load data in dedicated thread.
	 */
	public void loadDataWithinThread() {

		if (this.isLoading==false) {
			
			this.isLoading = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						AbstractDataTreeNodeDataSource.this.loadData();
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						AbstractDataTreeNodeDataSource.this.isLoading=false;
					}
				}
			}, "DataLoader-" + this.getClass().getSimpleName()).start();;
		}
	}
	
	/**
	 * Returns the current tablesaw table.
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	/**
	 * Sets the table.
	 * @param table the new table
	 */
	public void setTable(Table table) {
		this.table = table;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeObjectBase#getCaption()
	 */
	@Override
	public String getCaption() {
		return this.getDataSource().getName();
	}

}
