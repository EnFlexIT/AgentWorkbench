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
 * This class implements features shared by the different cell editor classes used 
 * by the chart data editing tables.
 *   
 * @author Nils
 */
public abstract class BasicCellEditor extends AbstractCellEditor implements KeyListener, TableCellEditor {

	private static final long serialVersionUID = 1402921772045346312L;

	/** The swing component used as cell editor. The exact type is specified by the subclass */
	protected Component editorComponent;
	/** The table */
	protected JTable table;
	/** The original height of the row in non-editing mode */
	protected int originalRowHeight;
	/** The row number of the cell being edited */
	protected int row;
	/** The column number of the row being edited */
	private int column;
	/** The code of the key that was used for confirming the changes */
	private int confirmationKey;
	

	@Override
	public abstract Object getCellEditorValue();
	
	/**
	 * Returns the editor component, initializes it if not done yet
	 * @param value The table cell's value before editing 
	 * @return The editor component
	 */
	protected abstract Component getEditorComponent(Object value);

	
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		this.table = table;
		this.row = row;
		this.column = column;
		this.originalRowHeight = table.getRowHeight(row);
		this.confirmationKey = KeyEvent.VK_UNDEFINED;
		
		table.setRowHeight(row, (int) (originalRowHeight*1.5));
		return this.getEditorComponent(value);
	}

	/** @return the row */
	public int getRow() {
		return row;
	}
	/** @return the column */
	public int getColumn() {
		return column;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		table.setRowHeight(row, originalRowHeight);
		if(confirmationKey == KeyEvent.VK_ENTER && row < table.getRowCount()){
			table.changeSelection(row+1, column, false, false);
		}else if(confirmationKey == KeyEvent.VK_TAB && column< table.getColumnCount()){
			table.changeSelection(row, column+1, false, false);
		}else{
			table.changeSelection(row, column, false, false);
		}
		return super.stopCellEditing();
	}
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#cancelCellEditing()
	 */
	@Override
	public void cancelCellEditing() {
		// Reset row height
		table.setRowHeight(row, originalRowHeight);
		// Set selection to the same cell
		table.changeSelection(row, column, false, false);
		super.cancelCellEditing();
	}
	@Override
	public void keyPressed(KeyEvent ke) {
		
		int keyCode = ke.getKeyCode();
		Object keSource = ke.getSource();
		JTextField jTextField = (JTextField) keSource;;
		int caretPos = jTextField.getCaretPosition();
		int textLength = jTextField.getText().length();
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			// Due to a java bug, the callback method is not called automatically when pressing escape.
			cancelCellEditing();
		} else if(keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB){
			// Remember which key was used to confirm editing
			confirmationKey = keyCode;
		} else if(keyCode==KeyEvent.VK_RIGHT && caretPos==textLength){
			confirmationKey = keyCode;
			stopCellEditing();
		} else if(keyCode==KeyEvent.VK_LEFT && caretPos==0){
			confirmationKey = keyCode;
			stopCellEditing();
		}
	}
	@Override
	public void keyTyped(KeyEvent ke) {
		// Method required by the interface, but not needed here.
	}
	@Override
	public void keyReleased(KeyEvent ke) {
		// Method required by the interface, but not needed here.
	}

}
