package mas.service.distribution.agents;

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
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import mas.service.distribution.JadeRemoteStart;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
import mas.service.distribution.ontology.PlatformAddress;
import mas.service.distribution.ontology.PlatformPerformance;
import mas.service.distribution.ontology.PlatformTime;
import mas.service.distribution.ontology.RemoteContainerConfig;
import mas.service.distribution.ontology.SlaveRegister;
import mas.service.distribution.ontology.SlaveTrigger;
import mas.service.distribution.ontology.SlaveUnregister;
import mas.service.load.LoadMeasureSigar;
import mas.service.load.LoadMeasureThread;
import mas.service.load.LoadUnits;
import network.JadeUrlChecker;
import application.Application;

public class SlaveServerAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformTime myPlatformTime = new PlatformTime();
	private PlatformAddress myPlatform = new PlatformAddress();
	private PlatformAddress mainPlatform = new PlatformAddress();
	private PlatformPerformance myPerformance = new PlatformPerformance();
	private AID mainPlatformAgent = null; 
	
	private ParallelBehaviour parBehaiv = null;
	
	@Override
	protected void setup() {
		super.setup();
		// --- Register Codec and Ontology ----------------
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

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
		myPerformance.setMemory_totalMB( (int) LoadUnits.bytes2(sys.getTotalMemory(), LoadUnits.CONVERT2_MEGA_BYTE));
		
		// --- Define Receiver of local Status-Info -------
		mainPlatformAgent = new AID("server.master" + "@" + myURL.getJADEurl(), AID.ISGUID );
		mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Send 'Register'-Information ----------------
		SlaveRegister reg = new SlaveRegister();
		reg.setSlaveAddress(myPlatform);
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		reg.setSlaveTime(myPlatformTime);
		reg.setSlavePerformance(myPerformance);
		this.sendMessage2MainServer(reg);

		// --- Add Main-Behaiviours -----------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new TriggerBehaiviour(this,1000*10) );
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		// --- Add Parallel Behaiviour --------------------
		this.addBehaviour(parBehaiv);
		
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaiviour -------------------
		this.removeBehaviour(parBehaiv);
		
		// --- Send 'Unregister'-Information --------------
		SlaveUnregister unReg = new SlaveUnregister();
		sendMessage2MainServer(unReg);

				
	}
	
	private boolean sendMessage2MainServer( Concept agentAction ) {
		
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

	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		public TriggerBehaiviour(Agent a, long period) {
			super(a, period);
		}
		@Override
		protected void onTick() {
			// --- Auswahl der entsprechenden AgentAction -------
			SlaveTrigger trig = new SlaveTrigger();
			myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
			trig.setTriggerTime( myPlatformTime );
			// --- Send Message ---------------------------------
			sendMessage2MainServer(trig);
		}
	}

	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
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
					
					agentAction = act.getAction();
					senderAID = act.getActor();
					
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- S T A R T -----------------
					// ------------------------------------------------------------------
					if (agentAction instanceof ClientRemoteContainerRequest) {
						
						ClientRemoteContainerRequest crcr = (ClientRemoteContainerRequest) agentAction;
						startRemoteContainer(crcr.getRemoteConfig());
						
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

	/**
	 * Starts a Remote-Container for given RemoteContainerConfig-Instance
	 */
	private void startRemoteContainer(RemoteContainerConfig rcc) {
		
		System.out.println("Starte Remote-Container ... ");
		new JadeRemoteStart(rcc).start();		
		
	}
	
	
}
