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
package agentgui.logging.components;

import java.util.HashMap;

import org.agentgui.gui.AwbConsole;
import org.agentgui.gui.AwbConsoleDialog;
import org.agentgui.gui.AwbConsoleFolder;

/**
 * This static class is used in order to identify, if the console output<br>
 * is already scanned by a SystemOutputScanner.<br>
 * <br>
 * Additionally, this class holds the information, if the current JVM holds<br> 
 * the Main-Container of the platform and which JTabbedPane can be used in order<br>
 * to display the console output of remote containers.<br>
 * 
 * @see SysOutScanner
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SysOutBoard {

	/** Reminder for the current output scanner  */
	private static SysOutScanner sosRunning;
	/** Reminder if the JADE Main-Container is located in the current JVM */
	private static boolean locationOfMainContainer;
	
	/** The dialog that will display the consoles, if needed */
	private static AwbConsoleDialog consoleDialog;
	/** Reminder for the location for further console windows */
	private static AwbConsoleFolder consoleFolder;
	/** The Hash of all console windows	*/
	private static HashMap<String, AwbConsole> consoleHashMap;
	

	/**
	 * Gets the current {@link SysOutScanner}.
	 * @return the sosRunning
	 */
	public static SysOutScanner getSysOutScanner() {
		return sosRunning;
	}
	/**
	 * Sets the current {@link SysOutScanner}.
	 * @param sosRunning the new SysOutScanner
	 */
	public static void setSysOutScanner(SysOutScanner sosRunning) {
		SysOutBoard.sosRunning = sosRunning;
	}
	/**
	 * Returns true, if a SysOutScanner is defined 
	 * @return true, if a scanner is defined
	 */
	public static boolean isRunningSysOutScanner() {
		if (sosRunning==null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Sets the checks if is location of main container.
	 * @param locationMainContainer the locationMainContainer to set
	 */
	public static void setIsLocationOfMainContainer(boolean locationMainContainer) {
		SysOutBoard.locationOfMainContainer = locationMainContainer;
	}
	/**
	 * Checks if is location of main container.
	 * @return the locationMainContainer
	 */
	public static boolean isLocationOfMainContainer() {
		return locationOfMainContainer;
	}
	
	/**
	 * Sets the console dialog.
	 * @param consoleDialog the new console dialog
	 */
	public static void setConsoleDialog(AwbConsoleDialog consoleDialog) {
		SysOutBoard.consoleDialog = consoleDialog;
	}
	/**
	 * Gets the console dialog.
	 * @return the consoleDialog
	 */
	public static AwbConsoleDialog getConsoleDialog() {
		return consoleDialog;
	}
	
	/**
	 * Sets the console folder.
	 * @param consoleFolder the new console folder
	 */
	public static void setConsoleFolder(AwbConsoleFolder consoleFolder) {
		SysOutBoard.consoleFolder = consoleFolder;
	}
	/**
	 * Gets the console folder.
	 * @return the console folder
	 */
	public static AwbConsoleFolder getConsoleFolder() {
		return consoleFolder;
	}
	
	/**
	 * Returns the HashMap for the available JPanelConsole's.
	 * @return the consoleHashMap
	 */
	public static HashMap<String, AwbConsole> getHashMapConsoles() {
		if (consoleHashMap==null) {
			consoleHashMap = new HashMap<>();
		}
		return consoleHashMap;
	}
	
}
