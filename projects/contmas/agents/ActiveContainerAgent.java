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

import java.util.Vector;

import mas.display.DisplayableAgent;
import mas.display.ontology.Position;
import mas.display.ontology.Size;
import mas.display.ontology.Speed;
import mas.movement.MoveToPointBehaviour;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import contmas.interfaces.MoveableAgent;
import contmas.interfaces.TacticalMemorizer;
import contmas.main.AgentGUIHelper;
import contmas.main.Const;
import contmas.ontology.*;

public class ActiveContainerAgent extends ContainerHolderAgent implements MoveableAgent,DisplayableAgent,TacticalMemorizer{

	/**
	 * 
	 */
	private static final long serialVersionUID= -5397340339244159587L;
	
//	private executeMovements moveBehaviour; 
	
	public List tacticalTargets=new ArrayList(); //<Phy_Position>


	public ActiveContainerAgent(String serviceType){
		this(serviceType,new ActiveContainerHolder());
	}

	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation){
		super(serviceType,ontologyRepresentation);
		AgentGUIHelper.enableForCommunication(this);

	}

	//TODO implement recursive transitive domain matching
	@Override
	public Integer matchOrder(TransportOrder curTO){

		Integer endMatch=super.matchOrder(curTO); //standard-Match: AID und ziel ist genau lebensraum
		Integer startMatch= -1;
		if(endMatch > 0){
			Domain startHabitat=curTO.getStarts_at().getAbstract_designation();
			Domain endHabitat=curTO.getEnds_at().getAbstract_designation();
			startHabitat=inflateDomain(startHabitat);
			endHabitat=inflateDomain(endHabitat);

			Iterator capabilities=((ActiveContainerHolder) this.ontologyRepresentation).getAllCapable_of();
			while(capabilities.hasNext()){
				Domain capability=(Domain) capabilities.next();


				if(startHabitat.getClass() == capability.getClass()){ //containeragent is able to handle orders in this start-habitat-domain
					//    			echoStatus("start passt");
					startMatch=1;
				}
				if((endMatch != 0) && (endMatch != 1) && (endHabitat.getClass() == capability.getClass())){ //containeragent is able to handle orders in this end-habitat-domain
					//    			echoStatus("end passt (besser)");
					endMatch=1;
				}
			}
			if((startMatch > -1) && (endMatch > -1)){ //order matcht
				return startMatch + endMatch;
			}
		}
		return -1; //order matcht nicht
	}
	
	
