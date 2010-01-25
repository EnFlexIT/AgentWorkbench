package mas.projects.contmas.agents;

import java.util.Random;

import com.missing.inspect.Inspector;

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
import mas.projects.contmas.ontology.*;

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
	
	public TransportOrder calculateEffort(TransportOrder call){
		call=super.calculateEffort(call);
		Random RandomGenerator=new Random(); 
		call.setTakes(RandomGenerator.nextFloat());
		return call;
	}
	
    public List determineContractors(){
    	if(contractors==null){
    		contractors=toAIDList(getAIDsFromDF("container-distributing"));
    	}
    	return contractors;
    }
	public void offerTransportOrder() {
//		addBehaviour(new disposePayload(this));
//		echoStatus("offerTransportOrder");
	}
/*
	class disposePayload extends TickerBehaviour{

		public disposePayload(Agent a) {
			super(a, 2000);
		}

		protected void onTick() {
			((ContainerAgent)myAgent).releaseAllContainer();
		}
	}
	*/
}