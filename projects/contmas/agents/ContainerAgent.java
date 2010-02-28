/**
 * 
 */
package contmas.agents;

import java.util.Random;

import contmas.ontology.*;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class ContainerAgent extends Agent {
	protected final Codec codec = new LEAPCodec();
	protected final Ontology ontology = ContainerTerminalOntology.getInstance();
	protected String serviceType;
	protected ContainerHolder ontologyRepresentation;
	
	public Integer lengthOfProposeQueue=2;
	
	public ContainerAgent() {
		this.serviceType="handling-containers";
		this.ontologyRepresentation=new ContainerHolder();
	}
	
	public ContainerAgent(String serviceType) {
		this();
		this.serviceType=serviceType;
	}
	
	public ContainerAgent(String serviceType, ContainerHolder ontologyRepresentation) {
		this(serviceType);
		this.ontologyRepresentation=ontologyRepresentation;
	}
	protected void setup(){ 
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		//register self at DF
        register(serviceType);
        
        if(ontologyRepresentation.getContains()==null){
    		BayMap LoadBay=new BayMap();
    		LoadBay.setX_dimension(1);
    		LoadBay.setY_dimension(1);
    		LoadBay.setZ_dimension(1);
    		ontologyRepresentation.setContains(LoadBay);
        }
	}
	public Integer getBaySize(){
		BayMap LoadBay=ontologyRepresentation.getContains();
		Integer baySize=LoadBay.getX_dimension()*LoadBay.getY_dimension()*LoadBay.getZ_dimension();
		return baySize;
	}
	public Integer getBayUtilization(){
		return ontologyRepresentation.getContains().getIs_filled_with().size();
	}
    public void register(String serviceType)
    {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(getLocalName());
        register(sd);
    }
	
    public void register( ServiceDescription sd)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }
    public DFAgentDescription[] getAgentsFromDF(String serviceType){
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( serviceType );
        dfd.addServices(sd);
        try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			return result;
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public AID[] getAIDsFromDF(String serviceType){
    	DFAgentDescription[] agents=getAgentsFromDF(serviceType);
		AID[] result= new AID[agents.length];
		for (int i = 0; i < agents.length; i++) {
			result[i]=agents[i].getName();
		}
		return result;
    }
    public AID getFirstAIDFromDF(String serviceType){
    	AID[] aids=getAIDsFromDF(serviceType);
    	if(aids.length>0){
    		return aids[0];
    	}else{
    		System.err.println("Kein Agent der Art vorhanden.");
    	}
    	return null;
    }
    public List toAIDList(AID[] input){
    	List output=new ArrayList();
		for (AID aid : input) {
			output.add(aid);
		}
    	return output;
    }
    
    public TransportOrder findMatchingOrder(TransportOrderChain haystack){
    	return findMatchingOrder(haystack,true);
    }
    
    public TransportOrder findMatchingOrder(TransportOrderChain haystack, boolean matchIncoming){
    	Iterator toc=haystack.getAllIs_linked_by();
    	TransportOrder matchingOrder=null;
		Integer matchRating=-1;
		Integer bestMatchRating=-1;
		//echoStatus("findMatchingOrder - jede order in der kette durchlaufen");
    	while(toc.hasNext()){
//			echoStatus("Ausschreibung ausprobieren.");

    		TransportOrder curTO=(TransportOrder) toc.next();
    		if(!matchIncoming && matchAID(curTO.getStarts_at())){
    			return curTO;
    		}
    		matchRating=matchOrder(curTO);
    		if(matchRating>-1 && matchIncoming){
    			if(bestMatchRating==-1 || bestMatchRating>matchRating){
	    			matchingOrder=curTO;
	    			bestMatchRating=matchRating;
//	    			echoStatus("Ausschreibung passt zu mir! (besser?)");
    			}
    		}
    	}
    	return matchingOrder;
    }
    
    /*
     * Matcht hier nur Habitat des Ziels (für Static und Passive).
     * Matching für Active ist angepasst, matcht sowohl Start als auch Ziel
     * und Habitat+Capabilities
     * Matching-Bewertung:
     * -1 NoMatch
     *  0 ExactMatch (AID), Static+Passive
     *  1 ExactMatch (AID), Active
     *  2 relativeMatch (Domain difference), Minimum für Static+Passive
     * ...relativeMatch 
     * 
     * Matching-Wert stellt also nahezu Aufwand des Transports dar
     */
	public Integer matchOrder(TransportOrder curTO){ 
		Designator end=(Designator) curTO.getEnds_at();
		if(matchAID(end)){ //Genau für mich bestimmt
			return 0;
		} else {
			Domain endHabitat=(Domain) end.getAbstract_designation();
			Domain ownHabitat=ontologyRepresentation.getLives_in();
			if(endHabitat.getClass()==ownHabitat.getClass()){ //domain entspricht genau Lebensraum
				return 2; //TODO +DomainDiffrence
			}
		}
		return -1; //order passt gar nicht
	}
	
	public Boolean matchAID(Designator designation){
		if(designation.getType().equals("concrete")){
			if(designation.getConcrete_designation().equals(this.getAID())){ //genau für diesen Agenten bestimmt
				return true;
			}
		} 
		return false;
	}
    
    public List determineContractors(){
    	ArrayList contractors=new ArrayList();
    	return contractors;
    }
    public void echoStatus(String statusMessage){
    	System.out.println(this.getAID().getLocalName()+": "+statusMessage);
    }
    
    public void echoStatus(String statusMessage, TransportOrderChain subject){
    	String additionalText="";
    	if(subject!=null){
    		additionalText=" BIC="+subject.getTransports().getBic_code();
    	}
    	echoStatus(statusMessage+additionalText);
    }
    
	public BlockAddress getEmptyBlockAddress(){
		BlockAddress empty=new BlockAddress();
		empty.setX_dimension(0);
		empty.setY_dimension(0);
		empty.setZ_dimension(0);
		return empty;
	}
	public ProposeLoadOffer GetLoadProposal(TransportOrderChain curTOC){
    	ProposeLoadOffer act=null;
    	TransportOrder matchingOrder=findMatchingOrder(curTOC);
    	if(matchingOrder!=null){ //passende TransportOrder gefunden
//			echoStatus("TransportOrder gefunden, die zu mir passt.",curTOC);
			matchingOrder.getEnds_at().setConcrete_designation(getAID());
			matchingOrder.getEnds_at().setType("concrete");

    		act=new ProposeLoadOffer();
    		calculateEffort(matchingOrder);
    		act.setLoad_offer(curTOC);
    		// put this TOC in the queue of TOC proposed for
    		changeTOCState(curTOC, new ProposedFor(), true);
    	} else {
			echoStatus("keine TransportOrder passt zu mir.",curTOC);
    	}
		return act;
	}
	
	public TransportOrder calculateEffort(TransportOrder call){
		Random RandomGenerator=new Random(); 
		call.setTakes(RandomGenerator.nextFloat()+getBayUtilization());
		return call;		
	}
	
	public Boolean aquireContainer(TransportOrderChain targetContainer){ //eigentlicher Vorgang des Container-Aufnehmens
//		ontologyRepresentation.getAdministers().addConsists_of(targetContainer); //Container zu Auftragsbuch hinzufügen
		if(changeTOCState(targetContainer, new Administerd())==null){
			echoStatus("ERROR: war noch nicht in States",targetContainer);
		}
		//physikalische Aktionen
		
		BlockAddress destination=getEmptyBlockAddress(); //zieladresse besorgen
		destination.setLocates(targetContainer.getTransports());
		ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
//		echoStatus("Nun wird der Container von mir transportiert");
		if(changeTOCState(targetContainer, new Administerd()) instanceof Administerd){ //Auftrag aus Liste von Bewerbungen streichen
			return true;
		}
		echoStatus("ERROR: Ausschreibung, auf die ich mich beworben habe, nicht gefunden.",targetContainer);
		return false;
	}

	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC){
		return changeTOCState(needleTOC,null);
	}
	
	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState){
		return changeTOCState(needleTOC,toState,false);
	}
	
	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch){
		Iterator queue=ontologyRepresentation.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState)queue.next();
			if(needleTOC.getTransports().getBic_code().equals(queuedTOCState.getSubjected_toc().getTransports().getBic_code())){
				TransportOrderChainState curState = queuedTOCState.getState();
				if(toState!=null){
					queuedTOCState.setState(toState); //set-Methode
				}
				if(toState==null && addRemoveSwitch){ //remove-Methode
					queue.remove();
				}
				return curState; //get-Methode
			}
		}
		if(toState!=null && addRemoveSwitch){ //add-Methode
			TOCHasState state=new TOCHasState();
			state.setState(toState);
			state.setSubjected_toc(needleTOC);
			ontologyRepresentation.getContainer_states().add(state);
			return state.getState();
		}
		return null; //nicht gefunden
	}
	
	public TransportOrderChain getSomeTOCOfState(TransportOrderChainState needleState){
		Iterator queue=ontologyRepresentation.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState)queue.next();
			if(needleState.getClass()==queuedTOCState.getState().getClass()){
				return queuedTOCState.getSubjected_toc();
			}
		}
		return null;
	}
	
	public boolean removeContainerFromBayMap(TransportOrderChain load_offer) {
//		echoStatus("removeContainerFromBayMap:",load_offer);
		Iterator allContainers=ontologyRepresentation.getContains().getIs_filled_with().iterator();
		while(allContainers.hasNext()){
			Container curContainer=((BlockAddress)allContainers.next()).getLocates();
//			echoStatus("curContainerID: "+curContainer.getId()+"load_offerID: "+load_offer.getTransports().getId());
			if(curContainer.getBic_code().equals(load_offer.getTransports().getBic_code())){
				allContainers.remove();
//				echoStatus("Container found and removed.",load_offer);
				return true;
			}
		}
		echoStatus("ERROR: Container NOT found to remove from BayMap.",load_offer);
		return false;
	}

	public Boolean isQueueNotFull(){
		return countTOCInState(new ProposedFor())<lengthOfProposeQueue;
	}
		
	public Integer countTOCInState(TransportOrderChainState TOCState){
		Integer queueCount=0;
		Iterator queue=ontologyRepresentation.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState)queue.next();
			if(queuedTOCState.getState().getClass()==TOCState.getClass()){
				queueCount++;
			}
		}
		return queueCount;
	}
	
	public boolean hasBayMapRoom(){
		return getBaySize()>getBayUtilization();
	}
	
	public boolean checkPlausibility(CallForProposalsOnLoadStage call){
		return true;
	}
	
	public boolean removeFromContractors(AID badContractor){
		Iterator contractorList=ontologyRepresentation.getAllContractors();
		while(contractorList.hasNext()){
			AID contractor=(AID)contractorList.next();
			if(badContractor.equals(contractor)){
				contractorList.remove();
				return true;
			}
		}
		return false;
	}

	public void fillMessage(ACLMessage msg, AgentAction act) {
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		try {
			getContentManager().fillContent(msg, act);
		}catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public AgentAction extractAction(ACLMessage msg) {
		AgentAction act=null;
		try {
			act=(AgentAction) getContentManager().extractContent(msg);
		}catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return act;
	}
	
	public void releaseContainer(TransportOrderChain curTOC,Behaviour MasterBehaviour){
		TransportOrder TO=new TransportOrder();
		
		Designator myself=new Designator();
		myself.setType("concrete");
		myself.setConcrete_designation(getAID());
		
		Designator target=new Designator(); //TODO mögliche ziele herausfinden
		target.setType("abstract");
		target.setAbstract_designation(new Street());//TODO change to Land but implement recursive Domain-determination in passiveHolder

		TO.setStarts_at(myself);
		TO.setEnds_at(target);
		curTOC.addIs_linked_by(TO);
		LoadList newCommission=new LoadList();
		newCommission.addConsists_of(curTOC);
		Behaviour b=new announceLoadOrders(this, newCommission,MasterBehaviour);
		addBehaviour(b);
	}
	
}