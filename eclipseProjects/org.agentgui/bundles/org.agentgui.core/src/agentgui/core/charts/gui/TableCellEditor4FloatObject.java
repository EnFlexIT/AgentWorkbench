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
import javax.swing.JTextField;

import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * JTextField-based table cell editor for Float objects.
 * @author Nils
 *
 */
public class TableCellEditor4FloatObject extends BasicCellEditor{

	private static final long serialVersionUID = -3915816882186813928L;

	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		String newValue = ((JTextField)editorComponent).getText();
		if(newValue.length() > 0){
			newValue = newValue.replace(",", ".");
			return new Float(newValue);
		}else{
			return null;
		}
	}
	
	@Override
	public void cancelCellEditing() {
		// Reset row height
		table.setRowHeight(row, originalRowHeight);
		super.cancelCellEditing();
	}

	@Override
	protected Component getEditorComponent(Object value) {
		if(editorComponent == null){
			editorComponent = new JTextField();
			editorComponent.addKeyListener(this);
			editorComponent.addKeyListener(new KeyAdapter4Numbers(true));
		}
		if(value != null){
			((JTextField)editorComponent).setText(""+((Float)value));
		}else{
			((JTextField)editorComponent).setText("");
		}
		return editorComponent;
	}

}
