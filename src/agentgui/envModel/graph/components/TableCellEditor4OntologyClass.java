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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.core.gui.ClassSelector;

/**
 * This class is used in the {@link ComponentTypeDialog} for showing the agent class selector dialog 
 * for the agent column in the JTable
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class TableCellEditor4OntologyClass extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = -1937780991527069423L;
	
	private JButton button;
	/** The ontology class selector dialog */
	private ClassSelector nodeClassSelector = null;
	/** The current ontology class. */
	private String currentOntologyClass = null;
	/** The current agent class	 */
	protected static final String EDIT = "edit";
	
	/**
	 * Default constructor
	 */
	public TableCellEditor4OntologyClass(ClassSelector nodeClassSelector) {
		button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        this.nodeClassSelector = nodeClassSelector;
        
	}
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return currentOntologyClass;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setText(currentOntologyClass);
            
            nodeClassSelector.setVisible(true);   
            // --- wait here for end of editing -----------
            if (nodeClassSelector.isCanceled()==false) { //If OK button pressed
            	if (nodeClassSelector.isValidClass()) {
            		currentOntologyClass = nodeClassSelector.getClassSelected();
            	}
            }
            //Make the renderer reappear.
            this.fireEditingStopped();

        } else { //User pressed dialog's "OK" button.
        	
        }
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentOntologyClass = (String) value;
		return button;
	}

}
