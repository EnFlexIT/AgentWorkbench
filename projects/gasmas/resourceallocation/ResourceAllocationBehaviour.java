package gasmas.resourceallocation;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;
import agentgui.simulationService.transaction.EnvironmentNotification;

public abstract class ResourceAllocationBehaviour {
	
	protected SimulationServiceBehaviour simServiceBehaviour;
	protected NetworkModel networkModel;
	protected NetworkComponent myNetworkComponent;
	
	/** NetworkComponentNames, which has positive flow */
	private List<String> incoming = new ArrayList<String>();

	/** NetworkComponentNames, which has negative flow */
	private List<String> outgoing = new ArrayList<String>();

	/** NetworkComponentNames, which has positive flow */
	private List<String> dead = new ArrayList<String>();
	
	private static final long serialVersionUID = 6832321297597749277L;
	/** HashMap which save the production of the neighbours */
	protected HashMap<String, AllocData> max_initial = new HashMap<String, AllocData>();

	/** HashMap which save the need of the neighbours */
	protected HashMap<String, AllocDataNeed> need = new HashMap<String, AllocDataNeed>();

	/** HashMap which save the usage of the neighbours */
	protected HashMap<String, AllocData> use = new HashMap<String, AllocData>();
	
	
	public ResourceAllocationBehaviour(SimulationServiceBehaviour simServiceBehaviour, NetworkModel networkModel, NetworkComponent networkComponent) {
		this.simServiceBehaviour = simServiceBehaviour;
		this.networkModel = networkModel;
		this.myNetworkComponent = networkComponent;
	}
	
	
	public List<String> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<String> incoming) {
		this.incoming = incoming;
	}

	public List<String> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<String> outgoing) {
		this.outgoing = outgoing;
	}

	public List<String> getDead() {
		return dead;
	}

	public void setDead(List<String> dead) {
		this.dead = dead;
	}

	public abstract void interpretMsg(EnvironmentNotification msg);

	public abstract String Status();
	
	public void msgSend(String receiver, GenericMesssageData content) {
		simServiceBehaviour.sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), content);

	}
}