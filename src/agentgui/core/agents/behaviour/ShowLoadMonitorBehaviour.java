package agentgui.core.agents.behaviour;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import agentgui.simulationService.ontology.ShowMonitorGUI;

/**
 * This Behaviour send a message to the DF, to be visible
 */
public class ShowLoadMonitorBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;
	private final static String loadAgentName = "server.load";
	
	@Override
	public void action() {

		AgentController ageCont = null;
		while (ageCont==null) {
			try {
				ageCont = myAgent.getContainerController().getAgent(loadAgentName);
			} catch (ControllerException e1) {
				block(100);
				//e1.printStackTrace();
			}
		}
		
		myAgent.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		AID receiver = new AID();   
		receiver.setLocalName(loadAgentName);
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		
		Action a = new Action();
		a.setActor( receiver );
		a.setAction( new ShowMonitorGUI() );
		try {
			myAgent.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
		myAgent.doDelete();
	}

}
