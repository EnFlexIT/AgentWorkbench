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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer for dates. Displays the date as formatted String according to the system locale.
 * @author Nils
 *
 */
public class TableCellRenderer4Time extends DefaultTableCellRenderer {
	
	private String timeFormat;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7378047653825108279L;
	
	public TableCellRenderer4Time(String timeFormat){
		this.timeFormat = timeFormat;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Object value) {
		Date date = null;
		if (value==null) {
			date = new Date(0);
		} else {
			if (value instanceof Number) {
				Long lngValue = ((Number) value).longValue();
				date = new Date(lngValue);
			} else {
				date = new Date(0);	
			}
		}
		DateFormat timeFormat = new SimpleDateFormat(this.timeFormat);
		setText(timeFormat.format(date));
	}

	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

}
