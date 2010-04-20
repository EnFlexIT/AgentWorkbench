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

import contmas.behaviours.receiveLoadOrders;
import contmas.behaviours.unload;
import contmas.main.UncompatibleDimensionsException;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class StraddleCarrierAgent extends ActiveContainerAgent implements TransportOrderHandler,TransportOrderOfferer{
	private static final Float speed=1.0F;
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
		echoStatus("my current relative position: " + positionToString(getRelativePosition()));
		echoStatus("my current absolute position: " + positionToString(getAbsolutePosition()));

	}

	public String positionToString(Phy_Position in){
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

		System.out.println("Starts at: " + call.getStarts_at().getAbstract_designation());
		System.out.println("Ends at: " + call.getEnds_at().getAbstract_designation());

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
			startPosStr="x=" + startPos.getPhy_x() + ", y:" + startPos.getPhy_x();
		}catch(NullPointerException e){
			System.out.println("Bad start: size=" + startSize + "; pos=:" + startPos);
		}

		try{
			endSizeStr="width=" + endSize.getPhy_width() + ", height:" + endSize.getPhy_height();
			endPosStr="x=" + endPos.getPhy_x() + ", y:" + endPos.getPhy_y();
		}catch(NullPointerException e){
			System.out.println("Bad end: size=" + endSize + "; pos=:" + endPos);
		}

		System.out.println("startSize: " + startSizeStr + "; startPos:" + startPosStr + "; endSize:" + endSizeStr + "; endPos:" + endPosStr);

	}

	@Override
	public TransportOrder calculateEffort(TransportOrder call){
		TransportOrder out=super.calculateEffort(call);
		
		Domain ccd=findClosestCommonDomain(this.getOntologyRepresentation().getLives_in(), call.getStarts_at().getAbstract_designation());
		ccd=findClosestCommonDomain(ccd,call.getEnds_at().getAbstract_designation());
		
		System.out.println(ccd);
		
		Phy_Position currentPos=getPositionRelativeTo(this.getOntologyRepresentation().getIs_in_position2(),this.getOntologyRepresentation().getLives_in(),ccd);
		Phy_Position startPos=getPositionRelativeTo(call.getStarts_at().getAbstract_designation().getIs_in_position(),call.getStarts_at().getAbstract_designation(),ccd);
		Phy_Position endPos=getPositionRelativeTo(call.getEnds_at().getAbstract_designation().getIs_in_position(),call.getEnds_at().getAbstract_designation(),ccd);

		System.out.println("currentPos: "+positionToString(currentPos));
		System.out.println("startPos: "+positionToString(startPos));
		System.out.println("endPos: "+positionToString(endPos));
		
		//transfer from current position to start position
		Float positioningEffort=getManhattanDistance(currentPos,startPos);

		//pickup

		//transfer from start position to end position
		Float transferEffort=getManhattanDistance(startPos,endPos);

		//drop

		printTOInfo(call);

		System.out.println("randomized effort: " + out.getTakes());

		out.setTakes(positioningEffort + transferEffort);
		System.out.println("calculated effort: positioningEffort (" + positioningEffort + ")+transferEffort (" + transferEffort + ")=" + out.getTakes());

		return out;
	}
	/*
	 * Assumes, that subDomain lies_in containingDomain or containingDomain has_subdomain subDomain transitively
	 */
	public Phy_Position getPositionRelativeTo(Phy_Position positionInSubDomain, Domain subDomain, Domain containingDomain){
		if(subDomain.getId().equals(containingDomain.getId())){
			return getZeroPosition();
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
		Float lambdaX=from.getPhy_x() - to.getPhy_x();
		Float lambdaY=from.getPhy_y() - to.getPhy_y();

		return Math.abs(lambdaX + lambdaY);
	}

}