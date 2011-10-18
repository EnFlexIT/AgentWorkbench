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

import java.util.ArrayList;

import agentgui.envModel.p2Dsvg.ontology.Position;
/**
 * The class is used as a datastructure. Moving agents can use this class for sending the required information to the simulationManager.
 * @see agentgui.simulationService.agents.SimulationManagerAgentPhysical2D 
 * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class Answer implements java.io.Serializable {	
	
	
	private static final long serialVersionUID = -4472768003380715304L;
	
	private Object speed;
	private ArrayList<Position> wayToDestination;
	private Position nextPosition;
	private int index;
	
	
	public Position getNextPosition() {
		return nextPosition;
	}
	public void setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Object getSpeed() {
		return speed;
	}
	public void setSpeed(Object speed) {
		this.speed = speed;
	}
	public ArrayList<Position> getWayToDestination() {
		return wayToDestination;
	}
	public void setWayToDestination(ArrayList<Position> wayToDestination) {
		this.wayToDestination = wayToDestination;
	}
	
	
	
	

}
