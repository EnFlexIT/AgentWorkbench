/**
 * @author Hanno - Felix Wagner, 06.03.2010
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

import contmas.behaviours.carryOutPlanning;
import contmas.behaviours.listenForExecuteAppointmentReq;
import contmas.behaviours.receiveLoadOrders;
import contmas.behaviours.unload;
import contmas.interfaces.TransportOrderHandler;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.Apron;
import contmas.ontology.Domain;
import contmas.ontology.Street;
import contmas.ontology.YardArea;

public class ApronAgent extends StaticContainerAgent implements TransportOrderHandler,TransportOrderOfferer{
	private static final long serialVersionUID=4904170788284891727L;

	public ApronAgent(Apron ontologyRepresentation){
		super("short-time-storage",ontologyRepresentation);
		this.targetAgentServiceType="container-distributing";
		this.targetAbstractDomain=new Street();
		this.targetAbstractDomain.setId("StraddleCarrierStreet"); //TODO hardcoded
	}
	
	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
		this.offerTransportOrder();
	}
	
	@Override
	public void processHarbourLayout(Domain current_harbour_layout){
		super.processHarbourLayout(current_harbour_layout);
		YardArea finalTarget=new YardArea();
		finalTarget.setId("StorageYard");
		
		addDefaultRoute(finalTarget);
	}

	@Override
	public void handleTransportOrder(){
		this.addBehaviour(new receiveLoadOrders(this));
		this.addBehaviour(new listenForExecuteAppointmentReq(this));
	}

	@Override
	public void offerTransportOrder(){
		this.addBehaviour(new unload(this));
		this.addBehaviour(new carryOutPlanning(this));
	}
}