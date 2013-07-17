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
	
	public TableCellEditor4Time(String timeFormat){
		this.timeFormat = timeFormat;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		
		Date date = (Date) ((JSpinner)editorComponent).getValue();
		return date.getTime();
	}

	@Override
	protected Component getEditorComponent(Object value) {
		if(this.editorComponent == null){
			SpinnerDateModel sdm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
			editorComponent = new JSpinner(sdm);
			JSpinner.DateEditor de = new JSpinner.DateEditor((JSpinner) editorComponent, timeFormat);
			de.getTextField().addKeyListener(this);
			((JSpinner)editorComponent).setEditor(de);
		}
		
		// Init spinner
		long timeStamp = (Long) value;
		((JSpinner)editorComponent).getModel().setValue(new Date(timeStamp));
		
		return this.editorComponent;
	}

}
