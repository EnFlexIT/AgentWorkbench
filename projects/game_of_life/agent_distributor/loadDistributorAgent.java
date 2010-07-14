package game_of_life.agent_distributor;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

import application.Application;
import game_of_life.gui.GameOfLifeObject;
import game_of_life.gui.GameOfLifeGUI;
import game_of_life.ontology.Platform;
import game_of_life.ontology.PlatformInfo;
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

public class loadDistributorAgent extends Agent {

	// Constants
	// ////////////////////////////////

	// Static variables
	// ////////////////////////////////
	public static GameOfLifeObject gameOfLifeObject[][];
	
	// Instance variables
	// ////////////////////////////////
	private int AgentCounter;
	private int pRow, pCol; // Agent's position
	private boolean startGameOfLife;
	private int startOnlyOnce = 0;

	// variables for ontology
	// ////////////////////////////////
	private int portNumber;
	private String typOfMessage;
	private String whatToDo;
	private String platformName;
	private String platformUrl;

	// Objects for cpu and memory
	// ////////////////////////////////
	//private deepMemoryUsageOfObject deepMemory;
	private memoryAndCpuLoad cpuLoad;

	// Instance variables for CPU and memory
	// ////////////////////////////////
	private int totalCpu;
	private double cpuSystemTime;
	private double cpuUserTime;
	private long combineTime;

	private long totalMemory;
	private long freeMemory;
	private long useMemory;

	// Instance variables for CPU and memory for various Platforms
	// ////////////////////////////////
	// PlatformA (PA)
	private long freeMemPA;
	private double cpuFreePA;

	// PlatformA (PA)
	private long freeMemPB;
	private double cpuFreePB;

	// PlatformA (PA)
	private long freeMemPC;
	private double cpuFreePC;

	// Constructors
	// ////////////////////////////////
	private DFAgentDescription template; // DF-Information
	private ServiceDescription templateSd;
	private SearchConstraints searchConstraints;
	private ContentManager manager; // We handle contents
	private Codec codec = new SLCodec(); // This agent speaks the SL language
	private Ontology ontology; // This agent complies with the People ontology

	// Objects for cpu and memory
	// ////////////////////////////////

	// Array Variables
	// ////////////////////////////////
	private AMSAgentDescription AMSSearch[] = null; // array for storing all
	// agents on platform
	private String AMSSearchName[] = null; // array for storing all agents name
	// on platform
	private Object object[] = null; // array for storing all agents in system
	private Object coordinates[] = null; // Array for storing the coordinates of
	// agents
	private AMSAgentDescription[] allAgents = null; // Array for storing all

	// agents on platform

	// External signature methods
	// ////////////////////////////////

	protected void setup() {
		
		startGameOfLife = true;

		coordinates = getArguments(); // extract stored objects of Agents
		// (Coordinates)
		
		// Objects for cpu and memory
		// ////////////////////////////////
		//deepMemory = new deepMemoryUsageOfObject();
		cpuLoad = new memoryAndCpuLoad();
		
		try {
			// Instance variables
			// ////////////////////////////////

			pRow = (Integer) coordinates[0]; // index of Agent's name at  possition 0
			
			pCol = (Integer) coordinates[1]; // index of Agent's name at possition 2

			Thread.sleep(1000);

		} catch (Exception e) {
			//System.out.println(" Problem " + e.getMessage());
		}
		
		//set the current Values of the platform
		setPlatformValues();
		
		//Array for storing Objects for the game of life
		gameOfLifeObject = new GameOfLifeObject[pRow][pCol]; 
		
		//Start distributing Agents in container
		int agents[][]= new int[pRow][pCol];
		int totalAgents = pRow * pCol;
		
		loadDistributor(4, 0, totalAgents, agents);
		
		AgentCounter = 0;

		// Instance variables for CPU and memory
		// ////////////////////////////////
		totalCpu = 0;
		cpuSystemTime = 0;
		cpuUserTime = 0;
		combineTime = 0;

		totalMemory = 0;
		freeMemory = 0;
		useMemory = 0;

		// Constructors
		// ////////////////////////////////

		template = new DFAgentDescription(); // Build the description used as
		// template for the search
		// templateSd = new ServiceDescription();
		// templateSd.setType("gameOfLfe");
		searchConstraints = new SearchConstraints(); // for controlling the
		// amount of messages
		// received
		searchConstraints.setMaxResults(new Long(500));
		// DFSearchAgent(template, sc);
		// registerMyselfDF( template ) ; // Register Agent in DF
		AMSSearchAgent(searchConstraints); // Search ALl Agents on platform
		manager = (ContentManager) getContentManager(); // manager for handling
		// content
		ontology = PlatformOntology.getInstance();
		getContentManager().registerLanguage(codec); // register Ontology
		// information and
		getContentManager().registerOntology(ontology); // activate codec for
		// decoding and coding
		// languages

		// start behavours of agent
		// ////////////////////////////////
		addBehaviour(new AMSSearchAgent(this)); // Search for Agents on platform
		// addBehaviour(new randomSelectAgents(this));
		
	}

