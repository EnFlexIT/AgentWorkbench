package mas.environment.provider;

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
		Physical2DEnvironment environment = null;
		try {
			GenericCommand cmd = new GenericCommand(H_GET_ENVIRONMENT, EnvironmentProviderService.SERVICE_NAME, null);
			getNode().accept(cmd);
//			if(result != null){
//				if(result instanceof IMTPException){
//					throw (IMTPException)result;
//				}else if (result instanceof Throwable){
//					throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
//				}else{
//					environment = (Physical2DEnvironment) result;
//				}
//			}
		} catch (ServiceException e) {
			throw new IMTPException("Unable to access remote node", e);
		}
		return environment;
	}

	@Override
	public Physical2DObject getObject(String id) throws IMTPException {
		
		Physical2DObject object = null;
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
			object = (Physical2DObject) result;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	

}
