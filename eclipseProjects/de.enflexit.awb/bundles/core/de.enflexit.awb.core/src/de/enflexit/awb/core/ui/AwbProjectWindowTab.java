package de.enflexit.awb.core.ui;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * The Interface AwbProjectWindowTab.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbProjectWindowTab {

	public static final int DISPLAY_4_END_USER = 0;
	public static final int DISPLAY_4_END_USER_VISUALIZATION = 1;
	public static final int DISPLAY_4_DEVELOPER = 10;
	public static final String TAB_4_SUB_PANES_Configuration = "Configuration";
	public static final String TAB_4_SUB_PANES_Setup = "Setup";
	public static final String TAB_4_RUNTIME_VISUALIZATION = "Laufzeit-Visualisierung";

	
	/**
	 * Returns the current instance of the {@link ProjectWindow}.
	 * @return the project window
	 */
	public AwbProjectWindow getProjectWindow();

	/**
	 * Adds the current Tab-object to the project window.
	 */
	public void add();

	/**
	 * Adds the current Tab-object to the project window
	 * at the given index position.
	 *
	 * @param indexPosition the index position greater one
	 */
	public void add(int indexPosition);

	/**
	 * This removes the current Tab from the project window.
	 */
	public void remove();

	/**
	 * To string.
	 * @return the title of the component
	 */
	public String toString();

	
	/**
	 * Sets the project tab ID.
	 * @param projectTabID the new project tab ID
	 */
	public void setProjectTabID(AwbProjectTab projectTabID);
	
	/**
	 * Gets the project tab ID.
	 * @return the project tab ID
	 */
	public AwbProjectTab getProjectTabID();
	
	
	/**
	 * Sets the display type.
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(int displayType);

	/**
	 * Gets the display type.
	 * @return the displayType
	 */
	public int getDisplayType();

	
	/**
	 * Gets the title.
	 * @return the title
	 */
	public String getTitle();

	/**
	 * Sets the title.
	 * @param title the title to set
	 */
	public void setTitle(String title);

	
	/**
	 * Gets the icon.
	 * @return the icon
	 */
	public Icon getIcon();

	/**
	 * Sets the icon.
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon);

	
	/**
	 * Gets the tip text.
	 * @return the tipText
	 */
	public String getTipText();

	/**
	 * Sets the tip text.
	 * @param tipText the tipText to set
	 */
	public void setTipText(String tipText);

	
	/**
	 * Gets the parent name.
	 * @return the parentName
	 */
	public String getParentName();

	/**
	 * Sets the parent name.
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName);

	
	/**
	 * Returns the JComponent for the visualization.
	 * @return the JComponent
	 */
	public JComponent getJComponentForVisualization();

	/**
	 * Sets the JComponent for the visualization.
	 * @param comp the new JComponent for the visualization
	 */
	public void setJComponentForVisualization(JComponent comp);

	/**
	 * Sets the JTabbedPane for possible child components.
	 * @param jTabbedPane4ChildComponents the new JTabbedPane for child components
	 */
	public void setCompForChildComp(JTabbedPane jTabbedPane4ChildComponents);

	/**
	 * Gets the comp for child comp.
	 * @return the comp4childcomp
	 */
	public JTabbedPane getCompForChildComp();

	/**
	 * Sets the index position.
	 * @param indexPosition the indexPosition to set
	 */
	public void setIndexPosition(int indexPosition);

	/**
	 * Gets the index position.
	 * @return the indexPosition
	 */
	public Integer getIndexPosition();

	/**
	 * Updates the index position.
	 */
	public void updateIndexPosition();

}