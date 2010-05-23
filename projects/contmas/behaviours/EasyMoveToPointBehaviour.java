/**
 * @author Hanno - Felix Wagner, 20.05.2010
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
package contmas.behaviours;

import jade.core.Agent;
import mas.display.DisplayableAgent;
import mas.display.ontology.Position;
import mas.display.ontology.Speed;
import mas.movement.MoveToPointBehaviour;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class EasyMoveToPointBehaviour extends MoveToPointBehaviour{
	
	private static Speed startSpeed(){
		Speed speed=new Speed();
		speed.setSpeed(100.0F);
		return speed;
	}
	
	private static Position startPosition(){
		Position position=new Position();
		position.setX(10.0F);
		position.setY(10.0F);
		return position;
	}

	/**
	 * @param svgId
	 * @param a
	 * @param destPos
	 */
	public EasyMoveToPointBehaviour(Agent a, String svgId, Position destPos){
		super(svgId,(DisplayableAgent) a,startPosition(),destPos,startSpeed());
	}
}
