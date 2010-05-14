/**
 * @author Hanno - Felix Wagner, 17.03.2010
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

package contmas.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import contmas.agents.ContainerAgent;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.LoadingReceiver;
import contmas.interfaces.OntRepRequester;
import contmas.ontology.*;

public class striveForLoading extends ParallelBehaviour implements DFSubscriber,OntRepRequester,LoadingReceiver{
	/**
	 * 
	 */
	private static final long serialVersionUID=3676523464111472932L;
	private List allSourceAgents=new ArrayList();

	public striveForLoading(Agent a){
		super(a,ParallelBehaviour.WHEN_ALL);

		this.addSubBehaviour(new subscribeToDF(a,"long-term-transporting"));

	}

	/* (non-Javadoc)
	 * @see contmas.behaviours.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		if( !remove){
			Iterator avIt=updatedAgents.iterator();
			ContainerAgent.addToList(this.allSourceAgents,updatedAgents);
			while(avIt.hasNext()){
				AID curAvAg=(AID) avIt.next();
				this.addSubBehaviour(new requestOntologyRepresentation(this.myAgent,curAvAg,((ContainerAgent) this.myAgent).getHarbourMaster()));
			}
		}
	}

	/* (non-Javadoc)
	 * @see contmas.agents.OntRepRequester#processOntologyRepresentation(contmas.ontology.ContainerHolder, jade.core.AID)
	 */
	@Override
	public void processOntologyRepresentation(ContainerHolder recieved,AID agent){
		Iterator allContainer=recieved.getAllContainer_states();
		while(allContainer.hasNext()){
			Container curCont=((TOCHasState) allContainer.next()).getSubjected_toc().getTransports();
			((ContainerAgent) this.myAgent).echoStatus("Ship has Container available: " + curCont.getBic_code(),ContainerAgent.LOGGING_NOTICE);
		}
		this.addSubBehaviour(new requestRandomLoadSequence(this.myAgent,recieved.getContains(),recieved.getContainer_states(),agent));
	}

	/* (non-Javadoc)
	 * @see contmas.behaviours.LoadSequenceProvider#getLoadSequence()
	 */
	@Override
	public ContainerHolder processLoadSequence(LoadList loadSequence,AID agentForWhich){

		this.addSubBehaviour(new initiateLoadingStream(this.myAgent,loadSequence,agentForWhich));

		return null;
	}
}