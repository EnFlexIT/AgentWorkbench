package mas.display;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.JFrame;

import mas.environment.OntoUtilities;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import sma.ontology.DisplayOntology;
import sma.ontology.Environment;
import sma.ontology.EnvironmentInfo;
import sma.ontology.Movement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
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
		
		this.doWait(500);
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		Object[] args = getArguments();
//		// Environment
//		if( args != null && args.length > 2 && args[2] instanceof Environment){
//			this.environment = (Environment) args[2];
//		}
				
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
//		this.daGUI.setScale(environment.getScale());
		
//		StringReader reader= new StringReader(environment.getSvgDoc());
//		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
//		
//		try {
//			svgDoc = factory.createDocument(null, reader);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	
//		
//		
//		if(svgDoc != null){
//			this.daGUI.setSVGDoc(svgDoc);
//		}
		
		// Get the EnvironmentControllerAgent
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("EnvironmentController");
		sd.setName(sd.getType()+"_"+projectName);
		dfd.addServices(sd);
		
		try {
			DFAgentDescription[] results = DFService.search(this, dfd);
			if(results != null && results.length > 0){
				masterAgent = results[0].getName();
			}else{
				System.err.println("No ECA found.");
			}
		} catch (FIPAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(masterAgent != null){
			ACLMessage request = new ACLMessage(ACLMessage.QUERY_REF);
			request.addReceiver(masterAgent);
			request.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
			request.setLanguage(codec.getName());
			request.setOntology(ontology.getName());
			
			this.addBehaviour(new AchieveREInitiator(this, request){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				protected void handleInform(ACLMessage inform){
					System.out.println("DA: Environment information received");
					try {
						Action act = (Action) getContentManager().extractContent(inform);
						EnvironmentInfo envInf = (EnvironmentInfo) act.getAction();
						System.out.println(envInf.getEnvironment().getSvgDoc());
						setEnvironment(envInf.getEnvironment());
						addBehaviour(new MovementReceiver());
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
			});
		}
		
		
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
		
		OntoUtilities.setParent(env.getRootPlayground());
		this.environment = env;
		this.daGUI.setScale(env.getScale());
		
		StringReader reader= new StringReader(environment.getSvgDoc());
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
		
		try {
			svgDoc = factory.createDocument(null, reader);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(svgDoc != null){
			this.daGUI.setSVGDoc(svgDoc);
		}
		
		
		
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
//				System.out.println(getLocalName()+" received movement message from "+update.getSender().getLocalName());
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
//				System.out.println(getLocalName()+" waiting for movement messages");
				block();
			}
		}
		
		
		
	}
}
