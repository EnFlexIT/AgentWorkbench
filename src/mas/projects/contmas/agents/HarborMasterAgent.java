package mas.projects.contmas.agents;
import java.util.Arrays;

import mas.projects.contmas.agents.RandomGeneratorAgent.createRandomBayMap;
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
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class HarborMasterAgent extends ContainerAgent {
	public HarborMasterAgent() {
		super("harbor-managing");
	}

	protected void setup() {
		super.setup();
        //create filter for incoming messages
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
        System.out.println("HarborMaster gestartet (selbst)");
		addBehaviour(new listenForEnroll(this));
        addBehaviour(new offerCraneList (this,mt));
	}

	public class offerCraneList extends AchieveREResponder{

		public offerCraneList(Agent a, MessageTemplate mt) {
			super(a, mt);
			// TODO Auto-generated constructor stub
		}
		protected ACLMessage prepareResponses(ACLMessage request){
			ACLMessage reply=request.createReply();
			Concept content;
			try {
				content = ((AgentAction) getContentManager().extractContent(request));
		        if(content instanceof GetCraneList) {
		        	GetCraneList input=(GetCraneList) content;
		            reply.setPerformative(ACLMessage.INFORM); 
		            ProvideCraneList act=new ProvideCraneList();
		    		//look for RandomGeneratorAgent
		            DFAgentDescription dfd = new DFAgentDescription();
		            ServiceDescription sd  = new ServiceDescription();
		            sd.setType( "craning");
		            dfd.addServices(sd);
	    			DFAgentDescription[] result;
	    			try {
	    				result = DFService.search(myAgent, dfd);
		    			for (DFAgentDescription agentDescription : result) {
							act.addAvailable_cranes(agentDescription.getName());
						}
	    			} catch (FIPAException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}

					getContentManager().fillContent(reply, act);
		        }else{
		        	reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
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
			return reply;
		}
	}
	
	public class listenForEnroll extends CyclicBehaviour {
		public listenForEnroll(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				try {
					ContentElement content= getContentManager().extractContent(msg);
//					Concept action = ((Action) content).getAction();
					Concept action = ((AgentAction) content);

					switch (msg.getPerformative()) {
					case (ACLMessage.REQUEST): {
						if (action instanceof EnrollAtHarbor) {
							ACLMessage rplyMsg = msg.createReply();
							rplyMsg.setPerformative(ACLMessage.INFORM);
							AssignHarborQuay act = new AssignHarborQuay();
							Quay concept = new Quay();
							concept.setLies_in(new Sea());
							act.setAssigned_quay(concept);
							rplyMsg.setLanguage(codec.getName());
							rplyMsg.setOntology(ontology.getName());
//							getContentManager().fillContent(rplyMsg, new Action(,act));
							getContentManager().fillContent(rplyMsg, act);
				            System.out.println("Hafenmeister: Kai zugeteilt");
							send(rplyMsg);
						}
						break;
					}//end case
					}//end switch
				} catch (UngroundedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CodecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OntologyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else {
				block();
			}
		}

	}

}
