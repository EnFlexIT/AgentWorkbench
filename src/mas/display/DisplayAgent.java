package mas.display;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JFrame;

import mas.environment.ObjectTypes;

import org.w3c.dom.Document;

import application.Language;
import sma.ontology.AbstractObject;
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

public class DisplayAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Environment environment = null;
	
	private DisplayAgentGUI daGUI = null;
	
	private JFrame frame = null;
	
	private Document svgDoc = null;
	
	private MessageTemplate posTemplate = null;
	
	private Codec codec = new SLCodec();
	
	private Ontology ontology = DisplayOntology.getInstance();
	
	private HashMap<String, AbstractObject> movableObjects = null;
	
	private HashSet<AbstractObject> movedObjects = null;
	
	
	
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
		if( args != null && args.length > 0 && args[0] instanceof Environment){
			this.environment = (Environment) args[0];
		}
		if(this.environment != null){
			movableObjects = buildMovableHash(this.environment);
			movedObjects = new HashSet<AbstractObject>();			
		}else{
			System.err.println(Language.translate("Keine Umgebung übergeben"));
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
	 * Stores all movable environment objects in a HashMap for easy access via id
	 * @param env The project's environment
	 * @return A HashMap containing all movable environment objects, using their id as key
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, AbstractObject> buildMovableHash(Environment env){
		HashMap<String, AbstractObject> mh = new HashMap<String, AbstractObject>();
		
		Iterator<AbstractObject> objects = env.getAllObjects();
		while(objects.hasNext()){
			AbstractObject object = objects.next();
			
			switch(ObjectTypes.getType(object)){
			case AGENT:
			// When adding more movable types, put them here
				mh.put(object.getId(), object);
			break;
			}
			
		}
		
		
		return mh;
	}
	
	/**
	 * @return Iterator of AbstractObjects with changed positions
	 */
	public HashSet<AbstractObject>getMovedObjects(){
		return movedObjects;
	}
	
	private class MovementReceiver extends CyclicBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage update = receive(posTemplate);
			if(update != null){
				try {
					Action act = (Action) getContentManager().extractContent(update);
					Movement mv = (Movement) act.getAction();
					if(mv != null){
						AbstractObject moved = movableObjects.get(act.getActor().getLocalName());
						int newX = mv.getStartPos().getXPos()+mv.getSpeed().getXSpeed();
						int newY = mv.getStartPos().getYPos()+mv.getSpeed().getYSpeed();
						moved.getPosition().setXPos(newX);
						moved.getPosition().setYPos(newY);
						movedObjects.add(moved);
					}
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				block();
			}
		}
		
		
		
	}
}
