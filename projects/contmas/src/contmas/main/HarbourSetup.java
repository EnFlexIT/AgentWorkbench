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
package contmas.main;

import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public final class HarbourSetup{

	private Domain HarbourArea=null;
	/**
	 * 
	 */
	private HarbourSetup(){
		// TODO Auto-generated constructor stub
	}
	public Domain getHarbourArea(){
		if(HarbourArea==null){
			Domain workingDomainOne=null;
			Domain workingDomainTwo=null;

			HarbourArea=new Harbour();
			HarbourArea.setId("Port of Otago");
			
			workingDomainOne=new Sea();
			workingDomainOne.setLies_in(HarbourArea);

			workingDomainTwo=new Berth();
			workingDomainTwo.setLies_in(workingDomainOne);
			
			workingDomainOne=new Land();
			workingDomainOne.setLies_in(HarbourArea);

			workingDomainTwo=new Rail();
			workingDomainTwo.setId("CraneRails");
			workingDomainTwo.setLies_in(workingDomainOne);
			workingDomainTwo=new YardArea();
			workingDomainTwo.setLies_in(workingDomainOne);
			workingDomainTwo=new Street();
			workingDomainTwo.setLies_in(workingDomainOne);
			workingDomainOne=workingDomainTwo;
			workingDomainTwo=new ApronArea();
			workingDomainTwo.setLies_in(workingDomainOne);
			
		}
		return HarbourArea;
	}
}
