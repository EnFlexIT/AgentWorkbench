package contmas.behaviours;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.agents.ContainerAgent;
import contmas.agents.HarborMasterAgent;
import contmas.de.unidue.stud.sehawagn.contmas.control.Constants;
import contmas.main.MatchAgentAction;
import contmas.ontology.RequestEnvironmentAction;

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
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestEnvironmentAction())));
		return mt;
	}

	protected ACLMessage handleRequest(ACLMessage request){
		ACLMessage reply=null;
		reply=request.createReply();
		reply.setPerformative(ACLMessage.AGREE);
		RequestEnvironmentAction agAct=(RequestEnvironmentAction) ContainerAgent.extractAction(myAgent, request);
		String action=agAct.getAction();
		if(action.equals(Constants.ENVIRONMENT_ACTION_RESET)){
			((HarborMasterAgent) myAgent).clearHarbourEnvironment();
		} else if(action.equals(Constants.ENVIRONMENT_ACTION_HOLD)) {
			((HarborMasterAgent) myAgent).holdEnvironment();
		} else if(action.equals(Constants.ENVIRONMENT_ACTION_RESUME)) {
			((HarborMasterAgent) myAgent).resumeEnvironment();
		} else {
			 reply=null;
			 ((ContainerAgent) myAgent).echoStatus("Unknown action requestet: "+action);
		}
		return reply;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}