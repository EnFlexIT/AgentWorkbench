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
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Random;

import contmas.behaviours.*;
import contmas.interfaces.*;
import contmas.main.Const;
import contmas.main.HarbourSetup;
import contmas.main.NotYetReadyException;
import contmas.ontology.*;

public class ContainerHolderAgent extends ContainerAgent implements OntRepProvider,DFSubscriber,HarbourLayoutRequester{
	private static final long serialVersionUID=2742858685553327016L;
	protected String targetAgentServiceType=null;
	protected Domain targetAbstractDomain=null;
	protected ContainerHolder ontologyRepresentation;
	private Random RandomGenerator=new Random();
	public Integer lengthOfProposeQueue=2;
	private Domain harbourMap;
	private List sleepingBehaviours=new ArrayList();

	public ContainerHolderAgent(String serviceType){
		this(serviceType,new ContainerHolder());
	}

	public ContainerHolderAgent(String serviceType,ContainerHolder ontologyRepresentation){
		super(serviceType);
		this.serviceTypeStrings.add("container-handling");
		this.ontologyRepresentation=ontologyRepresentation;
	}

	public ContainerHolder getOntologyRepresentation(AID myAID){
		if(myAID.equals(this.getAID())){
			return this.ontologyRepresentation;
		}
		return null;
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
		this.addBehaviour(new requestHarbourSetup(this,this.getHarbourMaster()));
	}

	public ContainerHolder getOntologyRepresentation(){
		return this.getOntologyRepresentation(this.getAID());
	}

