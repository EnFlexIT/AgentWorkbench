package de.enflexit.common.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import de.enflexit.common.properties.Properties.PropertyType;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.common.swing.TableCellColorHelper;

/**
 * The Class PropertyValueCellEditor.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyValueCellEditor  extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;

	private PropertyValue propertyValueEdit;
	private JComponent jComponentEdit;
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		if (value==null) {
			this.fireEditingCanceled();
			return null;
		}

		this.jComponentEdit = null;
		this.propertyValueEdit = (PropertyValue) value;
		if (propertyValueEdit.getValueClass().equals(PropertyType.Boolean.name())==true) {
			this.jComponentEdit = this.getJCheckBox(this.propertyValueEdit);
		} else {
			this.jComponentEdit = this.getJTextFieldEdit(this.propertyValueEdit);
		}
		
		// ----------------------------------------------------------
		// --- Configure display component --------------------------
		// ----------------------------------------------------------
		if (AwbLookAndFeelAdjustments.isNimbusLookAndFeel()==true) {
			TableCellColorHelper.setTableCellRendererColors(this.jComponentEdit, row, isSelected);
		} else {
			if (this.jComponentEdit instanceof JCheckBox) {
				if (isSelected==true) {
					this.jComponentEdit.setBackground(table.getSelectionBackground());
				} else {
					this.jComponentEdit.setBackground(table.getBackground());
				}
			}
		}
		return this.jComponentEdit; 
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return propertyValueEdit;
	}
	
	/**
	 * Returns a JTextField for value editing.
	 *
	 * @param propertyValue the property value
	 * @return the j text field edit
	 */
	private JTextField getJTextFieldEdit(PropertyValue propertyValue) {
		
		JTextField jTextFieldEdit = new JTextField();
		jTextFieldEdit.setText(propertyValue.getValueString());
		
		switch (propertyValue.getPropertyType()) {
		case Integer:
		case Long:
			jTextFieldEdit.addKeyListener(new KeyAdapter4Numbers(false));
			break;

		case Float:
		case Double:
			jTextFieldEdit.addKeyListener(new KeyAdapter4Numbers(true));
			break;
			
		default:
			break;
		}
		
		// --- Add reaction to enter ----------------------
		jTextFieldEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JTextField textField = (JTextField) ae.getSource();
				PropertyValueCellEditor.this.propertyValueEdit.setValue(null);
				PropertyValueCellEditor.this.propertyValueEdit.setValueString(textField.getText());
				PropertyValueCellEditor.this.fireEditingStopped();
			}
		});
		
		return jTextFieldEdit;
	}
	
	/**
	 * Returns a JCheckbox for boolean values.
	 *
	 * @param isSelected the is selected
	 * @return the JCheckBox
	 */
	private JCheckBox getJCheckBox(PropertyValue propertyValue) {
		
		boolean isSelected = propertyValue.getBooleanValue();
		
		JCheckBox cBox = new JCheckBox();
		cBox.setSelected(isSelected);
		cBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JCheckBox checkBox = (JCheckBox) ae.getSource();
				PropertyValueCellEditor.this.propertyValueEdit.setValue(checkBox.isSelected());
				PropertyValueCellEditor.this.fireEditingStopped();
			}
		});
		return cBox; 
	}
	
}
