package mas.environment.provider;

import mas.environment.ontology.Position;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.SliceProxy;

public class EnvironmentProviderProxy extends SliceProxy implements EnvironmentProviderSlice{

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String INTERACTION_NAME = "posRequest";
	
	private int currentRequestNr = 1;

	@Override
	public Position getObjectPosition(String objectId) {
		try {
			GenericCommand cmd = new GenericCommand(H_POSITION_REQUEST, EnvironmentProviderService.SERVICE_NAME, INTERACTION_NAME+currentRequestNr++);
			cmd.addParam(objectId);
			getNode().accept(cmd);
		} catch (IMTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void test(String testMsg) {
		try {
			GenericCommand cmd = new GenericCommand(H_TEST_COMMAND, EnvironmentProviderService.SERVICE_NAME, null);
			cmd.addParam(testMsg);
			getNode().accept(cmd);
		} catch (IMTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
