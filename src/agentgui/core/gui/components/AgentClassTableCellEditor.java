/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.gui.AgentSelector;
import agentgui.envModel.graph.controller.ComponentTypeDialog;

/**
 * This class is used in the {@link ComponentTypeDialog} for showing the agent class selector dialog 
 * for the agent column in the JTable
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 *
 */
public class AgentClassTableCellEditor extends AbstractCellEditor
implements TableCellEditor,
ActionListener{
	private static final long serialVersionUID = -1937780991527069423L;
	JButton button;
	/**
	 * The agent class selector dialog
	 */
	AgentSelector agentSelector;
	/**
	 * The current agent class 
	 */
	String currentAgentClass;
	protected static final String EDIT = "edit";
	/**
	 * Default constructor
	 */
	public AgentClassTableCellEditor() {
		button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        agentSelector = new AgentSelector(Application.MainWindow);
	}
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return currentAgentClass;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setText(currentAgentClass);
            
            agentSelector.setVisible(true);    
            if (agentSelector.isCanceled()==false) { //If OK button pressed
            	Object[] selected = agentSelector.getSelectedAgentClasses();
    			if(selected != null && selected.length > 0){
    				AgentClassElement agentClass = (AgentClassElement) selected[0];
    				currentAgentClass = agentClass.getElementClass().getName();
    			}
            }
            //Make the renderer reappear.
            fireEditingStopped();

        } else { //User pressed dialog's "OK" button.
        	
        }
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table,
								            Object value,
								            boolean isSelected,
								            int row,
								            int column) {
		currentAgentClass = (String) value;
		return button;
	}

}
