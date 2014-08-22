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
package jade.debugging.components;

import java.util.HashMap;


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
	private static SysOutScanner sosRunning = null;
	/** Reminder if the JADE Main-Container is located in the current JVM */
	private static boolean locationOfMainContainer = false;
	/** The JFrame which will display the consoles, if needed */
	private static JFrame4Consoles displayFrame;
	/** Reminder for the location for further console windows */
	private static JTabbedPane4Consoles location4RemoteConsoles = null;
	/** The Hash of all console windows	*/
	private static HashMap<String, JPanelConsole> jPanelConsoles;
	

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
	 * Sets the current JFrame4Consoles.
	 * @param displayFrame the displayFrame to set
	 */
	public static void setJFrame4Display(JFrame4Consoles displayFrame) {
		SysOutBoard.displayFrame = displayFrame;
	}
	/**
	 * Returns the current JFrame4Consoles.
	 * @return the displayFrame
	 */
	public static JFrame4Consoles getJFrame4Display() {
		return displayFrame;
	}
	
	/**
	 * Sets the current JTabbedPane4Consoles.
	 * @param tabPane4Consoles the new j tabbed pane4 consoles
	 */
	public static void setJTabbedPane4Consoles(JTabbedPane4Consoles tabPane4Consoles) {
		SysOutBoard.location4RemoteConsoles = tabPane4Consoles;
	}
	/**
	 * Returns the current {@link JTabbedPane4Consoles}.
	 * @return the location4RemoteConsoles
	 */
	public static JTabbedPane4Consoles getJTabbedPane4Consoles() {
		return location4RemoteConsoles;
	}
	
	/**
	 * Sets the HashMap for the available JPanelConsole's.
	 * @param hash the hash
	 */
	public static void setHashMapJPanelConsoles(HashMap<String, JPanelConsole> hash) {
		SysOutBoard.jPanelConsoles = hash;
	}
	/**
	 * Returns the HashMap for the available JPanelConsole's.
	 * @return the jPanelConsoles
	 */
	public static HashMap<String, JPanelConsole> getHashMapJPanelConsoles() {
		return jPanelConsoles;
	}
	
}