	// Internal implementation methods CPU and Memory
	// ////////////////////////////////

	public double getCpuUserTime() {
		return cpuLoad.cpuPerc.getUser();
	}

	public double getCpuSystemTime() {
		// Avalable load for cpu
		double cpuLoadAvailable = 100 - cpuLoad.cpuPerc.getSys() * 100; 
		return cpuLoadAvailable;
	}

	public double getCpuIdelTime() {
		return cpuLoad.cpuPerc.getIdle();
	}

	public long getTotalMemory() {
		return cpuLoad.mem.getRam();
	}

	public long getFreeMemory() {
		return cpuLoad.mem.getFree()/1048576;
	}

	public long getUseMemory() {                   //Memory usage in MB
		return cpuLoad.mem.getUsed()/1048576;      //Memory usage in MB
	}

	public void setPlatformValues(){
		// PlatformA (PA)
		freeMemPA = (getFreeMemory()/2);           //container starts with 512RAM
		cpuFreePA = (getCpuSystemTime()/2);        //container starts with 50% of cpu
		
		// PlatformB (PB)
		freeMemPB = (getFreeMemory()/4);            //container starts with 256RAM
        cpuFreePB = (getCpuSystemTime()/4);         //container starts with 25% of cpu
        

		// PlatformC (PC)
		freeMemPC = (getFreeMemory()/4);           //container starts with 256RAM
        cpuFreePC = (getCpuSystemTime()/4);        //container starts with 25%cpu
		
	}

	// Internal implementation methods
	// ////////////////////////////////

	// current load of platform without Agents

	// current load of platform with Agents

	// Search for Agents on platform
	protected void AMSSearchAgent(SearchConstraints sc) {

		AMSAgentDescription[] results = null;

		try {
			if (sc != null) {

				allAgents = AMSService.search(this, new AMSAgentDescription(),
						sc);

			}
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}

		AMSSearchName = new String[allAgents.length];

		// Print out all agents on platform
		for (int i = 0; i < allAgents.length; i++) {

			AMSSearchName[i] = allAgents[i].getName().getLocalName();
			AID agentID = allAgents[i].getName();
		}

		this.AMSSearch = allAgents;

	}

