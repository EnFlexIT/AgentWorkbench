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
 * The Class TimeModelContinuous.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelContinuous extends TimeModel {

	private static final long serialVersionUID = -6787156387409895035L;

	private long timeDiff = 0;

	/**
	 * Instantiates a new time model continuous.
	 */
	public TimeModelContinuous() {
	
	}
	/**
	 * Instantiates a new time model continuous.
	 * @param startTime the start time
	 */
	public TimeModelContinuous(Long startTime) {
		this.setTimeDiff(System.currentTimeMillis() - startTime);
	}
	/**
	 * Returns the time as long.
	 * @return the time
	 */
	public long getTime() {
		return System.currentTimeMillis() - timeDiff;
	}
	
	/**
	 * Sets the time difference.
	 * @param timeDiff the timeDiff to set
	 */
	public void setTimeDiff(long timeDiff) {
		this.timeDiff = timeDiff;
	}
	/**
	 * Returns the time difference.
	 * @return the timeDiff
	 */
	public long getTimeDiff() {
		return timeDiff;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getJPanel4Configuration()
	 */
	@Override
	public DisplayJPanel4Configuration getJPanel4Configuration() {
		return null;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public DisplayJToolBar4Execution getJToolBar4Execution() {
		return null;
	}
	
}
