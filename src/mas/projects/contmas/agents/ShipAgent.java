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
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;
import jade.util.leap.List;

import java.io.IOException;
import java.util.Vector;

import mas.projects.contmas.ontology.*;

public class ShipAgent extends ContainerAgent {
	public ShipAgent() {
		super("long-term-transporting");
	}
    AID harborManager=null;
	private AID RandomGenerator;

	protected List craneList=null;

	protected void setup() {
		super.setup();
		ontologyRepresentation=new Ship();
		//TODO hardcoded
		((Ship)ontologyRepresentation).setLength((float) 120.5);
		
		//look for RandomGeneratorAgent
		RandomGenerator=getFirstAIDFromDF("random-generation");

		harborManager=getFirstAIDFromDF("harbor-managing");
			
		ContainerMessage msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchRandomBayMap(this,msg));
		/*
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new getPopulatedBayMap(this,msg));
		*/
		msg = new ContainerMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new enrollAtHarbor(this,msg));

		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchCraneList(this,msg));
		
		/*
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new unloadShip(this,msg));
		 */
		addBehaviour(new unloadShip(this));
	}
	
	public BayMap getLoadBay(){
		return ontologyRepresentation.getContains();
	}
	
	public class fetchRandomBayMap extends AchieveREInitiator {
		public fetchRandomBayMap(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector prepareRequests(ACLMessage request) {
			request.addReceiver(RandomGenerator);
			RequestRandomBayMap act = new RequestRandomBayMap();
			//TODO hardcoded
			act.setX_dimension(10);
			act.setY_dimension(10);
			act.setZ_dimension(10);
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
				content = ((Concept) getContentManager().extractContent(msg));
		        if (content instanceof ProvideBayMap) {
		        	((ShipAgent) myAgent).ontologyRepresentation.setContains(((ProvideBayMap) content).getProvides());
		        	System.out.println("BayMap recieved! X_dimension:"+getLoadBay().getX_dimension()+", Y_dimension:"+getLoadBay().getY_dimension()+", Z_dimension:"+getLoadBay().getZ_dimension());
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
				content = ((Concept) getContentManager().extractContent(msg));
		        if (content instanceof ProvidePopulatedBayMap) {
		        	((ShipAgent) myAgent).ontologyRepresentation.setContains(((ProvidePopulatedBayMap) content).getProvides());
		        	System.out.println("populatedBayMap recieved!"); 
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
		    		((ShipAgent) myAgent).craneList=craneList;
		    		myAgent.addBehaviour(new unloadShip(myAgent));
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

	public class unloadShip extends OneShotBehaviour{
		public unloadShip(Agent a) {
			super(a);
		}

		public void action() {
			if(((ShipAgent) myAgent).craneList!=null){
				LoadList currentLoadList=new LoadList();
				//TODO Bay-Map durchlaufen und oberste Lage bestimmen, transportOrder ausfertigen
				TransportOrder TO=new TransportOrder();
				Container transportObject=new Container();
				TO.setStarts_at(myAgent.getAID());
		//		TO.setEnds_at(new Yard());
				TransportOrderChain TOChain=new TransportOrderChain();
				TOChain.addIs_linked_by(TO);
				TOChain.setTransports(transportObject);
				currentLoadList.setConsists_of(TOChain);
				myAgent.addBehaviour(new passContainerOn(myAgent,currentLoadList));
				//block();
			}else{
				System.err.println("Noch keine Kranliste vorhanden");
				//block();
			}
		}
	}
	
	public class passContainerOn extends ContractNetInitiator{
		private LoadList currentLoadList=null;
		public passContainerOn(Agent a, LoadList currentLoadList) {
			super(a, null);
			this.currentLoadList=currentLoadList;
		}
		protected Vector prepareCfps(ACLMessage cfp){
			cfp=new ContainerMessage(ACLMessage.CFP);
			cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET); 
			List craneList=((ShipAgent)myAgent).craneList;
			//TODO nur der erste
			cfp.addReceiver((AID)craneList.iterator().next());
			CallForProposalsOnLoadStage act=new CallForProposalsOnLoadStage();
			act.setRequired_turnover_capacity(this.currentLoadList);
			try {
				getContentManager().fillContent(cfp, act);
				Vector<ACLMessage> messages = new Vector<ACLMessage>();
			    messages.add(cfp); 
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
	}
}