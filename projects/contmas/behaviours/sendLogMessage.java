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

package contmas.behaviours;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import contmas.agents.ContainerAgent;

public class sendLogMessage extends OneShotBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID=336783126262644079L;
	ContainerAgent myCAgent=null;
	MessageTemplate loggingTemplate=null;
	String logMessageText="";
	protected AID loggingTopic=null;

	/**
	 * 
	 */
	public sendLogMessage(ContainerAgent myCAgent,String logMessageText){
		super(myCAgent);
		this.myCAgent=myCAgent;

		this.logMessageText=logMessageText;

		try{
			TopicManagementHelper tmh;
			tmh=(TopicManagementHelper) this.myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.loggingTopic=tmh.createTopic("container-harbour-logging");
		}catch(ServiceException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.loggingTemplate=MessageTemplate.MatchTopic(this.loggingTopic);
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage logMsg=new ACLMessage(ACLMessage.INFORM);

		logMsg.addReceiver(this.loggingTopic);
		logMsg.setContent(this.logMessageText);
		this.myAgent.send(logMsg);

	}
}