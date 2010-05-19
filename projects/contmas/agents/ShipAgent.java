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

import jade.util.leap.List;
import contmas.behaviours.carryOutPlanning;
import contmas.behaviours.enrollAtHarbor;
import contmas.behaviours.listenForLoadingStreamIni;
import contmas.behaviours.unload;
import contmas.interfaces.TransportOrderOfferer;
import contmas.ontology.Rail;
import contmas.ontology.Ship;

public class ShipAgent extends StaticContainerAgent implements TransportOrderOfferer{
	private static final long serialVersionUID=6800105012920938089L;

	public ShipAgent(Ship ontologyRepresentation){
		super("long-term-transporting",ontologyRepresentation);
		this.targetAgentServiceType="craning"; //unneccesary, because done by enroll behaviour
		this.targetAbstractDomain=new Rail(); //TODO should be done by enroll behaviour
		this.targetAbstractDomain.setId("CraneRails"); //TODO hardcoded
	}

	@Override
	protected void setup(){
		super.setup();
		this.addBehaviour(new enrollAtHarbor(this));
		this.addBehaviour(new listenForLoadingStreamIni(this));
		offerTransportOrder();
//		this.addBehaviour(new scheduleUnloadStart(this));
	}
	
	@Override
	public void offerTransportOrder(){
//		this.addBehaviour(new unload(this));
		this.addBehaviour(new carryOutPlanning(this));
	}
	
	@Override
	public List determineContractors(){
		return this.ontologyRepresentation.getContractors();
	}
}