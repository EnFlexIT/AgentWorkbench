package agentgui.core.charts.timeseriesChart.gui;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer for dates. Displays the date as formatted String according to the system locale.
 * @author Nils
 *
 */
public class TableCellRenderer4Time extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7378047653825108279L;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Object value) {
		Date date = new Date((Long) value);
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		setText(timeFormat.format(date));
	}

}
