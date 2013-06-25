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
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;

/**
 * Cell Editor for date fields.
 * Uses JCalendarCombo as editor component, returns the timestamp as Float object
 * 
 * @author Nils
 */
public class TableCellEditor4Time extends AbstractCellEditor implements TableCellEditor{

	private static final long serialVersionUID = 1536069679238018382L;

	private JTable table;
	
	private JSpinner spinner = null;
	private String timeFormat;
	private int row2edit;
	private int originalHeight;

	
	public TableCellEditor4Time(String timeFormat){
		this.timeFormat = timeFormat;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		
		// Reset row height
		table.setRowHeight(row2edit, originalHeight);
		
		Date date = (Date) spinner.getValue();
		return date.getTime();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		if(spinner == null){
			this.table = table;
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			spinner = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, timeFormat);
			spinner.setEditor(de);
		}
		
		// Remember which row was edited
		row2edit = row;
		originalHeight = table.getRowHeight(row2edit);
		
		// Increase row height (the spinner needs more vertical space)
		table.setRowHeight(row2edit, (int) (table.getRowHeight(row2edit)*1.5));
		
		// Init spinner
		long timeStamp = (Long) table.getValueAt(row, column);
		spinner.getModel().setValue(new Date(timeStamp));
		
		return spinner;
	}

}
