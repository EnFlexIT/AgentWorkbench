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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;

/**
 * This class is used in the {@link ComponentTypeDialog} for showing the agent class selector dialog 
 * for the agent column in the JTable
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TableCellEditor4Domains extends AbstractCellEditor implements TableCellEditor, ActionListener {
	
	private static final long serialVersionUID = -1937780991527069423L;
	private static final String EDIT = "edit";
	
	private ComponentTypeDialog ctsDialog = null;
	
	private JTextField textField 	= new JTextField();
	private JLabel labelField 		= new JLabel();
	private String domainName;
	
	/**
	 * Default constructor
	 */
	public TableCellEditor4Domains(ComponentTypeDialog ctsDialog) {
		this.ctsDialog = ctsDialog;
		// --- edit appearance of the text field ----------
		textField.setActionCommand(EDIT);
        textField.addActionListener(this);
        textField.setBorder(BorderFactory.createEmptyBorder());
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return domainName;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand().equals(EDIT )) {
			String oldValue = domainName;
			domainName = textField.getText();
			// --- Make the renderer reappear -------------. 
            fireEditingStopped();
            // --- Apply changes to the display elements 
			if (oldValue!=null) {
				if (oldValue.equals(domainName)==false) {
					// --- Revise the current ComponentTypeSettings -----
					ctsDialog.renameDomainInComponents(oldValue, domainName);
				}
			}
			ctsDialog.setTableCellEditor4DomainsInComponents();
        }
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// This method is called when a cell value is edited by the user.
		domainName = (String) value;
		if (domainName==null) {
			textField.setText(domainName);
			return textField;
		} else if (domainName.equals(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME)==true) {
			labelField.setText(domainName);
			return labelField;
		} else {
			textField.setText(domainName);
			return textField;
		}
	}

	
	
}
