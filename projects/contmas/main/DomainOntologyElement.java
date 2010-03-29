/**
 * @author Hanno - Felix Wagner, 28.03.2010
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

import contmas.ontology.Domain;

public class DomainOntologyElement extends OntologyElement{

	/**
	 * @param ontologyConcept
	 */
	public DomainOntologyElement(Domain ontologyConcept){
		super(ontologyConcept);
	}
	
	public Domain getDomain(){
		return (Domain) this.ontologyConcept;
	}
	
	@Override
	public String toString(){
		String renderedString=super.toString()+" - "+((Domain) this.ontologyConcept).getId();
		return renderedString;
	}
	
}