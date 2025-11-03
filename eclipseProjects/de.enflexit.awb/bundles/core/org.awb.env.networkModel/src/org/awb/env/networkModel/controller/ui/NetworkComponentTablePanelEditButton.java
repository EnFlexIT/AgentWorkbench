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

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.TableCellColorHelper;

/**
 * Is used in the {@link ComponentTypeDialog}.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentTablePanelEditButton extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

	private static final long serialVersionUID = 3607367692654837941L;
	
	private GraphEnvironmentController graphController;
	
	private String netCompID;
    private int clickCountToStart = 1;
    
    
    /**
     * Constructor of this class.
     * @param graphController the current GraphEnvironmentController
     */
    public NetworkComponentTablePanelEditButton(GraphEnvironmentController graphController) {
    	this.graphController = graphController;
    }

    /* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JButton jButtonRendered = new JButton();
		jButtonRendered.setIcon(new AwbThemeImageIcon(new ImageIcon(this.getClass().getResource(GraphGlobals.getPathImages() + "EditNetComp.png"))));
		jButtonRendered.setToolTipText("Edit data model ...");
		
		TableCellColorHelper.setTableCellRendererColors(jButtonRendered, row, isSelected);
		TableCellColorHelper.setTableBackAndForeGroundToTableDefinition(table, jButtonRendered, isSelected);
		
		return jButtonRendered;
	}
	
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

    	this.netCompID = (String)table.getValueAt(row, 0);
        
    	JButton jButtonEdit = new JButton();
		jButtonEdit.setIcon(new AwbThemeImageIcon(new ImageIcon(this.getClass().getResource(GraphGlobals.getPathImages() + "EditNetComp.png"))));
		jButtonEdit.setToolTipText("Edit data model ...");
		jButtonEdit.addActionListener(this);
		
		TableCellColorHelper.setTableCellRendererColors(jButtonEdit, row, isSelected);
		TableCellColorHelper.setTableBackAndForeGroundToTableDefinition(table, jButtonEdit, isSelected);
		
		return jButtonEdit;
    }
   
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

		// --- Converting view coordinates to model coordinates -----
		NetworkComponent comp = this.graphController.getNetworkModel().getNetworkComponent(this.netCompID);

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
        return null;
    }

}