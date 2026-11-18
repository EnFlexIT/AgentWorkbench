package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;
import de.enflexit.df.core.processing.transformation.columnOperation.TemporalColumnOperation;
import tech.tablesaw.columns.Column;

/**
 * This {@link TableCellEditor} for the parameter column provides either a TextField-based
 * editor for numerical parameters or a special implementation for temporal parameters, 
 * depending on the type of the currently selected TableSaw column. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorParameter extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -12650684625258769L;

	private TableCellEditorNumericParameter numericParameterCellEditor;
	private TableCellEditorTemporalParameter temporalParameterCellEditor;

	private TableCellEditor activeEditor;

	private final ColumnOperationTableModel model;
	private final JTable jTable;

	/**
	 * Instantiates a new table cell editor parameter.
	 * @param jTable the j table
	 * @param model  the model
	 */
	public TableCellEditorParameter(JTable jTable, ColumnOperationTableModel model) {
		this.model = model;
		this.jTable = jTable;
	}
	
	/**
	 * Gets the cell editor for numeric parameters.
	 * @return the numeric parameter cell editor
	 */
	private TableCellEditorNumericParameter getNumericParameterCellEditor() {
		if (numericParameterCellEditor==null) {
			numericParameterCellEditor = new TableCellEditorNumericParameter(this.jTable, this.model);
		}
		return numericParameterCellEditor;
	}
	
	/**
	 * Gets the cell editor for temporal parameters.
	 * @return the temporal parameter cell editor
	 */
	private TableCellEditorTemporalParameter getTemporalParameterCellEditor() {
		if (temporalParameterCellEditor==null) {
			temporalParameterCellEditor = new TableCellEditorTemporalParameter();
		}
		return temporalParameterCellEditor;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		int modelRow = table.convertRowIndexToModel(row);

		ColumnOperationDefinition rule = model.getRule(modelRow);

		Column<?> sourceColumn = model.findColumnByName(rule.getColumnName());

		if (TemporalColumnOperation.isSupportedTemporalColumn(sourceColumn) == true) {
			activeEditor = this.getTemporalParameterCellEditor();
		} else {
			activeEditor = this.getNumericParameterCellEditor();
		}

		return activeEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return activeEditor != null ? activeEditor.getCellEditorValue() : null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		return activeEditor == null || activeEditor.stopCellEditing() && super.stopCellEditing();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#cancelCellEditing()
	 */
	@Override
	public void cancelCellEditing() {
		if (activeEditor != null) {
			activeEditor.cancelCellEditing();
		}
		super.cancelCellEditing();
	}

}
