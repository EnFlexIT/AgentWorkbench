package agentgui.core.gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class Simulation extends JScrollPane implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	
	private JButton btnStart = null;
	
	/**
	 * This is the default constructor
	 */
	public Simulation( Project CP ) {
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
		
		this.add(getBtnStart());
		
		this.setVisible(true);		
	}
	
	/**
	 * This method initializes btnStart
	 * @return javax.swing.JButton
	 */
	
	private JButton getBtnStart(){
		if(btnStart == null){
			btnStart = new JButton();
			btnStart.setSize(150, 25);
			btnStart.setLocation(this.getWidth()/2-75, this.getHeight()/2-15);
			btnStart.setText(Language.translate("Simulation starten"));
			btnStart.addActionListener(this);
		}
		return btnStart;
	}
	
	/**
	 * Starts a DisplayAgent and the project agents (later)
	 */
	private void startSimulation(){
		
//		this.btnStart.setEnabled(false);
				
		if ( Application.JadePlatform.jadeMainContainerIsRunning(true)){
			
			
			String projectContainer = CurrProject.getProjectFolder();
			
			Object[] args = {CurrProject.getEnvironment(), CurrProject.getSVGDoc()};
			Application.JadePlatform.jadeContainerCreate(projectContainer);
			Application.JadePlatform.jadeAgentStart("EPA_"+CurrProject.getProjectName(), agentgui.physical2Denvironment.provider.EnvironmentProviderAgent.class, args, projectContainer);

		}
		
	}

	/**
	 * This method initializes ProjectTitel	
	 * 	
	 * @return javax.swing.JTextField	
	 */


	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
//		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
//		System.out.println( "ActCMD/Wert => " + ActCMD );
//		System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == "ProjectName" ) {
			CurrProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectFolder" ) {
			CurrProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectDescription" ) {
			CurrProject.setProjectDescription( ae.getActionCommand() );
		}else if( Trigger == btnStart){
			startSimulation();
		}
		else {
			
		};
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
