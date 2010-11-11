package agentgui.simulationService.balancing;

import jade.core.Agent;
import agentgui.core.application.Application;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;

public class SimStartAgent extends Agent {

	private static final long serialVersionUID = 7768569966599564839L;

	public final static int BASE_ACTION_Start = 0; 
	public final static int BASE_ACTION_Pause = 1;
	public final static int BASE_ACTION_Stop = 2;
	
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		int startArg = (Integer) startArgs[0];
		
		switch (startArg) {
		case BASE_ACTION_Start:
			StaticLoadBalancingBase staticBalancing = this.getStartAndStaticLoadBalancingClass();
			if (staticBalancing!=null) {
				this.addBehaviour(staticBalancing);	
			} else {
				this.doDelete();
			}
			break;

		case BASE_ACTION_Pause:
			System.out.println("Pausiere die Simulation .....");
			break;
		case BASE_ACTION_Stop:
			System.out.println("Stop die Simulation .....");
			break;
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
