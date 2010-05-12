/**
 * @author Hanno - Felix Wagner, 10.05.2010
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
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.*;

class handleAcceptProposalOLD extends SimpleBehaviour{

		/**
		 * 
		 */
		private final receiveLoadOrders receiveLoadOrders;
		private static final long serialVersionUID= -1740553491760609807L;
		private final ContainerHolderAgent myCAgent;
		private Boolean isDone=false;
		private Boolean firstRun=true;

		handleAcceptProposalOLD(receiveLoadOrders receiveLoadOrders, ContainerHolderAgent myCAgent){
			this.receiveLoadOrders=receiveLoadOrders;
			this.myCAgent=myCAgent;
		}

		@Override
		public void action(){
			DataStore ds=this.getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(this.receiveLoadOrders.ACCEPT_PROPOSAL_KEY);
			Concept content=this.myCAgent.extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getCorresponds_to();
			ACLMessage rply=accept.createReply();

			this.myCAgent.echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC,ContainerAgent.LOGGING_INFORM);

			TransportOrderChainState curState=this.myCAgent.touchTOCState(acceptedTOC);
			if(curState instanceof ProposedFor || curState instanceof InExecution){//Angebot wurde angenommen->Alles weitere in die Wege leiten
				if(this.myCAgent.hasBayMapRoom()){
					TransportOrder offer=this.myCAgent.findMatchingOrder(acceptedTOC); //get transport order TO me

//					((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");
					if(myAgent instanceof StraddleCarrierAgent){
						if(firstRun){ //First run
							firstRun=false;

							Domain startsAt=myCAgent.inflateDomain(offer.getStarts_at().getAbstract_designation());
							StraddleCarrierAgent strad=((StraddleCarrierAgent) myAgent);
							strad.addAsapMovementTo(startsAt.getIs_in_position());
							InExecution newState=new InExecution();
							newState.setLoad_offer(offer);
							this.myCAgent.touchTOCState(acceptedTOC,newState);
							curState=newState;
						}
						if(curState instanceof InExecution){
//							myCAgent.echoStatus("In state InExecution",acceptedTOC);

							TransportOrder currentOrder=((InExecution) curState).getLoad_offer();
							StraddleCarrierAgent strad=((StraddleCarrierAgent) myAgent);
							Domain startsAt=myCAgent.inflateDomain(currentOrder.getStarts_at().getAbstract_designation());

							if(strad.isAt(startsAt.getIs_in_position())){
								myCAgent.echoStatus("I am in target pickup position",acceptedTOC);
							}else{
//								myCAgent.echoStatus("i have not yet reached target pickup position: block",acceptedTOC);
								block(1000);
								this.isDone=false;
								return;
							}
						}
					}
					AnnounceLoadStatus loadStatus;

					loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"READY");
					myCAgent.addBehaviour(new listenForLoadStatusAnnouncement(myCAgent,rply));
					/*
					if(myAgent instanceof YardAgent || myAgent instanceof ApronAgent){
						loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"READY");
						myCAgent.addBehaviour(new listenForLoadStatusAnnouncement(myCAgent,rply));
					}else{
						loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
						if(this.myCAgent.aquireContainer(acceptedTOC)){
					//							((ContainerAgent)myAgent).echoStatus("Order executed, Container aquired.",acceptedTOC);
						}else{ //not able to aquire
							this.myCAgent.echoStatus("ERROR: Auftrag kann nicht ausgeführt werden.",acceptedTOC,ContainerAgent.LOGGING_ERROR);
						}
					}
					*/
					this.isDone=true;
					firstRun=true;
					rply.setPerformative(ACLMessage.INFORM);
					this.myCAgent.fillMessage(rply,loadStatus);
					ds.put(this.receiveLoadOrders.REPLY_KEY,rply);
					this.myAgent.doWake();
					return;

				}else{ //no room in bay map
					if(this.myCAgent instanceof TransportOrderOfferer){
						TransportOrderChain someTOC=this.myCAgent.getSomeTOCOfState(new Administered());
						if(someTOC == null){
							someTOC=this.myCAgent.getSomeTOCOfState(new Announced());
							if(someTOC == null){
								someTOC=this.myCAgent.getSomeTOCOfState(new InExecution());
							}
							if(someTOC == null){
								someTOC=this.myCAgent.getSomeTOCOfState(new Assigned());
							}
							if(someTOC != null){
								myCAgent.registerForWakeUpCall(this);
								this.block();
								this.isDone=false;
								return;
							}
						}

						if(someTOC != null){
							this.myCAgent.echoStatus("BayMap voll, versuche Räumung für",acceptedTOC,ContainerAgent.LOGGING_INFORM);
							this.myCAgent.touchTOCState(acceptedTOC,new PendingForSubCFP());
							this.myCAgent.releaseContainer(someTOC,this);
							this.block();
							this.isDone=false;
							return;
						}else{ //keine administrierten TOCs da
							this.myCAgent.echoStatus("FAILURE: BayMap full, no administered TOCs available, clearing not possible.",ContainerAgent.LOGGING_NOTICE);
						}
					}else{//Agent kann keine Aufträge abgeben=>Senke
						this.myCAgent.echoStatus("FAILURE: Bin Senke, kann keine Aufträge weitergeben.",ContainerAgent.LOGGING_NOTICE);
					}
				}
			}else if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(this.myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					this.myCAgent.echoStatus("Unterauftrag läuft noch:",acceptedTOC,ContainerAgent.LOGGING_INFORM);
					this.block();
					this.isDone=false;
					return;
				}else{ // ausschreibungsqueque ist leer
					this.myCAgent.touchTOCState(acceptedTOC,new ProposedFor());
					this.myCAgent.echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.",ContainerAgent.LOGGING_INFORM);
					this.isDone=false;
					return;
				}
			}else if(curState instanceof FailedOut){
				this.myCAgent.echoStatus("FAILURE: Ausschreibung des Unterauftrags ist bereits fehlgeschlagen.",ContainerAgent.LOGGING_NOTICE);
			}
			//Ausschreibung ist fehlgeschlagen, keine administrierten TOCs da, Irgendwas schiefgelaufen bei der Ausschreibung des Unterauftrags
			this.myCAgent.touchTOCState(acceptedTOC,new FailedIn());

			AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"BayMap voll und kann nicht geräumt werden.");
			rply.setPerformative(ACLMessage.FAILURE);
			this.myCAgent.fillMessage(rply,loadStatus);

			ds.put(this.receiveLoadOrders.REPLY_KEY,rply);
			this.isDone=true;
			return;
		}

		@Override
		public boolean done(){
			return this.isDone;
		}
	}