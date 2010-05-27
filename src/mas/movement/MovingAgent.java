package mas.movement;

import mas.display.DisplayableAgent;
import mas.display.ontology.AgentObject;
import mas.display.ontology.DisplayOntology;
import mas.display.ontology.PlaygroundObject;
import mas.display.ontology.Position;
import mas.display.ontology.Size;
import mas.display.ontology.Speed;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Superclass for all agents using the display functionality
 * @author Nils
 */
public abstract class MovingAgent extends Agent implements DisplayableAgent{
	/**
	 * The playground this agent lives in
	 */
	PlaygroundObject playground = null;
	/**
	 * Ontology object containing the agents data
	 */
	protected AgentObject self = null;
	
	protected AID posTopic = null;
	/**
	 * AID of the projects EnvironmentControllerAgent
	 */
	AID masterAgent = null;
	/**
	 * The language codec
	 */
	Codec codec = new SLCodec();
	/**
	 * The ontology
	 */
	Ontology ontology = DisplayOntology.getInstance();
	
//	/**
//	 * The agent's (center) position
//	 */
//	private Position position = null;
	/**
	 * Is this agent currently moving?
	 */
	private boolean moving = false;
	
	/**
	 * @return Is this agent moving?
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @param moving The movement state
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public void setup(){
		// Register codec and language
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		// Get the EnvironmentControllerAgent from the DF
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("EnvironmentProvider");
		sd.setName("EnvironmentProvider_SMA");
		dfd.addServices(sd);
		
		try {
			DFAgentDescription[] dfResults = DFService.search(this, dfd);
			if(dfResults != null && dfResults.length>0){
				masterAgent = dfResults[0].getName();
//				System.out.println("MasterAgent found :)");
			}else{
//				System.out.println("MasterAgent not found :(");
			}
		} catch (FIPAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Get the agent data 
		Object[] args = getArguments();
		if(args != null && args.length>0 && args[0] instanceof AgentObject){
			this.self = (AgentObject) args[0];			
			this.playground = self.getParent();			
			
			try {
				TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				this.posTopic = tmh.createTopic("position");
				tmh.register(posTopic);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Add behaviours
//			addBehaviour(new MovingExampleBehaviour(this, 100));
			
			
			
			System.out.println("Starting "+getLocalName()+" in playground "+this.playground.getId());
		}
	}

	@Override
	public Speed getCurrentSpeed() {
		return this.self.getCurrentSpeed();		
	}
	
	public void setCurrentSpeed(Speed speed){
		this.setCurrentSpeed(speed);
	}

	@Override
	public Codec getDisplayCodec() {
		return this.codec;
	}

	@Override
	public Ontology getDisplayOntology() {
		return this.ontology;
	}

	@Override
	public Speed getMaxSpeed() {
		return this.self.getMaxSpeed();
	}
	
	public void setMaxSpeed(Speed max){
		this.self.setMaxSpeed(max);
	}

	@Override
	public Position getPosition() {
		return this.self.getPosition();
	}
	@Override
	public void setPosition(Position position) {
		this.self.setPosition(position);		
	}
	
	public Size getSize(){
		return this.self.getSize();
	}
	
	public void setSize(Size size){
		this.self.setSize(size);
	}

	
	
	@Override
	public AID getUpdateReceiver() {
		return posTopic;
	}

}
