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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import contmas.behaviours.*;
import contmas.de.unidue.stud.sehawagn.contmas.control.Constants;
import contmas.interfaces.*;
import contmas.main.EnvironmentHelper;
import contmas.main.HarbourSetup;
import contmas.main.NotYetReadyException;
import contmas.main.BlockedBecausePlanningException;
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
		this.serviceTypeStrings.add(Constants.SERVICE_NAME_CONTAINER_HANDLING);
		this.ontologyRepresentation=ontologyRepresentation;
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

	public ContainerHolder getOntologyRepresentation(AID myAID){
		if(myAID.equals(this.getAID())){
			return this.ontologyRepresentation;
		}
		return null;
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
//		if( !b.getBehaviourName().equals("unload") && !b.getBehaviourName().equals("carryOutPlanning")){
//			echoStatus("registering " + b.getBehaviourName() + " for wakeUpCall");
//		}
		sleepingBehaviours.add(b);
		b.block();
	}

	public void wakeSleepingBehaviours(TransportOrderChain event){
		echoStatus("wakeSleepingBehaviours",event,LOGGING_INFORM);
		for(Iterator iterator=sleepingBehaviours.iterator();iterator.hasNext();){
			Behaviour b=(Behaviour) iterator.next();
			b.restart();
			iterator.remove();
		}
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

	public Boolean dropContainer(TransportOrderChain load_offer){
	//		echoStatus("removeContainerFromBayMap:",load_offer);
	
			touchTOCState(load_offer,null,true);
	//		echoStatus("Container dropped successfully (Message, TOCState).",load_offer,ContainerAgent.LOGGING_INFORM);
	//		wakeSleepingBehaviours(load_offer);
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
//		wakeSleepingBehaviours(needleTOC);
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
					if(toState.getAt_address() == null){
						toState.setAt_address(curState.getAt_address()); //transfer BlockAddress from old to new state
					}
					if(toState.getLoad_offer() == null){
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
			if(alsoReserved && curTocState.getState() instanceof Reserved && curTocState.getState().getAt_address() != null){
				output.add(curTocState);
			}
		}
		return output;
	}

	public TransportOrderChain getSomeTOCOfState(TransportOrderChainState needleState){
		return getSomeTOCOfState(needleState,true);
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

	public Integer getBayMapCapacity(){
		BayMap loadBay=this.ontologyRepresentation.getContains();
		Integer baySize=loadBay.getX_dimension() * loadBay.getY_dimension() * loadBay.getZ_dimension();
		return baySize;
	}

	public Integer getBayUtilization(){
		return getAllHeldContainers().size();
	}

	public BlockAddress getEmptyBlockAddress(TransportOrderChain subject){
		BayMap loadBay=getOntologyRepresentation().getContains();
		List allContainers=getAllHeldContainers(true);
		BlockAddress emptyBA=null;
		
		if(allContainers.size()==getBayMapCapacity()){ //bay map is already full
			return emptyBA;
		}

		TransportOrderChainState[][][] bayMapArray=ContainerHolderAgent.convertToBayMapArray(loadBay, allContainers);
		
		Integer x, y, z=0;
		while(emptyBA==null){
			x=RandomGenerator.nextInt(loadBay.getX_dimension());
			y=RandomGenerator.nextInt(loadBay.getY_dimension());
			BlockAddress upmostContainer;
			try {
				upmostContainer = this.getUpmostOccupiedBlockAddress(x,y);
			} catch (BlockedBecausePlanningException e) {
				echoStatus("Stack at current coordinate blocked because of pending plans, skipping.",ContainerAgent.LOGGING_INFORM); //leads to containers hanging in the air
				continue;
			}
	
			if(upmostContainer == null || (upmostContainer.getZ_dimension() < loadBay.getZ_dimension()-1)){ //there is room on top
				if(upmostContainer != null){
					z=upmostContainer.getZ_dimension() + 1;
				}
				
				emptyBA=new BlockAddress();
				emptyBA.setX_dimension(x);
				emptyBA.setY_dimension(y);
				emptyBA.setZ_dimension(z);
//				emptyBA.setLocates(subject.getTransports());

			}
		}
		return emptyBA;
	}

	public BlockAddress getUpmostOccupiedBlockAddress(Integer x,Integer y) throws BlockedBecausePlanningException{
		TransportOrderChainState[][][] bayMapArray=ContainerHolderAgent.convertToBayMapArray(getOntologyRepresentation().getContains(), getAllHeldContainers(true));
		BlockAddress upmostOcc=null;
		TransportOrderChainState[] stack=bayMapArray[x][y];
		
		for (Integer i = 0; i < stack.length; i++) {
			if(stack[i]!=null){ //there is a container at this position
				if(stack[i] instanceof Holding){
					upmostOcc=stack[i].getAt_address(); //memorize it
				} else {
					throw new BlockedBecausePlanningException();
				}
			} else { //no container at this position
				if(upmostOcc!=null){ //already found some container, that one is the upmost
					break;
				}
			}
		}
		return upmostOcc;
	}

	/**
	 * @param subjectedToc
	 * @return
	 */
	private Boolean isUpmostContainer(TransportOrderChain subjectedToc){
		BayMap LoadBay=this.getOntologyRepresentation().getContains();
	
		for(int x=0;x < LoadBay.getX_dimension();x++){ //baymap zeilen-
			for(int y=0;y < LoadBay.getY_dimension();y++){ //und spaltenweise durchlaufen
				BlockAddress upmostContainer;
				try {
					upmostContainer = this.getUpmostOccupiedBlockAddress(x,y);
				} catch (BlockedBecausePlanningException e) {
					continue;
				}
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
		return this.getBayMapCapacity() > this.getBayUtilization();
	}

	public Boolean isQueueNotFull(){
		return this.countTOCInState(new ProposedFor()) < this.lengthOfProposeQueue;
	}

	public ProposeLoadOffer getLoadProposal(TransportOrderChain curTOC){
		ProposeLoadOffer act=null;
		TransportOrder matchingOrder=this.findMatchingOrder(curTOC);
		if(matchingOrder != null){ //passende TransportOrder gefunden
//			echoStatus("EndsAt-Designator vorher:"+matchingOrder.getEnds_at().getType()+" "+matchingOrder.getEnds_at().getAbstract_designation(),curTOC,LOGGING_INFORM);
			
			BlockAddress askedBA = matchingOrder.getEnds_at().getAt_address();
			matchingOrder.setEnds_at(this.getMyselfDesignator());
			
			if(askedBA!=null){ //check, if BlockAddress that is beeing asked for is actually free
				echoStatus("have been asked for slot at "+ EnvironmentHelper.blockAddressToString(askedBA),curTOC);
				TransportOrderChainState[][][] bayMapArray = convertToBayMapArray(getOntologyRepresentation().getContains(), getAllHeldContainers(true));
				if(bayMapArray[askedBA.getX_dimension()][askedBA.getY_dimension()][askedBA.getZ_dimension()] == null){
					matchingOrder.getEnds_at().setAt_address(askedBA);
					echoStatus("adopted it");
				}
			}
			
//			echoStatus("EndsAt-Designator nachher:"+matchingOrder.getEnds_at().getType()+" "+matchingOrder.getEnds_at().getAbstract_designation(),curTOC,LOGGING_INFORM);
			
			act=new ProposeLoadOffer();
			this.calculateEffort(matchingOrder);
			act.setLoad_offer(matchingOrder);
			act.setCorresponds_to(curTOC);
			// put this TOC in the queue of TOC proposed for
			this.touchTOCState(curTOC,new ProposedFor(),true);
		}else{
			this.echoStatus("No TransportOrder fits me.",curTOC,ContainerAgent.LOGGING_NOTICE);
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

	public Designator getAbstractTargetDesignator(){
		Designator target=new Designator(); //TODO mögliche ziele herausfinden
		target.setType("abstract");
		target.setAbstract_designation(this.targetAbstractDomain);
		return target;
	}

	public TransportOrder findMatchingOrder(TransportOrderChain haystack){
		return this.findMatchingOrder(haystack,true);
	}

	public TransportOrder findMatchingOrder(TransportOrderChain haystack,Boolean matchIncoming){
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
//					echoStatus("matching outgoing",haystack);
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

	public Boolean matchAID(Designator designation){
		if(designation!=null && designation.getType().equals("concrete")){
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
		}else if(end!=null && end.getType().equals("abstract")){
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

	public static String transportOrderToString(TransportOrder to){
		if(to==null){
			return "[NULL]";
		}
		Designator start = to.getStarts_at();
		Designator end = to.getStarts_at();

		String output="from ";
		output+=start+" ";

		output+="to ";
		output+=end+" ";
		
		String formattedTime="UNKNOWN";
		if(to.getTakes_until()!=null){
			Long untilTime=Long.valueOf(to.getTakes_until());
			
			SimpleDateFormat smDaFo=new SimpleDateFormat();
			smDaFo.applyPattern(smDaFo.toPattern()+" s.S");

			formattedTime=smDaFo.format(new Date(untilTime));
		}

		
		output+="until ";
		output+=formattedTime+" ";

		return output;
	}
	
	public void releaseContainer(TransportOrderChain curTOC){
		echoStatus("releasing container: ",curTOC);
		TransportOrder TO=findMatchingOrder(curTOC, false);
		if(TO!=null){
			echoStatus("found outgoing transport order for release: "+transportOrderToString(TO),curTOC);
		}
		if(TO==null){ //no predefined outgoing transport order found, just create a new one
			TO=new TransportOrder();
		}

		TO.setStarts_at(this.getMyselfDesignator());
		TO.getStarts_at().setAt_address(((Holding) getTOCState(curTOC)).getAt_address()); // add current position of container to start designator
		if(TO.getEnds_at()==null){
			TO.setEnds_at(this.getAbstractTargetDesignator());
			echoStatus("no predefined end designator, create new one "+transportOrderToString(TO),curTOC);
		}
		curTOC.addIs_linked_by(TO);
		Behaviour b=new announceLoadOrders(this,curTOC);
		this.addBehaviour(b);
	}

	public Boolean removeFromContractors(AID badContractor){
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

	public Boolean removeFromContractors(List badContractors){
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
		if(in == null){
			System.out.println("ERROR: lookForID=" + lookForID);
		}
		if(in.getId().equals(lookForID)){
			return in;
		}
		if(in.getHas_subdomains() == null){
			System.out.println("ERROR: findDomain getHas_subdomains==null, lookForID=" + lookForID + ", inID=" + in.getId());
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
		Phy_Position targetPosition=EnvironmentHelper.addPositions(dom.getIs_in_position(),EnvironmentHelper.getDisplayPositionBlock(address));
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

	public static TransportOrderChainState[][][] convertToBayMapArray(BayMap loadBay,List allContainers){
		TransportOrderChainState[][][] bayMapArray=new TransportOrderChainState[loadBay.getX_dimension()][loadBay.getY_dimension()][loadBay.getZ_dimension()];
		
		Iterator allConts=allContainers.iterator();
		while(allConts.hasNext()){
			TransportOrderChainState curState=((TOCHasState) allConts.next()).getState();
			BlockAddress curContainer=(BlockAddress) curState.getAt_address();
			bayMapArray[curContainer.getX_dimension()][curContainer.getY_dimension()][curContainer.getZ_dimension()]=curState;
		}
		
		return bayMapArray;
	}
}