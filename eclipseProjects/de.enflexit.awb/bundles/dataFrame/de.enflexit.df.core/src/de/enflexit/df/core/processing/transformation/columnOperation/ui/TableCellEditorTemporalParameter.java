package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.columnOperation.TemporalParameter;
import de.enflexit.df.core.processing.transformation.columnOperation.TimeUnit;

/**
 * {@link TableCellEditor} for defining time ranges, combining a text field for the numerical value with a combo box for the time unit.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableCellEditorTemporalParameter extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = -535945267720932465L;

	private final JPanel panel = new JPanel(new BorderLayout(5, 0));
	private final JTextField valueField = new JTextField();
	private final JComboBox<TimeUnit> unitComboBox = new JComboBox<>(TimeUnit.values());
	
	/**
	 * Instantiates a new table cell editor temporal parameter.
	 */
	public TableCellEditorTemporalParameter() {
		panel.add(valueField, BorderLayout.CENTER);
		panel.add(unitComboBox, BorderLayout.EAST);

		unitComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof TimeUnit unit) {
					setText(unit.getLabel());
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
		if (value instanceof TemporalParameter parameter) {
			valueField.setText(Long.toString(parameter.getValue()));
			unitComboBox.setSelectedItem(parameter.getUnit());
		} else {
			valueField.setText("");
			unitComboBox.setSelectedItem(TimeUnit.DAYS);
		}

		return panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		try {
			long value = Long.parseLong(valueField.getText().trim());
			return new TemporalParameter(value, (TimeUnit) unitComboBox.getSelectedItem());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.AbstractCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		// --- Accept long values only from the text field ---------- 
		try {
            Long.parseLong(valueField.getText().trim());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(panel, "Please enter a valid integer.", "Invalid parameter", JOptionPane.ERROR_MESSAGE);
			valueField.requestFocusInWindow();
			return false;
        }
		return super.stopCellEditing();
	}

}
