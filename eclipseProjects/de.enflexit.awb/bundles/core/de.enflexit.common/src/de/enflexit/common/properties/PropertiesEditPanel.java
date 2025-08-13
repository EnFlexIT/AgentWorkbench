package de.enflexit.common.properties;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.common.properties.Properties.PropertyType;
import de.enflexit.common.properties.PropertiesEvent.Action;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * The Class PropertiesEditPanel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesEditPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 2631495936975175409L;

	private Properties properties;
	private String identifier;
	private Action currAction;
	
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
	
	private ArrayList<String> readOnlyProperties;
	
	
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
	
	/**
	 * Sets the current Properties instance to this panel.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * Return the current properties.
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
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
		gbc_jLabelProperty.insets = new Insets(5, 0, 5, 0);
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
		int newWidth = tbColumnWidth[0] - this.getJLabelProperty().getWidth() - 5;
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
	 * Sets the current identifier to edit.
	 * @param identifier the new identifier
	 */
	public void setIdentifier(String identifier) {

		this.identifier = identifier;
		
		boolean isKnownIdentifier = this.getProperties()!=null ?  this.getProperties().contains(this.identifier) : false;
		this.currAction = isKnownIdentifier ? Action.PropertyUpdate : Action.PropertyAdded;
		this.getJButtonOk().setText(isKnownIdentifier==true ? "Update" : "Add");
		
		// --- Set to UI ----------------------------------
		this.getJTextFieldName().setText(this.identifier);
		PropertyValue pValue = this.getProperties()!=null ? this.getProperties().getPropertyValue(this.identifier) : null; 
		this.setPropertyValue(pValue);
		
		// --- Check if the property is read only ---------
		if (this.isReadOnlyProperty(identifier)==true) {
			this.setEditorComponentsEnabled(false);
		} else {
			this.setEditorComponentsEnabled(true);
		}
		
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
			if (value==null) value=0;
			break;

		case Float:
		case Double:
			this.getJTextFieldValue().addKeyListener(this.getKeyAdapter4NumbersDecimal());
			if (value==null) value=0.0;
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
			jButtonOk.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
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
			this.doAddOrUpdateAction();
		}
	}
	
	/**
	 * Do add or update action.
	 */
	private void doAddOrUpdateAction() {
		
		// --- Collect and check settings -----------------
		String identifier = this.getJTextFieldName().getText().trim();
		PropertyType propertyType = (PropertyType) this.getJComboBoxType().getSelectedItem();
		String propertyValueString = this.getJTextFieldValue().getText();
		if (propertyType==PropertyType.Boolean) {
			propertyValueString = Boolean.toString(this.getJCheckBoxValue().isSelected());
		}
		
		// --- Check for proper current settings ----------
		PropertyValue pValue = this.hasErrors(identifier, propertyType, propertyValueString); 
		if (pValue==null) return;
		
		// --- Check type of action -----------------------
		if (this.currAction==Action.PropertyAdded) {
			// --- Add Action -----------------------------
			boolean knownProperty = this.getProperties().contains(identifier);
			if (knownProperty==true) {
				String title = "Overwrite exiting property?";
				String msg   = "The property '" + identifier + "' already exists. Overwrite this property?";
				boolean addOrOverwrite = (JOptionPane.showConfirmDialog(this.getParent(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION);
				if (addOrOverwrite==true) {
					this.getProperties().setValue(identifier, pValue);
				}
			} else {
				this.getProperties().setValue(identifier, pValue);
			}
			
		} else if (this.currAction==Action.PropertyUpdate) {
			// --- Update Action --------------------------
			boolean renamedProperty  = identifier.equals(this.identifier)==false;
			if (renamedProperty==true) {
				String title = "Rename property '" + this.identifier + "'?";
				String msg   = "Should the property '" + this.identifier + "' be renamed to '" + identifier + "'?";
				boolean isUserConfirmed = (JOptionPane.showConfirmDialog(this.getParent(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION);
				if (isUserConfirmed) {
					this.getProperties().remove(this.identifier);
					this.getProperties().setValue(identifier, pValue);
				}
			} else {
				this.getProperties().setValue(identifier, pValue);
			}
			
		}

	}
	/**
	 * Checks for errors in the current settings.
	 *
	 * @param identifier the identifier
	 * @param propertyType the property type
	 * @param propertyValueString the property value string
	 * @return true, if successful
	 */
	private PropertyValue hasErrors(String identifier, PropertyType propertyType, String propertyValueString) {
		
		String title = null; 
		String msg = null;

		// --- Check identifier ---------------------------
		if (identifier==null || identifier.isBlank()==true) {
			title = "Missing Identifier!";
			msg = "The identfier of a property is not allowed to be null!";
			JOptionPane.showMessageDialog(this.getParent(), msg, title, JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		// --- Check value according to type -------------- 
		PropertyValue pValue = new PropertyValue();
		pValue.setValueClass(propertyType.name());
		pValue.setValueString(propertyValueString);
		
		// --- Try to get value instance ------------------
		pValue.getValue(false);
		if (pValue.getErrorMessage()!=null) {
			title = "Error proceeding value!";
			msg = pValue.getErrorMessage();
			JOptionPane.showMessageDialog(this.getParent(), msg, title, JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return pValue;
	}

	/**
	 * Gets the list of read only properties.
	 * @return the read only properties
	 */
	private ArrayList<String> getReadOnlyProperties() {
		if (readOnlyProperties==null) {
			readOnlyProperties = new ArrayList<>();
		}
		return readOnlyProperties;
	}
	
	/**
	 * Adds a property to the read only list, editor components will be disabled for that property then.
	 * @param propertyKey the property key
	 */
	public void addReadOnlyProperty(String propertyKey) {
		this.getReadOnlyProperties().add(propertyKey);
	}
	
	/**
	 * Removes a property from the read only list.
	 * @param propertyKey the property key
	 */
	public void removeReadOnyProperty(String propertyKey) {
		this.getReadOnlyProperties().remove(propertyKey);
	}
	
	/**
	 * Adds several properties to the read only list, editor components will be disabled for these properties then.
	 * @param propertyKeys the property keys
	 */
	public void addReadOnlyProperties(List<String> propertyKeys) {
		this.getReadOnlyProperties().addAll(propertyKeys);
	}
	
	/**
	 * Checks if a property is configured to be read only..
	 * @param propertyKey the property key
	 * @return true, if is read only property
	 */
	public boolean isReadOnlyProperty(String propertyKey) {
		return this.getReadOnlyProperties().contains(propertyKey);
	}
	
	/**
	 * Enables or disables the guy components for modifying the current property.
	 * @param enabled the new editor components enabled
	 */
	private void setEditorComponentsEnabled(boolean enabled) {
		this.getJTextFieldName().setEnabled(enabled);
		this.getJComboBoxType().setEnabled(enabled);
		this.getJTextFieldValue().setEnabled(enabled);
		this.getJCheckBoxValue().setEnabled(enabled);
		this.getJButtonOk().setEnabled(enabled);
		
		this.getJButtonOk().setToolTipText((enabled==true) ? null : "This property is can't be modified.");
	}
	
}
