package game_of_life.agent_controller;
import game_of_life.gui.GameOfLifeGUI;
import game_of_life.ontology.PlatformOntology;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

import jade.lang.acl.*;
import jade.util.leap.Iterator;


public class agentController extends Agent {
	// Constants
    //////////////////////////////////


    // topic variables
    //////////////////////////////////
	private AID startGameOfLife;
	private AID changeStatus;
	private TopicManagementHelper topicHelper;

    // Instance variables
    //////////////////////////////////
	private int AgentCounter;
	private int pRow, pCol; // Agent's position
	
	
	// variables for ontology
	// ////////////////////////////////
	private int portNumber;
	private String typOfMessage;
	private String whatToDo;
	private String platformName;
	private String platformUrl;


    // Constructors
    //////////////////////////////////

	private DFAgentDescription template;               // DF-Information
	private ServiceDescription templateSd;
	private SearchConstraints searchConstraints;
	private ContentManager manager;                    // We handle contents
	private Codec codec = new SLCodec();               // This agent speaks the SL language
	private Ontology ontology;                         // This agent complies with the People ontology	

    // Array Variables
    //////////////////////////////////
	
	private AMSAgentDescription AMSSearch[] = null;     // array for storing all agents on platform
	private String AMSSearchName[] = null;	            // array for storing all agents name  on platform
	private Object object[] = null;	                    // array for storing all agents in system
	private Object coordinates[] = null;                //Array for storing the coordinates of agents  
	AMSAgentDescription[] allAgents = null;             // Array for storing all agents on platform
	
    // External signature methods
    //////////////////////////////////
	

