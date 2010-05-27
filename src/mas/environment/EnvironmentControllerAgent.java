package mas.environment;

import mas.display.DisplayConstants;
import mas.display.ontology.DisplayOntology;
import mas.display.ontology.Environment;
import mas.display.ontology.EnvironmentInfo;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * Central controller agent class
 * - Sending the environment definition to DisplayAgents
 * @author Nils
 *
 */
public class EnvironmentControllerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Project name of the simulation project
	 */
	private String projectName;
	/**
	 * Ontology object of the project environment
	 */
	private Environment environment;
	
	private Ontology ontology = DisplayOntology.getInstance();
	
	private Codec codec = new SLCodec();
	
	public void setup(){
		
		getContentManager().registerLanguage(codec);
		
		getContentManager().registerOntology(ontology);
		
		Object[] args = getArguments();
		
		if(args != null && args.length > 0){
			environment = (Environment) args[0];
			projectName = environment.getProjectName();
		}
		
		// Register at the DF
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(DisplayConstants.ECA_SERVICE_TYPE);
		sd.setName(DisplayConstants.ECA_SERVICE_TYPE+"_"+projectName);
		dfd.addServices(sd);
//		dfd.addOntologies(ontology.getName());
//		dfd.addLanguages(codec.getName());
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageTemplate envRequestTemplate = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_QUERY);
		this.addBehaviour(new AchieveREResponder(this, envRequestTemplate){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
				ACLMessage reply = request.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				
				environment.getRootPlayground().unsetParent();
				
				EnvironmentInfo envInf = new EnvironmentInfo();
				envInf.setEnvironment(environment);
								
				
				Action act = new Action();
				act.setActor(getAID());
				act.setAction(envInf);
				
				try {
					getContentManager().fillContent(reply, act);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				environment.getRootPlayground().setParent();
				
				return reply;				
			}
		}); 
		
	}
	
	/**
	 * 
	 * @return The project environment
	 */
	public Environment getEnvironment(){
		return this.environment;
	}
	/**
	 * @return The DisplayOntology
	 */
	public Ontology getOntology(){
		return this.ontology;
	}
	/**
	 * 
	 * @return The language codec
	 */
	public Codec getCodec(){
		return this.codec;
	}
}
