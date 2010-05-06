package contmas.behaviours;

import contmas.agents.HarborMasterAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.RequestEnvironmentReset;
import contmas.ontology.RequestHarbourSetup;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class listenForEnvironmentResetRequest extends AchieveREResponder{
	private static final long serialVersionUID= -1150287292943219636L;

	/**
	 * @param a
	 * @param simulationControlAgent TODO
	 */
	public listenForEnvironmentResetRequest(Agent a){
		super(a,listenForEnvironmentResetRequest.createMessageTemplate(a));
	}

	private static MessageTemplate createMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestEnvironmentReset())));
		return mt;
	}

	protected ACLMessage handleRequest(ACLMessage request){
		((HarborMasterAgent) myAgent).clearHarbourEnvironment();
		ACLMessage reply=request.createReply();
		reply.setPerformative(ACLMessage.AGREE);
		return reply;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}