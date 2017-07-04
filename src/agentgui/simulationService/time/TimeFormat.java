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
package agentgui.simulationService.time;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class TimeFormat describes a predefined format for a date.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormat implements Serializable {

	private static final long serialVersionUID = 6691784575006563592L;

	private String format = null;
	private Date displayFormat = null;
	
	
	/**
	 * Instantiates a new time unit.
	 * @param format the String representation of the time format
	 */
	public TimeFormat(String format) {
		this.format = format;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.format + " (" + new SimpleDateFormat(this.format).format(this.getDateDisplay()) + ")";
	}

	/**
	 * Gets the date to display.
	 * @return the date to display
	 */
	private Date getDateDisplay() {
		if (displayFormat==null) {
			displayFormat = new Date();
		}
		return displayFormat;
	}
	
	/**
	 * Sets the format.
	 * @param format the new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * Gets the format.
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

}
