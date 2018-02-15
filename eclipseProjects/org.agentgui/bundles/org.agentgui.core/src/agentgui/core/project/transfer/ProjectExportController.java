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

import agentgui.core.project.Project;

/**
 * This interface can be implemented to provide specialized project export controllers.
 * It is highly recommended not to implement this interface yourself, but extend the
 * provided class {@link DefaultProjectExportController} instead, which provides basic
 * project export functionality and several callback functions to add own code. 
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ProjectExportController {
	
	/**
	 * Gets the project export settings.
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings(Project project);
	
	/**
	 * Export project.
	 * @param project the project
	 * @param projectExportSettings the project export settings
	 */
	public void exportProject(Project project, ProjectExportSettings projectExportSettings);
	
	/**
	 * Checks if the export was successfully finished
	 * @return true if successfully finished
	 */
	public boolean isExportSuccessful();
}
