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
package agentgui.core.plugin;

import agentgui.core.project.Project;

/**
 * This class is used within the observer pattern of projects. It informs
 * about changes regarding a plug-in.
 * 
 * @see Project
 * @see Project#setNotChangedButNotify(Object)
 * 
 * @see PlugIn#CHANGED
 * @see PlugIn#ADDED
 * @see PlugIn#REMOVED
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class PlugInNotification {

	private PlugIn plugIn = null;
	private int updateReason = 0; 
	
	/**
	 * Instantiates a new plug in notification.
	 *
	 * @param reason the reason
	 * @param plugIn the plug in
	 */
	public PlugInNotification(int reason, PlugIn plugIn) {
		this.setUpdateReason(reason);
		this.setPlugIn(plugIn);
	}
	
	/**
	 * Sets the update reason.
	 *
	 * @param updateReason the new update reason
	 */
	public void setUpdateReason(int updateReason) {
		this.updateReason = updateReason;
	}
	
	/**
	 * Gets the update reason.
	 *
	 * @return the update reason
	 */
	public int getUpdateReason() {
		return updateReason;
	}
	
	/**
	 * Sets the plug in.
	 *
	 * @param plugIn the new plug in
	 */
	public void setPlugIn(PlugIn plugIn) {
		this.plugIn = plugIn;
	}
	
	/**
	 * Gets the plug in.
	 *
	 * @return the plug in
	 */
	public PlugIn getPlugIn() {
		return plugIn;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlugIn.CHANGED;
	}
	
}
