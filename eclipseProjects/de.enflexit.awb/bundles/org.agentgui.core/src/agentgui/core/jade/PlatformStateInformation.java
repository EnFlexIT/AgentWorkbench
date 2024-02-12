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
package agentgui.core.jade;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import agentgui.core.application.Application;

/**
 * The Class PlatformStateInformation serves as a state board for the Jade {@link Platform}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PlatformStateInformation {

	public enum PlatformState {
		Standby("Awaiting call to execute the Multi-Agent System"),
		InformPlugins("Informing registered PlugIns about upcoming start of JADE"),
		PreparingProjectResources("Preparing project resources for distributed execution of the MAS"),
		StartingJADE("Starting JADE platform"),
		StartBackgroundSystemAgents("Starting Background System agents"),
		StartingHTTP("Starting HTTP-Server for project resource distribution"),
		StartingRemote("Starting remote container for agent distribution"),
		StartingMAS("Starting Multi-Agent System"),
		PausingMAS("Pausing Multi-Agent System"),
		RestartingMAS("Restarting paused Multi-Agent System"),
		RunningMAS("Running Multi-Agent System"),
		TerminatingMAS("Terminating Multi-Agent System")
		;
		
		private final String description;
		private PlatformState(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}
	}
	
	
	public static final String PLATFORM_STATE = PlatformState.class.getSimpleName();
	
	private PlatformState platformState;
	private List<PropertyChangeListener> listener;
	
	
	/**
	 * Sets the current platform state.
	 * @param newStatePlatformState the new platform state
	 */
	public void setPlatformState(PlatformState newStatePlatformState) {
		if (newStatePlatformState!=null && newStatePlatformState!=this.platformState) {
			PlatformState oldStatePlatformState = this.platformState; 
			this.platformState = newStatePlatformState;
			this.informListener(oldStatePlatformState, newStatePlatformState);
		}
	}
	/**
	 * Returns the current platform state.
	 * @return the platform state
	 */
	public PlatformState getPlatformState() {
		return platformState;
	}
	
	/**
	 * Returns the list of subsequent platform states, starting from the specified old platform state.
	 *
	 * @param startPlatformState the old platform state
	 * @return the platform state list
	 */
	public static List<PlatformState> getPlatformStateList(PlatformState startPlatformState) {
		
		boolean isAvailableProject = Application.getProjectFocused()!=null;
		boolean isResDistribution = Application.getJadePlatform().isRequiredProjectResourcesDistribution();
		
		boolean addToList = false;
		List<PlatformState>  stateList = new ArrayList<>();
		for (PlatformState ps : PlatformState.values()) {
			if (ps==startPlatformState) addToList = true;
			if (addToList==true) {
				// --- Some special excludes --------------
				if (isAvailableProject==false) {
					if (ps==PlatformState.InformPlugins) continue;
					if (ps==PlatformState.PreparingProjectResources) continue;
					if (ps==PlatformState.StartingHTTP) continue;
					if (ps==PlatformState.StartingMAS) continue;
					if (ps==PlatformState.PausingMAS) continue;
					if (ps==PlatformState.RestartingMAS) continue;
				}
				
				if (isResDistribution==false) {
					if (ps==PlatformState.PreparingProjectResources) continue;
					if (ps==PlatformState.StartingHTTP) continue;
					if (ps==PlatformState.StartingRemote) continue;
				}
				stateList.add(ps);
			}
		}
		return stateList;
	}
	
	
	
	// --------------------------------------------------------------
	// --- From here listener handling ------------------------------
	// --------------------------------------------------------------	
	/**
	 * Returns the platform state listener.
	 * @return the platform state listener
	 */
	private List<PropertyChangeListener> getPlatformStateListener() {
		if (listener==null) {
			listener = new ArrayList<>();
		}
		return listener;
	}
	/**
	 * Adds the specified property change listener.
	 * @param listener the listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener!=null && this.getPlatformStateListener().contains(listener)==false) {
			this.getPlatformStateListener().add(listener);
		}
	}
	/**
	 * Removes the specified property change listener.
	 * @param listener the listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener==null && this.getPlatformStateListener().contains(listener)==true) {
			this.getPlatformStateListener().add(listener);
		}
	}
	/**
	 * Inform listener about state change.
	 *
	 * @param oldStatePlatformState the old state platform state
	 * @param newStatePlatformState the new state platform state
	 */
	private void informListener(PlatformState oldStatePlatformState, PlatformState newStatePlatformState) {
		
		PropertyChangeEvent pcEv = new PropertyChangeEvent(this, PLATFORM_STATE, oldStatePlatformState, newStatePlatformState);
		for (PropertyChangeListener listener : this.getPlatformStateListener()) {
			try {
				listener.propertyChange(pcEv);
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while informing about state change to '" + newStatePlatformState.name() + "':");
				ex.printStackTrace();
			}
		}
	}
	
}