	protected void setup() {
		
		coordinates = getArguments();                  // extract stored objects of Agents (Coordinates)
		
      try {
    	// Instance variables
    	//////////////////////////////////
    	  
		pRow = (Integer) coordinates[0];                 // index of Agent's name at possition 0
		pCol = (Integer) coordinates[1];                 // index of Agent's name at possition 2

		Thread.sleep(1000);
			
		} catch (Exception e) {
			System.out.println(" Problem "+e.getMessage());
		}
        
		AgentCounter =0;

		//register Topic for communication
		////////////////////////////////////////////////////////
		// Periodically send messages about topic "JADE"
		try {
			
			// Periodically send messages about topic "JADE"
			topicHelper = (TopicManagementHelper)getHelper(TopicManagementHelper.SERVICE_NAME);
			startGameOfLife = topicHelper.createTopic("startGameOfLife");
			changeStatus =  topicHelper.createTopic("changeStatus");
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
        // Constructors
        //////////////////////////////////
           
		template = new DFAgentDescription();            // Build the description used as template for the search
		//templateSd = new ServiceDescription();
		//templateSd.setType("gameOfLfe");
		searchConstraints = new SearchConstraints();                   // for controlling the amount of messages received
		searchConstraints.setMaxResults(new Long(500));
		//DFSearchAgent(template, sc);
		//registerMyselfDF( template ) ;                // Register Agent in DF
		AMSSearchAgent(searchConstraints);                             //Search ALl Agents on platform
		manager = (ContentManager) getContentManager(); // manager for handling content
		ontology = PlatformOntology.getInstance();
		getContentManager().registerLanguage(codec);    // register Ontology information and 
		getContentManager().registerOntology(ontology); //activate codec for decoding and coding languages

		
		// start behavours of agent
        //////////////////////////////////
		addBehaviour(new AMSSearchAgent(this));        // Search for Agents on platform 
		// addBehaviour(new randomSelectAgents(this));
		
		GameOfLifeGUI.agentController = this;           // Store Object Of AgentController
		
	}

	 // Internal implementation methods
    //////////////////////////////////
	
	//braodcast start to all agents
	public void broadcastStartGameOfLife(){
		
		//System.out.println("Agent "+getLocalName()+": Sending message about topic "+startGameOfLife.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(startGameOfLife);
		msg.setContent("Start game of life");
		send(msg);
	}

	//broadcast change status to all agent
	public void broadcastChangeStatus(){

		//System.out.println("Agent "+getLocalName()+": Sending second message about topic "+changeStatus.getLocalName());
		ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
		msg2.addReceiver(changeStatus);
		msg2.setContent("Changing status of agent ");
		send(msg2);
		
	}
	
	// Search for Agents on platform
	protected void AMSSearchAgent(SearchConstraints sc) {

		AMSAgentDescription [] results = null;
    	
    	try {
    		if(sc!=null){
    			
			allAgents = AMSService.search( this, new AMSAgentDescription (), sc );
			
    		}
		}
		catch (Exception e) {
            System.out.println( "Problem searching AMS: " + e );
            e.printStackTrace();
		}
		
		AMSSearchName = new String[allAgents.length];
    	
		//Print out all agents on platform
		for (int i=0; i<allAgents.length;i++)
		{
			
			AMSSearchName[i] = allAgents[i].getName().getLocalName();
			AID agentID = allAgents[i].getName();
			//System.out.println(" Agent"   + i + " : " + agentID.getName());
		}
		
		this.AMSSearch = allAgents;
		
	}
	// Search for Agents with services of type "gameOfLive"
	protected void DFSearchAgent(DFAgentDescription dfd, SearchConstraints sc) {

		try {

			DFAgentDescription[] results = DFService.search(this, dfd, sc);

			// Array for storing Agents with the length of result
			object = new Object[results.length];
          
			//System.out.println(" Length of Object  "+object.length);
			if (results.length > 0) {
				System.out.println("Agent " + getLocalName()
						+ " found the following gameOfLive services:");

				for (int i = 0; i < results.length; ++i) {

					DFAgentDescription df = results[i];
					AID provider = df.getName();

					object[i] = provider.getLocalName();
					// The same agent may provide several services; we are only
					// interested in the GameOfLife one
					Iterator it = df.getAllServices();
					while (it.hasNext()) {
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals("gameOfLife")
								&& !provider.getLocalName().equals(
										getLocalName())) {
							System.out.println("- Service \"" + sd.getName()
									+ "\" provided by agent "
									+ provider.getLocalName());
						}
					}
				}
			} else {
				System.out.println("Agent " + getLocalName()
						+ " did not find any gameOfLive service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	// Register Agent in DF
	protected void registerMyselfDF(DFAgentDescription dfd) {
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	// Sellect all agents on Jade Platform
	protected void platformSearchAgents(SearchConstraints sc) {

		try {
			if (sc != null) {

				sc.setMaxResults(new Long(-1));
				this.allAgents = AMSService.search(this,
						new AMSAgentDescription(), sc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Print out all agents on platform
		for (int i = 0; i < allAgents.length; i++) {

			// AID agentID = allAgents[i].getName();
			String AgentName = allAgents[i].getName().getLocalName();
			if (!AgentName.equals("amm") && !AgentName.equals("ams")
					&& !AgentName.equals("rma") && !AgentName.equals("df")
					&& !AgentName.equals(getLocalName())) {
				
				System.out.println(" Agents on Platform  AgentName   = "
						+ allAgents[i].getName().getLocalName());
			}
		}
	}

	// get all agents on Jade Platform
	protected AMSAgentDescription[] setAllAgents() {

		return allAgents;
	}

	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		try {
			
			DFService.deregister(this);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	// Internal implementation behavours
    //////////////////////////////////
	
	// Search all agents on the platform
	class AMSSearchAgent extends Behaviour {

		private boolean finished = false;
		
		public AMSSearchAgent(Agent a) {
			super(a);
		}

		public void action() {
			
			AMSAgentDescription [] results = null;
	    	
	    	try {
	    		if(searchConstraints!=null){
	    			
				allAgents = AMSService.search( myAgent, new AMSAgentDescription (), searchConstraints );
				
	    		}
			}
			catch (Exception e) {
	            System.out.println( "Problem searching AMS: " + e );
	            e.printStackTrace();
			}
			
			AMSSearchName = new String[allAgents.length];
	    	
			//Print out all agents on platform
			for (int i=0; i<allAgents.length;i++)
			{
				
				AMSSearchName[i] = allAgents[i].getName().getLocalName();
				AID agentID = allAgents[i].getName();
				//System.out.println(" Agent"   + i + " : " + agentID.getName());
			}
			
			AMSSearch = allAgents;
			
			
			finished = true;
		}
		 
	
		public boolean done() {
			return finished;
		  }
		}
}
