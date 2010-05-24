/**
 * @author Hanno - Felix Wagner, 24.05.2010
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

import contmas.agents.ActiveContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.interfaces.TacticalMemorizer;
import contmas.ontology.Phy_Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class TacticalStationer extends TickerBehaviour{
	private static Integer TICK_INTERVAL=1000;
	ContainerHolderAgent myAgent;
	TacticalMemorizer owner;

	/**
	 * @param a
	 * @param period
	 */
	public TacticalStationer(Agent a){
		super(a,TICK_INTERVAL);
		myAgent=(ContainerHolderAgent) a;
		owner=(TacticalMemorizer) a;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.TickerBehaviour#onTick()
	 */
	@Override
	protected void onTick(){
		List tacticalTargets=owner.getTacticalTargets();
		Phy_Position tacPos=null;
		for(Iterator iterator=tacticalTargets.iterator();iterator.hasNext();){
			Phy_Position position=(Phy_Position) iterator.next();
			if(tacPos==null){
				tacPos=position;
			}
			if(tacPos.getPhy_x()==position.getPhy_x() && tacPos.getPhy_y()==position.getPhy_y()){
				iterator.remove();
			}
		}
		if(tacPos!=null){
			((ActiveContainerAgent) myAgent).addDisplayMove(myAgent.getAID().getLocalName(),tacPos);

		}

	}
}