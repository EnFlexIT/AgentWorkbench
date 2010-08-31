package game_of_life.LBC;

import game_of_life.agents.GameOfLifeAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import application.Application;
import game_of_life.ontology.Platform;
import game_of_life.ontology.PlatformInfo;
import game_of_life.ontology.PlatformOntology;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.mobility.*;

public class CenterDynamicLoadBalancingCoodinator extends Agent {

	private static final long serialVersionUID = 1L;
	//	---- Arraylist of containers for storing agents ----------------------------
	private ArrayList<String> MainContainer = new ArrayList<String>();
	private ArrayList<String> Container1 = new ArrayList<String>();
	private ArrayList<String> Container2 = new ArrayList<String>();
	private ArrayList<String> Container3 = new ArrayList<String>();
	
	// ----- Hash table for storing Container location --------------------------------------
	private Map<String, Location>  locations = new HashMap<String, Location>();
	
	//------------- set GUI co-ordinates -------------------------------------------
	private int cRow;
	private int cCol;
	private int totalAgents;
	
	// --------------- objects variables -------------------------------------------
	private Object coordinate[];
	
	// ----- Ontology variables ----------------------------------------------------
	private ContentManager manager;
	private ContentManager managerMobile;
	private Codec codec;
	private Ontology ontology;

	// ---- variables remote container CPU & Memory --------------------------------
	private long maximumMemory;
	private double maximumCPU;

	// -------- CPU & JVM & Threshold ----------------------------------------------
	private boolean cpuAndMemThresholdExceeded;
	private double currentCpuIdleTime;
	private long currentJvmMemoFree;

	// --- remote container URL & name
	// -----------------------------------------------------------------------------
	private String remoteContainerName;

