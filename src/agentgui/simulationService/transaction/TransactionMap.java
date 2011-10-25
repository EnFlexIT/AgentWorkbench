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
package agentgui.simulationService.transaction;

import java.util.HashMap;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelStroke;

/**
 * This class is used inside the {@link SimulationService} for storing
 * different {@link EnvironmentModel} over time. It extends a HashMap
 * in order to assign time to a state of an {@link EnvironmentModel}.
 * <br><br>
 * This class is not finalized by now.
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TransactionMap extends HashMap<Long, EnvironmentModel> {

	private static final long serialVersionUID = 7858350066101095998L;
	
	// --- In case that we are dealing with time  -----
	// --- in our TimeModel: For a faster Mapping! ----
	private HashMap<Long, Long> time2Counter = new HashMap<Long, Long>();
	private long internalCounter = 0;
	
	public EnvironmentModel put(EnvironmentModel envModel) {
		
		long hashKey = 0;
		
		// --- If the environment model is null -------------------
		if (envModel==null) {
			return null;
		}
		
		// --- Case distinction for the TimeModel -----------------
		if (envModel.getTimeModel()==null) {
			hashKey = internalCounter;
			internalCounter++;
			
		} else if (envModel.getTimeModel() instanceof TimeModelStroke) {
			TimeModelStroke tm = (TimeModelStroke) envModel.getTimeModel();
			hashKey = tm.getCounter();
			
		} else if ( envModel.getTimeModel() instanceof TimeModelDiscrete ) {
			TimeModelDiscrete tm = (TimeModelDiscrete) envModel.getTimeModel();
			hashKey = tm.getTime();
		
			// ------------------------------------------------
			// --- In case that we are dealing with time  -----
			// --- in our TimeModel: For a faster Mapping! ----
			time2Counter.put(tm.getTime(), internalCounter);
			internalCounter++;
			// ------------------------------------------------
			
		}
		
		// --------------------------------------------------------
		// --- execute the super method ---------------------------
		// --------------------------------------------------------
		return super.put(hashKey, envModel);
		// --------------------------------------------------------
	}
	
	

}
