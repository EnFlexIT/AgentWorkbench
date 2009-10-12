/**
 * 
 */
package mas.projects.contmas.agents;

import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import mas.projects.contmas.ontology.AGV;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.PassiveContainerHolder;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class AGVAgent extends PassiveContainerAgent implements TransportOrderHandler {
	public AGVAgent() {
		this(new AGV());
	}
	public AGVAgent(PassiveContainerHolder ontologyRepresentation) {
		super("container-distributing",ontologyRepresentation);
	}
	
	public void setup(){
		super.setup();
		handleTransportOrder();
	}

	/* (non-Javadoc)
	 * @see mas.projects.contmas.agents.TransportOrderHandler#handleTransportOrder()
	 */
	@Override
	public void handleTransportOrder() {
		// TODO Auto-generated method stub
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new recieveLoadOrders(this,mt));
	}

}