	@Override
	protected void setup() {
		
		
		// ----- get the arguments of agents ---------------------------------------
		coordinate = getArguments();
		
		// ----- create coordinates of Agents --------------------------------------
		cRow = (Integer) coordinate[0];
		cCol = (Integer) coordinate[1];
		totalAgents = cRow*cCol;
		
		// ---- Ontology, coding and decoding language ------------------------------
		manager = getContentManager();
		managerMobile = getContentManager(); 
		codec = new SLCodec();
		ontology = PlatformOntology.getInstance();
		manager.registerLanguage(codec);
		manager.registerOntology(ontology);
		managerMobile.registerOntology(MobilityOntology.getInstance());
		
		 // ---- Get available locations with AMS -------------------------------------
	     sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));

	     //Receive response from AMS
	     receiveResponseFromAMS();

	     setMaximumCPU(0);
	     setMaximumMemory(0);

	     //addBehaviour(new currentLoadOfAgent(this, 2000));
	     addBehaviour(new receiverLoadOfContainer(this));
	     
	     
		//---- Start remote dynamic load balancing coordinator ------------------------ 
		 //createDynamicLoadBalancingCoordinators();
	    //---- start and distribute game of life Agents -------------------------------
	     createGameOfLifeAgents(cRow, cCol);

	}


	// ----send request for migration to the an Agent ----------------------
	 private  void sendRequest(Action action) {

		      ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		      request.setLanguage(new SLCodec().getName());
		      request.setOntology(MobilityOntology.getInstance().getName());
		      try {
			     getContentManager().fillContent(request, action);
			     request.addReceiver(action.getActor());
			     send(request);
			  }
			  catch (Exception ex) { ex.printStackTrace(); }
		   }

	private void receiveResponseFromAMS() {

		try {

			MessageTemplate mt = MessageTemplate.and(
					MessageTemplate.MatchSender(getAMS()),
					MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage resp = blockingReceive(mt);
			ContentElement ce = getContentManager().extractContent(resp);
			Result result = (Result) ce;
			jade.util.leap.Iterator it = result.getItems().iterator();
			
			// ---- store location on a hash table ------------------------------------
			while (it.hasNext()) {
				Location loc = (Location) it.next();
				locations.put(loc.getName(), loc);
				System.out.println(loc.getName());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	// ----- create dynamic load balancing coordinator --------------------------------- 
	private void createDynamicLoadBalancingCoordinators() {
		
		Object obj[] = new Object[0];
		obj[0] = getLocalName();
		// ---- mainContainer -----------------------------------------------------------
		Application.JadePlatform.jadeAgentStart("DLBC-MainContainer", DynamicLoadBalancingCoodinator.class.getName(), obj);
		
		// ---- Remote Container-1 ------------------------------------------------------
		Application.JadePlatform.jadeAgentStart("DLBC-Container1", DynamicLoadBalancingCoodinator.class.getName(), obj);
				//------ Migrate DLBC-Container1 to Container-1 -------------------------
		doWait(500);
		//------ Migrate Agent to Container-2 -----------------------------------------------
		 AID aid = new AID("DLBC-Container1", AID.ISLOCALNAME);
		 String destName = "Container-1";
         Location dest = (Location)locations.get(destName);
         MobileAgentDescription mad = new MobileAgentDescription();
         mad.setName(aid);
         mad.setDestination(dest);
         MoveAction ma = new MoveAction();
         ma.setMobileAgentDescription(mad);
         sendRequest(new Action(aid, ma));
		
		// ---- Remote Container-2 -----------------------------------------------------------
		Application.JadePlatform.jadeAgentStart("DLBC-Container2", DynamicLoadBalancingCoodinator.class.getName(), obj);
				//------ Migrate DLBC-Container1 to Container-2 ------------------------------
		doWait(500);
		//------ Migrate Agent to Container-2 ------------------------------------------------
		 AID aid2 = new AID("DLBC-Container2", AID.ISLOCALNAME);
		 String destName2 = "Container-2";
         Location dest2 = (Location)locations.get(destName2);
         MobileAgentDescription mad2 = new MobileAgentDescription();
         mad2.setName(aid2);
         mad.setDestination(dest2);
         MoveAction ma2 = new MoveAction();
         ma2.setMobileAgentDescription(mad2);
         sendRequest(new Action(aid2, ma2));
         
     	// ---- Remote Container-3 -----------------------------------------------------------
 		Application.JadePlatform.jadeAgentStart("DLBC-Container3", DynamicLoadBalancingCoodinator.class.getName(), obj);
 				//------ Migrate DLBC-Container1 to Container-2 ------------------------------
 		doWait(500);
 		//------ Migrate Agent to Container-3 ------------------------------------------------
 		 AID aid3 = new AID("DLBC-Container3", AID.ISLOCALNAME);
 		 String destName3 = "Container-3";
          Location dest3 = (Location)locations.get(destName2);
          MobileAgentDescription mad3 = new MobileAgentDescription();
          mad2.setName(aid3);
          mad.setDestination(dest3);
          MoveAction ma3 = new MoveAction();
          ma3.setMobileAgentDescription(mad3);
          sendRequest(new Action(aid3, ma3));
         
	}

	//----------- start agents for the game of life ----------------------------------------
	private void createGameOfLifeAgents(int cRow, int cCol){
		
		int counter =0;
		for (int i = 0; i < cRow; i++) {
			for (int j = 0; j < cCol; j++) {
				//----------- arguments for objects to use ---------------------------------
				
				Object obj[] = new Object[5];
				obj[0] = i;
				obj[1] = j;
				obj[2] = cRow;
				obj[3] = cCol;
				obj[4] = getAID();
				String agentName = i+"&"+j;	
				
				//mainContainer
				if(totalAgents/4>counter){
				Application.JadePlatform.jadeAgentStart(agentName, GameOfLifeAgent.class.getName(), obj);
				
				MainContainer.add(agentName);
				}
				// ---- Container-1: create new agent ---------------------------------------------
				if(totalAgents/4<=counter &&totalAgents*1/2>counter){
				Application.JadePlatform.jadeAgentStart(agentName, GameOfLifeAgent.class.getName(), obj);
				Container1.add(agentName);
				
				doWait(50);
				//------ Migrate Agent to Container-1 -----------------------------------------------
				 AID aid = new AID(agentName, AID.ISLOCALNAME);
				 String destName = "Container-1";
		         Location dest = (Location)locations.get(destName);
		         MobileAgentDescription mad = new MobileAgentDescription();
		         mad.setName(aid);
		         mad.setDestination(dest);
		         MoveAction ma = new MoveAction();
		         ma.setMobileAgentDescription(mad);
		         sendRequest(new Action(aid, ma));
		         
				}
				//Container-2
				if(totalAgents*1/2<=counter &&totalAgents*3/4>counter){
				Application.JadePlatform.jadeAgentStart(agentName, GameOfLifeAgent.class.getName(), obj);
				Container2.add(agentName);
				doWait(50);
				//------ Migrate Agent to Container-2 -----------------------------------------------
				 AID aid = new AID(agentName, AID.ISLOCALNAME);
				 String destName = "Container-2";
		         Location dest = (Location)locations.get(destName);
		         MobileAgentDescription mad = new MobileAgentDescription();
		         mad.setName(aid);
		         mad.setDestination(dest);
		         MoveAction ma = new MoveAction();
		         ma.setMobileAgentDescription(mad);
		         sendRequest(new Action(aid, ma));
				}
				
				//Container-3
				if(totalAgents*3/4<=counter){
				Application.JadePlatform.jadeAgentStart(agentName, GameOfLifeAgent.class.getName(), obj);
				Container2.add(agentName);
				doWait(50);
				//------ Migrate Agent to Container-3 -----------------------------------------------
				 AID aid = new AID(agentName, AID.ISLOCALNAME);
				 String destName = "Container-3";
		         Location dest = (Location)locations.get(destName);
		         MobileAgentDescription mad = new MobileAgentDescription();
		         mad.setName(aid);
		         mad.setDestination(dest);
		         MoveAction ma = new MoveAction();
		         ma.setMobileAgentDescription(mad);
		         sendRequest(new Action(aid, ma));
				}
				counter++;
			}
		}
		System.out.println(" Finished "+counter);
	}

	/**
	 * @param maximumMemory
	 *            the maximumMemory to set
	 */
	public void setMaximumMemory(long maximumMemory) {
		this.maximumMemory = maximumMemory;
	}
	/**
	 * @return the maximumMemory
	 */
	public long getMaximumMemory() {
		return maximumMemory;
	}
	/**
	 * @param maximumCPU
	 *  the maximumCPU to set
	 */
	public void setMaximumCPU(long maximumCPU) {
		this.maximumCPU = maximumCPU;
	}

	/**
	 * @return the maximumCPU
	 */
	public double getMaximumCPU() {
		return maximumCPU;
	}
	/**
	 * @param remoteContainerName the remoteContainerName to set
	 */
	public void setRemoteContainerName(String remoteContainerName) {
		this.remoteContainerName = remoteContainerName;
	}


	/**
	 * @return the remoteContainerName
	 */
	public String getRemoteContainerName() {
		return remoteContainerName;
	}
	// --- behavour perminently receive information about container load from
	// remote LBC ------------
	class receiverLoadOfContainer extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public receiverLoadOfContainer(Agent agent) {

			super(agent);
		}

	public void action() {

			try {

				// ------ Register to messages about topic
				// "currentLoadOfContainer" ----------------

				ACLMessage msg = myAgent.blockingReceive();

				if (msg != null) {

					String AgentName = msg.getSender().getLocalName();
					
						// Remote containers: compare free memory and migrate to remote
						// -----  Decoding remote message ------------------------------------
						Platform platform = (Platform) manager
								.extractContent(msg);
						PlatformInfo platformInfo = (PlatformInfo) platform
								.getPlatformInfo();
						

						float tempMaximumMemory = platformInfo
								.getCurrentFreeMemory();
						double tempMaximumCPU = platformInfo
								.getCurrentCpuIdleTime();
						
						// --- store remote container with enough space for new Agents --------
						if (maximumMemory < tempMaximumMemory
								&& maximumCPU < tempMaximumCPU) {
							
							setMaximumCPU((long) tempMaximumCPU);
							setMaximumMemory((long) tempMaximumMemory);
							setRemoteContainerName(platformInfo.getRemoteContainerName());
						}

						// ---- Threshold value. CPU & Memory available ---------------------------
						boolean tempThresholdExceeded = platformInfo.getThresholdExceeded();

						if (tempThresholdExceeded) {
							// ---- migrate Agents to reduce workload on container ----------------
							Location migrateTo = (Location) locations.get(getRemoteContainerName());
							String migrateFrom = platformInfo.getRemoteContainerName();
							
							//migrateAgents(migrateTo, migrateFrom);
							
							// ---- reset list of the hash table ----------------------------------
							
						}
						
						System.out.println(" receiver: " + getLocalName()
								+ " remoteContainerName: "
								+ " RemoteContenerURL: "
							    + " maxMemory: "
								+ maximumMemory + " maxCPU: " + maximumCPU+" Content: "+msg.getContent());
				}

			} catch (Exception e) {

			}
		}

	private void migrateAgents(Location migrateTo, String migrateFrom) {
		
		if(migrateFrom.equals("MainContainer")){
			 
			 // ------ reset Arraylist of Agents ---------------------------------
			 String AgentName = MainContainer.get(0);
			 MainContainer.remove(0);
			 String newLocationName = migrateTo.getName(); 
			 resetListOfAgents(newLocationName, AgentName);
			 
			 AID aid = new AID(AgentName, AID.ISLOCALNAME);
	         MobileAgentDescription mad = new MobileAgentDescription();
	         mad.setName(aid);
	         mad.setDestination(migrateTo);
	         MoveAction ma = new MoveAction();
	         ma.setMobileAgentDescription(mad);
	         sendRequest(new Action(aid, ma));
			
		}
		else if(migrateFrom.equals("Container1")){
			
			 // ------ reset Arraylist of Agents ---------------------------------
			 String AgentName = Container1.get(0);
			 Container1.remove(0);
			 String newLocationName = migrateTo.getName(); 
			 resetListOfAgents(newLocationName, AgentName);
			 
			 AID aid = new AID(AgentName, AID.ISLOCALNAME);
	         MobileAgentDescription mad = new MobileAgentDescription();
	         mad.setName(aid);
	         mad.setDestination(migrateTo);
	         MoveAction ma = new MoveAction();
	         ma.setMobileAgentDescription(mad);
	         sendRequest(new Action(aid, ma));
			
		}
		else if(migrateFrom.equals("Container2")){
			
			 // ------ reset Arraylist of Agents ---------------------------------
			 String AgentName = Container2.get(0);
			 Container2.remove(0);
			 String newLocationName = migrateTo.getName(); 
			 resetListOfAgents(newLocationName, AgentName);
			 
			 AID aid = new AID(AgentName, AID.ISLOCALNAME);
	         MobileAgentDescription mad = new MobileAgentDescription();
	         mad.setName(aid);
	         mad.setDestination(migrateTo);
	         MoveAction ma = new MoveAction();
	         ma.setMobileAgentDescription(mad);
	         sendRequest(new Action(aid, ma));
			
		}
		
	}

	private void resetListOfAgents(String newLocationName, String agentName) {
		
		if(newLocationName.equals("MainContainer")){
			 
			MainContainer.add(agentName);
		}
		else if(newLocationName.equals("Container1")){
			
			Container1.add(agentName);
		}
		else if(newLocationName.equals("Container2")){
			
			Container2.add(agentName);
			
		}
	}
	}
}
