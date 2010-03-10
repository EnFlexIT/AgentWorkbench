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
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Vector;

import behaviours.announceLoadOrders;

import contmas.ontology.*;

public class ShipAgent extends StaticContainerAgent implements TransportOrderOfferer{
	public class enrollAtHarbor extends AchieveREInitiator{
		/**
		 * 
		 */
		private static final long serialVersionUID= -1583891049645164006L;

		public enrollAtHarbor(Agent a,ACLMessage initiation){
			super(a,initiation);
		}

		@Override
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
			request.addReceiver(ShipAgent.this.HarborManager);
			EnrollAtHarbor act=new EnrollAtHarbor();
			act.setShip_length(((Ship) ((ShipAgent) this.myAgent).ontologyRepresentation).getLength());
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
			request.addReceiver(ShipAgent.this.HarborManager);
			AgentAction act=new GetCraneList();
			((ContainerAgent) this.myAgent).fillMessage(request,act);

			Vector<ACLMessage> messages=new Vector<ACLMessage>();
			messages.add(request);
			return messages;
		}
	}

	public class fetchRandomBayMap extends AchieveREInitiator{
		/**
		 * 
		 */
		private static final long serialVersionUID= -1832052412333457494L;

		public fetchRandomBayMap(Agent a,ACLMessage msg){
			super(a,msg);
		}

		@Override
		protected void handleInform(ACLMessage msg){
			Concept content;
			content=((ContainerAgent) this.myAgent).extractAction(msg);
			if(content instanceof ProvideBayMap){
				((ContainerAgent) this.myAgent).ontologyRepresentation.setContains(((ProvideBayMap) content).getProvides());
				//echoStatus("BayMap recieved! X_dimension:"+getLoadBay().getX_dimension()+", Y_dimension:"+getLoadBay().getY_dimension()+", Z_dimension:"+getLoadBay().getZ_dimension());
				msg=new ACLMessage(ACLMessage.REQUEST);
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
				ShipAgent.this.addBehaviour(new getPopulatedBayMap(this.myAgent,msg));
			}else{
				ShipAgent.this.echoStatus("Error");
			}
		}

		@Override
		protected Vector prepareRequests(ACLMessage request){
			request.addReceiver(ShipAgent.this.RandomGenerator);
			RequestRandomBayMap act=new RequestRandomBayMap();
			//TODO hardcoded
			act.setX_dimension(4);
			act.setY_dimension(4);
			act.setZ_dimension(2);
			((ContainerAgent) this.myAgent).fillMessage(request,act);
			Vector<ACLMessage> messages=new Vector<ACLMessage>();
			messages.add(request);
			return messages;
		}
	}

	public class getPopulatedBayMap extends AchieveREInitiator{
		/**
		 * 
		 */
		private static final long serialVersionUID= -6587230887404034233L;

		public getPopulatedBayMap(Agent a,ACLMessage msg){
			super(a,msg);
		}

		@Override
		protected void handleInform(ACLMessage msg){
			Concept content;
			content=((ContainerAgent) this.myAgent).extractAction(msg);

			if(content instanceof ProvidePopulatedBayMap){
				ShipAgent.this.ontologyRepresentation.setContains(((ProvidePopulatedBayMap) content).getProvides());
				Iterator allConts=ShipAgent.this.ontologyRepresentation.getContains().getAllIs_filled_with();
				while(allConts.hasNext()){
					BlockAddress curBaymap=(BlockAddress) allConts.next();
					TransportOrderChain curTOC=new TransportOrderChain();
					curTOC.setTransports(curBaymap.getLocates());
					ShipAgent.this.changeTOCState(curTOC,new Administered(),true);
				}
				//echoStatus("populatedBayMap recieved!"); 
				ShipAgent.this.offerTransportOrder();
			}else{
				ShipAgent.this.echoStatus("Error");
			}
		}

		@Override
		protected Vector prepareRequests(ACLMessage request){
			request.addReceiver(ShipAgent.this.RandomGenerator);
			//BayMap aus Agent auslesen
			RequestPopulatedBayMap act=new RequestPopulatedBayMap();
			act.setPopulate_on(ShipAgent.this.ontologyRepresentation.getContains());
			((ContainerAgent) this.myAgent).fillMessage(request,act);
			Vector messages=new Vector();
			messages.add(request);

			return messages;
		}
	}

	//	public class unload extends TickerBehaviour{
	public class unload extends OneShotBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID=3933460156486819068L;

		public unload(Agent a){
			//			super(a, 1000);
			super(a);

		}

		//		public void onTick() {
		@Override
		public void action(){

			//			echoStatus("Tick: Entladen geht los");
			if(((ShipAgent) this.myAgent).ontologyRepresentation.getContractors() != null){
				BayMap LoadBay=((ShipAgent) this.myAgent).ontologyRepresentation.getContains();
				new LoadList();
				Designator myself=new Designator();
				myself.setType("concrete");
				myself.setConcrete_designation(this.myAgent.getAID());
				myself.setAbstract_designation(((ContainerAgent) this.myAgent).ontologyRepresentation.getLives_in());

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
							target.setAbstract_designation(new Street());
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

	private AID HarborManager=null;

	private AID RandomGenerator=null;

	public ShipAgent(){
		this(new Ship());
	}

	public ShipAgent(Ship ontologyRepresentation){
		super("long-term-transporting",ontologyRepresentation);
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

	@Override
	protected void setup(){
		super.setup();

		//TODO hardcoded
		this.ontologyRepresentation.setLives_in(new Sea());

		//look for RandomGeneratorAgent
		this.RandomGenerator=this.getFirstAIDFromDF("random-generation");
		this.HarborManager=this.getFirstAIDFromDF("harbor-managing");

		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new enrollAtHarbor(this,msg));

		if((this.ontologyRepresentation.getContains().getX_dimension() == -1) || (this.ontologyRepresentation.getContains().getY_dimension() == -1) || (this.ontologyRepresentation.getContains().getZ_dimension() == -1)){ //default-größe
			msg=new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			this.addBehaviour(new fetchRandomBayMap(this,msg));
		}else{ //direkt füllen
			msg=new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			this.addBehaviour(new getPopulatedBayMap(this,msg));
		}
	}
}