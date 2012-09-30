package agentgui.core.charts.timeseriesChart.gui;

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
	
	private JTable table;
	
	private int row2edit;
	
	private int originalHeight;
	

	@Override
	public Object getCellEditorValue() {
		
		// Reset row height
		table.setRowHeight(row2edit, originalHeight);
		
		Date date = (Date) spinner.getValue();
		return date.getTime();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		if(spinner == null){
			this.table = table;
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			spinner = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, "HH:mm");
			spinner.setEditor(de);
		}
		
		// Remember which row was edited
		row2edit = row;
		originalHeight = table.getRowHeight(row2edit);
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
		
		// Init spinner
		long timeStamp = (Long) table.getValueAt(row, column);
		spinner.getModel().setValue(new Date(timeStamp));
		
		return spinner;
	}

}
