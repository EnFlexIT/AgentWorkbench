package mas.projects.contmas.agents;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import mas.projects.contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class CraneAgent extends ActiveContainerAgent implements TransportOrderHandler{
	public CraneAgent() {
		this(new Crane());
	}
	public Integer lengthOfQueue=3;
	public List loadOrderPostQueue=new ArrayList();
	public CraneAgent(Crane ontologyRepresentation) {
		super("craning", ontologyRepresentation );
	}
	public void setup(){
		super.setup();
		handleTransportOrder();
	}

	public void handleTransportOrder() {
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new recieveLoadOrders(this,mt));
	}
}