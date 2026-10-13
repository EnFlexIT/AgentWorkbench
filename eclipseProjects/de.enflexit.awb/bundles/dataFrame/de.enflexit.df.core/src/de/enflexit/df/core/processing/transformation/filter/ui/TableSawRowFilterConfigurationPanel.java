package de.enflexit.df.core.processing.transformation.filter.ui;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.filter.DataTransformationServiceFilterRows;
import de.enflexit.df.core.processing.transformation.filter.DateTimeInputParser;
import de.enflexit.df.core.processing.transformation.filter.Filters;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterCondition;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterExpression;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterGroup;
import de.enflexit.df.core.processing.transformation.filter.Filters.TableFilter;
import de.enflexit.df.core.processing.transformation.filter.ui.TableSawRowFilterTableModel.FilterRow;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;
import de.enflexit.df.core.processing.transformation.ui.DataTransformationConfigurationPanel;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.ShortColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.temporal.TemporalColumn;

import javax.swing.JLabel;
import java.awt.Font;

public class TableSawRowFilterConfigurationPanel extends AbstractTransformationParameterConfigurationPanel implements ActionListener, PropertyChangeListener {
	
	private static final long serialVersionUID = 4220145948646880891L;
	
	private JButton jButtonAdd;
	private JButton jButtonRemove;
	private JScrollPane jScrollPaneFilterConfigurationTable;
	private JTable jTableFilterConfiguration;
	private TableSawRowFilterTableModel tableModel;
	private JLabel jLabelRowFilters;
	
	/**
	 * Instantiates a new table saw row filter configuration panel.
	 * Only added for window builder compatibility, use the other constructor for actual instantiation.
	 */
	@Deprecated 
	public TableSawRowFilterConfigurationPanel() {
		this(null);
	}
	
