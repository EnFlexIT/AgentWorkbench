package mas.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.util.Observable;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.load.LoadMeasureAvgJVM;
import mas.service.load.LoadMeasureAvgSigar;
import mas.service.load.LoadMeasureThread;
import mas.service.load.LoadThresholdLeveles;
import mas.service.sensoring.ServiceSensor;
import mas.service.time.TimeModelStroke;

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
		this.addBehaviour(new HelloBehaviour(this,3000));
		
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
