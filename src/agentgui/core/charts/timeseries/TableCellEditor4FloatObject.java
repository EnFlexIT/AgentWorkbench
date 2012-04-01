package agentgui.core.charts.timeseries;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

/**
 * Table cell editor for Float objects.
 * @author Nils
 *
 */
public class TableCellEditor4FloatObject extends AbstractCellEditor implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3915816882186813928L;
	private JSpinner spinner;

	@Override
	public Object getCellEditorValue() {
		Double value = (Double) spinner.getValue();
		return new Float(value.floatValue());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
//		if(textField == null){
//			textField = new JTextField();
//		}
//		textField.setText(table.getValueAt(row, column).toString());
//		return textField;
		
		if(spinner == null){
			spinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
		}
		spinner.setValue(table.getValueAt(row, column));
		return spinner;
	}

}