//	/* (non-Javadoc)
//	 * @see contmas.interfaces.MoveableAgent#addMovementTo(contmas.ontology.Phy_Position)
//	 */
//	@Override
//	public void addAsapMovementTo(Phy_Position to){
//		echoStatus("adding asap movement to "+Const.positionToString(to),ContainerAgent.LOGGING_INFORM);
////		Float distance=Const.getManhattanDistance(getCurrentPosition(),to);
////		echoStatus("distance: "+distance+" distance.longValue"+distance.longValue());
////		Long eta=calculateDuration(distance.longValue())+System.currentTimeMillis();
///*
//		Movement mov=new Movement();
//		mov.setMove_to(to);
//		mov.setBe_there_at(eta.toString());
//		((ActiveContainerHolder)getOntologyRepresentation()).getScheduled_movements().add(mov);
//		*/
//		addDisplayMove(getAID().getLocalName(),to);
//
////		moveBehaviour.restart();
//	}
	@Override
	public MoveToPointBehaviour addDisplayMove(String reporter,Phy_Position destPos){
		echoStatus("Adding display move to "+Const.positionToString(destPos),ContainerAgent.LOGGING_INFORM);

		Speed speed=new Speed();
		speed.setSpeed(SPEED_VALUE);
		MoveToPointBehaviour movingBehaviour;
		
		Vector<Position> wp=new Vector<Position>();
		wp.add(AgentGUIHelper.convertPosition(Const.getManhattanTurningPoint(getCurrentPosition(),destPos)));
		wp.add(AgentGUIHelper.convertPosition(destPos));
		movingBehaviour=new MoveToPointBehaviour(reporter + SHADOW_SUFFIX,this,getPosition(), wp,speed);

//		movingBehaviour=new MoveToPointBehaviour(reporter + SHADOW_SUFFIX,this,getPosition(), AgentGUIHelper.convertPosition(destPos),speed);

		addBehaviour(movingBehaviour);
		return movingBehaviour;
	}
	
	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#getPendingMoves()
	 */
	@Override
	public List getPendingMovements(){
		return ((ActiveContainerHolder)getOntologyRepresentation()).getScheduled_movements();
	}
	
	@Override
	public TransportOrder calculateEffort(TransportOrder call){
		TransportOrder out=super.calculateEffort(call);

		Domain startAt=inflateDomain(call.getStarts_at().getAbstract_designation());
		Domain endAt=inflateDomain(call.getEnds_at().getAbstract_designation());

		Domain ccd=Const.findClosestCommonDomain(this.getOntologyRepresentation().getLives_in(),startAt);
		ccd=Const.findClosestCommonDomain(ccd,endAt);

//		echoStatus("ClosestCommonDomain: "+ccd);

		Phy_Position currentPos=Const.getPositionRelativeTo(this.getOntologyRepresentation().getIs_in_position2(),this.getOntologyRepresentation().getLives_in(),ccd);
		Phy_Position startPos=Const.getPositionRelativeTo(startAt.getIs_in_position(),startAt.getLies_in(),ccd);
//		Phy_Position endPos=Const.getPositionRelativeTo(call.getEnds_at().getAbstract_designation().getIs_in_position(),call.getEnds_at().getAbstract_designation(),ccd);

//		echoStatus("currentPos: " + positionToString(currentPos));
//		echoStatus("startPos: " + positionToString(startPos));
//		System.out.println("endPos: "+positionToString(endPos));

		//transfer from current position to start position
		Long positioningEffort=calculateDuration(Const.getManhattanDistance(currentPos,startPos).longValue());
		Long eta=positioningEffort+getLastPlannedTOCFinishTime();
		//pickup

		//NO FURTHER EFFORTS needed so far!
		//transfer from start position to end position
//		Float transferEffort=getManhattanDistance(startPos,endPos);

		//drop

//		printTOInfo(call);

//		echoStatus("randomized effort: " + out.getTakes());

		out.setTakes_until(eta+""); // + transferEffort);
//		echoStatus("calculated Takes_until: positioningEffort (" + positioningEffort + ")=" + out.getTakes_until()); // ")  +transferEffort (" + transferEffort + ")=" + out.getTakes());

		return out;
	}
	
	public Long getLastPlannedTOCFinishTime(){
//		List allPlannedTOCs=getSomeTOCOfState(new PlannedIn());
		Long lastPlannedFinishTime=System.currentTimeMillis();
		java.util.List<TOCHasState> allPlannedTOCs=getAllTOCOfState(PlannedIn.class);
		allPlannedTOCs.addAll(getAllTOCOfState(PlannedOut.class));

		for(java.util.Iterator<TOCHasState> iterator=allPlannedTOCs.iterator();iterator.hasNext();){
			TOCHasState tocHasState=iterator.next();
			Long curFinishTime=Long.valueOf(tocHasState.getState().getLoad_offer().getTakes_until());
			if(lastPlannedFinishTime<curFinishTime){
				curFinishTime=lastPlannedFinishTime;
			}
		}
		return lastPlannedFinishTime;
	}
	
	@Override
	public Boolean isAt(Phy_Position requested){
//		echoStatus("isAt requestet " + positionToString(requested));

		Phy_Position curPos=getCurrentPosition();
//		echoStatus("isAt current " + positionToString(curPos));

		if(requested.getPhy_x() == curPos.getPhy_x() && requested.getPhy_y() == curPos.getPhy_y()){
			return true;
		}
		return false;
	}
	@Override
	public void setAt(Phy_Position to){
//		echoStatus("I am now at " + positionToString(to));
		getOntologyRepresentation().getIs_in_position2().setPhy_x(to.getPhy_x());
		getOntologyRepresentation().getIs_in_position2().setPhy_y(to.getPhy_y());
	}
	@Override
	public Long calculateDuration(Long distance){
//		echoStatus("speed="+speed);
		Float duration= (distance/speed);
		return duration.longValue();
	}
	@Override
	public Phy_Position interpolatePosition(Movement mov){
		Phy_Position oldPos=getCurrentPosition();
		Long curTime=System.currentTimeMillis();
		
		Phy_Position targetPos=mov.getMove_to();
		Long targetEta=Long.parseLong(mov.getBe_there_at());
		
		Long ttg=targetEta-curTime;
//		echoStatus("targetEta: "+targetEta+" curTime: "+curTime+" ttg: "+ttg);
		if(ttg<0){
			ttg=0L;
		}
		Float dtg=ttg*speed;
		
//		echoStatus("oldPos "+positionToString(oldPos)+" targetPos "+positionToString(targetPos)+ " dtg "+dtg);
		
		return Const.getManhattanPosition(oldPos,targetPos,dtg);

	}
	@Override
	public Phy_Position getRelativePosition(){
		Phy_Position position=(Phy_Position) this.getOntologyRepresentation().getIs_in_position2();

		return position;
	}
	@Override
	public Phy_Position getCurrentPosition(){
		return getRelativePosition();
	}

	private Boolean isMoving=false;

	@Override
	public boolean isMoving(){
		return isMoving;
	}

	@Override
	public void setMoving(boolean moving){
		isMoving=moving;
	}

	@Override
	public Speed getCurrentSpeed(){
		return null;
	}

	@Override
	public Speed getMaxSpeed(){
		return null;
	}

	@Override
	public Size getSize(){
		return null;
	}

	@Override
	public AID getUpdateReceiver(){
		return AgentGUIHelper.getDisplayTopic(this);
	}

	@Override
	public Codec getDisplayCodec(){
		return AgentGUIHelper.getDisplayCodec();
	}

	@Override
	public Ontology getDisplayOntology(){
		return AgentGUIHelper.getDisplayOntology();
	}

	@Override
	public Position getPosition(){
		return AgentGUIHelper.convertPosition(ontologyRepresentation.getIs_in_position2());
	}

	@Override
	public void setPosition(Position position){
		Phy_Position setPos=AgentGUIHelper.convertPosition(position);
//		setPos=null;
		ontologyRepresentation.setIs_in_position2(setPos);
	}
	
	@Override
	public void memorizeTacticalTarget(Designator target){
		Phy_Position targetPosition=inflateDomain(target.getAbstract_designation()).getIs_in_position();
		echoStatus("memorizing tactical target "+Const.positionToString(targetPosition),LOGGING_INFORM);
		tacticalTargets.add(targetPosition);
	}
	
	@Override
	public List getTacticalTargets(){
		return this.tacticalTargets;
	}

	
