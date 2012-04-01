package agentgui.core.charts.timeseries;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
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

	@Override
	public Object getCellEditorValue() {
		Double value = (Double) spinner.getValue();
		return new Float(value.floatValue());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		
		if(spinner == null){
			spinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
		}
		spinner.setValue(table.getValueAt(row, column));
		return spinner;
	}
}