	public void updateContractors(List newContractors,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(this.ontologyRepresentation.getContractors(),newContractors);
		}else{
			this.removeFromContractors(newContractors);
		}
	}

	public void registerForWakeUpCall(Behaviour b){
		sleepingBehaviours.add(b);
	}

	public Boolean aquireContainer(TransportOrderChain targetContainer,BlockAddress destination){ //eigentlicher Vorgang des Container-Aufnehmens
//		this.echoStatus("aquiringContainer",targetContainer);

		//		ontologyRepresentation.getAdministers().addConsists_of(targetContainer); //Container zu Auftragsbuch hinzufügen
		Administered newState=new Administered();
		newState.setAt_address(destination);
		if(this.setTOCState(targetContainer,newState) == null){
			this.echoStatus("ERROR: war noch nicht in States",targetContainer,ContainerAgent.LOGGING_ERROR);
			this.echoStatus("ERROR: Ausschreibung, auf die ich mich beworben habe, nicht gefunden.",targetContainer,ContainerAgent.LOGGING_ERROR);

			return false;
		}
		//physikalische Aktionen

		destination.setLocates(targetContainer.getTransports());
//		this.ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
		//		echoStatus("Nun wird der Container von mir transportiert");
		return true;
	}

	public TransportOrder calculateEffort(TransportOrder call){
		//call.setTakes_until(Math.abs((RandomGenerator.nextLong() + this.getBayUtilization()))+"");
		call.setTakes_until((Math.abs(RandomGenerator.nextInt(10)) + this.getBayUtilization() + System.currentTimeMillis()) + "");
		return call;

	}

	public TransportOrderChainState getTOCState(TransportOrderChain needleTOC){
		return this.setTOCState(needleTOC,null);
	}

	public TransportOrderChainState setTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState){
		TransportOrderChainState oldState=this.touchTOCState(needleTOC,toState,false);
//		if(toState!=null){
//			echoStatus("ChangedTOCState from "+oldState+" to "+toState,needleTOC);
//		}
		return oldState;
	}

	public TransportOrderChainState touchTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch){
		return ContainerHolderAgent.touchTOCState(needleTOC,toState,addRemoveSwitch,this.getOntologyRepresentation());
	}

	public static TransportOrderChainState touchTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch,ContainerHolder ontRep){

		Iterator queue=ontRep.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState storedTOCState=(TOCHasState) queue.next();
			if(needleTOC.getTransports().getBic_code().equals(storedTOCState.getSubjected_toc().getTransports().getBic_code())){
				TransportOrderChainState curState=storedTOCState.getState();
				if(toState != null){
					if(toState.getAt_address()==null){
						toState.setAt_address(curState.getAt_address()); //transfer BlockAddress from old to new state
					}
					if(toState.getLoad_offer()==null){
//						System.out.println("preserving load offer");
						toState.setLoad_offer(curState.getLoad_offer()); //transfer load offer from old to new state
					}
//					System.out.println("load offer="+toState.getLoad_offer());

					storedTOCState.setState(toState); //set-Methode
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

	public List getAllHeldContainers(){
		return getAllHeldContainers(false);
	}
	
	public List getAllHeldContainers(Boolean alsoReserved){
		List output=new ArrayList();
		for(Iterator it=ontologyRepresentation.getAllContainer_states();it.hasNext();){
			TOCHasState curTocState=(TOCHasState) it.next();
			if(curTocState.getState() instanceof Holding){
				output.add(curTocState);
			}
			if(alsoReserved && curTocState.getState() instanceof Reserved && curTocState.getState().getAt_address()!=null){
				output.add(curTocState);
			}
		}
		return output;
	}
	
	public TransportOrderChain getSomeTOCOfState(TransportOrderChainState needleState){
		return getSomeTOCOfState(needleState, true);
	}
	
	public TransportOrderChain getSomeTOCOfState(TransportOrderChainState needleState,Boolean careOverstowed){
		Iterator queue=this.getOntologyRepresentation().getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState) queue.next();
			if(needleState.getClass() == queuedTOCState.getState().getClass()){
				if(careOverstowed && this.isUpmostContainer(queuedTOCState.getSubjected_toc())){
					return queuedTOCState.getSubjected_toc();
				}
			}
		}
		return null;
	}
	
	public java.util.List<TOCHasState> getAllTOCOfState(Class<? extends TransportOrderChainState> needleClass){
		java.util.List<TOCHasState> output=new java.util.ArrayList<TOCHasState>();
		for(Iterator it=ontologyRepresentation.getAllContainer_states();it.hasNext();){
			TOCHasState curTocState=(TOCHasState) it.next();
			if(needleClass.isInstance(curTocState.getState())){
				output.add(curTocState);
			}
		}
		return output;
	}
	
	public BlockAddress getUpmost(Integer x,Integer y){
		BlockAddress upmostContainerPos=null;
		List allHeldContainers=getAllHeldContainers();
		Iterator allContainers=allHeldContainers.iterator();
		while(allContainers.hasNext()){ //alle geladenen Container überprüfen 
			TOCHasState curTOCState=(TOCHasState) allContainers.next();
			Holding curState=(Holding) curTOCState.getState();
			BlockAddress curContainerPos=curState.getAt_address();
			
			if((curContainerPos.getX_dimension() == x) && (curContainerPos.getY_dimension() == y)){ //betrachteter Container steht im stapel auf momentaner koordinate
				if((upmostContainerPos == null) || (upmostContainerPos.getZ_dimension() < curContainerPos.getZ_dimension())){
					upmostContainerPos=curContainerPos;
				}
			}
		} //end while
		return upmostContainerPos;
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
			/*
			curTO.getStarts_at().setAbstract_designation(inflateDomain(curTO.getStarts_at().getAbstract_designation()));
			curTO.getEnds_at().setAbstract_designation(inflateDomain(curTO.getEnds_at().getAbstract_designation()));
			 */
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
		BayMap loadBay=this.ontologyRepresentation.getContains();
		Integer baySize=loadBay.getX_dimension() * loadBay.getY_dimension() * loadBay.getZ_dimension();
		return baySize;
	}

	public Integer getBayUtilization(){
		return getAllHeldContainers().size();
	}

	public BlockAddress getEmptyBlockAddress(TransportOrderChain subject){
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
		myself.setAbstract_designation(reduceDomain(this.ontologyRepresentation.getLives_in()));
		return myself;
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
					if(upmostContainer.
							getLocates().
							getBic_code().
							equals(subjectedToc.
									getTransports().
									getBic_code())){
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
			return matchDomainsTransitive(this.ontologyRepresentation.getLives_in(),end.getAbstract_designation());
		}else{
			return -1;
		}
	}

	public Integer matchDomainsTransitive(Domain inQuestion,Domain suspectedIn){
		//		System.out.println(inQuestion.getClass() + " in " + suspectedIn.getClass() + "?");
		inQuestion=inflateDomain(inQuestion);
		suspectedIn=inflateDomain(suspectedIn);

		if(inQuestion.getId().equals(suspectedIn.getId())){
			return 2; //passt genau
		}
		if(inQuestion.getLies_in() != null){
			Integer match=matchDomainsTransitive(inQuestion.getLies_in(),suspectedIn);
			if(match > -1){
				return match + 1;
			}
		}
		return -1; //passt gar nicht
	}

	public void releaseContainer(TransportOrderChain curTOC){
		TransportOrder TO=new TransportOrder();

		TO.setStarts_at(this.getMyselfDesignator());
		TO.getStarts_at().setAt_address(((Holding)getTOCState(curTOC)).getAt_address()); // add current position of container to start designator 
		TO.setEnds_at(this.getAbstractTargetDesignator());
		curTOC.addIs_linked_by(TO);
		Behaviour b=new announceLoadOrders(this,curTOC);
		this.addBehaviour(b);
	}

	public boolean dropContainer(TransportOrderChain load_offer){
//		echoStatus("removeContainerFromBayMap:",load_offer);
		
		touchTOCState(load_offer,null,true);
//		echoStatus("Container dropped successfully (Message, TOCState).",load_offer,ContainerAgent.LOGGING_INFORM);
		wakeSleepingBehaviours(load_offer);
		return true;

	}

	public void wakeSleepingBehaviours(TransportOrderChain event){
		echoStatus("wakeSleepingBehaviours because container was removed",event,LOGGING_INFORM);
		for(Iterator iterator=sleepingBehaviours.iterator();iterator.hasNext();){
			Behaviour b=(Behaviour) iterator.next();
			b.restart();
			iterator.remove();
		}
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

	/* (non-Javadoc)
	 * @see contmas.behaviours.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		this.updateContractors(updatedAgents,remove);
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.HarbourLayoutRequester#processHarbourLayout(contmas.ontology.Domain)
	 */
	@Override
	public void processHarbourLayout(Domain current_harbour_layout){
//		echoStatus("processHarbourLayout");

		interlaceDomains(current_harbour_layout);
		harbourMap=current_harbour_layout;
//		entangleOntologyRepresentation();
	}
	
	public void entangleOntologyRepresentation(){
//		echoStatus("lives in "+getOntologyRepresentation().getLives_in().getId());
		getOntologyRepresentation().setLives_in(inflateDomain(getOntologyRepresentation().getLives_in()));
		
		if(getOntologyRepresentation() instanceof ActiveContainerHolder){
			List inflatedCapabilities=new ArrayList();
			Iterator iter=((ActiveContainerHolder) getOntologyRepresentation()).getAllCapable_of();
			while(iter.hasNext()){
				inflatedCapabilities.add(inflateDomain((Domain) iter.next()));
			}
			((ActiveContainerHolder) getOntologyRepresentation()).setCapable_of(inflatedCapabilities);
		}
	}

	/*
	 * completes Domains which only have has_subdomains with the backlink lies_in
	 */
	public static void interlaceDomains(Domain input){
		Iterator iter=input.getAllHas_subdomains();
		while(iter.hasNext()){
			Domain curSubDom=(Domain) iter.next();
			curSubDom.setLies_in(input);
			interlaceDomains(curSubDom);
		}
	}

	/*
	 * Reduces an input Domain to its ID, to get transmitted by ACLMessage
	 */
	public static Domain reduceDomain(Domain input){
		Domain output=new Domain();
		output.setId(input.getId());
		return output;
	}
	
	/*
	 * Blows up an input Domain to it's whole lies_in and has_subdomains properties, by finding it in the harbourMap
	 */
	public Domain inflateDomain(Domain input){
		Domain output=findDomain(input.getId(),harbourMap);
		return output;
	}

	//Has_subdomains variant
	public static Domain findDomain(String lookForID,Domain in){
		if(in==null){
			System.out.println("ERROR: lookForID="+lookForID);
		}
		if(in.getId().equals(lookForID)){
			return in;
		}
		if(in.getHas_subdomains()==null){
			System.out.println("ERROR: findDomain getHas_subdomains==null, lookForID="+lookForID+", inID="+in.getId());
		}
		Iterator iter=in.getAllHas_subdomains();
		Domain found=null;
		while(iter.hasNext()){
			Domain curDom=(Domain) iter.next();

			found=findDomain(lookForID,curDom);
			if(found != null){
				return found;
			}
		}
		return null;
	}
	
	public Phy_Position calculateTargetPosition(Designator desig){
		Domain dom=inflateDomain(desig.getAbstract_designation());
		BlockAddress address=desig.getAt_address();
		Phy_Position targetPosition=Const.addPositions(dom.getIs_in_position(),Const.getDisplayPositionBlock(address));
		/*
		echoStatus("dom "+dom.getId()+" Is_in_position="+ Const.positionToString(dom.getIs_in_position()));
		echoStatus("ba "+Const.blockAddressToString(address)+" is at="+ Const.positionToString(Const.getDisplayPositionBlock(address)));

		echoStatus("targetPosition="+ Const.positionToString(targetPosition));
		*/
		return targetPosition;
	}
	
	public void addDefaultRoute(Domain finalTarget){
		List routingTable=new ArrayList();
		RouteInformation route=new RouteInformation();
		route.setTarget(inflateDomain(finalTarget));
		route.setNext_hop(inflateDomain(targetAbstractDomain));
		
		this.getOntologyRepresentation().setReaches_eventually(routingTable);
	}
}