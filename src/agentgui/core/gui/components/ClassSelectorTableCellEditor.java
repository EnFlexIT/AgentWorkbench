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
package agentgui.core.gui.components;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.core.gui.ClassSelector;

public class ClassSelectorTableCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	private ClassSelector classSelector = null;
	
	private String currentClass = null;

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -445634898566639002L;
	
	public ClassSelectorTableCellEditor(Frame owner, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description){
		classSelector = new ClassSelector(owner, clazz2Search4, clazz2Search4CurrentValue, clazz2Search4DefaultValue, clazz2Search4Description);
		JButton btnOk = classSelector.getJButtonOK(); 
		btnOk.removeActionListener(btnOk.getActionListeners()[0]);
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("CellEditor Listener");
//				currentClass = classSelector.getClassSelected();
//				fireEditingStopped();
				
				classSelector.handleOkClick();
				currentClass = classSelector.getClassSelected();
				fireEditingStopped();
			}
		});
		classSelector.getJButtonCancel().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingCanceled();
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		return currentClass;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentClass = (String)value;
		JButton btn = new JButton();
		btn.setText(currentClass);
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				classSelector.setClassSelected(currentClass);
				classSelector.setVisible(true);
				
			}
		});
		return btn;
	}
	
	

}
