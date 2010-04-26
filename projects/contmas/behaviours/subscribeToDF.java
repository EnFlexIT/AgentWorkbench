/**
 * @author Hanno - Felix Wagner, 17.03.2010
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

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;
import contmas.agents.ContainerAgent;
import contmas.interfaces.DFSubscriber;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class subscribeToDF extends SubscriptionInitiator{

	/**
	 * 
	 */
	private static final long serialVersionUID=5004964558751306936L;
	private DFSubscriber parent;

	private static ACLMessage getSubscriptionMessage(Agent a,String serviceType){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		ACLMessage subscriptionMessage=DFService.createSubscriptionMessage(a,a.getDefaultDF(),dfd,null);
		return subscriptionMessage;
	}

	public subscribeToDF(Agent a,String serviceType){
		super(a,subscribeToDF.getSubscriptionMessage(a,serviceType));
	}

	private void setParent(){
		if(super.parent != null){ //as subBehaviour
			this.parent=((DFSubscriber) super.parent);
		}else{ //standalone, i.e. directly run by an agent
			this.parent=((DFSubscriber) this.myAgent);
		}
	}

	@Override
	protected void handleInform(ACLMessage inform){
		this.setParent();
		try{
			DFAgentDescription[] dfds=DFService.decodeNotification(inform.getContent());
			DFAgentDescription[] subscribe=new DFAgentDescription[dfds.length];
			DFAgentDescription[] unsubscribe=new DFAgentDescription[dfds.length];
			try{
				for(int i=0;i < dfds.length;i++){
					DFAgentDescription agentDescription=dfds[i];
					Iterator services=agentDescription.getAllServices();
					if(services.hasNext()){
						subscribe[i]=agentDescription;
					}else{
						unsubscribe[i]=agentDescription;
					}
				}

				this.parent.processSubscriptionUpdate(ContainerAgent.toAIDList(ContainerAgent.agentDescToAIDArray(subscribe)),false);

				this.parent.processSubscriptionUpdate(ContainerAgent.toAIDList(ContainerAgent.agentDescToAIDArray(unsubscribe)),true);

			}catch(IllegalArgumentException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
	}
}
