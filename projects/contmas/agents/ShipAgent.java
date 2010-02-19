package contmas.agents;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.io.IOException;
import java.util.Vector;

import contmas.ontology.*;

import de.planetxml.tools.DebugPrinter;


public class ShipAgent extends StaticContainerAgent implements TransportOrderOfferer {
	public ShipAgent() {
		this(new Ship());
	}

	public ShipAgent(Ship ontologyRepresentation) {
		super("long-term-transporting", ontologyRepresentation);
	}
	private AID HarborManager=null;
	private AID RandomGenerator=null;

	protected void setup() {
		super.setup();

		//TODO hardcoded
		((Ship)ontologyRepresentation).setLength((float) 120.5);
		ontologyRepresentation.setLives_in(new Sea());
		
		//look for RandomGeneratorAgent
		RandomGenerator=getFirstAIDFromDF("random-generation");
		HarborManager=getFirstAIDFromDF("harbor-managing");
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new enrollAtHarbor(this,msg));
		
		if(ontologyRepresentation.getContains().getX_dimension()==-1 ||
			ontologyRepresentation.getContains().getY_dimension()==-1 ||
			ontologyRepresentation.getContains().getZ_dimension()==-1){ //default-größe
			msg = new ACLMessage(ACLMessage.REQUEST);
		    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
			addBehaviour(new fetchRandomBayMap(this,msg));
		} else { //direkt füllen
			msg = new ACLMessage(ACLMessage.REQUEST);
		    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
    		addBehaviour(new getPopulatedBayMap(this,msg));
		}
	}
		
    public List determineContractors(){
    	return 	contractors;
    }

	public class fetchRandomBayMap extends AchieveREInitiator {
		public fetchRandomBayMap(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector prepareRequests(ACLMessage request) {
			request.addReceiver(RandomGenerator);
			RequestRandomBayMap act = new RequestRandomBayMap();
			//TODO hardcoded
			act.setX_dimension(4);
			act.setY_dimension(4);
			act.setZ_dimension(2);
			((ContainerAgent)myAgent).fillMessage(request,act);
		    Vector<ACLMessage> messages = new Vector<ACLMessage>();
		    messages.add(request); 
			return messages; 
		}
	    protected void handleInform(ACLMessage msg) { 
			Concept content;
			content = ((ContainerAgent)myAgent).extractAction(msg);
	        if (content instanceof ProvideBayMap) {
	        	((ContainerAgent) myAgent).ontologyRepresentation.setContains(((ProvideBayMap) content).getProvides());
	        	//echoStatus("BayMap recieved! X_dimension:"+getLoadBay().getX_dimension()+", Y_dimension:"+getLoadBay().getY_dimension()+", Z_dimension:"+getLoadBay().getZ_dimension());
	    		msg = new ACLMessage(ACLMessage.REQUEST);
	    	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
	    		addBehaviour(new getPopulatedBayMap(myAgent,msg));
	        } else {
	            echoStatus("Error"); 
	        } 
	    }
	}
	
	public class getPopulatedBayMap extends AchieveREInitiator {
		public getPopulatedBayMap(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector prepareRequests(ACLMessage request) {
			request.addReceiver(RandomGenerator);
			//BayMap aus Agent auslesen
			RequestPopulatedBayMap act = new RequestPopulatedBayMap();
			act.setPopulate_on(((ShipAgent) myAgent).ontologyRepresentation.getContains());
			((ContainerAgent)myAgent).fillMessage(request,act);
		    Vector messages = new Vector();
		    messages.add(request);

		    return messages; 
		}
	    protected void handleInform(ACLMessage msg) { 
			Concept content;
			content = ((ContainerAgent)myAgent).extractAction(msg);

	        if (content instanceof ProvidePopulatedBayMap) {
	        	((ShipAgent) myAgent).ontologyRepresentation.setContains(((ProvidePopulatedBayMap) content).getProvides());
	        	//echoStatus("populatedBayMap recieved!"); 
	    		offerTransportOrder();
	        } else {
	            echoStatus("Error"); 
	        } 
	    }
	}
	
	public class enrollAtHarbor extends AchieveREInitiator {
		public enrollAtHarbor(Agent a, ACLMessage initiation) {
			super(a, initiation);
		}
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
				request.addReceiver(HarborManager);
				EnrollAtHarbor act = new EnrollAtHarbor();
				act.setShip_length(((Ship)((ShipAgent) myAgent).ontologyRepresentation).getLength() );
				((ContainerAgent)myAgent).fillMessage(request,act);

	            Vector<ACLMessage> messages=new Vector<ACLMessage>();
				messages.add(request);
				return messages;
		}
	}
	public class fetchCraneList extends AchieveREInitiator {
		public fetchCraneList(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
			request.addReceiver(HarborManager);
			AgentAction act=new GetCraneList();
			((ContainerAgent)myAgent).fillMessage(request,act);

		    Vector<ACLMessage> messages = new Vector<ACLMessage>();
		    messages.add(request); 
		    return messages; 
		}
	    protected void handleInform(ACLMessage msg) {
			Concept content;
			content = ((ContainerAgent)myAgent).extractAction(msg);

	        if(content instanceof ProvideCraneList) {
	    		List craneList=((ProvideCraneList) content).getAvailable_cranes();
	    		((ShipAgent) myAgent).contractors=craneList;
	    		myAgent.addBehaviour(new unload(myAgent));
	        }
	    }
	}

//	public class unload extends TickerBehaviour{
	public class unload extends OneShotBehaviour{

		public unload(Agent a) {
//			super(a, 1000);
			super(a);
			
		}

//		public void onTick() {
		public void action() {

//			echoStatus("Tick: Entladen geht los");
			if(((ShipAgent) myAgent).contractors!=null){
				BayMap LoadBay=((ShipAgent) myAgent).ontologyRepresentation.getContains();
				LoadList completeLoadList=new LoadList();
				Designator myself=new Designator();
				myself.setType("concrete");
				myself.setConcrete_designation(myAgent.getAID());
				myself.setAbstract_designation(((ContainerAgent)myAgent).ontologyRepresentation.getLives_in());


				for(int x=0;x<LoadBay.getX_dimension();x++){ //baymap zeilen-
					for(int y=0;y<LoadBay.getY_dimension();y++){ //und spaltenweise durchlaufen
						BlockAddress upmostContainer=null;
						Iterator allContainers=LoadBay.getAllIs_filled_with();
						while(allContainers.hasNext()){ //alle geladenen Container überprüfen 
							BlockAddress curContainer=(BlockAddress) allContainers.next();
							if(curContainer.getX_dimension()==x && curContainer.getY_dimension()==y){ //betrachteter Container steht im stapel auf momentaner koordinate
								if(upmostContainer==null || upmostContainer.getZ_dimension()<curContainer.getZ_dimension()){
									upmostContainer=curContainer;
								}
							}
						} //end while
						if(upmostContainer!=null){ //an dieser Koordinate steht ein Container obenauf
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
							myAgent.addBehaviour(new announceLoadOrders(myAgent,currentLoadList));
							currentLoadList=null;
							upmostContainer=null;
						}
					}
				}
				//Variante: alle Container einer Lage zusammen
//				myAgent.addBehaviour(new announceLoadOrders(myAgent,completeLoadList));

				block();
			}else{
				System.err.println("Noch keine Kranliste vorhanden");
				block();
			}
		}

	}

	public void offerTransportOrder() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchCraneList(this,msg));
	}
}