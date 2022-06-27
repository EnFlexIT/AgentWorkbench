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
package org.agentgui.gui;


/**
 * The Interface AwbProjectNewOpenDialog defines the required methods for a user dialog
 * that handles (create, open, export and delete) projects.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * 
 * @see org.agentgui.gui.swing.dialogs.ProjectNewOpen
 * @see org.agentgui.gui.swt.dialogs.ProjectNewOpen
 */
public interface AwbProjectNewOpenDialog {
	
	/**
	 * The enumeration describing the ProjectAction.
	 */
	public enum ProjectAction {
		NewProject,
		OpenProject,
		ExportProject,
		DeleteProject
	}

	/**
	 * Sets the dialog visible.
	 * @param b the new visible
	 */
	public void setVisible(boolean b);
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled();

	/**
	 * Closes the dialog.
	 * @return true, if successful
	 */
	public boolean close();

	
	/**
	 * Sets the project name.
	 * @param projectName the new project name
	 */
	public void setProjectName(String projectName);
	/**
	 * Returns the project name.
	 * @return the project name
	 */
	public String getProjectName();

	
	/**
	 * Sets the project folder.
	 * @param projectDirectory the new projects sub-directory
	 */
	public void setProjectDirectory(String projectDirectory);
	/**
	 * Returns the project folder.
	 * @return the project folder
	 */
	public String getProjectDirectory();

	
	/**
	 * Checks if a project is to export before it will be deleted.
	 * @return true, if is export before delete
	 */
	public boolean isExportBeforeDelete();
	
	
}
