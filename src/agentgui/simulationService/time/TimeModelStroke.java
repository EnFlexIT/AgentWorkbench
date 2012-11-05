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
 * This is a stroke time model, which inherits just a simple counter and can
 * be used in an {@link EnvironmentModel}
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStroke extends TimeModel {

	private static final long serialVersionUID = -63223704339241994L;

	private int counter = 0;
	
	/**
	 * Instantiates a new time model stroke.
	 */
	public TimeModelStroke() {
	}
	/**
	 * Instantiates a new time model stroke.
	 * @param counterStart the position number the counter has to start from
	 */
	public TimeModelStroke(Integer counterStart) {
		this.counter = counterStart;
	}

	/**
	 * Steps the counter plus 1.
	 */
	public void step() {
		counter++;
	}
	
	/**
	 * Steps the counter minus 1.
	 */
	public void stepBack() {
		counter--;
	}

	/**
	 * Sets the counter.
	 * @param counter the counter to set
	 */
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	/**
	 * Returns the counter.
	 * @return the counter
	 */
	public Integer getCounter() {
		return counter;
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
	
} // --- End of Sub-Class -----