package mas.projects.contmas.agents;

import java.util.Random;

import com.missing.inspect.Inspector;

import de.planetxml.tools.DebugPrinter;

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
        MessageTemplate mt = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		addBehaviour(new recieveLoadOrders(this,mt));
	}
	
	public TransportOrder calculateEffort(TransportOrder call){
		call=super.calculateEffort(call);
		Random RandomGenerator=new Random(); 
		call.setTakes(RandomGenerator.nextFloat());
		return call;
	}
	
	public void aquireContainer(TransportOrderChain targetContainer){ //eigentlicher Vorgang des Container-Aufnehmens
		super.aquireContainer(targetContainer); //in AUftragsliste eintragen
		
		//physikalische Aktionen
		
		BlockAddress destination=getEmptyBlockAddress(); //zieladresse besorgen
		destination.setLocates(targetContainer.getTransports());
		ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
		echoStatus("Nun hängt der Container am Haken");
	}
	
    public List determineContractors(){
    	if(!contractors.iterator().hasNext()){
    		contractors=toAIDList(getAIDsFromDF("container-distributing"));
    	}
    	return contractors;
    }
	public void offerTransportOrder() {
		addBehaviour(new disposePayload(this));
	}
	class disposePayload extends TickerBehaviour{

		public disposePayload(Agent a) {
			super(a, 2000);
		}

		protected void onTick() {
			Iterator commissions=((ActiveContainerHolder)((ContainerAgent)myAgent).ontologyRepresentation).getAdministers().getAllConsists_of();
			Designator myself=new Designator();
			myself.setType("concrete");
			myself.setConcrete_designation(myAgent.getAID());
			Boolean hasCommissions=commissions.hasNext();
			while(commissions.hasNext()){ //Agent hat Transportaufträge abzuarbeiten
				echoStatus("Ticking: commissions available - dropping Container on the hook");
				TransportOrderChain curTOC=((TransportOrderChain) commissions.next());
				TransportOrderChain TOChain=curTOC;
				
				TOChain=new TransportOrderChain();
//				TOChain.setIs_linked_by(curTOC.getIs_linked_by());
				TOChain.setTransports(curTOC.getTransports());

				TransportOrder TO=new TransportOrder();
				TO.setStarts_at(myself);
				
				Designator target=new Designator();
				target.setType("abstract");
				target.setAbstract_designation(new Street());//TODO change to Land but implement recursive Domain-determination in passiveHolder
				TO.setEnds_at(target);
//				TO.setLinks(TOChain);
				TOChain.addIs_linked_by(TO);
				LoadList newCommission=new LoadList();
				newCommission.addConsists_of(TOChain);
				
				/*
				DebugPrinter DB=new DebugPrinter(TOChain,8);
//				echoStatus(DB.dump());
//				Inspector.inspect(newCommission);
				*/
				
				addBehaviour(new announceLoadOrders(myAgent, newCommission));
				commissions.remove();
			}
			if(hasCommissions){
				echoStatus("Ticking: no commissions administered - no Container on the hook to be dropped");
			}
		}
	}
}