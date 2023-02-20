package de.enflexit.common.properties;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.common.properties.Properties.PropertyType;

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
		
		String displayText = "";
		
		String displayInstruction = (String) table.getValueAt(row, 0);
		
		String propertyName = "";
		String propertyType = "";
		String propertyValue = "";
		
		if (value instanceof String) {
			// --- Work on the property name ------------------------ 
			propertyName = (String) value;
			
		} else if (value instanceof PropertyValue) {
			// --- Work on the provided PropertyValue ---------------
			PropertyValue pv = (PropertyValue) value;
			propertyType = this.getPropertyTypeAsString(pv);
			propertyValue = pv.getValueString();
		}
		
		// --- Get the JLabel to set the text with ------------------
		JLabel jLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// --- Set text for JLabel ----------------------------------
		switch (column) {
		case PropertiesPanel.COLUMN_DisplayInstruction:
			displayText = propertyName;
			break;
			
		case PropertiesPanel.COLUMN_PropertyName:
			displayText = propertyName;
			if (displayInstruction!=null) {
				String[] diArray = displayInstruction.split(";");
				displayText = diArray[0];
				int treeLevel = Integer.parseInt(diArray[1]);
				jLabel.setBorder(BorderFactory.createEmptyBorder(0, treeLevel*20, 0, 0));
			}
			break;
			
		case PropertiesPanel.COLUMN_PropertyType:
			displayText = propertyType;
			break;

		case PropertiesPanel.COLUMN_PropertyValue:
			displayText = propertyValue;
			break;
		}
		jLabel.setText(displayText);
		
		// --- Set font style to BOLD -------------------------------
		//this.setJLabelFontBold(jLabel);
		
		return jLabel; 
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
	 * Sets the specified JLabels font to bold.
	 * @param jLabel the new j label font bold
	 */
	private void setJLabelFontBold(JLabel jLabel) {
		jLabel.setFont(jLabel.getFont().deriveFont(Font.BOLD));
	}
	
}
