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
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 *
 */

public class JTableButtonEditor extends AbstractCellEditor implements TableCellEditor,
                                                         ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3607367692654837941L;
	JTable table;
    JButton button = new JButton();
    int clickCountToStart = 1;

    public JTableButtonEditor(JTable table) {
        this.table = table;
        button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
//        int row = table.getEditingRow();
//        int col = table.getEditingColumn();
       // System.out.printf("row = %d  col = %d%n", row, col);    
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        button.setText(value.toString());
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }

    public boolean isCellEditable(EventObject anEvent) {
        if(anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}