	/**
	 * Instantiates a new table saw row filter configuration panel.
	 * @param transformation the transformation
	 */
	public TableSawRowFilterConfigurationPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
	}

	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelRowFilters = new GridBagConstraints();
		gbc_jLabelRowFilters.anchor = GridBagConstraints.WEST;
		gbc_jLabelRowFilters.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelRowFilters.gridx = 0;
		gbc_jLabelRowFilters.gridy = 0;
		add(getJLabelRowFilters(), gbc_jLabelRowFilters);
		GridBagConstraints gbc_jButtonAdd = new GridBagConstraints();
		gbc_jButtonAdd.anchor = GridBagConstraints.EAST;
		gbc_jButtonAdd.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonAdd.gridx = 1;
		gbc_jButtonAdd.gridy = 0;
		add(getJButtonAdd(), gbc_jButtonAdd);
		GridBagConstraints gbc_jButtonRemove = new GridBagConstraints();
		gbc_jButtonRemove.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonRemove.gridx = 2;
		gbc_jButtonRemove.gridy = 0;
		add(getJButtonRemove(), gbc_jButtonRemove);
		GridBagConstraints gbc_jScrollPaneFilterConfigurationTable = new GridBagConstraints();
		gbc_jScrollPaneFilterConfigurationTable.gridwidth = 3;
		gbc_jScrollPaneFilterConfigurationTable.insets = new Insets(0, 0, 0, 5);
		gbc_jScrollPaneFilterConfigurationTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFilterConfigurationTable.gridx = 0;
		gbc_jScrollPaneFilterConfigurationTable.gridy = 1;
		add(getJScrollPaneFilterConfigurationTable(), gbc_jScrollPaneFilterConfigurationTable);
	}

	private JLabel getJLabelRowFilters() {
		if (jLabelRowFilters == null) {
			jLabelRowFilters = new JLabel("Row filters");
			jLabelRowFilters.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelRowFilters;
	}

	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new JButton(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAdd.setSize(26, 26);
			jButtonAdd.addActionListener(this);
		}
		return jButtonAdd;
	}
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemove.setSize(26, 26);
			jButtonRemove.addActionListener(this);
		}
		return jButtonRemove;
	}
	private JScrollPane getJScrollPaneFilterConfigurationTable() {
		if (jScrollPaneFilterConfigurationTable == null) {
			jScrollPaneFilterConfigurationTable = new JScrollPane();
			jScrollPaneFilterConfigurationTable.setViewportView(getJTableFilterConfiguration());
		}
		return jScrollPaneFilterConfigurationTable;
	}
	private JTable getJTableFilterConfiguration() {
		if (jTableFilterConfiguration == null) {
			jTableFilterConfiguration = new JTable();
			jTableFilterConfiguration.setModel(this.getTableModel());
			
			// --- Register custom renderers and editors for the first two columns
			this.registerRenderersAndEditors();
			
		}
		return jTableFilterConfiguration;
	}
	
	private TableSawRowFilterTableModel getTableModel() {
		if (tableModel==null) {
			tableModel = new TableSawRowFilterTableModel(null);
		}
		return tableModel;
	}
	
	private void setTableModel(TableSawRowFilterTableModel tableModel) {
		this.tableModel = tableModel;
		this.getJTableFilterConfiguration().setModel(tableModel);
		this.registerRenderersAndEditors();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAdd()) {
			this.getTableModel().addEmptyRow();
		} else if (ae.getSource()==this.getJButtonRemove()) {
			if (this.getJTableFilterConfiguration().isEditing()==true) {
				TableCellEditor cellEditor = this.getJTableFilterConfiguration().getCellEditor();
				
				if (cellEditor!=null && cellEditor.stopCellEditing()==false) {
					// --- Editing could not be stopped, abort ------
					return;
				}
			}
			this.getTableModel().removeRow(this.getJTableFilterConfiguration().getSelectedRow());
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_SELECTED)) {
			
			// --- Create and set a new table model -----------------
			DataTableNode affectedNode = (DataTableNode) pce.getNewValue();
			this.setTableModel(new TableSawRowFilterTableModel(affectedNode.getDataTable()));
		}
	}
	
	/**
	 * Registers the custom renderers and editors.
	 */
	private void registerRenderersAndEditors() {
		TableColumn columnsColumn = this.getJTableFilterConfiguration().getColumnModel().getColumn(0);
		columnsColumn.setCellEditor(new ColumnSelectionCellEditor());
		columnsColumn.setCellRenderer(new ColumnSelectionCellRenderer());
		TableColumn operationsColumn = this.getJTableFilterConfiguration().getColumnModel().getColumn(1);
		operationsColumn.setCellEditor(new OperationSelectionCellEditor());
		operationsColumn.setCellRenderer(new OperationSelectionCellRenderer());
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Map<String, Object> getConfiguredParameters() {
		List<FilterExpression> filtersList = this.createFiltersList();
		
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(DataTransformationServiceFilterRows.REQUIRED_PARAMETER, filtersList);
		return paramsMap;
	}
	
	/**
	 * Creates a list of filters from the table entries.
	 * @return the list
	 */
	private List<FilterExpression> createFiltersList(){
		List<FilterExpression> filtersList = new ArrayList<FilterExpression>();
		
		for (FilterRow row : this.getTableModel().getRows()) {
			
			// --- No column or operator defined -> skip ------------
			if (row.column()==null || row.operator()==null ) {
				continue;
			}
			
			String columnName = row.column().name();
			
			switch(row.operator()) {
			
				// --- Null comparisons -----------------------------
				case IS_NULL -> filtersList.add(Filters.isNull(columnName));
				case IS_NOT_NULL -> filtersList.add(Filters.isNotNull(columnName));
				
				// --- Between filters (two arguments) --------------
				case BETWEEN -> {
					Object lowerValue = this.parseValue(row.column(), row.value());
					Object upperValue = this.parseValue(row.column(), row.upperValue());
					
					filtersList.add(Filters.between(columnName, lowerValue, upperValue));
				}

				// --- All other compare filters (one argument) -----
				default -> {
					Object value = this.parseValue(row.column(), row.value());
					filtersList.add(Filters.compare(columnName, row.operator(), value));
				}
				
			}
		}
		
		return filtersList;
	}
	

	/**
	 * Parses the provided String value to the type of the provided column, if possible.
	 * @param column the column
	 * @param valueString the value string
	 * @return the parsed object
	 */
	private Object parseValue(Column<?> column, String valueString) {
		
		// --- String column, no further parsing required ----------- 
		if (column instanceof StringColumn) {
			return valueString;
		}
		
		// --- For other types, try to parse accordingly
		try {
		
			// --- Handle integer types -------------------
			if (column instanceof BooleanColumn) {
				return Boolean.parseBoolean(valueString);
			}
			if (column instanceof IntColumn) {
				return Integer.parseInt(valueString);
			}
			if (column instanceof ShortColumn) {
				return Short.parseShort(valueString);
			}
			if (column instanceof LongColumn) {
				return Long.parseLong(valueString);
			}
			
			// --- Handle floating point types ------------
			if (column instanceof DoubleColumn) {
				return Double.parseDouble(valueString);
			}
			if (column instanceof FloatColumn) {
				return Float.parseFloat(valueString);
			}
			
			// --- Delegate temporal types to a separate parser class
			if (column instanceof TemporalColumn<?>) {
				return DateTimeInputParser.parse(valueString);
			}
		
		// --- Parsing error -> input does not match target type
		} catch (NumberFormatException | DateTimeParseException ex) {
			throw new IllegalArgumentException("Value " + valueString + " is nof of suitable format for column type " + column.type()); 
		}
		
		// --- Column type not recognized (should never occur)
		throw new IllegalArgumentException("Unsupported column type " + column.type());
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(java.util.Map)
	 */
	@Override
	public void setConfiguredParameters(Map<String, Object> parameters) {
		@SuppressWarnings("unchecked")
		List<FilterExpression> filtersList = (List<FilterExpression>) parameters.get(DataTransformationServiceFilterRows.REQUIRED_PARAMETER);
		for (FilterExpression fe : filtersList) {
			this.addFilterExpressionToTableModel(fe);
		}
	}
	
	/**
	 * Adds the filter expression to the table model.
	 * @param filterExpression the filter expression
	 */
	private void addFilterExpressionToTableModel(FilterExpression filterExpression){
		if (filterExpression instanceof FilterCondition fc) {
			this.addTableRowForFilter(fc);
		} else if (filterExpression instanceof FilterGroup fg) {
			this.addTableRowsForFilterGroup(fg);
		}
	}
	
	/**
	 * Adds the table rows for a filter group to the table.
	 * @param filterGroup the filter group
	 */
	private void addTableRowsForFilterGroup(FilterGroup filterGroup) {
		for (FilterExpression fe : filterGroup.expressions()) {
			this.addFilterExpressionToTableModel(fe);
		}
	}
	
	/**
	 * Adds the table row for a single filter to the table.
	 * @param filterCondition the filter condition
	 */
	private void addTableRowForFilter(FilterCondition filterCondition) {
		TableFilter tableFilter = filterCondition.filter();
		this.getTableModel().addRow(tableFilter);
	}
}
