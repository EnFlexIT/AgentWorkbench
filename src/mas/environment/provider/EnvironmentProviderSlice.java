package mas.environment.provider;

import mas.environment.ontology.Position;
import jade.core.Service.Slice;

public interface EnvironmentProviderSlice extends Slice {
	public static final String H_POSITION_REQUEST = "posRequest";
	public static final String H_POSITION_RESPONSE = "posResponse";
	public static final String H_TEST_COMMAND = "test";
	
	public Position getObjectPosition(String objectId);
	public void test(String testMsg);
}
