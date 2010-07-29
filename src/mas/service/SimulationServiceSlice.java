package mas.service;

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Service;
import mas.service.time.TimeModel;

public interface SimulationServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent -------------------------
	static final String SIM_SET_MANAGER_AGENT = "set-manager";
	static final String SIM_GET_MANAGER_AGENT = "get-manager";
	
	public void setManagerAgent(AID agentAddress) throws IMTPException;
	public AID getManagerAgent() throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods on the TimeModel -----------------------------
	static final String SIM_SET_TIMEMODEL = "set-timemodel";
	static final String SIM_GET_TIMEMODEL = "get-timemodel";
	static final String SIM_STEP_TIMEMODEL = "step-timemodel";
	
	public void setTimeModel(TimeModel newTimeModel) throws IMTPException;
	public TimeModel getTimeModel() throws IMTPException;
	public void stepTimeModel() throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods on the EnvironmentModel ----------------------
	static final String SIM_SET_ENVIRONMENT = "set-environment";
	static final String SIM_GET_ENVIRONMENT = "get-environment";

	public void setEnvironmentInstance(Object envObjectInstance) throws IMTPException;
	public Object getEnvironmentInstance() throws IMTPException;
	

	// ----------------------------------------------------------
	// --- Methods for 'Notify Sensor' --------------------------
	public final String SERVICE_UPDATE_TIME_MODEL = "service-update-time-model";
	public final String SERVICE_UPDATE_TIME_STEP = "service-update-time-step";
	public final String SERVICE_UPDATE_ENVIRONMENT = "service-update-environment";
	public final String SERVICE_UPDATE_SIMULATION = "service-update-simulation";

	public void notifySensors(String topicWhichChanged) throws IMTPException;

	
	
	
}
