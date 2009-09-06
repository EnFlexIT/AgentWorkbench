package mas.projects.contmas.agents;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;
import jade.util.leap.List;

import java.util.Vector;

import mas.projects.contmas.ontology.*;

public class ShipAgent extends ContainerAgent {
	public ShipAgent() {
		super("long-term-transporting");
	}
    AID harborManager=null;
	private AID RandomGenerator;
	private float length=0;

	private BayMap LoadBay;
	
	protected List craneList=null;

	protected void setup() {
		super.setup();
		ontologyRepresentation=new Ship();
	//	ontologyRepresentation.;
		length=(float) 120.5;
		
		//look for RandomGeneratorAgent
		RandomGenerator=getFirstAIDFromDF("random-generation");

		harborManager=getFirstAIDFromDF("harbor-managing");
		
		ContainerMessage msg = new ContainerMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new enrollAtHarbor(this,msg));
		
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchRandomBayMap(this,msg));
		
		msg = new ContainerMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchCraneList(this,msg));
	}


	public class enrollAtHarbor extends AchieveREInitiator {
		public enrollAtHarbor(Agent a, ACLMessage initiation) {
			super(a, initiation);
		}
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
				request.addReceiver(harborManager);
				EnrollAtHarbor act = new EnrollAtHarbor();
				//TODO hardcoded
				act.setShip_length(((ShipAgent) myAgent).length );
				
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
		private Agent myAgent;
		public fetchCraneList(Agent a, ACLMessage msg) {
			super(a, msg);
			myAgent=a;
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
		    		myAgent.addBehaviour(new unloadShip());
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
	    	//nutin'
	    }
	}

	public class fetchRandomBayMap extends AchieveREInitiator {
		public fetchRandomBayMap(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector prepareRequests(ACLMessage request) {
			request.addReceiver(RandomGenerator);
			RequestRandomBayMap act = new RequestRandomBayMap();
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
		        	((ShipAgent) myAgent).LoadBay=((ProvideBayMap) content).getProvides();
		        	System.out.println("BayMap recieved! X_dimension:"+LoadBay.getX_dimension()+", Y_dimension:"+LoadBay.getY_dimension()+", Z_dimension:"+LoadBay.getZ_dimension()); 
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
	
	public class unloadShip extends OneShotBehaviour{

		@Override
		public void action() {
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
/*
	public class getPopulatedBayMap extends SimpleBehaviour {
		public getPopulatedBayMap(Agent a) {
			super(a);
		}

		private boolean finished = false;

		public void action() {
			ACLMessage sndMsg = new ACLMessage(ACLMessage.REQUEST);
			try {
				sndMsg.setContentObject(LoadBay);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			AID dest = new AID("RandomGeneratorAgent", AID.ISLOCALNAME);
			sndMsg.addReceiver(dest);
			send(sndMsg);
			ACLMessage rcvMsg = receive();
			if (rcvMsg != null) {
				try {
					LoadBay = (BayMap) rcvMsg.getContentObject();
					for (int z = 0; z < LoadBay.Height; z++) {
						for (int y = 0; y < LoadBay.Length; y++) {
							for (int x = 0; x < LoadBay.Width; x++) {
								System.out.println("Container auf " + "x:" + x
										+ " y:" + y + " z:" + z + " : "
										+ LoadBay.getContainerAt(x, y, z).Name);
							}
						}
					}
					finished = true; // lastChange

				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public boolean done() {
			return finished;
		}
	}


	*/
}
