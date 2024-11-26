package de.enflexit.awb.core.utillity;

import de.enflexit.awb.simulation.ontology.ShowMonitorGUI;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

/**
 * This behaviour class will be used by the UtilityAgent, if the LoadAgent (local name = 'server.load') 
 * should be displayed.<br> 
 * Actually a message to the 'server.load'-agent will be send and the Agent will appear. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ShowLoadMonitorBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;
	private final static String loadAgentName = "server.load";
	
	@Override
	public void action() {

		// --- Wait for the start of the LoadMeasureAgent -----------
		long waitTimeMax = System.currentTimeMillis() + (1000 * 7);
		AgentController ageCont = null;
		while (ageCont==null) {
			if (System.currentTimeMillis()>=waitTimeMax) break;
			try {
				ageCont = myAgent.getContainerController().getAgent(loadAgentName);
			} catch (ControllerException e1) {
				block(100);
			}
		}
		
		this.myAgent.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		this.myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		AID receiver = new AID();   
		receiver.setLocalName(loadAgentName);
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		
		Action a = new Action();
		a.setActor( receiver );
		a.setAction(new ShowMonitorGUI());
		try {
			this.myAgent.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.myAgent.send(msg);
		this.myAgent.doDelete();
	}

}
