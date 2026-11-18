package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition.ResultHandling;

/**
 * TableCellEditor for selecting {@link ResultHandling} options.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorResultHandlingSelection extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -4175541173932198400L;
	
	private final JComboBox<ResultHandling> comboBox = new JComboBox<ResultHandling>();
	
	private final ColumnOperationTableModel tableModel;
	
	/**
	 * Instantiates a new table cell editor result handling selection.
	 * @param tableModel the table model
	 */
	public TableCellEditorResultHandlingSelection(ColumnOperationTableModel tableModel) {
		this.tableModel = tableModel;
		this.comboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if (value instanceof ResultHandling rh) {
					this.setText(rh.getLabel());
				}
				
				return this;
			}
		});
		Arrays.asList(ResultHandling.values()).forEach(this.comboBox::addItem);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		int modelRow = table.convertRowIndexToModel(row);
		ColumnOperationDefinition rule = this.tableModel.getRule(modelRow);
		if (rule.getResultHandling()!=null) {
			this.comboBox.setSelectedItem(rule.getResultHandling());
		} else {
			this.comboBox.setSelectedIndex(0);
		}
		return this.comboBox;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.comboBox.getSelectedItem();
	}


}
