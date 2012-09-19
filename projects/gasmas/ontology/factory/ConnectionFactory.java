package gasmas.ontology.factory;


import gasmas.ontology.Connection;
import gasmas.transfer.zib.net.GasConnectionType;

public class ConnectionFactory {
	
	public static void setConnectionAttributes(Connection connection, GasConnectionType gasConnectionType) {
		
		connection.setAlias(gasConnectionType.getAlias());
		connection.setID(gasConnectionType.getId());
		connection.setFrom(gasConnectionType.getFrom());
		connection.setTo(gasConnectionType.getTo());
		connection.setFlowMin(ValueTypeFactory.newInstance(gasConnectionType.getFlowMin()));
		connection.setFlowMax(ValueTypeFactory.newInstance(gasConnectionType.getFlowMax()));
		
	}
}
