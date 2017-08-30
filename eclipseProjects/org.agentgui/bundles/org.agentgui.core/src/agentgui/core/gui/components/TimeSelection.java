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
package agentgui.core.gui.components;


/**
 * The class TimeSelection can be used for a ComboBoxModel of the sampling interval as user object.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSelection {

	/** The time in milliseconds. */
	private long timeInMill = 0;
	
	/**
	 * Instantiates a new time selection.
	 * @param timeInMillis the time in milliseconds
	 */
	public TimeSelection(long timeInMillis) {
		this.timeInMill = timeInMillis;
	}
	
	/**
	 * Gets the time in milliseconds.
	 * @return the time in milliseconds
	 */
	public long getTimeInMill() {
		return timeInMill;
	}
	/**
	 * Sets the time in milliseconds.
	 * @param timeInMill the milliseconds to set
	 */
	public void setTimeInMill(int timeInMill) {
		this.timeInMill = timeInMill;
	}
	
	/**
	 * Converts the milliseconds into seconds.
	 * @return the text to display in seconds
	 */
	public String toString() {
		int timeInTenth = Math.round(timeInMill/100);
		float timeInSecFloat = (float) timeInTenth / 10;  
		int timeInSecInt = (int) timeInSecFloat;
		
		if ((timeInSecFloat-timeInSecInt)>0) {
			return timeInSecFloat + " s";
		} else {
			return timeInSecInt + " s";
		}			
	}
}
