package de.enflexit.awb.desktop.mainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
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
		
		if (Application.isOperatingHeadless()==true) return;
		
		// --- Check if the state can be shown in the UI ------------------
		GlobalInfo gInfo = Application.getGlobalInfo();
		boolean isApplication = gInfo.getExecutionMode()==ExecutionMode.APPLICATION;
		boolean isDeviceApplication = gInfo.getExecutionMode()==ExecutionMode.DEVICE_SYSTEM & gInfo.getDeviceServiceExecutionMode()==DeviceSystemExecutionMode.SETUP; 
		if (isApplication==false && isDeviceApplication==false) return;
		
		// --- Try to get the MainWindow instance ---------------------
		final MainWindow mainWindow = Application.isMainWindowInitiated()==true ? (MainWindow)Application.getMainWindow() : null;
		if (mainWindow!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// --- Place the pending states first ---------------------
					HibernateStateVisualService.this.setPendingSessionFactoryStates();
					// --- Place the current state ----------------------------
					mainWindow.getStatusBar().setSessionFactoryState(factoryID, sessionFactoryState);
				}
			});
			
		} else {
			// --- Remind state for a later handling ------------------
			this.getPendingSessionFactoryStates().put(factoryID, sessionFactoryState);
			this.getTimerForSessionFactoryState().restart();
			
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
		MainWindow mainWindow = Application.isMainWindowInitiated()==true ? (MainWindow)Application.getMainWindow() : null;
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
