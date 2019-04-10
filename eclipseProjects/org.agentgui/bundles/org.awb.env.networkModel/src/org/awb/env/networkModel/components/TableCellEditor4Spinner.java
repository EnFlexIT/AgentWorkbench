/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package org.awb.env.networkModel.components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.getJSpinner().setValue(value);
		return this.getJSpinner();
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.getJSpinner().setValue(value);
		return this.getJSpinner();
	}
	@Override
	public Object getCellEditorValue() {
		return this.getJSpinner().getValue();
	}

}
