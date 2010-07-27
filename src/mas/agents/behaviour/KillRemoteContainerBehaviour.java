package mas.agents.behaviour;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.Vector;

import application.Application;

/**
 * This Behaviour sends messages to 
 * all remote containers to shut them down 
 */
public class KillRemoteContainerBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 5956720451151824210L;

	@Override
	public void action() {
		// --- Get the list of currently added containers --------
		// --- and remove them from the running Plattform --------
		killRemoteContainer(Application.JadePlatform.MASremoteContainer);
		// --- Fertig: Agenten schlafen legen --------------------
		myAgent.doDelete();
	}

	/**
	 * Here all Remote-Containers should be killed during the shutdown-process
	 * of this agent
	 */
	public void killRemoteContainer(Vector<ContainerID> remoteContainer) {

		myAgent.getContentManager().registerLanguage(new SLCodec(0) );
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		
		Iterator<ContainerID> it = remoteContainer.iterator();
		while (it.hasNext()) {

			ContainerID conID = it.next();
			KillContainer kc = new KillContainer();
			kc.setContainer(conID);
			try {
				Action act = new Action();
				act.setActor(myAgent.getAMS());
				act.setAction(kc);

				ACLMessage requestMsg = getRequest();
				requestMsg.setOntology(JADEManagementOntology.NAME);
				myAgent.getContentManager().fillContent(requestMsg, act);
				myAgent.send(requestMsg);
				
			} catch (Exception fe) {
				fe.printStackTrace();
			}
		}

	}

	/**
	 * Retrieve the <code>request</code> ACL message with which this tool agent
	 * requests the AMS tool-specific actions.
	 * @return The request ACL message.
	 */
	private ACLMessage getRequest() {
		ACLMessage AMSRequest = new ACLMessage(ACLMessage.REQUEST);
		AMSRequest.setSender(myAgent.getAID());
		AMSRequest.addReceiver(myAgent.getAMS());
		AMSRequest.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		AMSRequest.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		return AMSRequest;
	}
	
}
