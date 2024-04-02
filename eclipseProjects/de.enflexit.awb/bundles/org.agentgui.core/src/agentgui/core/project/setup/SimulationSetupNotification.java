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
		SIMULATION_SETUP_SAVED,
		
		SIMULATION_SETUP_DETAILS_LOADED,
		SIMULATION_SETUP_DETAILS_SAVED,
		
		SIMULATION_SETUP_AGENT_ADDED,
		SIMULATION_SETUP_AGENT_REMOVED,
		SIMULATION_SETUP_AGENT_RENAMED
	}
	
	
	private SimNoteReason updateReason;
	private Object notificationObject;
	
	
	/**
	 * Instantiates a new simulation setups change notification.
	 * @param reason the reason
	 */
	public SimulationSetupNotification(SimNoteReason reason) {
		this(reason, null);
	}
	/**
	 * Instantiates a new simulation setups change notification.
	 *
	 * @param reason the reason
	 * @param notificationObject the notification object that corresponds to the current reason
	 */
	public SimulationSetupNotification(SimNoteReason reason, Object notificationObject) {
		this.setUpdateReason(reason);
		this.setNotificationObject(notificationObject);
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
	
	
	/**
	 * Sets the notification object that corresponds to the current reason.
	 * @param notificationObject the new notification object
	 */
	public void setNotificationObject(Object notificationObject) {
		this.notificationObject = notificationObject;
	}
	/**
	 * Returns the notification object that corresponds to the current reason.
	 * @return the notification object
	 */
	public Object getNotificationObject() {
		return notificationObject;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "SimulationSetup." + updateReason.toString();
	}
	
}

