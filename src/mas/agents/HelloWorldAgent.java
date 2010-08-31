package mas.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.util.Observable;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
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
		private Integer loop = 0;

		public HelloBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		public void onTick() { 

			loop++;
			SimulationServiceHelper agentGUIHelper = null;
			TimeModelStroke tmd = null;
			try {
				agentGUIHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				agentGUIHelper.stepTimeModel();
				
				tmd = (TimeModelStroke)agentGUIHelper.getTimeModel();
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			//Date date = new Date(tmd.getTime());
			System.out.println( "Loop: " + loop);
			System.out.println( "Result: " + tmd.getCounter() );
			
		} 
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}
} 
