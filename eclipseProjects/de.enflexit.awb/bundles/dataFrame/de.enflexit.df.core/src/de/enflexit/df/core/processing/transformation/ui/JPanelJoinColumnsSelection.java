package de.enflexit.df.core.processing.transformation.ui;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.enflexit.df.core.processing.transformation.DataTransformationJoin;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.ui.JPanelTransformationGraphEditor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * This panel allows to select one column from each involved table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelJoinColumnsSelection extends AbstractTransformationParameterConfigurationPanel {

	private static final long serialVersionUID = 9163445036477367442L;
	
	private JLabel jLabelHeader;
	private JScrollPane jScrollPaneSelectionTable;
	private JButton jButtonVerify;
	
	private JTable jTableSelectColumns;
	private JoinConfigurationTableModel tableSelectionModel;
	
	private JPanelTransformationGraphEditor parentEditorPanel;

	/**
	 * Instantiates a new j panel join columns selection.
	 * @param transformation the transformation
	 */
	public JPanelJoinColumnsSelection(DataTransformationService transformation, JPanelTransformationGraphEditor parentEditorPanel) {
		super(transformation);
		this.parentEditorPanel = parentEditorPanel;
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.insets = new Insets(0, 0, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jButtonVerify = new GridBagConstraints();
		gbc_jButtonVerify.anchor = GridBagConstraints.EAST;
		gbc_jButtonVerify.insets = new Insets(0, 0, 0, 0);
		gbc_jButtonVerify.gridx = 1;
		gbc_jButtonVerify.gridy = 0;
		add(getJButtonVerify(), gbc_jButtonVerify);
		GridBagConstraints gbc_jScrollPaneSelectionTable = new GridBagConstraints();
		gbc_jScrollPaneSelectionTable.gridwidth = 2;
		gbc_jScrollPaneSelectionTable.insets = new Insets(0, 0, 0, 0);
		gbc_jScrollPaneSelectionTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneSelectionTable.gridx = 0;
		gbc_jScrollPaneSelectionTable.gridy = 1;
		add(getJScrollPaneSelectionTable(), gbc_jScrollPaneSelectionTable);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Select table columns for joining");
			jLabelHeader.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelHeader;
	}
	private JScrollPane getJScrollPaneSelectionTable() {
		if (jScrollPaneSelectionTable == null) {
			jScrollPaneSelectionTable = new JScrollPane();
			jScrollPaneSelectionTable.setViewportView(getJTableSelectColumns());
		}
		return jScrollPaneSelectionTable;
	}
	private JButton getJButtonVerify() {
		if (jButtonVerify == null) {
			jButtonVerify = new JButton("Verify");
			jButtonVerify.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jButtonVerify;
	}
	private JTable getJTableSelectColumns() {
		if (jTableSelectColumns == null) {
			jTableSelectColumns = new JTable();
			jTableSelectColumns.setModel(this.getTableSelectionModel());
			jTableSelectColumns.getColumnModel().getColumn(1).setCellEditor(new JoinConfigurationTableCellEditor(this.getTableSelectionModel()));
			jTableSelectColumns.getColumnModel().getColumn(0).setCellRenderer(new JoinConfigurationTableCellRenderer());
			jTableSelectColumns.getColumnModel().getColumn(1).setCellRenderer(new JoinConfigurationTableCellRenderer());
			
		}
		return jTableSelectColumns;
	}
	
	/**
	 * Gets the table selection model.
	 * @return the table selection model
	 */
	private JoinConfigurationTableModel getTableSelectionModel() {
		if (tableSelectionModel==null) {
			tableSelectionModel = new JoinConfigurationTableModel();
		}
		return tableSelectionModel;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(JPanelDataTransformationConfiguration.EVENT_ID_TABLE_ADDED)) {
			this.getTableSelectionModel().addTable((DataTableNode) pce.getNewValue());
		} else if (pce.getPropertyName().equals(JPanelDataTransformationConfiguration.EVENT_ID_TABLE_REMOVED)) {
			this.getTableSelectionModel().removeTable((DataTableNode) pce.getOldValue());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Map<String, Object> getConfiguredParameters() {

		// --- Collect the selected join columns for all tables -----
		HashMap<Table, Column<?>> joinColumnsMap = new HashMap<Table, Column<?>>();
		for (int i=0; i<this.getTableSelectionModel().getRowCount(); i++) {
			Table joinTable = ((DataTableNode) this.getTableSelectionModel().getValueAt(i, 0)).getDataTable();
			Column<?> joinColumn = (Column<?>) this.getTableSelectionModel().getValueAt(i, 1);
			joinColumnsMap.put(joinTable, joinColumn);
		}

		// --- Wrap in a String Object HashMap to match the generic interface
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(DataTransformationJoin.PARAM_NAME_JOIN_COLUMNS, joinColumnsMap);
		return parameters;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(java.util.Map)
	 */
	@Override
	public void setConfiguredParameters(Map<String, Object> parameters) {
		if (parameters!=null) {
			Object joinConfigObject = parameters.get(DataTransformationJoin.PARAM_NAME_JOIN_COLUMNS);
			
			if (DataTransformationJoin.checkConfigurationParameterParamType(joinConfigObject)==true) {
				
				@SuppressWarnings("unchecked")
				HashMap<Table, Column<?>> joinConfigMap = (HashMap<Table, Column<?>>) joinConfigObject;
				for (Table joinTable : joinConfigMap.keySet()) {
					
					DataTableNode tableNode = this.parentEditorPanel.findCorrespondingTableNode(joinTable);
					
					if (tableNode==null) {
						System.err.println("[" + this.getClass().getSimpleName() + "] No table node found for table " + joinTable.name());
					} else {
						Column<?> joinColumn = joinConfigMap.get(joinTable);
						this.getTableSelectionModel().addTable(tableNode, joinColumn);
					}
					
				}
				
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] Invalid parameter provided for " + DataTransformationJoin.PARAM_NAME_JOIN_COLUMNS + ", must be a HashMap<Table, Column<?>>");
			}
		}
	}
	
	
	
	/**
	 * Wrapper class for the handling of table column selections.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class JoinConfigurationTableEntry {
		private final DataTableNode tableNode;
		private Column<?> selectedColumn;

		public JoinConfigurationTableEntry(DataTableNode table) {
			this.tableNode = table;
			if (table.getDataTable().columnCount() > 0) {
				this.selectedColumn = table.getDataTable().column(0);
			}
		}
		public DataTableNode getTableNode() {
			return tableNode;
		}
		
		public void setSelectedColumn(Column<?> selectedColumn) {
			this.selectedColumn = selectedColumn;
		}
		public Column<?> getSelectedColumn() {
			return selectedColumn;
		}
	}
	
	/**
	 * Custom table model for handling the selection of one column per table.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class JoinConfigurationTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 9002990473734098303L;
		private ArrayList<JoinConfigurationTableEntry> selections;
		
		private ArrayList<JoinConfigurationTableEntry> getSelections() {
			if (selections==null) {
				selections = new ArrayList<JPanelJoinColumnsSelection.JoinConfigurationTableEntry>();
			}
			return selections;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		@Override
		public int getRowCount() {
			return this.getSelections().size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return 2;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int column) {
			return switch(column) {
				case 0 -> "Table";
				case 1 -> "Column";
				default -> throw new IllegalArgumentException();
			};
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex==1;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			JoinConfigurationTableEntry selection = this.getSelections().get(rowIndex);
			
			return switch(columnIndex) {
				case 0 -> selection.getTableNode();
				case 1 -> selection.getSelectedColumn();
				default -> throw new IllegalArgumentException();
			};
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex==1) {
				JoinConfigurationTableEntry selection = selections.get(rowIndex);
				selection.setSelectedColumn((Column<?>) aValue);
				this.fireTableCellUpdated(rowIndex, columnIndex);
			}
		}
		
		/**
		 * Adds the table.
		 * @param tableNode the table
		 */
		public void addTable(DataTableNode tableNode) {
			this.addTable(tableNode, null);
		}
		
		/**
		 * Adds a table node to the table model, setting the provided column as selected.
		 * @param tableNode the table node
		 * @param selectedColumn the selected column
		 */
		public void addTable(DataTableNode tableNode, Column<?> selectedColumn) {
			
			// --- Make sure the same table node is not added twice -
			if (this.findSelectionForTableNode(tableNode)==null) {
				int row = this.getSelections().size();
				
				JoinConfigurationTableEntry tableSelection = new JoinConfigurationTableEntry(tableNode);
				if (selectedColumn!=null) {
					tableSelection.setSelectedColumn(selectedColumn);
				}
				this.getSelections().add(tableSelection);
				this.fireTableRowsInserted(row, row);
			}
		}
		
		
		/**
		 * Removes the table.
		 * @param tableNode the table
		 */
		public void removeTable(DataTableNode tableNode) {
			JoinConfigurationTableEntry selection = this.findSelectionForTableNode(tableNode);
			if (selection!=null) {
				int rowIndex = this.getSelections().indexOf(selection); 
				this.getSelections().remove(rowIndex);
				this.fireTableRowsDeleted(rowIndex, rowIndex);
			}
		}
		
		/**
		 * Finds the table selection object for the provided table.
		 * @param tableNode the table
		 * @return the table selection
		 */
		private JoinConfigurationTableEntry findSelectionForTableNode(DataTableNode tableNode) {
			for (JoinConfigurationTableEntry selection : this.getSelections()) {
				if (selection.getTableNode()==tableNode) {
					return selection;
				}
			}
			return null;
		}
		
		public JoinConfigurationTableEntry getSelection(int row) {
	        return selections.get(row);
	    }
	}
	
	/**
	 * ComboBox-based cell editor for column selection.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class JoinConfigurationTableCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 9023251161016795128L;
		private JComboBox<Column<?>> comboBox;
		private JoinConfigurationTableModel model;

		public JoinConfigurationTableCellEditor(JoinConfigurationTableModel model) {
			super();
			this.model = model;
		}

		/* (non-Javadoc)
		 * @see javax.swing.CellEditor#getCellEditorValue()
		 */
		@Override
		public Object getCellEditorValue() {
			return this.getComboBox().getSelectedItem();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
		 */
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			
			JoinConfigurationTableEntry selection = model.getSelection(table.convertRowIndexToModel(row));

			this.getComboBox().removeAllItems();
			
			for (Column<?> col : selection.getTableNode().getDataTable().columns()) {
	            this.getComboBox().addItem(col);
	        }

	        this.getComboBox().setSelectedItem(selection.getSelectedColumn());

	        return comboBox;
		}
		
		/**
		 * Gets the combo box.
		 * @return the combo box
		 */
		private JComboBox<Column<?>> getComboBox() {
			if (comboBox==null) {
				comboBox = new JComboBox<Column<?>>();
				comboBox.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (comboBox.getSelectedItem()!=null) {
							JoinConfigurationTableCellEditor.this.stopCellEditing();
						}
					}
				});
				
				comboBox.setRenderer(new DefaultListCellRenderer() {
					@Override
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
						super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
						if (value instanceof Column<?> column) {
							this.setText(column.name());
						}
						return this;
					}
				});
			}
			return comboBox;
		}
		
	}
	
	/**
	 * Simple table cell renderer, showing the name of the provided table or column
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class JoinConfigurationTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -7939348078905414343L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if (value instanceof DataTableNode) {
				this.setText(((DataTableNode)value).getLabelText());
			} else if (value instanceof Column<?>) {
				this.setText(((Column<?>)value).name());
			}
			
			return this;
		}
	}

	
	
}
