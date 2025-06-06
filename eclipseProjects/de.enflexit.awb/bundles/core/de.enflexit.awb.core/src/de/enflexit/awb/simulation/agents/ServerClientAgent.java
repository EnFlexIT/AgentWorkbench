package de.enflexit.awb.simulation.agents;

import jade.content.Concept;
import jade.content.ContentElement;
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
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.introspection.Occurred;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.jade.JadeUrlConfiguration;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.simulation.LoadService;
import de.enflexit.awb.simulation.LoadServiceHelper;
import de.enflexit.awb.simulation.load.LoadMeasureThread;
import de.enflexit.awb.simulation.ontology.AWB_DistributionOntology;
import de.enflexit.awb.simulation.ontology.BenchmarkResult;
import de.enflexit.awb.simulation.ontology.ClientAvailableMachinesReply;
import de.enflexit.awb.simulation.ontology.ClientAvailableMachinesRequest;
import de.enflexit.awb.simulation.ontology.ClientRegister;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerReply;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerRequest;
import de.enflexit.awb.simulation.ontology.ClientTrigger;
import de.enflexit.awb.simulation.ontology.ClientUnregister;
import de.enflexit.awb.simulation.ontology.MasterUpdateNote;
import de.enflexit.awb.simulation.ontology.OSInfo;
import de.enflexit.awb.simulation.ontology.PlatformAddress;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.awb.simulation.ontology.PlatformPerformance;
import de.enflexit.awb.simulation.ontology.PlatformTime;
import de.enflexit.awb.simulation.ontology.RegisterReceipt;
import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import de.enflexit.awb.simulation.ontology.Version;

