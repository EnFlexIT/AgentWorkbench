package mas.service.distribution.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.time.TimeModelStroke;

/**
 * @version 1.0
 */ 
public class ASimulationServiceControllerAgent extends Agent { 

	private static final long serialVersionUID = 1L;
	
	private SimulationServiceHelper simHelper = null;
	private HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
	private TimeModelStroke tmd = null;
	
	protected void setup() { 

		// --- Setup the Simulation with the Simulation-Service ------------
		tmd = new TimeModelStroke();
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setManagerAgent(this.getAID());
			simHelper.setTimeModel(tmd);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
							
		// --- Setup the Simulation with the Simulation-Service ------------
		try {
			simHelper.setEnvironmentInstance(localEnvModel);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		this.addBehaviour(new ReceiveAndStepBehaviour());
		
	} 
	
	
	private class ReceiveAndStepBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;
		private Integer loop = 0;
		private boolean doStep = true;
		private Integer msgBackSetPoint = localEnvModel.size();
		private Integer msgBackActualValue = 0;
		private Long lastMessage = new Long(0);
		
		@Override
		public void action() {
			
			if (doStep==true) {
				// --- Internal Counting --------------------------------------
				loop++;
				System.out.println( "Agent internal counting: " + loop);
				try {
					// --- Step the simulation one forward --------------------
					// --- automatically informs all involved agents as well --
					simHelper.stepTimeModel(); 

				} catch (ServiceException e) {
					e.printStackTrace();
				}
				doStep = false;
				msgBackActualValue = 0;
				// ---- Now, wait for the response of all other agents --------
				
			} else {
				// ------------------------------------------------------------
				// --- Waiting for the reply of all Agents --------------------
				ACLMessage msg = myAgent.receive();			
				if (msg!=null) {
					
					lastMessage = System.currentTimeMillis();

					// --- Am Modell arbeiten ---------------------------------					
					Integer newValueInteger = Integer.parseInt(msg.getContent());
					localEnvModel.put("1_1", newValueInteger);
					msgBackActualValue++;
					
					// --- Wenn die Liste der zu erwartenden Elemente fertig ist: --- 
					if (msgBackActualValue==msgBackSetPoint) {
						doStep = true;	
						try {
							simHelper.setEnvironmentInstance(localEnvModel);
						} catch (ServiceException e) {
							e.printStackTrace();
						}
					}
					
				} else {
					
					if (System.currentTimeMillis()-lastMessage >= 3 * (1000)) {
						try {
							// --- erneut Notifyen ... ---------------------
							simHelper.notifySensors(SimulationService.SERVICE_UPDATE_TIME_STEP);
						} catch (ServiceException e) {
							e.printStackTrace();
						}						
					}
					block(200);
				}	
			}
			
			
			
			
					
		}
		
	}
} 
