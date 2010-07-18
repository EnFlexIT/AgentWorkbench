package game_of_life.agents;

import java.lang.management.ManagementFactory;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.PlatformID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import game_of_life.agent_distributor.loadDistributorAgent;
import game_of_life.gui.GameOfLifeObject;
import game_of_life.gui.GameOfLifeGUI;
import game_of_life.ontology.Platform;
import game_of_life.ontology.PlatformInfo;
import game_of_life.ontology.PlatformOntology;

public class gameOfLifeAgent extends Agent {

	// Constants
	// ////////////////////////////////

	// Static variables
	// ////////////////////////////////

	private long threadId;

	// Instance variables
	// ////////////////////////////////
	private int myCurrentState;
	private int myNextState;
	private int pRow, pCol; // Agent's position
	private int recievedMessages; // total number of messages received by agent
	private int counterNeighbour; // total number of neighbours
	private int neighbourAlive; // Total number of neighbours alive
	// Constructors
	// ////////////////////////////////
	private GameOfLifeObject gol = null; // an object of Agents controler

	// DF-Information
	private DFAgentDescription templateDf;
	private ServiceDescription templateSd;
	private SearchConstraints sc;
	private ContentManager manager; // We handle contents
	private Codec codec; // This agent speaks the SL language
	private Ontology ontology; // This agent complies with the People ontology

	private int portNumber;
	private String typOfMessage;
	private String whatToDo;
	private String platformName;
	private String platformUrl;
	private int currentState;

	// Topic Variables
	// ////////////////////////////////
	// Register to messages about topic "JADE"
	private TopicManagementHelper topicHelper;
	private AID startGame;
	private AID changeStatus;
	private AID registerTopics;
	private AID createTopics[];
	private Object[] neighbour; // array for storing neighbour

	protected void setup() {

		// extract my neighbours
		neighbour = getArguments();

		recievedMessages = 0;

		neighbourAlive = 0;
		myCurrentState = 0;
		myNextState = 0;

		try {
			// Register to messages about topic "JADE"
			TopicManagementHelper topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			startGame = topicHelper.createTopic("startGameOfLife");
			changeStatus = topicHelper.createTopic("changeStatus");
			topicHelper.register(startGame);
			topicHelper.register(changeStatus);

			// messages b/w game of life agents
			// create topic about my neighbours and wait for messages from them
			createTopics = new AID[8];
			for (int i = 0; i < 8; i++) {
				if (neighbour[i] != null) {
					createTopics[i] = topicHelper
							.createTopic((String) neighbour[i]);
					topicHelper.register(createTopics[i]);
				}

			}

			// send message to my neiboughs under this topic name
			registerTopics = topicHelper.createTopic(getLocalName());

		} catch (Exception e) {
			System.err.println("Agent " + getLocalName()
					+ ": ERROR registering to topic \"JADE\"");
			e.printStackTrace();
		}

		try {

			// index of Agent's name at possition 0
			pRow = (Integer) neighbour[8];

			// index of Agent's name at possition 2
			pCol = (Integer) neighbour[9];

			Thread.sleep(500);

		} catch (Exception e) {
			System.out.println(" Problem " + e.getMessage());
		}

		// System.out.println(" Coordinates row: "+pRow+" Col "+pCol);

		gol = new GameOfLifeObject(this); // an object of Agents controler
											// created
		loadDistributorAgent.gameOfLifeObject[pRow][pCol] = gol; // store
																	// created
																	// object in
																	// an array

		// Constructors
		// ////////////////////////////////
		manager = (ContentManager) getContentManager(); // manager for handling
														// content
		codec = new SLCodec();
		ontology = PlatformOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// start internal implementation methods
		// ////////////////////////////////
		counterNeighbour = setAmountOfNeighbours(); // store total number of
													// neibours agent is having

		// start behavours of agent
		// ////////////////////////////////
		addBehaviour(new messageListener(this)); // behavour for listening
													// incoming messages

	}

	// Internal implementation methods
	// ////////////////////////////////

	// set the total amount of neighbours an agent is having
	public int setAmountOfNeighbours() {

		int count = 0;

		for (int i = 0; i < 8; i++) {
			if (neighbour[i] != null) {

				count++;
			}
		}
		return count;

	}

	// reset all variables used during the game of life
	public void resetAll() {

		myCurrentState = 0;
		myNextState = 0;
		neighbourAlive = 0;
		gol.setBackground(0);

	}

