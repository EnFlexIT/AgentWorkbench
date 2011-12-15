/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.p2Dsvg.imageProcessing;

import jade.util.leap.Serializable;
import agentgui.envModel.p2Dsvg.ontology.Position;
/**
 * The class is used as a datastructure. It can be used in the concrete simulationManager for handling the collision information 
 *  * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class CombinedNameAndPos implements Serializable {
	
	     	String name;
	    	Position pos;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Position getPos() {
				return pos;
			}
			public void setPos(Position pos) {
				this.pos = pos;
			}
	      	
	    

}
