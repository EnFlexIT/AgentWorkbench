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

/**
 * The Class DiscreteSimulationStep is used in the context of discrete simulations to
 * iteratively transfer an agents state (or a specific data model) to a simulation manager.
 * Based on this, the simulation manager may decide for a next discrete simulation step
 * or to iteratively reactivated the current simulation round.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDiscreteSimulationStep<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 7906198315000330449L;

	/**
	 * The enumeration SystemStateType.
	 */
	public enum DiscreteSystemStateType {
		Iteration,
		Final
	}
	
	private T systemState;
	private DiscreteSystemStateType discreteSystemStateType;
	
	
	/**
	 * Instantiates a new discrete simulation step (default constructor).
	 * <b>For serialization purposes only!!</b>
	 */
	@Deprecated 
	public AbstractDiscreteSimulationStep() { }
	
	/**
	 * Instantiates a new discrete simulation step.
	 *
	 * @param systemState the system state
	 * @param discreteSystemStateType the system state type
	 */
	public AbstractDiscreteSimulationStep(T systemState, DiscreteSystemStateType discreteSystemStateType) {
		this.setSystemState(systemState);
		this.setDiscreteSystemStateType(discreteSystemStateType);
	}
	
	
	/**
	 * Sets the system state for a simulation step.
	 * @param systemState the new system state
	 */
	public void setSystemState(T systemState) {
		this.systemState = systemState;
	}
	/**
	 * Returns the system state for a simulation step.
	 * @return the system state
	 */
	public T getSystemState() {
		return systemState;
	}
	
	
	/**
	 * Sets the system state type.
	 * @param discreteSystemStateType the new system state type
	 */
	public void setDiscreteSystemStateType(DiscreteSystemStateType discreteSystemStateType) {
		this.discreteSystemStateType = discreteSystemStateType;
	}
	/**
	 * Returns the system state type.
	 * @return the system state type
	 */
	public DiscreteSystemStateType getDiscreteSystemStateType() {
		return discreteSystemStateType;
	}
	
}
