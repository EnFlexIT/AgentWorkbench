/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Random;

import contmas.behaviours.announceLoadOrders;
import contmas.behaviours.listenForOntRepReq;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.OntRepProvider;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.*;

public class ContainerHolderAgent extends ContainerAgent implements OntRepProvider,DFSubscriber{

	/**
	 * 
	 */
	private static final long serialVersionUID=2742858685553327016L;

	protected String targetAgentServiceType=null;

	protected Domain targetAbstractDomain=null;

	protected ContainerHolder ontologyRepresentation;

	private Random RandomGenerator=new Random();

	public ContainerHolder getOntologyRepresentation(AID myAID){
		if(myAID.equals(this.getAID())){
			return this.ontologyRepresentation;
		}
		return null;
	}

	public ContainerHolder getOntologyRepresentation(){
		return this.getOntologyRepresentation(this.getAID());
	}

	public Integer lengthOfProposeQueue=2;

	public ContainerHolderAgent(String serviceType){
		this(serviceType,new ContainerHolder());
	}

	public ContainerHolderAgent(String serviceType,ContainerHolder ontologyRepresentation){
		super(serviceType);
		this.ontologyRepresentation=ontologyRepresentation;
	}

	public void addToContractors(List newContractors,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(this.ontologyRepresentation.getContractors(),newContractors);
		}else{
			this.removeFromContractors(newContractors);
		}
	}

	public Boolean aquireContainer(TransportOrderChain targetContainer){ //eigentlicher Vorgang des Container-Aufnehmens
		//		ontologyRepresentation.getAdministers().addConsists_of(targetContainer); //Container zu Auftragsbuch hinzufügen
		if(this.touchTOCState(targetContainer,new Administered()) == null){
			this.echoStatus("ERROR: war noch nicht in States",targetContainer,ContainerAgent.LOGGING_ERROR);
		}
		//physikalische Aktionen

		BlockAddress destination=this.getEmptyBlockAddress(); //zieladresse besorgen
		destination.setLocates(targetContainer.getTransports());
		this.ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
		//		echoStatus("Nun wird der Container von mir transportiert");
		if(this.touchTOCState(targetContainer,new Administered()) instanceof Administered){ //Auftrag aus Liste von Bewerbungen streichen
			return true;
		}
		this.echoStatus("ERROR: Ausschreibung, auf die ich mich beworben habe, nicht gefunden.",targetContainer,ContainerAgent.LOGGING_ERROR);
		return false;
	}

	public TransportOrder calculateEffort(TransportOrder call){
		call.setTakes(RandomGenerator.nextFloat() + this.getBayUtilization());
		return call;
	}

	public TransportOrderChainState touchTOCState(TransportOrderChain needleTOC){
		return this.touchTOCState(needleTOC,null);
	}

	public TransportOrderChainState touchTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState){
		return this.touchTOCState(needleTOC,toState,false);
	}

	public TransportOrderChainState touchTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch){
		return ContainerHolderAgent.touchTOCState(needleTOC,toState,addRemoveSwitch,this.getOntologyRepresentation());
	}

	public static TransportOrderChainState touchTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch,ContainerHolder ontRep){
		Iterator queue=ontRep.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState) queue.next();
			if(needleTOC.getTransports().getBic_code().equals(queuedTOCState.getSubjected_toc().getTransports().getBic_code())){
				TransportOrderChainState curState=queuedTOCState.getState();
				if(toState != null){
					queuedTOCState.setState(toState); //set-Methode
				}
				if((toState == null) && addRemoveSwitch){ //remove-Methode
					queue.remove();
				}
				return curState; //get-Methode
			}
		}
		if((toState != null) && addRemoveSwitch){ //add-Methode
			TOCHasState state=new TOCHasState();
			state.setState(toState);
			state.setSubjected_toc(needleTOC);
			ontRep.getContainer_states().add(state);
			return state.getState();
		}
		return null; //nicht gefunden
	}

	public BlockAddress getUpmost(Integer x,Integer y){
		BlockAddress upmostContainer=null;
		Iterator allContainers=this.ontologyRepresentation.getContains().getAllIs_filled_with();
		while(allContainers.hasNext()){ //alle geladenen Container überprüfen 
			BlockAddress curContainer=(BlockAddress) allContainers.next();
			if((curContainer.getX_dimension() == x) && (curContainer.getY_dimension() == y)){ //betrachteter Container steht im stapel auf momentaner koordinate
				if((upmostContainer == null) || (upmostContainer.getZ_dimension() < curContainer.getZ_dimension())){
					upmostContainer=curContainer;
				}
			}
		} //end while
		return upmostContainer;
	}

	public Integer countTOCInState(TransportOrderChainState TOCState){
		Integer queueCount=0;
		Iterator queue=this.ontologyRepresentation.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState) queue.next();
			if(queuedTOCState.getState().getClass() == TOCState.getClass()){
				queueCount++;
			}
		}
		return queueCount;
	}

	public List determineContractors(){
		if( !(this instanceof TransportOrderOfferer)){
			return null;
		}
		if(this.ontologyRepresentation.getContractors().isEmpty()){
//			this.ontologyRepresentation.setContractors(ContainerAgent.toAIDList(this.getAIDsFromDF(this.targetAgentServiceType)));
/*
			DFsubscribePreparer=new prepareSubscribeToDF(this,this.ontologyRepresentation.getContractors(),this.targetDFAgentDescription);
			this.addBehaviour(DFsubscribePreparer);
*/
			Behaviour DFsubscriber;

			DFsubscriber=new subscribeToDF(this,this.targetAgentServiceType);
			this.addBehaviour(DFsubscriber);

		}
		return this.ontologyRepresentation.getContractors();
	}

	public TransportOrder findMatchingOrder(TransportOrderChain haystack){
		return this.findMatchingOrder(haystack,true);
	}

	public TransportOrder findMatchingOrder(TransportOrderChain haystack,boolean matchIncoming){
		Iterator toc=haystack.getAllIs_linked_by();
		TransportOrder matchingOrder=null;
		Integer matchRating= -1;
		Integer bestMatchRating= -1;
		//echoStatus("findMatchingOrder - jede order in der kette durchlaufen");
		while(toc.hasNext()){
			//			echoStatus("Ausschreibung ausprobieren.");

			TransportOrder curTO=(TransportOrder) toc.next();
			if( !matchIncoming){
				if(this.matchAID(curTO.getStarts_at())){//exactly from this agent
					return curTO;
				}else{
					continue;
				}
			}

			//	/*// why did i comment this out ?
			if(matchIncoming && this.matchAID(curTO.getEnds_at())){ //exactly for this agent
				return curTO;
			}

			if(matchIncoming){
				matchRating=this.matchOrder(curTO);
//				echoStatus("Order Ends_at "+ curTO.getEnds_at().getType()+" "+curTO.getEnds_at().getAbstract_designation()+" matcht="+matchRating,haystack,LOGGING_INFORM;
				if((matchRating > -1)){
					if((bestMatchRating == -1) || (bestMatchRating > matchRating)){
						matchingOrder=curTO;
						bestMatchRating=matchRating;
						//	    			echoStatus("Ausschreibung passt zu mir! (besser?)");
					}
				}
			}
		}
		return matchingOrder;
	}

	public Designator getAbstractTargetDesignator(){
		Designator target=new Designator(); //TODO mögliche ziele herausfinden
		target.setType("abstract");
		target.setAbstract_designation(this.targetAbstractDomain);
		return target;
	}

	public Integer getBaySize(){
		BayMap LoadBay=this.ontologyRepresentation.getContains();
		Integer baySize=LoadBay.getX_dimension() * LoadBay.getY_dimension() * LoadBay.getZ_dimension();
		return baySize;
	}

	public Integer getBayUtilization(){
		return this.ontologyRepresentation.getContains().getIs_filled_with().size();
	}

	public BlockAddress getEmptyBlockAddress(){
		BayMap LoadBay=this.getOntologyRepresentation().getContains();
		Integer tries=0;
		Integer maxTries=getBaySize();
		Integer x, y, z;
		while(tries < maxTries){
			x=RandomGenerator.nextInt(LoadBay.getX_dimension());
			y=RandomGenerator.nextInt(LoadBay.getY_dimension());
			BlockAddress upmostContainer=this.getUpmost(x,y);
			z=0;
			if(upmostContainer != null){
				if(upmostContainer.getZ_dimension() < LoadBay.getZ_dimension()){ //there is room on top
					z=upmostContainer.getZ_dimension() + 1;
				}else{
					tries++;
					continue;
				}
			}
			BlockAddress emptyBA=new BlockAddress();
			emptyBA.setX_dimension(x);
			emptyBA.setY_dimension(y);
			emptyBA.setZ_dimension(z);
			return emptyBA;
		}
		return null;
	}

	public ProposeLoadOffer getLoadProposal(TransportOrderChain curTOC){
		ProposeLoadOffer act=null;
		TransportOrder matchingOrder=this.findMatchingOrder(curTOC);
		if(matchingOrder != null){ //passende TransportOrder gefunden
//			echoStatus("EndsAt-Designator vorher:"+matchingOrder.getEnds_at().getType()+" "+matchingOrder.getEnds_at().getAbstract_designation(),curTOC,LOGGING_INFORM);
			matchingOrder.setEnds_at(this.getMyselfDesignator());
//			echoStatus("EndsAt-Designator nachher:"+matchingOrder.getEnds_at().getType()+" "+matchingOrder.getEnds_at().getAbstract_designation(),curTOC,LOGGING_INFORM);

			act=new ProposeLoadOffer();
			this.calculateEffort(matchingOrder);
			act.setLoad_offer(matchingOrder);
			act.setCorresponds_to(curTOC);
			// put this TOC in the queue of TOC proposed for
			this.touchTOCState(curTOC,new ProposedFor(),true);
		}else{
			this.echoStatus("keine TransportOrder passt zu mir.",curTOC,ContainerAgent.LOGGING_NOTICE);
		}
		return act;
	}

	public Designator getMyselfDesignator(){
		Designator myself=new Designator();
		myself.setType("concrete");
		myself.setConcrete_designation(this.getAID());
		myself.setAbstract_designation(this.ontologyRepresentation.getLives_in());
		return myself;
	}

	public TransportOrderChain getSomeTOCOfState(TransportOrderChainState needleState){
		Iterator queue=this.getOntologyRepresentation().getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState) queue.next();
			if(needleState.getClass() == queuedTOCState.getState().getClass()){
				if(this.isUpmostContainer(queuedTOCState.getSubjected_toc())){
					return queuedTOCState.getSubjected_toc();
				}
			}
		}
		return null;
	}

	/**
	 * @param subjectedToc
	 * @return
	 */
	private Boolean isUpmostContainer(TransportOrderChain subjectedToc){
		BayMap LoadBay=this.getOntologyRepresentation().getContains();

		for(int x=0;x < LoadBay.getX_dimension();x++){ //baymap zeilen-
			for(int y=0;y < LoadBay.getY_dimension();y++){ //und spaltenweise durchlaufen
				BlockAddress upmostContainer=this.getUpmost(x,y);
				if(upmostContainer != null){ //an dieser Koordinate steht ein Container obenauf
					if(upmostContainer.getLocates().getBic_code().equals(subjectedToc.getTransports().getBic_code())){
						return true;
					}
				}
			}
		}

		return false;
	}


	public boolean hasBayMapRoom(){
		return this.getBaySize() > this.getBayUtilization();
	}

	public Boolean isQueueNotFull(){
		return this.countTOCInState(new ProposedFor()) < this.lengthOfProposeQueue;
	}

	public Boolean matchAID(Designator designation){
		if(designation.getType().equals("concrete")){
			if(designation.getConcrete_designation().equals(this.getAID())){ //genau für diesen Agenten bestimmt
				return true;
			}
		}
		return false;
	}

	/*
	 * Matcht hier nur Habitat des Ziels (für Static und Passive). Matching für
	 * Active ist angepasst, matcht sowohl Start als auch Ziel und
	 * Habitat+Capabilities Matching-Bewertung:
	 *  -1 NoMatch 
	 *   0 ExactMatch (AID), Static+Passive 
	 *   1 ExactMatch (AID), Active 
	 *   2 relativeMatch (Domain difference), Minimum für Static+Passive
	 *  ...relativeMatch Matching-Wert
	 * stellt also nahezu Aufwand des Transports dar
	 */
	public Integer matchOrder(TransportOrder curTO){
		Designator end=curTO.getEnds_at();
		if(this.matchAID(end)){ //Genau für mich bestimmt
//			echoStatus("Genau für mich bestimmt",LOGGING_INFORM);
			return 0;
		}else if(end.getType().equals("abstract")){
			return ContainerAgent.matchDomainsTransitive(this.ontologyRepresentation.getLives_in(),end.getAbstract_designation());
		}else{
			return -1;
		}
	}

	public void releaseContainer(TransportOrderChain curTOC,Behaviour MasterBehaviour){
		TransportOrder TO=new TransportOrder();

		TO.setStarts_at(this.getMyselfDesignator());
		TO.setEnds_at(this.getAbstractTargetDesignator());
		curTOC.addIs_linked_by(TO);
		LoadList newCommission=new LoadList();
		newCommission.addConsists_of(curTOC);
		Behaviour b=new announceLoadOrders(this,newCommission,MasterBehaviour);
		this.addBehaviour(b);
	}

	public boolean dropContainer(TransportOrderChain load_offer){
		//		echoStatus("removeContainerFromBayMap:",load_offer);
		Iterator allContainers=this.ontologyRepresentation.getContains().getIs_filled_with().iterator();
		while(allContainers.hasNext()){
			Container curContainer=((BlockAddress) allContainers.next()).getLocates();
			//			echoStatus("curContainerID: "+curContainer.getId()+"load_offerID: "+load_offer.getTransports().getId());
			if(curContainer.getBic_code().equals(load_offer.getTransports().getBic_code())){
				allContainers.remove();
				//				echoStatus("Container found and removed.",load_offer);
				touchTOCState(load_offer,null,true);
				echoStatus("Container dropped successfully (Message, BayMap, TOCState).",load_offer,ContainerAgent.LOGGING_INFORM);

				return true;
			}
		}
		this.echoStatus("ERROR: Container NOT found to remove from BayMap.",load_offer,ContainerAgent.LOGGING_ERROR);
		return false;
	}

	public boolean removeFromContractors(AID badContractor){
		Iterator contractorList=this.ontologyRepresentation.getAllContractors();
		while(contractorList.hasNext()){
			AID contractor=(AID) contractorList.next();
			if(badContractor.equals(contractor)){
				contractorList.remove();
				return true;
			}
		}
		return false;
	}

	public boolean removeFromContractors(List badContractors){
		Iterator contractorList=badContractors.iterator();
		Boolean someoneRemoved=false;
		while(contractorList.hasNext()){
			AID contractor=(AID) contractorList.next();
			if(this.removeFromContractors(contractor)){
				someoneRemoved=true;
			}
		}
		return someoneRemoved;
	}

	@Override
	protected void setup(){
		super.setup();
		if(this.ontologyRepresentation.getContains() == null){
			BayMap LoadBay=new BayMap();
			LoadBay.setX_dimension(1);
			LoadBay.setY_dimension(1);
			LoadBay.setZ_dimension(1);
			this.ontologyRepresentation.setContains(LoadBay);
		}

		this.determineContractors();
		this.addBehaviour(new listenForOntRepReq(this));
	}
	
	public Domain flattenDomain(Domain dom){
		Domain newDom=new Domain();
		newDom.setId(dom.getId());
		return dom;
	}

	/* (non-Javadoc)
	 * @see contmas.behaviours.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		this.addToContractors(updatedAgents,remove);
	}
}