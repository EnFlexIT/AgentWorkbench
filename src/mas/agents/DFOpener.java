package mas.agents;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShowGui;
import jade.lang.acl.ACLMessage;


public class DFOpener extends Agent{

	/**
	 * This is just an Agent which is able to open the DF-GUI
	 */
	private static final long serialVersionUID = 1L;
	
	private ACLMessage msg;
	
	protected void setup() {
				
		this.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		this.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(this.getDefaultDF());
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		Action a = new Action();
		a.setActor( this.getDefaultDF() );
		a.setAction( new ShowGui() );
		try {
		 	this.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.send(msg);
		// --- Agents kills himself after sending his message -------
		this.doDelete();
		
	}

}
