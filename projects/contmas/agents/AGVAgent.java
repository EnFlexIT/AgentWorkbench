/**
 * 
 */
package contmas.agents;

import contmas.ontology.AGV;
import contmas.ontology.ContainerHolder;
import contmas.ontology.PassiveContainerHolder;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class AGVAgent extends PassiveContainerAgent implements TransportOrderHandler {
	public Integer lengthOfQueue=0;
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

	public void handleTransportOrder() {
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new receiveLoadOrders(this,mt));
	}

    public List determineContractors(){//AGV kann noch keine Container loswerden
    	return null;
    }
}
