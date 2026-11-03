package de.enflexit.df.core.processing.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Static helper methods for the use with {@link JTable}s
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JTableHelper {
	
	/**
	 * Auto resize table columns according to their content..
	 * @param table the table
	 */
	public static void autoResizeColumns(JTable table) {
		for (int column = 0; column < table.getColumnCount(); column++) {
			TableColumn tableColumn = table.getColumnModel().getColumn(column);

			int columnWidth = 0;

			// --- Table header -----------------
			TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

			Component header = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false,	false, -1, column);

			columnWidth = header.getPreferredSize().width;

			// --- Table contents ---------------
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component component = table.prepareRenderer(renderer, row, column);
				columnWidth = Math.max(columnWidth, component.getPreferredSize().width);
			}

			// --- Some additional space --------
			tableColumn.setPreferredWidth(columnWidth + 10);
		}
	}
}
