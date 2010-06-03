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
import contmas.ontology.*;

public class carryOutPlanning extends CyclicBehaviour{
	private static final long serialVersionUID=1198231234951152102L;
	private final ContainerHolderAgent myCAgent;

	private requestExecuteAppointment requestingBehaviour;

	public carryOutPlanning(Agent a){
		super(a);
		this.myCAgent=(ContainerHolderAgent) a;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
//		if(requestingBehaviour == null || requestingBehaviour.done()){
			TransportOrderChain someTOC=this.myCAgent.getSomeTOCOfState(new PlannedOut());
			if(someTOC != null){
				TransportOrder curTO=((PlannedOut) myCAgent.getTOCState(someTOC)).getLoad_offer();
				TransportOrderChainState oldState=myCAgent.setTOCState(someTOC,new Assigned());
				requestingBehaviour=new requestExecuteAppointment(myCAgent,someTOC,curTO);
				myCAgent.addBehaviour(requestingBehaviour);
				myCAgent.echoStatus("added plan execution",someTOC,ContainerAgent.LOGGING_INFORM);
			}
//			myCAgent.registerForWakeUpCall(this);
//		}
	}

}