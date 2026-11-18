package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationRegistry;
import de.enflexit.df.core.processing.transformation.columnOperation.DataOperation;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition.ResultHandling;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class ColumnOperationTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1662729256093404502L;

	private final List<ColumnOperationDefinition> rules = new ArrayList<>();

	private Table tableSawTable;

	private static final String[] COLUMN_NAMES = { "Column", "Operation", "Parameter", "ResultMode" };

	/**
	 * Instantiates a new column operation table model.
	 * @param tableSawTable the table saw table
	 */
	public ColumnOperationTableModel(Table tableSawTable) {
		this.tableSawTable = tableSawTable;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return rules.size();
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
		ColumnOperationDefinition rule = rules.get(rowIndex);
		return switch(columnIndex) {
		case 0 -> findColumnByName(rule.getColumnName());
		case 1 -> ColumnOperationRegistry.getInstance().getOperationByID(rule.getOperationID());
		case 2 -> rule.getParameter();
		case 3 -> rule.getResultHandling();
		default -> null;
		};
	}
	
	/**
	 * Find table column.
	 * @param columnName the column name
	 * @return the column
	 */
	protected Column<?> findColumnByName(String columnName) {
		if (this.tableSawTable.columnNames().contains(columnName)) {
			return this.tableSawTable.column(columnName);
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		ColumnOperationDefinition rule = rules.get(rowIndex);
		
		switch(columnIndex) {
		case 0:
			Column<?> valueColumn = (Column<?>) value;
			rule.setColumnName(valueColumn!=null ? valueColumn.name() : null);
			rule.setOperationID(null);
			rule.setParameter(null);
			break;
		case 1:
			DataOperation valueOperation = (DataOperation) value;
			rule.setOperationID(valueOperation!=null ? valueOperation.getOperationID() : null);
			rule.setParameter(null);
			break;
		case 2:
			rule.setParameter(value.toString());
			break;
		case 3:
			rule.setResultHandling((ResultHandling) value);
			break;
			
		}
		
		fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	/**
	 * Adds a new rule to the table.
	 */
	public void addRule() {
		rules.add(new ColumnOperationDefinition());
		fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
	}
	
	/**
	 * Removes the rule at the specified row from the table.
	 * @param row the row
	 */
	public void removeRule(int row) {
		rules.remove(row);
		fireTableRowsDeleted(row, row);
	}

	/**
	 * Gets the currently defined rules.
	 * @return the rules
	 */
	public List<ColumnOperationDefinition> getRules() {
		return this.rules;
	}
	
	/**
	 * Sets the operations list.
	 * @param operationsList the new operations list
	 */
	public void setOperationsList(List<ColumnOperationDefinition> operationsList) {
		this.rules.clear();
		this.rules.addAll(operationsList);
	}
	
	/**
	 * Gets the rule at the specified row.
	 * @param rowIndex the row index
	 * @return the rule
	 */
	public ColumnOperationDefinition getRule(int rowIndex) {
	    return rules.get(rowIndex);
	}

	/**
	 * Gets the columns.
	 * @return the columns
	 */
	public List<Column<?>> getColumns() {
		return this.tableSawTable.columns();
	}
	
	/**
	 * Gets the possible operations for the provided column.
	 * @param column the column
	 * @return the operations
	 */
	public List<DataOperation> getOperations(String columnName) {
		
		Column<?> column = null;
		
		if (columnName!=null && columnName.isBlank()==false) {
			column = this.tableSawTable.column(columnName);
		}

		if (column == null) {
			return List.of();
		}

		return ColumnOperationRegistry.getInstance().getOperationsForColumn(column);
	}

}
