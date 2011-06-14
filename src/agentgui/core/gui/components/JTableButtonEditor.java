/**
 * 
 */
package agentgui.core.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * @author Satyadeep
 *
 */

public class JTableButtonEditor extends AbstractCellEditor implements TableCellEditor,
                                                         ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3607367692654837941L;
	JTable table;
    JButton button = new JButton();
    int clickCountToStart = 1;

    public JTableButtonEditor(JTable table) {
        this.table = table;
        button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        int row = table.getEditingRow();
        int col = table.getEditingColumn();
       // System.out.printf("row = %d  col = %d%n", row, col);    
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        button.setText(value.toString());
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }

    public boolean isCellEditable(EventObject anEvent) {
        if(anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}