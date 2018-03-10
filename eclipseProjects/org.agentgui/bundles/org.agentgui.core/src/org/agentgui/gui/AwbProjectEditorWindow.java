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

import org.agentgui.gui.swing.project.ProjectWindowTab;

import agentgui.core.project.Project;

/**
 * The Interface ProjectWindow defines the required methods for the editor of a project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbProjectEditorWindow {

	
	/**
	 * The Enumeration ProjectCloseUserFeedback.
	 */
	public enum ProjectCloseUserFeedback {
		SaveProject,
		DoNotSaveProject,
		CancelCloseAction
	}
	
	
	/**
	 * Has to return the current project.
	 * @return the project
	 */
	public Project getProject();
	
	/**
	 * Checks, how the user wants to proceed, if unsaved projects has to be closed.
	 *
	 * @param msgTitle the message title
	 * @param msgText the message text
	 * @param parentVisualizationComponent the parent visualization component
	 * @return true, if is user allowed to close project
	 */
	public ProjectCloseUserFeedback getUserFeedbackForClosingProject(String msgTitle, String msgText, Object parentVisualizationComponent); 
	
	/**
	 * Has to show an error message.
	 *
	 * @param msgText the message text
	 * @param msgHead the message header
	 */
	public void showErrorMessage(String msgText, String msgHead);
	
	/**
	 * Adds the default tabs.
	 */
	public void addDefaultTabs();
	
	/**
	 * Adds the specified project tab.
	 * @param projectWindowTab the project window tab
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab);

	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow
	 * at the specified index position. The index position has to be greater than 1, in order
	 * to keep the 'Info'-Tab and the 'Configuration'-Tab at its original position!
	 *
	 * @param projectWindowTab the project window tab
	 * @param indexPosition the index position (greater one)
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab, int indexPosition);

	/**
	 * Removes the specified project tab.
	 *
	 * @param projectWindowTab the project window tab
	 */
	public void removeProjectTab(ProjectWindowTab projectWindowTab);	
	
	/**
	 * Disposes the AwbProjectEditorWindow.
	 */
	public void dispose();
	
	/**
	 * Moves the AwbProjectEditorWindow to the front.
	 */
	public void moveToFront();
	
	/**
	 * Maximizes the current AwbProjectEditorWindow.
	 */
	public void setMaximized();
	
	/**
	 * Has to set the focus to the specified tab.
	 * @param tabName the new focus 2 tab
	 */
	public void setFocus2Tab(String tabName);
	
	/**
	 * Rebuilds the AwbProjectEditorWindow depending on the selected view that is either {@link Project#VIEW_User}, {@link Project#VIEW_Developer} or <code>null</code>.
	 */
	public void setViewForDeveloperOrEndUser();

	/**
	 * Returns the specified tab for sub panels.
	 *
	 * @param superPanelName the super panel name
	 * @return the tab for sub panels
	 */
	public ProjectWindowTab getTabForSubPanels(String superPanelName);

	/**
	 * Has to validates the current start tab settings that could be 
	 * changed, if the view was changed from developer to end user view.
	 */
	public void validateStartTab();


}
