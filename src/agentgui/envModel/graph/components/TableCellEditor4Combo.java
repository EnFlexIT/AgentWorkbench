package agentgui.envModel.graph.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class TableCellEditor4Combo extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = -1544056145162025328L;

	private JComboBox combo = null;
	private int clickCountToStart = 1;
	 
	public TableCellEditor4Combo(JComboBox comboBox) {
		this.combo = comboBox;
		this.combo.addActionListener(this);
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		combo.setSelectedItem(value);
		return combo;
	}
	
	@Override
	public Object getCellEditorValue() {
		return combo.getSelectedItem();
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
        if(anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}
	@Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
	@Override
	public void cancelCellEditing() {
        super.cancelCellEditing();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}
