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
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.states.ReplySender;
import jade.util.leap.List;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.interfaces.MoveableAgent;
import contmas.main.EnvironmentHelper;
import contmas.ontology.*;

public class announceLoadOrders extends ContractNetInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID=7080809105355535853L;
	private final TransportOrderChain curTOC;
	private TransportOrder curTO;
	Domain endDomain;

	private final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;

	public announceLoadOrders(Agent a,TransportOrderChain currentTOC){
		super(a,null);
		this.curTOC=currentTOC;
	}

	@Override
	protected Vector<ACLMessage> prepareCfps(ACLMessage cfp){
		TransportOrderChainState oldState=this.myCAgent.setTOCState(curTOC,new Announced());
//		this.myCAgent.echoStatus("announceLoadOrders - prepareCfps state set to Announced",curTOC);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();

		if(oldState instanceof Announced){
			this.myCAgent.echoStatus("Auftrag bereits ausgeschrieben, nicht nocheinmal.",curTOC,ContainerAgent.LOGGING_INFORM);
			messages=null;
		}else if(oldState instanceof Administered){
			this.myCAgent.echoStatus("Schreibe Auftrag aus.",curTOC,ContainerAgent.LOGGING_INFORM);
			cfp=new ACLMessage(ACLMessage.CFP);
			cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
			List contractorList=this.myCAgent.determineContractors();
			if((contractorList == null) || contractorList.isEmpty()){
				this.myCAgent.echoStatus("FAILURE: Keine Contractors mehr vorhanden. Ausschreibung nicht möglich.",curTOC,ContainerAgent.LOGGING_NOTICE);
				this.myCAgent.setTOCState(curTOC,new FailedOut());
				messages=null;
			}
			Iterator<?> allContractors=contractorList.iterator();

			while(allContractors.hasNext()){
				cfp.addReceiver((AID) allContractors.next());
			}
			CallForProposalsOnLoadStage act=new CallForProposalsOnLoadStage();
			act.setCorresponds_to(this.curTOC);
			this.myCAgent.fillMessage(cfp,act);
			cfp.setReplyByDate(new Date(System.currentTimeMillis() + 50000)); //500000
			messages.add(cfp);

			//		((ContainerAgent)myAgent).echoStatus("Auftrag ausgeschrieben.",curTOC);
		}else{
			this.myCAgent.echoStatus("FAILURE: TOC is not going to be announced, currentState: " + oldState.getClass().getSimpleName(),curTOC,ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.setTOCState(curTOC,new FailedOut());
			messages=null;
		}
		myCAgent.wakeSleepingBehaviours(curTOC);
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
					TransportOrder offer=proposal.getLoad_offer();
//					TransportOrder offer=this.myCAgent.findMatchingOrder(proposal.getCorresponds_to(),false);
					if((bestOffer == null) || (Long.parseLong(offer.getTakes_until()) < Long.parseLong(bestOffer.getTakes_until()))){ //bisher beste Zeit
						bestOffer=offer;
						bestOfferMessage=propose;
						bestOfferToc=proposal.getCorresponds_to();
					}
				}
			} // End if content !=null
		}
		ACLMessage accept=null;

		if(bestOffer == null){ //Abnehmer momentan alle beschäftigt
			this.myCAgent.echoStatus("FAILURE: No proper proposals received, canceling announcement process.",ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.setTOCState(curTOC,new FailedOut());
			myCAgent.wakeSleepingBehaviours(curTOC);

			this.reset();
			return;
		}
		if(bestOffer != null){
			curTO=bestOffer;
			endDomain=myCAgent.inflateDomain(curTO.getEnds_at().getAbstract_designation());
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

//		myCAgent.echoStatus("handleAllResultNotifications");
		for(Iterator iterator=resultNotifications.iterator();iterator.hasNext();){
			ACLMessage resNot=(ACLMessage) iterator.next();
			AnnounceLoadStatus act=(AnnounceLoadStatus) myCAgent.extractAction(resNot);
			if(act.getLoad_status().equals("PENDING")){
				PlannedOut newState=new PlannedOut();
				newState.setLoad_offer(curTO);
//				newState.setAt_address(curTO.getEnds_at().getAt_address());
//				myCAgent.echoStatus("BIC="+curTOC.getTransports().getBic_code(),curTOC);

				
			
//				myCAgent.echoStatus("negotiation, received loadStatus PENDING, changing TOCState to PlannedOut, startBA="+Const.blockAddressToString(curTO.getStarts_at().getAt_address()),curTOC);
//				myCAgent.echoStatus("negotiation, received loadStatus PENDING, changing TOCState to PlannedOut, endBA="+Const.blockAddressToString(curTO.getEnds_at().getAt_address()),curTOC);

				
				myCAgent.setTOCState(curTOC,newState);
				
//				myCAgent.echoStatus("currently stored at myself,BA="+Const.blockAddressToString(myCAgent.touchTOCState(curTOC).getAt_address()));
//				myCAgent.echoStatus("bestOffer has endBA="+Const.blockAddressToString(myCAgent.touchTOCState(curTOC).getLoad_offer().getEnds_at().getAt_address()));

				
				

				
				myCAgent.wakeSleepingBehaviours(curTOC);
//				myCAgent.addBehaviour(new requestExecuteAppointment(myCAgent,curTOC,curTO));
			}
		}
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei announce (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}
}