package mas.projects.contmas.agents;

import java.util.Random;

import jade.core.Agent;
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
	
	public void aquireContainer(TransportOrderChain targetContainer){ //eigentlicher Vorgang des Container-Aufnehmens
		super.aquireContainer(targetContainer);
		
		//physikalische Aktionen
		
		//TODO aus bisheriger BayMap entfernen
		BlockAddress destination=getEmptyBlockAddress(); //zieladresse besorgen
		destination.setLocates(targetContainer.getTransports());
		ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
		echoStatus("Nun hängt der Container am Haken");
	}
	
    public List determineContractors(){
    	return toAIDList(getAIDsFromDF("container-distributing"));
    }
	public void offerTransportOrder() {
		addBehaviour(new disposePayload(this));
	}
	class disposePayload extends TickerBehaviour{

		public disposePayload(Agent a) {
			super(a, 2000);
		}

		protected void onTick() {
			LoadList commissions=((ActiveContainerHolder)((ContainerAgent)myAgent).ontologyRepresentation).getAdministers();
			Iterator commissionIter=commissions.getAllConsists_of();
			while(commissionIter.hasNext()){ //Agent hat Transportaufträge abzuarbeiten
				echoStatus("Ticking: commissions available - dropping Container on the hook");

				TransportOrderChain TOChain=((TransportOrderChain) commissionIter.next());
				TransportOrder TO=new TransportOrder();
				TO.setStarts_at(((ContainerAgent) myAgent).ontologyRepresentation);
	//			TO.setEnds_at(new Yard());
//				TOChain.addIs_linked_by(TO);
				LoadList newCommission=new LoadList();
				newCommission.addConsists_of(TOChain);
				addBehaviour(new announceLoadOrders(myAgent, newCommission));
			}
			echoStatus("Ticking: no commissions administered - no Container on the hook to be dropped");
		}
	}
}