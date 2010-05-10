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

import jade.util.leap.Iterator;
import jade.util.leap.List;
import contmas.interfaces.MoveableAgent;
import contmas.ontology.*;

public class ActiveContainerAgent extends ContainerHolderAgent implements MoveableAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID= -5397340339244159587L;

	public ActiveContainerAgent(String serviceType){
		this(serviceType,new ActiveContainerHolder());
	}

	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation){
		super(serviceType,ontologyRepresentation);
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

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#addAsapMovementTo(contmas.ontology.Phy_Position)
	 */
	@Override
	public void addAsapMovementTo(Phy_Position to){
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#getCurrentPosition()
	 */
	@Override
	public Phy_Position getCurrentPosition(){
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#getPendingMovements()
	 */
	@Override
	public List getPendingMovements(){
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#interpolatePosition(contmas.ontology.Movement)
	 */
	@Override
	public Phy_Position interpolatePosition(Movement mov){
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#isAt(contmas.ontology.Phy_Position)
	 */
	@Override
	public Boolean isAt(Phy_Position requested){
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.MoveableAgent#setAt(contmas.ontology.Phy_Position)
	 */
	@Override
	public void setAt(Phy_Position to){
		// TODO Auto-generated method stub
		
	}

}