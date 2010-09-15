package game_of_life.LBC;

import game_of_life.agents.GameOfLifeAgent;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
import mas.service.distribution.ontology.PlatformLoad;
import network.JadeUrlChecker;
import application.Application;

public class CenterDynamicLoadBalancingCoodinator extends Agent {

	private static final long serialVersionUID = 1L;
	// ----- Hash table for storing Container location -----------------------------
	private Map<String, Location>  locations = new HashMap<String, Location>();
	private Map<String, String>  totalNumberOfAgents = new HashMap<String, String>();
	// -------store valures --------------------------------------------------------
	
	// ---- variables for delay ----------------------------------------------------
	String DLBC_Created = null;
	
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
	private int maxNumberOfThread;
	
	// --- remote container variables ----------------------------------------------
	private Location remoteContainerName;
	private int currentThresholdExceededLocal;
	private int currentThresholdExceededRemote;
	private int containerCounter;
	@Override
	protected void setup() {
		
		containerCounter =0;
		
		// ----- get the arguments of agents ---------------------------------------
		coordinate = getArguments();
		
		// ----- create coordinates of Agents --------------------------------------
		cRow = (Integer) coordinate[0];
		cCol = (Integer) coordinate[1];
		totalAgents = cRow*cCol;
		System.out.println(" total Agents "+totalAgents);
		
		// ---- Ontology, coding and decoding language ------------------------------
		manager = getContentManager();
		managerMobile = getContentManager(); 
		codec = new SLCodec();
		ontology = AgentGUI_DistributionOntology.getInstance();
		manager.registerLanguage(codec);
		manager.registerOntology(ontology);
		managerMobile.registerOntology(MobilityOntology.getInstance());
	     
	     setMaximumCPU(0);
	     setMaximumMemory(0);
	     setMaxNumberOfThread(0);
	     setCurrentThresholdExceededLocal(0);
	     setCurrentThresholdExceededRemote(0);
	     
	     //---- start and distribute game of life Agents ---------------------------
	     addBehaviour(new createGameOfLifeAgents(cRow, cCol));

	}
	
