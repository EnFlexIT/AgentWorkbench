package gasmas.resourceallocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gasmas.agents.components.GenericNetworkAgent;
import agentgui.simulationService.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

public abstract class ResourceAllocationBehaviour extends CyclicBehaviour {
	
	/** NetworkComponentNames, which has positive flow */
	private List<String> incoming = new ArrayList<String>();

	/** NetworkComponentNames, which has negative flow */
	private List<String> outgoing = new ArrayList<String>();

	/** NetworkComponentNames, which could have a positive or negative flow */
	private List<String> optional = new ArrayList<String>();

	/** NetworkComponentNames, which has positive flow */
	private List<String> dead = new ArrayList<String>();

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6832321297597749277L;
	/** HashMap which save the production of the neighbours */
	protected HashMap<String, AllocData> max_initial = new HashMap<String, AllocData>();

	/** HashMap which save the need of the neighbours */
	protected HashMap<String, AllocDataNeed> need = new HashMap<String, AllocDataNeed>();

	/** HashMap which save the usage of the neighbours */
	protected HashMap<String, AllocData> use = new HashMap<String, AllocData>();

	@Override
	public void action() {

	}
	
	public abstract void interpretMsg(EnvironmentNotification msg);

	public abstract String Status();
	
	public void msgSend(String receiver, GenericMesssageData content) {
		((GenericNetworkAgent) myAgent).sendAgentNotification(new AID(receiver, AID.ISLOCALNAME), content);

	}
}