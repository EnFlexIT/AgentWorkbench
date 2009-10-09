package gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;

import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class SetupEnvironment extends JScrollPane implements Observer, ActionListener {

	private Project CurrProject;
	
	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public SetupEnvironment( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		initialize();	
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setLayout(null);
		this.setSize(850, 500);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		
		
	}

	/**
	 * This method initializes ProjectTitel	
	 * 	
	 * @return javax.swing.JTextField	
	 */


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
