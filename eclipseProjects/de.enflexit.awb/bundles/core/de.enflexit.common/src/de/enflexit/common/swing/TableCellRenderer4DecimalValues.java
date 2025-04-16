package de.enflexit.common.swing;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The Class TableCellRenderer4DecimalValues can be used to format decimal values within a JTable.
 */
public class TableCellRenderer4DecimalValues extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -3622107579400153000L;
	
	private String decimalFormat;
	private DecimalFormat formatter;
	  
	/**
	 * Instantiates a new table cell render for decimal values.
	 * @param decimalFormat the decimal format to be used by the renderer. If <code>null</code> the default format '#.00' will be used.
	 */
	public TableCellRenderer4DecimalValues(String decimalFormat) {
		this.decimalFormat = decimalFormat;
	}
	
	/**
	 * Returns the decimal formatter.
	 * @return the decimal formatter
	 */
	private DecimalFormat getDecimalFormatter() {
		if (formatter==null) {
			if (decimalFormat==null) {
				formatter = new DecimalFormat("#.00");
			} else {
				formatter = new DecimalFormat(decimalFormat);				
			}
		}
		return formatter;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		value = this.getDecimalFormatter().format((Number)value);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
	
}