	// change from grey to blue depending on current status
	public void ChangeStatus() {
		addBehaviour(new DoMoveAgent(this));
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
	

	// Create Neighbours of Agents
	protected Object[] setMyNeighbours(int r, int c, int length) {

		Object obj[] = new Object[11];

		// Add 8 Neighbours to every Agent

		if (r != 0){
			obj[0] = (r - 1) + "&" + c; // North
		}
		if (c != 0){
			obj[1] = r + "&" + (c - 1); // West
		}
		if (c != 0 && r!=length-1){
			obj[2] = (r + 1) + "&" + (c - 1); // South West
		}
		if (r != 0 && c != 0){
			obj[3] = (r - 1) + "&" + (c - 1); // North West
		}
		if (r != 0 && c!=length-1){
			obj[4] = (r - 1) + "&" + (c + 1); // North East
		}
		
		if(r!=length-1){
		obj[5] = (r + 1) + "&" + c; // South
		}
		
		if(c !=length-1){
		obj[6] = r + "&" + (c + 1); // East
		}
		
		if(r!=length-1&&c !=length-1){
		obj[7] = (r + 1) + "&" + (c + 1); // South East
		}
		
		obj[8] = r;
		obj[9] = c;
		obj[10] =this;

		return obj;

	}

	public void loadDistributor(int numberOfAgents, int countAllAgents, int totalAgents, 
		int maximumAgents[][]) {
		int countAgents = 0; // maximum amount of agents permited to be started
		int controlChanges = 0; // control if an agent has been created or not
		int Agents[][] = maximumAgents;

		
		// PlatformA (PA)
		//////////////////////////////////////////////////////////////////77
		long requestMemPA = (1/2) * numberOfAgents;           // requested amount of RAM
		long requestCpuPA = (1/8) * numberOfAgents;            // requested % of cpu

		// verify the level of cpu usage and memory available before starting
		// Agents on platform
		if (freeMemPA > requestMemPA && cpuFreePA > requestCpuPA
				&& numberOfAgents != 0) {

			for (int i = 0; i < maximumAgents.length; i++) {

				for (int j = 0; j < maximumAgents.length; j++) {

					if (Agents[i][j] != -1) {

						//set Neighbours of Agents
						Object obj[] = setMyNeighbours(i, j, maximumAgents.length);
						//create agent
							Application.JadePlatform.jadeAgentStart(i+"&"+j,
									"game_of_life.gameOfLifeAgent", obj);
							
						countAgents++;
						controlChanges++;
						countAllAgents++;

						// reduce Ram
						cpuFreePA -= requestCpuPA / numberOfAgents;
						freeMemPA -= requestMemPA / numberOfAgents;

						Agents[i][j] = -1; // prevent creating agents with the
						// same name

						if (countAgents == numberOfAgents) {
							i = maximumAgents.length;
							j = maximumAgents.length;

						}
					}
				}
			}

		}

		// PlatformB (PB)
		////////////////////////////////////////////
		long requestMemPB = (1/2) * numberOfAgents;     //requested amount of RAM 
		long requestCpuPB = (1/8) * numberOfAgents;      //requested % of cpu  
		countAgents = 0;

		// verify the level of cpu usage and memory available before starting
		// Agents on platform
		if (freeMemPB > requestMemPB && cpuFreePB > requestCpuPB
				&& numberOfAgents != 0) {

			for (int i = 0; i < maximumAgents.length; i++) {
				for (int j = 0; j < maximumAgents.length; j++) {

					if (Agents[i][j] != -1) {

						countAgents++;
						controlChanges++;
						countAllAgents++;

						// reduce Ram
						cpuFreePB -= requestCpuPB / numberOfAgents;
						freeMemPB -= requestMemPB / numberOfAgents;

						Agents[i][j] = -1; // prevent creating agents with the
						// same name

						//set Neighbours of Agents
						Object obj[] = setMyNeighbours(i, j, maximumAgents.length);
						
						//create agent
						Application.JadePlatform.jadeAgentStart(i+"&"+j,
									"game_of_life.gameOfLifeAgent",obj, "SubPlatformB");

						if (countAgents == numberOfAgents) {
							i = maximumAgents.length;
							j = maximumAgents.length;
						}
					}
				}
			}
		}

		// PlatformC (PC)
		//////////////////////////////////////////////
		long requestMemPC = (1/2) * numberOfAgents;    //requested amount of RAM 
		long requestCpuPC = (1/8) * numberOfAgents;     //requested % of cpu
		countAgents = 0;

		// verify the level of cpu usage and memory available before starting
		// Agents on platform
		if (freeMemPC > requestMemPC && cpuFreePC > requestCpuPC
				&& numberOfAgents != 0) {

			for (int i = 0; i < maximumAgents.length; i++) {
				for (int j = 0; j < maximumAgents.length; j++) {

					if (Agents[i][j] != -1) {

						//set Neighbours of Agents
						Object obj[] = setMyNeighbours(i, j, maximumAgents.length);
						
						//create agent
						Application.JadePlatform.jadeAgentStart(i+"&"+j,
									"game_of_life.gameOfLifeAgent", obj, "SubPlatformC");
						countAgents++;
						controlChanges++;
						countAllAgents++;

						// reduce Ram
						cpuFreePC -= requestCpuPC / numberOfAgents;
						freeMemPC -= requestMemPC / numberOfAgents;

						Agents[i][j] = -1; // prevent creating agents with the
						// same name

						if (countAgents == numberOfAgents) {
							i = maximumAgents.length;
							j = maximumAgents.length;
						}
					}
				}
			}
		}

		// restarting the recursive method
		if (countAllAgents < totalAgents && controlChanges == 0
				&& numberOfAgents != 0) {
			
			 //reducing the amount of Agents to be started at the same time => reducing the amounth of RAM
			
			numberOfAgents--;

			loadDistributor(numberOfAgents, countAllAgents,totalAgents, maximumAgents);
		}
		// restarting the recursive method
		if (countAllAgents < totalAgents && controlChanges != 0) {
			loadDistributor(numberOfAgents, countAllAgents, totalAgents, maximumAgents);
		}
        
		//Start game of life when all agents have been started
		if(countAllAgents==totalAgents){
           
			addBehaviour(new startGameOfLife(this, 1000));
			
		}
	}
	
	//Start game of life when all agents have been started
	public void startGameOfLife(final int pRow, final int pCol, final GameOfLifeObject controller [][]){
		
		         
                SwingUtilities.invokeLater(
				
				new Runnable() {
			
			public void run() {
				
				//run game of life framework on top of central GUI
				JInternalFrame iF = new GameOfLifeGUI(pRow, pCol, controller);
				JDesktopPane dF  =  Application.ProjectCurr.ProjectDesktop;
				dF.add(iF);
				
			}
		}
	   
	  );	
	}
	
	// Do move Agent when platform load limit has been reached.
	class DoMoveAgent extends Behaviour {

		private boolean finished = false;

		public DoMoveAgent(Agent a) {
			super(a);
		}

		public void action() {

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			// AID receiver = new AID("centerAgent", false);

			try {

				for (int i = 0; i < AMSSearchName.length; i++) {

					if (AMSSearchName[i] != null) {
						msg.addReceiver(new AID(AMSSearchName[i],
								AID.ISLOCALNAME));
					}
				}

				msg.setLanguage(codec.getName());
				msg.setOntology(ontology.getName());

				// Sample Messages to be sand

				PlatformInfo platformInfo = new PlatformInfo();
				platformInfo.setPlatformName(getAID().getName());
				platformInfo.setPlatformUrl("URL");
				platformInfo.setTypOfMessage("gameOfLive");
				platformInfo.setWhatToDo("DoMove");

				Platform comm = new Platform();
				comm.setPlatformInfo(platformInfo);

				// Fill the content of the message
				manager.fillContent(msg, comm);

				// Send the message
				send(msg);

			} catch (Exception e) {
				e.printStackTrace();
			}

			finished = true;
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return finished;
		}

	}

	// Search all agents on the platform
	class AMSSearchAgent extends Behaviour {

		private boolean finished = false;

		public AMSSearchAgent(Agent a) {
			super(a);
		}

		public void action() {

			AMSAgentDescription[] results = null;

			try {
				if (searchConstraints != null) {

					allAgents = AMSService.search(myAgent,
							new AMSAgentDescription(), searchConstraints);

				}
			} catch (Exception e) {
				//System.out.println("Problem searching AMS: " + e);
				e.printStackTrace();
			}

			AMSSearchName = new String[allAgents.length];

			// Print out all agents on platform
			for (int i = 0; i < allAgents.length; i++) {

				AMSSearchName[i] = allAgents[i].getName().getLocalName();
				AID agentID = allAgents[i].getName();
				// System.out.println(" Agent" + i + " : " + agentID.getName());
			}

			AMSSearch = allAgents;

			finished = true;
		}

		public boolean done() {
			return finished;
		}
	}

	class receiveCurrentLoad extends CyclicBehaviour {

		public receiveCurrentLoad(Agent a) {
			super(a);
		}

		public void action() {

			try {
				// Register to messages about topic "JADE"
				TopicManagementHelper topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				final AID topic = topicHelper.createTopic("GameOfLife");
				topicHelper.register(topic);
			} catch (Exception e) {
				//System.err.println("Agent " + getLocalName()
					//	+ ": ERROR registering to topic \"JADE\"");
				e.printStackTrace();
			}

			ACLMessage msg = receive();
			// myAgent.receive(MessageTemplate.MatchTopic(topic));

			if (msg != null) {
				//System.out.println("Agent " + myAgent.getLocalName()
					//	+ ": Message about topic , received. Content is "
						//+ msg.getContent());
			} else {
				block();
			}
		}
	}
	
private class startGameOfLife extends TickerBehaviour {

		public startGameOfLife(Agent a, int dt) {

			super(a, dt);
		
		}

		@Override
		protected void onTick() {
		
			
			
			for (int i = 0; i < gameOfLifeObject.length; i++) {
				
				for (int j = 0; j < gameOfLifeObject.length; j++) {
					
					if(gameOfLifeObject[i][j]==null){
						startGameOfLife=false;
					}
				}
			}
			
			if(startGameOfLife ==true && startOnlyOnce==0){

				startGameOfLife(pRow, pCol, gameOfLifeObject);
				
				
				//store coordinates
				Object [] obj = new Object[2];
				  obj[0] = pRow;
				  obj[1] = pCol;
					
				//Start Agent for controlling Game of Life
				  Application.JadePlatform.jadeAgentStart("AgentController",
						"game_of_life.agent_controller.agentController", obj);
				
				startOnlyOnce = 1;
				
			}
			startGameOfLife = true;
		}
	}

	private class broadcastCurrentLoad extends TickerBehaviour {

		AID topic;
		TopicManagementHelper topicHelper;

		public broadcastCurrentLoad(Agent a, int dt) {

			super(a, dt);

			try {

				// Periodically send messages about topic "JADE"
				topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				topic = topicHelper.createTopic("GameOfLife");

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected void onTick() {

			//System.out.println("Agent " + myAgent.getLocalName()
				//	+ ": Sending message about topic " + topic.getLocalName());
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(topic);
			msg.setContent(String.valueOf(getTickCount()));
			myAgent.send(msg);
						
		}
	}
}
