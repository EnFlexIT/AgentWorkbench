package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition.ResultHandling;

/**
 * The Class TableCellRendererResultHandlingSelection.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellRendererResultHandlingSelection extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6033400673663647100L;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof ResultHandling rh) {
			this.setText(rh.getLabel());
		}
		return this;
	}

}
