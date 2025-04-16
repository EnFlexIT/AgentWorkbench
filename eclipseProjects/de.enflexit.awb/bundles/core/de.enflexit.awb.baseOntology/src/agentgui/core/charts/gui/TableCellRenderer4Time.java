package agentgui.core.charts.gui;

import java.time.Instant;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.common.DateTimeHelper;
import de.enflexit.common.GlobalRuntimeValues;

/**
 * Table cell renderer for date/time data. Displays the date/time as formatted String according the configured time format and zone.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellRenderer4Time extends DefaultTableCellRenderer {
	
	private String timeFormat;
	
	private static final long serialVersionUID = 7378047653825108279L;
	
	public TableCellRenderer4Time(String timeFormat){
		this.timeFormat = timeFormat;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Object value) {
		Instant instant = null;
		if (value!=null && value instanceof Number) {
			long timeMillis = ((Number)value).longValue();
			instant = Instant.ofEpochMilli(timeMillis);
		} else {
			instant = Instant.now();
		}
		this.setText(DateTimeHelper.getDateTimeAsString(instant.toEpochMilli(), this.timeFormat, GlobalRuntimeValues.getZoneId()));
	}

	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	
}
