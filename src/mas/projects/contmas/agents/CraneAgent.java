package mas.projects.contmas.agents;

import java.util.Random;

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
public class CraneAgent extends ActiveContainerAgent implements TransportOrderHandler, TransportOrderOfferer{
	public CraneAgent() {
		this(new Crane());
	}
	public Integer lengthOfQueue=3;
	public List loadOrderPostQueue=new ArrayList();
	public CraneAgent(Crane ontologyRepresentation) {
		super("craning", ontologyRepresentation);
		ontologyRepresentation.setContains(new BayMap());
	}
	public void setup(){
		super.setup();
		handleTransportOrder();
	}

	public void handleTransportOrder() {
        MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new recieveLoadOrders(this,mt));
	}
	
	public ProposeLoadOffer GetLoadProposal(TransportOrder call){
		ProposeLoadOffer act=super.GetLoadProposal(call);
		Random RandomGenerator=new Random(); 
		call.setTakes(RandomGenerator.nextFloat());
		act.setLoad_offer(call);
		return act;
	}
	
	public void aquireContainer(Container targetContainer){
		super.aquireContainer(targetContainer);
		//TODO aus bisheriger BayMap entfernen
		BlockAddress destination=getEmptyBlockAddress();
		destination.setLocates(targetContainer);
		ontologyRepresentation.getContains().addIs_filled_with(destination);
		System.out.println("Nun hängt der Container am Haken");
	}
	
	public BlockAddress getEmptyBlockAddress(){
		BlockAddress empty=new BlockAddress();
		empty.setX_dimension(0);
		empty.setY_dimension(0);
		empty.setZ_dimension(0);
		return empty;

	}

	public void offerTransportOrder() {
		addBehaviour(new announceLoadOrders(this, commissions));
	}
}