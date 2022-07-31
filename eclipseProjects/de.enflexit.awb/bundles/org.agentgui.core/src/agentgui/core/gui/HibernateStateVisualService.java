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
package agentgui.core.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.Timer;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizationService;

/**
 * The Class HibernateStateVisualService.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class HibernateStateVisualService implements HibernateStateVisualizationService {

	private HashMap<String, SessionFactoryState> pendingSessionStates;
	private Timer timerForSessionFactoryState;
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		
		if (factoryID==null || sessionFactoryState==null) return;
		
		if (Application.isOperatingHeadless()==false) {
			// --- Check if the state can be shown in the UI ------------------
			GlobalInfo gInfo = Application.getGlobalInfo();
			boolean isApplication = gInfo.getExecutionMode()==ExecutionMode.APPLICATION;
			boolean isDeviceApplication = gInfo.getExecutionMode()==ExecutionMode.DEVICE_SYSTEM & gInfo.getDeviceServiceExecutionMode()==DeviceSystemExecutionMode.SETUP; 
			if (isApplication || isDeviceApplication) {
				// --- Try to get the MainWindow instance ---------------------
				MainWindow mainWindow = Application.getMainWindow();
				if (mainWindow!=null) {
					// --- Place the pending states first ---------------------
					this.setPendingSessionFactoryStates();
					// --- Place the current state ----------------------------
					mainWindow.getStatusBar().setSessionFactoryState(factoryID, sessionFactoryState);
					
				} else {
					// --- Remind state for a later handling ------------------
					this.getPendingSessionFactoryStates().put(factoryID, sessionFactoryState);
					this.getTimerForSessionFactoryState().restart();
					
				}
			}
		}
	}
	
	/**
	 * Returns the pending session states that are states that reached this class, but
	 * that could not be placed since the {@link MainWindow} is not visible yet.
	 * 
	 * @return the pending session states
	 */
	private HashMap<String, SessionFactoryState> getPendingSessionFactoryStates() {
		if (pendingSessionStates==null) {
			pendingSessionStates = new HashMap<>();
		}
		return pendingSessionStates;
	}
	
	/**
	 * Returns the timerForSessionFactoryState for session factory state.
	 * @return the timerForSessionFactoryState for session factory state
	 */
	public Timer getTimerForSessionFactoryState() {
		if (timerForSessionFactoryState==null) {
			timerForSessionFactoryState = new Timer(200, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					HibernateStateVisualService.this.setPendingSessionFactoryStates();
				}
			});
		}
		return timerForSessionFactoryState;
	}

	/**
	 * If possible, will set the pending session factory states to the visualization.
	 * If the MainWindow is still not there
	 */
	private synchronized void setPendingSessionFactoryStates() {
		
		if (this.pendingSessionStates==null) return;
		
		// --- Try to get the MainWindow now ----------------------------------
		MainWindow mainWindow = Application.getMainWindow();
		if (mainWindow==null) {
			// --- Restart the local timerForSessionFactoryState ------------------------------------ 
			this.getTimerForSessionFactoryState().restart();
			
		} else {
			// --- Stop the local timerForSessionFactoryState ---------------------------------------
			this.getTimerForSessionFactoryState().stop();
			this.timerForSessionFactoryState = null;

			// --- Are there pending states to show? --------------------------
			if (this.pendingSessionStates.size()>0) {
				// --- Place the states pending in the UI ---------------------
				List<String> factoryIdList = new ArrayList<>(this.pendingSessionStates.keySet());
				Collections.sort(factoryIdList);
				for (String factoryID : factoryIdList) {
					SessionFactoryState sessionFactoryState = this.pendingSessionStates.remove(factoryID);
					if (sessionFactoryState!=null) {
						mainWindow.getStatusBar().setSessionFactoryState(factoryID, sessionFactoryState);
					}
				}
			}
			// --- Set pending states to null ---------------------------------  
			this.pendingSessionStates = null;
		}
	}
}
