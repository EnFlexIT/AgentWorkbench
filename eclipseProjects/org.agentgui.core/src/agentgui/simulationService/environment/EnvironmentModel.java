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
package agentgui.simulationService.environment;

import java.io.Serializable;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.time.TimeModel;

/**
 * This is the generalized environment model to use with the SimulationService
 * and in order to extend individual environment models.
 * 
 * @see SimulationService
 * 
 * @see SimulationServiceHelper#setEnvironmentModel(EnvironmentModel)
 * @see SimulationServiceHelper#stepSimulation(EnvironmentModel, int)
 * @see SimulationServiceHelper#stepSimulation(EnvironmentModel, int, boolean)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModel implements Serializable {

	private static final long serialVersionUID = -2845036237763599630L;
	
	private TimeModel timeModel;
	private AbstractEnvironmentModel abstractEnvironment;
	private DisplaytEnvironmentModel displayEnvironment;	

	
	/**
	 * Returns true if nothing is set yet (e.g. timeModel, abstractEnvironment or displayEnvironment)
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		if (timeModel==null && abstractEnvironment==null && displayEnvironment==null) {
			return true;
		} else {
			return false;	
		}	
	}
	
	/**
	 * Gets the time model.
	 * @return the timeModel
	 */
	public TimeModel getTimeModel() {
		return timeModel;
	}
	/**
	 * Sets the time model.
	 * @param timeModel the timeModel to set
	 */
	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	
	/**
	 * Gets the abstract environment.
	 * @return the abstract environment
	 */
	public AbstractEnvironmentModel getAbstractEnvironment() {
		return abstractEnvironment;
	}
	/**
	 * Sets the abstract environment.
	 * @param newAbstractEnvironment the new abstract environment
	 */
	public void setAbstractEnvironment(AbstractEnvironmentModel newAbstractEnvironment) {
		this.abstractEnvironment = newAbstractEnvironment;
	}
	
	/**
	 * Gets the display environment.
	 * @return the display environment
	 */
	public DisplaytEnvironmentModel getDisplayEnvironment() {
		return displayEnvironment;
	}
	/**
	 * Sets the display environment.
	 * @param displayEnvironment the new display environment
	 */
	public void setDisplayEnvironment(DisplaytEnvironmentModel displayEnvironment) {
		this.displayEnvironment = displayEnvironment;
	}

	/**
	 * Returns a copy of the current environment model.
	 * @return the copy
	 */
	public EnvironmentModel getCopy() {
		
		if (this.isEmpty()==true) {
			return null;
		}
				
		EnvironmentModel copy = new EnvironmentModel();
		if (this.timeModel!=null) {
			copy.setTimeModel(this.timeModel.getCopy());
		}
		if (this.displayEnvironment!=null) {
			copy.setDisplayEnvironment(this.displayEnvironment.getCopy());
		}
		if (this.abstractEnvironment!=null) {
			copy.setAbstractEnvironment(this.abstractEnvironment.getCopy());
		}
		return copy;
	}

}
