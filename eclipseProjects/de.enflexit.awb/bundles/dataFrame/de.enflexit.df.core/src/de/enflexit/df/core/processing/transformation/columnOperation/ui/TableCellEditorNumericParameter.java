package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationRegistry;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;

/**
 * TableCellEditor for column operation parameters.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorNumericParameter extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -4165561231611848172L;
	
	private final JTextField textField = new JTextField();

	private final ColumnOperationTableModel tableModel;

	private final JTable jTtable;
	
	/**
	 * Instantiates a new table cell editor operation parameter.
	 * @param jTable the table
	 * @param tableModel the table model
	 */
	public TableCellEditorNumericParameter(JTable jTable, ColumnOperationTableModel tableModel) {
		this.jTtable = jTable;
		this.tableModel = tableModel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		textField.setText(value != null ? value.toString() : "");
		return textField;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return textField.getText();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {

		int viewRow = this.jTtable.getEditingRow();
		int modelRow = this.jTtable.convertRowIndexToModel(viewRow);

		ColumnOperationDefinition rule = this.tableModel.getRule(modelRow);

		try {
			if (rule.getOperationID() == null) {
				throw new IllegalArgumentException("Please select an operation first!");
			}

			ColumnOperationRegistry.getInstance().getOperationByID(rule.getOperationID()).parseParameter(textField.getText());

			return super.stopCellEditing();

		} catch (RuntimeException ex) {

			JOptionPane.showMessageDialog(this.jTtable, ex.getMessage(), "Ungültiger Parameter", JOptionPane.ERROR_MESSAGE);

			textField.requestFocusInWindow();
			return false;
		}
	}

}
