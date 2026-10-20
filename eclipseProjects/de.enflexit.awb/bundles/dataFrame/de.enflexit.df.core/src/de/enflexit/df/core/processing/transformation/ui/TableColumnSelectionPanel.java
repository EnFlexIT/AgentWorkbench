package de.enflexit.df.core.processing.transformation.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;

import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.processing.transformation.DataTransformationSelectColumns;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.ui.CheckBoxList;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * A simple list panel for selecting columns from table saw tables.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableColumnSelectionPanel extends AbstractTransformationParameterConfigurationPanel {
	
	private static final long serialVersionUID = 7519193216640795836L;
	private JScrollPane jScrollPaneSelectionList;
	private CheckBoxList<Column<?>> jListColumnSelection;
	private DefaultListModel<Column<?>> columnsListModel;
	private JLabel jLabelHeader;

	/**
	 * Instantiates a new j panel table column selection.
	 */
	public TableColumnSelectionPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJLabelHeader(), BorderLayout.NORTH);
		add(getJScrollPaneSelectionList(), BorderLayout.CENTER);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Select columns");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}

	private JScrollPane getJScrollPaneSelectionList() {
		if (jScrollPaneSelectionList == null) {
			jScrollPaneSelectionList = new JScrollPane();
			jScrollPaneSelectionList.setViewportView(getJListColumnSelection());
		}
		return jScrollPaneSelectionList;
	}
	
	private CheckBoxList<Column<?>> getJListColumnSelection() {
		if (jListColumnSelection == null) {
			jListColumnSelection = new CheckBoxList<Column<?>>(this.getColumnsListModel(), Column::name);
		}
		return jListColumnSelection;
	}
	
	private DefaultListModel<Column<?>> getColumnsListModel() {
		if (columnsListModel==null) {
			columnsListModel = new DefaultListModel<Column<?>>();
		}
		return columnsListModel;
	}
	
	/**
	 * Adds the table's columns to the selection list.
	 * @param table the table
	 */
	private void addTable(Table table) {
		for (Column<?> column : table.columns()) {
			this.columnsListModel.addElement(column);
		}
	}
	
	/**
	 * Removes the table's columns from the selection list.
	 * @param table the table
	 */
	private void removeTable(Table table) {
		for (Column<?> column : table.columns()) {
			this.columnsListModel.removeElement(column);
		}
	}
	
	/**
	 * Clears the table columns list.
	 */
	private void clearTableColumnsList() {
		this.getColumnsListModel().clear();
	}
	
	/**
	 * Gets the selected columns.
	 * @return the selected columns
	 */
	public List<Column<?>> getSelectedColumns(){
		List<Column<?>> selectedColumns = new ArrayList<Column<?>>();
		
		for (int i=0; i<this.getColumnsListModel().size(); i++) {
			if (this.getJListColumnSelection().isSelectedIndex(i)) {
				selectedColumns.add(this.getColumnsListModel().getElementAt(i));
			}
		}
		return selectedColumns;
	}
	
	/**
	 * Gets the names of the selected columns.
	 * @return the selected column names
	 */
	public List<String> getSelectedColumnNames(){
		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		for (Column<?> column : this.getSelectedColumns()) {
			selectedColumnNames.add(column.name());
		}
		return selectedColumnNames;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Properties getConfiguredParameters() {
		
		Properties parameterProperties = new Properties();
		
		String columnsString = this.getSelectedColumnNames().stream().collect(Collectors.joining(","));
		parameterProperties.setStringValue(DataTransformationSelectColumns.PROPERTY_NAME_SELECT_COLUMNS, columnsString);
		
		return parameterProperties;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(de.enflexit.common.properties.Properties)
	 */
	@Override
	public void setConfiguredParameters(Properties parametersAsProperties) {
		String columnsString = parametersAsProperties.getStringValue(DataTransformationSelectColumns.PROPERTY_NAME_SELECT_COLUMNS);
		
		if (columnsString==null || columnsString.isBlank()==true) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No property found for required parameter " + DataTransformationSelectColumns.PROPERTY_NAME_SELECT_COLUMNS);
		} else {
			String[] columnNames = columnsString.split(DataTransformationSelectColumns.COLUMN_NAME_SEPARATOR);
			ArrayList<Column<?>> selectedColumnsList = new ArrayList<Column<?>>();
			for (String columnName : columnNames) {
				Column<?> column = this.getColumnByName(columnName);
				if (column!=null) {
					selectedColumnsList.add(column);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_ADDED)) {
			DataTableNode affectedNode = (DataTableNode) pce.getNewValue();
			this.addTable(affectedNode.getDataTable());
		} else if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_REMOVED)) {
			DataTableNode affectedNode = (DataTableNode) pce.getOldValue();
			this.removeTable(affectedNode.getDataTable());
		} else if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_SELECTED)) {
			DataTableNode affectedNode = (DataTableNode) pce.getNewValue();
			this.clearTableColumnsList();
			this.addTable(affectedNode.getDataTable());
		}
	}
	
	/**
	 * Gets the column by name.
	 * @param columnName the column name
	 * @return the column by name
	 */
	private Column<?> getColumnByName(String columnName){
		for (int i=0; i<this.getColumnsListModel().size(); i++) {
			Column<?> column = this.getColumnsListModel().getElementAt(i);
			if (column.name().equals(columnName)) {
				return column;
			}
		}
		return null;
	}

}
