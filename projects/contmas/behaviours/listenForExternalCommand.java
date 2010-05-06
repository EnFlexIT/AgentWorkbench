package contmas.behaviours;

import contmas.agents.ContainerAgent;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class listenForExternalCommand extends AchieveREResponder{
	private static final long serialVersionUID= -1150287292943219636L;

	/**
	 * @param a
	 * @param simulationControlAgent TODO
	 */
	ContainerAgent myAgent;
	
	public listenForExternalCommand(Agent a){
		super(a,listenForExternalCommand.createMessageTemplate());
		myAgent=(ContainerAgent) a;
	}

	private static MessageTemplate createMessageTemplate(){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,MessageTemplate.MatchContent("reset"));
		return mt;
	}

	protected ACLMessage handleRequest(ACLMessage request){
		String content="Reset command received. <br /> Requesting HarbourMaster to reset harbour environment.";

		ACLMessage reply=request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(content);
		myAgent.addBehaviour(new requestEnvironmentSetup(myAgent,myAgent.getHarbourMaster()));
		return reply;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}