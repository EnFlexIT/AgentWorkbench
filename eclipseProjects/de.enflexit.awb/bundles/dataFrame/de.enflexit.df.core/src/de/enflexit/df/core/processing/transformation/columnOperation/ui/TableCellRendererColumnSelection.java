package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import tech.tablesaw.columns.Column;

/**
 * The Class TableCellRendererColumnSelection.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellRendererColumnSelection extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 2777358260292065604L;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Column<?> columnValue) {
			setText(columnValue.name());
		} else {
			setText(ColumnOperationConfigurationPanel.COMBOBOX_NULL_PLACEHOLDER);
		}

		return this;
	}

}
