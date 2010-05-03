/**
 * @author Hanno - Felix Wagner, 22.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.agents;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
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
import contmas.behaviours.sendLogMessage;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.ContainerTerminalOntology;
import contmas.ontology.Domain;
import contmas.ontology.TransportOrderChain;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class ContainerAgent extends Agent{

	/**
	 * 
	 */
	public static final Integer LOGGING_ALL=5;
	public static final Integer LOGGING_INFORM=4;
	public static final Integer LOGGING_NOTICE=3;
	public static final Integer LOGGING_ERROR=2;
	public static final Integer LOGGING_DEBUG=1;
	public static final Integer LOGGING_NONE=0;
	private static final Integer SYSTEM_LOGGING=ContainerAgent.LOGGING_NOTICE;
	private static final Integer GUI_LOGGING=ContainerAgent.LOGGING_NOTICE;
	private static final Integer LOGGING_DEFAULT=ContainerAgent.LOGGING_DEBUG;
	private static final long serialVersionUID=202350816610492193L;
	private static final Codec codec=new XMLCodec();
//	private static final Codec codec=new LEAPCodec();
//	private static final Codec codec=new SLCodec();

	private static final Ontology ontology=ContainerTerminalOntology.getInstance();

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
			if(agents[i] != null){
				result[i]=agents[i].getName();
			}
		}
		return result;
	}

	public static AnnounceLoadStatus getLoadStatusAnnouncement(TransportOrderChain curTOC,String content){
		AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
		loadStatus.setLoad_status(content);
		loadStatus.setCorresponds_to(curTOC);
		return loadStatus;
	}



	public static List toAIDList(AID[] input){
		List output=new ArrayList();
		for(int i=0;i < input.length;i++){
			AID aid=input[i];
			if(aid != null){
				output.add(aid);
			}

		}
		return output;
	}

	public static AgentAction extractAction(Agent containerAgent,ACLMessage msg){
		AgentAction agact=null;
		Action act=null;

		try{
			act=(Action) containerAgent.getContentManager().extractContent(msg);
			agact=(AgentAction) act.getAction();
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
		return agact;
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
		return ContainerAgent.getAgentsFromDF(dfd,searcher);
	}

	public static AID[] getAIDsFromDF(String serviceType,Agent searcher){
		DFAgentDescription[] agents=ContainerAgent.getAgentsFromDF(serviceType,searcher);
		return ContainerAgent.agentDescToAIDArray(agents);
	}

	public static AID getFirstAIDFromDF(String serviceType,Agent searcher){
		AID[] aids=ContainerAgent.getAIDsFromDF(serviceType,searcher);
		if(aids.length > 0){
			return aids[0];
		}else{
			String ermsg="Kein Agent der Art '" + serviceType + "' vorhanden.";
			if(searcher instanceof ContainerAgent){
				((ContainerAgent) searcher).echoStatus(ermsg,ContainerAgent.LOGGING_NOTICE);
			}else{
				System.out.println(searcher.getLocalName() + ": " + ermsg);
			}
		}
		return null;
	}

	public static void enableForCommunication(Agent toBeEnabled){
		toBeEnabled.getContentManager().registerLanguage(ContainerAgent.codec);
		toBeEnabled.getContentManager().registerOntology(ContainerAgent.ontology);
	}

	/**
	 * @param a
	 * @param msg
	 * @param agact
	 */
	public static void fillMessage(ACLMessage msg,AgentAction agact,Agent a){
		msg.setLanguage(ContainerAgent.codec.getName());
		msg.setOntology(ContainerAgent.ontology.getName());
		try{
//				a.getContentManager().fillContent(msg,agact);

			Action act=new Action(a.getAID(),agact);
			a.getContentManager().fillContent(msg,act);

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

	protected String ownServiceType;
	private AID harbourMaster=null;

	public AID getHarbourMaster(){
		if(this.harbourMaster == null){

		}
		return this.harbourMaster;
	}

	public AID getRandomGenerator(){
		if(this.randomGenerator == null){

		}
		return this.randomGenerator;
	}

	private AID randomGenerator=null;

	/**
	 * 
	 */
	public ContainerAgent(){
		this("container-handling");
	}

	public ContainerAgent(String serviceType){
		this.ownServiceType=serviceType;
	}

	@Deprecated
	public void echoStatus(String statusMessage){
		this.echoStatus(statusMessage,ContainerAgent.LOGGING_DEFAULT);
	}

	@Override
	protected void setup(){
		ContainerAgent.enableForCommunication(this);

		//register self at DF
		this.echoStatus(this.ownServiceType,ContainerAgent.LOGGING_INFORM);
		this.register(this.ownServiceType);

		//look for RandomGeneratorAgent
		this.randomGenerator=this.getFirstAIDFromDF("random-generation");
		this.harbourMaster=this.getFirstAIDFromDF("harbor-managing");

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

	public DFAgentDescription[] getAgentsFromDF(DFAgentDescription dfd){
		return ContainerAgent.getAgentsFromDF(dfd,this);
	}

	public DFAgentDescription[] getAgentsFromDF(String serviceType){
		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		return ContainerAgent.getAgentsFromDF(serviceType,this);
	}

	public AID[] getAIDsFromDF(String serviceType){
		return ContainerAgent.getAIDsFromDF(serviceType,this);
	}

	public AID getFirstAIDFromDF(String serviceType){
		return ContainerAgent.getFirstAIDFromDF(serviceType,this);
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

	@Override
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AgentAction extractAction(ACLMessage msg){
		return ContainerAgent.extractAction(this,msg);
	}

	public void fillMessage(ACLMessage msg,AgentAction agact){
		ContainerAgent.fillMessage(msg,agact,this);
	}
}