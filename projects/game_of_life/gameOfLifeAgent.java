package game_of_life;

import java.lang.management.ManagementFactory;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.PlatformID;
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
	
	// Instance variables for CPU
	// ////////////////////////////////
	private long cpuUserTime;
	private long cpuTime;
	private long threadId;
	
	// Instance variables
	// ////////////////////////////////
	private int myCurrentState;
	private int myNextState;
	private int pRow, pCol;                      // Agent's position
	private int recievedMessages;                //total number of messages received by agent
	private int counterNeighbour;                //total number of neighbours
	private int neighbourAlive;                  //Total number of neighbours alive
    private long currentCpuUsage;                //Total CPU usage of this Agent 
    private long currentMemorryUsage;            //Total Memory usage of this Agent
    
	// Constructors
	// ////////////////////////////////
	private GameOfLifeObject gol = null;           // an object of Agents controler
	
	// DF-Information
	private DFAgentDescription templateDf;
	private ServiceDescription templateSd;
	private SearchConstraints sc;
	private ContentManager manager;                 // We handle contents
	private Codec codec;                            // This agent speaks the SL language
	private Ontology ontology;                      // This agent complies with the People ontology
	
	private int portNumber;
	private String typOfMessage;
	private String whatToDo;
	private String platformName;
	private String platformUrl;
	private int currentState;

	  // Array Variables
    //////////////////////////////////
	private Object[] neighbour;                     // array for storing neighbour
	

	protected void setup() {

		// extract my neighbours
		neighbour = getArguments();
		
		recievedMessages = 0;

		// Initialising DF Information
		templateDf = new DFAgentDescription();
		templateSd = new ServiceDescription();
		templateSd.setName("loadbalance");
		templateSd.setType("gameOfLife");
		templateDf.addServices(templateSd);
		
		// Register the service
		DFRegisterAgent(templateDf);
		
		neighbourAlive = 0;
		myCurrentState = 0;
		myNextState = 0;
		currentCpuUsage=0;
		currentMemorryUsage=0;
		cpuUserTime =0;
		cpuTime =0;
		
		
		try {
			
			// index of Agent's name at possition 0
			pRow = (Integer) neighbour[8];

			// index of Agent's name at possition 2
			pCol = (Integer) neighbour[9];
			

			Thread.sleep(500);
			
		} catch (Exception e) {
			//System.out.println(" Problems with co ordinates "+e.getMessage());
		}
		
		gol = new GameOfLifeObject(this);                         // an object of Agents controler created
		loadDistributorAgent.gameOfLifeObject[pRow][pCol] = gol;                   // store created object in an array
		
	    
        // Constructors
        //////////////////////////////////
		
		manager = (ContentManager) getContentManager();          // manager for handling content
		
		codec = new SLCodec(); 
		ontology = PlatformOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// start internal implementation methods
        //////////////////////////////////
		
		counterNeighbour = setAmountOfNeighbours();               //store total number of neibours agent is having
		
		// start behavours of agent
		//////////////////////////////////
		addBehaviour(new commandListener(this));                  // behavour for listening incoming messages
		
		//addBehaviour(new currentLoadOfAgent(this , 10000));   //Send load every 1min

	}
	
	
	 // Internal implementation methods
    //////////////////////////////////
	
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public void setCpuUserTime(long cpuUserTime) {
		this.cpuUserTime = cpuUserTime;
	}
	public void setCpuTime(long cpuTime) {
         this.cpuTime = cpuTime;		
	}
	
	public long setThreadId() {
		return threadId;
	}

	public long setCpuUserTime() {
		return cpuUserTime;
	}
	public long setCpuTime() {
         return cpuTime;		
	}
	
	//set the total amount of neighbours an agent is having
	public int setAmountOfNeighbours(){
		
		int  count =0;
		
		for (int i = 0; i < 8; i++) {
			if(neighbour[i]!=null){
				
				count++;
			}
		}
		return count;
		
	}
	
	//reset all variables used during the game of life
    public void resetAll(){
    	
    	myCurrentState =0;
    	myNextState = 0;
    	neighbourAlive =0;
    	gol.setBackground(0);
    	
    }	

 // Register the game of life service
	public void DFRegisterAgent(DFAgentDescription dfd){
		
	  	try {
	  	
	  		DFService.register(this, dfd);
	  	}
	  	catch (FIPAException fe) {
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
			if(nbAlive==2){
				myNextState = 1;
			}
		}
           //Agent not Alive
		else {
			if (nbAlive == 3) { // 4.Any dead cell with exactly three live
				// neighbours becomes a live cell
				myNextState = 1;
			}
		}
	}

	//final update the state of agent (e.g grey to blue)
	void updateAgent() {

   // do the test to avoid re-setting same color for nothing
		if (myCurrentState != myNextState) {
			
			myCurrentState = myNextState;
			gol.setBackground(myCurrentState);
			setMyCurrentState(myCurrentState);
			
		}
		else{
		gol.setBackground(myCurrentState);
		}
		//reset Variables
		neighbourAlive = 0;
		gol.setFinished(true);
		
	}

	public void setMyCurrentState(int myS) {

		this.myCurrentState = myS;
		//gol.setBackground(myCurrentState);
	}

	public int getMyCurrentState() {
		return myCurrentState;
	}

	public void setMyNextState(int myNS) {

		this.myNextState = myNS;
		//gol.setBackground(myCurrentState);
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


	//broadcasting current state of Agent to neighbours
	public void broadcastMyState() {

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

	
	class commandListener extends CyclicBehaviour {

		public commandListener(Agent a) {

			super(a);
		}

		public void action() {
			
			 
			try {
				ACLMessage msg = receive();

				if (msg != null) {

					String AgentName = msg.getSender().getLocalName();

					switch (msg.getPerformative()) {

					case ACLMessage.INFORM:

						ContentManager cm = getContentManager();
						Platform pc = (Platform) cm.extractContent(msg);
						PlatformInfo pi = (PlatformInfo) pc.getPlatformInfo();

						platformName = pi.getPlatformName();
						platformUrl = pi.getPlatformUrl();
						portNumber = pi.getPortNumber();
						typOfMessage = pi.getTypOfMessage();
						whatToDo = pi.getWhatToDo();
						currentState = pi.getCurrentState();
						// nextState = pi.getNextState();


						if(pi.getTypOfMessage().equals("gameOfLife")
								&& pi.getWhatToDo().equals("myCurrentState")){
							
							//control to be sure if the message is from Agent's neighbour
							if(checkIfNeighbour(AgentName)){
								
								//set number of neighbours alive
								//if(pi.getCurrentState()==1)
									setNeighbourAlive(pi.getCurrentState());
                                   
									recievedMessages++;
									GameOfLifeGUI.totalMessagesReceived++;
									
									//Agent has recieve messages from all it's neighbouts so it can change it's state
									if(recievedMessages==counterNeighbour){

										//Updating State of Agent
										recievedMessages=0;
										updateStateOfAgent();
										
									}
							}
							
						}
						else if (pi.getTypOfMessage().equals("gameOfLife")
								&& pi.getWhatToDo().equals("startGameOfLife")) {
							    
							    // Set state of agent to be not finish
							    gol.setFinished(false);
							    
								GameOfLifeGUI.counterAgent++;
								
								// all Agents broadcast their current state
								broadcastMyState();
								
						}

						 else if (pi.getTypOfMessage().equals("gameOfLife")
								&& pi.getWhatToDo().equals("ChangeStatus")) {
							
							// Change the status of the Agent( e.g grey to blue)
							updateAgent();
							
							//System.out.println("AgentName: "+getLocalName()+", Number Of Neighbours = "+neighbourAlive+"");
							neighbourAlive =0;
							//System.out.println("===================================================================================");
							
						}
						 else if (pi.getTypOfMessage().equals("gameOfLife")
									&& pi.getWhatToDo().equals("DoMove")) {
								    
								    // moving Agent to a different container
							 //System.out.println("AgentName: "+getLocalName()+"  moving Agent to a different container ");
							 
							 addBehaviour(new MovingAgent(myAgent));
							 
							}


					case ACLMessage.REQUEST:
						
						break;

					default:
					}
				}

			} catch (Exception e) {

			}
		}
	}
private class currentLoadOfAgent extends TickerBehaviour {

	
	AID topic;
	TopicManagementHelper topicHelper;
	
	public currentLoadOfAgent(Agent a, int dt) {

		super(a, dt);
	 
		try {
		
		// Periodically send messages about topic "JADE"
		topicHelper = (TopicManagementHelper)getHelper(TopicManagementHelper.SERVICE_NAME);
		topic = topicHelper.createTopic("currentLoadOfAgent");
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onTick() {
		
		//System.out.println("AgentName "+myAgent.getLocalName()+": Sending message about topic "+topic.getLocalName());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(topic);
		
		//Load of Agent on Platform
		cpuUserTime = ManagementFactory.getThreadMXBean().getCurrentThreadUserTime();
		setCpuUserTime(cpuUserTime);
		cpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
		setCpuTime(cpuTime);
		threadId = Thread.currentThread().getId();
		setThreadId(threadId);
		msg.setContent(String.valueOf(" "+getTickCount()+" CpuTime: "+cpuTime+" Thread Id = "+threadId));
		myAgent.send(msg);
		
	}

}
private class MovingAgent extends SimpleBehaviour{
	
	private boolean _done;
	private int _state;
	public PlatformID dest;

public MovingAgent(Agent a){
	
    super(a);
    _state = 0;
    _done = false;
    
}

public void beforeMove(){
	
	System.out.println("Moving now to location : " + dest.getName());
}

public void afterMove(){
	
	System.out.println("Arrived at location : " + dest.getName());
}
   
public void action(){
	
    switch(_state){
    
    case 0:
    	System.out.println("Moving to a new Platform ");
        
      	AID a = new AID("amm@thinka00:1099/JADE",true);
    	a.addAddresses("http://thinka00:7778/acc");
    	
    	dest = new PlatformID(a);
    	_state++;
    	
    	beforeMove();

    	myAgent.doMove(dest);
    	
    	break;
    	
    case 1:
    	System.out.println("I arrived savely, thanks...");
    	
    	afterMove();
    	_done = true;
    	System.out.println("Its the end, dying4...");
    	
    	//send(msg2);
    	
    	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	//myAgent.doDelete();
    }
}

public boolean done(){
    return _done;
}


}
}