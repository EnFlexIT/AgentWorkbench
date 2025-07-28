package de.enflexit.awb.timeSeriesDataProvider.jdbc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataScourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSource;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSource.ConnectionState;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
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
//	private Connection connection;
	
	private JLabel jLabelJDBCDataSource;
	private JLabel jLabelSourceName;
	private JTextField jTextFieldSourceName;
	
	private JLabel jLabelDBSettings;
	private JLabel jLabelJdbcUrl;
	private JTextField jTextFieldJdbcUrl;
	private JButton jButtonConnect;
	private JLabel jLabelConnectionState;
	
	private JLabel jLabelDataSelection;
	private JButton jButtonAutoCreateSeries;
	private JLabel jLabelSelectTable;
	private JComboBox<String> jComboBoxSelectTable;
	private JButton jButtonLoadData;
	private JLabel jLabelSelectDateTimeColumn;
	private JComboBox<String> jComboBoxSelectDateTimeColumn;
	private JScrollPane jScrollPaneDataTable;
	
	private JTable jTableDataTable;
	
	private JDBCDataSeriesConfigurationPanel jPanelSeriesConfiguration;
	
	private Vector<String> columnNames;
	
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
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelJDBCDataSource = new GridBagConstraints();
		gbc_jLabelJDBCDataSource.anchor = GridBagConstraints.WEST;
		gbc_jLabelJDBCDataSource.gridwidth = 3;
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
		gbc_jTextFieldSourceName.gridwidth = 4;
		gbc_jTextFieldSourceName.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldSourceName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldSourceName.gridx = 1;
		gbc_jTextFieldSourceName.gridy = 1;
		add(getJTextFieldSourceName(), gbc_jTextFieldSourceName);
		GridBagConstraints gbc_jLabelDBSettings = new GridBagConstraints();
		gbc_jLabelDBSettings.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDBSettings.gridx = 0;
		gbc_jLabelDBSettings.gridy = 2;
		add(getJLabelDBSettings(), gbc_jLabelDBSettings);
		GridBagConstraints gbc_jLabelJdbcUrl = new GridBagConstraints();
		gbc_jLabelJdbcUrl.anchor = GridBagConstraints.WEST;
		gbc_jLabelJdbcUrl.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelJdbcUrl.gridx = 0;
		gbc_jLabelJdbcUrl.gridy = 3;
		add(getJLabelJdbcUrl(), gbc_jLabelJdbcUrl);
		GridBagConstraints gbc_jTextFieldJdbcUrl = new GridBagConstraints();
		gbc_jTextFieldJdbcUrl.gridwidth = 4;
		gbc_jTextFieldJdbcUrl.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldJdbcUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldJdbcUrl.gridx = 1;
		gbc_jTextFieldJdbcUrl.gridy = 3;
		add(getJTextFieldJdbcUrl(), gbc_jTextFieldJdbcUrl);
		GridBagConstraints gbc_jLabelConnectionState = new GridBagConstraints();
		gbc_jLabelConnectionState.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelConnectionState.gridx = 5;
		gbc_jLabelConnectionState.gridy = 3;
		add(getJLabelConnectionState(), gbc_jLabelConnectionState);
		GridBagConstraints gbc_jButtonConnect = new GridBagConstraints();
		gbc_jButtonConnect.anchor = GridBagConstraints.WEST;
		gbc_jButtonConnect.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonConnect.gridx = 6;
		gbc_jButtonConnect.gridy = 3;
		add(getJButtonConnect(), gbc_jButtonConnect);
		GridBagConstraints gbc_jLabelDataSelection = new GridBagConstraints();
		gbc_jLabelDataSelection.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSelection.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDataSelection.gridx = 0;
		gbc_jLabelDataSelection.gridy = 4;
		add(getJLabelDataSelection(), gbc_jLabelDataSelection);
		GridBagConstraints gbc_jLabelSelectTable = new GridBagConstraints();
		gbc_jLabelSelectTable.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelSelectTable.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSelectTable.gridx = 0;
		gbc_jLabelSelectTable.gridy = 5;
		add(getJLabelSelectTable(), gbc_jLabelSelectTable);
		GridBagConstraints gbc_jComboBoxSelectTable = new GridBagConstraints();
		gbc_jComboBoxSelectTable.gridwidth = 2;
		gbc_jComboBoxSelectTable.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxSelectTable.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectTable.gridx = 1;
		gbc_jComboBoxSelectTable.gridy = 5;
		add(getJComboBoxSelectTable(), gbc_jComboBoxSelectTable);
		GridBagConstraints gbc_jButtonLoadData = new GridBagConstraints();
		gbc_jButtonLoadData.anchor = GridBagConstraints.WEST;
		gbc_jButtonLoadData.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonLoadData.gridx = 3;
		gbc_jButtonLoadData.gridy = 5;
		add(getJButtonLoadData(), gbc_jButtonLoadData);
		GridBagConstraints gbc_jLabelSelectDateTimeColumn = new GridBagConstraints();
		gbc_jLabelSelectDateTimeColumn.anchor = GridBagConstraints.WEST;
		gbc_jLabelSelectDateTimeColumn.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelSelectDateTimeColumn.gridx = 4;
		gbc_jLabelSelectDateTimeColumn.gridy = 5;
		add(getJLabelSelectDateTimeColumn(), gbc_jLabelSelectDateTimeColumn);
		GridBagConstraints gbc_jComboBoxSelectDateTimeColumn = new GridBagConstraints();
		gbc_jComboBoxSelectDateTimeColumn.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxSelectDateTimeColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectDateTimeColumn.gridx = 5;
		gbc_jComboBoxSelectDateTimeColumn.gridy = 5;
		add(getJComboBoxSelectDateTimeColumn(), gbc_jComboBoxSelectDateTimeColumn);
		GridBagConstraints gbc_jButtonAutoCreateSeries = new GridBagConstraints();
		gbc_jButtonAutoCreateSeries.anchor = GridBagConstraints.WEST;
		gbc_jButtonAutoCreateSeries.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonAutoCreateSeries.gridx = 6;
		gbc_jButtonAutoCreateSeries.gridy = 5;
		add(getJButtonAutoCreateSeries(), gbc_jButtonAutoCreateSeries);
		GridBagConstraints gbc_jScrollPaneDataTable = new GridBagConstraints();
		gbc_jScrollPaneDataTable.insets = new Insets(0, 0, 5, 0);
		gbc_jScrollPaneDataTable.gridwidth = 7;
		gbc_jScrollPaneDataTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneDataTable.gridx = 0;
		gbc_jScrollPaneDataTable.gridy = 6;
		add(getJScrollPaneDataTable(), gbc_jScrollPaneDataTable);
		GridBagConstraints gbc_jPanelSeriesConfiguration = new GridBagConstraints();
		gbc_jPanelSeriesConfiguration.gridwidth = 7;
		gbc_jPanelSeriesConfiguration.fill = GridBagConstraints.BOTH;
		gbc_jPanelSeriesConfiguration.gridx = 0;
		gbc_jPanelSeriesConfiguration.gridy = 7;
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
			jTextFieldSourceName.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					JDBCDataSourceConfigurationPanel.this.renameDataSource();
				}
			});
			jTextFieldSourceName.setColumns(10);
		}
		return jTextFieldSourceName;
	}
	
	private JLabel getJLabelDBSettings() {
		if (jLabelDBSettings == null) {
			jLabelDBSettings = new JLabel("Databse Settings");
			jLabelDBSettings.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDBSettings;
	}
	private JLabel getJLabelJdbcUrl() {
		if (jLabelJdbcUrl == null) {
			jLabelJdbcUrl = new JLabel("JDBC URL");
			jLabelJdbcUrl.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelJdbcUrl;
	}
	
	private JTextField getJTextFieldJdbcUrl() {
		if (jTextFieldJdbcUrl == null) {
			jTextFieldJdbcUrl = new JTextField();
			jTextFieldJdbcUrl.setColumns(10);
			jTextFieldJdbcUrl.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					JDBCDataSourceConfigurationPanel.this.setJdbcURL();
				}
			});
		}
		return jTextFieldJdbcUrl;
	}
	private JButton getJButtonConnect() {
		if (jButtonConnect == null) {
			jButtonConnect = new JButton("Connect");
			jButtonConnect.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonConnect.addActionListener(this);
		}
		return jButtonConnect;
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
	private JButton getJButtonLoadData() {
		if (jButtonLoadData == null) {
			jButtonLoadData = new JButton("Load Preview Data");
			jButtonLoadData.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonLoadData.addActionListener(this);
			jButtonLoadData.setEnabled(false);
		}
		return jButtonLoadData;
	}
	private JLabel getJLabelSelectDateTimeColumn() {
		if (jLabelSelectDateTimeColumn == null) {
			jLabelSelectDateTimeColumn = new JLabel("Select Date/Time Column");
			jLabelSelectDateTimeColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSelectDateTimeColumn;
	}
	private JComboBox<String> getJComboBoxSelectDateTimeColumn() {
		if (jComboBoxSelectDateTimeColumn == null) {
			jComboBoxSelectDateTimeColumn = new JComboBox<String>();
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
	public AbstractDataSourceConfiguration getDataSourceConfiguraiton() {
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
			throw new IllegalArgumentException("The provided data source configuration is not for a CSV data source!");
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
		this.getJTextFieldSourceName().setText(this.sourceConfiguration.getName());
		this.getJTextFieldJdbcUrl().setText(this.sourceConfiguration.getJdbcURL());
		
		// --- If a JDBC URL is configured, try to establish a connection
		if (this.sourceConfiguration.getJdbcURL()!=null) {
			this.establishConnection();
		}
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
		String textFieldContent = this.getJTextFieldSourceName().getText();
		if (this.sourceConfiguration.getName().equals(textFieldContent)==false) {
			this.sourceConfiguration.setName(this.getJTextFieldSourceName().getText());
		}
	}
	
	private void setJdbcURL() {
		String textFieldContent = this.getJTextFieldJdbcUrl().getText();
		if (textFieldContent.equals(this.sourceConfiguration.getJdbcURL())==false) {
			this.sourceConfiguration.setJdbcURL(textFieldContent);
		}
	}
	
	private JComboBox<String> getJComboBoxSelectTable() {
		if (jComboBoxSelectTable == null) {
			jComboBoxSelectTable = new JComboBox<String>();
			jComboBoxSelectTable.setModel(this.createComboBoxModel(true, null));
			jComboBoxSelectTable.setEnabled(false);
			jComboBoxSelectTable.addActionListener(this);
		}
		return jComboBoxSelectTable;
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
		if (ae.getSource()==this.getJButtonConnect()) {
			// --- Connect to the configured database
			this.establishConnection();
		} else if (ae.getSource()==this.getJComboBoxSelectTable()) {
			// --- Enable the load data button if a table is selected
			String selectedTable = this.getJComboBoxSelectTable().getItemAt(this.getJComboBoxSelectTable().getSelectedIndex());
			if (selectedTable.equals(COMBO_BOX_NO_SELECTION)==false) {
				this.sourceConfiguration.setTableName(selectedTable);
				this.getJButtonLoadData().setEnabled(true);
			} else {
				this.sourceConfiguration.setTableName(null);
				this.getJButtonLoadData().setEnabled(false);
				this.resetTableDependentComponents();
			}
		} else if (ae.getSource()==this.getJButtonLoadData()) {
			this.loadPreviewData();
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
		}
	}

	/**
	 * Resets all UI components with a content that depends on the selected table.
	 */
	private void resetTableDependentComponents() {
		this.getJTableDataTable().setModel(new DefaultTableModel());
		this.getJComboBoxSelectDateTimeColumn().setModel(this.createComboBoxModel(true, null));
		this.getJButtonLoadData().setEnabled(false);
		this.getJButtonAutoCreateSeries().setEnabled(false);
	}

	/**
	 * Connects to the configured database.
	 */
	private void establishConnection() {
		// --- Establish the JDBC connection ----------------------------------
		boolean connectionSuccess = this.getDataSource().connectToDatabase();
		
		if (connectionSuccess) {
			// --- Request the list of tables ---------------------------------
			this.getJComboBoxSelectTable().setModel(this.getDbTablesComboBoxModel());
			this.getJComboBoxSelectTable().setEnabled(true);
			
			if (this.getSourceConfiguration().getTableName()!=null) {
				// --- If already configured, select the table and load the preview data
				this.getJComboBoxSelectTable().setSelectedItem(this.getSourceConfiguration().getTableName());
				this.loadPreviewData();
			}			
		}
		
		this.updateConnectionStateLabel(dataSource.getConnectionState());
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
				try (ResultSet rs = dbMetaData.getTables(null, "public", "%", null) ){
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
					this.getJComboBoxSelectDateTimeColumn().setModel(this.createComboBoxModel(true, this.columnNames));
					String dateTimeColumn = this.findDateTimeColumn(rsmd);
					if (dateTimeColumn!=null) {
						this.getJComboBoxSelectDateTimeColumn().setSelectedItem(dateTimeColumn);
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
	 * Looks for a column containing date time values in the provided {@link ResultSetMetaData}.
	 * If there is more than one, only the first will be found.
	 * @param resultSetMetaData the result set meta data
	 * @return the name of the column, null if none was found
	 * @throws SQLException the SQL exception
	 */
	private String findDateTimeColumn(ResultSetMetaData resultSetMetaData) throws SQLException {
		for (int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
			if (resultSetMetaData.getColumnTypeName(i).equals("timestamp")) {
				return resultSetMetaData.getColumnName(i);
			}
		}
		return null;
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
				this.getDataSourceConfiguraiton().addDataSeriesConfiguration(seriesConfig);
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
	
}
