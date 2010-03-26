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
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;

import contmas.behaviours.*;
import contmas.ontology.*;

public class HarborMasterAgent extends ContainerAgent implements OntRepProvider, OntRepRequester{
	private HashMap<AID, ContainerHolder> activeContainerHolders=new HashMap<AID, ContainerHolder>();
	private HashMap<AID, Behaviour> ontRepInquieries=new HashMap<AID, Behaviour>();

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

	public void processOntologyRepresentation(ContainerHolder ontRep, AID agent){
		this.activeContainerHolders.put(agent,ontRep);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID= -2094037480381093187L;

	public HarborMasterAgent(){
		super("harbor-managing");
	}

	@Override
	protected void setup(){
		super.setup();

		this.setupEnvironment();
		this.addBehaviour(new listenForEnroll(this));
		this.addBehaviour(new offerCraneList(this));
		this.addBehaviour(new listenForOntRepRequest(this));
		this.addBehaviour(new listenForStartAgentReq(this));
	}

	public ContainerHolder getOntologyRepresentation(AID inQuestion){

		ContainerHolder ontRep=this.getCachedOntRep(inQuestion);
		if(ontRep == null){
			if( !this.ontRepInquieries.containsKey(inQuestion)){
					Behaviour b=new getOntologyRepresentation(this,inQuestion);
					this.addBehaviour(b);
					this.ontRepInquieries.put(inQuestion,b);
			}
			return null;
		}else{
			this.ontRepInquieries.remove(inQuestion);
			return ontRep;
		}
	}

	protected void setupEnvironment(){
		AgentContainer c=this.getContainerController();
		AgentController a;
		try{
			Crane ontologyRepresentation=new Crane();
			Domain terminalArea=new Land();
			Domain habitat=new Rail();
			habitat.setLies_in(terminalArea);
			ontologyRepresentation.setLives_in(habitat);
			Domain capability=new Rail();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Street();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Sea();
			ontologyRepresentation.addCapable_of(capability);
			capability=new ApronArea();
			ontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("Crane-#1",new CraneAgent(ontologyRepresentation));
			a.start();

			ontologyRepresentation=new Crane();
			ontologyRepresentation.setLives_in(habitat);
			capability=habitat;
			ontologyRepresentation.addCapable_of(capability);
			capability=new Street();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Sea();
			ontologyRepresentation.addCapable_of(capability);
			capability=new ApronArea();
			ontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("Crane-#2",new CraneAgent(ontologyRepresentation));
			a.start();

			Apron ApronontologyRepresentation=new Apron();
			habitat=new ApronArea();
			habitat.setLies_in(terminalArea);
			ApronontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("Apron",new ApronAgent(ApronontologyRepresentation));
			a.start();

			StraddleCarrier StraddleCarrierontologyRepresentation=new StraddleCarrier();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			StraddleCarrierontologyRepresentation.setLives_in(habitat);
			capability=new Rail();
			StraddleCarrierontologyRepresentation.addCapable_of(capability);
			capability=new Street();
			StraddleCarrierontologyRepresentation.addCapable_of(capability);
			capability=new YardArea();
			StraddleCarrierontologyRepresentation.addCapable_of(capability);
			capability=new ApronArea();
			StraddleCarrierontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("StraddleCarrier-#1",new StraddleCarrierAgent(StraddleCarrierontologyRepresentation));
			a.start();

			Yard YardontologyRepresentation=new Yard();
			habitat=new YardArea();
			habitat.setLies_in(terminalArea);
			YardontologyRepresentation.setLives_in(habitat);
			YardontologyRepresentation.setContains(new BayMap());
			YardontologyRepresentation.getContains().setX_dimension(1);
			YardontologyRepresentation.getContains().setY_dimension(1);
			YardontologyRepresentation.getContains().setZ_dimension(1);

			a=c.acceptNewAgent("Yard",new YardAgent(YardontologyRepresentation));
			a.start();

			/*
			AGV AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #1",new AGVAgent(AGVontologyRepresentation));
			a.start();

			AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #2",new AGVAgent(AGVontologyRepresentation));
			a.start();

			AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #3",new AGVAgent(AGVontologyRepresentation));
			a.start();
			*/

		}catch(StaleProxyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see contmas.agents.OntRepProvider#getOntologyRepresentation()
	 */
	@Override
	public ContainerHolder getOntologyRepresentation(){
		return null;
	}
}