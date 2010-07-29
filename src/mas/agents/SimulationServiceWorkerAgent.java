package mas.agents;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Observable;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.sensoring.ServiceSensor;

/**
 * @version 1.0
 */ 
public class SimulationServiceWorkerAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	private Codec codec = new SLCodec();
	private AID simulationManager = null;
		
	protected void setup() { 

		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simulationManager = simHelper.getManagerAgent();
			simHelper.addSensor(this);	
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	} 
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		
		if (arg.equals( SimulationService.SERVICE_UPDATE_TIME_STEP )) {
			// --- Simulationsimpuls erhalten --------------
			try {
				// --- get Environment-Object-Instance ------------
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				Object simInst = simHelper.getEnvironmentInstance();
				System.out.println( "=> " + simInst.toString() );

				// --- Cast it to the right Type ------------------
				HashMap<String, Integer> localEnvModel = new HashMap<String, Integer>();
				localEnvModel = (HashMap<String, Integer>) simInst;

				// --- Look at the neighbours ---------------------
				Integer myValue = localEnvModel.get("1_1");
				
				// --- Decide what to do --------------------------
				if (myValue>2) {
					myValue = -2;
				} else {
					myValue++;					
				}
				// --- Send a message to the main controler -------
				this.sendMessage2ManagerAgent(myValue);
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private boolean sendMessage2ManagerAgent(Integer newValue) {
		
		// --- Definition einer neuen 'Action' --------
		//Action act = new Action();
		//act.setActor(getAID());
		//act.setAction(agentAction);

		// --- Nachricht zusammenbauen und ... --------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(simulationManager);
		msg.setLanguage(codec.getName());
		// msg.setOntology(ontology.getName());
		msg.setContent(newValue.toString());

		// --- ... versenden --------------------------
		//getContentManager().fillContent(msg, act);
		send(msg);			
		return true;
		
	}
	
} 
