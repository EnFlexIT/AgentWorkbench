package de.enflexit.df.core.dataSources.integration;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_Base;
import de.enflexit.df.core.workbook.DataWorkbook;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * The Class AbstractDataSourceDTNO basically holds the TableSaw table that 
 * is to be managed by a PagnationDataLoader. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDataSourceDTNO<DS extends DefaultDataSource> extends DTNO_Base {

	private AbstractDataSourceIntegration<DS> dsIntegration; 

	private Table table;
	private List<ColumnDescription> columnDescriptionList;
	
	private boolean isLoading;
	
	private int rowSelected = 1;

	
	/**
	 * Instantiates a new data tree node data source.
	 * @param dsIntegration the DataSourceIntegration
	 */
	public AbstractDataSourceDTNO(AbstractDataSourceIntegration<DS> dsIntegration) {
		this.setDataSourceIntegration(dsIntegration);
	}
	
	/**
	 * Returns the current data source integration.
	 * @return the data source integration
	 */
	public AbstractDataSourceIntegration<DS> getDataSourceIntegration() {
		return dsIntegration;
	}
	/**
	 * Sets the data source integration.
	 * @param dsIntegration the new data source integration
	 */
	public void setDataSourceIntegration(AbstractDataSourceIntegration<DS>  dsIntegration) {
		this.dsIntegration = dsIntegration;
	}
	/**
	 * Returns the current DataController.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return getDataSourceIntegration().getDataController();
	}
	/**
	 * Returns the current/corresponding {@link DataWorkbook}.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return getDataSourceIntegration().getDataWorkbook();
	}
	/**
	 * Returns the current DataSource.
	 * @return the data source
	 */
	public DS getDataSource() {
		return getDataSourceIntegration().getDataSource();
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
	 * Returns the column description list.
	 * @return the column description list
	 */
	public List<ColumnDescription> getColumnDescriptionList() {
		if (columnDescriptionList==null) {
			columnDescriptionList = new ArrayList<>();
		}
		return columnDescriptionList;
	}
	/**
	 * Update column description list.
	 */
	public void updateColumnDescriptionList() {
		
		if (this.getTable()==null || this.getTable().columnCount()==0) return;
		if (this.getTable().columnCount()==this.getColumnDescriptionList().size()) return;
		this.getColumnDescriptionList().clear();
		
		DataWorkbook dw = this.getDataWorkbook();
		DefaultDataSource ds = this.getDataSource();
		
		Table tsTable = this.getTable();
		for (Column<?> col : tsTable.columns()) {
			// --- Prepare for a corresponding ColumnDescription ----
			String columnName = col.name();
			String columnType = col.type().getPrinterFriendlyName();
			String tableName = null;
			
			// --- May not return null for sub classes -------------- 
			try {
				tableName = this.getTableName(columnName);
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			// --- Add description to list --------------------------
			this.getColumnDescriptionList().add(new ColumnDescription(dw, ds, tableName, columnName, columnType));
		}
		
		// --- Check for an update of the ColumnDescription ---------
		dw.getExtensionCache().updateColumnDescriptionList(this.getColumnDescriptionList());
		
	}
	/**
	 * Has to return the table name for the specified column name.
	 *
	 * @param columnName the column name
	 * @return the table name
	 */
	public String getTableName(String columnName) {
		return null;
	}
	
	
	/**
	 * Has to return the pagination loader for the specific data source.
	 * @return the pagination loader
	 */
	public abstract AbstractPaginationDataLoader<DS> getPaginationDataLoader();
	

	/**
	 * Asynchronously reloads the data table.
	 */
	public void reloadDataTableAsynchronous() {
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
						AbstractDataSourceDTNO.this.loadNextPage();
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						AbstractDataSourceDTNO.this.isLoading=false;
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
		if (newPage!=null) {
			if (this.getTable()==null) {
				this.setTable(newPage);
			} else {	
				this.getTable().append(newPage);
			}
			this.updateColumnDescriptionList();
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
