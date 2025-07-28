package de.enflexit.awb.timeSeriesDataProvider.jdbc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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

import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSeriesConfiguration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * A panel for configuring JDBC-based data series.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSeriesConfigurationPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	private static final long serialVersionUID = -4344439482955233575L;
	
	private static final String COMBO_BOX_NO_SELECTION = "--- Please Select ---";
	
	private JLabel jLabelDataSeriesConfiguration;
	private JLabel jLabelSeriesName;
	private JTextField jTextFieldSeriesName;
	private JLabel jLabelDataColumn;
	private JComboBox<String> jComboBoxDataColumn;
	
	private JDBCDataSeriesConfiguration currentSeriesConfiguration;
	
	/**
	 * Instantiates a new JDBC data series configuration panel.
	 */
	public JDBCDataSeriesConfigurationPanel() {
		initialize();
	}
	/**
	 * Sets the column names.
	 * @param columnNames the new column names
	 */
	public void setColumnNames(Vector<String> columnNames) {
		this.updateComboBoxModel(columnNames);
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelDataSeriesConfiguration = new GridBagConstraints();
		gbc_jLabelDataSeriesConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSeriesConfiguration.gridwidth = 2;
		gbc_jLabelDataSeriesConfiguration.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDataSeriesConfiguration.gridx = 0;
		gbc_jLabelDataSeriesConfiguration.gridy = 0;
		add(getJLabelDataSeriesConfiguration(), gbc_jLabelDataSeriesConfiguration);
		GridBagConstraints gbc_jLabelSeriesName = new GridBagConstraints();
		gbc_jLabelSeriesName.insets = new Insets(5, 5, 0, 5);
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
		gbc_jLabelDataColumn.anchor = GridBagConstraints.EAST;
		gbc_jLabelDataColumn.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelDataColumn.gridx = 2;
		gbc_jLabelDataColumn.gridy = 1;
		add(getJLabelDataColumn(), gbc_jLabelDataColumn);
		GridBagConstraints gbc_jCompoBoxDataColumn = new GridBagConstraints();
		gbc_jCompoBoxDataColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCompoBoxDataColumn.gridx = 3;
		gbc_jCompoBoxDataColumn.gridy = 1;
		add(getJComboBoxDataColumn(), gbc_jCompoBoxDataColumn);
	}
	
	/**
	 * Gets the series configuration.
	 * @return the series configuration
	 */
	public JDBCDataSeriesConfiguration getCurrentSeriesConfiguration() {
		return currentSeriesConfiguration;
	}

	/**
	 * Sets the series configuration.
	 * @param seriesConfiguration the new series configuration
	 */
	public void setCurrentSeriesConfiguration(JDBCDataSeriesConfiguration seriesConfiguration) {
		this.currentSeriesConfiguration = seriesConfiguration;
		if (seriesConfiguration!=null) {
			this.getJTextFieldSeriesName().setText(seriesConfiguration.getName());
			this.getJTextFieldSeriesName().setEditable(true);
			this.getJTextFieldSeriesName().setEnabled(true);
			this.getJComboBoxDataColumn().setEnabled(true);
			this.getJComboBoxDataColumn().setSelectedItem(seriesConfiguration.getDataColumn());
		} else {
			this.getJTextFieldSeriesName().setText(null);
			this.getJTextFieldSeriesName().setEditable(false);
			this.getJComboBoxDataColumn().setSelectedItem(COMBO_BOX_NO_SELECTION);
			this.getJComboBoxDataColumn().setEnabled(false);
		}
	}


	/**
	 * Gets the j label data series configuration.
	 * @return the j label data series configuration
	 */
	private JLabel getJLabelDataSeriesConfiguration() {
		if (jLabelDataSeriesConfiguration == null) {
			jLabelDataSeriesConfiguration = new JLabel("Data Series Configuration");
			jLabelDataSeriesConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDataSeriesConfiguration;
	}
	
	/**
	 * Gets the j label series name.
	 * @return the j label series name
	 */
	private JLabel getJLabelSeriesName() {
		if (jLabelSeriesName == null) {
			jLabelSeriesName = new JLabel("Series Name:");
			jLabelSeriesName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSeriesName;
	}
	
	/**
	 * Gets the j text field series name.
	 * @return the j text field series name
	 */
	private JTextField getJTextFieldSeriesName() {
		if (jTextFieldSeriesName == null) {
			jTextFieldSeriesName = new JTextField();
			jTextFieldSeriesName.setColumns(15);
			jTextFieldSeriesName.setEnabled(false);
			
			// --- Trigger rename when leaving the text field -------
			jTextFieldSeriesName.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					JDBCDataSeriesConfigurationPanel.this.renameSeries();
				}
			});
//			// --- Trigger rename when enter was pressed ------------
//			jTextFieldSeriesName.addKeyListener(new KeyAdapter() {
//				@Override
//				public void keyReleased(KeyEvent ke) {
//					if (ke.getKeyCode()==KeyEvent.VK_ENTER) {
//						JDBCDataSeriesConfigurationPanel.this.renameSeries();
//					}
//				}
//			});
		}
		return jTextFieldSeriesName;
	}
	
	/**
	 * Gets the j label data column.
	 * @return the j label data column
	 */
	private JLabel getJLabelDataColumn() {
		if (jLabelDataColumn == null) {
			jLabelDataColumn = new JLabel("Data Column:");
			jLabelDataColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataColumn;
	}
	
	/**
	 * Gets the j combo box data column.
	 * @return the j combo box data column
	 */
	private JComboBox<String> getJComboBoxDataColumn() {
		if (jComboBoxDataColumn == null) {
			jComboBoxDataColumn = new JComboBox<String>();
			this.updateComboBoxModel(null);
			jComboBoxDataColumn.addActionListener(this);
			jComboBoxDataColumn.setEnabled(false);
		}
		return jComboBoxDataColumn;
	}
	
	/**
	 * Updates the combo box model.
	 * @param columnNames the column names
	 */
	private void updateComboBoxModel(Vector<String> columnNames) {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		comboBoxModel.addElement(COMBO_BOX_NO_SELECTION);
		if (columnNames!=null) {
			comboBoxModel.addAll(columnNames);
		}
		this.getJComboBoxDataColumn().setModel(comboBoxModel);
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Renames the currently selected data series.
	 */
	protected void renameSeries() {
		String textFieldContent = this.getJTextFieldSeriesName().getText();
		// --- Check if the name actually changed to avoid unnecessary change events.
		if (this.currentSeriesConfiguration.getName().equals(textFieldContent)==false) {
			this.currentSeriesConfiguration.setName(this.getJTextFieldSeriesName().getText());
		}
	}
	
}
