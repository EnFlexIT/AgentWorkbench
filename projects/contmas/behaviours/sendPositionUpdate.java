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

import jade.content.AgentAction;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import contmas.agents.ContainerAgent;
import contmas.ontology.Phy_Movement;
import contmas.ontology.Phy_Position;

public class sendPositionUpdate extends OneShotBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID=336783126262644079L;
	ContainerAgent myCAgent=null;
	MessageTemplate posUpdTemplate=null;
	Phy_Position newPosition=null;
	protected AID loggingTopic=null;

	/**
	 * 
	 */
	public sendPositionUpdate(ContainerAgent myCAgent,Phy_Position newPosition){
		super(myCAgent);
		this.myCAgent=myCAgent;

		this.newPosition=newPosition;

		try{
			TopicManagementHelper tmh;
			tmh=(TopicManagementHelper) this.myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.loggingTopic=tmh.createTopic("container-harbour-position-update");
		}catch(ServiceException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.posUpdTemplate=MessageTemplate.MatchTopic(this.loggingTopic);
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		ACLMessage updMsg=new ACLMessage(ACLMessage.INFORM);

		updMsg.addReceiver(this.loggingTopic);
		Phy_Movement agact=new Phy_Movement();
		List steps=new ArrayList();
		steps.add(newPosition);
		agact.setPhy_Steps(steps);
		myCAgent.fillMessage(updMsg,agact);

		this.myAgent.send(updMsg);

	}
}