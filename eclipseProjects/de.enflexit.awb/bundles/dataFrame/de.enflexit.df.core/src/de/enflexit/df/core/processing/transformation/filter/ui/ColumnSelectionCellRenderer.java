package de.enflexit.df.core.processing.transformation.filter.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import tech.tablesaw.columns.Column;

/**
 * Table cell renderer for the column selection column. Uses the name of the selected table saw column as renderer text.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnSelectionCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -648672399769662551L;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		// --- Get the column name, use it as value for the renderer instead of the column itself
		String text = value instanceof Column<?> c ? c.name() : "";
		return super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
	}

}
