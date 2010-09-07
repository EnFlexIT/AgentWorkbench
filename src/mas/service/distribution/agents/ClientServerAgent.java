package mas.service.distribution.agents;

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
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.AddedContainer;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.domain.introspection.Occurred;
import jade.domain.introspection.RemovedContainer;
import jade.lang.acl.ACLMessage;

import java.util.Map;

import mas.Platform;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.BenchmarkResult;
import mas.service.distribution.ontology.ClientRegister;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
import mas.service.distribution.ontology.ClientTrigger;
import mas.service.distribution.ontology.ClientUnregister;
import mas.service.distribution.ontology.OSInfo;
import mas.service.distribution.ontology.PlatformAddress;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.PlatformPerformance;
import mas.service.distribution.ontology.PlatformTime;
import mas.service.distribution.ontology.RemoteContainerConfig;
import mas.service.load.LoadMeasureSigar;
import mas.service.load.LoadMeasureThread;
import mas.service.load.LoadUnits;
import network.JadeUrlChecker;
import application.Application;

public class ClientServerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Ontology ontologyJadeMgmt = JADEManagementOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformTime myPlatformTime = new PlatformTime();
	private PlatformAddress myPlatform = new PlatformAddress();
	private PlatformAddress mainPlatform = new PlatformAddress();
	private PlatformPerformance myPerformance = new PlatformPerformance();
	private OSInfo myOS = new OSInfo();
	private PlatformLoad myLoad = new PlatformLoad();
	private AID mainPlatformAgent = null; 
	
	private ParallelBehaviour parBehaiv = null;
	private long triggerTime = new Long(1000);
	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		getContentManager().registerOntology(ontologyJadeMgmt);
		
		// --- Define Platfornm-Info ----------------------
		JadeUrlChecker myURL = new JadeUrlChecker( this.getContainerController().getPlatformName() );
		myPlatform.setIp(myURL.getHostIP());
		myPlatform.setUrl(myURL.getHostName());
		myPlatform.setPort(myURL.getPort());
		myPlatform.setHttp4mtp( getAMS().getAddressesArray()[0] );
		
		// --- Define Main-Platform-Info ------------------
		myURL = Application.JadePlatform.MASmasterAddress;
		mainPlatform.setIp(myURL.getHostIP());
		mainPlatform.setUrl(myURL.getHostName());
		mainPlatform.setPort(myURL.getPort());
		mainPlatform.setHttp4mtp(myURL.getJADEurl4MTP());
		
		// --- Set the Performance of machine -------------
		LoadMeasureSigar sys = LoadMeasureThread.getLoadCurrent();
		myPerformance.setCpu_vendor(sys.getVendor());
		myPerformance.setCpu_model(sys.getModel());
		myPerformance.setCpu_numberOf(sys.getTotalCpu());
		myPerformance.setCpu_speedMhz((int) sys.getMhz());
		myPerformance.setMemory_totalMB((int) LoadUnits.bytes2(sys.getTotalMemory(), LoadUnits.CONVERT2_MEGA_BYTE));
		
		// --- Set OS-Informations ------------------------
		myOS.setOs_name(System.getProperty("os.name"));
		myOS.setOs_version(System.getProperty("os.version"));
		myOS.setOs_arch(System.getProperty("os.arch"));
		
		// --- Define Receiver of local Status-Info -------
		mainPlatformAgent = new AID("server.master" + "@" + myURL.getJADEurl(), AID.ISGUID );
		mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Send 'Register'-Information ----------------
		ClientRegister reg = new ClientRegister();
		reg.setClientAddress(myPlatform);
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		reg.setClientTime(myPlatformTime);
		reg.setClientPerformance(myPerformance);
		reg.setClientOS(myOS);
		this.sendMessage2MainServer(reg);
		
		// --- Add Main-Behaiviours -----------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new amsSubscriber() );
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		parBehaiv.addSubBehaviour( new TriggerBehaiviour(this,triggerTime) );
		// --- Add Parallel Behaiviour --------------------
		this.addBehaviour(parBehaiv);
		
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaiviour -------------------
		this.removeBehaviour(parBehaiv);
		
		// --- Send 'Unregister'-Information --------------
		ClientUnregister unReg = new ClientUnregister();
		this.sendMessage2MainServer(unReg);
		
	}

	
	private boolean sendMessage2MainServer(Concept agentAction) {
		
		try {
			// --- Definition einer neuen 'Action' --------
			Action act = new Action();
			act.setActor(getAID());
			act.setAction(agentAction);

			// --- Nachricht zusammenbauen und ... --------
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(mainPlatformAgent);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			// msg.setContent(trig);

			// --- ... versenden --------------------------
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
			// --- Falls keine Konfiguration vorgenommen wurde, diese nun vornehmen ---
			String myServices = Application.JadePlatform.MASplatformConfig.getServiceListArgument();
			String myIP = myPlatform.getIp();
			Integer myPort = myPlatform.getPort();
			String newContainerName = Application.JadePlatform.jadeContainerGetNewRemoteName();
			
			remConf = new RemoteContainerConfig();
			remConf.setJadeServices(myServices);
			remConf.setJadeIsRemoteContainer(true);
			remConf.setJadeHost(myIP);
			remConf.setJadePort(myPort.toString());
			remConf.setJadeContainerName(newContainerName);
			remConf.setJadeShowGUI(true);
			
			req.setRemoteConfig(remConf);
		}
		this.sendMessage2MainServer(req);
		
	}
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
			@SuppressWarnings("unused")
			Occurred occ = null;
			Concept agentAction = null; 
			@SuppressWarnings("unused")
			AID senderAID = null;

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				if (msg.getPerformative()==ACLMessage.FAILURE) {
					// --- No Ontology-specific Message -------------
					act = null;
					System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );
				} else {
					// --- Ontology-specific Message ----------------
					try {
						ContentElement con = getContentManager().extractContent(msg);	
						if (con instanceof Action) {
							act = (Action) con;	
						} else if (con instanceof Occurred) {
							occ = (Occurred) con;
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
				
				// --- Work on the Content-Msg ----------------------
				if (act!=null) {
					
					agentAction = act.getAction();
					senderAID = act.getActor();
					
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- S T A R T -----------------
					// ------------------------------------------------------------------
					if (agentAction instanceof ClientRemoteContainerRequest) {
						// --- Direkt an den Server.Master weiterleiten -------
						forwardRemoteContainerRequest(agentAction);
						
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
	// --- Message-Receive-Behaiviour --- E N D ------------
	// -----------------------------------------------------
	

	// -----------------------------------------------------
	// --- Trigger-Behaiviour --- S T A R T ----------------
	// -----------------------------------------------------
	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		public TriggerBehaiviour(Agent a, long period) {
			super(a, period);
		}
		@Override
		protected void onTick() {
			// --- Current Time ---------------------------------
			ClientTrigger trig = new ClientTrigger();
			myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
			trig.setTriggerTime( myPlatformTime );
			
			// --- get current Load-Level -----------------------
			myLoad.setLoadCPU(LoadMeasureThread.getLoadCPU());
			myLoad.setLoadMemory(LoadMeasureThread.getLoadMemory());
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
	// -----------------------------------------------------
	// --- Trigger-Behaiviour --- S T A R T ----------------
	// -----------------------------------------------------

	

	// -----------------------------------------------------
	// --- amsSubscriber-SubClass/Behaiviour --- S T A R T -
	// -----------------------------------------------------
	private class amsSubscriber extends AMSSubscriber {
		
		private static final long serialVersionUID = -4346695401399663561L;

		@SuppressWarnings("unchecked")
		@Override
		protected void installHandlers(Map handlers) {
			// ----------------------------------------------------------------
			EventHandler containerAddedHandler = new EventHandler() {
				private static final long serialVersionUID = -7426704911904579411L;
				@Override
				public void handle(Event event) {
					AddedContainer aCon = (AddedContainer) event;
					if (aCon.getContainer().getName().equalsIgnoreCase("Main-Container")==false) {
						Platform.MAS_ContainerRemote.add(aCon.getContainer());
						//System.out.println( "Container hinzugefügt: " + aCon.getName() + " " + aCon.getContainer() + aCon );
					}
				}
			};
			handlers.put(IntrospectionVocabulary.ADDEDCONTAINER, containerAddedHandler);

			// ----------------------------------------------------------------
			EventHandler containerRemovedHandler = new EventHandler() {
				private static final long serialVersionUID = 8614456287558634409L;
				@Override
				public void handle(Event event) {
					RemovedContainer rCon = (RemovedContainer) event;
					Platform.MAS_ContainerRemote.remove(rCon.getContainer());
					//System.out.println( "Container gelöscht: " + rCon.getName() + " " + rCon.getContainer()  );
				}
				
			};
			handlers.put(IntrospectionVocabulary.REMOVEDCONTAINER, containerRemovedHandler);
			
			// ----------------------------------------------------------------
		}
		
	}
	// -----------------------------------------------------
	// --- amsSubscriber-SubClass/Behaiviour --- E N D -----
	// -----------------------------------------------------

	
}