	private void requestNewRemoteContainer() {
		
		// --- Nachricht zusammenbauen und ... --------
		Ontology ontology = AgentGUI_DistributionOntology.getInstance();
		Codec codec = new SLCodec();

		// --- AID des lokalen Agent.GUI-Agenten ------
		JadeUrlChecker myURL = new JadeUrlChecker( this.getContainerController().getPlatformName());
		AID localAgentGUIAgent = new AID("server.client" + "@" + myURL.getJADEurl(), AID.ISGUID );
		
		// --- Nachricht aufbauen ---------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(localAgentGUIAgent);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());

		// --- Definition einer neuen 'Action' --------
		Action act = new Action();
		act.setActor(getAID());
		act.setAction(new ClientRemoteContainerRequest());
		
		// --- ... versenden --------------------------
		try {
			this.getContentManager().registerLanguage(codec);
			this.getContentManager().registerOntology(ontology);
			this.getContentManager().fillContent(msg, act);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		this.send(msg);	
		
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



	//----------- start agents for the game of life -------------------------------------------
	private class createGameOfLifeAgents extends OneShotBehaviour {
		int cRow;
		int cCol;
		Location newDest = null; 
		
		public createGameOfLifeAgents(int cRow, int cCol) {
			this.cRow = cRow;
			this.cCol = cCol;
		}
		
		public void action() {
			int counter = 0;
			int totalAgentsStarted = 0;
			int tempTotalAgent = 0;
			String agentContDestination = "Main-Container";
			PlatformLoad pload = null;
			
			for (int i = 0; i < cRow; i++) {
				for (int j = 0; j < cCol; j++) {
					//----------- arguments for objects to use ------------------------------------
					//--- renew load information --------------------------------------------------
					
					Object obj[] = new Object[5];
					obj[0] = i;
					obj[1] = j;
					obj[2] = cRow;
					obj[3] = cCol;
					obj[4] = getAID();
					String agentName = i+"&"+j;	
					Integer levelsExceeded = 0;
					
					// --- Main-Container ------------------------------------------------------------- 
					//System.out.println( "=> Create Agent "+agentName );
					Application.JadePlatform.jadeAgentStart(agentName, GameOfLifeAgent.class.getName(), obj);
					tempTotalAgent++;
					totalAgentsStarted++;
					
					// ---  forcing the system to accept 1000 Agents ----------------------------------
					if ( tempTotalAgent==0 || tempTotalAgent>=500) {
						doWait(1000);
						pload = currentLoadOfContainer(agentContDestination);	
						levelsExceeded = pload.getLoadExceeded();
						tempTotalAgent = 1;
					} else {
						pload = null;
						levelsExceeded = 0;
					}
					
					// --- If Levels exceeded, start new Container -----------------------------------
					if ( levelsExceeded != 0) {
						
						//System.out.println( " Starting a remote Container " );						
						// ----- create new containers -----------------------------------------------
						
						SimulationServiceHelper simHelper = null;
						Hashtable<String, PlatformLoad> lma = null;
						String newRemoteContainerName = null;
						try {
							simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
							newRemoteContainerName= simHelper.startNewRemoteContainer();
							containerCounter++;
							while (simHelper.getContainerLocation(newRemoteContainerName)==null) {
								doWait(1000);
							}
							 newDest = simHelper.getContainerLocation(newRemoteContainerName);
							
						} catch (ServiceException e) {
							e.printStackTrace();
						}
						
						agentContDestination = newDest.getName();
						levelsExceeded = 0; // -- to prevent more remote containers to be created
					}
					
					// --- Migrate the current Agent? ----------------------------------------------------
					if (agentContDestination.equalsIgnoreCase("Main-Container") == false) {
						//------ Migrate Agent to Remote ------------------------------------------------
						AID aid = new AID(agentName, AID.ISLOCALNAME);
				        MobileAgentDescription mad = new MobileAgentDescription();
				        mad.setName(aid);
				        mad.setDestination(newDest);
				        MoveAction ma = new MoveAction();
				        ma.setMobileAgentDescription(mad);
				        sendRequest(new Action(aid, ma));
					}
					// --- put Agent into a hash table with the name of the container -------------------
					totalNumberOfAgents.put(agentName,agentContDestination);
				}
			}
			
			if(totalAgentsStarted == totalAgents){
				System.out.println(" Finished "+tempTotalAgent);
				// -- start Dynamic load balancing only when there are remote containers
				doWait(2000);
				if(containerCounter!=0){
				addBehaviour(new startDynamicLoadBalancingCoordinator(myAgent, 1000));
				}
			}
		}
} 
	
	private void migrateAgents(Location migrateTo, String migrateFrom) {
		
		for (int i = 0; i < 5; i++) {
		
			// ---- migrate Agents to reduce workload on container ----------------
			String AgentName = searchLocationOfAgent(migrateFrom);
		
	       //------ Migrate Agent to Remote ---------------------------------------------------
			 AID aid = new AID(AgentName, AID.ISLOCALNAME);
	         MobileAgentDescription mad = new MobileAgentDescription();
	         mad.setName(aid);
	         mad.setDestination(migrateTo);
	         MoveAction ma = new MoveAction();
	         ma.setMobileAgentDescription(mad);
	         sendRequest(new Action(aid, ma));		
	         
	         totalNumberOfAgents.put(AgentName, migrateTo.getName());
		}
	}

	private String searchLocationOfAgent(String migrateFrom) {
	 
		for (int i = 0; i < cRow; i++) {
			for (int j = 0; j < cCol; j++) {
				//----------- arguments for objects to use ------------------------------------
				
				String agentName = i+"&"+j;
				String newLocation = totalNumberOfAgents.get(agentName);
				if(newLocation.equals(migrateFrom)){
					
					totalNumberOfAgents.remove(agentName);
					
					return agentName;
				}
			}
		}
		
	 return null;
	}
	
	public PlatformLoad currentLoadOfContainer(String containerName){
			
			SimulationServiceHelper simHelper = null;
			Hashtable<String, PlatformLoad> lma = null;
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				lma = simHelper.getContainerLoads();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			// ---- Vector carrying all containers -------------------------------------- 
			Vector<String> v = new Vector<String>( lma.keySet() );
			
		    Iterator<String> it = v.iterator();
			// ---- chose one containers, run through and give its content out ---------- 
		    while (it.hasNext()) {
		    	String container = it.next();
		    	if (container.equalsIgnoreCase(containerName)) {
		    		PlatformLoad pl = lma.get(container);
		    		//System.out.println( "Load on '" + container + "': CPU: " + pl.getLoadCPU() + " - Memory: " + pl.getLoadMemory() + " - NoThreads: " + pl.getLoadNoThreads() + " - Exceeded: " + pl.getLoadExceeded() + "" );
		    		return pl;
		    	}
		    }
		    return null;
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
	public void setRemoteContainerName(Location remoteContainerName) {
		this.remoteContainerName = remoteContainerName;
	}
	/**
	 * @return the remoteContainerName
	 */
	public Location getRemoteContainerName() {
		return remoteContainerName;
	}
	/**
	 * @param maxNumberOfThread the maxNumberOfThread to set
	 */
	public void setMaxNumberOfThread(int maxNumberOfThread) {
		this.maxNumberOfThread = maxNumberOfThread;
	}


	/**
	 * @return the maxNumberOfThread
	 */
	public int getMaxNumberOfThread() {
		return maxNumberOfThread;
	}

	/**
	 * @param currentThresholdExceededLocal the currentThresholdExceededLocal to set
	 */
	public void setCurrentThresholdExceededLocal(
			int currentThresholdExceededLocal) {
		this.currentThresholdExceededLocal = currentThresholdExceededLocal;
	}

	/**
	 * @return the currentThresholdExceededLocal
	 */
	public int getCurrentThresholdExceededLocal() {
		return currentThresholdExceededLocal;
	}

	/**
	 * @param currentThresholdExceededRemote the currentThresholdExceededRemote to set
	 */
	public void setCurrentThresholdExceededRemote(
			int currentThresholdExceededRemote) {
		this.currentThresholdExceededRemote = currentThresholdExceededRemote;
	}

	/**
	 * @return the currentThresholdExceededRemote
	 */
	public int getCurrentThresholdExceededRemote() {
		return currentThresholdExceededRemote;
	}
	
	// ----- Dynamic Load Balancing phase ( All Agents started) -------------
	private class startDynamicLoadBalancingCoordinator extends TickerBehaviour {
		
		private static final long serialVersionUID = 1L;
		
		SimulationServiceHelper simHelper = null;
		Hashtable<String, PlatformLoad> lma = null;
		Vector<String> vector =null;
		Iterator<String> it = null;
		int containerCount = 0;
		Location loc = null;
		
		public startDynamicLoadBalancingCoordinator(Agent a, long period) {
			super(a, period);
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				lma = simHelper.getContainerLoads();
				loc = simHelper.getContainerLocation("Main-Container");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			vector = new Vector<String>( lma.keySet() );
			it = vector.iterator();
			System.out.println(" Starting DLBC");
			
		 	PlatformLoad pl = lma.get("Main-Container");
			maximumMemory = (long) pl.getLoadMemoryJVM();
			maximumCPU = pl.getLoadCPU();
			setRemoteContainerName(loc);
		}

		@Override
		protected void onTick() {
			
			if(containerCount>=vector.size()){
				try {
					lma = simHelper.getContainerLoads();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				// ---- Vector carrying all containers -------------------------------------- 
				vector = new Vector<String>( lma.keySet() );
				it = vector.iterator();
				containerCount =0;
			}

			// ---- chose one containers, run through and give its content out -------------- 
		    if (it.hasNext()) {
		    	
		    	containerCount++;
		    	String containerName = it.next();
		    	//System.out.println(" ContainerName  "+ containerName);
		    	PlatformLoad pl = lma.get(containerName);
		    	//System.out.println( "Load on '" + containerName + "': CPU: " + pl.getLoadCPU() + " - Memory: " + pl.getLoadMemory() + " - NoThreads: " + pl.getLoadNoThreads() + " - Exceeded: " + pl.getLoadExceeded() + "" );
		    	
		    	if(!"game_of_life".equals(containerName)){
		    			
		    		float tempMaximumMemory = pl.getLoadMemoryJVM();
					double tempMaximumCPU = pl.getLoadCPU();
					//int tempMaxThread = pl.getLoadNoThreads();
					//int tempThresholdExceeded = pl.getLoadExceeded();
					
		    		
					// --- store remote container with maxi CPU & Memory ---------
					if (maximumMemory > tempMaximumMemory
							&& maximumCPU > tempMaximumCPU) {
						
						setMaximumCPU((long) tempMaximumCPU);
						setMaximumMemory((long) tempMaximumMemory);
						try {
							lma = simHelper.getContainerLoads();
							setRemoteContainerName(simHelper.getContainerLocation(containerName));
						} catch (ServiceException e) {
							e.printStackTrace();
						}
						
					}
					// ---- distribute according to CPU & Memory values ---------------------------
					if (getMaximumCPU()+(10)<tempMaximumCPU && !getRemoteContainerName().equals(containerName)) {
						
						// ---- migrate Agents from overloaded container to less overloaded container ---
						String migrateFrom = containerName;
						//System.out.println(" migrateFrom : "+containerName+" migrateTo: "+migrateTo);
						migrateAgents(getRemoteContainerName(), migrateFrom);
						
					}
					
					/*
					// ---- distribute according to Threshold value ---------------------------
					if (tempThresholdExceeded!=0) {
						
						// ---- migrate Agents from overloaded container to less overloaded container ---
							String migrateFrom = containerName;
						    String migrateTo = getRemoteContainerName();
						
						migrateAgents(migrateTo, migrateFrom);
						
						// ---- reset list of the hash table ----------------------------------
						if(tempThresholdExceeded==1){
							// for threshold only 
						}
						else if(tempThresholdExceeded ==-1){
							//Shut down remote container 
						}
					}
					*/

		      }
		    }
		}
	}
}