//	public Phy_Position getAbsolutePosition(){
//	Phy_Position relPosition=getRelativePosition();
//	Phy_Position positionOfHabitat=this.getOntologyRepresentation().getLives_in().getIs_in_position();
//	Phy_Position absPosition=null;
//	try{
//		absPosition=calculateAbsolutePosition((Phy_Position) positionOfHabitat,(Phy_Position) relPosition);
//	}catch(UncompatibleDimensionsException e){
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return absPosition;
//}
//

//
//
//public static Phy_Position calculateAbsolutePosition(Phy_Position master,Phy_Position slave) throws UncompatibleDimensionsException{
//	Phy_Position absPosition=new Phy_Position();
//	/*
//	if(master instanceof Phy_Position){
//		absPosition=new Phy_Position();
//		
//		if(slave instanceof Phy_Position){
//			((Phy_Position) absPosition).setPhy_z_dimension(((Phy_Position) master).getPhy_z_dimension() + ((Phy_Position) slave).getPhy_z_dimension());
//		}else{
//			((Phy_Position) absPosition).setPhy_z_dimension(((Phy_Position) master).getPhy_z_dimension() + 0);
//
//		}
//		
//	}else if(slave instanceof Phy_Position){ //3d position in a 2d environment
//		throw new UncompatibleDimensionsException();
//	}
//	*/
//	absPosition=Const.addPositions(master,slave);
//
//	return absPosition;
//}
	

}