package agentgui.core.charts.timeseries;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;

/**
 * Cell Editor for date fields.
 * Uses JCalendarCombo as editor component, returns the timestamp as Float object
 * 
 * @author Nils
 *
 */
public class TableCellEditor4Time extends AbstractCellEditor implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536069679238018382L;

	private JSpinner spinner = null;
	
	@Override
	public Object getCellEditorValue() {
		Date date = (Date) spinner.getValue();
		return new Float(date.getTime());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		if(spinner == null){
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			spinner = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, "HH:mm");
			spinner.setEditor(de);
		}
		long timeStamp = ((Float)table.getValueAt(row, column)).longValue();
		spinner.getModel().setValue(new Date(timeStamp));
		
		return spinner;
	}

}
