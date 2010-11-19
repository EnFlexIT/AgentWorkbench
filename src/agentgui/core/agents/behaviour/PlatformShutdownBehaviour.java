package agentgui.core.agents.behaviour;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;

/**
 * This Behaviour send a message to the DF, to be visible
 */
public class PlatformShutdownBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;

	@Override
	public void action() {

		if (simulationServiceIsRunning()) {
			// --- Stop all simulation-agents via Simulation Service ----------
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
				simHelper.stopSimulationAgents();
			} catch (ServiceException e) {
				e.printStackTrace();
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
	 * Checks if the simulations service is running or not 
	 * @return
	 */
	private boolean simulationServiceIsRunning() {
		
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
