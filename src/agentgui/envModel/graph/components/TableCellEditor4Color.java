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

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Is used in the {@link ComponentTypeDialog} for selecting colors.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellEditor4Color extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 2997237817617185155L;

	private JColorChooser colorChooser = null;
	private Color currentColor;
	private JButton button;

    /**
     * Instantiates a new color editor.
     */
    public TableCellEditor4Color() {
        // --- Set up the editor (from the table's point of view), which is a button.
        // --- This button brings up the color chooser dialog, which is the editor 
    	// --- from the user's point of view.
        button = new JButton();
        button.setBorderPainted(false);
        button.addActionListener(this);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
    	if (ae.getSource()==this.button) {
            //The user has clicked the cell, so bring up the dialog.
            button.setBackground(currentColor);
           
            //Set up the dialog that the button brings up.
            colorChooser = new JColorChooser();
            colorChooser.setColor(currentColor);
            JDialog dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this,  null); 
            dialog.setVisible(true);
            // --- From here: user action in the dialog ---
            fireEditingStopped();
            
        } else { //User pressed dialog's "OK" button.
            currentColor = colorChooser.getColor();
        }
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return currentColor;
    }

    //Implement the one method defined by TableCellEditor.
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color)value;
        button.setBackground(currentColor);
        return button;
    }
}

