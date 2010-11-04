package agentgui.simulationService.transaction;

import java.util.HashMap;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDiscrete;


public class TransactionMap extends HashMap<Long, EnvironmentModel> {

	private static final long serialVersionUID = 7858350066101095998L;
	
	// --- In case that we are dealing with time  -----
	// --- in our TimeModel: For a faster Mapping! ----
	private HashMap<Long, Long> time2Counter = new HashMap<Long, Long>();
	
	@Override
	public EnvironmentModel put(Long key, EnvironmentModel envModel) {
		EnvironmentModel returnValue = super.put(key, envModel);

		// ------------------------------------------------
		// --- In case that we are dealing with time  -----
		// --- in our TimeModel: For a faster Mapping! ----
		TimeModel timeModelInstance = envModel.getTimeModel();
		if (timeModelInstance instanceof TimeModelDiscrete) {
			time2Counter.put(((TimeModelDiscrete) timeModelInstance).getTime(), key);
		}		
		// ------------------------------------------------
		
		return returnValue;
	}
	
	

}
