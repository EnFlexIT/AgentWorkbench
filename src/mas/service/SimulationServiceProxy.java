package mas.service;

import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;
import mas.service.time.TimeModel;

public class SimulationServiceProxy extends SliceProxy implements SimulationServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

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

	
	
}
