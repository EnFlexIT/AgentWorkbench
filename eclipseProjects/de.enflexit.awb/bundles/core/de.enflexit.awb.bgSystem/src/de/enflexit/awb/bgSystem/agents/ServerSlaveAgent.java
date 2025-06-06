package de.enflexit.awb.bgSystem.agents;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.jade.JadeUrlConfiguration;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.simulation.LoadService;
import de.enflexit.awb.simulation.LoadServiceHelper;
import de.enflexit.awb.simulation.distribution.JadeRemoteStartAgent;
import de.enflexit.awb.simulation.distribution.JadeRemoteStartConfiguration;
import de.enflexit.awb.simulation.load.LoadMeasureThread;
import de.enflexit.awb.simulation.ontology.AWB_DistributionOntology;
import de.enflexit.awb.simulation.ontology.BenchmarkResult;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerReply;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerRequest;
import de.enflexit.awb.simulation.ontology.MasterUpdateNote;
import de.enflexit.awb.simulation.ontology.OSInfo;
import de.enflexit.awb.simulation.ontology.PlatformAddress;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.awb.simulation.ontology.PlatformPerformance;
import de.enflexit.awb.simulation.ontology.PlatformTime;
import de.enflexit.awb.simulation.ontology.RegisterReceipt;
import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import de.enflexit.awb.simulation.ontology.SlaveRegister;
import de.enflexit.awb.simulation.ontology.SlaveTrigger;
import de.enflexit.awb.simulation.ontology.SlaveUnregister;
import de.enflexit.awb.simulation.ontology.Version;
import jade.content.Concept;
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
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * This agent is part of the <b>Agent.GUI</b> background-system and waits for a 
 * so called {@link ClientRemoteContainerRequest} in order to start a new 
 * container for a remotely located platform.<br>
 * Additionally this agent informs the {@link ServerMasterAgent} about the current
 * state of the machine, where this agent is located. 
 * 
 * @see ServerMasterAgent
 * @see ServerClientAgent
 * 
 * @see JadeRemoteStartAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServerSlaveAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AWB_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformAddress mainPlatform = new PlatformAddress();
	private AID mainPlatformAgent;
	private boolean mainPlatformReachable = true;
	
	private ClientRemoteContainerReply myCRCreply;
	private PlatformAddress myPlatform;
	private PlatformPerformance myPerformance;
	private OSInfo myOS;
	private Version myVersion;
	
	private PlatformTime myPlatformTime = new PlatformTime();
	private SlaveRegister myRegistration = new SlaveRegister();
	private PlatformLoad myLoad = new PlatformLoad();	
	
	private ParallelBehaviour parBehaiv;
	private TriggerBehaiviour trigger;
	private SaveNodeDescriptionBehaviour sndBehaiv;
	
	private long triggerTime = Long.valueOf(1000*1);
	private long triggerTime4Reconnection = Long.valueOf(1000*20);

	private int sendNotReachable = 0;
	private int sendNotReachableMax = 3;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		System.out.println("Starting Background System-Agent '" + this.getName() + "'");
		
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		LoadServiceHelper simHelper = null;
		try {
			simHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
			// --- get the local systems-informations ---------
			myCRCreply = simHelper.getLocalCRCReply();
			// --- Define Platform-Info -----------------------
			myPlatform = myCRCreply.getRemoteAddress();
			// --- Set the Performance of machine -------------
			myPerformance = myCRCreply.getRemotePerformance();
			// --- Set OS-Informations ------------------------
			myOS = myCRCreply.getRemoteOS();
			// --- Set version info ---------------------------
			myVersion = myCRCreply.getRemoteVersion();
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// --- Define Main-Platform-Info ------------------
		JadeUrlConfiguration myURL = Application.getGlobalInfo().getJadeUrlConfigurationForMaster();
		if(!myURL.hasErrors()){
			mainPlatform.setIp(myURL.getHostIP());
			mainPlatform.setUrl(myURL.getHostName());
			mainPlatform.setPort(myURL.getPort());
			mainPlatform.setHttp4mtp(myURL.getJadeURL4MTP());
			
			// --- Define Receiver of local Status-Info -------
			mainPlatformAgent = new AID("server.master" + "@" + myURL.getJadeURL(), AID.ISGUID );
			mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		}

		// --- Set myTime ---------------------------------
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		
		// --- Send 'Register'-Information ----------------
		myRegistration.setSlaveAddress(myPlatform);
		myRegistration.setSlaveTime(myPlatformTime);
		myRegistration.setSlavePerformance(myPerformance);
		myRegistration.setSlaveOS(myOS);
		myRegistration.setSlaveVersion(myVersion);
		this.sendMessage2MainServer(myRegistration);
		
		// --- Add Main-Behaviors -------------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour(new MessageReceiveBehaviour());
		trigger = new TriggerBehaiviour(this,triggerTime);
		parBehaiv.addSubBehaviour(trigger);
		sndBehaiv = new SaveNodeDescriptionBehaviour(this,500);
		parBehaiv.addSubBehaviour(sndBehaiv);
		
		// --- Add Parallel Behaviour ---------------------
		this.addBehaviour(parBehaiv);

		// --- Check for a remote start configuration -----  
		try {
			JadeRemoteStartConfiguration startConfig = JadeRemoteStartConfiguration.loadRemoteStartConfiguration();
			if (startConfig!=null) {
				System.out.println("[" + this.getLocalName() + "] Found remote container start configuration.");
				JadeRemoteStartConfiguration.deleteRemoteStartConfiguration();
				this.startRemoteContainer(startConfig);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaiviour -------------------
		this.removeBehaviour(parBehaiv);
		
		// --- Send 'Unregister'-Information --------------
		SlaveUnregister unReg = new SlaveUnregister();
		sendMessage2MainServer(unReg);

				
	}
	
	/**
	 * This method sends a message to the ServerMasterAgent.
	 *
	 * @param agentAction the agent action
	 * @return true, if successful
	 */
	private boolean sendMessage2MainServer(Concept agentAction) {
		
		if (mainPlatformAgent==null) return false;
		
		try {
			// --- Define the 'Action' --------------------
			Action act = new Action();
			act.setActor(getAID());
			act.setAction(agentAction);

			// --- Configure message and ... --------------
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(mainPlatformAgent);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());

			// --- ... send -------------------------------
			getContentManager().fillContent(msg, act);
			send(msg);			
			return true;

		} catch (CodecException e) {
			e.printStackTrace();
			return false;
		} catch (OntologyException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * The TriggerBehaiviour of the agents, which informs the ServerMasterAgent
	 * about the current system state and load.
	 */
	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/**
		 * Instantiates a new trigger behaiviour.
		 *
		 * @param agent the agent
		 * @param period the period
		 */
		public TriggerBehaiviour(Agent agent, long period) {
			super(agent, period);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			// --- Current Time ---------------------------------
			myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
			
			if (mainPlatformReachable==false)  {
				// --------------------------------------------------
				// --- Try to (re)register --------------------------
				// --------------------------------------------------
				
				// --- Refresh registration time --------------------
				myRegistration.setSlaveTime(myPlatformTime);

				// --- Send Message ---------------------------------
				sendMessage2MainServer(myRegistration);

			} else {
				// --------------------------------------------------
				// --- Just send a trigger --------------------------
				// --------------------------------------------------
				SlaveTrigger trig = new SlaveTrigger();

				// --- Current Time ---------------------------------
				trig.setTriggerTime( myPlatformTime );
				
				// --- get current Load-Level -----------------------
				myLoad.setLoadCPU(LoadMeasureThread.getLoadCPU());
				myLoad.setLoadMemorySystem(LoadMeasureThread.getLoadRAM());
				myLoad.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
				myLoad.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
				myLoad.setLoadExceeded(LoadMeasureThread.getThresholdLevelExceeded());
				trig.setSlaveLoad(myLoad);
				
				// --- get Current Benchmark-Result -----------------
				BenchmarkResult bmr = new BenchmarkResult(); 
				bmr.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
				trig.setSlaveBenchmarkValue(bmr);
				
				// --- Send Message ---------------------------------
				sendMessage2MainServer(trig);
			}
			
		}
	}

	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	/**
	 * The Class MessageReceiveBehaviour of this agent.
	 */
	private class MessageReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			Action act = null;
			Concept agentAction = null; 

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				act = null; // --- default ---
				
				if (msg.getPerformative()==ACLMessage.FAILURE) {
					// --- Server.Master not reachable ? ------------
					String msgContent = msg.getContent();
					if (msgContent.contains("server.master")) {
						if (sendNotReachable<sendNotReachableMax) {
							System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
							sendNotReachable++;
							if (sendNotReachable>=sendNotReachableMax) {
								System.out.println( "Server.Master not reachable! Stop now to inform that Server.Master is not reachable!" );
							}
						}
						System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
						trigger.reset(triggerTime4Reconnection);
						mainPlatformReachable = false;							
					} else {
						System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );	
					}
					
				} else {
					// --- Ontology-specific Message ----------------
					try {
						act = (Action) getContentManager().extractContent(msg);
					} catch (UngroundedException e) {
						e.printStackTrace();
					} catch (CodecException e) {
						e.printStackTrace();
					} catch (OntologyException e) {
						e.printStackTrace();
					}
				}
				
				if (act!=null) {
					// -- Check the AgentAction -------------------------------
					agentAction = act.getAction();
					if (agentAction instanceof ClientRemoteContainerRequest) {
						ClientRemoteContainerRequest crcr = (ClientRemoteContainerRequest) agentAction;
						startRemoteContainer(crcr.getRemoteConfig());
					
					} else if (agentAction instanceof RegisterReceipt) {
						System.out.println( "Server.Master (re)connected!" );
						sendNotReachable = 0;
						mainPlatformReachable = true;
						trigger.reset(triggerTime);
						
					} else if (agentAction instanceof MasterUpdateNote) {
						System.out.println( "Server.Master (re)connected, but call for an update!" );
						@SuppressWarnings("unused")
						MasterUpdateNote masterUpdateNote = (MasterUpdateNote) agentAction;
						// --- Start update process ---------------------------
						new AWBUpdater(false).start();
						
					}
				}
				
			} else {
				this.block();
			}			
		}
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- E N D ------------
	// -----------------------------------------------------

	/**
	 * Starts a Remote-Container for a given RemoteContainerConfig-Instance.
	 * @param remoteContainerConfig the remote container configuration
	 */
	private void startRemoteContainer(RemoteContainerConfig remoteContainerConfig) {
		Application.getJadePlatform().startAgent("rcsa", JadeRemoteStartAgent.class.getName(), new Object[] {remoteContainerConfig});
	}
	/**
	 * Starts a Remote-Container based on the given {@link JadeRemoteStartConfiguration}.
	 * @param startConfiguration the start configuration
	 */
	private void startRemoteContainer(JadeRemoteStartConfiguration startConfiguration) {
		Application.getJadePlatform().startAgent("rcsa", JadeRemoteStartAgent.class.getName(), new Object[] {startConfiguration});
	}
	
	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- S T A R T ---
	// -----------------------------------------------------
	/**
	 * The SaveNodeDescriptionBehaviour, which waits for the result of the benchmark 
	 * in order to save this value also in the local node description by using the LoadService.
	 */
	private class SaveNodeDescriptionBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = 5704581376150290621L;
		
		/**
		 * Instantiates a new save node description behaviour.
		 *
		 * @param a the a
		 * @param period the period
		 */
		public SaveNodeDescriptionBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		protected void onTick() {
			
			if (LoadMeasureThread.getCompositeBenchmarkValue()!=0) {
				// --- Put the local NodeDescription into the -----
				// --- SimulationService 					  -----
				try {
					BenchmarkResult bench = new BenchmarkResult();
					bench.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
					myCRCreply.setRemoteBenchmarkResult(bench);
					
					LoadServiceHelper simHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
					simHelper.putContainerDescription(myCRCreply);
					simHelper.setAndSaveCRCReplyLocal(myCRCreply);

				} catch (ServiceException servEx) {
					servEx.printStackTrace();
				}
				this.stop();
			}
		}
		
	}
	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- E N D -------
	// -----------------------------------------------------
	
}
