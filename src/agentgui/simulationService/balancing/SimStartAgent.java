package agentgui.simulationService.balancing;

import jade.core.Agent;
import jade.core.ServiceException;
import agentgui.core.application.Application;
import agentgui.core.gui.CoreWindow;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;

public class SimStartAgent extends Agent {

	private static final long serialVersionUID = 7768569966599564839L;

	public final static int BASE_ACTION_Start = 0; 
	public final static int BASE_ACTION_Pause = 1;
	public final static int BASE_ACTION_Restart = 2;
	public final static int BASE_ACTION_Stop = 3;
	
	private CoreWindow mainWindow = Application.MainWindow;
	
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		int startArg = (Integer) startArgs[0];
		
		// --- If the Action is a 'start'-action, it can be also a 'restart'-action -----
		if (startArg==BASE_ACTION_Start) {
			if (mainWindow.jButtonSimStop.isEnabled()) {
				startArg = BASE_ACTION_Restart;
			}
		}
		
		// --- Do the wanted/necessary action -------------------------------------------
		switch (startArg) {
		case BASE_ACTION_Start:
			StaticLoadBalancingBase staticBalancing = this.getStartAndStaticLoadBalancingClass();
			if (staticBalancing!=null) {
				this.addBehaviour(staticBalancing);	
				mainWindow.setEnableSimStart(false);
				mainWindow.setEnableSimPause(true);
				mainWindow.setEnableSimStop(true);
			} else {
				this.doDelete();
			}
			break;

		case BASE_ACTION_Pause:
			this.setPauseSimulation(true);
			mainWindow.setEnableSimStart(true);
			mainWindow.setEnableSimPause(false);
			mainWindow.setEnableSimStop(true);
			this.doDelete();
			break;
		case BASE_ACTION_Restart:
			this.setPauseSimulation(false);
			mainWindow.setEnableSimStart(false);
			mainWindow.setEnableSimPause(true);
			mainWindow.setEnableSimStop(true);
			this.doDelete();
			break;
		case BASE_ACTION_Stop:
			//Application.JadePlatform.jadeStop();
			mainWindow.setEnableSimStart(true);
			mainWindow.setEnableSimPause(false);
			mainWindow.setEnableSimStop(false);
			this.doDelete();
			break;
		}
		
	}
	
	/**
	 * This method will pause or restart the current simulation
	 * @param pause
	 */
	private void setPauseSimulation(boolean pause) {
		
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setPauseSimulation(pause);
		} catch (ServiceException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * This method will start the one shot behaviour to start the agency
	 * @return
	 */
	private StaticLoadBalancingBase getStartAndStaticLoadBalancingClass() {
		
		// --- Get the current simulation setup -----------
		SimulationSetup currSimSetup = Application.ProjectCurr.simSetups.getCurrSimSetup();
		// --- Get the current distribution setup ---------
		DistributionSetup currDisSetup = currSimSetup.getDistributionSetup();
		
		if (currDisSetup.isDoStaticLoadBalalncing()== true) {

			try {
				@SuppressWarnings("unchecked")
				Class<? extends StaticLoadBalancingBase> staticLoadBalancingClass = (Class<? extends StaticLoadBalancingBase>) Class.forName(currDisSetup.getStaticLoadBalancingClass());	
				return staticLoadBalancingClass.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} else {
			return new StaticLoadBalancing();
		}
		return null;
	}
	
}
