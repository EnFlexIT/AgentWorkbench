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
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.BadLocationException;

import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;

/**
 * This class is used in the {@link ComponentTypeDialog} for showing the agent class selector dialog 
 * for the agent column in the JTable
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TableCellEditor4Domains extends AbstractCellEditor implements TableCellEditor {
	
	private static final long serialVersionUID = -1937780991527069423L;
	
	private ComponentTypeDialog ctsDialog = null;
	private String domainName;
	private Vector<String> domainVector; 
	
	/**
	 * Default constructor.
	 * @param ctsDialog the ComponentTypeDialog dialog
	 */
	public TableCellEditor4Domains(ComponentTypeDialog ctsDialog) {
		this.ctsDialog = ctsDialog;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return domainName;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		Component editComponent = null;
		
		this.domainVector = ctsDialog.getDomainVector();
		this.domainName = (String) value;
		if (this.domainName!=null && this.domainName.equals(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME)==true) {
			JLabel jLabel = new JLabel(this.domainName);
			editComponent = jLabel;
			
		} else {
			JTextField jTextField = new JTextField(this.domainName);
	        jTextField.setBorder(BorderFactory.createEmptyBorder());
	        jTextField.getDocument().addDocumentListener(this.getTextFieldDocumentListener());
	        editComponent = jTextField;
	        
		}
		return editComponent;
	}
	
	/**
	 * Gets the text field document listener.
	 * @return the text field document listener
	 */
	private DocumentListener getTextFieldDocumentListener() {
		
		DocumentListener docListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent de) {
				this.setNewDocValue(de);
			}
			@Override
			public void insertUpdate(DocumentEvent de) {
				this.setNewDocValue(de);
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
//				this.setNewDocValue(de);
			}
			private void setNewDocValue(DocumentEvent de) {
				// --- Get old and new value for domain name --------
				String oldValue = domainName;
				try {
					domainName = de.getDocument().getText(0, de.getDocument().getLength());
				} catch (BadLocationException ble) {
					ble.printStackTrace();
					domainName = null;
				}
			    // --- Apply changes to the display elements --------
				if (oldValue!=null) {
					if (oldValue.equals(domainName)==false) {
						// --- Revise the ComponentTypeSettings -----
						ctsDialog.renameDomainInComponents(oldValue, domainName);
					}
				}
				// --- Exchange name in the domain vector ----------- 
				int pos = domainVector.indexOf(oldValue);
				if (pos>=0) {
					domainVector.set(pos, domainName);
				}
				// --- Update JComboBox for the domain selection ----
				ctsDialog.setTableCellEditor4DomainsInComponents(domainVector);
			}
		};
		return docListener;
	}

	
}
