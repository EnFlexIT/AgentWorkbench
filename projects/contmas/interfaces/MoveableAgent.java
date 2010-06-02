/**
 * @author Hanno - Felix Wagner, 03.05.2010
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
package contmas.interfaces;

import mas.movement.MoveToPointBehaviour;
import contmas.behaviours.MovementController;
import contmas.ontology.Movement;
import contmas.ontology.Phy_Position;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public interface MoveableAgent{
	
	static final String SHADOW_SUFFIX="Shadow";

	static final Float speed=1F/10F; // pixel pro ms= 0,1px/ms

	static final Float SPEED_VALUE=100.0F; //realwelteinheiten pro sekunde = 100 px/s= 100px/1000ms= 1/10 px/ms= 0,1 px/ms


	List getPendingMovements();

	void setAt(Phy_Position to);

	public Boolean isAt(Phy_Position requested);

//	void addAsapMovementTo(Phy_Position to);

	public Phy_Position getCurrentPosition();

	public Phy_Position interpolatePosition(Movement mov);

	public MoveToPointBehaviour addDisplayMove(Phy_Position destPos);
	
	public MovementController getMovementController();

	/**
	 * @param distance
	 * @return
	 */
	Long calculateDuration(Long distance);

	/**
	 * @return
	 */
	Phy_Position getRelativePosition();
}