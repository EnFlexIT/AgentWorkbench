/**
 * @author Hanno - Felix Wagner, 24.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.behaviours;

import contmas.agents.*;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class listenForStartAgentReq extends SequentialBehaviour implements OntRepProvider{
	public ContainerHolder ontRep=null;
	public Boolean populate=true;
	public Boolean randomize=false;
	public ACLMessage START_AGENT_REQUEST_KEY=null;
	public ACLMessage START_AGENT_RESPONSE_KEY=null;

	/**
	 * @param a
	 */
	public listenForStartAgentReq(Agent a){
		super(a);

		Behaviour b=new receiveStartAgentRequest(a);//AchieveREListener (send agree?)
		this.setDataStore(b.getDataStore());
		this.addSubBehaviour(b);

	}

	public void nextStep(){
		Behaviour b;
		if(randomize){
			b=new fetchRandomBayMap(myAgent);//GetRandomBayMap
			this.addSubBehaviour(b);
			b.setDataStore(this.getDataStore());
		}
		if(populate){
			b=new getPopulatedBayMap(myAgent);//PopulateBayMap
			this.addSubBehaviour(b);
			b.setDataStore(this.getDataStore());
		}
		b=new startAgent(myAgent);//Start the Agent and send inform
		this.addSubBehaviour(b);
		b.setDataStore(this.getDataStore());
	}

	/* (non-Javadoc)
	 * @see contmas.agents.OntRepProvider#getOntologyRepresentation(jade.core.AID)
	 */
	@Override
	public ContainerHolder getOntologyRepresentation(AID request){
		return null;
	}

	/* (non-Javadoc)
	 * @see contmas.agents.OntRepProvider#getOntologyRepresentation()
	 */
	@Override
	public ContainerHolder getOntologyRepresentation(){
		return ontRep;
	}

	class startAgent extends OneShotBehaviour{

		/**
		 * @param a
		 */
		public startAgent(Agent a){
			super(a);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			ACLMessage request=START_AGENT_REQUEST_KEY;
			StartNewContainerHolder act=(StartNewContainerHolder) ContainerAgent.extractAction(myAgent,request);
			AgentContainer c=myAgent.getContainerController();
			AgentController a=null;
			ContainerHolder ontRep=getOntologyRepresentation();
			try{
				if(ontRep instanceof Ship){
					a=c.acceptNewAgent(act.getName(),new ShipAgent((Ship) ontRep));
				}else if(ontRep instanceof Crane){
					a=c.acceptNewAgent(act.getName(),new CraneAgent((Crane) ontRep));
				}else if(ontRep instanceof Apron){
					a=c.acceptNewAgent(act.getName(),new ApronAgent((Apron) ontRep));
				}else if(ontRep instanceof StraddleCarrier){
					a=c.acceptNewAgent(act.getName(),new StraddleCarrierAgent((StraddleCarrier) ontRep));
				}else if(ontRep instanceof Yard){
					a=c.acceptNewAgent(act.getName(),new YardAgent((Yard) ontRep));
				}
				a.start();
			}catch(StaleProxyException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ACLMessage inform=START_AGENT_RESPONSE_KEY;
			myAgent.send(inform);

			//Restart
			Agent tmp=myAgent;
			myAgent.removeBehaviour(this);
			tmp.addBehaviour(new listenForStartAgentReq(tmp));
		}
	}
}