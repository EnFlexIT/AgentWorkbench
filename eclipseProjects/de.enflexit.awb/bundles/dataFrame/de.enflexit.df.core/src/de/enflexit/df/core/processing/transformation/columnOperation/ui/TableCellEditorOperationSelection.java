package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.columnOperation.DataOperation;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;

/**
 * TableCellEditor for selecting column operations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorOperationSelection extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -4311264859994351847L;

	private final JComboBox<DataOperation> comboBox = new JComboBox<>();

	private final ColumnOperationTableModel tableModel;

	public TableCellEditorOperationSelection(ColumnOperationTableModel tableModel) {
		this.tableModel = tableModel;
		comboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected,	boolean focus) {

				super.getListCellRendererComponent(list, value, index, selected, focus);

				if (value==null) {
					this.setText(ColumnOperationConfigurationPanel.COMBOBOX_NULL_PLACEHOLDER);
				} else  if (value instanceof DataOperation op) {
					this.setText(op.getDisplayName());
				}

				return this;
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		int modelRow = table.convertRowIndexToModel(row);

        ColumnOperationDefinition rule = tableModel.getRule(modelRow);

        // --- Populate the ComboBox with the operations that are applicable for the selected column.  
        this.comboBox.removeAllItems();
        this.comboBox.addItem(null);
        if (rule.getColumnName()!=null) {
        	this.tableModel.getOperations(rule.getColumnName()).forEach(comboBox::addItem);
        }
        this.comboBox.setSelectedItem(value);
		
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
