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
package agentgui.core.charts.gui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;
/**
 * JSpinner-based table cell editor for Float objects
 * @author Nils
 *
 */
public class TableCellSpinnerEditor4FloatObject extends AbstractCellEditor implements TableCellEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7758086423044836617L;
	private JSpinner spinner;
	
	private JTable table;
	
	private int row2edit;
	
	private int originalHeight;

	@Override
	public Object getCellEditorValue(){
		
		// Reset row height
		table.setRowHeight(row2edit, originalHeight);
		
		String value = ((DefaultEditor)spinner.getEditor()).getTextField().getText();
		value = value.replace(",", ".");
		
		return new Float(Float.parseFloat(value));
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		
		if(spinner == null){
			this.table = table;
			spinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
		}
		// Remember which row was edited
		row2edit = row;
		originalHeight = table.getRowHeight(row2edit);
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
				
		spinner.setValue(table.getValueAt(row, column));
		return spinner;
	}
}
