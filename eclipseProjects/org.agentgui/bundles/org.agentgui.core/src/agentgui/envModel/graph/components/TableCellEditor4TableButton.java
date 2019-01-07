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
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;


/**
 * Is used in the {@link ComponentTypeDialog}.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellEditor4TableButton extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 3607367692654837941L;
	
	private JTable componentsTable;
	private GraphEnvironmentController graphController;
	
	private JButton button;
    private int clickCountToStart = 1;
    

    /**
     * Constructor of this class.
     *
     * @param graphController the current GraphEnvironmentController
     * @param componentsTable the components table
     */
    public TableCellEditor4TableButton(GraphEnvironmentController graphController, JTable componentsTable) {
    	this.graphController = graphController;
    	this.componentsTable = componentsTable;
    }


    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.getJButton().setText(value.toString());
        return getJButton();
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return this.getJButton().getText();
    }

    /**
     * Returns the JBbutton.
     * @return the JButton
     */
    private JButton getJButton() {
    	if (button==null) {
    		button = new JButton();
    		//button.setUI(new BasicButtonUI());
    		//button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    		button.addActionListener(this);
    	}
    	return button;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

		// --- Converting view coordinates to model coordinates -----
    	int row = this.componentsTable.getEditingRow();
		int modelRowIndex = this.componentsTable.convertRowIndexToModel(row);
		String compID = (String) this.componentsTable.getModel().getValueAt(modelRowIndex, 0);
		
		NetworkComponent comp = this.graphController.getNetworkModelAdapter().getNetworkComponent(compID);

		NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
		nmn.setInfoObject(comp);
		this.graphController.notifyObservers(nmn);
		
		this.stopCellEditing();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        if(anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#shouldSelectCell(java.util.EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#stopCellEditing()
     */
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#cancelCellEditing()
     */
    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}