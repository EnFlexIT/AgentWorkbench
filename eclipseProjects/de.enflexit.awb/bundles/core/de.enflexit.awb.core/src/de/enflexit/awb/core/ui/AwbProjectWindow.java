package de.enflexit.awb.core.ui;

import de.enflexit.awb.core.project.Project;

/**
 * The Interface AwbProjectWindow defines the required methods for the editor window of a project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbProjectWindow {

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
	public void addProjectTab(AwbProjectWindowTab projectWindowTab);

	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow
	 * at the specified index position. The index position has to be greater than 1, in order
	 * to keep the 'Info'-Tab and the 'Configuration'-Tab at its original position!
	 *
	 * @param projectWindowTab the project window tab
	 * @param indexPosition the index position (greater one)
	 */
	public void addProjectTab(AwbProjectWindowTab projectWindowTab, int indexPosition);

	/**
	 * Removes the specified project tab.
	 *
	 * @param projectWindowTab the project window tab
	 */
	public void removeProjectTab(AwbProjectWindowTab projectWindowTab);	
	
	/**
	 * Disposes the AwbProjectWindow.
	 */
	public void dispose();
	
	/**
	 * Moves the AwbProjectWindow to the front.
	 */
	public void moveToFront();
	
	/**
	 * Maximizes the current AwbProjectWindow.
	 */
	public void setMaximized();
	
	/**
	 * Has to set the focus to the specified tab.
	 * @param tabName the new focus 2 tab
	 */
	public void setFocus2Tab(String tabName);
	
	/**
	 * Rebuilds the AwbProjectWindow depending on the selected view that is either {@link Project#VIEW_User}, {@link Project#VIEW_Developer} or <code>null</code>.
	 */
	public void setViewForDeveloperOrEndUser();

	
	/**
	 * Sets the project tree visible or not.
	 * @param isTreeVisible the new project tree visible
	 */
	public void setProjectTreeVisible(boolean isTreeVisible);
	
	/**
	 * Sets the projects tab header visible or not.
	 * @param isProjectTabHeaderVisible the new project tab header visible
	 */
	public void setProjectTabHeaderVisible(boolean isProjectTabHeaderVisible);
	
	/**
	 * Returns the specified tab for sub panels.
	 *
	 * @param superPanelName the super panel name
	 * @return the tab for sub panels
	 */
	public AwbProjectWindowTab getTabForSubPanels(String superPanelName);

	/**
	 * Has to validates the current start tab settings that could be 
	 * changed, if the view was changed from developer to end user view.
	 */
	public void validateStartTab();

	/**
	 * Has to return the visualization container for the agents runtime.
	 * @return the runtime visualization container
	 */
	public Object getRuntimeVisualizationContainer();
	
}
