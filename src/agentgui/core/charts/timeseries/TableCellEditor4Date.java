package agentgui.core.charts.timeseries;

import java.awt.Component;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 * Cell Editor for date fields.
 * Uses JCalendarCombo as editor component, returns the timestamp as Float object
 * 
 * @author Nils
 *
 */
public class TableCellEditor4Date extends AbstractCellEditor implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536069679238018382L;

	private JCalendarCombo dateTimePicker = null;
	
	private long timestamp = 0;
	
	@Override
	public Object getCellEditorValue() {
		return new Float(timestamp);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		
		// Store old value
		timestamp = ((Float)table.getValueAt(row, column)).longValue();
		
		// Initialize JCalendarCombo if necessary
		if(dateTimePicker == null){
			dateTimePicker = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE | JCalendarCombo.DISPLAY_TIME, true);
		    dateTimePicker.setEditable(true);
		    dateTimePicker.addDateListener(new DateListener() {
				
				@Override
				public void dateChanged(DateEvent de) {
					Calendar c = de.getSelectedDate();
					if(c != null){
						timestamp = c.getTimeInMillis();
					}
					
				}

			});
		}
		return dateTimePicker;
	}

}
