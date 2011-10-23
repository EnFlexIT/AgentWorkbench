package agentgui.simulationService.agents.example;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;

public class RemoteStarterAgent extends Agent {

	
	private static final long serialVersionUID = 3649851139158388559L;

	@Override
	protected void setup() {

		this.addBehaviour(new StarterCycle());
	}
	
	private class StarterCycle extends CyclicBehaviour{

		private static final long serialVersionUID = -3389907697703023520L;

		@Override
		public void action() {
		
			//this.block();
			// --- Start a new remote container -----------------
			LoadServiceHelper loadHelper;
			try {
				loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
				String newContainerName = loadHelper.startNewRemoteContainer(false);
				while (true) {
					Container2Wait4 waitCont = loadHelper.startNewRemoteContainerStaus(newContainerName);	
					if (waitCont.isStarted()) {
						System.out.println("Remote Container '" + newContainerName + "' was started!");
						break;
					}
					if (waitCont.isCancelled()) {
						System.out.println("Remote Container '" + newContainerName + "' was NOT started!");
						break;
					}
					if (waitCont.isTimedOut()) {
						System.out.println("Remote Container '" + newContainerName + "' timed out!");
						break;	
					}
					this.block(100);
				} // end while
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			myAgent.doSuspend();
			
		}

		
	}
	
	
	
}
