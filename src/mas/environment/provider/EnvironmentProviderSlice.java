package mas.environment.provider;

import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import jade.core.IMTPException;
import jade.core.Service.Slice;

public interface EnvironmentProviderSlice extends Slice {
	public static final String H_GET_ENVIRONMENT = "getEnvironment";
	public static final String H_GET_OBJECT = "getObject";
	
	public Physical2DEnvironment getEnvironment() throws IMTPException;
	public Physical2DObject getObject(String id) throws IMTPException;
}
