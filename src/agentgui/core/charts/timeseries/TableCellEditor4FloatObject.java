package agentgui.core.charts.timeseries;

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

	@Override
	public Object getCellEditorValue() {
		String newValue = textField.getText();
		return new Float(newValue);
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		if(textField == null){
			textField = new JTextField();
		}
		return textField;
	}

}
