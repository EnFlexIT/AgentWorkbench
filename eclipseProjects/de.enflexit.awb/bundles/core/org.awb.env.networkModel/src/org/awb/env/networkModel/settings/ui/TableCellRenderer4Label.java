package org.awb.env.networkModel.settings.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.enflexit.common.swing.TableCellColorHelper;

/**
 * Is used in the {@link ComponentTypeDialog} for displaying agent classes and selected graph prototypes.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellRenderer4Label implements TableCellRenderer {

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JComponent compColOne = (JComponent) table.getCellRenderer(row, 0);
		
		String simpleClassName = "";
		if(value!=null){
			String className = (String) value;
			int simpleNameStart = className.lastIndexOf(".");
			if(simpleNameStart > -1){
				simpleClassName = className.substring(simpleNameStart+1);
			}
		}
		
		JLabel rendererComponent = new JLabel();
		rendererComponent.setText(simpleClassName);
		rendererComponent.setOpaque(true);
		rendererComponent.setBackground(compColOne.getBackground());
		
		TableCellColorHelper.setTableCellRendererColors(rendererComponent, row, isSelected);
		
		return rendererComponent;
	}

}
