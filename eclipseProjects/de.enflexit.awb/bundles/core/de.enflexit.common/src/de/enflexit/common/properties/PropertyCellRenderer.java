package de.enflexit.common.properties;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.common.properties.Properties.PropertyType;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.common.swing.TableCellColorHelper;

/**
 * The Class PropertyCellRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		String displayInstruction = (String) table.getValueAt(row, 0);
		String propertyName = "";
		
		PropertyValue pv = null;
		String propertyType = "";
		String propertyValue = "";
		
		// ----------------------------------------------------------
		// --- Work on possible cell values -------------------------
		// ----------------------------------------------------------
		if (value instanceof String) {
			// --- Work on the property name ------------------------ 
			propertyName = (String) value;
			
		} else if (value instanceof PropertyValue) {
			// --- Work on the provided PropertyValue ---------------
			pv = (PropertyValue) value;
			propertyType = this.getPropertyTypeAsString(pv);
			propertyValue = pv.getValueString();
		}
		
		// ----------------------------------------------------------
		// --- Get the JLabel to set the text in --------------------
		// ----------------------------------------------------------
		JLabel jLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JCheckBox jCheckBox = null;
		
		// ----------------------------------------------------------
		// --- Set text for JLabel ----------------------------------
		// ----------------------------------------------------------
		String displayText = "";
		switch (column) {
		case PropertiesPanel.COLUMN_DisplayInstruction:
			displayText = propertyName;
			break;
			
		case PropertiesPanel.COLUMN_PropertyName:
			displayText = propertyName;
			if (displayInstruction!=null) {
				String[] diArray = displayInstruction.split("\\.");
				int treeLevel = diArray.length-1;
				displayText = diArray[treeLevel];
				jLabel.setBorder(BorderFactory.createEmptyBorder(0, treeLevel*20, 0, 0));
			}
			break;
			
		case PropertiesPanel.COLUMN_PropertyType:
			displayText = propertyType;
			break;

		case PropertiesPanel.COLUMN_PropertyValue:
			displayText = propertyValue;
			// ------------------------------------------------------
			// --- Exchange display component by a JCheckbox? -------
			// ------------------------------------------------------
			if (pv!=null && pv.getPropertyType()==PropertyType.Boolean) {
				jCheckBox = this.getJCheckBox(pv.getBooleanValue());
			}
			break;
		}
		jLabel.setText(displayText);
		
		// ----------------------------------------------------------
		// --- Configure display component --------------------------
		// ----------------------------------------------------------
		JComponent displayComponent = jCheckBox==null ? jLabel : jCheckBox;
		if (AwbLookAndFeelAdjustments.isNimbusLookAndFeel()==true) {
			TableCellColorHelper.setTableCellRendererColors(displayComponent, row, isSelected);
		} else if (jCheckBox!=null) {
			if (isSelected==true) {
				jCheckBox.setBackground(table.getSelectionBackground());
			} else {
				jCheckBox.setBackground(table.getBackground());
			}
		}
		
		return displayComponent; 
	}
	
	/**
	 * Returns the property type as string.
	 *
	 * @param propertyValue the property value
	 * @return the property type as string
	 */
	private String getPropertyTypeAsString(PropertyValue propertyValue) {
		
		String propTypeString = "";
		if (propertyValue!=null) {
			PropertyType pType = propertyValue.getPropertyType();
			if (pType!=null) {
				propTypeString = pType.name();
			} else {
				propTypeString = propertyValue.getValueClass();
			}
		}
		return propTypeString;
	}

	/**
	 * Returns a JCheckbox for boolean values.
	 *
	 * @param isSelected the is selected
	 * @return the JCheckBox
	 */
	private JCheckBox getJCheckBox(boolean isSelected) {
		JCheckBox cBox = new JCheckBox();
		cBox.setSelected(isSelected);
		return cBox; 
	}
	
	
}
