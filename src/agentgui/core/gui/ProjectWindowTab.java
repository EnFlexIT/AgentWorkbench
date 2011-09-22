package agentgui.core.gui;

import javax.swing.Icon;
import javax.swing.JComponent;
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

	private Project currProject;
	
	private String title;
	private String tipText;
	private Icon icon;
	
	private String parentName;
	private JComponent comp;
	private JTabbedPane compForChildComp;

	private int indexPosition = -1;

	/**
	 * Default constructor for this class
	 * @param displayType_DEV_or_USER
	 * @param head_title
	 * @param ico
	 * @param component
	 * @param tip
	 */
	public ProjectWindowTab(Project project, int displayType_DEV_or_USER, String head_title, String aTipText, Icon ico, JComponent component, String pareName, JTabbedPane jTabbedPane4ChildComponents) {
		
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
		this.compForChildComp = jTabbedPane4ChildComponents;
		
	}
	
	/**
	 * Adds the current Tab-object to the project window 
	 */
	public void add() {
		this.currProject.projectWindow.addProjectTab(this);	
	}
	/**
	 * Adds the current Tab-object to the project window  
	 * at the given index position
	 * @param indexPosotionGreaterOne
	 */
	public void add(int indexPositionGreaterOne) {
		this.currProject.projectWindow.addProjectTab(this, indexPositionGreaterOne);	
	}
	
	/**
	 * This removes the current Tab from the project window  
	 */
	public void remove() {
		this.currProject.projectWindow.remove(this);
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
	public JComponent getComponent() {
		return comp;
	}
	/**
	 * @param comp the comp to set
	 */
	public void setJComponent(JComponent comp) {
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

	/**
	 * @param indexPosition the indexPosition to set
	 */
	public void setIndexPosition(int indexPosition) {
		this.indexPosition = indexPosition;
	}
	/**
	 * @return the indexPosition
	 */
	public int getIndexPosition() {
		return indexPosition;
	}

}

	

