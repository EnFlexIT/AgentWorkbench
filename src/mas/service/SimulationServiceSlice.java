package mas.service;

import mas.service.time.TimeModel;
import jade.core.IMTPException;
import jade.core.Service;

public interface SimulationServiceSlice extends Service.Slice {

	// Horizontal commands
	static final String SIM_SET_TIMEMODEL = "set-timemodel";
	static final String SIM_GET_TIMEMODEL = "get-timemodel";
	static final String SIM_STEP_TIMEMODEL = "step-timemodel";
	
	public void setTimeModel(TimeModel newTimeModel) throws IMTPException;
	public TimeModel getTimeModel() throws IMTPException;
	
	public void stepTimeModel() throws IMTPException;
	
	
	
}
