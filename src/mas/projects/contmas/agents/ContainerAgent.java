/**
 * 
 */
package mas.projects.contmas.agents;

import mas.projects.contmas.ontology.*;
import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
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
	protected Codec codec = new LEAPCodec();
	protected Ontology ontology = ContainerTerminalOntology.getInstance();
	protected String serviceType;
	protected ContainerHolder ontologyRepresentation;
	
	public Integer lengthOfQueue=3;
	public List loadOrdersProposedForQueue=new ArrayList();
	
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
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( serviceType );
        sd.setName( getLocalName() );
        register( sd );
        
		BayMap LoadBay=new BayMap();
		LoadBay.setX_dimension(1);
		LoadBay.setY_dimension(1);
		LoadBay.setZ_dimension(1);
		ontologyRepresentation.setContains(LoadBay);
	}
	public int getBaySize(){
		BayMap LoadBay=ontologyRepresentation.getContains();
		int baySize=LoadBay.getX_dimension()*LoadBay.getY_dimension()*LoadBay.getZ_dimension();
		return baySize;
	}
    void register( ServiceDescription sd)
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
    	System.err.println("findMatchingOrder in ContainerAgent not implemented");
    	return null;
    }
    public TransportOrder findMatchingOutgoingOrder(TransportOrderChain haystack){
    	System.err.println("findMatchingOutgoingOrder in ContainerAgent not implemented");
    	return null;
    }
    public List determineContractors(){
    	ArrayList contractors=new ArrayList();
    	return contractors;
    }
    public void echoStatus(String statusMessage){
    	System.out.println(this.getAID().getLocalName()+": "+statusMessage);
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
			echoStatus("TransportOrder gefunden, die zu mir passt");
			matchingOrder.getEnds_at().setConcrete_designation(getAID());
			matchingOrder.getEnds_at().setType("concrete");

    		act=new ProposeLoadOffer();
    		calculateEffort(matchingOrder);
    		act.setLoad_offer(curTOC);
    		loadOrdersProposedForQueue.add(curTOC);
    	}
		return act;
	}
	
	public TransportOrder calculateEffort(TransportOrder call){
		call.setTakes(0);
		return call;		
	}
	
	public void aquireContainer(TransportOrderChain targetContainer){
		
	}
	public boolean checkQueueCapacity(){
		echoStatus("lengthOfQueue: "+lengthOfQueue+", loadOrderPostQueue.size(): "+loadOrdersProposedForQueue.size());
		return loadOrdersProposedForQueue.size()<lengthOfQueue;
	}
	public boolean checkPlausibility(CallForProposalsOnLoadStage call){
		return true;
	}

	public boolean removeContainerFromBayMap(TransportOrderChain load_offer) {
		Iterator allContainers=ontologyRepresentation.getContains().getIs_filled_with().iterator();
		while(allContainers.hasNext()){
			Container curContainer=((BlockAddress)allContainers.next()).getLocates();
			echoStatus("curContainerID: "+curContainer.getId()+"load_offerID: "+load_offer.getTransports().getId());
			if(curContainer.getId().equals(load_offer.getTransports().getId())){
				allContainers.remove();
				return true;
			}
		}
		return false;
	}

	public void fillMessage(ACLMessage accept, AgentAction act) {
		try {
			getContentManager().fillContent(accept, act);
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
	public AgentAction extractAction(ACLMessage accept) {
		AgentAction act=null;
		try {
			act=(AgentAction) getContentManager().extractContent(accept);
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
}
