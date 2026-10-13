package de.enflexit.df.core.processing.transformation.ui;

import java.awt.Color;
import java.awt.Component;
import java.time.format.DateTimeParseException;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;

import de.enflexit.df.core.processing.transformation.filter.DateTimeInputParser;

/**
 * Textfield-based TableCellEditor with date/time format verification.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DateTimeCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 3243462611019919166L;
	
	private final JTextField field = new JTextField();
	private final javax.swing.border.Border normalBorder = field.getBorder();

	/**
	 * Instantiates a new date time cell editor.
	 */
	public DateTimeCellEditor() {
		field.getDocument().addDocumentListener(new DocumentListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				validateInput();
			}

			/* (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				validateInput();
			}

			/* (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
			 */
			@Override
			public void changedUpdate(DocumentEvent e) {
				validateInput();
			}
		});

		field.addActionListener(e -> stopCellEditing());
		field.setToolTipText("dd.MM.yyyy [HH:mm[:ss]]");
	}

	private boolean isValidInput() {
		try {
			DateTimeInputParser.parse(field.getText());
			return true;
		} catch (DateTimeParseException ex) {
			return false;
		}
	}

    /**
     * Validates the input against the list of accepted formats.
     */
	private void validateInput() {
		if (field.getText().isBlank()) {
			field.setBorder(normalBorder);
			field.setToolTipText("Bitte ein Datum eingeben.");
		} else if (isValidInput()) {
			field.setBorder(BorderFactory.createLineBorder(new Color(40, 150, 70)));
			field.setToolTipText("Gültige Datums-/Zeitangabe");
		} else {
			field.setBorder(normalBorder);
			field.setToolTipText("Erlaubt: dd.MM.yyyy, dd.MM.yyyy HH:mm " + "oder dd.MM.yyyy HH:mm:ss");
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		field.setText(value == null ? "" : value.toString());
		field.setBorder(normalBorder);
		validateInput();

		SwingUtilities.invokeLater(field::selectAll);
		return field;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return field.getText().trim();
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractCellEditor#stopCellEditing()
     */
    @Override
    public boolean stopCellEditing() {
        if (!isValidInput()) {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            field.setToolTipText("Invalid date/time entry, please check tho format of your input!");
            field.requestFocusInWindow();
            return false;
        }

        return super.stopCellEditing();
    }

}
