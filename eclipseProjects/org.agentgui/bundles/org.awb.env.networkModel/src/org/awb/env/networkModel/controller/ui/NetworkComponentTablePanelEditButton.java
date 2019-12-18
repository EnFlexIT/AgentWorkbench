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

package org.awb.env.networkModel.controller.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.settings.ui.ComponentTypeDialog;

import de.enflexit.common.swing.TableCellColorHelper;

/**
 * Is used in the {@link ComponentTypeDialog}.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentTablePanelEditButton extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

	private static final long serialVersionUID = 3607367692654837941L;
	
	private JTable componentsTable;
	private GraphEnvironmentController graphController;
	
	private JButton jButtonEdit;
    private int clickCountToStart = 1;
    

    /**
     * Constructor of this class.
     *
     * @param graphController the current GraphEnvironmentController
     * @param componentsTable the components table
     */
    public NetworkComponentTablePanelEditButton(GraphEnvironmentController graphController) {
    	this.graphController = graphController;
    }

    /* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.componentsTable = table;
		TableCellColorHelper.setTableCellRendererColors(this.getJButtonEdit(), row, isSelected);
		return this.getJButtonEdit();
	}

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    	this.componentsTable = table;
        return getJButtonEdit();
    }

    /**
     * Returns the JBbutton.
     * @return the JButton
     */
    private JButton getJButtonEdit() {
    	if (jButtonEdit==null) {
    		jButtonEdit = new JButton();
    		jButtonEdit.setIcon(new ImageIcon(this.getClass().getResource(GraphGlobals.getPathImages() + "EditNetComp.png")));
    		jButtonEdit.setToolTipText("Edit data model ...");
    		jButtonEdit.addActionListener(this);
    	}
    	return jButtonEdit;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

		// --- Converting view coordinates to model coordinates -----
    	int row = this.componentsTable.getEditingRow();
		int modelRowIndex = this.componentsTable.convertRowIndexToModel(row);
		String compID = (String) this.componentsTable.getModel().getValueAt(modelRowIndex, 0);
		
		NetworkComponent comp = this.graphController.getNetworkModel().getNetworkComponent(compID);

		NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
		nmn.setInfoObject(comp);
		this.graphController.notifyObservers(nmn);
		
		this.stopCellEditing();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) { 
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }
    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return this.getJButtonEdit().getText();
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