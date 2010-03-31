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

import jade.content.Concept;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class OntologyElement extends DefaultMutableTreeNode{
	/**
	 * 
	 */
	private static final long serialVersionUID= -4666450144015812982L;
	protected Object ontologyConcept;

	public Concept getOntologyConcept(){
		if(this.ontologyConcept instanceof Concept){
			return (Concept) this.ontologyConcept;
		}else{
			return null;
		}
	}

	public void setOntologyConcept(Concept ontologyConcept){
		this.ontologyConcept=ontologyConcept;
	}

	public OntologyElement(Concept ontologyConcept){
		this.ontologyConcept=ontologyConcept;
	}

	public OntologyElement(Object ontologyConcept){
		this.ontologyConcept=ontologyConcept;
	}

	@Override
	public String toString(){
		String renderedString=this.ontologyConcept.getClass().getSimpleName();
		return renderedString;
	}
}
