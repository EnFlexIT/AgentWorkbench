package mas.service;

import jade.core.AID;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;
import mas.service.time.TimeModel;

public class SimulationServiceProxy extends SliceProxy implements SimulationServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T A R T -----------
	// ----------------------------------------------------------
	public void setManagerAgent(AID agentAddress) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_MANAGER_AGENT, SimulationService.NAME, null);
			cmd.addParam(agentAddress);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}

	@Override
	public AID getManagerAgent() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_MANAGER_AGENT, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (AID) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}

	}
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T O P -------------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Methods on the TimeModel --- S T A R T ---------------
	// ----------------------------------------------------------
	@Override
	public void setTimeModel(TimeModel newTimeModel) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_TIMEMODEL, SimulationService.NAME, null);
			cmd.addParam(newTimeModel);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}

	@Override
	public TimeModel getTimeModel() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_TIMEMODEL, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (TimeModel) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}

	}

	@Override
	public void stepTimeModel() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_STEP_TIMEMODEL, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	// ----------------------------------------------------------
	// --- Methods on the TimeModel --- E N D -------------------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Methods on the Environment --- S T A R T -------------
	// ----------------------------------------------------------
	@Override
	public void setEnvironmentInstance(Object envObjectInstance) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT, SimulationService.NAME, null);
			cmd.addParam(envObjectInstance);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	
	@Override
	public Object getEnvironmentInstance() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_ENVIRONMENT, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Object) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}

	}
	// ----------------------------------------------------------
	// --- Methods on the Environment --- E N D -----------------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Method for Notify Sensor --- S T A R T ---------------
	// ----------------------------------------------------------
	@Override
	public void notifySensors(String topicWhichChanged) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(topicWhichChanged, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	// ----------------------------------------------------------
	// --- Method for Notify Sensor --- E N D -------------------
	// ----------------------------------------------------------

	
}
