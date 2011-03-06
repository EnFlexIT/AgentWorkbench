package agentgui.core.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import agentgui.core.application.Project;

/**
 * 
 * @author Christian Derksen
 */
public class ProjectWindowTab {
	
	public final static int DISPLAY_4_END_USER = 0;
	public final static int DISPLAY_4_END_USER_VISUALIZATION = 1;
	public final static int DISPLAY_4_DEVELOPER = 10;
	private int displayType = 0;

	private Project project;
	
	private String title;
	private String tipText;
	private Icon icon;
	
	private String parentName;
	private Component comp;
	private JTabbedPane compForChildComp;


	/**
	 * Default constructor for this class
	 * @param displayType_DEV_or_USER
	 * @param head_title
	 * @param ico
	 * @param component
	 * @param tip
	 */
	public ProjectWindowTab(Project currProject, int displayType_DEV_or_USER, String head_title, String aTipText, Icon ico, Component component, String pareName, JTabbedPane jTabbedPane4ChildComponents) {
		
		this.project = currProject;
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
		this.compForChildComp = jTabbedPane4ChildComponents;
		
	}
	
	/**
	 * Adds the current Tab-Object to the ProjectWindow 
	 */
	public void add() {
		this.project.projectWindow.addProjectTab(this);	
	}
	
	/**
	 * @return the nam of the component
	 */
	public String toString() {
		return this.title;
	}
	
	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	/**
	 * @return the displayType
	 */
	public int getDisplayType() {
		return displayType;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the icon
	 */
	public Icon getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	/**
	 * @return the tipText
	 */
	public String getTipText() {
		return tipText;
	}
	/**
	 * @param tipText the tipText to set
	 */
	public void setTipText(String tipText) {
		this.tipText = tipText;
	}
	
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	/**
	 * @return the comp
	 */
	public Component getComponent() {
		return comp;
	}
	/**
	 * @param comp the comp to set
	 */
	public void setComponent(Component comp) {
		this.comp = comp;
	}

	/**
	 * @param comp4childcomp the comp4childcomp to set
	 */
	public void setCompForChildComp(JTabbedPane jTabbedPane4ChildComponents) {
		this.compForChildComp = jTabbedPane4ChildComponents;
	}
	/**
	 * @return the comp4childcomp
	 */
	public JTabbedPane getCompForChildComp() {
		return compForChildComp;
	}
	
}

	

