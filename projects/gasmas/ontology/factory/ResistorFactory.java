package gasmas.ontology.factory;

import gasmas.ontology.Resistor;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.ResistorType;

public 	class ResistorFactory {
	public static Resistor newInstance(GasConnectionType gasConnectionType) {
		
		Resistor resistor = new Resistor();
		ConnectionFactory.setConnectionAttributes(resistor, gasConnectionType);
		ResistorType resistorType = (ResistorType)gasConnectionType;
		
		resistor.setDiameter(ValueTypeFactory.newInstance(resistorType.getDiameter()));
		resistor.setPressureLoss(ValueTypeFactory.newInstance(resistorType.getPressureLoss()));
		resistor.setDragFactor(getDragFactor(resistorType));

		
		
		return resistor;
	}
	private static float getDragFactor(ResistorType resistorType) {
			if(resistorType.getDragFactor()== null) {
				return 0;
			} else {			
				return (float) resistorType.getDragFactor().getValue();
			}
		}
}
