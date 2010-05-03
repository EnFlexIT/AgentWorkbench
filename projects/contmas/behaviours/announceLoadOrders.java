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
import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.util.leap.List;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.ontology.*;

public class announceLoadOrders extends ContractNetInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID=7080809105355535853L;
	private final LoadList currentLoadList;
	private final Behaviour masterBehaviour;
	private final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;

	public announceLoadOrders(Agent a,LoadList currentLoadList){
		this(a,currentLoadList,null);
	}

	public announceLoadOrders(Agent a,LoadList currentLoadList,Behaviour masterBehaviour){
		super(a,null);
		this.currentLoadList=currentLoadList;
		this.masterBehaviour=masterBehaviour;
	}

	@Override
	protected Vector<ACLMessage> prepareCfps(ACLMessage cfp){
		TransportOrderChain curTOC=(TransportOrderChain) this.currentLoadList.getConsists_of().iterator().next();
		TransportOrderChainState oldState=this.myCAgent.touchTOCState(curTOC,new Announced());
		if(oldState instanceof Administered){
			//OK, go on
		}else if(oldState instanceof Announced){
			this.myCAgent.echoStatus("Auftrag bereits ausgeschrieben, nicht nocheinmal.",curTOC,ContainerAgent.LOGGING_INFORM);
			return null;
		}else{
			this.myCAgent.echoStatus("FAILURE: Auftrag wird nicht ausgeschrieben werden, nicht administriert, wahrscheinlich schon failure.",curTOC,ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new Failed());
			return null;
		}
		this.myCAgent.echoStatus("Schreibe Auftrag aus.",curTOC,ContainerAgent.LOGGING_INFORM);
		cfp=new ACLMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		List contractorList=this.myCAgent.determineContractors();
		if((contractorList == null) || contractorList.isEmpty()){
			if(this.masterBehaviour != null){
				this.myCAgent.echoStatus("MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
				this.masterBehaviour.restart();
			}
			this.myCAgent.echoStatus("FAILURE: Keine Contractors mehr vorhanden. Ausschreibung nicht möglich.",curTOC,ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new Failed());
			return null;
		}
		Iterator<?> allContractors=contractorList.iterator();

		while(allContractors.hasNext()){
			cfp.addReceiver((AID) allContractors.next());
		}
		CallForProposalsOnLoadStage act=new CallForProposalsOnLoadStage();
		act.setRequired_turnover_capacity(this.currentLoadList);
		this.myCAgent.fillMessage(cfp,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		cfp.setReplyByDate(new Date(System.currentTimeMillis() + 5000)); //500000
		messages.add(cfp);

		//		((ContainerAgent)myAgent).echoStatus("Auftrag ausgeschrieben.",curTOC);
		return messages;
	}

	@Override
	protected void handleAllResponses(Vector responses,Vector acceptances){
		TransportOrder bestOffer=null;
		TransportOrderChain bestOfferToc=null;

		ACLMessage bestOfferMessage=null;
		for(Object message: responses){
			ACLMessage propose=(ACLMessage) message;
			if(propose.getPerformative() == ACLMessage.REFUSE){
				this.myCAgent.echoStatus("Ablehnung empfangen von " + propose.getSender().getLocalName() + ": " + propose.getContent(),ContainerAgent.LOGGING_INFORM);
			}
			if((propose.getContent() != null) && (propose.getPerformative() == ACLMessage.PROPOSE)){
				Concept content;
				content=this.myCAgent.extractAction(propose);
				if(content instanceof ProposeLoadOffer){
					ProposeLoadOffer proposal=(ProposeLoadOffer) content;
					TransportOrder offer=this.myCAgent.findMatchingOrder(proposal.getCorresponds_to(),false);
					if((bestOffer == null) || (Long.parseLong(offer.getTakes_until()) < Long.parseLong(bestOffer.getTakes_until()))){ //bisher beste Zeit
						bestOffer=offer;
						bestOfferMessage=propose;
						bestOfferToc=proposal.getCorresponds_to();
					}
				}
			} // End if content !=null
		}
		ACLMessage accept=null;
		TransportOrderChain curTOC=(TransportOrderChain) this.currentLoadList.getConsists_of().iterator().next();

		if(bestOffer == null){ //Abnehmer momentan alle beschäftigt
			this.myCAgent.echoStatus("FAILURE: Nur Ablehnungen empfangen, Abbruch.",ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new Failed());
			if(this.masterBehaviour != null){
				this.myCAgent.echoStatus("REFUSE: MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
				this.masterBehaviour.restart();
			}
			this.reset();
			return;

			/*
			 * Alt: warten, benötigt aber weiteren zwischenspeicher oder so
			 * if(((ContainerAgent)myAgent).isInFailedQueue(curTOC)){
			 * if(masterBehaviour!=null){((ContainerAgent)myAgent).echoStatus(
			 * "REFUSE: MasterBehaviour wird neugestartet.");
			 * masterBehaviour.restart(); } return; } else {
			 * ((ContainerAgent)myAgent
			 * ).echoStatus("Nur Ablehnungen empfangen, versuche es nochmal");
			 * ((ContainerAgent)myAgent).doWait(1000); //kurz warten, vielleicht
			 * beruhigt sich ja die Lage
			 * ((ContainerAgent)myAgent).changeTOCState(curTOC, new Failed());
			 * this.reset(); return; }
			 */
		}
		if(bestOffer != null){
			accept=bestOfferMessage.createReply();
			AcceptLoadOffer act=new AcceptLoadOffer();
			act.setCorresponds_to(bestOfferToc);
			this.myCAgent.fillMessage(accept,act);
			accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			acceptances.add(accept);
		}
		for(Object message: responses){
			ACLMessage propose=(ACLMessage) message;
			if((propose != bestOfferMessage) && (propose.getPerformative() == ACLMessage.PROPOSE)){
				accept=propose.createReply();
				accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.add(accept);
			}
		}
	}

	@Override
	protected void handleAllResultNotifications(Vector resultNotifications){
		for(Object message: resultNotifications){
			ACLMessage notification=(ACLMessage) message;
			AgentAction content=this.myCAgent.extractAction(notification);
			if(content instanceof AnnounceLoadStatus){
				AnnounceLoadStatus loadStatus=(AnnounceLoadStatus) content;
				TransportOrderChain load_offer=loadStatus.getCorresponds_to();
				if((notification.getPerformative() == ACLMessage.INFORM) && loadStatus.getLoad_status().equals("FINISHED")){
					//					((ContainerAgent)myAgent).echoStatus("AnnounceLoadStatus FINISHED empfangen, bearbeiten");
					if(this.myCAgent.dropContainer(load_offer)){
						if(this.masterBehaviour != null){
							this.myCAgent.echoStatus("INFORM: MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
							this.masterBehaviour.restart();
						}
					}
				}else if(notification.getPerformative() == ACLMessage.FAILURE){ // && loadStatus.getLoad_status().substring(0, 4).equals("ERROR")) {
					this.myCAgent.removeFromContractors(notification.getSender());
					this.myCAgent.touchTOCState(load_offer,new Failed());
					this.myCAgent.echoStatus("Containerabgabe fehlgeschlagen. " + loadStatus.getLoad_status(),load_offer,ContainerAgent.LOGGING_NOTICE);
					if(this.masterBehaviour != null){
						this.myCAgent.echoStatus("FAILURE: MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
						this.masterBehaviour.restart();
					}
				}
			}
		}
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei announce (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}

}
