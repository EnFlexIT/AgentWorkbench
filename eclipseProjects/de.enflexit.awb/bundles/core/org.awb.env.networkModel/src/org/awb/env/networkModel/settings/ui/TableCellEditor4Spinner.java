package org.awb.env.networkModel.settings.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.enflexit.common.swing.AwbThemeColor;

/**
 * The Class TableCellEditor4Spinner.
 */
public class TableCellEditor4Spinner extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = -1544056145162025328L;

	private double minimum, maximum, stepSize;
	private JSpinner jSpinner = null;
	 
	/**
	 * Instantiates a new table cell editor 4 a JSpinner.
	 *
	 * @param minimum the minimum value
	 * @param maximum the maximum value
	 * @param stepSize the step size for the spinner
	 */
	public TableCellEditor4Spinner(double minimum, double maximum, double stepSize) { 
		this.minimum  = minimum;
		this.maximum  = maximum;
		this.stepSize = stepSize;
	}
	
	/**
	 * Returns the spinner of this cell editor.
	 * @return the JPpinner to use
	 */
	private JSpinner getJSpinner() {
		if (jSpinner==null) {
			jSpinner = new JSpinner(new SpinnerNumberModel(5.0, this.minimum, this.maximum, this.stepSize));
			jSpinner.setEditor(new JSpinner.NumberEditor(jSpinner, "0.0"));
			jSpinner.setFont(new Font("Dialog", Font.PLAIN, 12));

			// --- Remove the border !! -------------------
			Border borderToUse = BorderFactory.createEmptyBorder();
			jSpinner.setBorder(borderToUse);
			for (int i = 0; i < jSpinner.getComponents().length; i++) {
				if (jSpinner.getComponent(i) instanceof JSpinner.NumberEditor) {
					// --- Edit the number editor ---------
					JSpinner.NumberEditor numEditor = (NumberEditor) jSpinner.getComponent(i); 
					numEditor.setBorder(borderToUse);
					numEditor.getTextField().setBorder(borderToUse);
					numEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
					
					for (int j = 0; j < numEditor.getComponentCount(); j++) {
						if (numEditor.getComponent(j) instanceof JFormattedTextField) {
							JFormattedTextField jfTextField = (JFormattedTextField) numEditor.getComponent(j);
							jfTextField.setBorder(borderToUse);
						}
					} 
				}
			}
		}
		return jSpinner;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent comp = (JComponent) table.getCellRenderer(row, 0);
		this.getJSpinner().setValue(value);
		this.getJSpinner().setBackground(comp.getBackground());
		return this.getJSpinner();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.getJSpinner().setValue(value);
		this.getJSpinner().setBackground(AwbThemeColor.Canvas_Background.getColor());
		return this.getJSpinner();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.getJSpinner().getValue();
	}

}
