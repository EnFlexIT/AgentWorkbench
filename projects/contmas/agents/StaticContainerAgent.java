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

import contmas.ontology.StaticContainerHolder;

public class StaticContainerAgent extends ContainerHolderAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID= -4081147908985783035L;

	public StaticContainerAgent(String serviceType){
		this(serviceType,new StaticContainerHolder());
	}

	public StaticContainerAgent(String serviceType,StaticContainerHolder ontologyRepresentation){
		super(serviceType,ontologyRepresentation);
	}

}
