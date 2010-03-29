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

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.List;
import contmas.behaviours.enrollAtHarbor;
import contmas.ontology.Administered;
import contmas.ontology.Rail;
import contmas.ontology.Ship;
import contmas.ontology.TransportOrderChain;

public class ShipAgent extends StaticContainerAgent implements TransportOrderOfferer{
	class scheduleUnloadStart extends SimpleBehaviour{
		Boolean done=false;

		public scheduleUnloadStart(Agent a){
			super(a);
		}

		@Override
		public void action(){
			if( !((ContainerHolderAgent) myAgent).getOntologyRepresentation().getContains().getIs_filled_with().isEmpty()){
				offerTransportOrder();
				done=true;
			}
		}

		@Override
		public boolean done(){
			return done;
		}
	}
	public class unload extends TickerBehaviour{
		private final ContainerHolderAgent myCAgent;
		private static final long serialVersionUID=3933460156486819068L;

		public unload(Agent a){
			super(a,1000);
			this.myCAgent=((ContainerHolderAgent) this.myAgent);
		}

		@Override
		public void onTick(){
			TransportOrderChain someTOC=this.myCAgent.getSomeTOCOfState(new Administered());
			if(someTOC != null){
				this.myCAgent.releaseContainer(someTOC,null);
			}else{
				this.block();
				this.stop();
			}
		}
	}

	private static final long serialVersionUID=6800105012920938089L;

	public ShipAgent(Ship ontologyRepresentation){
		super("long-term-transporting",ontologyRepresentation);
		this.targetAgentServiceType="craning"; //unneccesary, because done by enroll behaviour
		this.targetAbstractDomain=new Rail(); //TODO should be done by enroll behaviour 
	}

	@Override
	public List determineContractors(){
		return this.ontologyRepresentation.getContractors();
	}
	
	@Override
	public void offerTransportOrder(){}

	@Override
	protected void setup(){
		super.setup();
		this.offerTransportOrder();
		this.addBehaviour(new enrollAtHarbor(this));
		this.addBehaviour(new scheduleUnloadStart(this));
	}
}