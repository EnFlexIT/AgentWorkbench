package de.enflexit.awb.timeSeriesDataProvider.jdbc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider.ConfigurationScope;
import de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataScourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSource;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSource.ConnectionState;
import de.enflexit.common.swing.JComboBoxWide;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.gui.DatabaseSettings;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Panel to manage the settings for a {@link JDBCDataSource}
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSourceConfigurationPanel extends AbstractDataSourceConfigurationPanel implements ActionListener {
	
	private static final long serialVersionUID = 1221965879225470214L;
	
	private static final String ICON_PATH_NOT_CONNECTED = "/icons/TrafficLightGrey.png";
	private static final String ICON_PATH_CONNECTED = "/icons/TrafficLightGreen.png";
	private static final String ICON_PATH_ERROR = "/icons/TrafficLightRed.png";
	
	private static final String COMBO_BOX_NO_SELECTION = "--- Please Select ---";
	private static final int NUMBER_OF_PREVIEW_ROWS = 25;
	
	private JDBCDataScourceConfiguration sourceConfiguration;
	private JDBCDataSource dataSource;
	
	private JLabel jLabelJDBCDataSource;
	private JLabel jLabelSourceName;
	private JTextField jTextFieldSourceName;
	
	private JLabel jLabelDBSettings;
	private JLabel jLabelConnectionState;
	
	private JLabel jLabelDataSelection;
	private JButton jButtonAutoCreateSeries;
	private JLabel jLabelSelectTable;
	private JComboBoxWide<String> jComboBoxSelectTable;
	private JLabel jLabelSelectDateTimeColumn;
	private JComboBoxWide<String> jComboBoxSelectDateTimeColumn;
	private JScrollPane jScrollPaneDataTable;
	
	private JTable jTableDataTable;
	
	private JDBCDataSeriesConfigurationPanel jPanelSeriesConfiguration;
	
	private Vector<String> columnNames;
	private JButton jButtonConfigDialog;
	private JLabel jLabelCurrentDBConfig;
	
	private DateTimeFormatter dateTimeFormatter;
	private JLabel jLabelStatistics;
	private JLabel jLabelStoreAt;
	private JComboBox<ConfigurationScope> jComboBoxConfigurationScope;
	
	private boolean pauseListener;
	
	/**
	 * Instantiates a new JDBC data source configuration panel.
	 */
	public JDBCDataSourceConfigurationPanel() {
		initialize();
	}

	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelJDBCDataSource = new GridBagConstraints();
		gbc_jLabelJDBCDataSource.anchor = GridBagConstraints.WEST;
		gbc_jLabelJDBCDataSource.gridwidth = 2;
		gbc_jLabelJDBCDataSource.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelJDBCDataSource.gridx = 0;
		gbc_jLabelJDBCDataSource.gridy = 0;
		add(getJLabelJDBCDataSource(), gbc_jLabelJDBCDataSource);
		GridBagConstraints gbc_jLabelSourceName = new GridBagConstraints();
		gbc_jLabelSourceName.anchor = GridBagConstraints.WEST;
		gbc_jLabelSourceName.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSourceName.gridx = 0;
		gbc_jLabelSourceName.gridy = 1;
		add(getJLabelSourceName(), gbc_jLabelSourceName);
		GridBagConstraints gbc_jTextFieldSourceName = new GridBagConstraints();
		gbc_jTextFieldSourceName.gridwidth = 2;
		gbc_jTextFieldSourceName.insets = new Insets(5, 0, 5, 5);
		gbc_jTextFieldSourceName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldSourceName.gridx = 1;
		gbc_jTextFieldSourceName.gridy = 1;
		add(getJTextFieldSourceName(), gbc_jTextFieldSourceName);
		GridBagConstraints gbc_jLabelStoreAt = new GridBagConstraints();
		gbc_jLabelStoreAt.anchor = GridBagConstraints.EAST;
		gbc_jLabelStoreAt.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelStoreAt.gridx = 3;
		gbc_jLabelStoreAt.gridy = 1;
		add(getJLabelStoreAt(), gbc_jLabelStoreAt);
		GridBagConstraints gbc_jComboBoxConfigurationScope = new GridBagConstraints();
		gbc_jComboBoxConfigurationScope.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxConfigurationScope.insets = new Insets(5, 0, 5, 5);
		gbc_jComboBoxConfigurationScope.gridx = 4;
		gbc_jComboBoxConfigurationScope.gridy = 1;
		add(getJComboBoxConfigurationScope(), gbc_jComboBoxConfigurationScope);
		GridBagConstraints gbc_jLabelDBSettings = new GridBagConstraints();
		gbc_jLabelDBSettings.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDBSettings.gridx = 0;
		gbc_jLabelDBSettings.gridy = 2;
		add(getJLabelDBSettings(), gbc_jLabelDBSettings);
		GridBagConstraints gbc_jButtonConfigDialog = new GridBagConstraints();
		gbc_jButtonConfigDialog.anchor = GridBagConstraints.WEST;
		gbc_jButtonConfigDialog.insets = new Insets(5, 5, 5, 5);
		gbc_jButtonConfigDialog.gridx = 0;
		gbc_jButtonConfigDialog.gridy = 3;
		add(getJButtonConfigDialog(), gbc_jButtonConfigDialog);
		GridBagConstraints gbc_jLabelCurrentDBConfig = new GridBagConstraints();
		gbc_jLabelCurrentDBConfig.anchor = GridBagConstraints.WEST;
		gbc_jLabelCurrentDBConfig.gridwidth = 2;
		gbc_jLabelCurrentDBConfig.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelCurrentDBConfig.gridx = 1;
		gbc_jLabelCurrentDBConfig.gridy = 3;
		add(getJLabelCurrentDBConfig(), gbc_jLabelCurrentDBConfig);
		GridBagConstraints gbc_jLabelConnectionState = new GridBagConstraints();
		gbc_jLabelConnectionState.gridwidth = 2;
		gbc_jLabelConnectionState.anchor = GridBagConstraints.WEST;
		gbc_jLabelConnectionState.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelConnectionState.gridx = 3;
		gbc_jLabelConnectionState.gridy = 3;
		add(getJLabelConnectionState(), gbc_jLabelConnectionState);
		GridBagConstraints gbc_jLabelDataSelection = new GridBagConstraints();
		gbc_jLabelDataSelection.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSelection.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDataSelection.gridx = 0;
		gbc_jLabelDataSelection.gridy = 4;
		add(getJLabelDataSelection(), gbc_jLabelDataSelection);
		GridBagConstraints gbc_jLabelSelectTable = new GridBagConstraints();
		gbc_jLabelSelectTable.anchor = GridBagConstraints.WEST;
		gbc_jLabelSelectTable.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSelectTable.gridx = 0;
		gbc_jLabelSelectTable.gridy = 5;
		add(getJLabelSelectTable(), gbc_jLabelSelectTable);
		GridBagConstraints gbc_jComboBoxSelectTable = new GridBagConstraints();
		gbc_jComboBoxSelectTable.insets = new Insets(5, 0, 5, 5);
		gbc_jComboBoxSelectTable.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectTable.gridx = 1;
		gbc_jComboBoxSelectTable.gridy = 5;
		add(getJComboBoxSelectTable(), gbc_jComboBoxSelectTable);
		GridBagConstraints gbc_jLabelSelectDateTimeColumn = new GridBagConstraints();
		gbc_jLabelSelectDateTimeColumn.anchor = GridBagConstraints.WEST;
		gbc_jLabelSelectDateTimeColumn.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSelectDateTimeColumn.gridx = 2;
		gbc_jLabelSelectDateTimeColumn.gridy = 5;
		add(getJLabelSelectDateTimeColumn(), gbc_jLabelSelectDateTimeColumn);
		GridBagConstraints gbc_jComboBoxSelectDateTimeColumn = new GridBagConstraints();
		gbc_jComboBoxSelectDateTimeColumn.insets = new Insets(5, 0, 5, 5);
		gbc_jComboBoxSelectDateTimeColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectDateTimeColumn.gridx = 3;
		gbc_jComboBoxSelectDateTimeColumn.gridy = 5;
		add(getJComboBoxSelectDateTimeColumn(), gbc_jComboBoxSelectDateTimeColumn);
		GridBagConstraints gbc_jButtonAutoCreateSeries = new GridBagConstraints();
		gbc_jButtonAutoCreateSeries.anchor = GridBagConstraints.WEST;
		gbc_jButtonAutoCreateSeries.insets = new Insets(5, 0, 5, 5);
		gbc_jButtonAutoCreateSeries.gridx = 4;
		gbc_jButtonAutoCreateSeries.gridy = 5;
		add(getJButtonAutoCreateSeries(), gbc_jButtonAutoCreateSeries);
		GridBagConstraints gbc_jLabelStatistics = new GridBagConstraints();
		gbc_jLabelStatistics.anchor = GridBagConstraints.WEST;
		gbc_jLabelStatistics.gridwidth = 6;
		gbc_jLabelStatistics.insets = new Insets(5, 5, 5, 0);
		gbc_jLabelStatistics.gridx = 0;
		gbc_jLabelStatistics.gridy = 6;
		add(getJLabelStatistics(), gbc_jLabelStatistics);
		GridBagConstraints gbc_jScrollPaneDataTable = new GridBagConstraints();
		gbc_jScrollPaneDataTable.insets = new Insets(5, 5, 5, 0);
		gbc_jScrollPaneDataTable.gridwidth = 6;
		gbc_jScrollPaneDataTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneDataTable.gridx = 0;
		gbc_jScrollPaneDataTable.gridy = 7;
		add(getJScrollPaneDataTable(), gbc_jScrollPaneDataTable);
		GridBagConstraints gbc_jPanelSeriesConfiguration = new GridBagConstraints();
		gbc_jPanelSeriesConfiguration.insets = new Insets(0, 0, 0, 5);
		gbc_jPanelSeriesConfiguration.gridwidth = 5;
		gbc_jPanelSeriesConfiguration.fill = GridBagConstraints.BOTH;
		gbc_jPanelSeriesConfiguration.gridx = 0;
		gbc_jPanelSeriesConfiguration.gridy = 8;
		add(getSeriesConfigurationPanel(), gbc_jPanelSeriesConfiguration);
	}

	/**
	 * Gets the header label.
	 * @return the header label
	 */
	private JLabel getJLabelJDBCDataSource() {
		if (jLabelJDBCDataSource == null) {
			jLabelJDBCDataSource = new JLabel("JDBC Data Source Configuration");
			jLabelJDBCDataSource.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelJDBCDataSource;
	}
	
	private JLabel getJLabelSourceName() {
		if (jLabelSourceName == null) {
			jLabelSourceName = new JLabel("Source Name");
			jLabelSourceName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSourceName;
	}
	private JTextField getJTextFieldSourceName() {
		if (jTextFieldSourceName == null) {
			jTextFieldSourceName = new JTextField();
			jTextFieldSourceName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldSourceName.addFocusListener(new FocusAdapter() {
				// --- Change name when leaving the field
				@Override
				public void focusLost(FocusEvent fe) {
					if (pauseListener==false) {
						JDBCDataSourceConfigurationPanel.this.renameDataSource();
					}
				}
			});
			jTextFieldSourceName.addKeyListener(new KeyAdapter() {
				// --- Change name when pressing enter
				@Override
				public void keyReleased(KeyEvent ke) {
					if (ke.getKeyCode()==KeyEvent.VK_ENTER) {
						JDBCDataSourceConfigurationPanel.this.renameDataSource();
					}
				}
			});
			jTextFieldSourceName.setColumns(10);
		}
		return jTextFieldSourceName;
	}
	
	private JLabel getJLabelStoreAt() {
		if (jLabelStoreAt == null) {
			jLabelStoreAt = new JLabel("Store at");
			jLabelStoreAt.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelStoreAt;
	}

	private JComboBox<ConfigurationScope> getJComboBoxConfigurationScope() {
		if (jComboBoxConfigurationScope == null) {
			jComboBoxConfigurationScope = new JComboBox<>();
			jComboBoxConfigurationScope.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxConfigurationScope.setModel(new DefaultComboBoxModel<TimeSeriesDataProvider.ConfigurationScope>(ConfigurationScope.values()));
			jComboBoxConfigurationScope.setToolTipText("Decide where to store the data source. If stored in the application, it will be available independently of the current project. If stored in the project, it is limited to that project, but can easily be shared together with it.");
			jComboBoxConfigurationScope.addActionListener(this);
		}
		return jComboBoxConfigurationScope;
	}

	private JLabel getJLabelDBSettings() {
		if (jLabelDBSettings == null) {
			jLabelDBSettings = new JLabel("Databse Settings");
			jLabelDBSettings.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDBSettings;
	}
	private JButton getJButtonConfigDialog() {
		if (jButtonConfigDialog == null) {
			jButtonConfigDialog = new JButton("Edit Settings");
			jButtonConfigDialog.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonConfigDialog.addActionListener(this);
		}
		return jButtonConfigDialog;
	}
	private JLabel getJLabelCurrentDBConfig() {
		if (jLabelCurrentDBConfig == null) {
			jLabelCurrentDBConfig = new JLabel("No database connection configured!");
			jLabelCurrentDBConfig.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelCurrentDBConfig;
	}
	private JLabel getJLabelConnectionState() {
		if (jLabelConnectionState == null) {
			jLabelConnectionState = new JLabel("Not connected");
			jLabelConnectionState.setFont(new Font("Dialog", Font.PLAIN, 12));
			this.updateConnectionStateLabel(ConnectionState.NOT_CONNECTED);
		}
		return jLabelConnectionState;
	}
	
	private JLabel getJLabelDataSelection() {
		if (jLabelDataSelection == null) {
			jLabelDataSelection = new JLabel("Data Selection");
			jLabelDataSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDataSelection;
	}
	
	private JLabel getJLabelSelectTable() {
		if (jLabelSelectTable == null) {
			jLabelSelectTable = new JLabel("Select Table");
			jLabelSelectTable.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSelectTable;
	}
	private JComboBoxWide<String> getJComboBoxSelectTable() {
		if (jComboBoxSelectTable == null) {
			jComboBoxSelectTable = new JComboBoxWide<String>();
			jComboBoxSelectTable.setPreferredSize(new Dimension(150, 26));
			jComboBoxSelectTable.setMaximumSize(new Dimension(150, 26));
			jComboBoxSelectTable.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxSelectTable.setModel(this.createComboBoxModel(true, null));
			jComboBoxSelectTable.setEnabled(false);
			jComboBoxSelectTable.addActionListener(this);
		}
		return jComboBoxSelectTable;
	}

	private JLabel getJLabelSelectDateTimeColumn() {
		if (jLabelSelectDateTimeColumn == null) {
			jLabelSelectDateTimeColumn = new JLabel("Select Date/Time Column");
			jLabelSelectDateTimeColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSelectDateTimeColumn;
	}
	private JComboBoxWide<String> getJComboBoxSelectDateTimeColumn() {
		if (jComboBoxSelectDateTimeColumn == null) {
			jComboBoxSelectDateTimeColumn = new JComboBoxWide<String>();
			jComboBoxSelectDateTimeColumn.setPreferredSize(new Dimension(150, 26));
			jComboBoxSelectDateTimeColumn.setMaximumSize(new Dimension(150, 26));
			jComboBoxSelectDateTimeColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxSelectDateTimeColumn.setModel(this.createComboBoxModel(true, null));
			jComboBoxSelectDateTimeColumn.setEnabled(false);
			jComboBoxSelectDateTimeColumn.addActionListener(this);
		}
		return jComboBoxSelectDateTimeColumn;
	}
	private JButton getJButtonAutoCreateSeries() {
		if (jButtonAutoCreateSeries == null) {
			jButtonAutoCreateSeries = new JButton("Create Data Series");
			jButtonAutoCreateSeries.setToolTipText("Autmatically create data series for all columns");
			jButtonAutoCreateSeries.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonAutoCreateSeries.addActionListener(this);
			jButtonAutoCreateSeries.setEnabled(false);
		}
		return jButtonAutoCreateSeries;
	}
	
	private JLabel getJLabelStatistics() {
		if (jLabelStatistics == null) {
			jLabelStatistics = new JLabel("");
			jLabelStatistics.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelStatistics;
	}

	private JScrollPane getJScrollPaneDataTable() {
		if (jScrollPaneDataTable == null) {
			jScrollPaneDataTable = new JScrollPane();
			jScrollPaneDataTable.setViewportView(getJTableDataTable());
		}
		return jScrollPaneDataTable;
	}
	private JTable getJTableDataTable() {
		if (jTableDataTable == null) {
			jTableDataTable = new JTable();
		}
		return jTableDataTable;
	}
	private JDBCDataSeriesConfigurationPanel getSeriesConfigurationPanel() {
		if (jPanelSeriesConfiguration == null) {
			jPanelSeriesConfiguration = new JDBCDataSeriesConfigurationPanel();
		}
		return jPanelSeriesConfiguration;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#getDataSourceConfiguraiton()
	 */
	@Override
	public AbstractDataSourceConfiguration getDataSourceConfiguration() {
		return this.getSourceConfiguration();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSourceConfiguration(de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration)
	 */
	@Override
	public void setDataSourceConfiguration(AbstractDataSourceConfiguration sourceConfiguration) {
		if (sourceConfiguration== null || sourceConfiguration instanceof JDBCDataScourceConfiguration) {
			this.setSourceConfiguration((JDBCDataScourceConfiguration) sourceConfiguration);
		} else {
			throw new IllegalArgumentException("The provided data source configuration is not for a JDBC data source!");
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSeriesConfiguration(de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration)
	 */
	@Override
	public void setDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguraiton) {
		if (seriesConfiguraiton instanceof JDBCDataSeriesConfiguration) {
			this.getSeriesConfigurationPanel().setCurrentSeriesConfiguration((JDBCDataSeriesConfiguration) seriesConfiguraiton);
		}
		
	}
	
	/**
	 * Sets the source configuration.
	 * @param sourceConfiguration the new source configuration
	 */
	private void setSourceConfiguration(JDBCDataScourceConfiguration sourceConfiguration) {
		this.sourceConfiguration = sourceConfiguration;
		if (sourceConfiguration!=null) {
			this.getJTextFieldSourceName().setText(this.sourceConfiguration.getName());
			this.getJComboBoxConfigurationScope().setSelectedItem(this.sourceConfiguration.getConfigurationScope());
			this.setDBSettingsLabelText(sourceConfiguration);
			// --- If database settings are configured, try to establish a connection
			if (this.sourceConfiguration.getDatabaseSettings()!=null) {
				this.establishConnection();
			}
		} else {
			this.resetUIComponenst();
		}
		
	}
	
	/**
	 * Sets all UI components to a blank and disabled state if no data source is selected
	 */
	private void resetUIComponenst() {
		this.pauseListener = true;
		
		this.getJTextFieldSourceName().setText(null);
		this.getJTextFieldSourceName().setEnabled(false);
		this.setDBSettingsLabelText(this.sourceConfiguration);
		this.getJButtonConfigDialog().setEnabled(false);
		this.updateConnectionStateLabel(ConnectionState.NOT_CONNECTED);
		
		this.getJComboBoxSelectTable().setSelectedIndex(0);
		this.getJComboBoxSelectTable().setEnabled(false);
		this.getJComboBoxSelectDateTimeColumn().setSelectedIndex(0);
		this.getJComboBoxSelectDateTimeColumn().setEnabled(false);
		this.getJButtonAutoCreateSeries().setEnabled(false);
		
		this.getJTableDataTable().setModel(new DefaultTableModel());
		
		this.pauseListener = false;
	}
	
	/**
	 * Gets the source configuration.
	 * @return the source configuration
	 */
	private JDBCDataScourceConfiguration getSourceConfiguration() {
		return sourceConfiguration;
	}
	
	/**
	 * Changes the name of the current data source.
	 */
	private void renameDataSource() {
		String newName = this.getJTextFieldSourceName().getText();
		this.pauseListener = true;
		if (super.renameDataSource(newName)==false) {
			// If the name was rejected, return to the textfield
			this.getJTextFieldSourceName().requestFocus();
		}
		this.pauseListener = false;
	}
	
	/**
	 * Creates a {@link DefaultComboBoxModel} containing the provided String values. If configured, am entry indicating
	 * no value is selected will be included at the first position. 
	 * @param includeNothingSelected specifies if an initial entry for nothing selected should be included
	 * @param entries the entries
	 * @return the default combo box model
	 */
	private DefaultComboBoxModel<String> createComboBoxModel(boolean includeNothingSelected, List<String> entries){
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		if (includeNothingSelected==true) {
			comboBoxModel.addElement(COMBO_BOX_NO_SELECTION);
		}
		if (entries!=null) {
			comboBoxModel.addAll(entries);
		}
		return comboBoxModel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (this.pauseListener==false) {
			if (ae.getSource()==this.getJComboBoxConfigurationScope()) {
				
				// --- Project scope not possible if no project is loaded
				ConfigurationScope configScope = (ConfigurationScope) this.getJComboBoxConfigurationScope().getSelectedItem();
				if (configScope==ConfigurationScope.PROJECT && Application.getProjectFocused()==null) {
					JOptionPane.showMessageDialog(this, "No active project, storing in project scope is not possible!", "No active Project!", JOptionPane.ERROR_MESSAGE);
					this.getJComboBoxConfigurationScope().setSelectedItem(ConfigurationScope.APPLICATION);
					return;
				}
				this.sourceConfiguration.setConfigurationScope((ConfigurationScope) this.getJComboBoxConfigurationScope().getSelectedItem());
				
			} else if (ae.getSource()==this.getJComboBoxSelectTable()) {
				String selectedTable = this.getJComboBoxSelectTable().getItemAt(this.getJComboBoxSelectTable().getSelectedIndex());
				if (selectedTable.equals(COMBO_BOX_NO_SELECTION)==false) {
					this.sourceConfiguration.setTableName(selectedTable);
				} else {
					if (this.sourceConfiguration!=null) {
						this.sourceConfiguration.setTableName(null);
					}
					this.resetTableDependentComponents();
				}
				this.loadPreviewData();
				this.getStatistics();
				
			} else if (ae.getSource()==this.getJComboBoxSelectDateTimeColumn()) {
				String selectedColumn = (String) this.getJComboBoxSelectDateTimeColumn().getSelectedItem();
				if (selectedColumn.equals(COMBO_BOX_NO_SELECTION)==false) {
					this.getSourceConfiguration().setDateTimeColumn(selectedColumn);
					this.getJButtonAutoCreateSeries().setEnabled(true);
				} else {
					this.getSourceConfiguration().setDateTimeColumn(null);
					this.getJButtonAutoCreateSeries().setEnabled(false);
				}
				
			} else if (ae.getSource()==this.getJButtonAutoCreateSeries()) {
				this.autoCreateDataSeries();
			} else if (ae.getSource()==this.getJButtonConfigDialog()) {
				this.showDbConfigDialog();
			}
		}
	}

	/**
	 * Shows the database configuration dialog.
	 */
	private void showDbConfigDialog() {

		DatabaseSettings initialSettings = null;
		if (this.getSourceConfiguration().getDbmsName()!=null && this.getSourceConfiguration().getDatabaseSettings()!=null) {
			initialSettings = new DatabaseSettings(this.getSourceConfiguration().getDbmsName(), this.getSourceConfiguration().getDatabaseSettings());
		}
		
		Window owner = null;
		if (Application.getMainWindow()!=null) {
			owner = (Window) Application.getMainWindow();
		}
		
		DBSettingsDialog dbSettingsDialog = new DBSettingsDialog(owner, "Database Settings", initialSettings);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(dbSettingsDialog, JDialogPosition.ParentCenter);
		dbSettingsDialog.setVisible(true);
		
		if (dbSettingsDialog.isCanceled()==false) {
			DatabaseSettings dbSettings = dbSettingsDialog.getDbSettings();
			this.getSourceConfiguration().setDbmsName(dbSettings.getDatabaseSystemName());
			this.getSourceConfiguration().setDatabaseSettings(dbSettings.getHibernateDatabaseSettings());
			this.setDBSettingsLabelText(this.getSourceConfiguration());
			this.getDataSource().setSourceConfiguration(this.getSourceConfiguration());
			
			this.establishConnection();
		}
	}

	/**
	 * Resets all UI components with a content that depends on the selected table.
	 */
	private void resetTableDependentComponents() {
		this.getJTableDataTable().setModel(new DefaultTableModel());
		this.getJComboBoxSelectDateTimeColumn().setModel(this.createComboBoxModel(true, null));
		this.getJButtonAutoCreateSeries().setEnabled(false);
	}

	/**
	 * Connects to the configured database.
	 * @throws SQLException 
	 */
	private void establishConnection() {
		// --- Establish the JDBC connection ----------------------------------
		boolean connectionSuccess = (this.getDataSource().getConnection()!=null);
		
		if (connectionSuccess) {
			// --- Request the list of tables ---------------------------------
			this.getJComboBoxSelectTable().setModel(this.getDbTablesComboBoxModel());
			this.getJComboBoxSelectTable().setEnabled(true);
			
			if (this.getSourceConfiguration().getTableName()!=null) {
				// --- If already configured, select the table and load the preview data
				this.getJComboBoxSelectTable().setSelectedItem(this.getSourceConfiguration().getTableName());
				this.loadPreviewData();
				this.getStatistics();
			}
			
		}
		
		this.updateConnectionStateLabel(dataSource.getConnectionState());
	}
	
	/**
	 * Gets and displays some statistical information on the table data.
	 * @return the statistics
	 */
	private void getStatistics() {
		int numOfRows = this.getDataSource().getNumberOfRows();

		// --- If possible, determine the earliest and latest entry
		String timeRangeString = "";
		long firstTimestamp = this.getDataSource().getFirstTimestamp();
		long lastTImestamp = this.getDataSource().getLastTimestamp();
		if (firstTimestamp>=0 && lastTImestamp>=0) {
			Instant firstInstand = Instant.ofEpochMilli(firstTimestamp);
			Instant lastInstant = Instant.ofEpochMilli(lastTImestamp);
			timeRangeString = " between " + this.getDateTimeFormatter().format(firstInstand) + " and " + this.getDateTimeFormatter().format(lastInstant);
		}
		
		// --- Compose and display the statistics string
		String statisticsString = "The selected table contains " + numOfRows + " rows" + timeRangeString + ", showing the first " + NUMBER_OF_PREVIEW_ROWS;
		this.getJLabelStatistics().setText(statisticsString);
	}

	/**
	 * Establishes the JDBC connection.
	 * @param jdbcURL the jdbc URL
	 * @return the connection, null in case of failure
	 */
	private Connection getConnection() {
		return this.getDataSource().getConnection();
	}
	
	/**
	 * Gets a list of all available tables from the connected database
	 * @return the list of tables, null in case of failure
	 */
	private Vector<String> requestDatabaseTables(){
		if (this.getConnection()!=null) {
			try {
				Vector<String> tablesList = new Vector<String>();
				tablesList.add("--- Please Select ---");
				DatabaseMetaData dbMetaData = this.getConnection().getMetaData();
				String catalog = this.getSourceConfiguration().getDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
				String[] typeFilter = {"TABLE", "VIEW"};
				try (ResultSet rs = dbMetaData.getTables(catalog, "public", "%", typeFilter) ){
					while(rs.next()) {
						tablesList.add(rs.getString("TABLE_NAME"));
					}
				}
				return tablesList;
			} catch (SQLException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error requesting the tables from the data  base!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Creates a combo box model that contains all table names from the currently connected database
	 * @return the combo box model, null in case of failure
	 */
	private DefaultComboBoxModel<String> getDbTablesComboBoxModel(){
		DefaultComboBoxModel<String> comboBoxModel = null;
		if (this.getConnection()!=null) {
			Vector<String> tableNames = this.requestDatabaseTables();
			if (tableNames!=null) {
				comboBoxModel = new DefaultComboBoxModel<String>(tableNames);
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Curently not connected to a database");
		}
		return comboBoxModel;
	}
	
	/**
	 * Loads a number of rows from the selected table to preview the data.
	 * The number can be configured using the corresponding class constant.
	 */
	private void loadPreviewData() {
		if (this.getConnection()!=null) {
			
			Statement statement;
			try {
				// --- Query the data -------------------------------
				statement = this.getConnection().createStatement();
				String selectSql = "SELECT * FROM " + this.getSourceConfiguration().getTableName() + " LIMIT " + NUMBER_OF_PREVIEW_ROWS;
				ResultSet resultSet = statement.executeQuery(selectSql);
				
				if (resultSet!=null) {
					
					this.getJComboBoxSelectDateTimeColumn().setEnabled(true);
					
					// --- Get the column names from the result set
					ResultSetMetaData rsmd = resultSet.getMetaData();
					this.columnNames = new Vector<String>();
					for (int i=1; i<=rsmd.getColumnCount(); i++) {
						this.columnNames.add(rsmd.getColumnName(i));
					}
					
					// --- Create a table model containing the retrieved data and set it to the table
					Vector<Vector<String>> tableData = new Vector<Vector<String>>();
					while(resultSet.next()==true) {
						Vector<String> rowVector = new Vector<String>();
						for (String columnName : this.columnNames) {
							rowVector.add(resultSet.getString(columnName));
						}
						tableData.add(rowVector);
					}
					
					DefaultTableModel tableModel = new DefaultTableModel(tableData, this.columnNames);
					this.getJTableDataTable().setModel(tableModel);
					
					// --- Create a combo box model of the column names for selecting the date time column
					List<String> dateTimeColumns = this.findTimeStampColumns(rsmd);
					if (dateTimeColumns.size()>0) {
						this.getJComboBoxSelectDateTimeColumn().setModel(this.createComboBoxModel(true, dateTimeColumns));
						this.getJComboBoxSelectDateTimeColumn().setSelectedItem(dateTimeColumns.get(0));
					} else {
						JOptionPane.showMessageDialog(OwnerDetection.getOwnerWindowForComponent(this), "Warning: The selected database table contains no date/time column!", "No date/time column!", JOptionPane.WARNING_MESSAGE);
					}
					this.getSeriesConfigurationPanel().setColumnNames(columnNames);
				}
			} catch (SQLException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error loading preview data from the database!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Find time stamp columns.
	 * @param resultSetMetaData the result set meta data
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	private List<String> findTimeStampColumns(ResultSetMetaData resultSetMetaData) throws SQLException {
		List<String> columnNames = new ArrayList<String>();
		for (int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
			if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("timestamp")) {
				columnNames.add(resultSetMetaData.getColumnName(i));
			}
		}
		return columnNames;
	}
	
	/**
	 * Gets the corresponding icon for the provided {@link ConnectionState} to the connection state label.
	 * @param connectionState the connection state
	 */
	private void updateConnectionStateLabel(ConnectionState connectionState) {
		String iconPath = null;
		String labelText = null;
		switch (connectionState) {
		case NOT_CONNECTED:
			iconPath = ICON_PATH_NOT_CONNECTED;
			labelText = "Not Connected";
			break;
		case CONNECTED:
			iconPath = ICON_PATH_CONNECTED;
			labelText = "Connected";
			break;
		case ERROR:
			iconPath = ICON_PATH_ERROR;
			labelText = "Connection Error!";
			break;
		}
		ImageIcon connectionStateIcon = new ImageIcon(this.getClass().getResource(iconPath));
		this.getJLabelConnectionState().setIcon(connectionStateIcon);
		this.getJLabelConnectionState().setText(labelText);
		
	}
	
	/**
	 * Automatically creates a data series for every non-date column of the table.
	 */
	private void autoCreateDataSeries() {
		
		for (String columnName : this.columnNames) {
			if (columnName.equals(this.getSourceConfiguration().getDateTimeColumn())==false) {
				JDBCDataSeriesConfiguration seriesConfig = new JDBCDataSeriesConfiguration();
				seriesConfig.setName(columnName);
				seriesConfig.setDataColumn(columnName);
				this.getDataSourceConfiguration().addDataSeriesConfiguration(seriesConfig);
			}
		}
	}
	
	/**
	 * Gets the data source.
	 * @return the data source
	 */
	private JDBCDataSource getDataSource() {
		if (dataSource==null) {
			String dataSourceName = this.getSourceConfiguration().getName();
			dataSource = (JDBCDataSource) TimeSeriesDataProvider.getInstance().getDataSource(dataSourceName);
		}
		return dataSource;
	}
	
	/**
	 * Sets a text to the corresponding label that describes the currently configured database connection.
	 * @param sourceConfig the new DB settings label text
	 */
	private void setDBSettingsLabelText(JDBCDataScourceConfiguration sourceConfig) {
		String labelText = null;
		if (sourceConfig!=null) {
			Properties dbProps = sourceConfig.getDatabaseSettings();
			if (dbProps!=null) {
				String jdbcURL = dbProps.getProperty("hibernate.connection.url");
				String dbHost = this.getHostFromJdbcURL(jdbcURL);
				String dbName = dbProps.getProperty("hibernate.default_catalog");
				String dbmsName = sourceConfig.getDbmsName();
				
				if (dbHost!=null && dbName!=null && dbmsName!=null) {
					labelText = "Database " + dbName + " on " + dbHost + " (" + dbmsName + ")";
				}
			}
		}
		
		this.getJLabelCurrentDBConfig().setText(labelText!=null ? labelText :  "No database connection configured!");
	}
	
	/**
	 * Extracts the host part from a JDBC URL
	 * @param jdbcURL the jdbc URL
	 * @return the host
	 */
	private String getHostFromJdbcURL(String jdbcURL) {
		if (jdbcURL==null) return null;
		int startPos = jdbcURL.indexOf("//")+2;
		int endPos = jdbcURL.indexOf("/", startPos);
		return jdbcURL.substring(startPos, endPos);
	}
	
	/**
	 * Gets the date time formatter.
	 * @return the date time formatter
	 */
	private DateTimeFormatter getDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
		}
		return dateTimeFormatter;
	}
}
