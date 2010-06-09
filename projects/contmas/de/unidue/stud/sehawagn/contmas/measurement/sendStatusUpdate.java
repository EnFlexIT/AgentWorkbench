/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import contmas.agents.ContainerAgent;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.TransportOrderChain;

public class sendStatusUpdate extends OneShotBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID=336783126262644079L;
	StatusUpdater myCAgent=null;
	MessageTemplate loggingTemplate=null;
	String updText="";
	protected AID updateTopic=null;
	private TransportOrderChain subject;
	private Long happendAt;

	/**
	 * 
	 */
	public sendStatusUpdate(StatusUpdater myCAgent,String logMessageText, TransportOrderChain subject, Long happendAt){
		super((Agent) myCAgent);
		this.myCAgent=myCAgent;

		this.updText=logMessageText;
		
		this.updateTopic=myCAgent.getStatusUpdateTopic();

		this.loggingTemplate=MessageTemplate.MatchTopic(this.updateTopic);
		
		this.subject=subject;
		this.happendAt=happendAt;
		ContainerAgent.enableForCommunication(myAgent);
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage updMsg=new ACLMessage(ACLMessage.INFORM);

		updMsg.addReceiver(this.updateTopic);
		AnnounceLoadStatus announcement=new AnnounceLoadStatus();
		announcement.setCorresponds_to(subject);
		announcement.setHappend_at(happendAt+"");
		announcement.setLoad_status(updText);
		ContainerAgent.fillMessage(updMsg, announcement, myAgent);
//		updMsg.setContent(this.updText);
		this.myAgent.send(updMsg);
//		((ContainerAgent)myCAgent).echoStatus("sent status update msg '"+updText+"' to "+updateTopic.getName());

	}
}