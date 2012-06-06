package gasmas.resourceallocation;

import gasmas.agents.components.GenericNetworkAgent;
import jade.core.behaviours.TickerBehaviour;

public class Status extends TickerBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3596062928829613743L;
	
	ResourceAllocationBehaviour test1;
	public Status(GenericNetworkAgent a, long period, ResourceAllocationBehaviour test) {
		super(a, period);
		test1=test;

	}

	@Override
	protected void onTick() {
		System.out.println(myAgent.getLocalName()+"::"+test1.Status());
	}

}
