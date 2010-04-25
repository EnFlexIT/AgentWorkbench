package mas.environment;

import sma.ontology.DisplayOntology;
import sma.ontology.Environment;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Central controller agent class
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
		sd.setType("EnvironmentProvider");
		sd.setName("EnvironmentProvider_"+projectName);
		dfd.addServices(sd);
		dfd.addOntologies(ontology.getName());
		dfd.addLanguages(codec.getName());
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
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
