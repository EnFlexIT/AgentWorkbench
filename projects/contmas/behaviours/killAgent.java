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

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.ontology.StartNewContainerHolder;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class killAgent extends AchieveREInitiator{

	/**
	 * 
	 */
	private static final long serialVersionUID= -1067315000037572948L;
	private AID toBeKilled=null;
	ContainerAgent myAgent;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public killAgent(Agent a,AID toBeKilled){
		super(a,killAgent.getRequestMessage(a));
		myAgent=(ContainerAgent) a;
		this.toBeKilled=toBeKilled;
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		Ontology ont=JADEManagementOntology.getInstance();
		Codec codec=new SLCodec();
		myAgent.getContentManager().registerOntology(ont);
		myAgent.getContentManager().registerLanguage(codec);
		request.setOntology(ont.getName());
		request.setLanguage(codec.getName());
		
		Action act=new Action();
		KillAgent agAct=new KillAgent();
		agAct.setAgent(toBeKilled);
		act.setActor(myAgent.getAMS());
		act.setAction(agAct);
		request.addReceiver(myAgent.getAMS());
		try{
			myAgent.getContentManager().fillContent(request,act);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		try{
			Done done=(Done) myAgent.getContentManager().extractContent(msg);
			String deadAgentName=((KillAgent)((Action)done.getAction()).getAction()).getAgent().getLocalName();
			myAgent.echoStatus("Agent "+deadAgentName+" has been killed.");
		}catch(UngroundedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handleRefuse(ACLMessage msg){
	}

	@Override
	protected void handleNotUnderstood(ACLMessage msg){
	}
}