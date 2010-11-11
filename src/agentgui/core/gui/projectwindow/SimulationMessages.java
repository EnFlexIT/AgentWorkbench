package agentgui.core.gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import agentgui.core.application.Language;
import agentgui.core.application.Project;

import javax.swing.JDesktopPane;

/**
 * @author: Christian Derksen
 *
 */
public class SimulationMessages extends JDesktopPane implements Observer, ActionListener {

	private Project CurrProject;
	
	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public SimulationMessages( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		initialize();	
		this.CurrProject.ProjectDesktop = this;
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
	 * Set's the focus to the current project-message
	 */
	public void setFocus () {
		CurrProject.setFocus(false);
		CurrProject.ProjectGUI.setFocusOnProjectTab(Language.translate("Simulations-Meldungen"));
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
			CurrProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectFolder" ) {
			CurrProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectDescription" ) {
			CurrProject.setProjectDescription( ae.getActionCommand() );
		}
		else {
			
		};
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
