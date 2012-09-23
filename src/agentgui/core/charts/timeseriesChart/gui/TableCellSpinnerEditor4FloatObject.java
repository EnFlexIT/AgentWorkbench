package agentgui.core.charts.timeseriesChart.gui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;
/**
 * JSpinner-based table cell editor for Float objects
 * @author Nils
 *
 */
public class TableCellSpinnerEditor4FloatObject extends AbstractCellEditor implements TableCellEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7758086423044836617L;
	private JSpinner spinner;
	
	private JTable table;
	
	private int row2edit;

	@Override
	public Object getCellEditorValue(){
		
		// Reset row height
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)/1.5));
		
		String value = ((DefaultEditor)spinner.getEditor()).getTextField().getText();
		value = value.replace(",", ".");
		
		return new Float(Float.parseFloat(value));
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		
		if(spinner == null){
			this.table = table;
			spinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
		}
		// Remember which row was edited
		row2edit = row;	
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
				
		spinner.setValue(table.getValueAt(row, column));
		return spinner;
	}
}
