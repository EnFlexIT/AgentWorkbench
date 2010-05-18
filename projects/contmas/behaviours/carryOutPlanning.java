/**
 * @author Hanno - Felix Wagner, 03.05.2010
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

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.MoveableAgent;
import contmas.ontology.PlannedOut;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;

public class carryOutPlanning extends CyclicBehaviour{
	MoveableAgent myAgent;
	ContainerHolderAgent myCAgent;

	public carryOutPlanning(Agent a){
		super(a);
		myAgent=(MoveableAgent) a;
		myCAgent=(ContainerHolderAgent) a;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		TransportOrderChain curTOC=myCAgent.getSomeTOCOfState(new PlannedOut());
		if(curTOC != null){
			TransportOrder curTO=((PlannedOut) myCAgent.touchTOCState(curTOC)).getLoad_offer();
			myCAgent.addBehaviour(new requestExecuteAppointment(myCAgent,curTOC,curTO));
			myCAgent.echoStatus("added plan execution");
		}
		block();
	}
}