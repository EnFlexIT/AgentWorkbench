package mas.projects.contmas.agents;
import java.io.IOException;
import java.util.Vector;

import mas.projects.contmas.ontology.*;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.FIPAProtocolNames;

import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Ship extends Agent {
	private Codec codec = new LEAPCodec();
	private Ontology ontology = ContainerTerminalOntology.getInstance();
	private AID[] RandomGenerator;
	private float length=0;

	private BayMap LoadBay;

	protected void setup() {
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		length=(float) 120.5;
		
		//register self at DF
		ServiceDescription sd = new ServiceDescription();
		sd.setType("long-term-transporting");
		sd.setName(getLocalName());
		register(sd);
		
		//look for RandomGeneratorAgent
        DFAgentDescription dfd = new DFAgentDescription();
        sd  = new ServiceDescription();
        sd.setType( "random-generation");
        dfd.addServices(sd);
			DFAgentDescription[] result;
			try {
				result = DFService.search(this, dfd);
				RandomGenerator= new AID[result.length];
				for (int i = 0; i < result.length; i++) {
					RandomGenerator[i]=result[i].getName();
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		addBehaviour(new enrollAtHarbor(this));
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
	    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
		addBehaviour(new fetchRandomBayMap(this,msg));
	}

	void register(ServiceDescription sd) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public class enrollAtHarbor extends OneShotBehaviour {
	    AID[] harborManager=null;
		public enrollAtHarbor(Agent a) {
			super(a);
		}
	
		public void action() {
	        DFAgentDescription dfd = new DFAgentDescription();
	        ServiceDescription sd  = new ServiceDescription();
	        sd.setType( "harbor-managing" );
	        dfd.addServices(sd);
	        try {
				DFAgentDescription[] result = DFService.search(myAgent, dfd);
				harborManager= new AID[result.length];
				for (int i = 0; i < result.length; i++) {
					harborManager[i]=result[i].getName();
				}
				ACLMessage msg= new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(harborManager[0]);
				EnrollAtHarbor act = new EnrollAtHarbor();
				act.setShip_length(((Ship) myAgent).length );
				
				msg.setLanguage(codec.getName());
				msg.setOntology(ontology.getName());
				
//				getContentManager().fillContent(msg,  new Action(harborManager[0],act));
				getContentManager().fillContent(msg,  act);
	            System.out.println("Schiff: Hafenanmeldung wird verschickt");
				send(msg);
				
			} catch (FIPAException e) {
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

	public class fetchRandomBayMap extends AchieveREInitiator {
		public fetchRandomBayMap(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		protected Vector prepareRequests(ACLMessage request) {
			RequestRandomBayMap act = new RequestRandomBayMap();
			act.setX_dimension(10);
			act.setY_dimension(10);
			act.setZ_dimension(10);
			request.setLanguage(codec.getName());
			request.setOntology(ontology.getName());
			request.addReceiver(RandomGenerator[0]);
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
				content = ((Concept) getContentManager().extractContent(msg));
		        if (content instanceof ProvideBayMap) {
		        	BayMap LoadMap=((ProvideBayMap) content).getProvides();
		        	System.out.println("BayMap recieved! X_dimension:"+LoadMap.getX_dimension()+", Y_dimension:"+LoadMap.getY_dimension()+", Z_dimension:"+LoadMap.getZ_dimension()); 
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

	public class recieveGentryCraneInformation extends SimpleBehaviour {
		public recieveGentryCraneInformation(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println(" - " + myAgent.getLocalName() + " <- "
						+ msg.getContent());
				this.finished = true;
			}
			block();

		}

		private boolean finished = false;

		public boolean done() {
			return finished;
		}
	}
	*/
}
