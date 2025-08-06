package org.awb.env.networkModel.controller.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.enflexit.common.swing.TableCellColorHelper;

/**
 * The Class NetworkComponentTablePanelEditID is used in the table of 
 * NetworkComponent's in order to display or edit the ID of a single NetworkComponent.
 * 
 * @see NetworkComponentTablePanel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentTablePanelEditID extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = 1154095803299095301L;

	private JTextField jTextFieldEdit = null; 
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel jLabelDisplay = new JLabel("");
		if (value!=null) {
			jLabelDisplay.setText(value.toString());
		}
		jLabelDisplay.setFont(new Font("Dialog", Font.PLAIN, 12));
		TableCellColorHelper.setTableCellRendererColors(jLabelDisplay, row, isSelected);
		return jLabelDisplay;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.getJTextFieldEditor().getText();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.getJTextFieldEditor().setText(value.toString());
		TableCellColorHelper.setTableCellRendererColors(this.getJTextFieldEditor(), row, isSelected);
		return this.getJTextFieldEditor();
	}

	/**
	 * Gets the JTextField of the editor.
	 * @return the editor JTextField  
	 */
	private JTextField getJTextFieldEditor() {
		if (jTextFieldEdit==null) {
			jTextFieldEdit = new JTextField();
			jTextFieldEdit.setOpaque(true);
			jTextFieldEdit.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldEdit.setBackground(new Color(0,0,0,0));
		}
		return jTextFieldEdit;
	}
	
}
