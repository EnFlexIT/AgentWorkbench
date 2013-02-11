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
package agentgui.core.gui.projectwindow;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import agentgui.core.gui.ProjectWindow;
import agentgui.core.project.Project;

/**
 * This class can be used in order to add a customized component to the ProjectWindow
 * 
 * @see ProjectWindow
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectWindowTab {
	
	public final static int DISPLAY_4_END_USER = 0;
	public final static int DISPLAY_4_END_USER_VISUALIZATION = 1;
	public final static int DISPLAY_4_DEVELOPER = 10;
	private int displayType = 0;

	public final static String TAB_4_SUB_PANES_Configuration = "Configuration";
	public final static String TAB_4_SUB_PANES_SimSetup ="SimulationSetup";
	
	private Project currProject;
	
	private String title;
	private String tipText;
	private Icon icon;
	
	private String parentName;
	private JComponent comp;
	private JTabbedPane compForChildComp;

	private int indexPosition = -1;

	/**
	 * Default constructor for this class.
	 *
	 * @param project the project
	 * @param displayType_DEV_or_USER the display type_ de v_or_ user
	 * @param head_title the head_title
	 * @param aTipText the a tip text
	 * @param ico the ico
	 * @param component the component
	 * @param pareName the pare name
	 */
	public ProjectWindowTab(Project project, int displayType_DEV_or_USER, String head_title, String aTipText, Icon ico, JComponent component, String pareName) {
		
		this.currProject = project;		
		this.displayType = displayType_DEV_or_USER;
		
		this.title = head_title;
		if (aTipText==null) {
			this.tipText = this.title;
		} else {
			this.tipText = aTipText;
		}
		this.tipText = aTipText;
		this.icon = ico;
		
		if (pareName==null || pareName.equals("")) {
			this.parentName = null;
		} else {
			this.parentName = pareName;	
		}		
		
		this.comp = component;
		if (component instanceof TabForSubPanels) {
			this.compForChildComp = ((TabForSubPanels) component).getJTabbedPane();	
		}
		
	}
	
	/**
	 * Adds the current Tab-object to the project window.
	 */
	public void add() {
		try {
			this.currProject.getProjectWindow().addProjectTab(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Adds the current Tab-object to the project window
	 * at the given index position.
	 *
	 * @param indexPositionGreaterOne the index position greater one
	 */
	public void add(int indexPositionGreaterOne) {
		this.currProject.getProjectWindow().addProjectTab(this, indexPositionGreaterOne);	
	}
	
	/**
	 * This removes the current Tab from the project window.
	 */
	public void remove() {
		this.currProject.getProjectWindow().remove(this);
	}
	
	/**
	 * To string.
	 *
	 * @return the nam of the component
	 */
	public String toString() {
		return this.title;
	}
	
	/**
	 * Sets the display type.
	 *
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	
	/**
	 * Gets the display type.
	 *
	 * @return the displayType
	 */
	public int getDisplayType() {
		return displayType;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public Icon getIcon() {
		return icon;
	}
	
	/**
	 * Sets the icon.
	 *
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	/**
	 * Gets the tip text.
	 *
	 * @return the tipText
	 */
	public String getTipText() {
		return tipText;
	}
	
	/**
	 * Sets the tip text.
	 *
	 * @param tipText the tipText to set
	 */
	public void setTipText(String tipText) {
		this.tipText = tipText;
	}
	
	/**
	 * Gets the parent name.
	 *
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	
	/**
	 * Sets the parent name.
	 *
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	/**
	 * Gets the component.
	 *
	 * @return the comp
	 */
	public JComponent getComponent() {
		return comp;
	}
	
	/**
	 * Sets the j component.
	 *
	 * @param comp the comp to set
	 */
	public void setJComponent(JComponent comp) {
		this.comp = comp;
	}

	/**
	 * Sets the comp for child comp.
	 *
	 * @param jTabbedPane4ChildComponents the new comp for child comp
	 */
	public void setCompForChildComp(JTabbedPane jTabbedPane4ChildComponents) {
		this.compForChildComp = jTabbedPane4ChildComponents;
	}
	
	/**
	 * Gets the comp for child comp.
	 *
	 * @return the comp4childcomp
	 */
	public JTabbedPane getCompForChildComp() {
		return compForChildComp;
	}

	/**
	 * Sets the index position.
	 *
	 * @param indexPosition the indexPosition to set
	 */
	public void setIndexPosition(int indexPosition) {
		this.indexPosition = indexPosition;
	}
	
	/**
	 * Gets the index position.
	 *
	 * @return the indexPosition
	 */
	public int getIndexPosition() {
		return indexPosition;
	}

}

	

