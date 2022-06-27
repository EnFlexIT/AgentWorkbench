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

/**
 * The Class StopWatch can be used as tool to measure time between two events.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StopWatch implements Serializable {

	private static final long serialVersionUID = 975852140501115006L;

	private long stopWatchStart = 0;
	private long stopWatchStop = 0;
		
	/**
	 * Instantiates a new stop watch.
	 */
	public StopWatch() {
	}

	/**
	 * Stopwatch start.
	 */
	public void start() {
		this.stopWatchStart = System.currentTimeMillis();
	}
	/**
	 * Stopwatch stop.
	 */
	public void stop() {
		this.stopWatchStop = System.currentTimeMillis();
	}
	/**
	 * Stopwatch restart. Works only, if   
	 * the clock was stopped and not reseted 
	 */
	public void restart() {
		if (this.stopWatchStop!=0) {
			this.stopWatchStart = System.currentTimeMillis() - this.getTimeMeasured(); 
			this.stopWatchStop = 0;	
		}
	}
	/**
	 * Stopwatch reset.
	 */
	public void reset() {
		this.stopWatchStart = 0;
		this.stopWatchStop = 0;
	}
	/**
	 * Returns the currently stopped time in milliseconds.
	 * @return the long
	 */
	public long getTimeMeasured() {
		long stopWatchTime = 0;
		if (stopWatchStart==0 && stopWatchStop==0) {
			// --- Stop watch is not running ----
			// => Do nothing
		} else if (stopWatchStart!=0 && stopWatchStop!=0) {
			// --- Stop watch was stopped -------
			stopWatchTime = stopWatchStop - stopWatchStart;
		} else if (stopWatchStart==0 && stopWatchStop!=0) {
			// --- Something wrong --------------
			// => Do nothing
		} else if (stopWatchStart!=0 && stopWatchStop==0) {
			// --- Clock is running ! -----------
			stopWatchTime = System.currentTimeMillis() - stopWatchStart;
		}
		return stopWatchTime;
	}
	
}
