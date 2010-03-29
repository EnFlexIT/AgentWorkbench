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
		getHarbourArea();
		return ontReps;
	}

	public Domain getHarbourArea(){
		if(this.HarbourArea == null){
			ontReps=new ContainerHolder[5];
			ontReps[0]=new Crane();
			ontReps[0].setLocalName("Crane-#1");
			ontReps[1]=new Crane();
			ontReps[1].setLocalName("Crane-#2");
			ontReps[2]=new Apron();
			ontReps[2].setLocalName("Apron");
			ontReps[3]=new StraddleCarrier();
			ontReps[3].setLocalName("StraddleCarrier");
			ontReps[4]=new Yard();
			ontReps[4].setLocalName("Yard");

			Domain workingDomainOne=null;
			Domain workingDomainTwo=null;

			this.HarbourArea=new Harbour();
			this.HarbourArea.setId("Port of Otago");

			workingDomainOne=new Sea();
			workingDomainOne.setId("Pacific");
			((Crane) ontReps[0]).addCapable_of(workingDomainOne); //Crane1
			((Crane) ontReps[1]).addCapable_of(workingDomainOne); //Crane2
			addSub(this.HarbourArea,workingDomainOne);

			workingDomainTwo=new Berth();
			workingDomainTwo.setId("BerthNumberOne");
			addSub(workingDomainOne,workingDomainTwo);


			workingDomainOne=new Land();
			addSub(this.HarbourArea,workingDomainOne);

			workingDomainTwo=new Rail();
			workingDomainTwo.setId("TrainRails");
			((Crane) ontReps[0]).addCapable_of(workingDomainOne); //Crane1
			((Crane) ontReps[1]).addCapable_of(workingDomainOne); //Crane2
			addSub(workingDomainOne,workingDomainTwo);

			workingDomainTwo=new Rail();
			workingDomainTwo.setId("CraneRails");
			ontReps[0].setLives_in(workingDomainTwo); //Crane1
			ontReps[1].setLives_in(workingDomainTwo); //Crane2
			addSub(workingDomainOne,workingDomainTwo);

			workingDomainTwo=new YardArea();
			workingDomainTwo.setId("StorageYard");
			ontReps[4].setLives_in(workingDomainTwo); //Yard
			((StraddleCarrier) ontReps[3]).addCapable_of(workingDomainTwo); //StraddleCarrier
			addSub(workingDomainOne,workingDomainTwo);


			workingDomainTwo=new Street();
			workingDomainTwo.setId("StraddleCarrierStreet");
			ontReps[3].setLives_in(workingDomainTwo); //StraddleCarrier
			addSub(workingDomainOne,workingDomainTwo);

			workingDomainOne=workingDomainTwo;
			workingDomainTwo=new ApronArea();
			workingDomainTwo.setId("CraneApron");
			ontReps[2].setLives_in(workingDomainTwo); //ApronAgent
			((Crane) ontReps[0]).addCapable_of(workingDomainTwo); //Crane1
			((Crane) ontReps[1]).addCapable_of(workingDomainTwo); //Crane2
			((StraddleCarrier) ontReps[3]).addCapable_of(workingDomainTwo); //StraddleCarrier
			addSub(workingDomainOne,workingDomainTwo);

		}
		return this.HarbourArea;
	}
}
