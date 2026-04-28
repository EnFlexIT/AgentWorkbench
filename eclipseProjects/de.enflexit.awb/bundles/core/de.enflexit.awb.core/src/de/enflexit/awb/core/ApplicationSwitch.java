package de.enflexit.awb.core;

import java.awt.Component;

import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.language.Language;

/**
 * The Class ApplicationSwitch is responsible for performing a switch
 * in the execution mode. It decides if and which compoments have to be
 * stopped and restarted.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class ApplicationSwitch {

	
	/**
	 * Switches the applications execution mode.
	 *
	 * @param executionModeOld the old execution mode 
	 * @param executionModeNew the new execution mode 
	 */
	public static void switchExecutionMode(ExecutionMode executionModeOld, ExecutionMode executionModeNew) {
		ApplicationSwitch.switchExecutionMode(null, executionModeOld, executionModeNew);
	}
	
	/**
	 * Switches the applications execution mode.
	 *
	 * @param parentComponent the parent component
	 * @param executionModeOld the old execution mode 
	 * @param executionModeNew the new execution mode
	 */
	public static void switchExecutionMode(Component parentComponent, ExecutionMode executionModeOld, ExecutionMode executionModeNew) {
		
		if (executionModeNew==executionModeOld) {
			// ------------------------------------------------------
			// --- Same ExecutionMode -------------------------------
			// ------------------------------------------------------
			switch (executionModeOld) {
			case APPLICATION:
				// --- Do nothing in this case ----------------------
				break;
			
			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				System.out.println("\n" + Language.translate("Neustart des Server-Dienstes") + " ...");
				Application.getJadePlatform().stop(false);
				Application.removeTrayIcon();
				Application.startAgentWorkbench();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				System.out.println("\n" + Language.translate("Neustart") + " " + Application.getGlobalInfo().getExecutionModeDescription(executionModeNew) + " ...");
				Application.getJadePlatform().stop(false);
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(parentComponent, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				Application.startAgentWorkbench();
				break;
			}
			
		} else {
			// ------------------------------------------------------
			// --- New ExecutionMode --------------------------------
			// ------------------------------------------------------
			String textPrefix = Language.translate("Umschalten von");
			String executionModeTextOld = Application.getGlobalInfo().getExecutionModeDescription(executionModeOld);
			String textMiddle = Language.translate("auf");
			String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(executionModeNew);
			System.out.println(textPrefix + " '" + executionModeTextOld + "' " + textMiddle + " '" + executionModeTextNew + "'");
			
			// ------------------------------------------------------
			// --- Controlled shutdown of the current execution -----
			Application.getJadePlatform().stop(false);
			
			// --- Case separation for current ExecutionMode --------
			switch (executionModeOld) {
			case APPLICATION:
				// --- Application Modus ----------------------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(parentComponent) == false ) return;	
				}		
				// --- Close main window and TrayIcon ---------------
				Application.setMainWindow(null);
				Application.removeTrayIcon();
				break;

			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				Application.removeTrayIcon();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(parentComponent, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				//Application.stopLoggingWriter(); TODO Check if to be used 
				break;
			}
			// --- Restart ------------------------------------------
			Application.startAgentWorkbench();
		}
	}
	
}