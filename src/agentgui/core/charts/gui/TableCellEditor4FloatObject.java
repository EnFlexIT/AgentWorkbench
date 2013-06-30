/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * JTextField-based table cell editor for Float objects.
 * @author Nils
 *
 */
public class TableCellEditor4FloatObject extends AbstractCellEditor implements TableCellEditor, KeyListener{

	private static final long serialVersionUID = -3915816882186813928L;

	private JTextField textField;
	private JTable table;
	
	private int originalHeight;
	private int row2edit;

	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// Reset row height
		this.table.setRowHeight(row2edit, originalHeight);
		
		String newValue = textField.getText();
		if(newValue.length() > 0){
			return new Float(newValue);
		}else{
			return null;
		}
	}
	
	@Override
	public void cancelCellEditing() {
		// Reset row height
		table.setRowHeight(row2edit, originalHeight);
		super.cancelCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		if(textField == null){
			this.table = table;
			textField = new JTextField();
			textField.addKeyListener(this);
		}
		
		// Initialize with current table cell value, if there is one 
		if(table.getValueAt(row, column) != null){
			textField.setText(table.getValueAt(row, column).toString());
		}
		
		// Remember which row was edited
		row2edit = row;	
		originalHeight = table.getRowHeight(row2edit);
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
		
		return textField;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Due to a java bug, the callback method is not called automatically when pressing escape.  
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			cancelCellEditing();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// Method required by the interface, but not needed here.
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Method required by the interface, but not needed here.
	}

}
