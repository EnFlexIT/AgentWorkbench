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
import contmas.ontology.ActiveContainerHolder;
import contmas.ontology.Domain;
import contmas.ontology.TransportOrder;

public class ActiveContainerAgent extends ContainerAgent{

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
		if(endMatch>0){
			Domain startHabitat=curTO.getStarts_at().getAbstract_designation();
			Domain endHabitat=curTO.getEnds_at().getAbstract_designation();
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

}