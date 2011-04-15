package agentgui.simulationService.agents;

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
import agentgui.core.application.Application;
import agentgui.core.network.JadeUrlChecker;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;

import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.ontology.AgentGUI_DistributionOntology;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientRegister;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.ClientRemoteContainerRequest;
import agentgui.simulationService.ontology.ClientTrigger;
import agentgui.simulationService.ontology.ClientUnregister;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.PlatformTime;
import agentgui.simulationService.ontology.RegisterReceipt;
import agentgui.simulationService.ontology.RemoteContainerConfig;

public class ServerClientAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Ontology ontologyJadeMgmt = JADEManagementOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformAddress mainPlatform = new PlatformAddress();
	private AID mainPlatformAgent = null;
	private boolean mainPlatformReachable = true;
	
	private ClientRemoteContainerReply myCRCreply = null;
	private PlatformAddress myPlatform = null;
	private PlatformPerformance myPerformance = null;
	private OSInfo myOS = null;
	
	private PlatformTime myPlatformTime = new PlatformTime();
	private ClientRegister myRegistration = new ClientRegister();
	private PlatformLoad myLoad = new PlatformLoad();
	
	private ParallelBehaviour parBehaiv = null;
	private TriggerBehaiviour trigger = null;
	private SaveNodeDescriptionBehaviour sndBehaiv = null;
	
	private long triggerTime = new Long(1000*1);
	private long triggerTime4Reconnection = new Long(1000*20);
	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		getContentManager().registerOntology(ontologyJadeMgmt);
		
		LoadServiceHelper loadHelper = null;
		try {
			loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
			// --- get the local systems-informations ---------
			myCRCreply = loadHelper.getLocalCRCReply();

			// --- Define Platform-Info -----------------------
			myPlatform = myCRCreply.getRemoteAddress();
			// --- Set the Performance of machine -------------
			myPerformance = myCRCreply.getRemotePerformance();
			// --- Set OS-Informations ------------------------
			myOS = myCRCreply.getRemoteOS();
			
		} catch (ServiceException e) {
			// --- problems to get the SimulationsService ! ---
			if (loadHelper==null) {
				this.doDelete();
				return;
			} else {
				e.printStackTrace();	
			}
		}
		
		// --- Define Main-Platform-Info ------------------
		JadeUrlChecker myURL = Application.JadePlatform.MASmasterAddress;
		mainPlatform.setIp(myURL.getHostIP());
		mainPlatform.setUrl(myURL.getHostName());
		mainPlatform.setPort(myURL.getPort());
		mainPlatform.setHttp4mtp(myURL.getJADEurl4MTP());
		
		// --- Define Receiver of local Status-Info -------
		String jadeURL = myURL.getJADEurl();
		if (jadeURL!=null) {
			mainPlatformAgent = new AID("server.master" + "@" + myURL.getJADEurl(), AID.ISGUID );
			mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());	
		}		
		
		// --- Set myTime ---------------------------------
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		
		// --- Send 'Register'-Information ----------------
		myRegistration.setClientAddress(myPlatform);
		myRegistration.setClientTime(myPlatformTime);
		myRegistration.setClientPerformance(myPerformance);
		myRegistration.setClientOS(myOS);
		this.sendMessage2MainServer(myRegistration);
		
		// --- Add Main-Behaviours ------------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		trigger = new TriggerBehaiviour(this,triggerTime);
		parBehaiv.addSubBehaviour( trigger );
		sndBehaiv = new SaveNodeDescriptionBehaviour(this,500);
		parBehaiv.addSubBehaviour( sndBehaiv );
		
		// --- Add the parallel Behaviour from above ------
		this.addBehaviour(parBehaiv);
		
		// --- Finally start the LoadAgent ----------------
		try {
			this.getContainerController().createNewAgent("server.load", agentgui.simulationService.agents.LoadAgent.class.getName(), null).start();
		} catch (StaleProxyException agentErr) {
			agentErr.printStackTrace();
		}
		
	}
	
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
	
	private void forwardRemoteContainerRequest(Concept agentAction) {
		
		// --- Request to start a new Remote-Container -----
		ClientRemoteContainerRequest req = (ClientRemoteContainerRequest) agentAction;
		RemoteContainerConfig remConf = req.getRemoteConfig();
		if (remConf==null) {

			LoadServiceHelper loadHelper = null;
			try {
				loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
				remConf = loadHelper.getDefaultRemoteContainerConfig();
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
	private class ReceiveBehaviour extends CyclicBehaviour {

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
							System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
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
					// --- Fallunterscheidung AgentAction --- S T A R T -----------------
					// ------------------------------------------------------------------
					if (agentAction instanceof RegisterReceipt) {
						System.out.println( "Server.Master (re)connected!" );
						mainPlatformReachable = true;
						trigger.reset(triggerTime);
					
					} else if (agentAction instanceof ClientRemoteContainerRequest) {
						// --- Direkt an den Server.Master weiterleiten -------
						forwardRemoteContainerRequest(agentAction);
					
					} else if (agentAction instanceof ClientRemoteContainerReply) {
						// --- Antwort auf 'RemoteContainerRequest' -----------
						LoadServiceHelper loadHelper = null;
						try {
							loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
							loadHelper.putContainerDescription((ClientRemoteContainerReply) agentAction);
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
					// --- Fallunterscheidung AgentAction --- E N D E -------------------
					// ------------------------------------------------------------------
				}
			}
			else {
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
	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		public TriggerBehaiviour(Agent a, long period) {
			super(a, period);
		}
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
				myLoad.setLoadMemorySystem(LoadMeasureThread.getLoadMemorySystem());
				myLoad.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
				myLoad.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
				myLoad.setLoadExceeded(LoadMeasureThread.getThresholdLevelesExceeded());
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
	// --- Trigger-Behaviour --- S T A R T -----------------
	// -----------------------------------------------------
	

	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- S T A R T ---
	// -----------------------------------------------------
	private class SaveNodeDescriptionBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = 5704581376150290621L;
		
		public SaveNodeDescriptionBehaviour(Agent a, long period) {
			super(a, period);
		}
		protected void onTick() {
			
			if (LoadMeasureThread.getCompositeBenchmarkValue()!=0) {
				// --- Put the local NodeDescription into the -----
				// --- SimulationService 					  -----
				try {

					BenchmarkResult bench = new BenchmarkResult();
					bench.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
					myCRCreply.setRemoteBenchmarkResult(bench);
				
					LoadServiceHelper loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
					loadHelper.putContainerDescription(myCRCreply);
					loadHelper.setAndSaveCRCReplyLocal(myCRCreply);

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
