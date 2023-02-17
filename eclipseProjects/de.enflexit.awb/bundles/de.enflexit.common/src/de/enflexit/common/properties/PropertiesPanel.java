package de.enflexit.common.properties;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;

/**
 * The Class PropertiesPanel provides an panel to edit {@link Properties}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesPanel extends JPanel {
	
	private static final long serialVersionUID = 8199025287240543269L;
	
	private Properties properties;
	
	private JLabel jLabelHeader;
	private JButton jButtonAddProperty;
	private JButton jButtonRemoveProperty;

	private JScrollPane jScrollPaneProperties;
	private DefaultTableModel tableModelProperties;
	private JTable jTableProperties;
	
	/**
	 * Instantiates a new properties panel.
	 */
	public PropertiesPanel() {
		this(null, "Properties");
	}
	
	/**
	 * Instantiates a new properties panel.
	 *
	 * @param properties the properties
	 * @param header the header to be shown
	 */
	public PropertiesPanel(Properties properties, String header) {
		this.initialize();
		this.setProperties(properties);
		this.getJLabelHeader().setText(header);
	}
	
	/**
	 * Return the current properties.
	 * @return the properties
	 */
	public Properties getProperties() {
		if (properties==null) {
			properties = new Properties();
			properties.fillWithTestData();
		}
		return properties;
	}
	/**
	 * Sets the properties.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
		this.refillTable();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 13, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jButtonAddProperty = new GridBagConstraints();
		gbc_jButtonAddProperty.insets = new Insets(10, 0, 0, 5);
		gbc_jButtonAddProperty.gridx = 1;
		gbc_jButtonAddProperty.gridy = 0;
		this.add(getJButtonAddProperty(), gbc_jButtonAddProperty);
		
		GridBagConstraints gbc_jButtonRemoveProperty = new GridBagConstraints();
		gbc_jButtonRemoveProperty.insets = new Insets(10, 0, 0, 10);
		gbc_jButtonRemoveProperty.gridx = 2;
		gbc_jButtonRemoveProperty.gridy = 0;
		this.add(getJButtonRemoveProperty(), gbc_jButtonRemoveProperty);
		
		GridBagConstraints gbc_jScrollPaneProperties = new GridBagConstraints();
		gbc_jScrollPaneProperties.gridwidth = 3;
		gbc_jScrollPaneProperties.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneProperties.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneProperties.gridx = 0;
		gbc_jScrollPaneProperties.gridy = 1;
		this.add(getJScrollPaneProperties(), gbc_jScrollPaneProperties);
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Properties");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JButton getJButtonAddProperty() {
		if (jButtonAddProperty == null) {
			jButtonAddProperty = new JButton("+");
			jButtonAddProperty.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonAddProperty.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonAddProperty;
	}
	private JButton getJButtonRemoveProperty() {
		if (jButtonRemoveProperty == null) {
			jButtonRemoveProperty = new JButton("-");
			jButtonRemoveProperty.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonRemoveProperty.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonRemoveProperty;
	}
	private JScrollPane getJScrollPaneProperties() {
		if (jScrollPaneProperties == null) {
			jScrollPaneProperties = new JScrollPane();
			jScrollPaneProperties.setViewportView(getJTableProperties());
		}
		return jScrollPaneProperties;
	}
	
	public DefaultTableModel getTableModelProperties() {
		if (tableModelProperties==null) {
			Vector<String> colName = new Vector<>();
			colName.add("Property Name");
			colName.add("Property Type");
			colName.add("Property Value");
			tableModelProperties = new DefaultTableModel(null, colName) {
				private static final long serialVersionUID = 7841309046550089999L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					Class<?> columnClass = null;
					switch (columnIndex) {
					case 0:
						columnClass = String.class;
						break;
					case 1:
					case 2:
						columnClass = PropertyValue.class;
						break;
					}
					
					if (columnClass==null) {
						columnClass = super.getColumnClass(columnIndex);
					}
					return columnClass;
				}
			};
		}
		return tableModelProperties;
	}
	
	private JTable getJTableProperties() {
		if (jTableProperties == null) {
			jTableProperties = new JTable(this.getTableModelProperties());
			jTableProperties.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableProperties.setFillsViewportHeight(true);
			
			
		}
		return jTableProperties;
	}
	
	/**
	 * Refills the properties table (clear & fill).
	 */
	private void refillTable() {
		this.getTableModelProperties().setRowCount(0);
		this.fillTable();
	}
	/**
	 * Fill the properties table.
	 */
	private void fillTable() {
		List<String> idList = this.getProperties().getIdentifierList();
		for (String identifyer : idList) {
			PropertyValue pValue = this.getProperties().getPropertyValue(identifyer);
			this.addPropertyRow(identifyer, pValue);
		}
	}
	
	/**
	 * Adds the property specified property.
	 *
	 * @param identifier the identifier of the property
	 * @param propertyValue the property value
	 */
	private void addPropertyRow(String identifier, PropertyValue propertyValue) {
		Vector<Object> row = new Vector<>();
		row.add(identifier);
		row.add(propertyValue);
		row.add(propertyValue);
		this.getTableModelProperties().addRow(row);
		
	}
	
}
