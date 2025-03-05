package de.enflexit.awb.core.ui;

import de.enflexit.awb.core.jade.Platform.JadeStatusColor;

/**
 * The Interface AwbMainWindow describes the methods to interact with 
 * the MainWindow of an {@link AgentWorkbenchUI} implementation.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbMainWindow<MenueType, MenuItemType, ToolBarType, ToolBarComponentType> {

	/**
	 * Has to refresh / repaint the current main window view.
	 */
	public void refreshView();
	
	/**
	 * This method sets back the focus to this main window frame, even if its iconfied or behind other application windows.
	 */
	public void restoreFocus();
	
	/**
	 * Has to close and destroy the AWB main window by also releasing all of resources.
	 */
	public void dispose();

	/**
	 * This method is used in case a project is open. Than, the project name has to be displayed behind the applications title (e.g. 'Agent.Workbench: project name')
	 * @param add2BasicTitel the new title addition
	 */
	public void setTitelAddition(String add2BasicTitle);
	
	/**
	 * Has to set the text in the applications status bar.
	 * @param message the new status bar
	 */
	public void setStatusBarMessage(String statusText);
	
	/**
	 * Has to set the specified {@link JadeStatusColor}.
	 * @param jadeStatus the new jade status color
	 */
	public void setJadeStatusColor(JadeStatusColor jadeStatus);

	
	/**
	 * Has to return the instance of the application toolbar.
	 * @return the application toolbar
	 */
	public ToolBarType getApplicationToolbar();
		

	
	/**
	 * Configures the appearance of the application, depending on the current project configuration
	 */
	public void setProjectView(); 
	
	/**
	 * Has to show or hide the close button for a project.
	 * @param setVisible the new close project button visible
	 */
	public void setCloseProjectButtonVisible(boolean setVisible);
	
	
	/**
	 * Enable/Disables the SetupSelector in the toolbar.
	 * @param enable the enable
	 */
	public void setEnabledSetupSelector(boolean isSetEnabled);
	/**
	 * Checks if is enabled setup selector.
	 * @return true, if is enabled setup selector
	 */
	public boolean isEnabledSetupSelector();
	
	
	/**
	 * Sets the simulation ready2 start.
	 */
	public void setSimulationReady2Start();
	/**
	 * Checks if the simulation buttons are in the state ready to start.
	 * @return true, if the toolbar buttons for simulations are in state ready to start
	 */
	public boolean isSimulationReady2Start();

	
	/**
	 * Enable/Disables the button that starts a simulation .
	 * @param isSetEnabled the indicator to enable or disable
	 */
	public void setEnabledSimStart(boolean isSetEnabled);
	/**
	 * Checks if the button that starts a simulation is enabled or not.
	 * @return true, if the button is enabled
	 */
	public boolean isEnabledSimStart();

	
	/**
	 * Has to set the button that pauses a simulation enabled or disabled.
	 * @param isSetEnabled the indicator to enable or disable
	 */
	public void setEnabledSimPause(boolean isSetEnabled);
	/**
	 * Checks if the button to pause a simulation is enabled.
	 * @return true, if the button is enabled 
	 */
	public boolean isEnabledSimPause();
	
	/**
	 * Enable/Disables the button that stops a simulation.
	 * @param isSetEnabled the indicator to enable or disable
	 */
	public void setEnabledSimStop(boolean isSetEnabled);
	/**
	 * Checks if the button to stop a simulation is enabled.
	 * @return true, if the button is enabled 
	 */
	public boolean isEnabledSimStop();

	
	
	

	/**
	 * Has to add a menu to the AwbMainWindow.
	 * @param newMenu the new menu
	 */
	public void addMenu(Object newMenu);
	/**
	 * Has to add a menu to the AwbMainWindow.
	 *
	 * @param newMenu the new menu
	 * @param indexPosition the index position
	 */
	public void addMenu(Object newMenu, int indexPosition);

	
	/**
	 * Has to add a menu item component.
	 *
	 * @param menu2add2 the menu to which the item is to add
	 * @param newMenuItemComponent the new menu item component
	 */
	public void addMenuItemComponent(Object menu2add2, Object newMenuItemComponent);
	/**
	 * Has to add a menu item component.
	 *
	 * @param menu2add2 the menu to which the item is to add
	 * @param newMenuItemComponent the new menu item component
	 * @param indexPosition the index position
	 */
	public void addMenuItemComponent(Object menu2add2, Object newMenuItemComponent, int indexPosition);


	/**
	 * Has to add a new toolbar component to the toolbar.
	 * @param newComponent the new component
	 */
	public void addToolbarComponent(Object newComponent);
	/**
	 * Has to add a new toolbar component to the toolbar.
	 *
	 * @param newComponent the new component
	 * @param indexPosition the index position
	 */
	public void addToolbarComponent(Object newComponent, int indexPosition);


	public void removeComponent(Object component);
	
}
