package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import de.enflexit.awb.timeSeriesDataProvider.dataModel.CsvDataSeriesConfiguration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * GUI panel for configuring a single CSV data seties.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSeriesConfigurationPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	private static final long serialVersionUID = 5580007635919268803L;
	
	private JLabel jLabelCsvDataSeries;
	private JLabel jLabelSeriesName;
	private JTextField jTextFieldSeriesName;
	private JLabel jLabelDataColumn;
	private JComboBox<String> jComboBoxDataColumn;
	
	private CsvDataSeriesConfiguration currentSeriesConfiguration;
	
	/**
	 * Instantiates a new csv data series configuration panel.
	 * Just added for Window builder compatibility, please use the constructor below!
	 */
	@Deprecated
	public CsvDataSeriesConfigurationPanel() {
		initialize();
	}
	
	/**
	 * Instantiates a new csv data series configuration panel.
	 * @param parentPanel the parent panel
	 */
	public CsvDataSeriesConfigurationPanel(CsvDataSourceConfigurationPanel parentPanel) {
		this.initialize();
	}
	

	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCsvDataSeries = new GridBagConstraints();
		gbc_jLabelCsvDataSeries.anchor = GridBagConstraints.WEST;
		gbc_jLabelCsvDataSeries.gridwidth = 3;
		gbc_jLabelCsvDataSeries.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelCsvDataSeries.gridx = 0;
		gbc_jLabelCsvDataSeries.gridy = 0;
		add(getJLabelCsvDataSeries(), gbc_jLabelCsvDataSeries);
		GridBagConstraints gbc_jLabelSeriesName = new GridBagConstraints();
		gbc_jLabelSeriesName.insets = new Insets(0, 5, 0, 5);
		gbc_jLabelSeriesName.anchor = GridBagConstraints.EAST;
		gbc_jLabelSeriesName.gridx = 0;
		gbc_jLabelSeriesName.gridy = 1;
		add(getJLabelSeriesName(), gbc_jLabelSeriesName);
		GridBagConstraints gbc_jTextFieldSeriesName = new GridBagConstraints();
		gbc_jTextFieldSeriesName.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldSeriesName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldSeriesName.gridx = 1;
		gbc_jTextFieldSeriesName.gridy = 1;
		add(getJTextFieldSeriesName(), gbc_jTextFieldSeriesName);
		GridBagConstraints gbc_jLabelDataColumn = new GridBagConstraints();
		gbc_jLabelDataColumn.insets = new Insets(0, 5, 0, 5);
		gbc_jLabelDataColumn.anchor = GridBagConstraints.EAST;
		gbc_jLabelDataColumn.gridx = 2;
		gbc_jLabelDataColumn.gridy = 1;
		add(getJLabelDataColumn(), gbc_jLabelDataColumn);
		GridBagConstraints gbc_jComboBoxDataColumn = new GridBagConstraints();
		gbc_jComboBoxDataColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataColumn.gridx = 3;
		gbc_jComboBoxDataColumn.gridy = 1;
		add(getJComboBoxDataColumn(), gbc_jComboBoxDataColumn);
	}
	
	/**
	 * Gets the series configuration.
	 * @return the series configuration
	 */
	public CsvDataSeriesConfiguration getCurrentSeriesConfiguration() {
		return currentSeriesConfiguration;
	}

	/**
	 * Sets the series configuration.
	 * @param seriesConfiguration the new series configuration
	 */
	public void setCurrentSeriesConfiguration(CsvDataSeriesConfiguration seriesConfiguration) {
		this.currentSeriesConfiguration = seriesConfiguration;
		if (seriesConfiguration!=null) {
			this.getJTextFieldSeriesName().setText(seriesConfiguration.getName());
			this.getJTextFieldSeriesName().setEditable(true);
			this.getJTextFieldSeriesName().setEnabled(true);
			if (seriesConfiguration.getDataColumn()<=this.getJComboBoxDataColumn().getItemCount()) {
				this.getJComboBoxDataColumn().setSelectedIndex(seriesConfiguration.getDataColumn());
			}
			this.getJComboBoxDataColumn().setEnabled(true);
		} else {
			this.getJTextFieldSeriesName().setText(null);
			this.getJTextFieldSeriesName().setEditable(false);
			this.getJComboBoxDataColumn().setEnabled(false);
		}
	}

	// ------------------------------------------------------------------------
	// --- From here, private getter methods for the GUI components -----------

	private JLabel getJLabelCsvDataSeries() {
		if (jLabelCsvDataSeries == null) {
			jLabelCsvDataSeries = new JLabel("CSV Data Series Configuration");
			jLabelCsvDataSeries.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCsvDataSeries;
	}
	private JLabel getJLabelSeriesName() {
		if (jLabelSeriesName == null) {
			jLabelSeriesName = new JLabel("Series Name:");
			jLabelSeriesName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSeriesName;
	}
	private JTextField getJTextFieldSeriesName() {
		if (jTextFieldSeriesName == null) {
			jTextFieldSeriesName = new JTextField();
			jTextFieldSeriesName.setColumns(15);
			jTextFieldSeriesName.setEnabled(false);
			
			// --- Trigger rename when leaving the text field -------
			jTextFieldSeriesName.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					CsvDataSeriesConfigurationPanel.this.renameSeries();
				}
			});
			// --- Trigger rename when enter was pressed ------------
			jTextFieldSeriesName.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					if (ke.getKeyCode()==KeyEvent.VK_ENTER) {
						CsvDataSeriesConfigurationPanel.this.renameSeries();
					}
				}
			});
		}
		return jTextFieldSeriesName;
	}
	private JLabel getJLabelDataColumn() {
		if (jLabelDataColumn == null) {
			jLabelDataColumn = new JLabel("Data Column:");
			jLabelDataColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataColumn;
	}
	private JComboBox<String> getJComboBoxDataColumn() {
		if (jComboBoxDataColumn == null) {
			jComboBoxDataColumn = new JComboBox<>();
			jComboBoxDataColumn.addActionListener(this);
			jComboBoxDataColumn.setEnabled(false);
		}
		return jComboBoxDataColumn;
	}
	
	/**
	 * Rebuilds the combo box model for the column selection.
	 * @param newTableModel the new table model
	 */
	private void rebuildComboBoxModel(DefaultTableModel newTableModel) {
		DefaultComboBoxModel<String> comboBoxModel = null;
		Vector<String> columnNames = new Vector<>();
		columnNames.add("-");
		if (newTableModel!=null) {
			for (int i=1; i<newTableModel.getColumnCount(); i++) {
				columnNames.add(newTableModel.getColumnName(i));
			}
			comboBoxModel = new DefaultComboBoxModel<>(columnNames);
		}
		this.getJComboBoxDataColumn().setModel(comboBoxModel);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJComboBoxDataColumn()) {
			this.currentSeriesConfiguration.setDataColumn(this.getJComboBoxDataColumn().getSelectedIndex());
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(CsvDataSourceConfigurationPanel.TABLEMODEL_CHANGED)) {
			// --- Rebuild the combo box model based on the new table model ---
			DefaultTableModel tableModel = (DefaultTableModel) evt.getNewValue();
			this.rebuildComboBoxModel(tableModel);
		}
	}

	/**
	 * Sets the name of the current data series according to the text field.
	 */
	private void renameSeries() {
		String textFieldContent = this.getJTextFieldSeriesName().getText();
		// --- Check if the name actually changed to avoid unnecessary change events.
		if (this.currentSeriesConfiguration.getName().equals(textFieldContent)==false) {
			this.currentSeriesConfiguration.setName(this.getJTextFieldSeriesName().getText());
		}
	}
}
