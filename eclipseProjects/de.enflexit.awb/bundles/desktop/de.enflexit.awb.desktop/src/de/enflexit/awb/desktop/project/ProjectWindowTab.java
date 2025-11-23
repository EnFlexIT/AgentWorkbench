package de.enflexit.awb.desktop.project;

import java.awt.Container;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.ui.AwbProjectTab;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;


/**
 * This class can be used in order to add a customized component to the ProjectWindow
 * 
 * @see ProjectWindow
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectWindowTab implements AwbProjectWindowTab {
	
	private int displayType = 0;

	private Project currProject;
	private AwbProjectTab projectTabID;
	
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
	 * @param project the current project
	 * @param displayType_DEV_or_USER the display type_ de v_or_ user
	 * @param head_title the head_title
	 * @param aTipText the a tip text
	 * @param ico the icon
	 * @param component the component
	 * @param pareName the parent tabs name
	 */
	public ProjectWindowTab(Project project, int displayType_DEV_or_USER, String head_title, String aTipText, Icon ico, JComponent component, String pareName) {
		this(project, null, displayType_DEV_or_USER, head_title, aTipText, ico, component, pareName);
	}
	/**
	 * Default constructor for this class.
	 *
	 * @param project the current project
	 * @param projectTab the AwbProjectTab instance that identifies this tab
	 * @param displayType_DEV_or_USER the display type_ de v_or_ user
	 * @param head_title the head_title
	 * @param aTipText the a tip text
	 * @param ico the icon
	 * @param component the component
	 * @param pareName the parent tabs name
	 */
	public ProjectWindowTab(Project project, AwbProjectTab projectTabID, int displayType_DEV_or_USER, String head_title, String aTipText, Icon ico, JComponent component, String pareName) {
		
		this.currProject = project;		
		this.projectTabID = projectTabID;
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
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getProjectWindow()
	 */
	@Override
	public AwbProjectWindow getProjectWindow() {
		return this.currProject.getProjectEditorWindow();
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#add()
	 */
	@Override
	public void add() {
		try {
			this.getProjectWindow().addProjectTab(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#add(int)
	 */
	@Override
	public void add(int indexPosition) {
		this.setIndexPosition(indexPosition);
		this.getProjectWindow().addProjectTab(this, indexPosition);	
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#remove()
	 */
	@Override
	public void remove() {
		this.getProjectWindow().removeProjectTab(this);
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#toString()
	 */
	@Override
	public String toString() {
		return this.title;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbProjectWindowTab#setProjectTabID(de.enflexit.awb.core.ui.AwbProjectTab)
	 */
	@Override
	public void setProjectTabID(AwbProjectTab newProjectTabID) {
		this.projectTabID = newProjectTabID;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbProjectWindowTab#getProjectTabID()
	 */
	@Override
	public AwbProjectTab getProjectTabID() {
		return this.projectTabID;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setDisplayType(int)
	 */
	@Override
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getDisplayType()
	 */
	@Override
	public int getDisplayType() {
		return displayType;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getIcon()
	 */
	@Override
	public Icon getIcon() {
		return icon;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setIcon(javax.swing.Icon)
	 */
	@Override
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getTipText()
	 */
	@Override
	public String getTipText() {
		return tipText;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setTipText(java.lang.String)
	 */
	@Override
	public void setTipText(String tipText) {
		this.tipText = tipText;
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getParentName()
	 */
	@Override
	public String getParentName() {
		return parentName;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setParentName(java.lang.String)
	 */
	@Override
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getJComponentForVisualization()
	 */
	@Override
	public JComponent getJComponentForVisualization() {
		return comp;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setJComponentForVisualization(javax.swing.JComponent)
	 */
	@Override
	public void setJComponentForVisualization(JComponent comp) {
		this.comp = comp;
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setCompForChildComp(javax.swing.JTabbedPane)
	 */
	@Override
	public void setCompForChildComp(JTabbedPane jTabbedPane4ChildComponents) {
		this.compForChildComp = jTabbedPane4ChildComponents;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getCompForChildComp()
	 */
	@Override
	public JTabbedPane getCompForChildComp() {
		return compForChildComp;
	}

	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#setIndexPosition(int)
	 */
	@Override
	public void setIndexPosition(int indexPosition) {
		this.indexPosition = indexPosition;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#getIndexPosition()
	 */
	@Override
	public Integer getIndexPosition() {
		if (this.getJComponentForVisualization()!=null) {
			Container container = this.getJComponentForVisualization().getParent();
			if (container!=null && container instanceof JTabbedPane) {
				JTabbedPane tabbedPane = (JTabbedPane) container;
				indexPosition = tabbedPane.indexOfComponent(getJComponentForVisualization());
			}
		}
		return indexPosition;
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.project.AwbProjectWindowTab#updateIndexPosition()
	 */
	@Override
	public void updateIndexPosition() {
		this.getIndexPosition();
	}

}
