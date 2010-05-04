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

import contmas.agents.ContainerAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.interfaces.MoveableAgent;
import contmas.ontology.Movement;
import contmas.ontology.Phy_Position;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class executeMovements extends CyclicBehaviour{

	MoveableAgent myAgent;
	ContainerAgent myCAgent;
	
	public executeMovements(Agent a){
		super(a);
		myAgent=(MoveableAgent) a;
		myCAgent=(ContainerAgent) a;
	}
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		myCAgent.echoStatus("Executing pending movements now.");

		List movementList=myAgent.getPendingMovements();
		Iterator iter=movementList.iterator();
		while(iter.hasNext()){
			
			Movement nextMove=(Movement) iter.next();
			myCAgent.echoStatus("pending movement to "+StraddleCarrierAgent.positionToString(nextMove.getMove_to()));
			Phy_Position intermediate=myAgent.interpolatePosition(nextMove);
			myAgent.setAt(intermediate);
			if(intermediate.getPhy_x()==nextMove.getMove_to().getPhy_x() && intermediate.getPhy_y()==nextMove.getMove_to().getPhy_y()){
				iter.remove();
			} else {
				block(1000);
			}
			
//			this.restart();
		}
		block();
	}
}