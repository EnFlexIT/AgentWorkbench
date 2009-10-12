package mas.projects.contmas.agents;
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

import de.planetxml.tools.DebugPrinter;

import mas.projects.contmas.ontology.*;

public class ShipAgent extends PassiveContainerAgent implements TransportOrderOfferer {
	public ShipAgent() {
		super("long-term-transporting");
	}
    AID harborManager=null;
	private AID RandomGenerator;

	protected List contractors=null;

	protected void setup() {
		super.setup();
		ontologyRepresentation=new Ship();
		//TODO hardcoded
		((Ship)ontologyRepresentation).setLength((float) 120.5);
		((Ship)ontologyRepresentation).setLives_in(new Sea());
		//look for RandomGeneratorAgent
		RandomGenerator=getFirstAIDFromDF("random-generation");

		harborManager=getFirstAIDFromDF("harbor-managing");
		
		ContainerMessage msg = new ContainerMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new enrollAtHarbor(this,msg));
		
		
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchRandomBayMap(this,msg));
		/*
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new getPopulatedBayMap(this,msg));
		*/



		
		/*
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new unloadShip(this,msg));
		 */
		//addBehaviour(new unload(this));
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
			act.setX_dimension(1);
			act.setY_dimension(1);
			act.setZ_dimension(1);
			try {
				getContentManager().fillContent(request, act);
			    Vector<ACLMessage> messages = new Vector<ACLMessage>();
			    messages.add(request); 
			    return messages; 
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				System.out.println("Ship - prepareRequests - fillContent - CodecException");
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null; 
		}
	    protected void handleInform(ACLMessage msg) { 
			Concept content;
			try {
				content = ((Concept) getContentManager().extractContent(msg));
		        if (content instanceof ProvideBayMap) {
		        	((ContainerAgent) myAgent).ontologyRepresentation.setContains(((ProvideBayMap) content).getProvides());
		        	//System.out.println("BayMap recieved! X_dimension:"+getLoadBay().getX_dimension()+", Y_dimension:"+getLoadBay().getY_dimension()+", Z_dimension:"+getLoadBay().getZ_dimension());
		    		msg = new ContainerMessage(ACLMessage.REQUEST);
		    	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		    		addBehaviour(new getPopulatedBayMap(myAgent,msg));
		        } else {
		            System.out.println("Error"); 
		        } 
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			try {
				getContentManager().fillContent(request, act);

			    Vector messages = new Vector();
			    messages.add(request);

			    return messages; 
			    
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null; 
		}
	    protected void handleInform(ACLMessage msg) { 
			Concept content;
			try {

				content = ((AgentAction) getContentManager().extractContent(msg));

		        if (content instanceof ProvidePopulatedBayMap) {

		        	((ShipAgent) myAgent).ontologyRepresentation.setContains(((ProvidePopulatedBayMap) content).getProvides());
		        	//System.out.println("populatedBayMap recieved!"); 
		    		offerTransportOrder();

		        } else {
		            System.out.println("Error"); 
		        } 
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public class enrollAtHarbor extends AchieveREInitiator {
		public enrollAtHarbor(Agent a, ACLMessage initiation) {
			super(a, initiation);
		}
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
				request.addReceiver(harborManager);
				EnrollAtHarbor act = new EnrollAtHarbor();
				act.setShip_length(((Ship)((ShipAgent) myAgent).ontologyRepresentation).getLength() );
				try {
					getContentManager().fillContent(request,  act);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			request.addReceiver(harborManager);
			AgentAction act=new GetCraneList();
			try {
				getContentManager().fillContent(request, act);
			    Vector<ACLMessage> messages = new Vector<ACLMessage>();
			    messages.add(request); 
			    return messages; 
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	    protected void handleInform(ACLMessage msg) {
			Concept content;
			try {
				content = ((AgentAction) getContentManager().extractContent(msg));
		        if(content instanceof ProvideCraneList) {
		    		List craneList=((ProvideCraneList) content).getAvailable_cranes();
		    		((ShipAgent) myAgent).contractors=craneList;
		    		myAgent.addBehaviour(new unload(myAgent));
		        }
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

			echoStatus("Tick: Entladen geht los");
			if(((ShipAgent) myAgent).contractors!=null){
				BayMap LoadBay=((ShipAgent) myAgent).ontologyRepresentation.getContains();
				LoadList completeLoadList=new LoadList();

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
							TO.setStarts_at(((ContainerAgent) myAgent).ontologyRepresentation);
							ContainerHolder target=new ContainerHolder();
							target.setLives_in(new Domain());
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
		ContainerMessage msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchCraneList(this,msg));
	}
	

}