/**
 * This agent is part of the <b>Agent.GUI</b> background-system and connects the
 * end-user application with the {@link ServerMasterAgent} in order to allow the
 * start of new remote container.  
 * 
 * @see ServerMasterAgent
 * @see ServerSlaveAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServerClientAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AWB_DistributionOntology.getInstance();
	private Ontology ontologyJadeMgmt = JADEManagementOntology.getInstance();
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
	private ClientRegister myRegistration = new ClientRegister();
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
		this.getContentManager().registerOntology(ontologyJadeMgmt);
		
		LoadServiceHelper loadHelper = null;
		try {
			loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);

			// --- Get the local systems-informations ---------
			ClientRemoteContainerReply myCRCreply = this.getCrcReply(loadHelper);
			if (myCRCreply!=null) {
				// --- Define Platform-Info -------------------
				myPlatform = myCRCreply.getRemoteAddress();
				// --- Set the Performance of machine ---------
				myPerformance = myCRCreply.getRemotePerformance();
				// --- Set OS-Informations --------------------
				myOS = myCRCreply.getRemoteOS();
				// --- Set version info -----------------------
				myVersion = myCRCreply.getRemoteVersion();
				
			} else {
				this.doDelete();
				return;
			}
			
		} catch (ServiceException e) {
			// --- problems to get the SimulationsService ! ---
			this.doDelete();
			return;
		}
		
		// --- Define Main-Platform-Info ----------------------
		JadeUrlConfiguration myURL = Application.getGlobalInfo().getJadeUrlConfigurationForMaster();
		if (!myURL.hasErrors()) {
			mainPlatform.setIp(myURL.getHostIP());
			mainPlatform.setUrl(myURL.getHostName());
			mainPlatform.setPort(myURL.getPort());
			mainPlatform.setHttp4mtp(myURL.getJadeURL4MTP());
			
			// --- Define Receiver of local Status-Info -------
			String jadeURL = myURL.getJadeURL();
			if (jadeURL!=null) {
				mainPlatformAgent = new AID("server.master" + "@" + myURL.getJadeURL(), AID.ISGUID );
				mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());	
			}		
		}
		// --- Set myTime ---------------------------------
		myPlatformTime.setTimeStampAsString(Long.toString(System.currentTimeMillis())) ;
		
		// --- Add Main-Behaviours ------------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new MessageReceiveBehaviour() );
		trigger = new TriggerBehaiviour(this,triggerTime);
		parBehaiv.addSubBehaviour( trigger );
		sndBehaiv = new SaveNodeDescriptionBehaviour(this, 500);
		parBehaiv.addSubBehaviour( sndBehaiv );
		
		// --- Add the parallel Behaviour from above ------
		this.addBehaviour(parBehaiv);
		
		// --- Finally start the LoadAgent ----------------
		try {
			this.getContainerController().createNewAgent("server.load", de.enflexit.awb.simulation.agents.LoadMeasureAgent.class.getName(), null).start();
		} catch (StaleProxyException agentErr) {
			agentErr.printStackTrace();
		}
		
		// --- Send 'Register'-Information ----------------
		myRegistration.setClientAddress(myPlatform);
		myRegistration.setClientTime(myPlatformTime);
		myRegistration.setClientPerformance(myPerformance);
		myRegistration.setClientOS(myOS);
		myRegistration.setClientVersion(myVersion);
		this.sendMessage2MainServer(myRegistration);
		
	}
	
	/**
	 * Returns the local ClientRemoteContainerReply from the {@link LoadService}.
	 *
	 * @param loadServiceHelper the load service helper
	 * @return the crc reply
	 */
	private ClientRemoteContainerReply getCrcReply(LoadServiceHelper loadServiceHelper) {
		if (myCRCreply==null) {
			if (loadServiceHelper!=null) {
				try {
					myCRCreply = loadServiceHelper.getLocalCRCReply();
				} catch (ServiceException sEx) {
					sEx.printStackTrace();
				}
			}
		}
		return myCRCreply;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaviour --------------------
		if (parBehaiv!=null) {
			this.removeBehaviour(parBehaiv);
		}
		
		// --- Send 'Unregister'-Information --------------
		ClientUnregister unReg = new ClientUnregister();
		this.sendMessage2MainServer(unReg);
		
	}

	
	/**
	 * This method sends a message to the ServerMasterAgent.
	 *
	 * @param agentAction the agent action
	 * @return true, if successful
	 */
	private boolean sendMessage2MainServer(Concept agentAction) {
		
		// --- In case that we have no address ------------
		if (mainPlatformAgent==null) {
			return false;
		}
		
		// --- Define a new action ------------------------
		Action act = new Action();
		act.setActor(getAID());
		act.setAction(agentAction);

		// --- Build Messagr ... --------------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(mainPlatformAgent);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());

		try {
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
	 * Forwards a remote container request to the ServerMasterAgent, which comes from
	 * the local SimulationService.
	 *
	 * @param agentAction the agent action
	 */
	private void forwardRemoteContainerRequest(Concept agentAction) {
		
		// --- Request to start a new Remote-Container -----
		ClientRemoteContainerRequest req = (ClientRemoteContainerRequest) agentAction;
		RemoteContainerConfig remConf = req.getRemoteConfig();
		if (remConf==null) {

			LoadServiceHelper loadHelper = null;
			try {
				loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
				remConf = loadHelper.getAutoRemoteContainerConfig();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			req.setRemoteConfig(remConf);
			
		}
		this.sendMessage2MainServer(req);
		
	}
	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- S T A R T ---------
	// -----------------------------------------------------
	/**
	 * The MessageReceiveBehaviour of this agent.
	 */
	private class MessageReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
			Concept agentAction = null; 

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				act = null; // --- default ---
				
				// --------------------------------------------------
				// --- Is the msg-content-object an AgentAction? ----
				// --------------------------------------------------				
				try {
					Action actTest = (Action) msg.getContentObject();
					act = actTest;
				} catch (UnreadableException errAct) {
					//errAct.printStackTrace();
				}
				// --------------------------------------------------
				
				if (act==null) {
					// --------------------------------------------------
					// --- Try to use the ContentManager for decoding ---
					// --------------------------------------------------
					if (msg.getPerformative()==ACLMessage.FAILURE) {
						// --- Server.Master not reachable ? ------------
						String msgContent = msg.getContent();
						if (msgContent.contains("server.master")) {
							if (sendNotReachable < sendNotReachableMax) {
								System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
								sendNotReachable++;
								if (sendNotReachable>=sendNotReachableMax) {
									System.out.println( "Server.Master not reachable! Stop now to inform that Server.Master is not reachable!" );
								}
							}
							trigger.reset(triggerTime4Reconnection);
							mainPlatformReachable = false;							
						} else {
							System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );	
						}
						
					} else {
						// --- Ontology-specific Message ----------------
						try {
							ContentElement con = getContentManager().extractContent(msg);	
							if (con instanceof Action) {
								act = (Action) con;	
							} else if (con instanceof Occurred) {
								// Occurred occ = (Occurred) con;
								// --- Messages in the context of Introspection ---
								// --- Not of any further interest (yet)-- --------
								// System.out.println( "++++++ Introspection: " + occ.toString() + "++++++" );
							} else {
								System.out.println( "=> " + myAgent.getName() + " - Unknown MessageType: " + con.toString() );
							}						
						} catch (UngroundedException e) {
							e.printStackTrace();
						} catch (CodecException e) {
							e.printStackTrace();
						} catch (OntologyException e) {
							e.printStackTrace();
						};
					}
					// --------------------------------------------------
				}

				// --------------------------------------------------
				// --- Work on the AgentAction of the Message -------
				// --------------------------------------------------
				if (act!=null) {
					
					agentAction = act.getAction();
					
					// ------------------------------------------------------------------
					// --- Case separation AgentAction --- S T A R T --------------------
					// ------------------------------------------------------------------
					if (agentAction instanceof RegisterReceipt) {
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
						
					} else if (agentAction instanceof ClientRemoteContainerRequest) {
						// --- Forward to Server.Master -----------------------
						forwardRemoteContainerRequest(agentAction);
					
					} else if (agentAction instanceof ClientRemoteContainerReply) {
						// --- Answer to 'RemoteContainerRequest' -------------
						try {
							LoadServiceHelper loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
							loadHelper.putContainerDescription((ClientRemoteContainerReply) agentAction);
						} catch (ServiceException e) {
							e.printStackTrace();
						}

					} else if (agentAction instanceof ClientAvailableMachinesRequest) {
						// --- Forwards a remote container request to the -----
						// --- Server.Master (received from SimulationService -
						ServerClientAgent.this.sendMessage2MainServer(agentAction);
						
					} else if (agentAction instanceof ClientAvailableMachinesReply) {
						// --- Received information about available machines --
						try {
							LoadServiceHelper loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
							loadHelper.putAvailableMachines((ClientAvailableMachinesReply) agentAction);
						} catch (ServiceException e) {
							e.printStackTrace();
						}
						
						
					} else {
						// --- Unknown AgentAction ------------
						System.out.println( "----------------------------------------------------" );
						System.out.println( myAgent.getLocalName() + ": Unknown Message-Type!" );
						System.out.println( agentAction.toString() );
					}
					// ------------------------------------------------------------------
					// --- Case separation  AgentAction --- E N D E ---------------------
					// ------------------------------------------------------------------
				}
			
			} else {
				block();
			}			
		}
		
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- E N D -------------
	// -----------------------------------------------------
	

	// -----------------------------------------------------
	// --- Trigger-Behaviour --- S T A R T -----------------
	// -----------------------------------------------------
	/**
	 * The TriggerBehaiviour of the agents, which informs the ServerMasterAgent
	 * about the current system state and load.
	 */
	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/**
		 * Instantiates a new trigger behaiviour.
		 *
		 * @param a the a
		 * @param period the period
		 */
		public TriggerBehaiviour(Agent a, long period) {
			super(a, period);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {

			myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
			
			if (mainPlatformReachable==false)  {
				// --------------------------------------------------
				// --- Try to (re)register --------------------------
				// --------------------------------------------------
				
				// --- Refresh registration time --------------------
				myRegistration.setClientTime(myPlatformTime);

				// --- Send Message ---------------------------------
				sendMessage2MainServer(myRegistration);
				
			} else {
				// --------------------------------------------------
				// --- Just send a trigger --------------------------
				// --------------------------------------------------
				ClientTrigger trig = new ClientTrigger();
				
				// --- Current Time ---------------------------------
				trig.setTriggerTime( myPlatformTime );
				
				// --- get current Load-Level -----------------------
				myLoad.setLoadCPU(LoadMeasureThread.getLoadCPU());
				myLoad.setLoadMemorySystem(LoadMeasureThread.getLoadRAM());
				myLoad.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
				myLoad.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
				myLoad.setLoadExceeded(LoadMeasureThread.getThresholdLevelExceeded());
				trig.setClientLoad(myLoad);
				
				// --- get Current Benchmark-Result -----------------
				BenchmarkResult bmr = new BenchmarkResult(); 
				bmr.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
				trig.setClientBenchmarkValue(bmr);
				
				// --- Send Message ---------------------------------
				sendMessage2MainServer(trig);
			}
			
		}
	}
	// -----------------------------------------------------
	// --- Trigger-Behaviour --- E N D ---------------------
	// -----------------------------------------------------
	

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
					
					LoadServiceHelper loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
					ClientRemoteContainerReply myCRCreply = getCrcReply(loadHelper);
					if (myCRCreply!=null) {
						myCRCreply.setRemoteBenchmarkResult(bench);
						loadHelper.putContainerDescription(myCRCreply);
						loadHelper.setAndSaveCRCReplyLocal(myCRCreply);
						this.stop();
					}

				} catch (ServiceException servEx) {
					servEx.printStackTrace();
				}
			}
		}
		
	}
	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- E N D -------
	// -----------------------------------------------------

	
}
