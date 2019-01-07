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
package agentgui.core.config;

//import java.io.IOException;
//
//import org.agentgui.PlugInActivator;
//import org.eclipse.equinox.security.storage.ISecurePreferences;
//import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
//import org.eclipse.equinox.security.storage.StorageException;

/**
 * The Class SecureStorageManager provides some simple help methods, e.g. to save or load
 * a password to / from the SecureStorage of eclipse.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SecureStorageManager {
	
//	/**
//	 * Returns the secure preferences.
//	 * @return the secure preferences
//	 */
//	private static ISecurePreferences getAgentWorkbenchSecurePreferences() {
//		return SecurePreferencesFactory.getDefault().node(PlugInActivator.PLUGIN_ID);
//	}
//	/**
//	 * Returns the secure preferences with the specified preference node ID.
//	 *
//	 * @param nodeID the node ID
//	 * @return the secure preferences
//	 */
//	public static ISecurePreferences getSecurePreferences(String nodeID) {
//		return getAgentWorkbenchSecurePreferences().node(nodeID);
//	}
//
//	
//	/**
//	 * Saves the specified password for the specified preference node ID.
//	 *
//	 * @param nodeID the node ID
//	 * @param password the password
//	 * @return true, if successful
//	 */
//	public static boolean savePassword(String nodeID, String password) {
//		
//		boolean success = false;
//		ISecurePreferences prefNode = getSecurePreferences(nodeID);
//		try {
//			prefNode.put("Password", password, true);
//			prefNode.flush();
//			
//		} catch (StorageException | IOException sEx) {
//			sEx.printStackTrace();
//		}
//		return success;
//	}
//	/**
//	 * Returns the password for the specified preference node ID.
//	 *
//	 * @param nodeID the node ID
//	 * @return the password
//	 */
//	public static String getPassword(String nodeID) {
//		
//		String password = null;
//		ISecurePreferences prefNode = getSecurePreferences(nodeID);
//		try {
//			password = prefNode.get(nodeID, null) ;
//			
//		} catch (StorageException sEx) {
//			sEx.printStackTrace();
//		}
//		return password;
//	}
	
}
