package mas.projects.contmas.agents;
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
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class HarborMasterAgent extends Agent {
	private Codec codec = new LEAPCodec();
	private Ontology ontology = ContainerTerminalOntology.getInstance();

	protected void setup() {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "harbor-managing" );
        sd.setName( getLocalName() );
        register( sd );
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
        System.out.println("HarborMaster gestartet (selbst)");
		addBehaviour(new listenForEnroll(this));
	}

    void register( ServiceDescription sd)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
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
