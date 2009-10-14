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
		Domain habitat = new Rail();
		habitat.setLies_in(terminalArea);
        ontologyRepresentation.setLives_in(habitat);

		Domain capability=new Rail();
		ontologyRepresentation.addCapable_of(capability);
		capability=new Street();
		ontologyRepresentation.addCapable_of(capability);
		capability=new Sea();
		ontologyRepresentation.addCapable_of(capability);


        AgentContainer c = getContainerController();
        AgentController a;
		try {
			//a = c.createNewAgent( "Crane #1", "mas.projects.contmas.agents.CraneAgent",null );
			
			a=c.acceptNewAgent("Crane #1", new CraneAgent(ontologyRepresentation));
	        a.start();

			ontologyRepresentation=new Crane();
			ontologyRepresentation.setLives_in(habitat);
			ontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("Crane #2", new CraneAgent(ontologyRepresentation));
	        a.start();
	        

	        AGV AGVontologyRepresentation=new AGV();
			habitat = new Street();
			habitat.setLies_in(terminalArea);
	        AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #1", new AGVAgent(AGVontologyRepresentation));
	        a.start();
	        
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      //  System.out.println("Kran gestartet");
	}

	public class offerCraneList extends AchieveREResponder{
		public offerCraneList(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
		protected ACLMessage prepareResponse(ACLMessage request){
			ACLMessage reply=request.createReply();
			Concept content = ((ContainerAgent)myAgent).extractAction(request);
			if(content instanceof GetCraneList) {
				GetCraneList input=(GetCraneList) content;
			    reply.setPerformative(ACLMessage.INFORM); 
			    ProvideCraneList act=new ProvideCraneList();
				//look for Cranes
				act.setAvailable_cranes(toAIDList(getAIDsFromDF("craning"))); 
				((ContainerAgent)myAgent).fillMessage(reply, act);
			}else{
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
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
			content = ((ContainerAgent)myAgent).extractAction(request);
			Concept action = ((AgentAction) content);
			if (action instanceof EnrollAtHarbor) {
				ACLMessage rplyMsg = request.createReply();
				rplyMsg.setPerformative(ACLMessage.INFORM);
				AssignHarborQuay act = new AssignHarborQuay();
				Quay concept = new Quay();
				concept.setLies_in(new Sea());
				act.setAssigned_quay(concept);
				((ContainerAgent)myAgent).fillMessage(rplyMsg, act);
				return rplyMsg;
			}
			return null;
		}
	}
}