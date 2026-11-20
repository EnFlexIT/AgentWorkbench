package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	private JComboBox<DataOperation> comboBoxCellEditor;

	private final ColumnOperationTableModel tableModel;

	/**
	 * Instantiates a new table cell editor operation selection.
	 *
	 * @param tableModel the table model
	 */
	public TableCellEditorOperationSelection(ColumnOperationTableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	/**
	 * Gets the combo box cell editor.
	 * @return the combo box cell editor
	 */
	private JComboBox<DataOperation> getComboBoxCellEditor() {
		if (comboBoxCellEditor==null) {
			comboBoxCellEditor = new JComboBox<DataOperation>();
			comboBoxCellEditor.setRenderer(new DefaultListCellRenderer() {
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
			comboBoxCellEditor.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TableCellEditorOperationSelection.this.stopCellEditing();
				}
			});
				
		}
		return comboBoxCellEditor;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		int modelRow = table.convertRowIndexToModel(row);

        ColumnOperationDefinition rule = tableModel.getRule(modelRow);

        // --- Populate the ComboBox with the operations that are applicable for the selected column.  
        this.getComboBoxCellEditor().removeAllItems();
        this.getComboBoxCellEditor().addItem(null);
        if (rule.getColumnName()!=null) {
        	this.tableModel.getOperations(rule.getColumnName()).forEach(comboBoxCellEditor::addItem);
        }
        this.getComboBoxCellEditor().setSelectedItem(value);
		
		return this.getComboBoxCellEditor();
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.getComboBoxCellEditor().getSelectedItem();
	}
}
