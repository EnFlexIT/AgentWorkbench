package gasmas.transfer.zib.factory;


import gasmas.ontology.Connection;
import gasmas.transfer.zib.net.GasConnectionType;

public class ConnectionFactory {
	
	public static void setConnectionAttributes(Connection connection, GasConnectionType gasConnectionType) {
		
		
		connection.setID(gasConnectionType.getId());
		if (gasConnectionType.getAlias()==null || gasConnectionType.getAlias().equals("")) {
			connection.setAlias(gasConnectionType.getId());
		} else {
			connection.setAlias(gasConnectionType.getAlias());	
		}
		connection.setFrom(gasConnectionType.getFrom());
		connection.setTo(gasConnectionType.getTo());
		connection.setFlowMin(ValueTypeFactory.newInstance(gasConnectionType.getFlowMin()));
		connection.setFlowMax(ValueTypeFactory.newInstance(gasConnectionType.getFlowMax()));
		
	}
}
