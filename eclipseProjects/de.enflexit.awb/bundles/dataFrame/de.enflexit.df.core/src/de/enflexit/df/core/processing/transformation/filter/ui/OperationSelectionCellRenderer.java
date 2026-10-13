package de.enflexit.df.core.processing.transformation.filter.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;

/**
 * {@link TableCellRenderer} for user friendly representation of filter operators.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OperationSelectionCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -5888476541059811343L;

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
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
		
		return super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
	}
}
