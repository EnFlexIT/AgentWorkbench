package mas.projects.contmas.agents;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import mas.projects.contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class CraneAgent extends ContainerAgent {
	public CraneAgent() {
		super("craning");
		// TODO Auto-generated constructor stub
	}
	public void setup(){
		super.setup();
		Object[] args=getArguments();
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new recieveLoadOrders(this,mt));
	}
	public class recieveLoadOrders extends ContractNetResponder{
		public recieveLoadOrders(Agent a, MessageTemplate mt) {
			super(a, mt);
			// TODO Auto-generated constructor stub
		}
		protected ACLMessage handleCfp(ACLMessage cfp){
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			return reply;
		}
	}
}