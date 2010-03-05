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

import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.List;
import contmas.ontology.Crane;

public class CraneAgent extends ActiveContainerAgent implements TransportOrderHandler,TransportOrderOfferer{

	private static final long serialVersionUID= -193864979181761694L;

	public CraneAgent(){
		this(new Crane());
	}

	public CraneAgent(Crane ontologyRepresentation){
		super("craning",ontologyRepresentation);
	}

	@Override
	public List determineContractors(){
		if(this.ontologyRepresentation.getContractors().isEmpty()){
			this.ontologyRepresentation.setContractors(ContainerAgent.toAIDList(this.getAIDsFromDF("container-distributing")));
		}
		return this.ontologyRepresentation.getContractors();
	}

	public void handleTransportOrder(){
		MessageTemplate mt=ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		this.addBehaviour(new receiveLoadOrders(this,mt));
	}

	@Override
	public void offerTransportOrder(){
		// TODO Auto-generated method stub
	}

	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
		this.offerTransportOrder();
	}

}