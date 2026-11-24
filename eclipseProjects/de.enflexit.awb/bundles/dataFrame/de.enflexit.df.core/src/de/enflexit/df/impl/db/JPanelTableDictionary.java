package de.enflexit.df.impl.db;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The Class JPanelTableDictionary.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTableDictionary extends JPanel {

	private static final long serialVersionUID = 3871846230244677856L;
	
	//--- Identifiers for property change events ----------
	public static final String TABLE_SELECTION_CHANGED = "TableSelectionChanged";
	public static final String COLUMN_SELECTION_CHANGED = "ColumnSelecitonChanged";
	
	private static final int COLUMN_INDEX_COLUMN_SELECTED = 1;
	private static final int COLUMN_INDEX_COLUMN_NAME = 2;
	
	private TableDictionary tableDictionary;
	
	private DatabaseDataSourceIntegration dataSourceIntegration;
	
	private JLabel jLLabelHeaderTable;
	private JScrollPane jScrollPaneTables;
	private DefaultListModel<String> listModelTables;
	private JList<String> jListTables;

	private JScrollPane jScrollPaneColumns;
	private JLabel jLabelHeaderColumn;
	private DefaultTableModel tableModelColumns;
	private JTable jTableColumns;
	
	private HashMap<String, HashMap<String, String>> columnDescriptionsByTableName;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	/**
	 * Instantiates a new JPanelTableDictionary.
	 */
	public JPanelTableDictionary(DatabaseDataSourceIntegration dataSourceIntegration) {
		this(null, dataSourceIntegration);
	}
	/**
	 * Instantiates a new JPanelTableDictionary.
	 * @param tableDictionary the {@link TableDictionary} to display
	 */
	public JPanelTableDictionary(TableDictionary tableDictionary, DatabaseDataSourceIntegration dataSourceIntegration) {
		this.initialize();
		this.setTableDictionary(tableDictionary, dataSourceIntegration);
	}
	/**
	 * Sets the table dictionary.
	 * @param tableDictionary the new table dictionary
	 */
	public void setTableDictionary(TableDictionary tableDictionary, DatabaseDataSourceIntegration dataSourceIntegration) {
		this.tableDictionary = tableDictionary;
		this.dataSourceIntegration = dataSourceIntegration;
		this.fillListModelTables();
		this.fillTableModelTableColumns();
	}
	
	/**
	 * Gets the table dictionary.
	 * @return the table dictionary
	 */
	protected TableDictionary getTableDictionary() {
		return tableDictionary;
	}
	
	/**
	 * Gets the name of the selected table.
	 * @return the selected table
	 */
	protected String getSelectedTable() {
		return this.getJListTables().getSelectedValue();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 300, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 2.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLLabelHeaderTable = new GridBagConstraints();
		gbc_jLLabelHeaderTable.anchor = GridBagConstraints.WEST;
		gbc_jLLabelHeaderTable.gridx = 0;
		gbc_jLLabelHeaderTable.gridy = 0;
		this.add(this.getJLLabelHeaderTable(), gbc_jLLabelHeaderTable);
		GridBagConstraints gbc_jLabelHeaderColumn = new GridBagConstraints();
		gbc_jLabelHeaderColumn.insets = new Insets(0, 10, 0, 0);
		gbc_jLabelHeaderColumn.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderColumn.gridx = 1;
		gbc_jLabelHeaderColumn.gridy = 0;
		this.add(this.getJLabelHeaderColumn(), gbc_jLabelHeaderColumn);
		GridBagConstraints gbc_jScrollPaneTables = new GridBagConstraints();
		gbc_jScrollPaneTables.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneTables.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTables.gridx = 0;
		gbc_jScrollPaneTables.gridy = 1;
		this.add(this.getJScrollPaneTables(), gbc_jScrollPaneTables);
		GridBagConstraints gbc_jScrollPaneColumns = new GridBagConstraints();
		gbc_jScrollPaneColumns.insets = new Insets(5, 10, 0, 0);
		gbc_jScrollPaneColumns.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneColumns.gridx = 1;
		gbc_jScrollPaneColumns.gridy = 1;
		this.add(this.getJScrollPaneColumns(), gbc_jScrollPaneColumns);
	}
	
	
	private JLabel getJLLabelHeaderTable() {
		if (jLLabelHeaderTable == null) {
			jLLabelHeaderTable = new JLabel("Tables:");
			jLLabelHeaderTable.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLLabelHeaderTable;
	}
	private JScrollPane getJScrollPaneTables() {
		if (jScrollPaneTables == null) {
			jScrollPaneTables = new JScrollPane();
			jScrollPaneTables.setViewportView(getJListTables());
		}
		return jScrollPaneTables;
	}
	private DefaultListModel<String> getListModelTables() {
		if (listModelTables==null) {
			listModelTables = new DefaultListModel<>();
		}
		return listModelTables;
	}
	private JList<String> getJListTables() {
		if (jListTables == null) {
			jListTables = new JList<>(this.getListModelTables());
			jListTables.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListTables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lsEv) {
					if (lsEv.getValueIsAdjusting()==true) return;
					JPanelTableDictionary.this.fillTableModelTableColumns();
					// --- Notify listeners that a different table was selected -------
					JPanelTableDictionary.this.notifyTableSelectionChandeg();
				}
			});
		}
		return jListTables;
	}
	private void fillListModelTables() {
		this.getListModelTables().clear();
		if (this.tableDictionary!=null) {
			this.tableDictionary.getTablesAndViews().forEach(tb -> this.getListModelTables().addElement(tb));
		}
	}
	
	private JLabel getJLabelHeaderColumn() {
		if (jLabelHeaderColumn == null) {
			jLabelHeaderColumn = new JLabel("Columns:");
			jLabelHeaderColumn.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderColumn;
	}
	private JScrollPane getJScrollPaneColumns() {
		if (jScrollPaneColumns == null) {
			jScrollPaneColumns = new JScrollPane();
			jScrollPaneColumns.setViewportView(getJTableColumns());
		}
		return jScrollPaneColumns;
	}
	private DefaultTableModel getTableModelTableColumns() {
		if (tableModelColumns==null) {
			
			Vector<String> columnHeader = new Vector<>();
			columnHeader.add("TableColumn");
			columnHeader.add("Select");
			columnHeader.add("Column Name");
			columnHeader.add("Column Description");
			columnHeader.add("Data Type");
			columnHeader.add("Size");
			columnHeader.add("nullable");
			
			tableModelColumns = new DefaultTableModel(null, columnHeader) {
				@Override
				public boolean isCellEditable(int row, int column) {
					// --- Only the select column can be changed ----
					return column==1;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					Class<?> clazz = null;
					switch (columnIndex) {
					case 0:
						clazz = TableColumn.class;
						break;
					case 2, 3, 4: 
						clazz = String.class;
						break;
					case 5:
						clazz = Integer.class;
						break;
					case 1, 6:
						clazz = Boolean.class;
						break;
					default:
						clazz = super.getColumnClass(columnIndex);
						break;
					}
					return clazz;
				}
			};

			// --- Register a listener to notify about column selection changes
			tableModelColumns.addTableModelListener(new TableModelListener() {
				/* (non-Javadoc)
				 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
				 */
				@Override
				public void tableChanged(TableModelEvent tme) {
					if (tme.getColumn()==COLUMN_INDEX_COLUMN_SELECTED) {
						JPanelTableDictionary.this.notifyColumnSelectionChanged();
					}
				}
			});
		}
		return tableModelColumns;
	}
	private void fillTableModelTableColumns() {
		this.fillTableModelTableColumns(this.getJListTables().getSelectedValue());
	}
	private void fillTableModelTableColumns(String tableName) {
		
		if (this.tableDictionary==null || tableName==null || tableName.isBlank()==true) return;
		List<TableColumn> tcList = this.tableDictionary.getTableColumnList(tableName);
		if (tcList==null) return;
		this.getTableModelTableColumns().setRowCount(0);
		tcList.forEach(tc -> this.addTableColumnToTableModel(tc, this.getColumnDescriptionsForTable(tableName)));
	}
	private void addTableColumnToTableModel(TableColumn tc, HashMap<String, String> comumnDescriptions) {
		
		Vector<Object> dataRow = new Vector<>();
		dataRow.add(tc);
		dataRow.add(true);	// --- Include in selection by default
		dataRow.add(tc.getColumnName());
		dataRow.add(comumnDescriptions!=null ? comumnDescriptions.get(tc.getColumnName()) : null);
		dataRow.add(tc.getDataType());
		dataRow.add(tc.getSize());
		dataRow.add(tc.isNullable());
		this.getTableModelTableColumns().addRow(dataRow);
	}
	
	private JTable getJTableColumns() {
		if (jTableColumns == null) {
			jTableColumns = new JTable(this.getTableModelTableColumns());
			jTableColumns.setFillsViewportHeight(true);
			jTableColumns.setFont(new Font("Dialog", Font.PLAIN, 12));
			
			// --- Define the column widths -------------------------
			TableColumnModel colModel = jTableColumns.getColumnModel();
			// --- TableColumn instance ---------
			colModel.getColumn(0).setPreferredWidth(0);
			colModel.getColumn(0).setMinWidth(0);
			colModel.getColumn(0).setMaxWidth(0);
			// --- Include in select statement? -
			colModel.getColumn(1).setPreferredWidth(80);
			colModel.getColumn(1).setMinWidth(60);
			colModel.getColumn(1).setMaxWidth(100);
			
			// --- Column Name ------------------
			colModel.getColumn(2).setPreferredWidth(180);
			colModel.getColumn(2).setMinWidth(160);
			colModel.getColumn(2).setMaxWidth(240);
			// --- Column Description -----------
//			colModel.getColumn(3).setPreferredWidth(100);
//			colModel.getColumn(3).setMinWidth(80);
//			colModel.getColumn(3).setMaxWidth(120);
			// --- Data Type --------------------		
			colModel.getColumn(4).setPreferredWidth(80);
			colModel.getColumn(4).setMinWidth(60);
			colModel.getColumn(4).setMaxWidth(100);
			// --- Size -------------------------
			colModel.getColumn(5).setPreferredWidth(80);
			colModel.getColumn(5).setMinWidth(60);
			colModel.getColumn(5).setMaxWidth(100);
			// --- nullable ---------------------
			colModel.getColumn(6).setPreferredWidth(80);
			colModel.getColumn(6).setMinWidth(60);
			colModel.getColumn(6).setMaxWidth(100);
			
			jTableColumns.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lsEv) {
					
					if (lsEv.getValueIsAdjusting()==true) return;
					
					
				}
			});
			
		}
		return jTableColumns;
	}
	
	// ----------------------------------------------------
	// --- Methods for handling column descriptions -------
	
	/**
	 * Gets the column descriptions for the specified table.
	 * @param tableName the table name
	 * @return the column descriptions
	 */
	private HashMap<String, String> getColumnDescriptionsForTable(String tableName) {
		HashMap<String, String> colDescsForTable = this.getColumnDescriptionsByTableName().get(tableName);
		if (colDescsForTable == null) {
			colDescsForTable = this.dataSourceIntegration.getDataWorkbook().getExtensionCache().getColumnDescriptionsForDatabaseTable(tableName);
			if (colDescsForTable!=null) {
				this.getColumnDescriptionsByTableName().put(tableName, colDescsForTable);
			}
		}
		return colDescsForTable;
	}
	
	/**
	 * Gets the column descriptions by table name.
	 * @return the column descriptions by table name
	 */
	private HashMap<String, HashMap<String, String>> getColumnDescriptionsByTableName() {
		if (columnDescriptionsByTableName==null) {
			columnDescriptionsByTableName = new HashMap<String, HashMap<String,String>>();
		}
		return columnDescriptionsByTableName;
	}
	
	// ----------------------------------------------------
	// --- Methods for handling column selection ----------
	
	/**
	 * Gets the selected column names.
	 * @return the selected column names
	 */
	private List<String> getSelectedColumnNames(){
		ArrayList<String> selectedColumns = new ArrayList<String>();
		for (int i=0; i<this.getTableModelTableColumns().getRowCount(); i++) {
			Boolean isSelected = (Boolean) this.getTableModelTableColumns().getValueAt(i, COLUMN_INDEX_COLUMN_SELECTED);
			if (isSelected==true) {
				String columnName = (String) this.getTableModelTableColumns().getValueAt(i, COLUMN_INDEX_COLUMN_NAME);
				selectedColumns.add(columnName);
			}
		}
		return selectedColumns;
	}
	
	/**
	 * Gets a query string for column selection based on the currently selected columns.
	 * @return the column selection string
	 */
	protected String getColumnSelectionString() {
		List<String> selectedColumns = this.getSelectedColumnNames();
		if (selectedColumns.size()==this.getTableModelTableColumns().getRowCount()) {
			// --- Select all columns ---------------------
			return "*";
		} else {
			// --- Select individual  columns -------------
			return selectedColumns.stream().collect(Collectors.joining(", "));
		}
	}
	
	private PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport==null) {
			propertyChangeSupport = new PropertyChangeSupport(this);
		}
		return propertyChangeSupport;
	}
	
	// ------------------------------------------------------------------------
	// --- Methods for sending selection changes to other components ----------
	
	/* (non-Javadoc)
	 * @see java.awt.Container#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.getPropertyChangeSupport().addPropertyChangeListener(propertyChangeListener);
	}
	
	/**
	 * Removes a property change listener.
	 * @param propertyChangeListener the property change listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.getPropertyChangeSupport().removePropertyChangeListener(propertyChangeListener);
	}
	
	/**
	 * Sends a notification about a table selection change to the {@link PropertyChangeListener}s.
	 */
	private void notifyTableSelectionChandeg() {
		// --- Notify listeners that a different table was selected -------
		this.getPropertyChangeSupport().firePropertyChange(TABLE_SELECTION_CHANGED, null, this.getJListTables().getSelectedValue());
	}
	
	/**
	 * Sends a notification about a column selection change to the {@link PropertyChangeListener}s.
	 */
	private void notifyColumnSelectionChanged() {
		this.getPropertyChangeSupport().firePropertyChange(COLUMN_SELECTION_CHANGED, null, this.getColumnSelectionString());
	}
	
}
