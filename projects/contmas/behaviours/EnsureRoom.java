/**
 * 
 */
package contmas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.Administered;
import contmas.ontology.Announced;
import contmas.ontology.Assigned;
import contmas.ontology.InExecution;
import contmas.ontology.PendingForSubCFP;
import contmas.ontology.PlannedOut;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
import contmas.ontology.TransportOrderChainState;

class EnsureRoom extends OneShotBehaviour{
	//FSM events
	final static Integer HAS_ROOM=0;
	final static Integer TRY_FREEING= -1;
	private TransportOrderChain curTOC;
	private TransportOrder curTO;
	
	/**
	 * 
	 */
	private ContainerHolderAgent myCAgent;
	Integer returnState;

	EnsureRoom(Agent a,DataStore ds){
		super(a);
		myCAgent=(ContainerHolderAgent) a;
		setDataStore(ds);

	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		returnState=HAS_ROOM;

		if( !myCAgent.hasBayMapRoom()){
			if(myCAgent instanceof TransportOrderOfferer){
				TransportOrderChain someTOC;
				someTOC=myCAgent.getSomeTOCOfState(new Announced());
				if(someTOC == null){
					someTOC=myCAgent.getSomeTOCOfState(new InExecution());
				}
				if(someTOC == null){
					someTOC=myCAgent.getSomeTOCOfState(new Assigned());
				}
				if(someTOC == null){
					someTOC=myCAgent.getSomeTOCOfState(new PlannedOut());
				}
				if(someTOC == null){
					someTOC=myCAgent.getSomeTOCOfState(new Administered());
					if(someTOC != null){
						myCAgent.echoStatus("BayMap voll, versuche Räumung für",curTOC,ContainerAgent.LOGGING_INFORM);
						TransportOrderChainState state=new PendingForSubCFP();
						state.setLoad_offer(curTO);
						myCAgent.setTOCState(curTOC,state);
						myCAgent.releaseContainer(someTOC);
//						myCAgent.registerForWakeUpCall(this);
					}
				}

				if(someTOC != null){ // TOC is in one of the above states, so all steps taken, just sit back and relax
//					myCAgent.registerForWakeUpCall(this);
					returnState=TRY_FREEING;
				}else{ //keine administrierten TOCs da
					myCAgent.echoStatus("FAILURE: BayMap full, no administered TOCs available, clearing not possible.",ContainerAgent.LOGGING_ERROR);
					returnState=ACLMessage.REFUSE;
				}
			}
		}
	}
	
	public void configure(TransportOrderChain curTOC, TransportOrder curTO){
		this.curTOC=curTOC;
		this.curTO=curTO;
	}

	@Override
	public int onEnd(){
		return returnState;
	}
}