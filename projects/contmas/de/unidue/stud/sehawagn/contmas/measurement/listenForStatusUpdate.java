/**
 * @author Hanno - Felix Wagner, 08.06.2010
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
package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import contmas.agents.ContainerAgent;
import contmas.ontology.AnnounceLoadStatus;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class listenForStatusUpdate extends CyclicBehaviour{

	Measurer myAgent;
	private MessageTemplate updateTemplate;
	
	private static MessageTemplate createMessageTemplate(AID topic){
		MessageTemplate mt=MessageTemplate.MatchTopic(topic);
		return mt;
	}
	
	/**
	 * @param dashboardAgent
	 */
	public listenForStatusUpdate(Measurer measurer){
		myAgent=measurer;
		updateTemplate=createMessageTemplate(myAgent.getMeasureTopic());

	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage updMsg=((Agent)myAgent).receive(updateTemplate);
		if(updMsg != null){
			AnnounceLoadStatus statusUpdate=(AnnounceLoadStatus) ContainerAgent.extractAction((Agent) myAgent,updMsg);
			String status=statusUpdate.getLoad_status();
			Long eventTime=Long.valueOf(statusUpdate.getHappend_at());
			myAgent.processStatusUpdate(updMsg.getSender(),eventTime,status);
		}else{
			this.block();
		}
	}
}