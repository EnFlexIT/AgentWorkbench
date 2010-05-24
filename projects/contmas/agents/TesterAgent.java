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

import java.util.Vector;

import mas.display.DisplayableAgent;
import mas.display.ontology.Position;
import mas.display.ontology.Size;
import mas.display.ontology.Speed;
import mas.movement.MoveToPointBehaviour;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import contmas.behaviours.*;
import contmas.interfaces.*;
import contmas.main.AgentGUIHelper;
import contmas.main.Const;
import contmas.main.UncompatibleDimensionsException;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class TesterAgent extends ActiveContainerAgent implements TransportOrderHandler,TransportOrderOfferer,HarbourLayoutRequester,DisplayableAgent,TacticalMemorizer{
	/**
	 * 
	 */
	private static final long serialVersionUID=2675047952726694600L;

	public TesterAgent(){
		this(new StraddleCarrier());
	}

	/**
	 *
	 */
	public TesterAgent(StraddleCarrier ontologyRepresentation){
		super("container-distributing",ontologyRepresentation);
		this.targetAgentServiceType="container-storing";
		this.targetAbstractDomain=new YardArea();
		this.targetAbstractDomain.setId("StorageYard"); //TODO hardcoded
	}

	/* (non-Javadoc)
	 * @see contmas.agents.TransportOrderHandler#handleTransportOrder()
	 */
	@Override
	public void handleTransportOrder(){
		this.addBehaviour(new receiveLoadOrders(this));
		this.addBehaviour(new listenForExecuteAppointmentReq(this));
		this.addBehaviour(new TacticalStationer(this));
	}

	/* (non-Javadoc)
	 * @see contmas.agents.TransportOrderOfferer#offerTransportOrder()
	 */
	@Override
	public void offerTransportOrder(){
		this.addBehaviour(new unload(this));
		this.addBehaviour(new carryOutPlanning(this));
	}

	@Override
	public void setup(){
		super.setup();
		
		AgentGUIHelper.enableForCommunication(this);
		
		this.handleTransportOrder();
		this.offerTransportOrder();
/*
		moveBehaviour=new executeMovements(this);
		addBehaviour(moveBehaviour);
*/
//		echoStatus("my current relative position: " + positionToString(getRelativePosition()));
//		echoStatus("my current absolute position: " + positionToString(getAbsolutePosition()));
		Domain root=Const.findRootDomain(this.getOntologyRepresentation().getLives_in());

//		echoStatus("my root domain: " + root);

//		experiment();
//		getManhattanPositionTester();

	}

	public void experiment(){
		echoStatus("experiment starts");
		ACLMessage test=new ACLMessage(ACLMessage.CFP);
		Domain master=new Domain();
		Domain slave=new Domain();

		master.addHas_subdomains(slave);
		slave.setLies_in(master);

		ProvideHarbourSetup haSet=new ProvideHarbourSetup();
		haSet.setCurrent_harbour_layout(master);
		this.fillMessage(test,haSet);
		echoStatus("experiment ended");
	}


	@Override
	public void memorizeTacticalTarget(Designator target){
		Phy_Position targetPosition=inflateDomain(target.getAbstract_designation()).getIs_in_position();
		echoStatus("memorizing tactical target "+Const.positionToString(targetPosition));
		tacticalTargets.add(targetPosition);
	}
	
	public List tacticalTargets=new ArrayList(); //<Phy_Position>

	public List getTacticalTargets(){
		return this.tacticalTargets;
	}
}