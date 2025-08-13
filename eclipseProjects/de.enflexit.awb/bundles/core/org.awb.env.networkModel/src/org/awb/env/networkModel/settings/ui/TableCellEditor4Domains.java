package org.awb.env.networkModel.settings.ui;

import java.awt.Component;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.BadLocationException;

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
		
		JTextField jTextField = new JTextField(this.domainName);
        jTextField.setBorder(BorderFactory.createEmptyBorder());
        jTextField.getDocument().addDocumentListener(this.getTextFieldDocumentListener());
        editComponent = jTextField;
		
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
				this.setNewDocValue(de);
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
