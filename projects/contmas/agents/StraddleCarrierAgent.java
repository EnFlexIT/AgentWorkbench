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
		echoStatus("my current relative position: "+positionToString(getRelativePosition()));
		echoStatus("my current absolute position: "+positionToString(getAbsolutePosition()));

	}

	public String positionToString(Phy_Position in){
		String out="";
		out+="x=";
		out+=in.getPhy_x_dimension();
		out+="; y=";
		out+=in.getPhy_y_dimension();
		return out;
	}

	public Phy_Position getAbsolutePosition(){
		Phy_Position relPosition=getRelativePosition();
		Phy_Position positionOfHabitat=this.getOntologyRepresentation().getLives_in().getIs_in_position();
		Phy_Position absPosition=null;
		try{
			absPosition=calculateAbsolutePosition((Phy_Position)positionOfHabitat,(Phy_Position)relPosition);
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
		absPosition.setPhy_x_dimension(master.getPhy_x_dimension() + slave.getPhy_x_dimension());
		absPosition.setPhy_y_dimension(master.getPhy_y_dimension() + slave.getPhy_y_dimension());

		return absPosition;
	}
}