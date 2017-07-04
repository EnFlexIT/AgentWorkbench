package gasmas.transfer.zib.factory;

import gasmas.ontology.Entry;
import gasmas.ontology.HeatCapacityCoefficient;
import gasmas.transfer.zib.net.GasNodeType;
import gasmas.transfer.zib.net.SourceType;

public class EntryFactory extends ExitFactory {

	public static Entry newInstance(GasNodeType gasNodeType) {
		
		Entry entry = new Entry();
		
		setInnodeAttributes(entry, gasNodeType);
		
		
		SourceType sourceType = (SourceType) gasNodeType;
		
		entry.setCalorificValue(ValueTypeFactory.newInstance(sourceType.getCalorificValue()));
		entry.setGasTemperature(ValueTypeFactory.newInstance(sourceType.getGasTemperature()));
		entry.setHeatCapacityCoefficient(newHeatCapacityCoefficientInstance(sourceType));
		
		entry.setFlowMin(ValueTypeFactory.newInstance(sourceType.getFlowMin()));
		entry.setFlowMax(ValueTypeFactory.newInstance(sourceType.getFlowMax()));
		entry.setMolarMass((float)sourceType.getMolarMass().getValue());
		entry.setNormDensity(ValueTypeFactory.newInstance(sourceType.getNormDensity()));
		entry.setPseudocricalPressure(ValueTypeFactory.newInstance(sourceType.getPseudocriticalPressure()));
		entry.setPseudocriticalTemperature(ValueTypeFactory.newInstance(sourceType.getPseudocriticalTemperature()));
		
		return entry;
	}

	private static HeatCapacityCoefficient newHeatCapacityCoefficientInstance(
			SourceType sourceType) {
		
		HeatCapacityCoefficient heatCapacityCoefficient = new HeatCapacityCoefficient();
		heatCapacityCoefficient.setA((float) sourceType.getCoefficientAHeatCapacity().getValue());
		heatCapacityCoefficient.setB((float) sourceType.getCoefficientBHeatCapacity().getValue());
		heatCapacityCoefficient.setC((float) sourceType.getCoefficientCHeatCapacity().getValue());
		return heatCapacityCoefficient;
	}
	
}
