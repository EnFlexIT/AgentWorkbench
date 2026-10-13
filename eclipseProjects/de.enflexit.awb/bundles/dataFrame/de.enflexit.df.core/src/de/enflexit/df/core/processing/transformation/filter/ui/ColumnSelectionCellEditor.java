package de.enflexit.df.core.processing.transformation.filter.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import tech.tablesaw.columns.Column;

/**
 * {@link TableCellEditor} for selecting table saw columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnSelectionCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -3169525095124103849L;
	
	private final JComboBox<Column<?>> columnSelectionComboBox = new JComboBox<>();

	/**
	 * Instantiates a new table saw row filter column cell editor.
	 */
	public ColumnSelectionCellEditor() {
		columnSelectionComboBox.setRenderer(new ColumnListCellRenderer());
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return columnSelectionComboBox.getSelectedItem();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		TableSawRowFilterTableModel model = (TableSawRowFilterTableModel) table.getModel();

		columnSelectionComboBox.removeAllItems();

		for (Column<?> c : model.getAvailableColumns()) {
			columnSelectionComboBox.addItem(c);
		}

		columnSelectionComboBox.setSelectedItem(value);
		SwingUtilities.invokeLater(this.columnSelectionComboBox::showPopup);
		return columnSelectionComboBox;
	}
	
	/**
	 * List cell renderer that shows the name for a RableSaw column object.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class ColumnListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -2055378427507130722L;

		/* (non-Javadoc)
		 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Column<?> column) {
				setText(column.name());
			} else {
				setText("");
			}

			return this;
		}
	}

}
