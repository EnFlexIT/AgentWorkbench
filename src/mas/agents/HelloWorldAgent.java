package mas.agents;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.load.LoadInformation;
import mas.service.load.LoadMeasureAvgJVM;
import mas.service.load.LoadMeasureAvgSigar;
import mas.service.load.LoadMeasureThread;
import mas.service.load.LoadThresholdLeveles;
import mas.service.sensoring.ServiceSensor;
import mas.service.time.TimeModelStroke;
import network.JadeUrlChecker;

/**
 * @version 1.0
 */ 
public class HelloWorldAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	protected void setup() { 

		//System.out.println("Local - Name:" + getAID().getLocalName() );
		//System.out.println("GUID - Name:" + getAID().getName() );
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			TimeModelStroke tmd = new TimeModelStroke();
			simHelper.setTimeModel(tmd);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// ---- Add Cyclic Behaviour -----------
		//this.addBehaviour(new HelloBehaviour(this,3000));
		//this.requestNewRemoteContainer();
		this.addBehaviour(new HelloBehaviour(this, 1000));
	} 
	
	class OpenRemotContainer extends OneShotBehaviour {

		private static final long serialVersionUID = -601217778912358114L;

		@Override
		public void action() {
		
			requestNewRemoteContainer();
			
		}
		
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
	
	class HelloBehaviour extends TickerBehaviour { 

		private static final long serialVersionUID = 1L;

		public HelloBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		public void onTick() { 
			
//			LoadMeasureAvgSigar loadCurrentAvg = LoadMeasureThread.getLoadCurrentAvg();
//			LoadMeasureAvgJVM loadCurrentAvgJVM = LoadMeasureThread.getLoadCurrentAvgJVM();
//			LoadThresholdLeveles thresholdLeveles = LoadMeasureThread.getThresholdLeveles();
//			
//			// --- Current percentage "CPU used" --------------
//			double tempCPU  = (double)Math.round((1-loadCurrentAvg.getCpuIdleTime())*10000)/100;
//			// --- Current percentage "Memory used" -----------
//			double tempMemo = (double)Math.round(((double)loadCurrentAvgJVM.getJvmHeapUsed() / (double)loadCurrentAvgJVM.getJvmHeapMax()) * 10000)/100;
//			// --- Current number of running threads ----------
//			int tempNoThreads = loadCurrentAvgJVM.getJvmThreadCount();
//						
//			System.out.println( "[Agent] -> CPU used: " + tempCPU + "% (" + thresholdLeveles.getThCpuL() + "/" + thresholdLeveles.getThCpuH() + ")" );
//			System.out.println( "[Agent] -> Memory used: " + tempMemo + "% (" + thresholdLeveles.getThMemoL() + "/" + thresholdLeveles.getThMemoH() + ")" );
//			System.out.println( "[Agent] -> N-Threads: " + tempNoThreads + " (" + thresholdLeveles.getThNoThreadsL() + "/" + thresholdLeveles.getThNoThreadsH() + ")" );

			
			SimulationServiceHelper simHelper = null;
			Hashtable<String, PlatformLoad> lma = null;
			String newContainerName = null;
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				lma = simHelper.getContainerLoads();
				newContainerName = simHelper.startNewRemoteContainer();
				System.out.println("Neuer Container: " + newContainerName);
				while (simHelper.getLocation(newContainerName)==null) {
					doWait(1000);
				}
				Location loc = simHelper.getLocation(newContainerName);
				System.out.println("Location: " + loc.getName() + " - " + loc.getID()+ " - " + loc.getAddress());
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			myAgent.doSuspend();
			
			
//			Vector<String> v = new Vector<String>( lma.keySet() );
//		    Iterator<String> it = v.iterator();
//		    while (it.hasNext()) {
//		    	String container = it.next();
//		    	PlatformLoad pl = lma.get(container);
//		    	System.out.println( "Load on '" + container + "': CPU: " + pl.getLoadCPU() + " - Memory: " + pl.getLoadMemory() + " - NoThreads: " + pl.getLoadNoThreads() + " - Exceeded: " + pl.getLoadExceeded() + "" );
//		    }
			
		} 
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}
	
} 
