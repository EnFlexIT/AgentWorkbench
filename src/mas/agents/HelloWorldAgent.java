package mas.agents;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Observable;
import java.util.Vector;

import application.Application;

import mas.Platform;
import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
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
		this.addBehaviour(new OpenRemotContainer());
		
	} 
	
	class OpenRemotContainer extends OneShotBehaviour {

		private static final long serialVersionUID = -601217778912358114L;

		@Override
		public void action() {

			int noOfContainer = Platform.MAS_ContainerRemote.size();
			while (noOfContainer<5) {
				requestNewRemoteContainer();
				while (noOfContainer == Platform.MAS_ContainerRemote.size()) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}		
				noOfContainer = Platform.MAS_ContainerRemote.size() ;
			}
			
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
			
			LoadMeasureAvgSigar loadCurrentAvg = LoadMeasureThread.getLoadCurrentAvg();
			LoadMeasureAvgJVM loadCurrentAvgJVM = LoadMeasureThread.getLoadCurrentAvgJVM();
			LoadThresholdLeveles thresholdLeveles = LoadMeasureThread.getThresholdLeveles();
			
			// --- Current percentage "CPU used" --------------
			double tempCPU  = (double)Math.round((1-loadCurrentAvg.getCpuIdleTime())*10000)/100;
			// --- Current percentage "Memory used" -----------
			double tempMemo = (double)Math.round(((double)loadCurrentAvgJVM.getJvmHeapUsed() / (double)loadCurrentAvgJVM.getJvmHeapMax()) * 10000)/100;
			// --- Current number of running threads ----------
			int tempNoThreads = loadCurrentAvgJVM.getJvmThreadCount();
						
			System.out.println( "[Agent] -> CPU used: " + tempCPU + "% (" + thresholdLeveles.getThCpuL() + "/" + thresholdLeveles.getThCpuH() + ")" );
			System.out.println( "[Agent] -> Memory used: " + tempMemo + "% (" + thresholdLeveles.getThMemoL() + "/" + thresholdLeveles.getThMemoH() + ")" );
			System.out.println( "[Agent] -> N-Threads: " + tempNoThreads + " (" + thresholdLeveles.getThNoThreadsL() + "/" + thresholdLeveles.getThNoThreadsH() + ")" );
			
		} 
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}
	
} 
