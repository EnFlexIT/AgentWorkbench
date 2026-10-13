package de.enflexit.df.core.processing.transformation.filter.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.enflexit.df.core.processing.transformation.filter.Filters.BetweenFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.ComparisonFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;
import de.enflexit.df.core.processing.transformation.filter.Filters.NullFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.TableFilter;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * Table model implementation for the TableSaw row filter configuration table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableSawRowFilterTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 7217734849941836599L;
	
	private static final String[] COLUMN_NAMES = {"Column", "Filter", "Value/From", "To"};
	
	public record FilterRow(Column<?> column, FilterOperator operator, String value, String upperValue) {}
	
	private final ArrayList<FilterRow> rows = new ArrayList<TableSawRowFilterTableModel.FilterRow>();
	private final List<Column<?>> columns;
	
	/**
	 * Instantiates a new table saw row filter table model.
	 * @param table the table
	 */
	public TableSawRowFilterTableModel(Table table) {
		this.columns = table!=null ? List.copyOf(table.columns()) : new ArrayList<Column<?>>();
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return rows.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		FilterRow row = this.rows.get(rowIndex);
		return switch(columnIndex) {
			case 0 -> row.column();
			case 1 -> row.operator();
			case 2 -> row.value();
			case 3 -> row.upperValue();
			default -> throw new IndexOutOfBoundsException();
		};
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		FilterRow old = this.rows.get(rowIndex);
		
		FilterRow updated = switch (columnIndex) {
			case 0 -> new FilterRow((Column<?>) value, null, "", "");
			case 1 -> new FilterRow(old.column(), (FilterOperator) value, old.value(), old.upperValue());
			case 2 -> new FilterRow(old.column(), old.operator(), value == null ? "" : value.toString(), old.upperValue());
			case 3 -> new FilterRow(old.column(), old.operator(), old.value(), value == null ? "" : value.toString());
			default -> throw new IndexOutOfBoundsException();
		};
		this.rows.set(rowIndex, updated);
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		// --- Determine which columns are editable, based on the currently selected operator
		FilterRow row = rows.get(rowIndex);
		
		// --- The first two columns are always editable ------------
		if (columnIndex <= 1) {
			return true;
		}

		// --- For null comparisons, both value columns are not needed / editable
		if (row.operator() == null || row.operator() == FilterOperator.IS_NULL || row.operator() == FilterOperator.IS_NOT_NULL) {
			return false;
		}

		// --- The second value column is only editable for between comparisons
		if (columnIndex == 3) {
			return row.operator() == FilterOperator.BETWEEN;
		}

		// --- The first value column is editable for all but null comparisons
		return columnIndex == 2;
	}

	/**
	 * Gets the available column names.
	 * @return the available column names
	 */
	public List<String> getAvailableColumnNames() {
		ArrayList<String> columnNames = new ArrayList<String>();
		for (Column<?> column : this.columns) {
			columnNames.add(column.name());
		}
		return columnNames;
	}
	
	/**
	 * Gets the available columns.
	 * @return the available columns
	 */
	public List<Column<?>> getAvailableColumns(){
		return this.columns;
	}

	/**
	 * Gets the row.
	 * @param rowIndex the row index
	 * @return the row
	 */
	public FilterRow getRow(int rowIndex) {
		return rows.get(rowIndex);
	}

	/**
	 * Adds the empty row.
	 */
	public void addEmptyRow() {
		Column<?> firstColumn = columns.isEmpty() ? null : columns.getFirst();
		rows.add(new FilterRow(firstColumn, FilterOperator.EQUALS, "", ""));
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}
	
	/**
	 * Adds a row for the provided TableFiler.
	 * @param tableFilter the table filter
	 */
	public void addRow(TableFilter tableFilter) {
		if (tableFilter!=null) {

			// --- Determine the column to filter on ----------------
			Column<?> filterColumn = this.getColumnByName(tableFilter.column());
			if (filterColumn==null) {
				throw new IllegalArgumentException("The column " + tableFilter.column() + " could not be found!");
			}

			// --- Create a filter row according to the actual type of the filter
			FilterRow filterRow = null;
			if (tableFilter instanceof NullFilter nf) {
				FilterOperator operator = nf.isNull() ? FilterOperator.IS_NULL : FilterOperator.IS_NOT_NULL;
				filterRow = new FilterRow(filterColumn, operator, null, null);
			} else if (tableFilter instanceof ComparisonFilter cf) {
				filterRow = new FilterRow(filterColumn, cf.operator(), cf.value().toString(), null);
			} else if (tableFilter instanceof BetweenFilter bf) {
				filterRow = new FilterRow(filterColumn, FilterOperator.BETWEEN, bf.lower().toString(), bf.upper().toString());
			}
			
			// --- Add the row to the table model -------------------
			if (filterRow!=null) {
				this.rows.add(filterRow);
				fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
			}
			
		}
	}

	/**
	 * Removes the row.
	 * @param rowIndex the row index
	 */
	public void removeRow(int rowIndex) {
		rows.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	/**
	 * Gets the rows.
	 * @return the rows
	 */
	public List<FilterRow> getRows() {
		return List.copyOf(rows);
	}
	
	private Column<?> getColumnByName(String columnName){
		for (Column<?> column : this.getAvailableColumns()) {
			if (column.name().equals(columnName)) {
				return column;
			}
		}
		return null;
	}

}
