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
package agentgui.core.charts.timeseriesChart;

/**
 * This class defines a length restriction for a time series, based on the duration and/or number of states. 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSeriesLengthRestriction {
	private long maxDuration;
	private int maxNumberOfStates;
	
	/**
	 * Gets the max duration.
	 * @return the max duration
	 */
	public long getMaxDuration() {
		return maxDuration;
	}
	
	/**
	 * Sets the max duration.
	 * @param maxDuration the new max duration
	 */
	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	/**
	 * Gets the max number of states.
	 * @return the max number of states
	 */
	public int getMaxNumberOfStates() {
		return maxNumberOfStates;
	}
	
	/**
	 * Sets the max number of states.
	 * @param maxNumberOfStates the new max number of states
	 */
	public void setMaxNumberOfStates(int maxNumberOfStates) {
		this.maxNumberOfStates = maxNumberOfStates;
	}
}
