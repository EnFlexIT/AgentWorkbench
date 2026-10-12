package de.enflexit.df.core.processing.transformation.filter.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.columns.Column;

/**
 * {@link TableCellEditor} for selecting filter operations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OperationSelectionCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 3570253162799585361L;
	
	private final JComboBox<FilterOperator> comboBox = new JComboBox<FilterOperator>();
	
	public OperationSelectionCellEditor() {
		this.comboBox.setRenderer(new OperationSelectionCellRenderer());
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		TableSawRowFilterTableModel model = (TableSawRowFilterTableModel) table.getModel();
		int modelRow = table.convertRowIndexToModel(row);
		
		// --- Determine the possible operators for this column -----
		Column<?> selectedColumn = model.getRow(modelRow).column();
		List<FilterOperator> operators = selectedColumn == null ? List.of() : operatorsForColumn(selectedColumn);
		
		// --- Add them to the combo box model ----------------------
		this.comboBox.removeAllItems();
		operators.forEach(this.comboBox::addItem);
		
		// --- Select the current operator --------------------------
		if (value!=null && operators.contains(value)) {
			this.comboBox.setSelectedItem(value);
		} else if (operators.isEmpty()==false) {
			this.comboBox.setSelectedIndex(0);
		}

		SwingUtilities.invokeLater(this.comboBox::showPopup);
		return this.comboBox;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.comboBox.getSelectedItem();
	}
	
	/**
	 * Provides the possible filter operators for a column, based on its' type.
	 * @param column the column
	 * @return the list
	 */
	private static List<FilterOperator> operatorsForColumn(Column<?> column) {

		if (column instanceof StringColumn) {
			return List.of(FilterOperator.EQUALS, FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);
		}

		if (column instanceof BooleanColumn) {
			return List.of(FilterOperator.EQUALS, FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);
		}

		if (column instanceof NumericColumn<?>) {
			return List.of(FilterOperator.EQUALS, FilterOperator.GREATER_THAN, FilterOperator.LESS_THAN,
					FilterOperator.GREATER_OR_EQUAL, FilterOperator.LESS_OR_EQUAL, FilterOperator.BETWEEN,
					FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);
		}

		if (column instanceof DateColumn || column instanceof DateTimeColumn || column instanceof InstantColumn) {
			return List.of(FilterOperator.EQUALS, FilterOperator.GREATER_THAN, FilterOperator.LESS_THAN,
					FilterOperator.GREATER_OR_EQUAL, FilterOperator.LESS_OR_EQUAL, FilterOperator.BETWEEN,
					FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);
		}

		return List.of(FilterOperator.EQUALS, FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);
	}
	
	/**
	 * The Class OperationSelectionCellRenderer.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class OperationSelectionCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -4409185749731297083L;
		
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			String text = switch (value instanceof FilterOperator op ? op : null) {
				case EQUALS -> "=";
				case GREATER_THAN -> ">";
				case LESS_THAN -> "<";
				case GREATER_OR_EQUAL -> ">=";
				case LESS_OR_EQUAL -> "<=";
				case BETWEEN -> "between";
				case IS_NULL -> "null";
				case IS_NOT_NULL -> "not null";
				case null -> "";
			};
			
			return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
		}
		
	}
}
