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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import contmas.agents.ContainerAgent;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class subscribeToDF extends SubscriptionInitiator{

	/**
	 * 
	 */
	private static final long serialVersionUID=5004964558751306936L;
	private Method callbackMethod=null;

	private static ACLMessage getSubscriptionMessage(Agent a,String serviceType){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		 ACLMessage subscriptionMessage=DFService.createSubscriptionMessage(a,a.getDefaultDF(),dfd,null);
		 return subscriptionMessage;
	}
	
	public subscribeToDF(Agent a,Method methode, String serviceType){
		super(a,getSubscriptionMessage(a,serviceType));
		this.callbackMethod=methode;
	}

	@Override
	protected void handleInform(ACLMessage inform){
		try{
			DFAgentDescription[] dfds=DFService.decodeNotification(inform.getContent());
			try{
				this.callbackMethod.invoke(this.myAgent,ContainerAgent.toAIDList(ContainerAgent.agentDescToAIDArray(dfds)));
			}catch(IllegalArgumentException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IllegalAccessException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(InvocationTargetException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
	}
}
