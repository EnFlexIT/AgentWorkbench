package gasmas.transfer.zib.factory;

import gasmas.ontology.ControlValve;
import gasmas.transfer.zib.net.ControlValveType;
import gasmas.transfer.zib.net.GasConnectionType;


public class ControlValveFactory {

	public static ControlValve newInstance(GasConnectionType gasConnectionType) {
		
		ControlValve controlValve = new ControlValve();
		ConnectionFactory.setConnectionAttributes(controlValve, gasConnectionType);
		ControlValveType controlValveType = (ControlValveType) gasConnectionType;

		controlValve.setDiameterIn(ValueTypeFactory.newInstance(controlValveType.getDiameterIn()));
		controlValve.setDiameterOut(ValueTypeFactory.newInstance(controlValveType.getDiameterOut()));
		controlValve.setDragFactorIn(getDragFactorIn(controlValveType));
		controlValve.setDragFactorOut(getDragFactorOut( controlValveType));
		controlValve.setIncreasedOutputTemperature(ValueTypeFactory.newInstance(controlValveType.getIncreasedOutputTemperature()));	
		controlValve.setPressureDifferentialMax(ValueTypeFactory.newInstance(controlValveType.getPressureDifferentialMax()));
		controlValve.setPressureDifferentialMin(ValueTypeFactory.newInstance(controlValveType.getPressureDifferentialMin()));
		controlValve.setPressureInMin(ValueTypeFactory.newInstance(controlValveType.getPressureInMin()));
		controlValve.setPressureOutMax(ValueTypeFactory.newInstance(controlValveType.getPressureOutMax()));
		controlValve.setPressureSet(ValueTypeFactory.newInstance(controlValveType.getPressureSet()));

		return controlValve;
	}

	private static float getDragFactorOut(ControlValveType controlValveType) {
		if(controlValveType.getDragFactorOut()== null) {
			return 0;
		} else {			
			return (float) controlValveType.getDragFactorOut().getValue();
		}
	}
	
	private static float getDragFactorIn(ControlValveType controlValveType) {
		if (controlValveType.getDragFactorIn() == null) {
			return 0;
		} else {
			return (float) controlValveType.getDragFactorIn().getValue();
		}
	}
}
