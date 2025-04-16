package de.enflexit.awb.core.utillity;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShowGui;
import jade.lang.acl.ACLMessage;

/**
 * This behaviour class will be used by the UtilityAgent, if the DF should be displayed.<br>
 * Actually a message to the DF will be send and the DF will appear. 
 * 
 * @see UtilityAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ShowDFBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;

	@Override
	public void action() {

		myAgent.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(myAgent.getDefaultDF());
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		Action a = new Action();
		a.setActor( myAgent.getDefaultDF() );
		a.setAction( new ShowGui() );
		try {
			myAgent.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
		myAgent.doDelete();
	}

}
