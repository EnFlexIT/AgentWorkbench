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

import contmas.behaviours.*;
import contmas.interfaces.TransportOrderHandler;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.ApronArea;
import contmas.ontology.Crane;

public class CraneAgent extends ActiveContainerAgent implements TransportOrderHandler,TransportOrderOfferer{
	private static final long serialVersionUID= -193864979181761694L;

	public CraneAgent(Crane ontologyRepresentation){
		super("craning",ontologyRepresentation);
		this.targetAgentServiceType="short-time-storage";
		this.targetAbstractDomain=new ApronArea();
		this.targetAbstractDomain.setId("CraneApron");
	}

	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
		this.offerTransportOrder();
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
		this.addBehaviour(new TacticalStationer(this));

	}
}