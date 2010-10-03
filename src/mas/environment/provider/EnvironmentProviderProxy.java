package mas.environment.provider;

import java.util.HashSet;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.ServiceException;
import jade.core.SliceProxy;

public class EnvironmentProviderProxy extends SliceProxy implements
		EnvironmentProviderSlice {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4218111250581084215L;

	@Override
	public Physical2DEnvironment getEnvironment() throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(H_GET_ENVIRONMENT, EnvironmentProviderService.SERVICE_NAME, null);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)){
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Physical2DEnvironment) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	@Override
	public Physical2DObject getObject(String id) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_GET_OBJECT, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(id);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Physical2DObject) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashSet<ActiveObject> getCurrentlyMoving() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(H_GET_CURRENTLY_MOVING, EnvironmentProviderService.SERVICE_NAME, null);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (HashSet<ActiveObject>) result;
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	@Override
	public boolean setMovement(String agentID, Movement movement)
			throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(H_SET_MOVEMENT, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(agentID);
			cmd.addParam(movement);
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				}
				else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return ((Boolean)result).booleanValue();
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
	}

	

}
