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
package agentgui.envModel.graph.components;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.enflexit.common.classSelection.ClassSelectionDialog;

/**
 * Is used in the {@link ComponentTypeDialog} for displaying agent classes.
 *
 * @author Nils Loose - University of Duisburg - Essen
 */
public class TableCellEditor4ClassSelector extends AbstractCellEditor implements TableCellEditor{
	
	private static final long serialVersionUID = -445634898566639002L;
	
	private ClassSelectionDialog classSelector = null;
	private String currentClass = null;


	/**
	 * Instantiates a new class selector table cell editor.
	 *
	 * @param owner the owner
	 * @param clazz2Search4 the clazz2 search4
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 */
	public TableCellEditor4ClassSelector(Frame owner, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull){
		
		classSelector = new ClassSelectionDialog(owner, clazz2Search4, clazz2Search4CurrentValue, clazz2Search4DefaultValue, clazz2Search4Description, allowNull);
		
		JButton btnOk = classSelector.getJButtonOK(); 
		btnOk.removeActionListener(btnOk.getActionListeners()[0]);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classSelector.handleOkClick();
				currentClass = classSelector.getClassSelected();
				if (currentClass!=null && currentClass.isEmpty()) {
					currentClass = null;
				}
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

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return currentClass;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
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
