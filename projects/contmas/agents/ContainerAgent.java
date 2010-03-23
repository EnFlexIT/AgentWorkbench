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

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Random;

import contmas.agents.HarborMasterAgent.listenForEnroll;
import contmas.agents.HarborMasterAgent.offerCraneList;
import contmas.behaviours.announceLoadOrders;
import contmas.behaviours.listenForOntRepRequest;
import contmas.behaviours.prepareSubscribeToDF;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 */
public class ContainerAgent extends Agent{

	class sendLogMessage extends OneShotBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID=336783126262644079L;
		ContainerAgent myCAgent=null;
		MessageTemplate loggingTemplate=null;
		String logMessageText="";

		/**
		 * 
		 */
		public sendLogMessage(ContainerAgent myCAgent,String logMessageText){
			super(myCAgent);
			this.myCAgent=myCAgent;

			this.logMessageText=logMessageText;
			if(loggingTopic!=null){
				this.loggingTemplate=MessageTemplate.MatchTopic(myCAgent.loggingTopic);
			} else {
				this.block();
			}
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			ACLMessage logMsg=new ACLMessage(ACLMessage.INFORM);

			logMsg.addReceiver(ContainerAgent.this.loggingTopic);
			logMsg.setContent(this.logMessageText);
			this.myAgent.send(logMsg);

		}
	}

	/**
	 * 
	 */
	public static final Integer LOGGING_ALL=5; //just information for the user
	public static final Integer LOGGING_INFORM=4; //just information for the user, debugging
	public static final Integer LOGGING_NOTICE=3; //failure in harbour processing, but no complecations with program
	public static final Integer LOGGING_ERROR=2; //heavy error, possibly bug
	public static final Integer LOGGING_DEBUG=1; //default setting for short time debugging
	public static final Integer LOGGING_NONE=0; //No error logging
	private static final Integer SYSTEM_LOGGING=ContainerAgent.LOGGING_NOTICE;
	private static final Integer GUI_LOGGING=ContainerAgent.LOGGING_NOTICE;
	public static final Integer LOGGING_DEFAULT=LOGGING_DEBUG; //default setting for short time debugging


	private static final long serialVersionUID=202350816610492193L;
	public static final Codec codec=new XMLCodec();
	public static final Ontology ontology=ContainerTerminalOntology.getInstance();

	public static List addToList(List base,List addition){
		Iterator it=addition.iterator();
		while(it.hasNext()){
			base.add(it.next());
		}
		return base;
	}

	public static AID[] agentDescToAIDArray(DFAgentDescription[] agents){
		AID[] result=new AID[agents.length];
		for(int i=0;i < agents.length;i++){
			result[i]=agents[i].getName();
		}
		return result;
	}

	public static AnnounceLoadStatus getLoadStatusAnnouncement(TransportOrderChain curTOC,String content){
		AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
		loadStatus.setLoad_status(content);
		loadStatus.setLoad_offer(curTOC);
		return loadStatus;
	}

	/*
	 * Überprüft, ob Domain inQuestion in Domain suspectedIn liegt
	 */
	public static Integer matchDomainsTransitive(Domain inQuestion,Domain suspectedIn){
//		System.out.println(inQuestion.getClass() + " in " + suspectedIn.getClass() + "?");
		if(inQuestion.getClass() == suspectedIn.getClass()){
			return 2; //passt genau
		}
		if(inQuestion.getLies_in() != null){
			Integer match=ContainerAgent.matchDomainsTransitive(inQuestion.getLies_in(),suspectedIn);
			if(match>-1){
				return  match + 1;
			}
		}
		return -1; //passt gar nicht
	}

	public static List toAIDList(AID[] input){
		List output=new ArrayList();
		for(AID aid: input){
			output.add(aid);
		}
		return output;
	}

	protected String targetAgentServiceType=null;

	protected DFAgentDescription targetDFAgentDescription=null;

	protected Domain targetAbstractDomain=null;

	protected AID loggingTopic=null;

	protected String ownServiceType;

	protected ContainerHolder ontologyRepresentation;

	public ContainerHolder getOntologyRepresentation(){
		return this.ontologyRepresentation;
	}

	public Integer lengthOfProposeQueue=2;

	public ContainerAgent(){
		this.ownServiceType="container-handling";
		this.ontologyRepresentation=new ContainerHolder();
	}

	public ContainerAgent(String serviceType){
		this();
		this.ownServiceType=serviceType;
	}

	public ContainerAgent(String serviceType,ContainerHolder ontologyRepresentation){
		this(serviceType);
		this.ontologyRepresentation=ontologyRepresentation;
	}

	public void addToContractors(List newContractors){
		ContainerAgent.addToList(this.ontologyRepresentation.getContractors(),newContractors);
	}

	public Boolean aquireContainer(TransportOrderChain targetContainer){ //eigentlicher Vorgang des Container-Aufnehmens
		//		ontologyRepresentation.getAdministers().addConsists_of(targetContainer); //Container zu Auftragsbuch hinzufügen
		if(this.changeTOCState(targetContainer,new Administered()) == null){
			this.echoStatus("ERROR: war noch nicht in States",targetContainer,ContainerAgent.LOGGING_ERROR);
		}
		//physikalische Aktionen

		BlockAddress destination=this.getEmptyBlockAddress(); //zieladresse besorgen
		destination.setLocates(targetContainer.getTransports());
		this.ontologyRepresentation.getContains().addIs_filled_with(destination); //Container mit neuer BlockAdress in eigene BayMap aufnehmens
		//		echoStatus("Nun wird der Container von mir transportiert");
		if(this.changeTOCState(targetContainer,new Administered()) instanceof Administered){ //Auftrag aus Liste von Bewerbungen streichen
			return true;
		}
		this.echoStatus("ERROR: Ausschreibung, auf die ich mich beworben habe, nicht gefunden.",targetContainer,ContainerAgent.LOGGING_ERROR);
		return false;
	}

	public TransportOrder calculateEffort(TransportOrder call){
		Random RandomGenerator=new Random();
		call.setTakes(RandomGenerator.nextFloat() + this.getBayUtilization());
		return call;
	}

	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC){
		return this.changeTOCState(needleTOC,null);
	}

	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState){
		return this.changeTOCState(needleTOC,toState,false);
	}

	public TransportOrderChainState changeTOCState(TransportOrderChain needleTOC,TransportOrderChainState toState,Boolean addRemoveSwitch){
		Iterator queue=this.ontologyRepresentation.getAllContainer_states();
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
			this.ontologyRepresentation.getContainer_states().add(state);
			return state.getState();
		}
		return null; //nicht gefunden
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
			Behaviour DFsubscribePreparer;

			try{
				DFsubscribePreparer=new prepareSubscribeToDF(this,this.getClass().getMethod("addToContractors",List.class),this.targetDFAgentDescription);
				this.addBehaviour(DFsubscribePreparer);
			}catch(SecurityException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NoSuchMethodException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return this.ontologyRepresentation.getContractors();
	}

	@Deprecated
	public void echoStatus(String statusMessage){
		this.echoStatus(statusMessage,ContainerAgent.LOGGING_DEFAULT);
	}

	public void echoStatus(String statusMessage,Integer severity){
		statusMessage=this.getAID().getLocalName() + ": " + statusMessage;
		if(severity <= ContainerAgent.GUI_LOGGING){
			this.addBehaviour(new sendLogMessage(this,statusMessage + "\n"));
		}
		if(severity <= ContainerAgent.SYSTEM_LOGGING){
			System.out.println(statusMessage);
		}
	}

	@Deprecated
	public void echoStatus(String statusMessage,TransportOrderChain subject){
		this.echoStatus(statusMessage,subject,ContainerAgent.LOGGING_INFORM);
	}

	public void echoStatus(String statusMessage,TransportOrderChain subject,Integer severity){
		String additionalText="";
		if(subject != null){
			additionalText=" BIC=" + subject.getTransports().getBic_code();
		}
		this.echoStatus(statusMessage + additionalText,severity);
	}

	public AgentAction extractAction(ACLMessage msg){
		return extractAction(this,msg);
	}
	
	public static AgentAction extractAction(Agent containerAgent,ACLMessage msg){
		AgentAction act=null;
		try{
			act=(AgentAction) containerAgent.getContentManager().extractContent(msg);
		}catch(UngroundedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return act;
	}

	public void fillMessage(ACLMessage msg,AgentAction agact){
		ContainerAgent.fillMessage(msg,agact,this);

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
				} else {
					continue;
				}
			}
			
			/*
			if( matchIncoming && this.matchAID(curTO.getEnds_at())){ //exactly for this agent
				return curTO;
			}
			*/

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

	public DFAgentDescription[] getAgentsFromDF(DFAgentDescription dfd){
		return getAgentsFromDF(dfd,this);
	}
	
	public static DFAgentDescription[] getAgentsFromDF(DFAgentDescription dfd,Agent searcher){
		try{
			DFAgentDescription[] result=DFService.search(searcher,dfd);
			return result;
		}catch(FIPAException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static DFAgentDescription[] getAgentsFromDF(String serviceType,Agent searcher){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		return getAgentsFromDF(dfd,searcher);
	}
	
	public DFAgentDescription[] getAgentsFromDF(String serviceType){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		return getAgentsFromDF(serviceType,this);
	}

	public AID[] getAIDsFromDF(String serviceType){
		return getAIDsFromDF(serviceType,this);
	}
	
	public static AID[] getAIDsFromDF(String serviceType,Agent searcher){
		DFAgentDescription[] agents=getAgentsFromDF(serviceType,searcher);
		return ContainerAgent.agentDescToAIDArray(agents);
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
		BlockAddress empty=new BlockAddress();
		empty.setX_dimension(0);
		empty.setY_dimension(0);
		empty.setZ_dimension(0);
		return empty;
	}

	public AID getFirstAIDFromDF(String serviceType){
		return getFirstAIDFromDF(serviceType,this);
	}
	
	public static AID getFirstAIDFromDF(String serviceType,Agent searcher){
		AID[] aids=getAIDsFromDF(serviceType,searcher);
		if(aids.length > 0){
			return aids[0];
		}else{
			System.out.println("Kein Agent der Art vorhanden.");

//			searcher.echoStatus("Kein Agent der Art vorhanden.",ContainerAgent.LOGGING_NOTICE);
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
			act.setLoad_offer(curTOC);
			// put this TOC in the queue of TOC proposed for
			this.changeTOCState(curTOC,new ProposedFor(),true);
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
		Iterator queue=this.ontologyRepresentation.getAllContainer_states();
		while(queue.hasNext()){
			TOCHasState queuedTOCState=(TOCHasState) queue.next();
			if(needleState.getClass() == queuedTOCState.getState().getClass()){
				return queuedTOCState.getSubjected_toc();
			}
		}
		return null;
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
		} else{
			return -1;
		}
	}

	public void register(ServiceDescription sd){
		this.register(new ServiceDescription[] {sd});
	}

	public void register(ServiceDescription[] sd){
		DFAgentDescription dfd=new DFAgentDescription();
		dfd.setName(this.getAID());
		for(ServiceDescription serviceDescription: sd){
			dfd.addServices(serviceDescription);
		}
		try{
			DFService.register(this,dfd);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
	}

	public void register(String serviceType){
		String[] allSTs=new String[] {"container-handling", serviceType};
		this.register(allSTs);
	}

	public void register(String[] serviceType){
		ServiceDescription[] allSDs=new ServiceDescription[serviceType.length];
		for(int i=0;i < serviceType.length;i++){
			String sT=serviceType[i];
			ServiceDescription sd=new ServiceDescription();
			sd.setType(sT);
			sd.setName(this.getLocalName());
			allSDs[i]=sd;
		}
		this.register(allSDs);
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

	public boolean removeContainerFromBayMap(TransportOrderChain load_offer){
		//		echoStatus("removeContainerFromBayMap:",load_offer);
		Iterator allContainers=this.ontologyRepresentation.getContains().getIs_filled_with().iterator();
		while(allContainers.hasNext()){
			Container curContainer=((BlockAddress) allContainers.next()).getLocates();
			//			echoStatus("curContainerID: "+curContainer.getId()+"load_offerID: "+load_offer.getTransports().getId());
			if(curContainer.getBic_code().equals(load_offer.getTransports().getBic_code())){
				allContainers.remove();
				//				echoStatus("Container found and removed.",load_offer);
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

	@Override
	protected void setup(){
		this.getContentManager().registerLanguage(ContainerAgent.codec);
		this.getContentManager().registerOntology(ContainerAgent.ontology);

		//register self at DF
		echoStatus(this.ownServiceType,LOGGING_INFORM);
		this.register(this.ownServiceType);

		if(this.ontologyRepresentation.getContains() == null){
			BayMap LoadBay=new BayMap();
			LoadBay.setX_dimension(1);
			LoadBay.setY_dimension(1);
			LoadBay.setZ_dimension(1);
			this.ontologyRepresentation.setContains(LoadBay);
		}

		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(this.targetAgentServiceType);
		dfd.addServices(sd);
		this.targetDFAgentDescription=dfd;

		try{
			TopicManagementHelper tmh;
			tmh=(TopicManagementHelper) this.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.loggingTopic=tmh.createTopic("container-harbour-logging");
		}catch(ServiceException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.determineContractors();
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new listenForOntRepRequest(this,MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestOntologyRepresentation())))));
	}

	@Override
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void enableForCommunication(Agent toBeEnabled){
		toBeEnabled.getContentManager().registerLanguage(ContainerAgent.codec);
		toBeEnabled.getContentManager().registerOntology(ContainerAgent.ontology);
	}

	/**
	 * @param containerAgent
	 * @param msg
	 * @param agact
	 */
	public static void fillMessage(ACLMessage msg,AgentAction agact,Agent containerAgent){
		msg.setLanguage(ContainerAgent.codec.getName());
		msg.setOntology(ContainerAgent.ontology.getName());
		try{
			Action act=new Action(containerAgent.getAID(),agact);
//			this.getContentManager().fillContent(msg,act);

			containerAgent.getContentManager().fillContent(msg,agact);
		}catch(UngroundedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}