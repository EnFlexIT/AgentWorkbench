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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.Insets;
import java.util.List;
import java.util.Vector;

/**
 * The Class JPanelTableDictionary.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTableDictionary extends JPanel {

	private static final long serialVersionUID = 3871846230244677856L;
	
	private TableDictionary tableDictionary;
	
	private JLabel jLLabelHeaderTable;
	private JScrollPane jScrollPaneTables;
	private DefaultListModel<String> listModelTables;
	private JList<String> jListTables;

	private JScrollPane jScrollPaneColumns;
	private JLabel jLabelHeaderColumn;
	private DefaultTableModel tableModelColumns;
	private JTable jTableColumns;

	/**
	 * Instantiates a new JPanelTableDictionary.
	 */
	public JPanelTableDictionary() {
		this(null);
	}
	/**
	 * Instantiates a new JPanelTableDictionary.
	 * @param tableDictionary the {@link TableDictionary} to display
	 */
	public JPanelTableDictionary(TableDictionary tableDictionary) {
		this.initialize();
		this.setTableDictionary(tableDictionary);
	}
	/**
	 * Sets the table dictionary.
	 * @param tableDictionary the new table dictionary
	 */
	public void setTableDictionary(TableDictionary tableDictionary) {
		this.tableDictionary = tableDictionary;
		this.fillListModelTables();
		this.fillTableModelTableColumns();
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
			columnHeader.add("Column Name");
			columnHeader.add("Data Type");
			columnHeader.add("Size");
			columnHeader.add("nullable");
			
			tableModelColumns = new DefaultTableModel(null, columnHeader) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					Class<?> clazz = null;
					switch (columnIndex) {
					case 0:
						clazz = TableColumn.class;
						break;
					case 1, 2: 
						clazz = String.class;
						break;
					case 3:
						clazz = Integer.class;
						break;
					case 4:
						clazz = Boolean.class;
						break;
					default:
						clazz = super.getColumnClass(columnIndex);
						break;
					}
					return clazz;
				}
			};
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
		tcList.forEach(tc -> this.addTableColumnToTableModel(tc));
	}
	private void addTableColumnToTableModel(TableColumn tc) {
		
		Vector<Object> dataRow = new Vector<>();
		dataRow.add(tc);
		dataRow.add(tc.getColumnName());
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
			// --- Column Name ------------------
			//colModel.getColumn(1).setPreferredWidth(100);
			//colModel.getColumn(1).setMinWidth(80);
			//colModel.getColumn(1).setMaxWidth(120);
			// --- Data Type --------------------		
			colModel.getColumn(2).setPreferredWidth(80);
			colModel.getColumn(2).setMinWidth(60);
			colModel.getColumn(2).setMaxWidth(100);
			// --- Size -------------------------
			colModel.getColumn(3).setPreferredWidth(80);
			colModel.getColumn(3).setMinWidth(60);
			colModel.getColumn(3).setMaxWidth(100);
			// --- nullable ---------------------
			colModel.getColumn(4).setPreferredWidth(80);
			colModel.getColumn(4).setMinWidth(60);
			colModel.getColumn(4).setMaxWidth(100);
			
			jTableColumns.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lsEv) {
					
					if (lsEv.getValueIsAdjusting()==true) return;
					
					
				}
			});
			
		}
		return jTableColumns;
	}
}
