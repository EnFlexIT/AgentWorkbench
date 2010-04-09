package mas.display;

import javax.swing.JFrame;

import org.w3c.dom.Document;

import sma.ontology.DisplayOntology;
import sma.ontology.Environment;
import sma.ontology.Movement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
/**
 * This type of agent is controlling a DisplayAgentGUI instance
 * @author Nils
 *
 */
public class DisplayAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ontology object containing the projects environment
	 */
	private Environment environment = null;
	/**
	 * The GUI this agent controls
	 */
	private DisplayAgentGUI daGUI = null;
	/**
	 * Container for the DisplayAgentGUI, only needed in stand alone mode
	 */
	private JFrame frame = null;
	/**
	 * The SVG document showing the projects current state
	 */
	private Document svgDoc = null;
	/**
	 * MessageTemplate for the TopicManagementHelper
	 */
	private MessageTemplate posTemplate = null;
	/**
	 * The language codec
	 */
	private Codec codec = new SLCodec();
	/**
	 * The ontology
	 */
	private Ontology ontology = DisplayOntology.getInstance();
	/**
	 * The project's name	
	 */
	private String projectName = null;
	/**
	 * The project's EnvironmentControllerAgent
	 */
	private AID masterAgent;
	
	
	/**
	 * Initializing the DisplayAgent. Arguments:
	 * - args[0]: Environment instance containing the projects environment data. Required
	 * - args[1]: DisplayAgentGUI instance the DisplayAgent should use. Optional, if not specified the agent will create a new one on a new JFrame
	 */
	public void setup(){
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		Object[] args = getArguments();
		// Environment
		if( args != null && args.length > 2 && args[2] instanceof Environment){
			this.environment = (Environment) args[2];
		}
				
		// Project name
		if( args != null && args.length > 0 && args[0] instanceof String){
			this.projectName = (String) args[0];
		}
		
		// GUI
		if( args != null && args.length > 1 && args[1] instanceof DisplayAgentGUI){
			// Use the DisplayAgentGUI passed as second argument
			 this.daGUI = (DisplayAgentGUI) args[1];			 
		}else{
			// No DisplayAgentGUI passed, create a new one in a JFrame
			this.daGUI = new DisplayAgentGUI();
			frame = new JFrame("DisplayAgentGUI - Stand Alone Mode");
			frame.setSize(500,300);
			frame.add(this.daGUI);
			frame.setVisible(true);			
		}
		
		
		this.daGUI.setAgent(this);
		this.svgDoc = (Document) this.environment.getSvgDoc();
		if(svgDoc != null){
			this.daGUI.setSVGDoc(svgDoc);
		}
		
//		addBehaviour(new EnvironmentRequestBehaviour(this));
		
		// TopicManagementHelper 		
		TopicManagementHelper tmh;
		try {
			tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.register(positionTopic);
			posTemplate = MessageTemplate.MatchTopic(positionTopic);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addBehaviour(new MovementReceiver());		
	}
	
	
	
	
	/**
	 * @return The agent's ontology
	 */
	public Ontology getOntology(){
		return this.ontology;
	}
	/**
	 * @return The codec
	 */
	public Codec getCodec(){
		return this.codec;
	}
	/**
	 * @return The project's name
	 */
	public String getProjectName(){
		return this.projectName;
	}
	/**
	 * @param env The project's environment
	 */
	public void setEnvironment(Environment env){
		this.environment = env;
		
		System.out.println(getLocalName()+" received environment");
//		this.svgDoc = (Document) env.getSvgDoc();
	}
	
	
	/**
	 * @param masterAgent the masterAgent to set
	 */
	public void setMasterAgent(AID masterAgent) {
		this.masterAgent = masterAgent;
	}


	/**
	 * Behaviour receiving GUI position updates from moving objects and storing them to movedObjects
	 * @author Nils
	 *
	 */
	class MovementReceiver extends CyclicBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage update = receive(posTemplate);
			if(update != null){
				System.out.println(getLocalName()+" received movement message from "+update.getSender().getLocalName());
				try {
					Action act = (Action) getContentManager().extractContent(update);
					Movement mv = (Movement) act.getAction();
					if(mv != null){
						daGUI.addMovement(update.getSender().getLocalName(), mv);
					}
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println(getLocalName()+" waiting for movement messages");
				block();
			}
		}
		
		
		
	}
}
