/**
 * @author Hanno - Felix Wagner, 01.06.2010
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

import java.util.AbstractList;
import java.util.List;
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.PositionReporter;
import contmas.interfaces.BehaviourNotAvailableException;
import contmas.interfaces.MoveableAgent;
import contmas.main.AgentGUIHelper;
import contmas.main.Const;
import contmas.main.AlreadyMovingException;
import contmas.main.MovementBlockedException;
import contmas.ontology.*;

import mas.display.DisplayableAgent;
import mas.display.ontology.Position;
import mas.display.ontology.Speed;
import mas.movement.MoveToPointBehaviour;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.Iterator;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class MovementController extends SimpleBehaviour{

	private MoveToPointBehaviour movingBehaviour;
	private WaitUntilTargetReached positionChecker;

	private Boolean isDone=false;

	private ContainerHolderAgent myAgent;
	private MoveableAgent myMoveableAgent=null;
	private DisplayableAgent myDisplayableAgent=null;

	public static final Integer MOVE_TYPE_JUST_MOVE= -1;
	public static final Integer MOVE_TYPE_PICK_UP=0;
	public static final Integer MOVE_TYPE_DROP=1;

	private Integer currentMoveType=MOVE_TYPE_JUST_MOVE;

	static final Float speed=1F / 10F; // pixel pro ms= 0,1px/ms

	static final Float SPEED_VALUE=100.0F; //realwelteinheiten pro sekunde = 100 px/s= 100px/1000ms= 1/10 px/ms= 0,1 px/ms

	private static final String SHADOW_SUFFIX="Shadow";

	private final String AGENT_ALIAS;

	private Phy_Position curDest;
	private Boolean blocked=false;
	private List<Behaviour> registeredAppointments;

	public MovementController(ContainerHolderAgent a) throws BehaviourNotAvailableException{
		super(a);
		myAgent=a;
		if(a instanceof MoveableAgent && a instanceof DisplayableAgent){ //otherwise just skip this behaviour
			myMoveableAgent=(MoveableAgent) a;
			myDisplayableAgent=(DisplayableAgent) a;
		}else{
			throw new BehaviourNotAvailableException();
		}
		AGENT_ALIAS=myAgent.getLocalName();
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		isDone=movingBehaviour.done();
		if(isDone){
			((ContainerAgent) myAgent).echoStatus("I am in target position",ContainerAgent.LOGGING_INFORM);
		}else{
//				((ContainerAgent)myAgent).echoStatus("i have not yet reached target drop position: block",curTOC);
			if(myAgent instanceof PositionReporter){
				((PositionReporter) myAgent).reportPosition();
			}
			block(200);
		}
	}

	public Boolean isMoving(){
		Boolean isMoving=true;
		if(movingBehaviour == null || movingBehaviour.done()){
			isMoving=false;
		}
		return isMoving;
	}

	public void executeAppointment(TransportOrderChain curTOC,TransportOrder curTO,Integer moveType) throws AlreadyMovingException,MovementBlockedException{
		Phy_Position destPos=null;
		Designator curDesig=null;
		TransportOrderChainState oldState=null;
		TransportOrderChainState newState=null;

		if(isMoving()){
			throw new AlreadyMovingException();
		}
		if(blocked){
			throw new MovementBlockedException();
		}

		if(moveType.equals(MOVE_TYPE_PICK_UP)){
			newState=new InExecution();
			curDesig=curTO.getStarts_at();
		}else if(moveType.equals(MOVE_TYPE_DROP)){
			newState=new Assigned();
			curDesig=curTO.getEnds_at();
		}

		oldState=myAgent.setTOCState(curTOC,newState);
		destPos=myAgent.calculateTargetPosition(curDesig);
		currentMoveType=moveType;
		movingBehaviour=createDisplayMove(destPos);

	}

	private MoveToPointBehaviour createDisplayMove(Phy_Position destPos){
		Speed speed=new Speed();
		speed.setSpeed(SPEED_VALUE);

		Vector<Position> wp=new Vector<Position>();
		wp.add(AgentGUIHelper.convertPosition(Const.getManhattanTurningPoint(myMoveableAgent.getCurrentPosition(),destPos)));
		wp.add(AgentGUIHelper.convertPosition(destPos));
		MoveToPointBehaviour movingBehaviour=new MoveToPointBehaviour(AGENT_ALIAS + SHADOW_SUFFIX,myDisplayableAgent,myDisplayableAgent.getPosition(),wp,speed);

		myAgent.addBehaviour(movingBehaviour);
		positionChecker.setMovingBehaviour(movingBehaviour);
		positionChecker.setTargetPosition(curDest);
		curDest=destPos;

		return movingBehaviour;
	}

	public void blockMovement(){
		blocked=true;
	}

	public void unblockMovement(){
		blocked=false;
	}

	public void registerAppointment(Behaviour requester){
		registeredAppointments.add(requester);
	}

	public void executeRegisteredAppointment(){
		java.util.Iterator<Behaviour> iterator=registeredAppointments.iterator();
		Behaviour b=(Behaviour) registeredAppointments.iterator().next();
		b.restart();
		iterator.remove();
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done(){
		return isDone;
	}

}
