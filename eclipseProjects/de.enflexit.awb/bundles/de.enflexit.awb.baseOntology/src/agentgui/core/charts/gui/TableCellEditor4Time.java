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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Cell Editor for date fields.
 * Uses JCalendarCombo as editor component, returns the timestamp as Float object
 * 
 * @author Nils
 */
public class TableCellEditor4Time extends BasicCellEditor{

	private static final long serialVersionUID = 1536069679238018382L;

	private String timeFormat;
	
	/**
	 * Remembers the difference between the full original date and that returned by the spinner right after the initialization,
	 * to compensate errors caused by shorter time formats
	 */
	private long difference = 0;
	
	private JSpinner.DateEditor dateEditor;
	
	public TableCellEditor4Time(String timeFormat){
		this.timeFormat = timeFormat;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		
		// Add the difference remembered at initialization time, to compensate errors caused by the time formats 
		Date returnedDate = (Date) ((JSpinner)editorComponent).getValue();
		Date correctDate = new Date(returnedDate.getTime() + difference);
		
		return correctDate.getTime();
	}

	@Override
	protected Component getEditorComponent(Object value) {
		if(this.editorComponent == null){
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			editorComponent = new JSpinner(sdm);
			dateEditor = new JSpinner.DateEditor((JSpinner) editorComponent, timeFormat);
			dateEditor.getTextField().addKeyListener(this);
			((JSpinner)editorComponent).setEditor(dateEditor);
		}
		
		// Init spinner
		long timeStamp = (Long) value;
		
		// Remember the original date
		Date originalDate = new Date(timeStamp);
		((JSpinner)editorComponent).getModel().setValue(originalDate);
		try {
			// Refresh the spinner value according to the internal JTextField. Date  
			// components missing in the time format will be set to zero by this. 
			((JSpinner)editorComponent).commitEdit();
		} catch (ParseException e) {
			// This cannot happen, as the spinner is initialized with a valid value right before  
		}

		// Compare the possibly errorneous date returned by the spinner to the original one and remember the difference
		Date returnedDate = new Date((Long) getCellEditorValue());
		difference = originalDate.getTime() - returnedDate.getTime();
		
		return this.editorComponent;
	}

}
