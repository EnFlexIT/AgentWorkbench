package game_of_life.LBC;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.distribution.ontology.AgentGUI_DistributionOntology;
import mas.service.distribution.ontology.ClientRemoteContainerRequest;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.sensoring.ServiceSensor;
import mas.service.time.TimeModelStroke;
import network.JadeUrlChecker;

/**
 * @version 1.0
 */ 
public class MeasureAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	// -------store valures --------------------------------------------------------
	float MainContainerCPU []=new float[100];
	float remote1CPU []=new float[100];
	float remote2CPU []=new float[100];
	float remote3CPU []=new float[100];
	float MainContainerMem []=new float[100];
	float remote1Mem []=new float[100];
	float remote2Mem []=new float[100];
	float remote3Mem []=new float[100];
	private int counterValuesM = 0;
	private int counterValuesR1 = 0;
	private int counterValuesR2 = 0;
	private int counterValuesR3 = 0;
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
		this.requestNewRemoteContainer();
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
		int counterValues = 0;

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
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				lma = simHelper.getContainerLoads();

			} catch (ServiceException e) {
				e.printStackTrace();
			}

			Vector<String> v = new Vector<String>( lma.keySet() );
		    Iterator<String> it = v.iterator();
		    while (it.hasNext()) {
		    	String containerName = it.next();
		    	PlatformLoad pl = lma.get(containerName);
		    	//System.out.println( "Load on '" + containerName + "': CPU: " + pl.getLoadCPU() + " - Memory: " + pl.getLoadMemory() + " - NoThreads: " + pl.getLoadNoThreads() + " - Exceeded: " + pl.getLoadExceeded() + "" );
		    	
		    	
		    	// --- store values -------------------------------------------------------
		    		
		    		if(containerName.equals("Main-Container")&& counterValuesM<100 ){
		    			MainContainerMem[counterValuesM]= pl.getLoadMemoryJVM();
		    			MainContainerCPU[counterValuesM]= pl.getLoadCPU();
		    			counterValuesM++;
		    		}
		    		if(containerName.equals("remote1")&& counterValuesR1<100){
		    			remote1Mem[counterValuesR1]= pl.getLoadMemoryJVM();
		    			remote1CPU[counterValuesR1]= pl.getLoadCPU();
		    			counterValuesR1++;
		    		}
		    		if(containerName.equals("remote2")&& counterValuesR2<100){
		    			remote2Mem[counterValuesR2]= pl.getLoadMemoryJVM();
		    			remote2CPU[counterValuesR2]= pl.getLoadCPU();
		    			counterValuesR2++;
		    		}
		    		if(containerName.equals("remote3")&& counterValuesR3<100){
		    			remote3Mem[counterValuesR3]= pl.getLoadMemoryJVM();
		    			remote3CPU[counterValuesR3]= pl.getLoadCPU();
		    			counterValuesR3++;
		    		}
		    	
		    
		    		//System.out.println(" start printer : ");
		    			counterValues = 0;
		    			if(counterValuesM==99){
		    			System.out.println(" ------- MainContainerCPU ---------------");
		    			for (int i = 0; i < MainContainerCPU.length; i++) {
		    				System.out.println(MainContainerCPU[i]);
		    			}
		    			System.out.println("-------- MainContainerMemory ---------------");
		    			for (int i = 0; i < MainContainerMem.length; i++) {
		    				System.out.println(MainContainerMem[i]);
		    			}
		    			counterValuesM= 0;
		    			}
		    			if(counterValuesR1==99){
		    			System.out.println("------------remote1CPU-----------------------");
		    			for (int i = 0; i < remote1CPU.length; i++) {
		    				System.out.println(remote1CPU[i]);
		    				
		    			}
		    			System.out.println("-------------remote1Memory--------------------------");
		    			for (int i = 0; i < remote1Mem.length; i++) {
		    				System.out.println(remote1Mem[i]);
		    				
		    			}
		    			counterValuesR1=0;
		    			}
		    			
		    			if(counterValuesR2==99){
		    			System.out.println("---------------remote2CPU-------------------------");
		    			for (int i = 0; i < remote2CPU.length; i++) {
		    				System.out.println(remote2CPU[i]);
		    				
		    			}
		    			System.out.println("-------------remote2Memory----------------------------");
		    			for (int i = 0; i < remote2Mem.length; i++) {
		    				System.out.println(remote2Mem[i]);
		    				
		    			}
		    			counterValuesR2=0;
		    			}
		    			if(counterValuesR3==99){
		    			System.out.println("-------------remote3CPU--------------------------");
		    			for (int i = 0; i < remote3CPU.length; i++) {
		    			
		    				System.out.println(remote3CPU[i]);
		    			}
		    			System.out.println("---------------remote3Memory------------------------");
		    			for (int i = 0; i < remote3Mem.length; i++) {
		    			
		    				System.out.println(remote3Mem[i]);
		    		}
		    			counterValuesR3=0;
		    	 }
		    	}
		    }
			
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}
	
} 
