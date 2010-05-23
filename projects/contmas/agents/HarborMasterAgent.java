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

import jade.util.leap.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import contmas.behaviours.*;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.OntRepProvider;
import contmas.interfaces.OntRepRequester;
import contmas.main.HarbourSetup;
import contmas.ontology.ActiveContainerHolder;
import contmas.ontology.ContainerHolder;
import contmas.ontology.Domain;
import contmas.ontology.StartNewContainerHolder;

public class HarborMasterAgent extends ContainerAgent implements OntRepProvider,OntRepRequester,DFSubscriber{
	private HashMap<AID, ContainerHolder> activeContainerHolders=new HashMap<AID, ContainerHolder>();
	private HashMap<AID, Behaviour> ontRepInquieries=new HashMap<AID, Behaviour>();
	private HarbourSetup harbourSetup=null;
	public List allContainerHandlers=new ArrayList();
	private static final long serialVersionUID= -2094037480381093187L;

	public HarborMasterAgent(){
		super("harbor-managing");
	}

	@Override
	protected void setup(){
		super.setup();

		this.addBehaviour(new setupEnvironment(this));
		this.addBehaviour(new listenForEnroll(this));
		this.addBehaviour(new listenForOntRepReq(this));
		this.addBehaviour(new listenForStartAgentReq(this));
		this.addBehaviour(new listenForHarbourAreaReq(this));
		this.addBehaviour(new subscribeToDF(this,"container-handling"));
		this.addBehaviour(new listenForEnvironmentResetRequest(this));
	}


	private HarbourSetup getHarbourSetup(){
		String[] args=(String[]) this.getArguments();
		if(this.harbourSetup == null){
			this.harbourSetup=HarbourSetup.getInstance(args[0]);
		}
		return this.harbourSetup;
	}

	public boolean isAlreadyCached(String lookForAgent){
		if(this.activeContainerHolders.get(lookForAgent) != null){
			return true;
		}
		return false;
	}

	public ContainerHolder getCachedOntRep(AID lookForAgent){
		ContainerHolder lookedForOntRep=this.activeContainerHolders.get(lookForAgent);
		this.activeContainerHolders.remove(lookForAgent);//no real caching
		return lookedForOntRep;
	}

	public void processOntologyRepresentation(ContainerHolder ontRep,AID agent){
		this.activeContainerHolders.put(agent,ontRep);
	}


	public ContainerHolder getOntologyRepresentation(AID inQuestion){

		ContainerHolder ontRep=this.getCachedOntRep(inQuestion);
		if(ontRep == null){
			if( !this.ontRepInquieries.containsKey(inQuestion)){
				Behaviour b=new requestOntologyRepresentation(this,inQuestion);
				this.addBehaviour(b);
				this.ontRepInquieries.put(inQuestion,b);
			}
			return null;
		}else{
			this.ontRepInquieries.remove(inQuestion);
			return ontRep;
		}
	}

	public Domain getHarbourArea(){
		return this.getHarbourSetup().getHarbourArea();
	}

	public void setupEnvironment(){

		ContainerHolder[] ontReps=this.getHarbourSetup().getOntReps();
		for(int i=0;i < ontReps.length;i++){
			ContainerHolder containerHolder=ontReps[i];

			StartNewContainerHolder act=new StartNewContainerHolder();
			act.setName(containerHolder.getLocalName());
			HarbourSetup.reduceCH(containerHolder);

			act.setTo_be_added(containerHolder);
			
			act.setRandomize(false);
			act.setPopulate(false);
			this.addBehaviour(new requestStartAgent(this,this.getAID(),act));
		}
	}
	
	public void clearHarbourEnvironment(){
		this.addBehaviour(new setupEnvironment(this));

		Iterator allActiveAgents=allContainerHandlers.iterator();
		while(allActiveAgents.hasNext()){
			this.addBehaviour(new killAgent(this,(AID) allActiveAgents.next()));
		}
	}

	/* (non-Javadoc)
	 * @see contmas.agents.OntRepProvider#getOntologyRepresentation()
	 */
	@Override
	public ContainerHolder getOntologyRepresentation(){
		ContainerHolder test=new ContainerHolder();
		test.setLives_in(this.getHarbourArea());
		return test;
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(allContainerHandlers,updatedAgents);
		}else{
			ContainerAgent.removeFromList(allContainerHandlers,updatedAgents);
		}
	}
}