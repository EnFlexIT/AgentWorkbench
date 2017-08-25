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
package agentgui.core.project.setup;

/**
 * Class which is used for notifications about changes and actions
 * inside the SimulationSetups, like 'addNew', 'saved' and so on.
 * Which action was used can be set by the constructor while using 
 * the containing enumeration {@link SimNoteReason}. 
 *     
 * @see SimulationSetups
 * @see SimNoteReason
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimulationSetupNotification {
	
	
	/** The Enumeration for the notification reasons. */
	public enum SimNoteReason {
		SIMULATION_SETUP_LOAD,
		SIMULATION_SETUP_ADD_NEW,
		SIMULATION_SETUP_COPY,
		SIMULATION_SETUP_REMOVE,
		SIMULATION_SETUP_RENAME,
		SIMULATION_SETUP_PREPARE_SAVING,
		SIMULATION_SETUP_SAVED
	}
	
	private SimNoteReason updateReason;
	
	/**
	 * Instantiates a new simulation setups change notification.
	 * @param reason the reason
	 */
	public SimulationSetupNotification(SimNoteReason reason) {
		updateReason = reason;
	}
	/**
	 * Sets the update reason.
	 * @param updateReason the new update reason
	 */
	public void setUpdateReason(SimNoteReason updateReason) {
		this.updateReason = updateReason;
	}
	/**
	 * Gets the update reason.
	 * @return the update reason
	 */
	public SimNoteReason getUpdateReason() {
		return updateReason;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "SimulationSetup." + updateReason.toString();
	}
	
}

