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

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import contmas.behaviours.executeMovements;
import contmas.behaviours.getHarbourSetup;
import contmas.behaviours.receiveLoadOrders;
import contmas.behaviours.unload;
import contmas.interfaces.HarbourLayoutRequester;
import contmas.interfaces.MoveableAgent;
import contmas.interfaces.TransportOrderHandler;
import contmas.interfaces.TransportOrderOfferer;
import contmas.main.UncompatibleDimensionsException;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class StraddleCarrierAgent extends ActiveContainerAgent implements TransportOrderHandler,TransportOrderOfferer,HarbourLayoutRequester,MoveableAgent{
	private static final Float speed=1F/100F;
	private executeMovements moveBehaviour; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID=2675047952726694600L;

	public StraddleCarrierAgent(){
		this(new StraddleCarrier());
	}

	/**
	 *
	 */
	public StraddleCarrierAgent(StraddleCarrier ontologyRepresentation){
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
	}

	/* (non-Javadoc)
	 * @see contmas.agents.TransportOrderOfferer#offerTransportOrder()
	 */
	@Override
	public void offerTransportOrder(){
		this.addBehaviour(new unload(this));
	}

	@Override
	public void setup(){
		super.setup();
		this.handleTransportOrder();
		this.offerTransportOrder();

		moveBehaviour=new executeMovements(this);
		addBehaviour(moveBehaviour);

//		echoStatus("my current relative position: " + positionToString(getRelativePosition()));
//		echoStatus("my current absolute position: " + positionToString(getAbsolutePosition()));
		Domain root=findRootDomain(this.getOntologyRepresentation().getLives_in());

//		echoStatus("my root domain: " + root);

//		experiment();
//		getManhattanPositionTester();
	}

	public static String positionToString(Phy_Position in){
		String out="";
		out+="x=";
		out+=in.getPhy_x();
		out+="; y=";
		out+=in.getPhy_y();
		return out;
	}

	public Phy_Position getAbsolutePosition(){
		Phy_Position relPosition=getRelativePosition();
		Phy_Position positionOfHabitat=this.getOntologyRepresentation().getLives_in().getIs_in_position();
		Phy_Position absPosition=null;
		try{
			absPosition=calculateAbsolutePosition((Phy_Position) positionOfHabitat,(Phy_Position) relPosition);
		}catch(UncompatibleDimensionsException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return absPosition;
	}

	public Phy_Position getRelativePosition(){
		Phy_Position position=(Phy_Position) this.getOntologyRepresentation().getIs_in_position2();

		return position;
	}
	public Phy_Position getCurrentPosition(){
		return getRelativePosition();
	}
	

/*
	public P1_Size getSize(){
		P1_Size size=this.getOntologyRepresentation().getHas_size();
		return size;
	}
*/
	public static Phy_Position calculateAbsolutePosition(Phy_Position master,Phy_Position slave) throws UncompatibleDimensionsException{
		Phy_Position absPosition=new Phy_Position();
		/*
		if(master instanceof Phy_Position){
			absPosition=new Phy_Position();
			
			if(slave instanceof Phy_Position){
				((Phy_Position) absPosition).setPhy_z_dimension(((Phy_Position) master).getPhy_z_dimension() + ((Phy_Position) slave).getPhy_z_dimension());
			}else{
				((Phy_Position) absPosition).setPhy_z_dimension(((Phy_Position) master).getPhy_z_dimension() + 0);

			}
			
		}else if(slave instanceof Phy_Position){ //3d position in a 2d environment
			throw new UncompatibleDimensionsException();
		}
		*/
		absPosition=addPositions(master,slave);

		return absPosition;
	}

	public void printTOInfo(TransportOrder call){

//		echoStatus("Starts at: " + call.getStarts_at().getAbstract_designation());
//		echoStatus("Ends at: " + call.getEnds_at().getAbstract_designation());

		Phy_Size startSize=call.getStarts_at().getAbstract_designation().getHas_size();
		Phy_Position startPos=call.getStarts_at().getAbstract_designation().getIs_in_position();

		Phy_Size endSize=call.getEnds_at().getAbstract_designation().getHas_size();
		Phy_Position endPos=call.getEnds_at().getAbstract_designation().getIs_in_position();

		String startSizeStr="";
		String startPosStr="";
		String endSizeStr="";
		String endPosStr="";

		try{
			startSizeStr="width=" + startSize.getPhy_width() + ", height:" + startSize.getPhy_height();
			startPosStr=positionToString(startPos);
		}catch(NullPointerException e){
			echoStatus("Bad start: size=" + startSize + "; pos=:" + startPos);
		}

		try{
			endSizeStr="width=" + endSize.getPhy_width() + ", height:" + endSize.getPhy_height();
			endPosStr=positionToString(endPos);
		}catch(NullPointerException e){
			echoStatus("Bad end: size=" + endSize + "; pos=:" + endPos);
		}

		echoStatus("startSize: " + startSizeStr + "; startPos:" + startPosStr + "; endSize:" + endSizeStr + "; endPos:" + endPosStr);

	}
	
	@Override
	public TransportOrder calculateEffort(TransportOrder call){
		TransportOrder out=super.calculateEffort(call);

		Domain startAt=inflateDomain(call.getStarts_at().getAbstract_designation());
		Domain endAt=inflateDomain(call.getEnds_at().getAbstract_designation());

		Domain ccd=findClosestCommonDomain(this.getOntologyRepresentation().getLives_in(),startAt);
		ccd=findClosestCommonDomain(ccd,endAt);

//		echoStatus("ClosestCommonDomain: "+ccd);

		Phy_Position currentPos=getPositionRelativeTo(this.getOntologyRepresentation().getIs_in_position2(),this.getOntologyRepresentation().getLives_in(),ccd);
		Phy_Position startPos=getPositionRelativeTo(startAt.getIs_in_position(),startAt.getLies_in(),ccd);
//		Phy_Position endPos=getPositionRelativeTo(call.getEnds_at().getAbstract_designation().getIs_in_position(),call.getEnds_at().getAbstract_designation(),ccd);

//		echoStatus("currentPos: " + positionToString(currentPos));
//		echoStatus("startPos: " + positionToString(startPos));
//		System.out.println("endPos: "+positionToString(endPos));

		//transfer from current position to start position
		Long positioningEffort=calculateDuration(getManhattanDistance(currentPos,startPos).longValue());
		Long eta=positioningEffort+System.currentTimeMillis();
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
	
	public Long calculateDuration(Long distance){
//		echoStatus("speed="+speed);
		Float duration= (distance/speed);
		return duration.longValue();
	}

	/*
	 * Assumes, that subDomain lies_in containingDomain or containingDomain has_subdomain subDomain transitively
	 */
	public Phy_Position getPositionRelativeTo(Phy_Position positionInSubDomain,Domain subDomain,Domain containingDomain){
		if(subDomain.getId().equals(containingDomain.getId())){
			return positionInSubDomain;
//			return getZeroPosition();
		}

		//TODO so SOMETHING with containingDomain. Some kind of check or so.
		Phy_Position a=positionInSubDomain;
		Phy_Position b=subDomain.getIs_in_position();
		Phy_Position insidePosition=addPositions(a,b);

		return insidePosition;
	}

	public static Phy_Position addPositions(Phy_Position a,Phy_Position b){
		Phy_Position added=new Phy_Position();
		added.setPhy_x(a.getPhy_x() + b.getPhy_x());
		added.setPhy_y(a.getPhy_y() + b.getPhy_y());
		return added;
	}

	public static Phy_Position getZeroPosition(){
		Phy_Position zero=new Phy_Position();
		zero.setPhy_x(0.0F);
		zero.setPhy_y(0.0F);
		return zero;
	}

	public Domain findClosestCommonDomain(Domain a,Domain b){

		return a; //TODO algorithm
	}

	public Float getManhattanDistance(Phy_Position from,Phy_Position to){
		Float deltaX=from.getPhy_x() - to.getPhy_x();
		Float deltaY=from.getPhy_y() - to.getPhy_y();

		return Math.abs(deltaX) + Math.abs(deltaY);
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
	
	public void setAt(Phy_Position to){
//		echoStatus("I am now at " + positionToString(to));
		getOntologyRepresentation().getIs_in_position2().setPhy_x(to.getPhy_x());
		getOntologyRepresentation().getIs_in_position2().setPhy_y(to.getPhy_y());
	}
	
	public Boolean isAt(Phy_Position requested){
//		echoStatus("isAt requestet " + positionToString(requested));

		Phy_Position curPos=getCurrentPosition();
//		echoStatus("isAt current " + positionToString(curPos));

		if(requested.getPhy_x() == curPos.getPhy_x() && requested.getPhy_y() == curPos.getPhy_y()){
			return true;
		}
		return false;
	}
	
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
		
		return getManhattanPosition(oldPos,targetPos,dtg);

	}
	
	public Phy_Position getManhattanPosition(Phy_Position from,Phy_Position to,Float dtg){
		Float distance=getManhattanDistance(from,to)- dtg;
		Phy_Position interpolatedPosition=new Phy_Position();
		Float deltaX=to.getPhy_x() - from.getPhy_x();
		Float deltaY=to.getPhy_y() - from.getPhy_y();
		Float calcX; 
		Float calcY;
		if(distance<=Math.abs(deltaX)){
			calcX=distance;
			calcY=0F;
		} else {
			calcX=Math.abs(deltaX);
			calcY=distance-Math.abs(deltaX);
		}
		
		if(deltaX<0){
			calcX=-calcX;
		}
		if(deltaY<0){
			calcY=-calcY;
		}
		interpolatedPosition.setPhy_x(from.getPhy_x()+calcX);
		interpolatedPosition.setPhy_y(from.getPhy_y()+calcY);

//		echoStatus("interpolated position "+positionToString(interpolatedPosition));
		
		return interpolatedPosition;
	}
	/*
	public void getManhattanPositionTester(){
		Phy_Position pos1=new Phy_Position();
		pos1.setPhy_x(0.0F);
		pos1.setPhy_y(0.0F);
		Phy_Position pos2=new Phy_Position();
		pos2.setPhy_x(2.0F);
		pos2.setPhy_y(2.0F);
		
		Phy_Position interPos=getManhattanPosition(pos1,pos2,1.0F);

		echoStatus(positionToString(interPos));
	}
*/


	public Domain findRootDomain(Domain accessPoint){
		Domain liesIn=accessPoint.getLies_in();
		if(liesIn == null){
			return accessPoint;
		}else{
			return findRootDomain(liesIn);
		}
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#addMovementTo(contmas.ontology.Phy_Position)
	 */
	@Override
	public void addAsapMovementTo(Phy_Position to){
		echoStatus("adding asap movement to "+positionToString(to));
		Float distance=getManhattanDistance(getCurrentPosition(),to);
//		echoStatus("distance: "+distance+" distance.longValue"+distance.longValue());
		Long eta=calculateDuration(distance.longValue())+System.currentTimeMillis();

		Movement mov=new Movement();
		mov.setMove_to(to);
		mov.setBe_there_at(eta.toString());
		((ActiveContainerHolder)getOntologyRepresentation()).getScheduled_movements().add(mov);
		moveBehaviour.restart();
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#getPendingMoves()
	 */
	@Override
	public List getPendingMovements(){
		return ((ActiveContainerHolder)getOntologyRepresentation()).getScheduled_movements();
	}

/*	//Lies_in variant
	public Domain findDomain(String lookForID,Domain in){
		if(in.getId().equals(lookForID)){
			return in;
		}else{
			return findDomain(lookForID,in.getLies_in());
		}
	}
*/

/*
	@Override
	public Designator getAbstractTargetDesignator(){
		Designator target=new Designator(); //TODO mögliche ziele herausfinden
		target.setType("abstract");
		target.setAbstract_designation(this.targetAbstractDomain);
		return target;
	}
*/
}