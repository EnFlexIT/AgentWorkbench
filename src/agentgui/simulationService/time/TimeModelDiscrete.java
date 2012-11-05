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
 * This is a discrete time model, which can either start from 0 or 
 * from a specified time. Additionally the step width in time has to 
 * be configured in order to allow a discrete temporal progression.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelDiscrete extends TimeModel {

	private static final long serialVersionUID = 3931340225354221294L;
	
	private Long time = new Long(0);
	private Long step;
	
	/**
	 * Instantiates a new time model discrete.
	 * @param stepInTime the step width in time
	 */
	public TimeModelDiscrete(Long stepInTime) {
		this.step = stepInTime;
	}
	
	/**
	 * Instantiates a new time model discrete.
	 *
	 * @param stepInTime the step width in time
	 * @param startTime the start time
	 */
	public TimeModelDiscrete(Long stepInTime, Long startTime) {
		this.step = stepInTime;
		this.time = startTime;			
	}
	
	
	/**
	 * Steps the time with the given time step.
	 */
	public void step() {
		this.time = this.time + step;
	}

	/**
	 * Steps back the time with the given time step.
	 */
	public void stepBack() {
		this.time = this.time - step;		
	}
	
	/**
	 * Sets the time.
	 * @param counter the counter to set
	 */
	public void setTime(Long counter) {
		this.time = counter;
	}
	/**
	 * Returns the time as long.
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}
	
	/**
	 * Returns the step width as long.
	 * @return the step
	 */
	public Long getStep() {
		return step;
	}
	/**
	 * Sets the step width.
	 * @param step the step width as long
	 */
	public void setStep(Long step) {
		this.step = step;
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