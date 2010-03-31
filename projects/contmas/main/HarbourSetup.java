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
	private ContainerHolder[] ontReps=null;

	/**
	 * 
	 */
	private HarbourSetup(){
		// TODO Auto-generated constructor stub
	}

	public static HarbourSetup getInstance(){
		return new HarbourSetup();
	}

	public static void addSub(Domain master,Domain sub){
//		sub.setLies_in(master);

		master.addHas_subdomains(sub);
	}

	public ContainerHolder[] getOntReps(){
		this.getHarbourArea();
		return this.ontReps;
	}

	public Domain getHarbourArea(){
		if(this.HarbourArea == null){
			this.ontReps=new ContainerHolder[5];
			this.ontReps[0]=new Crane();
			this.ontReps[0].setLocalName("Crane-#1");
			this.ontReps[1]=new Crane();
			this.ontReps[1].setLocalName("Crane-#2");
			this.ontReps[2]=new Apron();
			this.ontReps[2].setLocalName("Apron");
			this.ontReps[3]=new StraddleCarrier();
			this.ontReps[3].setLocalName("StraddleCarrier");
			this.ontReps[4]=new Yard();
			this.ontReps[4].setLocalName("Yard");

			Domain workingDomainOne=null;
			Domain workingDomainTwo=null;

			this.HarbourArea=new Harbour();
			this.HarbourArea.setId("Port of Otago");

			workingDomainOne=new Sea();
			workingDomainOne.setId("Pacific");
			((Crane) this.ontReps[0]).addCapable_of(workingDomainOne); //Crane1
			((Crane) this.ontReps[1]).addCapable_of(workingDomainOne); //Crane2
			HarbourSetup.addSub(this.HarbourArea,workingDomainOne);

			workingDomainTwo=new Berth();
			workingDomainTwo.setId("BerthNumberOne");
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

			workingDomainOne=new Land();
			HarbourSetup.addSub(this.HarbourArea,workingDomainOne);

			workingDomainTwo=new Rail();
			workingDomainTwo.setId("TrainRails");
			((Crane) this.ontReps[0]).addCapable_of(workingDomainTwo); //Crane1
			((Crane) this.ontReps[1]).addCapable_of(workingDomainTwo); //Crane2
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

			workingDomainTwo=new Rail();
			workingDomainTwo.setId("CraneRails");
			this.ontReps[0].setLives_in(workingDomainTwo); //Crane1
			this.ontReps[1].setLives_in(workingDomainTwo); //Crane2
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

			workingDomainTwo=new YardArea();
			workingDomainTwo.setId("StorageYard");
			this.ontReps[4].setLives_in(workingDomainTwo); //Yard
			((StraddleCarrier) this.ontReps[3]).addCapable_of(workingDomainTwo); //StraddleCarrier
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

			workingDomainTwo=new Street();
			workingDomainTwo.setId("StraddleCarrierStreet");
			this.ontReps[3].setLives_in(workingDomainTwo); //StraddleCarrier
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

			workingDomainOne=workingDomainTwo;
			workingDomainTwo=new ApronArea();
			workingDomainTwo.setId("CraneApron");
			this.ontReps[2].setLives_in(workingDomainTwo); //ApronAgent
			((Crane) this.ontReps[0]).addCapable_of(workingDomainTwo); //Crane1
			((Crane) this.ontReps[1]).addCapable_of(workingDomainTwo); //Crane2
			((StraddleCarrier) this.ontReps[3]).addCapable_of(workingDomainTwo); //StraddleCarrier
			HarbourSetup.addSub(workingDomainOne,workingDomainTwo);

		}
		return this.HarbourArea;
	}
}
