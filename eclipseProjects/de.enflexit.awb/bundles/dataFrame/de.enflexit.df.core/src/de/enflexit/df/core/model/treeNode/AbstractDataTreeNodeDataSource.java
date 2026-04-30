package de.enflexit.df.core.model.treeNode;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.data.PaginationDataLoader;
import de.enflexit.df.core.model.AffectedDataObjects;
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
	
	private int rowSelected = 1;
	
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

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeObjectBase#getCaption()
	 */
	@Override
	public String getCaption() {
		return this.getDataSource().getName();
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
	
	
	/**
	 * Has to return the pagination loader for the specific .
	 * @return the pagination loader
	 */
	public abstract PaginationDataLoader<DS> getPaginationDataLoader();
	

	
	/**
	 * Reload.
	 */
	public void reloadTable() {
		try {
			this.setTable(null);
			this.getPaginationDataLoader().reset();
			this.loadDataWithinThread();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Will call load the data in dedicated thread.
	 */
	public void loadDataWithinThread() {

		if (this.isLoading==false) {
			
			this.isLoading = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						AbstractDataTreeNodeDataSource.this.loadNextPage();
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						AbstractDataTreeNodeDataSource.this.isLoading=false;
					}
				}
			}, "DataLoader-" + this.getClass().getSimpleName()).start();
		}
	}
	
	/**
	 * Load next page.
	 */
	private void loadNextPage() {
		
		// --- Try loading the next page ---------------------------- 
		Table page = this.getPaginationDataLoader().loadNextPage();
		if (page==null) {
			
		} else {
			if (this.getTable()==null) {
				this.setTable(page);
			} else {	
				this.getTable().append(page);
			}
		}
		
		// --- Transfer error into node instance --------------------
		this.setErrorMessage(this.getPaginationDataLoader().getErrorMessage());
		
		// --- Inform about page loading ----------------------------
		this.informLoaded(this.getTable(), page);
	}
	
	/**
	 * Informs by firing a PropertyChangeEvent using the {@link DataController}.
	 *
	 * @param oldTable the old tablesaw table
	 * @param newTable the new tablesaw table
	 */
	protected void informLoaded(Table oldTable, Table newTable) {
		this.getDataController().firePropertyChange(DataController.DC_DATA_LOADED, AffectedDataObjects.create(this, this.getDataSource(), oldTable), AffectedDataObjects.create(this, this.getDataSource(), newTable));
	}

	
	/**
	 * Sets the reminder for the row selected.
	 * @param rowSelected the new row selected
	 */
	public void setRowSelected(int rowSelected) {
		this.rowSelected = rowSelected;
	}
	/**
	 * Returns the reminded row selected.
	 * @return the row selected
	 */
	public int getRowSelected() {
		return rowSelected;
	}

}
