package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import tech.tablesaw.columns.Column;

/**
 * Table cell editor for selecting table saw columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorColumnSelection extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -6680389599369085465L;
	
	private JComboBox<Column<?>> comboBoxCellEditor;

	/**
	 * Instantiates a new column selection table cell editor.
	 * @param columns the columns
	 */
	public TableCellEditorColumnSelection(List<Column<?>> columns) {
		this.getComboBoxCellEditor().addItem(null);
		columns.forEach(this.getComboBoxCellEditor()::addItem);
	}
	
	/**
	 * Gets the combo box cell editor.
	 * @return the combo box cell editor
	 */
	private JComboBox<Column<?>> getComboBoxCellEditor() {
		if (comboBoxCellEditor==null) {
			comboBoxCellEditor = new JComboBox<Column<?>>();
			comboBoxCellEditor.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
		
					super.getListCellRendererComponent(list, value, index, selected, focus);
		
					if (value==null) {
						this.setText(ColumnOperationConfigurationPanel.COMBOBOX_NULL_PLACEHOLDER);
					} else if (value instanceof Column<?> column) {
						this.setText(column.name());
					}
		
					return this;
				}
			});
			comboBoxCellEditor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TableCellEditorColumnSelection.this.stopCellEditing();
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
