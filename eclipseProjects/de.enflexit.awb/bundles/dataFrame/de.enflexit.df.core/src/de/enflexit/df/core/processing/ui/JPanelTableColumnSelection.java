package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import javax.swing.DefaultListModel;

/**
 * A simple list panel for selecting columns from table saw tables.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelTableColumnSelection extends JPanel {
	
	private static final long serialVersionUID = 7519193216640795836L;
	private JScrollPane jScrollPaneSelectionList;
	private CheckBoxList<Column<?>> jListColumnSelection;
	private DefaultListModel<Column<?>> columnsListModel;
	
	/**
	 * Instantiates a new j panel table column selection.
	 */
	public JPanelTableColumnSelection() {
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJScrollPaneSelectionList(), BorderLayout.CENTER);
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
	public void addTable(Table table) {
		for (Column<?> column : table.columns()) {
			this.columnsListModel.addElement(column);
		}
	}
	
	/**
	 * Removes the table's columns from the selection list.
	 * @param table the table
	 */
	public void removeTable(Table table) {
		for (Column<?> column : table.columns()) {
			this.columnsListModel.removeElement(column);
		}
	}
	
	/**
	 * Clears the table columns list.
	 */
	public void clearTableColumnsList() {
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
	
}
