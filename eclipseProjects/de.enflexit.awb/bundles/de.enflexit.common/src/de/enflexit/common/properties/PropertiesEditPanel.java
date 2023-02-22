package de.enflexit.common.properties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.common.properties.Properties.PropertyType;
import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * The Class PropertiesEditPanel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesEditPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 2631495936975175409L;

	public enum Action {
		Edit,
		Add
	}
	
	private Properties properties;
	private String identifier;
	
	private JLabel jLabelProperty;

	private JTextField jTextFieldName;
	private DefaultComboBoxModel<PropertyType> comboBoxModel;
	private JComboBox<PropertyType> jComboBoxType;
	
	private JTextField jTextFieldValue;
	private KeyAdapter4Numbers keyAdapter4NumbersDecimal;
	private KeyAdapter4Numbers keyAdapter4NumbersInteger;
	private JCheckBox jCheckBoxValue;

	private JComponent jComponentValueEdit;
	
	private JButton jButtonOk;
	
	private int[] tbColumnWidth;
	
	
	/**
	 * Instantiates a new properties edit panel.
	 */
	public PropertiesEditPanel() {
		this(null);
	}
	/**
	 * Instantiates a new properties edit panel.
	 * @param properties the properties to work on
	 */
	public PropertiesEditPanel(Properties properties) {
		this.setProperties(properties);
		this.initialize();
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public Properties getProperties() {
		return properties;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
		
		// --- Set to UI ----------------------------------
		PropertyValue pValue = this.getProperties().getPropertyValue(this.identifier); 
		this.getJTextFieldName().setText(this.identifier);
		this.setPropertyValue(pValue);
		this.getJButtonOk().setText(pValue==null ? "Add" : "Update");
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelProperty = new GridBagConstraints();
		gbc_jLabelProperty.anchor = GridBagConstraints.WEST;
		gbc_jLabelProperty.insets = new Insets(5, 5, 5, 0);
		gbc_jLabelProperty.gridx = 0;
		gbc_jLabelProperty.gridy = 0;
		this.add(this.getJLabelProperty(), gbc_jLabelProperty);
		
		GridBagConstraints gbc_jTextFieldName = new GridBagConstraints();
		gbc_jTextFieldName.insets = new Insets(5, 5, 5, 0);
		gbc_jTextFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldName.gridx = 1;
		gbc_jTextFieldName.gridy = 0;
		this.add(this.getJTextFieldName(), gbc_jTextFieldName);
		
		GridBagConstraints gbc_jComboBoxType = new GridBagConstraints();
		gbc_jComboBoxType.insets = new Insets(5, 5, 5, 0);
		gbc_jComboBoxType.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxType.gridx = 2;
		gbc_jComboBoxType.gridy = 0;
		this.add(this.getJComboBoxType(), gbc_jComboBoxType);
		
		GridBagConstraints gbc_jTextFieldValue = new GridBagConstraints();
		gbc_jTextFieldValue.insets = new Insets(5, 5, 5, 0);
		gbc_jTextFieldValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldValue.gridx = 3;
		gbc_jTextFieldValue.gridy = 0;
		this.add(this.getJTextFieldValue(), gbc_jTextFieldValue);
		
		GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
		gbc_jButtonOk.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButtonOk.insets = new Insets(5, 5, 5, 0);
		gbc_jButtonOk.gridx = 4;
		gbc_jButtonOk.gridy = 0;
		this.add(this.getJButtonOk(), gbc_jButtonOk);
		
		this.jComponentValueEdit = this.getJTextFieldValue();
	}
	
	/**
	 * Updates the column width according to the specified int array.
	 * @param tbColumnWidth the current table column width
	 */
	public void updateColumnWidth(int[] tbColumnWidth) {
		
		if (tbColumnWidth==null) return;
		if (this.tbColumnWidth!=null && this.tbColumnWidth[0]==tbColumnWidth[0]) return;
		
		this.tbColumnWidth = tbColumnWidth;
		
		// --- Column property name -------------
		int newWidth = tbColumnWidth[0] - this.getJLabelProperty().getWidth() - 10;
		if (newWidth>=100) {
			int currHeight = this.getJTextFieldName().getHeight();
			this.setComponentSize(this.getJTextFieldName(), new Dimension(newWidth, currHeight));
		}

		// --- Column property type -------------
		newWidth = tbColumnWidth[1] - 10;
		int currHeight = this.getJComboBoxType().getHeight();
		this.setComponentSize(this.getJComboBoxType(), new Dimension(newWidth, currHeight));
		
		this.validate();
		this.repaint();
	}
	/**
	 * Nails the component size.
	 *
	 * @param comp the component to resize
	 * @param newSize the new size
	 */
	private void setComponentSize(JComponent comp, Dimension newSize) {
		comp.setMinimumSize(newSize);
		comp.setPreferredSize(newSize);
		comp.setSize(newSize);
	}
	
	
	private JLabel getJLabelProperty() {
		if (jLabelProperty == null) {
			jLabelProperty = new JLabel("Property:");
			jLabelProperty.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelProperty;
	}
	
	private JTextField getJTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setToolTipText("Property-Name");
			jTextFieldName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldName.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldName;
	}
	
	public DefaultComboBoxModel<PropertyType> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>(PropertyType.values());
		}
		return comboBoxModel;
	}
	private JComboBox<PropertyType> getJComboBoxType() {
		if (jComboBoxType == null) {
			jComboBoxType = new JComboBox<>(this.getComboBoxModel());
			jComboBoxType.setToolTipText("Property-Type");
			jComboBoxType.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxType.setPreferredSize(new Dimension(80, 26));
			jComboBoxType.addActionListener(this);
		}
		return jComboBoxType;
	}

	
	/**
	 * Sets the current property value to the UI.
	 * @param propertyValue the new property value
	 */
	private void setPropertyValue(PropertyValue propertyValue) {
		if (propertyValue==null) {
			this.setPropertyType(null, null);
		} else {
			this.setPropertyType(propertyValue.getPropertyType(), propertyValue.getValue());
		}
	}
	/**
	 * Sets the current property type and will adjust visualization accordingly.
	 *
	 * @param propertyType the new property type
	 * @param value the value
	 */
	private void setPropertyType(PropertyType propertyType, Object value) {
		
		PropertyType pt = propertyType==null ?  PropertyType.String : propertyType;
		this.getJComboBoxType().setSelectedItem(pt);
		
		// --- Reset all previous settings --------------------------
		this.getJTextFieldValue().removeKeyListener(this.getKeyAdapter4NumbersInteger());
		this.getJTextFieldValue().removeKeyListener(this.getKeyAdapter4NumbersDecimal());
		
		// --- Prepare for editing ----------------------------------
		JComponent newDisplayComponent = this.getJTextFieldValue();
		switch (pt) {
		case String:
			break;
		
		case Boolean:
			newDisplayComponent = this.getJCheckBoxValue();
			break;
		
		case Integer:
		case Long:
			this.getJTextFieldValue().addKeyListener(this.getKeyAdapter4NumbersInteger());
			break;

		case Float:
		case Double:
			this.getJTextFieldValue().addKeyListener(this.getKeyAdapter4NumbersDecimal());
			break;
		}
 
		// --- Exchange component for value editing ? --------------- 
		if (newDisplayComponent!=this.jComponentValueEdit) {
			GridBagLayout gbLayout = (GridBagLayout) this.getLayout();
			GridBagConstraints gbc = gbLayout.getConstraints(this.jComponentValueEdit);
			this.remove(this.jComponentValueEdit);
			this.add(newDisplayComponent, gbc);
			this.jComponentValueEdit = newDisplayComponent;
			this.validate();
			this.repaint();
		}
		
		// --- Set value to UI --------------------------------------
		if (pt==PropertyType.Boolean) {
			this.getJCheckBoxValue().setSelected(value==null ? false : (boolean)value);
		} else {
			this.getJTextFieldValue().setText(value==null ? null : value.toString());
		}
		
	}
	
	private JTextField getJTextFieldValue() {
		if (jTextFieldValue == null) {
			jTextFieldValue = new JTextField();
			jTextFieldValue.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldValue.setToolTipText("Property-Value");
			jTextFieldValue.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldValue;
	}
	
	private JCheckBox getJCheckBoxValue() {
		if (jCheckBoxValue==null) {
			jCheckBoxValue = new JCheckBox();
			jCheckBoxValue.setToolTipText("Property-Value");
			jCheckBoxValue.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jCheckBoxValue;
	}
	
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("Update");
			jButtonOk.setForeground(new Color(0, 128, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setPreferredSize(new Dimension(185, 26));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}

	public KeyAdapter4Numbers getKeyAdapter4NumbersInteger() {
		if (keyAdapter4NumbersInteger==null) {
			keyAdapter4NumbersInteger = new KeyAdapter4Numbers(false);
		}
		return keyAdapter4NumbersInteger;
	}
	public KeyAdapter4Numbers getKeyAdapter4NumbersDecimal() {
		if (keyAdapter4NumbersDecimal==null) {
			keyAdapter4NumbersDecimal = new KeyAdapter4Numbers(true);
		}
		return keyAdapter4NumbersDecimal;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJComboBoxType()) {
			// --- Update view for selected data type ---------------
			this.setPropertyType((PropertyType) this.getJComboBoxType().getSelectedItem(), null);
			
		} else if (ae.getSource()==this.getJButtonOk()) {
			// --- Update or add action -----------------------------
			
			
		}
		
	}
	
}
