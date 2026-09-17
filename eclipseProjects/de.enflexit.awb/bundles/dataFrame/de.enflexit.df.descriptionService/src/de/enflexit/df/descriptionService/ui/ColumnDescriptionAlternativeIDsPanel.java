package de.enflexit.df.descriptionService.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.descriptionService.db.DataColumnAlternativeID;
import de.enflexit.df.descriptionService.db.DataColumnDescription;

import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 * A sub-panel for handling alternative identifiers.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnDescriptionAlternativeIDsPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	public static final String PROPERTY_CHANGE_ALT_ID_ADDED = "AlternativeIdentifierAdded";
	public static final String PROPERTY_CHANGE_ALT_ID_EDITED = "AlternativeIdentifierEdited";
	public static final String PROPERTY_CHANGE_ALT_ID_REMOVED = "AlternativeIdentifierRemoved";
	
	private static final long serialVersionUID = 460837079926113493L;
	
	private static final Dimension BUTTON_SIZE = new Dimension(26, 26);
	
	private DataColumnDescription dataColumnDescription;
	
	private JButton jButtonAdd;
	private JButton jButtonRemove;
	private JButton jButtonEdit;
	private JScrollPane jScrollPaneAlternateIDs;
	private JList<DataColumnAlternativeID> jListAlternateIDs;
	private DefaultListModel<DataColumnAlternativeID> alternativeIDsListModel;
	
	private ArrayList<PropertyChangeListener> changeListeners; 
	
	/**
	 * Instantiates a new alternative IDs editor panel.
	 */
	public ColumnDescriptionAlternativeIDsPanel() {
		initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jButtonAdd = new GridBagConstraints();
		gbc_jButtonAdd.insets = new Insets(5, 5, 5, 5);
		gbc_jButtonAdd.gridx = 0;
		gbc_jButtonAdd.gridy = 0;
		add(getJButtonAdd(), gbc_jButtonAdd);
		GridBagConstraints gbc_jButtonRemove = new GridBagConstraints();
		gbc_jButtonRemove.insets = new Insets(5, 5, 5, 5);
		gbc_jButtonRemove.gridx = 1;
		gbc_jButtonRemove.gridy = 0;
		add(getJButtonRemove(), gbc_jButtonRemove);
		GridBagConstraints gbc_jButtonEdit = new GridBagConstraints();
		gbc_jButtonEdit.insets = new Insets(5, 5, 5, 0);
		gbc_jButtonEdit.anchor = GridBagConstraints.WEST;
		gbc_jButtonEdit.gridx = 2;
		gbc_jButtonEdit.gridy = 0;
		add(getJButtonEdit(), gbc_jButtonEdit);
		GridBagConstraints gbc_jScrollPaneAlternateIDs = new GridBagConstraints();
		gbc_jScrollPaneAlternateIDs.gridwidth = 3;
		gbc_jScrollPaneAlternateIDs.insets = new Insets(5, 5, 5, 10);
		gbc_jScrollPaneAlternateIDs.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneAlternateIDs.gridx = 0;
		gbc_jScrollPaneAlternateIDs.gridy = 1;
		add(getJScrollPaneAlternateIDs(), gbc_jScrollPaneAlternateIDs);
	}

	/**
	 * Gets the j button add.
	 * @return the j button add
	 */
	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new JButton();
			jButtonAdd.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAdd.setToolTipText("Add a category");
			jButtonAdd.setSize(BUTTON_SIZE);
			jButtonAdd.setPreferredSize(BUTTON_SIZE);
			jButtonAdd.setMinimumSize(BUTTON_SIZE);
			jButtonAdd.setMaximumSize(BUTTON_SIZE);
			jButtonAdd.addActionListener(this);
		}
		return jButtonAdd;
	}
	
	/**
	 * Gets the j button remove.
	 * @return the j button remove
	 */
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton();
			jButtonRemove.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemove.setToolTipText("Remove a category");
			jButtonRemove.setSize(BUTTON_SIZE);
			jButtonRemove.setPreferredSize(BUTTON_SIZE);
			jButtonRemove.setMinimumSize(BUTTON_SIZE);
			jButtonRemove.setMaximumSize(BUTTON_SIZE);
			jButtonRemove.addActionListener(this);
		}
		return jButtonRemove;
	}
	
	/**
	 * Gets the j button edit.
	 * @return the j button edit
	 */
	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setIcon(BundleHelper.getImageIcon("Edit.png"));
			jButtonEdit.setToolTipText("Edit a category");
			jButtonEdit.setSize(BUTTON_SIZE);
			jButtonEdit.setPreferredSize(BUTTON_SIZE);
			jButtonEdit.setMinimumSize(BUTTON_SIZE);
			jButtonEdit.setMaximumSize(BUTTON_SIZE);
			jButtonEdit.addActionListener(this);
		}
		return jButtonEdit;
	}
	
	/**
	 * Gets the j scroll pane alternate IDs.
	 * @return the j scroll pane alternate IDs
	 */
	private JScrollPane getJScrollPaneAlternateIDs() {
		if (jScrollPaneAlternateIDs == null) {
			jScrollPaneAlternateIDs = new JScrollPane();
			jScrollPaneAlternateIDs.setViewportView(getJListAlternateIDs());
			jScrollPaneAlternateIDs.setPreferredSize(new Dimension(200, 100));
		}
		return jScrollPaneAlternateIDs;
	}
	
	/**
	 * Gets the j list alternate IDs.
	 * @return the j list alternate IDs
	 */
	private JList<DataColumnAlternativeID> getJListAlternateIDs() {
		if (jListAlternateIDs == null) {
			jListAlternateIDs = new JList<>();
			jListAlternateIDs.setCellRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					DataColumnAlternativeID altID = (DataColumnAlternativeID) value;
					this.setText(altID.getAlternativeIdentifier());
					return this;
				}
			});
			jListAlternateIDs.addListSelectionListener(this);
		}
		return jListAlternateIDs;
	}
	
	/**
	 * Gets the alternative IDs list model.
	 * @return the alternative IDs list model
	 */
	public DefaultListModel<DataColumnAlternativeID> getAlternativeIDsListModel() {
		if (alternativeIDsListModel==null) {
			alternativeIDsListModel = new DefaultListModel<DataColumnAlternativeID>();
			
			if (this.dataColumnDescription!=null && this.dataColumnDescription.getAlternativeIDs()!=null) {
				for (DataColumnAlternativeID altID : this.dataColumnDescription.getAlternativeIDs()) {
					alternativeIDsListModel.addElement(altID);
				}
			}
		}
		return alternativeIDsListModel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAdd()) {
			String newIdentifier = JOptionPane.showInputDialog(this, "Add a new identifier");
			if (this.findIdentifier(newIdentifier)==null) {
				
				DataColumnAlternativeID newAltID = new DataColumnAlternativeID();
				newAltID.setAlternativeIdentifier(newIdentifier);
				newAltID.setDataColumn(this.dataColumnDescription);
				this.getAlternativeIDsListModel().addElement(newAltID);
				this.notifyChanged(PROPERTY_CHANGE_ALT_ID_ADDED, newAltID);
			} else {
				JOptionPane.showMessageDialog(this, "Identifier already in use, lease choose a different one!", "Identifier already in use!", JOptionPane.ERROR_MESSAGE);
			}
		} else if (ae.getSource()==this.getJButtonEdit()) {
			DataColumnAlternativeID selectedIdentifier = this.getJListAlternateIDs().getSelectedValue();
			String changedIdentifier = JOptionPane.showInputDialog(this, "Add a new identifier");
			if (changedIdentifier.equals(selectedIdentifier.getAlternativeIdentifier())) {
				return;		// Unchanged
			}
			
			if (this.findIdentifier(changedIdentifier)==null) {
				// --- No collision -> set the new identifier
				selectedIdentifier.setAlternativeIdentifier(changedIdentifier);
				this.notifyChanged(PROPERTY_CHANGE_ALT_ID_EDITED, selectedIdentifier);
			} else {
				JOptionPane.showMessageDialog(this, "Identifier already in use, lease choose a different one!", "Identifier already in use!", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if (ae.getSource()==this.getJButtonRemove()) {
			int selectedIndex = this.getJListAlternateIDs().getSelectedIndex();
			DataColumnAlternativeID removedID = this.getAlternativeIDsListModel().remove(selectedIndex);
//			this.dataColumnDescription.getAlternativeIDs().remove(removedID);
			this.notifyChanged(PROPERTY_CHANGE_ALT_ID_REMOVED, removedID);
		}
	}
	
	/**
	 * Finds the {@link DataColumnAlternativeID} instance for the specified identifier.
	 * @param identifier the identifier
	 * @return the data column alternative ID, null if not found
	 */
	private DataColumnAlternativeID findIdentifier(String identifier) {
		for (int i=0; i<this.getAlternativeIDsListModel().getSize(); i++) {
			DataColumnAlternativeID altID = this.getAlternativeIDsListModel().get(i);
			if (altID.getAlternativeIdentifier().equals(identifier)) {
				return altID;
			}
		}
		return null;
	}

	/**
	 * Sets the data column description to work on.
	 * @param dataColumnDescription the new data column description
	 */
	public void setDataColumnDescription(DataColumnDescription dataColumnDescription) {
		this.dataColumnDescription = dataColumnDescription;
		this.alternativeIDsListModel = null;
		this.getJListAlternateIDs().setModel(this.getAlternativeIDsListModel());
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
	 * Adds a change listener.
	 * @param changeListener the change listener
	 */
	public void addChangeListener(PropertyChangeListener changeListener) {
		if (this.getChangeListeners().contains(changeListener)==false) {
			this.getChangeListeners().add(changeListener);
		}
	}
	
	/**
	 * Removes a change listener.
	 * @param changeListener the change listener
	 */
	public void removeChangeListener(PropertyChangeListener changeListener) {
		if (this.getChangeListeners().contains(changeListener)==true) {
			this.getChangeListeners().remove(changeListener);
		}
	}
	
	/**
	 * Notifies registered change listeners.
	 * @param changeEvent the change event
	 * @param modifiedID the modified ID
	 */
	private void notifyChanged(String changeEvent, DataColumnAlternativeID modifiedID) {
		PropertyChangeEvent pce = new PropertyChangeEvent(this, changeEvent, null, modifiedID);
		for (PropertyChangeListener listener : this.getChangeListeners()) {
			listener.propertyChange(pce);
		}
	}
	
	public void setUiComponentsEnabled(boolean enabled) {
		this.getJButtonAdd().setEnabled(enabled);
		if (enabled==false) {
			// --- Enabling depends on list selection state 
			this.getJButtonRemove().setEnabled(enabled);
			this.getJButtonEdit().setEnabled(enabled);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		DataColumnAlternativeID selectedItem = this.getJListAlternateIDs().getSelectedValue();
		this.getJButtonRemove().setEnabled(selectedItem!=null);
		this.getJButtonEdit().setEnabled(selectedItem!=null);
	}
	
}
