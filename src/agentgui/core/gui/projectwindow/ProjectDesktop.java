package agentgui.core.gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import agentgui.core.application.Project;

import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;

/**
 * @author: Christian Derksen
 */
public class ProjectDesktop extends JDesktopPane implements Observer, ActionListener {

	private static final long serialVersionUID = -9219224867898326652L;
	private Project currProject;

	/**
	 * This is the default constructor
	 */
	public ProjectDesktop( Project cp ) {
		super();
		this.currProject = cp;
		this.currProject.addObserver(this);		
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


	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		System.out.println( "ActCMD/Wert => " + ActCMD );
		System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == "ProjectName" ) {
			currProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectFolder" ) {
			currProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectDescription" ) {
			currProject.setProjectDescription( ae.getActionCommand() );
		}
		else {
			
		};
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
