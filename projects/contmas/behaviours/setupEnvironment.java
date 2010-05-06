/**
 * @author Hanno - Felix Wagner, 06.05.2010
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

import contmas.agents.HarborMasterAgent;
import jade.core.behaviours.SimpleBehaviour;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class setupEnvironment extends SimpleBehaviour{
	HarborMasterAgent myAgent=null;
	Boolean done=false;
	public setupEnvironment(HarborMasterAgent a){
		super(a);
		myAgent=a;
	}
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		if(myAgent.allContainerHandlers.isEmpty()){
			myAgent.setupEnvironment();
			done=true;
		}else{
			block(500);
		}
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done(){
		return done;
	}

}
