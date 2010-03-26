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

package contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Vector;

import contmas.behaviours.announceLoadOrders;
import contmas.behaviours.fetchRandomBayMap;
import contmas.behaviours.getPopulatedBayMap;
import contmas.ontology.*;

public class ShipAgent extends StaticContainerAgent implements TransportOrderOfferer{
	public class enrollAtHarbor extends AchieveREInitiator{
		/**
		 * 
		 */
		private static final long serialVersionUID= -1583891049645164006L;
		private ShipAgent mySAgent=null;
		private ContainerAgent myCAgent=null;

		public enrollAtHarbor(Agent a,ACLMessage initiation){
			super(a,initiation);
			this.mySAgent=((ShipAgent) this.myAgent);
			this.myCAgent=((ContainerAgent) this.myAgent);

		}

		@Override
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
			request.addReceiver(ShipAgent.this.harborManager);
			EnrollAtHarbor act=new EnrollAtHarbor();
			act.setShip_length(((Ship) this.mySAgent.ontologyRepresentation).getLength());
			((ContainerAgent) this.myAgent).fillMessage(request,act);

			Vector<ACLMessage> messages=new Vector<ACLMessage>();
			messages.add(request);
			return messages;
		}
	}

	public class fetchCraneList extends AchieveREInitiator{
		/**
		 * 
		 */
		private static final long serialVersionUID= -7522523905068388327L;

		public fetchCraneList(Agent a,ACLMessage msg){
			super(a,msg);
		}

		@Override
		protected void handleInform(ACLMessage msg){
			Concept content;
			content=((ContainerAgent) this.myAgent).extractAction(msg);

			if(content instanceof ProvideCraneList){
				List craneList=((ProvideCraneList) content).getAvailable_cranes();
				ShipAgent.this.ontologyRepresentation.setContractors(craneList);
				this.myAgent.addBehaviour(new unload(this.myAgent));
			}
		}

		@Override
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
			request.addReceiver(ShipAgent.this.harborManager);
			AgentAction act=new GetCraneList();
			((ContainerAgent) this.myAgent).fillMessage(request,act);

			Vector<ACLMessage> messages=new Vector<ACLMessage>();
			messages.add(request);
			return messages;
		}
	}

	//	public class unload extends TickerBehaviour{
	public class unload extends OneShotBehaviour{

		/**
		 * 
		 */
		private ShipAgent mySAgent=null;
		private static final long serialVersionUID=3933460156486819068L;

		public unload(Agent a){
			//			super(a, 1000);
			super(a);
			this.mySAgent=((ShipAgent) this.myAgent);
		}

		//		public void onTick() {
		@Override
		public void action(){

			//			echoStatus("Tick: Entladen geht los");
			if(this.mySAgent.ontologyRepresentation.getContractors() != null){
				BayMap LoadBay=this.mySAgent.ontologyRepresentation.getContains();

				Iterator allConts=getOntologyRepresentation().getContains().getAllIs_filled_with();
				while(allConts.hasNext()){
					BlockAddress curBaymap=(BlockAddress) allConts.next();
					TransportOrderChain curTOC=new TransportOrderChain();
					curTOC.setTransports(curBaymap.getLocates());
					touchTOCState(curTOC,new Administered(),true);
				}

				new LoadList();
				Designator myself=ShipAgent.this.getMyselfDesignator();

				for(int x=0;x < LoadBay.getX_dimension();x++){ //baymap zeilen-
					for(int y=0;y < LoadBay.getY_dimension();y++){ //und spaltenweise durchlaufen
						BlockAddress upmostContainer=null;
						Iterator allContainers=LoadBay.getAllIs_filled_with();
						while(allContainers.hasNext()){ //alle geladenen Container überprüfen 
							BlockAddress curContainer=(BlockAddress) allContainers.next();
							if((curContainer.getX_dimension() == x) && (curContainer.getY_dimension() == y)){ //betrachteter Container steht im stapel auf momentaner koordinate
								if((upmostContainer == null) || (upmostContainer.getZ_dimension() < curContainer.getZ_dimension())){
									upmostContainer=curContainer;
								}
							}
						} //end while
						if(upmostContainer != null){ //an dieser Koordinate steht ein Container obenauf
							TransportOrder TO=new TransportOrder();
							TO.setStarts_at(myself);
							Designator target=new Designator();
							target.setType("abstract");
							target.setAbstract_designation(this.mySAgent.targetAbstractDomain);
							TO.setEnds_at(target);
							TransportOrderChain TOChain=new TransportOrderChain();
							TOChain.addIs_linked_by(TO);

							//TO.setLinks(TOChain); 

							TOChain.setTransports(upmostContainer.getLocates());

							//Variante: Alle Container einer Lage auf einmal
							//							completeLoadList.addConsists_of(TOChain);

							//Variante: Jeder Container einzeln
							LoadList currentLoadList=new LoadList();
							currentLoadList.addConsists_of(TOChain);

							//System.out.println("addBehaviour announceLoadOrders with "+currentLoadList.getConsists_of().get(0));
							this.myAgent.addBehaviour(new announceLoadOrders(this.myAgent,currentLoadList));
							currentLoadList=null;
							upmostContainer=null;
						}
					}
				}
				//Variante: alle Container einer Lage zusammen
				//				myAgent.addBehaviour(new announceLoadOrders(myAgent,completeLoadList));

				this.block();
			}else{
				System.err.println("Noch keine Kranliste vorhanden");
				this.block();
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID=6800105012920938089L;

	public ShipAgent(){
		this(new Ship());
	}

	public ShipAgent(Ship ontologyRepresentation){
		super("long-term-transporting",ontologyRepresentation);
		this.targetAbstractDomain=new Rail();
	}

	@Override
	public List determineContractors(){
		return this.ontologyRepresentation.getContractors();
	}

	public void offerTransportOrder(){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new fetchCraneList(this,msg));
	}

	class scheduleUnloadStart extends SimpleBehaviour{
		Boolean done=false;
		/**
		 * @param shipAgent
		 */
		public scheduleUnloadStart(Agent a){
			super(a);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			if( !((ContainerHolderAgent) myAgent).getOntologyRepresentation().getContains().getIs_filled_with().isEmpty()){
				offerTransportOrder();
				done=true;
			}
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#done()
		 */
		@Override
		public boolean done(){
			return done;
		}
	}

	@Override
	protected void setup(){
		super.setup();

		//TODO hardcoded
		this.ontologyRepresentation.setLives_in(new Sea());

		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new enrollAtHarbor(this,msg));
/*
		if((this.ontologyRepresentation.getContains().getX_dimension() == -1) || (this.ontologyRepresentation.getContains().getY_dimension() == -1) || (this.ontologyRepresentation.getContains().getZ_dimension() == -1)){ //default-größe
			this.addBehaviour(new fetchRandomBayMap(this));
		}else{ //direkt füllen
			this.addBehaviour(new getPopulatedBayMap(this));
		}
		*/
		this.addBehaviour(new scheduleUnloadStart(this));
	}
}