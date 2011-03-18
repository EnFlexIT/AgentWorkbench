package agentgui.simulationService.transaction;

import java.util.HashMap;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelStroke;


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
		
		// --- Fallunterscheidung TimeModel -----------------------
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
		EnvironmentModel returnValue = super.put(hashKey, envModel);

		return returnValue;
	}
	
	

}
