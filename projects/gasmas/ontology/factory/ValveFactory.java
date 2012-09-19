package gasmas.ontology.factory;


import gasmas.ontology.Valve;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.ValveType;

public class ValveFactory {

	public static Valve newInstance(GasConnectionType gasConnectionType) {
		
		Valve valve = new Valve();
		ConnectionFactory.setConnectionAttributes(valve, gasConnectionType);
		
		ValveType valveType = (ValveType)gasConnectionType;

		valve.setPressureDifferentialMax(ValueTypeFactory.newInstance(valveType.getPressureDifferentialMax()));

		return valve;
	}
}
