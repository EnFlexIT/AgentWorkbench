package de.enflexit.df.descriptionService.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.df.descriptionService.DescriptionsController;
import de.enflexit.df.descriptionService.db.DataColumnAlternativeID;
import de.enflexit.df.descriptionService.db.DataColumnDescription;
import de.enflexit.df.descriptionService.db.DataType;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * This panel implements the actual editor for the description of a single data column. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptionEditorColumnDetailsPanel extends JPanel implements ActionListener, DocumentListener, PropertyChangeListener {
	
	private static final long serialVersionUID = 261283042294893067L;
	
	private static final String COMBO_BOX_ENTRY_NOT_SELECTED = "--- Please Select ---";
	
	private JLabel jLabelColumnName;
	private JLabel jLabelColumnNameValue;
	private JLabel jLabelColumnNameUser;
	private JTextField jTextFieldColumnName;
	private JLabel lblNewLabel;
	private JTextField jTextFieldDescription;
	
	private DataColumnDescription dataColumnDescription;
	private JLabel jLabelDataType;
	private JComboBox<DataType> jComboBoxDataType;
	private JLabel jLabelUnit;
	private JTextField jTextFieldUnit;
	private JLabel jLabelMinValue;
	private JTextField jTextFieldMinValue;
	private JLabel jLabelMaxValue;
	private JTextField jTextFieldMaxValue;
	private JPanel jPanelButtons;
	private JButton jButtonApply;
	private JButton jButtonRevert;
	
	private boolean dirty;
	
	private ArrayList<PropertyChangeListener> changeListeners;
	private JLabel jLabelAlternateIDs;
	private AlternativeIDsEditorPanel alternativeIDsEditorPanel;
	
	public DescriptionEditorColumnDetailsPanel() {
		initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelColumnName = new GridBagConstraints();
		gbc_jLabelColumnName.anchor = GridBagConstraints.WEST;
		gbc_jLabelColumnName.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelColumnName.gridx = 0;
		gbc_jLabelColumnName.gridy = 0;
		add(getJLabelColumnName(), gbc_jLabelColumnName);
		GridBagConstraints gbc_jLabelColumnNameValue = new GridBagConstraints();
		gbc_jLabelColumnNameValue.anchor = GridBagConstraints.WEST;
		gbc_jLabelColumnNameValue.insets = new Insets(5, 5, 5, 10);
		gbc_jLabelColumnNameValue.gridx = 1;
		gbc_jLabelColumnNameValue.gridy = 0;
		add(getJLabelColumnNameValue(), gbc_jLabelColumnNameValue);
		GridBagConstraints gbc_jLabelColumnNameUser = new GridBagConstraints();
		gbc_jLabelColumnNameUser.anchor = GridBagConstraints.WEST;
		gbc_jLabelColumnNameUser.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelColumnNameUser.gridx = 0;
		gbc_jLabelColumnNameUser.gridy = 1;
		add(getJLabelColumnNameUser(), gbc_jLabelColumnNameUser);
		GridBagConstraints gbc_jTextFieldColumnName = new GridBagConstraints();
		gbc_jTextFieldColumnName.insets = new Insets(5, 5, 5, 10);
		gbc_jTextFieldColumnName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldColumnName.gridx = 1;
		gbc_jTextFieldColumnName.gridy = 1;
		add(getJTextFieldColumnName(), gbc_jTextFieldColumnName);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_jTextFieldDescription = new GridBagConstraints();
		gbc_jTextFieldDescription.insets = new Insets(5, 5, 5, 10);
		gbc_jTextFieldDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldDescription.gridx = 1;
		gbc_jTextFieldDescription.gridy = 2;
		add(getJTextFieldDescription(), gbc_jTextFieldDescription);
		GridBagConstraints gbc_jLabelDataType = new GridBagConstraints();
		gbc_jLabelDataType.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataType.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDataType.gridx = 0;
		gbc_jLabelDataType.gridy = 3;
		add(getJLabelDataType(), gbc_jLabelDataType);
		GridBagConstraints gbc_jComboBoxDataType = new GridBagConstraints();
		gbc_jComboBoxDataType.insets = new Insets(5, 5, 5, 10);
		gbc_jComboBoxDataType.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataType.gridx = 1;
		gbc_jComboBoxDataType.gridy = 3;
		add(getJComboBoxDataType(), gbc_jComboBoxDataType);
		GridBagConstraints gbc_jLabelUnit = new GridBagConstraints();
		gbc_jLabelUnit.anchor = GridBagConstraints.WEST;
		gbc_jLabelUnit.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelUnit.gridx = 0;
		gbc_jLabelUnit.gridy = 4;
		add(getJLabelUnit(), gbc_jLabelUnit);
		GridBagConstraints gbc_jTextFieldUnit = new GridBagConstraints();
		gbc_jTextFieldUnit.insets = new Insets(5, 5, 5, 10);
		gbc_jTextFieldUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldUnit.gridx = 1;
		gbc_jTextFieldUnit.gridy = 4;
		add(getJTextFieldUnit(), gbc_jTextFieldUnit);
		GridBagConstraints gbc_jLabelMinValue = new GridBagConstraints();
		gbc_jLabelMinValue.anchor = GridBagConstraints.WEST;
		gbc_jLabelMinValue.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelMinValue.gridx = 0;
		gbc_jLabelMinValue.gridy = 5;
		add(getJLabelMinValue(), gbc_jLabelMinValue);
		GridBagConstraints gbc_jTextFieldMinValue = new GridBagConstraints();
		gbc_jTextFieldMinValue.insets = new Insets(5, 5, 5, 10);
		gbc_jTextFieldMinValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldMinValue.gridx = 1;
		gbc_jTextFieldMinValue.gridy = 5;
		add(getJTextFieldMinValue(), gbc_jTextFieldMinValue);
		GridBagConstraints gbc_jLabelMaxValue = new GridBagConstraints();
		gbc_jLabelMaxValue.anchor = GridBagConstraints.WEST;
		gbc_jLabelMaxValue.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelMaxValue.gridx = 0;
		gbc_jLabelMaxValue.gridy = 6;
		add(getJLabelMaxValue(), gbc_jLabelMaxValue);
		GridBagConstraints gbc_jTextFieldMaxValue = new GridBagConstraints();
		gbc_jTextFieldMaxValue.insets = new Insets(5, 5, 5, 10);
		gbc_jTextFieldMaxValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldMaxValue.gridx = 1;
		gbc_jTextFieldMaxValue.gridy = 6;
		add(getJTextFieldMaxValue(), gbc_jTextFieldMaxValue);
		GridBagConstraints gbc_jLabelAlternateIDs = new GridBagConstraints();
		gbc_jLabelAlternateIDs.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelAlternateIDs.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelAlternateIDs.gridx = 0;
		gbc_jLabelAlternateIDs.gridy = 7;
		add(getJLabelAlternateIDs(), gbc_jLabelAlternateIDs);
		GridBagConstraints gbc_alternativeIDsEditorPanel = new GridBagConstraints();
		gbc_alternativeIDsEditorPanel.insets = new Insets(5, 5, 5, 10);
		gbc_alternativeIDsEditorPanel.fill = GridBagConstraints.BOTH;
		gbc_alternativeIDsEditorPanel.gridx = 1;
		gbc_alternativeIDsEditorPanel.gridy = 7;
		add(getAlternativeIDsEditorPanel(), gbc_alternativeIDsEditorPanel);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.gridwidth = 2;
		gbc_jPanelButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 8;
		add(getJPanelButtons(), gbc_jPanelButtons);
		
		this.setUiComponentsEnabled(false);
	}

	private JLabel getJLabelColumnName() {
		if (jLabelColumnName == null) {
			jLabelColumnName = new JLabel("Column");
			jLabelColumnName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelColumnName;
	}
	private JLabel getJLabelColumnNameValue() {
		if (jLabelColumnNameValue == null) {
			jLabelColumnNameValue = new JLabel("");
			jLabelColumnNameValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelColumnNameValue;
	}
	private JLabel getJLabelColumnNameUser() {
		if (jLabelColumnNameUser == null) {
			jLabelColumnNameUser = new JLabel("Name");
			jLabelColumnNameUser.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelColumnNameUser;
	}
	private JTextField getJTextFieldColumnName() {
		if (jTextFieldColumnName == null) {
			jTextFieldColumnName = new JTextField();
			jTextFieldColumnName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldColumnName.setColumns(10);
			jTextFieldColumnName.getDocument().addDocumentListener(this);
		}
		return jTextFieldColumnName;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Description");
			lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return lblNewLabel;
	}
	private JTextField getJTextFieldDescription() {
		if (jTextFieldDescription == null) {
			jTextFieldDescription = new JTextField();
			jTextFieldDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldDescription.setColumns(10);
			jTextFieldDescription.getDocument().addDocumentListener(this);
		}
		return jTextFieldDescription;
	}
	
	private JLabel getJLabelDataType() {
		if (jLabelDataType == null) {
			jLabelDataType = new JLabel("Data Type");
			jLabelDataType.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataType;
	}
	private JComboBox<DataType> getJComboBoxDataType() {
		if (jComboBoxDataType == null) {
			jComboBoxDataType = new JComboBox<>();
			jComboBoxDataType.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxDataType.addActionListener(this);
			
			// --- Add the data types and null as possible selection values
			jComboBoxDataType.addItem(null);
			for (DataType dataType : DataType.values()) {
				jComboBoxDataType.addItem(dataType);
			}
	
			// --- Define a custom renderer to include null with a placeholder String
			jComboBoxDataType.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					this.setText(value==null ? COMBO_BOX_ENTRY_NOT_SELECTED : value.toString());
					return this;
				}
			});
		}
		return jComboBoxDataType;
	}

	private JLabel getJLabelUnit() {
		if (jLabelUnit == null) {
			jLabelUnit = new JLabel("Unit");
			jLabelUnit.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelUnit;
	}
	private JTextField getJTextFieldUnit() {
		if (jTextFieldUnit == null) {
			jTextFieldUnit = new JTextField();
			jTextFieldUnit.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUnit.setColumns(10);
			jTextFieldUnit.getDocument().addDocumentListener(this);
		}
		return jTextFieldUnit;
	}
	private JLabel getJLabelMinValue() {
		if (jLabelMinValue == null) {
			jLabelMinValue = new JLabel("Min. Value");
			jLabelMinValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelMinValue;
	}
	private JTextField getJTextFieldMinValue() {
		if (jTextFieldMinValue == null) {
			jTextFieldMinValue = new JTextField();
			jTextFieldMinValue.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldMinValue.setColumns(10);
			jTextFieldMinValue.getDocument().addDocumentListener(this);
		}
		return jTextFieldMinValue;
	}
	private JLabel getJLabelMaxValue() {
		if (jLabelMaxValue == null) {
			jLabelMaxValue = new JLabel("Max. Value");
			jLabelMaxValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelMaxValue;
	}
	private JTextField getJTextFieldMaxValue() {
		if (jTextFieldMaxValue == null) {
			jTextFieldMaxValue = new JTextField();
			jTextFieldMaxValue.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldMaxValue.setColumns(10);
			jTextFieldMaxValue.getDocument().addDocumentListener(this);
		}
		return jTextFieldMaxValue;
	}
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
			gbc_jButtonApply.anchor = GridBagConstraints.EAST;
			gbc_jButtonApply.insets = new Insets(10, 10, 10, 10);
			gbc_jButtonApply.gridx = 0;
			gbc_jButtonApply.gridy = 0;
			jPanelButtons.add(getJButtonApply(), gbc_jButtonApply);
			GridBagConstraints gbc_jButtonRevert = new GridBagConstraints();
			gbc_jButtonRevert.insets = new Insets(10, 10, 10, 10);
			gbc_jButtonRevert.anchor = GridBagConstraints.WEST;
			gbc_jButtonRevert.gridx = 1;
			gbc_jButtonRevert.gridy = 0;
			jPanelButtons.add(getJButtonRevert(), gbc_jButtonRevert);
		}
		return jPanelButtons;
	}
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonApply.setPreferredSize(new Dimension(85, 26));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	private JButton getJButtonRevert() {
		if (jButtonRevert == null) {
			jButtonRevert = new JButton("Revert");
			jButtonRevert.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonRevert.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonRevert.setPreferredSize(new Dimension(85, 26));
			jButtonRevert.addActionListener(this);
		}
		return jButtonRevert;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonApply()) {
			this.applyChanges();
		} else if (ae.getSource()==this.getJButtonRevert()) {
			this.revertChanges();
		} else if (ae.getSource()==this.getJComboBoxDataType()) {
			// --- Mark as changed
			this.setDirty(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.setDirty(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.setDirty(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.setDirty(true);
	}

	public DataColumnDescription getDataColumnDescription() {
		return dataColumnDescription;
	}

	/**
	 * Sets the data column description.
	 *
	 * @param dataColumnDescription the new data column description
	 */
	public void setDataColumnDescription(DataColumnDescription dataColumnDescription) {
		this.dataColumnDescription = dataColumnDescription;
		this.setModelToForm();
		this.setUiComponentsEnabled(dataColumnDescription!=null);
	}

	/**
	 * Sets the current entries from the column description to editor fields.
	 */
	private void setModelToForm() {
		if (this.dataColumnDescription!=null) {
			this.getJLabelColumnNameValue().setText(this.dataColumnDescription.getColumnName());
			this.getJTextFieldColumnName().setText(this.dataColumnDescription.getName());
			this.getJTextFieldDescription().setText(this.dataColumnDescription.getDescription());
			this.getJComboBoxDataType().setSelectedItem(this.dataColumnDescription.getDataType());
			this.getJTextFieldUnit().setText(this.dataColumnDescription.getUnit());
			
			String minValText = this.dataColumnDescription.getMinValue() != null ? String.valueOf(this.dataColumnDescription.getMinValue()) : "";
			this.getJTextFieldMinValue().setText(String.valueOf(minValText));
			
			String maxValText = this.dataColumnDescription.getMaxValue() != null ? String.valueOf(this.dataColumnDescription.getMaxValue()) : ""; 
			this.getJTextFieldMaxValue().setText(maxValText);
			
			this.getAlternativeIDsEditorPanel().setDataColumnDescription(this.dataColumnDescription);
			
			this.setDirty(false);
			
		} else {
			this.getJLabelColumnNameValue().setText("");
			this.getJTextFieldColumnName().setText("");
			this.getJTextFieldDescription().setText("");
			this.getJComboBoxDataType().setSelectedItem(null);
			this.getJTextFieldUnit().setText("");
			this.getJTextFieldMinValue().setText("");
			this.getJTextFieldMaxValue().setText("");
			
			this.setDirty(false);
		}
	}

	/**
	 * Sets the entries of the column description according to the current editor field contents.
	 */
	private void setFormToModel() {
		this.dataColumnDescription.setName(this.getJTextFieldColumnName().getText());
		this.dataColumnDescription.setDescription(this.getJTextFieldDescription().getText());
		this.dataColumnDescription.setDataType((DataType) this.getJComboBoxDataType().getSelectedItem());
		this.dataColumnDescription.setUnit(this.getJTextFieldUnit().getText());
		
		if (this.getJTextFieldMinValue().getText()!=null && this.getJTextFieldMinValue().getText().isBlank()==false) {
			this.dataColumnDescription.setMinValue(this.parseDoubleValue(this.getJTextFieldMinValue().getText()));
		}
		if (this.getJTextFieldMaxValue().getText()!=null && this.getJTextFieldMaxValue().getText().isBlank()==false) {
			this.dataColumnDescription.setMaxValue(this.parseDoubleValue(this.getJTextFieldMaxValue().getText()));
		}
		
		if (this.dataColumnDescription.getAlternativeIDs()!=null) {
			this.dataColumnDescription.getAlternativeIDs().clear();
		}
		
		for (int i=0; i<this.getAlternativeIDsEditorPanel().getAlternativeIDsListModel().getSize(); i++) {
			DataColumnAlternativeID altID = this.getAlternativeIDsEditorPanel().getAlternativeIDsListModel().get(i);
			this.dataColumnDescription.getAlternativeIDs().add(altID);
		}
	}
	
	/**
	 * Parses double values form strings.
	 * @param doubleAsString the string to parse
	 * @return the double, null if parsing failed
	 */
	private Double parseDoubleValue(String doubleAsString) {
		try {
			return Double.parseDouble(doubleAsString);
		} catch(NumberFormatException nfe) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Invalid number format: " + doubleAsString);
			return null;
		}
	}
	
	/**
	 * Gets the change listeners.
	 * @return the change listeners
	 */
	private ArrayList<PropertyChangeListener> getChangeListeners() {
		if (changeListeners==null) {
			changeListeners = new ArrayList<PropertyChangeListener>();
		}
		return changeListeners;
	}
	
	/**
	 * Adds the change listener.
	 * @param propertyChangeListener the property change listener
	 */
	public void addChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.getChangeListeners().contains(propertyChangeListener)==false) {
			this.getChangeListeners().add(propertyChangeListener);
		}
	}
	
	/**
	 * Removes the change listeners.
	 * @param propertyChangeListener the property change listener
	 */
	public void removeChangeListeners(PropertyChangeListener propertyChangeListener) {
		if (this.getChangeListeners().contains(propertyChangeListener)==true) {
			this.getChangeListeners().remove(propertyChangeListener);
		}
	}
	
	/**
	 * Notify all listeners that the description was changed.
	 */
	private void notifyDescriptionChanged() {
		PropertyChangeEvent pce = new PropertyChangeEvent(this, DescriptionsController.DESCRIPTION_ADDED_OR_UPDATED, null, this.getDataColumnDescription());
		for (PropertyChangeListener listener : this.getChangeListeners()) {
			listener.propertyChange(pce);
		}
	}
	
	/**
	 * Applies the changes to the model and stores to the DB.
	 */
	public void applyChanges() {
		// --- Apply the changes to the original item and notify listeners
		this.setFormToModel();
		this.notifyDescriptionChanged();
		this.setDirty(false);
	}
	
	/**
	 * Replaces the form content with the original model.
	 */
	public void revertChanges() {
		// --- Replace form contents with the original item
		this.setModelToForm();
		this.setDirty(false);
	}

	/**
	 * Checks if the editor has unsaved changes.
	 * @return true, if is dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	private void setDirty(boolean dirty) {
		this.dirty = dirty;
		this.getJButtonApply().setEnabled(dirty);
		this.getJButtonRevert().setEnabled(dirty);
	}

	
	private JLabel getJLabelAlternateIDs() {
		if (jLabelAlternateIDs == null) {
			jLabelAlternateIDs = new JLabel("Alternative IDs");
			jLabelAlternateIDs.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelAlternateIDs;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getSource()==this.getAlternativeIDsEditorPanel()) {
			// --- Changes to the alternative IDs
			this.setDirty(true);
		}
	}
	
	private AlternativeIDsEditorPanel getAlternativeIDsEditorPanel() {
		if (alternativeIDsEditorPanel == null) {
			alternativeIDsEditorPanel = new AlternativeIDsEditorPanel();
			alternativeIDsEditorPanel.addChangeListener(this);
		}
		return alternativeIDsEditorPanel;
	}
	
	/**
	 * Sets the ui components enabled.
	 * @param enabled the new ui components enabled
	 */
	private void setUiComponentsEnabled(boolean enabled) {
		this.getJTextFieldColumnName().setEnabled(enabled);
		this.getJTextFieldDescription().setEnabled(enabled);
		this.getJComboBoxDataType().setEnabled(enabled);
		this.getJTextFieldUnit().setEnabled(enabled);
		this.getJTextFieldMinValue().setEnabled(enabled);
		this.getJTextFieldMaxValue().setEnabled(enabled);
		this.getAlternativeIDsEditorPanel().setUiComponentsEnabled(enabled);
		this.getJButtonApply().setEnabled(enabled==false ? enabled : this.isDirty());
		this.getJButtonRevert().setEnabled(enabled==false ? enabled : this.isDirty());
	}
}
