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
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;

import java.lang.reflect.Method;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class prepareSubscribeToDF extends OneShotBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID=5004964558751306936L;
	List targetStorage=null;
	DFAgentDescription targetDFAgentDescription=null;
	String targetAgentServiceType="";
	private Method callbackMethod=null;

	public prepareSubscribeToDF(Agent a,List targetStorage,DFAgentDescription targetDFAgentDescription){
		this.targetDFAgentDescription=targetDFAgentDescription;
		this.targetStorage=targetStorage;
	}

	/**
	 * @param a
	 * @param msg
	 */
	public prepareSubscribeToDF(Agent a,List targetStorage,String targetAgentServiceType){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(targetAgentServiceType);
		dfd.addServices(sd);
		this.targetAgentServiceType=targetAgentServiceType;
		this.targetDFAgentDescription=dfd;
		this.targetStorage=targetStorage;
	}

	public prepareSubscribeToDF(Agent a,Method callbackMethod,DFAgentDescription targetDFAgentDescription){
		this.targetDFAgentDescription=targetDFAgentDescription;
		this.callbackMethod=callbackMethod;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage subscriptionMessage=DFService.createSubscriptionMessage(this.myAgent,this.myAgent.getDefaultDF(),this.targetDFAgentDescription,null);
//		this.myAgent.addBehaviour(new subscribeToDF(this.myAgent,subscriptionMessage,targetStorage));
		this.myAgent.addBehaviour(new subscribeToDF(this.myAgent,subscriptionMessage,this.callbackMethod));
	}
}