	// Register the game of life service
	public void DFRegisterAgent(DFAgentDescription dfd) {

		try {

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
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

	// update my state after controlling my neighbours
	public void updateStateOfAgent() {

		int nbAlive = getNeighbourAlive();

		if (myCurrentState == 1) { // if alive

			if (nbAlive < 2) {// 1.Any live cell with fewer than two live
				// neighbours dies
				myNextState = 0;
			}
			if (nbAlive > 3) { // 2.Any live cell with more than three live
				// neighbours dies
				myNextState = 0;
			}
			if (nbAlive == 2) {
				myNextState = 1;
			}
		}
		// Agent not Alive
		else {
			if (nbAlive == 3) { // 4.Any dead cell with exactly three live
				// neighbours becomes a live cell
				myNextState = 1;
			}
		}
	}

	// final update the state of agent (e.g grey to blue)
	void updateAgent() {

		// do the test to avoid re-setting same color for nothing
		if (myCurrentState != myNextState) {

			myCurrentState = myNextState;
			gol.setBackground(myCurrentState);
			setMyCurrentState(myCurrentState);

		} else {
			gol.setBackground(myCurrentState);
		}
		// reset Variables
		neighbourAlive = 0;
		gol.setFinished(true);
		recievedMessages = 0;
	}

	public void setMyCurrentState(int myS) {

		this.myCurrentState = myS;
		// gol.setBackground(myCurrentState);
	}

	public int getMyCurrentState() {
		return myCurrentState;
	}

	public void setMyNextState(int myNS) {

		this.myNextState = myNS;
		// gol.setBackground(myCurrentState);
	}

	public int getMyNextState() {
		return myNextState;
	}

	// Control how many active Neighbours are around me
	public void setNeighbourAlive(int y) {

		this.neighbourAlive = this.neighbourAlive + y;

	}

	// Control how many active Neighbours are around me
	public int getNeighbourAlive() {

		return neighbourAlive;

	}

	// broadcasting current state of Agent to neighbours
	public void broadcastMyStateNN() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		// AID receiver = new AID("centerAgent", false);

		try {

			for (int i = 0; i < 8; i++) {

				if (neighbour[i] != null) {

					msg.addReceiver(new AID((String) neighbour[i],
							AID.ISLOCALNAME));

				}
			}

			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());

			// Sample Messages to be sand

			PlatformInfo platformInfo = new PlatformInfo();
			platformInfo.setPlatformName(getAID().getName());
			platformInfo.setPlatformUrl("URL");
			platformInfo.setTypOfMessage("gameOfLife");
			platformInfo.setWhatToDo("myCurrentState");
			platformInfo.setCurrentState(myCurrentState);
			platformInfo.setNextState(0);

			Platform comm = new Platform();
			comm.setPlatformInfo(platformInfo);

			// Fill the content of the message
			manager.fillContent(msg, comm);

			// Send the message
			send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// verify if AgentName is one of my neighbours
	protected boolean checkIfNeighbour(String AgentName) {

		boolean foundIt = false;

		for (int i = 0; i < 8; i++) {

			if (AgentName.equals(neighbour[i])) {

				foundIt = true;
			}
		}
		return foundIt;
	}

	class messageListener extends CyclicBehaviour {

		public messageListener(Agent a) {
			super(a);
		}

		public void action() {
			//collect messages from the corresponding topics
			ACLMessage msgA = myAgent.receive(MessageTemplate
					.MatchTopic(startGame));
			ACLMessage msgB = myAgent.receive(MessageTemplate
					.MatchTopic(changeStatus));

			ACLMessage msg[] = new ACLMessage[8];

			for (int i = 0; i < 8; i++) {
				if (createTopics[i] != null)
					msg[i] = myAgent.receive(MessageTemplate
							.MatchTopic(createTopics[i]));

			}

			if (msgA != null) {
				// ACL Message to start game of life from Agent controller
				gol.setFinished(false);

				GameOfLifeGUI.counterAgent++;

				// all Agents broadcast their current state
				addBehaviour(new BroadcastCurrentState(myAgent));

				// System.out.println("AgentName: "+getLocalName()+", total amount of message from AC = "+PerformAction.counterAgent);
			} else if (msgB != null) {
				// /ACL Message to update game of life from Agent controller
				updateAgent();

			} else if (checkMessage(msg)) {
				//not really needed
			}

			else {
				block();
			}
		}

		//Controle if the message is from my neighbours
		private boolean checkMessage(ACLMessage[] msg) {

			boolean foundIt = false;
			for (int i = 0; i < msg.length; i++) {
				if (msg[i] != null) {
					try {

						String AgentName = msg[i].getSender().getLocalName();

						if (checkIfNeighbour(AgentName)) {
							//get the content if the message. use it to 
							//change the state of the Agent
							if (msg[i].getContent().equals("true")) {
								setNeighbourAlive(1);
							} else {
								setNeighbourAlive(0);
							}
							recievedMessages++;

							if (recievedMessages == counterNeighbour) {
								//updating state of agent after recieving
								//messages from all it's neighbours
								updateStateOfAgent();

							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}

			return false;
		}
	}

	// broadcast current state of Agent to neighbours
	class BroadcastCurrentState extends Behaviour {

		boolean finished = false;

		public BroadcastCurrentState(Agent a) {
			super(a);
		}

		public void action() {

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(registerTopics);
			if (getMyCurrentState() == 1) {
				msg.setContent("true");
			} else {
				msg.setContent("false");
			}

				send(msg);

			finished = true;
		}

		public boolean done() {
			return finished;
		}
	}
}