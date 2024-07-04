package de.enflexit.expression;

import agentgui.core.application.Application;
import de.enflexit.common.ExecutionEnvironment;

/**
 * The static Class DevelopmentSwitch enables to switch between 
 * development or regular integrated mode.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DevelopmentSwitch {

	private static boolean debug = true;
	
	/**
	 * Checks if is debug.
	 * @return true, if is debug
	 */
	public static boolean isDebug() {
		return Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE && debug;
	}
	
}
