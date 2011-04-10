package agentgui.graphEnvironment.controller;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ClassNameTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel rendererComponent = new JLabel();
		String className = (String) value;
		int simpleNameStart = className.lastIndexOf(".");
		if(simpleNameStart > -1){
			rendererComponent.setText(className.substring(simpleNameStart+1));
		}
		if(row % 2 == 0){
			rendererComponent.setOpaque(true);
			rendererComponent.setBackground(new Color(242,242,242));
		}
		
		return rendererComponent;
	}

}
