package de.enflexit.awb.core.utillity;

import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;

/**
 * This behaviour class will be used by the UtilityAgent, if JADE should be shut down.<br> 
 * Actually a message to the AMS will be send and the AMS will do the shut down of the platform. 
 * 
 * @see UtilityAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PlatformShutdownBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;

	@Override
	public void action() {

		if (isSimulationServiceRunning()) {
			// --- Stop all simulation-agents via Simulation Service ----------
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
				simHelper.stopSimulationAgents();
			} catch (ServiceException sEx) {
				sEx.printStackTrace();
			}
		}
		
		myAgent.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(myAgent.getAMS());
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		Action a = new Action();
		a.setActor( myAgent.getAMS() );
		a.setAction( new ShutdownPlatform() );
		try {
			myAgent.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
		myAgent.doDelete();
	}

	/**
	 * Checks if the {@link SimulationService} is running or not .
	 * @return true, if the service is running
	 */
	private boolean isSimulationServiceRunning() {
		
		try {
			@SuppressWarnings("unused")
			SimulationServiceHelper simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
			return true;
		} catch (ServiceException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
}
