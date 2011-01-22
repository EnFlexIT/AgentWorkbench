package agentgui.core.gui.projectwindow.simsetup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import agentgui.core.application.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.graphEnvironment.controller.GraphEnvironmentControllerGUI;
import agentgui.physical2Denvironment.controller.Physical2DEnvironmentControllerGUI;

public class SimulationEnvironment extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3230313372954316520L;
	
	private StartSetupSelector jPanelTopNew = null;
	
	private Component environmentControllerGUI = null;
	
	private Project project;
	
	public SimulationEnvironment(Project project){
		this.project = project;
		this.initialize();
	}
	
	private void initialize(){
		
		// If no environment mode is specified, set Physical2D as default
		if(project.simSetups.getCurrSimSetup().getEnvironmentMode() == null){
			project.simSetups.getCurrSimSetup().setEnvironmentMode(SimulationSetup.ENVMODE_PHYSICAL2D);
		}
		
		
		this.setLayout(new BorderLayout());
		this.add(getJPanelTopNew(), BorderLayout.NORTH);
		this.add(getEnvironmentControllerGUI(), BorderLayout.CENTER);
		
		project.addObserver(this);
	}
	
	private Component getEnvironmentControllerGUI(){
		String envMode = project.simSetups.getCurrSimSetup().getEnvironmentMode();
		
		if(envMode.equals(SimulationSetup.ENVMODE_PHYSICAL2D)){
			environmentControllerGUI = new Physical2DEnvironmentControllerGUI(project);
		}else if(envMode.equals(SimulationSetup.ENVMODE_GRAPH)){
			environmentControllerGUI = new GraphEnvironmentControllerGUI(project);
		}
		return environmentControllerGUI;
	}
	
	private void switchEnvironmentMode(String mode){
		this.remove(environmentControllerGUI);
		if(mode == SimulationSetup.ENVMODE_PHYSICAL2D){
			this.environmentControllerGUI = new Physical2DEnvironmentControllerGUI(project);
		}else if(mode == SimulationSetup.ENVMODE_GRAPH){
			this.environmentControllerGUI = new GraphEnvironmentControllerGUI(project);
		}
		this.add(environmentControllerGUI, BorderLayout.CENTER);
	}
	
	/**
	 * This method initializes jPanelTopNew	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTopNew() {
		if (jPanelTopNew == null) {
			jPanelTopNew = new StartSetupSelector(this.project);
		}
		return jPanelTopNew;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(project) && arg.equals(StartSetupSelector.SETUP_ENVMODE_CHANGE)){
			switchEnvironmentMode(project.simSetups.getCurrSimSetup().getEnvironmentMode());
		}
	}

}
