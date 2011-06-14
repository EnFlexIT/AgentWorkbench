/**
 * 
 */
package agentgui.core.gui.components;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Satyadeep
 *
 */
public class JTableButtonRenderer implements TableCellRenderer {
	 JButton button = new JButton();

	    public Component getTableCellRendererComponent(JTable table,
	                                                   Object value,
	                                                   boolean isSelected,
	                                                   boolean hasFocus,
	                                                   int row, int column) {
	        button.setText(value.toString());
	        return button;
	    }
}
