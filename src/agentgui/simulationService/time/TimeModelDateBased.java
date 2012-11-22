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

/**
 * The Class TimeModelDateBased.
 */
public abstract class TimeModelDateBased extends TimeModel {

	private static final long serialVersionUID = 6116787943288451141L;

	protected long timeStart = System.currentTimeMillis();
	protected long timeStop = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
	protected String timeFormat = "dd.MM.yyyy HH:mm:ss.SSS";
	
	
	public abstract long getTime();
	
	/**
	 * Sets the start time .
	 * @param timeStart the new start time
	 */
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	/**
	 * Gets the start time.
	 * @return the start time 
	 */
	public long getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Sets the stop time.
	 * @param timeStop the new stop time
	 */
	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
	}
	/**
	 * Gets the stop time.
	 * @return the stop time 
	 */
	public long getTimeStop() {
		return timeStop;
	}
	
	/**
	 * Sets the time format.
	 * @param timeFormat the new time format
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	/**
	 * Gets the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		return timeFormat;
	}
	
}
