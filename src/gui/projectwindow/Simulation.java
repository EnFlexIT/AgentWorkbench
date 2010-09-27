package gui.projectwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;


import mas.display.DisplayAgentGUI;

import application.Application;
import application.Language;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class Simulation extends JScrollPane implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	
	private JButton btnStart = null;
	
	private JCheckBox cbStartAgents = null;
	
	private DisplayAgentGUI daGUI;
	
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
		this.add(getCbStartAgents());
		
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
	
	private JCheckBox getCbStartAgents(){
		if(cbStartAgents == null){
			cbStartAgents = new JCheckBox("Start Project Agents?", true);
			cbStartAgents.setSize(cbStartAgents.getPreferredSize());
			cbStartAgents.setLocation(getBtnStart().getLocation().x, getBtnStart().getLocation().y - 30 );
		}
		
		return cbStartAgents;
	}
	
	/**
	 * Starts a DisplayAgent and the project agents (later)
	 */
	@SuppressWarnings("unchecked")
	private void startSimulation(boolean agents){
		
		this.daGUI = new DisplayAgentGUI();
		this.daGUI.setSize(this.getWidth(), this.getHeight());
		this.remove(btnStart);
		this.remove(cbStartAgents);
		this.add(this.daGUI);
		this.daGUI.setVisible(true);
		
		if ( Application.JadePlatform.jadeMainContainerIsRunning(true)){
			
			
			String projectContainer = CurrProject.getProjectFolder();
			
			Object[] args = {CurrProject.getEnvironment()};
			Application.JadePlatform.jadeAgentStart("SMAStart", sma.agents.SMAStarterAgent.class, args, Application.JadePlatform.MASmc.getName());
			Application.JadePlatform.jadeContainerCreate(projectContainer);
//			Object[] args = {CurrProject.getEnvironment()};
//			String ecaBaseName = "ECA_"+CurrProject.getProjectName();
//			String ecaName = ecaBaseName;
//			int ecaSuffix = 0;
//			while( Application.JadePlatform.jadeAgentIsRunning(ecaName)){
//				ecaName = ecaBaseName + (++ecaSuffix);
//			}
//			
//			Application.JadePlatform.jadeAgentStart(ecaName, "mas.environment.EnvironmentControllerAgent", args, projectContainer);
//				
//			// Start DisplayAgent
//			// Find name
//			String daNameBase = "DA";
//			int daSuffix = 0;
//			String daName = daNameBase+daSuffix;
//			while( Application.JadePlatform.jadeAgentIsRunning(daName)){
//				daName = daNameBase + (++daSuffix);
//			}
//			args = new Object[]{CurrProject.getProjectName(), this.daGUI, CurrProject.getEnvironment()};
//			Application.JadePlatform.jadeAgentStart(daName, "mas.display.DisplayAgent", args, projectContainer );
//			
//			// Start project agents
//			if(agents){
//				
//			
////				Iterator<AbstractObject> objects = CurrProject.getEnvironment().getAllObjects();
////				
////				while(objects.hasNext()){
////					AbstractObject object = objects.next();
////					if(ObjectTypes.getType(object) == ObjectTypes.AGENT){
////						daName = object.getId();
////						String agentClass = ((AgentObject)object).getAgentClass();
////						args = new Object[]{object};					
////						Application.JadePlatform.jadeAgentStart(daName, agentClass, args, projectContainer );
////					}
////				}
//			}

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
			startSimulation(getCbStartAgents().isSelected());
		}
		else {
			
		};
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
