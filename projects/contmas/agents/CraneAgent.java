package contmas.agents;

import java.util.Random;

import com.missing.inspect.Inspector;

import contmas.ontology.*;

import de.planetxml.tools.DebugPrinter;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class CraneAgent extends ActiveContainerAgent implements TransportOrderHandler, TransportOrderOfferer{
	public CraneAgent() {
		this(new Crane());
	}

	public CraneAgent(Crane ontologyRepresentation) {
		super("craning", ontologyRepresentation);
	}
	public void setup(){
		super.setup();
		handleTransportOrder();
		offerTransportOrder();
	}

	public void handleTransportOrder() {
        MessageTemplate mt = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new receiveLoadOrders(this,mt));
	}
	
    public List determineContractors(){
    	if(ontologyRepresentation.getContractors().isEmpty()){
    		ontologyRepresentation.setContractors(toAIDList(getAIDsFromDF("container-distributing")));
    	}
    	return ontologyRepresentation.getContractors();
    }

	@Override
	public void offerTransportOrder() {
		// TODO Auto-generated method stub
	}

}