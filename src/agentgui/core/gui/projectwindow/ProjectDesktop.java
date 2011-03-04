package agentgui.core.gui.projectwindow;

import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;

import agentgui.core.application.Project;

/**
 * @author: Christian Derksen
 */
public class ProjectDesktop extends JDesktopPane {

	private static final long serialVersionUID = -9219224867898326652L;
	private Project currProject;

	/**
	 * This is the default constructor
	 */
	public ProjectDesktop( Project project ) {
		super();
		this.currProject = project;
		initialize();	
		this.currProject.projectDesktop = this;
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setAutoscrolls(true);		
		this.addContainerListener(new java.awt.event.ContainerListener() {
			public void componentAdded(java.awt.event.ContainerEvent e) {
				//System.out.println( e.getSource() );
				setFocus();
			}
			public void componentRemoved(java.awt.event.ContainerEvent e) {
			}
		});
	}

	/**
	 * Set's the focus to the project-desktop
	 */
	public void setFocus () {
		((JTabbedPane) this.getParent()).setSelectedComponent(this);
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
