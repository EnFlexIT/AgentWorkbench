package agentgui.envModel.graph.components;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class TableCellEditor4CheckBox extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -1544056145162025328L;

	private JCheckBox checkBox = null;
	private int clickCountToStart = 1;
	 
	public TableCellEditor4CheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		checkBox.isSelected();
		return checkBox;
	}
	
	@Override
	public Object getCellEditorValue() {
		return checkBox.isSelected();
	}
	@Override
	public boolean isCellEditable(EventObject anEvent) {
        if(anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }
	
}
