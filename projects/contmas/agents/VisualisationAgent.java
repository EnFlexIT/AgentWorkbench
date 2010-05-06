package contmas.agents;

import contmas.behaviours.listenForLogMessage;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.Logger;
import jade.core.AID;
import jade.core.Agent;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class VisualisationAgent extends Agent implements Logger,DFSubscriber{

	private static final long serialVersionUID= -2081699287513185474L;
	
	List allActors=new ArrayList();

	public VisualisationAgent(){
		super();
	}

	@Override
	protected void setup(){
		super.setup();
		addBehaviour(new listenForLogMessage(this));
		addBehaviour(new subscribeToDF(this,"container-handling"));
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.Logger#processLogMsg(java.lang.String)
	 */
	@Override
	public void processLogMsg(String logMsg){
		System.out.print("[VisualisationAgent says] "+logMsg);
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(allActors,updatedAgents);
		}else{
			ContainerAgent.removeFromList(allActors,updatedAgents);
		}
		if(!updatedAgents.isEmpty()){
			System.out.print("[VisualisationAgent says] Agents ");
			if(remove){
				System.out.println("disappeared: ");
			} else {
				System.out.println("emerged: ");
			}
			Iterator iter=updatedAgents.iterator();
			while(iter.hasNext()){
				AID actor=(AID) iter.next();
				System.out.print(actor.getLocalName());
			}
			System.out.print("\n");
		}
	}
}
