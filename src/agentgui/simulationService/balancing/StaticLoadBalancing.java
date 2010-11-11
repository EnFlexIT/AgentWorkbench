package agentgui.simulationService.balancing;

public class StaticLoadBalancing extends StaticLoadBalancingBase {

	private static final long serialVersionUID = -6884445863598676300L;

	public StaticLoadBalancing() {
		super();
	}
	
	@Override
	public void action() {
		
		// --- If we use the static load balancing, get the parameter ---- 
		// --- and start the indicated number of container			  ----
		int numberOfAgents = currDisSetup.getNumberOfAgents();
		int numberOfContainer = currDisSetup.getNumberOfContainer();
		
		// --- Just in case, that we don't have enough information ---
		if (numberOfContainer==0) {
			if (numberOfAgents!=0) {
				int noAgentsMax = currThresholdLevels.getThNoThreadsH();
				numberOfContainer = (int) Math.ceil(((float)numberOfAgents / (float)noAgentsMax)) + 1;
			}
		}
		
		if (numberOfAgents!=0) {
			// --- Start getting the desired number of container -----
			this.startRemoteContainer(numberOfContainer - 1);
		}
		
		
		
		
		
	}


	
}
