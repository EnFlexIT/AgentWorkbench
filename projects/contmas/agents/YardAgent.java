/**
 * @author Hanno - Felix Wagner, 11.03.2010
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

import contmas.behaviours.receiveLoadOrders;
import contmas.behaviours.requestBlockAddress;
import contmas.behaviours.striveForLoading;
import contmas.interfaces.OptimisationClient;
import contmas.interfaces.TransportOrderHandler;
import contmas.ontology.BayMap;
import contmas.ontology.BlockAddress;
import contmas.ontology.TransportOrderChain;
import contmas.ontology.Yard;

public class YardAgent extends StaticContainerAgent implements TransportOrderHandler, OptimisationClient{
	private static final long serialVersionUID= -3774026871349327373L;

	public YardAgent(Yard ontologyRepresentation){
		super("container-storing",ontologyRepresentation);
		this.targetAgentServiceType="";
		this.targetAbstractDomain=null;
	}
	
	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
		this.addBehaviour(new striveForLoading(this));
	}

	@Override
	public void handleTransportOrder(){
		this.addBehaviour(new receiveLoadOrders(this));
	}
	
	/* (non-Javadoc)
	 * @see contmas.interfaces.OptimisationClient#getOptimizedBayMap(contmas.ontology.BayMap)
	 */
	@Override
	public BlockAddress getEmptyBlockAddressFor(BayMap rawBayMap,TransportOrderChain subject){
		BlockAddress outputMemory=new BlockAddress();
		outputMemory.setX_dimension(-1);
		outputMemory.setY_dimension(-1);
		outputMemory.setZ_dimension(-1);

		addBehaviour(new requestBlockAddress(this,rawBayMap,getAllHeldContainers(true),subject, outputMemory));
		return outputMemory;
	}
	@Override
	public BlockAddress getEmptyBlockAddress(TransportOrderChain subject){
		BlockAddress ownSolution=super.getEmptyBlockAddress(subject);
		BlockAddress optimisedSolution=getEmptyBlockAddressFor(getOntologyRepresentation().getContains(),subject);
		return optimisedSolution;
	}
}