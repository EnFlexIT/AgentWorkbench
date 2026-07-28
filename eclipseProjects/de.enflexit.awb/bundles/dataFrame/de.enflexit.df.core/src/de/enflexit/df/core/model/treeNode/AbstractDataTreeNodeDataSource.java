package de.enflexit.df.core.model.treeNode;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.PaginationDataLoader;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.DataSourceConfigurationPanel;
import de.enflexit.df.core.workbook.DataWorkbook;
import tech.tablesaw.api.Table;

/**
 * The Class AbstractDataTreeNodeDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDataTreeNodeDataSource<DS extends DefaultDataSource> extends DataTreeNodeObjectBase implements DataSourceConfigurationPanel {

	private DataController dataController;
	private DataWorkbook dataWorkbook;
	private DS dataSource;

	private Table table;
	
	private boolean isLoading;
	
	private int rowSelected = 1;
	
	/**
	 * Instantiates a new data tree node data source.
	 *
	 * @param dataController the current {@link DataController}
	 * @param dataWorkbook the corresponding {@link DataWorkbook} to which the data source belongs
	 * @param dataSource the data source
	 */
	public AbstractDataTreeNodeDataSource(DataController dataController, DataWorkbook dataWorkbook, DS dataSource) {
		this.setDataController(dataController);
		this.setDataWorkbook(dataWorkbook);
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
	 * Returns the corresponding {@link DataWorkbook}.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the corresponding {@link DataWorkbook}.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
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
	 * Asynchronously reloads the data table.
	 */
	public void reloadTableAsynchronous() {
		try {
			this.setTable(null);
			if (this.getPaginationDataLoader()!=null) {
				this.getPaginationDataLoader().reset();
			}
			this.loadNextPageAsynchronous();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Reloads the data table.
	 */
	public void reloadTable() {
		try {
			this.setTable(null);
			if (this.getPaginationDataLoader()!=null) {
				this.getPaginationDataLoader().reset();
			}
			this.loadNextPage();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Will call to load the next page by doing that within in a dedicated thread.
	 */
	public synchronized void loadNextPageAsynchronous() {

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
	public void loadNextPage() {
		
		// --- Try loading the next page ---------------------------- 
		Table newPage = this.getPaginationDataLoader().loadNextPage();
		if (newPage==null) {
			
		} else {
			if (this.getTable()==null) {
				this.setTable(newPage);
			} else {	
				this.getTable().append(newPage);
			}
		}
		
		// --- Transfer error into node instance --------------------
		this.setErrorMessage(this.getPaginationDataLoader().getErrorMessage());
		
		// --- Inform about page loading ----------------------------
		this.informLoaded(this.getTable(), newPage);
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
