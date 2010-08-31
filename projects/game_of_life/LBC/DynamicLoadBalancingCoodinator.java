package game_of_life.LBC;


import game_of_life.ontology.Platform;
import game_of_life.ontology.PlatformInfo;
import game_of_life.ontology.PlatformOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.service.load.LoadMeasureThread;
import application.Application;
import config.GlobalInfo;

public class DynamicLoadBalancingCoodinator extends Agent {

	private static final long serialVersionUID = 1L;

	// ----- Ontology variables -------------------------------------------
	private ContentManager manager;
	private Codec codec;
	private Ontology ontology;
	
	private Location destination;
	private AID controller;
	
	// ---- objects variables ----------------------------------------------
	private Object coordinate[];
	
	// ---- variables remote container CPU & Memory ------------------------
	private long maximumMemory;
	private double maximumCPU;

	// --- topic management service variables -------------------------------
	private AID currentLoadOfContainer;
	private TopicManagementHelper topicHelper;

	// ------ Constuctors ---------------------------------------------------
	//private GlobalInfo global = Application.RunInfo;
	//LoadThreshold loadThreshold = global.getLoadThreshold();

	// -------- CPU & JVM & Threshold
	// ----------------------------------------------------------------------
	private boolean cpuAndMemThresholdExceeded;
	private double currentCpuIdleTime;
	private long currentJvmMemoFree;

	// --- remote container URL & name
	// ----------------------------------------------------------------------
	private String remoteContainerName;
	private String remoteContainerURL;

	// --- local container information URL & --------------------------------
	private String localContainerName;
	private String localContainerURL;

	@Override
	protected void setup() {
		
		// ----- get the arguments of agents --------------------------------
		coordinate = getArguments();
		// ----- needed for migration -----------------------------------------
		controller = (AID)coordinate[0];
		
		init() ;
		
		destination = here();

		setCpuAndMemThresholdExceeded(false);
		setMaximumCPU(0);
		setMaximumMemory(0);

		addBehaviour(new currentLoadOfAgent(this, 2000));

	}
	
	void init() {
		
		manager = getContentManager();
		ontology = PlatformOntology.getInstance();
		manager.registerOntology(ontology);
		// ---- Register language and ontology ------------------------------
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());

	}
	
	
	protected void beforeMove() {
		// -----------------------------
		System.out.println("Moving now to location : " + destination.getName());
	}

	protected void afterMove() {
		// ----------------------------
		init();
		System.out.println("Arrived at location : " + destination.getName());
	}


	// --- behavour perminently send information about container load
	// -----------------------------------------------------------------------
	class currentLoadOfAgent extends TickerBehaviour {

		private static final long serialVersionUID = 1L;

		public currentLoadOfAgent(Agent agent, int delayTime) {

			super(agent, delayTime);
		}

		@Override
		protected void onTick() {
			
			try{
				GlobalInfo global = Application.RunInfo;
				
			System.out.println(" Start ticking ");
				
			// ---- Threshold value. CPU & Memory available
			// ------------------------------------
//			setCpuAndMemThresholdExceeded(global.getLoadThreshold()
//					.isCpuAndMemThresholdExceeded());
//			setCurrentCpuIdleTime(global.getLoadThreshold()
//					.getCurrentCpuIdleTime());
//			setCurrentJvmMemoFree(global.getLoadThreshold()
//					.getCurrentJvmMemoFree());

			setCpuAndMemThresholdExceeded(LoadMeasureThread.isThresholdLevelesExceeded());
			setCurrentCpuIdleTime(LoadMeasureThread.getLoadCurrentAvg().getCpuIdleTime());
			setCurrentJvmMemoFree(LoadMeasureThread.getLoadCurrentAvgJVM().getJvmMemoFree());

			
			// ---- Sending message about current Load of Container
			// -----------------------------
			// sent currentJvmMemoryFree and currentCpuIdle ( Ontology need)
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			
			// ----- code information to be sent -------------------------------
			msg.setLanguage(new SLCodec().getName());
			msg.setOntology(PlatformOntology.getInstance().getName());
			
			PlatformInfo platformInfo = new PlatformInfo();
			platformInfo.setCurrentCpuIdleTime((float) getCurrentCpuIdleTime());
			platformInfo.setCurrentFreeMemory(getCurrentJvmMemoFree());
			platformInfo.setThresholdExceeded(isCpuAndMemThresholdExceeded());
			
			AID receiver = new AID(controller.getLocalName(), false);

			msg.setSender(getAID());
			msg.addReceiver(receiver);
			
			msg.setContent("Load balancing");
			
			Platform comm = new Platform();
			comm.setPlatformInfo(platformInfo);
		
			// Fill the content of the message
			manager.fillContent(msg, comm);

			// Send the message
			System.out.println("[" + getLocalName()
					+ "] Sending the message...");

			send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

		}
	}

	
	/*
	* Receive all commands from the controller agent
	*/
	class ReceiveCommands extends CyclicBehaviour {
	//-----------------------------------------------
         
		public ReceiveCommands(Agent a) {
			
			super(a);
		}

		  public void action() {

	      ACLMessage msg = receive(MessageTemplate.MatchSender(controller));

	      if (msg == null) { block(); return; }

		     if (msg.getPerformative() == ACLMessage.REQUEST){

			    try {
				   ContentElement content = getContentManager().extractContent(msg);
				   Concept concept = ((Action)content).getAction();

				  if (concept instanceof MoveAction){

					  MoveAction ma = (MoveAction)concept;
					  Location l = ma.getMobileAgentDescription().getDestination();
					  if (l != null) doMove(destination = l);
				   }
				}
				catch (Exception ex) { ex.printStackTrace();
				System.out.println("Problems Migration");
				}
			 }
			 else { System.out.println("Unexpected msg from controller agent"); }
		  }
	}
	
	/**
	 * @param currentCpuIdleTime
	 *            the currentCpuIdleTime to set
	 */
	public void setCurrentCpuIdleTime(double currentCpuIdleTime) {
		this.currentCpuIdleTime = currentCpuIdleTime;
	}

	/**
	 * @return the currentCpuIdleTime
	 */
	public double getCurrentCpuIdleTime() {
		return currentCpuIdleTime;
	}

	/**
	 * @param cpuAndMemThresholdExceeded
	 *            the cpuAndMemThresholdExceeded to set
	 */
	public void setCpuAndMemThresholdExceeded(boolean cpuAndMemThresholdExceeded) {
		this.cpuAndMemThresholdExceeded = cpuAndMemThresholdExceeded;
	}

	/**
	 * @return the cpuAndMemThresholdExceeded
	 */
	public boolean isCpuAndMemThresholdExceeded() {
		return cpuAndMemThresholdExceeded;
	}

	/**
	 * @param currentJvmMemoFree
	 *            the currentJvmMemoFree to set
	 */
	public void setCurrentJvmMemoFree(long currentJvmMemoFree) {
		this.currentJvmMemoFree = currentJvmMemoFree;
	}

	/**
	 * @return the currentJvmMemoFree
	 */
	public long getCurrentJvmMemoFree() {
		return currentJvmMemoFree;
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
	
}
