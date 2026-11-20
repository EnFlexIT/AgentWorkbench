package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.df.core.processing.transformation.columnOperation.TemporalParameter;

/**
 * Table cell renderer for the parameter column, providing a special representation for temporal parameters.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellRendererParameterColumn extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3562908460439979282L;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// --- Special handling for temporal parameters -------------
		if (value instanceof TemporalParameter parameter) {
			setText(parameter.getValue() + " " + parameter.getUnit().getLabel());
		}
		return this;
	}

}
