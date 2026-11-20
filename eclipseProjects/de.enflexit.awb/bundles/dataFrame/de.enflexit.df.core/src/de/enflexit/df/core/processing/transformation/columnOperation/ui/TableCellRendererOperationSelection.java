package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.df.core.processing.transformation.columnOperation.DataOperation;

/**
 * The Class TableCellRendererOperationSelection.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellRendererOperationSelection extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 2777358260292065604L;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof DataOperation operation) {
			setText(operation.getDisplayName());
		} else {
			setText(ColumnOperationConfigurationPanel.COMBOBOX_NULL_PLACEHOLDER);
		}

		return this;
	}

}
