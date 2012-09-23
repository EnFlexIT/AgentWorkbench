package agentgui.core.charts.timeseriesChart.gui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * JTextField-based table cell editor for Float objects.
 * @author Nils
 *
 */
public class TableCellEditor4FloatObject extends AbstractCellEditor implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3915816882186813928L;
	private JTextField textField;
	
	private JTable table;
	
	private int row2edit;

	@Override
	public Object getCellEditorValue() {
		// Reset row height
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)/1.5));
		
		String newValue = textField.getText();
		if(newValue.length() > 0){
			return new Float(newValue);
		}else{
			return null;
		}
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		
		if(textField == null){
			this.table = table;
			textField = new JTextField();
		}
		
		// Remember which row was edited
		row2edit = row;	
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
		
		return textField;
	}

}
