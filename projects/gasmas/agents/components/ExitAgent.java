package gasmas.agents.components;

import agentgui.envModel.graph.visualisation.notifications.DataModelNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.Location;

public class ExitAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 5755894155609484866L;

	@Override
	protected void setup() {
		super.setup();
	}
	
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		// TODO Auto-generated method stub
	}
	@Override
	public void setMigration(Location newLocation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification) {
		
		if (notification.getNotification() instanceof DataModelNotification) {
			System.out.println(this.getLocalName() + ": Data Model update received!");
		}
		
		
		return super.onEnvironmentNotification(notification);
	}
	
}
