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

import jade.util.leap.Iterator;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Resource;

import contmas.ontology.ContainerHolder;
import contmas.ontology.Domain;
import contmas.ontology.Harbour;

/**
 * @author Hanno - Felix Wagner
 *
 */
public final class HarbourSetup{

	private Domain HarbourArea=null;
	private ContainerHolder[] ontReps=null;
	private OWLImportMapper mapper=null;

	/**
	 * 
	 */
	private HarbourSetup(){
		final String individualFileName="resources\\Container-Ontologie-OWL_indiv.owl";
		final String structureFileName="resources\\Container-Ontologie-OWL.owl";
		final String ontologyGeneratorFileName="resources\\OWLSimpleJADEAbstractOntology.owl";

		String ontologyJavaPackage=this.getClass().getPackage().getName();
		ontologyJavaPackage=ontologyJavaPackage.substring(0,ontologyJavaPackage.lastIndexOf(".")) + ".ontology";
		this.mapper=new OWLImportMapper(individualFileName,ontologyJavaPackage);
		this.mapper.setStructureFile(structureFileName);
		this.mapper.setBeanGeneratorFile(ontologyGeneratorFileName);
	}

	public static HarbourSetup getInstance(){
		return new HarbourSetup();
	}

	public static void addSub(Domain master,Domain sub){
//		sub.setLies_in(master);

		master.addHas_subdomains(sub);
	}

	public static void removeBacklink(Domain master){
		master.setLies_in(null);
		Iterator sub=master.getAllHas_subdomains();
		while(sub.hasNext()){
			Domain subDomain=(Domain) sub.next();
			HarbourSetup.removeBacklink(subDomain);
		}
	}

	public ContainerHolder[] getOntReps(){
		this.getHarbourArea(); //TODO workaround: if missing, only some Domains get displayed, reason unknown
		if(this.ontReps == null){
			List<Resource> allCHClasses=this.mapper.getSubConceptsOf("ContainerHolder",true);
			List<Object> allContainerHolder=this.mapper.getMappedIndividualsOf(allCHClasses);
			this.ontReps=allContainerHolder.toArray(new ContainerHolder[allContainerHolder.size()]);
		}
		return this.ontReps;
	}

	public Domain getHarbourArea(){
		if(this.HarbourArea == null){
			this.HarbourArea=this.getOWLHarbourArea();
		}
		return this.HarbourArea;
	}

	/**
	 * @return
	 */
	private Domain getOWLHarbourArea(){
		java.util.Iterator<Object> allHarbours=this.mapper.getMappedIndividualsOf("Harbour").iterator();
		while(allHarbours.hasNext()){
			Harbour curHarbour=(Harbour) allHarbours.next();
			return curHarbour;
		}
		return null;
	}
}
