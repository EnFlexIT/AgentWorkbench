package gasmas.agents.components;

import gasmas.resourceallocation.AllocData;
import gasmas.resourceallocation.FindDirData;
import gasmas.resourceallocation.FindDirectionBehaviour;
import gasmas.resourceallocation.ResourceAllocationBehaviour;

import java.util.ArrayList;
import java.util.List;

import jade.core.ServiceException;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public abstract class GenericNetworkAgent extends SimulationAgent {

	private static final long serialVersionUID = 1743261783247570185L;

	protected SimulationServiceHelper simHelper;
	protected EnvironmentModel envModel;

	protected NetworkModel myNetworkModel = null;

	/** Msgpool, which contains messages, which are not needed at the moment */
	private List<EnvironmentNotification> msgpool = new ArrayList<EnvironmentNotification>();

	protected ResourceAllocationBehaviour resourceAllocationBehaviour;

	protected FindDirectionBehaviour findDirectionBehaviour;

	@Override
	protected void setup() {

		super.setup();

		while (this.myEnvironmentModel == null) {
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				envModel = simHelper.getEnvironmentModel();
				if (envModel != null) {
					this.myEnvironmentModel = envModel;
					break;
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		this.myNetworkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();
		findDirectionBehaviour = new FindDirectionBehaviour(this, 20000, myEnvironmentModel);
		findDirectionBehaviour.onStart();
//		this.addBehaviour(findDirectionBehaviour);
//		startRA();
	}

	@Override
	protected void onEnvironmentNotification(EnvironmentNotification notification) {
		// System.out.println("Got Message " + this.getLocalName() + "...von..."
		// + notification.getSender().getLocalName());
		if (notification.getNotification() instanceof AllocData) {
			if (resourceAllocationBehaviour == null) {
				msgpool.add(notification);
			} else {
				resourceAllocationBehaviour.interpretMsg(notification);
			}
		} else if (notification.getNotification() instanceof FindDirData) {
			if (findDirectionBehaviour == null) {
				System.out.println("is this happening?");
			} else {
				findDirectionBehaviour.interpretMsg(notification);
			}
		}

	}


}
