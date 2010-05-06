/**
 * @author Hanno - Felix Wagner, 06.05.2010
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

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.interfaces.Logger;

public class listenForLogMessage extends CyclicBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID=3580181910527477281L;
	MessageTemplate loggingTemplate;
	Logger myAgent;
	private TopicManagementHelper tmh;
	private AID loggingTopic=null;
	
	private static MessageTemplate createMessageTemplate(AID topic){
		MessageTemplate mt=MessageTemplate.MatchTopic(topic);
		return mt;
	}
	
	/* (non-Javadoc)
	 * @see contmas.interfaces.Logger#getLoggingTopic()
	 */

	public AID getLoggingTopic(){
		if(loggingTopic==null){
			try{
				this.tmh=(TopicManagementHelper) ((Agent) myAgent).getHelper(TopicManagementHelper.SERVICE_NAME);
				this.loggingTopic=this.tmh.createTopic("container-harbour-logging");
				this.tmh.register(this.loggingTopic);
			}catch(ServiceException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return loggingTopic;
	}
	
	/**
	 * @param controlGUIAgent
	 */
	public listenForLogMessage(Logger a){
		super((Agent) a);
		myAgent=a;
		loggingTemplate=createMessageTemplate(getLoggingTopic());
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage logMsg=((Agent)myAgent).receive(this.loggingTemplate);
		if(logMsg != null){
			String content=logMsg.getContent();
			myAgent.processLogMsg(content);
		}else{
			this.block();
		}
	}
	
	@Override
	public int onEnd(){
		System.out.println("Ja, geht");
		try{
			this.tmh.deregister(this.loggingTopic); //does it work?
		}catch(ServiceException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}