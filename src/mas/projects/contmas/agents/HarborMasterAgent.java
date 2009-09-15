package mas.projects.contmas.agents;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import mas.projects.contmas.ontology.*;

public class HarborMasterAgent extends ContainerAgent {
	public HarborMasterAgent() {
		super("harbor-managing");
	}

	protected void setup() {
		super.setup();
        //create filter for incoming messages
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
        System.out.println("HarborMaster gestartet (selbst)");
        setupEnvironment();
		addBehaviour(new listenForEnroll(this,mt));
        addBehaviour(new offerCraneList (this,mt));
	}
	
	protected void setupEnvironment(){
        Crane ontologyRepresentation=new Crane();
		Domain terminalArea=new Land();
		Subdomain habitat = new Rail();
		habitat.setLies_in(terminalArea);
        ontologyRepresentation.setLives_in(habitat);
        AgentContainer c = getContainerController();
        AgentController a;
		try {
			//a = c.createNewAgent( "Crane #1", "mas.projects.contmas.agents.CraneAgent",null );
			
			a=c.acceptNewAgent("Crane #1", new CraneAgent(ontologyRepresentation));
	        a.start();

			ontologyRepresentation=new Crane();
			ontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("Crane #2", new CraneAgent(ontologyRepresentation));
	        a.start();
	        
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("Kran gestartet");
	}

	public class offerCraneList extends AchieveREResponder{
		public offerCraneList(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
		protected ACLMessage prepareResponse(ACLMessage request){
			ACLMessage reply=request.createReply();
			try {
				Concept content = ((AgentAction) getContentManager().extractContent(request));
		        if(content instanceof GetCraneList) {
		        	GetCraneList input=(GetCraneList) content;
		            reply.setPerformative(ACLMessage.INFORM); 
		            ProvideCraneList act=new ProvideCraneList();
		    		//look for Cranes
	    			act.setAvailable_cranes(toAIDList(getAIDsFromDF("craning"))); 
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
	
	public class listenForEnroll extends AchieveREResponder {
		public listenForEnroll(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
		protected ACLMessage prepareResponse(ACLMessage request){
			ContentElement content;
			try {
				content = getContentManager().extractContent(request);
				Concept action = ((AgentAction) content);
				if (action instanceof EnrollAtHarbor) {
					ACLMessage rplyMsg = request.createReply();
					rplyMsg.setPerformative(ACLMessage.INFORM);
					AssignHarborQuay act = new AssignHarborQuay();
					Quay concept = new Quay();
					concept.setLies_in(new Sea());
					act.setAssigned_quay(concept);
					getContentManager().fillContent(rplyMsg, act);
					return rplyMsg;
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
			return null;
		}
	}
}