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
import jade.proto.AchieveREResponder;
import jade.util.leap.List;
import contmas.ontology.AGV;
import contmas.ontology.PassiveContainerHolder;

/**
 * @author Hanno - Felix Wagner
 */
public class AGVAgent extends PassiveContainerAgent implements TransportOrderHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID= -1129007275241247212L;
	public Integer lengthOfProposeQueue=0;

	public AGVAgent(){
		this(new AGV());
	}

	public AGVAgent(PassiveContainerHolder ontologyRepresentation){
		super("container-distributing",ontologyRepresentation);
	}

	@Override
	public List determineContractors(){//AGV kann noch keine Container loswerden
		return null;
	}

	public void handleTransportOrder(){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		this.addBehaviour(new receiveLoadOrders(this,mt));
	}

	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
	}
}
