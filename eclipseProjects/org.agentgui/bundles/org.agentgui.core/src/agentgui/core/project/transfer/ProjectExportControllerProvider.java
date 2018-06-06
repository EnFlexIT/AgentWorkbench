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
package agentgui.core.project.transfer;

import agentgui.core.classLoadService.ClassLoadServiceUtility;

/**
 * The Class ProjectExportControllerProvider provides the 
 *
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectExportControllerProvider {
	
	private static String projectExportControllerClassName;

	/**
	 * Sets a specialized project export controller class - must be a subclass of {@link DefaultProjectExportController}!
	 * @param projectExportControllerClassName the project export controller class name
	 */
	public static void setProjectExportControllerClass(String projectExportControllerClassName) {
		ProjectExportControllerProvider.projectExportControllerClassName = projectExportControllerClassName;
	}
	
	/**
	 * Unsets the project export controller class - the default class will be used.
	 */
	public static void unsetProjectExportControllerClass() {
		ProjectExportControllerProvider.projectExportControllerClassName = null;
	}
	
	/**
	 * Gets the project export controller. If a specialized subclass is set, an instance of that class
	 * will be returned, otherwise an instance of {@link DefaultProjectExportController} 
	 *
	 * @param project the project
	 * @return the project export controller
	 * @throws Exception the exception
	 */
	public static ProjectExportController getProjectExportController() {
		
		if (projectExportControllerClassName == null) {
			
			// --- Use the default implementation -------------
			return new DefaultProjectExportController();
			
		} else {
			
			// --- Use a specialized subclass ----------------- 
			try {
				return (ProjectExportController) ClassLoadServiceUtility.newInstance(projectExportControllerClassName);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				System.err.println("Error getting the specialized ProjectExportController implementation - using the default one!");
				e.printStackTrace();
				return new DefaultProjectExportController();
			}
			
		}
		
	}
	
}
